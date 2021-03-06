package com.example.federico.aldiaapp.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.federico.aldiaapp.R;
import com.example.federico.aldiaapp.model.Payment;
import com.example.federico.aldiaapp.model.QrToken;
import com.example.federico.aldiaapp.model.Status;
import com.example.federico.aldiaapp.network.AppController;
import com.example.federico.aldiaapp.utils.Constants;
import com.example.federico.aldiaapp.utils.Utils;
import com.example.federico.aldiaapp.viewmodel.MainActivityViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Main Activity";
    private static final int PERMISSION_REQUESTS = 1;
    private static final int REQUEST_CODE = 2;
    @BindView(R.id.fabEscanearQR)
    FloatingActionButton fabEscanearQR;
    @BindView(R.id.content_view_main)
    View content_view;
    @BindView(R.id.amountToDateTv)
    TextView amountToDateTv;
    @BindView(R.id.tvRegularHours)
    TextView tvRegularHours;
    @BindView(R.id.tvExtraHours)
    TextView tvExtraHours;
    @BindView(R.id.lastPaymentTv)
    TextView lastPaymentTv;
    @BindView(R.id.positionTv)
    TextView positionTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    SharedPreferences prefs;
    MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent comesFromIntent = getIntent();
        if (comesFromIntent.hasExtra(Constants.KEY_INTENT_WIDGET_BUTTON)) {
            //Means that the user clicked the camera button in widget,
            //so it should navigate directly to the camera.
            goToCamera();
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String businessName = prefs.getString(Constants.KEY_BUSINESS_NAME, "");
        Objects.requireNonNull(getSupportActionBar()).setTitle(businessName);
        createNavDrawer(toolbar);
        long businessId = prefs.getLong(Constants.KEY_BUSINESS_ID, 0);
        MainActivityViewModel.Factory factory = new MainActivityViewModel.Factory(AppController.get(this), businessId);
        mainActivityViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        mainActivityViewModel.getMediatorLiveData().observe(this, paymentResource -> {
            assert paymentResource != null;
            switch (paymentResource.status) {
                case RUNNING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.INVISIBLE);
                    updateUI(paymentResource.data);
                    //If no errors when getting information,
                    //check if there are any Qr codes saved in the DB
                    //and post them to the server.
                    tryToPostPendingQrCodes();
                    break;
                case FAILED:
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, paymentResource.msg, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    /**
     * Retrieves tokens that have not been posted to the server from the internal DB
     */
    public void tryToPostPendingQrCodes() {
        mainActivityViewModel.getPendingQrCodes().observe(this, qrTokens -> {
            assert qrTokens != null;
            if (qrTokens.size() > 0) {
                postSingleToken(qrTokens.get(0));
            } else {
                Log.d(TAG, "No tokens pending to be posted to server");
            }
        });
    }

    /**
     * If posting is successful, deletes token from DB. This will trigger the .getPendingQrCodes observer
     * with the next token.
     * If not successful, pending QrTokens will remain in the DB, waiting to be posted
     */
    public void postSingleToken(QrToken qrToken) {
        mainActivityViewModel.postQrToken(qrToken).observe(this, periodoResource -> {
            assert periodoResource != null;
            if (periodoResource.status == Status.SUCCESS) {
                mainActivityViewModel.deleteQrToken(qrToken);
            }
        });
    }

    private void updateUI(Payment lastPayment) {
        if (lastPayment != null) {
            try {
                positionTv.setText(lastPayment.getPosition().getNombre());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                lastPaymentTv.setText(Utils.getDateAndHour(lastPayment.getFecha(), MainActivity.this));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                amountToDateTv.setText(Utils.getFormattedAmount(lastPayment.getMontoTotal()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                tvRegularHours.setText(String.format(Locale.getDefault(), lastPayment.getHorasTotReg().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                tvExtraHours.setText(String.format(Locale.getDefault(), lastPayment.getHorasTotExt().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * On Click for View showing pending hours to be paid
     */
    @OnClick(R.id.viewHoursData)
    public void goToShifts() {
        Intent intentToShifts = new Intent(MainActivity.this, ShiftsActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intentToShifts, bundle);
    }

    /**
     * On Click for Camera button.
     */
    @OnClick(R.id.fabEscanearQR)
    public void goToCamera() {
        if (!allPermissionsGranted()) {
            getRuntimePermissions();
        }
        Intent intentToCamera = new Intent(MainActivity.this, CameraActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        ActivityCompat.startActivityForResult(MainActivity.this, intentToCamera, REQUEST_CODE, bundle);
    }

    /**
     * Refreshes the view if the Qr scan succeeded.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                long businessId = prefs.getLong(Constants.KEY_BUSINESS_ID, 0);
                mainActivityViewModel.refresh(businessId);

            } else {
                if (resultCode == RESULT_CANCELED) {
                    Log.i(TAG, "Result CANCELED");
                }
            }
        }
    }

    /*-------------------------------------- Permissions --------------------------------------------***/

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!Utils.isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!Utils.isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    /*-------------------------------------- Nav Drawer --------------------------------------------***/

    private void createNavDrawer(Toolbar toolbar) {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView tvNavUserName = header.findViewById(R.id.tvNavUserName);
        TextView tvNavUserMail = header.findViewById(R.id.tvNavUserMail);
        ImageView imageViewNavDrawer = header.findViewById(R.id.imageViewNavDrawer);

        try {
            tvNavUserName.setText(prefs.getString(Constants.KEY_USER_NAME, ""));
            tvNavUserMail.setText(prefs.getString(Constants.KEY_EMAIL_USER, ""));
            String imagenUsuario = prefs.getString(Constants.KEY_PHOTO_USER, "");
            if (!imagenUsuario.equals("")) {
                Picasso.get().load(imagenUsuario).into(imageViewNavDrawer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        switch (id) {
            case R.id.nav_mi_perfil:
                Intent pasarAMiPerfil = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(pasarAMiPerfil, bundle);

                break;
            case R.id.nav_periodos:
                Intent pasarAPeriodos = new Intent(MainActivity.this, ShiftsActivity.class);
                startActivity(pasarAPeriodos, bundle);

                break;
            case R.id.nav_liquidaciones:
                Intent pasarALiquidaciones = new Intent(MainActivity.this, PaymentsActivity.class);
                startActivity(pasarALiquidaciones, bundle);

                break;
            case R.id.nav_cerrar_sesion:
                Intent pasarASignIn = new Intent(MainActivity.this, SignInActivity.class);
                pasarASignIn.putExtra(Constants.KEY_INTENT_SIGN_OUT, "");
                startActivity(pasarASignIn, bundle);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

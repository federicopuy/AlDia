package com.example.federico.aldia.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.model.QrToken;
import com.example.federico.aldia.model.Resource;
import com.example.federico.aldia.model.Status;
import com.example.federico.aldia.network.AppController;
import com.example.federico.aldia.network.NetworkState;
import com.example.federico.aldia.utils.Constants;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.utils.Utils;
import com.example.federico.aldia.viewmodel.MainActivityViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Main Activity";
    private static final int PERMISSION_REQUESTS = 1;
    private static final int REQUEST_CODE = 2;
    @BindView(R.id.fabEscanearQR) FloatingActionButton fabEscanearQR;
    @BindView(R.id.content_view_main) View content_view;
    @BindView(R.id.tvRecaudado) TextView tvRecaudado;
    @BindView(R.id.tvHorasRegulares) TextView tvHorasRegulares;
    @BindView(R.id.tvHorasExtra) TextView tvHorasExtra;
    @BindView(R.id.tvFechaUltimaLiquidacion) TextView tvFechaUltimaLiquidacion;
    @BindView(R.id.tvCategoria) TextView tvCategoria;
    @BindView(R.id.recaudaciontv) TextView recaudaciontv;
    @BindView(R.id.horasRegularestv) TextView horasRegularestv;
    @BindView(R.id.horasExtratv) TextView horasExtratv;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progressBar) ProgressBar progressBar;
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
            /*
            Means that the user clicked the camera button in widget,
             so it should navigate directly to the camera.
             */
            goToCamera();
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String businessName = prefs.getString(Constants.KEY_BUSINESS_NAME, "");
        Objects.requireNonNull(getSupportActionBar()).setTitle(businessName);

        createNavDrawer(toolbar);

        long businessId = prefs.getLong(Constants.KEY_BUSINESS_ID, 0);

        //ViewModel creation
        MainActivityViewModel.Factory factory = new MainActivityViewModel.Factory(AppController.get(this), businessId);
        mainActivityViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        //todo chequear que no haya pending qr codes. SI llega a escanear uno nuevo no me va a dejar mandar el viejo.
        mainActivityViewModel.getLastPayment().observe(this, this::updateUI);

        mainActivityViewModel.getNetworkState().observe(this, networkState -> {
            if ((networkState != null ? networkState.getStatus() : null) == NetworkState.Status.RUNNING) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                if (networkState.getStatus() == NetworkState.Status.FAILED) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.e(TAG, networkState.getMsg());
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    //If no errors when getting information,
                    // check if there are any Qr codes saved in the DB
                    // and post them to the server.
                    tryToPostPendingQrCodes();
                }
            }
        });
    }
    /**
     Retrieves pending tokens from the DB
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

    public void postSingleToken(QrToken qrToken){
        mainActivityViewModel.postQrToken(qrToken).observe(this, new Observer<Resource<Periodo>>() {
            @Override
            public void onChanged(@Nullable Resource<Periodo> periodoResource) {

                if (periodoResource.status == Status.SUCCESS) {
                    //if successful, delete token from DB. This will trigger the .getPendingQrCodes observer
                    // once again.
                    mainActivityViewModel.deleteQrToken(qrToken);
                }
                //todo else
            }
        });
    }

    private void updateUI(Liquidacion lastPayment) {
        if (lastPayment != null) {
            try {
                tvCategoria.setText(lastPayment.getCategoria().getNombre());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                tvFechaUltimaLiquidacion.setText(Utils.obtenerFechaFormateada(lastPayment.getFecha()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (lastPayment.getCategoria().getTipoCategoria().equals("FIJO")) {
                //todo sacar empleado fijo
                Log.d(TAG, "Employee fijo");
                recaudaciontv.setText(R.string.sueldo_mensual);
                try {
                    tvRecaudado.setText(Utils.obtenerMontoFormateado(lastPayment.getCategoria().getMonto()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                horasRegularestv.setText(R.string.dias_trabajo);
                try {
                    tvHorasRegulares.setText(lastPayment.getCategoria().getDiasTrabajo().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                horasExtratv.setText(R.string.hours_per_shift);
                try {
                    tvHorasExtra.setText(lastPayment.getCategoria().getHorasTrabajo().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "Employee por Horas");
                try {
                    tvRecaudado.setText(Utils.obtenerMontoFormateado(lastPayment.getMontoTotal()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    tvHorasRegulares.setText(lastPayment.getHorasTotReg().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    tvHorasExtra.setText(lastPayment.getHorasTotExt().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClick(R.id.viewHoursData)
    public void goToShifts() {
        Intent intentToShifts = new Intent(MainActivity.this, ShiftsActivity.class);
        startActivity(intentToShifts);
    }

    /**
     On Click for Camera button.
     */
    @OnClick(R.id.fabEscanearQR)
    public void goToCamera() {
        if (!allPermissionsGranted()) {
            getRuntimePermissions();
        }
        Intent intentToCamera = new Intent(MainActivity.this, CameraActivity.class);
        startActivityForResult(intentToCamera, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //   obtenerUltimaLiquidacion();
                //todo check this
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
        if (id == R.id.nav_mi_perfil) {
            Intent pasarAMiPerfil = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(pasarAMiPerfil);

        } else if (id == R.id.nav_periodos) {
            Intent pasarAPeriodos = new Intent(MainActivity.this, ShiftsActivity.class);
            startActivity(pasarAPeriodos);

        } else if (id == R.id.nav_liquidaciones) {
            Intent pasarALiquidaciones = new Intent(MainActivity.this, PaymentsActivity.class);
            startActivity(pasarALiquidaciones);

        } else if (id == R.id.nav_cerrar_sesion) {
            Intent pasarASignIn = new Intent(MainActivity.this, SignInActivity.class);
            pasarASignIn.putExtra(Constants.KEY_INTENT_SIGN_OUT, "");
            startActivity(pasarASignIn);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

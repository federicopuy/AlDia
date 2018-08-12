package com.example.federico.aldia.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.federico.aldia.R;
import com.example.federico.aldia.network.NoConnectivityException;
import com.example.federico.aldia.notifications.NotificationUtils;
import com.example.federico.aldia.utils.Constants;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.network.APIInterface;
import com.example.federico.aldia.network.RetrofitClient;
import com.example.federico.aldia.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Main Activity";
    private static final int PERMISSION_REQUESTS = 1;
    private static final int REQUEST_CODE = 2;

    @BindView(R.id.fabEscanearQR)
    FloatingActionButton fabEscanearQR;
    @BindView(R.id.content_view_main)
    View content_view;
    @BindView(R.id.tvRecaudado)
    TextView tvRecaudado;
    @BindView(R.id.tvHorasRegulares)
    TextView tvHorasRegulares;
    @BindView(R.id.tvHorasExtra)
    TextView tvHorasExtra;
    @BindView(R.id.tvFechaUltimaLiquidacion)
    TextView tvFechaUltimaLiquidacion;
    @BindView(R.id.tvCategoria)
    TextView tvCategoria;
    @BindView(R.id.recaudaciontv)
    TextView recaudaciontv;
    @BindView(R.id.horasRegularestv)
    TextView horasRegularestv;
    @BindView(R.id.horasExtratv)
    TextView horasExtratv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    SharedPreferences prefs;

    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent comesFromIntent = getIntent();
        if (comesFromIntent.hasExtra(Constants.KEY_INTENT_WIDGET_BUTTON)) {
            Intent pasarACamara = new Intent(MainActivity.this, CameraActivity.class);

            if (!allPermissionsGranted()) {
                getRuntimePermissions();
            }
            startActivityForResult(pasarACamara, REQUEST_CODE);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String nombreComercio = prefs.getString(Constants.KEY_COMERCIO_NOMBRE, "");
        Objects.requireNonNull(getSupportActionBar()).setTitle(nombreComercio);

        createNavDrawer(toolbar);
        mCompositeDisposable = new CompositeDisposable();
        obtenerUltimaLiquidacion();

    }

    /*-------------------------------------- Llamada Obtener Ultima Liquidacion --------------------------------------------***/

    private void obtenerUltimaLiquidacion() {
        //todo composite disposable
        final String nombreLlamada = "getUltimaLiquidacion";
        long comercioId = prefs.getLong(Constants.KEY_COMERCIO_ID, 0);
        APIInterface mService = RetrofitClient.getClient(getApplicationContext()).create(APIInterface.class);

        Observable<Liquidacion> observable = mService.getUltimaLiquidacion(comercioId);
        observable.subscribeOn(Schedulers.io())
                .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                        return objectObservable.delay(30, TimeUnit.SECONDS);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Liquidacion>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Liquidacion ultimaLiquidacion) {
                        Log.i(TAG, getString(R.string.is_successful) + nombreLlamada);
                        if (ultimaLiquidacion!=null){
                            actualizarUI(ultimaLiquidacion);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, getString(R.string.on_failure) + nombreLlamada);
                        if (e instanceof NoConnectivityException) {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(), "Error de Conexión", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), "Error de Servidor", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*-------------------------------------- Actualizar UI --------------------------------------------***/

    private void actualizarUI(Liquidacion ultimaLiquidacion) {

        if (ultimaLiquidacion != null) {

            try {
                tvCategoria.setText(ultimaLiquidacion.getCategoria().getNombre());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                tvFechaUltimaLiquidacion.setText(Utils.obtenerFechaFormateada(ultimaLiquidacion.getFecha()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (ultimaLiquidacion.getCategoria().getTipoCategoria().equals("FIJO")) {
                Log.d(TAG, "Employee fijo");
                recaudaciontv.setText(R.string.sueldo_mensual);

                try {
                    tvRecaudado.setText(Utils.obtenerMontoFormateado(ultimaLiquidacion.getCategoria().getMonto()));
                } catch (NullPointerException n) {
                    n.printStackTrace();
                    tvRecaudado.setText(R.string.null_money_value);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                horasRegularestv.setText(R.string.dias_trabajo);
                try {
                    tvHorasRegulares.setText(ultimaLiquidacion.getCategoria().getDiasTrabajo().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                horasExtratv.setText(R.string.hours_per_shift);
                try {
                    tvHorasExtra.setText(ultimaLiquidacion.getCategoria().getHorasTrabajo().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.d(TAG, "Employee por Horas");

                try {
                    tvRecaudado.setText(Utils.obtenerMontoFormateado(ultimaLiquidacion.getMontoTotal()));
                } catch (NullPointerException n) {
                    n.printStackTrace();
                    tvRecaudado.setText(R.string.null_money_value);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    tvHorasRegulares.setText(ultimaLiquidacion.getHorasTotReg().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    tvHorasExtra.setText(ultimaLiquidacion.getHorasTotExt().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClick(R.id.viewHoursData)
    public void pasarAPeriodos() {
//        Intent pasarAPeriodos = new Intent(MainActivity.this, ShiftsActivity.class);
//        startActivity(pasarAPeriodos);

        NotificationUtils.remindUserBecausePeriodAboutToFinish(this);

    }
    /*-------------------------------------- On Click Escanear QR --------------------------------------------***/

    @OnClick(R.id.fabEscanearQR)
    public void pasarACamara() {

        if (!allPermissionsGranted()) {
            getRuntimePermissions();
        }
        Intent pasarACamara = new Intent(MainActivity.this, CameraActivity.class);
        startActivityForResult(pasarACamara, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                obtenerUltimaLiquidacion();
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
            tvNavUserName.setText(prefs.getString(Constants.KEY_NOMBRE_USER, ""));
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
            pasarASignIn.putExtra(Constants.KEY_INTENT_CERRAR_SESION, "");
            startActivity(pasarASignIn);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.dispose();
        super.onDestroy();
    }
}

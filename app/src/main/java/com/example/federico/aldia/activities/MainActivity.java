package com.example.federico.aldia.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Constantes;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.network.APIInterface;
import com.example.federico.aldia.network.RetrofitClient;
import com.example.federico.aldia.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


    @BindView(R.id.fabEscanearQR)
    FloatingActionButton fabEscanearQR;

    TextView tvNombreComercio, tvRecaudado, tvHorasRegulares, tvHorasExtra, tvFechaUltimaLiquidacion, tvCategoria;

    SharedPreferences prefs;

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        createNavDrawer(toolbar);

        View content_view = findViewById(R.id.content_view_main);

        tvNombreComercio = content_view.findViewById(R.id.tvNombreComercio);
        tvRecaudado = content_view.findViewById(R.id.tvRecaudado);
        tvHorasRegulares = content_view.findViewById(R.id.tvHorasRegulares);
        tvHorasExtra = content_view.findViewById(R.id.tvHorasExtra);
        tvFechaUltimaLiquidacion = content_view.findViewById(R.id.tvFechaUltimaLiquidacion);
        tvCategoria = content_view.findViewById(R.id.tvCategoria);

        obtenerUltimaLiquidacion();

    }

    /*-------------------------------------- Llamada Obtener Ultima Liquidacion --------------------------------------------***/

    private void obtenerUltimaLiquidacion() {

        final String nombreLlamada = "getUltimaLiquidacion";

        int comercioId = prefs.getInt(Constantes.KEY_COMERCIO_ID, 0);

        APIInterface mService = RetrofitClient.getClient(getApplicationContext()).create(APIInterface.class);

        Call<Liquidacion> callGetUltimaLiquidacion = mService.getUltimaLiquidacion(comercioId);

        callGetUltimaLiquidacion.enqueue(new Callback<Liquidacion>() {
            @Override
            public void onResponse(Call<Liquidacion> call, Response<Liquidacion> response) {

                Log.i(TAG, getString(R.string.on_response) + nombreLlamada);

                if (response.isSuccessful()) {

                    Log.i(TAG, getString(R.string.is_successful) + nombreLlamada);

                    Liquidacion ultimaLiquidacion = response.body();

                    actualizarUI(ultimaLiquidacion);

                } else {

                    Log.i(TAG, getString(R.string.is_not_successful) + nombreLlamada);

                    try {
                        Log.e(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<Liquidacion> call, Throwable t) {

                Log.i(TAG, getString(R.string.on_failure) + nombreLlamada);

            }
        });

    }

    /*-------------------------------------- Actualizar UI --------------------------------------------***/

    private void actualizarUI(Liquidacion ultimaLiquidacion) {

        if (ultimaLiquidacion != null) {

            try {
                //todo nombre comercio
                tvNombreComercio.setText("Chajuco Bar");
                tvCategoria.setText(ultimaLiquidacion.getCategoria().getNombre());
                tvRecaudado.setText(ultimaLiquidacion.getMontoTotal().toString());
                tvHorasRegulares.setText(ultimaLiquidacion.getHorasTotReg().toString());
                tvHorasExtra.setText(ultimaLiquidacion.getHorasTotExt().toString());
                tvFechaUltimaLiquidacion.setText(Utils.obtenerFechaFormateada(ultimaLiquidacion.getFecha()));


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    /*-------------------------------------- On Click Escanear QR --------------------------------------------***/

    @OnClick(R.id.fabEscanearQR)
    public void pasarACamara() {

        if (!allPermissionsGranted()) {
            getRuntimePermissions();
        }

        Intent pasarACamara = new Intent(MainActivity.this, CamaraActivity.class);

        startActivityForResult(pasarACamara, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Log.i(TAG, "Result OK");

            } else {
                if (resultCode==RESULT_CANCELED){

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
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        TextView tvNavUserName = header.findViewById(R.id.tvNavUserName);

        TextView tvNavUserMail = header.findViewById(R.id.tvNavUserMail);

        ImageView imageViewNavDrawer = header.findViewById(R.id.imageViewNavDrawer);

        Intent intent = getIntent();

        String nombreUsuario = "";
        String emailUsuario = "";
        String imagenUsuario = "";

        if ((intent.hasExtra(Constantes.KEY_INTENT_NOMBRE_USUARIO)) && (intent.hasExtra(Constantes.KEY_INTENT_EMAIL_USUARIO))) {

            try {

                nombreUsuario = intent.getStringExtra(Constantes.KEY_INTENT_NOMBRE_USUARIO);

                emailUsuario = intent.getStringExtra(Constantes.KEY_INTENT_EMAIL_USUARIO);

                imagenUsuario = intent.getStringExtra(Constantes.KEY_INTENT_IMAGEN_USUARIO);

                if (!imagenUsuario.equals("")) {
                    Picasso.get().load(imagenUsuario).into(imageViewNavDrawer);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        tvNavUserName.setText(nombreUsuario);
        tvNavUserMail.setText(emailUsuario);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mi_perfil) {

            Intent pasarAMiPerfil = new Intent(MainActivity.this, EmpleadoActivity.class);

            startActivity(pasarAMiPerfil);


        } else if (id == R.id.nav_periodos) {

            Intent pasarAPeriodos = new Intent(MainActivity.this, PeriodosActivity.class);

            startActivity(pasarAPeriodos);

        } else if (id == R.id.nav_liquidaciones) {

            Intent pasarALiquidaciones = new Intent(MainActivity.this, LiquidacionesActivity.class);

            startActivity(pasarALiquidaciones);

        } else if (id == R.id.nav_cerrar_sesion) {

            Intent pasarASignIn = new Intent(MainActivity.this, SignIn.class);
            startActivity(pasarASignIn);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

package com.example.federico.aldia.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Empleado;
import com.example.federico.aldia.network.APIInterface;
import com.example.federico.aldia.network.RetrofitClient;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmpleadoActivity extends AppCompatActivity {

    private static final String TAG = "Empleado Activity";

    @BindView(R.id.tvNombreValue)
    TextView tvNombreValue ;

    @BindView(R.id.tvDireccionValue)
    TextView tvDireccionValue;

    @BindView(R.id.tvEmailValue)
    TextView tvEmailValue;

    @BindView(R.id.tvDNIValue)
    TextView tvDNIValue;

    @BindView(R.id.tvTelefonoValue)
    TextView tvTelefonoValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        obtenerDatosEmpleado();

    }

    private void obtenerDatosEmpleado() {

        final String nombreLlamada = "callGetDatosEmpleado";

        APIInterface mService = RetrofitClient.getClient(getApplicationContext()).create(APIInterface.class);

        Call<Empleado> callGetDatosEmpleado = mService.getDatosEmpleado();

        callGetDatosEmpleado.enqueue(new Callback<Empleado>() {
            @Override
            public void onResponse(Call<Empleado> call, Response<Empleado> response) {

                Log.i(TAG, getString(R.string.on_response) + nombreLlamada);

                if (response.isSuccessful()) {

                    Log.i(TAG, getString(R.string.is_successful) + nombreLlamada);

                    Empleado empleado = response.body();

                    actualizarUI(empleado);

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
            public void onFailure(Call<Empleado> call, Throwable t) {

                Log.i(TAG, getString(R.string.on_failure) + nombreLlamada);

            }
        });

    }

    private void actualizarUI(Empleado empleado) {

        try{

            tvNombreValue.setText(empleado.getNombre());
            tvEmailValue.setText(empleado.getEmail());
            tvDNIValue.setText(empleado.getDni());
            tvTelefonoValue.setText(empleado.getTelefono());
            tvDireccionValue.setText(empleado.getDireccion());

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

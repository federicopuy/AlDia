package com.example.federico.aldia.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Employee;
import com.example.federico.aldia.network.RetrofitClient;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "Employee Activity";

    @BindView(R.id.tvNombreValue)
    TextView tvNombreValue;
    @BindView(R.id.tvDireccionValue)
    TextView tvDireccionValue;
    @BindView(R.id.tvEmailValue)
    TextView tvEmailValue;
    @BindView(R.id.tvDNIValue)
    TextView tvDNIValue;
    @BindView(R.id.tvTelefonoValue)
    TextView tvTelefonoValue;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        getEmployeeInfo();

    }

    private void getEmployeeInfo() {
        progressBar.setVisibility(View.VISIBLE);
        final String nombreLlamada = "callGetEmployeeInfo";

        RetrofitClient.getClient().getEmployeeData().enqueue(new Callback<Employee>() {

            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.i(TAG, getString(R.string.on_response) + nombreLlamada);
                if (response.isSuccessful()) {
                    Log.i(TAG, getString(R.string.is_successful) + nombreLlamada);
                    Employee employee = response.body();
                    updateUi(employee);

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
            public void onFailure(Call<Employee> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.i(TAG, getString(R.string.on_failure) + nombreLlamada);
            }
        });
    }

    private void updateUi(Employee employee) {

        try{
            tvNombreValue.setText(employee.getNombre());
            tvEmailValue.setText(employee.getEmail());
            tvDNIValue.setText(employee.getDni());
            tvTelefonoValue.setText(employee.getTelefono());
            tvDireccionValue.setText(employee.getDireccion());

        }catch (NullPointerException n) {
            n.printStackTrace();
        }
    }
}

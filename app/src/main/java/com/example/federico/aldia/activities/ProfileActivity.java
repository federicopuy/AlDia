package com.example.federico.aldia.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Employee;
import com.example.federico.aldia.network.NoConnectivityException;
import com.example.federico.aldia.network.RetrofitClient;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "Employee Activity";
    @BindView(R.id.tvNameValue)
    TextView tvNameValue;
    @BindView(R.id.tvAddressValue)
    TextView tvAddressValue;
    @BindView(R.id.tvEmailValue)
    TextView tvEmailValue;
    @BindView(R.id.tvDNIValue)
    TextView tvDNIValue;
    @BindView(R.id.tvPhoneValue)
    TextView tvPhoneValue;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        getEmployeeInfo();
    }

    private void getEmployeeInfo() {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getClient().getEmployeeData().enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    updateUi(response.body());
                } else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.i(TAG, getString(R.string.on_failure) + t.getMessage());
                if (t instanceof NoConnectivityException) {
                    t.printStackTrace();
                    Toast.makeText(getBaseContext(), R.string.error_conexion, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), R.string.error_servidor, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateUi(Employee employee) {
        try {
            tvNameValue.setText(employee.getNombre());
            tvEmailValue.setText(employee.getEmail());
            tvDNIValue.setText(employee.getDni());
            tvPhoneValue.setText(employee.getTelefono());
            tvAddressValue.setText(employee.getDireccion());
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }
}

package com.example.federico.aldia.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Employee;
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
        final String callName = "callGetEmployeeInfo";
        RetrofitClient.getClient().getEmployeeData().enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.i(TAG, getString(R.string.on_response) + callName);

                if (response.isSuccessful()) {
                    Log.i(TAG, getString(R.string.is_successful) + callName);
                    updateUi(response.body());
                } else {
                    Log.i(TAG, getString(R.string.is_not_successful) + callName);
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.i(TAG, getString(R.string.on_failure) + callName);
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

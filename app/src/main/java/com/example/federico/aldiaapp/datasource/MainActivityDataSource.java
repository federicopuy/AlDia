package com.example.federico.aldiaapp.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.federico.aldiaapp.R;
import com.example.federico.aldiaapp.model.Payment;
import com.example.federico.aldiaapp.model.QrToken;
import com.example.federico.aldiaapp.model.Resource;
import com.example.federico.aldiaapp.model.Shift;
import com.example.federico.aldiaapp.model.Status;
import com.example.federico.aldiaapp.network.AppController;
import com.example.federico.aldiaapp.network.NoConnectivityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityDataSource {
    private static final String TAG = "Main Activity DataS";

    private AppController appController;

    public MainActivityDataSource(AppController appController) {
        this.appController = appController;
    }

    public LiveData<Resource<Payment>> getLastPayment(long businessId) {
        final MutableLiveData<Resource<Payment>> data = new MutableLiveData<>();
        data.postValue(new Resource<>(Status.RUNNING));

        appController.getApiInterface().getLastPayment(businessId)
                .enqueue(new Callback<Payment>() {
                    @Override
                    public void onResponse(Call<Payment> call, Response<Payment> response) {
                        if (response.isSuccessful()) {
                            data.postValue(new Resource<>(Status.SUCCESS, response.body()));
                        } else {
                            data.postValue(new Resource<>(Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Payment> call, Throwable t) {
                        String errorMessage = t == null ? appController.getApplicationContext().getString(R.string.error_servidor) : t.getMessage();
                        Log.e(TAG, errorMessage);
                        if (t instanceof NoConnectivityException) {
                            data.postValue(new Resource<>(Status.FAILED, appController.getApplicationContext().getString(R.string.error_conexion)));
                            t.printStackTrace();
                        } else {
                            data.postValue(new Resource<>(Status.FAILED, appController.getApplicationContext().getString(R.string.error_servidor)));
                        }

                    }
                });
        return data;
    }

    public LiveData<Resource<Shift>> postSingleQr(QrToken qrToken) {
        final MutableLiveData<Resource<Shift>> data = new MutableLiveData<>();

        appController.getApiInterface().newShiftOffline(qrToken)
                .enqueue(new Callback<Shift>() {
                    @Override
                    public void onResponse(Call<Shift> call, Response<Shift> response) {
                        if (response.isSuccessful()) {
                            data.setValue(new Resource<>(Status.SUCCESS, response.body()));
                        } else {
                            data.postValue(new Resource<>(Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Shift> call, Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        data.postValue(new Resource<>(Status.FAILED, errorMessage));
                    }
                });
        return data;
    }
}

package com.example.federico.aldia.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.model.QrToken;
import com.example.federico.aldia.model.Resource;
import com.example.federico.aldia.model.Status;
import com.example.federico.aldia.network.AppController;
import com.example.federico.aldia.network.NoConnectivityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityDataSource {
    private static final String TAG = "Main Activity DataS";

    private AppController appController;

    public MainActivityDataSource(AppController appController) {
        this.appController = appController;
    }

    public LiveData<Resource<Liquidacion>> getLastPayment(long businessId) {
        final MutableLiveData<Resource<Liquidacion>> data = new MutableLiveData<>();
        data.postValue(new Resource<>(Status.RUNNING));

        appController.getApiInterface().getLastPayment(businessId)
                .enqueue(new Callback<Liquidacion>() {
                    @Override
                    public void onResponse(Call<Liquidacion> call, Response<Liquidacion> response) {
                        if (response.isSuccessful()) {
                            data.postValue(new Resource<>(Status.SUCCESS, response.body()));
                        } else {
                            data.postValue(new Resource<>(Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Liquidacion> call, Throwable t) {
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

    public LiveData<Resource<Periodo>> postSingleQr(QrToken qrToken){
        final MutableLiveData<Resource<Periodo>> data = new MutableLiveData<>();

        appController.getApiInterface().newShiftOffline(qrToken)
                .enqueue(new Callback<Periodo>() {
                    @Override
                    public void onResponse(Call<Periodo> call, Response<Periodo> response) {
                        if (response.isSuccessful()) {
                            data.setValue(new Resource<>(Status.SUCCESS, response.body()));
                        } else {
                            data.postValue(new Resource<>(Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Periodo> call, Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        data.postValue(new Resource<>(Status.FAILED, errorMessage));
                    }
                });
        return data;
    }
}

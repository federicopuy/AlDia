package com.example.federico.aldia.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.model.QrToken;
import com.example.federico.aldia.model.Resource;
import com.example.federico.aldia.model.Status;
import com.example.federico.aldia.network.AppController;
import com.example.federico.aldia.network.NetworkState;

import java.time.Instant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityDataSource {

    private AppController appController;

    public MainActivityDataSource(AppController appController) {
        this.appController = appController;
    }

    public LiveData<Resource<Liquidacion>> getLastPayment(long businessId) {
        final MutableLiveData<Resource<Liquidacion>> data = new MutableLiveData<>();

        data.postValue(new Resource<>(Status.RUNNING, null));


        appController.getApiInterface().getLastPayment(businessId)
                .enqueue(new Callback<Liquidacion>() {
                    @Override
                    public void onResponse(Call<Liquidacion> call, Response<Liquidacion> response) {
                        if (response.isSuccessful()) {
                            Liquidacion lastPayment = response.body();
                            data.postValue(new Resource<Liquidacion>(Status.SUCCESS, lastPayment));
                        } else {
                            data.postValue(new Resource<>(Status.FAILED,null));
                        }
                    }

                    @Override
                    public void onFailure(Call<Liquidacion> call, Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
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
                            try {
                                Periodo periodoEscaneado = response.body();
                                data.setValue(new Resource<Periodo>(Status.SUCCESS, periodoEscaneado));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                                //todo log
                                data.setValue(new Resource<Periodo>(Status.FAILED, null));
                            }
                    }

                    @Override
                    public void onFailure(Call<Periodo> call, Throwable t) {
                        data.setValue(new Resource<Periodo>(Status.FAILED, null));
                    }
                });
        return data;
    }
}

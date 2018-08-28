package com.example.federico.aldia.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.model.QrToken;
import com.example.federico.aldia.model.Resource;
import com.example.federico.aldia.model.Status;
import com.example.federico.aldia.model.TokenQR;
import com.example.federico.aldia.network.AppController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraActivityDataSource {

    private AppController appController;

    public CameraActivityDataSource(AppController appController) {
        this.appController = appController;
    }

    public LiveData<Resource<Periodo>> postToApi(QrToken qrToken) {
        TokenQR tokenQR = new TokenQR(qrToken.getIdToken());
        final MutableLiveData<Resource<Periodo>> data = new MutableLiveData<>();

        appController.getApiInterface().newPeriodo(tokenQR)
                .enqueue(new Callback<Periodo>() {
                    @Override
                    public void onResponse(Call<Periodo> call, Response<Periodo> response) {
                        if (response.isSuccessful()) {
                            data.postValue(new Resource<>(Status.SUCCESS, response.body()));
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

package com.example.federico.aldia.datasource;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.util.Log;

import com.example.federico.aldia.R;
import com.example.federico.aldia.activities.CameraActivity;
import com.example.federico.aldia.activities.EntryExitActivity;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.model.QrToken;
import com.example.federico.aldia.model.Resource;
import com.example.federico.aldia.model.Status;
import com.example.federico.aldia.model.TokenQR;
import com.example.federico.aldia.network.APIInterface;
import com.example.federico.aldia.network.AppController;
import com.example.federico.aldia.network.RetrofitClient;
import com.example.federico.aldia.utils.Constants;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrTokenDataSource {

    private AppController appController;

    public QrTokenDataSource(AppController appController) {
        this.appController = appController;
    }

    public LiveData<Resource<Periodo>> postToApi (QrToken qrToken) {

        final String callName = "postNewPeriodo";
        TokenQR tokenQR = new TokenQR(qrToken.getMToken());
        final MutableLiveData<Resource<Periodo>> data = new MutableLiveData<>();

        appController.getApiInterface().newPeriodo(tokenQR)
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
                        //todo log
                        data.setValue(new Resource<Periodo>(Status.FAILED, null));
                    }
                });


        return data;
    }
}

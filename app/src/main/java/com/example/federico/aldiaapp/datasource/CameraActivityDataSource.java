package com.example.federico.aldiaapp.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.federico.aldiaapp.R;
import com.example.federico.aldiaapp.model.QrToken;
import com.example.federico.aldiaapp.model.Resource;
import com.example.federico.aldiaapp.model.Shift;
import com.example.federico.aldiaapp.model.Status;
import com.example.federico.aldiaapp.model.TokenQR;
import com.example.federico.aldiaapp.network.AppController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraActivityDataSource {

    private AppController appController;
    private static final String TAG = "Camera Activity DS";

    public CameraActivityDataSource(AppController appController) {
        this.appController = appController;
    }

    public LiveData<Resource<Shift>> postToApi(QrToken qrToken) {
        TokenQR tokenQR = new TokenQR(qrToken.getIdToken());
        final MutableLiveData<Resource<Shift>> data = new MutableLiveData<>();

        appController.getApiInterface().newShift(tokenQR)
                .enqueue(new Callback<Shift>() {
                    @Override
                    public void onResponse(Call<Shift> call, Response<Shift> response) {
                        if (response.isSuccessful()) {
                            data.postValue(new Resource<>(Status.SUCCESS, response.body()));
                        } else {
                            data.postValue(new Resource<>(Status.FAILED, response.message()));
                        }
                    }
                    @Override
                    public void onFailure(Call<Shift> call, Throwable t) {
                        String errorMessage = t == null ? appController.getApplicationContext().getString(R.string.error_servidor) : t.getMessage();
                        Log.e(TAG, errorMessage);
                        data.postValue(new Resource<>(Status.FAILED, errorMessage));
                    }
                });
        return data;
    }
}

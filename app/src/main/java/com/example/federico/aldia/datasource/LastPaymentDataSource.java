package com.example.federico.aldia.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.network.AppController;
import com.example.federico.aldia.network.NetworkState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LastPaymentDataSource {

    private AppController appController;
    private MutableLiveData networkState;

    public LastPaymentDataSource(AppController appController) {
        this.appController = appController;
        networkState = new MutableLiveData();
    }

    public LiveData<Liquidacion> getLastPayment(long businessId) {
        final MutableLiveData<Liquidacion> data = new MutableLiveData<>();
        networkState.postValue(NetworkState.LOADING);

        appController.getApiInterface().getUltimaLiquidacion(businessId)
                .enqueue(new Callback<Liquidacion>() {
                    @Override
                    public void onResponse(Call<Liquidacion> call, Response<Liquidacion> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                            networkState.postValue(NetworkState.LOADED);
                        } else {
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Liquidacion> call, Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });
        return data;
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

}

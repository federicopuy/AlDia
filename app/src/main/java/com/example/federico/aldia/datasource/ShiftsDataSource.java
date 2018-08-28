package com.example.federico.aldia.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.model.Resource;
import com.example.federico.aldia.model.Status;
import com.example.federico.aldia.network.AppController;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShiftsDataSource {

    private AppController appController;
    public ShiftsDataSource(AppController appController) {
        this.appController = appController;
    }

    public LiveData<Resource<List<Periodo>>> getShifts(String searchType, long id) {
        final MutableLiveData<Resource<List<Periodo>>> data = new MutableLiveData<>();

        appController.getApiInterface().getShifts(searchType, id)
                .enqueue(new Callback<List<Periodo>>() {
                    @Override
                    public void onResponse(Call<List<Periodo>> call, Response<List<Periodo>> response) {
                        if (response.isSuccessful()) {
                            data.postValue(new Resource<>(Status.SUCCESS, response.body()));
                        } else {
                            data.postValue(new Resource<>(Status.FAILED, response.message()));
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Periodo>> call, Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        data.postValue(new Resource<>(Status.FAILED, errorMessage));
                    }
                });
        return data;
    }
}

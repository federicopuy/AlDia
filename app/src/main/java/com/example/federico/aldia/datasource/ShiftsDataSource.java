package com.example.federico.aldia.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.federico.aldia.model.Periodo;
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

    public LiveData<List<Periodo>> getShifts(String searchType, long id) {

        final String callName = "getShifts";
        final MutableLiveData<List<Periodo>> data = new MutableLiveData<>();

        appController.getApiInterface().getShifts(searchType, id)
                .enqueue(new Callback<List<Periodo>>() {
                    @Override
                    public void onResponse(Call<List<Periodo>> call, Response<List<Periodo>> response) {
                        if (response.isSuccessful()) {
                            try {
                                data.setValue(response.body());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            //todo not succeful
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Periodo>> call, Throwable t) {

                    }
                });

        return data;
    }

}

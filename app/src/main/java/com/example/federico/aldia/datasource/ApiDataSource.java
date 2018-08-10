package com.example.federico.aldia.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.View;

import com.example.federico.aldia.R;
import com.example.federico.aldia.activities.ShiftsActivity;
import com.example.federico.aldia.adapters.ShiftAdapter;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.network.APIInterface;
import com.example.federico.aldia.network.AppController;
import com.example.federico.aldia.network.RetrofitClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiDataSource {

    private AppController appController;

    public ApiDataSource(AppController appController) {
        this.appController = appController;
    }

    public LiveData<List<Periodo>> getShifts(String searchType, long id) {

        final String callName = "getShifts";
        final MutableLiveData<List<Periodo>> data = new MutableLiveData<>();

        appController.getApiInterface().getPeriodos(searchType, id)
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

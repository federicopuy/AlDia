package com.example.federico.aldiaapp.datasource;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.federico.aldiaapp.model.AllPayments;
import com.example.federico.aldiaapp.model.Liquidacion;
import com.example.federico.aldiaapp.network.AppController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentsActivityDataSource extends PageKeyedDataSource<Long, Liquidacion> {

    private static final String TAG = PaymentsActivityDataSource.class.getSimpleName();
    private AppController appController;
    private long businessId;

    PaymentsActivityDataSource(AppController appController, long businessId) {
        this.appController = appController;
        this.businessId = businessId;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Liquidacion> callback) {
        appController.getApiInterface().getAllPayments(businessId, 0, 20)
                .enqueue(new Callback<AllPayments>() {
                    @Override
                    public void onResponse(Call<AllPayments> call, Response<AllPayments> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                callback.onResult(response.body().getLiquidacion(), null, 1L);
                            } else {
                                Log.e(TAG, response.message());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AllPayments> call, Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        Log.e(TAG, errorMessage);
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Liquidacion> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Liquidacion> callback) {
        Log.i(TAG, "Loading Range " + params.key + " Count " + params.requestedLoadSize);
        appController.getApiInterface().getAllPayments(businessId, params.key, params.requestedLoadSize)
                .enqueue(new Callback<AllPayments>() {
                    @Override
                    public void onResponse(Call<AllPayments> call, Response<AllPayments> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                long nextKey;
                                if (!response.body().getLast()) {
                                    callback.onResult(response.body().getLiquidacion(), null);
                                } else {
                                    nextKey = params.key + 1;
                                    callback.onResult(response.body().getLiquidacion(), nextKey);

                                }
                            } else {
                                Log.e(TAG, response.message());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AllPayments> call, Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        Log.e(TAG, errorMessage);
                    }
                });


    }
}

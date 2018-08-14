package com.example.federico.aldia.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.federico.aldia.datasource.LastPaymentDataSource;
import com.example.federico.aldia.datasource.ShiftsDataSource;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.network.AppController;
import com.example.federico.aldia.network.NetworkState;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private AppController appController;
    private LiveData<Liquidacion> lastPayment;
    private LiveData<NetworkState> networkState;


    public MainActivityViewModel(AppController appController, long businessId) {
        this.appController = appController;
        LastPaymentDataSource lastPaymentDataSource = new LastPaymentDataSource(appController);
        lastPayment = lastPaymentDataSource.getLastPayment(businessId);
        networkState = lastPaymentDataSource.getNetworkState();

    }

    public LiveData<Liquidacion> getLastPayment() {
        return lastPayment;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }


    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final AppController appController;
        private final long businessId;

        public Factory(@NonNull AppController appController, long businessId) {
            this.appController = appController;
            this.businessId = businessId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new MainActivityViewModel(appController, businessId);
        }
    }
}
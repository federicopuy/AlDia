package com.example.federico.aldia.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.federico.aldia.db.MainActivityRepository;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.model.QrToken;
import com.example.federico.aldia.model.Resource;
import com.example.federico.aldia.model.Status;
import com.example.federico.aldia.network.AppController;
import com.example.federico.aldia.network.NetworkState;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private AppController appController;
    private LiveData<Liquidacion> lastPayment;
    private LiveData<NetworkState> networkState;
    MainActivityRepository mRepository;
    LiveData<List<QrToken>> pendingQrTokens;
   public MutableLiveData<QrToken> qrTokenLiveData = new MutableLiveData<QrToken>();
    LiveData<Resource<Periodo>> qrTokenLive;
    MutableLiveData<QrToken> qrTokenMutableLiveData = new MutableLiveData<>();

    public MainActivityViewModel(AppController appController, long businessId) {
        this.appController = appController;
        mRepository = new MainActivityRepository(appController, businessId);
        lastPayment = mRepository.getLastPayment(businessId);
        networkState = mRepository.getNetworkState();
        pendingQrTokens = mRepository.getmAllPendingTokenQrs();
    }

    public LiveData<Liquidacion> getLastPayment() {
        return lastPayment;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<List<QrToken>> getPendingQrCodes(){
        return pendingQrTokens;
    }

    public LiveData<Resource<Periodo>> postQrToken(QrToken qrToken){

        return  mRepository.postTokenToServer(qrToken);
    }

    public void setQrTokenMutableLiveData(QrToken qrToken) {
        this.qrTokenMutableLiveData.setValue(qrToken);
    }

    public void deleteQrToken (QrToken qrToken){
        mRepository.delete(qrToken);
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
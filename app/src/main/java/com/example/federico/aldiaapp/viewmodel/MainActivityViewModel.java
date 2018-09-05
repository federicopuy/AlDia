package com.example.federico.aldiaapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.federico.aldiaapp.db.MainActivityRepository;
import com.example.federico.aldiaapp.model.Liquidacion;
import com.example.federico.aldiaapp.model.QrToken;
import com.example.federico.aldiaapp.model.Resource;
import com.example.federico.aldiaapp.model.Shift;
import com.example.federico.aldiaapp.network.AppController;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private LiveData<Resource<Liquidacion>> lastPayment;
    private MainActivityRepository mRepository;
    private LiveData<List<QrToken>> pendingQrTokens;
    private MediatorLiveData<Resource<Liquidacion>> mediatorLiveData;

    MainActivityViewModel(AppController appController, long businessId) {
        mRepository = new MainActivityRepository(appController, businessId);
        lastPayment = mRepository.getLastPayment(businessId);
        pendingQrTokens = mRepository.getmAllPendingTokenQrs();
        mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.addSource(lastPayment,
                liquidacionResource -> mediatorLiveData.setValue(liquidacionResource));
    }

    public LiveData<List<QrToken>> getPendingQrCodes() {
        return pendingQrTokens;
    }

    public LiveData<Resource<Shift>> postQrToken(QrToken qrToken) {
        return mRepository.postTokenToServer(qrToken);
    }

    public void deleteQrToken(QrToken qrToken) {
        mRepository.delete(qrToken);
    }

    public MediatorLiveData<Resource<Liquidacion>> getMediatorLiveData() {
        return mediatorLiveData;
    }

    //copied from https://medium.com/@_AB/mediatorlivedata-practice-and-usage-6dddcebf6a0
    //https://medium.com/@BladeCoder/to-implement-a-manual-refresh-without-modifying-your-existing-livedata-logic-i-suggest-that-your-7db1b8414c0e
    public void refresh(long businessId) {
        mediatorLiveData.removeSource(lastPayment);
        lastPayment = mRepository.getLastPayment(businessId);
        mediatorLiveData.addSource(lastPayment, liquidacionResource -> mediatorLiveData.setValue(liquidacionResource));
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
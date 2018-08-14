package com.example.federico.aldia.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.federico.aldia.db.QrTokenRepository;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.model.QrToken;
import com.example.federico.aldia.model.Resource;
import com.example.federico.aldia.network.AppController;

import java.util.List;

public class QrTokenViewModel extends AndroidViewModel {

    private QrTokenRepository mRepository;
    private LiveData<List<QrToken>> mAllPendingQrTokens;
    private LiveData<Resource<Periodo>> scannedShift;

    public QrTokenViewModel(AppController appController, QrToken qrToken) {
        super(appController);
        mRepository = new QrTokenRepository(appController);
        scannedShift = mRepository.postQrToken(qrToken);
        mAllPendingQrTokens = mRepository.getmAllPendingTokenQrs();
    }

    public LiveData<List<QrToken>> getmAllPendingQrTokens() {
        return mAllPendingQrTokens;
    }
    public void insert(QrToken qrToken){
        mRepository.insert(qrToken);
    }

    public LiveData<Resource<Periodo>> postQrTokenToServer (QrToken qrToken){
        return scannedShift;
    }


    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final AppController appController;
        private final QrToken qrToken;




        public Factory(@NonNull AppController appController, QrToken qrToken) {
            this.appController = appController;
            this.qrToken = qrToken;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new QrTokenViewModel(appController, qrToken);
        }
    }

}

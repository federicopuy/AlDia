package com.example.federico.aldiaapp.viewmodel;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.federico.aldiaapp.db.CameraActivityRepository;
import com.example.federico.aldiaapp.model.QrToken;
import com.example.federico.aldiaapp.model.Resource;
import com.example.federico.aldiaapp.model.Shift;
import com.example.federico.aldiaapp.network.AppController;

public class CameraActivityViewModel extends AndroidViewModel {

    private CameraActivityRepository mRepository;
    private LiveData<Resource<Shift>> scannedShift;

    CameraActivityViewModel(AppController appController, QrToken qrToken) {
        super(appController);
        mRepository = new CameraActivityRepository(appController);
        scannedShift = mRepository.postQrToken(qrToken);
    }

    public void insert(QrToken qrToken){
        mRepository.insert(qrToken);
    }

    public LiveData<Resource<Shift>> postQrTokenToServer() {
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
            return (T) new CameraActivityViewModel(appController, qrToken);
        }
    }
}

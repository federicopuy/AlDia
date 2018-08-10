package com.example.federico.aldia.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.example.federico.aldia.datasource.ApiDataSource;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.network.AppController;

import java.util.List;

public class ShiftsViewModel extends ViewModel {

    private AppController appController;
    private LiveData<List<Periodo>> shiftsList;

    public ShiftsViewModel(AppController appController, String searchType, long id) {
        this.appController = appController;
        ApiDataSource apiDataSource = new ApiDataSource(appController);
        shiftsList = apiDataSource.getShifts(searchType, id);
    }

    public LiveData<List<Periodo>> getShiftsList() {
        return shiftsList;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final AppController appController;
        private final String searchType;
        private final long id;


        public Factory(@NonNull AppController appController, String searchType, long id) {
            this.appController = appController;
            this.searchType = searchType;
            this.id = id;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new ShiftsViewModel(appController, searchType, id);
        }
    }
}

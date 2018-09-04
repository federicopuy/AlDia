package com.example.federico.aldiaapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.federico.aldiaapp.datasource.ShiftsDataSource;
import com.example.federico.aldiaapp.model.Periodo;
import com.example.federico.aldiaapp.model.Resource;
import com.example.federico.aldiaapp.network.AppController;

import java.util.List;

public class ShiftsViewModel extends ViewModel {

    private LiveData<Resource<List<Periodo>>> shiftsList;

    ShiftsViewModel(AppController appController, String searchType, long id) {
        ShiftsDataSource shiftsDataSource = new ShiftsDataSource(appController);
        shiftsList = shiftsDataSource.getShifts(searchType, id);
    }

    public LiveData<Resource<List<Periodo>>> getShiftsList() {
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

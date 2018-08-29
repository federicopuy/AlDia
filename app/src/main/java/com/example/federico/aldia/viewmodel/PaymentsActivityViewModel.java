package com.example.federico.aldia.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.federico.aldia.datasource.PaymentsDataSourceFactory;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.network.AppController;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PaymentsActivityViewModel extends ViewModel {

    private LiveData<PagedList<Liquidacion>> paymentsLiveData;
    private AppController appController;
    private long businessId;

    PaymentsActivityViewModel(AppController appController, long businessId) {
        this.appController = appController;
        this.businessId = businessId;
        init();
    }

    private void init() {
        PaymentsDataSourceFactory paymentsDataSourceFactory = new PaymentsDataSourceFactory(appController, businessId);
        Executor executor = Executors.newFixedThreadPool(5);

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(20).build();
        paymentsLiveData = (new LivePagedListBuilder(paymentsDataSourceFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<PagedList<Liquidacion>> getPaymentsLiveData() {
        return paymentsLiveData;
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
            //noinspection unchecked
            return (T) new PaymentsActivityViewModel(appController, businessId);
        }
    }
}

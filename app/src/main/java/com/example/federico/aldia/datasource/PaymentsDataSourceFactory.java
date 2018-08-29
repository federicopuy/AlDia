package com.example.federico.aldia.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.example.federico.aldia.network.AppController;

public class PaymentsDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PaymentsActivityDataSource> mutableLiveData;
    private AppController appController;
    private long businessId;
    private PaymentsActivityDataSource paymentsActivityDataSource;

    public PaymentsDataSourceFactory(AppController appController, long businessId) {
        this.appController = appController;
        this.mutableLiveData = new MutableLiveData<>();
        this.businessId = businessId;
    }

    @Override
    public DataSource create() {
        paymentsActivityDataSource = new PaymentsActivityDataSource(appController, businessId);
        mutableLiveData.postValue(paymentsActivityDataSource);
        return paymentsActivityDataSource;
    }

    public MutableLiveData<PaymentsActivityDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

    public PaymentsActivityDataSource getPaymentsActivityDataSource() {
        return paymentsActivityDataSource;
    }


}

package com.example.federico.aldia.db;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.federico.aldia.datasource.MainActivityDataSource;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.model.QrToken;
import com.example.federico.aldia.model.Resource;
import com.example.federico.aldia.model.Status;
import com.example.federico.aldia.network.AppController;
import com.example.federico.aldia.network.NetworkState;

import java.util.List;

public class MainActivityRepository {

    private QrTokenDAO mDao;
    AppController appController;
    private LiveData<Liquidacion> lastPayment;
    private LiveData<NetworkState> networkState;
    MainActivityDataSource mainActivityDataSource;

    public MainActivityRepository(AppController appController, long businessId) {
        this.appController = appController;
        QrTokenDatabase db = QrTokenDatabase.getDatabase(appController);
        mDao = db.qrTokenDAO();
        mainActivityDataSource = new MainActivityDataSource(appController);
        lastPayment = mainActivityDataSource.getLastPayment(businessId);
        networkState = mainActivityDataSource.getNetworkState();
    }

    public LiveData<List<QrToken>> getmAllPendingTokenQrs() {
        return mDao.loadAllPendingQrTokens();
    }

    public void delete(QrToken qrToken) {
        new deleteAsyncTast(mDao).execute(qrToken);
    }

    private static class deleteAsyncTast extends AsyncTask<QrToken, Void, Void> {
        private QrTokenDAO mAsyncTaskDao;

        deleteAsyncTast(QrTokenDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final QrToken... params) {
            mAsyncTaskDao.deleteQrToken(params[0]);
            return null;
        }
    }

    public LiveData<Liquidacion> getLastPayment(long businessId) {
        return lastPayment;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<Resource<Periodo>> postPendingQRCode(QrToken pendingQrToken) {
        LiveData<Resource<Periodo>> resourceLiveData = mainActivityDataSource.postSingleQr(pendingQrToken);
        return resourceLiveData;
    }
}

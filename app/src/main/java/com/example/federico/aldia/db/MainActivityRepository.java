package com.example.federico.aldia.db;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.federico.aldia.datasource.MainActivityDataSource;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.model.QrToken;
import com.example.federico.aldia.model.Resource;
import com.example.federico.aldia.network.AppController;

import java.util.List;

public class MainActivityRepository {

    private QrTokenDAO mDao;
    private MainActivityDataSource mainActivityDataSource;

    public MainActivityRepository(AppController appController, long businessId) {
        QrTokenDatabase db = QrTokenDatabase.getDatabase(appController);
        mDao = db.qrTokenDAO();
        mainActivityDataSource = new MainActivityDataSource(appController);
    }

    /*-------------------------------------- DB --------------------------------------------***/

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

    /*-------------------------------------- API -------------------------------------------***/

    public LiveData<Resource<Liquidacion>> getLastPayment(long businessId) {
        return mainActivityDataSource.getLastPayment(businessId);
    }

    public LiveData<Resource<Periodo>> postTokenToServer(QrToken qrToken) {
        return mainActivityDataSource.postSingleQr(qrToken);
    }

}

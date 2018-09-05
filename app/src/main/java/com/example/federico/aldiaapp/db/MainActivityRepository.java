package com.example.federico.aldiaapp.db;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.federico.aldiaapp.datasource.MainActivityDataSource;
import com.example.federico.aldiaapp.model.Payment;
import com.example.federico.aldiaapp.model.QrToken;
import com.example.federico.aldiaapp.model.Resource;
import com.example.federico.aldiaapp.model.Shift;
import com.example.federico.aldiaapp.network.AppController;

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

    public LiveData<Resource<Payment>> getLastPayment(long businessId) {
        return mainActivityDataSource.getLastPayment(businessId);
    }

    public LiveData<Resource<Shift>> postTokenToServer(QrToken qrToken) {
        return mainActivityDataSource.postSingleQr(qrToken);
    }

}

package com.example.federico.aldia.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.federico.aldia.datasource.QrTokenDataSource;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.model.QrToken;
import com.example.federico.aldia.model.Resource;
import com.example.federico.aldia.model.Status;
import com.example.federico.aldia.network.AppController;

import java.util.List;

public class QrTokenRepository {

    private QrTokenDAO mDao;
    private LiveData<List<QrToken>> mAllPendingTokenQrs;
    AppController appController;

    public QrTokenRepository(AppController appController) {
        this.appController = appController;
        QrTokenDatabase db = QrTokenDatabase.getDatabase(appController);
        mDao = db.qrTokenDAO();
        mAllPendingTokenQrs = mDao.loadAllPendingQrTokens();
    }

    public LiveData<List<QrToken>> getmAllPendingTokenQrs (){
        return mAllPendingTokenQrs;
    }

    public void insert (QrToken qrToken) {
        new insertAsyncTask(mDao).execute(qrToken);
    }

    private static class insertAsyncTask extends AsyncTask<QrToken, Void, Void> {

        private QrTokenDAO mAsyncTaskDao;
        insertAsyncTask(QrTokenDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final QrToken... params) {
            mAsyncTaskDao.insertQrToken(params[0]);
            return null;
        }
    }

    public void delete (QrToken qrToken) {
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

    public LiveData<Resource<Periodo>> postQrToken(QrToken qrToken){

        QrTokenDataSource dataSource = new QrTokenDataSource(appController);

        LiveData<Resource<Periodo>> periodoLiveData = dataSource.postToApi(qrToken);

        if (periodoLiveData.getValue().status == Status.SUCCESS){
            // API is working and user is connected to internet
            List<QrToken> pendingQrTokens = getmAllPendingTokenQrs().getValue();
            for (QrToken qrToken1:pendingQrTokens) {
                delete(qrToken1);
                //postQrToken(qrToken1);
                //todo post qr token with alternative post
                System.out.println("Alternative post " + qrToken1.getMTimestamp());
            }
        } else {
            if (periodoLiveData.getValue().status == Status.FAILED){
                // add QrToken to be sent to API later
                insert(qrToken);
            }
        }

        return periodoLiveData;
    }

}

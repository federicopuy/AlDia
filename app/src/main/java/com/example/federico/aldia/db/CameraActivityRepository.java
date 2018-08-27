package com.example.federico.aldia.db;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.federico.aldia.datasource.CameraActivityDataSource;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.model.QrToken;
import com.example.federico.aldia.model.Resource;
import com.example.federico.aldia.network.AppController;

public class CameraActivityRepository {

    private QrTokenDAO mDao;
    AppController appController;
    LiveData<Resource<Periodo>> periodoLiveData;

    public CameraActivityRepository(AppController appController) {
        this.appController = appController;
        QrTokenDatabase db = QrTokenDatabase.getDatabase(appController);
        mDao = db.qrTokenDAO();
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

    public LiveData<Resource<Periodo>> postQrToken(QrToken qrToken){
        CameraActivityDataSource dataSource = new CameraActivityDataSource(appController);
        periodoLiveData = dataSource.postToApi(qrToken);
        return periodoLiveData;
    }
}

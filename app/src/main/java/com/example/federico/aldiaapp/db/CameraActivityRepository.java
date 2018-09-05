package com.example.federico.aldiaapp.db;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.federico.aldiaapp.datasource.CameraActivityDataSource;
import com.example.federico.aldiaapp.model.QrToken;
import com.example.federico.aldiaapp.model.Resource;
import com.example.federico.aldiaapp.model.Shift;
import com.example.federico.aldiaapp.network.AppController;

public class CameraActivityRepository {

    private QrTokenDAO mDao;
    private CameraActivityDataSource dataSource;

    public CameraActivityRepository(AppController appController) {
        QrTokenDatabase db = QrTokenDatabase.getDatabase(appController);
        mDao = db.qrTokenDAO();
        dataSource = new CameraActivityDataSource(appController);
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

    public LiveData<Resource<Shift>> postQrToken(QrToken qrToken) {
        return dataSource.postToApi(qrToken);
    }
}

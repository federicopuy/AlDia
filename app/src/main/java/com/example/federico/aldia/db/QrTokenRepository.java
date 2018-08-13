package com.example.federico.aldia.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.federico.aldia.model.QrToken;

import java.util.List;

public class QrTokenRepository {

    private QrTokenDAO mDao;
    private LiveData<List<QrToken>> mAllPendingTokenQrs;

    public QrTokenRepository(Application application) {
        QrTokenDatabase db = QrTokenDatabase.getDatabase(application);
        mDao = db.qrTokenDAO();
        mAllPendingTokenQrs = mDao.loadAllPendingQrTokens();
    }

    LiveData<List<QrToken>> getmAllPendingTokenQrs (){
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

}

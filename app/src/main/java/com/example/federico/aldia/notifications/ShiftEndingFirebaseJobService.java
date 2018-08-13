package com.example.federico.aldia.notifications;

import android.annotation.TargetApi;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class  ShiftEndingFirebaseJobService extends JobService{

    private AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        mBackgroundTask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = ShiftEndingFirebaseJobService.this;
                RemiderTask.executeTask(context, RemiderTask.ACTION_REMIND_SHIFT_END);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(jobParameters, false);
            }
        };

        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}
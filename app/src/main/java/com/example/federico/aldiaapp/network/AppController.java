package com.example.federico.aldiaapp.network;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class AppController extends Application {

    private APIInterface apiInterface;
    //Only references application context, so there should not be memory leaks
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        AppController.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return AppController.context;
    }

    public static AppController get(Context context) {
        return (AppController) context.getApplicationContext();
    }

    public APIInterface getApiInterface() {
        if(apiInterface == null) {
            apiInterface = RetrofitClient.getClient();
        }
        return apiInterface;
    }



}


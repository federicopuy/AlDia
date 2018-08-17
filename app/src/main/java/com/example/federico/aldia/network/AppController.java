package com.example.federico.aldia.network;

import android.app.Application;
import android.content.Context;

public class AppController extends Application {

    private APIInterface apiInterface;
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
            apiInterface = RetrofitClient.getClientVM();
        }
        return apiInterface;
    }



}


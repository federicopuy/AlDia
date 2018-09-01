package com.example.federico.aldia.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {

    private static final String Tag = "Network Interceptor";

    ConnectivityInterceptor( ) {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d(Tag, " Network Intercepted");

        if (!NetworkUtils.isOnline(AppController.getAppContext())) {
            Log.e("Error", "Error de Conexion");
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }
}


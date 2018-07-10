package com.example.federico.aldia.network;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {

    private static final String Tag = "Network Interceptor";
    private Context mContext;

    ConnectivityInterceptor(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d(Tag, " Network Intercepted");

        if (!NetworkUtils.isOnline(mContext)) {
            Log.e("Error", "Error de Conexion");
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }
}


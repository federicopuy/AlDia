package com.example.federico.aldiaapp.network;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.federico.aldiaapp.utils.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    private static final String Tag = "Token Interceptor";

    TokenInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getAppContext());
            String token = prefs.getString(Constants.KEY_TOKEN_JWT, "");
            request = request.newBuilder()
                    .addHeader("authorization", token).build();

            return chain.proceed(request);

        }

    }


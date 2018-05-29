package com.example.federico.aldia.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.federico.aldia.model.Constantes;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    private static final String Tag = "Token Interceptor";

    private Context mContext;

    public TokenInterceptor(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {


            Request request = chain.request();

            SharedPreferences prefs;
            prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

            String token = prefs.getString(Constantes.KEY_TOKEN_JWT, "");

            request = request.newBuilder()
                    .addHeader("authorization", token).build();

            return chain.proceed(request);


        }


//            //getAccessToken is your own accessToken(retrieve it by saving in shared preference or any other option )
//            if(getAccessToken().isEmpty()){
//                PrintLog.error("retrofit 2","Authorization header is already present or token is empty....");
//                return chain.proceed(chain.request());
//            }
//            Request authorisedRequest = chain.request().newBuilder()
//                    .addHeader("Authorization", getAccessToken()).build();
//            PrintLog.error("retrofit 2","Authorization header is added to the url....");
//            return chain.proceed(authorisedRequest);
//        }}).build();
//
//    }


    }


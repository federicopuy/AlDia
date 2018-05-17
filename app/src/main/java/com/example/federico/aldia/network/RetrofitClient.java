package com.example.federico.aldia.network;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    private static OkHttpClient client = null;

    public static Retrofit getClient(Context mContext) {

        if (retrofit==null){

//            client = new OkHttpClient.Builder()
//                    .addInterceptor(new ConnectivityInterceptor(mContext))
//                    .addInterceptor(new TokenExpiryInterceptor(mContext))
//                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(URLs.APIURLCOMPLETA)
                //    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}

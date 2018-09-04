package com.example.federico.aldiaapp.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Objects;

public class NetworkState {

    public enum Status{
        RUNNING,
        SUCCESS,
        FAILED
    }

        public final Status status;
        private final String msg;

        public static final NetworkState LOADED;
        public static final NetworkState LOADING;

        public NetworkState(Status status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        static {
            LOADED=new NetworkState(Status.SUCCESS,"Success");
            LOADING=new NetworkState(Status.RUNNING,"Running");
        }

        public Status getStatus() {
            return status;
        }

        public String getMsg() {
            return msg;
        }


        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public static boolean isNetworkConnected(Context context) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }


}

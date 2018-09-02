package com.example.federico.aldia.network;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.federico.aldia.model.FirebaseToken;
import com.example.federico.aldia.utils.Constants;
import com.example.federico.aldia.utils.Utils;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

public class TokenAuthenticator implements Authenticator {

    private static final String Tag = "Token Authenticator";

    @Nullable
    @Override
    public Request authenticate(Route route, Response response) throws IOException {

        Log.e(Tag, "Error 401");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getAppContext());
        String tokenFirebase = prefs.getString(Constants.KEY_TOKEN_FIREBASE, "");
        if (tokenFirebase == "") {
            return null;
        } else {
            FirebaseToken firebaseToken = new FirebaseToken(tokenFirebase);
            Call<String> callRefresh = AppController.get(AppController.getAppContext()).getApiInterface().loginUser(firebaseToken);
            String refreshedTokenJWT = "";
            refreshedTokenJWT = Utils.getJwtTokenFormatted(callRefresh.execute().body());
            Log.d(Tag, "Refreshed Token " + refreshedTokenJWT);
            prefs.edit().putString(Constants.KEY_TOKEN_JWT, refreshedTokenJWT).apply();
            if (refreshedTokenJWT.equals("")) {
                Log.e(Tag, "Refreshed Token Null");
            }
            return response.request().newBuilder()
                    .header("authorization", refreshedTokenJWT)
                    .build();
        }
    }
}

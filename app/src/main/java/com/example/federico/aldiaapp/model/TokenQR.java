package com.example.federico.aldiaapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenQR {

    @SerializedName("id_token")
    @Expose
    private String idToken;

    /**
     * No args constructor for use in serialization
     *
     */
    public TokenQR() {
    }

    /**
     *
     * @param idToken
     */
    public TokenQR(String idToken) {
        super();
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

}
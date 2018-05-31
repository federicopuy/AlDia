package com.example.federico.aldia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comercio {


    @SerializedName("userId")
    @Expose
    private long userId;
    @SerializedName("userComercio")
    @Expose
    private String userComercio;
    @SerializedName("fechaFin")
    @Expose
    private String fechaFin;
    @SerializedName("fechaInicio")
    @Expose
    private String fechaInicio;



    /**
     * No args constructor for use in serialization
     *
     */
    public Comercio() {
    }

    public Comercio(long userId, String userComercio, String fechaFin, String fechaInicio) {
        this.userId = userId;
        this.userComercio = userComercio;
        this.fechaFin = fechaFin;
        this.fechaInicio = fechaInicio;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserComercio() {
        return userComercio;
    }

    public void setUserComercio(String userComercio) {
        this.userComercio = userComercio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
}
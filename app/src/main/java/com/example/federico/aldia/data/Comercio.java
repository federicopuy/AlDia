package com.example.federico.aldia.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comercio {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userComercio")
    @Expose
    private String userComercio;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("empleadoId")
    @Expose
    private Integer empleadoId;


    /**
     * No args constructor for use in serialization
     *
     */
    public Comercio() {
    }

    public Comercio(Integer id, String userComercio, Integer userId, Integer empleadoId) {
        this.id = id;
        this.userComercio = userComercio;
        this.userId = userId;
        this.empleadoId = empleadoId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserComercio() {
        return userComercio;
    }

    public void setUserComercio(String userComercio) {
        this.userComercio = userComercio;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Integer empleadoId) {
        this.empleadoId = empleadoId;
    }
}
package com.example.federico.aldia.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "qrtoken_table")
public class QrToken {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String idToken;

    @NonNull
    private String hora;

    @Ignore
    public QrToken(String idToken, String hora) {
        this.idToken = idToken;
        this.hora = hora;
    }

    public QrToken(int id, String idToken, String hora) {
        this.id = id;
        this.idToken = idToken;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}

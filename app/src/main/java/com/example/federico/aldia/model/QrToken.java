package com.example.federico.aldia.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "qrtoken_table")
public class QrToken {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String mToken;

    @NonNull
    private String mTimestamp;

    public QrToken(int id, String mToken, String mTimestamp) {
        this.id = id;
        this.mToken = mToken;
        this.mTimestamp = mTimestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }

    public String getMTimestamp() {
        return mTimestamp;
    }

    public void setmTimestamp(String mTimestamp) {
        this.mTimestamp = mTimestamp;
    }
}

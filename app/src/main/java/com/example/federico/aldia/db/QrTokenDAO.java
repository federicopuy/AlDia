package com.example.federico.aldia.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.federico.aldia.model.QrToken;

import java.util.List;

@Dao
public interface QrTokenDAO {

    @Query("SELECT * FROM qrtoken_table")
    LiveData<List<QrToken>> loadAllPendingQrTokens();

    @Insert
    void insertQrToken(QrToken qrToken);

    @Delete
    void deleteQrToken(QrToken qrToken);

}

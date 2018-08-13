package com.example.federico.aldia.db;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import android.util.Log;

import com.example.federico.aldia.model.QrToken;

@Database(entities = {QrToken.class}, version = 1, exportSchema = false)
public abstract class QrTokenDatabase extends RoomDatabase {

    private static QrTokenDatabase INSTANCE;
    private static final String LOG_TAG = QrTokenDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "qrTokenDatabase";

    static QrTokenDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            QrTokenDatabase.class, DATABASE_NAME)
                            .build();
                }

        }
        Log.d(LOG_TAG, "Getting the database instance");
        return INSTANCE;
    }

    public abstract QrTokenDAO qrTokenDAO();
}

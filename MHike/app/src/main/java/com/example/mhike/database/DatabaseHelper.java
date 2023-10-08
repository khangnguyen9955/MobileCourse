package com.example.mhike.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "m_hike.db";
    private static final int DATABASE_VERSION = 4;
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableContract.HikeEntry.CREATE_HIKES_TABLE);
        db.execSQL(TableContract.ObservationEntry.CREATE_OBSERVATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableContract.HIKES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableContract.OBSERVATIONS_TABLE_NAME);

        // Recreate the tables with the updated schema
        db.execSQL(TableContract.HikeEntry.CREATE_HIKES_TABLE);
        db.execSQL(TableContract.ObservationEntry.CREATE_OBSERVATIONS_TABLE);
    }
}


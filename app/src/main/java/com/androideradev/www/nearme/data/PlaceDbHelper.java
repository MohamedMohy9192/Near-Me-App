package com.androideradev.www.nearme.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.androideradev.www.nearme.data.PlaceContract.PlaceEntry;

public class PlaceDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "places.db";

    private static final int DATABASE_VERSION = 1;

    public PlaceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_PLACES_TABLE = "CREATE TABLE " + PlaceEntry.TABLE_NAME + " (" +
                PlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PlaceEntry.COLUMN_PLACE_ID + " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_PLACE_NAME + " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_PLACE_PHONE + " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_PLACE_ADDRESS + " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_PLACE_LAT + " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_PLACE_LNG + " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_PLACE_TYPE + " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_PLACE_IS_FAVORITE + " INTEGER DEFAULT 0, " +
                "UNIQUE (" + PlaceEntry.COLUMN_PLACE_ID + ") ON CONFLICT REPLACE" +
                "); ";

        db.execSQL(SQL_CREATE_PLACES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PlaceEntry.TABLE_NAME);
        onCreate(db);
    }
}

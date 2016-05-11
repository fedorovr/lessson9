package ru.csc.roman_fedorov.weatherapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by roman on 24.04.2016.
 */
public class WeatherDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "weatherDatabase";

    private static WeatherDatabaseHelper sInstance;

    public static synchronized WeatherDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new WeatherDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private WeatherDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_CURRENCY_TABLE =
            "CREATE TABLE " + WeatherTable.TABLE_NAME
                    + "("
                    + WeatherTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + WeatherTable.WEATHER_CITY + " TEXT, "
                    + WeatherTable.WEATHER_DATE + " TEXT, "
                    + WeatherTable.WEATHER_DESCRIPTION + " TEXT, "
                    + WeatherTable.WEATHER_LOW_TEMPERATURE + " INT, "
                    + WeatherTable.WEATHER_HIGH_TEMPERATURE + " INT"
                    + ")";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + WeatherTable.TABLE_NAME;

    public ArrayList<String> getCities() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> cities = new ArrayList<>();
        Cursor mCursor = null;
        try {
            mCursor = db.rawQuery("SELECT DISTINCT " + WeatherTable.WEATHER_CITY + " FROM " + WeatherTable.TABLE_NAME, null);
            while (mCursor.moveToNext()) {
                cities.add(mCursor.getString(mCursor.getColumnIndex(WeatherTable.WEATHER_CITY)));
            }
        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
        }
        return cities;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CURRENCY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion != oldVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }
}

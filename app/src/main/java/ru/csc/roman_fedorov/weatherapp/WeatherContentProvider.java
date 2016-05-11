package ru.csc.roman_fedorov.weatherapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by roman on 24.04.2016.
 */
public class WeatherContentProvider extends ContentProvider {
    public static final String AUTHORITY = "ru.csc.roman_fedorov.weatherapp";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final int ALL = 1;
    public static final int DISTINCT_QUERY = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, "/all", ALL);
        uriMatcher.addURI(AUTHORITY, "/distinct", DISTINCT_QUERY);
    }

    private WeatherDatabaseHelper helper;


    @Override
    public boolean onCreate() {
        helper = WeatherDatabaseHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = uriMatcher.match(uri);
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(WeatherTable.TABLE_NAME);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;
        switch (match) {
            case ALL:
                builder.setDistinct(false);
                cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case DISTINCT_QUERY:
                builder.setDistinct(true);
                cursor = builder.query(db, projection, selection, selectionArgs, WeatherTable.WEATHER_CITY, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = uriMatcher.match(uri);
        String tableName;
        switch (match) {
            case ALL:
            case DISTINCT_QUERY:
                tableName = WeatherTable.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        long rowId = helper.getWritableDatabase().insert(tableName, null, values);
        Uri inserted = ContentUris.withAppendedId(uri, rowId);
        getContext().getContentResolver().notifyChange(inserted, null);
        return inserted;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        int rowsUpdated = 0;
        switch (match) {
            case ALL:
            case DISTINCT_QUERY:
                SQLiteDatabase db = helper.getWritableDatabase();
                rowsUpdated = db.update(WeatherTable.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}

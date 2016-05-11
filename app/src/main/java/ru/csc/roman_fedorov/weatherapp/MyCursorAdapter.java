package ru.csc.roman_fedorov.weatherapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by roman on 24.04.2016.
 */
public class MyCursorAdapter extends CursorAdapter {
    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView cityTV = (TextView) view.findViewById(R.id.weather_item_city);
        TextView dateTV = (TextView) view.findViewById(R.id.weather_item_date);
        TextView descriptionTV = (TextView) view.findViewById(R.id.weather_item_description);
        TextView temperaureTV = (TextView) view.findViewById(R.id.weather_item_temperature);

        cityTV.setText(cursor.getString(cursor.getColumnIndexOrThrow(WeatherTable.WEATHER_CITY)));
        dateTV.setText(cursor.getString(cursor.getColumnIndexOrThrow(WeatherTable.WEATHER_DATE)));
        descriptionTV.setText(cursor.getString(cursor.getColumnIndexOrThrow(WeatherTable.WEATHER_DESCRIPTION)));
        temperaureTV.setText(cursor.getString(cursor.getColumnIndexOrThrow(WeatherTable.WEATHER_LOW_TEMPERATURE))
                + " - " + cursor.getString(cursor.getColumnIndexOrThrow(WeatherTable.WEATHER_HIGH_TEMPERATURE)));
    }
}

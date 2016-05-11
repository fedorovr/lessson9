package ru.csc.roman_fedorov.weatherapp;

import android.provider.BaseColumns;

/**
 * Created by roman on 24.04.2016.
 */
public interface WeatherTable extends BaseColumns {
    String TABLE_NAME = "Weather";
    String WEATHER_CITY = "City";
    String WEATHER_DATE = "Date";
    String WEATHER_LOW_TEMPERATURE = "LowTemperature";
    String WEATHER_HIGH_TEMPERATURE = "HighTemperature";
    String WEATHER_DESCRIPTION = "Description";
}

package ru.csc.roman_fedorov.weatherapp;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class AutoUpdateWeatherService extends IntentService {

    public AutoUpdateWeatherService() {
        super("AutoUpdateWeatherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ForecastItem[] forecast;
        String location;
        for (String locationStr : WeatherDatabaseHelper.getInstance(this).getCities()) {
            try {
                Scanner s = new java.util.Scanner(downloadUrl(MainActivity.getQuery(locationStr))).useDelimiter("\\A");
                String json = s.hasNext() ? s.next() : "";
                JSONObject serverOutput = new JSONObject(json);
                JSONObject results = serverOutput.getJSONObject("query").getJSONObject("results");
                JSONObject item = results.getJSONObject("channel").getJSONObject("item");
                JSONObject locationObj = results.getJSONObject("channel").getJSONObject("location");
                location = locationObj.getString("city") + ", " + locationObj.getString("country");
                JSONArray jsonForecast = item.getJSONArray("forecast");
                forecast = new ForecastItem[jsonForecast.length()];
                for (int i = 0; i < jsonForecast.length(); i++) {
                    JSONObject jsonForecastItem = jsonForecast.getJSONObject(i);
                    forecast[i] = new ForecastItem(jsonForecastItem.getString("date"),
                            jsonForecastItem.getInt("high"),
                            jsonForecastItem.getInt("low"),
                            jsonForecastItem.getString("text"));
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return;
            }
            for (ForecastItem currentItem : forecast) {
                ContentValues newValues = new ContentValues();
                newValues.put(WeatherTable.WEATHER_CITY, location);
                newValues.put(WeatherTable.WEATHER_DATE, currentItem.date);
                newValues.put(WeatherTable.WEATHER_DESCRIPTION, currentItem.description);
                newValues.put(WeatherTable.WEATHER_LOW_TEMPERATURE, currentItem.lowTemperature);
                newValues.put(WeatherTable.WEATHER_HIGH_TEMPERATURE, currentItem.highTemperature);
                String mSelectionClause = WeatherTable.WEATHER_CITY + " LIKE ? AND " + WeatherTable.WEATHER_DATE + " LIKE ?";
                String[] mSelectionArgs = {location, currentItem.date};
                int rowsUpdated = getContentResolver().update(MainActivity.DISTINCT_VALUES_URI, newValues, mSelectionClause, mSelectionArgs);
                if (rowsUpdated == 0) {
                    getContentResolver().insert(MainActivity.DISTINCT_VALUES_URI, newValues);
                }
            }
        }
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(10000 /* milliseconds */);
        return conn.getInputStream();
    }
}

package ru.csc.roman_fedorov.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by roman on 11.05.2016.
 */
class DownloadWeatherData extends AsyncTask<Object, Void, ForecastItem[]> {
    private String location;
    private Context context;

    @Override
    protected ForecastItem[] doInBackground(Object... params) {
        try {
            context = (Context) params[0];
            Scanner s = new java.util.Scanner(downloadUrl((String)params[1])).useDelimiter("\\A");
            String json = s.hasNext() ? s.next() : "";
            JSONObject serverOutput = new JSONObject(json);
            JSONObject results = serverOutput.getJSONObject("query").getJSONObject("results");
            JSONObject item = results.getJSONObject("channel").getJSONObject("item");
            JSONObject location = results.getJSONObject("channel").getJSONObject("location");
            this.location = location.getString("city") + ", " + location.getString("country");
            JSONArray jsonForecast = item.getJSONArray("forecast");
            ForecastItem[] forecast = new ForecastItem[jsonForecast.length()];
            for (int i = 0; i < jsonForecast.length(); i++) {
                JSONObject jsonForecastItem = jsonForecast.getJSONObject(i);
                forecast[i] = new ForecastItem(jsonForecastItem.getString("date"),
                        jsonForecastItem.getInt("high"),
                        jsonForecastItem.getInt("low"),
                        jsonForecastItem.getString("text"));
            }
            return forecast;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(ForecastItem[] data) {
        Toast.makeText(context, "Post Execute", Toast.LENGTH_LONG).show();

        if (data == null) {
            Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        } else {
            for (ForecastItem currentItem : data) {
                ContentValues newValues = new ContentValues();
                newValues.put(WeatherTable.WEATHER_CITY, location);
                newValues.put(WeatherTable.WEATHER_DATE, currentItem.date);
                newValues.put(WeatherTable.WEATHER_DESCRIPTION, currentItem.description);
                newValues.put(WeatherTable.WEATHER_LOW_TEMPERATURE, currentItem.lowTemperature);
                newValues.put(WeatherTable.WEATHER_HIGH_TEMPERATURE, currentItem.highTemperature);
                String mSelectionClause = WeatherTable.WEATHER_CITY + " LIKE ? AND " + WeatherTable.WEATHER_DATE + " LIKE ?";
                String[] mSelectionArgs = {location, currentItem.date};
                int rowsUpdated = context.getContentResolver().update(MainActivity.DISTINCT_VALUES_URI, newValues, mSelectionClause, mSelectionArgs);
                if (rowsUpdated == 0) {
                    context.getContentResolver().insert(MainActivity.DISTINCT_VALUES_URI, newValues);
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

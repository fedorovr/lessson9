package ru.csc.roman_fedorov.weatherapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements CitiesListFragment.OnCityQuiredListener {
    public static final Uri DISTINCT_VALUES_URI = Uri.withAppendedPath(WeatherContentProvider.CONTENT_URI, "distinct");
    public static final String QUERIED_CITY = "QUERIED_CITY";

    public static String getQuery(String city) {
        try {
            return "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from" +
                    "%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from" +
                    "%20geo.places(1)%20where%20text%3D" +
                    URLEncoder.encode("\"" + city + "\"", "utf-8") +
                    ")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_placeholder_main, new CitiesListFragment());
        int screenOrientation = getResources().getConfiguration().orientation;
        if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("TAGGG", "onCreate: " + "Landscape");
            Bundle bundle = new Bundle();
            bundle.putString(MainActivity.QUERIED_CITY, "Moscow");
            CityDetailFragment cityDetailFragment = new CityDetailFragment();
            cityDetailFragment.setArguments(bundle);
            ft.add(R.id.fragment_placeholder_details, cityDetailFragment);
        }

        ft.commit();

        Intent serviceIntent = new Intent(this, AutoUpdateWeatherService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, serviceIntent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(MainActivity.ALARM_SERVICE);
        int hour = 1000 * 60 * 60;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, hour, hour, pendingIntent);
    }

    @Override
    public void onQuire(String cityName) {
        int screenOrientation = getResources().getConfiguration().orientation;
        if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            CityDetailFragment detailFragment = (CityDetailFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_placeholder_details);

            if (detailFragment != null) {
                detailFragment.displayCityDetails();
            } else {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString(MainActivity.QUERIED_CITY, "Moscow");
                CityDetailFragment cityDetailFragment = new CityDetailFragment();
                cityDetailFragment.setArguments(bundle);

                ft.replace(R.id.fragment_placeholder_details, cityDetailFragment);

                ft.commit();
            }

        } else {
            Intent intent = new Intent(this, DetailWeatherActivity.class);
            intent.putExtra(QUERIED_CITY, cityName);
            startActivity(intent);
        }
    }
}

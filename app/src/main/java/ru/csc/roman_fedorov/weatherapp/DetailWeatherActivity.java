package ru.csc.roman_fedorov.weatherapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class DetailWeatherActivity extends AppCompatActivity {
    public static final Uri CITY_VALUES_URI = Uri.withAppendedPath(WeatherContentProvider.CONTENT_URI, "all");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_weather);

        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.QUERIED_CITY, getIntent().getStringExtra(MainActivity.QUERIED_CITY));
        CityDetailFragment cityDetailFragment = new CityDetailFragment();
        cityDetailFragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder_details, cityDetailFragment);
        ft.commit();
    }
}

package ru.csc.roman_fedorov.weatherapp;

/**
 * Created by roman on 24.04.2016.
 */
public class ForecastItem {
    public String date;
    public int highTemperature;
    public int lowTemperature;
    public String description;

    public ForecastItem(String date, int highTemperature, int lowTemperature, String description) {
        this.date = date;
        this.highTemperature = highTemperature;
        this.lowTemperature = lowTemperature;
        this.description = description;
    }
}

package com.example.kareemkanaan.cardview2;

/**
 * Created by ASUS on 4/15/2017.
 */

public class City {
    private static final String country="Lebanon"; // for now the application is only for lebanease community
    private String cityName;
    private double  Latitude;
    private double Longitude;

    public City(String cityName, double latitude, double longitude) {
        this.cityName = cityName;
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public double getLatitude() {
        return Latitude;
    }

    public static String getCountry() {
        return country;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}

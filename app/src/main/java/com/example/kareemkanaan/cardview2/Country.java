package com.example.kareemkanaan.cardview2;

/**
 * Created by ASUS on 4/15/2017.
 */

public class Country {
    private   City city;
    private static final String countryName="Lebanon"; // for not the application is only for lebanease community

    public Country(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCity(City c) {
        this.city = c;
    }
}

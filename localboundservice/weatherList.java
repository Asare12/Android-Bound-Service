package com.example.asare.localboundservice;

/**
 * Created by Asare on 09/03/2018.
 */

public class weatherList {

    private String weatherID;
    private String weatherDesc;

    public weatherList(String weatherID, String weatherDesc){

        this.weatherID = weatherID;
        this.weatherDesc = weatherDesc;
    }

    public String getWeatherID() {
        return weatherID;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }
}

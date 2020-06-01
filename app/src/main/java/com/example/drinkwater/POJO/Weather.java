package com.example.drinkwater.POJO;

public class Weather
{
    private int weather_id;//код погоды
    private int day_id;
    private int weather_humidity;
    private int weather_temperature;

    public Weather(int weather_id, int day_id, int weather_humidity, int weather_temperature) {
        this.weather_id = weather_id;
        this.day_id = day_id;
        this.weather_humidity = weather_humidity;
        this.weather_temperature = weather_temperature;
    }

    public int getWeather_id() {
        return weather_id;
    }

    public int getDay_id() {
        return day_id;
    }

    public int getWeather_humidity() {
        return weather_humidity;
    }

    public void setWeather_humidity(int weather_humidity) {
        this.weather_humidity = weather_humidity;
    }

    public int getWeather_temperature() {
        return weather_temperature;
    }

    public void setWeather_temperature(int weather_temperature) {
        this.weather_temperature = weather_temperature;
    }
}

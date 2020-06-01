package com.example.drinkwater.POJO;

public class WaterConsumption
{
    private long consumption_time;
    private int consumption_volume;
    private int consumption_count;

    public int getConsumption_count() {
        return consumption_count;
    }

    public void setConsumption_count(int consumption_count) {
        this.consumption_count = consumption_count;
    }

    private int day_id;
    private String liquid_name;

    public WaterConsumption(long consumption_time, int consumption_volume, int day_id, String liquid_name)
    {
        this.consumption_time = consumption_time;
        this.consumption_volume = consumption_volume;
        this.day_id = day_id;
        this.liquid_name = liquid_name;
    }

    public WaterConsumption(int consumption_volume, int consumption_count, String liquid_name)//для статистики по выпитым жидкостям
    {
        this.consumption_volume = consumption_volume;
        this.consumption_count = consumption_count;
        this.liquid_name = liquid_name;
    }

    public WaterConsumption(long consumption_time, int consumption_volume, String liquid_name) {
        this.consumption_time = consumption_time;
        this.consumption_volume = consumption_volume;
        this.liquid_name = liquid_name;
    }

    public long getConsumption_time() {
        return consumption_time;
    }

    public void setConsumption_time(long consumption_time) {
        this.consumption_time = consumption_time;
    }

    public int getConsumption_volume() {
        return consumption_volume;
    }

    public void setConsumption_volume(int consumption_volume) {
        this.consumption_volume = consumption_volume;
    }

    public int getDay_id() {
        return day_id;
    }

    public void setDay_id(int day_id) {
        this.day_id = day_id;
    }

    public String getLiquid_name() {
        return liquid_name;
    }

    public void setLiquid_name(String liquid_name) {
        this.liquid_name = liquid_name;
    }
}

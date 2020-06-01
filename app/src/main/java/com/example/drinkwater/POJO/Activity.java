package com.example.drinkwater.POJO;

public class Activity
{
    private int activity_id;//код активности
    private int day_id;//код дня
    private int activity_duration_hours;//Длительность активности в часах

    public Activity(int activity_id, int day_id, int activity_duration_hours)
    {
        this.activity_id = activity_id;
        this.day_id = day_id;
        this.activity_duration_hours = activity_duration_hours;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public int getDay_id() {
        return day_id;
    }

    public int getActivity_duration_hours() {
        return activity_duration_hours;
    }

    public void setActivity_duration_hours(int activity_duration_hours) {
        this.activity_duration_hours = activity_duration_hours;
    }
}

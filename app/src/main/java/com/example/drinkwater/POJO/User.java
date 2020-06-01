package com.example.drinkwater.POJO;

public class User
{
    private int user_id;
    private String user_name;
    private int weight;

    public User(int user_id, String user_name, int weight) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.weight = weight;
    }

    public int getUser_id()
    {
        return user_id;
    }

    public String getUser_name()
    {
        return user_name;
    }

    public int getWeight()
    {
        return weight;
    }
}

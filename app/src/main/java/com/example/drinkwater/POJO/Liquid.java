package com.example.drinkwater.POJO;

public class Liquid
{
    private String liquidName;//Название жидкости
    private double liquidRatio;//Коээффициент заполнения шкалы(-1...1)
    private int userId;//Пользователь, добавивший жидкость.TODO: добавить userId NULL в таблицу liquid

    public Liquid(String liquidName, double liquidRatio, int userId) //Пользовательская жидкость
    {
        this.liquidName = liquidName;
        this.liquidRatio = liquidRatio;
        this.userId = userId;
    }

    public Liquid(String liquidName, double liquidRatio) //Стандартная жидкость
    {
        this.liquidName = liquidName;
        this.liquidRatio = liquidRatio;
    }

    public String getLiquidName() {
        return liquidName;
    }

    public void setLiquidName(String liquidName) {
        this.liquidName = liquidName;
    }

    public double getLiquidRatio() {
        return liquidRatio;
    }

    public void setLiquidRatio(int liquidRatio) {
        this.liquidRatio = liquidRatio;
    }

    public int getUserId()
    {
        if(userId!= 0)
            return userId;
        else
            return 0;
    }

}

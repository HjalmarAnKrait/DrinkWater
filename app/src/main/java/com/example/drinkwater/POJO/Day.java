package com.example.drinkwater.POJO;

public class Day//Убраны сеттеры для userId и userName. Только чтение. Только хардкор.
{
    private int dayId;//id_дня
    private long dayDate;//Дата потребления в UNIX-epoch формате. Только день используется
    private int dayDrunkSumm;//Сумма выпитой воды. Считается на основании water_consumtion
    private int userId;//id пользователя
    private String userName;//Имя пользователя

    public Day(int dayId, long dayDate, int dayDrunkSumm, int userId, String userName)
    {
        this.dayId = dayId;
        this.dayDate = dayDate;
        this.dayDrunkSumm = dayDrunkSumm;
        this.userId = userId;
        this.userName = userName;
    }

    public void setDayDate(long dayDate) {
        this.dayDate = dayDate;
    }

    public void setDayDrunkSumm(int dayDrunkSumm) {
        this.dayDrunkSumm = dayDrunkSumm;
    }

    public int getDayId() {
        return dayId;
    }

    public long getDayDate() {
        return dayDate;
    }

    public int getDayDrunkSumm() {
        return dayDrunkSumm;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
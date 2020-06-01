package com.example.drinkwater.MVP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.drinkwater.DatabaseHelper;
import com.example.drinkwater.POJO.Liquid;
import com.example.drinkwater.POJO.WaterConsumption;

import java.io.IOException;
import java.util.ArrayList;

public class Repository implements MainContract.Repository
{
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Context context;

    public Repository(Context context)
    {
        this.context = context;
        dbInit();
    }

    @Override
    public boolean addUser(String userName, String password, int weight)
    {
        @SuppressLint("DefaultLocale")
        String SQL = String.format("INSERT INTO user (user_name, user_password, weight) VALUES('%s', '%s', %d)", userName, password, weight);
        if(isNameUnique(userName))
        {
            db.execSQL(SQL);
            Log.e("addUser - " + userName, "successAdded");
            return true;
        }
        else
        {
            return false;
        }

    }

    @Override
    public void addActivity(int duration)
    {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int dayId = preferences.getInt("dayId", 0);
        @SuppressLint("DefaultLocale")
        String SQL = String.format("INSERT INTO activity (activity_duration_hours, day_id) VALUES('%d', %d)", duration, dayId);
        db.execSQL(SQL);
    }

    @Override
    public boolean isNameUnique(String name)
    {
        String SQL = String.format("SELECT user_name FROM user WHERE user_name = '%s'", name);
        Cursor cursor = db.rawQuery(SQL, null);
        if(cursor.isAfterLast())
        {
            cursor.close();
            return true;
        }
        else
        {

            cursor.close();
            return false;
        }
    }

    @Override
    public boolean addLiquid(String liquidName, int liquidRatio)
    {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);
        @SuppressLint("DefaultLocale")
        String SQL = String.format("INSERT INTO liquid (liquid_name, liquid_ratio, liquid_user_id) VALUES('%s', %d, %d)", liquidName, liquidRatio, userId);
        db.execSQL(SQL);
        return false;
    }


    @Override
    public boolean addDay(Long day_date, int dat_drank_summ)
    {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);
        @SuppressLint("DefaultLocale")
        String SQL = String.format("INSERT INTO day (day_date, day_drank_summ, user_id) VALUES('%d', %d, %d)", day_date,
                dat_drank_summ, userId);
        db.execSQL(SQL);
        return false;
    }

    @Override
    public String getUserNameById(int userId)
    {
        String userName = "NULL";
        String SQL = String.format("SELECT user_name FROM USER WHERE user_id = %d", userId);
        Cursor cursor = db.rawQuery(SQL, null);
        if(cursor.moveToFirst())
        {
            Log.e("432", "founded");
            userName = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();
        return userName;
    }

    @Override
    public String getLiquidNameById(int liquidId) {
        return null;
    }

    @Override
    public int getUserIdByUserName(String userName)
    {
        int id = 0;
        String SQL = String.format("SELECT user_id FROM USER WHERE user_name LIKE '%s'", userName);
        Cursor cursor = db.rawQuery(SQL, null);
        if(cursor.moveToFirst())
        {
            id = cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();
        return id;
    }

    @Override
    public ArrayList<Liquid> getLiquidList()
    {
        ArrayList<Liquid> list = new ArrayList();
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);

        @SuppressLint("DefaultLocale")
        String SQL = String.format("SELECT liquid_name, liquid_ratio, liquid_user_id FROM liquid WHERE liquid_user_id IS NULL " +
                "UNION" +
                " SELECT liquid_name, liquid_ratio, liquid_user_id FROM liquid WHERE liquid_user_id = %d" +
                " ORDER by liquid_user_id;", userId);

        Cursor cursor = db.rawQuery(SQL, null);
        cursor.moveToFirst();
        do
        {
            list.add(new Liquid(cursor.getString(0), cursor.getDouble(1), cursor.getInt(2))) ;
        }while (cursor.moveToNext());
        cursor.close();
        return list;
    }

    @Override
    public ArrayList<WaterConsumption> getWaterConsumptionList(int dayId)//Получение списка потреблений за день
    {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);
        ArrayList<WaterConsumption> consumptions = new ArrayList();


        @SuppressLint("DefaultLocale")
        String SQL = String.format("Select liquid.liquid_name,\n" +// FIXME: 31.05.2020 йоба, да тут не правильный запрос
                "count(waterConsumption.liquid_id), sum(consumption_volume)\n" +
                "from waterConsumption\n" +
                "JOIN day on waterConsumption.day_id = day.day_id\n" +
                "JOIN liquid on waterConsumption.liquid_id = liquid.liquid_id\n" +
                "WHERE day.user_id = %d and day.day_id = %d\n" +
                "GROUP by waterConsumption.liquid_id\n ORDER BY waterConsumption.consuption_time asc", userId, dayId);

        Cursor cursor = db.rawQuery(SQL, null);
        cursor.moveToFirst();
        do
        {
            WaterConsumption  waterConsumption =
                    new WaterConsumption( cursor.getInt(2), cursor.getInt(1),cursor.getString(0));
            consumptions.add(waterConsumption) ;
        }while (cursor.moveToNext());
        cursor.close();

        return consumptions;
    }

    @Override
    public WaterConsumption getLastConsumption(int dayId)
    {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);
        dayId = preferences.getInt("dayId", 0);
        ArrayList<WaterConsumption> consumptions = new ArrayList();
        WaterConsumption  waterConsumption;

        @SuppressLint("DefaultLocale")
        String SQL = String.format("Select liquid.liquid_name,\n" +
                "waterConsumption.consuption_time, waterConsumption.consumption_volume\n" +
                "from waterConsumption\n" +
                "JOIN day on waterConsumption.day_id = day.day_id\n" +
                "JOIN liquid on waterConsumption.liquid_id = liquid.liquid_id\n" +
                "WHERE day.user_id = %d and day.day_id = %d\n" +
                "ORDER BY waterConsumption.consuption_time asc", userId, dayId);

        Cursor cursor = db.rawQuery(SQL, null);
        if(cursor.moveToLast())
        {
            waterConsumption = new WaterConsumption(cursor.getInt(2),cursor.getInt(1), cursor.getString(0));
            Log.e("432", "moveToFirst" + waterConsumption.getConsumption_volume()+
                    " user" + getUserNameById(userId) + " day:" + dayId);
        }
        else
        {
            waterConsumption = new WaterConsumption((long)0,0, "NULL");
            Log.e("432", "moveToFirst" + waterConsumption.getConsumption_volume()+
                    " user" + getUserNameById(userId) + " day:" + dayId);
        }

        return waterConsumption;
    }

    @Override
    public ArrayList<WaterConsumption> getWaterConsumptionList()//статистика по выпитым жидкостям.
    {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);
        ArrayList<WaterConsumption> consumptions = new ArrayList();


        @SuppressLint("DefaultLocale")
        String SQL = String.format("Select liquid.liquid_name,\n" +
                "count(waterConsumption.liquid_id), sum(consumption_volume)\n" +
                "from waterConsumption\n" +
                "JOIN day on waterConsumption.day_id = day.day_id\n" +
                "JOIN liquid on waterConsumption.liquid_id = liquid.liquid_id\n" +
                "WHERE day.user_id = %d\n" +
                "GROUP by waterConsumption.liquid_id", userId);

        Cursor cursor = db.rawQuery(SQL, null);
        cursor.moveToFirst();
        do
        {
            WaterConsumption  waterConsumption =
                    new WaterConsumption( cursor.getInt(2), cursor.getInt(1),cursor.getString(0));
            consumptions.add(waterConsumption) ;
        }while (cursor.moveToNext());
        cursor.close();

        return consumptions;
    }

    @Override
    public void addWaterConsumption(long time, int volume, int day_id, int liquid_id)
    {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int dayId = preferences.getInt("dayId", 0);
        @SuppressLint("DefaultLocale")
        String SQL = String.format("INSERT INTO waterConsumption(consuption_time, consumption_volume, day_id, liquid_id)" +
                " VALUES(%d, %d, %d, %d)", time, volume, day_id, liquid_id);
        db.execSQL(SQL);
    }


    @Override
    public int getDayIdByDate(long date)
    {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);
        int dayId = 0;

        Log.e("dayIdByDate " + date, getStringDateByLong(date));

        @SuppressLint("DefaultLocale")
        String SQL = String.format("SELECT \n" +
                "day.day_id\n" +
                "FROM day\n" +
                "where DATE(%d, 'unixepoch') = DATE(day.day_date, 'unixepoch') and user_id = %d", date, userId);

        Cursor cursor = db.rawQuery(SQL, null);

        if(!cursor.moveToFirst())
        {
            cursor.close();
            return dayId;
        }
        else
        {
            dayId =  cursor.getInt(0);
            cursor.close();
            return dayId;
        }

    }

    @Override
    public String getStringDateByLong(long date)
    {
        String SQL = String.format("SELECT DATE(%d,'unixepoch')", date);
        String stringDate = "";
        Cursor cursor = db.rawQuery(SQL, null);
        cursor.moveToFirst();
        stringDate = cursor.getString(0);
        cursor.close();
        return stringDate;
    }

    @Override
    public boolean checkAuth(String userName, String password)
    {
        String SQL = String.format("SELECT * FROM user WHERE user_name = '%s' AND user_password = '%s'", userName, password);
        Cursor cursor = db.rawQuery(SQL, null);
        if(!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }
        else
        {
            cursor.close();
            return true;
        }
    }

    @Override
    public void addWaterToDay(int volume)
    {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int dayId = preferences.getInt("dayId", 0);

        @SuppressLint("DefaultLocale")
        String SQL = String.format("UPDATE\n" +
                " day set day_drank_summ = %d \n" +
                " where day.day_id = %d", volume + getDayDrunkSumm(), dayId);
        db.execSQL(SQL);
    }



    @Override
    public int getDayDrunkSumm()
    {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int dayId = preferences.getInt("dayId", 0);
        @SuppressLint("DefaultLocale")
        String SQL = String.format("SELECT day_drank_summ FROM day where day_id = %d", dayId);

        Cursor cursor = db.rawQuery(SQL, null);
        if(!cursor.moveToFirst())
        {
            return 0;
        }
        else
        {
            return cursor.getInt(0);
        }

    }

    @Override
    public int summaryDrunked(int dayId)
    {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);
        int summ = 0;

        @SuppressLint("DefaultLocale")
        String SQL = String.format("SELECT \n" +
                "sum(waterConsumption.consumption_volume)\n" +
                "FROM waterConsumption\n" +
                "JOIN liquid on liquid.liquid_id = waterConsumption.liquid_id\n" +
                "JOIN day on day.day_id = waterConsumption.day_id\n" +
                "where day.user_id = %d AND day.day_id = %d",userId, dayId);
        Cursor cursor = db.rawQuery(SQL, null);
        if(cursor.moveToFirst())
        {
            summ = cursor.getInt(0);
        }
        return summ;
    }

    @Override
    public int summaryDrunked() {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);
        int summ = 0;

        @SuppressLint("DefaultLocale")
        String SQL = String.format("SELECT \n" +
                "sum(waterConsumption.consumption_volume)\n" +
                "FROM waterConsumption\n" +
                "JOIN liquid on liquid.liquid_id = waterConsumption.liquid_id\n" +
                "JOIN day on day.day_id = waterConsumption.day_id\n" +
                "where day.user_id = %d",userId);

        Cursor cursor = db.rawQuery(SQL, null);
        if(cursor.moveToFirst())
        {
            summ = cursor.getInt(0);
        }
        return summ;
    }




    private void dbInit()
    {
        this.databaseHelper = new DatabaseHelper(context);
        try
        {
            this.databaseHelper.updateDataBase();
        }
        catch (IOException e)
        {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            this.db = databaseHelper.getWritableDatabase();
        }
        catch (SQLException e)
        {
            throw e;
        }
    }

    @Override
    public void closeDb()
    {
        databaseHelper.close();
    }

    @Override
    public boolean isDayCreated(long date)
    {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);
        String SQL = String.format("SELECT day_date FROM day WHERE DATE(day.day_date, 'unixepoch') = DATE(%d, 'unixepoch') AND " +
                "day.user_id = %d", date, userId);
        Cursor cursor = db.rawQuery(SQL, null);
        if(cursor.isAfterLast())
        {
            cursor.close();
            return true;
        }
        else
        {
            cursor.close();
            return false;
        }
    }

    @Override
    public int getLiquidIdByName(String name)
    {
        int id = 0;
        String SQL = String.format("SELECT liquid_id FROM liquid WHERE liquid_name LIKE '%s'", name);
        Cursor cursor = db.rawQuery(SQL, null);
        if(cursor.moveToFirst())
        {
            id = cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();
        Log.e("liquid by name", "" + id);
        return id;
    }

    @Override
    public Liquid getLiquidById(int liquidId)
    {
        Liquid liquid;
        String SQL = String.format("SELECT liquid_name, liquid_ratio FROM liquid WHERE liquid_id = %d", liquidId);
        Cursor cursor = db.rawQuery(SQL, null);
        cursor.moveToFirst();
        liquid = new Liquid(cursor.getString(0), cursor.getDouble(1));

        return liquid;
    }

    @Override
    public int getActivityDuration()
    {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int dayId = preferences.getInt("dayId", 0);
        String SQL = String.format("SELECT activity_duration_hours FROM activity WHERE day_id = %d", dayId);
        Cursor cursor = db.rawQuery(SQL, null);
        if(cursor.moveToFirst())
        return cursor.getInt(0);
        else
            return 1;


    }

    @Override
    public int getUserWeight()
    {
        SharedPreferences preferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);
        String SQL = String.format("Select weight from user where user_id = %d", userId);
        Cursor cursor = db.rawQuery(SQL, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

}

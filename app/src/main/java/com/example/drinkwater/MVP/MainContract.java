package com.example.drinkwater.MVP;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.drinkwater.POJO.Activity;
import com.example.drinkwater.POJO.Day;
import com.example.drinkwater.POJO.Liquid;
import com.example.drinkwater.POJO.WaterConsumption;
import com.filippudak.ProgressPieView.ProgressPieView;

import java.util.ArrayList;

public interface MainContract
{
    interface Repository//Работа с базой данных
    {
        boolean addUser(String userName, String password, int weight);//Добавление пользователя в БД TODO: Сделать авторизацию локальной. Добавить пароль в таблицу user
        boolean addLiquid(String liquidName, int liquidRatio);//Добавление жидкости для всех пользователей(Администратор)
        boolean addDay(Long day_date, int dat_drank_summ);//Добавление дня
        String getUserNameById(int userId);//Получение имени пользователя через его id. TODO: Сделать проверку на пустоту. "NULL" если ничего не нашлось
        String getLiquidNameById(int liquidId);//Получение жидкости по её id
        int getUserIdByUserName(String userName);//Получение id пользователя через его имя. TODO: Сделать проверку на пустоту. 0 если ничего не нашлось.
        ArrayList<Liquid> getLiquidList();//получение всех жидкостей
        ArrayList<WaterConsumption> getWaterConsumptionList(int dayId);//Потребление за определённый день
        ArrayList<WaterConsumption> getWaterConsumptionList();//Статистика по выпитым жидкостям
        void addWaterConsumption(long time, int volume, int day_id, int liquid_id);
        WaterConsumption getLastConsumption(int dayId);
        int getDayIdByDate (long date);//Получение дня по дате
        int getDayDrunkSumm();
        String getStringDateByLong(long date);//Получение даты через long
        boolean checkAuth(String userName, String password);
        void addWaterToDay(int volume);
        boolean isNameUnique(String name);
        int summaryDrunked(int dayId);
        int summaryDrunked();
        void addActivity(int duration);
        public void closeDb();
        boolean isDayCreated(long date);
        int getLiquidIdByName(String name);
        Liquid getLiquidById(int liquidId);
        int getActivityDuration();
        int getUserWeight();


    }

    interface FirstStepPresenter
    {
        void onButtonWasClicked(int viewId);//Обработка нажатия на кнопку
        void onCreate(String className);
        void getText(String text);
        void getContext(Context context);
        void spinnerState(String item);
        void onDestroy(String className);
        String getStringFromResource(Context context, int id);
    }

    interface SecondStepPresenter
    {
        void onButtonWasClicked(int viewId);//Обработка нажатия на кнопку
        void onCreate(String className);
        void getText(EditText editText);
        void getContext(Context context);
        void onDestroy(String className);
        String getStringFromResource(Context context, int id);
    }

    interface RegAuthPresenter
    {
        void onButtonWasClicked(int viewId);//Обработка нажатия на кнопку
        void onCreate(String className);
        void onDestroy(String className);
        void getText(EditText editText);
        void getContext(Context context);
        void getToggleButtonState(boolean state);
        String getStringFromResource(Context context, int id);
    }

    interface MainPresenter
    {
        void onButtonWasClicked(int viewId);//Обработка нажатия на кнопку
        void onCreate(String className);
        void onDestroy(String className);
        void getText(EditText editText);
        void getContext(Context context);
        void onProgressChanged(int progress);
        void getDialogView(android.view.View view);
        void getDialog(Dialog dialog);
        void getActivityDialogView(android.view.View view);
        void getActivityDialog(Dialog dialog);
        String getStringFromResource(Context context, int id);
        void getProgressPieView(ProgressPieView progressPieView);
    }

    interface View
    {
        default void  showToast(String message)
        {
            Toast.makeText((Context)this, message, Toast.LENGTH_SHORT).show();
        }//Показывание тоста
        void finishActivity();
        void setText(String text, int textViewId); //установка текста в поле
    }

    interface ViewExtedned extends View
    {
        void showDialog();
        void showActivityDialog();
    }
}

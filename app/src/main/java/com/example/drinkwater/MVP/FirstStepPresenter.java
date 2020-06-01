package com.example.drinkwater.MVP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Spinner;

import com.example.drinkwater.Activities.LaunchActivities.SecondStepActivity;
import com.example.drinkwater.R;

public class FirstStepPresenter implements MainContract.FirstStepPresenter
{
    private MainContract.View view;
    private SharedPreferences preferences;
    private SharedPreferences servicePreferences;
    private String scaleType;
    private int weight;
    private Context context;

    public FirstStepPresenter(MainContract.View view)//Конструктор:)
    {
        this.view = view;
    }


    @Override
    public void onButtonWasClicked(int viewId)
    {
        if(!(Integer.valueOf(weight) > 0))
        {
            view.showToast(getStringFromResource(context, R.string.weight_error));
        }
        else
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("scaleType", scaleType);
            if(scaleType.equals("lb"))
            {
                editor.putString("volumeType", "oz");
            }
            else
            {
                editor.putString("volumeType", "ml");
            }
            editor.apply();
            editor = servicePreferences.edit();
            editor.putInt("weight", weight).apply();
            context.startActivity(new Intent(context, SecondStepActivity.class));
            view.finishActivity();
        }
    }

    @Override
    public void onCreate(String clName)
    {
        preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        servicePreferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        Log.e("onCreatePresenter", clName);
    }

    @Override
    public void getText(String text)
    {
        //Log.e("getText", text);
        if (!text.isEmpty()){
            weight = Integer.parseInt(text);
        }
        else
        {
            weight = 0;
        }

    }

    @Override
    public void getContext(Context context)
    {
        this.context = context;
    }

    @Override
    public void spinnerState(String item)
    {
        Log.e("spinnerSelect", item);
        scaleType = item;
    }


    @Override
    public void onDestroy(String className)
    {
        Log.e("onDestroyPresenter", className);
    }

    @Override
    public String getStringFromResource(Context context, int id)
    {
        return context.getResources().getString(id);
    }


}

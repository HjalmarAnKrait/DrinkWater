package com.example.drinkwater.Activities.LaunchActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drinkwater.Activities.MainActivity;
import com.example.drinkwater.R;

public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getSharedPreferences("service", Context.MODE_PRIVATE).getBoolean("isFirstLaunch", true))
        {
            this.getSharedPreferences("service", Context.MODE_PRIVATE).edit().putBoolean("isFirstLaunch", false).apply();
            startActivity(new Intent(this, FirstStepActivity.class));
            finish();
        }
        else
        {
            startActivity(new Intent(this, RegAuthActivity.class));
            finish();
        }

    }
}

package com.example.drinkwater.MVP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;

import com.example.drinkwater.Activities.LaunchActivities.RegAuthActivity;
import com.example.drinkwater.Activities.LaunchActivities.SecondStepActivity;
import com.example.drinkwater.POJO.Activity;
import com.example.drinkwater.POJO.Liquid;
import com.example.drinkwater.R;

import java.util.ArrayList;
import java.util.List;

public class SecondStepPresenter implements MainContract.SecondStepPresenter
{
    private MainContract.View view;
    private MainContract.Repository repository;
    private SharedPreferences servicePreferences;
    private String scaleType;
    private int weight;
    private Context context;
    private String userName, password, passwordAccept;

    public SecondStepPresenter(MainContract.View view)//Конструктор:)
    {
        this.view = view;
    }



    @Override
    public void onButtonWasClicked(int viewId)
    {
        switch (viewId)
        {
            case R.id.nextButton:
                addUser(userName, password, passwordAccept, weight);
                break;
            default:
                context.startActivity(new Intent(context, RegAuthActivity.class));
                view.finishActivity();
                break;
        }
    }

    @Override
    public void onCreate(String clName)
    {
        servicePreferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        weight = servicePreferences.getInt("weight", 0);

    }

    @Override
    public void getText(EditText editText) {
        String str = editText.getText().toString();
        switch (editText.getId())
        {
            case R.id.usernameEditText:
                userName = str;
                Log.e("username", str);
                break;
            case R.id.passwordEditText:
                password = str;
                break;
            case R.id.passwordAcceptEditText:
                passwordAccept = str;
                break;

        }
    }


    @Override
    public void getContext(Context context)
    {
        this.context = context;
        this.repository = new Repository(context);
    }

    @Override
    public void onDestroy(String className)
    {
        Log.e("onDestroyPresenter", className);
        repository.closeDb();
    }

    @Override
    public String getStringFromResource(Context context, int id)
    {
        return context.getResources().getString(id);
    }

    private void addUser(String userName, String password, String passwordAccept, int weight)
    {
        String[] matches = new String[] {".", "/", "&", "#", "!", "%", "^", "*", "(", ")", "+", "=", ","};
        boolean isCorrect = true;

        if(password.isEmpty() || userName.isEmpty())
        {
            view.showToast(context.getResources().getString(R.string.empty_fields_msg));
            isCorrect = false;
        }
        else if(!password.equals(passwordAccept))
        {
            view.showToast(context.getResources().getString(R.string.passwords_are_not_equal));
            isCorrect = false;
        }
        else
        {
            for(String s : matches)
            {
                if(userName.contains(s) || password.contains(s))
                {
                    Log.e("432", "lol");
                    isCorrect = false;
                }
            }
            if(!isCorrect)
            {
                view.showToast(context.getResources().getString(R.string.pass_restricted_symbols_msg));
            }
        }

        if(isCorrect)
        {
            if(repository.addUser(userName, password, weight))
            {
                view.showToast(context.getResources().getString(R.string.success));
                context.startActivity(new Intent(context, RegAuthActivity.class));
                context.getSharedPreferences("service", Context.MODE_PRIVATE).edit().putBoolean("isFirstLaunch", false).apply();
                view.finishActivity();
            }
            else
            {
                view.showToast(context.getResources().getString(R.string.username_exist_msg));
            }
        }

    }

}

package com.example.drinkwater.MVP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;

import com.example.drinkwater.Activities.LaunchActivities.SplashActivity;
import com.example.drinkwater.Activities.MainActivity;
import com.example.drinkwater.R;

public class RegAuthPresenter implements MainContract.RegAuthPresenter
{
    private MainContract.View view;
    private MainContract.Repository repository;
    private SharedPreferences servicePreferences;
    private Context context;
    private String userName, password;
    private boolean toggleButtonState;

    public RegAuthPresenter(MainContract.View view)//Конструктор:)
    {
        this.view = view;
    }

    @Override
    public void onButtonWasClicked(int viewId)
    {
        if(viewId == R.id.nextButton)
        {
            if(repository.checkAuth(userName, password))
            {
                if(toggleButtonState)
                {
                    servicePreferences.edit().putString("userName", userName).apply();
                    servicePreferences.edit().putString("password", password).apply();
                }
                view.showToast(context.getResources().getString(R.string.success));
                context.startActivity(new Intent(context, MainActivity.class));
                servicePreferences.edit().putInt("userId", repository.getUserIdByUserName(userName)).apply();
                servicePreferences.edit().putString("userName", userName).apply();
                Log.e("43231", " " +servicePreferences.getInt("userId", 0));
                view.finishActivity();
            }
            else
            {
                view.showToast(context.getResources().getString(R.string.invalid_log_pass_msg));
            }
        }
        else if(viewId == R.id.createNewAccount)
        {
            servicePreferences.edit().remove("isFirstLaunch").apply();
            context.startActivity(new Intent(context, SplashActivity.class));
            view.finishActivity();
        }
    }

    @Override
    public void onCreate(String className)
    {
        servicePreferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        repository = new Repository(context);
        if(!servicePreferences.getString("userName", "no").equals("no"))
        {
            String uName, pass;
            uName = servicePreferences.getString("userName", "no");
            pass = servicePreferences.getString("password", "no");
            repository.checkAuth(uName, pass);
            context.startActivity(new Intent(context, MainActivity.class));
            servicePreferences.edit().putInt("userId", repository.getUserIdByUserName(uName)).apply();
            Log.e("432", "kek");
            view.finishActivity();
        }
    }

    @Override
    public void onDestroy(String className)
    {
        Log.e("onDestroyPresenter", className);
        repository.closeDb();
    }

    @Override
    public void getText(EditText editText)
    {
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
        }
    }

    @Override
    public void getContext(Context context)
    {
        this.context = context;
    }

    @Override
    public void getToggleButtonState(boolean state)
    {
        toggleButtonState = state;
    }

    @Override
    public String getStringFromResource(Context context, int id)
    {
        return context.getResources().getString(id);
    }
}

package com.example.drinkwater.Activities.LaunchActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drinkwater.MVP.MainContract;
import com.example.drinkwater.MVP.Repository;
import com.example.drinkwater.MVP.SecondStepPresenter;
import com.example.drinkwater.R;

public class SecondStepActivity extends AppCompatActivity implements MainContract.View
{

    private EditText name, password, passwordAccept;
    private MainContract.SecondStepPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_step);
        init();
        presenter.getContext(this);
        presenter.onCreate(this.getLocalClassName());
    }

    @Override
    public void showToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setText(String text, int textViewId)
    {
        TextView textView = findViewById(textViewId);
        textView.setText(text);
    }


    @Override
    public void finishActivity() {
        finish();
    }

    public void onClick(View view)
    {
        presenter.getText(name);
        presenter.getText(password);
        presenter.getText(passwordAccept);
        presenter.onButtonWasClicked(view.getId());
    }

    void init()
    {
        name = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        passwordAccept = findViewById(R.id.passwordAcceptEditText);
        presenter = new SecondStepPresenter(this);
    }

    @Override
    protected void onDestroy()
    {
        presenter.onDestroy(this.getLocalClassName());
        super.onDestroy();
    }
}

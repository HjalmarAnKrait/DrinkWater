package com.example.drinkwater.Activities.LaunchActivities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.example.drinkwater.MVP.MainContract;
import com.example.drinkwater.MVP.RegAuthPresenter;
import com.example.drinkwater.MVP.SecondStepPresenter;
import com.example.drinkwater.R;
import com.google.android.material.snackbar.Snackbar;

public class RegAuthActivity extends AppCompatActivity implements MainContract.View
{


    private EditText name, password;
    private MainContract.RegAuthPresenter presenter;
    private ToggleButton toggleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_auth);
        init();
        presenter.getContext(this);
        presenter.onCreate(this.getLocalClassName());

        presenter.getToggleButtonState(false);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    presenter.getToggleButtonState(true);
                }
            }
        });
    }


    @Override
    public void setText(String text, int textViewId)
    {

    }

    @Override
    public void finishActivity()
    {
        finish();
    }

    public void init()
    {
        presenter = new RegAuthPresenter(this);
        name = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        toggleButton = findViewById(R.id.toggleButton);
    }

    public void onClick(View view)
    {
        presenter.getText(name);
        presenter.getText(password);
        presenter.onButtonWasClicked(view.getId());
    }

    @Override
    protected void onDestroy()
    {
        presenter.onDestroy(this.getLocalClassName());
        super.onDestroy();
    }
}

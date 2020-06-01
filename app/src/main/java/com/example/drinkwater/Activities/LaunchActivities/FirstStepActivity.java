package com.example.drinkwater.Activities.LaunchActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drinkwater.MVP.MainContract;
import com.example.drinkwater.MVP.FirstStepPresenter;
import com.example.drinkwater.R;

public class FirstStepActivity extends AppCompatActivity implements MainContract.View
{

    private MainContract.FirstStepPresenter firstStepPresenter;
    private Spinner spinner;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setup);
        init();
        firstStepPresenter.getContext(this);
        firstStepPresenter.onCreate(this.getLocalClassName());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                firstStepPresenter.spinnerState(spinner.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                firstStepPresenter.spinnerState(spinner.getPrompt().toString());
            }
        });
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
        firstStepPresenter.getText(editText.getText().toString());
        firstStepPresenter.onButtonWasClicked(view.getId());
    }

    void init()
    {
        spinner = findViewById(R.id.spinner);
        editText = findViewById(R.id.weightEditText1);
        firstStepPresenter = new FirstStepPresenter(this);
    }
}

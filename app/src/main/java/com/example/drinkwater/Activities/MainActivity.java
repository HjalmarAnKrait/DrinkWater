package com.example.drinkwater.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.drinkwater.MVP.MainContract;
import com.example.drinkwater.MVP.MainPresenter;
import com.example.drinkwater.R;
import com.filippudak.ProgressPieView.ProgressPieView;
import com.google.android.material.snackbar.Snackbar;
import com.lukelorusso.verticalseekbar.VerticalSeekBar;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity implements MainContract.ViewExtedned
{
    private MainContract.MainPresenter presenter;
    private ProgressPieView progressPieView;
    private View.OnClickListener onClickListener;

    private VerticalSeekBar dialogSeekBar;
    private TextView dialogLiquidVolume, dialogLiquidType, dialogLiquidTotalSum;
    private Button dialogCancelButton, dialogAddButton;

    private Button dialogActivityAddButton;
    private TextView dialogActivityHoursText;
    private VerticalSeekBar dialogActivitySeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.e("432", "beforeSetContent");
        setContentView(R.layout.activity_main);
        Log.e("432", "After");
        init();
        presenter.getContext(this);
        presenter.getProgressPieView(progressPieView);
        presenter.onCreate(this.getLocalClassName());


    }

    @Override
    public void finishActivity()
    {
        finish();
    }

    @Override
    public void setText(String text, int textViewId)
    {
        TextView textView = findViewById(textViewId);
        textView.setText(text);
    }

    private void init()
    {
        presenter = new MainPresenter(this);
        progressPieView = findViewById(R.id.progress_bar);
    }

    private void dialogViewsInit(View view)
    {
        dialogAddButton = view.findViewById(R.id.addButton);
        dialogCancelButton = view.findViewById(R.id.cancelButton);
        dialogLiquidTotalSum = view.findViewById(R.id.totalSum);
        dialogLiquidType = view.findViewById(R.id.liquidType);
        dialogSeekBar = view.findViewById(R.id.seekBar);
        dialogLiquidVolume = view.findViewById(R.id.liquidVolume);

        dialogCancelButton.setOnClickListener(onClickListener);
        dialogAddButton.setOnClickListener(onClickListener);
    }

    @Override
    public void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.liquid_add_drink, null);
        dialogViewsInit(view);
        builder.setView(view);
        presenter.getDialogView(view);
        presenter.onProgressChanged(250);
        dialogSeekBar.setOnProgressChangeListener(new Function1<Integer, Unit>()
        {
            @Override
            public Unit invoke(Integer progressValue)
            {
                presenter.onProgressChanged(progressValue);
                return null;
            }
        });
        AlertDialog dialog = builder.create();
        presenter.getDialog(dialog);
        dialog.show();
    }

    @Override
    public void showActivityDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.liquid_add_activity, null);
        dialogActivityViewsInit(view);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        presenter.getActivityDialog(dialog);
        presenter.getActivityDialogView(view);
        dialogActivityViewsInit(view);
        dialog.show();
    }


    private void dialogActivityViewsInit(View view)
    {
        Button two, four, six, eight;
        two = view.findViewById(R.id.button1);
        four = view.findViewById(R.id.button3);
        six = view.findViewById(R.id.button);
        eight = view.findViewById(R.id.button4);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                presenter.onButtonWasClicked(v.getId());
            }
        };
        two.setOnClickListener(onClickListener);
        four.setOnClickListener(onClickListener);
        six.setOnClickListener(onClickListener);
        eight.setOnClickListener(onClickListener);



    }

    public void onClick(View view)
    {
        if(view.getId() == R.id.exitButton)
        {
            Snackbar snackbar = Snackbar
                    .make(view, "Are you want to exit?", Snackbar.LENGTH_SHORT)
                    .setAction("Yes", new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {
                            presenter.onButtonWasClicked(10);
                        }
                    });
            snackbar.show();
        }
        else if(view.getId() == R.id.progress_bar)
        {
            showDialog();
        }
        else
        {
            presenter.onButtonWasClicked(view.getId());
        }

    }


}

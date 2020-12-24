package com.example.drinkwater.MVP;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drinkwater.Activities.LaunchActivities.RegAuthActivity;
import com.example.drinkwater.POJO.Liquid;
import com.example.drinkwater.R;
import com.filippudak.ProgressPieView.ProgressPieView;
import com.google.android.material.snackbar.Snackbar;
import com.lukelorusso.verticalseekbar.VerticalSeekBar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/*
  Почитай пж и исправь во всем проекте форматирование
* https://www.magnumblog.space/java/131-translating-java-code-conventions
* */
public class MainPresenter implements MainContract.MainPresenter
{
    private MainContract.ViewExtedned view;
    private MainContract.Repository repository;
    private SharedPreferences servicePreferences;
    private SharedPreferences settingsPreferences;
    private int weight;
    private ProgressPieView progressPieView;
    private Context context;
    private String userName, totalDrunk, daysComplete, lastDrunk, currentDate, city, temperature;
    private int userId, dayId;
    private Long tsLong = System.currentTimeMillis()/1000;  // добавить пробелы
    private String scaleType;
    private String volumeType;
    private double scaleMultipler;
    private double liquidRatio;
    private String liquidName;
    private Dialog dialog;

    private double onTodayGoal;
    private String todayGoalString;
    private Dialog activityDialog;
    private boolean isShownToast = false;

    private View dialogView; // убрать
    private VerticalSeekBar dialogSeekBar;  // убрать
    private TextView dialogLiquidVolume, dialogLiquidType, dialogLiquidTotalSum;  // убрать
    private Button dialogCancelButton, dialogAddButton;  // убрать
    private double prog;
    private DecimalFormat dc = new DecimalFormat("#.#");

    // Даже click listener здесь!?
    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.addButton:
                    break;

                case R.id.cancelButton:
                    break;
            }
        }
    };


    public MainPresenter(MainContract.ViewExtedned view)//Конструктор:)
    {
        this.view = view;
    }

    // почему viewId в presenter?
    @Override
    public void onButtonWasClicked(int viewId)
    {
        switch (viewId)
        {

            case R.id.progress_bar:
                break;
            case R.id.aboutButton:
                break;
            case 10:
                exit();
                break;

            case R.id.settingsButton:
                break;

            case R.id.historyButton:
                break;

            case R.id.helpButton:
                break;

            case R.id.button1://2hours
                repository.addActivity(2);
                onTodayGoal = (repository.getUserWeight() * 1) * 0.032 + 2 * (60/100)* 1000;
                activityDialog.cancel();
                break;
            case R.id.button3://4hours
                repository.addActivity(4);
                onTodayGoal = (repository.getUserWeight() * 1) * 0.032 + 4 * (60/100)* 1000;
                activityDialog.cancel();
                break;
            case R.id.button4://8hours
                repository.addActivity(8);
                onTodayGoal = (repository.getUserWeight() * 1) * 0.032 + 8 * (60/100)* 1000;
                activityDialog.cancel();
                break;
            case R.id.button://6hours
                repository.addActivity(6);
                onTodayGoal = (repository.getUserWeight() * 1) * 0.032 + 6 * (60/100)* 1000;
                activityDialog.cancel();
                break;

        }

    }

    // Зачем тут className вообще?
    @Override
    public void onCreate(String className)
    {
        servicePreferences = context.getSharedPreferences("service", Context.MODE_PRIVATE);
        settingsPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        userId = servicePreferences.getInt("userId", 0);
        Log.e("543", " " + userId);
        //dayId = servicePreferences.getInt("dayId", 0);
        dayId = repository.getDayIdByDate(tsLong);

        onTodayGoal = (repository.getUserWeight() * 1) * 0.032 + repository.getActivityDuration() * (60/100)* 1000;
        todayGoalString = "" + (int)repository.getDayDrunkSumm() + "/" + (int)onTodayGoal + " " + volumeType;
        progressPieView.setText(todayGoalString);


        servicePreferences.edit().putInt("dayId", repository.getDayIdByDate(tsLong)).apply();
        volumeType = settingsPreferences.getString("volumeType", "ml");
        scaleType = settingsPreferences.getString("scaleType", "kg");
        Log.e("volumeType", settingsPreferences.getString("volumeType", "ml"));
        if(volumeType.equals("ml"))
        {
            scaleMultipler = 1;
        }
        else
        {
            scaleMultipler = 0.03;
        }
        if(isFirstLaunchOnDay())
        {
            repository.addDay(tsLong, 0);
            view.showActivityDialog();

            Log.e("111", todayGoalString);
        }
        dayId = repository.getDayIdByDate(tsLong);
        Log.e("dayId", "" + dayId);
        updateInfo();
    }

    @Override
    public void onDestroy(String className) {

    }

    @Override
    public void getText(EditText editText) {

    }

    /*
    * Если мы даем Context в presenter то логичнее будет назвать метод setContext?
    * */
    @Override
    public void getContext(Context context)
    {
        this.context = context;
        this.repository = new Repository(context);
    }

    @Override
    public void onProgressChanged(int progress)
    {
        prog = progress*scaleMultipler;
        dialogLiquidVolume.setText("" + dc.format(prog) + volumeType);
        dialogLiquidTotalSum.setText("* " + liquidRatio + " = "+  (int)(prog * liquidRatio) + " " + volumeType);
    }

    @Override
    public void getDialogView(View view)
    {
        dialogView = view;
        dialogViewsInit(view);
    }

    @Override
    public void getDialog(Dialog dialog)
    {
        this.dialog = dialog;
    }

    @Override
    public void getActivityDialogView(View view)
    {
    }

    @Override
    public void getActivityDialog(Dialog dialog)
    {
        this.activityDialog = dialog;
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

        liquidName = repository.getLiquidList().get(0).getLiquidName();
        dialogLiquidType.setText(liquidName);
        liquidRatio = repository.getLiquidList().get(0).getLiquidRatio();
        dialogLiquidTotalSum.setText("* " + liquidRatio + " = "+  prog * liquidRatio);
        dialogLiquidType.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu popup = new PopupMenu(context, dialogLiquidType);
                ArrayList<Liquid> arrayList = repository.getLiquidList();
                for(Liquid liquid: arrayList)
                {
                    //popup.getMenu().add(liquid.getLiquidName() + " *" + liquid.getLiquidRatio());
                    popup.getMenu().add(liquid.getLiquidName());
                }

                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        Liquid liquid = repository.getLiquidById(repository.getLiquidIdByName(item.getTitle().toString()));
                        liquidRatio = liquid.getLiquidRatio();
                        liquidName = liquid.getLiquidName();
                        dialogLiquidType.setText(liquidName);
                        dialogLiquidTotalSum.setText("* " + liquidRatio + " = "+  dc.format(prog * liquidRatio) + " " + volumeType);
                        return true;
                    }
                });
                popup.show();//showing popup menu
            }
        });
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.cancel();
            }
        });
        dialogAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                repository.addWaterToDay((int)prog);
                Log.e("waterConsumptionAdd", " " + (int)prog + " " + dayId
                        + " " + repository.getLiquidIdByName(liquidName) + " " + liquidName);
                repository.addWaterConsumption(tsLong, (int)prog, dayId, repository.getLiquidIdByName(liquidName));
                updateInfo();
                dialog.cancel();
            }
        });

    }

    @Override
    public String getStringFromResource(Context context, int id)
    {
        return context.getResources().getString(id);
    }

    /* Это не MVP, MVVM и даже не MVC если ты даешь ссылки на вьюшки в presenter. ТАК НИГДЕ НЕЛЬЗЯ!)
     вместо этого создай по методу в контракте, что ты хочешь сделать с progressPieView. См ниже */
    @Override
    public void getProgressPieView(ProgressPieView progressPieView)
    {
        this.progressPieView = progressPieView;
    }

    private boolean isFirstLaunchOnDay()
    {
        boolean isFirstLaunch = false;
        if (!(repository.getStringDateByLong(servicePreferences.getLong("dateLong",0)).
                equals(repository.getStringDateByLong(tsLong))) && repository.isDayCreated(tsLong))
        {
            servicePreferences.edit().putLong("dateLong",tsLong).apply();
            isFirstLaunch = true;
            Log.e("isFirstLaunchOnDay", "firstLaunch");
        }
        return isFirstLaunch;
    }

    private void updateInfo()
    {
        userName = repository.getUserNameById(servicePreferences.getInt("userId", 0));
        userId = repository.getUserIdByUserName(userName);
        dayId = repository.getDayIdByDate(tsLong);
        servicePreferences.edit().putInt("dayId", dayId).apply();
        Log.e("summaryDrunked", "" + repository.summaryDrunked());
        totalDrunk = "" + dc.format(repository.summaryDrunked() * scaleMultipler) + " " + volumeType;
        lastDrunk = repository.getLastConsumption(dayId).getLiquid_name()
                + " " + (int)(repository.getLastConsumption(dayId).getConsumption_volume()*scaleMultipler) + " " + volumeType;
        currentDate = repository.getStringDateByLong(tsLong);

        // зачем NULL? просто пустая строка
        city = "NULL";
        temperature = "NULL";
        daysComplete = "NULL";
        onTodayGoal = ((repository.getUserWeight() * 1) * 0.032 + repository.getActivityDuration() * (60/100))*1000;
        todayGoalString = "" + repository.getDayDrunkSumm() + "/" + (int)onTodayGoal + " " + volumeType;

        // Вместо этого view.setProgressText(String text);
        progressPieView.setText(todayGoalString);

        int progress = (int)(repository.getDayDrunkSumm()/(onTodayGoal/100));
        if(progress >= 99)
        {
            // Вместо этого view.setProgressValue(int value);
            progressPieView.setProgress(100);
            if(!isShownToast)
            {
                isShownToast = true;
                view.showToast(context.getResources().getString(R.string.riached_prog_msg));
            }
        }
        else
        {
            // Вместо этого view.setProgressValue(int value);
            progressPieView.setProgress(progress);
        }


        /* Presenter не знает о ресурсах!!! */

        // view.setUserName(String userName);
        view.setText(userName, R.id.usernameText);

        // view?.setTotalDrunk(int totalDrunk);
        view.setText(context.getResources().getString(R.string.total_drank_summ) + totalDrunk + " " + scaleMultipler, R.id.totalDrunk);

        // view?.setDaysComplete(int daysComplete);
        view.setText(context.getResources().getString(R.string.days_complete) + daysComplete, R.id.daysCompletes);

        // по аналогии
        view.setText(context.getResources().getString(R.string.last_drunk) +lastDrunk, R.id.lastDrunk);

        // аналогично
        view.setText(currentDate, R.id.dateText);

        // аналогично
        view.setText(city, R.id.cityText);

        // аналогично
        view.setText(temperature + " C", R.id.temperatureText);
    }

    private void exit()
    {
        servicePreferences.edit().remove("userName").apply();
        servicePreferences.edit().remove("password").apply();
        servicePreferences.edit().remove("dateLong").apply();
        servicePreferences.edit().remove("userId").apply();

        // view.openAuth() и туда этот код. Контекста здесь быть не должно
        context.startActivity(new Intent(context, RegAuthActivity.class));

        // presenter не знает что за view у нас. То ли activity то ли fragment. А может просто View.
        // так что view.exit() будет логичнее
        view.finishActivity();
    }
}

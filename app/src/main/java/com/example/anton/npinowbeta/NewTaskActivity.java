package com.example.anton.npinowbeta;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    FloatingActionButton fab;
    DBHelper dbHelper;
    EditText eTitle;
    EditText eTask;
    ImageView White;
    ImageView Red;
    ImageView Orange;
    ImageView Blue;
    ImageView Green;
    TextView NotificationStat;
    int Hour = 4;
    int Minute = 20;
    int Year;
    int Month;
    int DayOfMonth;
    String Color = "white";
    final static int ALERT_DAY = 0;
    final static int ALERT_TIME = 1;
    final int _id = (int) System.currentTimeMillis();

    //Список опций
    ListView list;
    //Названия
    String[] web = {
            "День напоминания",
            "Время напоминания"
    };
    //Иконки
    Integer[] imageId = {
            R.drawable.ic_day,
            R.drawable.ic_time
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String ExColor = intent.getStringExtra("color");
        String ExTitle = intent.getStringExtra("title");
        String ExTask = intent.getStringExtra("task");
        int ExID = intent.getIntExtra("id", 0);
        int EXNot_id = intent.getIntExtra("not_id", 0);

        //FAB
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData();
                setNotification();
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        eTitle = (EditText) findViewById(R.id.edit_title);
        eTask = (EditText) findViewById(R.id.edit_task);
        dbHelper = new DBHelper(this);
        White = (ImageView) findViewById(R.id.color_white);
        White.setOnClickListener(this);
        Red = (ImageView) findViewById(R.id.color_red);
        Red.setOnClickListener(this);
        Orange = (ImageView) findViewById(R.id.color_orange);
        Orange.setOnClickListener(this);
        Green = (ImageView) findViewById(R.id.color_green);
        Green.setOnClickListener(this);
        Blue = (ImageView) findViewById(R.id.color_blue);
        Blue.setOnClickListener(this);
        NotificationStat = (TextView) findViewById(R.id.notification_stat);
        NotificationStat.setBackgroundColor(getResources().getColor(R.color.WHITE_NOTE));

        //Заполнение адаптера для списка опций
        OptionAdapter adapter = new OptionAdapter(this, web, imageId);
        list=(ListView)findViewById(R.id.option_list);
        list.setAdapter(adapter);
        //Клик по одной из опций
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position){
                    case 0:
                        showDialog(ALERT_DAY);
                        break;
                    case 1:
                        showDialog(ALERT_TIME);
                        break;
                }
            }
        });

        if(ExTitle != null){
            eTitle.setText(ExTitle);
        }
        else if (ExTask != null){
            eTask.setText(ExTask);
        }
        else if (ExColor != null){
            changeColor(ExColor);
        }
    }

    //Обработка диалогов
    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == ALERT_DAY){
            Calendar c = Calendar.getInstance();
            Year = c.get(Calendar.YEAR);
            Month = c.get(Calendar.MONTH);
            DayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(this, CallBackDay, Year, Month, DayOfMonth);
            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return dpd;
        }
        else if(id == ALERT_TIME){
            Calendar c = Calendar.getInstance();
            Hour = c.get(Calendar.HOUR_OF_DAY);
            Minute = c.get(Calendar.MINUTE);
            TimePickerDialog tpd = new TimePickerDialog(this, CallBackTime, Hour, Minute, true);
            return tpd;
        }
        return null;
    }

    //Обработка выбора даты
    DatePickerDialog.OnDateSetListener CallBackDay = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Year = year;
            Month = month;
            DayOfMonth = dayOfMonth;
            //Toast.makeText(getApplicationContext(),
                    //DayOfMonth+"."+Month+"."+Year,
                    //Toast.LENGTH_SHORT).show();
            NotificationStat.setText(DayOfMonth+"."+Month+"."+Year+" в "+Hour+":"+Minute);
        }
    };

    //Обработка выбора времени
    TimePickerDialog.OnTimeSetListener CallBackTime = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Hour = hourOfDay;
            Minute = minute;
            //Toast.makeText(getApplicationContext(),
                    //Hour+":"+Minute,
                    //Toast.LENGTH_SHORT).show();
            Calendar c = Calendar.getInstance();
            Year = c.get(Calendar.YEAR);
            Month = c.get(Calendar.MONTH);
            DayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            NotificationStat.setText(DayOfMonth+"."+Month+"."+Year+" в "+Hour+":"+Minute);
        }
    };

    //Установка оповещения для напоминания
    private void setNotification(){
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("title", eTitle.getText().toString());
        alarmIntent.putExtra("task", eTask.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, _id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager =  (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar c = Calendar.getInstance();
        c.set(Year, Month, DayOfMonth, Hour, Minute);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    //смена цвета
    public void  changeColor(String color){
        if (color.equals("white")){
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            NotificationStat.setBackgroundColor(getResources().getColor(R.color.WHITE_NOTE));
        }
        else if (color.equals("red")){
            toolbar.setBackgroundColor(getResources().getColor(R.color.RED));
            getWindow().setStatusBarColor(getResources().getColor(R.color.RED_ACCENT));
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.RED_ACCENT)));
            NotificationStat.setBackgroundColor(getResources().getColor(R.color.RED_NOTE));
        }
        else if (color.equals("orange")){
            toolbar.setBackgroundColor(getResources().getColor(R.color.ORANGE));
            getWindow().setStatusBarColor(getResources().getColor(R.color.ORANGE_ACCENT));
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ORANGE_ACCENT)));
            NotificationStat.setBackgroundColor(getResources().getColor(R.color.ORANGE_NOTE));
        }
        else if (color.equals("green")){
            toolbar.setBackgroundColor(getResources().getColor(R.color.GREEN));
            getWindow().setStatusBarColor(getResources().getColor(R.color.GREEN_ACCENT));
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.GREEN_ACCENT)));
            NotificationStat.setBackgroundColor(getResources().getColor(R.color.GREEN_NOTE));
        }
        else if (color.equals("blue")){
            toolbar.setBackgroundColor(getResources().getColor(R.color.BLUE));
            getWindow().setStatusBarColor(getResources().getColor(R.color.BLUE_ACCENT));
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.BLUE_ACCENT)));
            NotificationStat.setBackgroundColor(getResources().getColor(R.color.BLUE_NOTE));
        }
    }

    //Обработка нажатий на палитры
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.color_white:
                changeColor("white");
                Color = "white";
                break;
            case R.id.color_red:
                changeColor("red");
                Color = "red";
                break;
            case R.id.color_orange:
                changeColor("orange");
                Color = "orange";
                break;
            case R.id.color_green:
                changeColor("green");
                Color = "green";
                break;
            case R.id.color_blue:
                changeColor("blue");
                Color = "blue";
                break;
        }
    }

    public void SaveData() {

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // получаем данные из полей ввода
        String title = eTitle.getText().toString();
        String task = eTask.getText().toString();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.put("title", title);
        cv.put("task", task);
        cv.put("color", Color);
        cv.put("not_id", _id);
        // вставляем запись и получаем ее ID
        long rowID = db.insert("taskTable", null, cv);
        dbHelper.close();
    }
}

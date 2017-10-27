package com.example.anton.npinowbeta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

import static com.example.anton.npinowbeta.R.id.toolbar;

public class DayActivity extends AppCompatActivity {

    Toolbar toolbar;
    View view;
    RVAdapter adapter;
    RecyclerView rv;
    //Intent networkSetting;
    String[] Les1 = new String[5];
    String[] Les2 = new String[5];
    String[] Les3 = new String[5];
    String[] Les4 = new String[5];
    String prefFac;
    String prefCurs;
    String prefGroup;
    String URL;
    int DayOfWeek;
    String Week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().getThemedContext().setTheme(R.style.AppTheme_PopupOverlay);
        rv = (RecyclerView) findViewById(R.id.rv_day);
        rv.setNestedScrollingEnabled(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            rv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        }
        else{
            rv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
        Intent intent = getIntent();
        DayOfWeek = intent.getIntExtra("name", 0);
        setTitle(Title(DayOfWeek+1));
        Week = intent.getStringExtra("week");
        String strJson = readFromFile(getApplicationContext());
        if(strJson == null)
        {

        }
        else
        {
            ParsJson(strJson);
            initializeData();
            adapter = new RVAdapter(lessonses, getApplicationContext());
            rv.setAdapter(new AlphaInAnimationAdapter(adapter));
        }
    }

    public String Title(int NumDay){
        switch (NumDay) {
            case 1:
                return "Понедельник";
            case 2:
                return "Вторник";
            case 3:
                return "Среда";
            case 4:
                return "Четверг";
            case 5:
                return "Пятница";
            case 6:
                return "Суббота";
        }
        return "День";
    }

    private List<Lesson> lessonses;
    // This method creates an ArrayList that has three Lesson objects
    // Checkout the project associated with this tutorial on Github if
    // you want to use the same images.
    private void initializeData(){
        lessonses = new ArrayList<>();
        if (Les1 != null){
            lessonses.add(new Lesson(Les1[4], Les1[0], Les1[1], Les1[3],  Les1[2]));
        }
        if (Les2 != null){
            lessonses.add(new Lesson(Les2[4], Les2[0], Les2[1], Les2[3], Les2[2]));
        }
        if (Les3 != null){
            lessonses.add(new Lesson(Les3[4], Les3[0], Les3[1], Les3[3], Les3[2]));
        }
        if (Les4 != null){
            lessonses.add(new Lesson(Les4[4], Les4[0], Les4[1], Les4[3], Les4[2]));
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("data");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        return ret;
    }

    public boolean ParsJson(String strJson){
        Les1 = new String[5];
        Les2 = new String[5];
        Les3 = new String[5];
        Les4 = new String[5];

        JSONObject dataJsonObj = null;
        String secondName = "";



        try {
            if (strJson == null)
            {
                Intent intent = new Intent(view.getContext(), SettingActivity.class);
                startActivity(intent);
            }
            else
            {
                dataJsonObj = new JSONObject(strJson);
                // номер недели
                JSONObject week = dataJsonObj.getJSONObject(Week);

                // день недели
                JSONObject day = week.getJSONObject(Integer.toString(DayOfWeek+1));

                // расписание на выбранный день
                JSONArray names = day.names();
                JSONArray[] lesson = new JSONArray[names.length()];
                for(int i = 0; i < names.length(); i++) {
                    lesson[i] = day.getJSONArray(names.getString(i));
                }
                switch (lesson.length)
                {
                    case 0:
                        Les1 = null;
                        Les2 = null;
                        Les3 = null;
                        Les4 = null;
                        break;
                    case 1:
                        for (int i = 0; i < 4; i++) {
                            Les1[i] = lesson[0].getString(i);
                        }
                        Les1[4] = names.getString(0);
                        Les2 = null;
                        Les3 = null;
                        Les4 = null;
                        break;
                    case 2:
                        for (int i = 0; i < 4; i++) {
                            Les1[i] = lesson[0].getString(i);
                            Les2[i] = lesson[1].getString(i);
                        }
                        Les1[4] = names.getString(0);
                        Les2[4] = names.getString(1);
                        Les3 = null;
                        Les4 = null;
                        break;
                    case 3:
                        for (int i = 0; i < 4; i++) {
                            Les1[i] = lesson[0].getString(i);
                            Les2[i] = lesson[1].getString(i);
                            Les3[i] = lesson[2].getString(i);
                        }
                        Les1[4] = names.getString(0);
                        Les2[4] = names.getString(1);
                        Les3[4] = names.getString(2);
                        Les4 = null;
                        break;
                    case 4:
                        for (int i = 0; i < 4; i++) {
                            Les1[i] = lesson[0].getString(i);
                            Les2[i] = lesson[1].getString(i);
                            Les3[i] = lesson[2].getString(i);
                            Les4[i] = lesson[3].getString(i);
                        }
                        Les1[4] = names.getString(0);
                        Les2[4] = names.getString(1);
                        Les3[4] = names.getString(2);
                        Les4[4] = names.getString(3);
                        break;
                    default:
                        break;
                }
                return true;
            }
            return false;
        } catch (JSONException e) {
            Les1[1] = "";
            Les1[0] = "Сегодня выходной";
            Les1[3] = "Проведите день с пользой";
            Les2 = null;
            Les3 = null;
            Les4 = null;
            e.printStackTrace();
            return false;
        }
    }
}

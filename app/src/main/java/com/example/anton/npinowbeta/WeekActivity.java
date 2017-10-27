package com.example.anton.npinowbeta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

public class WeekActivity extends AppCompatActivity {

    TimeAdapter adapter;
    RecyclerView rv;
    String prefFac;
    String prefCurs;
    String prefGroup;
    String URL;
    String Week;

    List<String[]> Mon = null;
    List<String[]> Thu = null;
    List<String[]> Wed = null;
    List<String[]> Tue = null;
    List<String[]> Fri = null;
    List<String[]> Sat = null;

    public final static int REQUEST_CODE_OK = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        rv = (RecyclerView) findViewById(R.id.rv_timetable);
        rv.setNestedScrollingEnabled(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new SlideInDownAnimator());
        setTitle("Неделя");
        checkWeek();

        String strJson = readFromFile(getApplicationContext());
        if (strJson == null) {
            Intent intent = new Intent(getApplicationContext(), FirstSettingActivity.class);
            startActivityForResult(intent, REQUEST_CODE_OK);
        } else {
            ParsJson(strJson);
            initializeData();
            adapter = new TimeAdapter(days, getApplicationContext());
            rv.setAdapter(new AlphaInAnimationAdapter(adapter));
        }
    }

    private void checkWeek(){
        // определение недели
        Calendar c = Calendar.getInstance();
        int WeekOfYear = c.get(Calendar.WEEK_OF_YEAR);
        if (WeekOfYear % 2 == 0) {
            Week = "1";
            setTitle("Первая неделя");
        } else {
            Week = "2";
            setTitle("Вторая неделя");

        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean weekPref = sharedPref.getBoolean("prefWeeks", false);
        if(weekPref){
            // определения дня
            int DayOfWeek = c.get(Calendar.DAY_OF_WEEK); //недели начинаются с 1 (воскрсенье), 2 (понедельник) и т. д.
            //Если понедельник, то оставить все как есть
            if (DayOfWeek == 2) {
                if (Week.equals("2")) {
                    Week = "2";
                    setTitle("Вторая неделя");
                } else if (Week.equals("1")) {
                    Week = "1";
                    setTitle("Первая неделя");
                }
            }
        }
        else {
            // определения дня
            int DayOfWeek = c.get(Calendar.DAY_OF_WEEK); //недели начинаются с 1 (воскрсенье), 2 (понедельник) и т. д.
            //Если воскресенье, то переключить недели
            if (DayOfWeek == 1) {
                if (Week.equals("2")) {
                    Week = "1";
                    setTitle("Первая неделя");
                } else if (Week.equals("1")) {
                    Week = "2";
                    setTitle("Вторая неделя");
                }
            }
        }

    }

    private List<Day> days;
    private List<Lesson> lessons;
        // This method creates an ArrayList that has three Lesson objects
        // Checkout the project associated with this tutorial on Github if
        // you want to use the same images.

    private void initializeData() {
        days = new ArrayList<>();
        if (Mon != null) {
            days.add(new Day(true, Week, "Понедельник", Mon));
        } else {
            days.add(new Day(false, Week, "Понедельник", Mon));
        }
        if (Thu != null) {
            days.add(new Day(true, Week, "Вторник", Thu));
        } else {
            days.add(new Day(false, Week, "Вторник", Thu));
        }
        if (Wed != null) {
            days.add(new Day(true, Week, "Среда", Wed));
        } else {
            days.add(new Day(false, Week, "Среда", Wed));
        }
        if (Tue != null) {
            days.add(new Day(true, Week, "Четверг", Tue));
        } else {
            days.add(new Day(false, Week, "Четверг", Tue));
        }
        if (Fri != null) {
            days.add(new Day(true, Week, "Пятница", Fri));
        } else {
            days.add(new Day(false, Week, "Пятница", Fri));
        }
        if (Sat != null) {
            days.add(new Day(true, Week, "Суббота", Sat));
        } else {
            days.add(new Day(false, Week, "Суббота", Sat));
        }
        Mon = null;
        Thu = null;
        Wed = null;
        Tue = null;
        Fri = null;
        Sat = null;
    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("data", MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {

        }
    }

    private String readFromFile(Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("data");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return ret;
    }

    public List<String[]> ParsJsonDay(JSONObject dayJSON) {
        String[] Les1 = new String[5];
        String[] Les2 = new String[5];
        String[] Les3 = new String[5];
        String[] Les4 = new String[5];
        List<String[]> Lessons = new ArrayList<>(5);
        try {
            if (dayJSON == null) {

            } else {
                JSONArray names = dayJSON.names();
                JSONArray[] lesson = new JSONArray[names.length()];
                for (int i = 0; i < names.length(); i++) {
                    lesson[i] = dayJSON.getJSONArray(names.getString(i));

                }
                switch (lesson.length) {
                    case 0:
                        Les1 = null;
                        Les2 = null;
                        Les3 = null;
                        Les4 = null;
                        break;
                    case 1:
                        if (names.getString(0).equals("10:45-12:15")){
                            Les1 = null;
                            for (int i = 0; i < 4; i++) {
                                Les2[i] = lesson[0].getString(i);
                            }
                            Les2[4] = names.getString(0);
                            Les3 = null;
                            Les4 = null;
                        }
                        else if (names.getString(0).equals("12:45-14:15")){
                            Les1 = null;
                            Les2 = null;
                            for (int i = 0; i < 4; i++) {
                                Les3[i] = lesson[0].getString(i);
                            }
                            Les3[4] = names.getString(0);
                            Les4 = null;
                        }
                        else if (names.getString(0).equals("14:30-16:00")){
                            Les1 = null;
                            Les2 = null;
                            Les3 = null;
                            for (int i = 0; i < 4; i++) {
                                Les4[i] = lesson[0].getString(i);
                            }
                            Les4[4] = names.getString(0);
                        }
                        else {
                            for (int i = 0; i < 4; i++) {
                                Les1[i] = lesson[0].getString(i);
                            }
                            Les1[4] = names.getString(0);
                            Les2 = null;
                            Les3 = null;
                            Les4 = null;
                        }
                        break;
                    case 2:
                        if (names.getString(0).equals("10:45-12:15")) {
                            Les1 = null;
                            for (int i = 0; i < 4; i++) {
                                Les2[i] = lesson[0].getString(i);
                                Les3[i] = lesson[1].getString(i);
                            }
                            Les2[4] = names.getString(0);
                            Les3[4] = names.getString(1);
                            Les4 = null;
                        }
                        else if (names.getString(0).equals("12:45-14:15")) {
                            Les1 = null;
                            Les2 = null;
                            for (int i = 0; i < 4; i++) {
                                Les3[i] = lesson[0].getString(i);
                                Les4[i] = lesson[1].getString(i);
                            }
                            Les3[4] = names.getString(0);
                            Les4[4] = names.getString(1);
                        }
                        else {
                            for (int i = 0; i < 4; i++) {
                                Les1[i] = lesson[0].getString(i);
                                Les2[i] = lesson[1].getString(i);
                            }
                            Les1[4] = names.getString(0);
                            Les2[4] = names.getString(1);
                            Les3 = null;
                            Les4 = null;
                        }
                        break;
                    case 3:
                        if (names.getString(0).equals("10:45-12:15")){
                            Les1 = null;
                            for (int i = 0; i < 4; i++) {
                                Les2[i] = lesson[0].getString(i);
                                Les3[i] = lesson[1].getString(i);
                                Les4[i] = lesson[2].getString(i);
                            }
                            Les2[4] = names.getString(0);
                            Les3[4] = names.getString(1);
                            Les4[4] = names.getString(2);
                        }
                        else {
                            for (int i = 0; i < 4; i++) {
                                Les1[i] = lesson[0].getString(i);
                                Les2[i] = lesson[1].getString(i);
                                Les3[i] = lesson[2].getString(i);
                            }
                            Les1[4] = names.getString(0);
                            Les2[4] = names.getString(1);
                            Les3[4] = names.getString(2);
                            Les4 = null;
                        }
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
                Lessons.add(Les1);
                Lessons.add(Les2);
                Lessons.add(Les3);
                Lessons.add(Les4);
                return Lessons;
            }
            Lessons.add(Les1);
            Lessons.add(Les2);
            Lessons.add(Les3);
            Lessons.add(Les4);
            return Lessons;
        } catch (JSONException e) {
            Les1[1] = "";
            Les1[0] = "Сегодня выходной";
            Les1[3] = "Проведите день с пользой";
            Les2 = null;
            Les3 = null;
            Les4 = null;
            Lessons.add(Les1);
            Lessons.add(Les2);
            Lessons.add(Les3);
            Lessons.add(Les4);
            e.printStackTrace();
            return Lessons;
        }
    }


    public boolean ParsJson(String strJson) {
        List<List<String[]>> Days = new ArrayList<>(6);

        JSONObject dataJsonObj = null;
        String secondName = "";

        try {
            if (strJson == null) {
                //Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                //startActivity(intent);
            } else {
                dataJsonObj = new JSONObject(strJson);
                // номер недели
                JSONObject week = dataJsonObj.getJSONObject(Week);

                JSONArray names = week.names();
                JSONObject[] days = new JSONObject[6];
                for (int i = 0; i < week.names().length(); i++) {
                    days[i] = week.getJSONObject(week.names().getString(i));
                    switch (Integer.valueOf(week.names().get(i).toString())) {
                        case 1:
                            Mon = ParsJsonDay(days[i]);
                            break;
                        case 2:
                            Thu = ParsJsonDay(days[i]);
                            break;
                        case 3:
                            Wed = ParsJsonDay(days[i]);
                            break;
                        case 4:
                            Tue = ParsJsonDay(days[i]);
                            break;
                        case 5:
                            Fri = ParsJsonDay(days[i]);
                            break;
                        case 6:
                            Sat = ParsJsonDay(days[i]);
                            break;
                    }
                }
                return true;
            }
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_week, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_another) {
            if (Week.equals("2")){
                Week = "1";
                setTitle("Первая неделя");
            }
            else if (Week.equals("1")){
                Week = "2";
                setTitle("Вторая неделя");
            }
            String strJson = readFromFile(getApplicationContext());
            ParsJson(strJson);
            initializeData();
            adapter = new TimeAdapter(days, getApplicationContext());
            rv.setAdapter(new AlphaInAnimationAdapter(adapter));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Update extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                java.net.URL url = new URL(URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
                //networkSetting = new Intent(Intent.ACTION_MANAGE_NETWORK_USAGE);
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            // енкод
            JSON_encode json_encode = new JSON_encode();
            strJson = json_encode.encode(strJson);
            if (strJson == null) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        R.string.refresh_error, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                writeToFile(strJson, getApplicationContext());
                String TimeTableJson = readFromFile(getApplicationContext());
                ParsJson(TimeTableJson);
                initializeData();
                adapter = new TimeAdapter(days, getApplicationContext());
                rv.setAdapter(adapter);
                // mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
package com.example.anton.npinowbeta;

/**
 * Created by anton on 02.01.17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.content.Context.MODE_PRIVATE;

public class TomFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;
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

    public final static int REQUEST_CODE_OK = 0;

    public TomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_now, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv_now);
        rv.setNestedScrollingEnabled(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            rv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        }
        else{
            rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        // свайп то рефреш
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        // делаем повеселее
        //mSwipeRefreshLayout.setColorScheme(R.color.blue, R.color.green, R.color.yellow, R.color.red);
        // Inflate the layout for this fragment
        String strJson = readFromFile(getActivity());
        if(strJson == null)
        {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            prefFac = sharedPref.getString("prefFac", "");
            prefCurs = sharedPref.getString("prefCurs", "");
            prefGroup = sharedPref.getString("prefGroup", "");
            URL = "http://schedule.npi-tu.ru/data/?type=schedule&group="+prefGroup+"&faculty="+prefFac+"&kurs="+prefCurs;
            new Update().execute();
        }
        else
        {
            ParsJson(strJson);
            initializeData();
            adapter = new RVAdapter(lessonses, getContext());
            rv.setAdapter(new AlphaInAnimationAdapter(adapter));
        }
        return view;
    }

    public void FromActivityRefresh(){
        String TimeTableJson = readFromFile(getActivity());
        ParsJson(TimeTableJson);
        initializeData();
        adapter = new RVAdapter(lessonses, getContext());
        rv.setAdapter(new AlphaInAnimationAdapter(adapter));
    }

    public static String Ref(String Fac, String Curs, String Group){
        return "http://schedule.npi-tu.ru/data/?type=schedule&group="+Group+"&faculty="+Fac+"&kurs="+Curs;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_CODE_OK:
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                prefFac = sharedPref.getString("prefFac", "");
                prefCurs = sharedPref.getString("prefCurs", "");
                prefGroup = sharedPref.getString("prefGroup", "");
                URL = "http://schedule.npi-tu.ru/data/?type=schedule&group="+prefGroup+"&faculty="+prefFac+"&kurs="+prefCurs;
                new Update().execute();
                //you just got back from activity B - deal with resultCode
                //use data.getExtra(...) to retrieve the returned data
                break;
            default:
                break;
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                prefFac = sharedPref.getString("prefFac", "");
                prefCurs = sharedPref.getString("prefCurs", "");
                prefGroup = sharedPref.getString("prefGroup", "");
                URL = "http://schedule.npi-tu.ru/data/?type=schedule&group="+prefGroup+"&faculty="+prefFac+"&kurs="+prefCurs;
                if(isOnline()) {
                    new Update().execute();
                }
                else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Snackbar.make(view, R.string.refresh_error, Snackbar.LENGTH_LONG).show();

                }
                //Snackbar.make(view, "Заполните настройки приложения", Snackbar.LENGTH_LONG)
                //  .setAction("Action", null).show();
                // Отменяем анимацию обновления

            }
        }, 0);
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

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("data", MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Snackbar.make(view, R.string.refresh_error, Snackbar.LENGTH_LONG)
                    .setAction("НАСТРОЙКИ", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //startActivity(networkSetting);
                        }
                    });
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

        String Week;

        JSONObject dataJsonObj = null;
        String secondName = "";

        // определение недели
        Calendar c = Calendar.getInstance();
        int WeekOfYear = c.get(Calendar.WEEK_OF_YEAR);
        if(WeekOfYear % 2 == 0) {
            Week="1";
        } else {
            Week="2";
        }

        // определения дня
        int DayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        //Если воскресенье, то переключить недели
        if (DayOfWeek == 1)
        {
            if (Week.equals("2")){
                Week = "1";
            }
            else if (Week.equals("1")){
                Week = "2";
            }
        }

        try {
            if (strJson == null)
            {
                // intent = new Intent(view.getContext(), SettingActivity.class);
                //startActivity(intent);
            }
            else
            {
                dataJsonObj = new JSONObject(strJson);
                // номер недели
                JSONObject week = dataJsonObj.getJSONObject(Week);

                // день недели
                JSONObject day = week.getJSONObject(Integer.toString(DayOfWeek));

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
            e.printStackTrace();
            Les1[1] = "";
            Les1[0] = "Завтра выходной";
            Les1[3] = "Проведите день с пользой";
            Les2 = null;
            Les3 = null;
            Les4 = null;
            return false;
        }
    }

    public class Update extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL(URL);

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
            if (strJson == null)
            {
                Toast toast = Toast.makeText(view.getContext(),
                        R.string.refresh_error, Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
                writeToFile(strJson, getActivity());
                String TimeTableJson = readFromFile(getActivity());
                ParsJson(TimeTableJson);
                initializeData();
                adapter = new RVAdapter(lessonses, getContext());
                rv.setAdapter(new AlphaInAnimationAdapter(adapter));
                mSwipeRefreshLayout.setRefreshing(false);
                Fragment activeFragment = getFragmentManager().getFragments().get(0);
                ((OneFragment)activeFragment).FromActivityRefresh();
            }
        }
    }
}
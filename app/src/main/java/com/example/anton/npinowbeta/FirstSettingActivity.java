package com.example.anton.npinowbeta;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FirstSettingActivity extends AppCompatActivity {

    public final static int REQUEST_CODE_OK = 0;

    SharedPreferences.Editor editor;

    private final int FACUTIES = 0;
    private final int CURS = 1;
    private final int GROUP = 2;
    int result = 0;

    String prefFac;
    String prefCurs;
    String prefGroup;
    String URL;
    String GroupURL;
    List<String> GroupList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setting);
        //ImageView FSImageView = (ImageView) findViewById(R.id.imageView);
        //FSImageView.setImageResource(R.drawable.ic_shortcut_2);
        if(!isOnline()){
            Toast.makeText(getApplication(), "Для первоначальной настройки требуется Интернет. Повторите попытку позже.", Toast.LENGTH_SHORT).show();
        }
        Button EndButton = (Button) findViewById(R.id.doneBtn);
        EndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()){
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    if(!sharedPref.contains("prefFac"))
                    {
                        Toast.makeText(getApplication(), "Сначала выберите факультет", Toast.LENGTH_SHORT).show();
                    }
                    else if (!sharedPref.contains("prefCurs"))
                    {
                        Toast.makeText(getApplication(),  "Сначала выберите курс", Toast.LENGTH_SHORT).show();
                    }
                    else if (!sharedPref.contains("prefGroup"))
                    {
                        Toast.makeText(getApplication(),  "Сначала выберите группу", Toast.LENGTH_SHORT).show();
                    }
                    else if(sharedPref.contains("prefGroup")&&sharedPref.contains("prefCurs")&&
                            sharedPref.contains("prefFac"))
                    {
                        result = 3;
                        prefFac = sharedPref.getString("prefFac", "");
                        prefCurs = sharedPref.getString("prefCurs", "");
                        prefGroup = sharedPref.getString("prefGroup", "");
                        URL = "http://schedule.npi-tu.ru/data/?type=schedule&group="+prefGroup+"&faculty="+prefFac+"&kurs="+prefCurs;
                        new Update().execute();
                    }
                }
                else {
                    Toast.makeText(getApplication(), "Для первоначальной настройки требуется Интернет. Повторите попытку позже.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String[] SettingArray = new String[] { "Ваш факультет?", "На каком Вы курсе?", "В какой группе учитесь?"};

        // Statusbar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));

        ListView listView = (ListView) findViewById(R.id.setting_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplication(),
                R.layout.list_black_text, R.id.list_content, SettingArray);

        editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent;
                switch (position)
                {
                    case 0:
                        intent = new Intent(getApplicationContext(), FacSettingActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_OK);
                        overridePendingTransition(R.anim.left_to_right,
                                R.anim.right_to_left);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), CursActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_OK);
                        overridePendingTransition(R.anim.left_to_right,
                                R.anim.right_to_left);
                        break;
                    case 2:
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        if((sharedPref.contains("prefFac"))&&(sharedPref.contains("prefCurs")))
                        {
                            intent = new Intent(getApplicationContext(), GroupSettingActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_OK);
                            overridePendingTransition(R.anim.left_to_right,
                                    R.anim.right_to_left);
                        }
                        else {
                            Toast.makeText(getApplication(), "Сначала выберите факультет и курс", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    default:
                        break;
                }
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("data", MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            //Snackbar.make(, R.string.refresh_error, Snackbar.LENGTH_LONG)
                   // .setAction("НАСТРОЙКИ", new View.OnClickListener() {
                      //  @Override
                       // public void onClick(View view) {
                            //startActivity(networkSetting);
                  //      }
                  //  });
        }
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;

    }

    private class Update extends AsyncTask<Void, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(FirstSettingActivity.this);
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected void onPreExecute()
        {
            if (dialog!=null){
                this.dialog.setMessage("Подождите, скоро Ваше расписание загрузится");
                this.dialog.show();
            }
            //do initialization of required objects objects here
        };
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
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            // енкод
            JSON_encode json_encode = new JSON_encode();
            strJson = json_encode.encode(strJson);
            if (strJson == null)
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        R.string.refresh_error, Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
                Intent resultIntent = new Intent();
                writeToFile(strJson, getApplicationContext());
                resultIntent.putExtra("result", result);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }
    }

    private class GroupLoad extends AsyncTask<Void, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(FirstSettingActivity.this);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        @Override
        protected void onPreExecute()
        {
            if (dialog!=null){
                this.dialog.setMessage("Подождите, скоро список групп загрузится");
                this.dialog.show();
            }
            //do initialization of required objects objects here
        };

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL(GroupURL);

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
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String Fac = sharedPref.getString("prefFac", "2");
            String Curs = sharedPref.getString("prefCurs", "3");
            // енкод
            JSON_encode json_encode = new JSON_encode();
            strJson = json_encode.encode(strJson);
            if (strJson == null)
            {
                finish();
            }
            else
            {
                JSONObject dataJsonFac = null;
                JSONObject JsonGroup = null;

                try{
                    dataJsonFac = new JSONObject(strJson);
                    JsonGroup = dataJsonFac.getJSONObject(Fac);
                    JSONArray ArNames1 = JsonGroup.names();

                    JSONObject[] names1 = new JSONObject[ArNames1.length()];
                    for(int i = 0; i < ArNames1.length(); i++) {
                        names1[i] = JsonGroup.getJSONObject(ArNames1.getString(i));

                        JSONArray names2 = names1[i].getJSONArray(Curs);
                        for (int k = 0; k < names2.length(); k++) {
                            GroupList.add(names2.getString(k).replaceAll("\"|\\[|\\]", ""));
                        }
                    }
                }
                catch (JSONException e){
                    finish();
                }

                showDialog(GROUP);
            }

        }
    }
}
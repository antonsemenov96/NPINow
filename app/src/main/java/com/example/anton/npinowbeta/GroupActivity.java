package com.example.anton.npinowbeta;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class GroupActivity extends AppCompatActivity {

    String Fac;
    String Curs;
    String URL;
    ListView groupList;
    List<String> GroupList = new ArrayList<String>();
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        setTitle("Группы");
        groupList = (ListView) findViewById(R.id.group_list);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Fac = sharedPref.getString("prefFac", "2");
        Curs = sharedPref.getString("prefCurs", "3");
        URL = "http://schedule.npi-tu.ru/data/?type=group&faculty="+Fac+"&kurs="+Curs;
        new Update().execute();
    }

    private class Update extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        @Override
        protected void onPreExecute()
        {
            progressDialog = ProgressDialog.show(GroupActivity.this, "Загрузка","Подождите, список групп скоро загрузится", true);
            //do initialization of required objects objects here
        };

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
            progressDialog.dismiss();
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplication(),
                        R.layout.list_black_text, R.id.list_content, GroupList);
                groupList.setAdapter(adapter);
                groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        String selectedFromList = (groupList.getItemAtPosition(position)).toString();
                        editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
                        editor.putString("prefGroup", selectedFromList.split("-")[2]);
                        editor.commit();
                        finish();
                    }
                });
            }

        }
    }

}

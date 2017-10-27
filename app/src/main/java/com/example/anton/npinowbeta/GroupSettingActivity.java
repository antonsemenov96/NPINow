package com.example.anton.npinowbeta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class GroupSettingActivity extends AppCompatActivity {

    String Fac;
    String Curs;
    String URL;
    List<String> GroupList = new ArrayList<String>();
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    int result = 0;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_setting);
        setTitle("Группы");
        listView = (ListView) findViewById(R.id.group_list);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Fac = sharedPref.getString("prefFac", "");
        Curs = sharedPref.getString("prefCurs", "");
        URL = "http://schedule.npi-tu.ru/data/?type=group&faculty="+Fac+"&kurs="+Curs;
        new Update().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right_close,
                R.anim.right_to_left_close);
    }

    private class Update extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        @Override
        protected void onPreExecute()
        {
            progressDialog = ProgressDialog.show(GroupSettingActivity.this, "Загрузка","Подождите, список групп скоро загрузится", true);
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

                listView = (ListView) findViewById(R.id.group_list);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplication(),
                        R.layout.list_black_text, R.id.list_content, GroupList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        String selectedFromList = GroupList.get(position);
                        editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
                        editor.putString("prefGroup", selectedFromList.split("-")[2]);
                        editor.commit();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("result", result);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                        overridePendingTransition(R.anim.left_to_right_close,
                                R.anim.right_to_left_close);
                    }
                });
            }

        }
    }
}

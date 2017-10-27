package com.example.anton.npinowbeta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LessonViewActivity extends AppCompatActivity {

    Toolbar toolbar;
    AppBarLayout appBarLayout;
    android.support.design.widget.CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab;
    ImageView teacherPhoto;
    private ListView listView1;
    ProgressDialog progressDialog;
    String ImageURL;
    Context context;
    ListView list;
    TextView teacherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        teacherPhoto = (ImageView) findViewById(R.id.header);
        list = (ListView)findViewById(R.id.list);
        teacherName = (TextView)findViewById(R.id.teach_name_header);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        String room = intent.getStringExtra("room");
        final String discName = intent.getStringExtra("discName");
        String prepName = intent.getStringExtra("prepName");
        String discType = intent.getStringExtra("discType");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(view.getContext(), NewTaskActivity.class);
                //intent.putExtra("title", discName);
                //view.getContext().startActivity(intent);
                Snackbar.make(view, "Скоро...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();// возврат на предыдущий activity
            }
        });
        setTitle("");
        context = this;
        if (new String("ЛЕК").equals(discType))
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.LecTime));
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.LecAccent)));
            LoadData(prepName);
        }
        else if (new String("ПР").equals(discType))
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.PrTime));
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.PrAccent)));
            LoadData(prepName);
        }
        else if (new String("ЛАБ").equals(discType))
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.LabTime));
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.LabAccent)));
            LoadData(prepName);
        }

        else if (new String("СЕМ").equals(discType))
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.SemTime));
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.SemAccent)));
            LoadData(prepName);
        }
        else
        {
            teacherName.setText("Нет занятия");
            toolbar.setBackgroundColor(getResources().getColor(R.color.Pr));
            appBarLayout.setBackgroundColor(getResources().getColor(R.color.Pr));
            collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.Pr));
            getWindow().setStatusBarColor(getResources().getColor(R.color.PrTime));
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.PrAccent)));
        }


    }
    void LoadData(String name){
        try {
            ImageURL = "http://faculty.npi-tu.ru/api?name=" + name;
            ImageURL = ImageURL.replace(" ", "%20");
            URLEncoder.encode(ImageURL, "UTF-8");
            new LoadImageData().execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private class LoadImageData extends AsyncTask<Void, String, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        @Override
        protected void onPreExecute()
        {
            progressDialog = ProgressDialog.show(LessonViewActivity.this, "Загрузка","Подождите, данные скоро загрузятся", true);
        };

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(ImageURL);
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
                progressDialog.dismiss();
            }
            else {
                JSONObject dataJson = null;
                try {
                    dataJson = new JSONObject(strJson);
                    final String fullTeachName = dataJson.getString("name");
                    String title = dataJson.getString("title");
                    String urlImage = dataJson.getString("photo");
                    Picasso.with(context)
                            .load("http://faculty.npi-tu.ru/"+urlImage.replace("\\", ""))
                            .resize(800, 1200)
                            .into(teacherPhoto, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    teacherName.setText(fullTeachName);
                                    Intent intent = getIntent();
                                    String time = intent.getStringExtra("time");
                                    String room = intent.getStringExtra("room");
                                    String discName = intent.getStringExtra("discName");
                                    String[] web = {
                                            time,
                                            room,
                                            discName,
                                    } ;
                                    Integer[] imageId = {
                                            R.drawable.ic_lesson_time,
                                            R.drawable.ic_room,
                                            R.drawable.ic_disc,
                                    };

                                    ListAdapter adapter = new ListAdapter((Activity)context, web, imageId);
                                    list.setAdapter(adapter);
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                int position, long id) {
                                            //Toast.makeText(this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onError() {
                                    progressDialog.dismiss();
                                }
                            });
                }
                catch (JSONException e){
                    finish();
                }
            }
        }
    }
}

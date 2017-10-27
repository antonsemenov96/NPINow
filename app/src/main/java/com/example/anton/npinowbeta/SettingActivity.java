package com.example.anton.npinowbeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SettingActivity extends PreferenceActivity {
    Preference dataBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        dataBtn = findPreference("prefGroup");
        dataBtn.setSummary(sharedPref.getString("prefGroup", ""));
        dataBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if((sharedPref.contains("prefFac"))&&(sharedPref.contains("prefCurs")))
                {
                    Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplication(), "Сначала выберите факультет и курс", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        dataBtn = findPreference("prefGroup");
        dataBtn.setSummary(sharedPref.getString("prefGroup", ""));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

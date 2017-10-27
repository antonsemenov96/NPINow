package com.example.anton.npinowbeta;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FacSettingActivity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    int result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_setting);
        setTitle("Факультет");
        // Statusbar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));

        final String[] facArray = {
                "Аспирантура", "Военный институт", "Институт международного образования",
                "Институт физического воспитания и спорта", "Институт фундаментального инженерного образования",
                "Механический факультет", "Новочеркасский политехнический колледж", "Строительный факультет",
                "Технологический факультет", "Факультет геологии, горного и нефтегазового дела",
                "Факультет инноватики и организации производства", "Факультет информационных технологий и управления",
                "Электромеханический факультет", "Энергетический факультет"
        };
        ListView listView = (ListView) findViewById(R.id.fac_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplication(),
                R.layout.list_black_text, R.id.list_content, facArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
                switch (position) {
                    case 0:
                        editor.putString("prefFac", "9");
                        editor.commit();
                        break;
                    case 1:
                        editor.putString("prefFac", "C");
                        editor.commit();
                        break;
                    case 2:
                        editor.putString("prefFac", "E");
                        editor.commit();
                        break;
                    case 3:
                        editor.putString("prefFac", "4");
                        editor.commit();
                        break;
                    case 4:
                        editor.putString("prefFac", "A");
                        editor.commit();
                        break;
                    case 5:
                        editor.putString("prefFac", "5");
                        editor.commit();
                        break;
                    case 6:
                        editor.putString("prefFac", "F");
                        editor.commit();
                        break;
                    case 7:
                        editor.putString("prefFac", "8");
                        editor.commit();
                        break;
                    case 8:
                        editor.putString("prefFac", "7");
                        editor.commit();
                        break;
                    case 9:
                        editor.putString("prefFac", "1");
                        editor.commit();
                        break;
                    case 10:
                        editor.putString("prefFac", "B");
                        editor.commit();
                        break;
                    case 11:
                        editor.putString("prefFac", "2");
                        editor.commit();
                        break;
                    case 12:
                        editor.putString("prefFac", "3");
                        editor.commit();
                        break;
                    case 13:
                        editor.putString("prefFac", "6");
                        editor.commit();
                        break;
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", result);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                overridePendingTransition(R.anim.left_to_right_close,
                        R.anim.right_to_left_close);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right_close,
                R.anim.right_to_left_close);
    }
}

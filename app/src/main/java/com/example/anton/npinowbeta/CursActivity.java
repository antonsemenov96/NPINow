package com.example.anton.npinowbeta;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CursActivity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    int result = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curs);
        setTitle("Факультет");
        // Statusbar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        final String[] cursArray = { "Первом", "Втором", "Третем", "Четвертом" };
        ListView listView = (ListView) findViewById(R.id.fac_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplication(),
                R.layout.list_black_text, R.id.list_content, cursArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
                editor.putString("prefCurs", Integer.toString(position+1));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right_close,
                R.anim.right_to_left_close);
    }
}

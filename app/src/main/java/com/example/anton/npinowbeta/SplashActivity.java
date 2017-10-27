package com.example.anton.npinowbeta;

/**
 * Created by tonny on 12.04.2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

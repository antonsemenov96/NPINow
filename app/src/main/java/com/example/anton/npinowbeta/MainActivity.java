package com.example.anton.npinowbeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class MainActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    // Объявление вкладок (табов)
    private int[] tabIcons = {
            R.mipmap.ic_now,
            R.mipmap.ic_tomm,
            //R.mipmap.ic_update_black_48dp
    };
    public final static int REQUEST_CODE_OK = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Панель инструментов
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setSupportActionBar(toolbar);
        // Кнопка "Назад"
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String prefFac = sharedPref.getString("prefFac", "");
        String prefCurs = sharedPref.getString("prefCurs", "");
        String prefGroup = sharedPref.getString("prefGroup", "");
        if(prefFac.equals("")||prefCurs.equals("")||prefGroup.equals(""))
        {
            Intent intent = new Intent(getApplicationContext(), FirstSettingActivity.class);
            startActivityForResult(intent, REQUEST_CODE_OK);
        }
        // Просмотрщик табов
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Сами табы
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setTitle("Сейчас");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    setTitle("Сейчас");
                }
                else if(tab.getPosition() == 1){
                    setTitle("Завтра");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //changeThemes();
        setupTabIcons();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            ShortcutInfo shortcut_1 = new ShortcutInfo.Builder(this, "id1")
                    .setShortLabel("Преподаватели")
                    .setLongLabel("Открыть на сайте НПИ")
                    .setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_shortcut_2))
                    .setIntent(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://schedule.npi-tu.ru/application/prep")))
                    .build();
            shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut_1));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_timetable){
            Intent intent = new Intent(this, WeekActivity.class);
            startActivity(intent);
            return true;
        }
        /*else if (id == R.id.action_task){
            Intent intent = new Intent(this, TaskActivity.class);
            startActivity(intent);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    // Темы
    private void changeThemes(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String Themes = sharedPref.getString("prefTheme", "0");
        if (Themes.contentEquals("0"))
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        }
        else if (Themes.contentEquals("1"))
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.Dark_colorPrimaryDark));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.Dark_colorPrimary));
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.Dark_colorAccent));
        }
    }

    // Установка значков табов
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    // Настройка просмотрщика табов
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Добавление фрагментов
        adapter.addFragment(new OneFragment(), "Сейчас");
        adapter.addFragment(new TomFragment(), "Завтра");
        //adapter.addFragment(new TimeFragment(), "Неделя");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
           //
            if(position == 0){
                setTitle("Сейчас");
            }
            else if (position == 1){
                setTitle("Сейчас");
            }
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        //@Override
       // public CharSequence getPageTitle(int position) {
        //    return mFragmentTitleList.get(position);
        //}

        @Override
        public CharSequence getPageTitle(int position) {
            //return null to display only the icon
            return null;
        }
    }
}


package com.example.anton.npinowbeta;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class TaskActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView Log;
    TaskAdapter adapter;
    RecyclerView rv;
    List<String[]> Task;
    List<int[]> IDs;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Задания");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewTaskActivity.class);
                startActivity(intent);
            }
        });
        dbHelper = new DBHelper(this);
        //Log = (TextView) findViewById(R.id.LOG);

        rv = (RecyclerView) findViewById(R.id.rv_task);
        rv.setNestedScrollingEnabled(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            rv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
        else{
            rv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        }
        rv.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("taskTable", null, null, null, null, null, null);
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        Task = new ArrayList<String[]>();
        IDs = new ArrayList<int[]>();
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int idNotIndex = c.getColumnIndex("not_id");
            int titleColIndex = c.getColumnIndex("title");
            int taskColIndex = c.getColumnIndex("task");
            int colorColIndex = c.getColumnIndex("color");

            do {
                Task.add(new String[] {c.getString(colorColIndex), c.getString(titleColIndex), c.getString(taskColIndex)});
                IDs.add(new int[] {idColIndex, idNotIndex});

                // получаем значения по номерам столбцов и пишем все в лог
                //Log.setText("ID = " + c.getInt(idColIndex) +
                //      ", color = " + c.getString(colorColIndex) +
                //    ", title = " + c.getString(titleColIndex) +
                //  ", task = " + c.getString(taskColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        }
        else {
            Task.add(new String[] {"white", "Нет заданий", "Пошел нахер"});
            IDs.add(new int[] {0, 0});
        }

        //Log.setText("0 rows");
        c.close();


        adapter = new TaskAdapter(IDs, Task, getApplicationContext());
        rv.setAdapter(new AlphaInAnimationAdapter(adapter));
    }
}

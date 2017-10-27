package com.example.anton.npinowbeta;

/**
 * Created by anton on 02.01.17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anton on 29.12.16.
 */

public  class RVAdapter extends RecyclerView.Adapter<RVAdapter.LessonViewHolder>{

    List<Lesson> lessons;
    private Context context;

    RVAdapter(List<Lesson> lessons, Context curr){
        this.lessons = lessons;
        this.context = curr;
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView time;
        TextView prepName;
        TextView discName;
        TextView discType;

        LessonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            time = (TextView)itemView.findViewById(R.id.time_label);
            discName = (TextView)itemView.findViewById(R.id.disc_name);
            prepName = (TextView) itemView.findViewById(R.id.prep_name);
            discType = (TextView) itemView.findViewById(R.id.disc_type);
        }
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    @Override
    public LessonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_now, viewGroup, false);
        LessonViewHolder lvh = new LessonViewHolder(v);
        return lvh;
    }

    @Override
    public void onBindViewHolder(LessonViewHolder personViewHolder, int i) {
        final int CardNum = i;
        String discTypeStr;
        String roomStr;
        personViewHolder.time.setText(lessons.get(i).time);
        personViewHolder.discName.setText(lessons.get(i).discName);
        personViewHolder.prepName.setText(lessons.get(i).prepName);
        roomStr = lessons.get(i).room;
        discTypeStr = lessons.get(i).discType;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean color = sharedPref.getBoolean("prefColors", true);
        // Заголовок
        if (new String("ЛЕК").equals(discTypeStr))
        {
            personViewHolder.discType.setText("Лекция в "+roomStr);
            if(color)
            {
                personViewHolder.discType.setBackgroundColor(context.getResources().getColor(R.color.Lec));
                personViewHolder.time.setBackgroundColor(context.getResources().getColor(R.color.LecTime));
            }
            else
            {
                personViewHolder.discType.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                personViewHolder.time.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                personViewHolder.discType.setPadding(42,5,42,5);
                personViewHolder.time.setPadding(42,32,42,5);
                personViewHolder.discName.setPadding(42, 5, 42, 5);
                personViewHolder.discType.setPadding(42, 5, 42, 5);
                personViewHolder.prepName.setPadding(42, 5, 42, 32);
            }
        }
        else if (new String("ПР").equals(discTypeStr))
        {
            personViewHolder.discType.setText("Практическое занятие в "+roomStr);
            if(color)
            {
                personViewHolder.discType.setBackgroundColor(context.getResources().getColor(R.color.Pr));
                personViewHolder.time.setBackgroundColor(context.getResources().getColor(R.color.PrTime));
            }
            else
            {
                personViewHolder.discType.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                personViewHolder.time.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                personViewHolder.discType.setPadding(42,5,42,5);
                personViewHolder.time.setPadding(42,32,42,5);
                personViewHolder.discName.setPadding(42, 5, 42, 5);
                personViewHolder.discType.setPadding(42, 5, 42, 5);
                personViewHolder.prepName.setPadding(42, 5, 42, 32);
            }
        }
        else if (new String("ЛАБ").equals(discTypeStr))
        {
            personViewHolder.discType.setText("Лабораторная работа в "+roomStr);
            if(color)
            {
                personViewHolder.discType.setBackgroundColor(context.getResources().getColor(R.color.Lab));
                personViewHolder.time.setBackgroundColor(context.getResources().getColor(R.color.LabTime));
            }
            else
            {
                personViewHolder.discType.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                personViewHolder.time.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                personViewHolder.discType.setPadding(42,5,42,5);
                personViewHolder.time.setPadding(42,32,42,5);
                personViewHolder.discName.setPadding(42, 5, 42, 5);
                personViewHolder.discType.setPadding(42, 5, 42, 5);
                personViewHolder.prepName.setPadding(42, 5, 42, 32);
            }
        }

        else if (new String("СЕМ").equals(discTypeStr)) {
            personViewHolder.discType.setText("Семинар в " + roomStr);
            if(color)
            {
                personViewHolder.discType.setBackgroundColor(context.getResources().getColor(R.color.Sem));
                personViewHolder.time.setBackgroundColor(context.getResources().getColor(R.color.SemTime));
            }
            else
            {
                personViewHolder.discType.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                personViewHolder.time.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                personViewHolder.discType.setPadding(42,5,42,5);
                personViewHolder.time.setPadding(42,32,42,5);
                personViewHolder.discName.setPadding(42, 5, 42, 5);
                personViewHolder.discType.setPadding(42, 5, 42, 5);
                personViewHolder.prepName.setPadding(42, 5, 42, 32);
            }
        }
        else
        {
            personViewHolder.discType.setText(""+roomStr);
            if(color)
            {
                personViewHolder.discType.setBackgroundColor(context.getResources().getColor(R.color.Pr));
                personViewHolder.time.setBackgroundColor(context.getResources().getColor(R.color.PrTime));
            }
            else
            {
                personViewHolder.discType.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                personViewHolder.time.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                personViewHolder.discType.setPadding(42,5,42,5);
                personViewHolder.time.setPadding(42,32,42,5);
                personViewHolder.discName.setPadding(42, 5, 42, 5);
                personViewHolder.discType.setPadding(42, 5, 42, 5);
                personViewHolder.prepName.setPadding(42, 5, 42, 32);
            }
        }
        // Обработка нажатия

        personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LessonViewActivity.class);
                intent.putExtra("time", lessons.get(CardNum).time);
                intent.putExtra("room", lessons.get(CardNum).room);
                intent.putExtra("discName", lessons.get(CardNum).discName);
                intent.putExtra("prepName", lessons.get(CardNum).prepName);
                intent.putExtra("discType", lessons.get(CardNum).discType);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
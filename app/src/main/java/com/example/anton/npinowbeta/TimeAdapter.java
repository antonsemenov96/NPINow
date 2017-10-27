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

public  class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.DayViewHolder>{

    List<Day> days;
    private Context context;
    String discType;
    String Week;

    TimeAdapter(List<Day> days, Context curr){
        this.days = days;
        this.context = curr;
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView onetime;
        TextView oneroom;
        TextView onedisc;

        TextView twotime;
        TextView tworoom;
        TextView twodisc;

        TextView threetime;
        TextView threeroom;
        TextView threedisc;

        TextView fourtime;
        TextView fourroom;
        TextView fourdisc;

        DayViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            name = (TextView)itemView.findViewById(R.id.name);

            onetime = (TextView)itemView.findViewById(R.id.onetime);
            onedisc = (TextView)itemView.findViewById(R.id.onedisc);
            oneroom = (TextView)itemView.findViewById(R.id.oneroom);

            twotime = (TextView)itemView.findViewById(R.id.twotime);
            twodisc = (TextView)itemView.findViewById(R.id.twodisc);
            tworoom = (TextView)itemView.findViewById(R.id.tworoom);

            threetime = (TextView)itemView.findViewById(R.id.threetime);
            threedisc = (TextView)itemView.findViewById(R.id.threedisc);
            threeroom = (TextView)itemView.findViewById(R.id.threeroom);

            fourtime = (TextView)itemView.findViewById(R.id.fourtime);
            fourdisc = (TextView)itemView.findViewById(R.id.fourdisc);
            fourroom = (TextView)itemView.findViewById(R.id.fourroom);
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_time, viewGroup, false);
        DayViewHolder dvh = new DayViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(DayViewHolder personViewHolder, int i) {
        final int CardNum = i;
        String discTypeStr;
        String roomStr;
        personViewHolder.name.setText(days.get(i).Name);

        personViewHolder.onetime.setText(days.get(i).onetime);
        personViewHolder.onedisc.setText(days.get(i).onediscname);
        personViewHolder.oneroom.setText(days.get(i).oneroom);

        personViewHolder.twotime.setText(days.get(i).twotime);
        personViewHolder.twodisc.setText(days.get(i).twodiscname);
        personViewHolder.tworoom.setText(days.get(i).tworoom);

        personViewHolder.threetime.setText(days.get(i).threetime);
        personViewHolder.threedisc.setText(days.get(i).threediscname);
        personViewHolder.threeroom.setText(days.get(i).threeroom);

        personViewHolder.fourtime.setText(days.get(i).fourtime);
        personViewHolder.fourdisc.setText(days.get(i).fourdiscname);
        personViewHolder.fourroom.setText(days.get(i).fourroom);

        Week = days.get(i).Week;

        checkType(personViewHolder.onetime, personViewHolder.onedisc, personViewHolder.oneroom,
                days.get(i).onedisctype, days.get(i).oneroom);
        checkType(personViewHolder.twotime, personViewHolder.twodisc, personViewHolder.tworoom,
                days.get(i).twodisctype, days.get(i).tworoom);
        checkType(personViewHolder.threetime, personViewHolder.threedisc, personViewHolder.threeroom,
                days.get(i).threedisctype, days.get(i).threeroom);
        checkType(personViewHolder.fourtime, personViewHolder.fourdisc, personViewHolder.fourroom,
                days.get(i).fourdisctype, days.get(i).fourroom);

        personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DayActivity.class);
                intent.putExtra("name", CardNum);
                intent.putExtra("week", Week);
                view.getContext().startActivity(intent);
            }
        });
    }

    public void checkType (TextView time, TextView disc, TextView room, String discTypeStr, String roomStr){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean color = sharedPref.getBoolean("prefColors", true);

        if (new String("ЛЕК").equals(discTypeStr))
        {
            //room.setText("Лекция в "+roomStr);
            if(color)
            {
                time.setBackgroundColor(context.getResources().getColor(R.color.LecTime));
                disc.setBackgroundColor(context.getResources().getColor(R.color.Lec));
                room.setBackgroundColor(context.getResources().getColor(R.color.Lec));
            }
            else
            {
                time.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                disc.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                room.setTextColor(context.getResources().getColor(R.color.colorTitleText));
            }
        }
        else if (new String("ПР").equals(discTypeStr))
        {
            //room.setText("Практ. в "+roomStr);
            if(color)
            {
                time.setBackgroundColor(context.getResources().getColor(R.color.PrTime));
                disc.setBackgroundColor(context.getResources().getColor(R.color.Pr));
                room.setBackgroundColor(context.getResources().getColor(R.color.Pr));
            }
            else
            {
                time.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                disc.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                room.setTextColor(context.getResources().getColor(R.color.colorTitleText));
            }
        }
        else if (new String("ЛАБ").equals(discTypeStr))
        {
            //room.setText("Лаб. "+roomStr);
            if(color)
            {
                disc.setBackgroundColor(context.getResources().getColor(R.color.Lab));
                room.setBackgroundColor(context.getResources().getColor(R.color.Lab));
                time.setBackgroundColor(context.getResources().getColor(R.color.LabTime));
            }
            else
            {
                time.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                disc.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                room.setTextColor(context.getResources().getColor(R.color.colorTitleText));
            }
        }

        else if (new String("СЕМ").equals(discTypeStr)) {
            //room.setText("Семинар в " + roomStr);
            if(color)
            {
                disc.setBackgroundColor(context.getResources().getColor(R.color.Sem));
                room.setBackgroundColor(context.getResources().getColor(R.color.Sem));
                time.setBackgroundColor(context.getResources().getColor(R.color.SemTime));
            }
            else
            {
                time.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                disc.setTextColor(context.getResources().getColor(R.color.colorTitleText));
                room.setTextColor(context.getResources().getColor(R.color.colorTitleText));
            }
        }
        else
        {
            //room.setText(""+roomStr);
            if(color)
            {
                disc.setBackgroundColor(context.getResources().getColor(R.color.No));
                room.setBackgroundColor(context.getResources().getColor(R.color.No));
                time.setBackgroundColor(context.getResources().getColor(R.color.NoTime));
                time.setTextColor(context.getResources().getColor(R.color.colorGrey));
                disc.setTextColor(context.getResources().getColor(R.color.colorGrey));
                room.setTextColor(context.getResources().getColor(R.color.colorGrey));
            }
            else
            {
                time.setTextColor(context.getResources().getColor(R.color.colorGrey));
                disc.setTextColor(context.getResources().getColor(R.color.colorGrey));
                room.setTextColor(context.getResources().getColor(R.color.colorGrey));
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
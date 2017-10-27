package com.example.anton.npinowbeta;

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
 * Created by tonny on 19.03.2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    List<int[]> IDs;
    List<String[]> Task;
    private Context context;

    TaskAdapter(List<int[]>id, List<String[]> task, Context curr) {
        this.IDs = id;
        this.Task = task;
        this.context = curr;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView task;
        TextView notification_stat;

        TaskViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.title);
            task = (TextView) itemView.findViewById(R.id.task);
            notification_stat = (TextView) itemView.findViewById(R.id.notification_stat);
        }
    }

    @Override
    public int getItemCount() {
        return Task.size();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_task, viewGroup, false);
        TaskViewHolder tvh = new TaskViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder personViewHolder, int i) {
        final int CardNum = i;
        String discTypeStr;
        String roomStr;
        personViewHolder.title.setText(Task.get(i)[1]);
        personViewHolder.task.setText(Task.get(i)[2]);

        // Обработка нажатия
        personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewTaskActivity.class);
                intent.putExtra("color", Task.get(CardNum)[0]);
                intent.putExtra("title", Task.get(CardNum)[1]);
                intent.putExtra("task", Task.get(CardNum)[2]);
                intent.putExtra("id", IDs.get(CardNum)[0]);
                intent.putExtra("not_id", IDs.get(CardNum)[1]);
                view.getContext().startActivity(intent);
            }
        });
        if (Task.get(CardNum)[0].equals("white")){
            personViewHolder.title.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            personViewHolder.task.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            personViewHolder.notification_stat.setBackgroundColor(context.getResources().getColor(R.color.WHITE_NOTE));

        }
        else if (Task.get(CardNum)[0].equals("red")){
            //personViewHolder.cv.setBackgroundColor(context.getResources().getColor(R.color.RED));
            personViewHolder.title.setBackgroundColor(context.getResources().getColor(R.color.RED_ACCENT));
            personViewHolder.task.setBackgroundColor(context.getResources().getColor(R.color.RED));
            personViewHolder.notification_stat.setBackgroundColor(context.getResources().getColor(R.color.RED_NOTE));
        }
        else if (Task.get(CardNum)[0].equals("orange")){
            //personViewHolder.cv.setBackgroundColor(context.getResources().getColor(R.color.ORANGE));
            personViewHolder.title.setBackgroundColor(context.getResources().getColor(R.color.ORANGE_ACCENT));
            personViewHolder.task.setBackgroundColor(context.getResources().getColor(R.color.ORANGE));
            personViewHolder.notification_stat.setBackgroundColor(context.getResources().getColor(R.color.ORANGE_NOTE));
        }
        else if (Task.get(CardNum)[0].equals("green")){
            //personViewHolder.cv.setBackgroundColor(context.getResources().getColor(R.color.GREEN));
            personViewHolder.title.setBackgroundColor(context.getResources().getColor(R.color.GREEN_ACCENT));
            personViewHolder.task.setBackgroundColor(context.getResources().getColor(R.color.GREEN));
            personViewHolder.notification_stat.setBackgroundColor(context.getResources().getColor(R.color.GREEN_NOTE));
        }
        else if (Task.get(CardNum)[0].equals("blue")){
            //personViewHolder.cv.setBackgroundColor(context.getResources().getColor(R.color.BLUE));
            personViewHolder.title.setBackgroundColor(context.getResources().getColor(R.color.BLUE_ACCENT));
            personViewHolder.task.setBackgroundColor(context.getResources().getColor(R.color.BLUE));
            personViewHolder.notification_stat.setBackgroundColor(context.getResources().getColor(R.color.BLUE_NOTE));
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}


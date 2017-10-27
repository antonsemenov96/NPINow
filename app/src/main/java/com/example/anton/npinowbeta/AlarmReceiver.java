package com.example.anton.npinowbeta;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by tonny on 19.03.2017.
 */


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String Title = intent.getStringExtra("title");
        String Task = intent.getStringExtra("task");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(Title)
                        .setContentText(Task)
                        .setDefaults(Notification.DEFAULT_SOUND |
                            Notification.DEFAULT_VIBRATE);
        Intent resultIntent = new Intent(context, TaskActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(TaskActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
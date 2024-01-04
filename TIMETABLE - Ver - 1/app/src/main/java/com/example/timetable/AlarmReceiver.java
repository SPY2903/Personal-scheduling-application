package com.example.timetable;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    final String CHANEL_ID = "201";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

//        Intent inte = new Intent(context, WeekViewActivity.class);
//        inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,inte,0);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"foxandroid")
//                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
//                .setContentTitle("Thông báo")
//                .setContentText("Bạn có sự kiện")
//                .setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(pendingIntent);
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
//        notificationManagerCompat.notify(123,builder.build());
        String time = intent.getStringExtra("time");
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        if(intent.getAction().equals("MyAction")){
            NotificationChannel channel = new NotificationChannel(CHANEL_ID,"Chanel 1",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Bạn có sự kiện");
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANEL_ID)
                .setContentTitle("Thông báo " + time)
                .setContentText("Bạn có sự kiện")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setCategory(NotificationCompat.CATEGORY_ALARM);
        notificationManager.notify(getNotificationId(),builder.build());
    }
    private int getNotificationId(){
        int time = (int) new Date().getTime();
        return time;
    }
}

package com.faayda;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by vinove on 21/9/15.
 */

public class ReminderReceiver extends  BroadcastReceiver{

    int NOTIFICATION_ID = 10;

    @Override
    public void onReceive(Context context, Intent intent) {

       // Toast.makeText(context,"Receiver",Toast.LENGTH_LONG).show();


      NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Enter cash spend").setContentText("Hey, just take 30 sec to enter today's cash transaction.");

        Intent targetIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
        builder.setAutoCancel(true);

    }
}

/*
package com.faayda.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.faayda.MainActivity;
import com.faayda.R;

*/
/**
 * Created by vinove on 24/8/15.
 *//*

public class CashNotification extends Service {
    int NOTIFICATION_ID = 1;
  //  NotifyServiceReceiver notifyServiceReceiver;
    public final static String ACTION = "NotifyServiceAction";
    public final static int RQS_STOP_SERVICE = 0;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
*/
/*
    @Override
    public void onCreate() {
        notifyServiceReceiver = new NotifyServiceReceiver();
        super.onCreate();
    }*//*


  */
/*  @Override
    public void onDestroy() {
        this.unregisterReceiver(notifyServiceReceiver);
        super.onDestroy();
    }
*//*

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
*/
/*        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);*//*

       // registerReceiver(notifyServiceReceiver, intentFilter);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Enter cash spend").setContentText("Hey, just take 30 sec to enter today's cash transaction.");

        Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
        builder.setAutoCancel(true);
        return START_STICKY;
    }
*/
/*
    public class NotifyServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            int rqs = arg1.getIntExtra("CASH", 0);
            if (rqs == RQS_STOP_SERVICE) {
                stopSelf();
            }
        }
    }*//*


}
*/

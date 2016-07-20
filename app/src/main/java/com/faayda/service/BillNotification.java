/*
package com.faayda.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;

import com.faayda.processor.TransactionMessageProcessor;
import com.faayda.utils.Constants;

import java.util.Calendar;

*/
/**
 * Created by vinove on 24/8/15.
 *//*

public class BillNotification extends Service {
    int NOTIFICATION_ID = 12345;
    NotifyServiceReceiver notifyServiceReceiver;
    public final static String ACTION = "NotifyServiceAction";
    String getHoursAndTime = "";
    public final static int RQS_STOP_SERVICE = 1;
    Calendar calendar;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        notifyServiceReceiver = new NotifyServiceReceiver();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver(notifyServiceReceiver);
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        registerReceiver(notifyServiceReceiver, intentFilter);

        // If bill Msg is find then generate counter and go to notification

        Uri inboxURI = Uri.parse("content://sms/inbox");
        String[] reqCols = new String[]{"_id", "address", "body", "date"};
        ContentResolver cr = getApplicationContext().getContentResolver();
        TransactionMessageProcessor processor = new TransactionMessageProcessor(getApplicationContext());

        String where = ""; //timestamp

        Cursor cur = cr.query(inboxURI, reqCols, where, null, "_id");
        String body;

        while (cur.moveToNext()) {
            body = cur.getString(cur.getColumnIndex(reqCols[2]));
            if (processor.isTransactionMessage(body) && processor.checkTransactionMessage(body)) {
                String msgType = processor.smsType(getApplicationContext(), body);

                if (msgType.equalsIgnoreCase(Constants.BILL)) {

                }

            }
        }
    }

    public class NotifyServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            int rqs = arg1.getIntExtra("BILL", 0);
            if (rqs == RQS_STOP_SERVICE) {
                stopSelf();
            }
        }
    }

}
*/

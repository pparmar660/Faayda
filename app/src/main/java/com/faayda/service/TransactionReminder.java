package com.faayda.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;

import java.util.Calendar;

/**
 * Created by vinove on 16/9/15.
 */
public class TransactionReminder extends Service {
    int NOTIFICATION_ID = 2;
    NotifyServiceReceiver notifyServiceReceiver;
    public final static String ACTION = "NotifyServiceAction";
    public final static int RQS_STOP_SERVICE = 0;
    DBHelper dbHelper;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notifyServiceReceiver = new NotifyServiceReceiver();

    }


    @Override
    public void onDestroy() {
        this.unregisterReceiver(notifyServiceReceiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        registerReceiver(notifyServiceReceiver, intentFilter);
        Context context = this;
        dbHelper = new DBHelper(context);
        dbHelper.open();

        // Calculate Yesterday spent amount with where it spent

        /*DBHelper dbHelper = new DBHelper(this);
        dbHelper.open();

        String amount = dbHelper.getValue(DBConstants.TRANSACTIONS, DBConstants.TRANSACTION_AMOUNT, null);
        String sum_amount = "SELECT SUM( " + DBConstants.TRANSACTION_AMOUNT + ") FROM " + DBConstants.TRANSACTIONS;*/

        String creditId = dbHelper.getValue(DBConstants.TRANSACTION_TYPE,
                DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.CREDIT + "'");

        String refund = dbHelper.getValue(DBConstants.TRANSACTION_TYPE,
                DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.REFUND + "'");
        Calendar calendar = Calendar.getInstance();
        try {

            calendar.add(Calendar.DATE, -1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String sqlQuery = "select sum(" + DBConstants.TRANSACTION_AMOUNT + ") from " + DBConstants.TRANSACTIONS + " where " + DBConstants.TRANSACTION_TYPE_ID +
                "!=" + creditId + " and " + DBConstants.TRANSACTION_TYPE_ID + " != " + refund + " and " + DBConstants.TRANSACTION_DATE + " > " + calendar.getTimeInMillis();
        Cursor transactionAmountCursor = dbHelper.getDataThroughRaw(sqlQuery);

        double amount = 0;

        if (transactionAmountCursor != null)
            if (transactionAmountCursor.moveToFirst())
                amount = transactionAmountCursor.getDouble(0);

        if (amount > 0) {
            int numberOfTraction = dbHelper.getTableRecordsCount(DBConstants.TRANSACTIONS, null, DBConstants.TRANSACTION_TYPE_ID +
                    "!=" + creditId + " and " + DBConstants.TRANSACTION_TYPE_ID + " != " + refund + " and " + DBConstants.TRANSACTION_DATE + " > " + calendar.getTimeInMillis(), null);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("Rs. " + CommonUtils.DoubleToStringLimits(amount) + " spent Yesterday").setContentText(numberOfTraction + " Transaction");

            Intent targetIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.notify(NOTIFICATION_ID, builder.build());
            builder.setAutoCancel(true);
        }
        return START_STICKY;
    }

    public class NotifyServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            int rqs = arg1.getIntExtra("SPENT", 0);
            if (rqs == RQS_STOP_SERVICE) {
                stopSelf();
            }
        }
    }
}

package com.faayda.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.ReminderReceiver;
import com.faayda.preferences.Preferences;
import com.faayda.service.TransactionReminder;
import com.faayda.utils.Constants;

import java.util.Calendar;

/**
 * Created by vinove on 8/11/2015.
 */
public final class Settings extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    Switch play_alarm, daily_alarm, reminder;
    String play_on = "a", daily_on = "b", reminder_on = "c";
    Preferences pref;
    Context mContext;
    AlarmManager alarm, alarmDaily;
    Intent myIntent, dailyIntent;
    PendingIntent tracking, dailyTacking;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.settings, container, false);
        mContext = getActivity();
        ((MainActivity) getActivity()).setTopBarTitle(Constants.SETTINGS);
        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
        ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);

        pref = new Preferences(getActivity());
        //   play_alarm = (Switch) layoutView.findViewById(R.id.sw_play_alarm);
        // play_alarm.setOnCheckedChangeListener(this);
        daily_alarm = (Switch) layoutView.findViewById(R.id.sw_daily_alarm);
        daily_alarm.setOnCheckedChangeListener(this);
        reminder = (Switch) layoutView.findViewById(R.id.sw_cash_reminder);
        reminder.setOnCheckedChangeListener(this);

        if (pref.getBoolean(play_on) == true) {
            play_alarm.setChecked(true);
        }
        if (pref.getBoolean(daily_on) == true) {
            daily_alarm.setChecked(true);
        }
        if (pref.getBoolean(reminder_on) == true) {
            reminder.setChecked(true);
        }
        return layoutView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
           /* case R.id.sw_play_alarm:  // FOR BILL REMINDER  @ 6PM
                if (isChecked) {
                    pref.setBoolean(play_on, true);
                    setBillAlarm(6, 1);//0 for am and 1 for pm
                } else {
                    pref.setBoolean(play_on, false);
                    cancel();
                    stopBillAlarm();
                }
                break;*/
            case R.id.sw_daily_alarm: // DAILY REPORT TO ENTER TRANSACTION @ 9PM
                if (isChecked) {
                    pref.setBoolean(daily_on, true);
                    setCashAlarm();//0 for am and 1 for pm
                } else {
                    pref.setBoolean(daily_on, false);
                    cancelDaily();
                    stopCashAlarm();
                }
                break;
            case R.id.sw_cash_reminder: // FOR LATEST TRANSACTION REMINDER  @ 9AM
                if (isChecked) {
                    pref.setBoolean(reminder_on, true);
                    setTransactionAlarm();//0 for am and 1 for pm
                } else {
                    pref.setBoolean(reminder_on, false);
                    cancel();
                    stopTransactionAlarm();
                }
                break;
            default:
                break;
        }
    }

    private void cancelDaily() {
        try {
            alarmDaily.cancel(dailyTacking);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCashAlarm() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        calendar.add(Calendar.SECOND, 0);
        Calendar currentTimeCalendar = Calendar.getInstance();
        if (currentTimeCalendar.getTimeInMillis() > calendar.getTimeInMillis()) {
            calendar.add(Calendar.DATE, 1);
        }

        // REMINDER TO ENTER CASH AT 9PM
     /*   Calendar currentTimeCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.HOUR_OF_DAY, 21);
       // calendar.set(Calendar.MINUTE, 0);
        calendar.add(Calendar.SECOND, 5);

//        calendar.set(Calendar.AM_PM, am_pm);

        if (currentTimeCalendar.getTimeInMillis() > calendar.getTimeInMillis()) {
           // calendar.add(Calendar.DATE, 1);
        }
        Toast.makeText(mContext, "Night 30Sec : " + calendar.getTime() + "", Toast.LENGTH_LONG).show();

        alarmDaily = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        dailyIntent = new Intent(mContext, ReminderReceiver.class);
        dailyTacking = PendingIntent.getService(mContext, 123456789, dailyIntent, 0);
        alarmDaily.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),5*1000, dailyTacking);*/

        Intent receiverIntent = new Intent(mContext, ReminderReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 123456789, receiverIntent, 0);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        //  alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), 5*1000, sender);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);

    }

    private void setTransactionAlarm() {  // REMINDER FOR LATEST TRANSACTION AT 9AM
        Calendar currentTimeCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

//        calendar.set(Calendar.AM_PM, am_pm);

        if (currentTimeCalendar.getTimeInMillis() > calendar.getTimeInMillis())
            calendar.add(Calendar.DATE, 1);
        alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        myIntent = new Intent(mContext, TransactionReminder.class);
        tracking = PendingIntent.getService(mContext, 0, myIntent, 0);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, tracking);
    }

    /*public void setBillAlarm(int hours, int am_pm) {  // REMINDER OF BILL's AT 6PM
        Calendar currentTimeCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.AM_PM, am_pm);//0 for am and 1 for pm

        if (currentTimeCalendar.getTimeInMillis() > calendar.getTimeInMillis())
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        myIntent = new Intent(mContext, BillNotification.class);
        tracking = PendingIntent.getService(mContext, 0, myIntent, 0);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, tracking);
    }*/

    public void cancel() {
        try {
            alarm.cancel(tracking);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopCashAlarm() {
        cancelDaily();
/*        Intent intent = new Intent();
        intent.setAction(CashNotification.ACTION);
        intent.putExtra("CASH", CashNotification.RQS_STOP_SERVICE);
        mContext.sendBroadcast(intent);*/
    }

    private void stopTransactionAlarm() {
        cancel();
        Intent intent = new Intent();
        intent.setAction(TransactionReminder.ACTION);
        intent.putExtra("SPENT", TransactionReminder.RQS_STOP_SERVICE);
        mContext.sendBroadcast(intent);
    }

   /* private void stopBillAlarm() {
        cancel();
        Intent intent = new Intent();
        intent.setAction(BillNotification.ACTION);
        intent.putExtra("BILL", BillNotification.RQS_STOP_SERVICE);
        mContext.sendBroadcast(intent);
    }*/
}
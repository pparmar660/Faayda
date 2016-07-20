package com.faayda.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.faayda.database.DBHelper;
import com.faayda.processor.SMSReader;
import com.faayda.processor.TransactionMessageProcessor;

public class SmsListener extends BroadcastReceiver {

    static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public SmsListener() {
    }

    Context mcontext;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ACTION.equals(intent.getAction())) {
            mcontext = context;
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];

                for (int i = 0; i < pdus.length; i++)
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                for (SmsMessage message : messages) {

                    if (TransactionMessageProcessor.getInstance(context).isTransactionMessage(message.getMessageBody())) {
                        DBHelper dbHelper = new DBHelper(mcontext);
                        dbHelper.open();
                        SMSReader smsReader = new SMSReader();
                        smsReader.loadSMS(mcontext, true, dbHelper);
                    }
                }
            }
        }
    }
}
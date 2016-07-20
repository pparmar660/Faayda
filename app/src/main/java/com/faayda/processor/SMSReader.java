package com.faayda.processor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.fragment.DashBoardFragment;
import com.faayda.listeners.OnWebServiceResult;
import com.faayda.preferences.Preferences;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;
import com.faayda.utils.GloablVariable;
import com.faayda.utils.TemproryMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aashutosh @ vinove on 8/8/2015.
 */
public final class SMSReader {

    boolean creditCardIdentifier = false;
    boolean deditCardIdentifier = false;
    ContentValues contentValues;
    String smsBody;


    /**
     * Function used to load sms from inbox to database for records
     *
     * @param mContext
     */

 /*  public void loadTransactionSMSinDB(final Context mContext) {
        loadTransactionSMSinDB(mContext, false);
    }*/
    public void loadTransactionSMSinDB(final Context mContext, final boolean loadLatestSMS, final OnWebServiceResult result, final DBHelper dbHelper) {
        new AsyncTask<Void, Void, Void>() {
            Preferences preferences;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                preferences = new Preferences(mContext);
            }

            @Override
            protected Void doInBackground(Void... params) {
                Uri inboxURI = Uri.parse("content://sms/inbox");
                String[] reqCols = new String[]{"_id", "address", "body", "date"};
                ContentResolver cr = mContext.getContentResolver();

                String where = "";

                loadSMS(mContext, loadLatestSMS, dbHelper);

               /* if (loadLatestSMS) {
                    String timeStamp = ((BaseActivity) mContext).dbHelper.getValue(DBConstants.SMS, DBConstants.SMS_DATE, null, DBConstants.SMS_DATE + " DESC ", null);
                    where = (timeStamp == null || timeStamp.isEmpty()) ? null : "date > " + timeStamp;
                } else {
                    where = null;
                }
                System.out.println(where);
                Cursor cur = cr.query(inboxURI, reqCols, where, null, "_id");
                int i = 0;
                String number, body, date;
                ContentValues contentValues;
                while (cur.moveToNext()) {
                    body = cur.getString(cur.getColumnIndex(reqCols[2]));
                    if (((BaseActivity) mContext).processor.isTransactionMessage(body) && ((BaseActivity) mContext).processor.checkTransactionMessage(body)) {
                        contentValues = new ContentValues();
                        contentValues.put(DBConstants.SMS_SENDER, cur.getString(cur.getColumnIndex(reqCols[1])));
                        contentValues.put(DBConstants.SMS_BODY, body);
                        contentValues.put(DBConstants.SMS_DATE, cur.getString(cur.getColumnIndex(reqCols[3])));
                        ((BaseActivity) mContext).dbHelper.insertContentVals(DBConstants.SMS, contentValues);
                    }
                }
                filterSMSData(mContext);*/
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                preferences.setBoolean(Constants.SMS_LOADED, true);
                if (result != null) result.onWebServiceResult("", Constants.SERVICE_TYPE.SMS_LOAD);

                if (GloablVariable.isStatus()) {
                    smsRefreshed(mContext);
                }
            }
        }.execute();
    }

    private void smsRefreshed(Context mContext) {

        NotificationManager notif = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification(R.drawable.app_icon, "Refresh SMSs", System.currentTimeMillis());
        PendingIntent pending = PendingIntent.getActivity(mContext.getApplicationContext(), 0, new Intent(), 0);

        notify.setLatestEventInfo(mContext.getApplicationContext(), "Refresh SMSs", "Refresh complete", pending);
        notif.notify(0, notify);
        ((MainActivity) mContext).onFragmentAdd(new DashBoardFragment(), Constants.DASHBOARD);
    }

    public void loadTransactionSMSinDB(final Context mContext, final boolean loadLatestSMS, DBHelper dbHelper) {
        loadTransactionSMSinDB(mContext, loadLatestSMS, null, dbHelper);
    }

    public void loadSMS(Context mContext, boolean loadLatestSMS, DBHelper dbHelper) {

        if (!loadLatestSMS)
            dbHelper.deleteRecords(DBConstants.SMS, null, null);

        Uri inboxURI = Uri.parse("content://sms/inbox");
        String[] reqCols = new String[]{"_id", "address", "body", "date"};
        ContentResolver cr = mContext.getContentResolver();
        TransactionMessageProcessor processor = new TransactionMessageProcessor(mContext);
        String where = "";

        if (loadLatestSMS) {
            String timeStamp = dbHelper.getValue(DBConstants.SMS, "MAX(" + DBConstants.SMS_DATE + ")", null, null, null);

         /*   String time=timeStamp;
            Log.e("time",time);

            Calendar calendar=Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss");

            try {
                Date myDate = dateFormat.parse(timeStamp);
                calendar.setTime(myDate);
                timeStamp=calendar.getTimeInMillis()+"";
            } catch (ParseException e) {
                e.printStackTrace();
            }*/


            where = (timeStamp == null || timeStamp.isEmpty()) ? null : "date > " + timeStamp;
        } else {
            where = null;
        }
        Cursor cur = cr.query(inboxURI, reqCols, where, null, "_id");
        //Cursor cur = cr.query(inboxURI, reqCols, "date > 145121564", null, "_id");
        int i = 0;
        String number, body, date;
        ContentValues contentValues;

      /*  while (cur.moveToNext()) {
            body = cur.getString(cur.getColumnIndex(reqCols[2]));

            if (processor.isTransactionMessage(body) && processor.checkTransactionMessage(body)) {
                contentValues = new ContentValues();
                contentValues.put(DBConstants.ID, cur.getString(cur.getColumnIndex(reqCols[0])));
                contentValues.put(DBConstants.SMS_SENDER, cur.getString(cur.getColumnIndex(reqCols[1])));
                contentValues.put(DBConstants.SMS_BODY, body);
                contentValues.put(DBConstants.SMS_DATE, cur.getString(cur.getColumnIndex(reqCols[3])));
                //  Log.e("sms body--", smsBody + ',' + body);

                dbHelper.insertContentVals(DBConstants.SMS, contentValues);
            }
        }*/


        ArrayList<HashMap<String, String>> bankSmsList = TemproryMessage.List();
        for (int j = 0; j < bankSmsList.size(); j++) {

            contentValues = new ContentValues();
            contentValues.put(DBConstants.ID, bankSmsList.get(j).get(TemproryMessage.ID));
            contentValues.put(DBConstants.SMS_SENDER, bankSmsList.get(j).get(TemproryMessage.TAG));
            contentValues.put(DBConstants.SMS_BODY, bankSmsList.get(j).get(TemproryMessage.MSG));

            //String timeget = cur.getString(cur.getColumnIndex(reqCols[3]));
            // Log.e("date get", timeget);

            contentValues.put(DBConstants.SMS_DATE, bankSmsList.get(j).get(TemproryMessage.DATE));
            dbHelper.insertContentVals(DBConstants.SMS, contentValues);

        }


        filterSMSData(mContext, dbHelper);
    }


    public void filterSMSData(Context mContext, DBHelper dbHelper) {


        String sqlQuery = null;
        try {
            sqlQuery = "SELECT s." + DBConstants.ID + ", s." + DBConstants.SMS_SENDER + ",s." + DBConstants.SMS_BODY + ",s." +
                    DBConstants.SMS_DATE + "," +
                    "s." + DBConstants.TIME_KEY + " FROM " + DBConstants.SMS + " s LEFT JOIN " + DBConstants.TRANSACTIONS +
                    " t  ON t." + DBConstants.SMS_ID + " = s." + DBConstants.ID + " WHERE t." +
                    DBConstants.SMS_ID + " IS NULL AND s." + DBConstants.SMS_STATUS + "= 1";
        } catch (Exception e) {
            e.printStackTrace();
        }

        Cursor cursor = dbHelper.getDataThroughRaw(sqlQuery);

 /*       Cursor cursor = dbHelper.getTableRecords(DBConstants.SMS, null,
                DBConstants.SMS_STATUS + " = 1 ", DBConstants.ID);// for deleted transaction sms status is 0*//**//*
*/
        TransactionMessageProcessor processor = new TransactionMessageProcessor(mContext);
        String transactionType, accountNo;
        while (cursor.moveToNext()) {
            String accountNumber = processor.getAccountNumber(cursor.getString(cursor.getColumnIndex(DBConstants.SMS_BODY)));
            smsBody = cursor.getString(cursor.getColumnIndex(DBConstants.SMS_BODY));
            String smsSender = cursor.getString(cursor.getColumnIndex(DBConstants.SMS_SENDER));
            int count_1 = dbHelper.getTableRecordsCount(DBConstants.BANK_SMS_CODES, null, DBConstants.BANK_SMS_SENDER + " = '" + cursor.getString(cursor.getColumnIndex(DBConstants.SMS_SENDER)) + "'", null);

            if (count_1 <= 0)
                continue;

            contentValues = new ContentValues();

            boolean insertValue = true;
            smsBody = cursor.getString(cursor.getColumnIndex(DBConstants.SMS_BODY));
            transactionType = processor.smsType(mContext, smsBody);
            creditCardIdentifier = false;
            switch (transactionType) {
                case Constants.CREDIT:
                    //Credit SMS //
                    double amount_ = processor.getCreditedAmount(smsBody);

                    if (processor.getCreditedAmount(smsBody) <= 0) {
                        continue;
                    }
                    contentValues.put(DBConstants.TRANSACTION_AMOUNT, (processor.getCreditedAmount(smsBody)));
                    contentValues.put(DBConstants.TRANSACTION_TYPE_ID,
                            dbHelper.getValue(DBConstants.TRANSACTION_TYPE,
                                    DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.CREDIT + "'"));
                    //update amount

                    processor.UpdateAmountBalance(mContext, cursor);
                    ///

                    break;
                case Constants.DEBIT:
                    //Debit SMS //

                    if (processor.getDebitedAmount(smsBody) <= 0) {
                        continue;
                    }
                    contentValues.put(DBConstants.TRANSACTION_AMOUNT, (processor.getDebitedAmount(smsBody)));
                    contentValues.put(DBConstants.TRANSACTION_TYPE_ID, dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.DEBIT + "'"));
                    //update amount
                    processor.UpdateAmountBalance(mContext, cursor);
                    //
                    break;
                case Constants.ATM_WDL:
                    // ATM_WDL SMS

                    if (processor.getAtmWdlAmount(smsBody) <= 0) {
                        continue;
                    }


                    contentValues.put(DBConstants.TRANSACTION_AMOUNT, (processor.getAtmWdlAmount(smsBody)));
                    contentValues.put(DBConstants.TRANSACTION_TYPE_ID, dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.ATM_WDL + "'"));

                    //update amount

                    processor.UpdateAmountBalance(mContext, cursor);


                    ///


                    break;
                case Constants.FUND_TRANSFER:
                    // FUND TRANSFER SMS

                    if (processor.getFundTransfer(smsBody) <= 0) {
                        continue;
                    }
                    contentValues.put(DBConstants.TRANSACTION_AMOUNT, (processor.getFundTransfer(smsBody)));
                    contentValues.put(DBConstants.TRANSACTION_TYPE_ID, dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.FUND_TRANSFER + "'"));

                    //update amount

                    processor.UpdateAmountBalance(mContext, cursor);


                    ///

                    break;
                case Constants.BILL:
                    // BILL
                    getBillRecords(cursor, mContext, dbHelper);
                    continue;

                case Constants.EXPENSE:
                    //EXPENSE

                    if (processor.getExpenseAmountFromMsg(smsBody) <= 0) {
                        continue;
                    }
                    contentValues.put(DBConstants.TRANSACTION_AMOUNT, (processor.getExpenseAmountFromMsg(smsBody)));
                    contentValues.put(DBConstants.TRANSACTION_TYPE_ID, dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.EXPENSE + "'"));

                    break;
                case Constants.CREDIT_CARD_EXPENSE:
                    // CREDIT_CARD_EXPENSE SMS

                    if (processor.getCreditCardAmount(smsBody) <= 0) {
                        continue;
                    }
                    creditCardIdentifier = true;
                    long accountId = processor.getCreditCardAccountId(mContext, cursor);
                    if (accountId > 0)
                        contentValues.put(DBConstants.TRANSACTION_ACCOUNT_ID, accountId);
                    else continue;
                    contentValues.put(DBConstants.TRANSACTION_AMOUNT, processor.getCreditCardAmount(smsBody));
                    contentValues.put(DBConstants.TRANSACTION_TYPE_ID, dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.CREDIT_CARD_EXPENSE + "'"));

                    break;

                case Constants.DEBIT_CARD_EXPENSE:

                    if (processor.getDeditCardAmount(smsBody) <= 0) {
                        continue;
                    }
                    deditCardIdentifier = true;

                    String debitCardNumber = (processor.getDebitCardNumber(cursor.getString
                            (cursor.getColumnIndex(DBConstants.SMS_BODY))));
                    if (debitCardNumber.isEmpty() || debitCardNumber.equalsIgnoreCase("0"))
                        continue;


                    double amount = (processor.getDebitedAmount(smsBody));

                    // contentValues.put(DBConstants.TRANSACTION_AMOUNT, amount);


                    long accountID = processor.getDebitCardAccountId(mContext, cursor);
                    if (accountID > 0) {
                        contentValues.put(DBConstants.TRANSACTION_ACCOUNT_ID, accountID);
                    }
                    try {
                        contentValues.put(DBConstants.CARD_NUMBER, Long.parseLong(debitCardNumber));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    contentValues.put(DBConstants.TRANSACTION_AMOUNT, amount);
                    contentValues.put(DBConstants.TRANSACTION_TYPE_ID,
                            dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME +
                                    " = '" + Constants.DEBIT_CARD_EXPENSE + "'"));


                    break;

                case Constants.AVL_BAL:
                    // Available Balance
                    if (processor.getAccountBalance(smsBody) <= 0) {
                        continue;
                    }


                    break;

                case Constants.ACC_NO:
                    break;
                default:

                    // contentValues.put(DBConstants.TRANSACTION_TYPE_ID, 100);
                    insertValue = false;

                    break;
            }

            if (insertValue) {
                int count = dbHelper.getTableRecordsCount(DBConstants.TRANSACTIONS, null, DBConstants.SMS_ID + " = " + cursor.getLong(cursor.getColumnIndex(DBConstants.ID)), null);

                if (count <= 0) {

                    long accountId = processor.getAccountId(mContext, cursor);

                    if (!creditCardIdentifier && accountId > 0)
                        contentValues.put(DBConstants.TRANSACTION_ACCOUNT_ID, processor.getAccountId(mContext, cursor));
                    else if (!creditCardIdentifier) {
                        // get account id from bank id
                        String creditCardId = dbHelper
                                .getValue(DBConstants.ACCOUNT_TYPE, DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "'");

                        accountId = 0;
                        String bankId = dbHelper.getValue(DBConstants.BANK_SMS_CODES, DBConstants.BANK_ID,
                                DBConstants.BANK_SMS_SENDER + " like '" + smsSender + "'");

                        if ((dbHelper.getTableRecordsCount(DBConstants.USER_ACCOUNTS,
                                null, DBConstants.ACCOUNT_BANK_ID + " = '" + bankId + "' AND " + DBConstants.ACCOUNT_TYPE_ID + " != '" + creditCardId + "' ", null) == 1)) {


                            try {
                                accountId = Long.parseLong((dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ID,
                                        DBConstants.ACCOUNT_BANK_ID + " like '" + bankId + "' AND " + DBConstants.ACCOUNT_TYPE_ID + " NOT like '" + creditCardId + "'")));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            if (!(accountId <= 0))
                                contentValues.put(DBConstants.TRANSACTION_ACCOUNT_ID, accountId);


                        }


                    }
                    // else continue;


                    contentValues.put(DBConstants.SMS_ID, cursor.getLong(cursor.getColumnIndex(DBConstants.ID)));
                    contentValues.put(DBConstants.TRANSACTION_DATE, cursor.getLong(cursor.getColumnIndex(DBConstants.SMS_DATE)));
                    contentValues.put(DBConstants.TRANSACTION_BILLER_ID, CommonUtils.getSenderMerchantId(mContext, cursor, dbHelper));


                    //  pp start
                    long billerId = CommonUtils.getSenderMerchantId(mContext, cursor, dbHelper);
                    if (billerId > 0) {

                        String billerName = dbHelper.getValue(DBConstants.BILLER_LIST,
                                DBConstants.BILLER_NAME, DBConstants.ID + " like '" + CommonUtils.getSenderMerchantId(mContext, cursor, dbHelper) + "'");


                        String categoryId = dbHelper.getValue(DBConstants.BILLER_LIST,
                                DBConstants.CATEGORY_ID, DBConstants.ID + " like '" + CommonUtils.getSenderMerchantId(mContext, cursor, dbHelper) + "'");

                        contentValues.put(DBConstants.BILLER_NAME, billerName);
                        try {
                            contentValues.put(DBConstants.CATEGORY_ID, Long.parseLong(categoryId));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }


                    } else {
                        //set default category 1

                        long defaultCategory = Long.parseLong(dbHelper.getValue
                                (DBConstants.CATEGORIES, DBConstants.ID, DBConstants.CATEGORY_TITLE + "='Others'"));

                        contentValues.put(DBConstants.CATEGORY_ID, defaultCategory);
                    }


                    contentValues.put(DBConstants.DATE_MODIFIED, System.currentTimeMillis());
                    dbHelper.insertContentVals(DBConstants.TRANSACTIONS, contentValues);
                }
            }
        }
        //pp  end


        //pp
    }


    public void refreshAccountWithTransaction(Context mContext, DBHelper dbHelper) {
        TransactionMessageProcessor processor = new TransactionMessageProcessor(mContext);

        String sqlQuery = "SELECT s.* FROM "
                + DBConstants.SMS + " s, "
                + DBConstants.TRANSACTIONS + " t WHERE s."
                + DBConstants.ID + " = t."
                + DBConstants.SMS_ID;


        Cursor cursor = dbHelper.getDataThroughRaw(sqlQuery);

        String transactionType;
        if (cursor != null)
            while (cursor.moveToNext()) {
                smsBody = cursor.getString(cursor.getColumnIndex(DBConstants.SMS_BODY));
                transactionType = processor.smsType(mContext, smsBody);

                switch (transactionType) {
                    case Constants.CREDIT:
                        //update amount
                        processor.UpdateAmountBalance(mContext, cursor);
                        ///

                        break;
                    case Constants.DEBIT:

                        //update amount
                        processor.UpdateAmountBalance(mContext, cursor);
                        //
                        break;
                    case Constants.ATM_WDL:
                        processor.UpdateAmountBalance(mContext, cursor);
                        break;
                    case Constants.FUND_TRANSFER:

                        processor.UpdateAmountBalance(mContext, cursor);
                        ///

                        break;


                    case Constants.CREDIT_CARD_EXPENSE:


                        long accountId = processor.getCreditCardAccountId(mContext, cursor);
                        if (!(accountId > 0)) continue;
                        break;

                    case Constants.DEBIT_CARD_EXPENSE:
                        long accountID = processor.getDebitCardAccountId(mContext, cursor);

                        break;

                    case Constants.AVL_BAL:
                        // Available Balance
                        if (processor.getAccountBalance(smsBody) <= 0) {
                            continue;
                        }


                        break;

                    case Constants.ACC_NO:
                        break;

                }


            }
        //pp  end


        //pp
    }


    public void getBillRecords(Cursor cursor, Context mContext, DBHelper dbHelper) {
        /*Cursor cursor = dbHelper.getTableRecords(DBConstants.SMS, null, null, DBConstants.ID);
        ContentValues contentValues = new ContentValues();
        String smsBody = cursor.getString(cursor.getColumnIndex(DBConstants.SMS_BODY));*/
        TransactionMessageProcessor processor = new TransactionMessageProcessor(mContext);
        String smsBody = cursor.getString(cursor.getColumnIndex(DBConstants.SMS_BODY));
        double amount = processor.getBillAmount(smsBody);
        String dueDate = (processor.getBilldueDate(smsBody));


        if (amount <= 0) {
            return;
        }

        if (dbHelper.getTableRecordsCount(DBConstants.BILL_RECORDS, null,
                DBConstants.SMS_ID + "=" + cursor.getInt(cursor.getColumnIndex(DBConstants.ID)), null) > 0)
            return;

        contentValues.put(DBConstants.BILL_AMOUNT, amount);
        contentValues.put(DBConstants.BILLER_ID, CommonUtils.getSenderMerchantId(mContext, cursor, dbHelper));
        contentValues.put(DBConstants.SMS_ID, cursor.getInt(cursor.getColumnIndex(DBConstants.ID)));
        contentValues.put(DBConstants.BILL_PAID_STATUS, 0);


        //contentValues.put(DBConstants.TRANSACTION_TYPE_ID, dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.BILL + "'"));

        if (dueDate == null) {


            dueDate = "N/A";
        }
        contentValues.put(DBConstants.BILL_DUE_DATE, TransactionMessageProcessor.setDateFormat(dueDate));

        dbHelper.insertContentVals(DBConstants.BILL_RECORDS, contentValues);
    }


}

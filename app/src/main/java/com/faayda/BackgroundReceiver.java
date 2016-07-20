package com.faayda;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.listeners.OnWebServiceResult;
import com.faayda.models.BankModel;
import com.faayda.models.BillerModel;
import com.faayda.models.CategoryModel;
import com.faayda.models.LoginResponse;
import com.faayda.models.SMSTemplate;
import com.faayda.models.StartEndTemplateModel;
import com.faayda.network.NetworkManager;
import com.faayda.processor.SMSReader;
import com.faayda.utils.ConnectionDetector;
import com.faayda.utils.Constants;
import com.faayda.utils.GloablVariable;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by vinove on 21/9/15.
 */

public class BackgroundReceiver extends BroadcastReceiver implements OnWebServiceResult {

    Context mContext;
    DBHelper dbHelper;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;
        dbHelper = new DBHelper(context);
        dbHelper.open();
        checkConnection();

    }

    private void checkConnection() {
        ConnectionDetector cd = new ConnectionDetector(mContext);

        if (!cd.isConnectingToInternet()) {
            //TODO: Service Re-run to sync messages
            GloablVariable.setStatus(false);
            readSMS();
        } else {
            RefreshData();
        }
    }

    public void readSMS() {
        SMSReader smsReader = new SMSReader();
        //Run async task and receive the completion of task in webservice result
        smsReader.loadTransactionSMSinDB(mContext, true, dbHelper);
    }

    private void RefreshData() {
        try {
            String user_id = null;
            Cursor cursor = null;
            try {
                user_id = dbHelper.getValue(DBConstants.USER_DETAILS, DBConstants.USER_ID, null);

                String selectQuery = "SELECT SUM( " + DBConstants.TRANSACTION_AMOUNT + ") FROM " + DBConstants.TRANSACTIONS;
                cursor = dbHelper.getDataThroughRaw(selectQuery);
                if (cursor.moveToFirst()) {
                    cursor.getInt(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject obj = new JSONObject();
            obj.put(Constants.KEY_METHOD, Constants.KEY_REFRESH_DATA);
            obj.put(Constants.KEY_LAST_TIME, getUpdateTime());
            HashMap<String, String> params = new HashMap<>();
            params.put(mContext.getString(R.string.postKey), obj.toString());
            NetworkManager networkManager = new NetworkManager(mContext, params, this, Constants.SERVICE_TYPE.REFRESH_DATA, false);
            networkManager.callWebService(Constants.URL);

            obj = new JSONObject();
            obj.put(Constants.KEY_METHOD, Constants.KEY_UPDATE_TRANS);
            obj.put(Constants.KEY_CREDIT_TRANS, cursor.getInt(0));
            obj.put(Constants.KEY_DEBIT_TRANS, "null");
            obj.put("userId", user_id.trim());
            params = new HashMap<>();
            params.put(mContext.getString(R.string.postKey), obj.toString());
            networkManager = new NetworkManager(mContext, params, this, Constants.SERVICE_TYPE.UPDATE_TRANS, false);
            networkManager.callWebService(Constants.URL);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUpdateTime() {
        String timestamp = dbHelper.getValue(DBConstants.CATEGORIES, "MAX(" + DBConstants.DATE_MODIFIED + ")", null, null, null);

        long lastUpdateTime = Long.parseLong(timestamp);

        Date date = new Date(lastUpdateTime);
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf.setCalendar(cal);
        cal.setTime(date);
        return sdf.format(date);
    }

    @Override
    public void onWebServiceResult(String result, Constants.SERVICE_TYPE type) {
        {
            ContentValues values;
            long id;
            String categoryId;
            switch (type) {
                case REFRESH_DATA:
                    LoginResponse response = new Gson().fromJson(result, LoginResponse.class);
                    switch (response.code) {
                        case 200:
                            try {
                                if (response.Category != null && response.Category.length > 0) {

                                    dbHelper.deleteRecords(DBConstants.CATEGORIES, null, null);

                                    try {
                                        for (CategoryModel model : response.Category) {
                                            values = new ContentValues();
                                            values.put(DBConstants.CATEGORY_TITLE, model.catName);
                                            values.put(DBConstants.CATEGORY_IMAGE, model.image_url);
                                            values.put(DBConstants.DATE_MODIFIED, System.currentTimeMillis());
                                            dbHelper.insertContentVals(DBConstants.CATEGORIES, values);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (response.Bank != null && response.Bank.length > 0) {

                                    dbHelper.deleteRecords(DBConstants.BANKS, null, null);
                                    dbHelper.deleteRecords(DBConstants.BANK_SMS_CODES, null, null);

                                    try {
                                        for (BankModel model : response.Bank) {
                                            values = new ContentValues();
                                            values.put(DBConstants.BANK_NAME, model.bankName);
                                            values.put(DBConstants.BANK_IMAGE, model.image_url);
                                            values.put(DBConstants.BANK_CONTACT_NO, model.bankContact);
                                            id = dbHelper.insertContentVals(DBConstants.BANKS, values);

                                            for (String value : model.bankSenders) {
                                                values = new ContentValues();
                                                values.put(DBConstants.BANK_ID, id);
                                                values.put(DBConstants.BANK_SMS_SENDER, value);
                                                dbHelper.insertContentVals(DBConstants.BANK_SMS_CODES, values);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (response.Biller != null && response.Bank.length > 0) {

                                    dbHelper.deleteRecords(DBConstants.BILLER_LIST, null, null);
                                    dbHelper.deleteRecords(DBConstants.BILLER_SMS_CODES, null, null);

                                    try {
                                        for (BillerModel model : response.Biller) {
                                            values = new ContentValues();
                                            values.put(DBConstants.BILLER_NAME, model.billerName);
                                            categoryId = dbHelper.getValue(DBConstants.CATEGORIES, DBConstants.ID, DBConstants.CATEGORY_TITLE
                                                    + " like '" + model.categoryName + "'");
                                            values.put(DBConstants.CATEGORY_ID, categoryId);
                                            id = dbHelper.insertContentVals(DBConstants.BILLER_LIST, values);

                                            for (String value : model.billerSender) {
                                                values = new ContentValues();
                                                values.put(DBConstants.BILLER_ID, id);
                                                values.put(DBConstants.BILLER_SMS_SENDER, value);
                                                dbHelper.insertContentVals(DBConstants.BILLER_SMS_CODES, values);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                dbHelper.deleteRecords(DBConstants.SMS_TEMPLATES, null, null);

                                SMSTemplate template = response.SmsTemplate;

                                Cursor cursor = dbHelper.getTableRecords(DBConstants.SMS_TEMPLATES_TAG, null, null, null);

                                StartEndTemplateModel[] startEndTemplateModels = null;
                                while (cursor.moveToNext()) {
                                    id = cursor.getLong(cursor.getColumnIndex(DBConstants.ID));

                                    switch (cursor.getString(cursor.getColumnIndex(DBConstants.SMS_TAG_TITLE))) {
                                        case Constants.DEBITED_AMOUNT_TAG:
                                            startEndTemplateModels = template.debited_amount;
                                            break;
                                        case Constants.DEBIT_CARD_TAG:
                                            startEndTemplateModels = template.debit_card;
                                            break;
                                        case Constants.CREDITED_AMOUNT_TAG:
                                            startEndTemplateModels = template.credited_amount;
                                            break;
                                        case Constants.ACCOUNT_NUMBER_TAG:
                                            startEndTemplateModels = template.account_number;
                                            break;
                                        case Constants.AVAILABLE_BALANCE_TAG:
                                            startEndTemplateModels = template.available_balance;
                                            break;
                                        case Constants.BILL_DUE_DATE_TAG:
                                            startEndTemplateModels = template.bill_due_date;
                                            break;
                                        case Constants.BILL_AMOUNT_TAG:
                                            startEndTemplateModels = template.bill_amount;
                                            break;
                                        case Constants.PAYMENT_MADE_TAG:
                                            startEndTemplateModels = template.payment_made;
                                            break;
                                        case Constants.CREDIT_CARD_TRANS_TAG:
                                            startEndTemplateModels = template.credit_card_transaction;
                                            break;
                                        case Constants.ATM_WDL_TAG:
                                            startEndTemplateModels = template.atm_withdrawal;
                                            break;
                                        case Constants.CREDIT_CARD_TAG:
                                            startEndTemplateModels = template.credit_card;
                                            break;
                                        case Constants.FUND_TRANSFER_TAG:
                                            startEndTemplateModels = template.fund_transfer;
                                            break;
                                        default:
                                            break;
                                    }

                                    if (startEndTemplateModels != null) {
                                        for (StartEndTemplateModel model : startEndTemplateModels) {
                                            values = new ContentValues();
                                            values.put(DBConstants.SMS_TAG_ID, id);
                                            values.put(DBConstants.SMS_TAG_START, model.start);
                                            values.put(DBConstants.SMS_TAG_END, model.end);

                                            dbHelper.insertContentVals(DBConstants.SMS_TEMPLATES, values);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                GloablVariable.setStatus(false);
                                readSMS();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 500:
                            GloablVariable.setStatus(false);
                            readSMS();
                            break;
                        default:
                            break;
                    }

                    break;
                default:
                    break;
            }
        }
    }
}

package com.faayda;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.faayda.adapter.SlideMenuAdapter;
import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.fragment.Account;
import com.faayda.fragment.BillsAndEmi;
import com.faayda.fragment.CashManagment;
import com.faayda.fragment.DashBoardFragment;
import com.faayda.fragment.FaqFragment;
import com.faayda.fragment.Feedback;
import com.faayda.fragment.Settings;
import com.faayda.listeners.OnFragmentChangeListener;
import com.faayda.listeners.OnWebServiceResult;
import com.faayda.models.BankModel;
import com.faayda.models.BillerModel;
import com.faayda.models.CategoryModel;
import com.faayda.models.LoginResponse;
import com.faayda.models.SMSTemplate;
import com.faayda.models.StartEndTemplateModel;
import com.faayda.network.NetworkManager;
import com.faayda.processor.SMSReader;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.ConnectionDetector;
import com.faayda.utils.Constants;
import com.faayda.utils.Constants.SERVICE_TYPE;
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
 * Vinove @ 27th July 2015
 */
public final class MainActivity extends BaseActivity implements OnFragmentChangeListener, View.OnClickListener, AdapterView.OnItemClickListener, OnWebServiceResult {

    Uri uri;
    Intent intent;
    public DBHelper dbHelper;
    private ListView slidingMenu;
    public TextView tvTopBarTitle;
    private DrawerLayout drawerLayout;
    private LinearLayout faayda_icon;
    boolean doubleBackToExitPressedOnce;
    private FragmentManager fragmentManager;
    public TextView saveButton;
    public ImageButton ibMenuIcon, ibAppIcon, ibBackIcon, ibNotificationIcon;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        //TODO: Comment this statement before live
        CommonUtils.exportDB(this, DBConstants.DB_NAME);
        fragmentManager = getSupportFragmentManager();
        dbHelper = ((BaseActivity) this).dbHelper;
        TextView txtEmail = (TextView) findViewById(R.id.tvLoginEmail);
        txtEmail.setText(dbHelper.getValue(DBConstants.USER_DETAILS, DBConstants.USER_EMAIL, DBConstants.ID));
        ibAppIcon = (ImageButton) findViewById(R.id.ibAppIcon);
        ibMenuIcon = (ImageButton) findViewById(R.id.ibMenuIcon);
        ibBackIcon = (ImageButton) findViewById(R.id.ibBackIcon);
        slidingMenu = (ListView) findViewById(R.id.lvSlidingMenu);
        tvTopBarTitle = (TextView) findViewById(R.id.tvTopBarTitle);
        tvTopBarTitle.setText(Constants.DASHBOARD);
        saveButton = (TextView) findViewById(R.id.saveButton);
        faayda_icon = (LinearLayout) findViewById(R.id.faayda_icon);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ibNotificationIcon = (ImageButton) findViewById(R.id.ibNotificationIcon);
        ibNotificationIcon.setVisibility(View.INVISIBLE);
        ibBackIcon.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
        ibMenuIcon.setOnClickListener(this);
        ibBackIcon.setOnClickListener(this);
        ibNotificationIcon.setOnClickListener(this);
        slidingMenu.setOnItemClickListener(this);
        faayda_icon.setOnClickListener(this);
        ibAppIcon.setOnClickListener(this);
        slidingMenu.setAdapter(new SlideMenuAdapter(this));
        onFragmentReplace(new DashBoardFragment(), Constants.ACCOUNTS);

       /* try {
            Boolean newExpense = getIntent().getBooleanExtra("newBill", false);
            if (newExpense) {
//                requestDialog(getIntent().getStringExtra("smsBody"), getIntent().getLongExtra("smsDate", 0), getIntent().getStringExtra("smsSender"));
                ((MainActivity) this).onFragmentReplace(new Notification(), Constants.NOTIFICATION);
                ibNotificationIcon.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
/*
    private void requestDialog(String smsBody, Long smsDate, String smsSender) {
        // Include dialog.xml file
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bill_notification);
        dialog.setCancelable(false);
        TextView msg = (TextView) dialog.findViewById(R.id.tv_bill_detail);
        msg.setText(smsBody.trim());

        TextView sender = (TextView) dialog.findViewById(R.id.tv_msg_from);
        sender.setText(smsSender.trim());

        TextView date = (TextView) dialog.findViewById(R.id.tv_bill_due_date2);
        date.setText(CommonUtils.getDateFromTimestamp(smsDate).toString().trim());

        TextView btnLink = (TextView) dialog.findViewById(R.id.tv_yes_bill);
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((MainActivity) this).onFragmentReplace(new Notification(), Constants.NOTIFICATION);
                dialog.dismiss();
            }
        });

        TextView btnDismiss = (TextView) dialog.findViewById(R.id.tv_dismiss);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }*/


    public void setTopBarTitle(String title) {
        tvTopBarTitle.setText(title);
    }

    public void invalidateTopBar(Constants.TITLE_BAR service_type) {
        int[] menuItems = new int[]{ibNotificationIcon.getId(), saveButton.getId()};
        int shownId;
        switch (service_type) {
            default:
            case NOTIFICATION_ONLY:
                shownId = ibNotificationIcon.getId();
                break;
            case SAVE_BUTTON:
                shownId = saveButton.getId();
                break;
        }
        for (int value : menuItems) {
            if (value == shownId) {
                findViewById(shownId).setVisibility(View.VISIBLE);
            } else {
                findViewById(value).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onFragmentAdd(Fragment fragment, String TAG) {
        Fragment existingFragment = fragmentManager.findFragmentByTag(TAG);
        if (existingFragment == null) {
            fragmentManager.beginTransaction().add(R.id.fragmentViewFrame, fragment).addToBackStack(TAG).commit();
        } else {
            fragmentManager.beginTransaction().show(fragment).commit();
        }
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onFragmentReplace(Fragment fragment, String TAG) {
        Fragment existingFragment = fragmentManager.findFragmentByTag(TAG);
        if (existingFragment == null) {
            fragmentManager.beginTransaction().replace(R.id.fragmentViewFrame, fragment).addToBackStack(TAG).commit();
        } else {
            fragmentManager.beginTransaction().show(fragment).commit();
        }
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onFragmentReplace(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.fragmentViewFrame, fragment).commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onFragmentAddWithArgument(Fragment fragment, String TAG, Bundle arguments) {

        fragment.setArguments(arguments);

        Fragment existingFragment = fragmentManager.findFragmentByTag(TAG);
        if (existingFragment == null) {
            fragmentManager.beginTransaction().add(R.id.fragmentViewFrame, fragment).addToBackStack(TAG).commit();
        } else {
            fragmentManager.beginTransaction().show(fragment).commit();
        }
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibMenuIcon:
                openDrawer();
                break;
            case R.id.ibBackIcon:
                onBackPressed();
                break;
            case R.id.faayda_icon:
                clearFragmentStack();
                ((MainActivity) this).onFragmentAdd(new DashBoardFragment(), Constants.DASHBOARD);
                closeDrawer();
            case R.id.ibAppIcon:
                clearFragmentStack();
                ((MainActivity) this).onFragmentAdd(new DashBoardFragment(), Constants.DASHBOARD);
                closeDrawer();
            default:
                break;
        }
    }

    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();

            if (tag.equals(Constants.ACCOUNTS) || tag.equals(Constants.SETTINGS) || tag.equals(Constants.BILLS)
                    || tag.equals(Constants.CASH) || tag.equals(Constants.FEEDBACK) || tag.equals(Constants.FAQ)) {
                if (isDrawerOpen()) {
                    closeDrawer();
                    return;
                }
                DashBoardFragment dashBoardFragment = new DashBoardFragment();
                ibBackIcon.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);
                clearFragmentStack();
                ((MainActivity) this).onFragmentAdd(dashBoardFragment, Constants.DASHBOARD);
            } else {
                getSupportFragmentManager().popBackStack();
                ibBackIcon.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);

                String sub_tag = getSupportFragmentManager().getBackStackEntryAt
                        (getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
                if (sub_tag.equals(Constants.ADD_ACCOUNT)) {
                    setTopBarTitle(Constants.ACCOUNTS);
                }
                if (sub_tag.equals(Constants.EDIT_ACCOUNT)) {
                    setTopBarTitle(Constants.ACCOUNTS);
                }
                if (sub_tag.equals(Constants.ADD_CASH)) {
                    setTopBarTitle(Constants.CASH_MANAGEMENT);
                }
                if (sub_tag.equals(Constants.EDIT_CASH)) {
                    setTopBarTitle(Constants.CASH_MANAGEMENT);
                }
                if (sub_tag.equals(Constants.ADD_BILLER)) {
                    setTopBarTitle(Constants.BILLS);
                }
                if (sub_tag.equals(Constants.EDIT_BILLER)) {
                    setTopBarTitle(Constants.BILLS);
                }
                if (sub_tag.equals(Constants.ADD_TRANSACTION)) {
                    setTopBarTitle(Constants.DASHBOARD);
                }
                if (sub_tag.equals(Constants.EDIT_TRANSACTION)) {
                    setTopBarTitle(Constants.DASHBOARD);
                }

                 /*if (tag.equals(Constants.ADD_TRANSACTION) || tag.equals(Constants.EDIT_ACCOUNT) ||
                    tag.equals(Constants.ADD_CASH)|| tag.equals(Constants.ADD_BILLER) ) {
                     getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                 }*/
            }
        } else {
            String tag = "";//=getSupportFragmentManager()."";
            try {
                if (getSupportFragmentManager().getBackStackEntryCount() <= 0)
                    tag = getSupportFragmentManager().getBackStackEntryAt(0).getName();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tag.equals(Constants.ACCOUNTS) || tag.equals(Constants.SETTINGS) || tag.equals(Constants.BILLS)
                    || tag.equals(Constants.CASH) || tag.equals(Constants.FEEDBACK) || tag.equals(Constants.FAQ)) {

                if (isDrawerOpen()) {
                    closeDrawer();
                    return;
                }
                DashBoardFragment dashBoardFragment = new DashBoardFragment();
                clearFragmentStack();
                ((MainActivity) this).onFragmentAdd(dashBoardFragment, Constants.DASHBOARD);
                return;
            }
            if (isDrawerOpen()) {
                closeDrawer();
                return;
            }
            if (doubleBackToExitPressedOnce) {
                finish();
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        closeDrawer();
        switch (position + 1) {
         /*case Constants.FAAYDA_LOGO:
                clearFragmentStack();
                ((MainActivity)this).onFragmentReplace(new DashBoardFragment(), Constants.ACCOUNTS);
                break;*/
            case Constants.ACCOUNTS_INDEX:
                ((MainActivity) this).onFragmentAdd(new Account(), Constants.ACCOUNTS);
                break;
            case Constants.BILLS_INDEX:
                ((MainActivity) this).onFragmentAdd(new BillsAndEmi(), Constants.BILLS);
                break;
            case Constants.CASH_INDEX:
                ((MainActivity) this).onFragmentAdd(new CashManagment(), Constants.CASH);
                break;
            /*case Constants.SPEND_SUMMARY_INDEX: //Removed because of same data as dashboard
                break;*/
            case Constants.SETTINGS_INDEX:
                ((MainActivity) this).onFragmentAdd(new Settings(), Constants.SETTTINGS);
                break;
            case Constants.FEEDBACK_INDEX:
                ((MainActivity) this).onFragmentAdd(new Feedback(), Constants.FEEDBACK);
                break;
            case Constants.SHARE_INDEX:
                CommonUtils.shareApplication(this);
                break;
            case Constants.FAQ_INDEX:
                ((MainActivity) this).onFragmentAdd(new FaqFragment(), Constants.FAQ);
                break;
            case Constants.LIKE_US_ON_FACEBOOK_INDEX:
                uri = Uri.parse(Constants.FACEBOOK_PAGE_URL);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case Constants.FOLLOW_US_ON_TWITTER_INDEX:
                uri = Uri.parse(Constants.TWITTER_PAGE_URL);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case Constants.REFRESH_SMS_INDEX:
                checkConnection();
                break;
//            case Constants.ABOUT_INDEX:
//                break;
            default:
                break;
        }
    }

    private void checkConnection() {
        ConnectionDetector cd = new ConnectionDetector(mContext);

        if (!cd.isConnectingToInternet()) {
            //TODO: Service Re-run to sync messages
            GloablVariable.setStatus(true);
            readSMS();
            Toast.makeText(this, "Refresh SMS initiated..", Toast.LENGTH_SHORT).show();
        } else {
            RefreshData();
        }
    }

    private void RefreshData() {
        try {
            getUpdateTime();
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
            params.put(getString(R.string.postKey), obj.toString());
            NetworkManager networkManager = new NetworkManager(this, params, this, SERVICE_TYPE.REFRESH_DATA);
            networkManager.callWebService(Constants.URL);

            obj = new JSONObject();
            obj.put(Constants.KEY_METHOD, Constants.KEY_UPDATE_TRANS);
            obj.put(Constants.KEY_CREDIT_TRANS, cursor.getInt(0));
            obj.put(Constants.KEY_DEBIT_TRANS, "null");
            obj.put("userId", user_id.trim());
            params = new HashMap<>();
            params.put(getString(R.string.postKey), obj.toString());
            networkManager = new NetworkManager(this, params, this, SERVICE_TYPE.UPDATE_TRANS);
            networkManager.callWebService(Constants.URL);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUpdateTime() {
        String timestamp = ((BaseActivity) mContext).dbHelper.getValue(DBConstants.CATEGORIES, "MAX(" + DBConstants.DATE_MODIFIED + ")", null, null, null);

        long lastUpdateTime = Long.parseLong(timestamp);

        Date date = new Date(lastUpdateTime);
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf.setCalendar(cal);
        cal.setTime(date);
        return sdf.format(date);
    }

    public void clearFragmentStack() {
        int fragments = fragmentManager.getBackStackEntryCount();
        for (Fragment fragment : fragmentManager.getFragments()) {
            fragmentManager.popBackStack();
        }
    }

    public void readSMS() {
        SMSReader smsReader = new SMSReader();
        //Run async task and receive the completion of task in webservice result
        smsReader.loadTransactionSMSinDB(this, true, dbHelper);
    }

    @Override
    public void onWebServiceResult(String result, SERVICE_TYPE type) {
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
                            GloablVariable.setStatus(true);
                            dbHelper.deleteRecords(DBConstants.DEBIT_CARDS, null, null);
                            dbHelper.deleteRecords(DBConstants.BILL_RECORDS, null, null);
                            dbHelper.deleteRecords(DBConstants.TRANSACTIONS, null, null);
                            dbHelper.deleteRecords(DBConstants.USER_ACCOUNTS, null, null);


                            readSMS();
                            Toast.makeText(this, "Refresh SMS initiated..", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 500:
                        GloablVariable.setStatus(true);
                        readSMS();
                        Toast.makeText(this, "Refresh SMS initiated..", Toast.LENGTH_SHORT).show();
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
package com.faayda;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.faayda.adapter.ViewPagerAdapter;
import com.faayda.database.DBConstants;
import com.faayda.listeners.OnWebServiceResult;
import com.faayda.models.BankModel;
import com.faayda.models.BillerModel;
import com.faayda.models.CategoryModel;
import com.faayda.models.LoginResponse;
import com.faayda.models.SMSTemplate;
import com.faayda.models.StartEndTemplateModel;
import com.faayda.network.NetworkManager;
import com.faayda.preferences.Preferences;
import com.faayda.processor.SMSReader;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;
import com.faayda.utils.Constants.SERVICE_TYPE;
import com.faayda.utils.Validation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by vinove on 7/27/2015.
 */
public final class Login extends BaseActivity implements OnWebServiceResult, OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {

    CheckBox cb;
    Boolean IsLogin = true;
    Preferences pref;
    int pager_pic[] = {};
    int RC_SIGN_IN = 0;
    AutoScrollViewPager viewpager;
    ScrollView svLoginForm;
    LinearLayout preparing_db;
    EditText user_name, password;
    GoogleApiClient mGoogleApiClient;
    ConnectionResult mConnectionResult;
    CirclePageIndicator circlePageIndicator;
    boolean mIntentInProgress, mSignInClicked;
    private boolean backPressedToExitOnce = false;
    private AlarmManager alarm;
    TextView register, forgot_pass;
    LinearLayout login_btn, google_plus;
    String personName, email, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.login);
        pager_pic = new int[]{R.drawable.slider_image1, R.drawable.slider_image2, R.drawable.slider_image3};
        preparing_db = (LinearLayout) findViewById(R.id.preparing_db);
        svLoginForm = (ScrollView) findViewById(R.id.svLoginForm);
        user_name = (EditText) findViewById(R.id.et_user);
        password = (EditText) findViewById(R.id.et_pass);
        pref = new Preferences(getApplicationContext());
        IsLogin = pref.getBoolean(Constants.KEY_LOGIN_CHECK);
        google_plus = (LinearLayout) findViewById(R.id.bt_login_google);
        login_btn = (LinearLayout) findViewById(R.id.bt_login);
        register = (TextView) findViewById(R.id.tv_register);
        forgot_pass = (TextView) findViewById(R.id.tv_forgot_pass);
        cb = (CheckBox) findViewById(R.id.cb_remember);
        viewpager = (AutoScrollViewPager) findViewById(R.id.view_pager);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.vpIndicator);
        viewpager.setAdapter(new ViewPagerAdapter(this, pager_pic));
        circlePageIndicator.setViewPager(viewpager);
        login_btn.setOnClickListener(this);
        forgot_pass.setOnClickListener(this);
        register.setOnClickListener(this);
        google_plus.setOnClickListener(this);
        mGoogleApiClient = new GoogleApiClient.Builder(Login.this).addConnectionCallbacks(Login.this)
                .addOnConnectionFailedListener(Login.this).addApi(Plus.API, new Plus.PlusOptions.Builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        if (IsLogin = true) {
            user_name.setText(pref.getString(Constants.KEY_USER_NAME));
            password.setText(pref.getString(Constants.KEY_PASSWORD));
            cb.setChecked(true);
        }


        try {
            Boolean doLogin = getIntent().getBooleanExtra("doLogin", false);
            if (doLogin) {
                username = getIntent().getStringExtra("userName");
                sendData(username, getIntent().getStringExtra("password"), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonUtils.exportDB(this, DBConstants.DB_NAME);
        viewpager.setInterval(4000);
        viewpager.startAutoScroll();
        viewpager.setScrollDurationFactor(17);
    }

    public void sendData(String userName, String passwordtxt, String socialId) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(Constants.KEY_METHOD, Constants.KEY_LOGIN);
        obj.put(Constants.KEY_EMAIL, userName);
        obj.put(Constants.KEY_PASSWORD, passwordtxt);
        obj.put(Constants.KEY_SOCIAL_ID, socialId);
        HashMap<String, String> params = new HashMap<>();
        params.put(getString(R.string.postKey), obj.toString());
        NetworkManager networkManager = new NetworkManager(this, params, this, SERVICE_TYPE.LOGIN);
        networkManager.callWebService(Constants.URL);
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.isEmailAddress(user_name, true, Constants.ErrorMessage_email)) {
            ret = false;
        }
        if (!Validation.hasText(password, Constants.ErrorMessage_password)) {
            ret = false;
        }
        return ret;
    }

    @Override
    public void onClick(View v) {
        Intent i;
        username = user_name.getText().toString().trim();
        switch (v.getId()) {
            case R.id.bt_login:
                if (checkValidation()) {
                    try {
                        sendData(username, password.getText().toString(), "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Login.this, "Please provide your login details.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_forgot_pass:
                i = new Intent(Login.this, ForgotPassword.class);
                startActivity(i);
                finish();
                break;
            case R.id.tv_register:
                i = new Intent(Login.this, Register.class);
                i.putExtra("Name", "N");
                i.putExtra("Email", "N");
                startActivity(i);
                finish();
                break;
            case R.id.bt_login_google:
                google_plus_login();
                break;
            default:
                break;
        }
    }

    private void google_plus_login() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }

    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    // Handling back press
    @Override
    public void onBackPressed() {
        if (backPressedToExitOnce) {
            finish();
        } else {
            this.backPressedToExitOnce = true;
            Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedToExitOnce = false;
                }
            }, 2000);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        // Get user's information
        getProfileInformation();

        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }

        /*intent = new Intent(Login.this, Register.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Name", personName.trim());
        intent.putExtra("Email", email.trim());


        startActivity(intent);
        finish();*/

        try {
            username = email.trim();
            sendData("", "", email.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    private void getProfileInformation() {
        int PROFILE_PIC_SIZE = 400;
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                personName = currentPerson.getDisplayName();
                email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            } else {
                Toast.makeText(getApplicationContext(), "Unable to process login please try again.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    public void readSMS() {
        SMSReader smsReader = new SMSReader();
        //Run async task and receive the completion of task in webservice result
        smsReader.loadTransactionSMSinDB(this, false, this, dbHelper);
    }

    @Override
    public void onWebServiceResult(String result, SERVICE_TYPE type) {
        ContentValues values;
        long id;
        String categoryId;
        switch (type) {
            case LOGIN:
                LoginResponse response = new Gson().fromJson(result, LoginResponse.class);
                switch (response.code) {
                    case 200:
                        pref.setString("UserId", response.userId.toString().trim());
                        if (cb.isChecked()) {
                            pref.setBoolean(Constants.KEY_LOGIN_CHECK, true);
                            values = new ContentValues();
                            values.put(DBConstants.USER_ID, response.userId);
                            values.put(DBConstants.USER_EMAIL, username);
                            values.put(DBConstants.DATE_MODIFIED, System.currentTimeMillis());
                            dbHelper.insertContentVals(DBConstants.USER_DETAILS, values);
                        } else {
                            pref.setclear();
                        }

                        try {
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
                                    case Constants.OUTSTANDING_AMOUNT_TAG:
                                        startEndTemplateModels = template.outstanding_amount;
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
                                /*    case Constants.DEBIT_CARD_TRANS_TAG:
                                        startEndTemplateModels = template.debit_card_transaction;
                                        break;*/
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
                            readSMS();
                            svLoginForm.setVisibility(View.GONE);
                            preparing_db.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 500:
                        Toast.makeText(getApplicationContext(), response.message, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }

                break;
            case SMS_LOAD:
                goToDashBoard();
                break;
            default:
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(getApplicationContext(), "Error occurred! Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }

    private void goToDashBoard() {
        setRecurringAlarm(getBaseContext());
        Intent i = new Intent(Login.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void setRecurringAlarm(Context context) {  // SERVICE FOR MSG SYNC at 6AM
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.add(Calendar.SECOND, 0);
        Calendar currentTimeCalendar = Calendar.getInstance();
        if (currentTimeCalendar.getTimeInMillis() > calendar.getTimeInMillis()) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent receiverIntent = new Intent(this, BackgroundReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 1234567, receiverIntent, 0);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        //  alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), 5*1000, sender);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)

            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
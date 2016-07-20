package com.faayda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.faayda.listeners.OnWebServiceResult;
import com.faayda.models.LoginResponse;
import com.faayda.network.NetworkManager;
import com.faayda.utils.Constants;
import com.faayda.utils.Validation;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by vinove on 7/29/2015.
 */
public final class ForgotPassword extends BaseActivity implements OnWebServiceResult, OnClickListener {
    EditText email;
    Button send;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.forgot_password);
        email = (EditText) findViewById(R.id.et_forgot_pass);
        send = (Button) findViewById(R.id.bt_send_pass);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send_pass:
                hideSoftKeyboard(this);
                if (checkValidation()) {
                    try {
                        sendData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.isEmailAddress(email, true, Constants.ErrorMessage_email)) {
            ret = false;
        }
        return ret;
    }

    private void sendData() throws JSONException {
        // TODO Auto-generated method stub
        JSONObject obj = new JSONObject();
        obj.put(Constants.KEY_METHOD, Constants.KEY_FGT_PWD);
        obj.put(Constants.KEY_EMAIL, email.getText());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(getString(R.string.postKey), obj.toString());
        NetworkManager networkManager = new NetworkManager(this, params, this);
        networkManager.callWebService(Constants.URL);
    }

    public void onBackPressed() {
        Intent i = new Intent(ForgotPassword.this, Login.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onWebServiceResult(String result, Constants.SERVICE_TYPE type) {
        LoginResponse response = new Gson().fromJson(result, LoginResponse.class);


        if (!(response == null)) {
            switch (response.code) {
                case 200:
                    Toast.makeText(getApplicationContext(), response.message, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ForgotPassword.this, Login.class);
                    startActivity(i);
                    finish();
                    break;

                case 500:
                    Toast.makeText(getApplicationContext(), response.message, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), "User info is null", Toast.LENGTH_LONG).show();
        }
    }
}
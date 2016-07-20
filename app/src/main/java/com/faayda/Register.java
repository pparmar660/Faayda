package com.faayda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.faayda.listeners.OnWebServiceResult;
import com.faayda.models.RegisterResponse;
import com.faayda.models.TermsAndPolicyResponse;
import com.faayda.models.TermsPolicyModel;
import com.faayda.network.NetworkManager;
import com.faayda.utils.Constants;
import com.faayda.utils.Constants.SERVICE_TYPE;
import com.faayda.utils.Validation;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by vinove on 7/27/2015.
 */
public final class Register extends BaseActivity implements OnWebServiceResult, OnClickListener {

    EditText name, email, password, confirm;
    TextView privacy;
    Button register, cancel;
    CheckBox terms_check, signed_in_check;
    SpannableString span;
    String terms, policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.register);

        privacy = (TextView) findViewById(R.id.txt_privacy);
        name = (EditText) findViewById(R.id.et_name);
        email = (EditText) findViewById(R.id.et_email);
        signed_in_check = (CheckBox) findViewById(R.id.ck_signed_in);
        terms_check = (CheckBox) findViewById(R.id.ck_terms);
        password = (EditText) findViewById(R.id.et_password);
        confirm = (EditText) findViewById(R.id.et_confirm_password);
        register = (Button) findViewById(R.id.bt_register);
        cancel = (Button) findViewById(R.id.bt_cancel);
        register.setOnClickListener(this);
        span = new SpannableString("I agree to the Faayda.com Terms of Services and Privacy Policy");
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.login_background)), 26, 42, 0);
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.login_background)), 47, 61, 0);

        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                termsPopup();
                widget.invalidate();
            }
        }, 26, 43, 0);
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                privacyPopup();
                widget.invalidate();
            }
        }, 47, 62, 0);
        privacy.setMovementMethod(LinkMovementMethod.getInstance());

        privacy.setText(span);

        cancel.setOnClickListener(this);
        try {
            privacyPolicy();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register:
                hideSoftKeyboard(this);
                if (checkValidation()) {
                    if (terms_check.isChecked()) {
                        try {
                            sendData();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please agree to Terms & Policy ", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.bt_cancel:
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();
                break;
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Register.this, Login.class);
        startActivity(i);
        finish();
    }

    private void sendData() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(Constants.KEY_METHOD, Constants.KEY_REGISTRATION);
        obj.put(Constants.KEY_NAME, name.getText());
        obj.put(Constants.KEY_EMAIL, email.getText());
        obj.put(Constants.KEY_PASSWORD, password.getText());
        obj.put(Constants.KEY_CONFIRM_PASSWORD, confirm.getText());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(getString(R.string.postKey), obj.toString());
        NetworkManager networkManager = new NetworkManager(this, params, this, SERVICE_TYPE.REGISTER);
        networkManager.callWebService(Constants.URL);
    }

    private void privacyPolicy() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(Constants.KEY_METHOD, Constants.KEY_TERMS_POLICY);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(getString(R.string.postKey), obj.toString());
        NetworkManager networkManager = new NetworkManager(this, params, this, SERVICE_TYPE.POLICY);
        networkManager.callWebService(Constants.URL);
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(name, Constants.ErrorMessage_name)) {
            ret = false;
//            name.setError("Invalid Name");
        } else if (!Validation.isEmailAddress(email, true, Constants.ErrorMessage_email)) {
            ret = false;
//            email.setError("Invalid Email address");
        } else if (!Validation.hasText(password, Constants.ErrorMessage_password) && !Validation.hasLength(password, Constants.ErrorMessage_password)) {
            ret = false;
//            password.setError("Invalid Password");
        } else if (!Validation.hasText(password, Constants.ErrorMessage_password)) {
            ret = false;
//            password.setError("Invalid Password");
        } else if (!Validation.hasLength(password, Constants.ErrorMessage_password)) {
            ret = false;
        } else if (!Validation.hasMatching(password, confirm, Constants.ErrorMessage_match)) {
            ret = false;
//            confirm.setError("Password Mismatch");
        } else if (!Validation.hasLength(password, Constants.ErrorMessage_password)) {
            ret = false;
        }
        return ret;
    }


    @Override
    public void onWebServiceResult(String result, SERVICE_TYPE type) {
        switch (type) {
            case REGISTER: {
                RegisterResponse response = new Gson().fromJson(result, RegisterResponse.class);
                switch (response.code) {
                    case 200:
                        Toast.makeText(getApplicationContext(), response.message, Toast.LENGTH_LONG).show();
                        try {
                            if (signed_in_check.isChecked()) {


                                Intent iNsxt = new Intent(Register.this, Login.class);
                                iNsxt.putExtra("doLogin", true);
                                iNsxt.putExtra("userName", email.getText().toString());
                                iNsxt.putExtra("password", password.getText().toString());
                                startActivity(iNsxt);
                                finish();
                            } else {
                                Intent iNsxt = new Intent(Register.this, Login.class);
                                startActivity(iNsxt);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;

                    case 500: {
                        Toast.makeText(getApplicationContext(), response.message, Toast.LENGTH_LONG).show();
                        break;
                    }
                    default:
                        break;
                }
                break;

            }
            case POLICY:
                TermsAndPolicyResponse response = new Gson().fromJson(result, TermsAndPolicyResponse.class);
                switch (response.code) {
                    case 200:
                        if (!(response == null)) {
                            for (TermsPolicyModel model : response.termsnpolicy) {
                                terms = model.termsServices;
                                policy = model.policy;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "User info is null", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 500: {
                        Toast.makeText(getApplicationContext(), "REGISTER : " + "Server Side Error", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default:
                        break;
                }
                break;
        }
    }


    private void privacyPopup() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.privacy_policy);
        TextView privacy_pol = (TextView) dialog.findViewById(R.id.tv_privacy);
        privacy_pol.setText(policy.trim());
        dialog.show();
    }

    private void termsPopup() {
        final Dialog dlg = new Dialog(this);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.terms_and_condition);
        TextView terms_and_services = (TextView) dlg.findViewById(R.id.tv_terms);
        terms_and_services.setText(terms.trim());
        dlg.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
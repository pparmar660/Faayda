package com.faayda.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.listeners.OnWebServiceResult;
import com.faayda.models.FeedbackResponse;
import com.faayda.network.NetworkManager;
import com.faayda.preferences.Preferences;
import com.faayda.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by vinove on 11/8/15.
 */
public final class Feedback extends Fragment implements View.OnClickListener, OnWebServiceResult {

    // UI Variable
    CheckBox chkBug, chkFeature, chkOther;
    EditText etComments;
    RatingBar btnRating;
    Button btnSend;

    // Other Variable
    Float ratingValue;
    String userId;
    Context mContext;
    Preferences pref;
    StringBuffer sb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = inflater.inflate(R.layout.feedback, container, false);
        ((MainActivity) getActivity()).setTopBarTitle(Constants.FEEDBACK);
        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
        ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);

        mContext = getActivity();
        sb = new StringBuffer();

        pref = new Preferences(mContext);
        userId = pref.getString("UserId");

        chkBug = (CheckBox) view.findViewById(R.id.chk_bug);
        chkFeature = (CheckBox) view.findViewById(R.id.chk_feature);
        chkOther = (CheckBox) view.findViewById(R.id.chk_other);

        chkBug.setOnClickListener(this);
        chkFeature.setOnClickListener(this);
        chkOther.setOnClickListener(this);

        etComments = (EditText) view.findViewById(R.id.et_comments);
        btnRating = (RatingBar) view.findViewById(R.id.btn_rating);
        ratingValue = btnRating.getRating();

        btnRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                ratingValue = String.valueOf(ratingBar);
                ratingValue = btnRating.getRating();
            }
        });

        btnSend = (Button) view.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

        return view;
    }

   /* @Override
    public void onPause() {
        ((MainActivity) getActivity()).setTopBarTitle(Constants.FEEDBACK);
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).setTopBarTitle(Constants.FEEDBACK);
//        ((MainActivity) getActivity()).invalidateTopBar(Constants.TITLE_BAR.NOTIFICATION_ONLY);
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chk_bug:
                if (chkBug.isChecked()) {
                    sb.setLength(0);
                    sb.append("BUG");
                    chkFeature.setChecked(false);
                    chkOther.setChecked(false);
                }
                break;
            case R.id.chk_feature:
                if (chkFeature.isChecked()) {
                    sb.setLength(0);
                    sb.append("FEATURE");
                    chkBug.setChecked(false);
                    chkOther.setChecked(false);
                }
                break;
            case R.id.chk_other:
                if (chkOther.isChecked()) {
                    sb.setLength(0);
                    sb.append("OTHER");
                    chkBug.setChecked(false);
                    chkFeature.setChecked(false);
                }
                break;
            case R.id.btn_send:
                sendFeedback(sb.toString().trim());
                break;
            default:
                break;
        }
    }

    private void sendFeedback(String feedbackType) {
        try {
            if (feedbackType.isEmpty()) {
                Toast.makeText(mContext, "Please select FeedBack Type", Toast.LENGTH_SHORT).show();
            } else if (etComments.getText().toString().trim().isEmpty()) {
                Toast.makeText(mContext, "Please enter your comments", Toast.LENGTH_SHORT).show();
            } else if (ratingValue.equals(null)) {
                Toast.makeText(mContext, "Please RateUs", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject obj = new JSONObject();
                obj.put(Constants.KEY_METHOD, Constants.KEY_FEEDBACK);
                obj.put("feedback_type", feedbackType.trim());
                obj.put("comment", etComments.getText().toString().trim());
                obj.put("rating", ratingValue.toString().trim());
                obj.put("userId", userId);
                HashMap<String, String> params = new HashMap<>();
                params.put(getString(R.string.postKey), obj.toString());
                NetworkManager networkManager = new NetworkManager(mContext, params, this);
                networkManager.callWebService(Constants.URL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWebServiceResult(String result, Constants.SERVICE_TYPE type) {
        FeedbackResponse response = new Gson().fromJson(result, FeedbackResponse.class);
        if (!(response == null)) {
            switch (response.code) {
                case 200: {
                    Toast.makeText(mContext, response.message, Toast.LENGTH_LONG).show();
                    etComments.setText("");
                    chkFeature.setChecked(false);
                    chkBug.setChecked(false);
                    chkOther.setChecked(false);
                    break;
                }
                case 500: {
                    Toast.makeText(mContext, response.message, Toast.LENGTH_LONG).show();
                    break;
                }
                default:
                    break;
            }
        } else {
            Toast.makeText(mContext, Constants.ErrorMessage_default, Toast.LENGTH_LONG).show();
        }
    }
}
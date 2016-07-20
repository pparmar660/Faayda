/*
package com.faayda.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.utils.Constants;

*/
/**
 * Created by vinove on 28/8/15.
 *//*

public class Notification extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View layoutView = inflater.inflate(R.layout.bill_notification, container, false);
        ((MainActivity) getActivity()).setTopBarTitle(Constants.NOTIFICATION);
        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
        ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);
        ((MainActivity) getActivity()).ibNotificationIcon.setVisibility(View.VISIBLE);

        TextView msg = (TextView) layoutView.findViewById(R.id.tv_bill_detail);
//        msg.setText(smsBody.trim());

        TextView sender = (TextView) layoutView.findViewById(R.id.tv_msg_from);
//        sender.setText(smsSender.trim());

        TextView date = (TextView) layoutView.findViewById(R.id.tv_bill_due_date2);
//        date.setText(CommonUtils.getDateFromTimestamp(smsDate).toString().trim());

        TextView btnLink = (TextView) layoutView.findViewById(R.id.tv_yes_bill);
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        TextView btnDismiss = (TextView) layoutView.findViewById(R.id.tv_dismiss);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return getView();
    }
}
*/

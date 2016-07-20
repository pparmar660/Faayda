package com.faayda.fragment.dashboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.faayda.BaseActivity;
import com.faayda.R;
import com.faayda.customviews.PieChart;
import com.faayda.database.DBHelper;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Aashutosh @ vinove on 8/5/2015.
 */
public final class TrendFragment extends Fragment implements ViewTreeObserver.OnScrollChangedListener {

    PieChart pieChart;
    DBHelper dbHelper;
    ArrayList<Long> percentageList;
    LinearLayout pieChartContainer;
    TextView tvTotalAmount, tvExpenseValue, tvLatestTransactionsValue, tvSavingValue;
    Double total, totalIncome;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ScrollView layoutView = (ScrollView) inflater.inflate(R.layout.trends_layout, container, false);
        mContext = getActivity();
        pieChartContainer = (LinearLayout) layoutView.findViewById(R.id.llPieChartContainer);
        long latestTimeStamp = CommonUtils.checkLatestMonthTimeStamp(mContext);
        tvTotalAmount = (TextView) layoutView.findViewById(R.id.tvTotalAmount);
        tvLatestTransactionsValue = (TextView) layoutView.findViewById(R.id.tvLatestTransactionsValue);
        tvSavingValue = (TextView) layoutView.findViewById(R.id.tvSavingValue);
        tvExpenseValue = (TextView) layoutView.findViewById(R.id.tvExpenseValue);
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        int lastWeekDay = 7;


        if (calendar.get(Calendar.DAY_OF_MONTH) < 7)
            lastWeekDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.DATE, -lastWeekDay);
        long lastWeekTimeStamp = calendar.getTime().getTime();
//        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
        Double savingAmount = 0d;
        totalIncome = CommonUtils.getIncomeAmount(dbHelper, latestTimeStamp);
        Double expenseAmount = CommonUtils.getExpenseAmount(dbHelper, latestTimeStamp);
        Double latestTransaction = CommonUtils.getExpenseAmount(dbHelper, lastWeekTimeStamp);
        tvTotalAmount.setText(CommonUtils.DoubleToStringLimits(Double.parseDouble(String.valueOf(Math.round(totalIncome)))));

        savingAmount = totalIncome - expenseAmount;


        if (expenseAmount > latestTransaction)
            expenseAmount = expenseAmount - latestTransaction;
        else expenseAmount = 0d;

        if (savingAmount <= 0) {
            savingAmount = 0d;
            total = expenseAmount + latestTransaction;
        } else total = savingAmount + expenseAmount + latestTransaction;

        tvExpenseValue.setText(CommonUtils.DoubleToStringLimits(Double.parseDouble(String.valueOf(Math.round(expenseAmount)))));
        tvLatestTransactionsValue.setText(CommonUtils.DoubleToStringLimits(Double.parseDouble(String.valueOf(Math.round(latestTransaction)))));
        tvSavingValue.setText(CommonUtils.DoubleToStringLimits(Double.parseDouble(String.valueOf(Math.round(savingAmount)))));


        percentageList = new ArrayList<>();
        this.percentageList.add(Math.round((savingAmount / total) * 100));//Income
        this.percentageList.add(Math.round((expenseAmount / total) * 100));//Expense
        this.percentageList.add(Math.round((latestTransaction / total) * 100));//Latest Transactions
        pieChartContainer.post(new Runnable() {
            @Override
            public void run() {

                //    pieChart = new PieChart(mContext, percentageList, (int)pieChartContainer.getWidth() *(0.3), (int)((pieChartContainer.getWidth() / 2)- (pieChartContainer.getWidth() *0.1)));

                pieChart = new PieChart(mContext, percentageList, (int) (Constants.screenWith * (0.5)), (int) (Constants.screenHeight * (0.2)));

                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams((int) (Constants.screenWith * (1)), (int) (Constants.screenHeight * 0.4));
                pieChart.setLayoutParams(layoutParams);

                if (totalIncome <= 0) {
                    TextView tvError = new TextView(mContext);
                    tvError.setText("No Data");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    tvError.setLayoutParams(params);
                    tvError.setTextSize(16);
                    tvError.setTextColor(Color.rgb(0xAA, 0xAA, 0xAA));
                    pieChartContainer.addView(tvError);

                } else {
                    pieChartContainer.addView(pieChart);
                }
                pieChartContainer.invalidate();
            }
        });
        layoutView.getViewTreeObserver().addOnScrollChangedListener(this);
        return layoutView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = ((BaseActivity) activity).dbHelper;
    }

    @Override
    public void onScrollChanged() {

    }
}

/*        expenseAmount = expenseAmount - latestTransaction;
        savingAmount = savingAmount - expenseAmount;
        savingAmount = savingAmount <= 0 ? 0 : savingAmount;
        expenseAmount = expenseAmount <= 0 ? 0 : expenseAmount;

        tvExpenseValue.setText(String.valueOf(Math.round(expenseAmount)));
        tvLatestTransactionsValue.setText(String.valueOf(Math.round(latestTransaction)));


// total expense is greater than income then make saving zero and total  is sum of
// latest expenditure and expense so graph is make between only for them
        if (savingAmount <=0) {
            tvSavingValue.setText("0.0");
            savingAmount =0d;
            total =latestTransaction+expenseAmount;
        }
        else tvSavingValue.setText(String.valueOf(Math.round(savingAmount)));*/
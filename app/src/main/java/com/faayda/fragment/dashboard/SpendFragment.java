package com.faayda.fragment.dashboard;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.faayda.BaseActivity;
import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.imageloader.Loader;
import com.faayda.listeners.OnFragmentChangeListener;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;

/**
 * Created by vinove on 7/30/2015.
 */
public final class SpendFragment extends Fragment implements View.OnClickListener {

    DBHelper dbHelper;
    Button bt;
    LayoutInflater inflater;
    ProgressBar pbCashSlider;
    RelativeLayout rlspendFragmentRow;
    OnFragmentChangeListener onFragmentChangeListener;
    double totalCashWithdrawl, totalCashSpent;
    double progressBarFilledWidth, percentageOfCashSpent;
    LinearLayout llCategoryIconsContainer, llCashSliderIndicator;
    double progressBarWidth, cashSliderIndicatorWidth, cashSpentWidth;
    TextView tvSpendAmount, tvSpendMonth, tvTotalCashAmount, tvCashSpent, tvTotalAmountMonth;
    FragmentManager fragmentManager;
    Loader lodimg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View layoutView = inflater.inflate(R.layout.spend_fragment, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();

        rlspendFragmentRow = (RelativeLayout) layoutView.findViewById(R.id.rlspendFragmentRow);
   /*     bt= ((MainActivity)getActivity()).findViewById(R.id.ibBackIcon);*/
        pbCashSlider = (ProgressBar) layoutView.findViewById(R.id.pbCashSlider);
        llCashSliderIndicator = (LinearLayout) layoutView.findViewById(R.id.llCashSliderIndicator);
        llCategoryIconsContainer = (LinearLayout) layoutView.findViewById(R.id.llCategoryIconsContainer);
        tvSpendAmount = (TextView) layoutView.findViewById(R.id.tvSpendAmount);
        tvSpendMonth = (TextView) layoutView.findViewById(R.id.tvSpendMonth);
        tvTotalCashAmount = (TextView) layoutView.findViewById(R.id.tvTotalCashAmount);
        tvCashSpent = (TextView) layoutView.findViewById(R.id.tvCashSpent);
        tvTotalAmountMonth = (TextView) layoutView.findViewById(R.id.tvTotalAmountMonth);
        return layoutView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        totalCashWithdrawl = 0;
        totalCashSpent = 0;
        long latestTimeStamp = 0;
        Double expenseAmount = 0d;
        try {
            latestTimeStamp = CommonUtils.checkLatestMonthTimeStamp(getActivity());
            expenseAmount = CommonUtils.getExpenseAmount(dbHelper, latestTimeStamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvSpendAmount.setText(CommonUtils.DoubleToStringLimits(expenseAmount));
        tvSpendMonth.setText(CommonUtils.getCurrentMonthYear());
        tvTotalAmountMonth.setText(CommonUtils.getCurrentMonthYear());
        lodimg = new Loader(getActivity());
        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
        //Expense Categories

        // pp start
/*        String[] billerIds = dbHelper.getValues(true, DBConstants.TRANSACTIONS, DBConstants.TRANSACTION_BILLER_ID, DBConstants.TRANSACTION_DATE + ">" + latestTimeStamp, null);
        String queryString, categoryIds;
        String[] billerCategoryIds, categoryTitles;
        if (billerIds != null && billerIds.length > 0) {
            queryString = CommonUtils.getStringFromArray(billerIds);
            billerCategoryIds = dbHelper.getValues(false, DBConstants.BILLER_LIST, DBConstants.CATEGORY_ID, DBConstants.ID + " IN (" + queryString + ")", null);
            categoryIds = CommonUtils.getStringFromArray(billerCategoryIds);
            categoryTitles = dbHelper.getValues(false, DBConstants.CATEGORIES, DBConstants.CATEGORY_TITLE, DBConstants.ID + " IN (" + categoryIds + ")", null);
            System.out.println();


            for (String string : categoryTitles) {
                llCategoryIconsContainer.addView(addCategoryView(string));
            }
        }

        //Slider Logic
        //TODO: Remove Comment before putting on live device and comment dummy values used
        int cashWithdrawlId = Integer.parseInt(dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.ATM_WDL + "'"));
        String[] cashValues = dbHelper.getValues(false, DBConstants.TRANSACTIONS, DBConstants.TRANSACTION_AMOUNT, DBConstants.TRANSACTION_TYPE_ID + "=" + cashWithdrawlId + " AND " + DBConstants.TRANSACTION_DATE + ">" + latestTimeStamp, null);
        for (String value : cashValues) {
            totalCashWithdrawl += Double.parseDouble(value);
        }*/
        Cursor cursor = null;
        try {
/*            String selectQuery=  "SELECT T.X (SELECT C."+DBConstants.CATEGORY_TITLE+" X, SUM(O."+DBConstants.TRANSACTION_AMOUNT+") FROM "+
                      DBConstants.CATEGORIES+" C, " +
                      DBConstants.TRANSACTIONS+" O WHERE C."+DBConstants.ID+" = O."+DBConstants.CATEGORY_ID+" GROUP BY O."+
                      DBConstants.CATEGORY_ID+" ORDER BY 2 DESC LIMIT 1) T";*/

            String creditId = dbHelper.getValue(DBConstants.TRANSACTION_TYPE,
                    DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.CREDIT + "'");

            String refund = dbHelper.getValue(DBConstants.TRANSACTION_TYPE,
                    DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.REFUND + "'");
            ;


            String selectQuery = "SELECT C." + DBConstants.CATEGORY_TITLE + " , SUM(O." + DBConstants.TRANSACTION_AMOUNT + ") FROM " +
                    DBConstants.CATEGORIES + " C, " +
                    DBConstants.TRANSACTIONS + " O WHERE C." + DBConstants.ID + " = O." + DBConstants.CATEGORY_ID + " AND O." +
                    DBConstants.TRANSACTION_DATE + " > " + latestTimeStamp + " AND O." + DBConstants.TRANSACTION_TYPE_ID + " != " + creditId + " AND O." +
                    DBConstants.TRANSACTION_TYPE_ID + " != " + refund + " GROUP BY O." +
                    DBConstants.CATEGORY_ID + " ORDER BY SUM(O." + DBConstants.TRANSACTION_AMOUNT + ") DESC LIMIT 4";
            cursor = dbHelper.getDataThroughRaw(selectQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (cursor != null)
            if (cursor.moveToFirst()) {
                do {
                    llCategoryIconsContainer.addView(addCategoryView(cursor.getString(cursor.getColumnIndex(DBConstants.CATEGORY_TITLE)),
                            CommonUtils.DoubleToStringLimits(cursor.getDouble(1))));
                } while (cursor.moveToNext());

            }
        //pp end


        final int atmTransactionId = Integer.parseInt(dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " = '" +
                Constants.ATM_WDL + "'"));

        final int debitTransactionTypeId = Integer.parseInt(dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " = '" +
                Constants.DEBIT + "'"));


   /*     // OR (" + DBConstants.TRANSACTION_ACCOUNT_ID + " = '-10' AND "+DBConstants.TRANSACTION_TYPE_ID+" = '"+dbHelper.getValue() +"') "
        String[] cashSpent = dbHelper.getValues(false, DBConstants.TRANSACTIONS, DBConstants.TRANSACTION_AMOUNT,
                "(" + DBConstants.TRANSACTION_TYPE_ID + "=" + atmTransactionId + " OR (" + DBConstants.TRANSACTION_TYPE_ID + "=" +
                        debitTransactionTypeId + " AND " + DBConstants.TRANSACTION_ACCOUNT_ID + "='-10')) AND "
                        + DBConstants.TRANSACTION_DATE + ">" + latestTimeStamp, null);

        for (String value : cashSpent) {
            totalCashSpent += Double.parseDouble(value);
        }*/

        String sqlQuery = "select sum(" + DBConstants.TRANSACTION_AMOUNT + ") from " + DBConstants.TRANSACTIONS + " where " +
                DBConstants.TRANSACTION_TYPE_ID + " = " + atmTransactionId + " and  " + DBConstants.TRANSACTION_DATE + " > " + latestTimeStamp;

        Cursor atmWithdrawalCursor = dbHelper.getDataThroughRaw(sqlQuery);
        if (atmWithdrawalCursor != null)
            if (atmWithdrawalCursor.moveToFirst())
                totalCashWithdrawl = atmWithdrawalCursor.getDouble(0);

        sqlQuery = "select sum(" + DBConstants.TRANSACTION_AMOUNT + ") from " + DBConstants.TRANSACTIONS + " where " +
                DBConstants.TRANSACTION_TYPE_ID + " = " + debitTransactionTypeId + " and  " + DBConstants.TRANSACTION_ACCOUNT_ID + " = '-10' and " +
                DBConstants.TRANSACTION_DATE + " > " + latestTimeStamp;


        Cursor cashCursor = dbHelper.getDataThroughRaw(sqlQuery);
        if (cashCursor != null)
            if (cashCursor.moveToFirst())
                totalCashSpent = cashCursor.getDouble(0);


        pbCashSlider.post(new Runnable() {
            @Override
            public void run() {
                percentageOfCashSpent = ((totalCashSpent / totalCashWithdrawl) * 100);
                tvTotalCashAmount.setText(CommonUtils.DoubleToStringLimits(Double.parseDouble(String.valueOf(Math.round(totalCashWithdrawl)))));
//                tvCashSpent.setText(String.valueOf(Math.round(totalCashWithdrawl - totalCashSpent)));
                //tvCashSpent.setText(String.valueOf(Math.round((totalCashSpent / totalCashWithdrawl) * 100)));
                double i = (totalCashSpent / totalCashWithdrawl) * 100;
                double totalSpent = (totalCashWithdrawl * i) / 100;
                tvCashSpent.setText(CommonUtils.DoubleToStringLimits(Double.parseDouble(String.valueOf(Math.round(totalSpent)))));

                pbCashSlider.setProgress((int) Math.round(percentageOfCashSpent));
                progressBarWidth = pbCashSlider.getWidth();
                progressBarFilledWidth = progressBarWidth * (percentageOfCashSpent / 100);

                tvCashSpent.post(new Runnable() {
                    @Override
                    public void run() {
                        cashSpentWidth = tvCashSpent.getWidth();
                        RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        double leftMargin = progressBarFilledWidth - cashSpentWidth / 2;
                        if (leftMargin < cashSpentWidth)
                            tvParams.setMargins(0, 0, 0, 0);
                        else if (leftMargin > (progressBarFilledWidth - cashSpentWidth))
                            tvParams.setMargins((int) leftMargin, 0, 0, 0);
                        tvParams.addRule(RelativeLayout.BELOW, tvTotalAmountMonth.getId());
                        tvCashSpent.setLayoutParams(tvParams);
                        tvCashSpent.invalidate();

                        llCashSliderIndicator.post(new Runnable() {
                            @Override
                            public void run() {
                                cashSliderIndicatorWidth = llCashSliderIndicator.getWidth();
                                RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                                double leftMargin = progressBarFilledWidth - cashSliderIndicatorWidth / 2;
                                if (leftMargin < cashSliderIndicatorWidth)
                                    llParams.setMargins(0, 0, 0, 0);
                                else if (leftMargin > (progressBarFilledWidth - cashSliderIndicatorWidth))
//                                    llParams.setMargins((int) (progressBarFilledWidth - cashSliderIndicatorWidth), 0, 0, 0);
                                    llParams.setMargins((int) leftMargin, 0, 0, 0);
                                llParams.addRule(RelativeLayout.BELOW, tvCashSpent.getId());
                                llCashSliderIndicator.setLayoutParams(llParams);
                                llCashSliderIndicator.invalidate();
                            }
                        });
                    }
                });
            }
        });
        rlspendFragmentRow.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = LayoutInflater.from(getActivity());
        dbHelper = ((BaseActivity) activity).dbHelper;
        onFragmentChangeListener = (OnFragmentChangeListener) activity;
    }

    public View addCategoryView(String category, String amount) {
        View view = inflater.inflate(R.layout.max_expense_category_item, null);
        ImageView ivMaxExpenseCatIcon = (ImageView) view.findViewById(R.id.ivMaxExpenseCatIcon);
        TextView ivMaxExpenseCatText = (TextView) view.findViewById(R.id.ivMaxExpenseCatText);
        TextView ivMaxExpenseAmount = (TextView) view.findViewById(R.id.ivMaxExpenseAmount);
        ivMaxExpenseAmount.setVisibility(View.VISIBLE);

        ivMaxExpenseCatText.setText(category);
        ivMaxExpenseAmount.setText(amount);
        try {
            if (CommonUtils.getCategoryIcon(category) != -1)
                ivMaxExpenseCatIcon.setImageResource(CommonUtils.getCategoryIcon(category));
            else
                lodimg.displayImage(dbHelper.getValue(DBConstants.CATEGORIES, DBConstants.CATEGORY_IMAGE,
                        DBConstants.CATEGORY_TITLE + " = '" + category + "'"), ivMaxExpenseCatIcon,R.drawable.medical);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlspendFragmentRow:
                TransactionFragment fragment = new TransactionFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.EXPENSE, true);
                fragment.setArguments(bundle);
                onFragmentChangeListener.onFragmentReplace(fragment, Constants.TRANSACTION_TYPE);
                break;
            default:
                break;
        }
    }
}
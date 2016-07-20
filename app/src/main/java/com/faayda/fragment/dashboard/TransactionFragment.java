package com.faayda.fragment.dashboard;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.faayda.BaseActivity;
import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.adapter.TransactionAdapter;
import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.fragment.AddTransaction;
import com.faayda.fragment.DashBoardFragment;
import com.faayda.models.TransactionModel;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;

/**
 * Created by Aashutosh @ vinove on 8/5/2015.
 */
public final class TransactionFragment extends Fragment implements AdapterView.OnItemClickListener, OnScrollListener {

    DBHelper dbHelper;
    boolean expenseTransactions;
    private ListView transactionList;
    private TransactionAdapter adapter;
    private DashBoardFragment dashboardFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        String bankName;
        View layoutView = inflater.inflate(R.layout.transactions_layout, container, false);
        transactionList = (ListView) layoutView.findViewById(R.id.lvTransactionsList);

//        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
        /*((MainActivity) getActivity()).onFragmentReplace(dashboardFragment, Constants.ADD_TRANSACTION);*/
        adapter = new TransactionAdapter(getActivity());
        transactionList.setAdapter(adapter);
        transactionList.setOnScrollListener(this);
        transactionList.setOnItemClickListener(this);
      /*  back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onFragmentReplace(dashboardFragment, Constants.ADD_TRANSACTION);
            }
        });*/
        // Direct to dashboard

        long timeStamp = CommonUtils.checkLatestMonthTimeStamp(getActivity());
        String whereClause = "";
        if (getArguments() != null) {
            expenseTransactions = getArguments().getBoolean(Constants.EXPENSE);
            whereClause = " AND " + DBConstants.TRANSACTION_TYPE_ID + " IN (" + CommonUtils.getTransactionIdsString(dbHelper) + ")";
        }

    /*    Cursor transactionCursor = ((BaseActivity) getActivity()).dbHelper.getTableRecords(DBConstants.TRANSACTIONS, null, DBConstants.TRANSACTION_DATE + ">"
                + timeStamp + whereClause, null);*/
        Cursor transactionCursor = ((BaseActivity) getActivity()).dbHelper.getTableRecords(DBConstants.TRANSACTIONS, null, null, DBConstants.TRANSACTION_DATE + " DESC ", "20    ");


        if (transactionCursor.getCount() > 0) {

            TransactionModel transactionModel;
            String transactionDate = "", biller = "", bankIdStr = null;
            while (transactionCursor.moveToNext()) {
                long bankId = 0;
                bankIdStr=null;
                transactionModel = new TransactionModel();
                transactionModel.setId(transactionCursor.getLong(transactionCursor.getColumnIndex(DBConstants.ID)));
                transactionModel.setAmount(transactionCursor.getString(transactionCursor.getColumnIndex(DBConstants.TRANSACTION_AMOUNT)));
                transactionDate = CommonUtils.getDateFromMillis(Long.parseLong(transactionCursor.getString(transactionCursor.getColumnIndex(DBConstants.TRANSACTION_DATE))), "dd MMM yyyy");
                transactionModel.setCardNumber(transactionCursor.getString(transactionCursor.getColumnIndex(DBConstants.CARD_NUMBER)));
                transactionModel.setAccountNumber(dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ACCOUNT_NUMBER, DBConstants.ID
                        + " = " + transactionCursor.getLong(transactionCursor.getColumnIndex(DBConstants.TRANSACTION_ACCOUNT_ID))));
                transactionModel.setMonth(transactionDate);
                transactionModel.setTransactionTypeId(transactionCursor.getLong(transactionCursor.getColumnIndex(DBConstants.TRANSACTION_TYPE_ID)));
                transactionModel.setTransactionTypeId(transactionCursor.getLong(transactionCursor.getColumnIndex((DBConstants.TRANSACTION_ACCOUNT_ID))));

                //TODO: Change the biller as per SMS

                String sqlQuery = "select b." + DBConstants.BANK_ID + " from " + DBConstants.BANK_SMS_CODES + " b inner join " +
                        DBConstants.SMS + " s on b." + DBConstants.BANK_SMS_SENDER + " = s." + DBConstants.SMS_SENDER +
                        " where s." + DBConstants.ID + "=" + transactionCursor.getLong(transactionCursor.getColumnIndex(DBConstants.SMS_ID));

                Cursor bankIdCursor = dbHelper.getDataThroughRaw(sqlQuery);
                if (bankIdCursor != null)
                    if (bankIdCursor.moveToFirst())
                        bankIdStr = bankIdCursor.getLong(0) + "";

/*
                bankIdStr = dbHelper.getValue(DBConstants.BANK_SMS_CODES, DBConstants.BANK_ID, DBConstants.BANK_SMS_SENDER + " = '" +
                        transactionCursor.getString(transactionCursor.getColumnIndex(DBConstants.SMS_SENDER)) + "'");*/
                long accountId = transactionCursor.getLong(transactionCursor.getColumnIndex(DBConstants.TRANSACTION_ACCOUNT_ID));
                if (bankIdStr == null)
                    if (accountId > 0) {
                        String sqlquery = "select " + DBConstants.ACCOUNT_BANK_ID + " from " + DBConstants.USER_ACCOUNTS + " where " + DBConstants.ID + " = " + accountId;
                        if (dbHelper.getDataThroughRaw(sqlquery) != null)
                            bankIdCursor = dbHelper.getDataThroughRaw(sqlquery);
                        if (bankIdCursor.moveToFirst())
                            bankIdStr = bankIdCursor.getLong(0) + "";


                    }
                //bankIdStr = dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ACCOUNT_BANK_ID, DBConstants.ID + " = " + accountId);

                //bankIdStr = dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ACCOUNT_BANK_ID, DBConstants.ID + " = " + transactionCursor.getLong(transactionCursor.getColumnIndex(DBConstants.TRANSACTION_ACCOUNT_ID)));
                try {
                    if (bankIdStr != null)
                        bankId = Long.parseLong(bankIdStr);
                } catch (Exception e) {
                    bankId = 0;
                }
                if (bankId <= 0) {
                    transactionModel.setTitle("Other Bank");
                    transactionModel.setImage(CommonUtils.getMerchantImage("OTHER"));
                } else {
                    bankName = dbHelper.getValue(DBConstants.BANKS, DBConstants.BANK_NAME, DBConstants.ID + " = " + bankId);
                    transactionModel.setTitle(bankName);
                    transactionModel.setImage(CommonUtils.getMerchantImage(bankName));
                }

                if (transactionModel.getAmount() <= 0) {
                    continue;
                } else {
                    adapter.addItem(transactionModel);
                }
            }
        }
        return layoutView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position > adapter.dataModel.size())
            return;

        try {
            long transactionId = adapter.getItem(position).getId();
            Bundle bundle = new Bundle();
            bundle.putLong(Constants.TRANSACTION_ID, transactionId);
            bundle.putBoolean("tag", false);
            AddTransaction addTransaction = new AddTransaction();
            addTransaction.setArguments(bundle);
            boolean chkResult = checkCanEditTransaction(transactionId + "");
            if (chkResult)
                if (checkCanEditTransaction(transactionId + ""))
                    ((MainActivity) getActivity()).onFragmentReplace(addTransaction, Constants.ADD_TRANSACTION);
                else {
                    Toast.makeText(getActivity(), "Sorry you can not edit this transaction because no account can linked with it", Toast.LENGTH_LONG).show();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int mLastFirstVisibleItem = 0;
    boolean mIsScrollingUp;

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            //Scrolling Stopped
            if (dashboardFragment != null)
                dashboardFragment.setFloatingButtonsVisibility(View.VISIBLE);
        }
        //Scrolling
        if (absListView.getId() == transactionList.getId()) {
            final int currentFirstVisibleItem = transactionList.getFirstVisiblePosition();
            if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                //Scrolling Up
                mIsScrollingUp = false;
                if (dashboardFragment != null)
                    dashboardFragment.setFloatingButtonsVisibility(View.GONE);
            } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                //Scrolling Down
                mIsScrollingUp = true;
                if (dashboardFragment != null)
                    dashboardFragment.setFloatingButtonsVisibility(View.GONE);
            }
            mLastFirstVisibleItem = currentFirstVisibleItem;
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = ((BaseActivity) activity).dbHelper;
        // dashboardFragment = (DashBoardFragment)getParentFragment();
    }

    boolean checkCanEditTransaction(String transactionId) {

        //if account number exist
        try {
            if (Long.parseLong(dbHelper.getValue(DBConstants.TRANSACTIONS, DBConstants.TRANSACTION_ACCOUNT_ID, DBConstants.ID + " = " + transactionId)) > 0)
                return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // if debit card number exit

        try {
            if (Long.parseLong(dbHelper.getValue(DBConstants.TRANSACTIONS, DBConstants.CARD_NUMBER, DBConstants.ID + " = " + transactionId)) > 0)
                return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        //if account id is -10 then can edit because it is liked with cash
        try {
            if (Long.parseLong(dbHelper.getValue(DBConstants.TRANSACTIONS, DBConstants.TRANSACTION_ACCOUNT_ID, DBConstants.ID + " = " + transactionId)) == -10)
                return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        //if bank id can linked  with current
        try {

            String smsId = dbHelper.getValue(DBConstants.TRANSACTIONS, DBConstants.SMS_ID, DBConstants.ID + " = " + transactionId);

     /*       String smsSender = (dbHelper.getValue(DBConstants.TRANSACTIONS, DBConstants.SMS_SENDER, DBConstants.ID + " = " + transactionId));

            String bankIdStr = dbHelper.getValue(DBConstants.BANK_SMS_CODES, DBConstants.BANK_ID, DBConstants.BANK_SMS_SENDER + " = '" +
                    smsSender + "'");*/
            String sqlQuery = "select b." + DBConstants.BANK_ID + " from " + DBConstants.BANK_SMS_CODES + " b inner join " +
                    DBConstants.SMS + " s on b." + DBConstants.BANK_SMS_SENDER + " = s." + DBConstants.SMS_SENDER +
                    " where s." + DBConstants.ID + "=" +smsId;

            Cursor bankIdCursor = dbHelper.getDataThroughRaw(sqlQuery);
            String bankIdStr = "";
            if (bankIdCursor != null)
                if (bankIdCursor.moveToFirst())
                    bankIdStr = bankIdCursor.getLong(0) + "";


            if (Long.parseLong(dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ID, DBConstants.ACCOUNT_BANK_ID + " = " + bankIdStr)) > 0)
                return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return false;

    }
}
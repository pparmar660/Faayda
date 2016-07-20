package com.faayda.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.faayda.BaseActivity;
import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.adapter.CashManagmentAdapter;
import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.models.CashManagementModel;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;

/**
 * Created by vinove on 8/12/2015.
 */
public final class CashManagment extends Fragment implements OnItemClickListener, View.OnClickListener {

    ListView cashList;
    DBHelper dbHelper;
    ImageView floatingButton;
    CashManagmentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ((MainActivity) getActivity()).setTopBarTitle(Constants.CASH_MANAGEMENT);
        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
        ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);

        View layoutView = inflater.inflate(R.layout.cash_managment, container, false);
        cashList = (ListView) layoutView.findViewById(R.id.lv_cash_managment);
        floatingButton = (ImageView) layoutView.findViewById(R.id.floatingButton);

        adapter = new CashManagmentAdapter(getActivity());
        cashList.setAdapter(adapter);
        loadData();

        floatingButton.setOnClickListener(this);
        cashList.setOnItemClickListener(this);
        return layoutView;
    }

    /*@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).setTopBarTitle(Constants.CASH_MANAGEMENT);
//        ((MainActivity) getActivity()).invalidateTopBar(Constants.TITLE_BAR.NOTIFICATION_ONLY);
    }*/

    private void loadData() {
        long cashType = Long.parseLong(dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.ATM_WDL + "'"));
        Cursor cursor = dbHelper.getTableRecords(DBConstants.TRANSACTIONS, null, DBConstants.TRANSACTION_TYPE_ID + " = " + cashType, DBConstants.ID + " DESC");
        CashManagementModel managementModel;
        String account, bankName;
        int accountType = 1;
        long bankId = 0, transactionTime;
        String bankIdStr=null;
        while (cursor.moveToNext()) {
            bankIdStr=null;
            managementModel = new CashManagementModel();
            managementModel.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.ID)));
            account = dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ACCOUNT_NUMBER, DBConstants.ID + " = " + cursor.getLong(cursor.getColumnIndex(DBConstants.TRANSACTION_ACCOUNT_ID)));


            String sqlQuery = "select b." + DBConstants.BANK_ID + " from " + DBConstants.BANK_SMS_CODES + " b inner join " +
                    DBConstants.SMS + " s on b." + DBConstants.BANK_SMS_SENDER + " = s." + DBConstants.SMS_SENDER +

                    " where s." + DBConstants.ID + "=" + cursor.getLong(cursor.getColumnIndex(DBConstants.SMS_ID));

            Cursor bankIdCursor = dbHelper.getDataThroughRaw(sqlQuery);
            if (bankIdCursor != null)
                if (bankIdCursor.moveToFirst())
                    bankIdStr = bankIdCursor.getLong(0) + "";

            if (bankIdStr == null)
                bankIdStr = dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ACCOUNT_BANK_ID, DBConstants.ID + " = "
                        + cursor.getLong(cursor.getColumnIndex(
                        DBConstants.TRANSACTION_ACCOUNT_ID)));

            //bankIdStr = dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ACCOUNT_BANK_ID, DBConstants.ID + " = " + transactionCursor.getLong(transactionCursor.getColumnIndex(DBConstants.TRANSACTION_ACCOUNT_ID)));
            try {
                if (bankIdStr != null)
                    bankId = Long.parseLong(bankIdStr);
            } catch (Exception e) {
                bankId = 0;
            }

            try {
                if (dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ACCOUNT_TYPE_ID, DBConstants.ID + " = " + cursor.getLong(cursor.getColumnIndex(DBConstants.TRANSACTION_ACCOUNT_ID))) != null)
                    accountType = Integer.parseInt(dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ACCOUNT_TYPE_ID, DBConstants.ID + " = " + cursor.getLong(cursor.getColumnIndex(DBConstants.TRANSACTION_ACCOUNT_ID))));

                managementModel.setAccountType(accountType);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            managementModel.setAccountNo(account);
            managementModel.setAmount(cursor.getString(cursor.getColumnIndex(DBConstants.TRANSACTION_AMOUNT)));
            bankName = dbHelper.getValue(DBConstants.BANKS, DBConstants.BANK_NAME, DBConstants.ID + " = " + bankId);
            managementModel.setBankLogo(CommonUtils.getMerchantImage(bankName));
            managementModel.setBankName(bankName);
            transactionTime = cursor.getLong(cursor.getColumnIndex(DBConstants.TRANSACTION_DATE));
            managementModel.setExactDate(CommonUtils.formattedDate(transactionTime));
            managementModel.setMonth(CommonUtils.formattedDate(transactionTime, "MMMM yyyy"));

            adapter.addItem(managementModel);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = ((BaseActivity) activity).dbHelper;
    }

   /* @Override
    public void onPause() {
        ((MainActivity) getActivity()).setTopBarTitle(Constants.CASH_MANAGEMENT);
        super.onPause();
    }*/

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        long transactionId = adapter.getItem(i).getId();
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.TRANSACTION_ID, transactionId);
        bundle.putBoolean("tag", true);

        AddTransaction addTransaction = new AddTransaction();
        addTransaction.setArguments(bundle);
        ((MainActivity) getActivity()).onFragmentAdd(addTransaction, Constants.EDIT_CASH);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floatingButton:
                AddTransaction transaction = new AddTransaction();
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.ADD_CASH, true);
                transaction.setArguments(bundle);
                ((MainActivity) getActivity()).onFragmentAdd(transaction, Constants.ADD_CASH);
                break;
            default:
                break;
        }
    }
}

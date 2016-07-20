package com.faayda.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.faayda.BaseActivity;
import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.adapter.AccountAdapter;
import com.faayda.customviews.CustomListView;
import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.models.AccountModel;
import com.faayda.preferences.Preferences;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;

/**
 * Created by vinove on 8/13/2015.
 */
public final class Account extends BaseFragment implements AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener, View.OnTouchListener {

    DBHelper dbHelper;
    CheckBox chk_show_debits;
    ImageView iv_add_transaction;
    CustomListView accountList, creditCardList;
    TextView tv_account_amount, tv_acc_date;
    AccountAdapter adapter, creditCardAdapter;
    LinearLayout llcreditCard, instructingView;
    Preferences pref;
    private String bank_type[] = {"Bank Account", "Credit Card"};
    private ListView list;
    private float x1, x2;
    static final int MIN_DISTANCE = 150;
    private String swipe = "account";
    private String showDebitCard = "showDebitCard";

    Boolean r = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ((MainActivity) getActivity()).setTopBarTitle(Constants.ACCOUNTS);
        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
        ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);

        View layoutView = inflater.inflate(R.layout.accounts, container, false);
        llcreditCard = (LinearLayout) layoutView.findViewById(R.id.llcreditCard);
        accountList = (CustomListView) layoutView.findViewById(R.id.lv_accountinfo);
        creditCardList = (CustomListView) layoutView.findViewById(R.id.lvCreditCards);
        tv_account_amount = (TextView) layoutView.findViewById(R.id.tv_account_amount);
        tv_acc_date = (TextView) layoutView.findViewById(R.id.tv_acc_date);
        chk_show_debits = (CheckBox) layoutView.findViewById(R.id.chk_show_debits);
        iv_add_transaction = (ImageView) layoutView.findViewById(R.id.iv_add_transaction);
        instructingView = (LinearLayout) layoutView.findViewById(R.id.instructingView);
        pref = new Preferences(getActivity());
        iv_add_transaction.setOnClickListener(this);
        creditCardAdapter = new AccountAdapter(getActivity(), dbHelper);

        adapter = new AccountAdapter(getActivity(), dbHelper);
        accountList.setAdapter(adapter);
        creditCardList.setAdapter(creditCardAdapter);
        instructingView.setOnTouchListener(this);
        accountList.setOnItemClickListener(this);
        creditCardList.setOnItemClickListener(this);

        // TODO : ACCOUNT TYPE DIALOG
       /* final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.account_type);
        list = (ListView) dialog.findViewById(R.id.lv_account_type);
        list.setAdapter(new AccountListviewAdapter(getActivity(), bank_type));
        dialog.show();*/
        long netBalance = 0;
        AccountModel model;
        long accountBalance;
        String latestDate = "";
        if (pref.getBoolean(swipe) == true) {
            instructingView.setVisibility(View.GONE);
        }
        String[] bankAccountType = dbHelper.getValues(false, DBConstants.ACCOUNT_TYPE, DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.PERSONAL + "' OR " + DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.BUSINESS + "'", null);
        String normalTypes = CommonUtils.getStringFromArray(bankAccountType);


        Cursor accounts = dbHelper.getTableRecords(DBConstants.USER_ACCOUNTS, null, DBConstants.ACCOUNT_TYPE_ID + " IN (" + normalTypes + ")", null);

        if (accounts.moveToFirst()) {
            do {
                accountBalance = Math.round(accounts.getDouble(accounts.getColumnIndex(DBConstants.ACCOUNT_BALANCE)));
                String accountId = accounts.getInt(accounts.getColumnIndex(DBConstants.ID)) + "";
                model = new AccountModel();
                if (accountBalance <= 0)
                    model.setAmount("N/A");

                else
                    model.setAmount(String.valueOf(accountBalance));

                // set debit cards ids

                if (dbHelper.getTableRecordsCount(DBConstants.DEBIT_CARDS,
                        null, DBConstants.LINKED_ACCOUNT_ID + " = '" + accountId + "'", null) > 0) {
                    model.setDebitCardNo((dbHelper.getValue(DBConstants.DEBIT_CARDS, DBConstants.CARD_NUMBER, DBConstants.LINKED_ACCOUNT_ID + " = " + accountId)));
                    model.setStatus(Long.parseLong((dbHelper.getValue(DBConstants.DEBIT_CARDS, DBConstants.LINKED_ACCOUNT_STATUS, DBConstants.LINKED_ACCOUNT_ID + " = "
                            + accountId))));


                } else {


                    Cursor debitCardCursor = dbHelper.getTableRecords(DBConstants.DEBIT_CARDS, null, DBConstants.LINKED_ACCOUNT_STATUS + " ='0' AND " + DBConstants.BANK_ID + " = "
                            + accounts.getLong(accounts.getColumnIndex(DBConstants.ACCOUNT_BANK_ID)), null);
                    if (debitCardCursor != null)
                        if (debitCardCursor.moveToFirst()) {

                            model.setDebitCardNo(debitCardCursor.getLong(debitCardCursor.getColumnIndex(DBConstants.CARD_NUMBER)) + "");
                            model.setStatus(debitCardCursor.getLong(debitCardCursor.getColumnIndex(DBConstants.LINKED_ACCOUNT_STATUS)));
                        }

                }


                //
/*
                if ((dbHelper.getValue(DBConstants.DEBIT_CARDS, DBConstants.CARD_NUMBER, DBConstants.BANK_ID + " = " + accountId)) != null)
                    model.setDebitCardNo((dbHelper.getValue(DBConstants.DEBIT_CARDS, DBConstants.CARD_NUMBER, DBConstants.BANK_ID + " = " + accountId)));
                else model.setDebitCardNo("");*/


                model.setAccountNo(accounts.getString(accounts.getColumnIndex(DBConstants.ACCOUNT_NUMBER)));
                model.setBankName(CommonUtils.getBankName(dbHelper, accounts.getLong(accounts.getColumnIndex(DBConstants.ACCOUNT_BANK_ID))));
                /*  Changes */
                String sqlQuery = "SELECT * FROM " + DBConstants.SMS + " WHERE " + DBConstants.ID + "='" + accounts.getLong(accounts.getColumnIndex(DBConstants.SMS_ID)) + "'";
                Cursor cursor = dbHelper.getDataThroughRaw(sqlQuery);
                if (cursor != null)
                    if (cursor.moveToFirst())
                        model.setDate(CommonUtils.formattedDate(cursor.getLong(cursor.getColumnIndex(DBConstants.SMS_DATE))));
                    else
                        model.setDate(CommonUtils.formattedDate(accounts.getLong(accounts.getColumnIndex(DBConstants.DATE_MODIFIED))));

                model.setId(accounts.getInt(accounts.getColumnIndex(DBConstants.ID)));
                model.setMerchantImage(CommonUtils.getMerchantImage(model.getBankName()));
                adapter.addItem(model);


                netBalance += accountBalance;
                latestDate = model.getDate();
            } while (accounts.moveToNext());
        }
        adapter.setShowDebitCards(pref.getBoolean(showDebitCard));
        chk_show_debits.setChecked(pref.getBoolean(showDebitCard));
        tv_account_amount.setText(CommonUtils.DoubleToStringLimits(Double.parseDouble(String.valueOf(netBalance))));
        tv_acc_date.setText(latestDate);

        //Credit Card Section
        String[] creditCardAccountType = dbHelper.getValues(false, DBConstants.ACCOUNT_TYPE, DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "' OR " + DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD_BUSINESS + "'", null);
        String creditCardTypes = CommonUtils.getStringFromArray(creditCardAccountType);
        Cursor creditCards = dbHelper.getTableRecords(DBConstants.USER_ACCOUNTS, null, DBConstants.ACCOUNT_TYPE_ID + " IN (" + creditCardTypes + ")", null);
        if (creditCards.getCount() > 0) {
            llcreditCard.setVisibility(View.VISIBLE);

            if (creditCards.moveToFirst()) {
                creditCardAdapter.isSCreditType = true;

                do {

      /*              long creditCardId = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE, DBConstants.ID,
                            DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "'", null, null));

                    accounts = dbHelper.getTableRecords(DBConstants.USER_ACCOUNTS, null, DBConstants.ACCOUNT_TYPE_ID + "  like '" + creditCardId + "'", null);
                    if (accounts.moveToFirst()) {*/
                    try {
                        accountBalance = Math.round(creditCards.getDouble(creditCards.getColumnIndex(DBConstants.ACCOUNT_BALANCE)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        accountBalance = 0;
                    }
                    model = new AccountModel();
                    model.setId(creditCards.getLong(creditCards.getColumnIndex(DBConstants.ID)));
                    model.setAmount(String.valueOf(accountBalance));
                    model.setAccountNo(creditCards.getString(creditCards.getColumnIndex(DBConstants.ACCOUNT_NUMBER)));
                    model.setBankName(CommonUtils.getBankName(dbHelper, creditCards.getLong(creditCards.getColumnIndex(DBConstants.ACCOUNT_BANK_ID))));
                    // model.setDate(CommonUtils.formattedDate(creditCards.getLong(creditCards.getColumnIndex(DBConstants.DATE_MODIFIED))));

                    String sqlQuery = "SELECT * FROM " + DBConstants.SMS + " WHERE " + DBConstants.ID + "='" + creditCards.getLong(creditCards.getColumnIndex(DBConstants.SMS_ID)) + "'";
                    Cursor cursor = dbHelper.getDataThroughRaw(sqlQuery);
                    if (cursor != null)
                        if (cursor.moveToFirst())
                            model.setDate(CommonUtils.formattedDate(cursor.getLong(cursor.getColumnIndex(DBConstants.SMS_DATE))));
                        else
                            model.setDate(CommonUtils.formattedDate(creditCards.getLong(creditCards.getColumnIndex(DBConstants.DATE_MODIFIED))));

                    model.setMerchantImage(CommonUtils.getMerchantImage(model.getBankName()));
                    model.setOutStandingAmount(creditCards.getLong(creditCards.getColumnIndex(DBConstants.OUTSTANDING_AMOUNT)));

                    creditCardAdapter.addItem(model);
                    //  }
                } while (creditCards.moveToNext());
            }
        }
        chk_show_debits.setVisibility(View.GONE);
        if (dbHelper.getTableRecordsCount(DBConstants.DEBIT_CARDS, null, null, null) > 0)
            chk_show_debits.setVisibility(View.VISIBLE);

        chk_show_debits.setOnCheckedChangeListener(this);

        return layoutView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = ((BaseActivity) activity).dbHelper;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.lvCreditCards) {
            AccountModel model = creditCardAdapter.getItem(position);
            AddAccount fragment = new AddAccount();
            Bundle arguments = new Bundle();
            arguments.putLong(Constants.ACCOUNT_ID, model.getId());
            fragment.setArguments(arguments);

            ((MainActivity) getActivity()).onFragmentAdd(fragment, Constants.EDIT_ACCOUNT);

        } else {
            AccountModel model = adapter.getItem(position);
            AddAccount fragment = new AddAccount();
            Bundle arguments = new Bundle();
            arguments.putLong(Constants.ACCOUNT_ID, model.getId());
            fragment.setArguments(arguments);

            ((MainActivity) getActivity()).onFragmentAdd(fragment, Constants.EDIT_ACCOUNT);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        pref.setBoolean(showDebitCard, b);
        adapter.setShowDebitCards(b);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_transaction:
                ((MainActivity) getActivity()).onFragmentAdd(new AddAccount(), Constants.ADD_ACCOUNT);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                x1 = motionEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = motionEvent.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    instructingView.setVisibility(View.GONE);
                    pref.setBoolean(swipe, true);
                }
                break;
        }
        return true;
    }
}
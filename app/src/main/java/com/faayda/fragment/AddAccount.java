package com.faayda.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.faayda.BaseActivity;
import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.adapter.AccountListviewAdapter;
import com.faayda.adapter.BankListAdapter;
import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.imageloader.Loader;
import com.faayda.models.BankAdapterModel;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;

import java.util.Arrays;

public final class AddAccount extends BaseFragment implements OnClickListener, AdapterView.OnItemSelectedListener {
    Button btnSaveCardInfo;
    EditText etAccNo, etNickName, etAccBalance;
    TextView btnPersonal, btnBusiness;
    String accountNo, nickName, accountBalance;
    DBHelper dbHelper;
    BankListAdapter bankSpinner;
    Spinner sp_select_bank;
    ImageView img_bnk_img;
    long accountType = 1;
    long accountId;
    boolean editAccount;
    boolean populateData = false;
    private String bank_type[] = {"Bank Account", "Credit Card"};
    long selectedBankId = 0;
    boolean isCreditCardSelected = false;
    TextView delteAccountTV;
    Loader loading;
    TextView txt_accountType, txt_number, txt_balance, txt_outstandingAmnt;
    EditText et_outsatndingAmnt;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.add_account, container, false);
        ((MainActivity) getActivity()).setTopBarTitle(Constants.ADD_ACCOUNT);
        ((MainActivity) getActivity()).invalidateTopBar(Constants.TITLE_BAR.SAVE_BUTTON);
        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.VISIBLE);

        loading = new Loader(getActivity());

        txt_accountType = (TextView) layoutView.findViewById(R.id.txt_accountType);
        txt_accountType.setText(R.string.bnkacc);

        txt_number = (TextView) layoutView.findViewById(R.id.txt_number);
        txt_number.setText(R.string.accno);

        txt_balance = (TextView) layoutView.findViewById(R.id.txt_balance);
        txt_balance.setText(R.string.accbalance);

        txt_outstandingAmnt = (TextView) layoutView.findViewById(R.id.txt_outstandingAmnt);
        txt_outstandingAmnt.setText(R.string.outstanding_amnt);
        txt_outstandingAmnt.setVisibility(View.GONE);

        et_outsatndingAmnt = (EditText) layoutView.findViewById(R.id.et_outsatndingAmnt);
        et_outsatndingAmnt.setVisibility(View.GONE);


        delteAccountTV = (TextView) layoutView.findViewById(R.id.deleteAccount);
        btnPersonal = (TextView) layoutView.findViewById(R.id.bt_personal);
        btnBusiness = (TextView) layoutView.findViewById(R.id.bt_business);
        btnSaveCardInfo = (Button) layoutView.findViewById(R.id.bt_save_card);
        etAccNo = (EditText) layoutView.findViewById(R.id.et_accno);
        etNickName = (EditText) layoutView.findViewById(R.id.et_nickname);
        etAccBalance = (EditText) layoutView.findViewById(R.id.et_accbalance);
        sp_select_bank = (Spinner) layoutView.findViewById(R.id.sp_select_bank);
        img_bnk_img = (ImageView) layoutView.findViewById(R.id.img_bnk_img);

        bankSpinner = new BankListAdapter(getActivity());
        sp_select_bank.setAdapter(bankSpinner);

        Cursor cursor = dbHelper.getTableRecords(DBConstants.BANKS, null, null, null);
        BankAdapterModel model;
        while (cursor.moveToNext()) {
            model = new BankAdapterModel();
            model.setBankName(cursor.getString(cursor.getColumnIndex(DBConstants.BANK_NAME)));
            model.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.ID)));
            bankSpinner.addItem(model);
        }
        btnBusiness.setOnClickListener(this);
        btnPersonal.setOnClickListener(this);
        sp_select_bank.setOnItemSelectedListener(this);
        delteAccountTV.setOnClickListener(this);
        return layoutView;
    }

    private void populateData(long accountId) {
        //Use account id to populate data


        Cursor cursor = dbHelper.getTableRecords(DBConstants.USER_ACCOUNTS, null, DBConstants.ID + " = " + accountId, null);
        if (cursor.moveToFirst()) {
            etAccNo.setText(cursor.getString(cursor.getColumnIndex(DBConstants.ACCOUNT_NUMBER)));
            etNickName.setText(cursor.getString(cursor.getColumnIndex(DBConstants.ACCOUNT_NICK_NAME)));
            etAccBalance.setText(CommonUtils.DoubleToStringLimits(cursor.getDouble(cursor.getColumnIndex(DBConstants.ACCOUNT_BALANCE))).replace(",", ""));

            if (isCreditCardSelected) {
                et_outsatndingAmnt.setText(CommonUtils.DoubleToStringLimits(cursor.getDouble
                        (cursor.getColumnIndex(DBConstants.OUTSTANDING_AMOUNT))).replace(",", ""));
            }
            long bankId = cursor.getLong(cursor.getColumnIndex(DBConstants.ACCOUNT_BANK_ID));


            for (int i = 0; i < bankSpinner.getCount(); i++) {
                if (bankSpinner.getItem(i).getId() == bankId) {
                    sp_select_bank.setSelection(i);
                    break;
                }
            }

            accountType = cursor.getInt(cursor.getColumnIndex(DBConstants.ACCOUNT_TYPE_ID));
            setAccountType(accountType);


            etAccNo.setEnabled(false);


            if (cursor.getLong(cursor.getColumnIndex(DBConstants.ACCOUNT_CREATED_BY)) == 2)
                delteAccountTV.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).ibBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                ((MainActivity) getActivity()).setTopBarTitle(Constants.ACCOUNTS);
                ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
                ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);

            }
        });
        getActivity().findViewById(R.id.saveButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    //TODO: Insert in database
                    saveCardInfo();
                }
            }
        });

        if (getArguments() != null) {
            accountId = getArguments().getLong(Constants.ACCOUNT_ID);
            if (accountId > 0) {
                editAccount = true;

                try {
                    long accountTypeId = Long.parseLong(dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ACCOUNT_TYPE_ID, DBConstants.ID + " = " + accountId));

                    long creditCardId_personal = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                            DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "'"));

                    long creditCardId_business = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                            DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD_BUSINESS + "'"));

                    if (accountTypeId == creditCardId_personal || accountTypeId == creditCardId_business) {
                        isCreditCardSelected = true;
                        txt_accountType.setText(R.string.what_you_owe);
                        txt_number.setText(R.string.card_no);
                        txt_balance.setText(R.string.tot_credit_limit);
                        txt_outstandingAmnt.setVisibility(View.VISIBLE);
                        et_outsatndingAmnt.setVisibility(View.VISIBLE);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                populateData(accountId);
                ((MainActivity) getActivity()).setTopBarTitle(Constants.EDIT_ACCOUNT);
                sp_select_bank.setEnabled(false);


            }
        } else {


            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.account_type);
            dialog.setCancelable(false);
            ListView list = (ListView) dialog.findViewById(R.id.lv_account_type);
            list.setAdapter(new AccountListviewAdapter(getActivity(), bank_type));
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 0)
                        isCreditCardSelected = false;
                    else isCreditCardSelected = true;
                    dialog.dismiss();

                    if (isCreditCardSelected) {

                        txt_accountType.setText(R.string.what_you_owe);
                        txt_number.setText(R.string.card_no);
                        txt_balance.setText(R.string.tot_credit_limit);
                        txt_outstandingAmnt.setVisibility(View.VISIBLE);
                        et_outsatndingAmnt.setVisibility(View.VISIBLE);


                        long personalCreditId = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                                DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "'"));
                        long businessCreditId = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                                DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD_BUSINESS + "'"));

                        String banksId[] = dbHelper.getValues(false, DBConstants.USER_ACCOUNTS,
                                DBConstants.ACCOUNT_BANK_ID, DBConstants.ACCOUNT_TYPE_ID + " = " + personalCreditId + " OR " +
                                        DBConstants.ACCOUNT_TYPE_ID + " = " + businessCreditId, null);
                        accountType = personalCreditId;
                        bankSpinner = new BankListAdapter(getActivity());
                        sp_select_bank.setAdapter(bankSpinner);

                        Cursor cursor = dbHelper.getTableRecords(DBConstants.BANKS, null, null, null);
                        BankAdapterModel model;
                        while (cursor.moveToNext()) {
                            model = new BankAdapterModel();
                            model.setBankName(cursor.getString(cursor.getColumnIndex(DBConstants.BANK_NAME)));

                            model.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.ID)));
                            if (!Arrays.asList(banksId).contains(String.valueOf(cursor.getLong(cursor.getColumnIndex(DBConstants.ID)))))
                                bankSpinner.addItem(model);
                        }

                    } else {

                        txt_accountType.setText(R.string.bnkacc);
                        txt_number.setText(R.string.accno);
                        txt_balance.setText(R.string.accbalance);
                        txt_outstandingAmnt.setVisibility(View.GONE);
                        et_outsatndingAmnt.setVisibility(View.GONE);

                        long personalDebitId = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                                DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.PERSONAL + "'"));
                        long businessDebitId = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                                DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.BUSINESS + "'"));
                        accountType = personalDebitId;

                        String banksId[] = dbHelper.getValues(false, DBConstants.USER_ACCOUNTS,
                                DBConstants.ACCOUNT_BANK_ID, DBConstants.ACCOUNT_TYPE_ID + " = " + personalDebitId + " OR " +
                                        DBConstants.ACCOUNT_TYPE_ID + " = " + businessDebitId, null);

                        bankSpinner = new BankListAdapter(getActivity());
                        sp_select_bank.setAdapter(bankSpinner);

                        Cursor cursor = dbHelper.getTableRecords(DBConstants.BANKS, null, null, null);
                        BankAdapterModel model;
                        while (cursor.moveToNext()) {
                            model = new BankAdapterModel();
                            model.setBankName(cursor.getString(cursor.getColumnIndex(DBConstants.BANK_NAME)));

                            model.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.ID)));
                            if (!Arrays.asList(banksId).contains(String.valueOf(cursor.getLong(cursor.getColumnIndex(DBConstants.ID)))))
                                bankSpinner.addItem(model);
                        }

                    }


                }
            });
            dialog.show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save_card:
                saveCardInfo();
                break;
            case R.id.bt_personal:
                btnPersonal.setBackgroundColor(getResources().getColor(R.color.login_background));
                btnBusiness.setBackgroundColor(getResources().getColor(R.color.unselected));
                if (isCreditCardSelected)
                    accountType = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                            DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "'"));
                else accountType = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                        DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.PERSONAL + "'"));
                break;
            case R.id.bt_business:
                btnPersonal.setBackgroundColor(getResources().getColor(R.color.unselected));
                btnBusiness.setBackgroundColor(getResources().getColor(R.color.share_with));
                if (isCreditCardSelected)
                    accountType = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                            DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD_BUSINESS + "'"));
                else accountType = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                        DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.BUSINESS + "'"));
                break;
            case R.id.deleteAccount:


                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getActivity())
                        //set message, title, and icon
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to Delete ?")
                        .setIcon(R.drawable.delete)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code

                                try {
                            /*        String sqlQuery = "DELETE " + DBConstants.USER_ACCOUNTS + " , " + DBConstants.TRANSACTIONS + "  FROM " + DBConstants.USER_ACCOUNTS +
                                            "  INNER JOIN " + DBConstants.TRANSACTIONS +
                                            "   WHERE " + DBConstants.USER_ACCOUNTS + "." + DBConstants.ID + " = " + DBConstants.TRANSACTIONS + "." + DBConstants.TRANSACTION_ACCOUNT_ID +
                                            " and " + DBConstants.USER_ACCOUNTS + "." + DBConstants.ID + " = '" + accountId + "'";
                                    */

                  /*                          String sqlQuery = "DELETE  FROM " + DBConstants.USER_ACCOUNTS +" , "+DBConstants.TRANSACTIONS+" USING "+
                                            "  INNER JOIN " + DBConstants.TRANSACTIONS +
                                            "   ON( " + DBConstants.USER_ACCOUNTS + "." + DBConstants.ID + " = " + DBConstants.TRANSACTIONS + "." + DBConstants.TRANSACTION_ACCOUNT_ID +
                                            " ) WHERE " + DBConstants.USER_ACCOUNTS + "." + DBConstants.ID + " = '" + accountId + "'";
*/


                                    //dbHelper.deleteRawQuery(sqlQuery);

                                    dbHelper.deleteRecords(DBConstants.USER_ACCOUNTS, DBConstants.ID + " = " + accountId, null);
                                    dbHelper.deleteRecords(DBConstants.TRANSACTIONS, DBConstants.TRANSACTION_ACCOUNT_ID + " = " + accountId, null);
                                    ((MainActivity) getActivity()).onFragmentAdd(new Account(), Constants.ACCOUNTS);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        })


                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        })
                        .create();

                myQuittingDialogBox.show();

                break;
            default:
                break;



/*
            DELETE messages , usersmessages  FROM messages  INNER JOIN usersmessages
            WHERE messages.messageid= usersmessages.messageid and messages.messageid = '1'
*/
        }
    }

    private void setAccountType(long type) {

        if (type == Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.PERSONAL + "'"))) {

            btnPersonal.setBackgroundColor(getResources().getColor(R.color.login_background));
            btnBusiness.setBackgroundColor(getResources().getColor(R.color.unselected));
            accountType = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                    DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.PERSONAL + "'"));

            return;
        }

        if (type == Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.BUSINESS + "'"))) {
            btnBusiness.setBackgroundColor(getResources().getColor(R.color.share_with));
            btnPersonal.setBackgroundColor(getResources().getColor(R.color.unselected));
            accountType = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                    DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.BUSINESS + "'"));
            return;

        }

        if (type == Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "'"))) {

            isCreditCardSelected = true;

            btnPersonal.setBackgroundColor(getResources().getColor(R.color.login_background));
            btnBusiness.setBackgroundColor(getResources().getColor(R.color.unselected));
            accountType = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                    DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "'"));

            return;
        }

        if (type == Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD_BUSINESS + "'"))) {

            isCreditCardSelected = true;
            btnBusiness.setBackgroundColor(getResources().getColor(R.color.share_with));
            btnPersonal.setBackgroundColor(getResources().getColor(R.color.unselected));
            accountType = Long.parseLong(dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                    DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD_BUSINESS + "'"));

            return;

        }
    }

    private void saveCardInfo() {
        hideSoftKeyboard(getActivity());
        accountNo = etAccNo.getText().toString();
        nickName = etNickName.getText().toString();
        accountBalance = etAccBalance.getText().toString();

        ContentValues values = new ContentValues();
        values.put(DBConstants.ACCOUNT_BALANCE, accountBalance);
        values.put(DBConstants.ACCOUNT_NICK_NAME, nickName);
        values.put(DBConstants.ACCOUNT_NUMBER, accountNo);
        values.put(DBConstants.ACCOUNT_BANK_ID, selectedBankId);
        values.put(DBConstants.ACCOUNT_DATE, System.currentTimeMillis());
        values.put(DBConstants.DATE_MODIFIED, System.currentTimeMillis());
        values.put(DBConstants.ACCOUNT_TYPE_ID, accountType);

        if (!et_outsatndingAmnt.getText().toString().isEmpty() && isCreditCardSelected)
            values.put(DBConstants.OUTSTANDING_AMOUNT, et_outsatndingAmnt.getText().toString());
        if (editAccount) {
            dbHelper.updateRecords(DBConstants.USER_ACCOUNTS, values, DBConstants.ID + " = " + accountId, null);

        } else {
            values.put(DBConstants.ACCOUNT_CREATED_BY, 2);
            // 2 because it is created manually by user and can delete it
            if (isCreditCardSelected)
                values.put(DBConstants.ACCOUNT_TYPE_ID, dbHelper.getValue(DBConstants.ACCOUNT_TYPE,
                        DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "'"));


            dbHelper.insertContentVals(DBConstants.USER_ACCOUNTS, values);
        }
        ((MainActivity) getActivity()).onFragmentAdd(new Account(), Constants.ACCOUNTS);

        //getActivity().onBackPressed();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)

            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = ((BaseActivity) activity).dbHelper;
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(etAccNo.getText().toString().trim())) {
            if (isCreditCardSelected)
                Toast.makeText(getActivity(), "Please enter last 4 digits of your card number", Toast.LENGTH_LONG).show();

            else
                Toast.makeText(getActivity(), "Please enter last 4 digits of your account", Toast.LENGTH_LONG).show();
            etAccNo.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etNickName.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Please enter the nick name", Toast.LENGTH_SHORT).show();
            etNickName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etAccBalance.getText().toString().trim())) {
            if (isCreditCardSelected)
                Toast.makeText(getActivity(), "Please enter your total credit card limit", Toast.LENGTH_LONG).show();

            else Toast.makeText(getActivity(), "Please enter your account balance", Toast.LENGTH_LONG).show();
            etAccBalance.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        BankAdapterModel model = (BankAdapterModel) bankSpinner.getItem(i);

        selectedBankId = model.getId();
//        img_bnk_img.setImageResource(CommonUtils.getMerchantImage(model.getBankName()));


        try {
            if (CommonUtils.getMerchantImage(model.getBankName()) != -1)
                img_bnk_img.setImageResource(CommonUtils.getMerchantImage(model.getBankName()));
            else {


                String imageUrl = dbHelper.getValue(DBConstants.BANKS, DBConstants.BANK_IMAGE,
                        DBConstants.BANK_NAME + " = '" + model.getBankName() + "'");
                loading.displayImage(imageUrl, img_bnk_img, R.drawable.default_bank);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        BankAdapterModel model = (BankAdapterModel) bankSpinner.getItem(0);
        accountId = model.getId();
        img_bnk_img.setImageResource(CommonUtils.getMerchantImage(model.getBankName()));
    }
}

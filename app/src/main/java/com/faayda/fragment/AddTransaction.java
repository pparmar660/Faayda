package com.faayda.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.faayda.BaseActivity;
import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.adapter.AccountSpinnerAdapter;
import com.faayda.adapter.CategoriesGridAdapter;
import com.faayda.customviews.CustomGridView;
import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.imageloader.Loader;
import com.faayda.models.AccountsModel;
import com.faayda.models.CategoryGridModel;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vinove on 8/5/2015.
 */
public final class AddTransaction extends BaseFragment implements OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {


    boolean editTransaction, editCash;

    long acType = 1, acNoId = Constants.CASH_ACCOUNT_NO, transactionTypeID = 0, categoryId, billerId;
    String amount, transactionTo;
    DBHelper dbHelper;
    //    CategoriesGridAdapter adapter;
//    AddTransactionPopupAdapter noti_adapter;
    GridView gvCatagory;
    TextView date, tv_more_category, transactionTypeTv;
    ImageView date_image;
    ListView list;
    TextView deleteTransactionTV;
    RadioGroup radioGroup;
    int[] radioButtons;
    int accountType = 1;
    Spinner bank_name_spinner;
    private int year, month, day;
    private DatePickerDialog dpicker;
    Context context;
    EditText transactionAmount;
    RelativeLayout relative1, relative2, relative3;
    ImageView iv_add_transaction, iv_date_picker;
    AutoCompleteTextView billerName;
    CheckBox cb_cash_withdrawl;
    AccountSpinnerAdapter spinnerAdapter;
    ArrayAdapter<String> billerAdapter;
    RadioButton rb_debit, rb_credit, rb_refund;
    long selectedCategoryId = 0;
    CategoriesGridAdapter gridAdapter;
    long selectedDateInMillSecond = 0;
    boolean isAddingCash = false;
    Loader loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        context = getActivity().getBaseContext();
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setTopBarTitle(Constants.ADD_TRANSACTION);
        ((MainActivity) getActivity()).invalidateTopBar(Constants.TITLE_BAR.SAVE_BUTTON);
        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.VISIBLE);

        loading = new Loader(getActivity());

        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.ADD_CASH)) {
                isAddingCash = true;
                transactionTypeID = Integer.parseInt(dbHelper.getValue(DBConstants.TRANSACTION_TYPE,
                        DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.ATM_WDL + "'"));
                ((MainActivity) getActivity()).setTopBarTitle(Constants.ADD_CASH);
            }
        }

        View layoutView = inflater.inflate(R.layout.add_transaction, container, false);
        gvCatagory = (GridView) layoutView.findViewById(R.id.gv_category);
        date = (TextView) layoutView.findViewById(R.id.tv_date);
        deleteTransactionTV = (TextView) layoutView.findViewById(R.id.deleteTransaction);
        deleteTransactionTV.setOnClickListener(this);
        selectedDateInMillSecond = System.currentTimeMillis();
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        Date cal_date = new Date();
        String yourDate = dateFormat.format(cal_date);
        date.setText(yourDate.trim());

        //  date_image=(ImageView)layoutView.findViewById()
        transactionAmount = (EditText) layoutView.findViewById(R.id.transactionAmount);
        iv_add_transaction = (ImageView) layoutView.findViewById(R.id.iv_add_transaction);
        date_image = (ImageView) layoutView.findViewById(R.id.iv_date_picker);
        bank_name_spinner = (Spinner) layoutView.findViewById(R.id.sp_bankName);
        transactionTypeTv = (TextView) layoutView.findViewById(R.id.transcationTypeName);
        /*   tv_business = (TextView) layoutView.findViewById(R.id.tv_bussiness);
        tv_personal = (TextView) layoutView.findViewById(R.id.tv_personal);*/
        tv_more_category = (TextView) layoutView.findViewById(R.id.tv_more_category);
        tv_more_category.setOnClickListener(this);
        //

        String[] allBiller = dbHelper.getValues(false, DBConstants.BILLER_LIST, DBConstants.BILLER_NAME, null, null);
        billerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, allBiller);

        billerName = (AutoCompleteTextView) layoutView.findViewById(R.id.billerName);
        billerName.setAdapter(billerAdapter);

        radioButtons = new int[]{R.id.rb_debit, R.id.rb_credit, R.id.rb_refund};
        cb_cash_withdrawl = (CheckBox) layoutView.findViewById(R.id.cb_cash_withdrawl);
        rb_debit = (RadioButton) layoutView.findViewById(R.id.rb_debit);
        rb_credit = (RadioButton) layoutView.findViewById(R.id.rb_credit);
        rb_refund = (RadioButton) layoutView.findViewById(R.id.rb_refund);
        relative1 = (RelativeLayout) layoutView.findViewById(R.id.relative1);
        relative2 = (RelativeLayout) layoutView.findViewById(R.id.relative2);
        relative3 = (RelativeLayout) layoutView.findViewById(R.id.relative3);
     /*   tv_business.setOnClickListener(this);
        tv_personal.setOnClickListener(this);*/
        rb_debit.setOnClickListener(this);
        rb_credit.setOnClickListener(this);
        rb_refund.setOnClickListener(this);
       /* rb_debit.setButtonDrawable(R.drawable.white_radio_button_design);
        rb_credit.setButtonDrawable(R.drawable.white_radio_button_design);
        rb_refund.setButtonDrawable(R.drawable.white_radio_button_design);
        rb_debit.setCompoundDrawablesWithIntrinsicBounds(0, t, right, bottom)*/
        radioGroup = (RadioGroup) layoutView.findViewById(R.id.radio_group);

/*        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String transactionName = "";

                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_debit:
                        transactionName = Constants.DEBIT;
                        break;
                    case R.id.rb_credit:
                        transactionName = Constants.CREDIT;
                        break;
                    case R.id.rb_refund:
                        transactionName = Constants.REFUND;
                        break;
                    default:
                        break;
                }
                if (!transactionName.isEmpty())
                    transactionTypeID = Integer.parseInt(dbHelper.getValue(DBConstants.TRANSACTION_TYPE,
                            DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + transactionName + "'"));
            }
        });*/


        gridAdapter = new CategoriesGridAdapter(getActivity());
        gvCatagory.setAdapter(gridAdapter);
        gvCatagory.setSelected(true);
        gvCatagory.setDrawSelectorOnTop(true);
        gvCatagory.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        gvCatagory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // String categoryTitle = dbHelper.getValue(DBConstants.CATEGORIES, DBConstants.CATEGORY_TITLE, DBConstants.ID + " = " + position + 1);
                //System.out.println("SELECTED CATEGORY TITLE : " + categoryTitle + position);

                for (int i = 0; i < gridAdapter.categoryModelArrayList.size(); i++)
                    gridAdapter.categoryModelArrayList.get(i).setIsSelected(false);

                gridAdapter.categoryModelArrayList.get(position).setIsSelected(true);
                gridAdapter.notifyDataSetChanged();
                selectedCategoryId = Long.parseLong(gridAdapter.categoryModelArrayList.get(position).getCategoryId());
            }
        });


        Cursor cursor = dbHelper.getTableRecords(DBConstants.CATEGORIES, null, null, null);
        int i = 0;
        String categoryTitle;
        CategoryGridModel gridModel;
        while (cursor.moveToNext()) {
            if (i == 8) break;
            categoryTitle = cursor.getString(cursor.getColumnIndex(DBConstants.CATEGORY_TITLE));
            gridModel = new CategoryGridModel();
            gridModel.setCategoryTitle(categoryTitle);
            gridModel.setcategoryId(cursor.getString(cursor.getColumnIndex(DBConstants.ID)));


//            if (CommonUtils.getCategoryIcon(categoryTitle) == -1) {
            gridModel.setCategoryImage(CommonUtils.getCategoryIcon(categoryTitle));
//                gridModel.setCategoryImage(cursor.getString(cursor.getColumnIndex(DBConstants.CATEGORY_IMAGE)));
//                gridModel.setCategoryImage(CommonUtils.getCategoryIcon(categoryTitle));
//            } else {
//                gridModel.setCategoryImage(CommonUtils.getCategoryIcon(categoryTitle));
//            }


            gridAdapter.add(gridModel);
            i++;
        }

//        final Dialog dialog = new Dialog(getActivity());
        spinnerAdapter = new AccountSpinnerAdapter(getActivity());
        bank_name_spinner.setAdapter(spinnerAdapter);

        cursor = dbHelper.getTableRecords(DBConstants.USER_ACCOUNTS, null, null, null);


        String bankName, bankIdStr;
        long bankId, accountId;
        int bankImage;
        boolean cashItemAdded = false;
        AccountsModel accounts;

        while (cursor.moveToNext()) {
            if (!cashItemAdded && !isAddingCash) {
                accounts = new AccountsModel();
                accounts.setBankName("CASH");
                accounts.setAccountNo(Constants.CASH_ACCOUNT_NO + "");
                accounts.setAccountId(Constants.CASH_ACCOUNT_NO);
                accounts.setImage(CommonUtils.getMerchantImage(Constants.CASH));
                accounts.setBankId(0 + "");
                accounts.setBankTitle("CASH");
                accounts.setBankName(Constants.CASH);
                spinnerAdapter.add(accounts);
                cashItemAdded = true;
            }


            bankIdStr = cursor.getString(cursor.getColumnIndex(DBConstants.ACCOUNT_BANK_ID));
            accountId = cursor.getLong(cursor.getColumnIndex(DBConstants.ID));
            try {
                bankId = Long.parseLong(bankIdStr);
            } catch (Exception e) {
                bankId = 0;
                continue;
            }
            accounts = new AccountsModel();

            bankName = CommonUtils.getBankName(dbHelper, bankId);
            bankImage = CommonUtils.getMerchantImage(bankName);
            accounts.setAccountId(accountId);
            accounts.setAccountNo(cursor.getString(cursor.getColumnIndex(DBConstants.ACCOUNT_NUMBER)));
            accounts.setImage(bankImage);
            accounts.setBankId(bankId + "");
            accounts.setBankTitle(bankName);
            accounts.setBankName(bankName + " Account no - " + cursor.getString(cursor.getColumnIndex(DBConstants.ACCOUNT_NUMBER)));
            spinnerAdapter.add(accounts);
        }

        /*cursor = dbHelper.getTableRecords(DBConstants.BANKS, null, null, null);
        i=0;
        String bankName, bankId;
        int bankImage;
        BanksModel accounts;
        while(cursor.moveToNext()){
            accounts = new BanksModel();
            bankName = cursor.getString(cursor.getColumnIndex(DBConstants.BANK_NAME));
            bankId = cursor.getString(cursor.getColumnIndex(DBConstants.ID));
            bankImage = CommonUtils.getMerchantImage(bankName);

            accounts.setImage(bankImage);
            accounts.setBankId(bankId);
            accounts.setBankName(bankName);

            bankListAdapter.add(accounts);
        }*/

       /* // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_transaction_popup);
        list = (ListView) dialog.findViewById(R.id.lv_notification);
        list.setAdapter(new AddTransactionPopupAdapter(getActivity(), bank, bank_name, bank_bal));*/
        //set onclick
        date_image.setOnClickListener(this);
        bank_name_spinner.setOnItemSelectedListener(this);

        /*bank_name_spinner.setOnClickListener(this);*/
//        dialog.show();
        return layoutView;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void MoreCategoryDialog() {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        final Dialog dialog = new Dialog(getActivity());
        double hei = height * (0.8);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout((int) (width * (0.8)), (int) (height * (0.6)));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.more_categories);

        CustomGridView categories = (CustomGridView) dialog.findViewById(R.id.category);
        final CategoriesGridAdapter gridAdapter1 = new CategoriesGridAdapter(getActivity());

        Cursor cursor = dbHelper.getTableRecords(DBConstants.CATEGORIES, null, null, null);
        int i = 0;
        String categoryTitle;
        CategoryGridModel gridModel;
        while (cursor.moveToNext()) {
            categoryTitle = cursor.getString(cursor.getColumnIndex(DBConstants.CATEGORY_TITLE));
            gridModel = new CategoryGridModel();
            gridModel.setCategoryTitle(categoryTitle);
            gridModel.setcategoryId(cursor.getString(cursor.getColumnIndex(DBConstants.ID)));

/*            if (CommonUtils.getCategoryIcon(categoryTitle) == -1) {
                gridModel.setCategoryImage(cursor.getString(cursor.getColumnIndex(DBConstants.CATEGORY_IMAGE)));*/
            gridModel.setCategoryImage(CommonUtils.getCategoryIcon(categoryTitle));
            /*} else {
                gridModel.setCategoryImage(CommonUtils.getCategoryIcon(categoryTitle));
            }*/


            gridAdapter1.add(gridModel);
            i++;
        }

        categories.setAdapter(gridAdapter1);
        categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /*for (int i = 0; i < gridAdapter.categoryModelArrayList.size(); i++)
                    gridAdapter.categoryModelArrayList.get(i).setIsSelected(false);

                gridAdapter.categoryModelArrayList.get(position).setIsSelected(true);
                gridAdapter.notifyDataSetChanged();*/
                selectedCategoryId = Long.parseLong(gridAdapter1.categoryModelArrayList.get(position).getCategoryId());

                for (int i = 0; i < gridAdapter.categoryModelArrayList.size(); i++) {
                    if (Long.parseLong(gridAdapter.categoryModelArrayList.get(i).getCategoryId()) == selectedCategoryId)
                        gridAdapter.categoryModelArrayList.get(i).setIsSelected(true);
                    else gridAdapter.categoryModelArrayList.get(i).setIsSelected(false);
                }

                gridAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void populateData(long transactionId) {
        long acId;
        String transactionName;
        Cursor cursor = dbHelper.getTableRecords(DBConstants.TRANSACTIONS, null, DBConstants.ID + " = " + transactionId, null);
        while (cursor.moveToNext()) {
            acId = cursor.getLong(cursor.getColumnIndex(DBConstants.TRANSACTION_ACCOUNT_ID));

            for (int i = 0; i < spinnerAdapter.getCount(); i++) {
                if (acId == spinnerAdapter.getItem(i).getAccountId()) {
                    bank_name_spinner.setSelection(i);

                    break;
                }
            }

            transactionName = dbHelper.getValue(DBConstants.TRANSACTION_TYPE,
                    DBConstants.TRANSACTION_NAME, DBConstants.ID + " = " +
                            cursor.getString(cursor.getColumnIndex(DBConstants.TRANSACTION_TYPE_ID)));
            transactionTypeID = Long.parseLong(cursor.getString(cursor.getColumnIndex(DBConstants.TRANSACTION_TYPE_ID)));

            //pp start
            transactionTypeTv.setVisibility(View.VISIBLE);
            switch (transactionName) {

                case Constants.DEBIT:
                    rb_debit.setChecked(true);
                    rb_credit.setEnabled(false);
                    rb_refund.setEnabled(false);


                    relative1.setBackgroundColor(getResources().getColor(R.color.share_with));
                    rb_debit.setTextColor(getResources().getColor(R.color.white));
                    relative2.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_credit.setTextColor(getResources().getColor(R.color.black));
                    relative3.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_refund.setTextColor(getResources().getColor(R.color.black));

 /*                   transactionTypeID = Integer.parseInt(dbHelper.getValue(DBConstants.TRANSACTION_TYPE,
                            DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.DEBIT + "'"));*/

                    transactionTypeTv.setText("Debited from account");
                    break;
                case Constants.CREDIT:
                    rb_debit.setEnabled(false);
                    rb_credit.setChecked(true);
                    rb_refund.setEnabled(false);
                    relative1.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_debit.setTextColor(getResources().getColor(R.color.black));
                    relative2.setBackgroundColor(getResources().getColor(R.color.share_with));
                    rb_credit.setTextColor(getResources().getColor(R.color.white));
                    relative3.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_refund.setTextColor(getResources().getColor(R.color.black));
                  /*  transactionTypeID = Integer.parseInt(dbHelper.getValue(DBConstants.TRANSACTION_TYPE,
                            DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.CREDIT + "'"));*/
                    transactionTypeTv.setText("Credited to account");
                    break;
                case Constants.REFUND:
                    rb_debit.setEnabled(false);
                    rb_credit.setEnabled(false);
                    rb_refund.setChecked(true);
                    relative1.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_debit.setTextColor(getResources().getColor(R.color.black));
                    relative2.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_credit.setTextColor(getResources().getColor(R.color.black));
                    relative3.setBackgroundColor(getResources().getColor(R.color.share_with));
                    rb_refund.setTextColor(getResources().getColor(R.color.white));
                   /* transactionTypeID = Integer.parseInt(dbHelper.getValue(DBConstants.TRANSACTION_TYPE,
                            DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.REFUND + "'"));*/
                    transactionTypeTv.setText("Refunded");
                    break;
                case Constants.ATM_WDL:
                    rb_debit.setChecked(true);
                    rb_credit.setEnabled(false);
                    rb_refund.setEnabled(false);
                    transactionTypeTv.setText("Cash withdrawal");
                    relative1.setBackgroundColor(getResources().getColor(R.color.share_with));
                    rb_debit.setTextColor(getResources().getColor(R.color.white));
                    relative2.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_credit.setTextColor(getResources().getColor(R.color.black));
                    relative3.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_refund.setTextColor(getResources().getColor(R.color.black));
                    break;

                case Constants.EXPENSE:
                case Constants.CASH_EXPENSE:
                    rb_debit.setChecked(true);
                    rb_credit.setEnabled(false);
                    rb_refund.setEnabled(false);
                    transactionTypeTv.setText("Expense");
                    relative1.setBackgroundColor(getResources().getColor(R.color.share_with));
                    rb_debit.setTextColor(getResources().getColor(R.color.white));
                    relative2.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_credit.setTextColor(getResources().getColor(R.color.black));
                    relative3.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_refund.setTextColor(getResources().getColor(R.color.black));
                    break;
                case Constants.FUND_TRANSFER:
                    rb_debit.setChecked(true);
                    rb_credit.setEnabled(false);
                    rb_refund.setEnabled(false);
                    transactionTypeTv.setText("Amount transferred");
                    relative1.setBackgroundColor(getResources().getColor(R.color.share_with));
                    rb_debit.setTextColor(getResources().getColor(R.color.white));
                    relative2.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_credit.setTextColor(getResources().getColor(R.color.black));
                    relative3.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_refund.setTextColor(getResources().getColor(R.color.black));
                    break;
                case Constants.CREDIT_CARD_EXPENSE:
                    rb_debit.setChecked(true);
                    rb_credit.setEnabled(false);
                    rb_refund.setEnabled(false);
                    transactionTypeTv.setText("Credit card expense");
                    relative1.setBackgroundColor(getResources().getColor(R.color.share_with));
                    rb_debit.setTextColor(getResources().getColor(R.color.white));
                    relative2.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_credit.setTextColor(getResources().getColor(R.color.black));
                    relative3.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_refund.setTextColor(getResources().getColor(R.color.black));
                    break;
                case Constants.DEBIT_CARD_EXPENSE:
                    rb_debit.setChecked(true);
                    rb_credit.setEnabled(false);
                    rb_refund.setEnabled(false);
                    transactionTypeTv.setText("Debit card expense");
                    relative1.setBackgroundColor(getResources().getColor(R.color.share_with));
                    rb_debit.setTextColor(getResources().getColor(R.color.white));
                    relative2.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_credit.setTextColor(getResources().getColor(R.color.black));
                    relative3.setBackgroundColor(getResources().getColor(R.color.gray_add));
                    rb_refund.setTextColor(getResources().getColor(R.color.black));
                    break;

            }


            //pp stop


            transactionAmount.setText(cursor.getString(cursor.getColumnIndex(DBConstants.TRANSACTION_AMOUNT)));
            billerId = cursor.getLong(cursor.getColumnIndex(DBConstants.TRANSACTION_BILLER_ID));
            billerName.setText(cursor.getString(cursor.getColumnIndex(DBConstants.BILLER_NAME)));

            selectedCategoryId = cursor.getLong(cursor.getColumnIndex(DBConstants.CATEGORY_ID));

            date.setText(CommonUtils.getDateFromMillis
                    (Long.parseLong(cursor.getString(cursor.getColumnIndex(DBConstants.TRANSACTION_DATE))), "dd MMM yyyy"));


            for (int i = 0; i < gridAdapter.categoryModelArrayList.size(); i++) {
                if (Long.parseLong(gridAdapter.categoryModelArrayList.get(i).getCategoryId()) == selectedCategoryId)
                    gridAdapter.categoryModelArrayList.get(i).setIsSelected(true);
                else gridAdapter.categoryModelArrayList.get(i).setIsSelected(false);
            }

            gridAdapter.notifyDataSetChanged();
            checkDebitCard(cursor);

        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = ((BaseActivity) activity).dbHelper;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deleteTransactionTV.setVisibility(View.GONE);
        ((MainActivity) getActivity()).ibBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                if (isAddingCash) {
                    ((MainActivity) getActivity()).setTopBarTitle(Constants.CASH_MANAGEMENT);
                    ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
                    ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);
                }
                if (getArguments() != null) {
                    if (getArguments().getBoolean("tag")) {
                        ((MainActivity) getActivity()).setTopBarTitle(Constants.CASH_MANAGEMENT);
                        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
                        ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);
                    } else {
                        ((MainActivity) getActivity()).setTopBarTitle(Constants.DASHBOARD);
                        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
                        ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);
                    }
                } else {
                    ((MainActivity) getActivity()).setTopBarTitle(Constants.DASHBOARD);
                    ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
                    ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);
                }

            }
        });
        getActivity().findViewById(R.id.saveButton).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO: Validate Form and submit

                        if (checkValidation()) {
                            if (transactionAmount == null) {
                                Toast.makeText(getActivity(), "Please enter the transaction Amount", Toast.LENGTH_SHORT).show();
                            } else {
                                saveTransaction();
                                ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
                                ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);
                                if (isAddingCash) {
                                    ((MainActivity) getActivity()).clearFragmentStack();
                                    ((MainActivity) getActivity()).onFragmentAdd(new CashManagment(), Constants.CASH_MANAGEMENT);
                                }
                                else {

                                    ((MainActivity) getActivity()).clearFragmentStack();
                                    ((MainActivity) getActivity()).onFragmentAdd(new DashBoardFragment(), Constants.DASHBOARD);
                                }
                            }
                        }
                    }
                });


        if (getArguments() != null) {

            if (getArguments().containsKey(Constants.TRANSACTION_ID)) {

                if (getArguments().getBoolean("tag")) {
                    ((MainActivity) getActivity()).setTopBarTitle(Constants.EDIT_CASH);
                } else {
                    ((MainActivity) getActivity()).setTopBarTitle(Constants.EDIT_TRANSACTION);
                }

                editTransaction = true;
                deleteTransactionTV.setVisibility(View.VISIBLE);
                populateData(getArguments().getLong(Constants.TRANSACTION_ID));
                date_image.setVisibility(View.GONE);
                bank_name_spinner.setEnabled(true);
//                ((MainActivity) getActivity()).setTopBarTitle(Constants.EDIT_TRANSACTION);
                bank_name_spinner.setEnabled(chkCanEditAccountNumber(getArguments().getLong(Constants.TRANSACTION_ID)));
                transactionAmount.setEnabled(false);


            } else if (getArguments().containsKey(Constants.ADD_CASH)) {
                radioGroup.setVisibility(View.GONE);
                acNoId = Constants.CASH_ACCOUNT_NO;
                acType = Constants.CASH_ACCOUNT_NO;
                cb_cash_withdrawl.setVisibility(View.GONE);
                // transactionTypeID = Integer.parseInt(dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.CASH_EXPENSE + "'"));
            }
        }
    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(transactionAmount.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Please enter the transaction Amount", Toast.LENGTH_LONG).show();
            transactionAmount.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(billerName.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Please enter the transaction to", Toast.LENGTH_SHORT).show();
            billerName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(date.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Please select date", Toast.LENGTH_LONG).show();
            date.requestFocus();
            return false;
        } else if (transactionTypeID == 0 && !isAddingCash) {
            Toast.makeText(getActivity(), "Please select Transaction type", Toast.LENGTH_LONG).show();

            return false;

        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_date_picker:
                dpicker = new DatePickerDialog(getActivity(), pickerListener, year, month, day);
                DateFormat curFormater2 = new SimpleDateFormat("dd MMM yyyy");
                Calendar c = Calendar.getInstance();
                selectedDateInMillSecond = c.getTimeInMillis();

                String output = curFormater2.format(c.getTime());

                date.setText(output);
                dpicker.show();
                break;
    /*        case R.id.tv_bussiness:
                acType = 2;
                tv_business.setBackgroundColor(getResources().getColor(R.color.share_with));
                tv_personal.setBackgroundColor(getResources().getColor(R.color.unselected));
                break;
            case R.id.tv_personal:
                acType = 1;
                tv_business.setBackgroundColor(getResources().getColor(R.color.unselected));
                tv_personal.setBackgroundColor(getResources().getColor(R.color.login_background));
                break;*/

            case R.id.tv_more_category:
                MoreCategoryDialog();
                break;
            case R.id.rb_debit:
                if (editTransaction)
                    return;
                rb_debit.setChecked(true);
                rb_credit.setChecked(false);
                rb_refund.setChecked(false);
                relative1.setBackgroundColor(getResources().getColor(R.color.share_with));
                rb_debit.setTextColor(getResources().getColor(R.color.white));
                relative2.setBackgroundColor(getResources().getColor(R.color.gray_add));
                rb_credit.setTextColor(getResources().getColor(R.color.black));
                relative3.setBackgroundColor(getResources().getColor(R.color.gray_add));
                rb_refund.setTextColor(getResources().getColor(R.color.black));

                transactionTypeID = Integer.parseInt(dbHelper.getValue(DBConstants.TRANSACTION_TYPE,
                        DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.DEBIT + "'"));
                break;
            case R.id.rb_credit:
                if (editTransaction)
                    return;
                rb_credit.setChecked(true);
                rb_debit.setChecked(false);
                rb_refund.setChecked(false);
                relative1.setBackgroundColor(getResources().getColor(R.color.gray_add));
                rb_debit.setTextColor(getResources().getColor(R.color.black));
                relative2.setBackgroundColor(getResources().getColor(R.color.share_with));
                rb_credit.setTextColor(getResources().getColor(R.color.white));
                relative3.setBackgroundColor(getResources().getColor(R.color.gray_add));
                rb_refund.setTextColor(getResources().getColor(R.color.black));
                transactionTypeID = Integer.parseInt(dbHelper.getValue(DBConstants.TRANSACTION_TYPE,
                        DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.CREDIT + "'"));
                break;
            case R.id.rb_refund:
                if (editTransaction)
                    return;
                rb_refund.setChecked(true);
                rb_credit.setChecked(false);
                rb_debit.setChecked(false);
                relative1.setBackgroundColor(getResources().getColor(R.color.gray_add));
                rb_debit.setTextColor(getResources().getColor(R.color.black));
                relative2.setBackgroundColor(getResources().getColor(R.color.gray_add));
                rb_credit.setTextColor(getResources().getColor(R.color.black));
                relative3.setBackgroundColor(getResources().getColor(R.color.share_with));
                rb_refund.setTextColor(getResources().getColor(R.color.white));
                transactionTypeID = Integer.parseInt(dbHelper.getValue(DBConstants.TRANSACTION_TYPE,
                        DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.REFUND + "'"));
                break;
            case R.id.deleteTransaction:

                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getActivity())
                        //set message, title, and icon
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to Delete ?")
                        .setIcon(R.drawable.delete)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code

                                long smsId = 0;
                                try {
                                    smsId = Long.parseLong(dbHelper.getValue(DBConstants.TRANSACTIONS,
                                            DBConstants.SMS_ID, DBConstants.ID + " = " + getArguments().getLong
                                                    (Constants.TRANSACTION_ID), null, null));
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }


                                ContentValues values = new ContentValues();
                                values.put(DBConstants.SMS_STATUS, 0);
                                dbHelper.updateRecords(DBConstants.SMS, values,
                                        DBConstants.ID + " = " + smsId, null);
                                dbHelper.deleteRecords(DBConstants.TRANSACTIONS, DBConstants.ID + " = " +
                                        getArguments().getLong(Constants.TRANSACTION_ID), null);

                                // getArguments().getLong(Constants.TRANSACTION_ID)
                                dialog.dismiss();
                                ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
                                ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);
                                ((MainActivity) getActivity()).onFragmentAdd(new DashBoardFragment(), Constants.DASHBOARD);

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
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    //datepicker
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, selectedYear);
            c.set(Calendar.MONTH, selectedMonth);
            c.set(Calendar.DAY_OF_MONTH, selectedDay);
            DateFormat curFormater2 = new SimpleDateFormat("dd MMM yyyy");
            selectedDateInMillSecond = c.getTimeInMillis();
            String output = curFormater2.format(c.getTime());
            date.setText(output);
            //date.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "));
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        AccountsModel model = spinnerAdapter.getItem(i);
        acNoId = model.getAccountId();

        if (CommonUtils.getMerchantImage(model.getBankName()) == -1) {
            try {
                  String bankName=model.getBankName();
                String imageUrl=((BaseActivity) getActivity()).dbHelper.getValue(DBConstants.BANKS, DBConstants.BANK_IMAGE,
                        DBConstants.BANK_NAME + " = '" + model.getBankTitle()+ "'");

                loading.displayImage(imageUrl, iv_add_transaction, R.drawable.default_bank);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            iv_add_transaction.setImageResource(CommonUtils.getMerchantImage(model.getBankName()));
        }


//        iv_add_transaction.setImageResource(model.getImage());

        if (model.getAccountId() == Constants.CASH_ACCOUNT_NO) {
            amount = transactionAmount.getText().toString().trim();
            //transactionTypeID = Integer.parseInt(dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.CASH + "'"));
            billerId = 0; //TODO: calculate from DB from transaction to field
        } else {
            amount = transactionAmount.getText().toString().trim();
            String transactionName = Constants.DEBIT;
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.rb_debit:
                    transactionName = Constants.DEBIT;
                    break;
                case R.id.rb_credit:
                    transactionName = Constants.CREDIT;
                    break;
                case R.id.rb_refund:
                    transactionName = Constants.REFUND;
                    break;
                default:
                    break;
            }

            //  transactionTypeID = Integer.parseInt(dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + transactionName + "'"));
            billerId = 0; //TODO: calculate from DB from transaction to field
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        AccountsModel model = spinnerAdapter.getItem(0);
        iv_add_transaction.setImageResource(model.getImage());
    }


    private void check() {

        if (rb_debit.isChecked()) {
            rb_credit.setChecked(false);
            rb_refund.setChecked(false);
        } else if (rb_credit.isChecked()) {
            rb_debit.setChecked(false);
            rb_refund.setChecked(false);
        } else if (rb_refund.isChecked()) {
            rb_credit.setChecked(false);
            rb_debit.setChecked(false);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)

            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void saveTransaction() {
        hideSoftKeyboard(getActivity());
        ContentValues values = new ContentValues();
        values.put(DBConstants.TRANSACTION_AMOUNT, transactionAmount.getText().toString().trim());
        values.put(DBConstants.TRANSACTION_ACCOUNT_ID, acNoId);
        values.put(DBConstants.TRANSACTION_TYPE_ID, transactionTypeID);
        if (!editTransaction) {
            values.put(DBConstants.TRANSACTION_DATE, selectedDateInMillSecond);
            values.put(DBConstants.SMS_ID, -10);

        }
        values.put(DBConstants.TRANSACTION_BILLER_ID, billerId);
        if (selectedCategoryId <= 0)
            selectedCategoryId = Long.parseLong(dbHelper.getValue
                    (DBConstants.CATEGORIES, DBConstants.ID, DBConstants.CATEGORY_TITLE + "='Others'"));
        values.put(DBConstants.CATEGORY_ID, selectedCategoryId);
        values.put(DBConstants.BILLER_NAME, billerName.getText().toString());


        values.put(DBConstants.DATE_MODIFIED, System.currentTimeMillis());

        if (editTransaction) {
            dbHelper.updateRecords(DBConstants.TRANSACTIONS, values, DBConstants.ID + " = " + getArguments().getLong(Constants.TRANSACTION_ID), null);
        } else {
            dbHelper.insertContentVals(DBConstants.TRANSACTIONS, values);
        }
    }

    public boolean chkCanEditAccountNumber(long transactionId) {

        //if user has account then return false
        try {
            if (Long.parseLong(dbHelper.getValue(DBConstants.TRANSACTIONS, DBConstants.TRANSACTION_ACCOUNT_ID, DBConstants.ID + " = " + transactionId)) > 0)
                return false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        //if user has a debit card then false
        try {
            if (Long.parseLong(dbHelper.getValue(DBConstants.TRANSACTIONS, DBConstants.CARD_NUMBER, DBConstants.ID + " = " + transactionId)) > 0)
                return false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        //if account id is -10 then can edit because it is liked with cash
        try {
            if (Long.parseLong(dbHelper.getValue(DBConstants.TRANSACTIONS, DBConstants.TRANSACTION_ACCOUNT_ID, DBConstants.ID + " = " + transactionId)) == -10)
                return false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        // user has no account number with this transaction and bank id can linked  with account id then can edit


        try {

            String smsId = (dbHelper.getValue(DBConstants.TRANSACTIONS, DBConstants.SMS_ID, DBConstants.ID + " = " + transactionId));

         /*    String bankIdStr = dbHelper.getValue(DBConstants.BANK_SMS_CODES, DBConstants.BANK_ID, DBConstants.BANK_SMS_SENDER + " = '" +
                    smsSender + "'");*/


            String sqlQuery = "select b." + DBConstants.BANK_ID + " from " + DBConstants.BANK_SMS_CODES + " b inner join " +
                    DBConstants.SMS + " s on b." + DBConstants.BANK_SMS_SENDER + " = s." + DBConstants.SMS_SENDER +

                    " where s." + DBConstants.ID + "=" + smsId;
            String bankIdStr = null;
            Cursor bankIdCursor = dbHelper.getDataThroughRaw(sqlQuery);
            if (bankIdCursor != null)
                if (bankIdCursor.moveToFirst())
                    bankIdStr = bankIdCursor.getLong(0) + "";
            if (Long.parseLong(dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ID, DBConstants.ACCOUNT_BANK_ID + " = " + bankIdStr)) > 0) {


                AccountsModel accounts;
                Cursor cursor = dbHelper.getTableRecords(DBConstants.USER_ACCOUNTS, null, DBConstants.ACCOUNT_BANK_ID + " = " + bankIdStr, null, null);

                if (cursor != null) {

                    spinnerAdapter = new AccountSpinnerAdapter(getActivity());
                    bank_name_spinner.setAdapter(spinnerAdapter);
                    long accountId, bankId;
                    int bankImage;
                    ;
                    String bankName;
                    while (cursor.moveToNext()) {


                        bankIdStr = cursor.getString(cursor.getColumnIndex(DBConstants.ACCOUNT_BANK_ID));
                        accountId = cursor.getLong(cursor.getColumnIndex(DBConstants.ID));
                        try {
                            bankId = Long.parseLong(bankIdStr);
                        } catch (Exception e) {
                            bankId = 0;
                            continue;
                        }
                        accounts = new AccountsModel();
                        bankName = CommonUtils.getBankName(dbHelper, bankId);
                        bankImage = CommonUtils.getMerchantImage(bankName);
                        accounts.setAccountId(accountId);
                        accounts.setAccountNo(cursor.getString(cursor.getColumnIndex(DBConstants.ACCOUNT_NUMBER)));
                        accounts.setImage(bankImage);
                        accounts.setBankId(bankId + "");
                        accounts.setBankTitle(bankName);
                        accounts.setBankName(bankName + " Account no - " + (cursor.getString(cursor.getColumnIndex(DBConstants.ACCOUNT_NUMBER))));
                        spinnerAdapter.add(accounts);
                    }
                    return true;

                }

            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;

    }

    //CHK IT IS DEBIT CAARD TRANSACTION IF yes then set bank name with debit card


    public boolean checkDebitCard(Cursor tractionCursor) {

        long transactionTypeid = 0;

        transactionTypeid = tractionCursor.getLong(tractionCursor.getColumnIndex(DBConstants.TRANSACTION_TYPE_ID));

        long debitCardTransactionId = Long.parseLong(
                dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME +
                        " = '" + Constants.DEBIT_CARD_EXPENSE + "'"));


        if (debitCardTransactionId == transactionTypeid) {
            spinnerAdapter = new AccountSpinnerAdapter(getActivity());
            bank_name_spinner.setAdapter(spinnerAdapter);
            String bankIdStr = dbHelper.getValue(DBConstants.BANK_SMS_CODES, DBConstants.BANK_ID, DBConstants.BANK_SMS_SENDER + " = '" +
                    tractionCursor.getString(tractionCursor.getColumnIndex(DBConstants.SMS_SENDER)) + "'");

            int bankId = 0;
            //bankIdStr = dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ACCOUNT_BANK_ID, DBConstants.ID + " = " + transactionCursor.getLong(transactionCursor.getColumnIndex(DBConstants.TRANSACTION_ACCOUNT_ID)));
            try {
                if (bankIdStr != null)
                    bankId = Integer.parseInt(bankIdStr);
            } catch (Exception e) {
                bankId = 0;
            }

            spinnerAdapter = new AccountSpinnerAdapter(getActivity());
            bank_name_spinner.setAdapter(spinnerAdapter);
            AccountsModel accounts = new AccountsModel();
            accounts.setImage(CommonUtils.getMerchantImage(CommonUtils.getBankName(dbHelper, bankId)));
            accounts.setBankId(bankId + "");
            accounts.setBankTitle(CommonUtils.getBankName(dbHelper, bankId));
            accounts.setBankName(CommonUtils.getBankName(dbHelper, bankId) + " Debit Card - " + (tractionCursor.getString(tractionCursor.getColumnIndex(DBConstants.CARD_NUMBER))));
            spinnerAdapter.add(accounts);
            return true;
        } else return false;


    }

}

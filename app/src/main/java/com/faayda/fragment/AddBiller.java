package com.faayda.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.faayda.BaseActivity;
import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.adapter.AccountSpinnerAdapter;
import com.faayda.adapter.CategoriesGridAdapter;
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

/**
 * Created by Antonio on 16-08-2015.
 */
public final class AddBiller extends Fragment implements AdapterView.OnItemSelectedListener {

    long billId;
    boolean editMode;
    DBHelper dbHelper;
    GridView griddview;
    int accountType = 1;
    private DatePickerDialog dpicker;
    ImageView billerImage;
    Spinner sp_selectBiller;//sp_billing_cycle
    //BillerListAdapter adapter;
    private FragmentManager fragmentManager;
    EditText et_rupee_biller;//et_transaction_to_biller, et_ac_id;
    //TextView tv_personal_biller, tv_bussiness_biller;
    TextView tv_date_addCash;
    AccountSpinnerAdapter spinnerAdapter;
    CategoriesGridAdapter gridAdapter;
    boolean isEditing = false;
    ImageView dateImage;
    TextView deleteBillTV;
    Context context;
    long billerId;
    Loader loader;

    Loader lodimg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.add_biller, container, false);
        ((MainActivity) getActivity()).setTopBarTitle(Constants.ADD_BILLER);
        ((MainActivity) getActivity()).invalidateTopBar(Constants.TITLE_BAR.SAVE_BUTTON);
        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.VISIBLE);
        context = getActivity();

        loader = new Loader(context);
        sp_selectBiller = (Spinner) layoutView.findViewById(R.id.sp_selectBiller);
        dateImage = (ImageView) layoutView.findViewById(R.id.iv_datePicker_addCash);
      /*  tv_personal_biller = (TextView) layoutView.findViewById(R.id.tv_personal_biller);
        tv_bussiness_biller = (TextView) layoutView.findViewById(R.id.tv_bussiness_biller);
        sp_billing_cycle = (Spinner) layoutView.findViewById(R.id.sp_billing_cycle);*/
        billerImage = (ImageView) layoutView.findViewById(R.id.billerImage);
        tv_date_addCash = (TextView) layoutView.findViewById(R.id.tv_date_biller);
        et_rupee_biller = (EditText) layoutView.findViewById(R.id.et_rupee_biller);
        deleteBillTV = (TextView) layoutView.findViewById(R.id.deleteBill);
        lodimg = new Loader(getActivity());
/*        et_transaction_to_biller = (EditText) layoutView.findViewById(R.id.et_transaction_to_biller);
        et_ac_id = (EditText) layoutView.findViewById(R.id.et_ac_id);*/

        //    adapter = new BillerListAdapter(getActivity());
/*        sp_billing_cycle.setAdapter(adapter);

        tv_personal_biller.setOnClickListener(this);
        tv_bussiness_biller.setOnClickListener(this);*/


        //  actionPopupWindow();
        spinnerAdapter = new AccountSpinnerAdapter(getActivity());
        sp_selectBiller.setAdapter(spinnerAdapter);
/*
        sp_selectBiller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                billId = spinnerAdapter.banksList.get(position).getAccountId();
            }
        });*/
        sp_selectBiller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                billerId = spinnerAdapter.banksList.get(position).getAccountId();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                dpicker = new DatePickerDialog(getActivity(), pickerListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                DateFormat curFormater2 = new SimpleDateFormat("dd-MM-yyyy");


                String output = curFormater2.format(c.getTime());

                tv_date_addCash.setText(output);
                dpicker.show();
            }
        });


        deleteBillTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                    smsId = Long.parseLong(dbHelper.getValue(DBConstants.BILL_RECORDS,
                                            DBConstants.SMS_ID, DBConstants.ID + " = " + getArguments().getLong
                                                    (Constants.BILL_ID), null, null));
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }


                                ContentValues values = new ContentValues();
                                values.put(DBConstants.SMS_STATUS, 0);
                                dbHelper.updateRecords(DBConstants.SMS, values,
                                        DBConstants.ID + " = " + smsId, null);
                                dbHelper.deleteRecords(DBConstants.BILL_RECORDS, DBConstants.ID + " = " +
                                        getArguments().getLong(Constants.BILL_ID), null);

                                // getArguments().getLong(Constants.TRANSACTION_ID)
                                dialog.dismiss();
                                ((MainActivity) getActivity()).onFragmentAdd(new BillsAndEmi(), Constants.BILLS);

                            }
                        })


                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        })
                        .create();
                myQuittingDialogBox.show();


            }
        });

        return layoutView;
    }


    //datepicker
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, selectedYear);
            c.set(Calendar.MONTH, selectedMonth);
            c.set(Calendar.DAY_OF_MONTH, selectedDay);
            DateFormat curFormater2 = new SimpleDateFormat("dd-MM-yyyy");
            String output = curFormater2.format(c.getTime());
            tv_date_addCash.setText(output);
            //date.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "));
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Calendar c = Calendar.getInstance();
        DateFormat curFormater2 = new SimpleDateFormat("dd-MM-yyyy");


        String output = curFormater2.format(c.getTime());
        tv_date_addCash.setText(output);

        ((MainActivity) getActivity()).ibBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                ((MainActivity) getActivity()).setTopBarTitle(Constants.BILLS);
                ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
                ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);

            }
        });
/*        try {
            Cursor cursor = dbHelper.getTableRecords(DBConstants.BILLER_SMS_CODES, null, null, null);
            boolean cashItemAdded = false;
            AccountsModel accounts;
            while (cursor.moveToNext()) {
                accounts = new AccountsModel();
                accounts.setBankName(cursor.getString(cursor.getColumnIndex(DBConstants.BILLER_SMS_SENDER)));
                //    accounts.setImage(CommonUtils.getBillerIcon(accounts.getBankName()));
                accounts.setBankName(cursor.getString(cursor.getColumnIndex(DBConstants.BILLER_NAME)));
                accounts.setImage(CommonUtils.getBillerIcon(accounts.getBankName()));
                spinnerAdapter.add(accounts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

/*        try {
            String[] array = {"Monthly", "Bi-Monthly", "Quarterly", "Half-Yearly", "Annually"};
            BillerListModel model;
            for (int i = 0; i < 5; i++) {
                model = new BillerListModel();

                model.setName(array[i]);
                adapter.add(model);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        ((MainActivity) getActivity()).invalidateTopBar(Constants.TITLE_BAR.SAVE_BUTTON);
        getActivity().findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    //TODO: Insert in database
                    saveBillInfo();
                }
            }
        });


        if (getArguments() != null) {

            billId = getArguments().getLong(Constants.BILL_ID);
            if (billId > 0) {
                isEditing = true;
                populateData(billId);
                ((MainActivity) getActivity()).setTopBarTitle(Constants.EDIT_BILLER);

            } else

                billEmiType();


        } else billEmiType();


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.dbHelper = ((BaseActivity) activity).dbHelper;
    }

    private void populateData(long billId) {

        Cursor cursor = dbHelper.getTableRecords(DBConstants.BILL_RECORDS, null, DBConstants.ID + " = " + billId, null);
        deleteBillTV.setVisibility(View.VISIBLE);
        if (cursor.moveToFirst()) {

            billerId = cursor.getLong(cursor.getColumnIndex(DBConstants.BILLER_ID));

            String selectQuery = "SELECT  O." + DBConstants.BILLER_NAME + " , C." + DBConstants.CATEGORY_IMAGE + ", C." + DBConstants.CATEGORY_TITLE
                    + " FROM " +
                    DBConstants.CATEGORIES + " C, " +
                    DBConstants.BILLER_LIST + " O WHERE O." + DBConstants.ID + " = " + billerId
                    + " AND O." + DBConstants.CATEGORY_ID + " = " + " C." + DBConstants.ID;


            Cursor joinCursor = dbHelper.getDataThroughRaw(selectQuery);

            if (joinCursor != null)
                if (joinCursor.moveToFirst()) {

                    String categoryTitle = joinCursor.getString(joinCursor.getColumnIndex(DBConstants.CATEGORY_TITLE));
                    String imageUrl = joinCursor.getString(joinCursor.getColumnIndex(DBConstants.CATEGORY_IMAGE));


                    try {
                        if (CommonUtils.getCategoryIcon(categoryTitle) != -1)
                            billerImage.setImageResource(CommonUtils.getCategoryIcon(categoryTitle));
                        else lodimg.displayImage(imageUrl, billerImage,R.drawable.medical);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    spinnerAdapter = new AccountSpinnerAdapter(getActivity());
                    sp_selectBiller.setAdapter(spinnerAdapter);
                    try {

                        AccountsModel accounts;


                        accounts = new AccountsModel();
                        accounts.setBankName(joinCursor.getString(joinCursor.getColumnIndex(DBConstants.BILLER_NAME)));
                        accounts.setAccountId(billerId);
                        spinnerAdapter.add(accounts);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            tv_date_addCash.setText(cursor.getString(cursor.getColumnIndex(DBConstants.BILL_DUE_DATE)));
            et_rupee_biller.setText(cursor.getLong(cursor.getColumnIndex(DBConstants.BILL_AMOUNT)) + "");


        }
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(et_rupee_biller.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Please enter amount", Toast.LENGTH_LONG).show();
            et_rupee_biller.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(tv_date_addCash.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Please select date", Toast.LENGTH_SHORT).show();
            //etNickName.requestFocus();
            return false;
        }
        return true;
    }

/*    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_personal_biller:
                tv_personal_biller.setBackgroundColor(getResources().getColor(R.color.login_background));
                tv_bussiness_biller.setBackgroundColor(getResources().getColor(R.color.unselected));
                break;
            case R.id.tv_bussiness_biller:
                tv_personal_biller.setBackgroundColor(getResources().getColor(R.color.unselected));
                tv_bussiness_biller.setBackgroundColor(getResources().getColor(R.color.share_with));
                break;

            default:
                break;
        }
    }*/

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (view.getId()) {
            case R.id.sp_selectBiller:

                billId = spinnerAdapter.banksList.get(i).getAccountId();
                break;
     /*       case R.id.sp_billing_cycle:
                tv_personal_biller.setBackgroundColor(getResources().getColor(R.color.unselected));
                tv_bussiness_biller.setBackgroundColor(getResources().getColor(R.color.share_with));
                break;*/
            default:
                break;
        }


    }

    private void billEmiType() {
        String title;
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bill_emi_type);
        dialog.setCancelable(false);
        CategoryGridModel gridModel;

        // LinearLayout linearLayout=(LinearLayout)dialog.findViewById(R.id.popUpLinear);
        //   LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)linearLayout.getLayoutParams();
        double hei = height * (0.8);
        // params.height=(int)hei;
        // linearLayout.setLayoutParams(params);

        dialog.getWindow().setLayout((int) (width * (0.8)), (int) (height * (0.6)));
        griddview = (GridView) dialog.findViewById(R.id.gv_category);


        gridAdapter = new CategoriesGridAdapter(getActivity());
        try {
            String selectQuery = "SELECT C." + DBConstants.ID + " , C." + DBConstants.CATEGORY_TITLE + " , C." + DBConstants.CATEGORY_IMAGE
                    + " FROM " +
                    DBConstants.CATEGORIES + " C, " +
                    DBConstants.BILLER_LIST + " O WHERE C." + DBConstants.ID + " = O." + DBConstants.CATEGORY_ID + " GROUP BY O." +
                    DBConstants.CATEGORY_ID;
            Cursor cursor = dbHelper.getDataThroughRaw(selectQuery);

            if (cursor.moveToFirst())
                do {
                    gridModel = new CategoryGridModel();
                    gridModel.setcategoryId(cursor.getLong(cursor.getColumnIndex(DBConstants.ID)) + "");
                    gridModel.setCategoryTitle(cursor.getString(cursor.getColumnIndex(DBConstants.CATEGORY_TITLE)));
                    gridModel.setCategoryImageUrl(cursor.getString(cursor.getColumnIndex(DBConstants.CATEGORY_IMAGE)));


                    /*String categoryTitle = cursor.getString(cursor.getColumnIndex(DBConstants.CATEGORY_TITLE));
                    String imageUrl = cursor.getString(cursor.getColumnIndex(DBConstants.CATEGORY_IMAGE))*/


                /*    try {
                        if (CommonUtils.getCategoryIcon(categoryTitle) != -1)
                            billerImage.setImageResource(CommonUtils.getCategoryIcon(categoryTitle));
                        else lodimg.displayImage(imageUrl, billerImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    // System.out.println("ADDED : " + gridModel.getCategoryImage());
//                    gridModel.setCategoryImage(cursor.getString(cursor.getColumnIndex(DBConstants.CATEGORY_IMAGE)));
                    gridAdapter.add(gridModel);
                } while ((cursor.moveToNext()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        griddview.setAdapter(gridAdapter);
        griddview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (gridAdapter.categoryModelArrayList.get(i).getCategoryImage() == -1) {
                    try {
                        loader.displayImage(dbHelper.getValue(DBConstants.CATEGORIES, DBConstants.CATEGORY_IMAGE,
                                DBConstants.CATEGORY_TITLE + " = '" + gridAdapter.categoryModelArrayList.get(i).getCategoryTitle()
                                        + "'"), billerImage,R.drawable.medical);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    billerImage.setImageResource(gridAdapter.categoryModelArrayList.get(i).getCategoryImage());
                }

                // billerImage.setImageResource(gridAdapter.categoryModelArrayList.get(i).getCategoryImage());
                CategoryGridModel model = gridAdapter.categoryModelArrayList.get(i);
                if (CommonUtils.getCategoryIcon(model.getCategoryTitle()) == -1) {
                    //   lodimg.displayImage(model.getCategoryImageString(), viewHolder.categoryImages);

                    try {
                        lodimg.displayImage(dbHelper.getValue(DBConstants.CATEGORIES,
                                DBConstants.CATEGORY_IMAGE, DBConstants.CATEGORY_TITLE + " = '" + model.getCategoryTitle() + "'"), billerImage, R.drawable.medical);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    billerImage.setImageResource(CommonUtils.getCategoryIcon(model.getCategoryTitle()));

                }

                spinnerAdapter = new AccountSpinnerAdapter(getActivity());
                sp_selectBiller.setAdapter(spinnerAdapter);
                try {
                    Cursor cursor = dbHelper.getTableRecords(DBConstants.BILLER_LIST, null, DBConstants.CATEGORY_ID + "=" +
                            gridAdapter.categoryModelArrayList.get(i).getCategoryId(), null);
                    boolean cashItemAdded = false;
                    AccountsModel accounts;
                    if (cursor.moveToFirst()) {
                        do {
                            accounts = new AccountsModel();
                            accounts.setBankName(cursor.getString(cursor.getColumnIndex(DBConstants.BILLER_NAME)));
                            accounts.setAccountId(cursor.getLong(cursor.getColumnIndex(DBConstants.ID)));
                            // accounts.setImage(CommonUtils.getBillerIcon(accounts.getBankName()));
                            //accounts.setBankName(cursor.getString(cursor.getColumnIndex(DBConstants.BILLER_NAME)));
                            //accounts.setImage(CommonUtils.getBillerIcon(accounts.getBankName()));

                            spinnerAdapter.add(accounts);
                        } while (cursor.moveToNext());

                        sp_selectBiller.setSelection(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                dialog.dismiss();

            }
        });

        dialog.show();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
      /*  BillerListModel model = adapter.getItem(0);
        billerImage.setImageResource(model.getImage());
        billId = model.getId();*/
    }


    private void saveBillInfo() {

        // categoryid,billername,date,amount

        hideSoftKeyboard(getActivity());
        ContentValues values = new ContentValues();


        values.put(DBConstants.BILL_DUE_DATE, tv_date_addCash.getText().toString());
        values.put(DBConstants.BILL_AMOUNT, et_rupee_biller.getText().toString().trim());

        if (isEditing) {
            dbHelper.updateRecords(DBConstants.BILL_RECORDS, values, DBConstants.ID + " = " + billId, null);
        } else {
            values.put(DBConstants.BILLER_ID, billerId);
            dbHelper.insertContentVals(DBConstants.BILL_RECORDS, values);
        }
        ((MainActivity) getActivity()).onFragmentAdd(new BillsAndEmi(), Constants.BILLS);

        //getActivity().onBackPressed();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)

            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}

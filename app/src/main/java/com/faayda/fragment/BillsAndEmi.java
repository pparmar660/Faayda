package com.faayda.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.faayda.BaseActivity;
import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.adapter.BillAndEmiAdapter;
import com.faayda.customviews.CustomListView;
import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.models.BillAndEmiModel;
import com.faayda.preferences.Preferences;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by vinove on 8/13/2015.
 */
public final class BillsAndEmi extends BaseFragment implements AdapterView.OnItemClickListener, View.OnTouchListener, View.OnClickListener {

    DBHelper dbHelper;
    CustomListView bankInfo;
    BillAndEmiAdapter adapter;
    LinearLayout billEmiTutorial;
    Preferences pref;
    ImageView iv_add_biller;
    private String swipe = "bills", dash;
    private float x1, x2;
    static final int MIN_DISTANCE = 150;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ((MainActivity) getActivity()).setTopBarTitle(Constants.BILLS);
        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
        ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);

        View layoutView = inflater.inflate(R.layout.bill_emi, container, false);
        bankInfo = (CustomListView) layoutView.findViewById(R.id.lv_bankinfo);
        billEmiTutorial = (LinearLayout) layoutView.findViewById(R.id.billEmiTutorial);
        iv_add_biller = (ImageView) layoutView.findViewById(R.id.iv_add_biller);
        adapter = new BillAndEmiAdapter(getActivity(), dbHelper);
        pref = new Preferences(getActivity());
        bankInfo.setAdapter(adapter);
        bankInfo.setOnItemClickListener(this);
        billEmiTutorial.setOnTouchListener(this);
        iv_add_biller.setOnClickListener(this);
        loadData();
        if (pref.getBoolean(swipe) == true) {
            billEmiTutorial.setVisibility(View.GONE);
        }
        return layoutView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

/*
        switch (parent.getId()) {
            case R.id.lv_bankinfo:*/


    }

    /*@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).setTopBarTitle(Constants.BILLS);
//        ((MainActivity) getActivity()).invalidateTopBar(Constants.TITLE_BAR.NOTIFICATION_ONLY);
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = ((BaseActivity) activity).dbHelper;
    }

    private void loadData() {
        Cursor cursor = dbHelper.getTableRecords(DBConstants.BILL_RECORDS, null, null, null);
        long id;
        BillAndEmiModel billAndEmiModel;
        while (cursor.moveToNext()) {
            billAndEmiModel = new BillAndEmiModel();
            billAndEmiModel.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.ID)));

            String date = cursor.getString(cursor.getColumnIndex(DBConstants.BILL_DUE_DATE)).trim();
            try {


                String selectQuery = "SELECT  O." + DBConstants.BILLER_NAME + " , C." + DBConstants.CATEGORY_IMAGE + ", C." + DBConstants.CATEGORY_TITLE
                        + " FROM " +
                        DBConstants.CATEGORIES + " C, " +
                        DBConstants.BILLER_LIST + " O WHERE O." + DBConstants.ID + " = " + cursor.getLong(cursor.getColumnIndex(DBConstants.BILLER_ID))
                        + " AND O." + DBConstants.CATEGORY_ID + " = " + " C." + DBConstants.ID;


                Cursor joinCursor = dbHelper.getDataThroughRaw(selectQuery);

                if (joinCursor != null)
                    if (joinCursor.moveToFirst()) {

                        String categoryTitle = joinCursor.getString(joinCursor.getColumnIndex(DBConstants.CATEGORY_TITLE));
                        String imageUrl = joinCursor.getString(joinCursor.getColumnIndex(DBConstants.CATEGORY_IMAGE));
                        billAndEmiModel.setCategoryImageUri(imageUrl);
                        billAndEmiModel.setCategoryTitle(categoryTitle);
                    }

                //  billAndEmiModel.setMerchantImage(CommonUtils.getMerchantImage(cursor.getString(cursor.getColumnIndex(DBConstants.BILLER_NAME))));
//            billAndEmiModel.setBillRegarding(cur);
            } catch (Exception e
                    ) {
                e.printStackTrace();
            }
            billAndEmiModel.setDueAmount(cursor.getString(cursor.getColumnIndex(DBConstants.BILL_AMOUNT)));
            //TODO: set days to pay
            try {
                if (!date.equalsIgnoreCase("N/A")) {

                    Calendar calendar = Calendar.getInstance();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                    billAndEmiModel.setDaysDue(CommonUtils.get_count_of_days(
                            dateFormat.format(calendar.getTime()),
                            date) + "");
                } else
                    billAndEmiModel.setDaysDue("N/A");
            } catch (Exception e) {
                e.printStackTrace();
            }

            billAndEmiModel.setDueDate(cursor.getString(cursor.getColumnIndex(DBConstants.BILL_DUE_DATE)));
            id = cursor.getLong(cursor.getColumnIndex(DBConstants.BILLER_ID));
            billAndEmiModel.setMerchantName(CommonUtils.getBillerName(dbHelper, id));
            billAndEmiModel.setBillRegarding("");


            adapter.addItem(billAndEmiModel);
        }
    }

  /*  @Override
    public void onPause() {
        ((MainActivity) getActivity()).setTopBarTitle(Constants.BILL);
        super.onPause();
    }*/


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
                    billEmiTutorial.setVisibility(View.GONE);
                    pref.setBoolean(swipe, true);
                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_biller:
                ((MainActivity) getActivity()).onFragmentAdd(new AddBiller(), Constants.ADD_BILLER);
                break;
            default:
                break;
        }
    }
}

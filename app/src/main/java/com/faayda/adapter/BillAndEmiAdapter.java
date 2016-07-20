package com.faayda.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.fragment.AddBiller;
import com.faayda.imageloader.Loader;
import com.faayda.models.BillAndEmiModel;
import com.faayda.processor.TransactionMessageProcessor;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;

import java.util.ArrayList;

/**
 * Created by vinove on 8/13/2015.
 */
public final class BillAndEmiAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    public ArrayList<BillAndEmiModel> billAndEmiModels;
    Loader loding;
    DBHelper dbHelper;

    public BillAndEmiAdapter(Context mContext, DBHelper dbHelper) {
        this.mContext = mContext;
        this.dbHelper = dbHelper;
        billAndEmiModels = new ArrayList<>();
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        loding = new Loader(mContext);
    }

    public void addItem(BillAndEmiModel model) {
        billAndEmiModels.add(model);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return billAndEmiModels.size();
    }

    @Override
    public BillAndEmiModel getItem(int position) {
        return billAndEmiModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.bill_emi_row, parent, false);
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.bt_make_paid = (Button) convertView.findViewById(R.id.bt_make_paid);
        viewHolder.merchantImage = (ImageView) convertView.findViewById(R.id.img_merchant);
        viewHolder.payeeName = (TextView) convertView.findViewById(R.id.tv_payee_name);
        viewHolder.billRegarding = (TextView) convertView.findViewById(R.id.tv_card_no);
        viewHolder.dueAmount = (TextView) convertView.findViewById(R.id.tv_amount);
        viewHolder.due_date = (TextView) convertView.findViewById(R.id.tv_due_date);
        viewHolder.daysDue = (TextView) convertView.findViewById(R.id.tv_no_of_days_due);
        viewHolder.mainLinearLayout = (LinearLayout) convertView.findViewById(R.id.billInfoLinear);
        viewHolder.daysDueOrOverDueTv = (TextView) convertView.findViewById(R.id.tv_days_due);

        final BillAndEmiModel model = getItem(position);

        //viewHolder.merchantImage.setImageResource(model.getMerchantImage());
        if (model.getCategoryTitle() != null) {
            if (CommonUtils.getCategoryIcon(model.getCategoryTitle()) != -1)
                viewHolder.merchantImage.setImageResource(CommonUtils.getCategoryIcon(model.getCategoryTitle()));
            else loding.displayImage(model.getCategoryImageUri(), viewHolder.merchantImage,R.drawable.medical);

        } else viewHolder.merchantImage.setImageResource(R.drawable.default_bank);
        viewHolder.daysDue.setText("N/A");
        if (model.getDaysDue() != null)
            if (TransactionMessageProcessor.isInteger(model.getDaysDue())) {
                int days = Integer.parseInt(model.getDaysDue());
                if (days < 0) {
                    viewHolder.daysDue.setText((days * -1) + "");
                    viewHolder.daysDueOrOverDueTv.setText("overdue days");
                } else viewHolder.daysDue.setText(days + "");


            }


        viewHolder.billRegarding.setText(model.getBillRegarding());
        viewHolder.due_date.setText("Due on : " + model.getDueDate());
        viewHolder.dueAmount.setText(CommonUtils.DoubleToStringLimits(Double.parseDouble(model.getDueAmount())));
        viewHolder.payeeName.setText(model.getMerchantName());

        boolean isPaid = false;

        final String paidStatus = dbHelper.getValue(DBConstants.BILL_RECORDS, DBConstants.BILL_PAID_STATUS, DBConstants.ID + " = " + model.getId());

        if (paidStatus != null)
            if (Integer.parseInt(paidStatus) == 1)
                isPaid = true;


        if (!isPaid) {

            viewHolder.bt_make_paid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: Ask for transaction type; insert in Transaction table and update Biller Table

                    ContentValues values = new ContentValues();
                    values.put(DBConstants.BILL_PAID_STATUS, 1);


                    dbHelper.updateRecords(DBConstants.BILL_RECORDS, values, DBConstants.ID + " = " + model.getId(), null);
                    notifyDataSetChanged();

                }
            });
        } else {
            viewHolder.bt_make_paid.setText("Paid");
            viewHolder.bt_make_paid.setBackgroundColor(Color.parseColor("#00BD55"));
        }

        viewHolder.mainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle argument = new Bundle();
                argument.putLong(Constants.BILL_ID, billAndEmiModels.get(position).getId());

                boolean isPaid = false;

                final String paidStatus = dbHelper.getValue(DBConstants.BILL_RECORDS, DBConstants.BILL_PAID_STATUS, DBConstants.ID + " = " + model.getId());

                if (paidStatus != null)
                    if (Integer.parseInt(paidStatus) == 1)
                        isPaid = true;


                //if (paidStatus != 1)
                AddBiller billerFragment = new AddBiller();
                billerFragment.setArguments(argument);
                if (!isPaid)
                    ((MainActivity) mContext).onFragmentAdd(billerFragment, Constants.ADD_BILLER);

            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView merchantImage;
        Button bt_make_paid;
        TextView payeeName, billRegarding, due_date, dueAmount, daysDue, daysDueOrOverDueTv;
        LinearLayout mainLinearLayout;
    }
}


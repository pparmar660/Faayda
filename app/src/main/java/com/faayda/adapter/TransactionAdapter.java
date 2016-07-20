package com.faayda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.faayda.BaseActivity;
import com.faayda.R;
import com.faayda.database.DBConstants;
import com.faayda.imageloader.Loader;
import com.faayda.models.TransactionModel;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Aashutosh @ vinove on 8/6/2015.
 */
public final class TransactionAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    public ArrayList<TransactionModel> dataModel;
    Loader loding;

    public TransactionAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        dataModel = new ArrayList<>();
        loding = new Loader(mContext);
    }

    public void addItem(TransactionModel model) {
        dataModel.add(model);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataModel.size() + 1;
    }

    @Override
    public TransactionModel getItem(int position) {
        return dataModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.transaction_list_item, parent, false);
        ImageView ivBankIcon = (ImageView) convertView.findViewById(R.id.ivBankIcon);
        TextView tvTransactionWith = (TextView) convertView.findViewById(R.id.tvTransactionWith);
        TextView tvTransactionDate = (TextView) convertView.findViewById(R.id.tvTransactionDate);
        TextView tvTransactionAmount = (TextView) convertView.findViewById(R.id.tvTransactionAmount);
        RelativeLayout rel = (RelativeLayout) convertView.findViewById(R.id.rel);
        TextView accountNumberTv = (TextView) convertView.findViewById(R.id.accountNumber);
        if (dataModel.size() - 1 < position) {
            tvTransactionAmount.setVisibility(View.INVISIBLE);
            tvTransactionDate.setVisibility(View.INVISIBLE);
            tvTransactionWith.setVisibility(View.INVISIBLE);
            ivBankIcon.setVisibility(View.INVISIBLE);
            rel.setVisibility(View.INVISIBLE);
        } else {

            TransactionModel model = getItem(position);

//            ivBankIcon.setImageResource(model.getImage());

            if (CommonUtils.getMerchantImage(model.getTitle()) == -1) {
                //   lodimg.displayImage(model.getCategoryImageString(), viewHolder.categoryImages);

                try {
                    loding.displayImage(((BaseActivity) mContext).dbHelper.getValue(DBConstants.BANKS, DBConstants.BANK_IMAGE,
                            DBConstants.BANK_NAME + " = '" + model.getTitle() + "'"), ivBankIcon,R.drawable.default_bank);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                ivBankIcon.setImageResource(CommonUtils.getMerchantImage(model.getTitle()));
            }


            tvTransactionWith.setText(model.getTitle());
            tvTransactionDate.setText(model.getMonth());
            tvTransactionAmount.setText(CommonUtils.DoubleToStringLimits(Double.parseDouble(model.getAmount() + "")));
            long debitCardTransactionId = Long.parseLong(
                    ((BaseActivity) mContext).dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME +
                            " = '" + Constants.DEBIT_CARD_EXPENSE + "'"));

            if (model.getTransactionTypeId() == debitCardTransactionId) {
                if (model.getCardNumber() != null)
                    if (!model.getCardNumber().isEmpty())
                        accountNumberTv.setText("Debit card - " + model.getCardNumber());

            } else {
                if (model.getAccountNumber() != null) {
                    if (!model.getAccountNumber().equalsIgnoreCase("0"))
                        accountNumberTv.setText("A/C - " + model.getAccountNumber());
                    else accountNumberTv.setText("A/C - " + "N/A");
                } else accountNumberTv.setText("A/C - " + "N/A");
            }

            if (model.getTransactionTypeId() == -10)
                accountNumberTv.setText("Cash");
        }


        return convertView;
    }
}

package com.faayda.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.faayda.R;

public final class AddTransactionPopupAdapter extends BaseAdapter {

    Context mContext;
    int[] bank_image;
    String[] bank_bal;
    String[] bank_name;
    ViewHolder viewHolder;
    LayoutInflater mInflater;
    ImageView image;

    public AddTransactionPopupAdapter(Context mContext, int[] bank_image, String[] bank_name, String[] bank_bal) {
        this.mContext = mContext;
        this.bank_bal = bank_bal;
        this.bank_name = bank_name;
        this.bank_image = bank_image;

        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return bank_name.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.add_transaction_popup_row, null);
            viewHolder = new ViewHolder();
            viewHolder.bankImage = (ImageView) convertView
                    .findViewById(R.id.iv_bank_notification);
            viewHolder.bankName = (TextView) convertView
                    .findViewById(R.id.tv_notification_bankname);
            viewHolder.bankBal = (TextView) convertView
                    .findViewById(R.id.tv_notification_balance);
            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.bankImage.setImageResource(bank_image[position]);
        viewHolder.bankName.setText(bank_name[position]);
        viewHolder.bankBal.setText(bank_bal[position]);


        return convertView;

    }

    class ViewHolder {
        ImageView bankImage;
        TextView bankName, bankBal;
    }

}

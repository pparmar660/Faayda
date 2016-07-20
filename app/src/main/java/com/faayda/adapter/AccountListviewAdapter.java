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

public final class AccountListviewAdapter extends BaseAdapter {

    Context mContext;
    String[] acc_type;
    ViewHolder viewHolder;
    LayoutInflater mInflater;
    ImageView image;

    public AccountListviewAdapter(Context mContext, String[] acc_type) {
        this.mContext = mContext;
        this.acc_type = acc_type;

        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return acc_type.length;
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.account_type_row, null);
            viewHolder = new ViewHolder();
            viewHolder.acc_type = (TextView) convertView.findViewById(R.id.tv_account_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.acc_type.setText(acc_type[position]);

        return convertView;
    }


    class ViewHolder {
        TextView acc_type;
    }
}

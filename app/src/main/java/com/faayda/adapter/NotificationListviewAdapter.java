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

public final class NotificationListviewAdapter extends BaseAdapter {

    Context mContext;
    int[] provider_image;
    String[] due_balance;
    String[] provider_name;
    String[] bill_type;
    String[] due_date;
    String[] due_time;
    String[] msg_from;
    String[] msg_detail;
    ViewHolder viewHolder;
    LayoutInflater mInflater;
    ImageView image;

    public NotificationListviewAdapter(Context mContext, int[] provider_image, String[] provider_name, String[] due_balance
            , String[] due_date, String[] due_time, String[] bill_type, String[] msg_detail, String[] msg_from) {
        this.mContext = mContext;
        this.provider_image = provider_image;
        this.provider_name = provider_name;
        this.due_balance = due_balance;
        this.due_date = due_date;
        this.due_time = due_time;
        this.msg_from = msg_from;
        this.msg_detail = msg_detail;
        this.bill_type = bill_type;

        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return provider_image.length;
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
            convertView = mInflater.inflate(R.layout.bill_notification_row, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_provider);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_provider_name);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_bill_type);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_amount_due);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_bill_due_date);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_bill_due_time);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_msg_from);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_bill_detail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
     /*   viewHolder.imageView.setImageResource(bank_image[position]);
        viewHolder.textView.setText(bank_name[position]);
        viewHolder.textView.setText(bank_bal[position]);*/
        return convertView;
    }


    class ViewHolder {
        ImageView imageView, image;
        TextView textView;
    }
}

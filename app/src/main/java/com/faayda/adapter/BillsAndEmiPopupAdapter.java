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

/**
 * Created by vinove on 8/13/2015.
 */
public final class BillsAndEmiPopupAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    int catImage[] = {};
    Context mContext;
    ViewHolder viewHolder;
    String catName[] = {};

    public BillsAndEmiPopupAdapter(Context mContext, int[] catImage, String[] catName) {
        this.mContext = mContext;
        this.catImage = catImage;
        this.catName = catName;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return catImage.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.catagory_row, null);
            viewHolder = new ViewHolder();
            viewHolder.catImage = (ImageView) convertView.findViewById(R.id.iv_catagory);
            viewHolder.catName = (TextView) convertView.findViewById(R.id.tv_catagory);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        ImageView catImage;
        TextView catName;
    }
}

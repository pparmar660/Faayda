package com.faayda.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public final class SpinerAdapterAddTransaction extends BaseAdapter {

    private Activity context;

    private ArrayList<HashMap<String, String>> list;

    public SpinerAdapterAddTransaction(Activity context, ArrayList<HashMap<String, String>> list) {
        this.context = context;
        this.list = list;


    }

    public SpinerAdapterAddTransaction(Activity context) {
        this.context = context;

        this.list = new ArrayList<HashMap<String, String>>();
    }

    public void add(HashMap<String, String> hashMap) {
        this.list.add(hashMap);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public HashMap<String, String> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            holder = new ViewHolder();
            holder.nameTv = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTv.setText(list.get(position).get(Constant.KEY_NAME));
        holder.nameTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.tx_ar, 0);
        holder.nameTv.setTextColor(Color.BLACK);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.
                    R.layout.simple_spinner_dropdown_item, parent, false);
            holder = new ViewHolder();
            holder.nameTv = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTv.setBackgroundColor(Color.WHITE);
        holder.nameTv.setText(list.get(position).get(Constant.KEY_NAME));
        holder.nameTv.setTextColor(Color.BLACK);
        return convertView;
    }

    class ViewHolder {
        TextView nameTv;
    }*/
}

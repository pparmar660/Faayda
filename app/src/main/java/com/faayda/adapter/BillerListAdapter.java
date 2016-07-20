package com.faayda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.faayda.models.BillerListModel;

import java.util.ArrayList;

/**
 * Created by Antonio on 23-08-2015.
 */
public class BillerListAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    ArrayList<BillerListModel> modelList;

    public BillerListAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        modelList = new ArrayList<>();
    }

    public void add(BillerListModel model) {
        modelList.add(model);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public BillerListModel getItem(int i) {
        return modelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, viewGroup, false);
            holder.textView = (TextView) view.findViewById(android.R.id.text1);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textView.setText(getItem(i).getName());
        return view;
    }

    class ViewHolder {
        TextView textView;
    }
}

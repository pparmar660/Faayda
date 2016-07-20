package com.faayda.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.faayda.models.BankAdapterModel;

import java.util.ArrayList;

/**
 * Created by Antonio on 16-08-2015.
 */
public class BankListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    ArrayList<BankAdapterModel> modelArrayList;

    public BankListAdapter(Context mContext) {
        this.mContext = mContext;
        modelArrayList = new ArrayList<>();
        inflater = LayoutInflater.from(mContext);
    }

    public void addItem(BankAdapterModel model) {
        modelArrayList.add(model);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public BankAdapterModel getItem(int i) {
        return modelArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            viewHolder.textView = (TextView) view.findViewById(android.R.id.text1);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setTextColor(Color.BLACK);
        viewHolder.textView.setBackgroundColor(Color.WHITE);
        viewHolder.textView.setText(modelArrayList.get(i).getBankName());
        view.setId(i);
        return view;
    }

    class ViewHolder {
        TextView textView;
    }
}

package com.faayda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.faayda.models.AccountsModel;

import java.util.ArrayList;

/**
 * Created by vinove on 21/8/15.
 */
public class AccountSpinnerAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
   public ArrayList<AccountsModel> banksList;

    public AccountSpinnerAdapter(Context mContext) {
        this.mContext = mContext;
        banksList = new ArrayList<>();
        inflater = LayoutInflater.from(mContext);
    }

    public void add(AccountsModel model) {
        banksList.add(model);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return banksList.size();
    }

    @Override
    public AccountsModel getItem(int i) {
        return banksList.get(i);
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
        holder.textView.setText(getItem(i).getBankName());
        return view;
    }

    class ViewHolder {
        TextView textView;
    }
}

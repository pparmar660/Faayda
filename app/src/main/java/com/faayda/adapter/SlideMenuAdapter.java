package com.faayda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.faayda.R;

import java.util.Locale;

/**
 * Created by Aashutosh @ vinove on 8/3/2015.
 */
public final class SlideMenuAdapter extends BaseAdapter {

    private Context mContext;
    private int[] slideMenuIcons;
    private String[] slideMenuText;
    private LayoutInflater inflater;

    public SlideMenuAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        /*slideMenuIcons = new int[]{R.drawable.accounts, R.drawable.bills, R.drawable.cash,
                R.drawable.spend_summary, R.drawable.settings, R.drawable.feedback, R.drawable.share,
                R.drawable.faq, R.drawable.facebook, R.drawable.twitter, R.drawable.refresh_sms,
                R.drawable.about};*/
        slideMenuIcons = new int[]{R.drawable.slide_rupee, R.drawable.slide_bill, R.drawable.slide_cash,
                R.drawable.slide_settings, R.drawable.slide_feedback, R.drawable.slide_share,
                R.drawable.slide_faq, R.drawable.slide_fb, R.drawable.slide_twitter, R.drawable.slide_refersh};//,R.drawable.about};
        slideMenuText = mContext.getResources().getStringArray(R.array.slide_menu_items);
    }

    @Override
    public int getCount() {
        return slideMenuText.length;
    }

    @Override
    public String getItem(int position) {
        return slideMenuText[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.slide_menu_item, parent, false);
            viewHolder.itemText = (TextView) convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemText.setText(slideMenuText[position].toUpperCase(Locale.getDefault()));
        viewHolder.itemText.setCompoundDrawablesWithIntrinsicBounds(slideMenuIcons[position], 0, 0, 0);
        return convertView;
    }

    class ViewHolder {
        TextView itemText;
    }
}

package com.faayda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.faayda.R;
import com.faayda.models.FAQModel;

import java.util.ArrayList;

/**
 * Created by Antonio on 15-08-2015.
 */
public class FAQAdapter extends BaseAdapter {

    Animation anim;
    Context mContext;
    LayoutInflater inflater;
    ArrayList<FAQModel> faqList;

    public FAQAdapter(Context mContext) {
        this.mContext = mContext;
        faqList = new ArrayList<>();
        inflater = LayoutInflater.from(mContext);
        anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
    }

    public void addItem(FAQModel model) {
        faqList.add(model);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return faqList.size();
    }

    @Override
    public Object getItem(int i) {
        return faqList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder = new ViewHolder();
        view = inflater.inflate(R.layout.faq_row, viewGroup, false);
        viewHolder.question = (TextView) view.findViewById(R.id.faq_title);
        viewHolder.answer = (TextView) view.findViewById(R.id.faq_desc);
        FAQModel model = (FAQModel) getItem(i);
        viewHolder.question.setText(model.question);
        viewHolder.answer.setText(model.answer);
        viewHolder.question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //viewHolder.answer.setVisibility(viewHolder.answer.isShown() ? View.GONE : View.VISIBLE);
                if (viewHolder.answer.isShown()) {
                    viewHolder.answer.setVisibility(View.GONE);
                    viewHolder.question.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.faq, 0);
                    slide_up(mContext, viewHolder.answer);

                } else {
                    viewHolder.answer.setVisibility(View.VISIBLE);
                    viewHolder.question.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.faq2, 0);
                    slide_down(mContext, viewHolder.answer);
                }
            }
        });
        return view;
    }

    class ViewHolder {
        TextView question, answer;
    }

    public void slide_down(Context ctx, View v) {
        anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
        if (anim != null) {
            anim.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(anim);
            }
        }
    }

    public void slide_up(Context ctx, View v) {
        anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
        if (anim != null) {
            anim.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(anim);
            }
        }
    }
}

package com.faayda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.faayda.BaseActivity;
import com.faayda.R;
import com.faayda.database.DBConstants;
import com.faayda.imageloader.Loader;
import com.faayda.models.CashManagementModel;
import com.faayda.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by vinove on 8/12/2015.
 */
public final class CashManagmentAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    ArrayList<CashManagementModel> dataList;
    Loader loading;

    public CashManagmentAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        dataList = new ArrayList<>();
        loading = new Loader(mContext);
    }

    public void addItem(CashManagementModel model) {
        dataList.add(model);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public CashManagementModel getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.cash_managment_row, viewGroup, false);
        TextView tvCashMonth = (TextView) view.findViewById(R.id.tv_date_managment);
        TextView tvCashAcTypePersonal = (TextView) view.findViewById(R.id.tv_personal_cash);
        TextView tvCashAccTypeBusiness = (TextView) view.findViewById(R.id.tv_bussiness_cash);
        ImageView bankIcon = (ImageView) view.findViewById(R.id.image);
        TextView tvCardNumber = (TextView) view.findViewById(R.id.tvCardNumber);
        TextView tvAccNumber = (TextView) view.findViewById(R.id.tvAccNumber);
        TextView tvWithdrawlAmount = (TextView) view.findViewById(R.id.tvWithdrawlAmount);
        TextView tvExactWithdrawlDate = (TextView) view.findViewById(R.id.tvExactWithdrawlDate);

        CashManagementModel model = getItem(i);
        tvCashMonth.setText(model.getMonth());
        bankIcon.setImageResource(model.getBankLogo());


        if (CommonUtils.getMerchantImage(model.getBankName()) == -1) {
            //   lodimg.displayImage(model.getCategoryImageString(), viewHolder.categoryImages);

            try {
                loading.displayImage(((BaseActivity) mContext).dbHelper.getValue(DBConstants.BANKS, DBConstants.BANK_IMAGE,
                        DBConstants.BANK_NAME + " = '" + model.getBankName() + "'"), bankIcon, R.drawable.default_bank);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            bankIcon.setImageResource(CommonUtils.getMerchantImage(model.getBankName()));
        }

        tvCardNumber.setText(model.getAccountNo());
        tvWithdrawlAmount.setText(CommonUtils.DoubleToStringLimits(Double.parseDouble(model.getAmount())));
        tvAccNumber.setText("N/A");
        if (model.getAccountNo() != null)
            if (!model.getAccountNo().isEmpty() && !model.getAccountNo().equalsIgnoreCase("0"))
                tvAccNumber.setText(model.getAccountNo());


        tvExactWithdrawlDate.setText(model.getExactDate());
        //TODO: Check account type and hide the other type button
        tvCashAcTypePersonal.setVisibility(View.VISIBLE);
        tvCashAccTypeBusiness.setVisibility(View.GONE);
        if (model.getAccountType() == 2) {
            tvCashAcTypePersonal.setVisibility(View.GONE);
            tvCashAccTypeBusiness.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
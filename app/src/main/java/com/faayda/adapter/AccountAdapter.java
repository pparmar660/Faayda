package com.faayda.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.faayda.AsynTask.RefreshAccount;
import com.faayda.BaseActivity;
import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.fragment.Account;
import com.faayda.imageloader.Loader;
import com.faayda.models.AccountModel;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;

import java.util.ArrayList;

/**
 * Created by vinove on 8/13/2015.
 */
public final class AccountAdapter extends BaseAdapter {

    Context mContext;
    boolean showDebitCards;
    LayoutInflater mInflater;
    ArrayList<AccountModel> modelList;
    DBHelper dbHelper;
    public boolean isSCreditType = false;
    Loader loding;

    public AccountAdapter(Context mContext, DBHelper dbHelper) {
        this.mContext = mContext;
        this.dbHelper = dbHelper;
        mInflater = LayoutInflater.from(mContext);
        modelList = new ArrayList<>();
        loding = new Loader(mContext);
    }

    public void addItem(AccountModel model) {
        modelList.add(model);
        notifyDataSetChanged();
    }

    public void setShowDebitCards(boolean showDebitCards) {
        this.showDebitCards = showDebitCards;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public AccountModel getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = new ViewHolder();
        convertView = mInflater.inflate(R.layout.accounts_row, parent, false);
        viewHolder.merchantImage = (ImageView) convertView.findViewById(R.id.bank_image);
        viewHolder.refreshAccount = (ImageView) convertView.findViewById(R.id.refresh_account);
        viewHolder.bankName = (TextView) convertView.findViewById(R.id.tv_account_bank_name);
        viewHolder.accNo = (TextView) convertView.findViewById(R.id.tv_acount_number);
        viewHolder.Amount = (TextView) convertView.findViewById(R.id.tv_account_amount_total);
        viewHolder.date = (TextView) convertView.findViewById(R.id.tv_acount_date);
        viewHolder.linkCard = (CheckBox) convertView.findViewById(R.id.linkedStatus);
        viewHolder.debit_card_link_container = (RelativeLayout) convertView.findViewById(R.id.debit_card_link_container);
        viewHolder.debitCard = (TextView) convertView.findViewById(R.id.debit_card);
        viewHolder.fixedDebitCardLinear = (LinearLayout) convertView.findViewById(R.id.fixedDebitCardLinear);
        viewHolder.fixedDebitNOTV = (TextView) convertView.findViewById(R.id.debitcard_number);
        viewHolder.outstandingLinear = (LinearLayout) convertView.findViewById(R.id.outstandngLinear);
        viewHolder.outStandingAmountTV = (TextView) convertView.findViewById(R.id.outStandingAmount);
        final AccountModel model = getItem(position);

        viewHolder.bankName.setText(model.getBankName());
        viewHolder.date.setText(model.getDate());
        viewHolder.accNo.setText(model.getAccountNo());
        try {if (CommonUtils.isNumeric(model.getAmount().replace(",","")))
            viewHolder.Amount.setText(CommonUtils.DoubleToStringLimits(Double.parseDouble(model.getAmount())));
            else viewHolder.Amount.setText("N/A");
        } catch (NumberFormatException e) {
            e.printStackTrace();

        }


        if (CommonUtils.getMerchantImage(model.getBankName()) == -1) {
            try {
                loding.displayImage(((BaseActivity) mContext).dbHelper.getValue(DBConstants.BANKS, DBConstants.BANK_IMAGE,
                        DBConstants.BANK_NAME + " = '" + model.getBankName() + "'"), viewHolder.merchantImage, R.drawable.default_bank);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            viewHolder.merchantImage.setImageResource(CommonUtils.getMerchantImage(model.getBankName()));
        }

       /* try {
            viewHolder.merchantImage.setImageResource(model.getMerchantImage());
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        viewHolder.debitCard.setText("Debit card no. - " + model.getDebitCardNo());
        if (isSCreditType) {
            viewHolder.refreshAccount.setVisibility(View.GONE);
            viewHolder.outstandingLinear.setVisibility(View.VISIBLE);
            viewHolder.outStandingAmountTV.setText("N/A");
            if (model.getOutStandingAmount() > 0)
                viewHolder.outStandingAmountTV.setText(CommonUtils.DoubleToStringLimits(model.getOutStandingAmount()));


        }


        if (showDebitCards) {
            int linkedStatus = -1;
            if (model.getDebitCardNo() != null) {

                try {
                    linkedStatus = Integer.parseInt(model.getStatus() + "");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    linkedStatus = -1;
                }

                if (!model.getDebitCardNo().isEmpty()) {
                    switch (linkedStatus) {

                        case 0:
                            viewHolder.linkCard.setChecked(false);
                            viewHolder.debit_card_link_container.setVisibility(View.VISIBLE);
                            break;

                        case 1:
                            viewHolder.linkCard.setChecked(true);
                            viewHolder.debit_card_link_container.setVisibility(View.VISIBLE);
                            break;

                        case 2:
                            viewHolder.linkCard.setChecked(false);
                            viewHolder.debit_card_link_container.setVisibility(View.GONE);
                            viewHolder.fixedDebitCardLinear.setVisibility(View.VISIBLE);
                            viewHolder.fixedDebitNOTV.setText(model.getDebitCardNo());
                            break;
                        default:
                            viewHolder.debit_card_link_container.setVisibility(View.GONE);
                            break;
                    }

                }
            } else viewHolder.debit_card_link_container.setVisibility(View.INVISIBLE);
        } else
            viewHolder.debit_card_link_container.setVisibility(View.INVISIBLE);

        viewHolder.linkCard.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {

                new AlertDialog.Builder(mContext)
                        .setTitle("Link debit card")
                        .setMessage("Are you sure you want to Link it?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete


                                if (b) {
                                    ContentValues values = new ContentValues();
                                    values.put(DBConstants.LINKED_ACCOUNT_ID, model.getId());
                                    values.put(DBConstants.LINKED_ACCOUNT_STATUS, 1);

                                    String debitCardId = ((BaseActivity) mContext).dbHelper.getValue(DBConstants.DEBIT_CARDS, DBConstants.ID, DBConstants.CARD_NUMBER + " = '" + model.getDebitCardNo() + "'");
                                    ((BaseActivity) mContext).dbHelper.updateRecords(DBConstants.DEBIT_CARDS, values, DBConstants.ID + " = " + debitCardId, null);
                                } else {

                                    ContentValues values = new ContentValues();
                                    values.put(DBConstants.LINKED_ACCOUNT_ID, 0);
                                    values.put(DBConstants.LINKED_ACCOUNT_STATUS, 0);

                                    String debitCardId = ((BaseActivity) mContext).dbHelper.getValue(DBConstants.DEBIT_CARDS, DBConstants.ID, DBConstants.CARD_NUMBER + " = '" + model.getDebitCardNo() + "'");
                                    ((BaseActivity) mContext).dbHelper.updateRecords(DBConstants.DEBIT_CARDS, values, DBConstants.ID + " = " + debitCardId, null);

                                }

                                //  viewHolder.linkCard.setChecked(true);
                                dialog.dismiss();
                                new RefreshAccount(mContext, dbHelper).execute();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                // viewHolder.linkCard.setChecked(false);
                                ((MainActivity) mContext).onFragmentAdd(new Account(), Constants.ACCOUNTS);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        viewHolder.refreshAccount.setOnClickListener(new View.OnClickListener() {
            String contactNo;

            @Override
            public void onClick(View v) {
                try {
                    contactNo = dbHelper.getValue(DBConstants.BANKS, DBConstants.BANK_CONTACT_NO,
                            DBConstants.BANK_NAME + " = '" + model.getBankName() + "'");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.update_account);
                dialog.setCancelable(false);

                final RadioButton rb_Call = (RadioButton) dialog.findViewById(R.id.missedCallRB);
                rb_Call.setChecked(true);
                final RadioButton rb_SMS = (RadioButton) dialog.findViewById(R.id.smsRB);
                rb_SMS.setChecked(false);

                final TextView txt_updateType = (TextView) dialog.findViewById(R.id.descriptionTextView);
                TextView txt_bankNo = (TextView) dialog.findViewById(R.id.numberTextView);
                txt_bankNo.setText(contactNo);   // SELECT ACCOUNT BANK NUMBER

                final TextView txt_smsBody = (TextView) dialog.findViewById(R.id.balTextView);
                final TextView txt_Note = (TextView) dialog.findViewById(R.id.noteTextView);

                Button btn_ok = (Button) dialog.findViewById(R.id.okButton);
                try {
                    if (contactNo == null) {

                        btn_ok.setEnabled(false);
                    } else {


                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (rb_Call.isChecked()) {

                                    try {
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:" + contactNo.trim()));
                                        mContext.startActivity(callIntent);
                                        dialog.dismiss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (rb_SMS.isChecked()) {
                                    try {
                                        SmsManager sm = SmsManager.getDefault();
                                        String number = contactNo;
                                        sm.sendTextMessage(number, null, txt_smsBody.getText().toString().trim(), null, null);
                                        Toast.makeText(mContext, "SMS sent.", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    } catch (IllegalArgumentException e) {
                                        e.printStackTrace();
                                        Toast.makeText(mContext.getApplicationContext(), "Sending SMS failed.", Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(mContext.getApplicationContext(), "Sending SMS failed.", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                Button btn_cancel = (Button) dialog.findViewById(R.id.cancelButton);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                rb_Call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rb_Call.setChecked(true);
                        rb_SMS.setChecked(false);
                        txt_updateType.setText(R.string.missed_call_description);
                        txt_smsBody.setVisibility(View.GONE);
                        txt_Note.setText(R.string.missed_call_note);
                    }
                });

                rb_SMS.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rb_SMS.setChecked(true);
                        rb_Call.setChecked(false);
                        txt_updateType.setText(R.string.sms_description);
                        txt_smsBody.setVisibility(View.VISIBLE);
                        txt_smsBody.setText(R.string.label_sms);
                        txt_Note.setText(R.string.sms_note);
                    }
                });

                dialog.show();
            }
        });

        return convertView;
    }

    class ViewHolder {
        CheckBox linkCard;
        RelativeLayout debit_card_link_container;
        ImageView merchantImage, refreshAccount;
        TextView bankName, accNo, date, Amount, debitCard, fixedDebitNOTV, outStandingAmountTV;
        LinearLayout fixedDebitCardLinear, outstandingLinear;

    }
}

package com.faayda.AsynTask;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.faayda.BaseActivity;
import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.fragment.Account;
import com.faayda.processor.SMSReader;
import com.faayda.utils.Constants;

/**
 * Created by vinove_2 on 13-09-2015.
 */
public class RefreshAccount extends AsyncTask<Void, Void, Void> {

    Context context;
    private ProgressDialog dialog;
    DBHelper dbHelper;

    public RefreshAccount(Context context,DBHelper dbHelper) {

        this.context = context;
        this.dbHelper=dbHelper;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(context, R.style.MyTheme);
        dialog.setCancelable(false);
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        String creditCardId = ((BaseActivity) context).dbHelper
                .getValue(DBConstants.ACCOUNT_TYPE, DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "'");
        ContentValues values = new ContentValues();
        values.put(DBConstants.ACCOUNT_BALANCE, 0);
        ((BaseActivity) context).dbHelper.updateRecords(DBConstants.USER_ACCOUNTS, values,
                DBConstants.ACCOUNT_TYPE_ID + " != " + creditCardId, null);

        new SMSReader().refreshAccountWithTransaction(context,dbHelper);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();


        ((MainActivity) context).onFragmentAdd(new Account(), Constants.ACCOUNTS);
    }
}

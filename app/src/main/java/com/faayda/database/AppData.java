package com.faayda.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.faayda.utils.Constants;

/**
 * Created by Aashutosh @ Vinove on 8/4/2015.
 */
public class AppData extends SQLiteOpenHelper {


    public AppData(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table1 = DBConstants.CREATE_STATEMENT
                + DBConstants.USER_DETAILS + " ("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.USER_ID + " INTEGER, "
                + DBConstants.USER_NAME + " VARCHAR(255), "
                + DBConstants.USER_EMAIL + " VARCHAR(255), "
                + DBConstants.USER_MOBILE + " INTEGER, "
                + DBConstants.DATE_MODIFIED + " BIGINT)";

        String table2 = DBConstants.CREATE_STATEMENT
                + DBConstants.BANKS + " ("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.BANK_NAME + " INTEGER, "
                + DBConstants.BANK_IMAGE + " VARCHAR(255), "
                + DBConstants.BANK_CONTACT_NO + " INTEGER, "
                + DBConstants.DATE_MODIFIED + " BIGINT)";

        String table3 = DBConstants.CREATE_STATEMENT
                + DBConstants.DEBIT_CARDS + " ("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.CARD_NUMBER + " VARCHAR(255), "
                + DBConstants.BANK_ID + " INTEGER, "
                + DBConstants.LINKED_ACCOUNT_ID + " INTEGER, "
                + DBConstants.LINKED_ACCOUNT_STATUS + " INTEGER, "
                + DBConstants.DATE_MODIFIED + " BIGINT)";

        String table4 = DBConstants.CREATE_STATEMENT
                + DBConstants.BILL_RECORDS + " ("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.BILLER_ID + " INTEGER, "
                + DBConstants.BILL_AMOUNT + " INTEGER, "
                + DBConstants.BILL_DUE_DATE + " VARCHAR(255), "
                + DBConstants.BILL_PAID_STATUS + " INTEGER, "
                + DBConstants.BILL_TRANSACTION_ID + " INTEGER, "
                + DBConstants.SMS_ID + " INTEGER, "
                + DBConstants.DATE_MODIFIED + " BIGINT)";

        //BillersModel or Merchants will reside in this table
        String table5 = DBConstants.CREATE_STATEMENT
                + DBConstants.BILLER_LIST + " ("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.BILLER_NAME + " VARCHAR(255), "
                + DBConstants.CATEGORY_ID + " INTEGER, "
                + DBConstants.DATE_MODIFIED + " BIGINT)";

        String table6 = DBConstants.CREATE_STATEMENT
                + DBConstants.USER_ACCOUNTS + " ("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.ACCOUNT_NUMBER + " VARCHAR(255), "
                + DBConstants.ACCOUNT_NICK_NAME + " VARCHAR(255), "
                + DBConstants.ACCOUNT_BANK_ID + " INTEGER, "
                + DBConstants.ACCOUNT_BALANCE + " DOUBLE, "
                + DBConstants.OUTSTANDING_AMOUNT + " DOUBLE, "
                + DBConstants.ACCOUNT_DATE + " BIGINT, "
                + DBConstants.SMS_ID + " BIGINT, "
                + DBConstants.ACCOUNT_TYPE_ID + " INTEGER , "
                + DBConstants.ACCOUNT_CREATED_BY + " INTEGER DEFAULT 1, " // 1 if account read from msg and 2 for user added manually

                + DBConstants.DATE_MODIFIED + " BIGINT)";

        String table7 = DBConstants.CREATE_STATEMENT
                + DBConstants.TRANSACTION_TYPE + " ("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.TRANSACTION_NAME + " VARCHAR(255), "
                + DBConstants.DATE_MODIFIED + " BIGINT)";

        String table8 = DBConstants.CREATE_STATEMENT
                + DBConstants.TRANSACTIONS + " ("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
              /*  + DBConstants.TRANSACTION_STATUS + " INTEGER DEFAULT 1, "*/
                + DBConstants.TRANSACTION_AMOUNT + " DOUBLE, "
                + DBConstants.TRANSACTION_ACCOUNT_ID + " INTEGER, "
                + DBConstants.TRANSACTION_TYPE_ID + " INTEGER, "
                + DBConstants.TRANSACTION_DATE + " BIGINT, "
                + DBConstants.BILLER_NAME + " VARCHAR(255), "

                + DBConstants.SMS_ID + " INTEGER, "
                + DBConstants.CARD_NUMBER + " INTEGER, "

                + DBConstants.TRANSACTION_BILLER_ID + " INTEGER, "
                + DBConstants.CATEGORY_ID + " INTEGER, "
                + DBConstants.DATE_MODIFIED + " BIGINT)";
/*

        String table9 = DBConstants.CREATE_STATEMENT
                + DBConstants.SMS + " ("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                + DBConstants.SMS_SENDER + " VARCHAR(255), "
                + DBConstants.SMS_BODY + " VARCHAR(255), "
                + DBConstants.SMS_DATE + " BIGINT)";
*/


        String table9 = DBConstants.CREATE_STATEMENT
                + DBConstants.SMS + " ("
                + DBConstants.ID + " INTEGER PRIMARY KEY  , "
                + DBConstants.SMS_STATUS + " INTEGER DEFAULT 1, " // 1 for active msg and 0 for deleted transaction
                + DBConstants.SMS_SENDER + " VARCHAR(255), "
                + DBConstants.SMS_BODY + " VARCHAR(255), "
                + DBConstants.SMS_DATE + " BIGINT, "
                + DBConstants.TIME_KEY
                + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";



        /*String table10 = DBConstants.CREATE_STATEMENT
                + DBConstants.CASH_MANAGEMENT + " ("
                + DBConstants.ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.CASH_IN_HAND + " VARCHAR(255), "
                + DBConstants.DATE_MODIFIED + " BIGINT)";*/

        String table10 = DBConstants.CREATE_STATEMENT
                + DBConstants.BILLER_SMS_CODES + "("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.BILLER_ID + " INTEGER, "
                + DBConstants.BILLER_SMS_SENDER + " VARCHAR(255))";

        String table11 = DBConstants.CREATE_STATEMENT
                + DBConstants.CATEGORIES + "("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.CATEGORY_TITLE + " VARCHAR(255), "
                + DBConstants.CATEGORY_IMAGE + " VARCHAR(255), "
                + DBConstants.DATE_MODIFIED + " BIGINT)";

        String table12 = DBConstants.CREATE_STATEMENT
                + DBConstants.BANK_SMS_CODES + "("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.BANK_ID + " INTEGER, "
                + DBConstants.BANK_SMS_SENDER + " VARCHAR(255))";

        String table13 = DBConstants.CREATE_STATEMENT
                + DBConstants.SMS_TEMPLATES_TAG + "("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.SMS_TAG_TITLE + " VARCHAR(255))";

        String table14 = DBConstants.CREATE_STATEMENT
                + DBConstants.SMS_TEMPLATES + "("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.SMS_TAG_ID + " INTEGER, "
                + DBConstants.SMS_TAG_START + " VARCHAR(255), "
                + DBConstants.SMS_TAG_END + " VARCHAR(255))";

        String table15 = DBConstants.CREATE_STATEMENT
                + DBConstants.ACCOUNT_TYPE + "("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.ACCOUNT_TYPE_TITLE + " VARCHAR(255))";

        String table16 = DBConstants.CREATE_STATEMENT
                + DBConstants.NOTIFICATION + "("
                + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.NOTIFICATION_TITLE + " VARCHAR(255), "
                + DBConstants.NOTIFICATION_BODY + " VARCHAR(255))";

        String[] tables = new String[]{table1, table2, table3, table4, table5, table6, table7, table8, table9, table10, table11, table12, table13, table14, table15, table16};
        for (String table : tables) {
            db.execSQL(table);
        }

        //Insert default values
        insertDefaultValues(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            //TODO: Do the upgrade changes required
        }
        onCreate(db);
    }

    private void insertDefaultValues(SQLiteDatabase db) {
        ContentValues contentValues = null, contentValues1 = null;
        long id;
        String[] transactionTypes = new String[]{Constants.CREDIT, Constants.DEBIT, Constants.ATM_WDL, Constants.EXPENSE, Constants.CASH_EXPENSE, Constants.FUND_TRANSFER, Constants.CREDIT_CARD_EXPENSE, Constants.DEBIT_CARD_EXPENSE, Constants.REFUND};
        for (String string : transactionTypes) {
            contentValues = new ContentValues();
            contentValues.put(DBConstants.TRANSACTION_NAME, string);
            contentValues.put(DBConstants.DATE_MODIFIED, System.currentTimeMillis());

            db.insert(DBConstants.TRANSACTION_TYPE, null, contentValues);
        }


        String[] accountTypes = new String[]{Constants.PERSONAL, Constants.BUSINESS, Constants.CREDIT_CARD, Constants.CREDIT_CARD_BUSINESS};
        /* note

           personal is equal to : personal debit card
           business is equal to : business debit card
          credit card is equal to : personal credit card
          credit card business  is equal to business debit card
         */

        for (String string : accountTypes) {
            contentValues = new ContentValues();
            contentValues.put(DBConstants.ACCOUNT_TYPE_TITLE, string);

            db.insert(DBConstants.ACCOUNT_TYPE, null, contentValues);
        }

        String[] smsTemplateTags = new String[]{Constants.DEBITED_AMOUNT_TAG, Constants.CREDITED_AMOUNT_TAG, Constants.AVAILABLE_BALANCE_TAG,
                Constants.ACCOUNT_NUMBER_TAG, Constants.BILL_AMOUNT_TAG, Constants.BILL_DUE_DATE_TAG, Constants.PAYMENT_MADE_TAG, Constants.ATM_WDL_TAG, Constants.FUND_TRANSFER_TAG, Constants.CREDIT_CARD_TAG, Constants.CREDIT_CARD_TRANS_TAG, Constants.DEBIT_CARD_TAG, Constants.DEBIT_CARD_TRANS_TAG};
        for (String string : smsTemplateTags) {
            contentValues = new ContentValues();
            contentValues.put(DBConstants.SMS_TAG_TITLE, string);
            db.insert(DBConstants.SMS_TEMPLATES_TAG, null, contentValues);
        }
    }
}

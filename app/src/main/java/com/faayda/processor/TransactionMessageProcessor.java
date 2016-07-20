package com.faayda.processor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;
import com.faayda.utils.AppConstant;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vinove on 6/8/15.
 */
public final class TransactionMessageProcessor {

    DBHelper dbHelper;
    Context mContext;
/*    private static final String[] formats = {"yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd HH:mm:ss", "MM/dd/yyyy HH:mm:ss",
            "MM/dd/yyyy'T'HH:mm:ss.SSS'Z'", "MM/dd/yyyy'T'HH:mm:ss.SSSZ",
            "MM/dd/yyyy'T'HH:mm:ss.SSS", "MM/dd/yyyy'T'HH:mm:ssZ",
            "MM/dd/yyyy'T'HH:mm:ss", "yyyy:MM:dd HH:mm:ss", "yyyyMMdd"};*/

    private static final String[] formats = {"dd-MM-yyyy", "dd/MM/yyyy", "dd/MMM/yyyy", "dd-MMM-yyyy"};

    Matcher creditMatcher, debitMatcher, cashWdlMatcher, avlBalMatcher, creditCardMatcher, debitCardMatcher, expenseMatcher, billMatcher, transferMatcher, accountNoMatcher;

    Pattern creditPattern = Pattern.compile(AppConstant.CREDIT_REGEX, Pattern.CASE_INSENSITIVE);
    Pattern debitPattern = Pattern.compile(AppConstant.DEBIT_REGIX, Pattern.CASE_INSENSITIVE);
    Pattern cashWdlPattern = Pattern.compile(AppConstant.CASH_WITHDRAWAL_REGEX, Pattern.CASE_INSENSITIVE);
    Pattern avlBalPattern = Pattern.compile(AppConstant.AVL_BAL_REGEX, Pattern.CASE_INSENSITIVE);
    Pattern creditCardPattern = Pattern.compile(AppConstant.CREDIT_CARD_REGEX, Pattern.CASE_INSENSITIVE);
    Pattern debitCardPattern = Pattern.compile(AppConstant.DEBIT_CARD_REGEX, Pattern.CASE_INSENSITIVE);
    Pattern expensePattern = Pattern.compile(AppConstant.EXPENSE_REGEX, Pattern.CASE_INSENSITIVE);
    Pattern billPattern = Pattern.compile(AppConstant.BILL_REGEX, Pattern.CASE_INSENSITIVE);
    Pattern transferPattern = Pattern.compile(AppConstant.FUND_TRANSFER_REGEX, Pattern.CASE_INSENSITIVE);
    Pattern accountNoPattern = Pattern.compile(AppConstant.ACCOUNT_NO_REGEX, Pattern.CASE_INSENSITIVE);

    private static TransactionMessageProcessor transactionMessageProcessor;

    public TransactionMessageProcessor(Context mContext) {
        this.mContext = mContext;
        dbHelper = new DBHelper(mContext);
        dbHelper.open();
    }

    public static TransactionMessageProcessor getInstance(Context mContext) {

        if (transactionMessageProcessor == null)
            transactionMessageProcessor = new TransactionMessageProcessor(mContext);
        return transactionMessageProcessor;
    }

   /* public void readmsg(Cursor c, Context ctx, boolean isFirstCall) {

        int position = c.getCount() - 1;
        if (c.moveToLast() && isFirstCall) {

            while (position >= 0) {
                String smsBody = c.getString(c.getColumnIndexOrThrow(AppConstant.INBOX_BODY)).toString();
                if (isTransactionMessage(smsBody)) {
                    Double expenseAmount = getExpenseAmountFromMsg(smsBody);
                    String timeStamp = c.getString(c.getColumnIndexOrThrow(AppConstant.INBOX_DATE)).toString();
                    Date smsDate = CommonUtils.getDateFromTimestamp(Long.valueOf(timeStamp));
                    String where = c.getString(c.getColumnIndexOrThrow(AppConstant.INBOX_ADDRESS)).toString();
                    if (expenseAmount == 0) {
                        position--;
                        c.moveToPosition(position);
                        continue;
                    }

                    createData(ctx, smsBody);
                }
                position--;
                c.moveToPosition(position);
            }
        }
    }

    public void createData(Context ctx, String smsBody) {
        Matcher creditMatcher = creditPattern.matcher(smsBody);
        Matcher debitMatcher = debitPattern.matcher(smsBody);
        Matcher cashWdlMatcher = cashWdlPattern.matcher(smsBody);
        Matcher avlBalMatcher = avlBalPattern.matcher(smsBody);


        if (creditMatcher.find()) {
            if (avlBalMatcher.find()) {
                Double avlBalAmount = getAccountBalance(smsBody);
            }

        } else if (debitMatcher.find() || cashWdlMatcher.find()) {

            if (avlBalMatcher.find()) {
                Double avlBalAmount = getAccountBalance(smsBody);
            }

        } else if (cashWdlMatcher.find()) {

            if (avlBalMatcher.find()) {
                Double avlBalAmount = getAccountBalance(smsBody);
            }
        }
    }*/

    public boolean checkTransactionMessage(String smsBody) {
        creditMatcher = creditPattern.matcher(smsBody);
        debitMatcher = debitPattern.matcher(smsBody);
        cashWdlMatcher = cashWdlPattern.matcher(smsBody);
        avlBalMatcher = avlBalPattern.matcher(smsBody);
        creditCardMatcher = creditCardPattern.matcher(smsBody);
        debitCardMatcher = debitCardPattern.matcher(smsBody);
        expenseMatcher = expensePattern.matcher(smsBody);
        billMatcher = billPattern.matcher(smsBody);
        transferMatcher = transferPattern.matcher(smsBody);
        accountNoMatcher = accountNoPattern.matcher(smsBody);
        if (creditMatcher.find() || debitMatcher.find() || cashWdlMatcher.find() || avlBalMatcher.find() || creditCardMatcher.find() || expenseMatcher.find() || billMatcher.find() || debitCardMatcher.find() || transferMatcher.find() || accountNoMatcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * Check if message contains any amount
     *
     * @param smsBody
     * @return
     */
    public boolean isTransactionMessage(String smsBody) {
        Pattern r = Pattern.compile(AppConstant.AMOUNT_REGEX);
        Matcher m = r.matcher(smsBody);
        if (m.find() && (CommonUtils.isNumeric(CommonUtils.subStringBetween(smsBody, "INR", " ")) || CommonUtils.isNumeric(CommonUtils.subStringBetween(smsBody, "Rs.", " ")) || CommonUtils.isNumeric(CommonUtils.subStringBetween(smsBody, "Rs", " "))))
            return true;
        return false;
    }

    /**
     * SMS type
     *
     * @param mContext
     * @param smsBody
     * @return
     */
    public String smsType(Context mContext, String smsBody) {
        creditMatcher = creditPattern.matcher(smsBody);
        debitMatcher = debitPattern.matcher(smsBody);
        cashWdlMatcher = cashWdlPattern.matcher(smsBody);
        avlBalMatcher = avlBalPattern.matcher(smsBody);
        creditCardMatcher = creditCardPattern.matcher(smsBody);
        debitCardMatcher = debitCardPattern.matcher(smsBody);
        expenseMatcher = expensePattern.matcher(smsBody);
        billMatcher = billPattern.matcher(smsBody);
        transferMatcher = transferPattern.matcher(smsBody);
        accountNoMatcher = accountNoPattern.matcher(smsBody);

        String messageType = "";

/*        boolean ma1=transferMatcher.find();
        boolean ma2=creditMatcher.find();
        boolean ma3=debitCardMatcher.find();
        boolean ma4=debitMatcher.find();*/
        if (transferMatcher.find()) return Constants.FUND_TRANSFER;
        if (cashWdlMatcher.find()) return Constants.ATM_WDL;
        if (creditMatcher.find()) return Constants.CREDIT;
        if (debitMatcher.find()) return Constants.DEBIT;
        if (creditCardMatcher.find()) return Constants.CREDIT_CARD_EXPENSE;
        if (debitCardMatcher.find()) return Constants.DEBIT_CARD_EXPENSE;

        if (expenseMatcher.find()) return Constants.EXPENSE;
        if (billMatcher.find()) return Constants.BILL;

/*

        if (creditMatcher.find()) {
            messageType = Constants.CREDIT;//1
        } else if (debitMatcher.find()) {//&& !transferMatcher.find()
            messageType = Constants.DEBIT;//2
        } else if (cashWdlMatcher.find()) {
            messageType = Constants.ATM_WDL;//3
            // } else if (avlBalMatcher.find()) {
            // messageType = Constants.AVL_BAL;
        } else if (creditCardMatcher.find()) {
            messageType = Constants.CREDIT_CARD_EXPENSE;//7
        } else if (debitCardMatcher.find()) {
            messageType = Constants.DEBIT_CARD_EXPENSE;//8
        } else if (expenseMatcher.find()) {
            messageType = Constants.EXPENSE;//6
        } else if (billMatcher.find()) {
            messageType = Constants.BILL;//5
      */
/*  } else if (transferMatcher.find()) {
            messageType = Constants.FUND_TRANSFER;//4*//*

            //  } else if (accountNoMatcher.find()) {
            //   messageType = Constants.ACC_NO;
        }
*/

        return messageType;
    }

    /**
     * Any amount credited to the account
     *
     * @param smsBody
     * @return
     */
    public Double getCreditedAmount(String smsBody) {
        String[] start, end;
        long id = Long.parseLong(dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.CREDITED_AMOUNT_TAG + "'"));
        start = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_START, DBConstants.SMS_TAG_ID + " = " + id, null);
        end = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_END, DBConstants.SMS_TAG_ID + " = " + id, null);

        Double amount = 0.0;

        for (int i = 0; i < start.length; i++) {
            amount = Double.valueOf(CommonUtils.subStringBetween(smsBody, start[i], end[i]));
            if (amount > 0) {
                break;
            }
        }
        amount = amount > 0 ? amount : 0;
        return amount;
    }

    /**
     * Amount Withdrawl from ATM
     *
     * @param smsBody
     * @return
     */
    public Double getAtmWdlAmount(String smsBody) {
        String[] start, end;
        long id = Long.parseLong(dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.ATM_WDL_TAG + "'"));
        start = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_START, DBConstants.SMS_TAG_ID + " = " + id, null);
        end = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_END, DBConstants.SMS_TAG_ID + " = " + id, null);

        Double amount = 0.0;

        for (int i = 0; i < start.length; i++) {
            amount = Double.valueOf(CommonUtils.subStringBetween(smsBody, start[i], end[i]));
            if (amount > 0) {
                break;
            }
        }
        amount = amount > 0 ? amount : 0;
        return amount;
    }

    /**
     * Any fund transfer done
     *
     * @param smsBody
     * @return
     */
    public Double getFundTransfer(String smsBody) {

        String[] start, end;
        long id = Long.parseLong(dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.FUND_TRANSFER_TAG + "'"));
        start = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_START, DBConstants.SMS_TAG_ID + " = " + id, null);
        end = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_END, DBConstants.SMS_TAG_ID + " = " + id, null);

        Double amount = 0.0;

        for (int i = 0; i < start.length; i++) {
            amount = Double.valueOf(CommonUtils.subStringBetween(smsBody, start[i], end[i]));
            if (amount > 0) {
                break;
            }
        }
        amount = amount > 0 ? amount : 0;
        return amount;
    }

    /**
     * Get bill amount
     *
     * @param smsBody
     * @return
     */
    public Double getBillAmount(String smsBody) {

        String[] start, end;
        long id = Long.parseLong(dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.BILL_AMOUNT_TAG + "'"));
        start = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_START, DBConstants.SMS_TAG_ID + " = " + id, null);
        end = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_END, DBConstants.SMS_TAG_ID + " = " + id, null);

        Double amount = 0.0;


        for (int i = 0; i < start.length; i++) {

            String value = CommonUtils.subStringBetween(smsBody, start[i], end[i]).trim();
            amount = Double.valueOf(CommonUtils.subStringBetween(smsBody, start[i], end[i]).trim());
            if (amount > 0) {
                break;
            }
        }
        amount = amount > 0 ? amount : 0;
        return amount;
    }

    /**
     * Get bill due date
     *
     * @param smsBody
     * @return
     */
    public String getBilldueDate(String smsBody) {
        String[] start, end;
        long id = Long.parseLong(dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.BILL_DUE_DATE_TAG + "'"));
        start = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_START, DBConstants.SMS_TAG_ID + " = " + id, null);
        end = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_END, DBConstants.SMS_TAG_ID + " = " + id, null);

        String billDueDate = null;

        for (int i = 0; i < start.length; i++) {

            String value = CommonUtils.subStringBetween(smsBody, start[i], end[i]).trim();

            if (TransactionMessageProcessor.isDateString(value))
                billDueDate = value;

        }

        // if not find then check it at last
        if (billDueDate == null)
            for (int i = 0; i < start.length; i++) {

                if (smsBody.toLowerCase().contains(start[i].toLowerCase())) {
                    int startPos = smsBody.toLowerCase().indexOf(start[i].toLowerCase()) + start[i].toLowerCase().length();
                    String dateget = smsBody.substring(startPos, smsBody.length()).trim();
                    if (TransactionMessageProcessor.isDateString(dateget))
                        return dateget;

                }

            }

        return billDueDate;
    }

    /**
     * Transaction amount via Credit Card
     *
     * @param smsBody
     * @return
     */
    public Double getCreditCardAmount(String smsBody) {
        String[] start, end;
        long id = Long.parseLong(dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.CREDIT_CARD_TRANS_TAG + "'"));
        start = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_START, DBConstants.SMS_TAG_ID + " = " + id, null);
        end = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_END, DBConstants.SMS_TAG_ID + " = " + id, null);

        Double amount = 0.0;

        for (int i = 0; i < start.length; i++) {
            amount = Double.valueOf(CommonUtils.subStringBetween(smsBody, start[i], end[i]));
            if (amount > 0) {
                break;
            }
        }
        amount = amount > 0 ? amount : 0;
        return amount;
    }

    public Double getDeditCardAmount(String smsBody) {
        String[] start, end;
        long id = Long.parseLong(dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.DEBIT_CARD_TRANS_TAG + "'"));
        start = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_START, DBConstants.SMS_TAG_ID + " = " + id, null);
        end = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_END, DBConstants.SMS_TAG_ID + " = " + id, null);

        Double amount = 0.0;

        for (int i = 0; i < start.length; i++) {
            amount = Double.valueOf(CommonUtils.subStringBetween(smsBody, start[i], end[i]));
            if (amount > 0) {
                break;
            }
        }
        amount = amount > 0 ? amount : 0;
        return amount;
    }


    /**
     * Get amount debited from your account
     *
     * @param smsBody
     * @return
     */
    public Double getDebitedAmount(String smsBody) {
        String[] start, end;
        long id = Long.parseLong(dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.DEBITED_AMOUNT_TAG + "'"));
        start = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_START, DBConstants.SMS_TAG_ID + " = " + id, null);
        end = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_END, DBConstants.SMS_TAG_ID + " = " + id, null);

        Double amount = 0.0;

        for (int i = 0; i < start.length; i++) {
            amount = Double.valueOf(CommonUtils.subStringBetween(smsBody, start[i], end[i]));
            if (amount > 0) {
                break;
            }
        }
        amount = amount > 0 ? amount : 0;
        return amount;
    }

    public String getDebitCardNumber(String smsBody) {


//        debitCardMatcher.mat

//        if (debitCardMatcher.find()) {
        String[] start, end;
        long id = Long.parseLong(dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.DEBIT_CARD_TAG + "'"));
        start = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_START, DBConstants.SMS_TAG_ID + " = " + id, null);
        end = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_END, DBConstants.SMS_TAG_ID + " = " + id, null);

        String debitCardNumber = "";

        for (int i = 0; i < start.length; i++) {
            debitCardNumber = CommonUtils.subStringBetween(smsBody, start[i], end[i], true).trim();
            if (debitCardNumber.length() > 4)
                debitCardNumber = debitCardNumber.substring(debitCardNumber.length() - 4);

            if (checkIfStringContainsNumeric(debitCardNumber))

                return debitCardNumber;

        }

//        }
        return "0";
    }


    /**
     * Any amount paid as payment
     *
     * @param smsBody
     * @return
     */
    public Double getExpenseAmountFromMsg(String smsBody) {

        String[] start, end;

        long id = Long.parseLong(dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.PAYMENT_MADE_TAG + "'"));
        start = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_START, DBConstants.SMS_TAG_ID + " = " + id, null);
        end = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_END, DBConstants.SMS_TAG_ID + " = " + id, null);

        Double amount = 0.0;

        for (int i = 0; i < start.length; i++) {
            amount = Double.valueOf(CommonUtils.subStringBetween(smsBody, start[i], end[i]));
            if (amount > 0) {
                break;
            }
        }


        amount = amount > 0 ? amount : 0;
        return amount;
    }

    /**
     * Get available balance in account
     *
     * @param smsBody
     * @return
     */
    public Double getAccountBalance(String smsBody) {
        String[] start, end;

        long id = Long.parseLong(dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.AVAILABLE_BALANCE_TAG + "'"));
        start = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_START, DBConstants.SMS_TAG_ID + " = " + id, null);
        end = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_END, DBConstants.SMS_TAG_ID + " = " + id, null);

        Double amount = 0.0;

        for (int i = 0; i < start.length; i++) {
            String value = CommonUtils.subStringBetween(smsBody, start[i], end[i]);

            try {
                amount = Double.valueOf(value);
                if (amount > 0) {
                    break;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        amount = amount != null ? amount : -1;
        return amount;
    }

    public Double getOutstandingAmount(String smsBody) {


/*        long id = 0;
        try {
            if (dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.OUTSTANDING_AMOUNT_TAG + "'") != null)
                id = Long.parseLong(dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.OUTSTANDING_AMOUNT_TAG + "'"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (id == 0)
            return 0d;*/
        String[] start = {"curr o/s - Rs."};//dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_START, DBConstants.SMS_TAG_ID + " = " + id, null);
        String[] end = {"."};//dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_END, DBConstants.SMS_TAG_ID + " = " + id, null);

        Double amount = 0.0;

        for (int i = 0; i < start.length; i++) {
            String value = CommonUtils.subStringBetween(smsBody, start[i], end[i]);

            try {
                amount = Double.valueOf(value);
                if (amount > 0) {
                    break;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        amount = amount != null ? amount : -1;
        return amount;
    }

    /**
     * Get account number
     *
     * @param smsBody
     * @return
     */

    /*Thank you for using Debit Card ending 8670 for Rs.200.00 in Delhi at Delhi Metro Rail Corpo on 2015-07-12:14:47:12  Avl bal: Rs.141.22
      An amount of Rs.1.00 has been debited from your account number XXXX2708 for NEFT txn done using HDFC Bank NetBanking*/
    public String getAccountNumber(String smsBody) {

        String[] start, end;
        long id = Long.parseLong(dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.ACCOUNT_NUMBER_TAG + "'"));
        start = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_START, DBConstants.SMS_TAG_ID + " = " + id, null);
        end = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_END, DBConstants.SMS_TAG_ID + " = " + id, null);

        String accountNumber = "";

        for (int i = 0; i < start.length; i++) {
            accountNumber = CommonUtils.subStringBetween(smsBody, start[i], end[i], true).trim();
            if (checkIfStringContainsNumeric(accountNumber)) {
                return accountNumber;
            }
        }


        return "0";
    }

    /**
     * Get credit card number
     *
     * @param smsBody
     * @return
     */
    public String getCreditCardNumber(String smsBody) {

        //    if (creditCardMatcher.find()) {
        String[] start, end;
        long id = 0;
        try {
            id = Long.parseLong(dbHelper.getValue(DBConstants.SMS_TEMPLATES_TAG, DBConstants.ID, DBConstants.SMS_TAG_TITLE + " like '" + Constants.CREDIT_CARD_TAG + "'"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        start = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_START, DBConstants.SMS_TAG_ID + " = " + id, null);
        end = dbHelper.getValues(false, DBConstants.SMS_TEMPLATES, DBConstants.SMS_TAG_END, DBConstants.SMS_TAG_ID + " = " + id, null);

        String creditCardNumber = "";

        for (int i = 0; i < start.length; i++) {
            try {
                creditCardNumber = CommonUtils.subStringBetween(smsBody, start[i], end[i], true).trim();
                String card_number = "";
                if (creditCardNumber.length() > 4)
                    card_number = creditCardNumber.substring(creditCardNumber.length() - 4);
                else card_number = creditCardNumber;
                if (checkIfStringContainsNumeric(card_number)) {
                    return card_number;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //  }
        return "0";
    }

    /**
     * Check if string contains numeric
     *
     * @param string
     * @return
     */
    public boolean checkIfStringContainsNumeric(String string) {
        Pattern p = Pattern.compile(AppConstant.ONLY_NUMERICS);
        Matcher m = p.matcher(string);
        return m.find();
    }

    /**
     * Get Id of account from Database
     *
     * @param mContext
     * @param smsCursor
     * @returnACCOUNT_BALANCE
     */
    public long getAccountId(Context mContext, Cursor smsCursor) {
        String accountNo = getAccountNumber(smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_BODY)));

        // String sms_id=smsCursor.getString(smsCursor.getColumnIndex(DBConstants.ID));

        //  Log.e("Sms id ",sms_id);


        if (accountNo.isEmpty()) { //|| accountNo.equalsIgnoreCase("0")
            return 0;
        }
        int count = (dbHelper.getTableRecordsCount(DBConstants.USER_ACCOUNTS, null, DBConstants.ACCOUNT_NUMBER + " like '" + accountNo + "'", null));
        if (count < 1) {

            Log.i("message :", smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_BODY)));
            return insertInUserAccouts(mContext, smsCursor, accountNo);
        } else {

            long accountId = Long.parseLong(dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ID, DBConstants.ACCOUNT_NUMBER + " like '" + accountNo + "'"));

            updateUserAccounts(mContext, smsCursor, accountId);
            return accountId;

        }
    }

    /**
     * Get Id of credit card account from database
     *
     * @param mContext
     * @param smsCursor
     * @return
     */
    public long getCreditCardAccountId(Context mContext, Cursor smsCursor) {
        String creditCardNo = getCreditCardNumber(smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_BODY)));


        //    String sms_id=getCreditCardNumber(smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_ID)));

        //Log.e("Sms id ",sms_id);
        long accountId = 0;
        if (creditCardNo.isEmpty() || creditCardNo.equalsIgnoreCase("0")) {
            return 0;
        }
        int creditCardTypeId = Integer.parseInt((dbHelper.getValue(DBConstants.ACCOUNT_TYPE, DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "'")));

        int count = (dbHelper.getTableRecordsCount(DBConstants.USER_ACCOUNTS, null, DBConstants.ACCOUNT_NUMBER + " like '" + creditCardNo + "' AND " + DBConstants.ACCOUNT_TYPE_ID + "=" + creditCardTypeId, null));
        if (count < 1) {
            accountId = insertInUserAccouts(mContext, smsCursor, creditCardNo, true);
        } else {
            accountId = Long.parseLong((dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ID, DBConstants.ACCOUNT_NUMBER + " like '" + creditCardNo + "' AND " + DBConstants.ACCOUNT_TYPE_ID + "=" + creditCardTypeId)));
            updateUserAccounts(mContext, smsCursor, accountId);
            //return accountId;
        }

        double outStandingAmount = getOutstandingAmount(smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_BODY)));
        ContentValues values = new ContentValues();
        values.put(DBConstants.OUTSTANDING_AMOUNT, outStandingAmount);
        if (outStandingAmount > 0)
            dbHelper.updateRecords(DBConstants.USER_ACCOUNTS, values, DBConstants.ID + " = " + accountId, null);
        return accountId;
    }


    public void UpdateAmountBalance(Context mContext, Cursor smsCursor) {


        String smsSender = smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_SENDER));
        String smsBody = smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_BODY));
        String bankId = (dbHelper.getValue(DBConstants.BANK_SMS_CODES, DBConstants.BANK_ID, DBConstants.BANK_SMS_SENDER + " like '" + smsSender + "'"));

        String accountNumber = getAccountNumber(smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_BODY)));

        if (!accountNumber.equalsIgnoreCase("0")) {
            long accountId = 0;
            try {
                if (dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ID,
                        DBConstants.ACCOUNT_NUMBER + " like '" + accountNumber + "' ") != null)
                    accountId = Long.parseLong(dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ID,
                            DBConstants.ACCOUNT_NUMBER + " like '" + accountNumber + "' "));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (!(accountId <= 0))
                updateUserAccounts(mContext, smsCursor, accountId);// if account exist then update balance
            else insertInUserAccouts(mContext, smsCursor, accountNumber);

        } /*else {
            //  values.put(DBConstants.ACCOUNT_TYPE_ID, dbHelper
            // .getValue(DBConstants.ACCOUNT_TYPE, DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "'"));


            String creditCardId = dbHelper
                    .getValue(DBConstants.ACCOUNT_TYPE, DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "'");

            long accountId = 0;


            try {
                accountId = Long.parseLong(dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ID,
                        DBConstants.ACCOUNT_BANK_ID + " like '" + bankId + "' AND " + DBConstants.ACCOUNT_TYPE_ID + " NOT like '" + creditCardId + "'"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


            if (!(accountId <= 0))
                updateUserAccounts(mContext, smsCursor, accountId);// if account exist then update balance


        }*/

    }


    public long getDebitCardAccountId(Context mContext, Cursor smsCursor) {
        String debitCardNo = getDebitCardNumber(smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_BODY)));
        if (debitCardNo.isEmpty() || debitCardNo.equalsIgnoreCase("0")) {
            return 0;
        }
        String smsSender = smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_SENDER));
        String bankId = dbHelper.getValue(DBConstants.BANK_SMS_CODES, DBConstants.BANK_ID, DBConstants.BANK_SMS_SENDER + " like '" + smsSender + "'");
        String accountNumber = getAccountNumber(smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_BODY)));

        long accountId = 0;
        if (!accountNumber.equalsIgnoreCase("0")) {

            try {
                accountId = Long.parseLong(dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ID,
                        DBConstants.ACCOUNT_NUMBER + " like '" + accountNumber + "' "));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if ((accountId <= 0))
                accountId = insertInUserAccouts(mContext, smsCursor, accountNumber, false);

        } else
            try {
                if (dbHelper.getValue(DBConstants.DEBIT_CARDS, DBConstants.LINKED_ACCOUNT_ID,
                        DBConstants.CARD_NUMBER + " like '" + debitCardNo + "' AND  " + DBConstants.LINKED_ACCOUNT_STATUS + " like " + " '2'") != null)
                    accountId = Long.parseLong(dbHelper.getValue(DBConstants.DEBIT_CARDS, DBConstants.LINKED_ACCOUNT_ID,
                            DBConstants.CARD_NUMBER + " like '" + debitCardNo + "' AND  " + DBConstants.LINKED_ACCOUNT_STATUS + " like " + " '2'"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

//        if (accountId <= 0)
//          return 0;  // if account id not get then return


        int count_1 = dbHelper.getTableRecordsCount(DBConstants.DEBIT_CARDS, null, DBConstants.CARD_NUMBER + " = '" + debitCardNo + "'", null);

        ContentValues values = new ContentValues();
        if (count_1 <= 0) {


            values.put(DBConstants.BANK_ID, Long.parseLong(bankId));
            values.put(DBConstants.CARD_NUMBER, Long.parseLong(debitCardNo));
            values.put(DBConstants.DATE_MODIFIED, System.currentTimeMillis());
            if (accountId > 0) {
                values.put(DBConstants.LINKED_ACCOUNT_STATUS, 2);
                values.put(DBConstants.LINKED_ACCOUNT_ID, accountId);
            } else {
                if (dbHelper.getTableRecordsCount(DBConstants.USER_ACCOUNTS, null,
                        DBConstants.ACCOUNT_BANK_ID + " = '" + bankId + "'", null) == 1) {
                    values.put(DBConstants.LINKED_ACCOUNT_STATUS, 1);
                    accountId = Long.parseLong(dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ID,
                            DBConstants.ACCOUNT_BANK_ID + " = '" + bankId + "' "));
                    values.put(DBConstants.LINKED_ACCOUNT_ID, accountId);
                } else {
                    values.put(DBConstants.LINKED_ACCOUNT_ID, 0);
                    values.put(DBConstants.LINKED_ACCOUNT_STATUS, 0);
                }
            }

            dbHelper.insertContentVals(DBConstants.DEBIT_CARDS, values);
        } else {
            if (accountId > 0) {

                values.put(DBConstants.LINKED_ACCOUNT_STATUS, 2);
                values.put(DBConstants.LINKED_ACCOUNT_ID, accountId);

            } else {

                long creditCardId = 0;
                try {
                    creditCardId = Long.parseLong(
                            dbHelper.getValue(DBConstants.ACCOUNT_TYPE, DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "'"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                int count = 0;
                try {
                    count = dbHelper.getTableRecordsCount(DBConstants.USER_ACCOUNTS,
                            null, DBConstants.ACCOUNT_BANK_ID + " = " + Long.parseLong(bankId) + " AND " + DBConstants.ACCOUNT_TYPE_ID + " != " + creditCardId, null);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (count == 1) {
                    values.put(DBConstants.LINKED_ACCOUNT_STATUS, 1);
                    accountId = Long.parseLong(dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ID,
                            DBConstants.ACCOUNT_BANK_ID + " = " + Long.parseLong(bankId)));
                    values.put(DBConstants.LINKED_ACCOUNT_ID, accountId);
                }
            }

            values.put(DBConstants.DATE_MODIFIED, System.currentTimeMillis());
            dbHelper.updateRecords(DBConstants.DEBIT_CARDS, values, DBConstants.CARD_NUMBER + " = " + Long.parseLong(debitCardNo), null);

        }

        accountId = Long.parseLong(dbHelper.getValue(DBConstants.DEBIT_CARDS, DBConstants.LINKED_ACCOUNT_ID, DBConstants.CARD_NUMBER + " like '" + debitCardNo + "'"));


        updateUserAccounts(mContext, smsCursor, accountId);// if account exist then update balance
        return Long.parseLong(dbHelper.getValue(DBConstants.DEBIT_CARDS, DBConstants.LINKED_ACCOUNT_ID, DBConstants.CARD_NUMBER + " like '" + debitCardNo + "'"));


    }


    /**
     * Insert new account in user account table
     *
     * @param mContext
     * @param smsCursor
     * @param accountNo
     * @return
     */

    public long insertInUserAccouts(Context mContext, Cursor smsCursor, String accountNo) {
        return insertInUserAccouts(mContext, smsCursor, accountNo, false);
    }

    /**
     * Insert new account in user account table
     *
     * @param mContext
     * @param smsCursor
     * @param accountNo
     * @param creditCard
     * @return
     */
    public long insertInUserAccouts(Context mContext, Cursor smsCursor, String accountNo, boolean creditCard) {

        if (accountNo.equalsIgnoreCase("0")) {
            return 0;
        } else {


            String smsSender, tableName, columnName;
            String primaryId = "";
            String[] smsSenders;
            String sms_id = smsCursor.getString(smsCursor.getColumnIndex(DBConstants.ID));
            String smsBody = smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_BODY));

            tableName = DBConstants.BANK_SMS_CODES;
            columnName = DBConstants.BANK_SMS_SENDER;

            smsSenders = dbHelper.getValues(false, tableName, columnName, null, null);
            for (String value : smsSenders) {
                if (smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_SENDER)).contains(value)) {
                    smsSender = value;
                    primaryId = dbHelper.getValue(tableName, DBConstants.BANK_ID, columnName + " like '" + smsSender + "'");
                    break;
                }
            }

            if (primaryId.isEmpty()) {
                // primary id for OTHER
                primaryId = dbHelper.getValue(DBConstants.BANKS, DBConstants.ID, DBConstants.BANK_NAME + " like 'OTHER'");
            }

            ContentValues values = new ContentValues();
            values.put(DBConstants.ACCOUNT_NUMBER, accountNo);
            values.put(DBConstants.ACCOUNT_BANK_ID, primaryId);
            if (creditCard) {
                values.put(DBConstants.ACCOUNT_TYPE_ID, dbHelper.getValue(DBConstants.ACCOUNT_TYPE, DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.CREDIT_CARD + "'"));
            } else
                values.put(DBConstants.ACCOUNT_TYPE_ID, dbHelper.getValue(DBConstants.ACCOUNT_TYPE, DBConstants.ID, DBConstants.ACCOUNT_TYPE_TITLE + " like '" + Constants.PERSONAL + "'"));
            values.put(DBConstants.ACCOUNT_BALANCE, getAccountBalance(smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_BODY))));
            values.put(DBConstants.ACCOUNT_DATE, System.currentTimeMillis());
            values.put(DBConstants.DATE_MODIFIED, System.currentTimeMillis());
            values.put(DBConstants.SMS_ID, sms_id);
            return dbHelper.insertContentVals(DBConstants.USER_ACCOUNTS, values);
        }
    }

    public long updateUserAccounts(Context mContext, Cursor smsCursor, long accountId) {


        if (accountId <= 0)
            return 0;

        String smsBody = smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_BODY));
        double balance = getAccountBalance(smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_BODY)));

        if (balance <= 0)
            return 0;

        String id = accountId + "";

        String number = dbHelper.getValue(DBConstants.USER_ACCOUNTS, DBConstants.ACCOUNT_NUMBER, DBConstants.ID + " = " + accountId);


        Log.e("Log sms body ", smsBody + "," + number + "," + id + "," + balance);

        ContentValues values = new ContentValues();
        String sms_id = smsCursor.getString(smsCursor.getColumnIndex(DBConstants.ID));
        values.put(DBConstants.ACCOUNT_BALANCE, balance);
        values.put(DBConstants.DATE_MODIFIED, System.currentTimeMillis());
        values.put(DBConstants.SMS_ID, sms_id);
        return dbHelper.updateRecords(DBConstants.USER_ACCOUNTS, values, DBConstants.ID + " = '" + accountId + "'", null);


    }


    public static boolean isDateString(String date) {

        for (String parse : formats) {
            SimpleDateFormat sdf = new SimpleDateFormat(parse);
            try {
                sdf.parse(date);
                return true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    public static String setDateFormat(String dateGet) {
        if (dateGet != null) {
            for (String parse : formats) {
                SimpleDateFormat sdf = new SimpleDateFormat(parse);
                try {
                    sdf.parse(dateGet);

              /*      String datesPart[];
                    String myFormat = "";
                    if (parse.contains("-")) {
                        datesPart = dateGet.split("-");

                    } else {
                        datesPart = dateGet.split("/");

                    }

                    for (int i = 0; i < datesPart.length; i++) {

                        if (datesPart[i].length() > 2) {

                            if (i == 0)
                                myFormat = "yyyy-MM-dd";
                            else
                                myFormat = "dd-MM-yyyy";

                            if (dateGet.length() > 11)
                                myFormat = "";

                            break;

                        }

                    }

                    if (!myFormat.isEmpty()) {
                        sdf = new SimpleDateFormat(myFormat);
                    }*/


                    Date date = sdf.parse(dateGet);
                    Calendar calendar = Calendar.getInstance();

                    DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
                    return targetFormat.format(date);

                /*    String dueDay = CommonUtils.get_count_of_days(
                            dateFormat.format(calendar.getTime()),
                            targetFormat.format(date));

                    // if (Double.parseDouble(dueDay) > 0) {
                    return dueDay;*/

                    // }

                } catch (ParseException e) {

                } catch (NumberFormatException e) {
                }
            }
        }


        //System.out.println("Look date list " + date);


        return "0";
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }
}

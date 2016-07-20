package com.faayda.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.faayda.R;
import com.faayda.database.DBConstants;
import com.faayda.database.DBHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by vinove on 7/28/2015.
 */
public final class CommonUtils {

    public static String substringBetween(final String str, final String open, final String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        final int start = str.indexOf(open);
        if (start != -1) {

            int i = start + open.length();
            while (i < str.length()) {

                if (close.equalsIgnoreCase(String.valueOf(str.charAt(i))))
                    break;
                i++;
            }
            return removeSpecialCharacters(str.substring(start + open.length(), i));
        }
        return "0";
    }

    public static String subStringBetween(String mainString, String startString, String endString, boolean extractNumerals) {
        int start = mainString.indexOf(startString);
        if (start == -1) {
            return "0";
        }

        start += startString.length();

        mainString = mainString.substring(start, mainString.length());
        int end = mainString.indexOf(endString);

        if (end == -1) {
            return "0";
        }

        if (extractNumerals)
            return removeSpecialCharacters(mainString.substring(0, end).trim(), true);
        return removeSpecialCharacters(mainString.substring(0, end).trim());
    }

    public static String subStringBetween(String mainString, String startString, String endString) {
        return subStringBetween(mainString, startString, endString, false);
    }

    public static String removeSpecialCharacters(String value, boolean extractNumerals) {

        String[] value1 = value.split(",");
        if (value1.length > 1) {
            value = "";
            for (int i = 0; i < value1.length; i++) {
                value = value + value1[i];
            }
        }
        if (extractNumerals) {
            value = value.replaceAll("\\D+", "");
        }
        if (CommonUtils.isNumeric(value))
            return value;
        else
            return "0";
    }

    public static String removeSpecialCharacters(String value) {
        return removeSpecialCharacters(value, false);
    }

    public static boolean isNumeric(String value) {

        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Date getDateFromTimestamp(Long timestamp) {

        if (timestamp == null)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.getTime();
    }

    public static void exportDB(Context context, String dbName) {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + context.getPackageName() + "/databases/" + dbName;
        String backupDBPath = dbName;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param timeFormat dd/MM/yyyy hh:mm:ss
     * @return
     */
    public static long getMillisFromDate(String timeFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        formatter.setLenient(false);
        Date date = null;
        long millis = 0;
        try {
            date = formatter.parse(timeFormat);
            millis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return millis;
    }

    public static String getDateFromMillis(long millis) {
        return getDateFromMillis(millis, "dd/MM/yyyy hh:mm:ss");
    }

    public static String getDateFromMillis(long millis, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        //long millis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return formatter.format(calendar.getTime());
    }

    public static String DoubleToStringLimits(double pNumero) {
        // the function is call with the values Redondear(625.3f, 2)
        int limit = 2;
        BigDecimal roundfinalPrice = new BigDecimal(pNumero);
        try {
            // limit = Integer.parseInt(ConstantList.userCurrencyDecimal);
        } catch (Exception ex) {
        }
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(',');

        DecimalFormat formatter = new DecimalFormat(
                "###,###,###,###,###,###.##", symbols);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        //
        try {
            roundfinalPrice = new BigDecimal(pNumero).setScale(limit,
                    BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return df.format(roundfinalPrice);
        return formatter.format(roundfinalPrice).substring(0, formatter.format(roundfinalPrice).length() - 3);

    }


    public static long checkLatestMonthTimeStamp(Context mContext) {
/*        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        int month;
        int year = calendar.get(Calendar.YEAR);
        boolean smsFound = false;
        int count = 0;
        String monthString = "";
        for (int j = year; j >= (year - 5); j--) {
            if (j == calendar.get(Calendar.YEAR)) {
                month = calendar.get(Calendar.MONTH);
            } else {
                month = 11;
            }
            for (int i = month; i >= 0; i--) {
                monthString = (i + 1) < 10 ? "0" + (i + 1) : String.valueOf(i + 1);
                if (getMillisFromDate("01/" + monthString + "/" + year + " 00:00:00") >= 0) {

                    count = ((BaseActivity) mContext).dbHelper.getTableRecordsCount(DBConstants.SMS, null, DBConstants.SMS_DATE + ">" + getMillisFromDate("01/" + monthString + "/" + year + " 00:00:00"), null);
                } else {
                    Toast.makeText(mContext, "Inbox dont have any new message !", Toast.LENGTH_LONG).show();
                }
                if (count > 0) {
                    smsFound = true;
                    break;
                }
                year = j;
                if (smsFound) break;
            }
        }*/

        Calendar calendar = Calendar.getInstance();


        //Timestamp for month

        int month = calendar.get(Calendar.MONTH) + 1;

        String date = "01/" + month + "/" + calendar.get(Calendar.YEAR) + " 00:00:00";

        return getMillisFromDate(date);
    }

    public static String getStringFromArray(String[] array) {
        String arrayString = "";
        for (int i = 0; i < array.length; i++) {
            if (i > 0) arrayString += ",";
            arrayString += array[i];
        }
        return arrayString;
    }

    public static String getCurrentMonthYear() {
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        return Constants.months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR);
    }

    public static void rateApplication(Context mContext) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + mContext.getPackageName()));
        mContext.startActivity(intent);
    }

    public static void shareApplication(Context mContext) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.Share_Subject);
        sendIntent.putExtra(Intent.EXTRA_TEXT, Constants.Share_Faayda);
        try {
            mContext.startActivity(Intent.createChooser(sendIntent, "Share using .."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "There is no sharing application installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static double getExpenseAmount(DBHelper dbHelper, long latestTimeStamp) {
        //Spend for current month
        String expenseTransactionsIdsString = getTransactionIdsString(dbHelper);
        String[] transactionAmount = dbHelper.getValues(false, DBConstants.TRANSACTIONS, DBConstants.TRANSACTION_AMOUNT, DBConstants.TRANSACTION_TYPE_ID + " IN (" + expenseTransactionsIdsString + ") AND " + DBConstants.TRANSACTION_DATE + ">" + latestTimeStamp, null);
        Double expenseAmount = 0.0;
        String expense_or = "";
        for (String value : transactionAmount) {
            expenseAmount += Double.parseDouble(value);
            expense_or = expense_or + "," + value;
        }
        return expenseAmount;
    }

    public static String getTransactionIdsString(DBHelper dbHelper) {


        String[] expenseTransactionNames = new String[]{Constants.DEBIT, Constants.ATM_WDL, Constants.EXPENSE,
                Constants.CASH_EXPENSE, Constants.CREDIT_CARD_EXPENSE, Constants.DEBIT_CARD_EXPENSE};

        // String[] expenseTransactionNames = new String[]{Constants.EXPENSE, Constants.CASH_EXPENSE, Constants.CREDIT_CARD_EXPENSE,};
        String[] expenseTransactionIds = new String[expenseTransactionNames.length];
        for (int i = 0; i < expenseTransactionNames.length; i++) {
            expenseTransactionIds[i] = dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + expenseTransactionNames[i] + "'");
        }
        String expenseTransactionsIdsString = CommonUtils.getStringFromArray(expenseTransactionIds);
        return expenseTransactionsIdsString;
    }

    public static double getIncomeAmount(DBHelper dbHelper, long latestTimeStamp) {
        double income = 0;
        String transactionTypeCredit = dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.CREDIT + "'");
        String refundCredit = dbHelper.getValue(DBConstants.TRANSACTION_TYPE, DBConstants.ID, DBConstants.TRANSACTION_NAME + " like '" + Constants.REFUND + "'");


        String sqlQuery = "select sum(" + DBConstants.TRANSACTION_AMOUNT + ") from " + DBConstants.TRANSACTIONS + " where (" +
                DBConstants.TRANSACTION_TYPE_ID + " = " + transactionTypeCredit + " or  " + DBConstants.TRANSACTION_TYPE_ID + " = " + refundCredit + " )and " +
                DBConstants.TRANSACTION_DATE + " > " + latestTimeStamp;


        Cursor cashCursor = dbHelper.getDataThroughRaw(sqlQuery);
        if (cashCursor != null)
            if (cashCursor.moveToFirst())
                income = cashCursor.getDouble(0);

        return income;
    }

    public static String getBankName(DBHelper dbHelper, long id) {
        return dbHelper.getValue(DBConstants.BANKS, DBConstants.BANK_NAME, DBConstants.ID + " = " + id);
    }

    public static String formattedDate(long millis) {
        Date date = getDateFromTimestamp(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return dateFormat.format(date);
    }

    public static String formattedDate(long millis, String format) {
        Date date = getDateFromTimestamp(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
    /*
    private int[] catImages = new int[]{R.drawable.medical, R.drawable.restaurant, R.drawable.shooping,
            R.drawable.travel, R.drawable.pets, R.drawable.entertainment, R.drawable.education,
            R.drawable.beauty};
    private String[] catName = new String[]{"Medical", "Restaurant", "Shopping", "Travel", "Pets", "Entertainment", "Education", "Beauty"};*/

    public static int getServiceProvidersIcon(String providerName) {
        int id = R.drawable.airtel;
        switch (providerName) {
            case Constants.AIRTEL:
                id = R.drawable.airtel;
                break;
            case Constants.IDEA:
                id = R.drawable.idea;
                break;

            case Constants.RELIANCE:
                id = R.drawable.reliance;
                break;

            case Constants.UNINOR:
                id = R.drawable.uninor;
                break;
            case Constants.VODAFONE:
                id = R.drawable.vodaphone;
                break;
            default:
                id = R.drawable.default_provider;
                break;
        }
        return id;
    }

    public static int getMerchantImage(String bankName) {
        switch (bankName) {
            case Constants.ADCB:
                return R.drawable.adcb;
            case Constants.ALLAHBAD:
                return R.drawable.allahbad;
            case Constants.AMARICAN:
                return R.drawable.amarican;
            case Constants.ANDHRA:
                return R.drawable.andhra;
            case Constants.ANZ:
                return R.drawable.anz;
            case Constants.AXIS:
                return R.drawable.axis;
            case Constants.BOB:
                return R.drawable.bob;
            case Constants.CENRA:
                return R.drawable.cenra;
            case Constants.CITI:
                return R.drawable.citi;
            case Constants.CLUB:
                return R.drawable.club;
            case Constants.DBS:
                return R.drawable.dbs;
            case Constants.DENA:
                return R.drawable.dena;
            case Constants.HDFC:
                return R.drawable.hdfc;
            case Constants.HDFC_HOME_LOAN:
                return R.drawable.hdfc_home_loan;
            case Constants.HSBC:
                return R.drawable.hsbc;
            case Constants.ICICI:
                return R.drawable.icici;
            case Constants.IDBI:
                return R.drawable.idbi;
            case Constants.INDIAN:
                return R.drawable.indian;
            case Constants.INDUSLAND:
                return R.drawable.indusland;
            case Constants.ING:
                return R.drawable.ing;
            case Constants.IOB:
                return R.drawable.iob;
            case Constants.J_K:
                return R.drawable.j_k;
            case Constants.KOTAK:
                return R.drawable.kotak;
            case Constants.OBC:
                return R.drawable.obc;
            case Constants.PSB:
                return R.drawable.psb;
            case Constants.SARASWATI:
                return R.drawable.saraswat;
            case Constants.SBI:
                return R.drawable.sbi;
            case Constants.SOUTH:
                return R.drawable.south;
            case Constants.STANDARD:
                return R.drawable.standard;
            case Constants.SYNDICATE:
                return R.drawable.syndicate;
            case Constants.UCO:
                return R.drawable.uco;
            case Constants.UNION:
                return R.drawable.union;
            case Constants.CASH:
                return R.drawable.cash_spand;

            case Constants.YES:
                return R.drawable.yes;
            default:
                return -1;
        }
    }

    public static final String getBillerName(DBHelper dbHelper, long id) {
        return dbHelper.getValue(DBConstants.BILLER_LIST, DBConstants.BILLER_NAME, DBConstants.ID + " = " + id);
    }

    public static boolean checkSenderBank(Context mContext, Cursor smsCursor, DBHelper dbHelper) {
        boolean ifBank = false;
        String smsSender = smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_SENDER));
        int getBankCount = dbHelper.getTableRecordsCount(DBConstants.BANK_SMS_CODES, null, DBConstants.BANK_SMS_SENDER + " like '" + smsSender + "'", null);
        if (getBankCount > 0) {
            ifBank = true;
        }
        return ifBank;
    }

    public static boolean checkSenderMerchant(Context mContext, Cursor smsCursor, DBHelper dbHelper) {
        boolean ifBank = false;
        String smsSender = smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_SENDER));
        int getBankCount = dbHelper.getTableRecordsCount(DBConstants.BILLER_SMS_CODES, null, DBConstants.BILLER_SMS_SENDER + " like '" + smsSender + "'", null);
        if (getBankCount > 0) {
            ifBank = true;
        }
        return ifBank;
    }

    public static long getSenderBankId(Context mContext, Cursor smsCursor, DBHelper dbHelper) {
        boolean ifBank = false;
        String smsSender = smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_SENDER));
        String id = dbHelper.getValue(DBConstants.BANK_ID, DBConstants.BANK_SMS_SENDER + " like '" + smsSender + "'", null);
        if (Long.parseLong(id) > 0) {
            return Long.parseLong(id);
        }
        return 0;
    }

    public static long getSenderMerchantId(Context mContext, Cursor smsCursor, DBHelper dbHelper) {
        boolean ifBank = false;
        String smsSender = smsCursor.getString(smsCursor.getColumnIndex(DBConstants.SMS_SENDER));

        //Log.e("Sms sendar", smsSender);
        String id = dbHelper.getValue(DBConstants.BILLER_SMS_CODES, DBConstants.BILLER_ID, DBConstants.BILLER_SMS_SENDER + " like '" + smsSender + "'");
        try {
            if (id != null)
                if (Long.parseLong(id) > 0) {
                    long id_ = Long.parseLong(id);

                    return id_;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getCategoryId(DBHelper dbHelper, long id) {
        String categoryId = dbHelper.getValue(DBConstants.BILLER_LIST, DBConstants.CATEGORY_ID, DBConstants.ID + " = " + id);
        try {
            return Long.parseLong(categoryId);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getBillerIcon(String category) {
        switch (category) {
            case Constants.BILLER_CREDIT_CARD:
                return R.drawable.card;

            case Constants.INVESTMENT:
                return R.drawable.restaurant;

            case Constants.UTILITY:
                return R.drawable.shooping;

            case Constants.OTHER:
                return R.drawable.travel;

            default:
                return -1;
        }
    }

    public static int getCategoryIcon(String category) {
        switch (category) {
            case Constants.MEDICAL:
                return R.drawable.medical;

            case Constants.RESTAURANT:
                return R.drawable.restaurant;

            case Constants.SHOPPING:
                return R.drawable.shooping;

            case Constants.TRAVEL:
                return R.drawable.travel;

            case Constants.PETS:
                return R.drawable.pets;

            case Constants.ENTERTAINMENT:
                return R.drawable.entertainment;

            case Constants.EDUCATION:
                return R.drawable.education;

            case Constants.BEAUTY:
                return R.drawable.beauty;

            case Constants.MISC:
                return R.drawable.miscellaneous;

            case Constants.CAR:
                return R.drawable.car;

            case Constants.BILLS:
                return R.drawable.bills;

            case Constants.DINING:
                return R.drawable.dining;

            case Constants.PREPAID_WALLET:
                return R.drawable.prepaid_wallet;

            case Constants.HOUSE_HOLD:
                return R.drawable.house_hold;

            case Constants.EMI:
                return R.drawable.emi;

            case Constants.RENT:
                return R.drawable.rent;

            case Constants.BUSINESS_C:
                return R.drawable.business;

            case Constants.KIDS:
                return R.drawable.kids;

            case Constants.BOOKS:
                return R.drawable.books;

            case Constants.AIR_TICKET:
                return R.drawable.air_ticket;

            case Constants.FAST_FOOD:
                return R.drawable.fast_food;

            case Constants.STOCKS:
                return R.drawable.stock;

            case Constants.IMPS:
                return R.drawable.imps;

            default:
                return -1;

        }

    }

    public static int get_count_of_days(String Created_date_String, String Expire_date_String) {


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        Date Created_convertedDate = null, Expire_CovertedDate = null, todayWithZeroTime = null;

        try {
            Created_convertedDate = dateFormat.parse(Created_date_String);
            Expire_CovertedDate = dateFormat.parse(Expire_date_String);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int c_year = 0, c_month = 0, c_day = 0;

        if (Created_convertedDate.after(todayWithZeroTime)) {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(Created_convertedDate);

            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(todayWithZeroTime);

            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);
        }


        /*Calendar today_cal = Calendar.getInstance();
        int today_year = today_cal.get(Calendar.YEAR);
        int today = today_cal.get(Calendar.MONTH);
        int today_day = today_cal.get(Calendar.DAY_OF_MONTH);
        */


        Calendar e_cal = Calendar.getInstance();
        e_cal.setTime(Expire_CovertedDate);

        int e_year = e_cal.get(Calendar.YEAR);
        int e_month = e_cal.get(Calendar.MONTH);
        int e_day = e_cal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(c_year, c_month, c_day);
        date2.clear();
        date2.set(e_year, e_month, e_day);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return ((int) dayCount);
/*
        if (dayCount<0)
            return  (" Overdue in " +(-1* (int) dayCount) + " Days");

        return ("Due in " + (int) dayCount + " Days");*/
    }
}

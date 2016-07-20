package com.faayda.utils;

/**
 * Created by vinove on 6/8/15.
 */
public final class AppConstant {

    public static String INBOX_URI = "content://sms/inbox";

    public static String AMOUNT_REGEX = "INR \\d+|Rs.\\d+|INR\\d+";

    public static String CREDIT_REGEX = ".*\\bcredited.*|.*\\bdeposited\\b.*";

    public static String CASH_WITHDRAWAL_REGEX = ".*\\bWDL\\b.*|.*\\bwithdrawn\\b.*";

    public static String AVL_BAL_REGEX = ".*\\bA/c Balance\\b.*|.*\\bAvl bal\\b.*";

    public static String CREDIT_CARD_REGEX = ".*\\bCredit Card\\b.*";

    public static String DEBIT_REGIX = ".*\\bdebited.* ";

   // public static String DEBIT_CARD_REGEX = ".*\\bDebit Card\\b.*|.*\\bBank Card\\b.*";
   public static String DEBIT_CARD_REGEX = ".*\\bDebit Card\\b.*";

    public static String FUND_TRANSFER_REGEX = ".*\\bNEFT\\b.*|.*\\btransfer\\b.*";

    public static String ACCOUNT_NO_REGEX = ".*\\ba/c no.*";

    public static String BILL_REGEX = ".*\\bbill\\b.*|.*\\bDue\\b.*";

    public static String EXPENSE_REGEX = "payment of Rs.\\d+|payment of INR\\d+";

    public static String ONLY_NUMERICS = "^[1-9]\\d*(\\.\\d+)?$";

    public static String INBOX_BODY = "body";

    public static String INBOX_DATE = "date";

    public static String INBOX_ADDRESS = "address";

    public static String ACCOUNTING_TYPE_CREDIT = "CREDIT";

    public static String ACCOUNTING_TYPE_DEBIT = "DEBIT";

    public static String YTD = "YTD";

    public static String TRANSACTION_TYPE_CASH_WDL = "ATM WDL";

    public static String MONTH_NAME = "MonthName";

    public static String WEEK_NBR = "Week_Nbr";

    public static String CONSTANTS_STORAGE_FILE = "App_Constants_Storage_File";

    public static String LAST_UPDATED_DATE = "LAST_UPDATE_DATE";

    public static String IS_FIRST_CALL = "Is_First_Call";

    public static String TRANSACTION_STORAGE_FILE = "Transaction_Storage_File";

    public static String MONTH_NAME_SHORT = "MMM";

    public static String MONTH_NAME_LONG = "MMMM";

    public static String ITEM_POSITION = "position";

    public static String EDIT_AMOUNT = "amount";

    public static String EDIT_DATE = "date";
}

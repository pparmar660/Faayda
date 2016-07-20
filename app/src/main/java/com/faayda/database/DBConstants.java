package com.faayda.database;

/**
 * Created by Aashutosh @ vinove on 8/4/2015.
 */
public class DBConstants {

    public static final String DB_NAME = "financialAppFaayda.sqlite";

    //TODO: Update both variables below to be in sync with DB changes
    public static final int PREV_DB_VERSION = 0;
    public static final int DB_VERSION = 1;

    public static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS ";

    // Table Names
    public static final String BANKS = "banks";
    public static final String DEBIT_CARDS = "bank_cards";
    public static final String BILL_RECORDS = "bill_records";
    public static final String USER_DETAILS = "user_details";
    public static final String BILLER_LIST = "merchant_list";
    public static final String USER_ACCOUNTS = "user_accounts";
    public static final String TRANSACTION_TYPE = "transaction_type";
    public static final String TRANSACTIONS = "transactions";
    public static final String SMS = "sms";
    public static final String CASH_MANAGEMENT = "cash_management";
    public static final String BILLER_SMS_CODES = "biller_sms_codes";
    public static final String BANK_SMS_CODES = "bank_sms_codes";
    public static final String CATEGORIES = "categories";
    public static final String SMS_TEMPLATES = "sms_templates";
    public static final String SMS_TEMPLATES_TAG = "sms_templates_tag";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String NOTIFICATION = "notification";


    //General Column Names
    public static final String ID = "id";
    public static final String SMS_ID = "sms_id";
    public static final String INBOX_SMS_ID = "inbox_sms_id";
    public static final String DATE_MODIFIED = "date_modified";

    //BANKS
    public static final String BANK_NAME = "bank_name";
    public static final String BANK_IMAGE = "bank_icons";
    public static final String BANK_CONTACT_NO = "bank_contact_no";

    //BANK CARDS
    public static final String CARD_TYPE = "card_type";
    public static final String CARD_NUMBER = "card_number";
    public static final String LINKED_ACCOUNT_ID = "linked_account_id";
    public static final String LINKED_ACCOUNT_STATUS = "linked_bank_status";
    public static final String CREDIT_CARD_CREDIT_LIMIT = "credit_card_limit";

    //BILL_RECORDS
    public static final String BILL_AMOUNT = "bill_amount";
    public static final String BILL_DUE_DATE = "bill_due_date";
    public static final String BILL_PAID_STATUS = "bill_paid_status";
    public static final String BILL_TRANSACTION_ID = "bill_transaction_id";

    //USER_DETAILS
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_MOBILE = "user_mobile";

    //MERCHANT_LIST
    public static final String BILLER_NAME = "merchant_name";
    public static final String CATEGORY_ID = "category_id";

    //USER_ACCOUNTS
    public static final String ACCOUNT_NUMBER = "account_number";
    public static final String ACCOUNT_BANK_ID = "account_bank_id";
    public static final String ACCOUNT_BALANCE = "account_balance";
    public static final String OUTSTANDING_AMOUNT = "outstanding_amount";
    public static final String ACCOUNT_DATE = "account_date";
    public static final String ACCOUNT_TYPE_ID = "account_type_id";
    public static final String ACCOUNT_NICK_NAME = "account_nick_name";
    public static final String ACCOUNT_CREATED_BY = "account_create_by";

    //TRANSACTION_TYPE
    public static final String TRANSACTION_NAME = "transaction_name";

    //TRANSACTIONS
    public static final String TRANSACTION_AMOUNT = "transaction_amount";
    public static final String TRANSACTION_ACCOUNT_ID = "transaction_account_id";
    public static final String TRANSACTION_TYPE_ID = "transaction_type_id";
    public static final String TRANSACTION_DATE = "transaction_date";
    public static final String TRANSACTION_CREDIT = "debit_credit";
    public static final String TRANSACTION_BILLER_ID = "debit_merchant_id";
    public static final String TRANSACTION_BANK_ID = "trasaction_bank_id";


    //CATEGORIES
    public static final String CATEGORY_TITLE = "category_title";

    //SMS
    public static final String SMS_SENDER = "sms_sender";
    public static final String SMS_BODY = "sms_body";
    public static final String SMS_DATE = "sms_date";
    public static final String SMS_STATUS = "sms_status";
    //CASH_MANAGEMENT
    public static final String CASH_IN_HAND = "cash_in_hand";
    public static final String CATEGORY_IMAGE = "category_image";

    //BILLER_SMS_CODES
    public static final String BILLER_SMS_SENDER = "biller_sms_sender";
    public static final String BILLER_ID = "biller_id";

    //BANK_SMS_CODES
    public static final String BANK_SMS_SENDER = "bank_sms_sender";
    public static final String BANK_ID = "bank_id";

    //SMS_TEMPLATES_TAG
    public static final String SMS_TAG_TITLE = "sms_tag_title";

    // SMS_TEMPLATE
    public static final String SMS_TAG_ID = "sms_tag_title";
    public static final String SMS_TAG_START = "sms_tag_start";
    public static final String SMS_TAG_END = "sms_tag_end";

    // ACCOUNT TYPE
    public static final String ACCOUNT_TYPE_TITLE = "account_type_title";


    public static final String NOTIFICATION_TITLE = "notification_title";
    public static final String NOTIFICATION_BODY = "notification_body";
    public static final String TIME_KEY = "time_key";


}

package com.faayda.utils;

public final class Constants {

    public static final String[] months = new String[]{"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};

    public static final String CREDIT_CARD = "credit_card";//
    public static final String CREDIT_CARD_BUSINESS = "credit_card_business";
    public static final String DEBIT_CARD = "debit_card";
    public static final String BUSINESS = "business";
    public static final String PERSONAL = "personal";
    public static final String ADD_CASH = " Cash Withdrawal";
    public static final String ADD_ACCOUNT = "Add New Account";
    public static final String DASHBOARD = "DASHBOARD";
    public static final String FROM = "from";
    public static final String ADD_TRANSACTION = "ADD TRANSACTION";
    public static final String EDIT_TRANSACTION = "EDIT TRANSACTION";
    //    public static final String MORE_CATEGORY = " MORE CATEGORY";
    public static final String CASH_MANAGEMENT = "Cash Management";
    public static final String EDIT_CASH = "Edit Cash";
    public static final String SETTINGS = "Settings";
    public static final String ADD_BILLER = "Add Bill";
    public static final String EDIT_BILLER = "Edit Bill";
    public static final String ACCOUNT_ID = "Account Id";
    public static final String EDIT_ACCOUNT = "Edit Account";
    public static final int CASH_ACCOUNT_NO = -10; // unique no. for cash
    public static final String REFUND = "Refund";
    public static final String TRANSACTION_ID = "Transaction Id";


    public static final String BILL_ID = "Bill Id";
    public static final String BILLER_CREDIT_CARD = "Credit Card";
    public static final String INVESTMENT = "Investment";
    public static final String UTILITY = "Utilities";
    public static final String OTHER = "Other";

    public static int screenWith;
    public static int screenHeight;

    public static enum SERVICE_TYPE {
        DEFAULT {
            @Override
            public String toString() {
                return "Default";
            }
        }, ERROR {
            @Override
            public String toString() {
                return "Error";
            }
        }, SIGN_IN,
        LOGIN {
            @Override
            public String toString() {
                return "Login";
            }
        }, REGISTER {
            @Override
            public String toString() {
                return "Register";
            }
        }, GET_LIST, GET_UPPER_DATA, GET_BOTTOM_DATA, GET_DATA, SEND_DATA, GET_SEARCH_DATA, GET_FILTER_DATA,
        POLICY {
            @Override
            public String toString() {
                return "Policy";
            }
        }, SMS_LOAD {
            @Override
            public String toString() {
                return "Sms Loading";
            }

        }, REFRESH_DATA {
            @Override
            public String toString() {
                return "Refresh Data";
            }
        }, UPDATE_TRANS {
            @Override
            public String toString() {
                return "Update_Transaction";
            }
        }, CATEGORIES {
            @Override
            public String toString() {
                return "Categories";
            }
        }, BANK_LIST {
            @Override
            public String toString() {
                return "Bank List";
            }
        }, BILLER_LIST {
            @Override
            public String toString() {
                return "Biller List";
            }
        }, FAQ {
            @Override
            public String toString() {
                return "FAQ";
            }
        }, SMS_TEMPLATE;

        @Override
        public String toString() {
            return "SMS_TEMPLATE";
        }
    }

    public static enum TITLE_BAR {
        NOTIFICATION_ONLY, SAVE_BUTTON
    }

    static String encodedImage;

    public static String getEncodedImage() {
        return encodedImage;
    }

    public static void setEncodedImage(String encodedImage) {
        Constants.encodedImage = encodedImage;
    }

    public static final String DEBITED_AMOUNT_TAG = "debited_amount";
    public static final String DEBIT_CARD_TAG = "debit_card";
    public static final String DEBIT_CARD_TRANS_TAG = "debit_card_transaction";
    public static final String CREDITED_AMOUNT_TAG = "credited_amount";
    public static final String CREDIT_CARD_TAG = "credit_card";
    public static final String CREDIT_CARD_TRANS_TAG = "credit_card_transaction";
    public static final String AVAILABLE_BALANCE_TAG = "available_balance";
    public static final String OUTSTANDING_AMOUNT_TAG = "outstanding_amount";
    public static final String ACCOUNT_NUMBER_TAG = "account_number";
    public static final String BILL_AMOUNT_TAG = "bill_amount";
    public static final String BILL_DUE_DATE_TAG = "bill_due_date";
    public static final String PAYMENT_MADE_TAG = "payment_made";
    public static final String ATM_WDL_TAG = "atm_withdrawal";
    public static final String FUND_TRANSFER_TAG = "fund_transfer";


    public static final int ACCOUNTS_INDEX = 1;
    public static final int BILLS_INDEX = 2;
    public static final int CASH_INDEX = 3;
    //    public static final int SPEND_SUMMARY_INDEX = 4;
    public static final int SETTINGS_INDEX = 4;
    public static final int FEEDBACK_INDEX = 5;
    public static final int SHARE_INDEX = 6;
    public static final int FAQ_INDEX = 7;
    public static final int LIKE_US_ON_FACEBOOK_INDEX = 8;
    public static final int FOLLOW_US_ON_TWITTER_INDEX = 9;
    public static final int REFRESH_SMS_INDEX = 10;
    public static final int ABOUT_INDEX = 11;
    public static final int DASBOARD_INDEX = 12;
    public static final String ACCOUNTS = "Accounts";

    public static final String CASH = "Cash";
    public static final String SPEND_SUMMARY = "Spend Summary";
    public static final String SETTTINGS = "Settings";
    public static final String FEEDBACK = "Feedback";
    public static final String SHARE = "Share";
    public static final String FAQ = "FAQ";
    public static final String ABOUT = "About";

    public static final String KEY_EMAIL = "email";
    public static final String KEY_LAST_TIME = "lastTimeDataGet";
    public static final String KEY_CREDIT_TRANS = "creditTransaction";
    public static final String KEY_DEBIT_TRANS = "debitTransaction";
    //    public static final String[] KEY_EMAIL2 = {"email", "RAN"};
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_SOCIAL_ID = "social_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CONFIRM_PASSWORD = "confirm_password";
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_LOGIN_CHECK = "login_check";
    public static final String KEY_METHOD = "method";
    public static final String SMS_LOADED = "sms_loaded";
    public static final String USER_ID = "user_id";
    public static final String CATEGORIES_FETCHED = "categories_fetched";
    public static final String NOTIFICATION = "notification";
    //    public static final String MORE_CATEGORY = "More Category";
    public static final String BANK_LIST_FETCHED = "bank_list_fetched";
    public static final String BILLER_LIST_FETCHED = "biller_list_fetched";

    public static final String KEY_TOKEN = "token";

    // start for activity result request Code

    //    public static final String URL = "http://i.vinove.com/financial_mov/backend/web/index.php/api";
    public static final String URL = "https://faaydaapp.net/backend/web/index.php/api";
    public static final String FACEBOOK_PAGE_URL = "http://www.facebook.com/faaydacom";//http://www.fb.com";
    public static final String TWITTER_PAGE_URL = "http://www.twitter.com/faayda";//http://www.twitter.com";
    public static final String KEY_FEEDBACK = "feedback";
    public static final String KEY_LOGIN = "login1";
    public static final String KEY_REFRESH_DATA = "refreshData";
    public static final String KEY_REGISTRATION = "user_registration";
    public static final String KEY_FGT_PWD = "forgot_password";
    public static final String KEY_TERMS_POLICY = "getTermsnPolices";
    public static final String KEY_FAQ = "getfaq";
    public static final String KEY_BANK_LIST = "bank_list";
    public static final String KEY_BILLER_LIST = "biller_list";
    public static final String KEY_CATEGORY_LIST = "category_list";
    //    public static final String KEY_EXPENSE_CAT = "getexpensecategory";
    public static final String KEY_UPDATE_TRANS = "update_tanscation";
    public static final String KEY_SMS_TEMPLATES = "sms_templates";


    public static final String TRANSACTION_TYPE = "transaction_type";

    public static final String ATM_WDL = "ATM_WDL";
    public static final String FUND_TRANSFER = "FUND_TRANSFER";
    public static final String CASH_EXPENSE = "CASH";
    public static final String EXPENSE = "EXPENSE";
    public static final String CREDIT_CARD_EXPENSE = "credit_card_expense";
    public static final String DEBIT_CARD_EXPENSE = "debit_card_expense";
    public static final String CREDIT = "credit";
    public static final String DEBIT = "debit";
    public static final String BILL = "bill";
    public static final String AVL_BAL = "available_balance";
    public static final String ACC_NO = "account_number";

    public static final String EXPENSE_AMOUNT = "expense_amount";
    public static final String ACCOUNT_NUMBER = "account_number";
    public static final String ACCOUNT_BALANCE = "account_balance";

    // error message--------------

    public static final String ErrorMessage_UserName = "Please enter user name";
    public static final String ErrorMessage_password = "Password should be greater than 4 characters";
    public static final String ErrorMessage_match = "Password does not match";
    public static final String ErrorMessage_name = "Please enter name";
    public static final String ErrorMessage_email = "Please enter valid email";
    public static final String ErrorMessage_contact = "Please enter contact name";
    public static final String ErrorMessage_host = "Please enter host name";
    public static final String ErrorMessage_default = "Error occurred! Please try again later.";
    //    public static final String Share_Faayda = "One of the Best Financial Management Apps! - http://www.faayda.com";
    public static final String Share_Subject = "Faayda";
    public static final String Share_Faayda = "Hi, I wanted to share with you this fantastic app which simplifies managing your daily expenses. You can have a look at your bank balance, the transactions you have done and your bills due with a simple swipe... download this app here...";
    //Bank Names
    public static final String HDFC = "HDFC";
    public static final String ICICI = "ICICI";
    public static final String ADCB = "ADCB";
    public static final String ALLAHBAD = "ALLAHBAD";
    public static final String AMARICAN = "AMARICAN";
    public static final String ANDHRA = "ANDHRA";
    public static final String ANZ = "anz";
    public static final String AXIS = "AXIS";
    public static final String BOB = "BOB";
    public static final String CENRA = "CENRA";
    public static final String CITI = "CITI";
    public static final String CLUB = "CLUB";
    public static final String DBS = "DBS";
    public static final String DENA = "DENA";
    public static final String HDFC_HOME_LOAN = "HDFC_HOME_LOAN";
    public static final String HSBC = "HSBC";
    public static final String IDBI = "IDBI";
    public static final String INDIAN = "INDIAN";
    public static final String INDUSLAND = "INDUSLAND";
    public static final String ING = "ING";
    public static final String IOB = "IOB";
    public static final String J_K = "J_K";
    public static final String KOTAK = "KOTAK";
    public static final String OBC = "OBC";
    public static final String PSB = "PSB";
    public static final String SARASWATI = "SARASWATI";
    public static final String SBI = "SBI";
    public static final String SOUTH = "SOUTH";
    public static final String STANDARD = "STANDARD";
    public static final String SYNDICATE = "SYNDICATE";
    public static final String UCO = "UCO";
    public static final String UNION = "UNION";
    public static final String YES = "YES";

    // Catagory Name
    public static final String MEDICAL = "Medical";
    public static final String RESTAURANT = "Restaurant";
    public static final String SHOPPING = "Shopping";
    public static final String TRAVEL = "Travel";
    public static final String PETS = "Pets";
    public static final String ENTERTAINMENT = "Entertainment";
    public static final String EDUCATION = "Education";
    public static final String BEAUTY = "Beauty";
    public static final String MISC = "Miscellaneous";
    public static final String CAR = "Car";
    public static final String BILLS = "Bills";
    public static final String DINING = "Dining";
    public static final String PREPAID_WALLET = "Prepaid wallet";
    public static final String HOUSE_HOLD = "Household";
    public static final String EMI = "EMI";
    public static final String RENT = "Rent";
    public static final String BUSINESS_C = "Business";
    public static final String KIDS = "Kids";
    public static final String BOOKS = "Books";
    public static final String AIR_TICKET = "Air ticket";
    public static final String FAST_FOOD = "Fast Food";
    public static final String STOCKS = "Stocks";
    public static final String IMPS = "IMPS";
   /* case Constants.MISC:
       case Constants.RESTAURANT:
    image = R.drawable.bills;
    break;
    case Constants.SHOPPING:
    image = R.drawable.dining;
    break;
    case Constants.TRAVEL:
    image = R.drawable.prepaid_wallet;
    break;
    case Constants.PETS:
    image = R.drawable.house_hold;
    break;
    case Constants.ENTERTAINMENT:
    image = R.drawable.emi;
    break;
    case Constants.EDUCATION:
    image = R.drawable.rent;
    break;
    case Constants.BEAUTY:
    image = R.drawable.business;
    break;
    case Constants.MISC:
    image = R.drawable.kids;
    break;
    case Constants.PETS:
    image = R.drawable.books;
    break;
    case Constants.ENTERTAINMENT:
    image = R.drawable.emi;
    break;
    case Constants.EDUCATION:
    image = R.drawable.air_ticket;
    break;
    case Constants.BEAUTY:
    image = R.drawable.fast_food;
    break;
    case Constants.MISC:
    image = R.drawable.stock;
    break;
    case Constants.MISC:
    image = R.drawable.imps;
    break;*/

    // Service Provider
    public static final String AIRTEL = "Airtel";
    public static final String IDEA = "Idea";
    public static final String RELIANCE = "Reliance";
    public static final String UNINOR = "Uninor";
    public static final String VODAFONE = "Vodafone";
}

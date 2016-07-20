package com.faayda.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by vinove_2 on 12-09-2015.
 */


public class TemproryMessage {


    public static ArrayList<HashMap<String, String>> bankSmsList;
    public static String MSG = "msg", DATE = "date", TAG = "tag", ID = "id";
    static int intialId;

    public static ArrayList<HashMap<String, String>> List() {

        Calendar calendar = Calendar.getInstance();
        bankSmsList = new ArrayList<>();
        String tag = "";
        String msg = "";
        String date = calendar.getTimeInMillis() + "";
        //1
        tag = "VK-HDFCBK";
        msg = "An amount of Rs.30,354.98 has been debited from your a/c no: XXXX2652 for BillPay/Credit Card payment done using HDFC Bank NetBanking";
        bankSmsList.add(putInMap(tag, msg, date));
        //2
        tag = "VM-HDFCBK";
        msg = "INR 11,000.00 deposited to A/c No XX7878 towards 00111000122652  -TPT-loan Val 27-AUG-15. Clr Bal is INR 17,352.61 subject to clearing";
        bankSmsList.add(putInMap(tag, msg, date));

        //3
        tag = "VM-HDFCBK";
        msg = "An amount of Rs.11,000.00 has been debited from your account number XXXX2652 for TPT txn done using HDFC Bank NetBanking ";
        bankSmsList.add(putInMap(tag, msg, date));
        //4
        tag = "VK-HDFCBK";
        msg = "INR 33,000.00 deposited to A/c No XX7878 towards 00111000122652  -TPT-loan to company Val 02- SEP-15. Clr Bal is INR 39,352.61  subject to clearing.";
        bankSmsList.add(putInMap(tag, msg, date));
        //5

        tag = "VK-HDFCBK";
        msg = "An amount of Rs.31,500.00 has been debited from your account number XXXX7878 for NEFT txn done using HDFC Bank NetBanking";
        bankSmsList.add(putInMap(tag, msg, date));

        //6

        tag = "VK-HDFCBK";
        msg = "You have added/modified a beneficiary unKarma Consultancy to your HDFC Bank NetBanking NEFT Module for funds transfer";
        bankSmsList.add(putInMap(tag, msg, date));
        //7
        tag = "VK-HDFCBK";
        msg = "An amount of Rs.3,000.00 has been debited from your account number XXXX2652 for NEFT txn done using HDFC Bank NetBanking";
        bankSmsList.add(putInMap(tag, msg, date));

        //8
        tag = "VM-HDFCBK";
        msg = "Balance in A/c XXXXXXXXXX2652 as of 11-AUG-15 EOD is INR 77,765.65. Check A/c for current balance . Credits in A/c are subject to clearing";
        bankSmsList.add(putInMap(tag, msg, date));

        //9
        tag = "VM-HDFCBK";
        msg = "Rs.200.00 was spent on ur HDFCBank CREDIT Card ending 7885 on 2015-08-13:14:38:03 at Paytm.com.Avl bal - Rs.567571.00, curr o/s - Rs.32429.00";
        bankSmsList.add(putInMap(tag, msg, date));

        //10
        tag = "VM-HDFCBK";
        msg = "Rs.3051.03 was spent on ur HDFCBank CREDIT Card ending 7885 on 2015-08-14:16:55:21 at PAYPAL *BE ONLINE.Avl bal - Rs. 564519.97, curr o/s - Rs.35480.03";
        bankSmsList.add(putInMap(tag, msg, date));

        //11
        tag = "VM-HDFCBK";
        msg = "Balance in A/c XXXXXXXXXX2652 as of 27-AUG-15 EOD is INR 52,510.47. Check A/c for current balance . Credits in A/c are subject to clearing";
        bankSmsList.add(putInMap(tag, msg, date));
        //12

        tag = "VM-HDFCBK";
        msg = "Balance in A/c XXXXXXXXXX2652 as of 27-AUG-15 EOD is INR 52,510.47. Check A/c for current balance . Credits in A/c are subject to clearing";
        bankSmsList.add(putInMap(tag, msg, date));

        //13
        tag = "VM-HDFCBK";
        msg = "Rs.1376.84 was spent on ur HDFCBank CREDIT Card ending 7885 on 2015-08-30:19:40:03 at PAYPAL *ENVATO MKPL ENVAT.Avl bal - Rs.582387.16, curr o/s - Rs. 17612.84";
        bankSmsList.add(putInMap(tag, msg, date));

        //14
        tag = "VM-HDFCBK";
        msg = "INR 63,793.90 deposited to A/c No XX2652 towards NEFT Cr-BOFAOMM6205-JIM 0 ROBERTS IV-PRASHANT PARIKH-158U125406XC1X21 Val 31-AUG-15. Clr Bal is INR 1,16,304.37  subject to clearing.";
        bankSmsList.add(putInMap(tag, msg, date));
        //15

        tag = "VM-HDFCBK";
        msg = "An amount of Rs.3,500.00 has been debited from your account number XXXX7878 for an online payment txn done using HDFC Bank NetBanking.";
        bankSmsList.add(putInMap(tag, msg, date));

        //16
        tag = "VM-HDFCBK";
        msg = "One Time Password for NetBanking Transaction is 448499. Please use the password to complete the Transaction. Pls do not share this with anyone. Ref No- XXXX0763";
        bankSmsList.add(putInMap(tag, msg, date));

        //17
        tag = "YT-HDFCBK";
        msg = "EMI of Rs 23721 is due on 08-May-2015 for loan A/c 29778977 . Kindly ensure clearance. Logon to www.hdfcbank.com to view all your loan details on NETBANKING.";
        bankSmsList.add(putInMap(tag, msg, date));

        //18
        tag = "YT-HDFCBK";
        msg = "EMI of Rs 23721 is due on 05-Aug-2015 for loan A/c 29778977 . Kindly ignore our previous SMS. Regret the inconvenience.";
        bankSmsList.add(putInMap(tag, msg, date));

        //19
        tag = "VK-HDFCBK";
        msg = "You have added/modified a beneficiary unKarma Consultancy to your HDFC Bank NetBanking NEFT Module for funds transfer";
        bankSmsList.add(putInMap(tag, msg, date));
        //20
        tag = "VK-HDFCBK";
        msg = "An amount of Rs.3,000.00 has been debited from your account number XXXX2652 for NEFT txn done using HDFC Bank NetBanking";
        bankSmsList.add(putInMap(tag, msg, date));

        //21
        tag = "VK-HDFCBK";
        msg = "Rs.60.00 was spent on ur HDFCBank CREDIT Card ending 7885 on 2015-09-05:11:42:57 at ITUNES.COMBILL.Avl bal - Rs. 582327.00, curr o/s - Rs.17673.00";
        bankSmsList.add(putInMap(tag, msg, date));

        //22
        tag = "VK-HDFCBK";
        msg = "An amount of Rs.3,120.00 has been debited from your account number XXXX7878 for an online payment txn done using HDFC Bank NetBanking";
        bankSmsList.add(putInMap(tag, msg, date));

        //23
        tag = "VD-VFCARE";
        msg = "Hello! Your Bill dated 24-Aug-15 of Rs.650.42 has been generated and will be sent on your email ID by 28-Aug-15. To know your bill details, SMS MBILL to 199 or dial *111#.";
        bankSmsList.add(putInMap(tag, msg, date));

        //24
        tag = "VM-AxisBk";
        msg = "Your a/c 63820525 is debited Rs 197 on 2015-07-13 A/c balance is Rs 23215.82 Info: NEFT/MB/ AXMB151949396185/priyanka";
        bankSmsList.add(putInMap(tag, msg, date));

        //25
        tag = "RM-HDFCBK";
        msg = "Thank you for using Debit Card ending 8670 for Rs.21.00 in MUMBAI at AIRCEL on 2015-07-28:13:35:07 Avl bal: Rs.3000.22";
        bankSmsList.add(putInMap(tag, msg, date));

        //26
        tag = "VM-HDFCBK";
        msg = "Thank you or using Debit ar ending 8670 for Rs.200.00 in Delhi at Delhi Metro Rail Corpo on 2015-07-12:14:47:12 Avl bal: Rs.141.22";
        bankSmsList.add(putInMap(tag, msg, date));

        //27
        tag = "VM-HDFCBK";
        msg = "Thank you for using Debit Card ending 8670 for Rs.197.00 in MUMBAI at FREECHARGE on 2015-07-13:17:34:54 Avl bal: Rs.21.22";
        bankSmsList.add(putInMap(tag, msg, date));

        //28
        tag = "VM-HDFCBK";
        msg = "Thank you for using Debit Card ending 8670 for Rs.197.00 in MUMBAI at FREECHARGE on 2015-07-13:17:34:54 Avl bal: Rs.21.22";
        bankSmsList.add(putInMap(tag, msg, date));

        //29
        tag = "VM-HDFCBK";
        msg = "Rs.5000.00 was withdrawn using your HDFC Bank Card ending 8670 on 2015-07-15:09:53:15 at +GANDHINGR GHAZIABAD BR. Avl bal: Rs.18021.22";
        bankSmsList.add(putInMap(tag, msg, date));

        //30

        tag = "VM-HDFCBK";
        msg = "Rs.15000.00 was withdrawn using your HDFC Bank Card ending 8670 on 2015-07-15:09:53:58 at +GANDHINGR GHAZIABAD BR. Avl bal: Rs.3021.22";
        bankSmsList.add(putInMap(tag, msg, date));
//31

        tag = "VM-HDFCBK";
        msg = "Thank you for using Debit Card ending 8670 for Rs.500.00 in Delhi at Delhi Metro Rail Corpo on 2015-08-05:18:36:32 Avl bal: Rs.2483.12";
        bankSmsList.add(putInMap(tag, msg, date));

        //32
        tag = "VM-HDFCBK";
        msg = "Thank you for using Debit Card ending 8670 for Rs.24.00 in MUMBAI at AIRCEL on 2015-08-10:22:19:10 Avl bal: Rs.2459.12";
        bankSmsList.add(putInMap(tag, msg, date));
//33
        tag = "VM-HDFCBK";
        msg = "Thank you for using Debit Card ending 8670 for Rs.497.00 in MUMBAI at BOOKMYSHOW on 2015-08-15:14:45:43 Avl bal: Rs.24962.12";
        bankSmsList.add(putInMap(tag, msg, date));

        //34
        tag = "VM-HDFCBK";
        msg = "Thank you for using Debit Card ending 8670 for Rs.197.00 in MUMBAI at FREECHARGE on 2015-08-22:17:44:01 Avl bal: Rs.4124.12";
        bankSmsList.add(putInMap(tag, msg, date));

        //35
        tag = "DZ-AxisBk";
        msg = "Hello, this is to inform you that your a/c XXXX0525 is debited with Rs.15.00 on 11-JUL-15 for Value Added SMS Alert Fee";
        bankSmsList.add(putInMap(tag, msg, date));

        //36
        tag = "DZ-AxisBk";
        msg = "Your a/c 63820525 is credited Rs 1 on 2015-08-24 A/c balance is Rs 23216.82 Info: NEFT/N235150089403089/ PRIYANKA AGARWAL";
        bankSmsList.add(putInMap(tag, msg, date));

        //37
        tag = "DZ-HDFCBK";
        msg = "Salary of INR 23,000.00  credited to A/c XXXXXXXXXX2708 towards SALARY VINOVE SOFTWARE AND SERVICES P Value 14- JUL-2015 . Check A/c for balance";
        bankSmsList.add(putInMap(tag, msg, date));

        //38
        tag = "DZ-HDFCBK";
        msg = "Salary of INR 23,000.00  credited to A/c XXXXXXXXXX2708 towards SALARY VINOVE SOFTWARE AND SERVICES P Value 14- AUG-2015 . Check A/c for balance";
        bankSmsList.add(putInMap(tag, msg, date));

        //39
        tag = "DZ-HDFCBK";
        msg = "An amount of Rs.1.00 has been debited from your account number XXXX2708 for NEFT txn done using HDFC Bank NetBanking";
        bankSmsList.add(putInMap(tag, msg, date));

        //40
        tag = "DZ-HDFCBK";
        msg = "NEFT Transaction with reference number N235150089403089 for INR 1.00 has been credited to the beneficiary account on 24-08-2015 at 09:03:18.";
        bankSmsList.add(putInMap(tag, msg, date));

        //41
        tag = "DZ-HDFCBK";
        msg = "NEFT Transaction with reference number N235150089403089 for INR 1.00 has been credited to the beneficiary account on 24-08-2015 at 09:03:18.";
        bankSmsList.add(putInMap(tag, msg, date));

        //42
        tag = "DZ-HDFCBK";
        msg = "Thank you for using Debit Card ending 8670 for Rs.457.00 in MUMBAI at BOOKMYSHOW on 2015-08-29:20:14:18 Avl bal: Rs.3663.27";
        bankSmsList.add(putInMap(tag, msg, date));

        //43
        tag = "DZ-HDFCBK";
        msg = "Thank you or using Debit Card ending 8670 for Rs.500.00 in Delhi at Delhi Metro Rail Corpo on 2015-08-22:16:43:29 Avl bal: Rs.4462.12";
        bankSmsList.add(putInMap(tag, msg, date));

        //44
        tag = "DZ-HDFCBK";
        msg = "Thank you for using Debit Card ending 8670 for Rs.21.00 in MUMBAI at AIRCEL on 2015-08-22:17:35:50 Avl bal: Rs.4441.12";
        bankSmsList.add(putInMap(tag, msg, date));


        //45
        tag = "VM-INDUSB";
        msg = "Induslnd bank A/c No. 162***061990 has been credited for Rs. 60,000.00 towards a NEFT Credit from NIRANT SARIN with Transaction Reference Number 827669834. Available balance is 110,000.00";
        bankSmsList.add(putInMap(tag, msg, date));

        //46
        tag = "VK-HDFCBK";
        msg = "Rs.60.00 was spent on ur HDFCBank CREDIT Card ending 7885 on 2015-09-05:11:42:57 at ITUNES.COMBILL.AvI bal - Rs. 582327.00, curr o/s - Rs.17673.00";
        bankSmsList.add(putInMap(tag, msg, date));


        //47
        tag = "VK-HDFCBK";
        msg = "An amount of Rs.3,120.00 has been debited from your account number XXXX7878 for an online payment txn done using HDFC Bank NetBanking.";
        bankSmsList.add(putInMap(tag, msg, date));

        //48

        tag = "VK-HDFCBK";
        msg = "An amount of Rs.34,200.00 has been debited from your account number XXXX7878 for TPT txn done using HDFC Bank NetBanking";
        bankSmsList.add(putInMap(tag, msg, date));

        // 49
        tag = "VK-HDFCBK";
        msg = "INR 34,000.00 deposited to A/c No XX7878 towards 00111000122652 -TPT-loan to company Val 11- SEP-15. Clr Bal is INR 35,232.61  subject to clearing.";
        bankSmsList.add(putInMap(tag, msg, date));
//50
        tag = "VK-HDFCBK";
        msg = "INR 34,000.00 deposited to A/c No XX7878 towards 00111000122652 -TPT-loan to company Val 11- SEP-15. Clr Bal is INR 70,0000.78  subject to clearing.";
        bankSmsList.add(putInMap(tag, msg, date));

        return bankSmsList;
    }

    public static HashMap<String, String> putInMap(String tag, String msg, String date) {

        HashMap<String, String> map = new HashMap<>();
        intialId++;
        map.put(TAG, tag);
        map.put(ID, intialId + "");
        map.put(MSG, msg);
        map.put(DATE, date);

        return map;

    }

}

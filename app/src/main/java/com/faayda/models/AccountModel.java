package com.faayda.models;

/**
 * Created by Antonio on 15-08-2015.
 */
public class AccountModel {

    long id,status;


    int merchantImage;
    double outStandingAmount=0d;
    String date, accountNo, amount, bankName, debitCardNo;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public int getMerchantImage() {
        return merchantImage;
    }

    public void setMerchantImage(int merchantImage) {
        this.merchantImage = merchantImage;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getDebitCardNo() {
        return debitCardNo;
    }

    public void setDebitCardNo(String debitCardNo) {
        this.debitCardNo = debitCardNo;
    }
    public double getOutStandingAmount() {
        return outStandingAmount;
    }

    public void setOutStandingAmount(double outStandingAmount) {
        this.outStandingAmount = outStandingAmount;
    }

}

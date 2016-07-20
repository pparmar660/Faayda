package com.faayda.models;

/**
 * Created by Aashutosh @ vinove on 8/13/2015.
 */
public final class TransactionModel {
    public long getTransactionAccountId() {
        return transactionAccountId;
    }

    public void setTransactionAccountId(long transactionAccountId) {
        this.transactionAccountId = transactionAccountId;
    }

    long id,transactionTypeId,transactionAccountId;
    int image;
    String title, month, amount,cardNumber,accountNumber;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(long transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public long getAmount() {
        try {

             double amount1=Double.parseDouble(amount.trim());
            return (long)(amount1);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return 0;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

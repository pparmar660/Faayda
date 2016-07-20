package com.faayda.models;

/**
 * Created by Antonio on 15-08-2015.
 */
public class BillAndEmiModel {

    long id;

    String dueDate;
    String daysDue;
    String billRegarding;
    String DueAmount;



    String merchantName;
    String categoryTitle,categoryImageUri;

    public String getBillRegarding() {
        return billRegarding;
    }

    public void setBillRegarding(String billRegarding) {
        this.billRegarding = billRegarding;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }




    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategoryImageUri() {
        return categoryImageUri;
    }

    public void setCategoryImageUri(String categoryImageUri) {
        this.categoryImageUri = categoryImageUri;
    }

    public String getDaysDue() {
        return daysDue;
    }

    public void setDaysDue(String daysDue) {
        this.daysDue = daysDue;
    }

    public String getDueAmount() {
        return DueAmount;
    }

    public void setDueAmount(String dueAmount) {
        DueAmount = dueAmount;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}

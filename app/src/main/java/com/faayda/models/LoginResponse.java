package com.faayda.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vinove on 7/28/2015.
 */
public final class LoginResponse {
    //{"code" : "200", "message":"Login successfully", "userid":"0123456"}
    public int code;
    @SerializedName("userid")
    public String userId;
    public String message;
    public CategoryModel[] Category;
    public BankModel[] Bank;
    public BillerModel[] Biller;
    public SMSTemplate SmsTemplate;
}
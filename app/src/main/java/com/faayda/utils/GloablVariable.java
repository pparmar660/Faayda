package com.faayda.utils;

/**
 * Created by vinove on 11/8/15.
 */
public final class GloablVariable {

    public static String categoryResponse;
    public static String bankResponse;
    public static String billerResponse;
    public static boolean status = false;

    public static boolean isStatus() {
        return status;
    }

    public static void setStatus(boolean status) {
        GloablVariable.status = status;
    }

    public static String getBankResponse() {
        return bankResponse;
    }

    public static void setBankResponse(String bankResponse) {
        GloablVariable.bankResponse = bankResponse;
    }

    public static String getBillerResponse() {
        return billerResponse;
    }

    public static void setBillerResponse(String billerResponse) {
        GloablVariable.billerResponse = billerResponse;
    }

    public static String getCategoryResponse() {
        return categoryResponse;
    }

    public static void setCategoryResponse(String categoryResponse) {
        GloablVariable.categoryResponse = categoryResponse;
    }
}

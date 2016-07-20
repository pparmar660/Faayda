package com.faayda.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Antonio on 15-08-2015.
 */
public class FAQModel {
    @SerializedName("Id")
    public int id;
    public String question;
    public String answer;
}

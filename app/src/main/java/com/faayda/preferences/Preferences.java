package com.faayda.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class Preferences {

    SharedPreferences sharedPref;
    Editor edit;
    public static String MyPREFERENCES = "FaaydaPreferences";


    public Preferences(Context con) {
        sharedPref = con.getSharedPreferences(MyPREFERENCES, 0);
        edit = sharedPref.edit();
    }

    public void setString(String key, String value) {

        edit.putString(key, value);
        edit.commit();
    }

    public String getString(String key) {
        return sharedPref.getString(key, null);
    }

    public void setBoolean(String key, Boolean value) {
        edit = sharedPref.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public Boolean getBoolean(String key) {
        return sharedPref.getBoolean(key, false);
    }

    public void setclear() {
        edit.clear();
        edit.commit();
    }


}

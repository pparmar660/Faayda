package com.faayda.listeners;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Aashutosh @ vinove on 8/3/2015.
 */
public interface OnFragmentChangeListener {
    public void onFragmentAdd(Fragment fragment, String TAG);
    public void onFragmentReplace(Fragment fragment, String TAG);
    public void onFragmentReplace(Fragment fragment);
    public void onFragmentAddWithArgument(Fragment fragment, String TAG,Bundle arguments);
}

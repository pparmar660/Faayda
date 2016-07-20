package com.faayda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.faayda.database.DBHelper;
import com.faayda.preferences.Preferences;
import com.faayda.processor.TransactionMessageProcessor;

public class BaseActivity extends AppCompatActivity {
    public DBHelper dbHelper;
    public Preferences preferences;
    public TransactionMessageProcessor processor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        dbHelper.open();
        processor = TransactionMessageProcessor.getInstance(this);
        preferences = new Preferences(this);
    }
}


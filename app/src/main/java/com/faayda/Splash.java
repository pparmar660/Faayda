package com.faayda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.widget.LinearLayout;

import com.faayda.preferences.Preferences;
import com.faayda.utils.Constants;
import com.faayda.utils.GifView;

/**
 * Created by vinove on 7/22/2015.
 */
public final class Splash extends Activity {

    Preferences preferences;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        preferences = new Preferences(this);
        gotoNext();
        linearLayout = (LinearLayout) findViewById(R.id.liner);
        Display display = getWindowManager().getDefaultDisplay();
        GifView view = new GifView(this, display);
        linearLayout.addView(view);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void gotoNext() {
        Handler handler = new Handler();
        Runnable r = new Runnable() {

            @Override
            public void run() {
                Intent intent;
                if (preferences.getBoolean(Constants.KEY_LOGIN_CHECK)) {
                    intent = new Intent(Splash.this, MainActivity.class);
                } else {
                    intent = new Intent(Splash.this, Login.class);
                }
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(r, 2500);
    }
}
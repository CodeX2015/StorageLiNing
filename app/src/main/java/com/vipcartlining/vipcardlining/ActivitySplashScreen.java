package com.vipcartlining.vipcardlining;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Eugene on 09.07.2015.
 */
public class ActivitySplashScreen extends AppCompatActivity {
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSettings = getSharedPreferences(ActivitySettings.APP_PREFERENCES, MODE_PRIVATE);
                if (checkFirstLaunch()) {
                    startActivity(new Intent(ActivitySplashScreen.this, ActivitySettings.class));
                } else {
                    startActivity(new Intent(ActivitySplashScreen.this, ActivityVipCard.class));
                }
                finish();
            }
        }, 1500);
    }

    public boolean checkFirstLaunch() {
        if (mSettings.getBoolean("firstrun", true)) {
            mSettings.edit().putBoolean("firstrun", false).apply();
            return true;
        }
        return false;
    }
}


package com.vipcartlining.vipcardlining;

import android.app.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Eugene on 08.07.2015.
 */
public class VipCardLiNingApp extends Application {
    public static final String APP_PREFERENCES = "LiNingPrefs";
    public static final String API_ADDRESS = "address of api";
    public static final String USER_LOGIN = "Login";
    public static final String USER_PASS = "Password";
    public static final String SHOP = "Shop";

    private static Logger log = LoggerFactory.getLogger(VipCardLiNingApp.class);
    @Override
    public void onCreate() {
        setDefaultUncaughtExceptionHandler();
        super.onCreate();
    }

    private static void setDefaultUncaughtExceptionHandler() {
        try {
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    log.error("Uncaught Exception detected in thread {}", t, e);
                }
            });
        } catch (SecurityException e) {
            log.error("Could not set the Default Uncaught Exception Handler", e);
        }
    }
}

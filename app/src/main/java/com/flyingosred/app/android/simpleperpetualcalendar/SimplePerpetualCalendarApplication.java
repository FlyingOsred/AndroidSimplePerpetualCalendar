package com.flyingosred.app.android.simpleperpetualcalendar;

import android.app.Application;

import com.flyingosred.app.android.simpleperpetualcalendar.data.database.DatabaseContainer;

public class SimplePerpetualCalendarApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseContainer.getInstance().init(getApplicationContext());
    }
}

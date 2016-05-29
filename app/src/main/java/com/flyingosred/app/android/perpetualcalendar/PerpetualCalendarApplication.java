/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar;

import android.app.Application;

import com.flyingosred.app.android.perpetualcalendar.data.database.DatabaseContainer;

public class PerpetualCalendarApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseContainer.getInstance().init(getApplicationContext());
    }
}

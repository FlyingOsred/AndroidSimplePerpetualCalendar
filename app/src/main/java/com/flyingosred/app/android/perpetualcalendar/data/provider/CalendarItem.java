/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.provider;

import android.content.Context;

import com.flyingosred.app.android.perpetualcalendar.data.Lunar;

import java.util.Calendar;

public final class CalendarItem {

    private final Context mContext;

    private final Calendar mCalendar;

    private final LunarProvider mLunarProvider;

    private final Lunar mLunar;

    private final SolarTermProvider mSolarTermProvider;

    private final String mSolarTermName;

    public CalendarItem(Context context, Calendar calendar) {
        mContext = context;
        mCalendar = calendar;
        mLunarProvider = new LunarProvider(context);
        mLunar = mLunarProvider.get(mCalendar);
        mSolarTermProvider = new SolarTermProvider(context);
        mSolarTermName = mSolarTermProvider.getName(mCalendar);
    }
}

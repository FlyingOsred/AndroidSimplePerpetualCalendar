package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;

public class LunarDatabaseItem extends Lunar {

    private final int mYear;
    private final int mMonth;
    private final int mDay;

    private final boolean mLastDayInMonth;
    private final boolean mLeapMonth;

    public LunarDatabaseItem(int year, int month, int day, boolean isLastDayInMonth, boolean isLeapMonth) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mLastDayInMonth = isLastDayInMonth;
        mLeapMonth = isLeapMonth;
    }

    @Override
    public int getYear() {
        return mYear;
    }

    @Override
    public int getMonth() {
        return mMonth;
    }

    @Override
    public int getDay() {
        return mDay;
    }

    @Override
    public boolean isLastDayInMonth() {
        return mLastDayInMonth;
    }

    @Override
    public boolean isLeapMonth() {
        return mLeapMonth;
    }
}

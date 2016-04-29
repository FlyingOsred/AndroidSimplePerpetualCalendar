package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;

public class LunarDatabaseItem implements Lunar {

    private int mYear;
    private int mMonth;
    private int mDay;

    private boolean mLastDayInMonth;
    private boolean mLeapMonth;

    public void setYear(int year) {
        mYear = year;
    }

    public void setMonth(int month) {
        mMonth = month;
    }

    public void setDay(int day) {
        mDay = day;
    }

    public void setLastDayInMonth(boolean lastDayInMonth) {
        mLastDayInMonth = lastDayInMonth;
    }

    public void setLeapMonth(boolean leapMonth) {
        mLeapMonth = leapMonth;
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

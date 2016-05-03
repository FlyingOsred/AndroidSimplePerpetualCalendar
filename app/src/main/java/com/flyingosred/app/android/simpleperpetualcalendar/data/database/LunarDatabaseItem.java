package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;

public class LunarDatabaseItem extends Lunar {

    private final int mYear;
    private final int mMonth;
    private final int mDay;

    private final boolean mLastDayInMonth;
    private final boolean mLeapMonth;

    private LunarDatabaseItem(int year, int month, int day, boolean isLastDayInMonth, boolean isLeapMonth) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mLastDayInMonth = isLastDayInMonth;
        mLeapMonth = isLeapMonth;
    }

    public static LunarDatabaseItem FirstDay() {
        return new LunarDatabaseItem(LUNAR_YEAR_MIN, 1, 1, false, false);
    }

    public LunarDatabaseItem getNextDay() {
        int day = mDay;
        int month = mMonth;
        int year = mYear;
        boolean isLastDayInMonth = false;
        boolean isLeapMonth = mLeapMonth;
        int leapMonth = LunarDatabase.getLeapMonth(year);
        day++;
        int daysInMonth = LunarDatabase.getDaysInMonth(year, month);
        if (day > daysInMonth) {
            day = 1;
            if (month == leapMonth && !mLeapMonth) {
                isLeapMonth = true;
            } else {
                isLeapMonth = false;
                month++;
            }
        }
        if (month > Lunar.MONTHS_IN_YEAR) {
            month = 1;
            year++;
        }
        daysInMonth = LunarDatabase.getDaysInMonth(year, month);
        if (day == daysInMonth) {
            isLastDayInMonth = true;
        }

        return new LunarDatabaseItem(year, month, day, isLastDayInMonth, isLeapMonth);
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

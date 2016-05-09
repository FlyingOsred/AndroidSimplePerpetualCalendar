package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Solar;
import com.flyingosred.app.android.simpleperpetualcalendar.util.Utils;

public class SolarDatabaseItem implements Solar {

    private final int mYear;

    private final int mMonth;

    private final int mDay;

    public SolarDatabaseItem(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
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
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass())
            return false;
        if (o == this)
            return true;
        SolarDatabaseItem item = (SolarDatabaseItem) o;
        if (Utils.isSameDay(mYear, mMonth, mDay, item.mYear, item.mMonth, item.mDay)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (mDay & 0x1F) | ((mMonth & 0xF) << 5) | (mYear << 9);
    }
}

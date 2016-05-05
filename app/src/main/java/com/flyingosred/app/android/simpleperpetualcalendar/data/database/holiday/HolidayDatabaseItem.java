package com.flyingosred.app.android.simpleperpetualcalendar.data.database.holiday;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday;

public class HolidayDatabaseItem implements Holiday {

    private int mRegion = INVALID_FIELD;

    private int mStartYear = INVALID_FIELD;

    private int mResId = INVALID_FIELD;

    private int mYear = INVALID_FIELD;

    private int mOffOrWork = INVALID_FIELD;

    public int getOffOrWork() {
        return mOffOrWork;
    }

    public void setOffOrWork(int offOrWork) {
        mOffOrWork = offOrWork;
    }

    public int getRegion() {
        return mRegion;
    }

    public void setRegion(int region) {
        mRegion = region;
    }

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        mResId = resId;
    }

    public int getStartYear() {
        return mStartYear;
    }

    public void setStartYear(int startYear) {
        mStartYear = startYear;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }
}

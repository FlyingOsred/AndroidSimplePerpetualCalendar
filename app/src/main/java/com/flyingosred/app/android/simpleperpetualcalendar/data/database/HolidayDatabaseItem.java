package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday;

public class HolidayDatabaseItem implements Holiday {

    private String mRegion = null;

    private int mId = INVALID_FIELD;

    private int mOffOrWork = INVALID_FIELD;

    public void setRegion(String region) {
        mRegion = region;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setOffOrWork(int offOrWork) {
        mOffOrWork = offOrWork;
    }

    @Override
    public String getRegion() {
        return mRegion;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public int getOffOrWork() {
        return mOffOrWork;
    }
}

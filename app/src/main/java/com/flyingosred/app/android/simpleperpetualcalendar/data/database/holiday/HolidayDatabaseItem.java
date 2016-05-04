package com.flyingosred.app.android.simpleperpetualcalendar.data.database.holiday;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday.INVALID_FIELD;

public class HolidayDatabaseItem {

    private final int mStartYear;

    private final int mRegion;

    private final int mId;

    private final int mOffOrWork;

    public HolidayDatabaseItem(int region, int id) {
        this(INVALID_FIELD, region, id, INVALID_FIELD);
    }

    public HolidayDatabaseItem(int startYear, int region, int id, int offOrWork) {
        mStartYear = startYear;
        mRegion = region;
        mId = id;
        mOffOrWork = offOrWork;
    }
}

package com.flyingosred.app.android.simpleperpetualcalendar.data.database.holiday;

public final class HolidayId {

    public static final int OFFSET = 3000;

    public static final int START = 0;

    public static final int INTERNATIONAL_DAY_BASE = START;

    public static final int INTERNATIONAL_OBSERVANCE_BASE = INTERNATIONAL_DAY_BASE + OFFSET;

    public static final int LUNAR_FESTIVAL_BASE = INTERNATIONAL_OBSERVANCE_BASE + OFFSET;

    private int mId;

    private int mResId;

}

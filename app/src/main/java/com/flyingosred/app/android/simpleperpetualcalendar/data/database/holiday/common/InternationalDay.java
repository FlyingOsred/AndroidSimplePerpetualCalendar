package com.flyingosred.app.android.simpleperpetualcalendar.data.database.holiday.common;

import com.flyingosred.app.android.simpleperpetualcalendar.data.HolidayRegion;
import com.flyingosred.app.android.simpleperpetualcalendar.data.database.holiday.HolidayDatabase;

public final class InternationalDay {

    public static final int ID_BASE = 0;

    public static final int ID_NEW_YEAR = ID_BASE + 0;

    public static final int ID_WOMEN_DAY = ID_BASE + 1;

    public static final int ID_WORKERS_DAY = ID_BASE + 2;

    static {
        HolidayDatabase.add(1, 1, ID_NEW_YEAR, HolidayRegion.ALL);
        HolidayDatabase.add(3, 8, ID_WOMEN_DAY, HolidayRegion.ALL);
        HolidayDatabase.add(5, 1, ID_WORKERS_DAY, HolidayRegion.ALL);
    }
}

package com.flyingosred.app.android.simpleperpetualcalendar.data.database.holiday.china;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday;
import com.flyingosred.app.android.simpleperpetualcalendar.data.database.holiday.HolidayDatabase;


public class ChinaPublicHoliday {

    private static int mId = Holiday.REGION_CHINA * Holiday.REGION_ID_OFFSET;

    static {
        add(1, 1, 1950);
        add(3, 8, 1950);
        add(3, 12, 1979);
        add(5, 1, 1950);
        add(5, 4, 1950);
        add(6, 1, 1950);
        add(7, 1, 1941);
        add(7, 1, 1997);
        add(8, 1, 1933);
        add(9, 10, 1985);
        add(10, 1, 1949);
        add(11, 8, 2000);
        add(12, 13, 2014);
        add(12, 20, 1999);
        addLunar(1, 1);
        addLunar(1, 15);
        addLunar(2, 2);
    }

    private static void add(int month, int day, int year) {
        add(month, day, year, false);
    }

    private static void add(int month, int day, boolean isLunar) {
        add(month, day, Holiday.INVALID_FIELD, isLunar);
    }

    private static void add(int month, int day, int year, boolean isLunar) {
        HolidayDatabase.add(month, day, year, isLunar, mId++);
    }

    private static void addLunar(int month, int day) {
        HolidayDatabase.addLunar(month, day, mId++);
    }
}

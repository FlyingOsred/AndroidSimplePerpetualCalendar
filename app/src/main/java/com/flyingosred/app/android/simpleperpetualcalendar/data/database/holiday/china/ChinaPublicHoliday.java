package com.flyingosred.app.android.simpleperpetualcalendar.data.database.holiday.china;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday;
import com.flyingosred.app.android.simpleperpetualcalendar.data.database.holiday.HolidayDatabase;

public class ChinaPublicHoliday {
    static {
        add(1, 1, 0);
        add(3, 8, 0);
        add(3, 12, 0);
        add(5, 1, 3);
        add(5, 4, 3);
        add(6, 1, 3);
        add(7, 1, 3);
        add(8, 1, 3);
        add(9, 10, 0);
        add(10, 1, 3);
        add(11, 8, 3);
        add(6, 1, 3);
    }

    private static void add(int month, int day, int id) {
        HolidayDatabase.add(month, day, Holiday.REGION_CHINA, id);
    }
}

package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;

public class SolarTermDatabase {

    public static final int START_YEAR = 2016;

    public static final int END_YEAR = 2016;

    public static final int[] DAY_BASE = {5, 20, 3, 18, 5, 20, 4, 19, 5, 20, 5, 21, 6, 22, 7, 22, 7, 22, 8, 23, 7, 22, 6, 21};

    public static final long[] DATABASE = {
            0x1000410000051L
    };

    public static int get(int year, int month, int day) {
        if (year >= SolarTermDatabase.START_YEAR && year <= SolarTermDatabase.END_YEAR) {
            long yearData = SolarTermDatabase.DATABASE[year - SolarTermDatabase.START_YEAR];
            int index = (month - 1) * 2;
            for (int i = index; i < index + 2; i++) {
                int offset = (int) ((yearData >> i) & 0x03);
                int targetDay = SolarTermDatabase.DAY_BASE[i] + offset;
                if (day == targetDay) {
                    return i;
                }
            }
        }
        return PerpetualCalendar.INVALID_ID;
    }

}

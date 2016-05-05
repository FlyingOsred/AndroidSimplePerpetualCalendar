package com.flyingosred.app.android.simpleperpetualcalendar.data.database.holiday.common;

import com.flyingosred.app.android.simpleperpetualcalendar.data.HolidayRegion;
import com.flyingosred.app.android.simpleperpetualcalendar.data.database.holiday.HolidayDatabase;


public class LunarFestival {

    public static final int ID_BASE = 1000;

    public static final int ID_SPRING_FESTIVAL = ID_BASE + 0;

    public static final int ID_LANTERN_FESTIVAL = ID_BASE + 1;

    public static final int ID_TOMB_SWEEPING_DAY = ID_BASE + 2;

    public static final int ID_DRAGON_BOAT_FESTIVAL = ID_BASE + 3;

    public static final int ID_DOUBLE_SEVENTH_DAY = ID_BASE + 4;

    public static final int ID_MID_AUTUMN_FESTIVAL = ID_BASE + 5;

    public static final int ID_DOUBLE_NINTH_FESTIVAL = ID_BASE + 6;

    public static final int ID_NEW_YEARS_EVE = ID_BASE + 7;

    static {
        HolidayDatabase.addLunar(1, 1, ID_SPRING_FESTIVAL, HolidayRegion.CHINA_ALL);
        HolidayDatabase.addLunar(1, 15, ID_LANTERN_FESTIVAL, HolidayRegion.CHINA_ALL);
        HolidayDatabase.addSolarTerm(7, ID_TOMB_SWEEPING_DAY, HolidayRegion.CHINA_ALL);
        HolidayDatabase.addLunar(5, 5, ID_DRAGON_BOAT_FESTIVAL, HolidayRegion.CHINA_ALL);
        HolidayDatabase.addLunar(7, 7, ID_DOUBLE_SEVENTH_DAY, HolidayRegion.CHINA_ALL);
        HolidayDatabase.addLunar(8, 15, ID_MID_AUTUMN_FESTIVAL, HolidayRegion.CHINA_ALL);
        HolidayDatabase.addLunar(9, 9, ID_DOUBLE_NINTH_FESTIVAL, HolidayRegion.CHINA_ALL);
        HolidayDatabase.addLunar(12, true, ID_NEW_YEARS_EVE, HolidayRegion.CHINA_ALL);
    }

}

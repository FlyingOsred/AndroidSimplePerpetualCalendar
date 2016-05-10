package com.flyingosred.app.android.simpleperpetualcalendar.data;

import java.util.Calendar;
import java.util.List;

public interface PerpetualCalendar {

    public static final int INVALID_ID = -1;

    public static final int INVALID_POSITION = -1;

    public static final int START_YEAR = 1901;

    public static final int START_MONTH = 2;

    public static final int START_DAY = 19;

    public static final int START_DAY_OF_WEEK = Calendar.TUESDAY;

    public static final int END_YEAR = 2099;

    public static final int END_MONTH = 12;

    public static final int END_DAY = 31;

    public static final int MONTHS_IN_YEAR = 12;

    public static final int DAYS_IN_WEEK = 7;

    public Solar getSolar();

    public Lunar getLunar();

    public int getSolarTermId();

    public int getConstellationId();

    public List<Holiday> getHolidayList();
}

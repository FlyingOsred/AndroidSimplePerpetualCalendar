package com.flyingosred.app.android.simpleperpetualcalendar.data;

import java.util.Calendar;

public interface PerpetualCalendar {

    public static final int START_YEAR = 1901;

    public static final int START_MONTH = 1;

    public static final int START_DAY = 19;

    public static final int END_YEAR = 2099;

    public static final int END_MONTH = 11;

    public static final int END_DAY = 31;

    public Calendar get();

    public Lunar getLunar();

    public int getSolarTermId();
}

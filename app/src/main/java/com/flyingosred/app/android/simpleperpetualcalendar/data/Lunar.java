package com.flyingosred.app.android.simpleperpetualcalendar.data;

public interface Lunar {

    public static final int LUNAR_YEAR_MIN = 1901;

    public static final int LUNAR_YEAR_MAX = 2099;

    public int getYear();

    public int getMonth();

    public int getDay();

    public boolean isLastDayInMonth();

    public boolean isLeapMonth();

}



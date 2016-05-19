package com.flyingosred.app.android.simpleperpetualcalendar.data;

import android.os.Parcelable;

import java.util.Calendar;
import java.util.List;

public interface PerpetualCalendar extends Parcelable {

    int INVALID_ID = -1;

    int INVALID_POSITION = -1;

    int START_YEAR = 1901;

    int START_MONTH = 2;

    int START_DAY = 19;

    int START_DAY_OF_WEEK = Calendar.TUESDAY;

    int END_YEAR = 2099;

    int END_MONTH = 12;

    int END_DAY = 31;

    int MONTHS_IN_YEAR = 12;

    int DAYS_IN_WEEK = 7;

    Solar getSolar();

    Lunar getLunar();

    int getSolarTermId();

    Constellation getConstellation();

    List<Holiday> getHolidayList();
}

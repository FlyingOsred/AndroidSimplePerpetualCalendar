/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simpleperpetualcalendar.util;

import java.util.Calendar;

public final class Utils {

    public static final String EMPTY_STRING = "";

    public static final String WHITESPACE = " ";

    public static final String LOG_TAG = "SimplePerpetualCalendar";

    public static boolean isSameDay(Calendar day1, Calendar day2) {
        return day1.get(Calendar.YEAR) == day2.get(Calendar.YEAR) &&
                day1.get(Calendar.MONTH) == day2.get(Calendar.MONTH) &&
                day1.get(Calendar.DATE) == day2.get(Calendar.DATE);
    }

    public static boolean isDayAfter(int year, int month, int day, int endYear, int endMonth, int endDay) {
        if (year > endYear) {
            return true;
        } else if (year < endYear) {
            return false;
        }

        if (month > endMonth) {
            return true;
        } else if (month < endMonth) {
            return false;
        }

        return day > endDay;
    }

    public static boolean isSameDay(int year1, int month1, int day1, int year2, int month2, int day2) {
        return !(year1 <= 0 || year2 <= 0) && year1 == year2 && month1 == month2 && day1 == day2;
    }

    public static int dateHash(int year, int month, int day) {
        return (day & 0x1F) | ((month & 0xF) << 5) | (year << 9);
    }

    public static boolean isToday(Calendar calendar) {
        Calendar today = Calendar.getInstance();
        return isSameDay(today, calendar);
    }

}

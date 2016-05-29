/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.util;

import java.util.Calendar;

public final class Utils {

    public static final String EMPTY_STRING = "";

    public static final String WHITESPACE = " ";

    public static final String LOG_TAG = "PerpetualCalendar";

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

    public static boolean isDayAfter(Calendar calendar1, Calendar calendar2) {
        return isDayAfter(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DATE), calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DATE));
    }

    public static boolean isDayBefore(Calendar calendar1, Calendar calendar2) {
        return isDayBefore(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DATE), calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DATE));
    }

    public static boolean isDayBefore(int year, int month, int day, int endYear, int endMonth, int endDay) {
        if (year < endYear) {
            return true;
        } else if (year > endYear) {
            return false;
        }

        if (month < endMonth) {
            return true;
        } else if (month > endMonth) {
            return false;
        }

        return day < endDay;
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

    public static long daysBetween(Calendar start, Calendar end) {
        if (isSameDay(start, end)) {
            return 0;
        }
        int startYear;
        int startMonth;
        int endYear;
        int endMonth;
        boolean isNegative = false;
        if (isDayBefore(start, end)) {
            startYear = start.get(Calendar.YEAR);
            endYear = end.get(Calendar.YEAR);
            startMonth
        } else {
            startYear = end.get(Calendar.YEAR);
            endYear = start.get(Calendar.YEAR);
            isNegative = true;
        }
        long dayCount = 0;
        for (int i = startYear; i < endYear; i++) {
            dayCount += getDaysInYear(i);
        }
    }

    private static long daysBetweenOrdered(int startYear, int startMonth, int startDay,
                                           int endYear, int endMonth, int endDay) {
        if (isSameDay(startYear, startMonth, startDay, endYear, endMonth, endDay)) {
            return 0;
        }
        long dayCount = 0;
        for (int i = startYear; i < endYear; i++) {
            if (i == startYear) {
                dayCount += daysBetweenOrdered(i, startMonth, startDay, i, 12, 31);
                startMonth = 1;
                startDay = 1;
            } else {
                dayCount += getDaysInYear(i);
            }
        }

    }

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
    }

    public static int getDaysInYear(int year) {
        return isLeapYear(year) ? 366 : 365;
    }

}

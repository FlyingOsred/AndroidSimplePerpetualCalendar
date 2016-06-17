/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.util;

import android.view.View;
import android.widget.TextView;

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

    public static int daysBetween(Calendar start, Calendar end) {
        return daysBetween(start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1,
                start.get(Calendar.DATE), end.get(Calendar.YEAR), end.get(Calendar.MONTH) + 1,
                end.get(Calendar.DATE));
    }

    public static int daysBetween(int startYear, int startMonth, int startDay, int endYear, int endMonth,
                                  int endDay) {
        int y2, m2, d2;
        int y1, m1, d1;

        m1 = (startMonth + 9) % 12;
        y1 = startYear - m1 / 10;
        d1 = 365 * y1 + y1 / 4 - y1 / 100 + y1 / 400 + (m1 * 306 + 5) / 10 + (startDay - 1);

        m2 = (endMonth + 9) % 12;
        y2 = endYear - m2 / 10;
        d2 = 365 * y2 + y2 / 4 - y2 / 100 + y2 / 400 + (m2 * 306 + 5) / 10 + (endDay - 1);

        return (d2 - d1);
    }

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
    }

    public static int getDaysInYear(int year) {
        return isLeapYear(year) ? 366 : 365;
    }

    public static void setText(TextView textView, String text) {
        if (text != null) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}

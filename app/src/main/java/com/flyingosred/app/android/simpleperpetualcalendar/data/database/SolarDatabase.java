/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Solar;
import com.flyingosred.app.android.simpleperpetualcalendar.util.Utils;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.Solar.END_DAY;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.Solar.END_MONTH;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.Solar.END_YEAR;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.Solar.START_DAY;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.Solar.START_MONTH;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.Solar.START_YEAR;

public class SolarDatabase {

    private static final int[] DAYS_PER_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public Solar firstDay() {
        return new SolarDatabaseItem(START_YEAR, START_MONTH, START_DAY);
    }

    public Solar nextDay(Solar solar) {
        int day = solar.getDay();
        int month = solar.getMonth();
        int year = solar.getYear();
        day++;
        int daysInMonth = getDaysInMonth(year, month);
        if (day > daysInMonth) {
            day = 1;
            month++;
        }
        if (month > PerpetualCalendar.MONTHS_IN_YEAR) {
            month = 1;
            year++;
        }

        if (Utils.isDayAfter(year, month, day, END_YEAR, END_MONTH, END_DAY)) {
            return null;
        }

        return new SolarDatabaseItem(year, month, day);
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
    }

    private int getDaysInMonth(int year, int month) {
        int n = DAYS_PER_MONTH[month - 1];
        if (n != 28) {
            return n;
        }
        return isLeapYear(year) ? 29 : 28;
    }
}

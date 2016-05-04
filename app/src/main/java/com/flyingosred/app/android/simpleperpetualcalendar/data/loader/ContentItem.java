package com.flyingosred.app.android.simpleperpetualcalendar.data.loader;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.database.ConstellationDatabase;
import com.flyingosred.app.android.simpleperpetualcalendar.data.database.LunarDatabaseItem;
import com.flyingosred.app.android.simpleperpetualcalendar.data.database.SolarTermDatabase;
import com.flyingosred.app.android.simpleperpetualcalendar.util.Utils;

import java.util.Calendar;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.INVALID_ID;

public class ContentItem implements PerpetualCalendar {

    private static final int[] DAYS_PER_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private Calendar mCalendar = null;

    private final int mYear;

    private final int mMonth;

    private final int mDay;

    private final LunarDatabaseItem mLunar;

    private final int mSolarTermId;

    private final int mConstellationId;

    private final int mPosition;

    public ContentItem(int position, int year, int month, int day, LunarDatabaseItem lunar,
                       int solarTermId, int constellationId) {
        mPosition = position;
        mYear = year;
        mMonth = month;
        mDay = day;
        mLunar = lunar;
        mSolarTermId = solarTermId;
        mConstellationId = constellationId;
    }

    @Override
    public Calendar get() {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
            mCalendar.set(mYear, mMonth - 1, mDay);
        }
        return mCalendar;
    }

    @Override
    public Lunar getLunar() {
        return mLunar;
    }

    @Override
    public int getSolarTermId() {
        return mSolarTermId;
    }

    @Override
    public int getConstellationId() {
        return mConstellationId;
    }

    public static ContentItem FirstDay() {
        return new ContentItem(0, START_YEAR, START_MONTH, START_DAY, LunarDatabaseItem.FirstDay(),
                INVALID_ID, ConstellationDatabase.get(START_MONTH, START_DAY));
    }

    public int getPosition() {
        return mPosition;
    }

    public ContentItem getNextDay() {
        int day = mDay;
        int month = mMonth;
        int year = mYear;
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

        LunarDatabaseItem lunarNextDay = mLunar.getNextDay();
        int solarTermId = SolarTermDatabase.get(year, month, day);
        int constellationId = ConstellationDatabase.get(month, day);
        return new ContentItem(mPosition + 1, year, month, day, lunarNextDay, solarTermId, constellationId);
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

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }

    @Override
    public String toString() {
        return "position " + mPosition + " year " + mYear + " month " + mMonth + " day " + mDay;
    }
}

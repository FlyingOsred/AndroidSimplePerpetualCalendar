package com.flyingosred.app.android.simpleperpetualcalendar.data.database.holiday;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday;
import com.flyingosred.app.android.simpleperpetualcalendar.data.HolidayRegion;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.SolarTerm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday.INVALID_FIELD;

public class HolidayDatabase {

    private static final HashMap<HolidayKey, List<HolidayDatabaseItem>> mDatabase = new HashMap<>();

    public static void addSolarTerm(int solarTermId, int id, HolidayRegion region) {
        addSolarTerm(solarTermId, INVALID_FIELD, id);
    }

    public static void addSolarTerm(int solarTermId, int year, int id) {
        add(INVALID_FIELD, INVALID_FIELD, false, INVALID_FIELD, INVALID_FIELD, solarTermId, year, id);
    }

    public static void addLunar(int month, int day, int id, HolidayRegion region) {
        addLunar(month, day, INVALID_FIELD, id);
    }

    public static void addLunar(int month, boolean lastDayInMonth, int id, HolidayRegion region) {
        addLunar(month, INVALID_FIELD, INVALID_FIELD, id);
    }

    public static void addLunar(int month, int day, int year, int id) {
        add(month, day, true, INVALID_FIELD, INVALID_FIELD, INVALID_FIELD, year, id);
    }

    public static void add(int month, int day, int id, HolidayRegion region) {
        add(month, day, false, year, id);
    }

    public static void add(int month, int day, int year, int id) {
        add(month, day, false, year, id);
    }

    public static void add(int month, int day, boolean isLunar, int year, int id) {
        add(month, day, isLunar, INVALID_FIELD, INVALID_FIELD, INVALID_FIELD, year, id);
    }

    public static void add(int region, int month, int day, boolean isLunar, int dayOfWeek,
                           int dayOfWeekInMonth, int solarTermId, int startYear, int year,
                           int offOrWork, int resId) {
        HolidayKey key = new HolidayKey();
        key.setMonth(month);
        key.setDay(day);
        key.setIsLunar(isLunar);
        key.setDayOfWeek(dayOfWeek);
        key.setDayOfWeekInMonth(dayOfWeekInMonth);
        key.setSolarTermId(solarTermId);
        HolidayDatabaseItem item = new HolidayDatabaseItem();
        item.setOffOrWork(offOrWork);
        item.setStartYear(startYear);
        item.setRegion(region);
        item.setYear(year);
        item.setResId(resId);
        List<HolidayDatabaseItem> list = null;
        if (mDatabase.containsKey(key)) {
            list = mDatabase.get(key);
        } else {
            list = new ArrayList<>();
            mDatabase.put(key, list);
        }
        list.add(item);
    }
}

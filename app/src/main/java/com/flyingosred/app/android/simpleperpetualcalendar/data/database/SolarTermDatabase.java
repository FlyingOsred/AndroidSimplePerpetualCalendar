package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import android.content.Context;
import android.util.Log;

import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Solar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

public class SolarTermDatabase {

    public static final String ARRAYS_PREFIX = "solar_term_date_";

    public static final int START_YEAR = 2015;

    public static final int END_YEAR = 2016;

    public static final int[] DAY_BASE = {5, 20, 3, 18, 5, 20, 4, 19, 5, 20, 5, 21, 6, 22, 7, 22, 7, 22, 8, 23, 7, 22, 6, 21};

    public static final long[] DATABASE = {
            0x1000410000051L
    };

    private HashMap<Integer, Integer> mDatabase = new HashMap<>();

    public static int get(int year, int month, int day) {
        if (year >= SolarTermDatabase.START_YEAR && year <= SolarTermDatabase.END_YEAR) {
            long yearData = SolarTermDatabase.DATABASE[year - SolarTermDatabase.START_YEAR];
            int index = (month - 1) * 2;
            for (int i = index; i < index + 2; i++) {
                int offset = (int) ((yearData >> i) & 0x03);
                int targetDay = SolarTermDatabase.DAY_BASE[i] + offset;
                if (day == targetDay) {
                    return i;
                }
            }
        }
        return PerpetualCalendar.INVALID_ID;
    }

    public void init(Context context) {
        for (int year = START_YEAR; year <= END_YEAR; year++) {
            String arrayName = ARRAYS_PREFIX + year;
            int resId = context.getResources().getIdentifier(arrayName, "array",
                    context.getPackageName());
            Log.d(LOG_TAG, "Init year " + year + " with name " + arrayName + " resId " + resId);
            if (resId > 0) {
                String[] dates = context.getResources().getStringArray(resId);
                for (int i = 0; i < dates.length; i++) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
                    try {
                        Date date = formatter.parse(dates[i]);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        int hash = getDateHash(calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
                        Log.d(LOG_TAG, "Found solar term id " + i + " for " + date);
                        mDatabase.put(hash, i);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static int getDateHash(int year, int month, int day) {
        return (day & 0x1F) | ((month & 0xF) << 5) | (year << 9);
    }

    public int get(Solar solar) {
        int key = getDateHash(solar.getYear(), solar.getMonth(), solar.getDay());
        if (mDatabase.containsKey(key)) {
            return mDatabase.get(key);
        }
        return PerpetualCalendar.INVALID_ID;
    }
}

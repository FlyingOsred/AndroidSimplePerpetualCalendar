package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import android.content.Context;
import android.util.Log;

import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SolarTermDatabase {

    public static final String ARRAYS_PREFIX = "solar_term_date_";

    public static final int START_YEAR = 2016;

    public static final int END_YEAR = 2016;

    public static final int[] DAY_BASE = {5, 20, 3, 18, 5, 20, 4, 19, 5, 20, 5, 21, 6, 22, 7, 22, 7, 22, 8, 23, 7, 22, 6, 21};

    public static final long[] DATABASE = {
            0x1000410000051L
    };

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

    public static int get(Context context, int year, int month, int day) {
        String arrayName = ARRAYS_PREFIX + year;
        int resId = context.getResources().getIdentifier(arrayName, "string-array", context.getPackageName());
        if (resId > 0) {
            String[] dates = context.getResources().getStringArray(resId);
            for (int i = 0; i < dates.length; i++) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    Date date = formatter.parse(dates[i]);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    if (Utils.isSameDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DATE), year, month, day)) {
                        return i;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
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
        }
        return PerpetualCalendar.INVALID_ID;
    }
}

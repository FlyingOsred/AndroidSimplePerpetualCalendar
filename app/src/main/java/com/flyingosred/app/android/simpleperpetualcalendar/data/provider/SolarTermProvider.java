package com.flyingosred.app.android.simpleperpetualcalendar.data.provider;

import com.flyingosred.app.android.simpleperpetualcalendar.data.database.SolarTermDatabase;
import com.flyingosred.app.android.simpleperpetualcalendar.data.database.SolarTermDatabaseItem;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public final class SolarTermProvider {

    private static final String LOG_TAG = SolarTermProvider.class.getSimpleName();

    private HashMap<SolarTermDatabaseItem, Integer> mSolarTermMap = new HashMap<>();

    public SolarTermProvider(TimeZone timeZone) {
        init(timeZone);
    }

    public int get(Calendar calendar) {
        SolarTermDatabaseItem item = new SolarTermDatabaseItem(calendar);
        if (mSolarTermMap.containsKey(item)) {
            return mSolarTermMap.get(item);
        }
        return SolarTermDatabaseItem.INVALID_ID;
    }

    private void init(TimeZone timeZone) {
        for (int i = 0; i < SolarTermDatabase.DATABASE.length; i++) {
            int year = SolarTermDatabase.START_YEAR + i;
            int[][] yearData = SolarTermDatabase.DATABASE[i];
            for (int j = 0; j < yearData.length; j++) {
                int[] dateData = yearData[j];
                int month = dateData[0] - 1;
                int day = dateData[1];
                int hour = dateData[2];
                int minute = dateData[3];
                SolarTermDatabaseItem item = new SolarTermDatabaseItem(
                        year, month, day, hour, minute, 0,
                        TimeZone.getTimeZone(SolarTermDatabase.TIMEZONE), timeZone);
                mSolarTermMap.put(item, j);
            }
        }
    }
}

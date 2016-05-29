/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.database;

import android.content.Context;
import android.util.Log;

import com.flyingosred.app.android.perpetualcalendar.data.Constellation;
import com.flyingosred.app.android.perpetualcalendar.data.Database;
import com.flyingosred.app.android.perpetualcalendar.data.Holiday;
import com.flyingosred.app.android.perpetualcalendar.data.Lunar;
import com.flyingosred.app.android.perpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.perpetualcalendar.data.Solar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import static com.flyingosred.app.android.perpetualcalendar.data.PerpetualCalendar.INVALID_POSITION;
import static com.flyingosred.app.android.perpetualcalendar.util.Utils.LOG_TAG;

public class DatabaseContainer implements Database {

    private static final int[] DAYS_PER_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private static DatabaseContainer mInstance = new DatabaseContainer();

    private final HashMap<DatabaseKey, DatabaseItem> mDatabaseMap = new HashMap<>();

    private final Semaphore mInitSemaphore = new Semaphore(1);

    private final SolarTermDatabase mSolarTermDatabase = new SolarTermDatabase();

    private final SolarDatabase mSolarDatabase = new SolarDatabase();

    private final LunarDatabase mLunarDatabase = new LunarDatabase();

    private final ConstellationDatabase mConstellationDatabase = new ConstellationDatabase();

    private final HolidayDatabase mHolidayDatabase = new HolidayDatabase();

    public static DatabaseContainer getInstance() {
        return mInstance;
    }

    private DatabaseContainer() {
    }

    public Database get() {
        Log.d(LOG_TAG, "Get database begin.");
        try {
            mInitSemaphore.acquire();
        } catch (InterruptedException e) {
            Log.w(LOG_TAG, "Init semaphore was interrupted.");
        }
        mInitSemaphore.release();
        Log.d(LOG_TAG, "Get database end.");
        return this;
    }

    public void init(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "Init thread start");
                long startTime = System.currentTimeMillis();
                try {
                    mInitSemaphore.acquire();
                } catch (InterruptedException e) {
                    Log.w(LOG_TAG, "Init semaphore was interrupted.");
                }
                mSolarTermDatabase.init(context);
                mHolidayDatabase.init(context);

                int solarTermId;
                Constellation constellation;
                Solar solar = mSolarDatabase.firstDay();
                Lunar lunar = mLunarDatabase.firstDay();
                int position = 0;
                List<Holiday> holidayList;
                while (true) {
                    solarTermId = mSolarTermDatabase.get(solar);
                    constellation = mConstellationDatabase.get(solar);
                    holidayList = mHolidayDatabase.get(solar);
                    DatabaseItem item = new DatabaseItem(position, solar, lunar, solarTermId,
                            constellation, holidayList);
                    DatabaseKey keyPosition = new DatabaseKey(position);
                    mDatabaseMap.put(keyPosition, item);
                    DatabaseKey keyDate = new DatabaseKey(solar);
                    mDatabaseMap.put(keyDate, item);
                    solar = mSolarDatabase.nextDay(solar);
                    if (solar == null) {
                        break;
                    }
                    lunar = mLunarDatabase.nextDay(lunar);
                    position++;
                }
                mInitSemaphore.release();
                long endTime = System.currentTimeMillis();
                Log.i(LOG_TAG, "Init thread end, cost " + (endTime - startTime) + " milliseconds.");

            }
        }).start();
    }

    @Override
    public PerpetualCalendar get(int position) {
        Log.d(LOG_TAG, "Get item for position " + position);
        return mDatabaseMap.get(new DatabaseKey(position));
    }

    @Override
    public int getPosition(Calendar calendar) {
        Solar solar = new SolarDatabaseItem(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
        DatabaseKey key = new DatabaseKey(solar);
        if (mDatabaseMap.containsKey(key)) {
            DatabaseItem item = mDatabaseMap.get(key);
            return item.getPosition();
        }
        return INVALID_POSITION;
    }

    @Override
    public int getPosition(int year, int month, int day) {
        Solar solar = new SolarDatabaseItem(year, month, day);
        DatabaseKey key = new DatabaseKey(solar);
        if (mDatabaseMap.containsKey(key)) {
            DatabaseItem item = mDatabaseMap.get(key);
            return item.getPosition();
        }
        return INVALID_POSITION;
    }

    @Override
    public int getCount() {
        return mDatabaseMap.size() / 2;
    }
}

package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Database;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Solar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.INVALID_POSITION;
import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

public class DatabaseContainer implements Database {

    private static final int[] DAYS_PER_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private static DatabaseContainer mInstance = new DatabaseContainer();

    private final HashMap<DatabaseKey, DatabaseItem> mDatabaseMap = new HashMap<>();

    private final Semaphore mInitSemaphore = new Semaphore(1);

    private final SolarTermDatabase mSolarTermDatabase = new SolarTermDatabase();

    private final SolarDatabase mSolarDatabase = new SolarDatabase();

    private final LunarDatabase mLunarDatabase = new LunarDatabase();

    private final ConstellationDatabase mConstellationDatabase = new ConstellationDatabase();

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

                int solarTermId;
                int constellationId;
                Solar solar = mSolarDatabase.firstDay();
                Lunar lunar = mLunarDatabase.firstDay();
                int position = 0;
                while (true) {
                    solarTermId = mSolarTermDatabase.get(solar);
                    constellationId = mConstellationDatabase.get(solar);
                    DatabaseItem item = new DatabaseItem(position, solar, lunar, solarTermId,
                            constellationId);
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
    public int getCount() {
        return mDatabaseMap.size() / 2;
    }

    private int getDaysInMonth(int year, int month) {
        int n = DAYS_PER_MONTH[month - 1];
        if (n != 28) {
            return n;
        }
        return isLeapYear(year) ? 29 : 28;
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
    }

}

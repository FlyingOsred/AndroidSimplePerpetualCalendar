/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract;
import com.flyingosred.app.android.perpetualcalendar.data.Lunar;

import java.util.ArrayList;
import java.util.Calendar;

public class PerpetualCalendarContentProvider extends ContentProvider {

    private static final String LOG_TAG = PerpetualCalendarContentProvider.class.getSimpleName();

    private static final int CODE_DATE = 1;

    private static final int CODE_DATE_DAY = 2;

    private static final UriMatcher URI_MATCHER;

    private LunarProvider mLunarProvider;

    private SolarTermProvider mSolarTermProvider;

    private ConstellationProvider mConstellationProvider;

    private HolidayProvider mHolidayProvider;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(PerpetualCalendarContract.AUTHORITY, "date", CODE_DATE);
        URI_MATCHER.addURI(PerpetualCalendarContract.AUTHORITY, "date/*", CODE_DATE_DAY);
    }

    public PerpetualCalendarContentProvider() {
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        Log.e(LOG_TAG, "Not supported.");
        return 0;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(LOG_TAG, "getType called uri is " + uri.toString());
        switch (URI_MATCHER.match(uri)) {
            case CODE_DATE:
                return PerpetualCalendarContract.PerpetualCalendar.CONTENT_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Log.e(LOG_TAG, "Not supported.");
        return null;
    }

    @Override
    public boolean onCreate() {
        mLunarProvider = new LunarProvider(getContext());
        mSolarTermProvider = new SolarTermProvider(getContext());
        mConstellationProvider = new ConstellationProvider(getContext());
        mHolidayProvider = new HolidayProvider(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "Query for " + uri.toString());
        switch (URI_MATCHER.match(uri)) {
            case CODE_DATE_DAY:
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(uri.getLastPathSegment()));
                String holidayRegion = null;
                if (selection != null && selectionArgs != null && selectionArgs.length > 0) {
                    holidayRegion = selectionArgs[0];
                }
                Log.d(LOG_TAG, "Query for " + calendar.getTime() + " region " + holidayRegion);
                return getDate(projection, calendar, holidayRegion);
        }
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.e(LOG_TAG, "Not supported.");
        return 0;
    }

    private Cursor getDate(String[] projection, Calendar calendar, String holidayRegion) {
        int id = 0;
        Lunar lunar = null;
        MatrixCursor cursor = new MatrixCursor(projection);
        ArrayList<Object> row = new ArrayList<Object>(projection.length);
        for (String column : projection) {
            if (column.equals(PerpetualCalendarContract.PerpetualCalendar._ID)) {
                row.add(id);
            } else if (column.equals(PerpetualCalendarContract.PerpetualCalendar.SOLAR_TIME_MILLIS)) {
                row.add(calendar.getTimeInMillis());
            } else if (column.equals(PerpetualCalendarContract.PerpetualCalendar.LUNAR_SHORT_NAME)) {
                if (lunar == null) {
                    lunar = mLunarProvider.get(calendar);
                }
                row.add(mLunarProvider.getLunarShortName(lunar));
            } else if (column.equals(PerpetualCalendarContract.PerpetualCalendar.LUNAR_FULL_NAME)) {
                if (lunar == null) {
                    lunar = mLunarProvider.get(calendar);
                }
                row.add(mLunarProvider.getLunarLongName(lunar));
            } else if (column.equals(PerpetualCalendarContract.PerpetualCalendar.SOLAR_TERM_NAME)) {
                row.add(mSolarTermProvider.getName(calendar));
            } else {
                row.add(null);
            }
        }
        cursor.addRow(row);
        return cursor;
    }
}

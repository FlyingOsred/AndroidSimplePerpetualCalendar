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
import com.flyingosred.app.android.perpetualcalendar.data.database.DatabaseHelper;
import com.flyingosred.app.android.perpetualcalendar.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class PerpetualCalendarContentProvider extends ContentProvider {

    private static final String LOG_TAG = PerpetualCalendarContentProvider.class.getSimpleName();

    private static final int CODE_DATE = 1;

    private static final int CODE_DATE_DAY = 2;

    private static final int CODE_DATE_RANGE = 3;

    private static final UriMatcher URI_MATCHER;

    private LunarProvider mLunarProvider;

    private SolarTermProvider mSolarTermProvider;

    private ConstellationProvider mConstellationProvider;

    private HolidayProvider mHolidayProvider;

    private DatabaseHelper mDbHelper;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(PerpetualCalendarContract.AUTHORITY, "date", CODE_DATE);
        URI_MATCHER.addURI(PerpetualCalendarContract.AUTHORITY, "date/*", CODE_DATE_DAY);
        URI_MATCHER.addURI(PerpetualCalendarContract.AUTHORITY, "date/*/*", CODE_DATE_RANGE);
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
        mDbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int id = URI_MATCHER.match(uri);
        Log.d(LOG_TAG, "Query for " + uri.toString());
        Log.d(LOG_TAG, "id " + id);
        String holidayRegion = null;
        switch (URI_MATCHER.match(uri)) {
            case CODE_DATE_DAY:
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(uri.getLastPathSegment()));
                if (selection != null && selectionArgs != null && selectionArgs.length > 0) {
                    holidayRegion = selectionArgs[0];
                }
                Log.d(LOG_TAG, "Query for " + calendar.getTime() + " region " + holidayRegion);
                return getDate(projection, calendar, holidayRegion);
            case CODE_DATE_RANGE:
                List<String> segments = uri.getPathSegments();
                Calendar startDate = Calendar.getInstance();
                startDate.setTimeInMillis(Long.parseLong(segments.get(1)));
                Calendar endDate = Calendar.getInstance();
                endDate.setTimeInMillis(Long.parseLong(segments.get(2)));
                if (selection != null && selectionArgs != null && selectionArgs.length > 0) {
                    holidayRegion = selectionArgs[0];
                }
                return query(projection, startDate, endDate, holidayRegion);
        }
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.e(LOG_TAG, "Not supported.");
        return 0;
    }

    private Cursor query(String[] projection, Calendar startDate, Calendar endDate,
                         String holidayRegion) {
        MatrixCursor cursor = new MatrixCursor(projection);
        Log.d(LOG_TAG, "Starting query startDate " + startDate.getTime() + " endDate " + endDate.getTime());
        int id = 0;
        Lunar lunar = mLunarProvider.get(startDate);
        do {
            ArrayList<Object> row = new ArrayList<>(projection.length);
            for (String column : projection) {
                switch (column) {
                    case PerpetualCalendarContract.PerpetualCalendar._ID:
                        row.add(id);
                        break;
                    case PerpetualCalendarContract.PerpetualCalendar.SOLAR_TIME_MILLIS:
                        row.add(startDate.getTimeInMillis());
                        break;
                    case PerpetualCalendarContract.PerpetualCalendar.LUNAR_SHORT_NAME:
                        row.add(mLunarProvider.getLunarShortName(lunar));
                        break;
//                    case PerpetualCalendarContract.PerpetualCalendar.LUNAR_FULL_NAME:
//                        if (lunar == null) {
//                            lunar = mLunarProvider.get(startDate);
//                        }
//                        row.add(mLunarProvider.getLunarLongName(lunar));
//                        break;
//                    case PerpetualCalendarContract.PerpetualCalendar.SOLAR_TERM_NAME:
//                        row.add(mSolarTermProvider.getName(startDate));
//                        break;
//                    case PerpetualCalendarContract.PerpetualCalendar.CONSTELLATION_NAME:
//                        row.add(mConstellationProvider.getName(startDate));
//                        break;
//                    case PerpetualCalendarContract.PerpetualCalendar.HOLIDAY_NAMES:
//                        List<String> list = mHolidayProvider.getHolidayNames(holidayRegion,
//                                startDate);
//                        if (list.size() > 0) {
//                            StringBuilder sb = new StringBuilder();
//                            for (int i = 0; i < list.size(); i++) {
//                                sb.append(list.get(i));
//                                if (i != list.size() - 1) {
//                                    sb.append(",");
//                                }
//                            }
//                            row.add(sb.toString());
//                        } else {
//                            row.add(null);
//                        }
//                        break;
                    default:
                        row.add(null);
                }
            }
            cursor.addRow(row);
            startDate.add(Calendar.DATE, 1);
            lunar = mLunarProvider.nextDay(lunar);
            id++;
        } while (!Utils.isDayAfter(startDate, endDate));
        Log.d(LOG_TAG, "Query end");
        return cursor;
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
            } else if (column.equals(PerpetualCalendarContract.PerpetualCalendar.CONSTELLATION_NAME)) {
                row.add(mConstellationProvider.getName(calendar));
            } else if (column.equals(PerpetualCalendarContract.PerpetualCalendar.HOLIDAY_REGION_FLAG_IMG)) {
                row.add(mHolidayProvider.getFlagImage(holidayRegion));
            } else if (column.equals(PerpetualCalendarContract.PerpetualCalendar.HOLIDAY_NAMES)) {
                List<String> list = mHolidayProvider.getHolidayNames(holidayRegion, calendar);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(bos);
                    oos.writeObject(list);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] bytes = bos.toByteArray();
                row.add(bytes);
            } else {
                row.add(null);
            }
        }
        cursor.addRow(row);
        return cursor;
    }
}

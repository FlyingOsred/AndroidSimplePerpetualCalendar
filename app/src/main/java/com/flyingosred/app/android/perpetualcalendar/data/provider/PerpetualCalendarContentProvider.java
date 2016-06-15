/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract;
import com.flyingosred.app.android.perpetualcalendar.data.database.DatabaseHelper;
import com.flyingosred.app.android.perpetualcalendar.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PerpetualCalendarContentProvider extends ContentProvider {

    private static final String LOG_TAG = PerpetualCalendarContentProvider.class.getSimpleName();

    private static final int CODE_DATE = 1;

    private static final int CODE_DATE_DAY = 2;

    private static final int CODE_DATE_RANGE = 3;

    private static final UriMatcher URI_MATCHER;

    private DatabaseHelper mDbHelper;

    private SQLiteDatabase mDatabase;

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
        mDbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int id = URI_MATCHER.match(uri);
        Log.d(LOG_TAG, "Query for " + uri.toString());
        Log.d(LOG_TAG, "id " + id);
        if (mDatabase == null) {
            mDatabase = mDbHelper.getReadableDatabase();
        }
        Calendar startDate = null;
        Calendar endDate = null;
        switch (URI_MATCHER.match(uri)) {
            case CODE_DATE_DAY:
                startDate = Calendar.getInstance();
                startDate.setTimeInMillis(Long.parseLong(uri.getLastPathSegment()));
                break;
            case CODE_DATE_RANGE:
                List<String> segments = uri.getPathSegments();
                startDate = Calendar.getInstance();
                startDate.setTimeInMillis(Long.parseLong(segments.get(1)));
                endDate = Calendar.getInstance();
                endDate.setTimeInMillis(Long.parseLong(segments.get(2)));
                break;
            default:
                break;
        }

        if (startDate != null) {
            return query(startDate, endDate, projection);
        }
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.e(LOG_TAG, "Not supported.");
        return 0;
    }

    private Cursor query(Calendar startDate, Calendar endDate, String[] projection) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        if (Utils.isDayBefore(startDate, PerpetualCalendarContract.MIN_DATE)) {
            startDate = PerpetualCalendarContract.MIN_DATE;
        }
        if (endDate != null && Utils.isDayAfter(endDate, PerpetualCalendarContract.MAX_DATE)) {
            endDate = PerpetualCalendarContract.MAX_DATE;
        }
        String selection;
        String[] selectionArgs;
        if (endDate == null) {
            selection = PerpetualCalendarContract.PerpetualCalendar.SOLAR + "=?";
            selectionArgs = new String[]{formatter.format(startDate.getTime())};

        } else {
            selection = PerpetualCalendarContract.PerpetualCalendar.SOLAR + " BETWEEN ? AND ?";
            selectionArgs = new String[]{formatter.format(startDate.getTime()),
                    formatter.format(endDate.getTime())};
        }
        return mDatabase.query(DatabaseHelper.CALENDAR_TABLE_NAME, projection, selection,
                selectionArgs, null, null, null, null);
    }
}

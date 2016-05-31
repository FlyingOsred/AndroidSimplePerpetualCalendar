/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.api.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Calendar;

public final class PerpetualCalendarContract {

    public static final String AUTHORITY = "com.flyingosred.app.android.perpetualcalendar.provider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String DEFAULT_SORT_ORDER = "_id ASC";

    private PerpetualCalendarContract() {
    }

    public static final class PerpetualCalendar implements BaseColumns, SolarColumns, LunarColumns,
            SolarTermColumns, HolidayColumns, ConstellationColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, "date");

        public static final String CONTENT_TYPE = AUTHORITY + ".date";

        private PerpetualCalendar() {
        }

        public static Cursor query(ContentResolver cr, Calendar calendar, String[] projection) {
            return query(cr, calendar, null, projection);
        }

        public static Cursor query(ContentResolver cr, Calendar calendar, String holidayRegion,
                                   String[] projection) {
            String selection = null;
            String[] selectionArgs = null;
            if (holidayRegion != null) {
                selection = Holiday.HOLIDAY_REGION_WHERE + "=?";
                selectionArgs = new String[]{holidayRegion};
            }
            Uri.Builder builder = CONTENT_URI.buildUpon();
            ContentUris.appendId(builder, calendar.getTimeInMillis());
            return cr.query(builder.build(), projection, selection, selectionArgs, DEFAULT_SORT_ORDER);
        }

    }

    protected interface SolarColumns {

        String SOLAR_TIME_MILLIS = "solar_time_millis";
    }

    protected interface LunarColumns {

        String LUNAR_YEAR = "lunar_year";

        String LUNAR_MONTH = "lunar_month";

        String LUNAR_DAY = "lunar_day";

        String LUNAR_IS_LEAP_MONTH = "lunar_is_leap_month";

        String LUNAR_DAYS_IN_MONTH = "lunar_days_in_month";

        String LUNAR_SHORT_NAME = "lunar_short_name";

        String LUNAR_LONG_NAME = "lunar_long_name";

        String LUNAR_FULL_NAME = "lunar_full_name";

    }

    protected interface SolarTermColumns {

        String SOLAR_TERM_ID = "solar_term_id";

        String SOLAR_TERM_NAME = "solar_term_name";

    }

    protected interface ConstellationColumns {

        String CONSTELLATION_ID = "constellation_id";

        String CONSTELLATION_NAME = "constellation_name";

        String CONSTELLATION_SYMBOL = "constellation_symbol";

    }

    protected interface HolidayColumns {

        String HOLIDAY_REGION = "holiday_region";

        String HOLIDAY_REGION_NAMES = "holiday_region_names";

        String HOLIDAY_REGION_FLAG_IMG = "holiday_region_flag_img";

        String HOLIDAY_NAMES = "holiday_names";

    }

    public static final class Holiday implements HolidayColumns {

        public static final String HOLIDAY_REGION_WHERE = HOLIDAY_REGION + "=?";

    }

}

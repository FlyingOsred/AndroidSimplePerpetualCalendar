/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.api.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public final class PerpetualCalendarContract {

    public static final String AUTHORITY = "com.flyingosred.app.android.perpetualcalendar.provider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String DEFAULT_SORT_ORDER = "id ASC";

    public static final Calendar MAX_DATE = Calendar.getInstance();

    public static final Calendar MIN_DATE = Calendar.getInstance();

    public static final int INVALID_ID = -1;

    static {
        MIN_DATE.set(1901, 1, 19, 0, 0, 0);
        MAX_DATE.set(2099, 11, 31, 0, 0, 0);
    }

    private PerpetualCalendarContract() {
    }

    protected interface SolarColumns {

        String SOLAR = "solar";
    }

    protected interface LunarColumns {

        String LUNAR_YEAR = "lunar_year";

        String LUNAR_MONTH = "lunar_month";

        String LUNAR_DAY = "lunar_day";

        String LUNAR_IS_LEAP_MONTH = "lunar_is_leap_month";

        String LUNAR_DAYS_IN_MONTH = "lunar_days_in_month";

    }

    protected interface SolarTermColumns {

        String SOLAR_TERM_ID = "solar_term_id";

    }

    protected interface ConstellationColumns {

        String CONSTELLATION_ID = "constellation_id";

    }

    protected interface HolidayColumns {

        String HOLIDAY_PREFIX = "holiday_";

    }

    public static final class PerpetualCalendar implements BaseColumns, SolarColumns, LunarColumns,
            SolarTermColumns, HolidayColumns, ConstellationColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, "date");

        public static final String CONTENT_TYPE = AUTHORITY + ".date";

        private PerpetualCalendar() {
        }

        public static Uri getUri(Calendar startDate, Calendar endDate) {
            Uri.Builder builder = CONTENT_URI.buildUpon();
            ContentUris.appendId(builder, startDate.getTimeInMillis());
            ContentUris.appendId(builder, endDate.getTimeInMillis());
            return builder.build();
        }

        public static String[] getDefaultProjection(String holidayRegion) {
            List<String> projectionList = new ArrayList<>();
            projectionList.add(_ID);
            projectionList.add(SOLAR);
            projectionList.add(LUNAR_YEAR);
            projectionList.add(LUNAR_MONTH);
            projectionList.add(LUNAR_DAY);
            projectionList.add(LUNAR_IS_LEAP_MONTH);
            projectionList.add(LUNAR_DAYS_IN_MONTH);
            projectionList.add(SOLAR_TERM_ID);
            projectionList.add(CONSTELLATION_ID);
            if (holidayRegion != null && holidayRegion.length() > 0) {
                projectionList.add(HOLIDAY_PREFIX + holidayRegion);
            }
            return projectionList.toArray(new String[projectionList.size()]);
        }
    }

    public static final class Lunar implements BaseColumns, LunarColumns {

        public static final int DAYS_IN_LARGE_MONTH = 30;

        public static final int DAYS_IN_SMALL_MONTH = 29;

        public static final int ERA_YEAR_START = 1864;

        public static final int ERA_ANIMAL_YEAR_START = 1900;

        private Lunar() {
        }

        public static String formatNumber(String[] array, int number) {
            if (number <= array.length) {
                return array[number - 1];
            }
            StringBuilder sb = new StringBuilder();
            int tensPlace = number / 10;
            if (tensPlace > 1) {
                sb.append(array[tensPlace - 1]);
            }
            sb.append(array[array.length - 1]);
            int unitPlace = number % 10;
            if (unitPlace != 0) {
                sb.append(array[unitPlace - 1]);
            }
            return sb.toString();
        }
    }

    public static final class SolarTerm implements BaseColumns, SolarTermColumns {

        public static final int MAX_ID = 24;

        private SolarTerm() {
        }
    }

    public static final class Constellation implements BaseColumns, ConstellationColumns {

        public static final int MAX_ID = 12;

        private Constellation() {
        }
    }
}

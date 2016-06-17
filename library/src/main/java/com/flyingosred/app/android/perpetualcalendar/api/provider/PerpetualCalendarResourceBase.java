/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.api.provider;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract.Lunar.ERA_ANIMAL_YEAR_START;
import static com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract.Lunar.ERA_YEAR_START;

public abstract class PerpetualCalendarResourceBase {

    protected static final String PREFIX_FLAG = "ic_flag_";

    private static final String WHITESPACE = " ";

    private static final String NAME_SUFFIX = "_name";

    private final Context mContext;

    public PerpetualCalendarResourceBase(Context context) {
        mContext = context;
    }

    public Calendar getCalendar(Cursor cursor) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String solar = cursor.getString(
                cursor.getColumnIndex(PerpetualCalendarContract.PerpetualCalendar.SOLAR));
        Date date = null;
        try {
            date = formatter.parse(solar);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        }

        return null;
    }

    public String getLunarShortName(Cursor cursor) {
        int year = getIntValue(cursor, PerpetualCalendarContract.PerpetualCalendar.LUNAR_YEAR);
        int month = getIntValue(cursor, PerpetualCalendarContract.PerpetualCalendar.LUNAR_MONTH);
        int day = getIntValue(cursor, PerpetualCalendarContract.PerpetualCalendar.LUNAR_DAY);
        boolean isLeapMonth = getBooleanValue(cursor, PerpetualCalendarContract.PerpetualCalendar.LUNAR_IS_LEAP_MONTH);
        int daysInMonth = getIntValue(cursor, PerpetualCalendarContract.PerpetualCalendar.LUNAR_DAYS_IN_MONTH);
        if (isChineseLocale()) {
            return formatLunarChineseShort(mContext, month, day, isLeapMonth, daysInMonth);
        }
        return month + "/" + day;
    }

    public String getLunarFullName(Cursor cursor) {
        int year = getIntValue(cursor, PerpetualCalendarContract.PerpetualCalendar.LUNAR_YEAR);
        int month = getIntValue(cursor, PerpetualCalendarContract.PerpetualCalendar.LUNAR_MONTH);
        int day = getIntValue(cursor, PerpetualCalendarContract.PerpetualCalendar.LUNAR_DAY);
        boolean isLeapMonth = getBooleanValue(cursor, PerpetualCalendarContract.PerpetualCalendar.LUNAR_IS_LEAP_MONTH);
        int daysInMonth = getIntValue(cursor, PerpetualCalendarContract.PerpetualCalendar.LUNAR_DAYS_IN_MONTH);
        if (isChineseLocale()) {
            return formatLunarChineseLong(mContext, year, month, day, isLeapMonth, daysInMonth);
        } else {
            return String.valueOf(year) + "/" + month + "/" + day;
        }
    }

    public String getSolarTermName(Cursor cursor) {
        int solarTermId = getIntValue(cursor, PerpetualCalendarContract.SolarTerm.SOLAR_TERM_ID);
        if (solarTermId > 0 && solarTermId <= PerpetualCalendarContract.SolarTerm.MAX_ID) {
            String[] names = getSolarTermNameArray(mContext);
            if (names != null && names.length == PerpetualCalendarContract.SolarTerm.MAX_ID) {
                return names[solarTermId - 1];
            }
        }
        return null;
    }

    public String getConstellationName(Cursor cursor) {
        int constellationId = getIntValue(cursor, PerpetualCalendarContract.Constellation.CONSTELLATION_ID);
        if (constellationId > 0 && constellationId <= PerpetualCalendarContract.Constellation.MAX_ID) {
            String[] names = getConstellationNameArray(mContext);
            if (names != null && names.length == PerpetualCalendarContract.Constellation.MAX_ID) {
                return names[constellationId - 1];
            }
        }
        return null;
    }

    public String[] getHolidayName(Cursor cursor, String region) {
        String columnName = PerpetualCalendarContract.PerpetualCalendar.HOLIDAY_PREFIX + region;
        String holidayString = getStringValue(cursor, columnName);
        ArrayList<String> mHolidayNameList = new ArrayList<>();
        if (holidayString != null) {
            String[] tempArray = holidayString.split("#");
            if (tempArray.length >= 1) {
                String tempString = tempArray[0];
                if (tempString.length() > 0) {
                    String[] holidays = tempString.split(",");
                    for (String holidayPattern : holidays) {
                        String[] festivals = holidayPattern.split(":");
                        String festivalArrayName = festivals[0] + NAME_SUFFIX;
                        int id = Integer.parseInt(festivals[1]);
                        int resId = mContext.getResources().getIdentifier(festivalArrayName,
                                "array", mContext.getPackageName());
                        if (resId == 0) {
                            continue;
                        }
                        String[] festivalNames = mContext.getResources().getStringArray(resId);
                        mHolidayNameList.add(festivalNames[id - 1]);
                    }
                }
            }
        }
        return mHolidayNameList.toArray(new String[mHolidayNameList.size()]);
    }

    public int getHolidayOffWork(Cursor cursor, String region) {
        String columnName = PerpetualCalendarContract.PerpetualCalendar.HOLIDAY_PREFIX + region;
        String holidayString = getStringValue(cursor, columnName);
        if (holidayString != null) {
            String[] tempArray = holidayString.split("#");
            if (tempArray.length == 2) {
                return Integer.parseInt(tempArray[1]);
            }
        }
        return PerpetualCalendarContract.INVALID_ID;
    }

    public String getHolidayOffWorkString(Cursor cursor, String region) {
        return getOffWorkString(mContext, getHolidayOffWork(cursor, region));
    }

    public abstract Drawable getRegionFlag(String region);

    public abstract String getOffWorkString(Context context, int offWork);

    protected abstract String[] getChineseNumber(Context context);

    protected abstract String getLunarLeapMonthPrefix(Context context);

    protected abstract String getLunarFirstMonthPrefix(Context context);

    protected abstract String getLunarLastMonthPrefix(Context context);

    protected abstract String getLunarMonthSuffix(Context context);

    protected abstract String getLunarLargeSmallSuffix(Context context, boolean isLarge);

    protected abstract String getLunarDayPrefix(Context context);

    protected abstract String[] getLunarEraYearStemArray(Context context);

    protected abstract String[] getLunarEraYearBranchArray(Context context);

    protected abstract String getLunarEraYear(Context context);

    protected abstract String[] getLunarAnimalSignArray(Context context);

    protected abstract String[] getSolarTermNameArray(Context context);

    protected abstract String[] getConstellationNameArray(Context context);

    protected Context getContext() {
        return mContext;
    }

    private String formatLunarChineseLong(Context context, int year, int month, int day,
                                          boolean isLeapMonth, int daysInMonth) {
        StringBuilder sb = new StringBuilder();
        String eraYearString = formatChineseEraYear(context, year);
        String monthString = formatLunarChineseMonth(context, month, isLeapMonth, daysInMonth, true);
        String dayString = formatLunarChineseDay(context, day);
        sb.append(eraYearString);
        sb.append(WHITESPACE);
        sb.append(monthString);
        sb.append(WHITESPACE);
        sb.append(dayString);
        return sb.toString();
    }

    private String formatChineseNumber(Context context, int number) {
        String[] array = getChineseNumber(context);
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

    private int getIntValue(Cursor cursor, String columnName) {
        int value = PerpetualCalendarContract.INVALID_ID;
        int index = cursor.getColumnIndex(columnName);
        if (index >= 0) {
            value = cursor.getInt(index);
        }
        return value;
    }

    private boolean getBooleanValue(Cursor cursor, String columnName) {
        boolean value = false;
        int index = cursor.getColumnIndex(columnName);
        if (index >= 0) {
            int intValue = cursor.getInt(index);
            if (intValue == 1) {
                value = true;
            }
        }
        return value;
    }

    private String getStringValue(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index >= 0) {
            return cursor.getString(index);
        }
        return null;
    }

    private String formatLunarChineseMonth(Context context, int month, boolean isLeapMonth,
                                           int daysInMonth, boolean largeOrSmall) {
        StringBuilder sb = new StringBuilder();
        if (isLeapMonth) {
            sb.append(getLunarLeapMonthPrefix(context));
        }
        String prefix;
        if (month == 1) {
            prefix = getLunarFirstMonthPrefix(context);
        } else if (month == 12) {
            prefix = getLunarLastMonthPrefix(context);
        } else {
            prefix = formatChineseNumber(context, month);
        }
        sb.append(prefix);
        sb.append(getLunarMonthSuffix(context));
        if (largeOrSmall) {
            boolean isLarge = daysInMonth == PerpetualCalendarContract.Lunar.DAYS_IN_LARGE_MONTH;
            sb.append(getLunarLargeSmallSuffix(context, isLarge));
        }
        return sb.toString();
    }

    private boolean isChineseLocale() {
        Locale locale = Locale.getDefault();
        return locale.equals(Locale.CHINESE) || locale.equals(Locale.CHINA)
                || locale.equals(Locale.SIMPLIFIED_CHINESE)
                || locale.equals(Locale.TRADITIONAL_CHINESE)
                || locale.equals(Locale.TAIWAN);
    }

    private String formatLunarChineseShort(Context context, int month, int day, boolean isLeapMonth,
                                           int daysInMonth) {
        if (day == 1) {
            return formatLunarChineseMonth(context, month, isLeapMonth, daysInMonth, false);
        }
        return formatLunarChineseDay(context, day);
    }

    private String formatLunarChineseDay(Context context, int day) {
        StringBuilder sb = new StringBuilder();
        if (day <= 10) {
            sb.append(getLunarDayPrefix(context));
        }
        sb.append(formatChineseNumber(context, day));
        return sb.toString();
    }

    private String formatChineseEraYear(Context context, int year) {
        int i = (year - ERA_YEAR_START) % 60;
        return getLunarEraYearStemArray(context)[i % 10] +
                getLunarEraYearBranchArray(context)[i % 12] +
                getLunarEraYear(context) +
                "【" +
                getLunarAnimalSignArray(context)[(year - ERA_ANIMAL_YEAR_START) % 12] +
                getLunarEraYear(context) +
                "】";
    }
}

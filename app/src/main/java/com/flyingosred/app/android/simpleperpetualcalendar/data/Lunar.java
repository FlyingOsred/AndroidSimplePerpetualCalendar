package com.flyingosred.app.android.simpleperpetualcalendar.data;

import android.content.Context;
import android.os.Parcelable;

import com.flyingosred.app.android.simpleperpetualcalendar.R;

import java.util.Locale;

import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.EMPTY_STRING;
import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.NEW_LINE;

public abstract class Lunar implements Parcelable {

    public static final int LUNAR_YEAR_MIN = 1901;

    public static final int LUNAR_YEAR_MAX = 2099;

    public static final int MONTHS_IN_YEAR = 12;

    public static final int DAYS_IN_LARGE_MONTH = 30;

    public static final int DAYS_IN_SMALL_MONTH = 29;

    public abstract int getYear();

    public abstract int getMonth();

    public abstract int getDay();

    public abstract int getDaysInMonth();

    public abstract boolean isLeapMonth();

    private static final int ERA_YEAR_START = 1864;

    private static final int ERA_ANIMAL_YEAR_START = 1900;

    private static String getChineseNumber(Context context, int number) {
        return getChineseNumber(context, number, false);
    }

    private static String getChineseNumber(Context context, int number, boolean vertical) {
        String[] array = context.getResources().getStringArray(R.array.chinese_number);
        if (number <= array.length) {
            return array[number - 1];
        }

        StringBuilder chineseNumber = new StringBuilder();
        int tensPlace = number / 10;
        if (tensPlace > 1) {
            chineseNumber.append(array[tensPlace - 1]);
            if (vertical) {
                chineseNumber.append(NEW_LINE);
            }
        }
        chineseNumber.append(array[array.length - 1]);
        if (vertical) {
            chineseNumber.append(NEW_LINE);
        }
        int unitPlace = number % 10;
        if (unitPlace != 0) {
            chineseNumber.append(array[unitPlace - 1]);
        }
        return chineseNumber.toString();
    }

    private static String getChineseMonthName(Context context, int month, boolean isLeapMonth) {
        StringBuilder sb = new StringBuilder();
        if (isLeapMonth) {
            sb.append(context.getString(R.string.lunar_leap));
        }
        String prefix = null;
        if (month == 1) {
            prefix = context.getString(R.string.chinese_first_month_prefix);
        } else if (month == 12) {
            prefix = context.getString(R.string.chinese_last_month_prefix);
        } else {
            prefix = getChineseNumber(context, month);
        }
        sb.append(prefix);
        sb.append(context.getString(R.string.chinese_month_name));
        return sb.toString();
    }

    private static String formatChineseMonthString(Context context, Lunar lunar, boolean largeSmall, boolean vertical) {
        StringBuilder sb = new StringBuilder();
        if (lunar.isLeapMonth()) {
            sb.append(context.getString(R.string.lunar_leap));
            if (vertical) {
                sb.append(NEW_LINE);
            }
        }
        String prefix;
        int month = lunar.getMonth();
        if (month == 1) {
            prefix = context.getString(R.string.chinese_first_month_prefix);
        } else if (month == 12) {
            prefix = context.getString(R.string.chinese_last_month_prefix);
        } else {
            prefix = getChineseNumber(context, month, vertical);
        }
        sb.append(prefix);
        if (vertical) {
            sb.append(NEW_LINE);
        }
        sb.append(context.getString(R.string.chinese_month_name));
        if (largeSmall) {
            sb.append(NEW_LINE);
            if (lunar.getDaysInMonth() == Lunar.DAYS_IN_LARGE_MONTH) {
                sb.append(context.getString(R.string.lunar_large_month));
            } else {
                sb.append(context.getString(R.string.lunar_small_month));
            }

        }
        return sb.toString();
    }

    public static String formatMonthDayString(Context context, Lunar lunar) {
        if (isChineseLocale()) {
            return formatChineseMonthDayString(context, lunar);
        }
        return lunar.getMonth() + "/" + lunar.getDay();
    }

    public static String formatChineseMonthDayString(Context context, Lunar lunar) {
        StringBuilder sb = new StringBuilder();
        String monthName = getChineseMonthName(context, lunar.getMonth(), lunar.isLeapMonth());
        sb.append(monthName);
        if (lunar.getDay() <= 10) {
            sb.append(context.getString(R.string.chinese_day_prefix_name));
        }
        sb.append(getChineseNumber(context, lunar.getDay()));
        return sb.toString();
    }

    public static String formatDayString(Context context, Lunar lunar) {
        return formatDayString(context, lunar, false);
    }

    public static String formatDayString(Context context, Lunar lunar, boolean vertical) {
        if (isChineseLocale()) {
            StringBuilder sb = new StringBuilder();
            if (lunar.getDay() <= 10) {
                sb.append(context.getString(R.string.chinese_day_prefix_name));
                if (vertical) {
                    sb.append(NEW_LINE);
                }
            }
            sb.append(getChineseNumber(context, lunar.getDay(), vertical));
            return sb.toString();
        }
        return EMPTY_STRING;
    }

    public static String formatMonthString(Context context, Lunar lunar, boolean largeSmall, boolean vertical) {
        if (isChineseLocale()) {
            return formatChineseMonthString(context, lunar, largeSmall, vertical);
        }
        return EMPTY_STRING;
    }

    public static String formatFullString(Context context, Lunar lunar) {
        String monthDayString = formatMonthDayString(context, lunar);
        String eraYear = getEraYear(context, lunar.getYear());
        StringBuilder sb = new StringBuilder();
        if (eraYear != null) {
            sb.append(eraYear);
            sb.append(" ");
        }
        sb.append(monthDayString);
        return sb.toString();
    }

    private static boolean isChineseLocale() {
        Locale locale = Locale.getDefault();
        if (locale.equals(Locale.CHINESE) || locale.equals(Locale.CHINA)
                || locale.equals(Locale.SIMPLIFIED_CHINESE)
                || locale.equals(Locale.TRADITIONAL_CHINESE)
                || locale.equals(locale.TAIWAN)) {
            return true;
        }
        return false;
    }

    private static String getEraYear(Context context, int year) {
        if (isChineseLocale()) {
            int i = (year - ERA_YEAR_START) % 60;
            StringBuilder sb = new StringBuilder();
            sb.append(context.getResources().getStringArray(R.array.era_stem)[i % 10]);
            sb.append(context.getResources().getStringArray(R.array.era_branch)[i % 12]);
            sb.append(context.getString(R.string.lunar_era_year));
            sb.append("【");
            sb.append(context.getResources().getStringArray(R.array.animal_sign)[(year - ERA_ANIMAL_YEAR_START) % 12]);
            sb.append(context.getString(R.string.lunar_era_year));
            sb.append("】");
            return sb.toString();
        }
        return null;
    }
}



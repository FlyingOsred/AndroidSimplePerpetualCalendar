package com.flyingosred.app.android.simpleperpetualcalendar.data;

import android.content.Context;
import android.util.Log;

import com.flyingosred.app.android.simpleperpetualcalendar.R;
import com.flyingosred.app.android.simpleperpetualcalendar.util.Utils;

import java.util.Locale;

public abstract class Lunar {

    public static final int LUNAR_YEAR_MIN = 1901;

    public static final int LUNAR_YEAR_MAX = 2099;

    public static final int MONTHS_IN_YEAR = 12;

    public static final int MAX_DAY_IN_MONTH = 30;

    public abstract int getYear();

    public abstract int getMonth();

    public abstract int getDay();

    public abstract boolean isLastDayInMonth();

    public abstract boolean isLeapMonth();

    private static String getChineseNumber(Context context, int number) {
        String[] array = context.getResources().getStringArray(R.array.chinese_number);
        if (number <= array.length) {
            return array[number - 1];
        }

        StringBuilder chineseNumber = new StringBuilder();
        int tensPlace = number / 10;
        if (tensPlace > 1) {
            chineseNumber.append(array[tensPlace - 1]);
        }
        chineseNumber.append(array[array.length - 1]);
        int unitPlace = number % 10;
        if (unitPlace != 0) {
            chineseNumber.append(array[unitPlace - 1]);
        }
        return chineseNumber.toString();
    }

    private static String getChineseMonthName(Context context, int month) {
        String prefix = null;
        if (month == 1) {
            prefix = context.getString(R.string.chinese_first_month_prefix);
        } else if (month == 12) {
            prefix = context.getString(R.string.chinese_last_month_prefix);
        } else {
            prefix = getChineseNumber(context, month);
        }
        return prefix + context.getString(R.string.chinese_month_name);
    }

    public static String formatMonthDayString(Context context, int month, int day) {
        Locale locale = Locale.getDefault();
        if (locale.equals(Locale.CHINESE) || locale.equals(Locale.CHINA)
                || locale.equals(Locale.SIMPLIFIED_CHINESE)
                || locale.equals(Locale.TRADITIONAL_CHINESE)
                || locale.equals(locale.TAIWAN)) {
            return formatChineseMonthDayString(context, month, day);
        }
        return month + "/" + day;
    }

    private static String formatChineseMonthDayString(Context context, int month, int day) {
        StringBuilder sb = new StringBuilder();
        String monthName = getChineseMonthName(context, month);
        sb.append(monthName);
        if (day <= 10) {
            sb.append(context.getString(R.string.chinese_day_prefix_name));
        }
        sb.append(getChineseNumber(context, day));
        return sb.toString();
    }
}



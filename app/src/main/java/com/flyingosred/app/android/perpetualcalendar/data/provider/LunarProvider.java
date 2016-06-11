package com.flyingosred.app.android.perpetualcalendar.data.provider;

import android.content.Context;
import android.util.Log;

import com.flyingosred.app.android.perpetualcalendar.R;
import com.flyingosred.app.android.perpetualcalendar.data.Lunar;
import com.flyingosred.app.android.perpetualcalendar.data.database.LunarDatabase;
import com.flyingosred.app.android.perpetualcalendar.util.Utils;

import java.util.Calendar;
import java.util.Locale;

import static com.flyingosred.app.android.perpetualcalendar.util.Utils.EMPTY_STRING;
import static com.flyingosred.app.android.perpetualcalendar.util.Utils.WHITESPACE;

public final class LunarProvider extends BaseProvider {

    public LunarProvider(Context context) {
        super(context);
    }

    public Lunar get(Calendar solarCalendar) {

        int lunarYear = solarCalendar.get(Calendar.YEAR);
        int lunarMonth = 0;
        int lunarDay;

        boolean isLeapMonth = false;

        Calendar calendar = LunarDatabase.getSpringFestivalDay(lunarYear);

        int daysInMonth = 0;

        if (Utils.isSameDay(solarCalendar, calendar)) {
            lunarMonth = 1;
            lunarDay = 1;
        } else {
            if (Utils.isDayBefore(solarCalendar, calendar)) {
                lunarYear--;
                calendar = LunarDatabase.getSpringFestivalDay(lunarYear);
            }

            int leapMonth = LunarDatabase.getLeapMonth(lunarYear);
            for (int lunarMonthIndex = 1, bit = 0; lunarMonthIndex <= 12; lunarMonthIndex++, bit++) {
                if (leapMonth > 0 && (lunarMonthIndex == leapMonth + 1) && !isLeapMonth) {
                    lunarMonthIndex--;
                    isLeapMonth = true;
                } else {
                    isLeapMonth = false;
                }
                daysInMonth = LunarDatabase.getDaysInMonth(lunarYear, bit);
                calendar.add(Calendar.DATE, daysInMonth);
                if (Utils.isDayAfter(calendar, solarCalendar)) {
                    calendar.add(Calendar.DATE, 0 - daysInMonth);
                    lunarMonth = lunarMonthIndex;
                    break;
                }
            }
            lunarDay = 1;
            do {
                if (Utils.isSameDay(solarCalendar, calendar)) {
                    break;
                }
                calendar.add(Calendar.DATE, 1);
                lunarDay++;
            } while (true);
        }
        return new LunarDate(lunarYear, lunarMonth, lunarDay, daysInMonth, isLeapMonth);
    }

    public Lunar nextDay(Lunar previous) {
        int day = previous.getDay();
        int month = previous.getMonth();
        int year = previous.getYear();
        boolean isLeapMonth = previous.isLeapMonth();
        int leapMonth = LunarDatabase.getLeapMonth(year);
        day++;
        int daysInMonth = LunarDatabase.getDaysInMonth(year, month, isLeapMonth);
        if (day > daysInMonth) {
            day = 1;
            if (month == leapMonth && !previous.isLeapMonth()) {
                isLeapMonth = true;
            } else {
                isLeapMonth = false;
                month++;
            }
        }
        if (month > Lunar.MONTHS_IN_YEAR) {
            month = 1;
            year++;
        }
        daysInMonth = LunarDatabase.getDaysInMonth(year, month, isLeapMonth);
        return new LunarDate(year, month, day, daysInMonth, isLeapMonth);
    }

    public String getLunarLongName(Lunar lunar) {
        StringBuilder sb = new StringBuilder();
        if (isChineseLocale()) {
            String eraYear = formatChineseEraYear(lunar.getYear());
            String month = formatLunarChineseMonth(lunar, true);
            String day = formatDayString(lunar);
            sb.append(eraYear);
            sb.append(WHITESPACE);
            sb.append(month);
            sb.append(WHITESPACE);
            sb.append(day);
        } else {
            sb.append(lunar.getYear()).append("/").append(lunar.getMonth())
                    .append("/").append(lunar.getDay());
        }
        return sb.toString();
    }

    public String getLunarShortName(Lunar lunar) {
        if (isChineseLocale()) {
            return formatLunarChineseShort(lunar);
        }
        return lunar.getMonth() + "/" + lunar.getDay();
    }

    private String formatLunarChineseShort(Lunar lunar) {
        if (lunar.getDay() == 1) {
            return formatLunarChineseMonth(lunar);
        }
        return formatLunarChineseDay(lunar);
    }


    private String formatLunarChineseDay(Lunar lunar) {
        StringBuilder sb = new StringBuilder();
        if (lunar.getDay() <= 10) {
            sb.append(getContext().getString(R.string.chinese_day_prefix_name));
        }
        sb.append(formatChineseNumber(lunar.getDay()));
        return sb.toString();
    }

    private String formatLunarChineseMonth(Lunar lunar) {
        return formatLunarChineseMonth(lunar, false);
    }

    private String formatLunarChineseMonth(Lunar lunar, boolean largeOrSmall) {
        StringBuilder sb = new StringBuilder();
        if (lunar.isLeapMonth()) {
            sb.append(getContext().getString(R.string.lunar_leap));
        }
        String prefix;
        int month = lunar.getMonth();
        if (month == 1) {
            prefix = getContext().getString(R.string.chinese_first_month_prefix);
        } else if (month == 12) {
            prefix = getContext().getString(R.string.chinese_last_month_prefix);
        } else {
            prefix = formatChineseNumber(month);
        }
        sb.append(prefix);
        sb.append(getContext().getString(R.string.chinese_month_name));
        if (largeOrSmall) {
            if (lunar.getDaysInMonth() == Lunar.DAYS_IN_LARGE_MONTH) {
                sb.append(getContext().getString(R.string.lunar_large_month));
            } else {
                sb.append(getContext().getString(R.string.lunar_small_month));
            }

        }
        return sb.toString();
    }

    private String formatChineseNumber(int number) {
        String[] array = getContext().getResources().getStringArray(R.array.chinese_number);
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

    private boolean isChineseLocale() {
        Locale locale = Locale.getDefault();
        return locale.equals(Locale.CHINESE) || locale.equals(Locale.CHINA)
                || locale.equals(Locale.SIMPLIFIED_CHINESE)
                || locale.equals(Locale.TRADITIONAL_CHINESE)
                || locale.equals(Locale.TAIWAN);
    }

    private String formatChineseEraYear(int year) {
        int i = (year - Lunar.ERA_YEAR_START) % 60;
        return getContext().getResources().getStringArray(R.array.era_stem)[i % 10] +
                getContext().getResources().getStringArray(R.array.era_branch)[i % 12] +
                getContext().getString(R.string.lunar_era_year) +
                "【" +
                getContext().getResources().getStringArray(
                        R.array.animal_sign)[(year - Lunar.ERA_ANIMAL_YEAR_START) % 12] +
                getContext().getString(R.string.lunar_era_year) +
                "】";
    }

    private String formatDayString(Lunar lunar) {
        if (isChineseLocale()) {
            StringBuilder sb = new StringBuilder();
            if (lunar.getDay() <= 10) {
                sb.append(getContext().getString(R.string.chinese_day_prefix_name));
            }
            sb.append(formatChineseNumber(lunar.getDay()));
            return sb.toString();
        }
        return EMPTY_STRING;
    }

}

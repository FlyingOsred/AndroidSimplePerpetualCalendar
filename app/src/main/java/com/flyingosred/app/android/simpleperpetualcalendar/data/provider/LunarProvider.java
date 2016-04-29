package com.flyingosred.app.android.simpleperpetualcalendar.data.provider;

import com.flyingosred.app.android.simpleperpetualcalendar.data.database.LunarDatabase;
import com.flyingosred.app.android.simpleperpetualcalendar.data.database.LunarDatabaseItem;
import com.flyingosred.app.android.simpleperpetualcalendar.util.Utils;

import java.util.Calendar;

public class LunarProvider {

    private final LunarDatabase mDatabase = new LunarDatabase();

    public LunarDatabaseItem get(Calendar solarCalendar) {

        int lunarYear = solarCalendar.get(Calendar.YEAR);
        int lunarMonth = 0;
        int lunarDay;

        boolean isLastDayInMonth = false;
        boolean isLeapMonth = false;

        Calendar calendar = mDatabase.getSpringFestivalDay(lunarYear);

        int daysInMonth = 0;

        if (Utils.isSameDay(solarCalendar, calendar)) {
            lunarMonth = 1;
            lunarDay = 1;
        } else {
            if (Utils.isDayBefore(solarCalendar, calendar)) {
                lunarYear--;
                calendar = mDatabase.getSpringFestivalDay(lunarYear);
            }

            int leapMonth = mDatabase.getLeapMonth(lunarYear);

            for (int lunarMonthIndex = 1, bit = 0; lunarMonthIndex <= 12; lunarMonthIndex++, bit++) {
                if (leapMonth > 0 && (lunarMonthIndex == leapMonth + 1) && !isLeapMonth) {
                    lunarMonthIndex--;
                    isLeapMonth = true;
                } else {
                    isLeapMonth = false;
                }
                daysInMonth = mDatabase.getDaysInMonth(lunarYear, bit);
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
            if (lunarDay == daysInMonth) {
                isLastDayInMonth = true;
            }
        }
        LunarDatabaseItem lunarDate = new LunarDatabaseItem();
        lunarDate.setYear(lunarYear);
        lunarDate.setMonth(lunarMonth);
        lunarDate.setDay(lunarDay);
        lunarDate.setLastDayInMonth(isLastDayInMonth);
        lunarDate.setLeapMonth(isLeapMonth);
        return lunarDate;
    }
}

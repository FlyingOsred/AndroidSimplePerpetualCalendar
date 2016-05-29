package com.flyingosred.app.android.perpetualcalendar.data.provider;

import android.content.Context;

import com.flyingosred.app.android.perpetualcalendar.data.Lunar;
import com.flyingosred.app.android.perpetualcalendar.data.database.LunarDatabase;
import com.flyingosred.app.android.perpetualcalendar.util.Utils;

import java.util.Calendar;

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
                daysInMonth = LunarDatabase.getDaysInMonth(lunarYear, bit, isLeapMonth);
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
}

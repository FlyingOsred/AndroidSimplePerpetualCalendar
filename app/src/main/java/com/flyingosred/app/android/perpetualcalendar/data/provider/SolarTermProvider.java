/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.provider;

import android.content.Context;
import android.util.Log;

import com.flyingosred.app.android.perpetualcalendar.R;
import com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract;
import com.flyingosred.app.android.perpetualcalendar.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class SolarTermProvider extends BaseProvider {

    private static final String LOG_TAG = SolarTermProvider.class.getSimpleName();

    private static final String ARRAYS_PREFIX = "solar_term_date_";

    public SolarTermProvider(Context context) {
        super(context);
    }

    public String getName(Calendar calendar) {
        int id = -1;
        int year = calendar.get(Calendar.YEAR);
        String arrayName = ARRAYS_PREFIX + year;
        int resId = getContext().getResources().getIdentifier(arrayName, "array",
                getContext().getPackageName());
        if (resId > 0) {
            String[] dates = getContext().getResources().getStringArray(resId);
            for (int i = 0; i < dates.length; i++) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
                try {
                    Date date = formatter.parse(dates[i]);
                    Calendar tempCalendar = Calendar.getInstance();
                    tempCalendar.setTime(date);
                    if (Utils.isDayAfter(tempCalendar, calendar)) {
                        break;
                    }
                    if (Utils.isSameDay(calendar, tempCalendar)) {
                        Log.d(LOG_TAG, "Found id " + i + " for " + calendar.getTime());
                        id = i;
                        break;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (id >= 0) {
            String[] nameArray = getContext().getResources().getStringArray(R.array.solar_term_name);
            if (nameArray != null && nameArray.length > id) {
                return nameArray[id];
            }
        }
        return null;
    }
}

/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.provider;

import android.content.Context;

import com.flyingosred.app.android.perpetualcalendar.R;
import com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract;
import com.flyingosred.app.android.perpetualcalendar.data.database.ConstellationDatabase;

import java.util.Calendar;

public final class ConstellationProvider extends BaseProvider {

    public ConstellationProvider(Context context) {
        super(context);
    }

    public String getName(Calendar calendar) {
        int id = getId(calendar);
        if (id != PerpetualCalendarContract.Constellation.INVALID) {
            String[] nameArray = getContext().getResources().getStringArray(R.array.constellation_name);
            if (nameArray != null && nameArray.length > id) {
                return nameArray[id];
            }
        }
        return null;
    }

    private int getId(Calendar calendar) {
        int id = PerpetualCalendarContract.Constellation.INVALID;
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        for (int i = 0; i < ConstellationDatabase.CONSTELLATION_DATE.length; i++) {
            int[] date = ConstellationDatabase.CONSTELLATION_DATE[i];
            if ((month == date[0] && day >= date[1]) || (month == date[2] && day <= date[3])) {
                id = i;
                break;
            }
        }
        return id;
    }
}

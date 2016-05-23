/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import android.util.Log;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Constellation;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Solar;

import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

public final class ConstellationDatabase {

    private static final int[][] CONSTELLATION_DATE = {
            {1, 20, 2, 18},
            {2, 19, 3, 20},
            {3, 21, 4, 19},
            {4, 20, 5, 20},
            {5, 21, 6, 21},
            {6, 22, 7, 22},
            {7, 23, 8, 22},
            {8, 23, 9, 22},
            {9, 23, 10, 22},
            {10, 23, 11, 21},
            {11, 22, 12, 21},
            {12, 22, 1, 19}
    };

    public Constellation get(Solar solar) {
        int month = solar.getMonth();
        int day = solar.getDay();
        for (int i = 0; i < CONSTELLATION_DATE.length; i++) {
            int[] date = CONSTELLATION_DATE[i];
            if ((month == date[0] && day >= date[1]) || (month == date[2] && day <= date[3])) {
                return new ConstellationDatabaseItem(i);
            }
        }
        Log.e(LOG_TAG, "No Constellation found for month " + month + " day " + day);
        return null;
    }
}

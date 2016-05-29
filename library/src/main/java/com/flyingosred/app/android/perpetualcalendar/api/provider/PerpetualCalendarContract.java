/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.api.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;

import java.util.Calendar;

public final class PerpetualCalendarContract {

    public static final String AUTHORITY = "com.flyingosred.app.android.perpetualcalendar.provider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final String CONTENT_PROVIDER_PACKAGE_NAME = "com.flyingosred.app.android.perpetualcalendar";

    private PerpetualCalendarContract() {
    }

    public static final class SolarTerm {

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/solar_term");

        public static final String NAME_ARRAY = "solar_term_name";

        public static final int INVALID = -1;

        private static final String ID = "id";

        private SolarTerm() {
        }

        public static int get(Context context, Calendar calendar) {
            int id = INVALID;
            Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(calendar.getTimeInMillis()));
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                int index = cursor.getColumnIndex(ID);
                if (index >= 0) {
                    id = cursor.getInt(index);
                }
                cursor.close();
            }
            return id;
        }

        public static String getName(Context context, int id) {
            if (id >= 0) {
                PackageManager pm = context.getPackageManager();
                try {
                    Resources resources = pm.getResourcesForApplication(CONTENT_PROVIDER_PACKAGE_NAME);
                    int arrayId = resources.getIdentifier(NAME_ARRAY, "array", CONTENT_PROVIDER_PACKAGE_NAME);
                    if (arrayId != 0) {
                        String[] names = resources.getStringArray(arrayId);
                        if (names != null && names.length > id) {
                            return names[id];
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public static final class Constellation {
        public static final int INVALID = -1;

    }

}

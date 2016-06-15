/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.loader;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract;

import static com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract.MAX_DATE;
import static com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract.MIN_DATE;
import static com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract.PerpetualCalendar.getDefaultProjection;

public class MainCursorLoader extends CursorLoader {

    public MainCursorLoader(Context context, String region) {
        super(context, PerpetualCalendarContract.PerpetualCalendar.getUri(MIN_DATE, MAX_DATE),
                getDefaultProjection(region), null, null, null);
    }
}

/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data;

import java.util.Calendar;

public interface Database {

    int getCount();

    PerpetualCalendar get(int position);

    int getPosition(Calendar calendar);

    int getPosition(int year, int month, int day);

}

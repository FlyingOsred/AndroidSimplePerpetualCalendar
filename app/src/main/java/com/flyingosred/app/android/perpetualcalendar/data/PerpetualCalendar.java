/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data;

import android.os.Parcelable;

import java.util.List;

public interface PerpetualCalendar extends Parcelable {

    int INVALID_ID = -1;

    int INVALID_POSITION = -1;

    int START_YEAR = 1901;

    int MONTHS_IN_YEAR = 12;

    int DAYS_IN_WEEK = 7;

    Solar getSolar();

    Lunar getLunar();

    int getSolarTermId();

    Constellation getConstellation();

    List<Holiday> getHolidayList();
}

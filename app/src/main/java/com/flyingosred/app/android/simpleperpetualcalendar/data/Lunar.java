/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simpleperpetualcalendar.data;

import android.os.Parcelable;

public interface Lunar extends Parcelable {

    int LUNAR_YEAR_MIN = 1901;

    int LUNAR_YEAR_MAX = 2099;

    int MONTHS_IN_YEAR = 12;

    int DAYS_IN_LARGE_MONTH = 30;

    int DAYS_IN_SMALL_MONTH = 29;

    int ERA_YEAR_START = 1864;

    int ERA_ANIMAL_YEAR_START = 1900;

    int getYear();

    int getMonth();

    int getDay();

    int getDaysInMonth();

    boolean isLeapMonth();

}



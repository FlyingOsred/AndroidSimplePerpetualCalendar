/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data;

import android.os.Parcelable;

public interface Solar extends Parcelable {

    int START_YEAR = 1901;

    int START_MONTH = 2;

    int START_DAY = 19;

    int END_YEAR = 2099;

    int END_MONTH = 12;

    int END_DAY = 31;

    int getYear();

    int getMonth();

    int getDay();
}

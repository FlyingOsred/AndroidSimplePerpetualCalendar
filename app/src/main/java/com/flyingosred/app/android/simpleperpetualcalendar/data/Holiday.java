/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simpleperpetualcalendar.data;

import android.os.Parcelable;

public interface Holiday extends Parcelable {

    int INVALID_FIELD = -1;

    int TYPE_WORK = 0;

    int TYPE_OFF = 1;

    String getRegion();

    int getId();

    int getOffOrWork();
}

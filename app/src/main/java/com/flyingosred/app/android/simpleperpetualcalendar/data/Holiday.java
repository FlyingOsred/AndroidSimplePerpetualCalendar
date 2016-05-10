package com.flyingosred.app.android.simpleperpetualcalendar.data;

public interface Holiday {

    int INVALID_FIELD = -1;

    int TYPE_WORK = 0;

    int TYPE_OFF = 1;

    String getRegion();

    int getId();

    int getOffOrWork();

}

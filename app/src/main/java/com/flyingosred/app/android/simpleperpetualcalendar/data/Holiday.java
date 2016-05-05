package com.flyingosred.app.android.simpleperpetualcalendar.data;

public interface Holiday {

    public static final int REGION_INTERNATIONAL = 0;

    public static final int REGION_CHINA = 1 << 0;

    public static final int REGION_TAIWAN = 1 << 1;

    public static final int REGION_HONGKONG = 1 << 2;

    public static final int REGION_ID_OFFSET = 1000;

    public static final int INVALID_FIELD = -1;

}

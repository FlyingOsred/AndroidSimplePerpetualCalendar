package com.flyingosred.app.android.simpleperpetualcalendar.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class HolidayRegion {

    private static final int CHINA_ID = 5;

    private static final int TAIWAN_ID = 6;

    private static final int HONGKONG_ID = 7;

    public static final int ALL_ID = 5000;

    public static final HolidayRegion CHINA = new HolidayRegion(CHINA_ID);

    public static final HolidayRegion TAIWAN = new HolidayRegion(TAIWAN_ID);

    public static final HolidayRegion HONGKONG = new HolidayRegion(HONGKONG_ID);

    public static final List<HolidayRegion> CHINA_ALL = new ArrayList<>(Arrays.asList(CHINA, TAIWAN, HONGKONG));

    public static final HolidayRegion ALL = new HolidayRegion(ALL_ID);

    private int mRegion;

    private HolidayRegion(int region) {
        mRegion = region;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof HolidayRegion) {
            HolidayRegion o = (HolidayRegion) object;
            return mRegion == o.mRegion;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(mRegion).hashCode();
    }

    public boolean isRegion(int region) {
        return mRegion == region;
    }
}

package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Solar;

import java.util.Calendar;
import java.util.List;

public class DatabaseItem implements PerpetualCalendar {

    private final Solar mSolar;

    private final Lunar mLunar;

    private final int mSolarTermId;

    private final int mConstellationId;

    private final int mPosition;

    private final List<Holiday> mHolidayList;

    public DatabaseItem(int position, Solar solar, Lunar lunar, int solarTermId,
                        int constellationId, List<Holiday> holidayList) {
        mPosition = position;
        mSolar = solar;
        mLunar = lunar;
        mSolarTermId = solarTermId;
        mConstellationId = constellationId;
        mHolidayList = holidayList;
    }

    @Override
    public Solar getSolar() {
        return mSolar;
    }

    @Override
    public Lunar getLunar() {
        return mLunar;
    }

    @Override
    public int getSolarTermId() {
        return mSolarTermId;
    }

    @Override
    public int getConstellationId() {
        return mConstellationId;
    }

    @Override
    public List<Holiday> getHolidayList() {
        return mHolidayList;
    }

    public int getPosition() {
        return mPosition;
    }
}

package com.flyingosred.app.android.simpleperpetualcalendar.data.provider;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.database.LunarDatabaseItem;
import com.flyingosred.app.android.simpleperpetualcalendar.data.database.SolarTermDatabaseItem;

import java.util.Calendar;

public class ContentItem implements PerpetualCalendar {

    private final Calendar mCalendar;

    private final Lunar mLunar;

    private final int mSolarTermId;

    private final int mPosition;

    public ContentItem(int position, Calendar calendar, Lunar lunar, int solarTermId) {
        mPosition = position;
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(calendar.getTimeInMillis());
        mLunar = lunar;
        mSolarTermId = solarTermId;
    }

    @Override
    public Calendar get() {
        return mCalendar;
    }

    @Override
    public Lunar getLunar() {
        return mLunar;
    }

    @Override
    public int getSolarTermId() {
        return mSolarTermId;
    }

    public int getPosition() {
        return mPosition;
    }
}

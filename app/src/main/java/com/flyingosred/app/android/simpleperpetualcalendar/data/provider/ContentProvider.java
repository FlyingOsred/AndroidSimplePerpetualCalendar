package com.flyingosred.app.android.simpleperpetualcalendar.data.provider;

import com.flyingosred.app.android.simpleperpetualcalendar.data.database.LunarDatabaseItem;

import java.util.Calendar;
import java.util.TimeZone;

public final class ContentProvider {

    private final LunarProvider mLunarProvider = new LunarProvider();
    private final SolarTermProvider mSolarTermProvider;

    public ContentProvider(TimeZone timeZone) {
        mSolarTermProvider = new SolarTermProvider(timeZone);
    }

    public ContentItem get(int position, Calendar calendar) {
        LunarDatabaseItem lunarItem = mLunarProvider.get(calendar);

        int solarTermId = mSolarTermProvider.get(calendar);

        ContentItem contentItem = new ContentItem(position, calendar, lunarItem, solarTermId);

        return contentItem;
    }
}

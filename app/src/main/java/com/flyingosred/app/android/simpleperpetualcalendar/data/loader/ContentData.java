package com.flyingosred.app.android.simpleperpetualcalendar.data.loader;

import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.provider.ContentItem;

import java.security.Key;
import java.util.Calendar;
import java.util.HashMap;

class ContentData implements Content {

    private final HashMap<ContentKey, ContentItem> mContentMap = new HashMap<>();

    public void add(int position, ContentItem calendar) {
        ContentKey key = new ContentKey(position, calendar.get());
        mContentMap.put(key, calendar);
    }

    @Override
    public int getCount() {
        return mContentMap.size();
    }

    @Override
    public PerpetualCalendar get(int position) {
        ContentKey key = new ContentKey(position);
        if (mContentMap.containsKey(key)) {
            return mContentMap.get(key);
        }
        return null;
    }

    @Override
    public int get(Calendar calendar) {
        ContentKey key = new ContentKey(calendar);
        if (mContentMap.containsKey(key)) {
            ContentItem contentItem = mContentMap.get(key);
            return contentItem.getPosition();
        }
        return Content.INVALID_POSITION;
    }
}

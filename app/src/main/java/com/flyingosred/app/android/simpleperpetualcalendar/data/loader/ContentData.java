package com.flyingosred.app.android.simpleperpetualcalendar.data.loader;

import android.util.Log;

import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.util.Utils;

import java.util.HashMap;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.DAYS_IN_WEEK;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.START_DAY_OF_WEEK;
import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

class ContentData implements Content {

    private final HashMap<ContentKey, ContentItem> mContentMap = new HashMap<>();

    private int mFrontOffset = 0;

    public void add(ContentItem item) {
        ContentKey keyPosition = new ContentKey(item.getPosition());
        mContentMap.put(keyPosition, item);
        ContentKey keyDate = new ContentKey(item.getYear(), item.getMonth(), item.getDay());
        mContentMap.put(keyDate, item);
    }

    @Override
    public int getCount() {
        return mContentMap.size() / 2 + mFrontOffset;
    }

    @Override
    public PerpetualCalendar get(int position) {
        Log.d(LOG_TAG, "Get item for position " + position);
        if (position < mFrontOffset) {
            return null;
        }
        ContentKey key = new ContentKey(position - mFrontOffset);
        if (mContentMap.containsKey(key)) {
            return mContentMap.get(key);
        }
        return null;
    }

    @Override
    public int get(int year, int month, int day) {
        ContentKey key = new ContentKey(year, month, day);
        if (mContentMap.containsKey(key)) {
            ContentItem contentItem = mContentMap.get(key);
            return contentItem.getPosition();
        }
        return Content.INVALID_POSITION;
    }

    public void computeOffset(int firstDayOfWeek) {
        mFrontOffset = findDayOffset(START_DAY_OF_WEEK, firstDayOfWeek, DAYS_IN_WEEK);
        Log.d(LOG_TAG, "Front offset is " + mFrontOffset);
    }

    private int findDayOffset(int dayOfWeekStart, int firstDayOfWeek, int maxDaysInWeek) {
        final int offset = dayOfWeekStart - firstDayOfWeek;
        if (dayOfWeekStart < firstDayOfWeek) {
            return offset + maxDaysInWeek;
        }
        return offset;
    }
}

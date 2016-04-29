package com.flyingosred.app.android.simpleperpetualcalendar.data.loader;

import com.flyingosred.app.android.simpleperpetualcalendar.util.Utils;

import java.util.Calendar;

final class ContentKey {

    private final Calendar mCalendar;

    private final int mPosition;

    public ContentKey(int position) {
        this(position, null);
    }

    public ContentKey(Calendar calendar) {
        this(Content.INVALID_POSITION, calendar);
    }

    public ContentKey(int position, Calendar calendar) {
        if (calendar != null) {
            mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(calendar.getTimeInMillis());
        } else {
            mCalendar = null;
        }
        mPosition = position;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (o.getClass() != getClass())
            return false;
        ContentKey key = (ContentKey) o;
        if (Utils.isSameDay(key.get(), mCalendar) || (key.getPosition() == mPosition)) {
            return true;
        }
        return false;
    }

    public Calendar get() {
        return mCalendar;
    }

    public int getPosition() {
        return mPosition;
    }
}

package com.flyingosred.app.android.simpleperpetualcalendar.data.loader;

import com.flyingosred.app.android.simpleperpetualcalendar.util.Utils;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.loader.Content.INVALID_POSITION;

final class ContentKey {

    private final int mYear;

    private final int mMonth;

    private final int mDay;

    private final int mPosition;

    public ContentKey(int position) {
        this(position, -1, -1, -1);
    }

    public ContentKey(int year, int month, int day) {
        this(INVALID_POSITION, year, month, day);
    }

    public ContentKey(int position, int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
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
        if (Utils.isSameDay(mYear, mMonth, mDay, key.getYear(), key.getMonth(), key.getDay())
                || (mPosition != INVALID_POSITION && key.getPosition() == mPosition)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (mYear > 0) {
            return (mDay & 0x1F) | ((mMonth & 0xF) << 5) | (mYear << 9);
        } else if (mPosition != INVALID_POSITION) {
            return mPosition + 1;
        }
        return 31;
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }

    public int getPosition() {
        return mPosition;
    }
}

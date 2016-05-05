package com.flyingosred.app.android.simpleperpetualcalendar.data.database.holiday;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday.INVALID_FIELD;

public class HolidayKey {

    private int mMonth = INVALID_FIELD;

    private int mDay = INVALID_FIELD;

    private boolean mIsLunar = false;

    private int mDayOfWeek = INVALID_FIELD;

    private int mDayOfWeekInMonth = INVALID_FIELD;

    private int mSolarTermId = INVALID_FIELD;

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (o.getClass() != getClass())
            return false;
        HolidayKey e = (HolidayKey) o;
        if (e.getSolarTermId() != INVALID_FIELD && e.getSolarTermId() == mSolarTermId) {
            return true;
        }
        if ((e.getMonth() != INVALID_FIELD && e.getMonth() == mMonth)
                && (e.getDayOfWeek() != INVALID_FIELD && e.getDayOfWeek() == mDayOfWeek)
                && (e.getDayOfWeekInMonth() != INVALID_FIELD && e.getDayOfWeekInMonth() == mDayOfWeekInMonth)) {
            return true;
        }
        if ((e.getMonth() != INVALID_FIELD && e.getMonth() == mMonth)
                && (e.getDay() != INVALID_FIELD && e.getDay() == mDay)
                && (e.isLunar() == mIsLunar)) {
            return true;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        if (mSolarTermId != INVALID_FIELD) {
            return mSolarTermId + 1;
        } else if (mIsLunar) {
            return (mDay & 0x1F) | ((mMonth & 0xF) << 5) | (0x01 << 9);
        } else if (mDayOfWeek != INVALID_FIELD && mDayOfWeekInMonth != INVALID_FIELD) {
            return ((mMonth & 0xF) << 5) | (mDayOfWeek << 10) | (mDayOfWeekInMonth << 13);
        } else {
            return (mDay & 0x1F) | ((mMonth & 0xF) << 5);
        }
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }

    public int getDayOfWeek() {
        return mDayOfWeek;
    }

    public int getDayOfWeekInMonth() {
        return mDayOfWeekInMonth;
    }

    public boolean isLunar() {
        return mIsLunar;
    }

    public int getSolarTermId() {
        return mSolarTermId;
    }

    public void setDay(int day) {
        mDay = day;
    }

    public void setDayOfWeek(int dayOfWeek) {
        mDayOfWeek = dayOfWeek;
    }

    public void setDayOfWeekInMonth(int dayOfWeekInMonth) {
        mDayOfWeekInMonth = dayOfWeekInMonth;
    }

    public void setIsLunar(boolean isLunar) {
        mIsLunar = isLunar;
    }

    public void setMonth(int month) {
        mMonth = month;
    }

    public void setSolarTermId(int solarTermId) {
        mSolarTermId = solarTermId;
    }
}

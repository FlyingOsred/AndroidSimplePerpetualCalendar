package com.flyingosred.app.android.simpleperpetualcalendar.data.database.holiday;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.SolarTerm;

import java.util.HashMap;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday.INVALID_FIELD;

public class HolidayDatabase {

    private static final HashMap<HolidayKey, HolidayDatabaseItem> mDatabase = new HashMap<>();

    public static void add(int month, int day, int region, int id) {
        HolidayKey key = new HolidayKey(month, day);
        HolidayDatabaseItem item = new HolidayDatabaseItem(region, id);
        mDatabase.put(key, item);
    }

    private static final class HolidayKey {

        private final int mMonth;

        private final int mDay;

        private final boolean mIsLunar;

        private final int mDayOfWeek;

        private final int mDayOfWeekInMonth;

        private final int mSolarTermId;

        public HolidayKey(int month, int day) {
            this(month, day, false);
        }

        public HolidayKey(int month, int day, boolean isLunar) {
            this(month, day, isLunar, INVALID_FIELD, INVALID_FIELD, INVALID_FIELD);
        }

        public HolidayKey(int month, int dayOfWeek, int dayOfWeekInMonth) {
            this(month, INVALID_FIELD, false, dayOfWeek, dayOfWeekInMonth, INVALID_FIELD);
        }

        public HolidayKey(int solarTermId) {
            this(INVALID_FIELD, INVALID_FIELD, false, INVALID_FIELD, INVALID_FIELD, solarTermId);
        }

        public HolidayKey(int month, int day, boolean isLunar, int dayOfWeek,
                          int dayOfWeekInMonth, int solarTermId) {
            mMonth = month;
            mDay = day;
            mDayOfWeek = dayOfWeek;
            mDayOfWeekInMonth = dayOfWeekInMonth;
            mIsLunar = isLunar;
            mSolarTermId = solarTermId;
        }

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

        public int getMonth() {
            return mMonth;
        }

        public int getSolarTermId() {
            return mSolarTermId;
        }
    }
}

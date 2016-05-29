package com.flyingosred.app.android.perpetualcalendar.data.provider;

import android.os.Parcel;

import com.flyingosred.app.android.perpetualcalendar.data.Lunar;

final class LunarDate implements Lunar {

    private final int mYear;

    private final int mMonth;

    private final int mDay;

    private final int mDaysInMonth;

    private final boolean mLeapMonth;

    public LunarDate(int year, int month, int day, int daysInMonth, boolean isLeapMonth) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mDaysInMonth = daysInMonth;
        mLeapMonth = isLeapMonth;
    }

    protected LunarDate(Parcel in) {
        mYear = in.readInt();
        mMonth = in.readInt();
        mDay = in.readInt();
        mDaysInMonth = in.readInt();
        mLeapMonth = in.readByte() != 0;
    }

    public static final Creator<LunarDate> CREATOR = new Creator<LunarDate>() {
        @Override
        public LunarDate createFromParcel(Parcel in) {
            return new LunarDate(in);
        }

        @Override
        public LunarDate[] newArray(int size) {
            return new LunarDate[size];
        }
    };

    @Override
    public int getYear() {
        return mYear;
    }

    @Override
    public int getMonth() {
        return mMonth;
    }

    @Override
    public int getDay() {
        return mDay;
    }

    @Override
    public int getDaysInMonth() {
        return mDaysInMonth;
    }

    @Override
    public boolean isLeapMonth() {
        return mLeapMonth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mYear);
        dest.writeInt(mMonth);
        dest.writeInt(mDay);
        dest.writeInt(mDaysInMonth);
        dest.writeByte((byte) (mLeapMonth ? 1 : 0));
    }
}

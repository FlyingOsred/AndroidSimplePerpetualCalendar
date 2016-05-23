/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Solar;
import com.flyingosred.app.android.simpleperpetualcalendar.util.Utils;

public class SolarDatabaseItem implements Solar, Parcelable {

    private final int mYear;

    private final int mMonth;

    private final int mDay;

    public SolarDatabaseItem(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
    }

    protected SolarDatabaseItem(Parcel in) {
        mYear = in.readInt();
        mMonth = in.readInt();
        mDay = in.readInt();
    }

    public static final Creator<SolarDatabaseItem> CREATOR = new Creator<SolarDatabaseItem>() {
        @Override
        public SolarDatabaseItem createFromParcel(Parcel in) {
            return new SolarDatabaseItem(in);
        }

        @Override
        public SolarDatabaseItem[] newArray(int size) {
            return new SolarDatabaseItem[size];
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
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass())
            return false;
        if (o == this)
            return true;
        SolarDatabaseItem item = (SolarDatabaseItem) o;
        return Utils.isSameDay(mYear, mMonth, mDay, item.mYear, item.mMonth, item.mDay);
    }

    @Override
    public int hashCode() {
        return (mDay & 0x1F) | ((mMonth & 0xF) << 5) | (mYear << 9);
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
    }
}

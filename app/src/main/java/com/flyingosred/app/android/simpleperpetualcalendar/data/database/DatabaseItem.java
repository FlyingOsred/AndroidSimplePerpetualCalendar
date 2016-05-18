package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Solar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseItem implements PerpetualCalendar {

    private final Solar mSolar;

    private final Lunar mLunar;

    private final int mSolarTermId;

    private final int mConstellationId;

    private final int mPosition;

    private final List<Holiday> mHolidayList;

    public DatabaseItem(int position, Solar solar, Lunar lunar, int solarTermId,
                        int constellationId, List<Holiday> holidayList) {
        mPosition = position;
        mSolar = solar;
        mLunar = lunar;
        mSolarTermId = solarTermId;
        mConstellationId = constellationId;
        mHolidayList = holidayList;
    }

    protected DatabaseItem(Parcel in) {
        mSolar = in.readParcelable(Solar.class.getClassLoader());
        mLunar = in.readParcelable(Lunar.class.getClassLoader());
        mSolarTermId = in.readInt();
        mConstellationId = in.readInt();
        mPosition = in.readInt();
        mHolidayList = new ArrayList<>();
        in.readList(mHolidayList, Holiday.class.getClassLoader());
    }

    public static final Creator<DatabaseItem> CREATOR = new Creator<DatabaseItem>() {
        @Override
        public DatabaseItem createFromParcel(Parcel in) {
            return new DatabaseItem(in);
        }

        @Override
        public DatabaseItem[] newArray(int size) {
            return new DatabaseItem[size];
        }
    };

    @Override
    public Solar getSolar() {
        return mSolar;
    }

    @Override
    public Lunar getLunar() {
        return mLunar;
    }

    @Override
    public int getSolarTermId() {
        return mSolarTermId;
    }

    @Override
    public int getConstellationId() {
        return mConstellationId;
    }

    @Override
    public List<Holiday> getHolidayList() {
        return mHolidayList;
    }

    public int getPosition() {
        return mPosition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mSolar, flags);
        dest.writeParcelable(mLunar, flags);
        dest.writeInt(mSolarTermId);
        dest.writeInt(mConstellationId);
        dest.writeInt(mPosition);
        dest.writeList(mHolidayList);
    }
}

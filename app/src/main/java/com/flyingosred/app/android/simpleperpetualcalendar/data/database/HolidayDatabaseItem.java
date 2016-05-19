package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday;

public class HolidayDatabaseItem implements Holiday {

    private static final String PREFIX_FLAG = "ic_flag_";

    private static final String PREFIX_NAME_ARRAY = "holiday_";

    private String mRegion = null;

    private int mId = INVALID_FIELD;

    private int mOffOrWork = INVALID_FIELD;

    public HolidayDatabaseItem() {

    }

    protected HolidayDatabaseItem(Parcel in) {
        mRegion = in.readString();
        mId = in.readInt();
        mOffOrWork = in.readInt();
    }

    public static final Creator<HolidayDatabaseItem> CREATOR = new Creator<HolidayDatabaseItem>() {
        @Override
        public HolidayDatabaseItem createFromParcel(Parcel in) {
            return new HolidayDatabaseItem(in);
        }

        @Override
        public HolidayDatabaseItem[] newArray(int size) {
            return new HolidayDatabaseItem[size];
        }
    };

    public void setRegion(String region) {
        mRegion = region;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setOffOrWork(int offOrWork) {
        mOffOrWork = offOrWork;
    }

    @Override
    public String getRegion() {
        return mRegion;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public int getOffOrWork() {
        return mOffOrWork;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRegion);
        dest.writeInt(mId);
        dest.writeInt(mOffOrWork);
    }
}

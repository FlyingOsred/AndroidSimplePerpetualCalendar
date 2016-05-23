/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import android.os.Parcel;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Constellation;

public class ConstellationDatabaseItem implements Constellation {

    private final int mId;

    public ConstellationDatabaseItem(int id) {
        mId = id;
    }

    protected ConstellationDatabaseItem(Parcel in) {
        mId = in.readInt();
    }

    public static final Creator<ConstellationDatabaseItem> CREATOR = new Creator<ConstellationDatabaseItem>() {
        @Override
        public ConstellationDatabaseItem createFromParcel(Parcel in) {
            return new ConstellationDatabaseItem(in);
        }

        @Override
        public ConstellationDatabaseItem[] newArray(int size) {
            return new ConstellationDatabaseItem[size];
        }
    };

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
    }
}

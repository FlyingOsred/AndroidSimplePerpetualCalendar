/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Solar;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.INVALID_POSITION;

final class DatabaseKey {

    private final int mPosition;

    private final Solar mSolar;

    public DatabaseKey(int position) {
        this(null, position);
    }

    public DatabaseKey(Solar solar) {
        this(solar, INVALID_POSITION);
    }

    private DatabaseKey(Solar solar, int position) {
        mSolar = solar;
        mPosition = position;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass())
            return false;
        if (o == this)
            return true;
        DatabaseKey key = (DatabaseKey) o;
        return (mSolar != null && mSolar.equals(key.mSolar))
                || (mPosition != INVALID_POSITION && mPosition == key.mPosition);
    }

    @Override
    public int hashCode() {
        if (mSolar != null) {
            return mSolar.hashCode();
        } else if (mPosition != INVALID_POSITION) {
            return mPosition + 1;
        }
        return super.hashCode();
    }
}

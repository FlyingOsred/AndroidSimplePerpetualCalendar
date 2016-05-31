/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public final class SimulateContentProvider {



    private final Context mContext;

    public SimulateContentProvider(Context context) {
        mContext = context;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

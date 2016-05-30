/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.provider;

import android.content.Context;

abstract class BaseProvider {

    private final Context mContext;

    public BaseProvider(Context context) {
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }
}

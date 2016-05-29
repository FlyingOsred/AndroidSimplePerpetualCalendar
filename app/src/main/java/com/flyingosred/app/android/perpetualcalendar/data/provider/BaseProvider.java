package com.flyingosred.app.android.perpetualcalendar.data.provider;

import android.content.Context;

public abstract class BaseProvider {

    private final Context mContext;

    public BaseProvider(Context context) {
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }
}

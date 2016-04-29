package com.flyingosred.app.android.simpleperpetualcalendar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class DayRecyclerView extends RecyclerView {
    public DayRecyclerView(Context context) {
        this(context, null);
    }

    public DayRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}

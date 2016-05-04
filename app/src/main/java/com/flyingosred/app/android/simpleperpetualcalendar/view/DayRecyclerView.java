package com.flyingosred.app.android.simpleperpetualcalendar.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.flyingosred.app.android.simpleperpetualcalendar.data.adapter.DayAdapter;
import com.flyingosred.app.android.simpleperpetualcalendar.data.loader.Content;

import java.util.Calendar;

import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

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
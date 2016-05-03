package com.flyingosred.app.android.simpleperpetualcalendar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

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

    public void onLoadFinished(Content data) {
        Log.d(LOG_TAG, "onLoadFinished");
        Calendar calendar = Calendar.getInstance();
        int today = data.get(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
        getLayoutManager().scrollToPosition(today);
    }
}

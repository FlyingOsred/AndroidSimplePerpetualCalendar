package com.flyingosred.app.android.simpleperpetualcalendar.data.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.flyingosred.app.android.simpleperpetualcalendar.data.provider.ContentItem;
import com.flyingosred.app.android.simpleperpetualcalendar.data.provider.ContentProvider;
import com.flyingosred.app.android.simpleperpetualcalendar.util.Utils;

import java.util.Calendar;
import java.util.TimeZone;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.END_DAY;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.END_MONTH;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.END_YEAR;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.START_DAY;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.START_MONTH;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.START_YEAR;
import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

public class ContentLoader extends AsyncTaskLoader<Content> {

    public ContentLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Override
    public Content loadInBackground() {
        Log.d(LOG_TAG, "loadInBackground begin");
        ContentProvider contentProvider = new ContentProvider(TimeZone.getDefault());
        Calendar start = Calendar.getInstance();
        start.set(START_YEAR, START_MONTH, START_DAY);
        Calendar end = Calendar.getInstance();
        end.set(END_YEAR, END_MONTH, END_DAY);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(start.getTimeInMillis());
        ContentData contentData = new ContentData();
        int index = 0;
        while (true) {
            ContentItem contentItem = contentProvider.get(index, calendar);
            contentData.add(index, contentItem);
            if (Utils.isSameDay(calendar, end)) {
                break;
            }
            calendar.add(Calendar.DATE, 1);
            index++;
        }
        Log.d(LOG_TAG, "loadInBackground end");
        return contentData;
    }
}

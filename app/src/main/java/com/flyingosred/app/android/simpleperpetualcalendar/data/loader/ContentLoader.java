package com.flyingosred.app.android.simpleperpetualcalendar.data.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.Calendar;

import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

public class ContentLoader extends AsyncTaskLoader<Content> {

    private int mFirstDayOfWeek = Calendar.SUNDAY;

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
        long startTime = System.currentTimeMillis();

        ContentData contentData = new ContentData();
        ContentItem contentItem = ContentItem.FirstDay();
        while (true) {
            contentData.add(contentItem);
            contentItem = contentItem.getNextDay();
            if (contentItem == null) {
                break;
            }
        }
        long endTime = System.currentTimeMillis();
        Log.i(LOG_TAG, "loadInBackground end, cost " + (endTime - startTime) / 1000 + " seconds.");
        return contentData;
    }
}

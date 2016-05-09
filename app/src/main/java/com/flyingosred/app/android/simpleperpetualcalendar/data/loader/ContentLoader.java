package com.flyingosred.app.android.simpleperpetualcalendar.data.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Database;
import com.flyingosred.app.android.simpleperpetualcalendar.data.database.DatabaseContainer;

public class ContentLoader extends AsyncTaskLoader<Database> {

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
    public Database loadInBackground() {
        return DatabaseContainer.getInstance().get();
    }
}

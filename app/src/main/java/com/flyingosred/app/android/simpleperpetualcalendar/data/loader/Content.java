package com.flyingosred.app.android.simpleperpetualcalendar.data.loader;

import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;

import java.util.Calendar;

public interface Content {

    public static final int INVALID_POSITION = -1;

    public int getCount();

    public PerpetualCalendar get(int position);

    public int get(Calendar calendar);
}

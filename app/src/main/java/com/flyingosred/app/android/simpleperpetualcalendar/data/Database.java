package com.flyingosred.app.android.simpleperpetualcalendar.data;

import java.util.Calendar;

public interface Database {

    public int getCount();

    public PerpetualCalendar get(int position);

    public int getPosition(Calendar calendar);

    public int getPosition(int year, int month, int day);

}

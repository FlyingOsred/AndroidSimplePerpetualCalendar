/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simpleperpetualcalendar.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyingosred.app.android.simpleperpetualcalendar.DisplayCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.R;

public class DayInfoView extends LinearLayout {

    private TextView mDayTextView;

    private TextView mConstellationTextView;

    private TextView mTodayTextView;

    private TextView mOffWorkTextView;

    private TextView mLunarTextView;

    private TextView mSolarTermTextView;

    private LinearLayout mHolidayContainer;

    public DayInfoView(Context context) {
        this(context, null);
    }

    public DayInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.day_info_view, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    public void setData(DisplayCalendar displayCalendar) {
        if (displayCalendar == null) {
            return;
        }
        mDayTextView.setText(displayCalendar.getDay());
        mDayTextView.setTextColor(displayCalendar.getDayTextColor());
        mLunarTextView.setText(displayCalendar.getLunarShort());
        mConstellationTextView.setText(displayCalendar.getConstellation());
        setText(mSolarTermTextView, displayCalendar.getSolarTerm());
        setText(mOffWorkTextView, displayCalendar.getOffWork());
        setText(mTodayTextView, displayCalendar.getToday());
        setHoliday(displayCalendar);
    }

    private void initViews() {
        mDayTextView = (TextView) findViewById(R.id.day_info_day_text_view);
        mConstellationTextView = (TextView) findViewById(R.id.day_info_constellation_text_view);
        mTodayTextView = (TextView) findViewById(R.id.day_info_today_text_view);
        mOffWorkTextView = (TextView) findViewById(R.id.day_info_off_or_work_text_view);
        mLunarTextView = (TextView) findViewById(R.id.day_info_lunar_text_view);
        mSolarTermTextView = (TextView) findViewById(R.id.day_info_solar_term_text_view);
        mHolidayContainer = (LinearLayout) findViewById(R.id.day_info_holiday_container_view);
    }

    private void setText(TextView textView, String text) {
        if (text != null) {
            textView.setVisibility(VISIBLE);
            textView.setText(text);
        } else {
            textView.setVisibility(GONE);
        }
    }

    private void setHoliday(DisplayCalendar displayCalendar) {
        String[] holidays = displayCalendar.getHolidays();
        mHolidayContainer.removeAllViews();
        if (holidays == null || holidays.length == 0) {
            mHolidayContainer.setVisibility(GONE);
            return;
        }
        mHolidayContainer.setVisibility(VISIBLE);
        int flagId = displayCalendar.getRegionFlag();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < holidays.length; i++) {
            String holiday = holidays[i];
            @SuppressLint("InflateParams") View holidayItem = inflater.inflate(R.layout.holiday_item_view, null);
            AppCompatImageView imageView = (AppCompatImageView) holidayItem.findViewById(R.id.holiday_item_image_view);
            imageView.setImageResource(flagId);
            TextView textView = (TextView) holidayItem.findViewById(R.id.holiday_item_text_view);
            textView.setText(holiday);
            mHolidayContainer.addView(holidayItem, i);
        }
    }
}

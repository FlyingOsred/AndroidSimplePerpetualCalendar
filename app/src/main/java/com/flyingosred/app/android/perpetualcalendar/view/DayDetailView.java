/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.view;

import android.content.Context;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyingosred.app.android.perpetualcalendar.R;
import com.flyingosred.app.android.perpetualcalendar.data.resource.Resource;

import java.util.Calendar;
import java.util.Locale;

public class DayDetailView {

    private static final String LOG_TAG = DayDetailView.class.getSimpleName();

    private static final int SHOW_PERIOD = 3000;

    private final Context mContext;

    private final Resource mResource;

    private final View mRootView;

    private final TextView mDateTextView;

    private final TextView mDayTextView;

    private final TextView mConstellationTextView;

    private final TextView mSolarTermTextView;

    private final LinearLayout mHolidayContainer;

    private final TextView mLunarTextView;

    private final CountDownTimer mCountDownTimer;

    private final int mDefaultTextColor;

    private final boolean mIsFixed;

    public DayDetailView(Context context, View root, boolean isFixed) {
        mContext = context;
        mResource = new Resource(context);
        mRootView = root;
        mIsFixed = isFixed;
        Log.d(LOG_TAG, "Detail view fixed is " + mIsFixed);
        mDateTextView = (TextView) root.findViewById(R.id.day_detail_info_date_text_view);
        mDayTextView = (TextView) root.findViewById(R.id.day_detail_info_day_text_view);
        mDefaultTextColor = mDayTextView.getCurrentTextColor();
        mLunarTextView = (TextView) root.findViewById(R.id.day_detail_info_lunar_date_text_view);
        mConstellationTextView = (TextView) root.findViewById(R.id.day_detail_extra_info_constellation_text_view);
        mSolarTermTextView = (TextView) root.findViewById(R.id.day_detail_extra_info_solar_term_text_view);
        mHolidayContainer = (LinearLayout) root.findViewById(R.id.day_detail_extra_info_holiday_container_view);
        if (isFixed) {
            mCountDownTimer = null;
        } else {
            mCountDownTimer = new CountDownTimer(SHOW_PERIOD, SHOW_PERIOD) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    mRootView.setVisibility(View.GONE);
                }
            };
            mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                }
            });
        }

    }

    public void show() {
        if (!mIsFixed) {
            mCountDownTimer.cancel();
            mRootView.setVisibility(View.VISIBLE);
            mCountDownTimer.start();
        }
    }

    public void hide() {
        if (!mIsFixed) {
            mCountDownTimer.cancel();
            mRootView.setVisibility(View.GONE);
        }
    }

    public void setData(Cursor cursor, int position, String region) {
        if (cursor != null) {
            cursor.moveToPosition(position);
            Calendar calendar = mResource.getCalendar(cursor);
            Log.d(LOG_TAG, "Show detail for " + calendar.getTime());
            mDayTextView.setText(String.valueOf(calendar.get(Calendar.DATE)));
            String dateString = DateUtils.formatDateRange(mContext, calendar.getTimeInMillis(),
                    calendar.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY
                            | DateUtils.FORMAT_SHOW_YEAR).toUpperCase(Locale.getDefault());
            mResource.setText(mDateTextView, dateString);
            mResource.setText(mLunarTextView, mResource.getLunarFullName(cursor));
            mResource.setTextColor(mDayTextView, cursor, region, mDefaultTextColor);
            show();
        }
    }
}

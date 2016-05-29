/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.flyingosred.app.android.perpetualcalendar.util.Utils.EMPTY_STRING;
import static com.flyingosred.app.android.perpetualcalendar.util.Utils.LOG_TAG;

public class DayCardFragment extends Fragment {

    public static final String ARG_DATE = "date";

    private DisplayCalendar mDisplayCalendar = null;

    public DayCardFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate called");
        if (getArguments().containsKey(ARG_DATE)) {
            mDisplayCalendar = getArguments().getParcelable(ARG_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_day_card, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated called");
        getActivity().setTitle(EMPTY_STRING);
    }

    private void initViews(View parentView) {

        TextView dateTextView = (TextView) parentView.findViewById(R.id.day_card_date_text_view);
        dateTextView.setText(mDisplayCalendar.getDate());

        TextView daysFromTodayTextView = (TextView) parentView.findViewById(R.id.day_card_days_from_today_text_view);
        daysFromTodayTextView.setText(mDisplayCalendar.getDaysOffset());

        TextView dayTextView = (TextView) parentView.findViewById(R.id.day_card_day_text_view);
        dayTextView.setText(mDisplayCalendar.getDay());

        TextView lunarTextView = (TextView) parentView.findViewById(R.id.day_card_lunar_text_view);
        lunarTextView.setText(mDisplayCalendar.getLunarLong());

        TextView solarTermTextView = (TextView) parentView.findViewById(R.id.day_card_solar_term_text_view);
        String solarTerm = mDisplayCalendar.getSolarTerm();
        if (solarTerm != null) {
            solarTermTextView.setVisibility(View.VISIBLE);
            solarTermTextView.setText(solarTerm);
        } else {
            solarTermTextView.setVisibility(View.GONE);
        }

        TextView constellationTextView = (TextView) parentView.findViewById(R.id.day_card_constellation_text_view);
        constellationTextView.setText(mDisplayCalendar.getConstellation());

        View offWorkContainer = parentView.findViewById(R.id.day_card_off_work_container);
        String offWork = mDisplayCalendar.getOffWorkLong();
        if (offWork != null) {
            AppCompatImageView offWorkImageView = (AppCompatImageView) parentView.findViewById(R.id.day_card_off_work_region_flag_image_view);
            offWorkImageView.setImageResource(mDisplayCalendar.getRegionFlag());
            TextView offWorkTextView = (TextView) parentView.findViewById(R.id.day_card_off_work_text_view);
            offWorkTextView.setText(offWork);
            offWorkContainer.setVisibility(View.VISIBLE);
        } else {
            offWorkContainer.setVisibility(View.GONE);
        }

        View holidayContainer = parentView.findViewById(R.id.day_card_holiday_container);
        String[] holidays = mDisplayCalendar.getHolidays();
        if (holidays != null) {
            AppCompatImageView imageView = (AppCompatImageView) parentView.findViewById(R.id.day_card_holiday_region_flag_image_view);
            imageView.setImageResource(mDisplayCalendar.getRegionFlag());

            TextView holidayTextView = (TextView) parentView.findViewById(R.id.day_card_holiday_text_view);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < holidays.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(holidays[i]);
            }
            holidayTextView.setText(sb.toString());
            holidayContainer.setVisibility(View.VISIBLE);
        } else {
            holidayContainer.setVisibility(View.GONE);
        }
    }
}

/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyingosred.app.android.perpetualcalendar.R;

import java.util.Calendar;
import java.util.Locale;

public class DayOfWeekAdapter extends RecyclerView.Adapter {

    private int mFirstDayOfWeek = Calendar.SUNDAY;

    private boolean mShowWeekNumber = false;

    public DayOfWeekAdapter(int firstDayOfWeek, boolean showWeekNumber) {
        mShowWeekNumber = showWeekNumber;
        mFirstDayOfWeek = firstDayOfWeek;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_of_week_recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Calendar calendar = Calendar.getInstance();
        int maxDaysInWeek = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        if (mShowWeekNumber && position == 0) {
            viewHolder.mWeekNumberTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mWeekNumberTextView.setVisibility(View.GONE);
        }
        int dayOfWeek = mFirstDayOfWeek + position;
        if (dayOfWeek > maxDaysInWeek) {
            dayOfWeek -= maxDaysInWeek;
        }
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        String dayOfWeekString = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
                Locale.getDefault()).toUpperCase(Locale.getDefault());
        viewHolder.mDayOfWeekTextView.setText(dayOfWeekString);
    }

    @Override
    public int getItemCount() {
        return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_WEEK);
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        if (mFirstDayOfWeek != firstDayOfWeek) {
            mFirstDayOfWeek = firstDayOfWeek;
            notifyDataSetChanged();
        }
    }

    public void setShowWeekNumber(boolean showWeekNumber) {
        if (mShowWeekNumber != showWeekNumber) {
            mShowWeekNumber = showWeekNumber;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mDayOfWeekTextView;
        public final TextView mWeekNumberTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mDayOfWeekTextView = (TextView) itemView.findViewById(R.id.day_of_week_text_view);
            mWeekNumberTextView = (TextView) itemView.findViewById(R.id.week_number_label_text_view);
        }
    }
}

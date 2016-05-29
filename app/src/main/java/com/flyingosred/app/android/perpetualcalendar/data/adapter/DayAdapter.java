/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.flyingosred.app.android.perpetualcalendar.DisplayCalendar;
import com.flyingosred.app.android.perpetualcalendar.R;
import com.flyingosred.app.android.perpetualcalendar.data.Database;
import com.flyingosred.app.android.perpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.perpetualcalendar.data.Solar;
import com.flyingosred.app.android.perpetualcalendar.view.DayInfoView;

import java.util.Calendar;

import static com.flyingosred.app.android.perpetualcalendar.data.PerpetualCalendar.DAYS_IN_WEEK;
import static com.flyingosred.app.android.perpetualcalendar.util.Utils.LOG_TAG;

public class DayAdapter extends RecyclerView.Adapter {

    private final Context mContext;

    private Database mDatabase = null;

    private int mFrontOffset = 0;

    private int mFirstDayOfWeek;

    private boolean mShowWeekNumber;

    private String mHolidayRegion;

    private int mActivatedItem = AdapterView.INVALID_POSITION;

    public DayAdapter(Context context, int firstDayOfWeek, boolean showWeekNumber, String region) {
        mContext = context;
        mShowWeekNumber = showWeekNumber;
        mFirstDayOfWeek = firstDayOfWeek;
        mHolidayRegion = region;
        computeOffset(firstDayOfWeek);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (position < mFrontOffset) {
            return;
        }
        if (mDatabase != null) {
            viewHolder.bindData(position, mDatabase.get(position - mFrontOffset),
                    mShowWeekNumber && (position % PerpetualCalendar.DAYS_IN_WEEK == 0),
                    position == mActivatedItem);
        }
    }

    @Override
    public int getItemCount() {
        if (mDatabase != null) {
            return mDatabase.getCount() + mFrontOffset;
        }
        return 0;
    }

    public void changeDatabase(Database database) {
        mDatabase = database;
        computeOffset();
        notifyDataSetChanged();
    }

    public void activateItem(int position) {
        Log.d(LOG_TAG, "activateItem position " + position);
        if (position >= mFrontOffset && mActivatedItem != position) {
            int lastActivatedItem = mActivatedItem;
            mActivatedItem = position;
            if (lastActivatedItem != AdapterView.INVALID_POSITION) {
                notifyItemChanged(lastActivatedItem);
            }
            if (mActivatedItem != AdapterView.INVALID_POSITION) {
                notifyItemChanged(mActivatedItem);
            }
        }
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        if (mFirstDayOfWeek != firstDayOfWeek) {
            mFirstDayOfWeek = firstDayOfWeek;
            computeOffset(firstDayOfWeek);
            notifyDataSetChanged();
        }
    }

    public void setShowWeekNumber(boolean showWeekNumber) {
        if (mShowWeekNumber != showWeekNumber) {
            mShowWeekNumber = showWeekNumber;
            notifyDataSetChanged();
        }
    }

    public void setHolidayRegion(String region) {
        if (mHolidayRegion.equals(region) || (mHolidayRegion != null && mHolidayRegion.equals(region))) {
            return;
        }
        mHolidayRegion = region;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private DisplayCalendar mDisplayCalendar = null;
        private final TextView mWeekNumberTextView;
        private final DayInfoView mDayInfoView;

        public ViewHolder(View itemView) {
            super(itemView);
            mWeekNumberTextView = (TextView) itemView.findViewById(R.id.week_number_text_view);
            mDayInfoView = (DayInfoView) itemView.findViewById(R.id.day_info_view);
        }

        public void bindData(int position, PerpetualCalendar perpetualCalendar,
                             boolean showWeekNumber, boolean active) {
            if (perpetualCalendar == null) {
                return;
            }
            mDisplayCalendar = new DisplayCalendar(mContext, perpetualCalendar,
                    position - getTodayPosition(), mHolidayRegion);
            mDayInfoView.setData(mDisplayCalendar);
            setWeekNumber(showWeekNumber, perpetualCalendar.getSolar());
            setActive(active);
        }

        public DisplayCalendar getDisplayCalendar() {
            return mDisplayCalendar;
        }

        private void setWeekNumber(boolean showWeekNumber, Solar solar) {
            if (showWeekNumber) {
                Calendar weekCalendar = Calendar.getInstance();
                weekCalendar.setFirstDayOfWeek(mFirstDayOfWeek);
                weekCalendar.set(solar.getYear(), solar.getMonth() - 1, solar.getDay());
                int weekNumber = weekCalendar.get(Calendar.WEEK_OF_YEAR);
                mWeekNumberTextView.setText(String.valueOf(weekNumber));
                mWeekNumberTextView.setVisibility(View.VISIBLE);
            } else {
                mWeekNumberTextView.setVisibility(View.GONE);
            }
        }

        private void setActive(boolean active) {
            mDayInfoView.setActivated(active);
        }
    }

    public void computeOffset() {
        computeOffset(mFirstDayOfWeek);
    }

    public void computeOffset(int firstDayOfWeek) {
        if (mDatabase != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(mDatabase.get(0).getSolar().getYear(),
                    mDatabase.get(0).getSolar().getMonth() - 1,
                    mDatabase.get(0).getSolar().getDay());
            mFrontOffset = findDayOffset(calendar.get(Calendar.DAY_OF_WEEK), firstDayOfWeek,
                    DAYS_IN_WEEK);
        } else {
            mFrontOffset = 0;
        }
        Log.d(LOG_TAG, "Front offset is " + mFrontOffset);
    }

    private int findDayOffset(int dayOfWeekStart, int firstDayOfWeek, int maxDaysInWeek) {
        final int offset = dayOfWeekStart - firstDayOfWeek;
        if (dayOfWeekStart < firstDayOfWeek) {
            return offset + maxDaysInWeek;
        }
        return offset;
    }

    public int getTodayPosition() {
        int position = AdapterView.INVALID_POSITION;
        if (mDatabase != null) {
            Calendar calendar = Calendar.getInstance();
            int today = mDatabase.getPosition(calendar);
            position = today + mFrontOffset;
        }
        return position;
    }

    public PerpetualCalendar get(int position) {
        if (position >= mFrontOffset && mDatabase != null) {
            return mDatabase.get(position - mFrontOffset);
        }
        return null;
    }

    public int getPosition(int year, int month, int day) {
        int position = AdapterView.INVALID_POSITION;
        if (mDatabase != null) {
            position = mDatabase.getPosition(year, month, day);
        }
        if (position != AdapterView.INVALID_POSITION) {
            position += mFrontOffset;
        }
        return position;
    }
}

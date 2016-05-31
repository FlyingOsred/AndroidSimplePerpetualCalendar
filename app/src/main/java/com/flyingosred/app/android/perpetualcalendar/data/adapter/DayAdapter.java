/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyingosred.app.android.perpetualcalendar.R;
import com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract;
import com.flyingosred.app.android.perpetualcalendar.data.Database;
import com.flyingosred.app.android.perpetualcalendar.data.PerpetualCalendar;

import java.util.Calendar;

import static com.flyingosred.app.android.perpetualcalendar.data.PerpetualCalendar.DAYS_IN_WEEK;
import static com.flyingosred.app.android.perpetualcalendar.util.Utils.LOG_TAG;
import static com.flyingosred.app.android.perpetualcalendar.util.Utils.daysBetween;

public class DayAdapter extends RecyclerView.Adapter {

    private static final int YEAR_MIN = 1901;

    private static final int MONTH_MIN = 2;

    private static final int DAY_MIN = 19;

    private static final int YEAR_MAX = 2099;

    private static final int MONTH_MAX = 12;

    private static final int DAY_MAX = 31;

    private static final String[] PROJECTION = {
            PerpetualCalendarContract.PerpetualCalendar._ID,
            PerpetualCalendarContract.PerpetualCalendar.SOLAR_TIME_MILLIS,
            PerpetualCalendarContract.PerpetualCalendar.LUNAR_SHORT_NAME,
            PerpetualCalendarContract.PerpetualCalendar.LUNAR_FULL_NAME,
            PerpetualCalendarContract.PerpetualCalendar.SOLAR_TERM_NAME,
            PerpetualCalendarContract.PerpetualCalendar.CONSTELLATION_NAME,
            PerpetualCalendarContract.PerpetualCalendar.HOLIDAY_REGION_FLAG_IMG,
            PerpetualCalendarContract.PerpetualCalendar.HOLIDAY_NAMES,
    };

    private final Context mContext;

    private final int mCount;

    private final Calendar mMinDate;

    private Database mDatabase = null;

    private int mFrontOffset = 0;

    private int mFirstDayOfWeek;

    private boolean mShowWeekNumber;

    private String mHolidayRegion;

    private int mActivatedItem = AdapterView.INVALID_POSITION;

    public DayAdapter(Context context, int firstDayOfWeek, boolean showWeekNumber, String region) {
        mContext = context;
        mMinDate = Calendar.getInstance();
        mMinDate.set(YEAR_MIN, MONTH_MIN - 1, DAY_MIN);
        Calendar maxdate = Calendar.getInstance();
        maxdate.set(YEAR_MAX, MONTH_MAX - 1, DAY_MAX);
        mCount = daysBetween(mMinDate, maxdate) + 1;
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mMinDate.getTimeInMillis());
        calendar.add(Calendar.DATE, position - mFrontOffset);

        Cursor cursor = PerpetualCalendarContract.PerpetualCalendar.query(
                mContext.getContentResolver(), calendar, mHolidayRegion, PROJECTION);
        viewHolder.bindData(cursor);
        viewHolder.setActive(position == mActivatedItem);
        viewHolder.compute();
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.unbindData();
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

        private final TextView mWeekNumberTextView;
        private final View mDateContainerView;
        private final TextView mDayTextView;
        private final TextView mConstellationTextView;
        private final TextView mTodayTextView;
        private final TextView mOffWorkTextView;
        private final TextView mLunarTextView;
        private final TextView mSolarTermTextView;
        private final LinearLayout mHolidayContainerView;
        private Cursor mCursor = null;
        private boolean mIsActivated = false;

        public ViewHolder(View itemView) {
            super(itemView);
            mWeekNumberTextView = (TextView) itemView.findViewById(R.id.day_recycler_item_week_number_text_view);
            mDateContainerView = itemView.findViewById(R.id.day_recycler_item_date_info_container);
            mDayTextView = (TextView) itemView.findViewById(R.id.day_recycler_item_day_text_view);
            mConstellationTextView = (TextView) itemView.findViewById(R.id.day_recycler_item_constellation_text_view);
            mTodayTextView = (TextView) itemView.findViewById(R.id.day_recycler_item_today_text_view);
            mOffWorkTextView = (TextView) itemView.findViewById(R.id.day_recycler_item_off_work_text_view);
            mLunarTextView = (TextView) itemView.findViewById(R.id.day_recycler_item_lunar_text_view);
            mSolarTermTextView = (TextView) itemView.findViewById(R.id.day_recycler_item_solar_term_text_view);
            mHolidayContainerView = (LinearLayout) itemView.findViewById(R.id.day_recycler_item_holiday_container_view);
        }

        public void bindData(Cursor cursor) {
            unbindData();
            mCursor = cursor;
        }

        public void unbindData() {
            if (mCursor != null) {
                mCursor.close();
                mCursor = null;
            }
        }

        public void compute() {
            if (mCursor != null) {
                mCursor.moveToFirst();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(mCursor.getLong(mCursor.getColumnIndex(PerpetualCalendarContract.PerpetualCalendar.SOLAR_TIME_MILLIS)));
                boolean showWeekNumber = mShowWeekNumber
                        && (getAdapterPosition() % PerpetualCalendar.DAYS_IN_WEEK == 0);
                setWeekNumber(showWeekNumber, calendar);
                mDayTextView.setText(String.valueOf(calendar.get(Calendar.DATE)));
                setText(mLunarTextView,
                        mCursor.getString(mCursor.getColumnIndex(
                                PerpetualCalendarContract.PerpetualCalendar.LUNAR_SHORT_NAME)));
                setText(mSolarTermTextView,
                        mCursor.getString(mCursor.getColumnIndex(
                                PerpetualCalendarContract.PerpetualCalendar.SOLAR_TERM_NAME)));
                mDateContainerView.setActivated(mIsActivated);
            }
        }

        public Cursor getData() {
            return mCursor;
        }

        private void setWeekNumber(boolean showWeekNumber, Calendar calendar) {
            if (showWeekNumber) {
                int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
                mWeekNumberTextView.setText(String.valueOf(weekNumber));
                mWeekNumberTextView.setVisibility(View.VISIBLE);
            } else {
                mWeekNumberTextView.setVisibility(View.GONE);
            }
        }

        private void setActive(boolean active) {
            mIsActivated = active;
        }

        private void setText(TextView textView, String text) {
            if (text != null) {
                textView.setText(text);
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
            }
        }
    }

    public void computeOffset(int firstDayOfWeek) {
        mFrontOffset = findDayOffset(mMinDate.get(Calendar.DAY_OF_WEEK), firstDayOfWeek, DAYS_IN_WEEK);
        Log.d(LOG_TAG, "Front offset is " + mFrontOffset);
    }

    private int findDayOffset(int dayOfWeekStart, int firstDayOfWeek, int maxDaysInWeek) {
        final int offset = dayOfWeekStart - firstDayOfWeek;
        if (dayOfWeekStart < firstDayOfWeek) {
            return offset + maxDaysInWeek;
        }
        return offset;
    }

    public PerpetualCalendar get(int position) {
        if (position >= mFrontOffset && mDatabase != null) {
            return mDatabase.get(position - mFrontOffset);
        }
        return null;
    }

    public int getPosition(Calendar calendar) {
        return daysBetween(mMinDate, calendar) + mFrontOffset;
    }
}

/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
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
import com.flyingosred.app.android.perpetualcalendar.data.resource.Resource;
import com.flyingosred.app.android.perpetualcalendar.util.Utils;

import java.util.Calendar;

import static com.flyingosred.app.android.perpetualcalendar.util.Utils.LOG_TAG;

public class DayAdapter extends RecyclerView.Adapter {

    private final Context mContext;

    private final Resource mResource;

    private int mFrontOffset = 0;

    private int mFirstDayOfWeek;

    private boolean mShowWeekNumber;

    private String mHolidayRegion;

    private int mActivatedItem = AdapterView.INVALID_POSITION;

    private Cursor mCursor;

    public DayAdapter(Context context, int firstDayOfWeek, boolean showWeekNumber, String region) {
        mContext = context;
        mResource = new Resource(context);
        mShowWeekNumber = showWeekNumber;
        mFirstDayOfWeek = firstDayOfWeek;
        mHolidayRegion = region;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < mFrontOffset || mCursor == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bindData(mCursor);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mCursor != null) {
            count = mCursor.getCount() + mFrontOffset;
        }
        return count;
    }

    public void swapCursor(Cursor cursor) {
        if (mCursor != cursor) {
            mCursor = cursor;
            computeOffset(mFirstDayOfWeek);
            notifyDataSetChanged();
        }
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

    public Calendar get(int position) {
        if (position >= mFrontOffset || mCursor != null) {
            mCursor.moveToPosition(position - mFrontOffset);
            return mResource.getCalendar(mCursor);
        }
        return null;
    }

    public int getDataPosition(int position) {
        return position - mFrontOffset;
    }

    public Cursor getData() {
        return mCursor;
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

    public void computeOffset(int firstDayOfWeek) {
        if (mCursor != null) {
            mCursor.moveToFirst();
            Calendar calendar = mResource.getCalendar(mCursor);
            mFrontOffset = findDayOffset(calendar.get(Calendar.DAY_OF_WEEK), firstDayOfWeek,
                    calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
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

    public int getPosition(Calendar calendar) {
        int position = 0;
        if (mCursor != null) {
            position = Utils.daysBetween(PerpetualCalendarContract.MIN_DATE, calendar) + mFrontOffset;
        }
        return position;
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
        private final int mDefaultTextColor;

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
            mDefaultTextColor = mDayTextView.getCurrentTextColor();
        }

        public void bindData(Cursor cursor) {
            if (cursor == null) {
                return;
            }
            cursor.moveToPosition(getAdapterPosition() - mFrontOffset);
            Calendar calendar = mResource.getCalendar(cursor);
            if (calendar == null) {
                return;
            }
            boolean showWeekNumber = mShowWeekNumber
                    && (getAdapterPosition() % calendar.getActualMaximum(Calendar.DAY_OF_WEEK) == 0);
            setWeekNumber(showWeekNumber, calendar);
            String todayText;
            if (Utils.isToday(calendar)) {
                todayText = mContext.getString(R.string.action_today);
            } else {
                todayText = null;
            }
            mResource.setText(mTodayTextView, todayText);
            mDayTextView.setText(String.valueOf(calendar.get(Calendar.DATE)));
            mResource.setText(mLunarTextView, mResource.getLunarShortName(cursor));
            mResource.setText(mSolarTermTextView, mResource.getSolarTermName(cursor));
            mResource.setText(mConstellationTextView, mResource.getConstellationName(cursor));
            setHoliday(cursor);
            mResource.setText(mOffWorkTextView, mResource.getHolidayOffWorkString(cursor, mHolidayRegion));
            mDateContainerView.setActivated(getAdapterPosition() == mActivatedItem);
            mResource.setTextColor(mDayTextView, cursor, mHolidayRegion, mDefaultTextColor);
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

        private void setHoliday(Cursor cursor) {
            Drawable flagDrawable = mResource.getRegionFlag(mHolidayRegion);
            mHolidayContainerView.removeAllViews();
            String[] holidayNames = mResource.getHolidayName(cursor, mHolidayRegion);
            if (holidayNames.length > 0) {
                mHolidayContainerView.setVisibility(View.VISIBLE);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                for (int i = 0; i < holidayNames.length; i++) {
                    String holidayName = holidayNames[i];
                    @SuppressLint("InflateParams") View holidayItem = inflater.inflate(R.layout.day_recycler_view_item_holiday_view, null);
                    AppCompatImageView imageView = (AppCompatImageView) holidayItem.findViewById(R.id.holiday_item_image_view);
                    imageView.setImageDrawable(flagDrawable);
                    TextView textView = (TextView) holidayItem.findViewById(R.id.holiday_item_text_view);
                    textView.setText(holidayName);
                    mHolidayContainerView.addView(holidayItem, i);
                }
            } else {
                mHolidayContainerView.setVisibility(View.GONE);
            }
        }
    }
}

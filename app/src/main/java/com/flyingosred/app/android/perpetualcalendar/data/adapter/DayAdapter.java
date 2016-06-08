/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        Calendar maxDate = Calendar.getInstance();
        maxDate.set(YEAR_MAX, MONTH_MAX - 1, DAY_MAX);
        mCount = daysBetween(mMinDate, maxDate) + 1;
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
        Log.d(LOG_TAG, "onViewRecycled holder " + holder);
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
                calendar.setTimeInMillis(
                        mCursor.getLong(mCursor.getColumnIndex(
                                PerpetualCalendarContract.PerpetualCalendar.SOLAR_TIME_MILLIS)));
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
                setText(mConstellationTextView,
                        mCursor.getString(mCursor.getColumnIndex(
                                PerpetualCalendarContract.PerpetualCalendar.CONSTELLATION_NAME)));
                mDateContainerView.setActivated(mIsActivated);
                setTextColor(mDayTextView, calendar);
                byte[] holidayArray = mCursor.getBlob(mCursor.getColumnIndex(
                        PerpetualCalendarContract.PerpetualCalendar.HOLIDAY_NAMES));
                byte[] holidayFlagArray = mCursor.getBlob(mCursor.getColumnIndex(
                        PerpetualCalendarContract.PerpetualCalendar.HOLIDAY_REGION_FLAG_IMG));
                ObjectInputStream ois = null;
                ArrayList<String> list = null;
                try {
                    ois = new ObjectInputStream(new ByteArrayInputStream(holidayArray));
                    //noinspection unchecked
                    list = (ArrayList<String>) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (ois != null) {
                        try {
                            ois.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                setHoliday(list, holidayFlagArray);
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

        private void setTextColor(TextView textView, Calendar calendar) {
            int textColor = 0;
            int textColorResId = 0;
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                textColorResId = R.attr.colorSaturdayText;
            } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                textColorResId = R.attr.colorSundayText;
            }
            if (textColorResId != 0) {
                TypedValue typedValue = new TypedValue();
                TypedArray a = mContext.obtainStyledAttributes(typedValue.data, new int[]{textColorResId});
                textColor = a.getColor(0, 0);
                a.recycle();
            }

            if (textColor != 0) {
                textView.setTextColor(textColor);
            } else {
                textView.setTextColor(mDefaultTextColor);
            }
        }

        private void setHoliday(List<String> names, byte[] imgArray) {
            mHolidayContainerView.removeAllViews();
            if (names == null || names.size() == 0) {
                mHolidayContainerView.setVisibility(View.GONE);
                return;
            }
            mHolidayContainerView.setVisibility(View.VISIBLE);
            Bitmap bmp = null;
            if (imgArray != null) {
                bmp = BitmapFactory.decodeByteArray(imgArray, 0, imgArray.length);
            }
            LayoutInflater inflater = LayoutInflater.from(mContext);
            for (int i = 0; i < names.size(); i++) {
                String holiday = names.get(i);
                @SuppressLint("InflateParams") View holidayItem = inflater.inflate(R.layout.holiday_item_view, null);
                AppCompatImageView imageView = (AppCompatImageView) holidayItem.findViewById(R.id.holiday_item_image_view);
                imageView.setImageBitmap(bmp);
                TextView textView = (TextView) holidayItem.findViewById(R.id.holiday_item_text_view);
                textView.setText(holiday);
                mHolidayContainerView.addView(holidayItem, i);
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

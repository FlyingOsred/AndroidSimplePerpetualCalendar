package com.flyingosred.app.android.simpleperpetualcalendar.data.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyingosred.app.android.simpleperpetualcalendar.DisplayCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.R;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Database;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Solar;
import com.flyingosred.app.android.simpleperpetualcalendar.view.DayInfoView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday.INVALID_FIELD;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.DAYS_IN_WEEK;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.INVALID_ID;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.START_DAY_OF_WEEK;
import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

public class DayAdapter extends RecyclerView.Adapter {

    private final Context mContext;

    private Database mDatabase = null;

    private int mFrontOffset = 0;

    private int mFirstDayOfWeek;

    private boolean mShowWeekNumber;

    private String mHolidayRegion;

    private String[] mHolidayNames = null;

    private int mHolidayNationFlagId = -1;

    private int mActivatedItem = AdapterView.INVALID_POSITION;

    public DayAdapter(Context context, int firstDayOfWeek, boolean showWeekNumber, String region) {
        mContext = context;
        mShowWeekNumber = showWeekNumber;
        mFirstDayOfWeek = firstDayOfWeek;
        mHolidayRegion = region;
        getHolidayNames();
        computeOffset(firstDayOfWeek);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_recycler_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, parent.getContext());
        return viewHolder;
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
        if (mHolidayRegion == region || (mHolidayRegion != null && mHolidayRegion.equals(region))) {
            return;
        }
        mHolidayRegion = region;
        getHolidayNames();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private DisplayCalendar mDisplayCalendar = null;
        private final View mItemView;
        private final View mActivateAreaView;
        private final TextView mWeekNumberTextView;
        private final DayInfoView mDayInfoView;
        //private final TextView mHolidayTextView;
        //private final LinearLayout mHolidayContainer;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            mItemView = itemView;
            mWeekNumberTextView = (TextView) itemView.findViewById(R.id.week_number_text_view);
            mActivateAreaView = itemView.findViewById(R.id.day_info_view);
            mDayInfoView = (DayInfoView) itemView.findViewById(R.id.day_info_view);
            //mHolidayTextView = (TextView) itemView.findViewById(R.id.holiday_text_view);
            //mHolidayContainer = (LinearLayout) itemView.findViewById(R.id.holiday_container_view);
        }

        public void bindData(int position, PerpetualCalendar perpetualCalendar, boolean showWeekNumber, boolean active) {
            if (perpetualCalendar == null) {
                return;
            }
            mDisplayCalendar = new DisplayCalendar(mContext, perpetualCalendar,
                    position - getTodayPosition(), mHolidayRegion);
            mDayInfoView.setData(mDisplayCalendar);
            setWeekNumber(showWeekNumber, perpetualCalendar.getSolar());
            //setActive(active);
        }

        public DisplayCalendar getDisplayCalendar() {
            return mDisplayCalendar;
        }

//        private void setHolidayText(List<Holiday> holidayList) {
//            List<Integer> holidayIdList = new ArrayList<>();
//            int offOrWorkResId = INVALID_ID;
//            if (mHolidayRegion != null && holidayList != null) {
//                for (Holiday holiday : holidayList) {
//                    if (!mHolidayRegion.equals(holiday.getRegion())) {
//                        continue;
//                    }
//                    int id = holiday.getId();
//                    if (id != INVALID_FIELD) {
//                        holidayIdList.add(id);
//                    }
//                    int offOrWork = holiday.getOffOrWork();
//                    if (offOrWork == Holiday.TYPE_WORK) {
//                        offOrWorkResId = R.string.holiday_type_work;
//                    } else if (offOrWork == Holiday.TYPE_OFF) {
//                        offOrWorkResId = R.string.holiday_type_off;
//                    }
//                }
//            }
//
//            if (offOrWorkResId != INVALID_ID) {
//                mOffOrWorkTextView.setText(offOrWorkResId);
//                mOffOrWorkTextView.setVisibility(View.VISIBLE);
//            } else {
//                mOffOrWorkTextView.setVisibility(View.GONE);
//            }
//
//            mHolidayContainer.removeAllViews();
//            if (holidayIdList.size() > 0) {
//                LayoutInflater inflater = LayoutInflater.from(mContext);
//                for (int i = 0; i < holidayIdList.size(); i++) {
//                    int holidayId = holidayIdList.get(i);
//                    View holidayItem = inflater.inflate(R.layout.holiday_item_view, null);
//                    AppCompatImageView imageView = (AppCompatImageView) holidayItem.findViewById(R.id.holiday_item_image_view);
//                    imageView.setImageResource(mHolidayNationFlagId);
//                    TextView textView = (TextView) holidayItem.findViewById(R.id.holiday_item_text_view);
//                    textView.setText(mHolidayNames[holidayId]);
//                    mHolidayContainer.addView(holidayItem, i);
//                }
//                mHolidayContainer.setVisibility(View.VISIBLE);
//            } else {
//                mHolidayContainer.setVisibility(View.GONE);
//            }
//        }

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
            mActivateAreaView.setActivated(active);
        }
    }

    private boolean isToday(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        if (year == calendar.get(Calendar.YEAR)
                && month == calendar.get(Calendar.MONTH)
                && day == calendar.get(Calendar.DATE)) {
            return true;
        }
        return false;
    }

    public void computeOffset(int firstDayOfWeek) {
        mFrontOffset = findDayOffset(START_DAY_OF_WEEK, firstDayOfWeek, DAYS_IN_WEEK);
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

    private void getHolidayNames() {
        if (mHolidayRegion != null) {
            String arrayName = "holiday_" + mHolidayRegion;
            int resId = mContext.getResources().getIdentifier(arrayName, "array",
                    mContext.getPackageName());
            if (resId > 0) {
                mHolidayNames = mContext.getResources().getStringArray(resId);
            }
            String flagName = "ic_flag_" + mHolidayRegion;
            mHolidayNationFlagId = mContext.getResources().getIdentifier(flagName, "mipmap",
                    mContext.getPackageName());
        }
    }
}

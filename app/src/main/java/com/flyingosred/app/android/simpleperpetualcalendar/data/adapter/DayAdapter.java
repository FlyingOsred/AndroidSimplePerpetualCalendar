package com.flyingosred.app.android.simpleperpetualcalendar.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.flyingosred.app.android.simpleperpetualcalendar.R;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Database;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Solar;

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
            viewHolder.bindData(mDatabase.get(position - mFrontOffset),
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

        private final View mItemView;
        private final View mActivateAreaView;
        private final TextView mConstellationTextView;
        private final TextView mDateTextView;
        private final TextView mLunarTextView;
        private final TextView mSolarTermTextView;
        private final TextView mTodayTextView;
        private final TextView mOffOrWorkTextView;
        private final TextView mWeekNumberTextView;
        private final TextView mHolidayTextView;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            mItemView = itemView;
            mDateTextView = (TextView) itemView.findViewById(R.id.day_text_view);
            mConstellationTextView = (TextView) itemView.findViewById(R.id.constellation_text_view);
            mLunarTextView = (TextView) itemView.findViewById(R.id.lunar_text_view);
            mSolarTermTextView = (TextView) itemView.findViewById(R.id.solar_term_text_view);
            mTodayTextView = (TextView) itemView.findViewById(R.id.today_text_view);
            mOffOrWorkTextView = (TextView) itemView.findViewById(R.id.off_or_work_text_view);
            mWeekNumberTextView = (TextView) itemView.findViewById(R.id.week_number_text_view);
            mActivateAreaView = itemView.findViewById(R.id.activate_area_view);
            mHolidayTextView = (TextView) itemView.findViewById(R.id.holiday_text_view);
        }

        public void bindData(PerpetualCalendar calendar, boolean showWeekNumber, boolean active) {
            if (calendar == null) {
                return;
            }
            setDateText(calendar.getSolar().getDay());
            setLunarText(calendar.getLunar());
            setSolarTermText(calendar.getSolarTermId());
            setHolidayText(calendar.getHolidayList());
            setTodayText(calendar.getSolar());
            setConstellationText(calendar.getConstellationId());
            setWeekNumber(showWeekNumber, calendar.getSolar());
            setActive(active);
        }

        private void setDateText(int day) {
            mDateTextView.setText(String.valueOf(day));
        }

        private void setLunarText(Lunar lunar) {
            String lunarText = Lunar.formatMonthDayString(mContext, lunar.getMonth(), lunar.getDay());
            mLunarTextView.setText(lunarText);
        }

        private void setSolarTermText(int id) {
            if (id != INVALID_ID) {
                mSolarTermTextView.setVisibility(View.VISIBLE);
                mSolarTermTextView.setText(mContext.getResources().getStringArray(R.array.solar_term_name)[id]);
            } else {
                mSolarTermTextView.setVisibility(View.GONE);
            }
        }

        private void setHolidayText(List<Holiday> holidayList) {
            List<Integer> holidayIdList = new ArrayList<>();
            int offOrWorkResId = INVALID_ID;
            if (mHolidayRegion != null && holidayList != null) {
                for (Holiday holiday : holidayList) {
                    if (!mHolidayRegion.equals(holiday.getRegion())) {
                        continue;
                    }
                    int id = holiday.getId();
                    if (id != INVALID_FIELD) {
                        holidayIdList.add(id);
                    }
                    int offOrWork = holiday.getOffOrWork();
                    if (offOrWork == Holiday.TYPE_WORK) {
                        offOrWorkResId = R.string.holiday_type_work;
                    } else if (offOrWork == Holiday.TYPE_OFF) {
                        offOrWorkResId = R.string.holiday_type_off;
                    }
                }
            }

            if (offOrWorkResId != INVALID_ID) {
                mOffOrWorkTextView.setText(offOrWorkResId);
                mOffOrWorkTextView.setVisibility(View.VISIBLE);
            } else {
                mOffOrWorkTextView.setVisibility(View.GONE);
            }

            if (holidayIdList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < holidayIdList.size(); i++) {
                    int holidayId = holidayIdList.get(i);
                    if (i > 0) {
                        sb.append("\n");
                    }
                    sb.append(mHolidayNames[holidayId]);
                }
                mHolidayTextView.setVisibility(View.VISIBLE);
                mHolidayTextView.setText(sb.toString());
            } else {
                mHolidayTextView.setVisibility(View.GONE);
            }
        }

        private void setTodayText(Solar solar) {
            if (isToday(solar.getYear(), solar.getMonth() - 1, solar.getDay())) {
                mTodayTextView.setVisibility(View.VISIBLE);
                mTodayTextView.setText(R.string.month_day_today_text);
            } else {
                mTodayTextView.setVisibility(View.GONE);
            }
        }

        private void setConstellationText(int constellationId) {
            String constellationText = mContext.getResources().getStringArray(R.array.constellation_name)[constellationId];
            String constellationSymbol = mContext.getResources().getStringArray(R.array.constellation_symbol)[constellationId];
            mConstellationTextView.setText(constellationSymbol + constellationText);
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
        }
    }
}

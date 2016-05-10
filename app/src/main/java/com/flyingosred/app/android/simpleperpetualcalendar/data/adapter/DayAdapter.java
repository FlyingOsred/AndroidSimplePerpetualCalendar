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

import java.util.Calendar;
import java.util.List;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.DAYS_IN_WEEK;
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
            PerpetualCalendar calendar = mDatabase.get(position - mFrontOffset);
            if (calendar != null) {
                int day = calendar.getSolar().getDay();
                int month = calendar.getSolar().getMonth() - 1;
                int year = calendar.getSolar().getYear();
                viewHolder.mDateTextView.setText(String.valueOf(day));
                int constellationId = calendar.getConstellationId();
                String constellationText = viewHolder.getContext().getResources().getStringArray(R.array.constellation_name)[constellationId];
                String constellationSymbol = viewHolder.getContext().getResources().getStringArray(R.array.constellation_symbol)[constellationId];
                viewHolder.mConstellationTextView.setText(constellationSymbol + constellationText);
                String lunarText = Lunar.formatMonthDayString(viewHolder.getContext(), calendar.getLunar().getMonth(), calendar.getLunar().getDay());
                viewHolder.mLunarTextView.setText(lunarText);
                int solarTermId = calendar.getSolarTermId();
                if (solarTermId != PerpetualCalendar.INVALID_ID) {
                    viewHolder.mSolarTermTextView.setText(viewHolder.getContext().getResources().getStringArray(R.array.solar_term_name)[solarTermId]);
                } else {
                    viewHolder.mSolarTermTextView.setText("");
                }
                if (isToday(year, month, day)) {
                    viewHolder.mTodayTextView.setText(R.string.month_day_today_text);
                } else {
                    viewHolder.mTodayTextView.setText("");
                }
                if (mShowWeekNumber && (position % PerpetualCalendar.DAYS_IN_WEEK == 0)) {
                    Calendar weekCalendar = Calendar.getInstance();
                    weekCalendar.setFirstDayOfWeek(mFirstDayOfWeek);
                    weekCalendar.set(year, month, day);
                    int weekNumber = weekCalendar.get(Calendar.WEEK_OF_YEAR);
                    viewHolder.mWeekNumberTextView.setText(String.valueOf(weekNumber));
                    viewHolder.mWeekNumberTextView.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mWeekNumberTextView.setVisibility(View.GONE);
                }
                if (position == mActivatedItem) {
                    viewHolder.mActivateAreaView.setActivated(true);
                } else {
                    viewHolder.mActivateAreaView.setActivated(false);
                }
                if (mHolidayRegion != null && calendar.getHolidayList() != null) {
                    List<Holiday> list = calendar.getHolidayList();
                    for (Holiday holiday : list) {
                        if (mHolidayRegion.equals(holiday.getRegion())) {
                            if (holiday.getId() != Holiday.INVALID_FIELD) {
                                viewHolder.mHolidayTextView.setText(mHolidayNames[holiday.getId()]);
                            } else {
                                viewHolder.mHolidayTextView.setText("");
                            }
                            if (holiday.getOffOrWork() != Holiday.INVALID_FIELD) {
                                if (holiday.getOffOrWork() == Holiday.TYPE_WORK) {
                                    viewHolder.mOffOrWorkTextView.setText(R.string.holiday_type_work);
                                } else {
                                    viewHolder.mOffOrWorkTextView.setText(R.string.holiday_type_off);
                                }
                            }
                        } else {
                            //viewHolder.mOffOrWorkTextView.setText("");
                        }
                    }
                }
            }
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

        public final View mItemView;
        public final View mActivateAreaView;
        public final TextView mConstellationTextView;
        public final TextView mDateTextView;
        public final TextView mLunarTextView;
        public final TextView mSolarTermTextView;
        public final TextView mTodayTextView;
        public final TextView mOffOrWorkTextView;
        public final TextView mWeekNumberTextView;
        public final TextView mHolidayTextView;

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

        public Context getContext() {
            return mContext;
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

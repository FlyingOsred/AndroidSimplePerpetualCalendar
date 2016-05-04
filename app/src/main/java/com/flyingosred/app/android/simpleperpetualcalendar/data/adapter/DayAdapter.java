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
import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.loader.Content;

import java.util.Calendar;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.DAYS_IN_WEEK;
import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.START_DAY_OF_WEEK;
import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

public class DayAdapter extends RecyclerView.Adapter {

    private Content mContent = null;

    private int mFrontOffset = 0;

    private int mFirstDayOfWeek;

    private boolean mShowWeekNumber;

    private int mActivatedItem = AdapterView.INVALID_POSITION;

    public DayAdapter(int firstDayOfWeek, boolean showWeekNumber) {
        mShowWeekNumber = showWeekNumber;
        mFirstDayOfWeek = firstDayOfWeek;
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
        if (mContent != null) {
            PerpetualCalendar calendar = mContent.get(position - mFrontOffset);
            if (calendar != null) {
                int day = calendar.get().get(Calendar.DATE);
                int month = calendar.get().get(Calendar.MONTH);
                int year = calendar.get().get(Calendar.YEAR);
                viewHolder.mDateTextView.setText(String.valueOf(day));
                int constellationId = calendar.getConstellationId();
                String constellationText = viewHolder.getContext().getResources().getStringArray(R.array.constellation_name)[constellationId];
                String constellationSymbol = viewHolder.getContext().getResources().getStringArray(R.array.constellation_symbol)[constellationId];
                viewHolder.mConstellationTextView.setText(constellationSymbol + constellationText);
                String lunarText = Lunar.formatMonthDayString(viewHolder.getContext(), calendar.getLunar().getMonth(), calendar.getLunar().getDay());
                viewHolder.mLunarTextView.setText(lunarText);
                int solarTermId = calendar.getSolarTermId();
                if (solarTermId != PerpetualCalendar.INVALID_ID) {
                    viewHolder.mSolarTermTextView.setText(viewHolder.getContext().getResources().getStringArray(R.array.solar_term)[solarTermId]);
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
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mContent != null) {
            return mContent.getCount() + mFrontOffset;
        }
        return 0;
    }

    public void changeContent(Content content) {
        mContent = content;
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final Context mContext;
        public final View mItemView;
        public final View mActivateAreaView;
        public final TextView mConstellationTextView;
        public final TextView mDateTextView;
        public final TextView mLunarTextView;
        public final TextView mSolarTermTextView;
        public final TextView mTodayTextView;
        public final TextView mOffOrWorkTextView;
        public final TextView mWeekNumberTextView;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            mItemView = itemView;
            mDateTextView = (TextView) itemView.findViewById(R.id.day_text_view);
            mConstellationTextView = (TextView) itemView.findViewById(R.id.constellation_text_view);
            mLunarTextView = (TextView) itemView.findViewById(R.id.lunar_text_view);
            mSolarTermTextView = (TextView) itemView.findViewById(R.id.solar_term_text_view);
            mTodayTextView = (TextView) itemView.findViewById(R.id.today_text_view);
            mOffOrWorkTextView = (TextView) itemView.findViewById(R.id.off_or_work_text_view);
            mWeekNumberTextView = (TextView) itemView.findViewById(R.id.week_number_text_view);
            mActivateAreaView = (View) itemView.findViewById(R.id.activate_area_view);
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
        if (mContent != null) {
            Calendar calendar = Calendar.getInstance();
            int today = mContent.get(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
            position = today + mFrontOffset;
        }
        return position;
    }

    public PerpetualCalendar get(int position) {
        if (position >= mFrontOffset && mContent != null) {
            return mContent.get(position - mFrontOffset);
        }
        return null;
    }
}

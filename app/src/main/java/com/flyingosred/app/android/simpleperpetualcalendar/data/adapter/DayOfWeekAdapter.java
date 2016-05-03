package com.flyingosred.app.android.simpleperpetualcalendar.data.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyingosred.app.android.simpleperpetualcalendar.R;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;

import java.util.Calendar;
import java.util.Locale;

public class DayOfWeekAdapter extends RecyclerView.Adapter {

    private int mFirstDayOfWeek = Calendar.SUNDAY;

    private boolean mShowWeekNumber = false;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_of_week_recycler_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (mShowWeekNumber) {
            viewHolder.mWeekNumberTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mWeekNumberTextView.setVisibility(View.GONE);
        }
        int dayOfWeek = mFirstDayOfWeek + position;
        if (dayOfWeek > PerpetualCalendar.DAYS_IN_WEEK) {
            dayOfWeek -= PerpetualCalendar.DAYS_IN_WEEK;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        String dayOfWeekString = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
                Locale.getDefault()).toUpperCase(Locale.getDefault());
        viewHolder.mDayOfWeekTextView.setText(dayOfWeekString);
    }

    @Override
    public int getItemCount() {
        return PerpetualCalendar.DAYS_IN_WEEK;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mDayOfWeekTextView;
        public final TextView mWeekNumberTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mDayOfWeekTextView = (TextView) itemView.findViewById(R.id.day_of_week_text_view);
            mWeekNumberTextView = (TextView) itemView.findViewById(R.id.week_number_text_view);
        }
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        if (mFirstDayOfWeek != firstDayOfWeek) {
            mFirstDayOfWeek = firstDayOfWeek;
            notifyDataSetChanged();
        }
    }
}

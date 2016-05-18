package com.flyingosred.app.android.simpleperpetualcalendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.util.Utils;

import java.util.Calendar;
import java.util.Locale;

import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

public class DayCardFragment extends Fragment {

    public static final String ARG_DATE = "date";

    private PerpetualCalendar mPerpetualCalendar = null;

    private Calendar mCalendar = null;

    private TextView mDayTextView = null;

    private TextView mDayOfWeekTextView = null;

    private TextView mOffWorkTextView = null;

    private TextView mTodayTextView = null;

    public DayCardFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate called");
        if (getArguments().containsKey(ARG_DATE)) {
            mPerpetualCalendar = getArguments().getParcelable(DayCardFragment.ARG_DATE);
            mCalendar = Calendar.getInstance();
            mCalendar.set(mPerpetualCalendar.getSolar().getYear(),
                    mPerpetualCalendar.getSolar().getMonth() - 1,
                    mPerpetualCalendar.getSolar().getDay());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_day_card, container, false);
        mDayTextView = (TextView) view.findViewById(R.id.day_card_day_text_view);
        mDayOfWeekTextView = (TextView) view.findViewById(R.id.day_card_day_of_week_text_view);
        mOffWorkTextView = (TextView) view.findViewById(R.id.day_card_off_work_text_view);
        mTodayTextView = (TextView) view.findViewById(R.id.day_card_today_text_view);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated called");
        String date = DateUtils.formatDateRange(getContext(), mCalendar.getTimeInMillis(),
                mCalendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY
                        | DateUtils.FORMAT_SHOW_YEAR).toUpperCase(Locale.getDefault());
        getActivity().setTitle(date);
    }

    private void initViews(View parentView) {
        Log.d(LOG_TAG, "initViews called");

        TextView dayTextView = (TextView) parentView.findViewById(R.id.day_card_day_text_view);
        dayTextView.setText(String.valueOf(mCalendar.get(Calendar.DATE)));

        TextView dayOfWeekTextView = (TextView) parentView.findViewById(R.id.day_card_day_of_week_text_view);
        String dayOfWeekString = mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,
                Locale.getDefault());
        dayOfWeekTextView.setText(dayOfWeekString);

        TextView offWorkTextView = (TextView) parentView.findViewById(R.id.day_card_off_work_text_view);

        TextView todayTextView = (TextView) parentView.findViewById(R.id.day_card_today_text_view);
        if (Utils.isToday(mCalendar)) {
            todayTextView.setVisibility(View.VISIBLE);
        } else {
            todayTextView.setVisibility(View.INVISIBLE);
        }

        Lunar lunar = mPerpetualCalendar.getLunar();
        TextView lunarDayTextView = (TextView) parentView.findViewById(R.id.day_card_lunar_day_text_view);
        String lunarDayString = Lunar.formatDayString(getContext(), lunar, true);
        lunarDayTextView.setText(lunarDayString);

        TextView lunarMonthTextView = (TextView) parentView.findViewById(R.id.day_card_lunar_month_text_view);
        String lunarMonthString = Lunar.formatMonthString(getContext(), lunar, true, true);
        lunarMonthTextView.setText(lunarMonthString);
    }
}

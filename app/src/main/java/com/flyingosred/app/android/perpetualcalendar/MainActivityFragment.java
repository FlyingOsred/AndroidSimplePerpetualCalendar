/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract;
import com.flyingosred.app.android.perpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.perpetualcalendar.data.adapter.DayAdapter;
import com.flyingosred.app.android.perpetualcalendar.data.adapter.DayOfWeekAdapter;
import com.flyingosred.app.android.perpetualcalendar.view.DayRecyclerView;

import java.util.Calendar;
import java.util.Locale;

import static com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract.MAX_DATE;
import static com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract.MIN_DATE;
import static com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarContract.PerpetualCalendar.getDefaultProjection;
import static com.flyingosred.app.android.perpetualcalendar.util.Utils.LOG_TAG;

public class MainActivityFragment extends Fragment implements
        SharedPreferences.OnSharedPreferenceChangeListener, RecyclerView.OnItemTouchListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CONTENT_LOADER_ID = 1;

    private ProgressBar mProgressBar = null;

    private DayRecyclerView mDayView = null;

    private RecyclerView mDayOfWeekView = null;

    private DayAdapter mDayAdapter = null;

    private DayOfWeekAdapter mDayOfWeekAdapter = null;

    private GestureDetectorCompat mGestureDetector = null;

    private DateChangeReceiver mDateChangeReceiver = new DateChangeReceiver();

    private OnDaySelectedListener mOnDaySelectedListener = null;

    private String mHolidayRegion;

    private int mFirstDayOfWeek;

    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnDaySelectedListener = (OnDaySelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnDaySelectedListener");
        }

        getContext().registerReceiver(mDateChangeReceiver, new IntentFilter(Intent.ACTION_DATE_CHANGED));

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mDayView = (DayRecyclerView) view.findViewById(R.id.day_recycler_view);
        mDayOfWeekView = (RecyclerView) view.findViewById(R.id.day_of_week_recycler_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar_loading);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        mFirstDayOfWeek = getPrefFirstDayOfWeek(mSharedPreferences);
        Log.d(LOG_TAG, "mFirstDayOfWeek is " + mFirstDayOfWeek);
        boolean showWeekNumber = getPrefShowWeekNumber(mSharedPreferences);
        mHolidayRegion = getPrefHolidayRegion(mSharedPreferences);
        Log.d(LOG_TAG, "Holiday region is " + mHolidayRegion);
        mGestureDetector = new GestureDetectorCompat(getActivity(), new DayViewOnGestureListener());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        mDayView.setLayoutManager(layoutManager);
        mDayAdapter = new DayAdapter(getContext(), mFirstDayOfWeek, showWeekNumber, mHolidayRegion);
        mDayAdapter.registerAdapterDataObserver(new DayAdapterDataObserver());
        mDayView.setHasFixedSize(true);
        mDayView.setAdapter(mDayAdapter);
        mDayView.addOnItemTouchListener(this);
        mDayView.addOnScrollListener(new DayViewOnScrollListener());
        scrollToDate(Calendar.getInstance());
        GridLayoutManager dayOfWeekLayout = new GridLayoutManager(getContext(), 7);
        mDayOfWeekView.setLayoutManager(dayOfWeekLayout);
        mDayOfWeekAdapter = new DayOfWeekAdapter(mFirstDayOfWeek, showWeekNumber);
        mDayOfWeekView.setAdapter(mDayOfWeekAdapter);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }
        getActivity().getSupportLoaderManager().initLoader(CONTENT_LOADER_ID, null, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getContext().unregisterReceiver(mDateChangeReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_fragment_month_view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Log.d(LOG_TAG, "option selected " + id);

        if (id == R.id.action_today) {
            scrollToDate(Calendar.getInstance());
            return true;
        } else if (id == R.id.action_select_date) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = PerpetualCalendarContract.PerpetualCalendar.getUri(MIN_DATE, MAX_DATE);
        Log.d(LOG_TAG, "onCreateLoader uri is " + uri.toString());
        String[] projection = getDefaultProjection(mHolidayRegion);
        return new CursorLoader(getContext(), uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");
        mDayAdapter.swapCursor(data);
        scrollToDate(Calendar.getInstance());
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
        mDayAdapter.swapCursor(null);
    }

    public interface OnDaySelectedListener {
        void onDaySelected(DisplayCalendar displayCalendar);
    }

    private int getPrefFirstDayOfWeek(SharedPreferences sharedPreferences) {
        String key = getString(R.string.pref_key_first_day_of_week);
        String defaultValue = getString(R.string.pref_first_day_of_week_default);
        String pref = sharedPreferences.getString(key, defaultValue);

        int firstDayOfWeek = Calendar.getInstance().getFirstDayOfWeek();
        if (!defaultValue.equals(pref)) {
            int value = Integer.parseInt(pref);
            if (value == Calendar.SATURDAY) {
                firstDayOfWeek = Calendar.SATURDAY;
            } else if (value == Calendar.MONDAY) {
                firstDayOfWeek = Calendar.MONDAY;
            } else if (value == Calendar.SUNDAY) {
                firstDayOfWeek = Calendar.SUNDAY;
            }
        }

        return firstDayOfWeek;
    }

    private void setFirstDayOfWeek(int firstDayOfWeek) {
        if (mDayAdapter != null) {
            mDayAdapter.setFirstDayOfWeek(firstDayOfWeek);
        }
        if (mDayOfWeekAdapter != null) {
            mDayOfWeekAdapter.setFirstDayOfWeek(firstDayOfWeek);
        }
    }

    private void setShowWeekNumber(boolean showWeekNumber) {
        if (mDayAdapter != null) {
            mDayAdapter.setShowWeekNumber(showWeekNumber);
        }
        if (mDayOfWeekAdapter != null) {
            mDayOfWeekAdapter.setShowWeekNumber(showWeekNumber);
        }
    }

    private void setHolidayRegion(String region) {
        if (mDayAdapter != null) {
            mDayAdapter.setHolidayRegion(region);
        }
    }

    private boolean getPrefShowWeekNumber(SharedPreferences sharedPreferences) {
        String key = getString(R.string.pref_key_show_week_number);
        return sharedPreferences.getBoolean(key, false);
    }

    private String getPrefHolidayRegion(SharedPreferences sharedPreferences) {
        String key = getString(R.string.pref_key_holiday_region);
        String defaultRegion = getString(R.string.pref_holiday_region_default);
        return sharedPreferences.getString(key, defaultRegion);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(LOG_TAG, "onSharedPreferenceChanged key is " + key);
        if (key.equals(getString(R.string.pref_key_first_day_of_week))) {
            int firstDayOfWeek = getPrefFirstDayOfWeek(sharedPreferences);
            setFirstDayOfWeek(firstDayOfWeek);
        } else if (key.equals(getString(R.string.pref_key_show_week_number))) {
            boolean showWeekNumber = getPrefShowWeekNumber(sharedPreferences);
            setShowWeekNumber(showWeekNumber);
        } else if (key.equals(getString(R.string.pref_key_holiday_region))) {
            String holidayRegion = getPrefHolidayRegion(sharedPreferences);
            getLoaderManager().getLoader(CONTENT_LOADER_ID).forceLoad();
            setHolidayRegion(holidayRegion);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(LOG_TAG, "onTouchEvent e is " + e);

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Log.d(LOG_TAG, "onRequestDisallowInterceptTouchEvent disallowIntercept is " + disallowIntercept);

    }

    private class DayViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = mDayView.findChildViewUnder(e.getX(), e.getY());
            int position = mDayView.getChildAdapterPosition(view);
            Log.d(LOG_TAG, "onSingleTapConfirmed position is " + position);
            activeItem(position, true);
            return super.onSingleTapConfirmed(e);
        }
    }

    private void updateTitle(int position) {
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        DayAdapter.ViewHolder viewHolder = (DayAdapter.ViewHolder) mDayView.findViewHolderForAdapterPosition(position);
        Cursor cursor = viewHolder.getData();
        if (cursor != null && actionBar != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(PerpetualCalendarContract.PerpetualCalendar.SOLAR)));
            String date = DateUtils.formatDateRange(getContext(), calendar.getTimeInMillis(),
                    calendar.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_MONTH_DAY
                            | DateUtils.FORMAT_SHOW_YEAR).toUpperCase(Locale.getDefault());
            actionBar.setTitle(date);
        }
    }

    private class DayViewOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if ((dx != 0 || dy != 0)
                    && recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_DRAGGING) {
                View view = recyclerView.findChildViewUnder(dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int firstItem = layoutManager.findFirstCompletelyVisibleItemPosition();
                int lastItem = layoutManager.findLastCompletelyVisibleItemPosition();
                final int count = (lastItem - firstItem) / 2;
                int center = firstItem + count;
                updateTitle(center);
            }
        }
    }

    private class DayAdapterDataObserver extends RecyclerView.AdapterDataObserver {

        public void onChanged() {
            Log.d(LOG_TAG, "DayAdapterDataObserver onChanged");
        }
    }

    public void scrollToDate(Calendar calendar) {
        int position = mDayAdapter.getPosition(calendar);
        Log.d(LOG_TAG, "scrollToDate " + calendar.getTime() + " position " + position);
        GridLayoutManager layoutManager = (GridLayoutManager) mDayView.getLayoutManager();
        int offset = PerpetualCalendar.DAYS_IN_WEEK * 2 + findDayOffset(calendar);
        int innerPosition = position - offset;
        if (innerPosition < 0) {
            innerPosition = 0;
        }
        Log.d(LOG_TAG, "scrollToDate position with offset is " + innerPosition);
        layoutManager.scrollToPositionWithOffset(innerPosition, offset);
        mDayAdapter.activateItem(position);
    }

    private void activeItem(int position, boolean showCard) {
        if (showCard) {
            DayAdapter.ViewHolder viewHolder =
                    (DayAdapter.ViewHolder) mDayView.findViewHolderForAdapterPosition(position);
            //mOnDaySelectedListener.onDaySelected(viewHolder.getDisplayCalendar());
        }
        mDayAdapter.activateItem(position);
    }

    private class DateChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "onReceive date change");
        }
    }

    private int findDayOffset(Calendar calendar) {
        int dayOfWeekStart = calendar.get(Calendar.DAY_OF_WEEK);
        final int offset = dayOfWeekStart - mFirstDayOfWeek;
        if (dayOfWeekStart < mFirstDayOfWeek) {
            return offset + PerpetualCalendar.DAYS_IN_WEEK;
        }
        return offset;
    }
}

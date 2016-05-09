package com.flyingosred.app.android.simpleperpetualcalendar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GestureDetectorCompat;
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
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Database;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.adapter.DayAdapter;
import com.flyingosred.app.android.simpleperpetualcalendar.data.adapter.DayOfWeekAdapter;
import com.flyingosred.app.android.simpleperpetualcalendar.data.loader.ContentLoader;
import com.flyingosred.app.android.simpleperpetualcalendar.view.DayRecyclerView;

import java.util.Calendar;
import java.util.Locale;

import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Database>,
        SharedPreferences.OnSharedPreferenceChangeListener, RecyclerView.OnItemTouchListener {

    private static final int CONTENT_LOADER_ID = 1;

    private ProgressBar mProgressBar = null;

    private DayRecyclerView mDayView = null;

    private RecyclerView mDayOfWeekView = null;

    private DayAdapter mDayAdapter = null;

    private DayOfWeekAdapter mDayOfWeekAdapter = null;

    private SharedPreferences mSharedPreferences = null;

    private GestureDetectorCompat mGestureDetector = null;

    private boolean mScrollToToday = false;

    public MainActivityFragment() {
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
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        int firstDayOfWeek = getPrefFirstDayOfWeek(mSharedPreferences);
        boolean showWeekNumber = getPrefShowWeekNumber(mSharedPreferences);
        mGestureDetector = new GestureDetectorCompat(getActivity(), new DayViewOnGestureListener());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        mDayView.setLayoutManager(layoutManager);
        mDayAdapter = new DayAdapter(firstDayOfWeek, showWeekNumber);
        mDayAdapter.registerAdapterDataObserver(new DayAdapterDataObserver());
        mDayView.setAdapter(mDayAdapter);
        mDayView.addOnItemTouchListener(this);
        mDayView.addOnScrollListener(new DayViewOnScrollListener());
        GridLayoutManager dayOfWeekLayout = new GridLayoutManager(getContext(), 7);
        mDayOfWeekView.setLayoutManager(dayOfWeekLayout);
        mDayOfWeekAdapter = new DayOfWeekAdapter(firstDayOfWeek, showWeekNumber);
        mDayOfWeekView.setAdapter(mDayOfWeekAdapter);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("");
        getActivity().getSupportLoaderManager().initLoader(CONTENT_LOADER_ID, null, this);
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
            scrollToToday();
            return true;
        } else if (id == R.id.action_select_date) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Database> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        return new ContentLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Database> loader, Database database) {
        Log.d(LOG_TAG, "onLoadFinished");
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
        if (mDayAdapter != null) {
            mDayAdapter.changeDatabase(database);
        }
        mScrollToToday = true;
    }

    @Override
    public void onLoaderReset(Loader<Database> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
        if (mDayAdapter != null) {
            mDayAdapter.changeDatabase(null);
        }
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

    private boolean getPrefShowWeekNumber(SharedPreferences sharedPreferences) {
        String key = getString(R.string.pref_key_show_week_number);
        boolean showWeekNumber = sharedPreferences.getBoolean(key, false);
        return showWeekNumber;
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
            mDayAdapter.activateItem(position);
            return super.onSingleTapConfirmed(e);
        }
    }

    private void scrollToToday() {
        int today = mDayAdapter.getTodayPosition();
        if (today != AdapterView.INVALID_POSITION) {
            GridLayoutManager layoutManager = (GridLayoutManager) mDayView.getLayoutManager();
            int offset = PerpetualCalendar.DAYS_IN_WEEK * 2;
            int position = today - offset;
            if (position < 0) {
                position = 0;
            }
            Log.d(LOG_TAG, "Today position with offset is " + position);
            layoutManager.scrollToPositionWithOffset(position, offset);
        }
    }

    private void updateTitle(int position) {
        PerpetualCalendar perpetualCalendar = mDayAdapter.get(position);
        if (perpetualCalendar != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(perpetualCalendar.getSolar().getYear(),
                    perpetualCalendar.getSolar().getMonth() - 1,
                    perpetualCalendar.getSolar().getDay());
            String date = DateUtils.formatDateRange(
                    getContext(),
                    calendar.getTimeInMillis(),
                    calendar.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_MONTH_DAY
                            | DateUtils.FORMAT_SHOW_YEAR).toString().toUpperCase(Locale.getDefault());
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(date);
        }
    }

    private class DayViewOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            View view = recyclerView.findChildViewUnder(dx, dy);
            int position = recyclerView.getChildAdapterPosition(view);
            Log.d(LOG_TAG, "onScrolled position is " + position);
            GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            int firstItem = layoutManager.findFirstCompletelyVisibleItemPosition();
            int lastItem = layoutManager.findLastCompletelyVisibleItemPosition();
            final int count = (lastItem - firstItem) / 2;
            int center = firstItem + count;
            updateTitle(center);
        }
    }

    private class DayAdapterDataObserver extends RecyclerView.AdapterDataObserver {

        public void onChanged() {
            Log.d(LOG_TAG, "DayAdapterDataObserver onChanged");
            scrollToToday();
        }
    }
}

package com.flyingosred.app.android.simpleperpetualcalendar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Database;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.adapter.DayAdapter;
import com.flyingosred.app.android.simpleperpetualcalendar.data.adapter.DayOfWeekAdapter;
import com.flyingosred.app.android.simpleperpetualcalendar.data.loader.ContentLoader;
import com.flyingosred.app.android.simpleperpetualcalendar.view.DayInfoView;
import com.flyingosred.app.android.simpleperpetualcalendar.view.DayRecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    private DayInfoView mDayInfoView;

    private CountDownTimer mDayInfoViewCountDownTimer = null;

    //private Animator mDayInfoAnimator = null;

    //private View mDetailView = null;

    //private CountDownTimer mDetailViewCountDownTimer = null;

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
        mDayInfoView = (DayInfoView) view.findViewById(R.id.expend_day_info_view);
        //mDetailView = view.findViewById(R.id.day_detail_view);
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
        String holidayRegion = getPrefHolidayRegion(mSharedPreferences);
        Log.d(LOG_TAG, "Holiday region is " + holidayRegion);
        mGestureDetector = new GestureDetectorCompat(getActivity(), new DayViewOnGestureListener());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        mDayView.setLayoutManager(layoutManager);
        mDayAdapter = new DayAdapter(getContext(), firstDayOfWeek, showWeekNumber, holidayRegion);
        mDayAdapter.registerAdapterDataObserver(new DayAdapterDataObserver());
        mDayView.setHasFixedSize(true);
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

    private void setHolidayRegion(String region) {
        if (mDayAdapter != null) {
            mDayAdapter.setHolidayRegion(region);
        }
    }

    private boolean getPrefShowWeekNumber(SharedPreferences sharedPreferences) {
        String key = getString(R.string.pref_key_show_week_number);
        boolean showWeekNumber = sharedPreferences.getBoolean(key, false);
        return showWeekNumber;
    }

    private String getPrefHolidayRegion(SharedPreferences sharedPreferences) {
        String key = getString(R.string.pref_key_holiday_region);
        String defaultRegion = getString(R.string.pref_holiday_region_default);
        String region = sharedPreferences.getString(key, defaultRegion);
        return region;
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
            activeItem(position);
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
            activeItem(today);
        }
    }

    private void updateTitle(int position) {
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        PerpetualCalendar perpetualCalendar = mDayAdapter.get(position);
        if (perpetualCalendar != null && actionBar != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(perpetualCalendar.getSolar().getYear(),
                    perpetualCalendar.getSolar().getMonth() - 1,
                    perpetualCalendar.getSolar().getDay());
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
            if (mDayInfoView.getVisibility() == View.VISIBLE) {
                mDayInfoViewCountDownTimer.cancel();
                mDayInfoView.setVisibility(View.INVISIBLE);
            }
//            if (mDetailView.getVisibility() == View.VISIBLE) {
//                mDetailViewCountDownTimer.cancel();
//                mDetailView.setVisibility(View.GONE);
//            }
        }

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

    public void scrollToDate(int year, int month, int day) {
        int position = mDayAdapter.getPosition(year, month, day);
        Log.d(LOG_TAG, "scrollToDate year " + year + " month " + month + " day " + day
                + " position " + position);
        if (position != AdapterView.INVALID_POSITION) {
            GridLayoutManager layoutManager = (GridLayoutManager) mDayView.getLayoutManager();
            int offset = PerpetualCalendar.DAYS_IN_WEEK * 2;
            int innerPosition = position - offset;
            if (innerPosition < 0) {
                innerPosition = 0;
            }
            Log.d(LOG_TAG, "Today position with offset is " + innerPosition);
            layoutManager.scrollToPositionWithOffset(innerPosition, offset);
        }
    }

    private void activeItem(int position) {
        //showDetailView(mDayAdapter.get(position), true);
        GridLayoutManager layoutManager = (GridLayoutManager) mDayView.getLayoutManager();
        showExpandDayInfoView(layoutManager.findViewByPosition(position), mDayAdapter.get(position), true);
        mDayAdapter.activateItem(position);
    }

    private void showExpandDayInfoView(View view, PerpetualCalendar calendar, boolean show) {
        if (!show || calendar == null || view == null || getView() == null) {
            mDayInfoView.setVisibility(View.INVISIBLE);
        } else {
            mDayInfoView.setData(calendar);
            mDayInfoView.setVisibility(View.VISIBLE);

//            final Rect parentBounds = new Rect();
//            final Rect startBounds = new Rect();
//            final Rect finalBounds = new Rect();
//            final Point globalOffset = new Point();
//
//            getView().getGlobalVisibleRect(parentBounds, globalOffset);
//            view.getGlobalVisibleRect(startBounds);
//            mDayInfoView.getGlobalVisibleRect(finalBounds);
//
//            Log.d(LOG_TAG, "startBounds is  " + startBounds + " finalBounds is " + finalBounds
//                    + " globalOffset is " + globalOffset);
//
//            startBounds.offset(-globalOffset.x, -globalOffset.y);
//            finalBounds.offset(-globalOffset.x, -globalOffset.y);
//
//            if ((startBounds.left) <= parentBounds.width() / 2) {
//                finalBounds.left = startBounds.right;
//                finalBounds.right = finalBounds.left + finalBounds.width();
//            } else {
//                finalBounds.left = startBounds.left - finalBounds.width();
//                finalBounds.right = finalBounds.left + finalBounds.width();
//            }
//            if ((startBounds.top) <= parentBounds.height() / 2) {
//                finalBounds.top = startBounds.bottom;
//                finalBounds.bottom = finalBounds.top + finalBounds.height();
//            } else {
//                finalBounds.top = startBounds.top - finalBounds.height();
//                finalBounds.bottom = finalBounds.top + finalBounds.height();
//            }
//
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mDayInfoView.getLayoutParams();
//            layoutParams.topMargin = finalBounds.top;
//            layoutParams.leftMargin = finalBounds.left;
//            mDayInfoView.setLayoutParams(layoutParams);
//
//            float startScale;
//            if ((float) finalBounds.width() / finalBounds.height()
//                    > (float) startBounds.width() / startBounds.height()) {
//                startScale = (float) startBounds.height() / finalBounds.height();
//                float startWidth = startScale * finalBounds.width();
//                float deltaWidth = (startWidth - startBounds.width()) / 2;
//                startBounds.left -= deltaWidth;
//                startBounds.right += deltaWidth;
//            } else {
//                startScale = (float) startBounds.width() / finalBounds.width();
//                float startHeight = startScale * finalBounds.height();
//                float deltaHeight = (startHeight - startBounds.height()) / 2;
//                startBounds.top -= deltaHeight;
//                startBounds.bottom += deltaHeight;
//            }
//
//            Log.d(LOG_TAG, "startBounds is  " + startBounds + " finalBounds is " + finalBounds
//                    + " startScale is " + startScale);
//
//            AnimatorSet set = new AnimatorSet();
//            set
//                    .play(ObjectAnimator.ofFloat(mDayInfoView, View.X, startBounds.left,
//                            finalBounds.left))
//                    .with(ObjectAnimator.ofFloat(mDayInfoView, View.Y, startBounds.top,
//                            finalBounds.top))
//                    .with(ObjectAnimator.ofFloat(mDayInfoView, View.SCALE_X, startScale, 1f))
//                    .with(ObjectAnimator.ofFloat(mDayInfoView, View.SCALE_Y, startScale, 1f));
//            final int shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
//            set.setDuration(shortAnimationDuration);
//            set.setInterpolator(new DecelerateInterpolator());
//            set.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mDayInfoAnimator = null;
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//                    mDayInfoAnimator = null;
//                }
//            });
//            set.start();
//            mDayInfoAnimator = set;
//            final float startScaleFinal = startScale;
//            mDayInfoView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mDayInfoAnimator != null) {
//                        mDayInfoAnimator.cancel();
//                    }
//
//                    // Animate the four positioning/sizing properties in parallel, back to their
//                    // original values.
//                    AnimatorSet set = new AnimatorSet();
//                    set
//                            .play(ObjectAnimator.ofFloat(mDayInfoView, View.X, startBounds.left))
//                            .with(ObjectAnimator.ofFloat(mDayInfoView, View.Y, startBounds.top))
//                            .with(ObjectAnimator
//                                    .ofFloat(mDayInfoView, View.SCALE_X, startScaleFinal))
//                            .with(ObjectAnimator
//                                    .ofFloat(mDayInfoView, View.SCALE_Y, startScaleFinal));
//                    set.setDuration(shortAnimationDuration);
//                    set.setInterpolator(new DecelerateInterpolator());
//                    set.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            mDayInfoView.setVisibility(View.GONE);
//                            mDayInfoAnimator = null;
//                        }
//
//                        @Override
//                        public void onAnimationCancel(Animator animation) {
//                            mDayInfoView.setVisibility(View.GONE);
//                            mDayInfoAnimator = null;
//                        }
//                    });
//                    set.start();
//                    mDayInfoAnimator = set;
//                }
//            });
            if (mDayInfoViewCountDownTimer == null) {
                mDayInfoViewCountDownTimer = new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                    }
                };
            } else {
                mDayInfoViewCountDownTimer.cancel();
            }
            mDayInfoViewCountDownTimer.start();
        }
    }

//    private void showDetailView(PerpetualCalendar calendar, boolean show) {
//        if (!show || calendar == null) {
//            mDetailView.setVisibility(View.GONE);
//        } else {
//            setDetailViewContent(calendar);
//            mDetailView.setVisibility(View.VISIBLE);
//            if (mDetailViewCountDownTimer == null) {
//                mDetailViewCountDownTimer = new CountDownTimer(3000, 1000) {
//
//                    public void onTick(long millisUntilFinished) {
//                    }
//
//                    public void onFinish() {
//                        mDetailView.setVisibility(View.GONE);
//                    }
//                };
//            } else {
//                mDetailViewCountDownTimer.cancel();
//            }
//            mDetailViewCountDownTimer.start();
//        }
//    }

//    private void setDetailViewContent(PerpetualCalendar perpetualCalendar) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(perpetualCalendar.getSolar().getYear(),
//                perpetualCalendar.getSolar().getMonth() - 1,
//                perpetualCalendar.getSolar().getDay());
//        TextView dateTextView = (TextView) mDetailView.findViewById(R.id.day_detail_info_date_text_view);
//        String date = DateUtils.formatDateRange(getContext(), calendar.getTimeInMillis(),
//                calendar.getTimeInMillis(),
//                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY
//                        | DateUtils.FORMAT_SHOW_YEAR).toUpperCase(Locale.getDefault());
//        dateTextView.setText(date);
//        TextView dayTextView = (TextView) mDetailView.findViewById(R.id.day_detail_info_day_text_view);
//        dayTextView.setText(String.valueOf(calendar.get(Calendar.DATE)));
//        TextView lunarDateTextView = (TextView) mDetailView.findViewById(R.id.day_detail_info_lunar_date_text_view);
//        String lunarDate = Lunar.formatFullString(getContext(), perpetualCalendar.getLunar());
//        lunarDateTextView.setText(lunarDate);
//    }

}

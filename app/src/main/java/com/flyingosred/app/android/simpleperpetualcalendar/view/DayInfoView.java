package com.flyingosred.app.android.simpleperpetualcalendar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyingosred.app.android.simpleperpetualcalendar.DisplayCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.R;

import java.util.Calendar;

import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

public class DayInfoView extends LinearLayout {

    private static final int SIZE_TYPE_NORMAL = 0;

    private static final int SIZE_TYPE_LARGE = 1;

    private final int mSizeType;

    private TextViewHolder mDateTextHolder;

    private TextViewHolder mDayTextHolder;

    private TextViewHolder mConstellationTextHolder;

    private TextViewHolder mTodayTextHolder;

    private TextViewHolder mOffWorkTextHolder;

    private TextViewHolder mLunarTextHolder;

    private TextViewHolder mSolarTermTextHolder;

    private LinearLayout mHolidayContainer;

    public DayInfoView(Context context) {
        this(context, null);
    }

    public DayInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.day_info_view, this, true);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DayInfoView,
                0, 0);
        try {
            mSizeType = a.getInteger(R.styleable.DayInfoView_viewSize, SIZE_TYPE_NORMAL);
        } finally {
            a.recycle();
        }
        Log.d(LOG_TAG, "Size type is " + mSizeType);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    public void setData(DisplayCalendar displayCalendar) {
        if (displayCalendar == null) {
            return;
        }
        mDateTextHolder.setText(displayCalendar.getDate());
        mDayTextHolder.setText(displayCalendar.getDay());
        mLunarTextHolder.setText(displayCalendar.getLunarShort());
        setSolarTermText(displayCalendar.getSolarTerm());
        mConstellationTextHolder.setText(displayCalendar.getConstellation());
    }

    private void initViews() {
        mDayTextHolder = new TextViewHolder("day", mSizeType);
        mDateTextHolder = new TextViewHolder("date", mSizeType);
        mConstellationTextHolder = new TextViewHolder("constellation", mSizeType);
        mTodayTextHolder = new TextViewHolder("today", mSizeType);
        mOffWorkTextHolder = new TextViewHolder("off_or_work", mSizeType);
        mLunarTextHolder = new TextViewHolder("lunar", mSizeType);
        mSolarTermTextHolder = new TextViewHolder("solar_term", mSizeType);
        mHolidayContainer = (LinearLayout) findViewById(R.id.day_info_holiday_container_view);
        if (mSizeType == SIZE_TYPE_NORMAL) {
            mDateTextHolder.setVisibility(GONE);
            mConstellationTextHolder.setVisibility(GONE);
        }
    }

    private void setSolarTermText(String text) {
        if (text != null) {
            mSolarTermTextHolder.setVisibility(View.VISIBLE);
            mSolarTermTextHolder.setText(text);
        } else {
            mSolarTermTextHolder.setVisibility(View.GONE);
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

    private final class TextViewHolder {

        private final TextView mTextView;

        public TextViewHolder(String name, int type) {
            String viewIdName = "day_info_" + name + "_text_view";
            int resViewId = getContext().getResources().getIdentifier(viewIdName, "id",
                    getContext().getPackageName());
            mTextView = (TextView) getRootView().findViewById(resViewId);
            String dimensIdName = "day_info_view_" + name;
            if (type == SIZE_TYPE_LARGE) {
                dimensIdName += "_text_large_size";
            } else {
                dimensIdName += "_text_normal_size";
            }
            int textSizeId = getContext().getResources().getIdentifier(dimensIdName, "dimen",
                    getContext().getPackageName());
            if (textSizeId != 0) {
                float textSize = getContext().getResources().getDimension(textSizeId);
                mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
            Log.d(LOG_TAG, "text dimens name " + dimensIdName + " with textSizeId " + textSizeId);
        }

        public void setText(String text) {
            mTextView.setText(text);
        }

        public void setText(int id) {
            mTextView.setText(id);
        }

        public void setVisibility(int visibility) {
            mTextView.setVisibility(visibility);
        }

        public String getText() {
            return mTextView.getText().toString();
        }

        public Drawable getDrawableLeft() {
            return mTextView.getCompoundDrawables()[0];
        }
    }
}

/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.resource;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.flyingosred.app.android.perpetualcalendar.R;
import com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarResourceBase;

import java.util.Calendar;

public final class Resource extends PerpetualCalendarResourceBase {

    public Resource(Context context) {
        super(context);
    }

    @Override
    public String getOffWorkString(Context context, int offWork) {
        if (offWork == 1) {
            return context.getString(R.string.holiday_type_off);
        } else if (offWork == 0) {
            return context.getString(R.string.holiday_type_work);
        }
        return null;
    }

    @Override
    public Drawable getRegionFlag(String region) {
        String flagName = PREFIX_FLAG + region;
        int id = getContext().getResources().getIdentifier(flagName, "drawable",
                getContext().getPackageName());
        if (id != 0) {
            return ContextCompat.getDrawable(getContext(), id);
        }
        return null;
    }

    public int setTextColor(TextView textView, Cursor cursor, String region, int defaultColor) {
        int offWork = getHolidayOffWork(cursor, region);
        Calendar calendar = getCalendar(cursor);
        int textColor = 0;
        int textColorResId = 0;
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            textColorResId = R.attr.colorSaturdayText;
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            textColorResId = R.attr.colorSundayText;
        }
        if (offWork == 1) {
            textColorResId = R.attr.colorHolidayText;
        } else if (offWork == 0) {
            textColorResId = 0;
        }
        if (textColorResId != 0) {
            TypedValue typedValue = new TypedValue();
            TypedArray a = getContext().obtainStyledAttributes(
                    typedValue.data, new int[]{textColorResId});
            textColor = a.getColor(0, 0);
            a.recycle();
        }

        if (textColor != 0) {
            textView.setTextColor(textColor);
        } else {
            textView.setTextColor(defaultColor);
        }

        return textColor;
    }

    public void setText(TextView textView, String text) {
        if (text != null) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    protected String[] getChineseNumber(Context context) {
        return context.getResources().getStringArray(R.array.chinese_number);
    }

    @Override
    protected String getLunarLeapMonthPrefix(Context context) {
        return context.getString(R.string.lunar_leap);
    }

    @Override
    protected String getLunarFirstMonthPrefix(Context context) {
        return context.getString(R.string.chinese_first_month_prefix);
    }

    @Override
    protected String getLunarLastMonthPrefix(Context context) {
        return context.getString(R.string.chinese_last_month_prefix);
    }

    @Override
    protected String getLunarMonthSuffix(Context context) {
        return context.getString(R.string.chinese_month_name);
    }

    @Override
    protected String getLunarLargeSmallSuffix(Context context, boolean isLarge) {
        if (isLarge) {
            return context.getString(R.string.lunar_large_month);
        }
        return context.getString(R.string.lunar_small_month);
    }

    @Override
    protected String getLunarDayPrefix(Context context) {
        return context.getString(R.string.chinese_day_prefix_name);
    }

    @Override
    protected String[] getLunarEraYearStemArray(Context context) {
        return context.getResources().getStringArray(R.array.era_stem);
    }

    @Override
    protected String[] getLunarEraYearBranchArray(Context context) {
        return context.getResources().getStringArray(R.array.era_branch);
    }

    @Override
    protected String getLunarEraYear(Context context) {
        return context.getString(R.string.lunar_era_year);
    }

    @Override
    protected String[] getLunarAnimalSignArray(Context context) {
        return context.getResources().getStringArray(R.array.animal_sign);
    }

    @Override
    protected String[] getSolarTermNameArray(Context context) {
        return context.getResources().getStringArray(R.array.solar_term_name);
    }

    @Override
    protected String[] getConstellationNameArray(Context context) {
        return context.getResources().getStringArray(R.array.constellation_name);
    }
}

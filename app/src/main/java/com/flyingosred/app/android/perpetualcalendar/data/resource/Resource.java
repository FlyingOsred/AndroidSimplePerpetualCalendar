/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar.data.resource;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.flyingosred.app.android.perpetualcalendar.R;
import com.flyingosred.app.android.perpetualcalendar.api.provider.PerpetualCalendarResourceBase;

public final class Resource extends PerpetualCalendarResourceBase {

    public Resource(Context context) {
        super(context);
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

    @Override
    public String getOffWorkString(Context context, int offWork) {
        if (offWork == 1) {
            return context.getString(R.string.holiday_type_off);
        } else if (offWork == 0) {
            return context.getString(R.string.holiday_type_work);
        }
        return null;
    }
}

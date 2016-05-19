package com.flyingosred.app.android.simpleperpetualcalendar;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import com.flyingosred.app.android.simpleperpetualcalendar.data.Constellation;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar.INVALID_ID;
import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.EMPTY_STRING;
import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.WHITESPACE;

public final class DisplayCalendar implements Parcelable {

    private static final String PREFIX_FLAG = "ic_flag_";

    private static final String PREFIX_HOLIDAY_NAME_ARRAY = "holiday_";

    private final String mDay;

    private final String mDate;

    private final String mDaysOffset;

    private final String mLunarShort;

    private final String mLunarLong;

    private final String mConstellation;

    private final String mSolarTerm;

    private final int mRegionFlag;

    private final String mOffWork;

    private final String mOffWorkLong;

    private final String[] mHolidays;

    public DisplayCalendar(Context context, PerpetualCalendar perpetualCalendar, int offset,
                           String region) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(perpetualCalendar.getSolar().getYear(),
                perpetualCalendar.getSolar().getMonth() - 1,
                perpetualCalendar.getSolar().getDay());
        mDay = String.valueOf(calendar.get(Calendar.DATE));
        mDate = DateUtils.formatDateRange(context, calendar.getTimeInMillis(),
                calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY
                        | DateUtils.FORMAT_SHOW_YEAR).toUpperCase(Locale.getDefault());
        if (offset == 0) {
            mDaysOffset = context.getString(R.string.day_card_today_text);
        } else if (offset > 0) {
            mDaysOffset = String.format(context.getString(R.string.day_card_days_after_today_text),
                    offset);
        } else {
            mDaysOffset = String.format(context.getString(R.string.day_card_days_before_today_text),
                    -offset);
        }

        mLunarShort = formatLunarShort(context, perpetualCalendar.getLunar());

        mLunarLong = formatLunarLong(context, perpetualCalendar.getLunar());

        int constellationId = perpetualCalendar.getConstellation().getId();
        if (constellationId != Constellation.INVALID_ID) {
            mConstellation = context.getResources()
                    .getStringArray(R.array.constellation_name)[constellationId];
        } else {
            mConstellation = null;
        }

        int solarTermId = perpetualCalendar.getSolarTermId();
        if (solarTermId != INVALID_ID) {
            mSolarTerm = context.getResources().getStringArray(R.array.solar_term_name)[solarTermId];
        } else {
            mSolarTerm = null;
        }

        String flagName = PREFIX_FLAG + region;
        mRegionFlag = context.getResources().getIdentifier(flagName, "mipmap", context.getPackageName());

        List<Holiday> holidayList = perpetualCalendar.getHolidayList();
        List<Integer> holidayIdList = new ArrayList<>();
        int offOrWorkResId = 0;
        int offOrWorkLongResId = 0;
        if (region != null && holidayList != null) {
            for (Holiday holiday : holidayList) {
                if (!region.equals(holiday.getRegion())) {
                    continue;
                }
                int id = holiday.getId();
                if (id != Holiday.INVALID_FIELD) {
                    holidayIdList.add(id);
                }
                int offOrWork = holiday.getOffOrWork();
                if (offOrWork == Holiday.TYPE_WORK) {
                    offOrWorkResId = R.string.holiday_type_work;
                    offOrWorkLongResId = R.string.holiday_type_work_long;
                } else if (offOrWork == Holiday.TYPE_OFF) {
                    offOrWorkResId = R.string.holiday_type_off;
                    offOrWorkLongResId = R.string.holiday_type_off_long;
                }
            }
        }
        if (offOrWorkResId != 0) {
            mOffWork = context.getString(offOrWorkResId);
        } else {
            mOffWork = null;
        }
        if (offOrWorkLongResId != 0) {
            mOffWorkLong = context.getString(offOrWorkLongResId);
        } else {
            mOffWorkLong = null;
        }

        if (holidayIdList.size() > 0) {
            String arrayName = PREFIX_HOLIDAY_NAME_ARRAY + region;
            int resId = context.getResources().getIdentifier(arrayName, "array",
                    context.getPackageName());
            if (resId > 0) {
                String[] nameArrays = context.getResources().getStringArray(resId);
                mHolidays = new String[holidayIdList.size()];
                for (int i = 0; i < holidayIdList.size(); i++) {
                    int holidayId = holidayIdList.get(i);
                    mHolidays[i] = nameArrays[holidayId];
                }
            } else {
                mHolidays = null;
            }
        } else {
            mHolidays = null;
        }
    }

    protected DisplayCalendar(Parcel in) {
        mDay = in.readString();
        mDate = in.readString();
        mDaysOffset = in.readString();
        mLunarShort = in.readString();
        mLunarLong = in.readString();
        mConstellation = in.readString();
        mSolarTerm = in.readString();
        mRegionFlag = in.readInt();
        mOffWork = in.readString();
        mOffWorkLong = in.readString();
        mHolidays = in.createStringArray();
    }

    public static final Creator<DisplayCalendar> CREATOR = new Creator<DisplayCalendar>() {
        @Override
        public DisplayCalendar createFromParcel(Parcel in) {
            return new DisplayCalendar(in);
        }

        @Override
        public DisplayCalendar[] newArray(int size) {
            return new DisplayCalendar[size];
        }
    };

    public String getDay() {
        return mDay;
    }

    public String getDate() {
        return mDate;
    }

    public String getDaysOffset() {
        return mDaysOffset;
    }

    public String getLunarShort() {
        return mLunarShort;
    }

    public String getLunarLong() {
        return mLunarLong;
    }

    public String getConstellation() {
        return mConstellation;
    }

    public String getSolarTerm() {
        return mSolarTerm;
    }

    public int getRegionFlag() {
        return mRegionFlag;
    }

    public String getOffWork() {
        return mOffWork;
    }

    public String getOffWorkLong() {
        return mOffWorkLong;
    }

    public String[] getHolidays() {
        return mHolidays;
    }

    private String formatLunarLong(Context context, Lunar lunar) {
        StringBuilder sb = new StringBuilder();
        if (isChineseLocale()) {
            String eraYear = formatChineseEraYear(context, lunar.getYear());
            String month = formatLunarChineseMonth(context, lunar, true);
            String day = formatDayString(context, lunar);
            sb.append(eraYear);
            sb.append(WHITESPACE);
            sb.append(month);
            sb.append(WHITESPACE);
            sb.append(day);
        } else {
            sb.append(lunar.getYear() + "/" + lunar.getMonth() + "/" + lunar.getDay());
        }
        return sb.toString();
    }

    private String formatLunarShort(Context context, Lunar lunar) {
        if (isChineseLocale()) {
            return formatLunarChineseShort(context, lunar);
        }
        return lunar.getMonth() + "/" + lunar.getDay();
    }

    private String formatLunarChineseShort(Context context, Lunar lunar) {
        if (lunar.getDay() == 1) {
            return formatLunarChineseMonth(context, lunar);
        }
        return formatLunarChineseDay(context, lunar);
    }

    private String formatLunarChineseDay(Context context, Lunar lunar) {
        StringBuilder sb = new StringBuilder();
        if (lunar.getDay() <= 10) {
            sb.append(context.getString(R.string.chinese_day_prefix_name));
        }
        sb.append(formatChineseNumber(context, lunar.getDay()));
        return sb.toString();
    }

    private String formatLunarChineseMonth(Context context, Lunar lunar) {
        return formatLunarChineseMonth(context, lunar, false);
    }

    private String formatLunarChineseMonth(Context context, Lunar lunar, boolean largeOrSmall) {
        StringBuilder sb = new StringBuilder();
        if (lunar.isLeapMonth()) {
            sb.append(context.getString(R.string.lunar_leap));
        }
        String prefix;
        int month = lunar.getMonth();
        if (month == 1) {
            prefix = context.getString(R.string.chinese_first_month_prefix);
        } else if (month == 12) {
            prefix = context.getString(R.string.chinese_last_month_prefix);
        } else {
            prefix = formatChineseNumber(context, month);
        }
        sb.append(prefix);
        sb.append(context.getString(R.string.chinese_month_name));
        if (largeOrSmall) {
            if (lunar.getDaysInMonth() == Lunar.DAYS_IN_LARGE_MONTH) {
                sb.append(context.getString(R.string.lunar_large_month));
            } else {
                sb.append(context.getString(R.string.lunar_small_month));
            }

        }
        return sb.toString();
    }

    private String formatChineseNumber(Context context, int number) {
        String[] array = context.getResources().getStringArray(R.array.chinese_number);
        if (number <= array.length) {
            return array[number - 1];
        }

        StringBuilder chineseNumber = new StringBuilder();
        int tensPlace = number / 10;
        if (tensPlace > 1) {
            chineseNumber.append(array[tensPlace - 1]);
        }
        chineseNumber.append(array[array.length - 1]);
        int unitPlace = number % 10;
        if (unitPlace != 0) {
            chineseNumber.append(array[unitPlace - 1]);
        }
        return chineseNumber.toString();
    }

    private boolean isChineseLocale() {
        Locale locale = Locale.getDefault();
        if (locale.equals(Locale.CHINESE) || locale.equals(Locale.CHINA)
                || locale.equals(Locale.SIMPLIFIED_CHINESE)
                || locale.equals(Locale.TRADITIONAL_CHINESE)
                || locale.equals(locale.TAIWAN)) {
            return true;
        }
        return false;
    }

    private String formatDayString(Context context, Lunar lunar) {
        if (isChineseLocale()) {
            StringBuilder sb = new StringBuilder();
            if (lunar.getDay() <= 10) {
                sb.append(context.getString(R.string.chinese_day_prefix_name));
            }
            sb.append(formatChineseNumber(context, lunar.getDay()));
            return sb.toString();
        }
        return EMPTY_STRING;
    }

    private static String formatChineseEraYear(Context context, int year) {
        int i = (year - Lunar.ERA_YEAR_START) % 60;
        StringBuilder sb = new StringBuilder();
        sb.append(context.getResources().getStringArray(R.array.era_stem)[i % 10]);
        sb.append(context.getResources().getStringArray(R.array.era_branch)[i % 12]);
        sb.append(context.getString(R.string.lunar_era_year));
        sb.append("【");
        sb.append(context.getResources().getStringArray(R.array.animal_sign)[(year - Lunar.ERA_ANIMAL_YEAR_START) % 12]);
        sb.append(context.getString(R.string.lunar_era_year));
        sb.append("】");
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDay);
        dest.writeString(mDate);
        dest.writeString(mDaysOffset);
        dest.writeString(mLunarShort);
        dest.writeString(mLunarLong);
        dest.writeString(mConstellation);
        dest.writeString(mSolarTerm);
        dest.writeInt(mRegionFlag);
        dest.writeString(mOffWork);
        dest.writeString(mOffWorkLong);
        dest.writeStringArray(mHolidays);
    }
}

package com.flyingosred.app.android.perpetualcalendar.data.provider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.flyingosred.app.android.perpetualcalendar.util.Utils;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class HolidayProvider extends BaseProvider {

    private static final String LOG_TAG = HolidayProvider.class.getSimpleName();

    private static final String PREFIX_DATE = "holiday_";

    private static final String PREFIX_FLAG = "ic_flag_";

    private static final String PREFIX_HOLIDAY_NAME_ARRAY = "holiday_";

    public HolidayProvider(Context context) {
        super(context);
    }

    public byte[] getFlagImage(String region) {
        String flagName = PREFIX_FLAG + region;
        int id = getContext().getResources().getIdentifier(flagName, "drawable",
                getContext().getPackageName());

        if (id != 0) {
            Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(), id);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }

        return null;
    }

    public List<String> getHolidayNames(String region, Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int yearDateArrayId = getContext().getResources().getIdentifier(
                PREFIX_DATE + region + "_" + year, "array", getContext().getPackageName());
        List<String> nameList = new ArrayList<>();
        String[] regionHolidays = getRegionHolidayNames(region);
        if (yearDateArrayId > 0 && regionHolidays != null && regionHolidays.length != 0) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String[] yearDateArray = getContext().getResources().getStringArray(yearDateArrayId);
            for (int i = 0; i < yearDateArray.length; i++) {
                try {
                    Date date = formatter.parse(yearDateArray[i]);
                    Calendar tempCalendar = Calendar.getInstance();
                    tempCalendar.setTime(date);
                    if (Utils.isDayAfter(tempCalendar, calendar)) {
                        break;
                    }
                    if (Utils.isSameDay(calendar, tempCalendar)) {
                        nameList.add(regionHolidays[i]);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return nameList;
    }

    private String[] getRegionHolidayNames(String region) {
        String[] nameArrays = null;
        String arrayName = PREFIX_HOLIDAY_NAME_ARRAY + region;
        int nameArrayId = getContext().getResources().getIdentifier(arrayName, "array",
                getContext().getPackageName());
        if (nameArrayId > 0) {
            nameArrays = getContext().getResources().getStringArray(nameArrayId);
        }
        return nameArrays;
    }
}

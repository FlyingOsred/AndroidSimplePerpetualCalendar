package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.flyingosred.app.android.simpleperpetualcalendar.R;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Holiday;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Solar;
import com.flyingosred.app.android.simpleperpetualcalendar.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

public class HolidayDatabase {

    private static final String PREFIX = "holiday_";

    private static final String POSTFIX_START_YEAR = "_start_year";

    private static final String POSTFIX_END_YEAR = "_end_year";

    private final HashMap<Integer, List<Holiday>> mDatabase = new HashMap<>();

    public void init(Context context) {
        Resources res = context.getResources();
        String[] regionNames = res.getStringArray(R.array.holiday_region_name);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        for (String region : regionNames) {
            int startYear = res.getInteger(res.getIdentifier(PREFIX + region + POSTFIX_START_YEAR,
                    "integer", context.getPackageName()));
            int endYear = res.getInteger(res.getIdentifier(PREFIX + region + POSTFIX_END_YEAR,
                    "integer", context.getPackageName()));
            for (int year = startYear; year <= endYear; year++) {
                int yearDateArrayId = res.getIdentifier(PREFIX + region + "_" + year, "array",
                        context.getPackageName());
                if (yearDateArrayId <= 0) {
                    Log.w(LOG_TAG, "No date for region" + region + " year " + year);
                } else {
                    String[] yearDateArray = res.getStringArray(yearDateArrayId);
                    for (int i = 0; i < yearDateArray.length; i++) {
                        try {
                            Date date = formatter.parse(yearDateArray[i]);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            Integer hash = Utils.dateHash(calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
                            Log.d(LOG_TAG, "Found holiday id " + i + " of " + region + " for "
                                    + " year " + calendar.get(Calendar.YEAR)
                                    + " month " + calendar.get(Calendar.MONTH)
                                    + " day " + calendar.get(Calendar.DATE));
                            HolidayDatabaseItem item = new HolidayDatabaseItem();
                            item.setRegion(region);
                            item.setId(i);
                            List<Holiday> list;
                            if (mDatabase.containsKey(hash)) {
                                list = mDatabase.get(hash);
                                list.add(item);
                            } else {
                                list = new ArrayList<>();
                                list.add(item);
                                mDatabase.put(hash, list);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                int yearOffOrWorkArrayId = res.getIdentifier(PREFIX + region + "_off_work_" + year,
                        "array", context.getPackageName());
                if (yearOffOrWorkArrayId <= 0) {
                    Log.w(LOG_TAG, "No off work for region" + region + " year " + year);
                } else {
                    String[] yearOffOrWorkArray = res.getStringArray(yearOffOrWorkArrayId);
                    for (int i = 0; i < yearOffOrWorkArray.length; i++) {
                        String[] strings = yearOffOrWorkArray[i].split(":");
                        if (strings.length != 2) {
                            Log.w(LOG_TAG, "Invalid off work format for " + yearOffOrWorkArray[i]);
                            continue;
                        }
                        try {
                            Date date = formatter.parse(strings[0]);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            Integer hash = Utils.dateHash(calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
                            String offOrWorkString = strings[1];
                            int offOrWork = Holiday.INVALID_FIELD;
                            if (offOrWorkString.equals("work")) {
                                offOrWork = Holiday.TYPE_WORK;
                            } else if (offOrWorkString.equals("off")) {
                                offOrWork = Holiday.TYPE_OFF;
                            } else {
                                Log.w(LOG_TAG, "Unknown type format " + offOrWorkString);
                            }
                            Log.d(LOG_TAG, "Found off work " + offOrWorkString + " type "
                                    + offOrWork + " of " + region
                                    + " for " + " year " + calendar.get(Calendar.YEAR)
                                    + " month " + calendar.get(Calendar.MONTH)
                                    + " day " + calendar.get(Calendar.DATE));
                            HolidayDatabaseItem item = new HolidayDatabaseItem();
                            item.setRegion(region);
                            item.setOffOrWork(offOrWork);
                            List<Holiday> list;
                            if (mDatabase.containsKey(hash)) {
                                list = mDatabase.get(hash);
                                list.add(item);
                            } else {
                                list = new ArrayList<>();
                                list.add(item);
                                mDatabase.put(hash, list);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public List<Holiday> get(Solar solar) {
        Integer hash = Utils.dateHash(solar.getYear(), solar.getMonth(), solar.getDay());
        if (mDatabase.containsKey(hash)) {
            Log.d(LOG_TAG, "Found holiday for " + " year " + solar.getYear()
                    + " month " + solar.getMonth()
                    + " day " + solar.getDay());
            HolidayDatabaseItem item = new HolidayDatabaseItem();
        }
        return mDatabase.get(hash);
    }
}

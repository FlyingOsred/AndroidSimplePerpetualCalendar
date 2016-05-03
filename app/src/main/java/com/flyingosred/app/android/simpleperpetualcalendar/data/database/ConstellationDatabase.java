package com.flyingosred.app.android.simpleperpetualcalendar.data.database;

public final class ConstellationDatabase {

    private static final int[][] CONSTELLATION_DATE = {
            {1, 20, 2, 18},
            {2, 19, 3, 20},
            {3, 21, 4, 19},
            {4, 20, 5, 20},
            {5, 21, 6, 21},
            {6, 22, 7, 22},
            {7, 23, 8, 22},
            {8, 23, 9, 22},
            {9, 23, 10, 22},
            {10, 23, 11, 21},
            {11, 22, 12, 21},
            {12, 22, 1, 19}
    };

    public static int get(int month, int day) {
        for (int i = 0; i < CONSTELLATION_DATE.length; i++) {
            int[] date = CONSTELLATION_DATE[i];
            if ((month == date[0] && day >= date[1]) || (month == date[2] && day <= date[3])) {
                return i;
            }
        }
        return -1;
    }
}

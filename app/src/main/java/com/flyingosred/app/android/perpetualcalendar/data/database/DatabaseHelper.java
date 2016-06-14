package com.flyingosred.app.android.perpetualcalendar.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Copyright (C) 2016 Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */
public final class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    public static final String DATABASE_NAME = "database.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

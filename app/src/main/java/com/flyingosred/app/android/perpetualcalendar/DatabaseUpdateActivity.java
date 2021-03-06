/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.perpetualcalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.flyingosred.app.android.perpetualcalendar.data.database.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DatabaseUpdateActivity extends AppCompatActivity {

    private static final String LOG_TAG = DatabaseUpdateActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_update);
        updateDatabase();
    }

    private void updateDatabase() {
        String databaseVersion = getString(R.string.database_version_name);
        Log.i(LOG_TAG, "New database version " + databaseVersion);
        String versionPrefKey = getString(R.string.pref_key_database_version);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String prefDatabaseVersion = prefs.getString(versionPrefKey, null);
        Log.d(LOG_TAG, "Old database version " + prefDatabaseVersion);
        if (compareVersion(prefDatabaseVersion, databaseVersion)) {
            new DatabaseUpdateTask(this).execute();
            prefs.edit().putString(versionPrefKey, databaseVersion).apply();
        } else {
            Log.i(LOG_TAG, "Database is up to date with version " + prefDatabaseVersion);
            databaseUpdated();
        }
    }

    private boolean compareVersion(String oldVersion, String newVersion) {
        if (oldVersion != null && newVersion != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            try {
                Date oldDate = formatter.parse(oldVersion);
                Date newDate = formatter.parse(newVersion);
                Calendar oldCalendar = Calendar.getInstance();
                oldCalendar.setTime(oldDate);
                Calendar newCalendar = Calendar.getInstance();
                newCalendar.setTime(newDate);
                if (newCalendar.compareTo(oldCalendar) <= 0) {
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void databaseUpdated() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private final class DatabaseUpdateTask extends AsyncTask<Void, Void, Void> {

        private static final String ZIP_FILE_NAME = "database.zip";

        private final Context mContext;

        public DatabaseUpdateTask(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            long startTime = System.currentTimeMillis();
            File dbFile = mContext.getDatabasePath(DatabaseHelper.DATABASE_NAME);
            String dbPath = dbFile.getParent();
            Log.d(LOG_TAG, "DatabaseUpdateTask dbFile is " + dbFile.getAbsolutePath() + " dbPath is " + dbPath);
            File dbPathFile = new File(dbPath);
            if (!dbPathFile.exists()) {
                if (dbPathFile.mkdirs()) {
                    Log.d(LOG_TAG, dbPathFile + " is created.");
                }
            }
            long writtenBytes = 0;
            InputStream in = null;
            try {
                in = mContext.getAssets().open(ZIP_FILE_NAME);
                ZipInputStream zin = new ZipInputStream(in);
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null
                        && !ze.isDirectory()
                        && ze.getName().equals(DatabaseHelper.DATABASE_NAME)) {
                    Log.d(LOG_TAG, "DatabaseUpdateTask unzipping " + ze.getName());
                    if (!dbFile.exists()) {
                        if (dbFile.createNewFile()) {
                            Log.d(LOG_TAG, dbFile + " is created.");
                        }
                    }
                    FileOutputStream fileOutput = new FileOutputStream(dbFile);
                    byte[] buffer = new byte[4096];
                    int length = zin.read(buffer);
                    while (length > 0) {
                        fileOutput.write(buffer, 0, length);
                        writtenBytes += length;
                        length = zin.read(buffer);
                    }
                    zin.closeEntry();
                    fileOutput.close();
                }
                zin.close();
                Log.d(LOG_TAG, "DatabaseUpdateTask unzip done");
            } catch (IOException e) {
                e.printStackTrace();
            }
            long endTime = System.currentTimeMillis();
            Log.i(LOG_TAG, "DatabaseUpdateTask unzip " + writtenBytes + " bytes cost "
                    + (endTime - startTime) + " millis");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(LOG_TAG, "DatabaseUpdateTask database update done ");
            databaseUpdated();
        }
    }

}

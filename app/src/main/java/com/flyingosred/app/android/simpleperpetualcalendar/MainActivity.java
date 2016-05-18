package com.flyingosred.app.android.simpleperpetualcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnDaySelectedListener {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.day_card_toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.day_card_container) != null) {
            mTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDaySelected(PerpetualCalendar perpetualCalendar) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("date", perpetualCalendar);
            DayCardFragment fragment = new DayCardFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.day_card_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DayCardActivity.class);
            intent.putExtra("date", perpetualCalendar);
            startActivity(intent);
        }
    }
}

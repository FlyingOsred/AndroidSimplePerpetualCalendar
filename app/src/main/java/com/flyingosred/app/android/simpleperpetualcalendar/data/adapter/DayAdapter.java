package com.flyingosred.app.android.simpleperpetualcalendar.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyingosred.app.android.simpleperpetualcalendar.R;
import com.flyingosred.app.android.simpleperpetualcalendar.data.Lunar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.PerpetualCalendar;
import com.flyingosred.app.android.simpleperpetualcalendar.data.loader.Content;

import java.util.Calendar;

public class DayAdapter extends RecyclerView.Adapter {

    private Content mContent = null;

    private int mFirstDayOfWeek;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_recycler_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, parent.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (mContent != null) {
            PerpetualCalendar calendar = mContent.get(position);
            if (calendar != null) {
                int day = calendar.get().get(Calendar.DATE);
                int month = calendar.get().get(Calendar.MONTH);
                int year = calendar.get().get(Calendar.YEAR);
                viewHolder.mDateTextView.setText(String.valueOf(day));
                int constellationId = calendar.getConstellationId();
                String constellationText = viewHolder.getContext().getResources().getStringArray(R.array.constellation_name)[constellationId];
                String constellationSymbol = viewHolder.getContext().getResources().getStringArray(R.array.constellation_symbol)[constellationId];
                viewHolder.mConstellationTextView.setText(constellationSymbol + constellationText);
                String lunarText = Lunar.formatMonthDayString(viewHolder.getContext(), calendar.getLunar().getMonth(), calendar.getLunar().getDay());
                viewHolder.mLunarTextView.setText(lunarText);
                int solarTermId = calendar.getSolarTermId();
                if (solarTermId != PerpetualCalendar.INVALID_ID) {
                    viewHolder.mSolarTermTextView.setText(viewHolder.getContext().getResources().getStringArray(R.array.solar_term)[solarTermId]);
                } else {
                    viewHolder.mSolarTermTextView.setText("");
                }
                if (isToday(year, month, day)) {
                    viewHolder.mTodayTextView.setText(R.string.month_day_today_text);
                } else {
                    viewHolder.mTodayTextView.setText("");
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        if (mContent != null) {
            return mContent.getCount();
        }
        return 0;
    }

    public void changeContent(Content content) {
        mContent = content;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final Context mContext;
        private View mItemView;
        public final TextView mConstellationTextView;
        public final TextView mDateTextView;
        public final TextView mLunarTextView;
        public final TextView mSolarTermTextView;
        public final TextView mTodayTextView;
        public final TextView mOffOrWorkTextView;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            mItemView = itemView;
            mDateTextView = (TextView) itemView.findViewById(R.id.day_text_view);
            mConstellationTextView = (TextView) itemView.findViewById(R.id.constellation_text_view);
            mLunarTextView = (TextView) itemView.findViewById(R.id.lunar_text_view);
            mSolarTermTextView = (TextView) itemView.findViewById(R.id.solar_term_text_view);
            mTodayTextView = (TextView) itemView.findViewById(R.id.today_text_view);
            mOffOrWorkTextView = (TextView) itemView.findViewById(R.id.off_or_work_text_view);
        }

        public Context getContext() {
            return mContext;
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
}

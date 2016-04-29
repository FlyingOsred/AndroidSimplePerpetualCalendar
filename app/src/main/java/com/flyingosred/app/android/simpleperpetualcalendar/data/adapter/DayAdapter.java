package com.flyingosred.app.android.simpleperpetualcalendar.data.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyingosred.app.android.simpleperpetualcalendar.R;

public class DayAdapter extends RecyclerView.Adapter {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_recycler_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.mDateTextView.setText("1");
    }

    @Override
    public int getItemCount() {
        return 10000;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mItemView;
        public TextView mDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mDateTextView = (TextView) itemView.findViewById(R.id.day_text_view);
        }
    }
}

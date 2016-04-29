package com.flyingosred.app.android.simpleperpetualcalendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyingosred.app.android.simpleperpetualcalendar.data.adapter.DayAdapter;
import com.flyingosred.app.android.simpleperpetualcalendar.data.loader.Content;
import com.flyingosred.app.android.simpleperpetualcalendar.data.loader.ContentLoader;

import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Content> {

    private static final int CONTENT_LOADER_ID = 1;

    private RecyclerView mDayView = null;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mDayView = (RecyclerView) view.findViewById(R.id.day_recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        mDayView.setLayoutManager(layoutManager);
        DayAdapter adapter = new DayAdapter();
        mDayView.setAdapter(adapter);
        getActivity().getSupportLoaderManager().initLoader(CONTENT_LOADER_ID, null, this);
    }

    @Override
    public Loader<Content> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        return new ContentLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Content> loader, Content data) {
        Log.d(LOG_TAG, "onLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<Content> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
    }
}

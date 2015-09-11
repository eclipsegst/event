package com.example.android.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivityFragment extends Fragment implements FloatingActionButton.OnCheckedChangeListener {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private FloatingActionButton mFabNew;
    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mFabNew = (FloatingActionButton)rootView.findViewById(R.id.fab_new);
        mFabNew.setOnCheckedChangeListener(this);

        return rootView;
    }

    @Override
    public void onCheckedChanged(FloatingActionButton fabView, boolean isChecked) {
        // When a FAB is toggled, log the action.
        switch (fabView.getId()) {
            case R.id.fab_new:
                Log.d(LOG_TAG, String.format("FAB new was %s.", isChecked ? "checked" : "unchecked"));
                Intent intent = new Intent(getActivity(), EventActivity.class);
                startActivity(intent);
                break;

            default:
                break;

        }
    }
}
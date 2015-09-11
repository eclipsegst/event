package com.example.android.event;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class NotaActivityFragment extends Fragment {

    private static final String LOG_TAG = NotaActivityFragment.class.getSimpleName();
    private long notaRowId;

    protected TextView mNotaRowIdTextView;

    public NotaActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_nota, container, false);

        notaRowId = getActivity().getIntent().getExtras().getLong("notaRowId");

        mNotaRowIdTextView = (TextView) rootView.findViewById(R.id.notaRowId);

        mNotaRowIdTextView.setText("" + notaRowId);


        return rootView;
    }
}

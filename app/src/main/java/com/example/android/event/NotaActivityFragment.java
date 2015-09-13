package com.example.android.event;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.event.data.NotaContract.CategoryEntry;
import com.example.android.event.data.NotaContract.NotaEntry;

/**
 * A placeholder fragment containing a simple view.
 */
public class NotaActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = NotaActivityFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";
    static final String DETAIL_TRANSITION_ANIMATION = "DTA";
    private static final int DETAIL_LOADER = 0;

    private Uri mUri;
    private boolean mTransitionAnimation;
    private long notaRowId;

    // specify the columns we need
    // change the indices after those columns if we change the columns
    private static final String[] NOTA_COLUMNS = {
            NotaEntry.TABLE_NAME + "." + NotaEntry._ID,
            NotaEntry.COLUMN_SUBJECT,
            NotaEntry.COLUMN_START,
            NotaEntry.COLUMN_END,
            NotaEntry.COLUMN_DURATION,
            CategoryEntry.COLUMN_NAME,
            NotaEntry.COLUMN_NOTE,
            NotaEntry.COLUMN_LAT,
            NotaEntry.COLUMN_LON
    };

    // These indices are tied to NOTA_COLUMNS.
    // If NOTA_COLUMNS changes, these must change.
    public static final int COL_NOTA_ID = 0;
    public static final int COL_SUBJECT = 1;
    public static final int COL_START = 2;
    public static final int COL_END = 3;
    public static final int COL_DURATION = 4;
    public static final int COL_CATEGORY_NAME = 5;
    public static final int COL_NOTE = 6;
    public static final int COL_LAT = 7;
    public static final int COL_LON = 8;

    private TextView mSubjectView;
    private TextView mNoteView;
    private TextView mStartView;
    private TextView mEndView;
    private TextView mDurationView;
    private TextView mCategoryView;
    private TextView mLatitudeView;
    private TextView mLongitudeView;

    public NotaActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
//        if (arguments != null) {
//            mUri = arguments.getParcelable(NotaActivityFragment.DETAIL_URI);
//            mTransitionAnimation = arguments.getBoolean(NotaActivityFragment.DETAIL_TRANSITION_ANIMATION, false);
//        }

        View rootView = inflater.inflate(R.layout.fragment_nota, container, false);

        notaRowId = getActivity().getIntent().getExtras().getLong("notaRowId");

        mSubjectView = (TextView) rootView.findViewById(R.id.nota_subject);
        mSubjectView.setText("subject for nota id: " + notaRowId);
        mNoteView = (TextView) rootView.findViewById(R.id.nota_note);
        mStartView = (TextView) rootView.findViewById(R.id.nota_start);
        mEndView = (TextView) rootView.findViewById(R.id.nota_end);
        mDurationView = (TextView) rootView.findViewById(R.id.nota_duration);
        mCategoryView = (TextView) rootView.findViewById(R.id.nota_category);
        mLatitudeView = (TextView) rootView.findViewById(R.id.nota_latitude);
        mLongitudeView = (TextView) rootView.findViewById(R.id.nota_longitude);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.d(LOG_TAG, "onCreateLoader");

        mUri = NotaEntry.CONTENT_URI;

        Log.d(LOG_TAG, "mUri: " + mUri);
        if (null != mUri) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    NOTA_COLUMNS,
                    NotaEntry.TABLE_NAME + "." + NotaEntry._ID + " = ?",
                    new String[] {String.valueOf(notaRowId)},
                    null
            );
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        Log.d(LOG_TAG, "onLoadFinshied");
        Log.d(LOG_TAG, "" + cursor.getCount());

        if (cursor != null && cursor.moveToFirst()) {

            String subject = cursor.getString(COL_SUBJECT);
            mSubjectView.setText(subject);

            String note = cursor.getString(COL_NOTE);
            mNoteView.setText(note);

            long startTime = cursor.getLong(COL_START);
            mStartView.setText(utilities.getReadableDate(startTime));

            long endTime = cursor.getLong(COL_END);
            mEndView.setText(utilities.getReadableDate(endTime));

            long duration = cursor.getLong(COL_DURATION);
            mDurationView.setText(utilities.formatDuration(duration));

            String category = cursor.getString(COL_CATEGORY_NAME);
            mCategoryView.setText(category);

            float latitude = cursor.getFloat(COL_LAT);
            mLatitudeView.setText(String.valueOf(latitude));

            float longitude = cursor.getFloat(COL_LON);
            mLongitudeView.setText(String.valueOf(longitude));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

}

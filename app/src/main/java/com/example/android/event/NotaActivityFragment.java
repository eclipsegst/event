package com.example.android.event;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.event.data.NotaContract.CategoryEntry;
import com.example.android.event.data.NotaContract.NotaEntry;

/**
 * A placeholder fragment containing a simple view.
 */
public class NotaActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = NotaActivityFragment.class.getSimpleName();
    private static final int DETAIL_LOADER = 0;

    private Uri mUri;
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

    private TextView mSubjectTextView;
    private TextView mNoteTextView;
    private TextView mStartTextView;
    private TextView mEndTextView;
    private TextView mDurationTextView;
    private TextView mCategoryTextView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private Toolbar mToolbar;

    public NotaActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nota, container, false);

        // retrieve the nota row id
        notaRowId = getActivity().getIntent().getExtras().getLong("notaRowId");

        mSubjectTextView = (TextView) rootView.findViewById(R.id.subject_textview);
        mNoteTextView = (TextView) rootView.findViewById(R.id.note_textview);
        mStartTextView = (TextView) rootView.findViewById(R.id.start_textview);
        mEndTextView = (TextView) rootView.findViewById(R.id.end_textview);
        mDurationTextView = (TextView) rootView.findViewById(R.id.duration_textview);
        mCategoryTextView = (TextView) rootView.findViewById(R.id.category_textview);
        mLatitudeTextView = (TextView) rootView.findViewById(R.id.latitude_textview);
        mLongitudeTextView = (TextView) rootView.findViewById(R.id.longitude_textview);
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        mSubjectTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: popup window to edit
                Toast.makeText(getActivity(), "mSubjectTextView", Toast.LENGTH_LONG).show();
            }
        });

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
            mSubjectTextView.setText(subject);

            String note = cursor.getString(COL_NOTE);
            mNoteTextView.setText(note);

            long startTime = cursor.getLong(COL_START);
            mStartTextView.setText(utilities.getReadableDate(startTime));

            long endTime = cursor.getLong(COL_END);
            mEndTextView.setText(utilities.getReadableDate(endTime));

            long duration = cursor.getLong(COL_DURATION);
            mDurationTextView.setText(utilities.formatDuration(duration));

            String category = cursor.getString(COL_CATEGORY_NAME);
            mCategoryTextView.setText(category);

            float latitude = cursor.getFloat(COL_LAT);
            mLatitudeTextView.setText(String.valueOf(latitude));

            float longitude = cursor.getFloat(COL_LON);
            mLongitudeTextView.setText(String.valueOf(longitude));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

}

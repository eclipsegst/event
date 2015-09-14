package com.example.android.event;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.event.data.NotaContract.CategoryEntry;
import com.example.android.event.data.NotaContract.NotaEntry;

public class MainActivityFragment extends Fragment implements
        FloatingActionButton.OnCheckedChangeListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    public static NotaAdapter mNotaAdapter;
    private ListView mListView;

    private FloatingActionButton mFabNew;
    private static final int NOTA_LOADER = 0;

    // specify the columns we need
    // change the indices after those columns if we change the columns
    private static final String[] NOTA_COLUMNS = {
            NotaEntry.TABLE_NAME + "." + NotaEntry._ID,
            NotaEntry.COLUMN_SUBJECT,
            NotaEntry.COLUMN_START,
            NotaEntry.COLUMN_DURATION,
            CategoryEntry.COLUMN_NAME
    };

    // These indices are tied to NOTA_COLUMNS.
    // If NOTA_COLUMNS changes, these must change.
    public static final int COL_NOTA_ID = 0;
    public static final int COL_SUBJECT = 1;
    public static final int COL_START = 2;
    public static final int COL_DURATION = 3;
    public static final int COL_CATEGORY_NAME = 4;


    /**
     * A callback interface that all activities containing this fragment must implement.
     * This mechanism allows activitys to be notified of item selections.
     */
    public interface Callback {

        // NotaDetailFragmentCallback for when an item has been selected.
        public void onItemSelected(Uri dateUri);
    }

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mNotaAdapter = new NotaAdapter(getActivity(), null, 0);
//        mNotaAdapter = new NotaAdapter(getActivity(), new NotaAdapter.)
        mListView = (ListView) rootView.findViewById(R.id.listview_nota);
        mListView.setAdapter(mNotaAdapter);

        mFabNew = (FloatingActionButton)rootView.findViewById(R.id.fab_new);
        mFabNew.setOnCheckedChangeListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    long notaRowId = cursor.getLong(COL_NOTA_ID);
                    Intent intent = new Intent(getActivity(), NotaActivity.class);
                    intent.putExtra("notaRowId", notaRowId);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(NOTA_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Log.d(LOG_TAG, "onCreateLoader");
        String sortOrder = NotaEntry.COLUMN_START + " DESC";
        // if category is set to null, we just ignore category
        Uri notaUri = NotaEntry.buildNotaCategory(null);

        return new CursorLoader(getActivity(),
                notaUri,
                NOTA_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");
        Log.d(LOG_TAG, "nota row returned: " + data.getCount());
        mNotaAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNotaAdapter.swapCursor(null);
    }
}
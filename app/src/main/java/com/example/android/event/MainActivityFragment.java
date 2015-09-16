package com.example.android.event;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android.event.data.NotaContract.CategoryEntry;
import com.example.android.event.data.NotaContract.NotaEntry;

public class MainActivityFragment extends Fragment implements
        FloatingActionButton.OnCheckedChangeListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    public NotaAdapter mNotaAdapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFabNew;
    private int mChoiceMode;
    private static final String SELECTED_KEY = "selected_position";
    private static final int NOTA_LOADER = 0;

    private int mStatusBarColor;
    private int mPopupHeaderColor;

    // specify the columns we need
    // change the indices after those columns if we change the columns
    private static final String[] NOTA_COLUMNS = {
            NotaEntry.TABLE_NAME + "." + NotaEntry._ID,
            NotaEntry.COLUMN_SUBJECT,
            NotaEntry.COLUMN_NOTE,
            NotaEntry.COLUMN_START,
            NotaEntry.COLUMN_END,
            NotaEntry.COLUMN_DURATION,
            CategoryEntry.COLUMN_NAME
    };

    // These indices are tied to NOTA_COLUMNS.
    // If NOTA_COLUMNS changes, these must change.
    public static final int COL_NOTA_ID = 0;
    public static final int COL_SUBJECT = 1;
    public static final int COL_NOTE = 2;
    public static final int COL_START = 3;
    public static final int COL_END = 4;
    public static final int COL_DURATION = 5;
    public static final int COL_CATEGORY_NAME = 6;


    /**
     * A callback interface that all activities containing this fragment must implement.
     * This mechanism allows activitys to be notified of item selections.
     */
    public interface Callback {

        // NotaDetailFragmentCallback for when an item has been selected.
        public void onItemSelected(Uri dateUri, NotaAdapter.NotaAdapterViewHolder vh);
    }

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_nota);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        View emptyView = rootView.findViewById(R.id.recyclerview_nota_empty);

        // use this setting to improve performance if we know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mNotaAdapter = new NotaAdapter(getActivity(), new NotaAdapter.NotaAdapterOnClickHandler() {
            @Override
            public void onClick(Long id, NotaAdapter.NotaAdapterViewHolder vh) {
            }
        }, emptyView, mChoiceMode);

        mRecyclerView.setAdapter(mNotaAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Cursor mCursor = mNotaAdapter.getCursor();
                mCursor.moveToPosition(position);
                int idColumnIndex = mCursor.getColumnIndex(NotaEntry._ID);
                long noteId = mCursor.getLong(idColumnIndex);
                Intent intent = new Intent(getActivity(), NotaActivity.class);
                intent.putExtra("notaId", noteId);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));

        // save data when turning the device sideway
        if (savedInstanceState != null) {
            mNotaAdapter.onRestoreInstanceState(savedInstanceState);
        }


        mFabNew = (FloatingActionButton)rootView.findViewById(R.id.fab_new);
        mFabNew.setOnCheckedChangeListener(this);


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
    public void onSaveInstanceState(Bundle outState) {
        // when device rotate, the currently selected list item needs to be saved.
        mNotaAdapter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
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
    public void onDestroy() {
        super.onDestroy();
        if (null != mRecyclerView) {
            mRecyclerView.clearOnScrollListeners();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNotaAdapter.swapCursor(null);
    }
}
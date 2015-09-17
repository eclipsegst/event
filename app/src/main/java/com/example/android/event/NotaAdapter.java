/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.event;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import com.example.android.event.data.NotaContract;

/**
 * {@link NotaAdapter} exposes a list of nota
 * from a {@link android.database.Cursor} to a {@link android.support.v7.widget.RecyclerView}.
 */
public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaAdapterViewHolder>{

    private Cursor mCursor;
    final private Context mContext;
    final private NotaAdapterOnClickHandler mClickHandler;
    final private View mEmptyView;
    final private ItemChoiceManager mICM;
    final private int mPopupHeaderColor;

    /**
     * Cache of the children views for a nota list item.
     */
    public class NotaAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mSubjectView;
        public final TextView mNoteView;
        public final TextView mDurationView;
        public final TextView mStartView;
        public final TextView mEndView;
        public final TextView mCategoryView;
        public final LinearLayout mCardViewHeaderLinearLayout;
        public final TextView mPostTimeView;

        public NotaAdapterViewHolder(View view) {
            super(view);
            mSubjectView = (TextView) view.findViewById(R.id.list_item_subject);
            mNoteView = (TextView) view.findViewById(R.id.list_item_note);
            mDurationView = (TextView) view.findViewById(R.id.list_item_duration);
            mStartView = (TextView) view.findViewById(R.id.list_item_start_date);
            mEndView = (TextView) view.findViewById(R.id.list_item_end);
            mCategoryView = (TextView) view.findViewById(R.id.list_item_category);
            mCardViewHeaderLinearLayout = (LinearLayout) view.findViewById(R.id.cardview_item_header_linear_layout);

            mPostTimeView = (TextView) view.findViewById(R.id.list_item_post_time);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int idColumnIndex = mCursor.getColumnIndex(NotaContract.NotaEntry._ID);
            mClickHandler.onClick(mCursor.getLong(idColumnIndex), this);
            mICM.onClick(this);


            Log.d("NotaAdapter", "zztg2 onClick");

            Intent intent = new Intent(mContext, NotaActivity.class);
            intent.putExtra("notaRowId", mCursor.getLong(idColumnIndex));
            ((FragmentActivity) mContext).startActivity(intent);

            if (mContext instanceof FragmentActivity) {

            }

        }
    }

    public static interface NotaAdapterOnClickHandler {
        void onClick(Long id, NotaAdapterViewHolder vh);
    }

    public NotaAdapter(Context context, NotaAdapterOnClickHandler dh, View emptyView, int choiceMode) {
        mContext = context;
        mClickHandler = dh;
        mEmptyView = emptyView;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);
        mPopupHeaderColor = ((Constants) mContext.getApplicationContext()).getPopupHeaderClor();



    }

    /**
     * This takes advantage of the fact that the viewGroup passed to onCreateViewHolder is the
     * RecyclerView that will be used to contain the view, so that it can get the current
     * ItemSelectionManager from the view.
     * One could implement this pattern without modifying RecyclerView by taking advantage
     * of the view tag to store the ItemChoiceManager.
     * @param viewGroup
     * @param viewType
     * @return
     */
    @Override
    public NotaAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if ( viewGroup instanceof RecyclerView) {
            int layoutId = -1;
            layoutId = R.layout.list_item_nota;
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            view.setFocusable(true);
            return new NotaAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(NotaAdapterViewHolder viewHolder, int position) {
        mCursor.moveToPosition(position);
        int weatherId = mCursor.getInt(NotaActivityFragment.COL_NOTA_ID);
        // read subject from cursor
        String subject = mCursor.getString(MainActivityFragment.COL_SUBJECT);
        viewHolder.mSubjectView.setText(subject);

        // read note
        String note = mCursor.getString(MainActivityFragment.COL_NOTE);
        viewHolder.mNoteView.setText(note);

        // read start from cursor
        long startDateInMillis = mCursor.getLong(MainActivityFragment.COL_START);
        String start = utilities.getReadableDate(startDateInMillis, utilities.dateFormat);
        viewHolder.mStartView.setText(start);

        // read end from cursor
        long endDate = mCursor.getLong(MainActivityFragment.COL_END);
        String end = utilities.getReadableDate(endDate, utilities.dateFormat);
        viewHolder.mEndView.setText(end);

        // read duration from cursor
        long duration = mCursor.getLong(MainActivityFragment.COL_DURATION);
        viewHolder.mDurationView.setText("" + utilities.formatDuration(duration));

        // read category from cursor
        String category = mCursor.getString(MainActivityFragment.COL_CATEGORY_NAME);
        viewHolder.mCategoryView.setText(category);

        viewHolder.mPostTimeView.setText(utilities.getFriendlyDayString(mContext, startDateInMillis));

        viewHolder.mCardViewHeaderLinearLayout.setBackgroundColor(mPopupHeaderColor);

        mICM.onBindViewHolder(viewHolder, position);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mICM.onRestoreInstanceState(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        mICM.onSaveInstanceState(outState);
    }

    public int getSelectedItemPosition() {
        return mICM.getSelectedItemPosition();
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public void selectView(RecyclerView.ViewHolder viewHolder) {
        if ( viewHolder instanceof NotaAdapterViewHolder ) {
            NotaAdapterViewHolder vh = (NotaAdapterViewHolder)viewHolder;
            vh.onClick(vh.itemView);
            Log.d("NotaAdapter", "zztg2 onclick selectView");
        }
    }
}
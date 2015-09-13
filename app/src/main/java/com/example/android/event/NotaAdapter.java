package com.example.android.event;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NotaAdapter extends CursorAdapter {

    /**
     * cache of the children views of a nota list item
     */
    public static class ViewHolder {
        public final TextView mSubjectView;
        public final TextView mDurationView;
        public final TextView mStartTimeView;
        public final TextView mCategoryView;

        public ViewHolder(View view) {
            mSubjectView = (TextView) view.findViewById(R.id.list_item_subject);
            mDurationView = (TextView) view.findViewById(R.id.list_item_duration);
            mStartTimeView = (TextView) view.findViewById(R.id.list_item_start_date);
            mCategoryView = (TextView) view.findViewById(R.id.list_item_category);
        }
    }

    public NotaAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_nota, parent,false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // read subject from cursor
        String subject = cursor.getString(MainActivityFragment.COL_SUBJECT);
        viewHolder.mSubjectView.setText(subject);

        // read start date from cursor
        long startDate = cursor.getLong(MainActivityFragment.COL_START);
        String start = utilities.getReadableDate(startDate, "dd/MM/yy HH:mm");
        viewHolder.mStartTimeView.setText(start);

        // read duration from cursor
        long duration = cursor.getLong(MainActivityFragment.COL_DURATION);
        viewHolder.mDurationView.setText("" + utilities.formatDuration(duration));
        // read category from cursor
        String category = cursor.getString(MainActivityFragment.COL_CATEGORY_NAME);
        viewHolder.mCategoryView.setText(category);
    }
}

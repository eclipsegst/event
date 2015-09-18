package com.example.android.event;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.event.data.NotaContract.CategoryEntry;
import com.example.android.event.data.NotaContract.NotaEntry;
import com.example.android.event.data.NotaDbHelper;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class NotaActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = NotaActivityFragment.class.getSimpleName();
    private static final int DETAIL_LOADER = 0;

    private Uri mUri;
    private long notaId;
    private ContentValues mContentValues;
    private String mColumnName;

    Point p;
    private FrameLayout mFrameLayout;
    private TextView mTextView;

    private Button mDeleteButton;

    // specify the columns we need
    // change the indices after those columns if we change the columns
    private static final String[] NOTA_COLUMNS = {
            NotaEntry.TABLE_NAME + "." + NotaEntry._ID,
            NotaEntry.COLUMN_SUBJECT,
            NotaEntry.COLUMN_START,
            NotaEntry.COLUMN_END,
            NotaEntry.COLUMN_DURATION,
            NotaEntry.COLUMN_NOTE,
            NotaEntry.COLUMN_LAT,
            NotaEntry.COLUMN_LON,
            CategoryEntry.COLUMN_NAME
    };

    // These indices are tied to NOTA_COLUMNS.
    // If NOTA_COLUMNS changes, these must change.
    public static final int COL_NOTA_ID = 0;
    public static final int COL_SUBJECT = 1;
    public static final int COL_START = 2;
    public static final int COL_END = 3;
    public static final int COL_DURATION = 4;
    public static final int COL_NOTE = 5;
    public static final int COL_LAT = 6;
    public static final int COL_LON = 7;
    public static final int COL_CATEGORY_NAME = 8;

    private TextView mSubjectTextView;
    private TextView mNoteTextView;
    private TextView mStartTextView;
    private TextView mEndTextView;
    private TextView mDurationTextView;
    private TextView mCategoryTextView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private Toolbar mToolbar;

    private int mToolbarColor;
    private int mStatusBarColor;
    private int mPopupHeaderColor;

    public NotaActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nota, container, false);

        // retrieve the nota row id
        notaId = getActivity().getIntent().getExtras().getLong("notaId");
        Log.d(LOG_TAG, "notaId:" + notaId);

        mSubjectTextView = (TextView) rootView.findViewById(R.id.subject_textview);
        mNoteTextView = (TextView) rootView.findViewById(R.id.note_textview);
        mStartTextView = (TextView) rootView.findViewById(R.id.start_textview);
        mEndTextView = (TextView) rootView.findViewById(R.id.end_textview);
        mDurationTextView = (TextView) rootView.findViewById(R.id.duration_textview);
        mCategoryTextView = (TextView) rootView.findViewById(R.id.category_textview);
        mLatitudeTextView = (TextView) rootView.findViewById(R.id.latitude_textview);
        mLongitudeTextView = (TextView) rootView.findViewById(R.id.longitude_textview);
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        mDeleteButton = (Button) rootView.findViewById(R.id.delete_button);

        mToolbar.setTitle("");
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("detail");

//        (getActivity()).setSupportActionBar(mToolbar);

        mSubjectTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] location = new int[2];
                mSubjectTextView.getLocationOnScreen(location);

                p = new Point();
                p.x = location[0];
                p.y = location[1];
                if (p != null)
                    showPopup(getActivity(), p, NotaEntry.COLUMN_SUBJECT, String.valueOf(mSubjectTextView.getText()));

            }
        });

        mNoteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] location = new int[2];
                mNoteTextView.getLocationOnScreen(location);

                p = new Point();
                p.x = location[0];
                p.y = location[1];
                if (p != null)
                    showPopup(getActivity(), p, NotaEntry.COLUMN_NOTE, String.valueOf(mNoteTextView.getText()));

            }
        });

        mStartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] location = new int[2];
                mStartTextView.getLocationOnScreen(location);

                p = new Point();
                p.x = location[0];
                p.y = location[1];
                if (p != null)
                    showPopup(getActivity(), p, NotaEntry.COLUMN_START, String.valueOf(mStartTextView.getText()));

            }
        });

        mEndTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] location = new int[2];
                mEndTextView.getLocationOnScreen(location);

                p = new Point();
                p.x = location[0];
                p.y = location[1];
                if (p != null)
                    showPopup(getActivity(), p, NotaEntry.COLUMN_END, String.valueOf(mEndTextView.getText()));

            }
        });

        mLatitudeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] location = new int[2];
                mLatitudeTextView.getLocationOnScreen(location);

                p = new Point();
                p.x = location[0];
                p.y = location[1];
                if (p != null)
                    showPopup(getActivity(), p, NotaEntry.COLUMN_LAT, String.valueOf(mLatitudeTextView.getText()));

            }
        });

        mLongitudeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] location = new int[2];
                mLongitudeTextView.getLocationOnScreen(location);

                p = new Point();
                p.x = location[0];
                p.y = location[1];
                if (p != null)
                    showPopup(getActivity(), p, NotaEntry.COLUMN_LON, String.valueOf(mLongitudeTextView.getText()));

            }
        });

        mDurationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] location = new int[2];
                mDurationTextView.getLocationOnScreen(location);

                p = new Point();
                p.x = location[0];
                p.y = location[1];
                if (p != null)
                    showPopup(getActivity(), p, NotaEntry.COLUMN_DURATION, String.valueOf(mDurationTextView.getText()));

            }
        });

        mFrameLayout = (FrameLayout) rootView.findViewById(R.id.mainmenu);
        mFrameLayout.getForeground().setAlpha(0);
        mContentValues = new ContentValues();
        return rootView;
    }

    // Get the x and y position after the button is draw on screen
    // (It's important to note that we can't get the position in the onCreate(),
    // because at that stage most probably the view isn't drawn yet, so it will return (0, 0))
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//
//        int[] location = new int[2];
////        Button button = (Button) findViewById(R.id.show_popup);
//
//        // Get the x, y location and store it in the location[] array
//        // location[0] = x, location[1] = y.
////        mTextView.getLocationOnScreen(location);
//
//        //Initialize the Point with x, and y positions
//        p = new Point();
//        p.x = location[0];
//        p.y = location[1];
//    }

    // The method that displays the popup.
    private void showPopup(final Activity context, Point p, String columnName, String content) {
        // device window background
        mFrameLayout.getForeground().setAlpha(220);

        // Inflate the popup.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, viewGroup);

        layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        //
        LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.header_linear_layout);
//        linearLayout.setBackgroundTintList(getResources().getColorStateList(R.color.green));
//        linearLayout.setBackgroundTintList(getResources().getColorStateList(mPopupHeaderColor));
//        linearLayout.setBackgroundTintMode(PorterDuff.Mode.ADD);

        // we can dynamically set the popup window header title
        TextView mTitleView = (TextView)layout.findViewById(R.id.popup_header);
        mTitleView.setText(columnName);


        ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{0xffffcc00});
        mTitleView.setBackgroundTintList(csl);

        int mStatusBarColor = ((Constants) getActivity().getApplication()).getStatusBarColor();
//        mTitleView.setBackgroundColor(getResources().getColor(mStatusBarColor));
//        mTitleView.setBackgroundTintList(getActivity().getResources().getColorStateList(mStatusBarColor));


        // find the edit text field and loading default information
        final EditText mEditView = (EditText)layout.findViewById(R.id.popup_edittext);
        mEditView.setText(content);

        Button save = (Button) layout.findViewById(R.id.save);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);

        // this will make the popup window match the window(device screen)
        //int popupWidth = WindowManager.LayoutParams.MATCH_PARENT;
        //int popupHeight = WindowManager.LayoutParams.MATCH_PARENT;

        int popupWidth = layout.getMeasuredWidth();
        int popupHeight = layout.getMeasuredHeight();

        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);

        popup.setFocusable(true);
        popup.setOutsideTouchable(false);
        // popup window background
        popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_window_dim));
        popup.update();
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                //finish();
//                Toast.makeText(getActivity(), "onDismiss", Toast.LENGTH_SHORT).show();
                mFrameLayout.getForeground().setAlpha(0);

            }
        });

        // set outside untouchable
        //popup.setTouchable(true);
        //popup.setFocusable(false);
        //popup.setOutsideTouchable(false);

        popup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 0;
        int OFFSET_Y = 0;

        // Clear the default translucent background
//        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAtLocation(layout, Gravity.TOP, p.x + OFFSET_X, p.y + OFFSET_Y);

        // Getting a reference to Close button, and close the popup when clicked.
        final Button close = (Button) layout.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
                mFrameLayout.getForeground().setAlpha(0);
//                Toast.makeText(getActivity(), "cancel button clicked", Toast.LENGTH_LONG).show();
            }
        });




        mColumnName = columnName;

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
                mFrameLayout.getForeground().setAlpha(0);

                mContentValues.put(NotaEntry._ID, notaId);
                if (mColumnName.contains(NotaEntry.COLUMN_SUBJECT) ||
                        mColumnName.contains(NotaEntry.COLUMN_NOTE) ||
                        mColumnName.contains(NotaEntry.COLUMN_SUBJECT) ||
                        mColumnName.contains(NotaEntry.COLUMN_SUBJECT)) {
                    mContentValues.put(mColumnName, String.valueOf(mEditView.getText()));
                } else if (mColumnName.contains(NotaEntry.COLUMN_START) ||
                        mColumnName.contains(NotaEntry.COLUMN_END)) {
                    long updateValue = utilities.convertStringToMilliseconds(String.valueOf(mEditView.getText()));
                    if (updateValue != 1L) {
                        mContentValues.put(mColumnName, updateValue);
                    } else {
                        // todo: handle incorrect format
                    }
                } else if (mColumnName.contains(NotaEntry.COLUMN_LAT) ||
                        mColumnName.contains(NotaEntry.COLUMN_LON)) {
                    String str;
                    str = String.valueOf(mEditView.getText());
                    if (!str.isEmpty() && str != null) {
                        double updateValues = Double.parseDouble(str);
                        mContentValues.put(mColumnName, updateValues);
                    } else {
                        // todo: handle empty string
                    }
                } else if (mColumnName.contains(NotaEntry.COLUMN_DURATION)) {

                    long res = -1L;
                    try {
                        res = utilities.convertDurationToSeconds(String.valueOf(mEditView.getText()));
                    }catch (Exception e) {
                        // todo:
                    }

                    Log.d(LOG_TAG, "zztg2:" + res);
                    if (res != -1L) {
                        mContentValues.put(mColumnName, res);
                    }
                }

                mContentValues.remove(CategoryEntry.COLUMN_NAME);
                //mContentValues.put(NotaEntry.COLUMN_START, 1442428166000L);
                int count = 0;
                String str = Arrays.asList(mContentValues.valueSet()).toString();
                count = getActivity().getApplicationContext().getContentResolver().update(
                        NotaEntry.CONTENT_URI, mContentValues, NotaEntry.TABLE_NAME + "." + NotaEntry._ID + "= ?",
                        new String[]{Long.toString(notaId)}
                );


                // verify update correctly
                Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(
                        NotaEntry.CONTENT_URI,
                        NOTA_COLUMNS,
                        NotaEntry.TABLE_NAME + "." + NotaEntry._ID + " = ?",
                        new String[] {String.valueOf(notaId)},
                        null
                );

                cursor.moveToFirst();
                long id = cursor.getLong(COL_NOTA_ID);
                String note = cursor.getString(COL_NOTE);
                long start = cursor.getLong(COL_START);
                long end = cursor.getLong(COL_END);
                long duration = cursor.getLong(COL_DURATION);

                Log.d(LOG_TAG, "notaId in where clause=" + notaId);
                Log.d(LOG_TAG, "cursor count=" + cursor.getCount());
                Log.d(LOG_TAG, "values saved: " + "count=" + count + " "+ str );
                Log.d(LOG_TAG, "values from db: " + "id=" + id + " start=" + start + " end=" + end + " note=" + note + " duration=" + duration);
                if(!cursor.isClosed()) {
                    cursor.close();
                }

            }
        });
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setNavigationBarColor(this.getResources().getColor(R.color.red));

        mToolbarColor = ((Constants) getActivity().getApplication()).getToolbarColor();
        mStatusBarColor = ((Constants) getActivity().getApplication()).getStatusBarColor();
        mPopupHeaderColor = ((Constants) getActivity().getApplication()).getPopupHeaderClor();

        if (Build.VERSION.SDK_INT >= 21) {
            // top status bar
            getActivity().getWindow().setStatusBarColor(this.getResources().getColor(mStatusBarColor));

            // bottom navigation bar
            getActivity().getWindow().setNavigationBarColor(this.getResources().getColor(mStatusBarColor));
        }


        mToolbar.setBackgroundColor(getResources().getColor(mToolbarColor));

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
                    new String[] {String.valueOf(notaId)},
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
//            mContentValues.put(NotaEntry.COLUMN_SUBJECT, subject);

            String note = cursor.getString(COL_NOTE);
            mNoteTextView.setText(note);

            long startTime = cursor.getLong(COL_START);
            mStartTextView.setText(utilities.getReadableDate(startTime));

            long endTime = cursor.getLong(COL_END);
            mEndTextView.setText(utilities.getReadableDate(endTime));

            long duration = cursor.getLong(COL_DURATION);
            mDurationTextView.setText(utilities.convertToReadableDuration(duration));

            String category = cursor.getString(COL_CATEGORY_NAME);
            mCategoryTextView.setText(category);

            float latitude = cursor.getFloat(COL_LAT);
            mLatitudeTextView.setText(String.valueOf(latitude));

            float longitude = cursor.getFloat(COL_LON);
            mLongitudeTextView.setText(String.valueOf(longitude));

            // copy first row of cursor to content values
            DatabaseUtils.cursorRowToContentValues(cursor, mContentValues);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    getActivity().getContentResolver().delete(
                            NotaEntry.CONTENT_URI,
                            NotaEntry.TABLE_NAME + "." + NotaEntry._ID + " = ?",
                            new String[] {String.valueOf(notaId)}
                    );

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "delete record successfully", Toast.LENGTH_LONG).show();
                }
            });
        }

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        Toolbar toolbarView = (Toolbar) getView().findViewById(R.id.toolbar);


        activity.supportStartPostponedEnterTransition();

        if ( null != toolbarView ) {
            activity.setSupportActionBar(toolbarView);

            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

}

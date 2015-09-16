package com.example.android.event.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;


public class NotaContract {

    // The content authority is a name for the entire content provider.
    // It is similar to the relationship between a domain name and its website.
    // The package name for the app can be the content authority, which is guaranteed
    // to be unique on the device.
    public static final String CONTENT_AUTHORITY = "com.example.android.event";

    // Use content authority to create the base of URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_NOTA = "nota";
    public static final String PATH_CATEGORY = "category";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the Julian day at UTC.
    // Todo: refactor it with GregorianCalendar

    /* Inner class that defines the table contents of the category table */
    public static final class CategoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();

        // CURSOR_DIR_BASE_TYPE: URI containing a Cursor of zero or more items
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;

        // CURSOR_ITEM_BASE_TYPE: URI containing a Cursor of a single item
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;

        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TOTAL_TIME = "total_time";

        public static Uri buildCategoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    /* Inner class that defines the table contents of the nota table */
    public static final class NotaEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTA).build();


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTA;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTA;

        public static final String TABLE_NAME = "nota";

        // Column with the foreign key into the category table.
        public static final String COLUMN_CAT_KEY = "category_id";
        // Event subject
        public static final String COLUMN_SUBJECT = "subject";
        // Event note
        public static final String COLUMN_NOTE = "note";
        // Event start date time
        public static final String COLUMN_START = "start";
        // Event end data time
        public static final String COLUMN_END = "end";
        // Event duration
        public static final String COLUMN_DURATION = "duration";

        // We uniquely pinpoint the location where the event happens.
        // We store the current latitude and longitude.
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LON = "lon";

        public static Uri buildNotaUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildNotaCategory(String category) {
            return CONTENT_URI.buildUpon().appendPath(category).build();
        }

        public static Uri buildNotaCategoryWithStartDate(
                String category, long startDate) {
            return CONTENT_URI.buildUpon().appendPath(category)
                    .appendQueryParameter(COLUMN_START, Long.toString(startDate))
                    .build();
        }

        public static Uri buildNotaCategoryWithDate(String category, long date) {
            return CONTENT_URI.buildUpon().appendPath(category)
                    .appendPath(Long.toString(date)).build();
        }

        public static String getCategoryFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }

        public static long getStartDateFromUri(Uri uri) {
            String dateString = uri.getQueryParameter(COLUMN_START);
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }
    }
}

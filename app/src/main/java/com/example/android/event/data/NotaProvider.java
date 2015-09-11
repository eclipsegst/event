package com.example.android.event.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.example.android.event.data.NotaContract.CategoryEntry;
import com.example.android.event.data.NotaContract.NotaEntry;

public class NotaProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private NotaDbHelper mOpenHelper;

    static final int NOTA = 100;
    static final int NOTA_WITH_CATEGORY = 101;
    static final int NOTA_WITH_CATEGORY_AND_DATE = 102;
    static final int CATEGORY = 300;

    private static final SQLiteQueryBuilder sNotaByCategoryQueryBuilder;

    static {
        sNotaByCategoryQueryBuilder = new SQLiteQueryBuilder();

        // inner join
        sNotaByCategoryQueryBuilder.setTables(
                NotaEntry.TABLE_NAME + " INNER JOIN " +
                        CategoryEntry.TABLE_NAME +
                        " ON " + NotaEntry.TABLE_NAME +
                        "." + NotaEntry.COLUMN_CAT_KEY +
                        " = " + CategoryEntry.TABLE_NAME +
                        "." + CategoryEntry._ID
        );
    }

    // select by category: category.name = ?
    private static final String sCategorySelection =
            CategoryEntry.TABLE_NAME +
                    "." + CategoryEntry.COLUMN_NAME + " = ? ";

    // select by category and date range: category.name = ? AND date >= ?
    private static final String sCategoryWithStartDateSelection =
            CategoryEntry.TABLE_NAME +
                    "." + CategoryEntry.COLUMN_NAME + " = ? AND " +
                    NotaEntry.COLUMN_START + " >= ? ";

    // select by category and specific date: category.name = ? AND date = ?
    private static final String sCategoryAndDaySelection =
            CategoryEntry.TABLE_NAME +
                    "." + CategoryEntry.COLUMN_NAME + " = ? AND " +
                    NotaEntry.COLUMN_START + " = ? ";

    private Cursor getNotaByCategory(Uri uri, String[] projection, String sortOrder) {

        String category = NotaEntry.getCategoryFromUri(uri);
        long startDate = NotaEntry.getStartDateFromUri(uri);

        String[] selectionArgs = null;
        String selection = null;

        if (startDate == 0) {
            if (!category.equals("null")) {
                selection = sCategorySelection;
                selectionArgs = new String[]{category};
            }
        } else {
            selection = sCategoryWithStartDateSelection;
            selectionArgs = new String[] {category, Long.toString(startDate)};
        }

        return sNotaByCategoryQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getNotaByCategoryAndDate(Uri uri, String[] projection, String sortOrder) {

        String category = NotaEntry.getCategoryFromUri(uri);
        long date = NotaEntry.getDateFromUri(uri);

        return sNotaByCategoryQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sCategoryAndDaySelection,
                new String[] {category, Long.toString(date)},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NotaContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, NotaContract.PATH_NOTA, NOTA);
        matcher.addURI(authority, NotaContract.PATH_NOTA + "/*", NOTA_WITH_CATEGORY);
        matcher.addURI(authority, NotaContract.PATH_NOTA + "/*/#", NOTA_WITH_CATEGORY_AND_DATE);

        matcher.addURI(authority, NotaContract.PATH_CATEGORY, CATEGORY);

        return matcher;
    }

    @Override
    public boolean onCreate(){
        mOpenHelper = new NotaDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case NOTA_WITH_CATEGORY_AND_DATE:
                return NotaEntry.CONTENT_ITEM_TYPE;
            case NOTA_WITH_CATEGORY:
                return NotaEntry.CONTENT_TYPE;
            case NOTA:
                return NotaEntry.CONTENT_TYPE;
            case CATEGORY:
                return CategoryEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            // "nota/*/*"
            case NOTA_WITH_CATEGORY_AND_DATE:
            {
                retCursor = getNotaByCategoryAndDate(uri, projection, sortOrder);
                break;
            }
            // "nota/*"
            case NOTA_WITH_CATEGORY:
            {
                retCursor = getNotaByCategory(uri, projection, sortOrder);
                break;
            }
            // "nota"
            case NOTA:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NotaEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "category"
            case CATEGORY:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CategoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("unknown uri; " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case NOTA: {
                normalizeDate(values);
                long _id = db.insert(NotaEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = NotaEntry.buildNotaUri(_id);
                else
                    throw  new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CATEGORY: {
                long _id = db.insert(CategoryEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CategoryEntry.buildCategoryUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) selection = "1";

        switch (match) {
            case NOTA:
                rowsDeleted = db.delete(
                        NotaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CATEGORY:
                rowsDeleted = db.delete(
                        CategoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("unknow uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    private void normalizeDate(ContentValues values) {
        if (values.containsKey(NotaEntry.COLUMN_START)) {
            long dateValue = values.getAsLong(NotaEntry.COLUMN_START);
            values.put(NotaEntry.COLUMN_START, NotaContract.normalizeDate(dateValue));
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case NOTA:
                normalizeDate(values);
                rowsUpdated = db.update(NotaEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CATEGORY:
                rowsUpdated = db.update(CategoryEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("unknow uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
    
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case NOTA:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(NotaEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return  returnCount;
            default:
                return super.bulkInsert(uri, values);


        }
    }

    // this method is not necessary, it just assists the test framework in running smoothly.
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
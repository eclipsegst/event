package com.example.android.event.data;

import android.database.Cursor;
import android.test.AndroidTestCase;

import com.example.android.event.data.NotaContract.CategoryEntry;
import com.example.android.event.data.NotaContract.NotaEntry;

public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    // delete all record
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                NotaEntry.CONTENT_URI,
                null,
                null
        );

        mContext.getContentResolver().delete(
                CategoryEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                NotaEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals("Error: Records not deleted from nota table during delete.", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                CategoryEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals("Error: Records not deleted from category table during delete.", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }
}

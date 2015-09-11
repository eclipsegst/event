package com.example.android.event.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import com.example.android.event.data.NotaContract.CategoryEntry;
import com.example.android.event.data.NotaContract.NotaEntry;
import com.example.android.event.utils.PollingCheck;

public class TestUtilities extends AndroidTestCase{

    static final String TEST_CATEGORY = "run";
    static final long TEST_DATE = 1441822024L; // 9/9/2015

    static void validateCursor(String error, Cursor actualValuesCursor, ContentValues expectedValues) {

        assertTrue("Empty cursor returned. " + error, actualValuesCursor.moveToFirst());
        validateCurrentRecord(error, actualValuesCursor, expectedValues);
        actualValuesCursor.close();

    }

    static void validateCurrentRecord(String error, Cursor actualValuesCursor, ContentValues expectedValues) {

        Set<Map.Entry<String, Object>> expectedValueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry :expectedValueSet) {
            String columnName = entry.getKey();
            int idx = actualValuesCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found." + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match expected value '" + expectedValue  + "'. " + error,
                    expectedValue, actualValuesCursor.getString(idx)
            );
        }

    }

    static ContentValues createCategoryValues() {

        ContentValues testValuesCategory = new ContentValues();
        testValuesCategory.put(CategoryEntry.COLUMN_NAME, TEST_CATEGORY);
        testValuesCategory.put(CategoryEntry.COLUMN_TOTAL_TIME, 365);

        return testValuesCategory;
    }

    static ContentValues createNotaValues(long categoryRowId) {

        ContentValues testValuesNota = new ContentValues();
        testValuesNota.put(NotaEntry.COLUMN_CAT_KEY, categoryRowId);
        testValuesNota.put(NotaEntry.COLUMN_SUBJECT, "running");
        testValuesNota.put(NotaEntry.COLUMN_NOTE, "I'm feeling good!");
        testValuesNota.put(NotaEntry.COLUMN_START, 1441822024L);
        testValuesNota.put(NotaEntry.COLUMN_END, 1441822024L);
        testValuesNota.put(NotaEntry.COLUMN_DURATION, 365);
        testValuesNota.put(NotaEntry.COLUMN_LAT, 38.9338);
        testValuesNota.put(NotaEntry.COLUMN_LON, -92.3183);

        return testValuesNota;
    }

    static long insertCategoryValues(Context context) {
        NotaDbHelper dbHelper = new NotaDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValuesCategory = TestUtilities.createCategoryValues();

        long categoryRowId;
        categoryRowId = db.insert(CategoryEntry.TABLE_NAME, null, testValuesCategory);

        assertTrue("Error: Failure to insert category values", categoryRowId != -1);

        return categoryRowId;
    }

    /**
     * The functions we provide inside of TestProvider use this utility class to test the
     * ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
     * CTS tests.
     * Note that this only tests that the onChange function is called; it does not test that the
     * correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}

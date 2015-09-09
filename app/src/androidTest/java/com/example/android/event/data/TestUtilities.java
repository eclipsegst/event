package com.example.android.event.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import com.example.android.event.data.NotaContract.CategoryEntry;
import com.example.android.event.data.NotaContract.NotaEntry;

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

    static ContentValues createNotaValues(long notaRowId) {

        ContentValues testValuesNota = new ContentValues();
        testValuesNota.put(NotaEntry.COLUMN_CAT_KEY, notaRowId);
        testValuesNota.put(NotaEntry.COLUMN_SUBJECT, "running");
        testValuesNota.put(NotaEntry.COLUMN_NOTE, "I'm feeling good!");
        testValuesNota.put(NotaEntry.COLUMN_START, 1441822024L);
        testValuesNota.put(NotaEntry.COLUMN_END, 1441822024L);
        testValuesNota.put(NotaEntry.COLUMN_DURATION, 365);
        testValuesNota.put(NotaEntry.COLUMN_LAT, 38.9338);
        testValuesNota.put(NotaEntry.COLUMN_LON, -92.3183);

        return testValuesNota;
    }
}

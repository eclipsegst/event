package com.example.android.event.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

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

    /* verify the content provider is registered correctly */
    public void testProviderRegistry() {

        PackageManager pm = mContext.getPackageManager();

        // define the component name based on the package name from the context and
        // the NotaProvider class
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                NotaProvider.class.getName());
        try {
            // fetch the provider info using component name
            // throws an exception if the provider isn't registered
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            // make sure that the registered authrity matches the authority from the contract
            assertEquals("Error: NotaProvider registered with authority: " + providerInfo.authority +
                    " instead of authority: " + NotaContract.CONTENT_AUTHORITY,
                    providerInfo.authority, NotaContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: NotaProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /**
     *  This test doesn't touch the database. It verifies that the ContentProvider returns
     *  the correct type for each type of URI that it can handle.
     */
    public void testGetType() {
        // content://com.example.android.event/nota
        String type = mContext.getContentResolver().getType(NotaEntry.CONTENT_URI);
        assertEquals("Error: the NotaEntry CONTENT_URI should return NotaEntry.CONTENT_TYPE",
                NotaEntry.CONTENT_TYPE, type);

        String testCategory = "work out";
        // content://com.example.android.event/nota/work%20out
        type = mContext.getContentResolver().getType(
                NotaEntry.buildNotaCategory(testCategory));

        assertEquals("Error: the NotaEntry CONTENT_URI with category should return NotaEntry.CONTENT_TYPE",
                NotaEntry.CONTENT_TYPE, type);

        long testDate = 1441822024L;
        // content://com.example.android.event/nota/work%20out/20150909
        type = mContext.getContentResolver().getType(
                NotaEntry.buildNotaCategoryWithDate(testCategory, testDate));
        assertEquals("Error: the NotaEngry CONTENT_URI with category and date should return NotaEntry.CONTENT_TYPE",
                NotaEntry.CONTENT_ITEM_TYPE, type);

        //content://com.example.android.event/category
        type = mContext.getContentResolver().getType(CategoryEntry.CONTENT_URI);
        assertEquals("Error: the CategoryEntry CONTENT_URI should return CategoryEntry.CONTENT_TYPE",
                CategoryEntry.CONTENT_TYPE, type);
    }

    /**
     * This test uses the database directly to insert and then uses the ContentPovider to read out
     * the data.
     */
    public void testBasicCategoryQuery() {
        NotaDbHelper dbHelper = new NotaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValuesCategory = TestUtilities.createCategoryValues();
        long categoryRowId = TestUtilities.insertCategoryValues(mContext);

        Cursor cursorCategory = mContext.getContentResolver().query(
                CategoryEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicCategoryQuery", cursorCategory, testValuesCategory);
        if (Build.VERSION.SDK_INT >= 19) {
            assertEquals("Error: Category query didn't properly set NotificationUri",
                    cursorCategory.getNotificationUri(), CategoryEntry.CONTENT_URI);
        }
    }

    /**
     * This test uses the database directly to insert and then uses the ContentProvider to read out
     * the data.
     */
    public void testBasicNotaQuery() {
        // insert test records into database
        NotaDbHelper dbHelper = new NotaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValuesCategory = TestUtilities.createCategoryValues();
        long categoryRowId = TestUtilities.insertCategoryValues(mContext);

        ContentValues testValuesNota = TestUtilities.createNotaValues(categoryRowId);
        long notaRowId = db.insert(NotaEntry.TABLE_NAME, null, testValuesNota);
        assertTrue("Unable to insert nota entry into the database.", notaRowId != -1);

        db.close();

        // test the basic content provider query
        Cursor cursorNota = mContext.getContentResolver().query(
                NotaEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicNotaQuery", cursorNota, testValuesNota);
    }

    /**
     * This test uses the ContentProvider to insert and then update the data.
     */
    public void testUpdateCategory() {
        ContentValues testValuesCategory = TestUtilities.createCategoryValues();
        Uri categoryUri = mContext.getContentResolver().
                insert(CategoryEntry.CONTENT_URI, testValuesCategory);
        long categoryRowId = ContentUris.parseId(categoryUri);

        assertTrue("Error: Insert category entry failure.", categoryRowId != -1);
        Log.d(LOG_TAG, "New category row id: " + categoryRowId);

        ContentValues updateValues = new ContentValues(testValuesCategory);
        updateValues.put(CategoryEntry._ID, categoryRowId);
        updateValues.put(CategoryEntry.COLUMN_NAME, "gym");

        // create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor cursorCategory = mContext.getContentResolver().query(CategoryEntry.CONTENT_URI, null,
                null, null, null);
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        cursorCategory.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                CategoryEntry.CONTENT_URI, updateValues, CategoryEntry._ID + "= ?",
                new String[]{Long.toString(categoryRowId)}
        );

        assertEquals(1, count);

        // If our code is failing here, it means that the content provider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        cursorCategory.unregisterContentObserver(tco);
        cursorCategory.close();

        Cursor cursor = mContext.getContentResolver().query(
                CategoryEntry.CONTENT_URI,
                null,
                CategoryEntry._ID + " = " + categoryRowId,
                null,
                null
        );

        TestUtilities.validateCursor("testUpdateCategory", cursor, updateValues);
        cursor.close();
    }

    /**
     * Test insert and read provider.
     * It relies on insertions, so insert and query functionality must also be completed before
     * this test can be used.
     */
    public void testInsertReadProvider() {
        ContentValues testValuesCategory = TestUtilities.createCategoryValues();

        // register a content observer for insert
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(CategoryEntry.CONTENT_URI, true, tco);
        Uri categoryUri = mContext.getContentResolver().insert(CategoryEntry.CONTENT_URI, testValuesCategory);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long categoryRowId = ContentUris.parseId(categoryUri);

        assertTrue(categoryRowId != -1);

        Cursor cursorCategory = mContext.getContentResolver().query(
                CategoryEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns
                null, // columns for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating CategoryEntry",
                cursorCategory, testValuesCategory);

        ContentValues testValuesNota = TestUtilities.createNotaValues(categoryRowId);
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(NotaEntry.CONTENT_URI, true, tco);

        Uri notaInsertUri = mContext.getContentResolver()
                .insert(NotaEntry.CONTENT_URI, testValuesNota);
        assertTrue(categoryUri != null);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        Cursor cursorNota = mContext.getContentResolver().query(
                NotaEntry.CONTENT_URI,
                null, // leaving "columns" null just return all the columns
                null, // columns for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating NotaEntry insert.",
                cursorNota, testValuesNota);

        // get the joined category and nota data with a start date
        cursorNota = mContext.getContentResolver().query(
                NotaEntry.buildNotaCategoryWithStartDate(
                        TestUtilities.TEST_CATEGORY, TestUtilities.TEST_DATE),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testInsertReadProvider. Error validating joined category and nota data with start date",
                cursorNota, testValuesNota);

        // get the joined nota data for a specific date
        cursorNota = mContext.getContentResolver().query(
                NotaEntry.buildNotaCategoryWithStartDate(TestUtilities.TEST_CATEGORY, TestUtilities.TEST_DATE),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testInsertReadProvider. Error validating joined category and nota data with a specific date.",
                cursorNota, testValuesNota);
    }

    public void testDeleteRecords() {
        testInsertReadProvider();

        TestUtilities.TestContentObserver categoryObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(CategoryEntry.CONTENT_URI, true, categoryObserver);

        TestUtilities.TestContentObserver notaObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(CategoryEntry.CONTENT_URI, true, notaObserver);

        deleteAllRecordsFromProvider();
        categoryObserver.waitForNotificationOrFail();
        notaObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(categoryObserver);
        mContext.getContentResolver().unregisterContentObserver(notaObserver);
    }

    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static ContentValues[] createBulkInsertNotaValues(long categoryRowId) {
        long currentTestDate = TestUtilities.TEST_DATE;
        long millisecondsInADay = 1000*60*60*24;
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, currentTestDate+= millisecondsInADay ) {
            ContentValues testValuesNota = new ContentValues();
            testValuesNota.put(NotaEntry.COLUMN_CAT_KEY, categoryRowId);
            testValuesNota.put(NotaEntry.COLUMN_SUBJECT, "running" + i);
            testValuesNota.put(NotaEntry.COLUMN_NOTE, "I'm feeling good!" + i);
            testValuesNota.put(NotaEntry.COLUMN_START, 1441822024L);
            testValuesNota.put(NotaEntry.COLUMN_END, 1441822024L);
            testValuesNota.put(NotaEntry.COLUMN_DURATION, 365);
            testValuesNota.put(NotaEntry.COLUMN_LAT, 38.9338);
            testValuesNota.put(NotaEntry.COLUMN_LON, -92.3183);

            returnContentValues[i] = testValuesNota;
        }
        return returnContentValues;
    }

    public void testBulkInsert() {
        ContentValues testValuesCategory = TestUtilities.createCategoryValues();
        Uri categoryUri = mContext.getContentResolver().insert(CategoryEntry.CONTENT_URI, testValuesCategory);
        long categoryRowId = ContentUris.parseId(categoryUri);
        assertTrue(categoryRowId != -1);

        Cursor cursorCategory = mContext.getContentResolver().query(
                CategoryEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testBulkInsert. Error validating CategoryEntry",
                cursorCategory, testValuesCategory);

        ContentValues[] bulkInsertContentValues = createBulkInsertNotaValues(categoryRowId);

        TestUtilities.TestContentObserver notaObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(NotaEntry.CONTENT_URI, true, notaObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(NotaEntry.CONTENT_URI, bulkInsertContentValues);

        notaObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(notaObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        Cursor cursorNota = mContext.getContentResolver().query(
                NotaEntry.CONTENT_URI,
                null,
                null,
                null,
                NotaEntry.COLUMN_START + " ASC" // sort order == by date ascending
        );

        assertEquals(cursorNota.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        cursorNota.moveToFirst();
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursorNota.moveToNext()) {
            TestUtilities.validateCurrentRecord("testBulkInsert. Error validating NotaEntry " + i,
                    cursorNota, bulkInsertContentValues[i]);
        }
        cursorNota.close();


    }

}

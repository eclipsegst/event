package com.example.android.event.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

import com.example.android.event.data.NotaContract.CategoryEntry;
import com.example.android.event.data.NotaContract.NotaEntry;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();
    public static String zhao = "zhao: ";

    // Start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(NotaDbHelper.DATABASE_NAME);
    }

    /**
     * This function gets called before each test is executed to delete the database.
     * This makes sure that we always have clean test.
     */

    public void setUp() {
        deleteTheDatabase();
    }

    /* Test tables have been created and have the correct columns. */
    public void testCreatedDb() throws Throwable {

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(NotaContract.CategoryEntry.TABLE_NAME);
        tableNameHashSet.add(NotaContract.NotaEntry.TABLE_NAME);

        mContext.deleteDatabase(NotaDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new NotaDbHelper(
                this.mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());

        Cursor cTable = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        // moveToFirst: Move the cursor to the first row and will return false if the cursor is empty.
        assertTrue("Error: The database has not created correctly",
                cTable.moveToFirst());

        // verify if the tables have been created
        do {
            tableNameHashSet.remove(cTable.getString(0));
        } while( cTable.moveToNext());

        assertTrue("Error: The database was created without both the category and nota tables.",
                tableNameHashSet.isEmpty());

        // verify if the columns have been created correctly
        Cursor cColumnCategory = db.rawQuery("PRAGMA table_info(" + NotaContract.CategoryEntry.TABLE_NAME + ")",
                null);
        assertTrue("Error: We were unable to query the database for 'category' table information.",
                cColumnCategory.moveToFirst());

        final HashSet<String> categoryColumnHashSet = new HashSet<String>();
        categoryColumnHashSet.add(CategoryEntry._ID);
        categoryColumnHashSet.add(CategoryEntry.COLUMN_NAME);
        categoryColumnHashSet.add(CategoryEntry.COLUMN_TOTAL_TIME);

        int columnNameIndexCategory = cColumnCategory.getColumnIndex("name");
        do {
            String columnName = cColumnCategory.getString(columnNameIndexCategory);
            categoryColumnHashSet.remove(columnName);
        } while(cColumnCategory.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required category entry columns." + zhao,
                categoryColumnHashSet.isEmpty());
        // nota
        Cursor cColumnNota = db.rawQuery("PRAGMA table_info(" + NotaContract.NotaEntry.TABLE_NAME + ")",
                null);
        assertTrue("Error: We were unable to query the database for 'nota' table information.",
                cColumnNota.moveToFirst());

        final HashSet<String> notaColumnHashSet = new HashSet<String>();
        notaColumnHashSet.add(NotaEntry._ID);
        notaColumnHashSet.add(NotaEntry.COLUMN_CAT_KEY);
        notaColumnHashSet.add(NotaEntry.COLUMN_SUBJECT);
        notaColumnHashSet.add(NotaEntry.COLUMN_NOTE);
        notaColumnHashSet.add(NotaEntry.COLUMN_START);
        notaColumnHashSet.add(NotaEntry.COLUMN_END);
        notaColumnHashSet.add(NotaEntry.COLUMN_DURATION);
        notaColumnHashSet.add(NotaEntry.COLUMN_LAT);
        notaColumnHashSet.add(NotaEntry.COLUMN_LON);

        int columnNameIndexNota = cColumnNota.getColumnIndex("name");
        do {
            String columnName = cColumnNota.getString(columnNameIndexNota);
            notaColumnHashSet.remove(columnName);
        } while(cColumnNota.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required nota entry columns." + zhao,
                notaColumnHashSet.isEmpty());

        db.close();
    }

    /* Test that we can insert and query category table correctly */
    public void testCategoryTable() {
        insertCategory();
    }

    /* Test that we can insert and query nota talbe correctly */
    public void testNotaTable() {

        long categoryRowId = insertCategory();
        assertFalse("Error: Category not inserted correctly", categoryRowId == -1L);

        NotaDbHelper dbHelper = new NotaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testNotaValues = TestUtilities.createNotaValues(categoryRowId);

        long notaRowId = db.insert(NotaEntry.TABLE_NAME, null, testNotaValues);
        assertTrue("Error: Nota not inserted correctly", notaRowId != -1);

        Cursor cNota = db.query(
                NotaEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue("Error: No records returned from nota query", cNota.moveToFirst());
        TestUtilities.validateCurrentRecord("Error: Testing insert and query nota table failed to validate",
                cNota, testNotaValues);
        assertFalse("Error: More than one record returned from nota query",
                cNota.moveToNext());

        cNota.close();
        dbHelper.close();
    }

    public long insertCategory() {

        // first step: get reference to writable database
        NotaDbHelper dbHelper = new NotaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // second step: create content values of what we want to insert
        ContentValues testValuesCategory = TestUtilities.createCategoryValues();

        // third step: insert content values into datbase and get a row id back
        long categoryId;
        categoryId = db.insert(CategoryEntry.TABLE_NAME, null, testValuesCategory);
        // verify we got a row back
        assertTrue(categoryId != -1);

        // fourth step: query the database and receive a cursor back,
        // which is the primary interface to the query results
        Cursor cCategory = db.query(
                CategoryEntry.TABLE_NAME,
                null, // leaving null will return all columns
                null, // columns for the "where" clause
                null, // values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // verify if we got any records back
        assertTrue("Error: No records returned from category query", cCategory.moveToFirst());

        // fifth step: validate data in resulting cusor with the original content values
        TestUtilities.validateCurrentRecord("Error: Category query validation failed",
                cCategory, testValuesCategory);
        // verify there is only one record in the database
        assertFalse("Error: More than one record returned from category query",
                cCategory.moveToNext());

        // sixth step: close cursor and database
        cCategory.close();
        db.close();

        return categoryId;
    }



}

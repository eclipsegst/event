package com.example.android.event.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

public class TestUriMatcher extends AndroidTestCase{

    private static final String CATEGORY_QUERY = "work out";
    private static final long TEST_DATE = 1441822024L;
    private static final long TEST_CATEGORY_ID = 10L;
    private static final long TEST_NOTA_ID = 10L;

    // content://com.example.android.event/nota
    private static final Uri TEST_NOTA_DIR = NotaContract.NotaEntry.CONTENT_URI;
    private static final Uri TEST_NOTA_WITH_CATEGORY_DIR = NotaContract.NotaEntry
            .buildNotaCategory(CATEGORY_QUERY);
    private static final Uri TEST_NOTA_WITH_CATEGORY_AND_DATE_DIR = NotaContract.NotaEntry
            .buildNotaCategoryWithDate(CATEGORY_QUERY, TEST_DATE);
    // content:// com.example.android.event/category
    private static final Uri TEST_CATEGORY_DIR = NotaContract.CategoryEntry.CONTENT_URI;

    public void testUriMatcher() {
        UriMatcher testMatcher = NotaProvider.buildUriMatcher();

        assertEquals("Error: The NOTA URI was matched incorrectly.",
                testMatcher.match(TEST_NOTA_DIR), NotaProvider.NOTA);
        assertEquals("Error: The NOTA WITH CATEGORY URI was matched incorrectly.",
                testMatcher.match(TEST_NOTA_WITH_CATEGORY_DIR), NotaProvider.NOTA_WITH_CATEGORY);
        assertEquals("Error: The NOTA WITH CATEGORY WITH DATE URI was matched incorrectly.",
                testMatcher.match(TEST_NOTA_WITH_CATEGORY_AND_DATE_DIR), NotaProvider.NOTA_WITH_CATEGORY_AND_DATE);
        assertEquals("Error: The CATEGORY URI was matched incorrectly.",
                testMatcher.match(TEST_CATEGORY_DIR), NotaProvider.CATEGORY);
    }
}

package com.example.android.event.data;

import android.net.Uri;
import android.test.AndroidTestCase;
import com.example.android.event.data.NotaContract.NotaEntry;

public class TestNotaContract extends AndroidTestCase {

    private static final String TEST_NOTA_CATEGORY = "/work out";
    private static final long TEST_NOTA_DATE = 1441822024L; // 9/9/2015

    public void testBuildNotaCategory() {
        Uri categoryUri = NotaEntry.buildNotaCategory(TEST_NOTA_CATEGORY);
        assertNotNull("Error: Null Uri returned. You must fill in buildNotaCategory in" +
                        "NotaContract.",
                categoryUri);

        assertEquals("Error: Nota category not properly appended to the end of the Uri",
                TEST_NOTA_CATEGORY, categoryUri.getLastPathSegment());

        assertEquals("Error: Nota category Uri doesn't match our expected result",
                categoryUri.toString(),
                "content://com.example.android.event/nota/%2Fwork%20out");
    }

    public void testBuildNotaCategpruWithStartDate() {
        Uri notaCategoryWithStartDateUri = NotaEntry.buildNotaCategoryWithStartDate(TEST_NOTA_CATEGORY, TEST_NOTA_DATE);
        assertNotNull("Error: Null Uri returned. You must fill in buildNotaCategoryWithStartDate in" +
                        "NotaContract.",
                notaCategoryWithStartDateUri);
        assertEquals("Error: Nota category not properly appended to the end of the Uri",
                TEST_NOTA_CATEGORY,
                notaCategoryWithStartDateUri.getLastPathSegment());
        assertEquals("Error: Nota start date not properly appended to the end of the Uri",
                NotaContract.normalizeDate(TEST_NOTA_DATE),
                NotaEntry.getStartDateFromUri(notaCategoryWithStartDateUri));
    }

    public void testBuildNotaCategoryWithDate() {
        Uri notaCategoryWithDateUri = NotaEntry.buildNotaCategoryWithDate(TEST_NOTA_CATEGORY, TEST_NOTA_DATE);
        assertNotNull("Error: Null Uri returned. You must fill in buildNotaCategoryWithDate in" +
                        "NotaContract.",
                notaCategoryWithDateUri);
        assertEquals("Error: Nota category not properly appended to the end of the Uri",
                TEST_NOTA_CATEGORY,
                NotaEntry.getCategoryFromUri(notaCategoryWithDateUri));
        assertEquals("Error: Nota date not properly appended to the end of the Uri",
                NotaContract.normalizeDate(TEST_NOTA_DATE),
                NotaEntry.getDateFromUri(notaCategoryWithDateUri));
    }
}

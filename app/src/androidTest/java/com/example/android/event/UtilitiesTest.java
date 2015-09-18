package com.example.android.event;

import android.test.AndroidTestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtilitiesTest extends AndroidTestCase {

    static final long TEST_DATE = 1442428166000L; // 16/09/15 13:29:26
    static final String TEST_STRING = "16/09/15 13:29:26";
    String dateFormat = utilities.dateFormat;

    public void testcConvertStringToMilliseconds() {

        long result = -1L;

        try {
            result = utilities.convertStringToMilliseconds(TEST_STRING, dateFormat);

        } catch (Exception pe) {

        }

        assertEquals("Error: Cannot convert string to milliseconds.", TEST_DATE, result);
    }

}

package com.example.android.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zz on 9/11/15.
 */
public class Utilities {

    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format
     * @return String representing date in specified format
     */
    public static String getReadableDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String formatDuration(long duration) {
        String res = "";

        while (duration > 0) {
            if (duration / 3600 > 0) {
                res += duration / 3600 + "h ";
                duration = duration % 3600;
            } else if (duration / 60 > 0) {
                res += duration / 60 + "m ";
                duration = duration - duration % 60;
            } else {
                res += duration + "s ";
                if (duration < 60) break;
            }
        }

        return res;
    }
}

package com.example.android.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by zz on 9/11/15.
 */
public class utilities {

    public static String dateFormat = "dd/MM/yy HH:mm:ss";
    public static String timeFormat = "HH:mm:ss";

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

    public static String getReadableDate(long milliSeconds)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    public static String formatDuration(long duration) {
        String res = "";

        int h = 0;
        int m = 0;
        int s = 0;
        while (duration > 0) {
            if (duration / 3600 > 0) {
                h = (int)duration / 3600;
                duration = duration % 3600;
            } else if (duration / 60 > 0) {
                m = (int)duration / 60;
                duration = duration - duration % 60;
            } else {
                s = (int)duration;
                if (duration < 60) break;
            }
        }

        if (h > 0) {
            if (String.valueOf(h).length() == 1) {
                res += "0" + h;
            } else {
                res += h;
            }
            res += ":";

        } else {
            res += "00:";
        }

        if (m > 0) {
            if (String.valueOf(m).length() == 1) {
                res += "0" + m;
            } else {
                res += m;
            }
            res += ":";

        } else {
            res += "00:";
        }

        if (s > 0) {
            if (String.valueOf(s).length() == 1) {
                res += "0" + s;
            } else {
                res += s;
            }

        } else {
            res += "00";
        }


        return res;
    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static long convertStringToMilliseconds(String str, String dateFormat) {

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Date date;
        long result = -1L;

        try {
            date = formatter.parse(str);
            result = date.getTime();

        } catch (ParseException pe) {

        }

        return result;
    }



    public static long convertStringToMilliseconds(String str) {

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Date date;
        long result = -1L;

        try {
            date = formatter.parse(str);
            result = date.getTime();

        } catch (ParseException pe) {

        }

        return result;
    }public static long convertDurationToSeconds(String str) {

        long res = -1L;
//        String str = "14:35:06";
        try {
            String[] tokens = str.split(":");

            int hours = Integer.parseInt(tokens[0]);
            int minutes = Integer.parseInt(tokens[1]);
            int seconds = Integer.parseInt(tokens[2]);
            res = 3600 * hours + 60 * minutes + seconds;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}

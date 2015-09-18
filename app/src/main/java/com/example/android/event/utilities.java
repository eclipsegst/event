package com.example.android.event;

import android.content.Context;
import android.text.format.Time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class utilities {

    // Format used for storing dates in the database.  ALso used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyyMMdd";
    public static String dateFormat = "MM/dd/yy HH:mm:ss";
    public static String timeFormat = "HH:mm:ss";

    /**
     * Return date in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing date in specified format
     */
    public static String getReadableDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getReadableDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String convertToReadableDuration(long duration) {

        String res = "";

        long hours = 0;
        hours = duration / (60 * 60);

        if (hours > 0) {
            duration = duration - 3600 * hours;
        }

        long minutes = 0;
        minutes = duration / 60;

        if (minutes > 0) {
            duration = duration - 60 * minutes;
        }

        long seconds = 0;
        seconds = duration;

        res += duationPadding(hours) + ":" + duationPadding(minutes) + ":" + duationPadding(seconds);
        return res;
    }

    public static String duationPadding(long num) {
        if (String.valueOf(num).length() < 2) {
            return "0" + num;
        }

        return String.valueOf(num);
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
    }

    public static long convertDurationToSeconds(String str) {

        long res = -1L;
//        String str = "14:35:06";
        try {
            String[] tokens = str.split(":");

            long hours = Long.parseLong(tokens[0]);
            long minutes = Long.parseLong(tokens[1]);
            long seconds = Long.parseLong(tokens[2]);
            res = 3600 * hours + 60 * minutes + seconds;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    static String formatDate(long dateInMilliseconds) {
        Date date = new Date(dateInMilliseconds);
        return DateFormat.getDateInstance().format(date);
    }

    /**
     * Helper method to convert the database representation of the date into something to display
     * to users.  As classy and polished a user experience as "20140102" is, we can do better.
     *
     * @param context      Context to use for resource localization
     * @param dateInMillis The date in milliseconds
     * @return a user-friendly representation of the date.
     */
    public static String getFriendlyDayString(Context context, long dateInMillis) {
        // The day string for forecast uses the following logic:
        // For today: "Today, June 8"
        // For tomorrow:  "Tomorrow"
        // For the next 5 days: "Wednesday" (just the day name)
        // For all days after that: "Mon Jun 8"

        Time time = new Time();
        time.setToNow();
        long currentTime = System.currentTimeMillis();
        int julianDay = Time.getJulianDay(dateInMillis, time.gmtoff);
        int currentJulianDay = Time.getJulianDay(currentTime, time.gmtoff);

        // If the date we're building the String for is today's date, the format
        // is "Today, June 24"
        if (julianDay == currentJulianDay) {
            String today = context.getString(R.string.today);
            int formatId = R.string.format_full_friendly_date;
//            return String.format(context.getString(
//                    formatId,
//                    today,
//                    getFormattedMonthDay(context, dateInMillis)));
            return today;
        } else if (julianDay > currentJulianDay - 7) {
            // If the input date is less than a week in the future, just return the day name.
            return getDayName(context, dateInMillis);
        } else if (julianDay <= currentJulianDay - 7) {
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("MMM dd");
            return shortenedDateFormat.format(dateInMillis);
        } else {
            // Otherwise, use the form "Mon Jun 3"
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(dateInMillis);
        }
    }

    /**
     * Given a day, returns just the name to use for that day.
     * E.g "today", "tomorrow", "wednesday".
     *
     * @param context      Context to use for resource localization
     * @param dateInMillis The date in milliseconds
     * @return
     */
    public static String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if (julianDay == currentJulianDay + 1) {
            return context.getString(R.string.tomorrow);
        } else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }

    /**
     * Converts db date format to the format "Month day", e.g "June 24".
     *
     * @param context      Context to use for resource localization
     * @param dateInMillis The db formatted date string, expected to be of the form specified
     *                     in Utility.DATE_FORMAT
     * @return The day in the form of a string formatted "December 6"
     */
    public static String getFormattedMonthDay(Context context, long dateInMillis) {
        Time time = new Time();
        time.setToNow();
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(utilities.DATE_FORMAT);
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMMM dd");
        String monthDayString = monthDayFormat.format(dateInMillis);
        return monthDayString;
    }
}

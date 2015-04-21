package com.taskr.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by SoftwareSpot on 20/02/2015.
 */
public class DateUtils {
    private static final String sDataFormat = "yyyy-MM-dd HH:mm:ss"; // ISO 8601 format

    /**
     * Gets the current user's date and time
     *
     * @return Current date
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * Get the ISO 8601 format i.e. yyyy-MM-dd HH:mm:ss
     *
     * @return ISO 8601 format
     */
    public static String getDateFormat() {
        return sDataFormat;
    }

    /**
     * Parse an ISO 8601 formatted string to a date object reference
     *
     * @param date A date as the ISO 8601 format i.e. yyyy-MM-dd HH:mm:ss
     * @return Date object reference
     */
    public static Date stringToDate(String date) {
        try {
            return new SimpleDateFormat(sDataFormat, Locale.ROOT).parse(date);
        } catch (ParseException ignored) {
        }
        return new Date();
    }
}

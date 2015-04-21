package com.taskr.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by SoftwareSpot on 20/02/2015.
 */
public class StringUtils {
    private static final byte sAlphabet_Count = 26;
    public static final byte COMPACT_LENGTH = 25;
    public static final String EMPTY = "";
    public static final String SPACE = " ";

    /**
     * Return a compacted string with a specified length
     *
     * @param value  String to compact
     * @param length Characters to display starting from the left
     * @return Compacted string
     */
    public static String compact(String value, int length) {
        return !ObjectUtils.isNull(value) && length > 0 && length < value.length() ? String.format("%s...", value.substring(0, length)) : value;
    }

    /**
     * Return a compacted string with a length of StringUtils.COMPACT_LENGTH
     *
     * @param value String to compact
     * @return Compacted string
     */
    public static String compact(String value) {
        return compact(value, COMPACT_LENGTH);
    }

    /**
     * Parse a date object reference to an ISO 8601 format i.e. yyyy-MM-dd HH:mm:ss
     *
     * @param date Date object reference to convert
     * @return Date as an ISO 8601 format
     */
    public static String dateToString(Date date) {
        return new SimpleDateFormat(DateUtils.getDateFormat(), Locale.ROOT).format(date);
    }

    /**
     * Gets the current user's date and time as an ISO 8601 format
     *
     * @return Current date as an ISO 8601 format
     */
    public static String getCurrentDate() {
        return dateToString(new Date());
    }

    /**
     * Return the first character (letter or digit) of a string
     *
     * @param value String to parse
     * @return First character in an upper case form
     */
    public static String getFirstLetter(String value) {
        if (!value.isEmpty()) {
            for (int i = 0; i < value.length(); i++) {
                char character = value.charAt(i);
                if (Character.isLetterOrDigit(character)) {
                    return String.valueOf(Character.isLowerCase(character) ? Character.toUpperCase(character) : character);
                }
            }
        }
        return String.valueOf((char) (new Random().nextInt(sAlphabet_Count) + 'A')); // Generate a random character
    }

    /**
     * Checks if a string is null or empty
     *
     * @param value String to check
     * @return True the value is null or empty; otherwise, false
     */
    public static boolean isNullOrEmpty(CharSequence value) { // http://svn.apache.org/viewvc/commons/proper/lang/trunk/src/main/java/org/apache/commons/lang3/StringUtils.java
        int length;
        if (ObjectUtils.isNull(value) || (length = value.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

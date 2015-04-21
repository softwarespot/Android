package com.taskr.utilities;

/**
 * Created by SoftwareSpot on 07/04/2015.
 */
public class IntegerUtils {
    public static final int DEFAULT = 0;
    public static final int INTEGER_FALSE = 0;
    public static final int INTEGER_TRUE = 1;

    /**
     * Convert a boolean value to an integer representation
     *
     * @param value to convert
     * @return Integer of either INTEGER_TRUE or INTEGER_FALSE.
     */
    public static int booleanToInt(boolean value) {
        return value ? INTEGER_TRUE : INTEGER_FALSE;
    }

    /**
     * Safely parse a string representation of an integer to an integer
     *
     * @param value The string to parse
     * @return Integer datatype on success or default value on failure
     */
    public static int tryParse(String value) {
        return tryParse(value, DEFAULT);
    }

    /**
     * Safely parse a string representation of an integer to an integer
     *
     * @param value        The string to parse
     * @param defaultValue The default value on failure
     * @return Integer datatype on success or default value on failure
     */
    public static int tryParse(String value, int defaultValue) {
        if (ObjectUtils.isNull(value) || value.isEmpty()) {
            return defaultValue;
        }
        int startIndex = (value.charAt(0) == '-' || value.charAt(0) == '+' ? 1 : 0);
        for (int i = startIndex; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return defaultValue;
            }
        }
        return Integer.parseInt(value); // Assume that at this point is's a valid integer
    }
}

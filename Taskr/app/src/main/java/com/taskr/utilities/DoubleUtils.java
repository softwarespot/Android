package com.taskr.utilities;

/**
 * Created by SoftwareSpot on 07/04/2015.
 */
public class DoubleUtils {
    public static final double DEFAULT = 0.0d;

    /**
     * Safely parse a string representation of a double to a double
     *
     * @param value The string to parse
     * @return Double datatype on success or default value on failure
     */
    public static double tryParse(String value) {
        return tryParse(value, DEFAULT);
    }

    /**
     * Safely parse a string representation of a double to a double
     *
     * @param value        The string to parse
     * @param defaultValue The default value on failure
     * @return Double datatype on success or default value on failure
     */
    public static double tryParse(String value, double defaultValue) {
        if (ObjectUtils.isNull(value) || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ignored) {

        }
        return defaultValue;
    }
}

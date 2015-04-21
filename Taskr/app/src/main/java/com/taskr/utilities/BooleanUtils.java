package com.taskr.utilities;

/**
 * Created by SoftwareSpot on 3.3.2015.
 */
public class BooleanUtils {
    public static final boolean BOOLEAN_FALSE = false;
    public static final boolean BOOLEAN_TRUE = true;

    /**
     * Convert an integer value (non-zero) to a boolean representation
     *
     * @param value to convert
     * @return Boolean value of either true or false
     */
    public static boolean intToBoolean(int value) {
        return value != IntegerUtils.INTEGER_FALSE;
    }
}

package com.taskr.utilities;

/**
 * Created by SoftwareSpot on 24/02/2015.
 */
public class SerializationUtils {
    /*
    // Sample use:
    // Set:
    String dbValue = putSerializable(Suit.SPADE);
    // Get:
    Suit suit = getSerializable(Suit.class, dbValue);
    */

    /**
     * @param enumType Enumeration type e.g. Suit.class
     * @param value    Value to parse
     * @return Enumeration type
     */
    public static <E extends Enum<E>> E getSerializable(Class<E> enumType, String value) {
        return StringUtils.isNullOrEmpty(value) ? Enum.valueOf(enumType, "NONE") : Enum.valueOf(enumType, value);
    }

    /**
     * @param enumValue Enumeration value to convert to a string representation
     * @return String representation of an enumeration value
     */
    public static String putSerializable(Enum<?> enumValue) {
        return ObjectUtils.isNull(enumValue) ? StringUtils.EMPTY : enumValue.name();
    }
}

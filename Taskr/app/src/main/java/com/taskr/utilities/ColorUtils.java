package com.taskr.utilities;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by SoftwareSpot on 07/04/2015.
 */
public class ColorUtils {
    private static final byte sColor_Length = 6;
    private static final short sColor_Range = 255;

    public static int getColor(int red, int green, int blue) {
        return Color.rgb(red, green, blue);
    }

    public static int getColor(String color) {
        try {
            return Color.parseColor(color);
        } catch (IllegalArgumentException ignored) {
            return Color.GREEN;
        }
    }

    /**
     * Get a random color
     *
     * @return Random color
     */
    public static int getRandomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(sColor_Range), random.nextInt(sColor_Range), random.nextInt(sColor_Range));
    }

    public static int getRandomColor(int value) {
        String color = value + "";
        if (color.length() > sColor_Length) { // Length of webcolor
            color = color.substring(0, sColor_Length); // First 6 characters
        } else if (color.length() < sColor_Length) {
            color = String.format("%-" + sColor_Length + "d", color).replace(' ', '0');
        }
        return getColor(color);
    }
}
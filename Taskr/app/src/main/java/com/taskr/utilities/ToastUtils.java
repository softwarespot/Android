package com.taskr.utilities;

import android.app.Activity;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by SoftwareSpot on 27.2.2015.
 */
public class ToastUtils {
    private static Style sConfirm = new Style.Builder()
            .setBackgroundColorValue(ColorUtils.getColor("#27ae60"))
            .build();
    private static Style sError = new Style.Builder()
            .setBackgroundColorValue(ColorUtils.getColor("#e74c3c"))
            .build();
    private static Style sWarning = new Style.Builder()
            .setBackgroundColorValue(ColorUtils.getColor("#e67e22"))
            .build();

    public static void toastError(Activity activity, CharSequence text) {
        Crouton.makeText(activity, text, sError).show();
    }

    public static void toastOK(Activity activity, CharSequence text) {
        Crouton.makeText(activity, text, sConfirm).show();
    }

    public static void toastWarning(Activity activity, CharSequence text) {
        Crouton.makeText(activity, text, sWarning).show();
    }
}

package com.scholarlyapps.pathlingo.ui.utils;

import android.app.Activity;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ToastHelper {

    public static void success(Activity activity, String message) {
        MotionToast.Companion.createColorToast(
                activity, "Success", message,
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                null);
    }

    public static void successLong(Activity activity, String message) {
        MotionToast.Companion.createColorToast(
                activity, "Success", message,
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null);
    }

    public static void error(Activity activity, String message) {
        MotionToast.Companion.createColorToast(
                activity, "Error", message,
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null);
    }

    public static void errorShort(Activity activity, String message) {
        MotionToast.Companion.createColorToast(
                activity, "Error", message,
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                null);
    }

    public static void warning(Activity activity, String message) {
        MotionToast.Companion.createColorToast(
                activity, "Warning", message,
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                null);
    }

    public static void warningLong(Activity activity, String message) {
        MotionToast.Companion.createColorToast(
                activity, "Warning", message,
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null);
    }
}

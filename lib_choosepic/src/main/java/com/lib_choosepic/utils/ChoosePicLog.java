package com.lib_choosepic.utils;

import android.util.Log;

public class ChoosePicLog {
    public static boolean SHOW_LOG = false;
    private static String LOG_TAG = "ChoosePic";

    public static void i(String logMsg) {
        if (SHOW_LOG) {
            Log.i(LOG_TAG, logMsg);
        }
    }

    public static void e(String logMsg) {
        if (SHOW_LOG) {
            Log.e(LOG_TAG, logMsg);
        }
    }
}
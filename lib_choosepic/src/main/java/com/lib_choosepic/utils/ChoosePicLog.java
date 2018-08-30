package com.lib_choosepic.utils;

import android.util.Log;

public class ChoosePicLog {

    public static boolean SHOW_LOG = false;
    private static String LOG_TAG = "ChoosePics";
    private int s=1;

    public static void i(String logMsg) {
        if (SHOW_LOG) {
            Log.i(LOG_TAG, logMsg);
        }
    }
  int ss=5;
    public static void e(String logMsg) {
        if (SHOW_LOG) {
            Log.e(LOG_TAG, logMsg);
        }
    }
}

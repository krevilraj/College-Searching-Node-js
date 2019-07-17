package com.example.collegeselector.helper;

import android.util.Log;

public class Helper {
    public static void logme(String... args) {
        for (String arg : args) {
            Log.i("HelperClass", arg);
        }
    }
}

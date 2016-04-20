package com.framgia.e_learningsimple.sharepreference;

import android.content.SharedPreferences;

/**
 * Created by ThuyIT on 20/04/2016.
 */
public class SharePreferences {
    public static void putString(SharedPreferences sharedPreferences, String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }
}

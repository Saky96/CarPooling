package com.example.parleen.carpooling.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by parleen on 7/28/2017.
 */

public class SharedPref {
    private final SharedPreferences pref;
    public static SharedPref mInstance;

    public SharedPref(Context context) {
        mInstance = this;
        pref = context.getSharedPreferences("PoolMyCar", 0);
    }

    public static SharedPref getInstance() {

        return mInstance;
    }

    public void setString(String name, String value) {
        pref.edit().putString(name, value).commit();
    }

    public String getString(String name) {
        return pref.getString(name, "");
    }
}

package com.example.parleen.carpooling;

import android.app.Application;

import com.example.parleen.carpooling.Utils.SharedPref;

/**
 * Created by parleen on 7/28/2017.
 */

public class AppApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        new SharedPref(this);
    }
}

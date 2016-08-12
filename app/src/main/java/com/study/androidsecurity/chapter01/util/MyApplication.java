package com.study.androidsecurity.chapter01.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by 46358_000 on 2016/8/5 0005.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }

    public static Context getContext() {
        return context;
    }
}

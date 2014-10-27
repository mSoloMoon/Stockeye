package com.msolo.stockeye;

import android.app.Application;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.msolo.stockeye.gui.activity.ActivityMain;

/**
 *
 * @author msolo (yuezhi.msolo@outlook.com)
 * Created by mSolo on 2014/8/7.
 *
 */
public final class StockeyeApp extends Application
{

    public static Context appContext = null;
    public static ContentResolver appContentResolver = null;
    public static SharedPreferences appSharedPref = null;
    public static boolean isLargeScreen = false;
    public static int appWidth = 0;
    public static int appHeight = 0;

    public static PendingIntent activityMainPendingIntent;

    @Override
    public void onCreate() {

        super.onCreate();
        init();

    }

    /**
     *
     *  initialization
     *  1. global app objects
     *  2. global app data
     *
     */
    private void init() {

        appContext = getApplicationContext();
        appContentResolver = getContentResolver();
        appSharedPref = getSharedPreferences("stockeye_sharedpref", Context.MODE_PRIVATE);

        appWidth = getWallpaperDesiredMinimumWidth();
        appHeight = getWallpaperDesiredMinimumHeight();

        isLargeScreen = appHeight > 1280 ? true : false;

        activityMainPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, ActivityMain.class), 0);

    }

}

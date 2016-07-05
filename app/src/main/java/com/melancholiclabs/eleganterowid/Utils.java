package com.melancholiclabs.eleganterowid;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by Melancoholic on 7/1/2016.
 */
public class Utils {

    public static final int THEME_DEFAULT = 0;
    public static final int THEME_EXTRA = 1;
    public static int sTheme;

    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.AppTheme);
                break;
            case THEME_EXTRA:
                activity.setTheme(R.style.ExtraTheme);
                break;
        }
    }

    /**
     * Stops an AsyncTask after checking that it is not null and is currently runnning.
     *
     * @param task AsyncTask to be stopped
     */
    public static void gracefullyStop(AsyncTask task) {
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) task.cancel(true);
    }
}

package com.easymanager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

public class ThemeUtils {
    private static final String PREF_NAME = "theme_prefs";
    private static final String KEY_THEME_MODE = "theme_mode";

    public static final int MODE_SYSTEM = 0;
    public static final int MODE_LIGHT = 1;
    public static final int MODE_DARK = 2;

    public static void applyTheme(Activity activity) {
        int mode = getThemeMode(activity);
        Configuration config = new Configuration(activity.getResources().getConfiguration());
        int currentNightMode = config.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        
        int targetNightMode;
        if (mode == MODE_LIGHT) {
            targetNightMode = Configuration.UI_MODE_NIGHT_NO;
        } else if (mode == MODE_DARK) {
            targetNightMode = Configuration.UI_MODE_NIGHT_YES;
        } else {
            // 跟随系统模式：获取系统真实的夜间模式状态
            // 通过 ApplicationContext 获取，因为它不受当前 Activity 覆盖的影响
            Configuration systemConfig = activity.getApplicationContext().getResources().getConfiguration();
            targetNightMode = systemConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        }

        if (currentNightMode != targetNightMode) {
            config.uiMode = (config.uiMode & ~Configuration.UI_MODE_NIGHT_MASK) | targetNightMode;
            activity.getResources().updateConfiguration(config, activity.getResources().getDisplayMetrics());
        }
    }

    public static int getThemeMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_THEME_MODE, MODE_SYSTEM);
    }

    public static void setThemeMode(Context context, int mode) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_THEME_MODE, mode).apply();
    }
}

package com.easymanager.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.easymanager.R;

import com.easymanager.utils.ThemeUtils;

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applyTheme(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 重启 Activity 以应用资源更改（如黑暗模式切换）
        recreate();
    }
}

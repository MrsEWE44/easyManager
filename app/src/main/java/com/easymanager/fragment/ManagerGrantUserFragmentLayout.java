package com.easymanager.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.utils.easyManagerUtils;

public class ManagerGrantUserFragmentLayout extends Fragment {

    private Boolean isRoot, isADB, isDevice;
    private TextView status_title, status_description, service_status_text, working_mode_text;
    private ImageView status_icon;

    public ManagerGrantUserFragmentLayout(Boolean isRoot, Boolean isADB, Boolean isDevice) {
        this.isRoot = isRoot;
        this.isADB = isADB;
        this.isDevice = isDevice;
    }

    public ManagerGrantUserFragmentLayout(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vie = inflater.inflate(R.layout.manager_grant_user_fragment_layout,container,false);
        initView(vie);
        updateStatusDisplay();
        return vie;
    }

    private void initView(View vie) {
        status_title = vie.findViewById(R.id.status_title);
        status_description = vie.findViewById(R.id.status_description);
        service_status_text = vie.findViewById(R.id.service_status_text);
        working_mode_text = vie.findViewById(R.id.working_mode_text);
        status_icon = vie.findViewById(R.id.status_icon);
    }

    private void updateStatusDisplay() {
        easyManagerUtils ee = new easyManagerUtils();
        boolean serverRunning = ee.getServerStatus();
        
        // 更新服务运行状态文字
        if (serverRunning) {
            service_status_text.setText(getString(R.string.status_online));
            service_status_text.setTextColor(android.graphics.Color.parseColor("#4CAF50")); // Green
        } else {
            service_status_text.setText(getString(R.string.status_offline));
            service_status_text.setTextColor(android.graphics.Color.parseColor("#F44336")); // Red
        }

        // 更新核心模式状态
        if (isRoot != null && isRoot) {
            status_title.setText(getString(R.string.mode_root));
            status_description.setText(getString(R.string.desc_root));
            working_mode_text.setText(getString(R.string.mode_root));
            status_icon.setImageResource(R.drawable.manager_grant_app_foreground);
        } else if (isADB != null && isADB) {
            status_title.setText(getString(R.string.mode_adb));
            status_description.setText(getString(R.string.desc_adb));
            working_mode_text.setText(getString(R.string.mode_adb));
            status_icon.setImageResource(R.drawable.manager_grant_app_foreground);
        } else if (isDevice != null && isDevice) {
            status_title.setText(getString(R.string.mode_device_owner));
            status_description.setText(getString(R.string.desc_device_owner));
            working_mode_text.setText(getString(R.string.mode_device_owner));
            status_icon.setImageResource(R.drawable.manager_grant_app_foreground);
        } else {
            status_title.setText(getString(R.string.mode_general));
            status_description.setText(getString(R.string.desc_general));
            working_mode_text.setText(getString(R.string.mode_general));
            status_icon.setImageResource(R.drawable.manager_grant_app_foreground);
        }
    }

    public void updateStatus(Boolean isRoot, Boolean isADB, Boolean isDevice) {
        this.isRoot = isRoot;
        this.isADB = isADB;
        this.isDevice = isDevice;
        if (getView() != null) {
            updateStatusDisplay();
        }
    }
}

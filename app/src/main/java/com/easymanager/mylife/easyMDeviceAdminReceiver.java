package com.easymanager.mylife;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class easyMDeviceAdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
//        System.out.println("easyMDeviceAdminReceiver is enabled ....");

    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
//        System.out.println("easyMDeviceAdminReceiver is disabled ....");
    }
}

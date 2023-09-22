package com.easymanager.core.enums;

import android.app.AppOpsManager;

import java.util.ArrayList;

public class AppopsPermissionStr {

    public void getPhoneAndSMSPermissonStr(ArrayList<String> ops){
        ops.add(AppOpsManager.OPSTR_READ_PHONE_STATE);
        ops.add(AppOpsManager.OPSTR_READ_CONTACTS);
        ops.add(AppOpsManager.OPSTR_WRITE_CONTACTS);
        ops.add(AppOpsManager.OPSTR_READ_CALL_LOG);
        ops.add(AppOpsManager.OPSTR_WRITE_CALL_LOG);
        ops.add(AppOpsManager.OPSTR_CALL_PHONE);
        ops.add(AppOpsManager.OPSTR_READ_SMS);
        ops.add("android:write_sms");
        ops.add(AppOpsManager.OPSTR_SEND_SMS);
        ops.add(AppOpsManager.OPSTR_RECEIVE_SMS);
        ops.add("RECEIVE_EMERGECY_SMS");
        ops.add(AppOpsManager.OPSTR_RECEIVE_MMS);
        ops.add(AppOpsManager.OPSTR_RECEIVE_WAP_PUSH);
        ops.add("android:read_icc_sms");
        ops.add("android:write_icc_sms");
        ops.add(AppOpsManager.OPSTR_PROCESS_OUTGOING_CALLS);
        ops.add("android:read_cell_broadcasts");
        ops.add(AppOpsManager.OPSTR_ADD_VOICEMAIL);
        ops.add(AppOpsManager.OPSTR_ANSWER_PHONE_CALLS);
        ops.add(AppOpsManager.OPSTR_READ_CELL_BROADCASTS);
        ops.add(AppOpsManager.OPSTR_READ_PHONE_NUMBERS);
        ops.add(AppOpsManager.OPSTR_READ_PHONE_STATE);
    }


    public void getStoragePermissionStr(ArrayList<String> ops){
        ops.add(AppOpsManager.OPSTR_READ_EXTERNAL_STORAGE);
        ops.add(AppOpsManager.OPSTR_WRITE_EXTERNAL_STORAGE);
        ops.add("android:access_media_location");
        ops.add("android:legacy_storage");
        ops.add("android:write_media_audio");
        ops.add("android:read_media_audio");
        ops.add("android:read_media_video");
        ops.add("android:write_media_video");
        ops.add("android:read_media_images");
        ops.add("android:write_media_images");
        ops.add("android:manage_external_storage");
        ops.add(AppOpsManager.OPSTR_PICTURE_IN_PICTURE);
    }

    public void getClipboardPermissionStr(ArrayList<String> ops){
        ops.add("android:read_clipboard");
        ops.add("android:write_clipboard");
    }

    public void getRunAnyInBackgroudPermissionStr(ArrayList<String> ops){
        ops.add("android:run_any_in_background");
    }

    public void getRunInBackgroudPermissionStr(ArrayList<String> ops){
        ops.add("android:run_in_background");
    }

    public void getCameraAndAudioPermissionStr(ArrayList<String> ops){
        ops.add(AppOpsManager.OPSTR_CAMERA);
        ops.add("android:phone_call_camera");
        ops.add(AppOpsManager.OPSTR_RECORD_AUDIO);
        ops.add("android:take_audio_focus");
        ops.add("android:record_audio_hotword");
        ops.add("android:record_audio_output");
        ops.add("android:record_incoming_phone_audio");
        ops.add("android:receive_ambient_trigger_audio");
    }

    public void getLocationPermissionStr(ArrayList<String> ops){
        ops.add(AppOpsManager.OPSTR_COARSE_LOCATION);
        ops.add(AppOpsManager.OPSTR_FINE_LOCATION);
        ops.add(AppOpsManager.OPSTR_MOCK_LOCATION);
        ops.add(AppOpsManager.OPSTR_MONITOR_HIGH_POWER_LOCATION);
        ops.add(AppOpsManager.OPSTR_MONITOR_LOCATION);
    }

    public void getCalendarPermissionStr(ArrayList<String> ops){
        ops.add(AppOpsManager.OPSTR_READ_CALENDAR);
        ops.add(AppOpsManager.OPSTR_WRITE_CALENDAR);
    }

    public void getSenSorsScanPermissionStr(ArrayList<String> ops){
        ops.add(AppOpsManager.OPSTR_USE_SIP);
        ops.add("android:wifi_scan");
        ops.add("android:bluetooth_scan");
        ops.add("android:bluetooth_advertise");
        ops.add("android:bluetooth_connect");
        ops.add("android:audio_bluetooth_volume");
        ops.add(AppOpsManager.OPSTR_BODY_SENSORS);
        ops.add("android:body_sensors");
    }

    public void getNotificationPermissionStr(ArrayList<String> ops){
        ops.add("android:access_notifications");
        ops.add("android:post_notification");
    }

}

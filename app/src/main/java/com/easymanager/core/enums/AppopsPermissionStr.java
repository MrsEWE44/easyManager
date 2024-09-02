package com.easymanager.core.enums;

import android.Manifest;
import android.app.AppOpsManager;

import java.util.ArrayList;

public class AppopsPermissionStr {

    public final static int PhoneAndSMS = 0;
    public final static int Storage = 1;
    public final static int Clipboard = 2;
    public final static int RunAnyInBackgroud = 3;
    public final static int RunInBackgroud = 4;
    public final static int CameraAndAudio = 5;
    public final static int Location = 6;
    public final static int Calendar = 7;
    public final static int SENSORSSCAN = 8;
    public final static int Notification = 9;
    public final static int Fingerprint = 10;
    public final static int AlertWindow = 11;
    public final static int Accessibility = 12;
    public final static int Account = 13;
    public final static int WriteSettings = 14;
    public final static int DeviceIdentifiers = 15;
    
    public void getPhoneAndSMSPermissonStr2(ArrayList<String> ops){
        ops.add(Manifest.permission.CALL_PHONE);
        ops.add(Manifest.permission.FOREGROUND_SERVICE_MICROPHONE);
        ops.add(Manifest.permission.MODIFY_PHONE_STATE);
        ops.add(Manifest.permission.ANSWER_PHONE_CALLS);
        ops.add(Manifest.permission.READ_BASIC_PHONE_STATE);
        ops.add(Manifest.permission.FOREGROUND_SERVICE_PHONE_CALL);
        ops.add(Manifest.permission.READ_PHONE_NUMBERS);
        ops.add(Manifest.permission.MANAGE_DEVICE_POLICY_MICROPHONE);
        ops.add(Manifest.permission.READ_PRECISE_PHONE_STATE);
        ops.add(Manifest.permission.READ_PHONE_STATE);
        ops.add(Manifest.permission.READ_CONTACTS);
        ops.add(Manifest.permission.WRITE_CONTACTS);
        ops.add(Manifest.permission.CALL_PHONE);
        ops.add(Manifest.permission.READ_CALL_LOG);
        ops.add(Manifest.permission.WRITE_CALL_LOG);
        ops.add(Manifest.permission.SMS_FINANCIAL_TRANSACTIONS);
        ops.add(Manifest.permission.BROADCAST_SMS);
        ops.add(Manifest.permission.SEND_SMS);
        ops.add(Manifest.permission.READ_SMS);
        ops.add(Manifest.permission.RECEIVE_SMS);
        ops.add(Manifest.permission.MANAGE_DEVICE_POLICY_DEFAULT_SMS);
        ops.add(Manifest.permission.MANAGE_DEVICE_POLICY_SMS);
        ops.add(Manifest.permission.RECEIVE_MMS);
        ops.add(Manifest.permission.RECEIVE_WAP_PUSH);
        ops.add(Manifest.permission.BROADCAST_WAP_PUSH);
    }

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

    public void getStoragePermissionStr2(ArrayList<String> ops){
        ops.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        ops.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ops.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        ops.add(Manifest.permission.MEDIA_CONTENT_CONTROL);
        ops.add(Manifest.permission.READ_MEDIA_AUDIO);
        ops.add(Manifest.permission.READ_MEDIA_IMAGES);
        ops.add(Manifest.permission.READ_MEDIA_VIDEO);
        ops.add(Manifest.permission.MANAGE_MEDIA);
        ops.add(Manifest.permission.ACCESS_MEDIA_LOCATION);
        ops.add(Manifest.permission.MANAGE_DEVICE_POLICY_PHYSICAL_MEDIA);
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

    public void getCameraAndAudioPermissionStr2(ArrayList<String> ops){
        ops.add(Manifest.permission.CAMERA);
        ops.add(Manifest.permission.FOREGROUND_SERVICE_CAMERA);
        ops.add(Manifest.permission.MANAGE_DEVICE_POLICY_CAMERA);
        ops.add(Manifest.permission.RECORD_AUDIO);
        ops.add(Manifest.permission.CAPTURE_AUDIO_OUTPUT);
        ops.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
        ops.add(Manifest.permission.MANAGE_DEVICE_POLICY_AUDIO_OUTPUT);
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

    public void getLocationPermissionStr2(ArrayList<String> ops){
        ops.add(Manifest.permission.LOCATION_HARDWARE);
        ops.add(Manifest.permission.ACCESS_MEDIA_LOCATION);
        ops.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        ops.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        ops.add(Manifest.permission.ACCESS_FINE_LOCATION);
        ops.add(Manifest.permission.FOREGROUND_SERVICE_LOCATION);
        ops.add(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        ops.add(Manifest.permission.MANAGE_DEVICE_POLICY_LOCATION);
        ops.add(Manifest.permission.CONTROL_LOCATION_UPDATES);
        ops.add(Manifest.permission.INSTALL_LOCATION_PROVIDER);
    }

    public void getLocationPermissionStr(ArrayList<String> ops){
        ops.add(AppOpsManager.OPSTR_COARSE_LOCATION);
        ops.add(AppOpsManager.OPSTR_FINE_LOCATION);
        ops.add(AppOpsManager.OPSTR_MOCK_LOCATION);
        ops.add(AppOpsManager.OPSTR_MONITOR_HIGH_POWER_LOCATION);
        ops.add(AppOpsManager.OPSTR_MONITOR_LOCATION);
        ops.add(AppOpsManager.OPSTR_GPS);
    }

    public void getCalendarPermissionStr2(ArrayList<String> ops){
        ops.add(Manifest.permission.READ_CALENDAR);
        ops.add(Manifest.permission.WRITE_CALENDAR);
    }

    public void getCalendarPermissionStr(ArrayList<String> ops){
        ops.add(AppOpsManager.OPSTR_READ_CALENDAR);
        ops.add(AppOpsManager.OPSTR_WRITE_CALENDAR);
    }

    public void getSenSorsScanPermissionStr2(ArrayList<String> ops){
        ops.add(Manifest.permission.MANAGE_DEVICE_POLICY_WIFI);
        ops.add(Manifest.permission.BLUETOOTH_SCAN);
        ops.add(Manifest.permission.BLUETOOTH_ADVERTISE);
        ops.add(Manifest.permission.USE_SIP);
        ops.add(Manifest.permission.ACCESS_WIFI_STATE);
        ops.add(Manifest.permission.CHANGE_WIFI_STATE);
        ops.add(Manifest.permission.CHANGE_WIFI_MULTICAST_STATE);
        ops.add(Manifest.permission.CONFIGURE_WIFI_DISPLAY);
        ops.add(Manifest.permission.MANAGE_WIFI_INTERFACES);
        ops.add(Manifest.permission.MANAGE_WIFI_NETWORK_SELECTION);
        ops.add(Manifest.permission.NEARBY_WIFI_DEVICES);
        ops.add(Manifest.permission.OVERRIDE_WIFI_CONFIG);
        ops.add(Manifest.permission.BLUETOOTH);
        ops.add(Manifest.permission.BLUETOOTH_CONNECT);
        ops.add(Manifest.permission.BLUETOOTH_PRIVILEGED);
        ops.add(Manifest.permission.MANAGE_DEVICE_POLICY_BLUETOOTH);
        ops.add(Manifest.permission.BLUETOOTH_ADMIN);
        ops.add(Manifest.permission.BODY_SENSORS);
        ops.add(Manifest.permission.BODY_SENSORS_BACKGROUND);
        ops.add(Manifest.permission.HIGH_SAMPLING_RATE_SENSORS);
        ops.add(Manifest.permission.VIBRATE);
        ops.add(Manifest.permission.ACTIVITY_RECOGNITION);

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
        ops.add("android:body_sensors_background");
        ops.add(AppOpsManager.OPSTR_ACTIVITY_RECOGNITION);
        ops.add("android:activity_recognition");
        ops.add(AppOpsManager.OPSTR_VIBRATE);
    }

    public void getNotificationPermissionStr2(ArrayList<String> ops){
        ops.add(Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE);
        ops.add(Manifest.permission.POST_NOTIFICATIONS);
        ops.add(Manifest.permission.ACCESS_NOTIFICATION_POLICY);

    }

    public void getNotificationPermissionStr(ArrayList<String> ops){
        ops.add("android:access_notifications");
        ops.add("android:post_notification");
    }

    public void getFigerprintPermissionStr2(ArrayList<String> ops) {
        ops.add(Manifest.permission.USE_FINGERPRINT);
        ops.add(Manifest.permission.USE_BIOMETRIC);
    }

    public void getFigerprintPermissionStr(ArrayList<String> ops) {
        ops.add(AppOpsManager.OPSTR_USE_FINGERPRINT);
        ops.add(AppOpsManager.OPSTR_USE_BIOMETRIC);
    }

    public void getAlertWindowPermissionStr2(ArrayList<String> ops) {
        ops.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
        ops.add(Manifest.permission.HIDE_OVERLAY_WINDOWS);
        ops.add(Manifest.permission.MANAGE_DEVICE_POLICY_WINDOWS);
    }

    public void getAlertWindowPermissionStr(ArrayList<String> ops) {
        ops.add(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW);
        ops.add(AppOpsManager.OPSTR_TOAST_WINDOW);
    }

    public void getAccessibilityPermissionStr2(ArrayList<String> ops) {
        ops.add(Manifest.permission.BIND_ACCESSIBILITY_SERVICE);
        ops.add(Manifest.permission.MANAGE_DEVICE_POLICY_ACCESSIBILITY);
    }

    public void getAccessibilityPermissionStr(ArrayList<String> ops) {
        ops.add(AppOpsManager.OPSTR_AUDIO_ACCESSIBILITY_VOLUME);
        ops.add(AppOpsManager.OPSTR_BIND_ACCESSIBILITY_SERVICE);
        ops.add(AppOpsManager.OPSTR_ACCESS_ACCESSIBILITY);
    }

    public void getAccountPermissionStr2(ArrayList<String> ops) {
        ops.add(Manifest.permission.GET_ACCOUNTS);
        ops.add(Manifest.permission.ACCOUNT_MANAGER);
        ops.add(Manifest.permission.GET_ACCOUNTS_PRIVILEGED);
        ops.add(Manifest.permission.MANAGE_DEVICE_POLICY_ACCOUNT_MANAGEMENT);
    }

    public void getAccountPermissionStr(ArrayList<String> ops) {
        ops.add(AppOpsManager.OPSTR_GET_ACCOUNTS);
    }

    public void getWriteSettingsPermissionStr2(ArrayList<String> ops) {
        ops.add(Manifest.permission.WRITE_SETTINGS);
    }

    public void getWriteSettingsPermissionStr(ArrayList<String> ops) {
        ops.add(AppOpsManager.OPSTR_WRITE_SETTINGS);
    }

    public void getDeviceIdentifiersPermissionStr2(ArrayList<String> ops) {
        ops.add(Manifest.permission.MANAGE_DEVICE_POLICY_DEVICE_IDENTIFIERS);
        ops.add(Manifest.permission.USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER);
        ops.add(Manifest.permission.MANAGE_DEVICE_POLICY_ORGANIZATION_IDENTITY);
    }

    public void getDeviceIdentifiersPermissionStr(ArrayList<String> ops) {
        ops.add(AppOpsManager.OPSTR_READ_DEVICE_IDENTIFIERS);
    }

    public int getAppopsMode(int APP_PERMIS_INDEX){
        if(APP_PERMIS_INDEX == 0){
            return PhoneAndSMS;
        }

        if(APP_PERMIS_INDEX == 1){
            return Storage;
        }

        if(APP_PERMIS_INDEX == 2){
            return Clipboard;
        }

        if(APP_PERMIS_INDEX == 3){
            return RunAnyInBackgroud;
        }

        if(APP_PERMIS_INDEX == 4){
            return RunInBackgroud;
        }

        if(APP_PERMIS_INDEX == 5){
            return CameraAndAudio;
        }

        if(APP_PERMIS_INDEX == 6){
            return Location;
        }

        if(APP_PERMIS_INDEX == 7){
            return Calendar;
        }

        if(APP_PERMIS_INDEX == 8){
            return SENSORSSCAN;
        }

        if(APP_PERMIS_INDEX == 9){
            return Notification;
        }

        if(APP_PERMIS_INDEX == 10){
            return Fingerprint;
        }

        if(APP_PERMIS_INDEX == 11){
            return AlertWindow;
        }

        if(APP_PERMIS_INDEX == 12){
            return Accessibility;
        }

        if(APP_PERMIS_INDEX == 13){
            return Account;
        }

        if(APP_PERMIS_INDEX == 14){
            return WriteSettings;
        }

        if(APP_PERMIS_INDEX == 15){
            return DeviceIdentifiers;
        }

        return -1;
    }

    public ArrayList<String> getOPS(int mode){
        ArrayList<String> ops = new ArrayList<>();
        if(mode == PhoneAndSMS){
            getPhoneAndSMSPermissonStr(ops);
        }

        if(mode == Storage){
            getStoragePermissionStr(ops);
        }

        if(mode == Clipboard){
            getClipboardPermissionStr(ops);
        }

        if(mode == RunAnyInBackgroud){
            getRunAnyInBackgroudPermissionStr(ops);
        }

        if(mode == RunInBackgroud){
            getRunInBackgroudPermissionStr(ops);
        }

        if(mode == CameraAndAudio){
            getCameraAndAudioPermissionStr(ops);
        }

        if(mode == Location){
            getLocationPermissionStr(ops);
        }

        if(mode == Calendar){
            getCalendarPermissionStr(ops);
        }

        if(mode == SENSORSSCAN){
            getSenSorsScanPermissionStr(ops);
        }

        if(mode == Notification){
            getNotificationPermissionStr(ops);
        }

        if(mode == Fingerprint){
            getFigerprintPermissionStr(ops);
        }
        if(mode == AlertWindow){
            getAlertWindowPermissionStr(ops);
        }

        if(mode == Accessibility){
            getAccessibilityPermissionStr(ops);
        }

        if(mode == Account){
            getAccountPermissionStr(ops);
        }

        if(mode == WriteSettings){
            getWriteSettingsPermissionStr(ops);
        }

        if(mode == DeviceIdentifiers){
            getDeviceIdentifiersPermissionStr(ops);
        }

        return ops;
    }

    public ArrayList<String> getOPS2(int mode){
        ArrayList<String> ops = new ArrayList<>();
        if(mode == PhoneAndSMS){
            getPhoneAndSMSPermissonStr2(ops);
        }

        if(mode == Storage){
            getStoragePermissionStr2(ops);
        }

        if(mode == Clipboard){
            getClipboardPermissionStr(ops);
        }

        if(mode == RunAnyInBackgroud){
            getRunAnyInBackgroudPermissionStr(ops);
        }

        if(mode == RunInBackgroud){
            getRunInBackgroudPermissionStr(ops);
        }

        if(mode == CameraAndAudio){
            getCameraAndAudioPermissionStr2(ops);
        }

        if(mode == Location){
            getLocationPermissionStr2(ops);
        }

        if(mode == Calendar){
            getCalendarPermissionStr2(ops);
        }

        if(mode == SENSORSSCAN){
            getSenSorsScanPermissionStr2(ops);
        }

        if(mode == Notification){
            getNotificationPermissionStr2(ops);
        }

        if(mode == Fingerprint){
            getFigerprintPermissionStr2(ops);
        }
        if(mode == AlertWindow){
            getAlertWindowPermissionStr2(ops);
        }

        if(mode == Accessibility){
            getAccessibilityPermissionStr2(ops);
        }

        if(mode == Account){
            getAccountPermissionStr2(ops);
        }

        if(mode == WriteSettings){
            getWriteSettingsPermissionStr2(ops);
        }

        if(mode == DeviceIdentifiers){
            getDeviceIdentifiersPermissionStr2(ops);
        }

        return ops;
    }


}

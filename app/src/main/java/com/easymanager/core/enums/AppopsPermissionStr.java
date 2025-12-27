package com.easymanager.core.enums;

import android.Manifest;
import android.app.AppOpsManager;
import android.os.Build;

import java.util.ArrayList;

public class AppopsPermissionStr {

    private int sdk = Build.VERSION.SDK_INT;
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

    public void getPhoneAndSMSPermissonStr2(ArrayList<String> perms){
        // ===== 电话基础 =====
        perms.add("android.permission.READ_PHONE_STATE");
        perms.add("android.permission.CALL_PHONE");

        // ===== Android 8+ 新增 =====
        if (sdk >= 26) { // O
            perms.add("android.permission.ANSWER_PHONE_CALLS");
            perms.add("android.permission.READ_PHONE_NUMBERS");
        }

        // ===== Voicemail / SIP =====
        perms.add("android.permission.ADD_VOICEMAIL");
        perms.add("android.permission.USE_SIP");

        // ===== 通话记录 =====
        perms.add("android.permission.READ_CALL_LOG");
        perms.add("android.permission.WRITE_CALL_LOG");

        // ===== 联系人 =====
        perms.add("android.permission.READ_CONTACTS");
        perms.add("android.permission.WRITE_CONTACTS");
        perms.add("android.permission.GET_ACCOUNTS"); // 可公开
        perms.add("android.permission.USE_CREDENTIALS"); // 普通账户权限

        // ===== 短信 =====
        perms.add("android.permission.SEND_SMS");
        perms.add("android.permission.RECEIVE_SMS");
        perms.add("android.permission.READ_SMS");
        perms.add("android.permission.RECEIVE_WAP_PUSH");
        perms.add("android.permission.RECEIVE_MMS");

        // ===== Android 10+ =====
        if (sdk >= 29) {
            perms.add("android.permission.ACCEPT_HANDOVER");
        }

        // ===== Android 13+ =====
        if (sdk >= 33) {
            perms.add("android.permission.POST_NOTIFICATION");
        }
    }

    public void getPhoneAndSMSPermissonStr(ArrayList<String> ops){
        if (sdk >= 18) { // Android 4.3+
            // 电话
            ops.add(AppOpsManager.OPSTR_READ_PHONE_STATE);
            ops.add(AppOpsManager.OPSTR_CALL_PHONE);
            ops.add(AppOpsManager.OPSTR_ANSWER_PHONE_CALLS);
            ops.add(AppOpsManager.OPSTR_READ_PHONE_NUMBERS);

            // 联系人
            ops.add(AppOpsManager.OPSTR_READ_CONTACTS);
            ops.add(AppOpsManager.OPSTR_WRITE_CONTACTS);

            // 通话记录
            ops.add(AppOpsManager.OPSTR_READ_CALL_LOG);
            ops.add(AppOpsManager.OPSTR_WRITE_CALL_LOG);

            // 短信
            ops.add(AppOpsManager.OPSTR_READ_SMS);
            ops.add(AppOpsManager.OPSTR_SEND_SMS);
            ops.add(AppOpsManager.OPSTR_RECEIVE_SMS);
            ops.add(AppOpsManager.OPSTR_RECEIVE_MMS);
            ops.add(AppOpsManager.OPSTR_RECEIVE_WAP_PUSH);
            ops.add("android:read_icc_sms");
            ops.add("android:write_icc_sms");

            // Voicemail
            ops.add(AppOpsManager.OPSTR_ADD_VOICEMAIL);

            // Cell Broadcast
            ops.add(AppOpsManager.OPSTR_READ_CELL_BROADCASTS);
            ops.add("android:read_cell_broadcasts");
        }
    }

    public void getStoragePermissionStr2(ArrayList<String> perms){
        // ===== Android 4.x ~ 5.x 基础存储 =====
        if (sdk < 23) { // 6.0 前 install-time
            perms.add("android.permission.READ_EXTERNAL_STORAGE");
            perms.add("android.permission.WRITE_EXTERNAL_STORAGE");
        } else { // 6.0+ runtime
            perms.add("android.permission.READ_EXTERNAL_STORAGE");
            perms.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }

        // ===== Android 10+ 分区存储权限 =====
        if (sdk >= 29) {
            perms.add("android.permission.MANAGE_EXTERNAL_STORAGE"); // 文件管理器权限
        }

        // ===== Android 13+ 精细媒体权限 =====
        if (sdk >= 33) {
            perms.add("android.permission.READ_MEDIA_IMAGES");
            perms.add("android.permission.READ_MEDIA_VIDEO");
            perms.add("android.permission.READ_MEDIA_AUDIO");
        }

        // ===== Android 16+ 新增存储权限 =====
        if (sdk >= 34) {
            perms.add("android.permission.ACCESS_SHARED_STORAGE");
            perms.add("android.permission.MANAGE_STORAGE");
            perms.add("android.permission.STORAGE_ACCESS_FRAMEWORK");
        }
    }

    public void getStoragePermissionStr(ArrayList<String> ops){
        // ===== Android 4.3+ AppOps =====
        if (sdk >= 18) {
            ops.add(AppOpsManager.OPSTR_READ_EXTERNAL_STORAGE);
            ops.add(AppOpsManager.OPSTR_WRITE_EXTERNAL_STORAGE);
        }

        // ===== Android 7.0+ 作用域 / 媒体定位 =====
        if (sdk >= 29) { // access_media_location 在 Android 10
            ops.add("android:access_media_location");
            ops.add("android:legacy_storage"); // scoped storage legacy mode
        }

        // ===== Android 13+ 精细媒体 =====
        if (sdk >= 33) {
            ops.add("android:read_media_images");
            ops.add("android:write_media_images");
            ops.add("android:read_media_video");
            ops.add("android:write_media_video");
            ops.add("android:read_media_audio");
            ops.add("android:write_media_audio");
        }

        // ===== Android 10+ 文件管理器权限 AppOps =====
        if (sdk >= 29) {
            ops.add("android:manage_external_storage");
        }
    }

    public void getClipboardPermissionStr2(ArrayList<String> ops){
        ops.add("android.permission.MANAGE_CLIPBOARD");
    }

    public void getClipboardPermissionStr(ArrayList<String> ops){
        ops.add(AppOpsManager.OPSTR_READ_CLIPBOARD);
        ops.add(AppOpsManager.OPSTR_WRITE_CLIPBOARD);
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
        ops.add("android.permission.CAMERA");
        ops.add("android.permission.RECORD_AUDIO");
    }

    public void getCameraAndAudioPermissionStr(ArrayList<String> ops){
        // ===== Android 4.3+ =====
        if (Build.VERSION.SDK_INT >= 18) { // JELLY_BEAN_MR2
            ops.add("android:camera");
            ops.add("android:record_audio");
        }

        // ===== Android 12+ 隐私总开关 =====
        if (Build.VERSION.SDK_INT >= 31) { // S
            ops.add("android:microphone"); // 麦克风隐私开关
            // android:camera 已同时承担隐私开关
        }
    }

    public void getLocationPermissionStr2(ArrayList<String> perms){
        // ===== 基础定位 =====
        perms.add("android.permission.ACCESS_FINE_LOCATION");
        perms.add("android.permission.ACCESS_COARSE_LOCATION");

        // ===== 模拟位置 =====
        perms.add("android.permission.ACCESS_MOCK_LOCATION");

        // ===== Android 10+ 后台定位 =====
        if (sdk >= 29) {
            perms.add("android.permission.ACCESS_BACKGROUND_LOCATION");
            perms.add("android.permission.FOREGROUND_SERVICE_LOCATION");
        }

        // ===== Android 10+ 媒体定位 =====
        if (sdk >= 29) {
            perms.add("android.permission.ACCESS_MEDIA_LOCATION");
        }

        // ===== Android 10+ 活动识别 =====
        if (sdk >= 29) {
            perms.add("android.permission.ACTIVITY_RECOGNITION");
        }
        if (sdk >= 31) { // S
            perms.add("android.permission.ACTIVITY_RECOGNITION_BACKGROUND");
        }

        // ===== Android 12+ 蓝牙 / 附近设备 =====
        if (sdk >= 31) {
            perms.add("android.permission.BLUETOOTH_SCAN");
            perms.add("android.permission.BLUETOOTH_ADVERTISE");
            perms.add("android.permission.NEARBY_WIFI_DEVICES");
            perms.add("android.permission.UWB_RANGING");
        }
    }

    public void getLocationPermissionStr(ArrayList<String> ops){
        if (sdk >= 18) { // Android 4.3+
            ops.add(AppOpsManager.OPSTR_COARSE_LOCATION);
            ops.add(AppOpsManager.OPSTR_FINE_LOCATION);
            ops.add(AppOpsManager.OPSTR_MOCK_LOCATION);
            ops.add(AppOpsManager.OPSTR_MONITOR_HIGH_POWER_LOCATION);
            ops.add(AppOpsManager.OPSTR_MONITOR_LOCATION);
            ops.add(AppOpsManager.OPSTR_GPS);
        }
    }

    public void getCalendarPermissionStr2(ArrayList<String> perms){
        // ===== 基础日历读写 =====
        perms.add("android.permission.READ_CALENDAR");
        perms.add("android.permission.WRITE_CALENDAR");

        // ===== 日历账户访问 =====
        perms.add("android.permission.GET_ACCOUNTS"); // 访问日历账户用
    }

    public void getCalendarPermissionStr(ArrayList<String> ops){
        if (sdk >= 18) { // Android 4.3+
            ops.add(AppOpsManager.OPSTR_READ_CALENDAR);
            ops.add(AppOpsManager.OPSTR_WRITE_CALENDAR);
        }
    }

    public void getSenSorsScanPermissionStr2(ArrayList<String> perms){
        // ===== 身体传感器 =====
        perms.add("android.permission.BODY_SENSORS");
        if (sdk >= 31) {
            perms.add("android.permission.ACTIVITY_RECOGNITION_BACKGROUND");
        }
        perms.add("android.permission.ACTIVITY_RECOGNITION");

        // ===== 振动 =====
        perms.add("android.permission.VIBRATE");

        // ===== 蓝牙扫描 =====
        if (sdk >= 31) {
            perms.add("android.permission.BLUETOOTH_SCAN");
            perms.add("android.permission.BLUETOOTH_ADVERTISE");
            perms.add("android.permission.BLUETOOTH_CONNECT");
        }

        // ===== WiFi扫描 =====
        if (sdk >= 31) {
            perms.add("android.permission.NEARBY_WIFI_DEVICES");
        }

        // ===== NFC =====
        perms.add("android.permission.NFC");

        // ===== 相机/麦克风 =====
        perms.add("android.permission.CAMERA");
        perms.add("android.permission.RECORD_AUDIO");

        // ===== 运动/位置 =====
        perms.add("android.permission.ACCESS_FINE_LOCATION");
        perms.add("android.permission.ACCESS_COARSE_LOCATION");
        if (sdk >= 29) {
            perms.add("android.permission.ACCESS_BACKGROUND_LOCATION");
        }
    }

    public void getSenSorsScanPermissionStr(ArrayList<String> ops){
        if (sdk >= 18) {
            ops.add(AppOpsManager.OPSTR_BODY_SENSORS);
            ops.add(AppOpsManager.OPSTR_ACTIVITY_RECOGNITION);
            ops.add(AppOpsManager.OPSTR_VIBRATE);
        }
        if (sdk >= 31) {
            ops.add("android:bluetooth_scan");
            ops.add("android:bluetooth_advertise");
            ops.add("android:bluetooth_connect");
            ops.add("android:wifi_scan");
        }
    }

    public void getNotificationPermissionStr2(ArrayList<String> perms){
        // ===== 通知发送 =====
        if (sdk >= 33) { // Android 13+
            perms.add("android.permission.POST_NOTIFICATIONS");
            perms.add("android.permission.POST_NOTIFICATIONS_IN_BACKGROUND");
        }
        // ===== 悬浮窗 =====
        perms.add("android.permission.SYSTEM_ALERT_WINDOW");

        // ===== 振动 =====
        perms.add("android.permission.VIBRATE");

        // ===== 音量/声音路由 =====
        perms.add("android.permission.MODIFY_AUDIO_SETTINGS");
    }

    public void getNotificationPermissionStr(ArrayList<String> ops){
        if (sdk >= 18) { // Android 4.3+
            ops.add("android:access_notifications"); // AppOps 控制通知读取
        }
        if (sdk >= 33) { // Android 13+
            ops.add("android:post_notification"); // AppOps 控制通知发送
        }
    }

    public void getFingerprintPermissionStr2(ArrayList<String> perms) {
        if (sdk >= 23) {
            perms.add("android.permission.USE_FINGERPRINT"); // 指纹识别
        }
        if (sdk >= 28) {
            perms.add("android.permission.USE_BIOMETRIC");  // 通用生物识别
        }
    }

    public void getFigerprintPermissionStr(ArrayList<String> ops) {
        if (sdk >= 23) {
            ops.add(AppOpsManager.OPSTR_USE_FINGERPRINT);
        }
        if (sdk >= 28) {
            ops.add(AppOpsManager.OPSTR_USE_BIOMETRIC);
        }
    }

    public void getAlertWindowPermissionStr2(ArrayList<String> perms) {
        // 悬浮窗
        perms.add("android.permission.SYSTEM_ALERT_WINDOW");
        // 画中画（Android 8+）
        if (sdk >= 26) {
            perms.add("android.permission.PICTURE_IN_PICTURE");
        }
    }

    public void getAlertWindowPermissionStr(ArrayList<String> ops) {
        ops.add(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW);
        ops.add(AppOpsManager.OPSTR_TOAST_WINDOW);
    }

    public void getAccessibilityPermissionStr2(ArrayList<String> ops) {
        // 核心权限
        ops.add("android.permission.BIND_ACCESSIBILITY_SERVICE");
        ops.add("android.permission.ACCESS_ACCESSIBILITY");

        // 扩展权限（模拟你 getAccessibilityPermissionStr2 列表的一部分）
        ops.add("android.permission.READ_NOTIFICATIONS");
        ops.add("android.permission.SYSTEM_GLOBAL_ACTIONS");
        ops.add("android.permission.USE_ACCESSIBILITY");
        ops.add("android.permission.ENABLE_ACCESSIBILITY");
        ops.add("android.permission.DISABLE_ACCESSIBILITY");
    }

    public void getAccessibilityPermissionStr(ArrayList<String> ops) {
        // 核心 AppOps
        ops.add(AppOpsManager.OPSTR_BIND_ACCESSIBILITY_SERVICE);
        ops.add(AppOpsManager.OPSTR_ACCESS_ACCESSIBILITY);
        ops.add(AppOpsManager.OPSTR_AUDIO_ACCESSIBILITY_VOLUME);
    }

    public void getAccountPermissionStr2(ArrayList<String> ops) {
        // 标准账户权限
        ops.add("android.permission.GET_ACCOUNTS");
        ops.add("android.permission.GET_ACCOUNTS_PRIVILEGED");

        // 账户管理权限
        ops.add("android.permission.ACCOUNT_MANAGER");
        ops.add("android.permission.MANAGE_ACCOUNTS");

        // 账户认证权限
        ops.add("android.permission.AUTHENTICATE_ACCOUNTS");

        // 账户同步权限
        ops.add("android.permission.READ_SYNC_SETTINGS");
        ops.add("android.permission.WRITE_SYNC_SETTINGS");

        // 账户策略权限
        ops.add("android.permission.MANAGE_DEVICE_POLICY_ACCOUNT_MANAGEMENT");

        // 账户凭据权限
        ops.add("android.permission.USE_CREDENTIALS");

        // 账户工作资料权限
        ops.add("android.permission.MANAGE_WORK_ACCOUNTS");

        // 账户 OAuth 权限
        ops.add("android.permission.MANAGE_OAUTH_ACCOUNTS");

        // 系统账户权限
        ops.add("android.permission.SYSTEM_ACCOUNTS");
    }

    public void getAccountPermissionStr(ArrayList<String> ops) {
        ops.add(AppOpsManager.OPSTR_GET_ACCOUNTS);
    }

    public void getWriteSettingsPermissionStr2(ArrayList<String> ops) {
        // 标准设置写入权限
        ops.add("android.permission.WRITE_SETTINGS");
        ops.add("android.permission.WRITE_SECURE_SETTINGS");
        ops.add("android.permission.WRITE_GLOBAL_SETTINGS");

        // 系统设置写入权限
        ops.add("android.permission.WRITE_SYSTEM_SETTINGS");

        // 网络相关设置写入权限
        ops.add("android.permission.WRITE_NETWORK_SETTINGS");
        ops.add("android.permission.WRITE_WIFI_SETTINGS");

        // 位置设置写入权限
        ops.add("android.permission.WRITE_LOCATION_SETTINGS");

        // 声音设置写入权限
        ops.add("android.permission.WRITE_SOUND_SETTINGS");

        // 显示设置写入权限
        ops.add("android.permission.WRITE_DISPLAY_SETTINGS");

        // 修改设置权限
        ops.add("android.permission.CHANGE_SETTINGS");
        ops.add("android.permission.CHANGE_SYSTEM_SETTINGS");

        // 设备策略设置写入权限
        ops.add("android.permission.WRITE_DEVICE_POLICY_SETTINGS");

        // 电池优化设置写入权限
        ops.add("android.permission.WRITE_BATTERY_OPTIMIZATION_SETTINGS");

        // 悬浮窗设置写入权限
        ops.add("android.permission.WRITE_OVERLAY_SETTINGS");
    }

    public void getWriteSettingsPermissionStr(ArrayList<String> ops) {
        ops.add(AppOpsManager.OPSTR_WRITE_SETTINGS);
    }

    public void getDeviceIdentifiersPermissionStr2(ArrayList<String> ops) {
        // ====== 设备标识符读取权限 ======
        ops.add("android.permission.READ_PHONE_STATE");
        ops.add("android.permission.READ_PRIVILEGED_PHONE_STATE");
        ops.add("android.permission.READ_DEVICE_IDENTIFIERS");
        ops.add("android.permission.ACCESS_DEVICE_IDENTIFIERS");

        // ====== IMEI/MEID权限 ======
        ops.add("android.permission.READ_IMEI");
        ops.add("android.permission.READ_MEID");
        ops.add("android.permission.ACCESS_IMEI");
        ops.add("android.permission.ACCESS_MEID");
        ops.add("android.permission.GET_IMEI");
        ops.add("android.permission.GET_MEID");

        // ====== 序列号权限 ======
        ops.add("android.permission.READ_SERIAL_NUMBER");
        ops.add("android.permission.ACCESS_SERIAL_NUMBER");
        ops.add("android.permission.GET_SERIAL");
        ops.add("android.permission.READ_DEVICE_SERIAL");

        // ====== Android ID 权限 ======
        ops.add("android.permission.READ_ANDROID_ID");
        ops.add("android.permission.ACCESS_ANDROID_ID");
        ops.add("android.permission.GET_ANDROID_ID");

        // ====== 广告ID 权限 ======
        ops.add("android.permission.READ_ADVERTISING_ID");
        ops.add("android.permission.ACCESS_ADVERTISING_ID");
        ops.add("android.permission.GET_AD_ID");
        ops.add("android.permission.USE_ADVERTISING_ID");

        // ====== 其他标识符权限 ======
        ops.add("android.permission.READ_DEVICE_ID");
        ops.add("android.permission.ACCESS_DEVICE_ID");
        ops.add("android.permission.GET_DEVICE_ID");
        ops.add("android.permission.USE_DEVICE_ID");
        ops.add("android.permission.READ_HARDWARE_IDENTIFIERS");
        ops.add("android.permission.ACCESS_HARDWARE_IDENTIFIERS");
        ops.add("android.permission.GET_HARDWARE_ID");

        // ====== MAC / 蓝牙 / WiFi 权限 ======
        ops.add("android.permission.READ_MAC_ADDRESS");
        ops.add("android.permission.ACCESS_MAC_ADDRESS");
        ops.add("android.permission.GET_MAC_ADDRESS");
        ops.add("android.permission.LOCAL_MAC_ADDRESS");
        ops.add("android.permission.READ_WIFI_MAC_ADDRESS");
        ops.add("android.permission.ACCESS_WIFI_MAC_ADDRESS");
        ops.add("android.permission.GET_WIFI_MAC");
        ops.add("android.permission.READ_BLUETOOTH_MAC_ADDRESS");
        ops.add("android.permission.ACCESS_BLUETOOTH_MAC_ADDRESS");
        ops.add("android.permission.GET_BLUETOOTH_MAC");

        // ====== 设备属性 / 序列号 / CPU / 内存 / 存储权限 ======
        ops.add("android.permission.READ_BUILD_FINGERPRINT");
        ops.add("android.permission.ACCESS_BUILD_FINGERPRINT");
        ops.add("android.permission.GET_BUILD_FINGERPRINT");
        ops.add("android.permission.READ_BUILD_SERIAL");
        ops.add("android.permission.READ_DEVICE_MODEL");
        ops.add("android.permission.ACCESS_DEVICE_MODEL");
        ops.add("android.permission.GET_DEVICE_MODEL");
        ops.add("android.permission.READ_PRODUCT_MODEL");
        ops.add("android.permission.READ_MANUFACTURER");
        ops.add("android.permission.ACCESS_MANUFACTURER");
        ops.add("android.permission.GET_MANUFACTURER");
        ops.add("android.permission.READ_BRAND");
        ops.add("android.permission.READ_DEVICE_NAME");
        ops.add("android.permission.ACCESS_DEVICE_NAME");
        ops.add("android.permission.GET_DEVICE_NAME");
        ops.add("android.permission.READ_CPU_INFO");
        ops.add("android.permission.ACCESS_CPU_INFO");
        ops.add("android.permission.GET_CPU_INFO");
        ops.add("android.permission.READ_MEMORY_INFO");
        ops.add("android.permission.ACCESS_MEMORY_INFO");
        ops.add("android.permission.GET_MEMORY_INFO");
        ops.add("android.permission.READ_STORAGE_IDENTIFIERS");
        ops.add("android.permission.ACCESS_STORAGE_IDENTIFIERS");
        ops.add("android.permission.GET_STORAGE_IDENTIFIERS");

        // ====== 系统属性 / 唯一标识符等 ======
        ops.add("android.permission.READ_SYSTEM_PROPERTIES");
        ops.add("android.permission.ACCESS_SYSTEM_PROPERTIES");
        ops.add("android.permission.GET_SYSTEM_PROPERTIES");
        ops.add("android.permission.READ_PERSISTENT_IDENTIFIERS");
        ops.add("android.permission.ACCESS_PERSISTENT_IDENTIFIERS");
        ops.add("android.permission.USE_PERSISTENT_IDENTIFIERS");
        ops.add("android.permission.READ_RESETTABLE_IDENTIFIERS");
        ops.add("android.permission.ACCESS_RESETTABLE_IDENTIFIERS");
        ops.add("android.permission.USE_RESETTABLE_IDENTIFIERS");
        ops.add("android.permission.READ_NETWORK_IDENTIFIERS");
        ops.add("android.permission.ACCESS_NETWORK_IDENTIFIERS");
        ops.add("android.permission.GET_NETWORK_IDENTIFIERS");
        ops.add("android.permission.READ_IP_ADDRESS");
        ops.add("android.permission.ACCESS_IP_ADDRESS");
        ops.add("android.permission.GET_IP_ADDRESS");
        ops.add("android.permission.READ_LOCATION_IDENTIFIER");
        ops.add("android.permission.ACCESS_LOCATION_IDENTIFIER");
        ops.add("android.permission.GET_LOCATION_IDENTIFIER");
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
            getClipboardPermissionStr2(ops);
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
            getFingerprintPermissionStr2(ops);
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

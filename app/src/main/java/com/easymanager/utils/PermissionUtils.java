package com.easymanager.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import com.easymanager.R;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PermissionUtils {
    private static final Map<String, Integer> permMap = new HashMap<>();
    private static final Map<String, String> zhWords = new HashMap<>();
    private static final Map<String, String> hkWords = new HashMap<>();

    static {
        // 1. 高频权限全称映射 (直接匹配 android.permission.XXX)
        permMap.put("android.permission.CAMERA", R.string.spin_item_camera_microphone);
        permMap.put("android.permission.RECORD_AUDIO", R.string.spin_item_camera_microphone);
        permMap.put("android.permission.READ_CONTACTS", R.string.spin_item_account);
        permMap.put("android.permission.WRITE_CONTACTS", R.string.spin_item_account);
        permMap.put("android.permission.READ_SMS", R.string.spin_item_phone_sms);
        permMap.put("android.permission.SEND_SMS", R.string.spin_item_phone_sms);
        permMap.put("android.permission.ACCESS_FINE_LOCATION", R.string.spin_item_location);
        permMap.put("android.permission.ACCESS_COARSE_LOCATION", R.string.spin_item_location);
        permMap.put("android.permission.CALL_PHONE", R.string.spin_item_phone_sms);
        permMap.put("android.permission.READ_PHONE_STATE", R.string.spin_item_phone_sms);
        permMap.put("android.permission.READ_CALENDAR", R.string.spin_item_calendar);
        permMap.put("android.permission.WRITE_CALENDAR", R.string.spin_item_calendar);
        permMap.put("android.permission.BODY_SENSORS", R.string.spin_item_sensor);
        permMap.put("android.permission.POST_NOTIFICATIONS", R.string.spin_item_notify);
        permMap.put("android.permission.SYSTEM_ALERT_WINDOW", R.string.spin_item_alert);
        permMap.put("android.permission.VIBRATE", R.string.perm_vibrate);
        permMap.put("android.permission.INTERNET", R.string.perm_internet);
        permMap.put("android.permission.BLUETOOTH", R.string.perm_bluetooth);
        permMap.put("android.permission.ACCESS_NETWORK_STATE", R.string.perm_network);
        permMap.put("android.permission.ACCESS_WIFI_STATE", R.string.perm_wifi);

        // 2. 词典：用于 fallback 拆分翻译 (简体中文)
        zhWords.put("READ", "读取");
        zhWords.put("WRITE", "写入");
        zhWords.put("ACCESS", "访问");
        zhWords.put("FINE", "精确");
        zhWords.put("COARSE", "模糊");
        zhWords.put("LOCATION", "位置");
        zhWords.put("CAMERA", "摄像头");
        zhWords.put("AUDIO", "音频");
        zhWords.put("RECORD", "录制");
        zhWords.put("CONTACTS", "联系人");
        zhWords.put("SMS", "短信");
        zhWords.put("PHONE", "电话");
        zhWords.put("STATE", "状态");
        zhWords.put("CALENDAR", "日历");
        zhWords.put("STORAGE", "存储");
        zhWords.put("EXTERNAL", "外部");
        zhWords.put("INTERNAL", "内部");
        zhWords.put("SYSTEM", "系统");
        zhWords.put("ALERT", "弹窗");
        zhWords.put("WINDOW", "窗口");
        zhWords.put("PACKAGE", "应用包");
        zhWords.put("INSTALL", "安装");
        zhWords.put("DELETE", "删除");
        zhWords.put("UNINSTALL", "卸载");
        zhWords.put("BOOT", "启动");
        zhWords.put("COMPLETED", "完成");
        zhWords.put("BACKGROUND", "后台");
        zhWords.put("FOREGROUND", "前台");
        zhWords.put("CLIPBOARD", "剪切板");
        zhWords.put("VIBRATE", "震动");
        zhWords.put("INTERNET", "互联网");
        zhWords.put("WIFI", "无线网络");
        zhWords.put("BLUETOOTH", "蓝牙");
        zhWords.put("NETWORK", "网络");
        zhWords.put("NOTIFICATIONS", "通知");
        zhWords.put("RECEIVE", "接收");
        zhWords.put("SEND", "发送");
        zhWords.put("WAKE", "唤醒");
        zhWords.put("LOCK", "锁定");
        zhWords.put("SENSORS", "传感器");
        zhWords.put("BODY", "身体");
        zhWords.put("ACTIVITY", "活动");
        zhWords.put("RECOGNITION", "识别");
        zhWords.put("BATTERY", "电池");
        zhWords.put("OPTIMIZATIONS", "优化");
        zhWords.put("ALARM", "闹钟");
        zhWords.put("SETTINGS", "设置");
        zhWords.put("MODIFY", "修改");
        zhWords.put("ACCESSIBILITY", "辅助功能");
        zhWords.put("SERVICE", "服务");
        zhWords.put("SCREEN", "屏幕");
        zhWords.put("CAPTURE", "截取");

        // 3. 词典：繁体中文 (HK/TW/Hant)
        hkWords.put("READ", "讀取");
        hkWords.put("WRITE", "寫入");
        hkWords.put("ACCESS", "存取");
        hkWords.put("FINE", "精確");
        hkWords.put("COARSE", "概略");
        hkWords.put("LOCATION", "位置");
        hkWords.put("CAMERA", "相機");
        hkWords.put("AUDIO", "音訊");
        hkWords.put("RECORD", "錄製");
        hkWords.put("CONTACTS", "聯絡人");
        hkWords.put("SMS", "短訊");
        hkWords.put("PHONE", "電話");
        hkWords.put("STATE", "狀態");
        hkWords.put("CALENDAR", "日曆");
        hkWords.put("STORAGE", "儲存");
        hkWords.put("EXTERNAL", "外部");
        hkWords.put("INTERNAL", "內部");
        hkWords.put("SYSTEM", "系統");
        hkWords.put("ALERT", "彈窗");
        hkWords.put("WINDOW", "視窗");
        hkWords.put("PACKAGE", "套件");
        hkWords.put("INSTALL", "安裝");
        hkWords.put("DELETE", "刪除");
        hkWords.put("UNINSTALL", "解除安裝");
        hkWords.put("BOOT", "啟動");
        hkWords.put("COMPLETED", "完成");
        hkWords.put("BACKGROUND", "背景");
        hkWords.put("FOREGROUND", "前景");
        hkWords.put("CLIPBOARD", "剪貼板");
        hkWords.put("VIBRATE", "震動");
        hkWords.put("INTERNET", "互聯網");
        hkWords.put("WIFI", "無線網路");
        hkWords.put("BLUETOOTH", "藍牙");
        hkWords.put("NETWORK", "網路");
        hkWords.put("NOTIFICATIONS", "通知");
        hkWords.put("RECEIVE", "接收");
        hkWords.put("SEND", "傳送");
        hkWords.put("WAKE", "喚醒");
        hkWords.put("LOCK", "鎖定");
        hkWords.put("SENSORS", "感應器");
        hkWords.put("BODY", "身體");
        hkWords.put("ACTIVITY", "活動");
        hkWords.put("RECOGNITION", "辨識");
        hkWords.put("BATTERY", "電池");
        hkWords.put("OPTIMIZATIONS", "最佳化");
        hkWords.put("ALARM", "鬧鐘");
        hkWords.put("SETTINGS", "設定");
        hkWords.put("MODIFY", "修改");
        hkWords.put("ACCESSIBILITY", "無障礙");
        hkWords.put("SERVICE", "服務");
        hkWords.put("SCREEN", "螢幕");
        hkWords.put("CAPTURE", "擷取");
    }

    public static String getPermissionLabel(Context context, String permission) {
        if (permission == null) return "";

        // A. 优先内置全称映射 (确保最常用权限风格统一)
        if (permMap.containsKey(permission)) {
            return context.getString(permMap.get(permission));
        }

        // B. 处理名称（去除前缀）
        String nameToProcess = permission;
        if (permission.startsWith("android.permission.")) {
            nameToProcess = permission.replace("android.permission.", "");
        } else if (permission.contains(".")) {
            nameToProcess = permission.substring(permission.lastIndexOf(".") + 1);
        }

        // C. 检查特定简短名映射 (如 VIBRATE, INTERNET)
        String specific = getSpecificLabel(context, nameToProcess);
        if (specific != null) return specific;

        // D. 尝试通过系统 PackageManager 获取 (作为候补，以防自定义权限有现成翻译)
        try {
            PackageManager pm = context.getPackageManager();
            PermissionInfo pi = pm.getPermissionInfo(permission, 0);
            CharSequence label = pi.loadLabel(pm);
            if (label != null && !label.toString().equals(permission) && !label.toString().contains(".")) {
                return label.toString();
            }
        } catch (Exception ignored) {}

        // E. 最后 Fallback: 词典分词翻译
        return translateWords(context, nameToProcess);
    }

    private static String getSpecificLabel(Context context, String name) {
        if (name.equals("VIBRATE")) return context.getString(R.string.perm_vibrate);
        if (name.equals("INTERNET")) return context.getString(R.string.perm_internet);
        if (name.equals("BLUETOOTH")) return context.getString(R.string.perm_bluetooth);
        if (name.equals("ACCESS_NETWORK_STATE")) return context.getString(R.string.perm_network);
        if (name.equals("ACCESS_WIFI_STATE")) return context.getString(R.string.perm_wifi);
        if (name.equals("READ_EXTERNAL_STORAGE") || name.equals("WRITE_EXTERNAL_STORAGE")) return context.getString(R.string.spin_item_storage);
        return null;
    }

    private static String translateWords(Context context, String name) {
        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage();
        String country = locale.getCountry();
        boolean isZh = lang.startsWith("zh");
        // 判定是否为繁体 (HK, TW, 或者 Script 包含 Hant)
        boolean isTraditional = isZh && (country.equalsIgnoreCase("HK") || 
                                         country.equalsIgnoreCase("TW") || 
                                         Locale.getDefault().toString().contains("Hant"));
        
        String[] words = name.split("_");
        StringBuilder sb = new StringBuilder();
        Map<String, String> currentDict = isTraditional ? hkWords : zhWords;
        
        for (String word : words) {
            if (word.isEmpty()) continue;
            if (isZh) {
                String translated = currentDict.get(word.toUpperCase());
                if (translated != null) {
                    sb.append(translated);
                } else {
                    sb.append(capitalize(word.toLowerCase()));
                }
            } else {
                sb.append(capitalize(word.toLowerCase())).append(" ");
            }
        }
        
        String result = sb.toString().trim();
        return result.isEmpty() ? name : result;
    }

    // 为了保持兼容性
    public static String getTranslatePermissionName(Context context, String permission) {
        return getPermissionLabel(context, permission);
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}

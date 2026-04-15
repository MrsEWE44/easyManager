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

    static {
        // 高频权限映射到现有的多语言资源
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

        // 词典：用于拆分翻译
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
        zhWords.put("POST", "发布");
        zhWords.put("RECEIVE", "接收");
        zhWords.put("SEND", "发送");
        zhWords.put("WAKE", "唤醒");
        zhWords.put("LOCK", "锁定");
        zhWords.put("QUERY", "查询");
        zhWords.put("ALL", "全部");
        zhWords.put("PACKAGES", "软件包");
        zhWords.put("MANAGE", "管理");
        zhWords.put("MEDIA", "媒体");
        zhWords.put("IMAGES", "图片");
        zhWords.put("VIDEO", "视频");
        zhWords.put("SENSORS", "传感器");
        zhWords.put("BODY", "身体");
        zhWords.put("ACTIVITY", "活动");
        zhWords.put("RECOGNITION", "识别");
        zhWords.put("HARDWARE", "硬件");
        zhWords.put("IGNORE", "忽略");
        zhWords.put("BATTERY", "电池");
        zhWords.put("OPTIMIZATIONS", "优化");
        zhWords.put("REQUEST", "请求");
        zhWords.put("ALARM", "闹钟");
        zhWords.put("SCHEDULE", "计划");
        zhWords.put("EXACT", "精确");
        zhWords.put("LISTENER", "监听器");
        zhWords.put("SETTINGS", "设置");
        zhWords.put("MODIFY", "修改");
        zhWords.put("AUDIO", "音频");
        zhWords.put("STATS", "状态/统计");
        zhWords.put("USAGE", "使用");
        zhWords.put("ACCESSIBILITY", "辅助功能");
        zhWords.put("BIND", "绑定");
        zhWords.put("SERVICE", "服务");
        zhWords.put("SCREEN", "屏幕");
        zhWords.put("CAPTURE", "截取");
        zhWords.put("WINDOW", "窗口");
        zhWords.put("OVERLAY", "叠加");
    }

    public static String getPermissionLabel(Context context, String permission) {
        if (permission == null) return "";

        // 1. 尝试内置 Map 匹配
        if (permMap.containsKey(permission)) {
            return context.getString(permMap.get(permission));
        }

        // 2. 尝试系统 PackageManager 获取
        try {
            PackageManager pm = context.getPackageManager();
            PermissionInfo pi = pm.getPermissionInfo(permission, 0);
            CharSequence label = pi.loadLabel(pm);
            // 如果 label 存在且不是原始 ID 字符串（通常不含点），则使用它
            if (label != null && !label.toString().equals(permission) && !label.toString().contains(".")) {
                return label.toString();
            }
        } catch (Exception ignored) {}

        // 3. Fallback: 拆分下划线并翻译
        String nameToProcess = permission;
        if (permission.startsWith("android.permission.")) {
            nameToProcess = permission.replace("android.permission.", "");
        } else if (permission.contains(".")) {
            nameToProcess = permission.substring(permission.lastIndexOf(".") + 1);
        }

        return translateWords(context, nameToProcess);
    }

    private static String translateWords(Context context, String name) {
        String lang = Locale.getDefault().getLanguage();
        boolean isZh = lang.startsWith("zh");
        
        String[] words = name.split("_");
        StringBuilder sb = new StringBuilder();
        
        for (String word : words) {
            if (word.isEmpty()) continue;
            
            if (isZh) {
                // 中文模式：查词典
                String translated = zhWords.get(word.toUpperCase());
                if (translated != null) {
                    sb.append(translated);
                } else {
                    // 词典未收录，保持原样但首字母大写
                    sb.append(capitalize(word.toLowerCase()));
                }
            } else {
                // 英文模式：仅格式化
                sb.append(capitalize(word.toLowerCase())).append(" ");
            }
        }
        
        String result = sb.toString().trim();
        return result.isEmpty() ? name : result;
    }

    public static String getTranslatePermissionName(String permission) {
        if (permission == null) return "";

        // 尝试内置 Map 匹配
//        if (permMap.containsKey(permission)) {
//            return permission; // 这里通常返回的是 R.string.xxx，但我们在这里想要的是翻译后的文字。
//            // 由于 getTranslatePermissionName 主要是给注释用的，
//            // 如果能通过 getPermissionLabel 获取到有意义的标签，就用标签。
//        }

        // 拆分逻辑
        String nameToProcess = permission;
        if (permission.startsWith("android.permission.")) {
            nameToProcess = permission.replace("android.permission.", "");
        } else if (permission.contains(".")) {
            String[] parts = permission.split("\\.");
            nameToProcess = parts[parts.length - 1];
        }

        // 专门处理一些常见的权限缩写或全名
        if (nameToProcess.equals("READ_EXTERNAL_STORAGE")) return "读取外部存储";
        if (nameToProcess.equals("WRITE_EXTERNAL_STORAGE")) return "写入外部存储";
        if (nameToProcess.equals("CAMERA")) return "相机";
        if (nameToProcess.equals("RECORD_AUDIO")) return "录音";
        if (nameToProcess.equals("ACCESS_FINE_LOCATION")) return "精确位置";
        if (nameToProcess.equals("ACCESS_COARSE_LOCATION")) return "大概位置";
        if (nameToProcess.equals("INTERNET")) return "网络访问";
        if (nameToProcess.equals("VIBRATE")) return "震动";
        if (nameToProcess.equals("BLUETOOTH")) return "蓝牙";

        return translateWords(null, nameToProcess);
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}

package com.easymanager.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


import com.easymanager.R;
import com.easymanager.entitys.PKGINFO;

import java.util.ArrayList;
import java.util.Locale;

public class TextUtils {

    public TextUtils(){}

    public void copyText(Context context, String str){
        android.content.ClipboardManager cpm = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("label", str);
        cpm.setPrimaryClip(clip);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {
            Toast.makeText(context, getLanguageString(context, R.string.is_copy_ok), Toast.LENGTH_SHORT).show();
        }
    }

    public String getLanguageString(Context context , int id){
        return context.getResources().getString(id);
    }

    //搜索列表匹配项
    public ArrayList<PKGINFO> indexOfPKGS(Activity activity, String findStr, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs, Integer types){
        if(pkginfos.size() == 0){
            PackageUtils packageUtils = new PackageUtils();
            packageUtils.queryPKGS(activity,pkginfos,checkboxs,types);
        }
        return indexOfPKGS(pkginfos,checkboxs,findStr);
    }


    public Boolean isIndexOfStr(String str,String instr){
        return str.toLowerCase(Locale.ROOT).indexOf(instr.toLowerCase(Locale.ROOT)) != -1;
    }


    //搜索列表匹配项
    public ArrayList<PKGINFO> indexOfPKGS(ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,String findStr){
        checkboxs.clear();
        ArrayList<PKGINFO> pkginfos2 = new ArrayList<>();
        for (PKGINFO pkginfo : pkginfos) {
            if(isIndexOfStr(pkginfo.getAppname(),findStr) || isIndexOfStr(pkginfo.getPkgname(),findStr)){
                pkginfos2.add(pkginfo);
                checkboxs.add(false);
            }
        }
        pkginfos.clear();
        return pkginfos2;
    }

    //搜索列表匹配项
    public void indexOfLIST(ArrayList<String> list , ArrayList<Boolean> checkboxs,ArrayList<Boolean> switbs,String findStr){
        if(findStr!=null || !findStr.isEmpty()){
            checkboxs.clear();
            ArrayList<String> strings = new ArrayList<>();
            ArrayList<Boolean> switbs2 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);
                if(isIndexOfStr(s,findStr)){
                    strings.add(s);
                    checkboxs.add(false);
                    if(switbs !=null){
                        switbs2.add(switbs.get(i));
                    }
                }
            }
            list.clear();
            switbs.clear();
            switbs.addAll(switbs2);
            list.addAll(strings);
        }
    }

    private static final java.util.Map<String, String[]> PERMISSION_MAP = new java.util.HashMap<>();
    static {
        // [Simplified Chinese, Traditional Chinese]
        // --- Storage & Media ---
        PERMISSION_MAP.put("READ_EXTERNAL_STORAGE", new String[]{"读取外部存储", "讀取外部儲存"});
        PERMISSION_MAP.put("WRITE_EXTERNAL_STORAGE", new String[]{"写入外部存储", "寫入外部儲存"});
        PERMISSION_MAP.put("MANAGE_EXTERNAL_STORAGE", new String[]{"所有文件访问权限", "所有檔案存取權限"});
        PERMISSION_MAP.put("READ_MEDIA_IMAGES", new String[]{"读取图片", "讀取圖片"});
        PERMISSION_MAP.put("WRITE_MEDIA_IMAGES", new String[]{"写入图片", "寫入圖片"});
        PERMISSION_MAP.put("READ_MEDIA_VIDEO", new String[]{"读取视频", "讀取影片"});
        PERMISSION_MAP.put("WRITE_MEDIA_VIDEO", new String[]{"写入视频", "寫入影片"});
        PERMISSION_MAP.put("READ_MEDIA_AUDIO", new String[]{"读取音频", "讀取音訊"});
        PERMISSION_MAP.put("WRITE_MEDIA_AUDIO", new String[]{"写入音频", "寫入音訊"});
        PERMISSION_MAP.put("ACCESS_MEDIA_LOCATION", new String[]{"读取媒体地理位置", "讀取媒體地理位置"});
        PERMISSION_MAP.put("MANAGE_MEDIA", new String[]{"管理媒体库", "管理媒體庫"});
        PERMISSION_MAP.put("READ_MEDIA_VISUAL_USER_SELECTED", new String[]{"读取用户选定的媒体", "讀取使用者選定的媒體"});

        // --- Camera & Audio ---
        PERMISSION_MAP.put("CAMERA", new String[]{"相机", "相機"});
        PERMISSION_MAP.put("RECORD_AUDIO", new String[]{"录音", "錄音"});
        PERMISSION_MAP.put("MODIFY_AUDIO_SETTINGS", new String[]{"修改音频设置", "修改音訊設定"});
        PERMISSION_MAP.put("CAPTURE_AUDIO_OUTPUT", new String[]{"捕获音频输出", "擷取音訊輸出"});
        PERMISSION_MAP.put("CAPTURE_VIDEO_OUTPUT", new String[]{"捕获视频输出", "擷取視訊輸出"});

        // --- Location ---
        PERMISSION_MAP.put("ACCESS_FINE_LOCATION", new String[]{"精确定位", "精確定位"});
        PERMISSION_MAP.put("ACCESS_COARSE_LOCATION", new String[]{"大致定位", "大致定位"});
        PERMISSION_MAP.put("ACCESS_BACKGROUND_LOCATION", new String[]{"后台定位", "背景定位"});
        PERMISSION_MAP.put("ACCESS_LOCATION_EXTRA_COMMANDS", new String[]{"访问额外位置命令", "存取額外位置指令"});

        // --- Contacts, Calendar, SMS, Phone ---
        PERMISSION_MAP.put("READ_CONTACTS", new String[]{"读取联系人", "讀取聯絡人"});
        PERMISSION_MAP.put("WRITE_CONTACTS", new String[]{"写入联系人", "寫入聯絡人"});
        PERMISSION_MAP.put("GET_ACCOUNTS", new String[]{"获取账户", "獲取帳戶"});
        PERMISSION_MAP.put("READ_CALENDAR", new String[]{"读取日历", "讀取行事曆"});
        PERMISSION_MAP.put("WRITE_CALENDAR", new String[]{"写入日历", "寫入行事曆"});
        PERMISSION_MAP.put("READ_SMS", new String[]{"读取短信", "讀取簡訊"});
        PERMISSION_MAP.put("SEND_SMS", new String[]{"发送短信", "發送簡訊"});
        PERMISSION_MAP.put("RECEIVE_SMS", new String[]{"接收短信", "接收簡訊"});
        PERMISSION_MAP.put("RECEIVE_MMS", new String[]{"接收彩信", "接收多媒體簡訊"});
        PERMISSION_MAP.put("RECEIVE_WAP_PUSH", new String[]{"接收WAP推送", "接收WAP推送"});
        PERMISSION_MAP.put("READ_CELL_BROADCASTS", new String[]{"读取小区广播", "讀取小區廣播"});
        PERMISSION_MAP.put("CALL_PHONE", new String[]{"拨打电话", "撥打電話"});
        PERMISSION_MAP.put("READ_PHONE_STATE", new String[]{"读取电话状态", "讀取電話狀態"});
        PERMISSION_MAP.put("READ_PHONE_NUMBERS", new String[]{"读取电话号码", "讀取電話號碼"});
        PERMISSION_MAP.put("READ_CALL_LOG", new String[]{"读取通话记录", "讀取通話紀錄"});
        PERMISSION_MAP.put("WRITE_CALL_LOG", new String[]{"写入通话记录", "寫入通話紀錄"});
        PERMISSION_MAP.put("ADD_VOICEMAIL", new String[]{"添加语音邮件", "新增語音郵件"});
        PERMISSION_MAP.put("USE_SIP", new String[]{"使用SIP视频", "使用SIP視訊"});
        PERMISSION_MAP.put("PROCESS_OUTGOING_CALLS", new String[]{"修改拨出电话", "修改撥出電話"});
        PERMISSION_MAP.put("ANSWER_PHONE_CALLS", new String[]{"接听电话", "接聽電話"});
        PERMISSION_MAP.put("READ_PRIVILEGED_PHONE_STATE", new String[]{"读取特权电话状态", "讀取特權電話狀態"});

        // --- Sensors & Health ---
        PERMISSION_MAP.put("BODY_SENSORS", new String[]{"身体传感器", "身體感應器"});
        PERMISSION_MAP.put("BODY_SENSORS_BACKGROUND", new String[]{"后台访问身体传感器", "背景存取身體感應器"});
        PERMISSION_MAP.put("ACTIVITY_RECOGNITION", new String[]{"健身运动/活动识别", "健身運動/活動識別"});
        PERMISSION_MAP.put("HIGH_SAMPLING_RATE_SENSORS", new String[]{"高频率使用传感器", "高頻率使用感應器"});

        // --- Network & Connectivity ---
        PERMISSION_MAP.put("INTERNET", new String[]{"访问网络", "網路訪問"});
        PERMISSION_MAP.put("ACCESS_NETWORK_STATE", new String[]{"查看网络状态", "查看網路狀態"});
        PERMISSION_MAP.put("ACCESS_WIFI_STATE", new String[]{"查看WLAN状态", "查看WLAN狀態"});
        PERMISSION_MAP.put("CHANGE_WIFI_STATE", new String[]{"更改WLAN状态", "更改WLAN狀態"});
        PERMISSION_MAP.put("CHANGE_NETWORK_STATE", new String[]{"更改网络状态", "更改網路狀態"});
        PERMISSION_MAP.put("BLUETOOTH", new String[]{"与蓝牙设备配对", "與藍牙裝置配對"});
        PERMISSION_MAP.put("BLUETOOTH_ADMIN", new String[]{"蓝牙管理", "藍牙管理"});
        PERMISSION_MAP.put("BLUETOOTH_SCAN", new String[]{"扫描蓝牙设备", "掃描藍牙裝置"});
        PERMISSION_MAP.put("BLUETOOTH_CONNECT", new String[]{"连接蓝牙设备", "連接藍牙裝置"});
        PERMISSION_MAP.put("BLUETOOTH_ADVERTISE", new String[]{"定位蓝牙设备", "定位藍牙裝置"});
        PERMISSION_MAP.put("BLUETOOTH_PRIVILEGED", new String[]{"蓝牙特权访问", "藍牙特權存取"});
        PERMISSION_MAP.put("NFC", new String[]{"使用NFC", "使用NFC"});
        PERMISSION_MAP.put("NFC_TRANSACTION_EVENT", new String[]{"NFC交易事件", "NFC交易事件"});
        PERMISSION_MAP.put("CHANGE_WIFI_MULTICAST_STATE", new String[]{"允许WLAN多播接收", "允許WLAN多播接收"});
        PERMISSION_MAP.put("NEARBY_WIFI_DEVICES", new String[]{"发现附近WLAN设备", "發現附近WLAN裝置"});

        // --- System Tools & UI ---
        PERMISSION_MAP.put("SYSTEM_ALERT_WINDOW", new String[]{"悬浮窗/在其他应用上显示", "懸浮窗/在其他應用上顯示"});
        PERMISSION_MAP.put("WRITE_SETTINGS", new String[]{"修改系统设置", "修改系統設定"});
        PERMISSION_MAP.put("WRITE_SECURE_SETTINGS", new String[]{"修改安全系统设置", "修改安全系統設定"});
        PERMISSION_MAP.put("POST_NOTIFICATIONS", new String[]{"发送通知", "發送通知"});
        PERMISSION_MAP.put("VIBRATE", new String[]{"震动", "震動"});
        PERMISSION_MAP.put("WAKE_LOCK", new String[]{"防止手机休眠", "防止手機休眠"});
        PERMISSION_MAP.put("SET_WALLPAPER", new String[]{"设置壁纸", "設置桌布"});
        PERMISSION_MAP.put("SET_WALLPAPER_HINTS", new String[]{"设置壁纸尺寸提示", "設置桌布尺寸提示"});
        PERMISSION_MAP.put("EXPAND_STATUS_BAR", new String[]{"展开/收拢状态栏", "展開/收攏狀態欄"});
        PERMISSION_MAP.put("REORDER_TASKS", new String[]{"对任务进行重新排序", "對任務進行重新排序"});
        PERMISSION_MAP.put("GET_PACKAGE_SIZE", new String[]{"计算应用存储空间", "計算應用儲存空間"});
        PERMISSION_MAP.put("KILL_BACKGROUND_PROCESSES", new String[]{"结束后台进程", "結束背景進程"});
        PERMISSION_MAP.put("RECEIVE_BOOT_COMPLETED", new String[]{"开机启动", "開機啟動"});
        PERMISSION_MAP.put("INSTALL_SHORTCUT", new String[]{"安装快捷方式", "安裝快捷方式"});
        PERMISSION_MAP.put("UNINSTALL_SHORTCUT", new String[]{"卸载快捷方式", "解除安裝快捷方式"});
        PERMISSION_MAP.put("REQUEST_IGNORE_BATTERY_OPTIMIZATIONS", new String[]{"忽略电池优化", "忽略電池最佳化"});
        PERMISSION_MAP.put("SCHEDULE_EXACT_ALARM", new String[]{"设置精确闹钟", "設置精確鬧鐘"});
        PERMISSION_MAP.put("USE_EXACT_ALARM", new String[]{"使用精确闹钟", "使用精確鬧鐘"});
        PERMISSION_MAP.put("USE_FULL_SCREEN_INTENT", new String[]{"使用全屏意图", "使用全屏意圖"});

        // --- Advanced & Security ---
        PERMISSION_MAP.put("USE_FINGERPRINT", new String[]{"使用指纹", "使用指紋"});
        PERMISSION_MAP.put("USE_BIOMETRIC", new String[]{"使用生物识别", "使用生物識別"});
        PERMISSION_MAP.put("QUERY_ALL_PACKAGES", new String[]{"查询所有应用", "查詢所有應用"});
        PERMISSION_MAP.put("REQUEST_INSTALL_PACKAGES", new String[]{"请求安装应用", "請求安裝應用"});
        PERMISSION_MAP.put("REQUEST_DELETE_PACKAGES", new String[]{"请求卸载应用", "請求解除安裝應用"});
        PERMISSION_MAP.put("READ_LOGS", new String[]{"读取系统日志", "讀取系統日誌"});
        PERMISSION_MAP.put("DUMP", new String[]{"获取系统转储信息", "獲取系統轉儲資訊"});
        PERMISSION_MAP.put("PACKAGE_USAGE_STATS", new String[]{"使用情况统计", "使用情況統計"});
        PERMISSION_MAP.put("ACCESS_NOTIFICATION_POLICY", new String[]{"访问通知策略", "存取通知策略"});
        PERMISSION_MAP.put("BIND_NOTIFICATION_LISTENER_SERVICE", new String[]{"读取通知内容", "讀取通知內容"});
        PERMISSION_MAP.put("BIND_ACCESSIBILITY_SERVICE", new String[]{"无障碍服务", "輔助功能服務"});
        PERMISSION_MAP.put("FOREGROUND_SERVICE", new String[]{"前台服务", "前景服務"});
        PERMISSION_MAP.put("READ_INSTALL_SESSIONS", new String[]{"读取安装会话", "讀取安裝工作階段"});
        PERMISSION_MAP.put("UPDATE_DEVICE_STATS", new String[]{"更新设备统计", "更新裝置統計"});
        PERMISSION_MAP.put("CHANGE_CONFIGURATION", new String[]{"更改界面设置", "更改介面設定"});
        PERMISSION_MAP.put("MOUNT_UNMOUNT_FILESYSTEMS", new String[]{"挂载反挂载文件系统", "掛載反掛載檔案系統"});
        PERMISSION_MAP.put("WRITE_SYNC_SETTINGS", new String[]{"写入同步设置", "寫入同步設定"});
        PERMISSION_MAP.put("READ_SYNC_SETTINGS", new String[]{"读取同步设置", "讀取同步設定"});
        PERMISSION_MAP.put("READ_SYNC_STATS", new String[]{"读取同步统计", "讀取同步統計"});
        PERMISSION_MAP.put("INTERACT_ACROSS_USERS", new String[]{"跨用户交互", "跨使用者互動"});
        PERMISSION_MAP.put("INTERACT_ACROSS_USERS_FULL", new String[]{"跨用户全权限交互", "跨使用者全權限互動"});
        PERMISSION_MAP.put("ACCESS_SUPERUSER", new String[]{"访问超级用户", "存取超級用戶"});
        PERMISSION_MAP.put("MANAGE_USERS", new String[]{"管理用户", "管理使用者"});
        PERMISSION_MAP.put("CREATE_USERS", new String[]{"创建用户", "建立使用者"});
        PERMISSION_MAP.put("FORCE_STOP_PACKAGES", new String[]{"强制停止应用", "強制停止應用"});
        PERMISSION_MAP.put("BATTERY_STATS", new String[]{"读取电池统计信息", "讀取電池統計資訊"});
        PERMISSION_MAP.put("CLEAR_APP_USER_DATA", new String[]{"清除应用数据", "清除應用資料"});
        PERMISSION_MAP.put("READ_CLIPBOARD", new String[]{"读取剪贴板", "讀取剪貼簿"});
        PERMISSION_MAP.put("WRITE_CLIPBOARD", new String[]{"写入剪贴板", "寫入剪貼簿"});

        // --- AppOps & Specialized ---
        PERMISSION_MAP.put("WRITE_SMS", new String[]{"写入短信", "寫入簡訊"});
        PERMISSION_MAP.put("LEGACY_STORAGE", new String[]{"常规存储访问", "常規儲存存取"});
        PERMISSION_MAP.put("NO_ISOLATED_STORAGE", new String[]{"非隔离存储", "非隔離儲存"});
        PERMISSION_MAP.put("PICTURE_IN_PICTURE", new String[]{"画中画", "子母畫面"});
        PERMISSION_MAP.put("ACCESS_RESTRICTED_SETTINGS", new String[]{"访问受限制设置", "存取受限制設定"});
        PERMISSION_MAP.put("MANAGE_EXTERNAL_STORAGE", new String[]{"所有文件访问权限", "所有檔案存取權限"});
        PERMISSION_MAP.put("REQUEST_INSTALL_PACKAGES", new String[]{"请求安装应用", "請求安裝應用"});
        PERMISSION_MAP.put("SYSTEM_ALERT_WINDOW", new String[]{"悬浮窗/在其他应用上显示", "懸浮窗/在其他應用上顯示"});
        PERMISSION_MAP.put("ACCESS_NOTIFICATIONS", new String[]{"访问通知", "存取通知"});
        PERMISSION_MAP.put("WIFI_SCAN", new String[]{"WLAN扫描", "WLAN掃描"});
        PERMISSION_MAP.put("BLUETOOTH_SCAN", new String[]{"蓝牙扫描", "藍牙掃描"});
        PERMISSION_MAP.put("MOCK_LOCATION", new String[]{"模拟位置", "模擬位置"});
        PERMISSION_MAP.put("RUN_IN_BACKGROUND", new String[]{"后台运行", "背景執行"});
        PERMISSION_MAP.put("INSTANT_APP_START_FOREGROUND", new String[]{"即时应用前台启动", "即時應用前景啟動"});
        PERMISSION_MAP.put("AUTO_REVOKE_PERMISSIONS_IF_UNUSED", new String[]{"不使用时撤销权限", "不使用時撤銷權限"});
        PERMISSION_MAP.put("ACTIVATE_VPN", new String[]{"激活VPN", "啟用VPN"});
        PERMISSION_MAP.put("ACTIVATE_PLATFORM_VPN", new String[]{"激活平台VPN", "啟用平台VPN"});
    }

    public String translatePermission(Context context, String permission) {
        String technicalName = permission;
        if (technicalName.startsWith("android.permission.")) {
            technicalName = technicalName.substring("android.permission.".length());
        }
        
        String label = null;
        try {
            android.content.pm.PackageManager pm = context.getPackageManager();
            android.content.pm.PermissionInfo pi = pm.getPermissionInfo(permission, 0);
            label = pi.loadLabel(pm).toString();
        } catch (Exception e) {}

        java.util.Locale locale = context.getResources().getConfiguration().locale;
        String lang = locale.getLanguage();
        String country = locale.getCountry();
        boolean isZh = lang.equals("zh");
        boolean isHkOrTw = isZh && (country.equals("HK") || country.equals("TW"));

        String splitName = technicalName.replace("_", " ");
        
        if (label != null && !label.equalsIgnoreCase(permission) && !label.equalsIgnoreCase(technicalName)) {
            return label + "\n(" + splitName + ")";
        }

        String[] translations = PERMISSION_MAP.get(technicalName);
        if (translations != null) {
            String translated = isHkOrTw ? translations[1] : (isZh ? translations[0] : null);
            if (translated != null) {
                return translated + "\n(" + splitName + ")";
            }
        }
        
        return splitName;
    }
}

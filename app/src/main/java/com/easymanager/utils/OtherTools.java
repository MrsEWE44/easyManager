package com.easymanager.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.easymanager.R;
import com.easymanager.enums.AppManagerEnum;
import com.easymanager.utils.base.DialogBaseUtils;

public class OtherTools {
    private TextUtils tv = new TextUtils();

    //显示一个弹窗
    public static void showInfoMsg(Context context, String title, String msg) {
        new DialogBaseUtils().showInfoMsg(context, title, msg);
    }

    public void jump(Context context , Class clazz,int mode , Boolean isRoot , Boolean isADB ,Boolean isDevice, int uid){
        Intent intent = new Intent(context, clazz);
        intent.putExtra("mode",mode);
        intent.putExtra("isRoot",isRoot);
        intent.putExtra("isADB",isADB);
        intent.putExtra("isDevice",isDevice);
        intent.putExtra("uid",uid);
        context.startActivity(intent);
    }

    //页面布局跳转
    public void jump(Context srcA , Class<?> cls,Boolean isRoot,Boolean isADB, Boolean isDevice){
        Intent intent = new Intent(srcA, cls);
        intent.putExtra("isRoot", isRoot);
        intent.putExtra("isADB",isADB);
        intent.putExtra("isDevice",isDevice);
        srcA.startActivity(intent);
    }

    //页面布局跳转
    public void jump(Button b , Context srcA , Class<?> cls, Boolean isRoot, Boolean isADB, Boolean isDevice){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jump(srcA,cls,isRoot,isADB,isDevice);
            }
        });
    }

    public void setBtColor(Button b, boolean needRoot, boolean needADB, boolean needDevice, Boolean isRoot, Boolean isADB, Boolean isDevice) {
        Context context = b.getContext();
        android.content.res.Resources res = context.getResources();

        // 1. 判断最终权限状态
        boolean hasPermission = false;

        // 如果没有任何权限要求 (检查更新、GitHub、开源地址等)
        if (!needRoot && !needADB && !needDevice) {
            hasPermission = true;
        } else {
            // 检查是否具备要求的任一权限
            if (isRoot != null && isRoot && needRoot) hasPermission = true;
            else if (isADB != null && isADB && needADB) hasPermission = true;
            else if (isDevice != null && isDevice && needDevice) hasPermission = true;
        }

        // 2. 应用样式
        int bgColor;
        int textColor;

        if (hasPermission) {
            // 可用状态 (Ready) - 统一使用柔和蓝调
            bgColor = res.getColor(R.color.m3_btn_ready_bg);
            textColor = res.getColor(R.color.m3_btn_ready_text);
            b.setEnabled(true);
            b.setAlpha(1.0f);
        } else {
            // 权限缺失状态 (Blocked) - 统一使用深沉红调
            bgColor = res.getColor(R.color.m3_btn_blocked_bg);
            textColor = res.getColor(R.color.m3_btn_blocked_text);
            b.setEnabled(false);
            b.setAlpha(0.7f); // 降低不透明度，使其视觉上更“深”
        }

        // 使用 SRC_IN 确保颜色填充整个背景形状
        b.getBackground().setColorFilter(bgColor, android.graphics.PorterDuff.Mode.SRC_IN);
        b.setTextColor(textColor);
    }

    public void addMenuBase(Context context , Menu menu,int menumode){
        menu.clear();
        if(menumode == AppManagerEnum.RUN_CMD ||menumode == AppManagerEnum.APP_INSTALL_LOCAL_FILE || menumode == AppManagerEnum.MOUNT_LOCAL_IMG || menumode == AppManagerEnum.CREATE_IMG || menumode == AppManagerEnum.SET_NTP || menumode == AppManagerEnum.SET_RATE || menumode == AppManagerEnum.DEL_X || menumode == AppManagerEnum.FILE_SHARED){
            // No base menu items for these modes
        }else if(menumode == AppManagerEnum.APP_RESTORY){
            menu.add(Menu.NONE,9,0,tv.getLanguageString(context, R.string.menu_scan_local_backup_file));
        }else if(menumode == AppManagerEnum.APP_CLEAN_PROCESS){
            menu.add(Menu.NONE,7,0,tv.getLanguageString(context,R.string.menu_scan_get_all_process));
            menu.add(Menu.NONE,8,0,tv.getLanguageString(context,R.string.menu_scan_get_user_process));
        }else if(menumode == AppManagerEnum.APP_INFO_LAYOUT){
            menu.add(Menu.NONE,0,0,tv.getLanguageString(context,R.string.menu_get_app_permission));
            menu.add(Menu.NONE,1,0,tv.getLanguageString(context,R.string.menu_get_app_service));
            menu.add(Menu.NONE,2,0,tv.getLanguageString(context,R.string.menu_get_app_activity));
            menu.add(Menu.NONE,3,0,tv.getLanguageString(context,R.string.menu_get_app_recvi));
            menu.add(Menu.NONE,100,0,tv.getLanguageString(context,R.string.menu_copy_app_info));
        }else if(menumode == AppManagerEnum.APP_CLONE_MANAGE){
            menu.add(Menu.NONE,0,0,tv.getLanguageString(context,R.string.menu_get_all_app));
            menu.add(Menu.NONE,1,0,tv.getLanguageString(context,R.string.menu_get_all_app2));
            menu.add(Menu.NONE,2,0,tv.getLanguageString(context,R.string.menu_get_user_all_app));
            menu.add(Menu.NONE,3,0,tv.getLanguageString(context,R.string.menu_get_user_all_app2));
            menu.add(Menu.NONE,4,0,tv.getLanguageString(context,R.string.menu_get_user_all_disable_app));
            menu.add(Menu.NONE,10,0,tv.getLanguageString(context,R.string.show_clone_user_installed_app));
            menu.add(Menu.NONE,11,0,tv.getLanguageString(context,R.string.show_clone_user_all_installed_app));
            menu.add(Menu.NONE,12,0,tv.getLanguageString(context,R.string.show_clone_user_uid));
            menu.add(Menu.NONE,13,0,tv.getLanguageString(context,R.string.show_start_app_clone_user));
            menu.add(Menu.NONE,14,0,tv.getLanguageString(context,R.string.show_clone_unock_max_user));
        }else if(menumode == AppManagerEnum.APP_CLONE_REMOVE){
            menu.add(Menu.NONE,12,0,tv.getLanguageString(context,R.string.show_clone_user_uid));
        }else{
            menu.add(Menu.NONE,0,0,tv.getLanguageString(context,R.string.menu_get_all_app));
            menu.add(Menu.NONE,1,0,tv.getLanguageString(context,R.string.menu_get_all_app2));
            menu.add(Menu.NONE,2,0,tv.getLanguageString(context,R.string.menu_get_user_all_app));
            menu.add(Menu.NONE,3,0,tv.getLanguageString(context,R.string.menu_get_user_all_app2));
            menu.add(Menu.NONE,4,0,tv.getLanguageString(context,R.string.menu_get_user_all_disable_app));
        }
        menu.add(Menu.NONE, 5, 0, tv.getLanguageString(context, R.string.menu_help));
        menu.add(Menu.NONE, 6, 0, tv.getLanguageString(context, R.string.menu_exit));
    }

}

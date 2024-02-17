package com.easymanager.utils;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.easymanager.R;
import com.easymanager.enums.AppManagerEnum;

public class OtherTools {
    private TextUtils tv = new TextUtils();

    //页面布局跳转
    public void jump(Context srcA , Class<?> cls,Boolean isRoot,Boolean isADB){
        Intent intent = new Intent(srcA, cls);
        intent.putExtra("isRoot", isRoot);
        intent.putExtra("isADB",isADB);
        srcA.startActivity(intent);
    }

    //页面布局跳转
    public void jump(Button b , Context srcA , Class<?> cls, Boolean isRoot, Boolean isADB){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jump(srcA,cls,isRoot,isADB);
            }
        });
    }

    public void addMenuBase(Context context , Menu menu,int menumode){
        String help = tv.getLanguageString(context, R.string.menu_help);
        String exit = tv.getLanguageString(context, R.string.menu_exit);
        if(menumode == AppManagerEnum.APP_INSTALL_LOCAL_FILE || menumode == AppManagerEnum.MOUNT_LOCAL_IMG || menumode == AppManagerEnum.CREATE_IMG || menumode == AppManagerEnum.SET_NTP || menumode == AppManagerEnum.SET_RATE || menumode == AppManagerEnum.DEL_X || menumode == AppManagerEnum.FILE_SHARED){
            menu.add(Menu.NONE,5,0,help);
            menu.add(Menu.NONE,6,0,exit);
        }else if(menumode == AppManagerEnum.APP_RESTORY){
            menu.add(Menu.NONE,9,0,tv.getLanguageString(context, R.string.menu_scan_local_backup_file));
            menu.add(Menu.NONE,5,0,help);
            menu.add(Menu.NONE,6,0,exit);
        }else if(menumode == AppManagerEnum.APP_CLEAN_PROCESS){
            menu.add(Menu.NONE,7,0,tv.getLanguageString(context,R.string.menu_scan_get_all_process));
            menu.add(Menu.NONE,8,0,tv.getLanguageString(context,R.string.menu_scan_get_user_process));
            menu.add(Menu.NONE,5,0,help);
            menu.add(Menu.NONE,6,0,exit);
        }else if(menumode == AppManagerEnum.APP_INFO_LAYOUT){
            menu.add(Menu.NONE,0,0,tv.getLanguageString(context,R.string.menu_get_app_permission));
            menu.add(Menu.NONE,1,0,tv.getLanguageString(context,R.string.menu_get_app_service));
            menu.add(Menu.NONE,2,0,tv.getLanguageString(context,R.string.menu_get_app_activity));
            menu.add(Menu.NONE,3,0,tv.getLanguageString(context,R.string.menu_get_app_recvi));
            menu.add(Menu.NONE,4,0,help);
            menu.add(Menu.NONE,5,0,exit);
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
            menu.add(Menu.NONE,5,0,help);
            menu.add(Menu.NONE,6,0,exit);
        }else if(menumode == AppManagerEnum.APP_CLONE_REMOVE){
            menu.add(Menu.NONE,12,0,tv.getLanguageString(context,R.string.show_clone_user_uid));
            menu.add(Menu.NONE,5,0,help);
            menu.add(Menu.NONE,6,0,exit);
        }else{
            menu.add(Menu.NONE,0,0,tv.getLanguageString(context,R.string.menu_get_all_app));
            menu.add(Menu.NONE,1,0,tv.getLanguageString(context,R.string.menu_get_all_app2));
            menu.add(Menu.NONE,2,0,tv.getLanguageString(context,R.string.menu_get_user_all_app));
            menu.add(Menu.NONE,3,0,tv.getLanguageString(context,R.string.menu_get_user_all_app2));
            menu.add(Menu.NONE,4,0,tv.getLanguageString(context,R.string.menu_get_user_all_disable_app));
            menu.add(Menu.NONE,5,0,help);
            menu.add(Menu.NONE,6,0,exit);
        }
    }

}

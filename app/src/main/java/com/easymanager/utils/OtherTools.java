package com.easymanager.utils;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.easymanager.R;
import com.easymanager.enums.AppManagerEnum;

public class OtherTools {
    private TextUtils tv = new TextUtils();

    public void jump(Context context , Class clazz,int mode , Boolean isShizuku , Boolean isDhizuku, int uid){
        Intent intent = new Intent(context, clazz);
        intent.putExtra("mode",mode);
        intent.putExtra("isRoot",false);
        intent.putExtra("isADB",isShizuku);
        intent.putExtra("isDevice",isDhizuku);
        intent.putExtra("uid",uid);
        intent.putExtra("isShizuku", isShizuku);
        intent.putExtra("isDhizuku", isDhizuku);
        context.startActivity(intent);
    }

    //页面布局跳转
    public void jump(Context srcA , Class<?> cls,Boolean isShizuku , Boolean isDhizuku){
        Intent intent = new Intent(srcA, cls);
        intent.putExtra("isRoot", false);
        intent.putExtra("isADB",isShizuku);
        srcA.startActivity(intent);
    }

    public void setBtColor(Button b , boolean needShizuku , boolean needDhizuku, Boolean isShizuku,Boolean isDhizuku){
        boolean shizukuActive = isShizuku != null && isShizuku;
        boolean dhizukuActive = isDhizuku != null && isDhizuku;

        boolean canUse = false;
        if (!needShizuku && !needDhizuku) {
            canUse = true;
        } else if (needShizuku && shizukuActive) {
            canUse = true;
        } else if (needDhizuku && dhizukuActive) {
            canUse = true;
        }

        if (canUse) {
            b.setEnabled(true);
            b.setAlpha(1.0f);
        } else {
            b.setEnabled(false);
            b.setAlpha(0.38f);
        }
    }

    public void addMenuBase(Context context , Menu menu,int menumode){
        menu.clear();
        String help = tv.getLanguageString(context, R.string.menu_help);
        String exit = tv.getLanguageString(context, R.string.menu_exit);
        String search = tv.getLanguageString(context, R.string.menu_search_str);
        menu.add(Menu.NONE, R.id.actionbarsearch, 0, search).setIcon(R.drawable.search_foreground).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        if(menumode == AppManagerEnum.RUN_CMD ||menumode == AppManagerEnum.APP_INSTALL_LOCAL_FILE ||  menumode == AppManagerEnum.SET_NTP || menumode == AppManagerEnum.SET_RATE || menumode == AppManagerEnum.DEL_X || menumode == AppManagerEnum.FILE_SHARED){
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
            menu.add(Menu.NONE,14,0,tv.getLanguageString(context,R.string.show_clone_unock_max_user));
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
            menu.add(Menu.NONE,11,0,tv.getLanguageString(context,R.string.menu_get_system_all_app));
            menu.add(Menu.NONE,12,0,tv.getLanguageString(context,R.string.menu_get_system_all_app2));
            menu.add(Menu.NONE,5,0,help);
            menu.add(Menu.NONE,6,0,exit);
        }
    }

}

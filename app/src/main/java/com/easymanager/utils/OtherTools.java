package com.easymanager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.easymanager.entitys.PKGINFO;
import com.easymanager.enums.AppManagerEnum;

import java.util.ArrayList;

public class OtherTools {

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

    public void addMenuBase(Menu menu,int menumode){
        if(menumode == AppManagerEnum.MOUNT_LOCAL_IMG || menumode == AppManagerEnum.CREATE_IMG || menumode == AppManagerEnum.SET_NTP || menumode == AppManagerEnum.SET_RATE || menumode == AppManagerEnum.DEL_X || menumode == AppManagerEnum.FILE_SHARED){
            menu.add(Menu.NONE,5,0,"帮助");
            menu.add(Menu.NONE,6,0,"退出");
        }else if(menumode == AppManagerEnum.APP_RESTORY){
            menu.add(Menu.NONE,9,0,"扫描本地备份应用");
            menu.add(Menu.NONE,5,0,"帮助");
            menu.add(Menu.NONE,6,0,"退出");
        }else if(menumode == AppManagerEnum.APP_CLEAN_PROCESS){
            menu.add(Menu.NONE,7,0,"获取所有后台进程");
            menu.add(Menu.NONE,8,0,"获取用户后台进程");
            menu.add(Menu.NONE,5,0,"帮助");
            menu.add(Menu.NONE,6,0,"退出");
        }else if(menumode == AppManagerEnum.APP_INFO_LAYOUT){
            menu.add(Menu.NONE,0,0,"获取应用权限");
            menu.add(Menu.NONE,1,0,"获取应用服务");
            menu.add(Menu.NONE,2,0,"获取应用活动");
            menu.add(Menu.NONE,3,0,"获取应用广播接收器");
            menu.add(Menu.NONE,4,0,"帮助");
            menu.add(Menu.NONE,5,0,"退出");
        }else if(menumode == AppManagerEnum.APP_INSTALL_LOCAL_FILE){
            menu.add(Menu.NONE,5,0,"帮助");
            menu.add(Menu.NONE,6,0,"退出");
        }else{
            menu.add(Menu.NONE,0,0,"显示用户所有应用(包括已禁用)");
            menu.add(Menu.NONE,1,0,"显示用户所有应用");
            menu.add(Menu.NONE,2,0,"显示用户已安装应用(包括已禁用)");
            menu.add(Menu.NONE,3,0,"显示用户已安装应用");
            menu.add(Menu.NONE,4,0,"显示用户已禁用的应用");
            menu.add(Menu.NONE,5,0,"帮助");
            menu.add(Menu.NONE,6,0,"退出");
        }
    }

}

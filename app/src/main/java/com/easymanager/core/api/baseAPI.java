package com.easymanager.core.api;

public class baseAPI {

    //获取应用本身的uid，如果是adb授权模式，uid理应2000
    public int getMyuid(){
        return android.os.Process.myUid();
    }

    public int getMyuidHascode(){
        return android.os.Process.myUserHandle().hashCode();
    }

    //判断当前应用权限是否为root
    public boolean isRoot(){
        return getMyuid() == 0;
    }

    //判断当前应用权限是否为adb
    public boolean isADB(){
        return getMyuid() == 2000;
    }

}

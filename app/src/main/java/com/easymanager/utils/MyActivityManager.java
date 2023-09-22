package com.easymanager.utils;

import android.app.Activity;

import java.util.ArrayList;

public class MyActivityManager {

    private ArrayList<Activity> list = new ArrayList<>();

    private static MyActivityManager ins;

    public static MyActivityManager getIns(){
        if(ins == null){
            ins = new MyActivityManager();
        }
        return ins;
    }

    //添加一个activity实现
    public void add(Activity activity){
        list.add(activity);
    }

    //终止一个activity
    public void kill(Activity activity){
        for (Activity a : list) {
            if(a == activity){
                a.finish();
                list.remove(a);
            }
        }
    }

    //终止所保存的activity实例
    public void killall(){
        for (Activity activity : list) {
            activity.finish();
        }
        System.exit(0);
    }

}

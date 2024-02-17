package com.easymanager.utils;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.easymanager.entitys.MyApplicationInfo;
import com.easymanager.entitys.MyPackageInfo;
import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.entitys.PKGINFO;

import java.util.ArrayList;
import java.util.List;

public class ProcessUtils {

    private PackageUtils packageUtils = new PackageUtils();

    private easyManagerUtils ee = new easyManagerUtils();

    public ProcessUtils(){}

    public void queryRunningPKGSCore(List<PackageInfo> installedPackages, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs, PackageManager packageManager, boolean isAll){
        packageUtils.clearList(pkginfos,checkboxs);
        for (PackageInfo packageInfo : installedPackages) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if (isAll) {
                if (((ApplicationInfo.FLAG_STOPPED & applicationInfo.flags) == 0)) {
                    packageUtils.checkBoxs(pkginfos,checkboxs,packageInfo,packageManager);
                }
            } else {
                if (((ApplicationInfo.FLAG_SYSTEM & applicationInfo.flags) == 0)
                        && ((ApplicationInfo.FLAG_UPDATED_SYSTEM_APP & applicationInfo.flags) == 0)
                        && ((ApplicationInfo.FLAG_STOPPED & applicationInfo.flags) == 0)) {
                    packageUtils.checkBoxs(pkginfos,checkboxs,packageInfo,packageManager);
                }

            }
        }
        packageUtils.sortPKGINFOS(pkginfos);
    }

    public void queryRunningPKGSByUIDCore(List<MyPackageInfo> installedPackages, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs, PackageManager packageManager, boolean isAll){
        packageUtils.clearList(pkginfos,checkboxs);
        for (MyPackageInfo packageInfo : installedPackages) {
            MyApplicationInfo info = packageInfo.myapplicationInfo;
            if (isAll) {
                if (((ApplicationInfo.FLAG_STOPPED & info.flags) == 0)) {
                    packageUtils.appInfoAdd(packageManager,packageInfo,pkginfos,checkboxs);
                }
            } else {
                if (((ApplicationInfo.FLAG_SYSTEM & info.flags) == 0)
                        && ((ApplicationInfo.FLAG_UPDATED_SYSTEM_APP & info.flags) == 0)
                        && ((ApplicationInfo.FLAG_STOPPED & info.flags) == 0)) {
                    packageUtils.appInfoAdd(packageManager,packageInfo,pkginfos,checkboxs);
                }
            }
        }
        packageUtils.sortPKGINFOS(pkginfos);
    }


    //查询当前运行在后台的应用，用户安装部分
    public void queryRunningPKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs, Integer types){
        PackageManager packageManager = activity.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(types);
        queryRunningPKGSCore(installedPackages,pkginfos,checkboxs,packageManager,false);
    }

    public void queryRunningPKGSByUID(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer uid){
        List<MyPackageInfo> installedPackages = ee.getInstalledPackages(new TransmissionEntity(null,null,activity.getPackageName(),0,uid));
        queryRunningPKGSByUIDCore(installedPackages,pkginfos,checkboxs,activity.getPackageManager(),false);
    }

    //查询当前运行在后台的应用，所有
    public void queryAllRunningPKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer types){
        PackageManager packageManager = activity.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(types);
        queryRunningPKGSCore(installedPackages,pkginfos,checkboxs,packageManager,true);
    }
    public void queryAllRunningPKGSByUID(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer uid){
        List<MyPackageInfo> installedPackages = ee.getInstalledPackages(new TransmissionEntity(null,null,activity.getPackageName(),0,uid));
        queryRunningPKGSByUIDCore(installedPackages,pkginfos,checkboxs,activity.getPackageManager(),true);
    }

}

package com.easymanager.utils;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.easymanager.entitys.PKGINFO;

import java.util.ArrayList;
import java.util.List;

public class ProcessUtils {

    private PackageUtils packageUtils = new PackageUtils();

    public ProcessUtils(){}

    public void queryRunningPKGSCore(List<PackageInfo> installedPackages, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs, PackageManager packageManager, boolean isAll){
        pkginfos.clear();
        checkboxs.clear();
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


    //查询当前运行在后台的应用，用户安装部分
    public void queryRunningPKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs, Integer types){
        PackageManager packageManager = activity.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(types);
        queryRunningPKGSCore(installedPackages,pkginfos,checkboxs,packageManager,false);
    }

    //查询当前运行在后台的应用，所有
    public void queryAllRunningPKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer types){
        PackageManager packageManager = activity.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(types);
        queryRunningPKGSCore(installedPackages,pkginfos,checkboxs,packageManager,true);
    }

}

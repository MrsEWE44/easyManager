package com.easymanager.utils;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Build;

import com.easymanager.entitys.PKGINFO;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class PackageUtils {

    public  Integer QUERY_ALL_USER_ENABLE_PKG=2;
    public  Integer QUERY_ALL_ENABLE_PKG=0;
    public  Integer QUERY_ALL_USER_PKG=3;
    public  Integer QUERY_ALL_DISABLE_PKG=1;
    public  Integer QUERY_ALL_DEFAULT_PKG=4;

    public PackageUtils(){}

    //查询当前机主安装的应用
    public void queryPKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs, Integer types){
        clearList(pkginfos,checkboxs);
        queryPKGSCore(activity,pkginfos,checkboxs,types,QUERY_ALL_DEFAULT_PKG);
    }

    //查询当前机主安装的应用,用户部分
    public void queryUserPKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer types){
        clearList(pkginfos,checkboxs);
        queryPKGSCore(activity,pkginfos,checkboxs,types,QUERY_ALL_USER_PKG);
    }

    //查询当前机主安装的应用,用户启用部分
    public void queryUserEnablePKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer types){
        clearList(pkginfos,checkboxs);
        queryPKGSCore(activity,pkginfos,checkboxs,types,QUERY_ALL_USER_ENABLE_PKG);
    }

    //查询当前机主安装的应用,禁用部分
    public void queryDisablePKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer types){
        clearList(pkginfos,checkboxs);
        queryPKGSCore(activity,pkginfos,checkboxs,types,QUERY_ALL_DISABLE_PKG);
    }

    public PKGINFO getPKGINFO(Context context , String pkgname){
        PackageManager packageManager = getPackageManager(context);
        PackageInfo packageInfo = getPackageInfo(packageManager, pkgname, 0);
        return getPKGINFO(packageManager,packageInfo,packageInfo.applicationInfo.sourceDir);
    }

    public PKGINFO getPKGINFO(PackageManager packageManager,PackageInfo packageInfo , String filePath){
        try {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            applicationInfo.sourceDir=filePath;
            applicationInfo.publicSourceDir = filePath;
            Long filesieze = applicationInfo.sourceDir==null? 0: new File(applicationInfo.sourceDir).length();
            return new PKGINFO(applicationInfo.packageName, applicationInfo.loadLabel(packageManager).toString(), applicationInfo.sourceDir,applicationInfo.uid+"",packageInfo.versionName, applicationInfo.loadIcon(packageManager),filesieze) ;
        }catch (Exception e){
            return null;
        }
    }

    public void checkBoxs(ArrayList<PKGINFO> pkginfos,ArrayList<Boolean> checkboxs,PackageInfo packageInfo,PackageManager packageManager){
        if(checkboxs != null){
            checkboxs.add(false);
        }
        pkginfos.add(getPKGINFO(packageManager,packageInfo,packageInfo.applicationInfo.sourceDir));
    }

    public void checkBoxsHashMap(HashMap<String,PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, PackageInfo packageInfo, PackageManager packageManager){
        if(checkboxs != null){
            checkboxs.add(false);
        }
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        pkginfos.put(applicationInfo.packageName,
                new PKGINFO(applicationInfo.packageName, applicationInfo.loadLabel(packageManager).toString(), applicationInfo.sourceDir,applicationInfo.uid+"",packageInfo.versionName, applicationInfo.loadIcon(packageManager),new File(applicationInfo.sourceDir).length())) ;
    }

    public int getPkgUid(Context context , String pkgname){
        try {
            return getPackageManager(context).getApplicationInfo(pkgname,0).uid;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void appInfoAdd(PackageManager packageManager,PackageInfo packageInfo,ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer state){
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        switch (state){
            case 0:
                if(applicationInfo.enabled){
                    checkBoxs(pkginfos,checkboxs,packageInfo,packageManager);
                }
                break;
            case 1:
                if(!applicationInfo.enabled){
                    checkBoxs(pkginfos,checkboxs,packageInfo,packageManager);
                }
                break;
            case 2:
                if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && applicationInfo.enabled){
                    checkBoxs(pkginfos,checkboxs,packageInfo,packageManager);
                }
                break;
            case 3:
                if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                    checkBoxs(pkginfos,checkboxs,packageInfo,packageManager);
                }
                break;
            case 4:
                checkBoxs(pkginfos,checkboxs,packageInfo,packageManager);
                break;
        }

    }

    public void sortPKGINFOS(ArrayList<PKGINFO> pkginfos){
        if(pkginfos != null && pkginfos.size()>0){
            Collections.sort(pkginfos, new Comparator<PKGINFO>() {
                @Override
                public int compare(PKGINFO pkginfo, PKGINFO t1) {
                    return pkginfo.getAppname().compareTo(t1.getAppname());
                }
            });
        }
    }

    public void queryPKGSCore(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer types,Integer state){
        PackageManager packageManager = activity.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(types);
        for (PackageInfo packageInfo : installedPackages) {
            appInfoAdd(packageManager,packageInfo,pkginfos,checkboxs,state);
        }
        sortPKGINFOS(pkginfos);
    }

    //查询当前机主安装的应用,启用部分
    public void queryEnablePKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer types){
        clearList(pkginfos,checkboxs);
        queryPKGSCore(activity,pkginfos,checkboxs,types,QUERY_ALL_ENABLE_PKG);
    }

    public void clearList(ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs){
        checkboxs.clear();
        pkginfos.clear();
    }


    public int getPKGVersiongCode(Context context , String pkgname){
        try {
            return context.getApplicationContext().getPackageManager().getPackageInfo(pkgname,0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public PackageManager getPackageManager(Context context){
        return context.getPackageManager();
    }

    public PackageInfo getPackageInfo(PackageManager pm , String pkgname , Integer mode) {
        if(mode == 0){
            PackageInfo packageInfo = null;
            try {
                packageInfo = pm.getPackageInfo(pkgname, PackageManager.GET_PERMISSIONS|PackageManager.GET_ACTIVITIES|PackageManager.GET_DISABLED_COMPONENTS);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
            return packageInfo;
        }
        return null;
    }

    public PackageInfo getPackageInfo(Context context , String pkgname , Integer mode) {
        return getPackageInfo(getPackageManager(context),pkgname,mode);
    }

    public void getPKGActivitys(Context context , String pkgname, ArrayList<String> list , ArrayList<Boolean> checkboxs,ArrayList<Boolean> switbs) {
        clearList(list,checkboxs,switbs);
        PackageManager packageManager = getPackageManager(context);
        PackageInfo packageInfo = getPackageInfo(packageManager,pkgname,0);
        if(packageInfo.activities != null){
            ArrayList<String> mainActivityList = new ArrayList<>();
            ArrayList<String> otherActivityList = new ArrayList<>();
            for (ActivityInfo activity : packageInfo.activities) {
                int enabledSetting = packageManager.getComponentEnabledSetting(new ComponentName(packageInfo.packageName, activity.name));
                if(activity.exported){
                    mainActivityList.add(activity.name);
                }else{
                    otherActivityList.add(activity.name);
                }
                if(enabledSetting == PackageManager.COMPONENT_ENABLED_STATE_DISABLED){
                    switbs.add(false);
                }else {
                    switbs.add(true);
                }
                checkboxs.add(false);
            }
            list.addAll(mainActivityList);
            list.addAll(otherActivityList);
        }

    }

    public void clearList(ArrayList<String> list, ArrayList<Boolean> checkboxs, ArrayList<Boolean> switbs) {
        list.clear();
        checkboxs.clear();
        switbs.clear();
    }

    public void getPKGPermission(Context context , String pkgname, ArrayList<String> list , ArrayList<Boolean> checkboxs,ArrayList<Boolean> switbs) {
        clearList(list,checkboxs,switbs);
        PackageInfo packageInfo = getPackageInfo(context,pkgname,0);
        if(packageInfo.requestedPermissions != null){
            AppOpsManager opsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            for (String permission : packageInfo.requestedPermissions) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String permissionToOp = AppOpsManager.permissionToOp(permission);
                    list.add(permission);
                    checkboxs.add(false);
                    if(permissionToOp != null){
                        int checkop = -1;
                        int myuid = android.os.Process.myUid();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                            checkop = opsManager.unsafeCheckOp(permissionToOp,myuid,packageInfo.packageName);
                        }else {
                            checkop = opsManager.checkOp(permissionToOp,myuid,packageInfo.packageName);
                        }
                        switbs.add(checkop == AppOpsManager.MODE_ALLOWED);
                    }else{
                        switbs.add(false);
                    }
                }
            }
        }

    }

    public void getPKGServices(Context context , String pkgname, ArrayList<String> list , ArrayList<Boolean> checkboxs,ArrayList<Boolean> switbs){
        clearList(list,checkboxs,switbs);
        PackageManager packageManager = getPackageManager(context);
        PackageInfo packageInfo = getPackageInfo(packageManager, pkgname, 0);
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        PackageInfo archiveInfo = packageManager.getPackageArchiveInfo(applicationInfo.sourceDir, PackageManager.GET_SERVICES);
        if(archiveInfo.services != null){
            for (ServiceInfo service : archiveInfo.services) {
                ComponentName componentName = new ComponentName(packageInfo.packageName, service.name);
                int enabledSetting = packageManager.getComponentEnabledSetting(componentName);
                if(enabledSetting == PackageManager.COMPONENT_ENABLED_STATE_DISABLED){
                    switbs.add(false);
                }else{
                    switbs.add(true);
                }
                list.add(service.name);
                checkboxs.add(false);
            }
        }
    }

    public void getPKGReceivers(Context context , String pkgname, ArrayList<String> list , ArrayList<Boolean> checkboxs,ArrayList<Boolean> switbs){
        clearList(list,checkboxs,switbs);
        PackageManager packageManager = getPackageManager(context);
        PackageInfo packageInfo = getPackageInfo(packageManager, pkgname, 0);
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        PackageInfo archiveInfo = packageManager.getPackageArchiveInfo(applicationInfo.sourceDir, PackageManager.GET_RECEIVERS);
        if(archiveInfo.receivers != null){
            for (ActivityInfo receiver : archiveInfo.receivers) {
                ComponentName componentName = new ComponentName(packageInfo.packageName, receiver.name);
                int enabledSetting = packageManager.getComponentEnabledSetting(componentName);
                if(enabledSetting == PackageManager.COMPONENT_ENABLED_STATE_DISABLED){
                    switbs.add(false);
                }else{
                    switbs.add(true);
                }
                list.add(receiver.name);
                checkboxs.add(false);
            }

        }
    }

}

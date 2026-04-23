package com.easymanager.utils;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.easymanager.entitys.MyActivityInfo;
import com.easymanager.entitys.MyApplicationInfo;
import com.easymanager.entitys.MyAppopsInfo;
import com.easymanager.entitys.MyPackageInfo;
import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.entitys.PKGINFO;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageUtils {

    public  Integer QUERY_ALL_USER_ENABLE_PKG=2;
    public  Integer QUERY_ALL_ENABLE_PKG=0;
    public  Integer QUERY_ALL_USER_PKG=3;
    public  Integer QUERY_ALL_DISABLE_PKG=1;
    public  Integer QUERY_ALL_DEFAULT_PKG=4;
    public  Integer QUERY_ALL_SYSTEM_PKG=5;
    public  Integer QUERY_ALL_SYSTEM_ENABLE_PKG=6;

    private easyManagerUtils ee = new easyManagerUtils();

    public PackageUtils(){}

    //查询当前机主安装的应用
    public void queryPKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs, Integer types){
        clearList(pkginfos,checkboxs);
        queryPKGSCore(activity,pkginfos,checkboxs,types,QUERY_ALL_DEFAULT_PKG);
    }

    public void queryPKGSByUID(int uid ,Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs){
        clearList(pkginfos,checkboxs);
        queryPKGSCoreByUID(uid,activity,pkginfos,checkboxs,false,false,false,false);
    }

    public void querySystemPKGSByUID(int uid ,Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs){
        clearList(pkginfos,checkboxs);
        queryPKGSCoreByUID(uid,activity,pkginfos,checkboxs,false,false,true,false);
    }

    public void querySystemEnablePKGSByUID(int uid ,Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs){
        clearList(pkginfos,checkboxs);
        queryPKGSCoreByUID(uid,activity,pkginfos,checkboxs,false,true,true,false);
    }

    public void querySystemDisablePKGSByUID(int uid ,Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs){
        clearList(pkginfos,checkboxs);
        queryPKGSCoreByUID(uid,activity,pkginfos,checkboxs,true,false,true,false);
    }

    //查询当前机主安装的应用,用户部分
    public void queryUserPKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer types){
        clearList(pkginfos,checkboxs);
        queryPKGSCore(activity,pkginfos,checkboxs,types,QUERY_ALL_USER_PKG);
    }

    public void queryUserPKGSByUID(int uid ,Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs){
        clearList(pkginfos,checkboxs);
        queryPKGSCoreByUID(uid,activity,pkginfos,checkboxs,false,false,false,true);
    }

    //查询当前机主安装的应用,用户启用部分
    public void queryUserEnablePKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer types){
        clearList(pkginfos,checkboxs);
        queryPKGSCore(activity,pkginfos,checkboxs,types,QUERY_ALL_USER_ENABLE_PKG);
    }
    public void queryUserEnablePKGSByUID(int uid ,Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs){
        clearList(pkginfos,checkboxs);
        queryPKGSCoreByUID(uid,activity,pkginfos,checkboxs,false,true,false,true);
    }

    //查询当前机主安装的应用,禁用部分
    public void queryDisablePKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer types){
        clearList(pkginfos,checkboxs);
        queryPKGSCore(activity,pkginfos,checkboxs,types,QUERY_ALL_DISABLE_PKG);
    }

    public void queryDisablePKGSByUID(int uid ,Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs){
        clearList(pkginfos,checkboxs);
        queryPKGSCoreByUID(uid,activity,pkginfos,checkboxs,true,false,false,false);
    }

    public Drawable getPKGIcon(Context context , String pkgname){
        PackageManager packageManager = getPackageManager(context);
        PackageInfo packageInfo = getPackageInfo(context, pkgname, 0);
        return packageInfo.applicationInfo.loadIcon(packageManager);
    }

    public Drawable getPKGFileIcon(Context context , String filePath){
        PackageManager packageManager = getPackageManager(context);
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        return packageInfo.applicationInfo.loadIcon(packageManager);
    }

    public PKGINFO getPKGINFO(Context context , String pkgname){
        PackageManager packageManager = getPackageManager(context);
        PackageInfo packageInfo = getPackageInfo(context, pkgname, 0);
        return getPKGINFO(packageManager,packageInfo,packageInfo.applicationInfo.sourceDir);
    }

    public PKGINFO getPKGINFO(PackageManager packageManager,PackageInfo packageInfo , String filePath){
        try {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            applicationInfo.sourceDir=filePath;
            applicationInfo.publicSourceDir = filePath;
            Long filesieze = applicationInfo.sourceDir==null? 0: new File(applicationInfo.sourceDir).length();
            return new PKGINFO(applicationInfo.packageName, applicationInfo.loadLabel(packageManager).toString(), applicationInfo.sourceDir,applicationInfo.uid+"",packageInfo.versionName,filesieze) ;
        }catch (Exception e){
            return null;
        }
    }

    public PKGINFO getPKGINFOByUID(PackageManager packageManager,String pkgname){
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(pkgname, 0);
            return getPKGINFO(packageManager,packageInfo,packageInfo.applicationInfo.sourceDir);
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

    public int getPkgUid(Context context , String pkgname){
        try {
            return getPackageManager(context).getApplicationInfo(pkgname,0).uid;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void appInfoAdd(PackageManager packageManager,PackageInfo packageInfo,ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer state){
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        if(state.equals(QUERY_ALL_ENABLE_PKG)){
            if(applicationInfo.enabled){
                checkBoxs(pkginfos,checkboxs,packageInfo,packageManager);
            }
        }

        if(state.equals(QUERY_ALL_DISABLE_PKG)){
            if(!applicationInfo.enabled ||( (applicationInfo.flags & ApplicationInfo.FLAG_SUSPENDED) != 0 )){
                checkBoxs(pkginfos,checkboxs,packageInfo,packageManager);
            }
        }

        if(state.equals(QUERY_ALL_USER_ENABLE_PKG)){
            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && applicationInfo.enabled){
                checkBoxs(pkginfos,checkboxs,packageInfo,packageManager);
            }
        }

        if(state.equals(QUERY_ALL_USER_PKG)){
            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                checkBoxs(pkginfos,checkboxs,packageInfo,packageManager);
            }
        }

        if(state.equals(QUERY_ALL_DEFAULT_PKG)){
            checkBoxs(pkginfos,checkboxs,packageInfo,packageManager);
        }

        if(state.equals(QUERY_ALL_SYSTEM_PKG)){
            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                checkBoxs(pkginfos,checkboxs,packageInfo,packageManager);
            }
        }

        if(state.equals(QUERY_ALL_SYSTEM_ENABLE_PKG)){
            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0 && applicationInfo.enabled){
                checkBoxs(pkginfos,checkboxs,packageInfo,packageManager);
            }
        }

    }

    public void sortPKGINFOS(ArrayList<PKGINFO> pkginfos){
        if(pkginfos != null && pkginfos.size()>0){
            Collections.sort(pkginfos, new Comparator<PKGINFO>() {
                @Override
                public int compare(PKGINFO pkginfo, PKGINFO t1) {
                    if(pkginfo != null && t1 != null){
                        return pkginfo.getAppname().compareTo(t1.getAppname());
                    }
                    return 0;
                }
            });
        }
    }

    public void queryPKGSCore(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer types,Integer state){
        PackageManager packageManager = activity.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(types | PackageManager.MATCH_DISABLED_COMPONENTS | PackageManager.MATCH_UNINSTALLED_PACKAGES);
        for (PackageInfo packageInfo : installedPackages) {
            appInfoAdd(packageManager,packageInfo,pkginfos,checkboxs,state);
        }
        sortPKGINFOS(pkginfos);
    }

    public void queryPKGSCoreByUID(int uid,Activity activity , ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,boolean listDisabled , boolean listEnabled ,boolean listSystem ,boolean listThirdParty  ){
        List<MyPackageInfo> installedPackages = ee.getInstalledPackages(new TransmissionEntity(null,null,activity.getPackageName(),0,uid));
        for (MyPackageInfo packageInfo : installedPackages) {
            MyApplicationInfo myapplicationInfo = packageInfo.myapplicationInfo;

            // 提前计算好状态，代码更清晰
            boolean isSystem = (myapplicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
            boolean isSuspend = (myapplicationInfo.flags & ApplicationInfo.FLAG_SUSPENDED) != 0;
            boolean isEnabled = myapplicationInfo.enabled;

            // ====================== 核心筛选逻辑（多条件同时匹配） ======================
            boolean match = true;

            // 1. 启用/禁用 筛选
            if (listEnabled) {
                match = match && (isEnabled || !isSuspend);
            }
            if (listDisabled) {
                match = match && (!isEnabled || isSuspend);
            }

            // 2. 系统/第三方 筛选
            if (listSystem) {
                match = match && isSystem;
            }
            if (listThirdParty) {
                match = match && !isSystem;
            }

            // 3. 如果所有筛选都关闭 → 默认显示全部
            boolean allFilterOff = !listDisabled && !listEnabled && !listSystem && !listThirdParty;
            if (allFilterOff) {
                match = true;
            }

            // ====================== 满足条件就添加（只执行一次！） ======================
            if (match) {
                appInfoAdd(activity.getPackageManager(), packageInfo, pkginfos, checkboxs);
            }
        }

        sortPKGINFOS(pkginfos);
    }

    public void queryUninstalledPKGSByUID(int uid, Activity activity, ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        clearList(pkginfos,checkboxs);

        List<MyPackageInfo> installedPackages = ee.getInstalledPackages(new TransmissionEntity(null,null,activity.getPackageName(),0,uid));

        for (MyPackageInfo packageInfo : installedPackages) {
            MyApplicationInfo myapplicationInfo = packageInfo.myapplicationInfo;
            try {
                PackageInfo p = activity.getPackageManager().getPackageInfo(packageInfo.packageName,0);
            }catch (Exception e){
                String apkPathStr = myapplicationInfo.sourceDir;
                if(apkPathStr != null){
                    File apkPath = new File(apkPathStr);
                    if(apkPath.exists()){
                        try {
                            PackageInfo packageArchiveInfo = activity.getPackageManager().getPackageArchiveInfo(apkPathStr, 0);

                            appInfoAdd(activity.getPackageManager(), packageInfo, pkginfos, checkboxs);
                        }catch (Exception e2){

                        }
                    }
                }
            }

        }
        sortPKGINFOS(pkginfos);

    }

    public void appInfoAdd(PackageManager packageManager, MyPackageInfo packageInfo, ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        if (checkboxs != null) {
            checkboxs.add(false);
        }
        String appName = packageInfo.packageName;
        String uid = "";
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageInfo.packageName, PackageManager.MATCH_UNINSTALLED_PACKAGES);
            appName = applicationInfo.loadLabel(packageManager).toString();
            uid = String.valueOf(applicationInfo.uid);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        PKGINFO pkginfo = new PKGINFO(packageInfo.packageName, appName, packageInfo.myapplicationInfo.sourceDir, uid, packageInfo.versionName, 0L);
        pkginfos.add(pkginfo);
    }


    //查询当前机主安装的应用,启用部分
    public void queryEnablePKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer types){
        clearList(pkginfos,checkboxs);
        queryPKGSCore(activity,pkginfos,checkboxs,types,QUERY_ALL_ENABLE_PKG);
    }
    //查询当前机主安装的应用,启用部分
    public void queryEnablePKGSByUID(int uid ,Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs){
        clearList(pkginfos,checkboxs);
        queryPKGSCoreByUID(uid,activity,pkginfos,checkboxs,false,true,false,false);
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

    public PackageInfo getPackageInfo(Context context,String pkgname , Integer mode) {
        return getPackageInfo(getPackageManager(context),pkgname,mode);
    }

    //app组件禁用是会同步到主用户的,因为应用主程序不会额外重新安装，只会在对应的其它分身用户那里新建一个数据缓存文件夹
    public void getPKGInfoCore(Context context , String pkgname, ArrayList<String> list , ArrayList<Boolean> checkboxs,ArrayList<Boolean> switbs,Integer mode,Integer uid){
        clearList(list,checkboxs,switbs);
        MyPackageInfo myPackageInfo = ee.getMyPackageInfo(new TransmissionEntity(pkgname, null, context.getPackageName(), 0, uid));
        if(myPackageInfo != null){
            ArrayList<String> mainActivityList = new ArrayList<>();
            ArrayList<String> otherActivityList = new ArrayList<>();
            ArrayList<Boolean> mainBoolActivityList = new ArrayList<>();
            ArrayList<Boolean> otherBoolActivityList = new ArrayList<>();
            if(mode == 0 || mode == 2 || mode == 3){
                MyActivityInfo[] aa = myPackageInfo.getMyActivityInfos();
                if(mode ==2){
                    aa = myPackageInfo.getMyServices();
                }

                if(mode == 3){
                    aa = myPackageInfo.getMyReceivers();
                }

                if(aa != null){
                    for (MyActivityInfo activity : aa) {
                        int enabledSetting = ee.getComponentEnabledSetting(context,pkgname,activity.name,uid);
                        if(activity.isExported()){
                            mainActivityList.add(activity.name);
                        }else{
                            otherActivityList.add(activity.name);
                        }
                        switbs.add(!(enabledSetting == PackageManager.COMPONENT_ENABLED_STATE_DISABLED));
                        checkboxs.add(false);
                    }
                }
            }

            if(mode == 1 && myPackageInfo.requestedPermissions != null){
                List<MyAppopsInfo> myAppopsInfos = ee.getAppopsPKGPermissions(new TransmissionEntity(pkgname, null, context.getPackageName(), 0, uid));
                ArrayList<String> st1 = new ArrayList<String>();
                HashMap<String,Integer> map = new HashMap<>();


                for (MyAppopsInfo info : myAppopsInfos) {
                    if(info.getOPS_PER_STR() != null && info.getOPS_TO_PER_STR() == null){
                        map.put(info.getOPS_PER_STR(),1);
                    }

                    if(info.getOPS_PER_STR() != null && info.getOPS_TO_PER_STR() != null){
                        map.put(info.getOPS_TO_PER_STR(),1);
                    }

                }

                for (String permission : myPackageInfo.requestedPermissions) {
                    map.put(permission,1);
                }

                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    if(entry.getValue() == 1){
                        st1.add(entry.getKey());
                    }
                }



                for (String permission : st1) {
                    String permissionToOp = ee.permissionToOp(context,permission,uid);
                    if(permission.indexOf(".") == -1){
                        permissionToOp = permission;
                    }
                    checkboxs.add(false);
                    if(permissionToOp != null){
                        int checkop = ee.checkOp(context,pkgname,permissionToOp,uid);
                        boolean b = (checkop == AppOpsManager.MODE_ALLOWED || checkop == AppOpsManager.MODE_FOREGROUND);
                        otherActivityList.add(permission);
                        otherBoolActivityList.add(b);
                    }else{
                        int checkpermcode = ee.checkPermission(context,pkgname,permission,uid);
                        mainActivityList.add(permission);
                        mainBoolActivityList.add(checkpermcode == 0);
                    }
                }
                switbs.addAll(mainBoolActivityList);
                switbs.addAll(otherBoolActivityList);

            }
            list.addAll(mainActivityList);
            list.addAll(otherActivityList);
        }
    }

    public void getPKGActivitys(Context context , String pkgname, ArrayList<String> list , ArrayList<Boolean> checkboxs,ArrayList<Boolean> switbs,Integer uid) {
        getPKGInfoCore(context,pkgname,list,checkboxs,switbs,0,uid);
    }

    public void clearList(ArrayList<String> list, ArrayList<Boolean> checkboxs, ArrayList<Boolean> switbs) {
        list.clear();
        checkboxs.clear();
        switbs.clear();
    }

    public void getPKGPermission(Context context , String pkgname, ArrayList<String> list , ArrayList<Boolean> checkboxs,ArrayList<Boolean> switbs,Integer uid) {
        getPKGInfoCore(context,pkgname,list,checkboxs,switbs,1, uid);
    }

    public void getPKGServices(Context context , String pkgname, ArrayList<String> list , ArrayList<Boolean> checkboxs,ArrayList<Boolean> switbs,Integer uid){
        getPKGInfoCore(context,pkgname,list,checkboxs,switbs,2,uid);
    }

    public void getPKGReceivers(Context context , String pkgname, ArrayList<String> list , ArrayList<Boolean> checkboxs,ArrayList<Boolean> switbs,Integer uid){
        getPKGInfoCore(context,pkgname,list,checkboxs,switbs,3,uid);
    }


    public void queryAllPKGSByAppClone(Activity activity, ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        clearList(pkginfos, checkboxs);
        String[] appCloneUsers = ee.getAppCloneUsers();
        if (appCloneUsers != null) {
            for (String user : appCloneUsers) {
                try {
                    int uid = Integer.parseInt(user);
                    if (uid != ee.getCurrentUserID()) {
                        queryPKGSCoreByUID(uid, activity, pkginfos, checkboxs, false, false, false, false);
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void queryUserAllPKGSByAppClone(Activity activity, ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        clearList(pkginfos, checkboxs);
        String[] appCloneUsers = ee.getAppCloneUsers();
        if (appCloneUsers != null) {
            for (String user : appCloneUsers) {
                try {
                    int uid = Integer.parseInt(user);
                    if (uid != ee.getCurrentUserID()) {
                        queryPKGSCoreByUID(uid, activity, pkginfos, checkboxs, false, false, false, true);
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void querySystemAllPKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer types){
        queryPKGSCore(activity,pkginfos,checkboxs,types,QUERY_ALL_SYSTEM_PKG);
    }

    public void querySystemEnablePKGS(Activity activity, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,Integer types){
        queryPKGSCore(activity,pkginfos,checkboxs,types,QUERY_ALL_SYSTEM_ENABLE_PKG);
    }

    public void queryUninstalledPKGS(Activity activity, ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, int types) {
        clearList(pkginfos,checkboxs);
        PackageManager packageManager = activity.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(types | PackageManager.MATCH_DISABLED_COMPONENTS | PackageManager.MATCH_UNINSTALLED_PACKAGES);
        for (PackageInfo packageInfo : installedPackages) {
            try {
                PackageInfo p = packageManager.getPackageInfo(packageInfo.packageName,0);
            }catch (Exception e){
                String apkPathStr = packageInfo.applicationInfo.sourceDir;
                if(apkPathStr != null){
                    File apkPath = new File(apkPathStr);
                    if(apkPath.exists()){
                        try {
                            PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(apkPathStr, 0);
                            appInfoAdd(packageManager,packageInfo,pkginfos,checkboxs,QUERY_ALL_DEFAULT_PKG);
                        }catch (Exception e2){

                        }
                    }
                }
            }


        }
        sortPKGINFOS(pkginfos);
    }
}

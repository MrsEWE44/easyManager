package com.easymanager.core.server;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.os.IBinder;

import com.easymanager.core.api.AppopsAPI;
import com.easymanager.core.api.DhizukuSystemServerApi;
import com.easymanager.core.api.FunctionAPI;
import com.easymanager.core.api.PackageAPI;
import com.easymanager.core.api.ShizukuSystemServerApi;
import com.easymanager.core.api.baseAPI;
import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.core.utils.MyConfigUtils;
import com.easymanager.entitys.MyAppopsInfo;
import com.easymanager.entitys.MyPackageInfo;
import com.easymanager.utils.FileTools;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class easyManagerAPI extends baseAPI {

    private PackageAPI packageAPI = new PackageAPI();
    private AppopsAPI appopsAPI = new AppopsAPI();
    private FunctionAPI functionAPI = new FunctionAPI();

    private MyConfigUtils myConfigUtils = new MyConfigUtils();
    private FileTools ft = myConfigUtils.ft;


    public void killpkg(String pkgname,int uid){
        if(ShizukuSystemServerApi.isShizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU){
            packageAPI.killApp(pkgname,uid);
        }
    }

    public void setAppopsModeCore(String pkgname , String opstr , String opmode,int uid){
        killpkg(pkgname, uid);
        appopsAPI.SetMode(pkgname, opstr, opmode,uid);
    }

    public boolean isAppopsAllow(String modestr){
        return appopsAPI.isAllow(modestr);
    }

    public void setAppopsMode(TransmissionEntity te){
        int opsmode = te.getOpsmode();
        String pkgname = te.getPkgname();
        String opmodestr = te.getOpmodestr();
        int uid = te.getUid();
        killpkg(pkgname,uid);
        switch (opsmode){
            case 16:
                packageAPI.SetInactive(pkgname,opmodestr.equals("true"),uid);
                break;
            case 17:
                packageAPI.SetStandbyBucket(pkgname, opmodestr,uid);
                break;
            default:
                appopsAPI.setModeCore(pkgname,opmodestr,opsmode,uid);
                break;
        }
    }
    public int getPKGUID(String pkgname , int uid){return packageAPI.getPKGUID(pkgname, uid);}
    public void installAPK(String apkpath){packageAPI.InstallAPK(apkpath);}
    public void installAPK(String apkpath,int uid){packageAPI.InstallAPK(apkpath,uid);}
    public void installExistingPKG(String pkgname,int uid){packageAPI.InstallExistingPKG(pkgname,uid);}
    public void uninstallApp(String pkgname,int uid){
        killpkg(pkgname, uid);
        packageAPI.UninstallPKG(pkgname,uid);
    }
    public void setComponentOrPackageEnabledState(String pkgname_or_compname,int state,int uid){
        killpkg(pkgname_or_compname, uid);
        packageAPI.setComponentOrPackageEnabledState(pkgname_or_compname, state,uid);
    }

    public int getComponentOrPackageEnabledState(String pkgname_or_compname,int uid){
        return packageAPI.getComponentOrPackageEnabledState(pkgname_or_compname, uid);
    }
    public void setPackageHideState(String pkgname,boolean hide,int uid){
        killpkg(pkgname, uid);
        packageAPI.setPackageHideState(pkgname, hide , uid);
    }

    public int checkPermission(String pkgname , String permission_str, int uid){return packageAPI.checkPermission(pkgname, permission_str, uid);}

    public void setPackagesSuspendedAsUser(String pkgname, boolean suspended,int uid) {
        killpkg(pkgname, uid);
        packageAPI.setPackagesSuspendedAsUser(pkgname, suspended, uid);
    }
    public int isPackageSuspendedForUser(String packageName,int uid){return packageAPI.isPackageSuspendedForUser(packageName, uid);}

    public void clearPackageData(String packageName,int uid){
        killpkg(packageName, uid);
        packageAPI.clearPackageData(packageName, uid);
    }

    public void revokeRuntimePermission(String pkgname , String permission_str, int uid){
        killpkg(pkgname, uid);
        try {
            packageAPI.revokeRuntimePermission(pkgname,permission_str,packageAPI.getTranslatedUserId());
        }catch (Throwable e){
            appopsAPI.setPermissionStr(pkgname,permission_str,false,uid);
        }
    }

    public void grantRuntimePermission(String pkgname , String permission_str , int uid){
        killpkg(pkgname, uid);
        try {
            packageAPI.grantRuntimePermission(pkgname,permission_str,packageAPI.getTranslatedUserId());
        }catch (Throwable e){
            appopsAPI.setPermissionStr(pkgname,permission_str,true,uid);
        }
    }

    public void addRunningApps(String pkgname , String opstr , int uid){
        killpkg(pkgname,uid);
        String[] split = opstr.split("-");
        if(split != null){
            String timeStr = split[0];
            String timeTypeStr = split[1];
            int time = Integer.parseInt(timeStr.trim());
            int timeType = Integer.parseInt(timeTypeStr.trim());
            if(timeType == 1){
                time = time * 60;
            }

            if(timeType == 2){
                time = time * 60 * 60;
            }

            if(timeType == 3){
                time = time * 60 * 60 * 24;
            }

            if(myConfigUtils.getCleanAPPConfigTime(pkgname) != -1){
                myConfigUtils.changeCleanAPPConfigTime(pkgname,time);
            }else{
                myConfigUtils.addCleanAPPConfigTime(pkgname,time);
            }
        }

    }

    public void startStopRunningAPP(int uid){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    HashMap<String, Integer> cleanAPPs = myConfigUtils.getCleanAPPs();
                    int time = 0;
                    if(cleanAPPs.size() > 0){
                        for (Map.Entry<String, Integer> entry : cleanAPPs.entrySet()) {
                            time = entry.getValue();
                            break;
                        }
                        try {
                            Thread.sleep(time > 0 ?time * 1000:100);
                        } catch (InterruptedException e) {
                            System.err.println("startStopRunningAPP error :::  " + e.toString());
                        }

                        for (Map.Entry<String, Integer> entry : cleanAPPs.entrySet()) {
                            String pkgname = entry.getKey();
                            List<ActivityManager.RunningAppProcessInfo> runningApps = packageAPI.getRunningApps(0);
                            for (ActivityManager.RunningAppProcessInfo runningApp : runningApps) {
                                if(runningApp.processName.equals(pkgname) && runningApp.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                                    killpkg(pkgname,uid);
                                }
                            }

                        }
                    }


                }
            }
        }).start();
    }

    public void deleteCleanAPPConfig(){
        myConfigUtils.deleteCleanAPPConfig();
    }

    public ArrayList<String> getPathALLFiles(String path){
        ArrayList<File> s = new ArrayList<>();
        ArrayList<String> s2 = new ArrayList<>();
        ft.getAllFileByEndName(path,null,s);
        for (File file : s) {
            s2.add(file.toString());
        }
        return s2;
    }

    public HashMap<String,Integer> getGrantUsers(){
        return myConfigUtils.getGrantUsers();
    }

    public Integer getGrantUserState(String requestpkg) {
        return myConfigUtils.getGrantUserState(requestpkg);
    }

    public int changeGrantUserState(String requestpkg){
        return myConfigUtils.changeGrantUserState(requestpkg);
    }

    public int requestGrantUserState(String requestpkg){
        return myConfigUtils.requestGrantUserState(requestpkg);
    }

    public String[] getDisallowedPackages(){
        return packageAPI.getDisallowedPackages();
    }

    public int getMaxSupportedUsers(){
        return packageAPI.getMaxSupportedUsers();
    }

    public String getSYSProp(String key) {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method getMethod = clazz.getMethod("get", String.class);
            String value =  (String) getMethod.invoke(null, key);
//            System.out.println("getSYSProp ::: " + key + " -- " + value);
            return value;
        } catch (Exception e) {
            System.err.println("getProp error: " + e.getMessage());
        }
        return "";
    }

    public void createAppClone(){
        packageAPI.createUser();
    }

    public void startUser(int uid){
        packageAPI.startUser(uid);
    }

    public int getCurrentUserID(){return packageAPI.getCurrentUser();}

    public void removeAppClone(int userid){
        packageAPI.removeUser(userid);
    }

    public String[] getUsers(){
        return packageAPI.getUsers();
    }

    public List<MyPackageInfo> getInstalledPackages(int uid){return packageAPI.getInstalledPackages(uid);}

    public MyPackageInfo getMyPackageInfo(String pkgname , int uid){return packageAPI.getMyPackageInfo(pkgname, uid);}

    public int getComponentEnabledSetting(ComponentName componentName, int userId){return packageAPI.getComponentEnabledSetting(componentName,userId);}

    public int checkOp(String opstr, String packageName, int uid){return appopsAPI.checkOp(opstr, packageName, uid);}

    public List<MyAppopsInfo> getAppopsPKGPermissions(String pkgname, int userid){return appopsAPI.getAppopsPKGPermissions(pkgname,userid);}

    public String permissionToOp(String permission_str){return appopsAPI.permissionToOp(permission_str);}

    public int strOpToOp(String op){return appopsAPI.strOpToOp(op);}

    public List<String> getActiveAdmins(int userid){
        return functionAPI.getActiveAdmins(userid);
    }


}

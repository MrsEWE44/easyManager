package com.easymanager.mylife;

import android.content.ComponentName;
import android.os.Looper;
import android.os.Process;

import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.core.entity.easyManagerClientEntity;
import com.easymanager.core.server.easyManagerAPI;
import com.easymanager.core.utils.CMD;
import com.easymanager.enums.easyManagerEnums;

public class startAdbService {

    private final static easyManagerAPI managerAPI = new easyManagerAPI();

    public static void main(String[] args) {
        // 利用looper让线程循环
        Looper.prepareMainLooper();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new adbService(new adbService.SocketListener() {
                    @Override
                    public CMD sendCMD(String cmdstr) {
                        if(cmdstr != null){
                            return new CMD(cmdstr,managerAPI.isRoot());
                        }
                        return null;
                    }

                    @Override
                    public Object doSomeThing(easyManagerClientEntity adbEntity2) {
                        if(adbEntity2 != null && adbEntity2.getCmdstr() == null){
                            TransmissionEntity entity = adbEntity2.getTransmissionEntity();
                            if(entity != null && entity.getRequestpkg() != null ){
                                if(managerAPI.getGrantUserState(entity.getRequestpkg()) == 0 || entity.getRequestpkg().equals("com.easymanager")){
                                    switch (adbEntity2.getEasyManagerMode()){
                                        case easyManagerEnums.IS_ROOT:
                                            return managerAPI.isRoot();
                                        case easyManagerEnums.IS_ADB:
                                            return managerAPI.isADB();
                                        case easyManagerEnums.APP_FIREWALL:
                                            if(managerAPI.isRoot()){
                                                int pkguid = managerAPI.getPKGUID(entity.getPkgname(), entity.getUid());
                                                String  disable_cmd = "iptables -I OUTPUT -m owner --uid-owner "+pkguid+" -j DROP";
                                                String  enable_cmd = "iptables -I OUTPUT -m owner --uid-owner "+pkguid+" -j ACCEPT";
                                                CMD cmd = sendCMD(entity.getOpsmode()==0 ? enable_cmd : disable_cmd);
                                            }else {
                                                return getActiveROOT();
                                            }
                                            break;
                                        case easyManagerEnums.GET_SYSTEM_SERVICE:
                                            return managerAPI.getSystemService(entity.getPkgname());
                                        case easyManagerEnums.KILL_PROCESS:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.killpkg(entity.getPkgname(),entity.getUid());
                                            }else {
                                                return getActiveADB();
                                            }
                                            break;
                                        case easyManagerEnums.SET_APPOPS:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.setAppopsMode(entity);
                                            }else {
                                                return getActiveADB();
                                            }
                                            break;
                                        case easyManagerEnums.INSTALL_APK:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.installAPK(entity.getPkgname(),entity.getUid());
                                            }else {
                                                return getActiveADB();
                                            }
                                            break;
                                        case easyManagerEnums.UNINSTALL_APK:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.uninstallApp(entity.getPkgname(),entity.getUid());
                                            }else {
                                                return getActiveADB();
                                            }
                                            break;
                                        case easyManagerEnums.SET_COMPONENT_OR_PACKAGE_ENABLE_STATE:
                                            if(managerAPI.isRoot()){
                                                managerAPI.setComponentOrPackageEnabledState(entity.getPkgname(),entity.getOpsmode(),entity.getUid());
                                            }else {
                                                return getActiveROOT();
                                            }
                                            break;
                                        case easyManagerEnums.SET_PACKAGE_HIDE_STATE:
                                            if(managerAPI.isRoot()){
                                                managerAPI.setPackageHideState(entity.getPkgname(),entity.getOpsmode()==0,entity.getUid());
                                            }else {
                                                return getActiveROOT();
                                            }
                                            break;
                                        case easyManagerEnums.SET_PACKAGE_REVOKE_RUNTIME_PERMISSION:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.revokeRuntimePermission(entity.getPkgname(), entity.getOpmodestr(),entity.getUid());
                                            }else {
                                                return getActiveADB();
                                            }
                                            break;
                                        case easyManagerEnums.SET_PACKAGE_GRANT_RUNTIME_PERMISSION:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.grantRuntimePermission(entity.getPkgname(),entity.getOpmodestr(),entity.getUid());
                                            }else {
                                                return getActiveADB();
                                            }
                                            break;
                                        case easyManagerEnums.BACKUP_APK:
                                            if(managerAPI.isRoot()){
                                                int uid = entity.getUid();
                                                String[] users = managerAPI.getUsers();
                                                if(users == null || users.length <= 1){
                                                    uid=0;
                                                }
                                                managerAPI.backupApk(entity.getPkgname(), uid, entity.getOpmodestr());
                                            }else {
                                                return getActiveROOT();
                                            }
                                            break;
                                        case easyManagerEnums.RESTORY_APK:
                                            if(managerAPI.isRoot()){
                                                int uid = entity.getUid();
                                                String[] users = managerAPI.getUsers();
                                                if(users == null || users.length <= 1){
                                                    uid=0;
                                                }
                                                managerAPI.restoryApp(entity.getPkgname(), uid, entity.getOpmodestr());
                                            }else {
                                                return getActiveROOT();
                                            }
                                            break;
                                        case easyManagerEnums.GRANT_USER:
                                            int a = managerAPI.requestGrantUserState(entity.getRequestpkg());
                                            break;
                                        case easyManagerEnums.CHANGE_USER:
                                            managerAPI.changeGrantUserState(entity.getPkgname());
                                            break;
                                        case easyManagerEnums.GET_SERVER_STATUS:
                                            return true;
                                        case easyManagerEnums.GET_GRANT_USERS:
                                            return managerAPI.getGrantUsers();
                                        case easyManagerEnums.SET_APPOPS_CORE:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                String modestr = entity.getOpmodestr();
                                                String[] split = modestr.split("---");
                                                if(split  != null && split.length > 0){
                                                    managerAPI.setAppopsModeCore(entity.getPkgname(), split[0], split[1],entity.getUid());
                                                }
                                            }else {
                                                return getActiveADB();
                                            }
                                            break;
                                        case easyManagerEnums.APP_CLONE:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.createAppClone();
                                            }else {
                                                return getActiveADB();
                                            }
                                            break;
                                        case easyManagerEnums.APP_CLONE_REMOVE:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.removeAppClone(entity.getUid());
                                            }else {
                                                return getActiveADB();
                                            }
                                            break;
                                        case easyManagerEnums.QUERY_PACKAGES_UID:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                return managerAPI.getInstalledPackages(entity.getUid());
                                            }else {
                                                return getActiveADB();
                                            }
                                        case easyManagerEnums.GET_PACKAGEINFO_UID:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                return managerAPI.getMyPackageInfo(entity.getPkgname(), entity.getUid());
                                            }else {
                                                return getActiveADB();
                                            }
                                        case easyManagerEnums.START_USER_ID:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.startUser(entity.getUid());
                                            }else {
                                                return getActiveADB();
                                            }
                                            break;
                                        case easyManagerEnums.GET_COMPONENT_ENABLED_SETTING:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                return managerAPI.getComponentEnabledSetting(new ComponentName(entity.getPkgname(),entity.getOpmodestr()),entity.getUid());
                                            }else {
                                                return getActiveADB();
                                            }
                                        case easyManagerEnums.CHECK_OP:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                return managerAPI.checkOp(entity.getOpmodestr(), entity.getPkgname(), entity.getUid());
                                            }else {
                                                return getActiveADB();
                                            }

                                    }
                                }else {
                                    if(adbEntity2.getEasyManagerMode() == easyManagerEnums.GRANT_USER){
                                        managerAPI.requestGrantUserState(entity.getRequestpkg());
                                    } else {
                                        return  new String("the " + entity.getRequestpkg() + " has not permission use the val .");
                                    }
                                }

                            }else if(adbEntity2.getEasyManagerMode() == easyManagerEnums.IS_ADB){
                                return managerAPI.isADB();
                            }else if(adbEntity2.getEasyManagerMode() == easyManagerEnums.IS_ROOT){
                                return managerAPI.isRoot();
                            }else if(adbEntity2.getEasyManagerMode() == easyManagerEnums.GET_SERVER_STATUS){
                                return true;
                            }else if(adbEntity2.getEasyManagerMode() == easyManagerEnums.DEAD){
                                    System.exit(0);
                            }else if(adbEntity2.getEasyManagerMode() == easyManagerEnums.GET_CURRENT_USER_ID) {
                                    if(managerAPI.isRoot() || managerAPI.isADB()){
                                        return managerAPI.getCurrentUserID();
                                    }else {
                                        return getActiveADB();
                                    }
                            }else if(adbEntity2.getEasyManagerMode() == easyManagerEnums.APP_CLONE_GETUSERS){
                                    if(managerAPI.isRoot() || managerAPI.isADB()){
                                        return managerAPI.getUsers();
                                    }else {
                                        return getActiveADB();
                                    }
                            }else{
                                return new String("please put requestpkg val .");
                            }
                        }

                        return new String(Process.myUid()+" --- " + Process.myUserHandle().hashCode());
                    }
                });

            }
        }).start();
        Looper.loop();
    }

    private static  String getActiveADB(){
        return "not permisson use !! please active adb or root mode !!";
    }
    private static   String getActiveROOT(){
        return "not permisson use !! please active root mode !!";
    }


}

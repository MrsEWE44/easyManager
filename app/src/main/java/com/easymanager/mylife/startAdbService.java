package com.easymanager.mylife;

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
                    public CMD sendCMD(String cmdstr, boolean isRoot) {
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
                                        case easyManagerEnums.GET_SYSTEM_SERVICE:
                                            return managerAPI.getSystemService(entity.getPkgname());
                                        case easyManagerEnums.KILL_PROCESS:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.killpkg(entity.getPkgname());
                                            }else {
                                                return new String("not permisson use !! please active adb or root mode !!");
                                            }
                                            break;
                                        case easyManagerEnums.SET_APPOPS:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.setAppopsMode(entity);
                                            }else {
                                                return new String("not permisson use !! please active adb or root mode !!");
                                            }
                                            break;
                                        case easyManagerEnums.INSTALL_APK:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.installAPK(entity.getPkgname());
                                            }else {
                                                return new String("not permisson use !! please active adb or root mode !!");
                                            }
                                            break;
                                        case easyManagerEnums.UNINSTALL_APK:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.uninstallApp(entity.getPkgname());
                                            }else {
                                                return new String("not permisson use !! please active adb or root mode !!");
                                            }
                                            break;
                                        case easyManagerEnums.SET_COMPONENT_OR_PACKAGE_ENABLE_STATE:
                                            if(managerAPI.isRoot()){
                                                managerAPI.setComponentOrPackageEnabledState(entity.getPkgname(),entity.getOpsmode());
                                            }else {
                                                return new String("not permisson use !! please active root mode !!");
                                            }
                                            break;
                                        case easyManagerEnums.SET_PACKAGE_HIDE_STATE:
                                            if(managerAPI.isRoot()){
                                                managerAPI.setPackageHideState(entity.getPkgname(),entity.getOpsmode()==0);
                                            }else {
                                                return new String("not permisson use !! please active root mode !!");
                                            }
                                            break;
                                        case easyManagerEnums.SET_PACKAGE_REVOKE_RUNTIME_PERMISSION:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.revokeRuntimePermission(entity.getPkgname(), entity.getOpmodestr());
                                            }else {
                                                return new String("not permisson use !! please active adb or root mode !!");
                                            }
                                            break;
                                        case easyManagerEnums.SET_PACKAGE_GRANT_RUNTIME_PERMISSION:
                                            if(managerAPI.isRoot() || managerAPI.isADB()){
                                                managerAPI.grantRuntimePermission(entity.getPkgname(),entity.getOpmodestr());
                                            }else {
                                                return new String("not permisson use !! please active adb or root mode !!");
                                            }
                                            break;
                                        case easyManagerEnums.BACKUP_APK:
                                            if(managerAPI.isRoot()){
                                                managerAPI.backupApk(entity.getPkgname(), entity.getOpsmode(), entity.getOpmodestr());
                                            }else {
                                                return new String("not permisson use !! please active root mode !!");
                                            }
                                            break;
                                        case easyManagerEnums.RESTORY_APK:
                                            if(managerAPI.isRoot()){
                                                managerAPI.restoryApp(entity.getPkgname(), entity.getOpsmode(), entity.getOpmodestr());
                                            }else {
                                                return new String("not permisson use !! please active root mode !!");
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
                                                    managerAPI.setAppopsModeCore(entity.getPkgname(), split[0], split[1]);
                                                }
                                            }else {
                                                return new String("not permisson use !! please active adb or root mode !!");
                                            }
                                            break;

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
                            }else {
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

}

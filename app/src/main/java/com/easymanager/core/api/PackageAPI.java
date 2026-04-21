package com.easymanager.core.api;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.usage.IUsageStatsManager;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.IIntentReceiver;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstaller;
import android.content.pm.IPackageInstallerSession;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SuspendDialogInfo;
import android.content.pm.UserInfo;
import android.content.pm.VersionedPackage;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IUserManager;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.permission.IPermissionManager;

import com.easymanager.core.utils.IIntentSenderAdaptor;
import com.easymanager.core.utils.IntentSenderUtils;
import com.easymanager.core.utils.PackageInstallerUtils;
import com.easymanager.entitys.MyActivityInfo;
import com.easymanager.entitys.MyApplicationInfo;
import com.easymanager.entitys.MyPackageInfo;
import com.rosan.dhizuku.api.Dhizuku;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuBinderWrapper;

public class PackageAPI extends  baseAPI implements Serializable {

    public static final int COMPONENT_ENABLED_STATE_DEFAULT = 0;

    public static final int COMPONENT_ENABLED_STATE_ENABLED = 1;

    public static final int COMPONENT_ENABLED_STATE_DISABLED = 2;

    public static final int COMPONENT_ENABLED_STATE_DISABLED_USER = 3;

    public static final int COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED = 4;

    public static final int DELETE_ALL_USERS = 0x00000002;

    public static final int DELETE_SUCCEEDED = 1;
    public static final int DELETE_SYSTEM_APP = 0x00000004;
    public static final int MATCH_STATIC_SHARED_AND_SDK_LIBRARIES = 0x04000000;

    public static final int INSTALL_ALL_USERS = 0x00000040;
    public static final int INSTALL_FROM_ADB = 0x00000020;
    public static final int INSTALL_ALLOW_DOWNGRADE = 0x00000080;
    public static final int INSTALL_SUCCEEDED = 1;
    public static final int INSTALL_FAILED_ALREADY_EXISTS = -1;
    public static final int INSTALL_ALLOW_TEST = 0x00000004;
    public static final int INSTALL_REPLACE_EXISTING = 0x00000002;
    public static final int INSTALL_FAILED_DUPLICATE_PERMISSION = -112;
    public static final String EXTRA_FAILURE_EXISTING_PERMISSION
            = "android.content.pm.extra.FAILURE_EXISTING_PERMISSION";
    public static final String EXTRA_FAILURE_EXISTING_PACKAGE
            = "android.content.pm.extra.FAILURE_EXISTING_PACKAGE";

    public static final int INSTALL_ALL_WHITELIST_RESTRICTED_PERMISSIONS = 0x00400000;
    public static final int INSTALL_REASON_UNKNOWN = 0;
    public static final String USER_TYPE_PROFILE_MANAGED = "android.os.usertype.profile.MANAGED";
    public static final String USER_TYPE_PROFILE_CLONE = "android.os.usertype.profile.CLONE";
    public static final int FLAG_MANAGED_PROFILE = 0x00000020;
    public static final int FLAG_SUSPEND_QUARANTINED = 0x00000001;

    private String[] disallowedPackages = null;

    public IUsageStatsManager getIUsageStatsManager(){
        IUsageStatsManager manager = null;
        if(ShizukuSystemServerApi.isShizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU){
            manager = ShizukuSystemServerApi.USAGESTATS_MANAGER.get();
        }

        if(DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU){
            manager = DhizukuSystemServerApi.USAGESTATS_MANAGER.get();
        }

        return manager;
    }

    public IActivityManager getIActivityManager(){
        IActivityManager iActivityManager = null;
        if(ShizukuSystemServerApi.isShizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU){
            iActivityManager =  ShizukuSystemServerApi.ACTIVITY_MANAGER.get();
        }

        if(DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU){
            iActivityManager = DhizukuSystemServerApi.ACTIVITY_MANAGER.get();
        }

        return iActivityManager;
    }

    //通过调用activitymanager系统api实现进程清理
    public void killApp(String pkgname,int uid){
        IActivityManager iActivityManager = getIActivityManager();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
            iActivityManager.forceStopPackage(pkgname,uid);
        }else{
            iActivityManager.forceStopPackage(pkgname);
        }
    }


    public IPackageManager getIPackageManager(){
        IPackageManager iPackageManager = null;
        if(ShizukuSystemServerApi.isShizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU){
            iPackageManager =  ShizukuSystemServerApi.PACKAGE_MANAGER.get();
        }

        if(DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU){
            iPackageManager = DhizukuSystemServerApi.PACKAGE_MANAGER.get();
        }

        return iPackageManager;
    }

    public IUserManager getIUserManager(){
        IUserManager iUserManager = null;
        if(ShizukuSystemServerApi.isShizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU){
            iUserManager =  ShizukuSystemServerApi.USER_MANAGER.get();
        }

        if(DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU){
            iUserManager = DhizukuSystemServerApi.USER_MANAGER.get();
        }

        return iUserManager;
    }

    public IPermissionManager getIPermissionManager(){
        IPermissionManager iPermissionManager = null;
        if(ShizukuSystemServerApi.isShizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU){
            iPermissionManager =  ShizukuSystemServerApi.PERMISSION_MANAGER.get();
        }

        if(DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU){
            iPermissionManager = DhizukuSystemServerApi.PERMISSION_MANAGER.get();
        }

        return iPermissionManager;
    }

    public int getPKGUID(String pkgname , int uid){
        IPackageManager iPackageManager = getIPackageManager();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            return iPackageManager.getPackageUid(pkgname,0L,uid);
        }else if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.N){
            return iPackageManager.getPackageUid(pkgname,0,uid);
        }else if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.JELLY_BEAN){
            return iPackageManager.getPackageUid(pkgname,uid);
        }else {
            return iPackageManager.getPackageUid(pkgname);
        }
    }

    public IPackageInstaller getIPackageInstaller() throws RemoteException {
        IPackageInstaller iPackageInstaller = null;
        if(ShizukuSystemServerApi.isShizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU){
            iPackageInstaller = ShizukuSystemServerApi.getIPackageInstaller();
        }

        if(DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU){
            iPackageInstaller = DhizukuSystemServerApi.getIPackageInstaller();
        }

        return iPackageInstaller;
    }


    public void InstallAPK(String apkPath ){
        try{
            InstallAPK(apkPath,getTranslatedUserId());
        }catch (Throwable e){}

    }

    public int getTranslatedUserId(){
        return (isRoot()?0:getMyuidHascode());
    }

    public String getInstallerPackageName(){
        if(ShizukuSystemServerApi.isShizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU){
            return "com.android.shell";
        }else if(DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU){
            return DhizukuSystemServerApi.getDhizukuComponentName();
        }else{
            return "com.easymanager";
        }

    }

    public String getCallingPackage(){
        return (isRoot()?"root":"com.android.shell");
    }

    public int getInstallFlags(){
        return INSTALL_ALLOW_TEST | INSTALL_REPLACE_EXISTING |INSTALL_ALLOW_DOWNGRADE;
    }


    public void InstallExistingPKGQ(String pkgname,int userId){
        int installFlags = INSTALL_ALL_WHITELIST_RESTRICTED_PERMISSIONS;
        int installReason = INSTALL_REASON_UNKNOWN;
        int res = getIPackageManager().installExistingPackageAsUser(pkgname,userId,installFlags,installReason,null);
        System.out.println("InstallExistingPKGQ res ::: " + res);
    }

    public void InstallExistingPKGO(String pkgname,int userId){
        int installFlags = 0;
        int installReason = INSTALL_REASON_UNKNOWN;
        int res = getIPackageManager().installExistingPackageAsUser(pkgname,userId,installFlags,installReason);
        System.out.println("InstallExistingPKGO res ::: " + res);
    }

    public void InstallExistingPKGJKLMN(String pkgname,int userId){
        int res = getIPackageManager().installExistingPackageAsUser(pkgname,userId);
        System.out.println("InstallExistingPKGJKLMN res ::: " + res);
    }


    public int getComponentEnabledSetting(ComponentName componentName, int userId){
        IPackageManager iPackageManager = getIPackageManager();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            return iPackageManager.getComponentEnabledSetting(componentName,userId);
        }else {
            return iPackageManager.getComponentEnabledSetting(componentName);
        }
    }

    public void InstallAPK(String apkPath, int userId){
        PackageInstaller packageInstaller;
        PackageInstaller.Session session = null;
        StringBuilder res = new StringBuilder();
        String installerPackageName;
        String installerAttributionTag = null;
        boolean isRoot;

        try {
            IPackageInstaller _packageInstaller = getIPackageInstaller();
            isRoot = Shizuku.getUid() == 0;

            // the reason for use "com.android.shell" as installer package under adb is that getMySessions will check installer package's owner
            installerPackageName = getInstallerPackageName();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                installerAttributionTag = null;
            }
            userId = isRoot ? Process.myUserHandle().hashCode() : 0;
            packageInstaller = PackageInstallerUtils.createPackageInstaller(_packageInstaller, installerPackageName, installerAttributionTag, userId);
            int sessionId;
            res.append("createSession: ");

            PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
            int installFlags = getInstallFlags();
            installFlags |= 0x00000004/*PackageManager.INSTALL_ALLOW_TEST*/ | 0x00000002/*PackageManager.INSTALL_REPLACE_EXISTING*/;
            PackageInstallerUtils.setInstallFlags(params, installFlags);

            sessionId = packageInstaller.createSession(params);
            res.append(sessionId).append('\n');

            res.append('\n').append("write: ");

            IPackageInstallerSession _session = IPackageInstallerSession.Stub.asInterface(new ShizukuBinderWrapper(_packageInstaller.openSession(sessionId).asBinder()));

            if(DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU){
                _session = IPackageInstallerSession.Stub.asInterface(Dhizuku.binderWrapper(_packageInstaller.openSession(sessionId).asBinder()));
            }

            session = PackageInstallerUtils.createSession(_session);

            InputStream is = new FileInputStream(new File(apkPath));
            OutputStream os = session.openWrite("package", 0, -1);

            byte[] buf = new byte[8192];
            int len;
            try {
                while ((len = is.read(buf)) > 0) {
                    os.write(buf, 0, len);
                    os.flush();
                    session.fsync(os);
                }
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            Thread.sleep(100);
            res.append('\n').append("commit: ");

            Intent[] results = new Intent[]{null};
            CountDownLatch countDownLatch = new CountDownLatch(1);
            IntentSender intentSender = IntentSenderUtils.newInstance(new IIntentSenderAdaptor() {

                @Override
                public void send(int code, Intent intent, String resolvedType, IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) {
                    send(intent);
                }

                @Override
                public void send(int code, Intent intent, String resolvedType, IBinder whitelistToken, IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) {
                    send(intent);
                }

                @Override
                public void send(Intent intent) {
                    results[0] = intent;
                    countDownLatch.countDown();
                }
            });
            session.commit(intentSender);

            countDownLatch.await();
            Intent result = results[0];
            int status = result.getIntExtra(PackageInstaller.EXTRA_STATUS, PackageInstaller.STATUS_FAILURE);
            String message = result.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE);
            res.append('\n').append("status: ").append(status).append(" (").append(message).append(")");

        } catch (Throwable tr) {
            tr.printStackTrace();
            res.append(tr);
        } finally {
            if (session != null) {
                try {
                    session.close();

                } catch (Throwable tr) {
                    res.append(tr);
                }
            }
        }
//        System.out.println(res.toString());
    }

    public void InstallExistingPKG(String pkgname, int userId){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            InstallExistingPKGQ(pkgname,userId);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            InstallExistingPKGO(pkgname,userId);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            InstallExistingPKGJKLMN(pkgname,userId);
        }else {
            int res = getIPackageManager().installExistingPackage(pkgname);
            System.out.println("InstallExistingPKGJ res ::: " + res);
        }
    }

    public void UninstallPKG(String pkgname ,int uid){
        UninstallPKG(pkgname,uid,-1);
    }

    /**
     * uninstall pkg on android 4.x
     *
     * */
    public void UninstallPKGK(String pkgname,int userId){
        IPackageManager iPackageManager = getIPackageManager();
        PackageDeleteObserver obs = new PackageDeleteObserver();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
            iPackageManager.deletePackage(pkgname,obs,DELETE_ALL_USERS);
        }else{
            iPackageManager.deletePackageAsUser(pkgname,obs,userId,DELETE_ALL_USERS);
        }

    }

    /**
     * uninstall pkg on android 5.0-13.0
     *
     * */
    public void UninstallPKG(String pkgname , int userId , int versionCode) {
        try {
            int sdkInt = Build.VERSION.SDK_INT;
            if(sdkInt >= Build.VERSION_CODES.LOLLIPOP){
                int flags = 0;

                //加上这个flag,会删除所有用户下的应用。,非必要不需要加
//                flags |= DELETE_ALL_USERS;
                IPackageInstaller iPackageInstaller = getIPackageInstaller();
                LocalIntentReceiver r = new LocalIntentReceiver();
                int flags2 = Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1 ? 0 : MATCH_STATIC_SHARED_AND_SDK_LIBRARIES;
                PackageInfo packageInfo = getPackageInfo(pkgname,getTranslatedUserId(),flags2);
                if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                    flags |= DELETE_SYSTEM_APP;
                }

                String callerPackageName = null;

                if(DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU){
                    callerPackageName = DhizukuSystemServerApi.getDhizukuComponentName();
                }

                if (sdkInt >= Build.VERSION_CODES.O) {
                    iPackageInstaller.uninstall(new VersionedPackage(pkgname,
                                    versionCode), callerPackageName, flags,
                            r.getIntentSender(), userId);
//                    resultLocalIntent(r);
                }else if(sdkInt >= Build.VERSION_CODES.M){
                    /**
                     * 安卓6.0以下调用IIntentSender.send会出现找不到抽象接口得问题，但是，实际上
                     * 是可以正常处理提交得操作请求，故而，需要修改一下这部分得处理逻辑，不应该提交完操作后，就
                     * 被迫强制终止服务，如果是root模式，应该要自动重启服务
                     * */
                    iPackageInstaller.uninstall(pkgname,callerPackageName,flags,r.getIntentSender(),userId);
                    if(sdkInt >= Build.VERSION_CODES.N){
                        resultLocalIntent(r);//安卓6.0以及以下版本不能调用这个，不然会跟以上高版本安卓产生冲突
                    }
                }else if(sdkInt >= Build.VERSION_CODES.LOLLIPOP){
                    iPackageInstaller.uninstall(pkgname,flags,r.getIntentSender(),userId);
                }
            }else{
                UninstallPKGK(pkgname,userId);
            }

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int checkPermission(String pkgname , String permission_str, int uid){
        IPackageManager manager = getIPackageManager();
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                return manager.checkPermission(permission_str,pkgname,uid);
            }else {
                return manager.checkPermission(permission_str,pkgname);
            }
        }catch (Throwable t){
            t.printStackTrace();
        }
        return -1;
    }

    public void setComponentOrPackageEnabledState(String pkgname_or_compname , int state,int uid){
        IPackageManager iPackageManager = getIPackageManager();
        ComponentName componentName = ComponentName.unflattenFromString(pkgname_or_compname);
        if(componentName == null){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                iPackageManager.setApplicationEnabledSetting(pkgname_or_compname,state,0,uid,"shell:" + getMyuid());
            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                iPackageManager.setApplicationEnabledSetting(pkgname_or_compname,state,0,uid);
            }else{
                iPackageManager.setApplicationEnabledSetting(pkgname_or_compname,state,0);
            }
        }else{
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU){
                iPackageManager.setComponentEnabledSetting(componentName,state,0,uid,"shell");
            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                iPackageManager.setComponentEnabledSetting(componentName, state, 0, uid);
            }else {
                iPackageManager.setComponentEnabledSetting(componentName, state, 0);
            }
        }

    }

    public int getComponentOrPackageEnabledState(String pkgname_or_compname,int uid){
        IPackageManager iPackageManager = getIPackageManager();
        ComponentName componentName = ComponentName.unflattenFromString(pkgname_or_compname);
        if(componentName == null){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                return iPackageManager.getApplicationEnabledSetting(pkgname_or_compname,uid);
            }else{
                return iPackageManager.getApplicationEnabledSetting(pkgname_or_compname);
            }
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                return iPackageManager.getComponentEnabledSetting(componentName,uid);
            }else {
                return iPackageManager.getComponentEnabledSetting(componentName);
            }
        }
    }

    public int isPackageSuspendedForUser(String packageName,int uid){
        return getIPackageManager().isPackageSuspendedForUser(packageName,uid)?0:1;
    }

    public void clearPackageData(String packageName,int uid){
        try{
            ClearDataObserver obs = new ClearDataObserver();
            IActivityManager iActivityManager = getIActivityManager();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                iActivityManager.clearApplicationUserData(packageName,false,obs,uid);
            }else{
                iActivityManager.clearApplicationUserData(packageName,obs,uid);
            }
            synchronized (obs) {
                while (!obs.finished) {
                    try {
                        obs.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            if (obs.result) {

            } else {

            }
        }catch (Throwable t){

        }

    }

    public void setPackagesSuspendedAsUser(String packageName, boolean suspended,int uid) {
        setPackagesSuspendedAsUser(new String[]{packageName},suspended,uid);
    }

    public void setPackagesSuspendedAsUser(String[] packageNames, boolean suspended,int uid){
        IPackageManager iPackageManager = getIPackageManager();
        int translatedUserId = uid;
        String callingPackage = getCallingPackage();
        PersistableBundle appExtras = null;
        PersistableBundle launcherExtras = null;
        SuspendDialogInfo dialogInfo = null;
        String dialogMessage = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM){
            iPackageManager.setPackagesSuspendedAsUser(packageNames,suspended,appExtras
                    ,launcherExtras,dialogInfo,0,callingPackage,0,translatedUserId);
        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM && Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
            iPackageManager.setPackagesSuspendedAsUser(packageNames,suspended,appExtras,launcherExtras,dialogInfo,callingPackage,translatedUserId);
        }else if(Build.VERSION.SDK_INT ==  Build.VERSION_CODES.P){
            iPackageManager.setPackagesSuspendedAsUser(packageNames,suspended,appExtras,launcherExtras,dialogMessage,callingPackage,translatedUserId);
        }else if(Build.VERSION.SDK_INT <  Build.VERSION_CODES.P && Build.VERSION.SDK_INT >  Build.VERSION_CODES.M){
            iPackageManager.setPackagesSuspendedAsUser(packageNames,suspended,translatedUserId);
        }

    }

    public void revokeRuntimePermission(String pkgname , String permission_str, int uid){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM){
            IPermissionManager iPermissionManager = getIPermissionManager();
            iPermissionManager.revokeRuntimePermission(pkgname,permission_str,null,uid,null);
        }else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q && Build.VERSION.SDK_INT <= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
            IPermissionManager iPermissionManager = getIPermissionManager();
            iPermissionManager.revokeRuntimePermission(pkgname,permission_str,uid,null);
        }else{
            IPackageManager iPackageManager = getIPackageManager();
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
                iPackageManager.revokeRuntimePermission(pkgname,permission_str,uid);
            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                iPackageManager.revokePermission(pkgname,permission_str);
            }
        }

    }

    public void grantRuntimePermission(String pkgname , String permission_str,int uid){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM){
            IPermissionManager iPermissionManager = getIPermissionManager();
            iPermissionManager.grantRuntimePermission(pkgname,permission_str,null,uid);
        }else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q && Build.VERSION.SDK_INT <= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
            IPermissionManager iPermissionManager = getIPermissionManager();
            iPermissionManager.grantRuntimePermission(pkgname,permission_str,uid);
        }else{
            IPackageManager iPackageManager = getIPackageManager();
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
                iPackageManager.grantRuntimePermission(pkgname,permission_str,uid);
            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                iPackageManager.grantPermission(pkgname,permission_str);
            }
        }
    }

    public void setPackageHideState(String pkgname,boolean hide , int uid){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            IPackageManager iPackageManager = getIPackageManager();
            boolean isHide = iPackageManager.setApplicationHiddenSettingAsUser(pkgname, hide, uid);
//            System.out.println("setPackageHideState -- " + isHide);
        }
    }

    public List<ActivityManager.RunningAppProcessInfo> getRunningApps(int uid){
        return getIActivityManager().getRunningAppProcesses();
    }


    public void SetInactive(String pkgname,boolean b,int uid){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            IUsageStatsManager manager = getIUsageStatsManager();
            manager.setAppInactive(pkgname,b,uid);
        }
    }

    public void SetStandbyBucket(String pkgname,String op,int uid){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            IUsageStatsManager manager = getIUsageStatsManager();
            manager.setAppStandbyBucket(pkgname,bucketNameToBucketValue(op),uid);
        }
    }

    public List<String> getLauncherApps(String pkgName) {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        if(pkgName != null){
            intent.setPackage(pkgName);
        }

        IPackageManager iPackageManager = getIPackageManager();
        List<ResolveInfo> resolveInfoList = null;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            resolveInfoList = iPackageManager.queryIntentActivities(intent, intent.getType(),0L,getTranslatedUserId()).getList();
        }else{
            resolveInfoList = iPackageManager.queryIntentActivities(intent, intent.getType(),0,getTranslatedUserId()).getList();
        }

        List<String> list = new ArrayList<>();
        for (ResolveInfo resolveInfo : resolveInfoList) {
            list.add(resolveInfo.activityInfo.packageName);
        }
        return list;
    }


    public String[] getDisallowedPackages(){
        if(disallowedPackages == null){
            List<String> launcherApps = getLauncherApps(null);
            List<MyPackageInfo> installedPackages = getInstalledPackages(getCurrentUser());
            ArrayList<String> strings = new ArrayList<>();
            for (MyPackageInfo myPackageInfo : installedPackages) {
                if(checklauncherApps(launcherApps,myPackageInfo.packageName)){
                    strings.add(myPackageInfo.packageName);
                }
            }
            disallowedPackages = new String[strings.size()];
            for (int i = 0; i < strings.size(); i++) {
                disallowedPackages[i] = strings.get(i);
            }
        }
        return disallowedPackages;
    }

    public int getMaxSupportedUsers() {
        try {
            // 1. 特殊 build 限制
            if (Build.ID != null && Build.ID.startsWith("JVP")) {
                return 1;
            }

            // 2. fw.max_users
            Class<?> sp = Class.forName("android.os.SystemProperties");
            Method getInt = sp.getMethod("getInt", String.class, int.class);
            int fwMax = (int) getInt.invoke(null, "fw.max_users", -1);

            // 3. config_multiuserMaximumUsers
            Class<?> r = Class.forName("com.android.internal.R$integer");
            Field f = r.getField("config_multiuserMaximumUsers");
            int resId = f.getInt(null);
            int configMax = Resources.getSystem().getInteger(resId);

            // 4. framework 原始逻辑
            return Math.max(1, fwMax >= 0 ? fwMax : configMax);

        } catch (Throwable e) {
            return 1;
        }
    }


    public void createUser() {
        IUserManager iUserManager = getIUserManager();
        String name = "EAMA";
        int userID = 0;//这个uid设置为0，是因为每个手机用户只有一个主用户，通常它的uid是0，同时也只有这个用户的权限最高
        String userType = USER_TYPE_PROFILE_MANAGED;
        int flags = 0;
        int sdk_int = Build.VERSION.SDK_INT;
        disallowedPackages = getDisallowedPackages();
//        if(disallowedPackages == null){
//            List<String> launcherApps = getLauncherApps(null);
//            List<MyPackageInfo> installedPackages = getInstalledPackages(getCurrentUser());
//            ArrayList<String> strings = new ArrayList<>();
//            for (MyPackageInfo myPackageInfo : installedPackages) {
//                if(checklauncherApps(launcherApps,myPackageInfo.packageName)){
//                    strings.add(myPackageInfo.packageName);
//                }
//            }
//            disallowedPackages = new String[strings.size()];
//            for (int i = 0; i < strings.size(); i++) {
//                disallowedPackages[i] = strings.get(i);
//            }
//        }
        if(sdk_int >= Build.VERSION_CODES.R){
            iUserManager.createProfileForUserWithThrow(name,userType,flags,userID,disallowedPackages);
//            iUserManager.createProfileForUserWithThrow(name,userType,flags,translatedUserId,null);
        }else if(sdk_int >= Build.VERSION_CODES.O){
            flags |= FLAG_MANAGED_PROFILE;
            iUserManager.createProfileForUser(name,flags,userID,disallowedPackages);
//            iUserManager.createProfileForUser(name,flags,translatedUserId,null);
        }else{
            flags |= FLAG_MANAGED_PROFILE;
            iUserManager.createProfileForUser(name,flags,userID);
        }
    }

    public void removeUser(int userid){
        if(userid != getCurrentUser()){
            stopUser(userid);
            IUserManager iUserManager = getIUserManager();
            iUserManager.removeUser(userid);
        }
    }

    public void stopUser(int userid){
        IActivityManager iActivityManager = getIActivityManager();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            iActivityManager.stopUser(userid,true,null);
        }else{
            iActivityManager.stopUser(userid,null);
        }
    }

    public String[] getUsers(){
        List<UserInfo> ll = null;
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
            IUserManager iUserManager = getIUserManager();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                ll = iUserManager.getUsers(false, false, false);
            }else{
                ll = iUserManager.getUsers(false);
            }
        }

        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN){
            ll = getIPackageManager().getUsers();
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            String a[] = new String[1];
            a[0]="0";
            return a;
        }


        int size = ll.size();
        String a[] = new String[size];
        for (int i = 0; i < size; i++) {
            a[i]=String.valueOf(ll.get(i).id);
        }
        return a;
    }

    public void startUser(int userid){
        IActivityManager iActivityManager = getIActivityManager();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
            iActivityManager.startProfileWithListener(userid,null);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            iActivityManager.startUserInBackgroundWithListener(userid,null);
        }else{
            iActivityManager.startUserInBackground(userid);
        }

    }

    public int getCurrentUser(){
        int current_id = -1;
        try {
            current_id = getIActivityManager().getCurrentUser().id;
        }catch (Throwable e){
            current_id = Process.myUid();
        }
        return current_id;
    }

    public List<MyPackageInfo> getInstalledPackages(int userid){
        int flags = PackageManager.GET_PERMISSIONS|PackageManager.GET_ACTIVITIES|PackageManager.GET_DISABLED_COMPONENTS|PackageManager.GET_SERVICES|PackageManager.GET_RECEIVERS | PackageManager.MATCH_UNINSTALLED_PACKAGES;
        long flags2 = flags;
        IPackageManager iPackageManager = getIPackageManager();
        ArrayList<MyPackageInfo> myPackageInfos = new ArrayList<>();
        ParceledListSlice<PackageInfo> slice = Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU?iPackageManager.getInstalledPackages(flags2,userid):iPackageManager.getInstalledPackages(flags,userid);
        List<PackageInfo> list = slice.getList();
        for (PackageInfo packageInfo : list) {
            myPackageInfos.add(copyPkgInfoToMyPkginfo(packageInfo));
        }
        return myPackageInfos;
    }


    public PackageInfo getPackageInfo(String pkgname,int uid,int flags){
        IPackageManager iPackageManager = getIPackageManager();
        long flags2 = flags;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            return iPackageManager.getPackageInfo(pkgname, flags2, uid);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            return iPackageManager.getPackageInfo(pkgname, flags, uid);
        }else {
            return iPackageManager.getPackageInfo(pkgname, flags);
        }
    }

    public MyPackageInfo getMyPackageInfo(String pkgname , int uid){
        int flags = PackageManager.GET_PERMISSIONS|PackageManager.GET_ACTIVITIES|PackageManager.GET_DISABLED_COMPONENTS|PackageManager.GET_SERVICES|PackageManager.GET_RECEIVERS;
        PackageInfo packageInfo = getPackageInfo(pkgname,uid,flags);
        if(packageInfo == null){
            return null;
        }
        return copyPkgInfoToMyPkginfo(packageInfo);

    }

    public MyPackageInfo copyPkgInfoToMyPkginfo(PackageInfo packageInfo){
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        MyApplicationInfo myApplicationInfo = null;
        if (applicationInfo != null) {
            myApplicationInfo = new MyApplicationInfo(applicationInfo.flags, applicationInfo.enabled, applicationInfo.sourceDir);
        }
        ActivityInfo activities[] = packageInfo.activities;
        ServiceInfo[] services = packageInfo.services;
        ActivityInfo[] receivers = packageInfo.receivers;
        MyActivityInfo myActivityInfo[]=new MyActivityInfo[0];
        MyActivityInfo myServices[]=new MyActivityInfo[0];
        MyActivityInfo myReceivers[]=new MyActivityInfo[0];
        if(activities != null && activities.length > 0){
            myActivityInfo = new MyActivityInfo[activities.length];
            for (int i = 0; i < activities.length; i++) {
                ActivityInfo a = activities[i];
                myActivityInfo[i]=new MyActivityInfo(a.name,a.enabled,a.exported);
            }
        }
        if(services != null && services.length > 0){
            myServices = new MyActivityInfo[services.length];
            for (int i = 0; i < services.length; i++) {
                ServiceInfo service = services[i];
                myServices[i] = new MyActivityInfo(service.name,service.enabled,service.exported);
            }
        }
        if(receivers != null && receivers.length > 0){
            myReceivers = new MyActivityInfo[receivers.length];
            for (int i = 0; i < receivers.length; i++) {
                ActivityInfo receiver = receivers[i];
                myReceivers[i] = new MyActivityInfo(receiver.name,receiver.enabled,receiver.exported);
            }
        }

        return new MyPackageInfo(packageInfo.packageName,packageInfo.versionCode,packageInfo.versionName,
                myApplicationInfo,myActivityInfo,myServices,myReceivers,packageInfo.requestedPermissions);
    }


    class ClearDataObserver extends IPackageDataObserver.Stub {
        boolean finished;
        boolean result;

        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded)  {
            synchronized (this) {
                finished = true;
                result = succeeded;
                notifyAll();
            }
        }

        @Override
        public IBinder asBinder() {
            return this;
        }
    }


    class PackageDeleteObserver extends IPackageDeleteObserver.Stub {
        boolean finished;
        boolean result;

        public void packageDeleted(String packageName, int returnCode) {
            synchronized (this) {
                finished = true;
                result = returnCode == DELETE_SUCCEEDED;
                notifyAll();
            }
        }

        @Override
        public IBinder asBinder() {
            return this;
        }
    }

    private int bucketNameToBucketValue(String name) {
        String lower = name.toLowerCase();
        if (lower.startsWith("ac")) {
            return UsageStatsManager.STANDBY_BUCKET_ACTIVE;
        } else if (lower.startsWith("wo")) {
            return UsageStatsManager.STANDBY_BUCKET_WORKING_SET;
        } else if (lower.startsWith("fr")) {
            return UsageStatsManager.STANDBY_BUCKET_FREQUENT;
        } else if (lower.startsWith("ra")) {
            return UsageStatsManager.STANDBY_BUCKET_RARE;
        } else if (lower.startsWith("re")) {
            return UsageStatsManager.STANDBY_BUCKET_RESTRICTED;
        } else if (lower.startsWith("ne")) {
            return 50;
        } else {
            try {
                int bucket = Integer.parseInt(lower);
                return bucket;
            } catch (NumberFormatException nfe) {
                System.err.println("Error: Unknown bucket: " + name);
                nfe.printStackTrace();
            }
        }
        return -1;
    }
    public void resultLocalIntent(LocalIntentReceiver r){
        Intent result = null;
        try {
            result = r.getResult();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int status = result.getIntExtra(PackageInstaller.EXTRA_STATUS, PackageInstaller.STATUS_FAILURE);
        String message = result.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE);
        System.out.println("status ::: " + status + " ---    " + message);
    }
    private class LocalIntentReceiver {
        private final SynchronousQueue<Intent> mResult = new SynchronousQueue<>();

        private IIntentSender.Stub mLocalSender = new IIntentSender.Stub() {


            @Override
            public void send(int code, Intent intent, String resolvedType, IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) {
                if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1){
                    try {
                        mResult.offer(intent, 1, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void send(int code, Intent intent, String resolvedType, IBinder whitelistToken, IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) {
                try {
                    mResult.offer(intent, 1, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        public IntentSender getIntentSender() {
            return new IntentSender((IIntentSender) mLocalSender);
        }

        public Intent getResult() throws InterruptedException {
            return mResult.take();
        }
    }

    private boolean checklauncherApps(List<String> noallowpkgs , String pkgname){
        for (String s : noallowpkgs) {
            if(pkgname.equals(s)){
                return true;
            }
        }
        return false;
    }


}

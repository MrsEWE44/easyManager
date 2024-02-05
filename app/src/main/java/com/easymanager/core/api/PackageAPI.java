package com.easymanager.core.api;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.usage.IUsageStatsManager;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.IPackageInstaller;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.VerificationParams;
import android.content.pm.VersionedPackage;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.permission.IPermissionManager;

import com.easymanager.core.server.Singleton;
import com.easymanager.core.server.easyManagerBinderWrapper;
import com.easymanager.core.server.easyManagerPortService;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class PackageAPI extends  baseAPI implements Serializable {

    public static final int COMPONENT_ENABLED_STATE_DEFAULT = 0;

    public static final int COMPONENT_ENABLED_STATE_ENABLED = 1;

    public static final int COMPONENT_ENABLED_STATE_DISABLED = 2;

    public static final int COMPONENT_ENABLED_STATE_DISABLED_USER = 3;

    public static final int COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED = 4;

    public static final int DELETE_ALL_USERS = 0x00000002;

    public static final int DELETE_SUCCEEDED = 1;

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

    private static final Map<String, IUsageStatsManager> I_USAGE_STATS_MANAGER_CACHE = new HashMap<>();
    private static final Map<String, IActivityManager> I_ACTIVITY_MANAGER_CACHE = new HashMap<>();
    private static final Map<String, IPackageManager> I_PACKAGE_MANAGER_CACHE = new HashMap<>();
    private static final Map<String, IPermissionManager> I_PERMISSION_MANAGER_CACHE = new HashMap<>();


    public IActivityManager getIActivityManager(){
        IActivityManager iActivityManager = I_ACTIVITY_MANAGER_CACHE.get("iaservice");
        if(iActivityManager == null){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                Singleton<IActivityManager> iActivityManagerSingleton = new Singleton<IActivityManager>() {
                    @Override
                    protected IActivityManager create() {
                        return IActivityManager.Stub.asInterface(new easyManagerBinderWrapper(easyManagerPortService.getSystemService(Context.ACTIVITY_SERVICE)));
                    }
                };
                iActivityManager = iActivityManagerSingleton.get();
            }else{
                iActivityManager= ActivityManagerNative.asInterface(new easyManagerBinderWrapper(easyManagerPortService.getSystemService(Context.ACTIVITY_SERVICE)));
            }
            I_ACTIVITY_MANAGER_CACHE.put("iaservice",iActivityManager);
        }
        return iActivityManager;
    }

    //通过调用activitymanager系统api实现进程清理
    public void killApp(String pkgname){
        IActivityManager iActivityManager = getIActivityManager();
        iActivityManager.forceStopPackage(pkgname,getTranslatedUserId());
    }



    public IPackageManager getIPackageManager(){
        IPackageManager iPackageManager = I_PACKAGE_MANAGER_CACHE.get("ipkgservice");
        if(iPackageManager == null){
            Singleton<IPackageManager> iPackageManagerSingleton = new Singleton<IPackageManager>() {
                @Override
                protected IPackageManager create() {
                    return IPackageManager.Stub.asInterface(new easyManagerBinderWrapper(easyManagerPortService.getSystemService("package")));
                }
            };
            iPackageManager = iPackageManagerSingleton.get();
            I_PACKAGE_MANAGER_CACHE.put("ipkgservice",iPackageManager);
        }
        return iPackageManager;
    }

    public IPermissionManager getIPermissionManager(){
        IPermissionManager iPermissionManager = I_PERMISSION_MANAGER_CACHE.get("ipermservice");
        if(iPermissionManager == null){
            Singleton<IPermissionManager> iPermissionManagerSingleton = new Singleton<IPermissionManager>() {
                @Override
                protected IPermissionManager create() {
                    return IPermissionManager.Stub.asInterface(new easyManagerBinderWrapper(easyManagerPortService.getSystemService("permissionmgr")));
                }
            };
            iPermissionManager = iPermissionManagerSingleton.get();
            I_PERMISSION_MANAGER_CACHE.put("ipermservice",iPermissionManager);
        }
        return iPermissionManager;
    }

    public int getPKGUID(String pkgname){
        IPackageManager iPackageManager = getIPackageManager();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            return iPackageManager.getPackageUid(pkgname,0L,getTranslatedUserId());
        }else if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.N){
            return iPackageManager.getPackageUid(pkgname,0,getTranslatedUserId());
        }else{
            return iPackageManager.getPackageUid(pkgname,getTranslatedUserId());
        }

    }

    public IPackageInstaller getIPackageInstaller() throws RemoteException {
        return IPackageInstaller.Stub.asInterface(new easyManagerBinderWrapper(getIPackageManager().getPackageInstaller().asBinder()));
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
        return (isRoot()?"com.easymanager":"com.android.shell");
    }

    public int getInstallFlags(){
        return INSTALL_ALLOW_TEST | INSTALL_REPLACE_EXISTING |INSTALL_ALLOW_DOWNGRADE;
    }

    public void doCommintSession(IPackageInstaller iPackageInstaller , int sessionID , PackageInstaller.Session session) throws RemoteException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            session = new PackageInstaller.Session(iPackageInstaller.openSession(sessionID));
            LocalIntentReceiver r = new LocalIntentReceiver();
            session.commit(r.getIntentSender());
            resultLocalIntent(r);
        }
    }

    public void closeSession(PackageInstaller.Session session){
        if(session !=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                session.close();
            }
        }
    }



    /**
     *
     * install apk on android 12+
     * */

    public void InstallAPKS(String apkPath,int userId){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            try {
                PackageInstaller.SessionParams sessionParams = new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
                sessionParams.installFlags |= INSTALL_ALL_WHITELIST_RESTRICTED_PERMISSIONS | INSTALL_ALLOW_TEST | INSTALL_REPLACE_EXISTING |INSTALL_ALLOW_DOWNGRADE;
                IPackageInstaller iPackageInstaller = getIPackageInstaller();
                int sessionID = iPackageInstaller.createSession(sessionParams, getInstallerPackageName(), null, userId);
                PackageInstaller.Session session = new PackageInstaller.Session(iPackageInstaller.openSession(sessionID));
                try (OutputStream packageInSession = session.openWrite("package", 0, -1);
                     InputStream is = new FileInputStream(apkPath)) {
                    byte[] buffer = new byte[16384];
                    int n;
                    while ((n = is.read(buffer)) >= 0) {
                        packageInSession.write(buffer, 0, n);
                    }
                }
                doCommintSession(iPackageInstaller,sessionID,session);
                try {
                    session = new PackageInstaller.Session(iPackageInstaller.openSession(sessionID));
                    session.abandon();
                }catch (Exception e){

                }finally {
                    closeSession(session);
                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * install apk on android 7.x-11.x
     *
     * */
    public void InstallAPKN(String apkPath, int userId) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            IPackageInstaller iPackageInstaller = null;
            try {
                iPackageInstaller = getIPackageInstaller();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
            params.installFlags |= getInstallFlags();
            int sessionId=iPackageInstaller.createSession(params,getInstallerPackageName(),userId);
            try {
                PackageInstaller.SessionInfo sessionInfo = iPackageInstaller.getSessionInfo(sessionId);
                PackageInstaller.Session session = null;
                InputStream in = null;
                OutputStream out = null;
                try {
                    session = new PackageInstaller.Session(iPackageInstaller.openSession(sessionId));
                    in = new FileInputStream(apkPath);
                    out = session.openWrite("package", 0, params.sizeBytes);
                    int total = 0;
                    byte[] buffer = new byte[65536];
                    int c;
                    while ((c = in.read(buffer)) != -1) {
                        total += c;
                        out.write(buffer, 0, c);
                        if (sessionInfo.sizeBytes > 0) {
                            final float fraction = ((float) c / (float) sessionInfo.sizeBytes);
                            session.addProgress(fraction);
                        }
                    }
                    session.fsync(out);
                } catch (IOException e) {
                    System.err.println("Error: failed to write; " + e.getMessage());
                } finally {
                    closeIO(out);
                    closeIO(in);
                    closeSession(session);
                }
                try{
                    doCommintSession(iPackageInstaller,sessionId,session);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    closeSession(session);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(iPackageInstaller != null){
                    try{
                        iPackageInstaller.abandonSession(sessionId);
                    }catch (Exception e){

                    }
                }
            }

        }

    }

    private void closeIO(Closeable out) throws IOException {
        if(out != null){
            out.close();
        }
    }

    /**
     * install apk on android 4.4-6.0
     *
     * */
    public void InstallAPKKLM(String apkPath , int userId){
        int installFlags = INSTALL_ALL_USERS;
        installFlags |= getInstallFlags();
        IPackageManager iPackageManager = getIPackageManager();
        VerificationParams verificationParams = new VerificationParams(null,
                null, null, VerificationParams.NO_UID, null);
        Uri uri = Uri.fromFile(new File(apkPath));
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            PackageInstallObserver obs = new PackageInstallObserver();
            iPackageManager.installPackageWithVerificationAndEncryption(uri,obs,installFlags,null,verificationParams,null);
            synchronized (obs) {
                while (!obs.finished) {
                    try {
                        obs.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }else{
            LocalPackageInstallObserver obs = new LocalPackageInstallObserver();
            iPackageManager.installPackageAsUser(apkPath,obs.getBinder(),installFlags,null,verificationParams,null,userId);
            synchronized (obs) {
                while (!obs.finished) {
                    try {
                        obs.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }


    public void InstallAPK(String apkPath, int userId){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            InstallAPKS(apkPath,userId);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            InstallAPKN(apkPath,userId);
        }else{
            InstallAPKKLM(apkPath,userId);
        }
    }

    public void UninstallPKG(String pkgname ){
        UninstallPKG(pkgname,getTranslatedUserId(),-1);
    }

    /**
     * uninstall pkg on android 4.4
     *
     * */
    public void UninstallPKGK(String pkgname,int userId){
        IPackageManager iPackageManager = getIPackageManager();
        PackageDeleteObserver obs = new PackageDeleteObserver();
        iPackageManager.deletePackageAsUser(pkgname,obs,userId,DELETE_ALL_USERS);
    }

    /**
     * uninstall pkg on android 5.0-13.0
     *
     * */
    public void UninstallPKG(String pkgname , int userId , int versionCode) {
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                int flags = 0;
                flags |= DELETE_ALL_USERS;
                IPackageInstaller iPackageInstaller = getIPackageInstaller();
                LocalIntentReceiver r = new LocalIntentReceiver();
                int sdkInt = Build.VERSION.SDK_INT;
                if (sdkInt >= Build.VERSION_CODES.O) {
                    iPackageInstaller.uninstall(new VersionedPackage(pkgname,
                                    versionCode), null /*callerPackageName*/, flags,
                            r.getIntentSender(), userId);
                    resultLocalIntent(r);
                }else if(sdkInt >= Build.VERSION_CODES.M){
                    /**
                     * 安卓6.0以下调用IIntentSender.send会出现找不到抽象接口得问题，但是，实际上
                     * 是可以正常处理提交得操作请求，故而，需要修改一下这部分得处理逻辑，不应该提交完操作后，就
                     * 被迫强制终止服务，如果是root模式，应该要自动重启服务
                     * */
                    iPackageInstaller.uninstall(pkgname,null,flags,r.getIntentSender(),userId);
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

    public IUsageStatsManager getIUsageStatsManager(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            IUsageStatsManager manager = I_USAGE_STATS_MANAGER_CACHE.get("iusmservice");
            if(manager == null){
                Singleton<IUsageStatsManager> iUsageStatsManagerSingleton = new Singleton<IUsageStatsManager>() {
                    @Override
                    protected IUsageStatsManager create() {
                        return IUsageStatsManager.Stub.asInterface(new easyManagerBinderWrapper(easyManagerPortService.getSystemService(Context.USAGE_STATS_SERVICE)));
                    }
                };
                manager = iUsageStatsManagerSingleton.get();
                I_USAGE_STATS_MANAGER_CACHE.put("iusmservice",manager);
            }
            return manager;
        }
        return null;
    }

    public void setComponentOrPackageEnabledState(String pkgname_or_compname,int state){
//        System.out.println(pkgname_or_compname + " --- " + enabledSettingToString(state));
        IPackageManager iPackageManager = getIPackageManager();
        ComponentName componentName = ComponentName.unflattenFromString(pkgname_or_compname);
        int translatedUserId = getTranslatedUserId();
        if(componentName == null){
            iPackageManager.setApplicationEnabledSetting(pkgname_or_compname,state,0,translatedUserId,"shell:" + getMyuid());
//            System.out.println("Package "+pkgname_or_compname + " new state : " +enabledSettingToString(iPackageManager.getApplicationEnabledSetting(pkgname_or_compname,translatedUserId)));
        }else{
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU){
                iPackageManager.setComponentEnabledSetting(componentName,state,0,translatedUserId,"shell:" + getMyuid());
            }else{
                iPackageManager.setComponentEnabledSetting(componentName, state, 0, translatedUserId);
            }
        }

    }

    public void revokeRuntimePermission(String pkgname , String permission_str){
        int translatedUserId = getTranslatedUserId();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            IPermissionManager iPermissionManager = getIPermissionManager();
            iPermissionManager.revokeRuntimePermission(pkgname,permission_str,translatedUserId,null);
        }else{
            IPackageManager iPackageManager = getIPackageManager();
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
                iPackageManager.revokeRuntimePermission(pkgname,permission_str,translatedUserId);
            }else{
                iPackageManager.revokePermission(pkgname,permission_str);
            }
        }

    }

    public void grantRuntimePermission(String pkgname , String permission_str){
        int translatedUserId = getTranslatedUserId();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            IPermissionManager iPermissionManager = getIPermissionManager();
            iPermissionManager.grantRuntimePermission(pkgname,permission_str,translatedUserId);
        }else{
            IPackageManager iPackageManager = getIPackageManager();
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
                iPackageManager.grantRuntimePermission(pkgname,permission_str,translatedUserId);
            }else{
                iPackageManager.grantPermission(pkgname,permission_str);
            }
        }
    }

    public void setPackageHideState(String pkgname,boolean hide){
        IPackageManager iPackageManager = getIPackageManager();
        iPackageManager.setApplicationHiddenSettingAsUser(pkgname, hide, getTranslatedUserId());
    }


    public void SetInactive(String pkgname,boolean b){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            IUsageStatsManager manager = getIUsageStatsManager();
            manager.setAppInactive(pkgname,b,getMyuidHascode());
        }
    }

    public void SetStandbyBucket(String pkgname,String op){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            IUsageStatsManager manager = getIUsageStatsManager();
            manager.setAppStandbyBucket(pkgname,bucketNameToBucketValue(op),getMyuidHascode());
        }

    }


    class LocalPackageInstallObserver extends android.app.PackageInstallObserver {
        boolean finished;
        int result;
        String extraPermission;
        String extraPackage;

        @Override
        public void onPackageInstalled(String name, int status, String msg, Bundle extras) {
            synchronized (this) {
                finished = true;
                result = status;
                if (status == INSTALL_FAILED_DUPLICATE_PERMISSION) {
//                    extraPermission = extras.getString(EXTRA_FAILURE_EXISTING_PERMISSION);
//                    extraPackage = extras.getString(EXTRA_FAILURE_EXISTING_PACKAGE);
                }
                notifyAll();
            }
        }
    }

    class PackageInstallObserver extends IPackageInstallObserver.Stub {
        boolean finished;
        int result;

        public void packageInstalled(String name, int status) {
            synchronized( this) {
                finished = true;
                result = status;
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

    private String installFailureToString(int result) {
        Field[] fields = PackageManager.class.getFields();
        for (Field f: fields) {
            if (f.getType() == int.class) {
                int modifiers = f.getModifiers();
                // only look at public final static fields.
                if (((modifiers & Modifier.FINAL) != 0) &&
                        ((modifiers & Modifier.PUBLIC) != 0) &&
                        ((modifiers & Modifier.STATIC) != 0)) {
                    String fieldName = f.getName();
                    if (fieldName.startsWith("INSTALL_FAILED_") ||
                            fieldName.startsWith("INSTALL_PARSE_FAILED_")) {
                        // get the int value and compare it to result.
                        try {
                            if (result == f.getInt(null)) {
                                return fieldName;
                            }
                        } catch (IllegalAccessException e) {
                            // this shouldn't happen since we only look for public static fields.
                        }
                    }
                }
            }
        }

        // couldn't find a matching constant? return the value
        return Integer.toString(result);
    }

    private String enabledSettingToString(int state) {
        switch (state) {
            case COMPONENT_ENABLED_STATE_DEFAULT:
                return "default";
            case COMPONENT_ENABLED_STATE_ENABLED:
                return "enabled";
            case COMPONENT_ENABLED_STATE_DISABLED:
                return "disabled";
            case COMPONENT_ENABLED_STATE_DISABLED_USER:
                return "disabled-user";
            case COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED:
                return "disabled-until-used";
        }
        return "unknown";
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
//        System.out.println("status ::: " + status + " ---    " + message);
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


}

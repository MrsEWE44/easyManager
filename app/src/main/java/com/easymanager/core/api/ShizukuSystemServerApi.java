package com.easymanager.core.api;

import android.app.IActivityManager;
import android.app.usage.IUsageStatsManager;
import android.content.Context;
import android.content.pm.IPackageInstaller;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.os.IUserManager;
import android.os.RemoteException;
import android.permission.IPermissionManager;

import com.android.internal.app.IAppOpsService;
import com.easymanager.BuildConfig;
import com.easymanager.core.server.Singleton;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuBinderWrapper;
import rikka.shizuku.SystemServiceHelper;
import rikka.sui.Sui;

public class ShizukuSystemServerApi {

    public static Singleton<IUserManager> USER_MANAGER = new Singleton<IUserManager>() {
        @Override
        protected IUserManager create() {
            return IUserManager.Stub.asInterface(new ShizukuBinderWrapper(SystemServiceHelper.getSystemService(Context.USER_SERVICE)));
        }
    };
    public static Singleton<IPackageManager> PACKAGE_MANAGER = new Singleton<IPackageManager>() {
        @Override
        protected IPackageManager create() {
            return IPackageManager.Stub.asInterface(new ShizukuBinderWrapper(SystemServiceHelper.getSystemService("package")));
        }
    };
    public static Singleton<IUsageStatsManager> USAGESTATS_MANAGER= new Singleton<IUsageStatsManager>() {
        @Override
        protected IUsageStatsManager create() {
            return IUsageStatsManager.Stub.asInterface(new ShizukuBinderWrapper(SystemServiceHelper.getSystemService(Context.USAGE_STATS_SERVICE)));
        }
    };

    public static Singleton<IActivityManager> ACTIVITY_MANAGER = new Singleton<IActivityManager>() {
        @Override
        protected IActivityManager create() {
            return IActivityManager.Stub.asInterface(new ShizukuBinderWrapper(SystemServiceHelper.getSystemService(Context.ACTIVITY_SERVICE)));
        }
    };
    public static Singleton<IPermissionManager> PERMISSION_MANAGER= new Singleton<IPermissionManager>() {
        @Override
        protected IPermissionManager create() {
            return IPermissionManager.Stub.asInterface(new ShizukuBinderWrapper(SystemServiceHelper.getSystemService("permissionmgr")));
        }
    };
    public static Singleton<IAppOpsService> APPOPS_SERVICE = new Singleton<IAppOpsService>() {
        @Override
        protected IAppOpsService create() {
            return IAppOpsService.Stub.asInterface(new ShizukuBinderWrapper(SystemServiceHelper.getSystemService(Context.APP_OPS_SERVICE)));
        }
    };

    public static IPackageInstaller getIPackageInstaller() throws RemoteException {
        return IPackageInstaller.Stub.asInterface(new ShizukuBinderWrapper(PACKAGE_MANAGER.get().getPackageInstaller().asBinder()));
    }


    private static Shizuku.OnRequestPermissionResultListener REQUEST_PERMISSION_RESULT_LISTENER = new Shizuku.OnRequestPermissionResultListener() {
        @Override
        public void onRequestPermissionResult(int requestCode, int grantResult) {

            if(grantResult == PackageManager.PERMISSION_GRANTED){

            }
        }
    };

    public static boolean isShizuku(){
        try {
            Sui.init(BuildConfig.APPLICATION_ID);
            if (Shizuku.isPreV11()) {
                return false;
            }
            return Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean check(int i){
        Sui.init(BuildConfig.APPLICATION_ID);
        if (Shizuku.isPreV11()) {
            return false;
        }
        try {
            if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                System.out.println("shizuku is check ok");
                return true;
            } else {
                if (Shizuku.shouldShowRequestPermissionRationale()) {
                    System.out.println("User denied permission (shouldShowRequestPermissionRationale=true)");
                    return false;
                } else {
                    Shizuku.requestPermission(i);
                    return false;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }


    public static void bindRequestPermission(){
        Shizuku.addRequestPermissionResultListener(REQUEST_PERMISSION_RESULT_LISTENER);
    }

    public static void dead(){
        Shizuku.removeRequestPermissionResultListener(REQUEST_PERMISSION_RESULT_LISTENER);
    }


}

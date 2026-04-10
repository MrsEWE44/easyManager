package com.easymanager.core.api;

import android.app.IActivityManager;
import android.app.usage.IUsageStatsManager;
import android.content.Context;
import android.content.pm.IPackageInstaller;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.os.IUserManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.permission.IPermissionManager;

import com.android.internal.app.IAppOpsService;
import com.easymanager.core.server.Singleton;
import com.rosan.dhizuku.api.Dhizuku;
import com.rosan.dhizuku.api.DhizukuRequestPermissionListener;

public class DhizukuSystemServerApi {

    public static Singleton<IUserManager> USER_MANAGER = new Singleton<IUserManager>() {
        @Override
        protected IUserManager create() {
            return IUserManager.Stub.asInterface(Dhizuku.binderWrapper(ServiceManager.getService(Context.USER_SERVICE)));
        }
    };
    public static Singleton<IPackageManager> PACKAGE_MANAGER = new Singleton<IPackageManager>() {
        @Override
        protected IPackageManager create() {
            return IPackageManager.Stub.asInterface(Dhizuku.binderWrapper(ServiceManager.getService("package")));
        }
    };
    public static Singleton<IUsageStatsManager> USAGESTATS_MANAGER= new Singleton<IUsageStatsManager>() {
        @Override
        protected IUsageStatsManager create() {
            return IUsageStatsManager.Stub.asInterface(Dhizuku.binderWrapper(ServiceManager.getService(Context.USAGE_STATS_SERVICE)));
        }
    };

    public static Singleton<IActivityManager> ACTIVITY_MANAGER = new Singleton<IActivityManager>() {
        @Override
        protected IActivityManager create() {
            return IActivityManager.Stub.asInterface(Dhizuku.binderWrapper(ServiceManager.getService(Context.ACTIVITY_SERVICE)));
        }
    };
    public static Singleton<IPermissionManager> PERMISSION_MANAGER= new Singleton<IPermissionManager>() {
        @Override
        protected IPermissionManager create() {
            return IPermissionManager.Stub.asInterface(Dhizuku.binderWrapper(ServiceManager.getService("permissionmgr")));
        }
    };
    public static Singleton<IAppOpsService> APPOPS_SERVICE = new Singleton<IAppOpsService>() {
        @Override
        protected IAppOpsService create() {
            return IAppOpsService.Stub.asInterface(Dhizuku.binderWrapper(ServiceManager.getService(Context.APP_OPS_SERVICE)));
        }
    };

    public static IPackageInstaller getIPackageInstaller() throws RemoteException {
        return IPackageInstaller.Stub.asInterface(Dhizuku.binderWrapper(PACKAGE_MANAGER.get().getPackageInstaller().asBinder()));
    }

    private static DhizukuRequestPermissionListener REQUEST_PERMISSION_RESULT_LISTENER = new DhizukuRequestPermissionListener() {
        @Override
        public void onRequestPermission(int grantResult) throws RemoteException {
            if (grantResult == PackageManager.PERMISSION_GRANTED){
                System.out.println("dhizuku is granted");
            }else{
                System.out.println("dhizuku is not grante");
            }
        }
    };

    public static boolean isDhizuku(){
        try {
            Dhizuku.init();
            return Dhizuku.isPermissionGranted();
        }catch (Exception e){
            return false;
        }
    }

    public static boolean check(int i){
        Dhizuku.init();
        try {
            if (Dhizuku.isPermissionGranted()) {
                System.out.println("dhizuku is check ok");
                return true;
            }else{
                bindRequestPermission();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }


    private static void bindRequestPermission(){
        Dhizuku.requestPermission(REQUEST_PERMISSION_RESULT_LISTENER);
    }

    public static void dead(){
        //do something...
    }

    public static String getDhizukuComponentName(){
        if(isDhizuku()){
            return Dhizuku.getOwnerPackageName();
        }
        return null;
    }



}

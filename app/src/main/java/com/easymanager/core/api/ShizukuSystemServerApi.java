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

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuBinderWrapper;
import rikka.shizuku.SystemServiceHelper;
import rikka.sui.Sui;

public class ShizukuSystemServerApi {

    public static final int MODE_NONE = 0;
    public static final int MODE_SHIZUKU = 1;
    public static final int MODE_DHIZUKU = 2;
    public static int runtimeMode = MODE_NONE;

    public static void initMode(Context context) {
        runtimeMode = context.getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("runtime_mode", MODE_NONE);

        // 尝试唤醒 Shizuku Provider
        try {
            context.getContentResolver().getType(android.net.Uri.parse("content://" + context.getPackageName() + ".shizuku"));
        } catch (Throwable ignored) {}

        try {
            // 使用 context.getPackageName() 确保包名正确
            Sui.init(context.getPackageName());
        } catch (Throwable e) {
            e.printStackTrace();
        }

        // 监听 Binder 状态
        Shizuku.addBinderReceivedListener(() -> {
            System.out.println("Shizuku Binder Received");
            if (runtimeMode == MODE_SHIZUKU) {
                // 收到 Binder 后同步一下状态
                check(0);
            }
        });

        // 确保权限回调监听器已注册
        bindRequestPermission();

        // 尝试主动触发一次
        if (runtimeMode == MODE_SHIZUKU) {
            check(0);
        }
    }

    public static void saveMode(Context context, int mode) {
        runtimeMode = mode;
        context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putInt("runtime_mode", mode).apply();
    }

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
            if (Shizuku.isPreV11()) return false;

            // 只要获得了授权，就认为 Shizuku 模式可用（即使此时 Binder 正在连接中）
            // 这能解决“必须切换模式再切回来”的问题，因为之前的逻辑太严格了
            return Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 检查 Shizuku 服务是否真正连通且可调用
     */
    public static boolean isShizukuAlive() {
        try {
            return !Shizuku.isPreV11() && Shizuku.pingBinder();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDhizuku() {
        return DhizukuSystemServerApi.isDhizuku();
    }

    public static boolean check(int i){
        if (Shizuku.isPreV11()) return false;
        try {
            // 不再阻塞等待 pingBinder()。Shizuku.checkSelfPermission() 和 requestPermission()
            // 内部会尝试处理 Binder。如果真的没有 Binder，requestPermission 会抛出异常或不工作，
            // 此时我们捕获它，而不是直接返回 false 导致 UI 逻辑卡死。

            int result = PackageManager.PERMISSION_DENIED;
            try {
                result = Shizuku.checkSelfPermission();
            } catch (Throwable ignored) {}

            if (result == PackageManager.PERMISSION_GRANTED) {
                if (Shizuku.pingBinder()) {
                    System.out.println("Shizuku authorized and binder alive.");
                    return true;
                }
                // 已授权但 Binder 不通，尝试通过 requestPermission 唤醒服务
                try {
                    Shizuku.requestPermission(i);
                } catch (Throwable ignored) {}
                return false;
            } else {
                // 未授权或没有 Binder (返回 -1)，尝试请求权限
                System.out.println("Requesting Shizuku permission...");
                try {
                    Shizuku.requestPermission(i);
                } catch (Throwable e) {
                    System.err.println("Shizuku requestPermission failed: " + e.getMessage());
                }
                return false;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }


    public static void bindRequestPermission(){
        Shizuku.removeRequestPermissionResultListener(REQUEST_PERMISSION_RESULT_LISTENER);
        Shizuku.addRequestPermissionResultListener(REQUEST_PERMISSION_RESULT_LISTENER);
    }

    public static void dead(){
        Shizuku.removeRequestPermissionResultListener(REQUEST_PERMISSION_RESULT_LISTENER);
    }

    public static Process newProcess(String[] cmd, String[] env, String dir) {
        try {
            return (Process) HiddenApiBypass.invoke(Shizuku.class, null, "newProcess", cmd, env, dir);
        } catch (Exception e) {
            return null;
        }
    }

}

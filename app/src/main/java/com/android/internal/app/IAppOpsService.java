package com.android.internal.app;

import android.app.AppOpsManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;

import java.util.List;

public interface IAppOpsService {
    int checkOperation(int code, int uid, String packageName);
    int noteOperation(int code, int uid, String packageName);
    int permissionToOpCode(String permission);

    // Remaining methods are only used in Java.
    int checkPackage(int uid, String packageName);
    List<Parcelable> getPackagesForOps( int[] ops);
    List<AppOpsManager.PackageOps> getOpsForPackage(int uid, String packageName, int[] ops);
    List<AppOpsManager.PackageOps> getUidOps(int uid, int[] ops);
    void setUidMode(int code, int uid, int mode);
    void setMode(int code, int uid, String packageName, int mode);
    void resetAllModes(int reqUserId, String reqPackageName);



    abstract class Stub extends Binder implements IAppOpsService {
        public static IAppOpsService asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}
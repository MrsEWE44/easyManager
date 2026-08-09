package android.permission;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

public interface IPermissionManager extends IInterface {


    //android11+
    void grantRuntimePermission(String packageName, String permissionName, int userId);
    void grantRuntimePermission(String packageName, String permissionName,String persistentDeviceId, int userId);
    void revokeRuntimePermission(String packageName, String permissionName, int userId,String reason);
    void revokeRuntimePermission(String packageName, String permissionName,String persistentDeviceId, int userId, String reason);

    abstract class Stub extends Binder implements IPermissionManager {

        public static IPermissionManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}

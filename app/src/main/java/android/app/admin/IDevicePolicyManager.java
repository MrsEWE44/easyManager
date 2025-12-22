package android.app.admin;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

import java.util.List;

public interface IDevicePolicyManager extends IInterface {

    void setActiveAdmin(ComponentName policyReceiver, boolean refreshing);
    void setActiveAdmin(ComponentName policyReceiver, boolean refreshing, int userHandle);
    void setActiveAdmin(ComponentName policyReceiver, boolean refreshing, int userHandle, String provisioningContext);
    boolean isAdminActive(ComponentName policyReceiver);

    boolean isAdminActive(ComponentName policyReceiver, int userHandle);
    List<ComponentName> getActiveAdmins();

    List<ComponentName> getActiveAdmins(int userHandle);
    void removeActiveAdmin(ComponentName policyReceiver);
    void removeActiveAdmin(ComponentName policyReceiver, int userHandle);
    void forceRemoveActiveAdmin(ComponentName policyReceiver, int userHandle);
    boolean setDeviceOwner(String packageName);
    boolean setDeviceOwner(ComponentName who, int userId, boolean setProfileOwnerOnCurrentUserIfNecessary);
    boolean isDeviceOwner(String packageName);
    String getDeviceOwner();
    int getUserProvisioningState(int userHandle);
    void setUserProvisioningState(int state, int userHandle);

    abstract class Stub extends Binder implements IDevicePolicyManager {
        public static IDevicePolicyManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}

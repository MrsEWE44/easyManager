package android.app.usage;

import android.compat.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

public interface IUsageStatsManager extends IInterface {

    @UnsupportedAppUsage(maxTargetSdk = 30, trackingBug = 170729553)
    void setAppInactive(String packageName, boolean inactive, int userId);
    boolean isAppStandbyEnabled();
    @UnsupportedAppUsage(maxTargetSdk = 30, trackingBug = 170729553)
    boolean isAppInactive(String packageName, int userId, String callingPackage);
    int getAppStandbyBucket(String packageName, String callingPackage, int userId);
    void setAppStandbyBucket(String packageName, int bucket, int userId);

    abstract class Stub extends Binder implements IUsageStatsManager {
        public static IUsageStatsManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }

}

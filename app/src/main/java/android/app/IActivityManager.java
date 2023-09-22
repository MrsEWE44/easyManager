package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IActivityManager extends IInterface {

    void forceStopPackage(String packageName, int userId);
    boolean killPids( int[] pids,  String reason, boolean secure);

    abstract class Stub extends Binder implements IActivityManager {
        public static IActivityManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}

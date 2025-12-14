package android.app;

import android.content.pm.IPackageDataObserver;
import android.content.pm.UserInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IProgressListener;
import android.os.RemoteException;

import java.util.List;

public interface IActivityManager extends IInterface {

    void forceStopPackage(String packageName);
    void forceStopPackage(String packageName, int userId);
    boolean killPids( int[] pids,  String reason, boolean secure);
    boolean startUserInBackground(int userid);
    boolean startUserInBackgroundWithListener(int userid, IProgressListener unlockProgressListener);
    boolean startProfileWithListener(int userid, IProgressListener unlockProgressListener);
    int stopUser(int userid, boolean force,  IStopUserCallback callback);
    int stopUser(int userid, IStopUserCallback callback);
    UserInfo getCurrentUser();
    int getCurrentUserId();
    boolean clearApplicationUserData(String packageName, boolean keepState,
                                     IPackageDataObserver observer, int userId);
    boolean clearApplicationUserData(final String packageName,
                                     final IPackageDataObserver observer, int userId) throws RemoteException;
    List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses();



    abstract class Stub extends Binder implements IActivityManager {
        public static IActivityManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}

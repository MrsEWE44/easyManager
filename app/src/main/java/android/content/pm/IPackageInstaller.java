package android.content.pm;

import android.content.IntentSender;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;


import java.util.List;

/** {@hide} */
public interface IPackageInstaller extends IInterface {

    int createSession(android.content.pm.PackageInstaller.SessionParams params, String installerPackageName,
                      String installerAttributionTag, int userId);
    int createSession(PackageInstaller.SessionParams params, String installerPackageName, int userId);

    void abandonSession(int sessionId)
            throws RemoteException;

    PackageInstaller.SessionInfo getSessionInfo(int sessionId);
    IPackageInstallerSession openSession(int sessionId)
            throws RemoteException;

    ParceledListSlice<PackageInstaller.SessionInfo> getMySessions(String installerPackageName, int userId)
            throws RemoteException;

    ParceledListSlice getAllSessions(int userId);


    //android 5
    void uninstall(String packageName, int flags,  IntentSender statusReceiver, int userId);

    //android 8 以下
    void uninstall(String packageName, String callerPackageName, int flags,
                    IntentSender statusReceiver, int userId);

    //Android 8 或者 以上
    void uninstall(VersionedPackage versionedPackage, String callerPackageName, int flags,
                   IntentSender statusReceiver, int userId);

    void uninstallExistingPackage(VersionedPackage versionedPackage, String callerPackageName,
                                  IntentSender statusReceiver, int userId);

    void installExistingPackage(String packageName, int installFlags, int installReason,
                                 IntentSender statusReceiver, int userId,  List<String> whiteListedPermissions);

    void setPermissionsResult(int sessionId, boolean accepted);

    abstract class Stub extends Binder implements IPackageInstaller {

        public static IPackageInstaller asInterface(IBinder binder) {
            throw new UnsupportedOperationException();
        }
    }
}

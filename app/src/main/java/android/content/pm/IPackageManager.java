package android.content.pm;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

public interface IPackageManager extends IInterface {

    IPackageInstaller getPackageInstaller()
            throws RemoteException;

    ParceledListSlice getInstalledPackages(int flags, int userId);
    ParceledListSlice getInstalledPackages(long flags, int userId);
    PackageInfo getPackageInfo(String packageName, int flags, int userId);
    PackageInfo getPackageInfo(String packageName, long flags, int userId);
    PackageInfo getPackageInfo(String packageName, int flags);


    //android4-5
    boolean addPermission(PermissionInfo info);
    void removePermission(String name);
    void grantPermission(String packageName, String permissionName);
    void revokePermission(String packageName, String permissionName);

    //android6-10
    void grantRuntimePermission(String packageName, String permissionName, int userId);
    void revokeRuntimePermission(String packageName, String permissionName, int userId);

    int checkPermission(String permName, String pkgName);
    int checkPermission(String permName, String pkgName, int userId);
    int checkUidPermission(String permName, int uid);

    //android 4.0.x
    void installPackage( Uri packageURI, IPackageInstallObserver observer, int flags, String installerPackageName);
    //android4.4
    void installPackageWithVerification(Uri packageURI, IPackageInstallObserver observer,
                                        int flags,  String installerPackageName,  Uri verificationURI,
                                         ManifestDigest manifestDigest, ContainerEncryptionParams encryptionParams);

    void installPackageWithVerificationAndEncryption( Uri packageURI,
                                                      IPackageInstallObserver observer, int flags,  String installerPackageName,
                                                      VerificationParams verificationParams,
                                                      ContainerEncryptionParams encryptionParams);



    void deletePackageAsUser(String packageName, IPackageDeleteObserver observer, int userId, int flags);
    void deletePackage(String packageName, IPackageDeleteObserver observer, int flags);
    int getPackageUid(String packageName, long flags, int userId);
    int getPackageUid(String packageName, int userId);
    int getPackageUid(String packageName, int flags, int userId);
    int getPackageUid(String packageName);

    void setComponentEnabledSetting( ComponentName componentName,int newState,  int flags, int userId);

    void setComponentEnabledSettings(List<PackageManager.ComponentEnabledSetting> settings, int userId);

    void setComponentEnabledSetting(ComponentName componentName, int newState,int flags, int userId, String callingPackage);
    void setComponentEnabledSetting(ComponentName componentName, int newState, int flags);
    void setComponentEnabledSettings(List<PackageManager.ComponentEnabledSetting> settings, int userId, String callingPackage);

    int getComponentEnabledSetting(ComponentName componentName, int userId);
    int getComponentEnabledSetting(ComponentName componentName);
    void setApplicationEnabledSetting(String packageName,int newState, int flags,int userId, String callingPackage);
    void setApplicationEnabledSetting(String packageName, int newState, int flags, int userId);
    void setApplicationEnabledSetting(String packageName, int newState, int flags);
    int getApplicationEnabledSetting(String packageName, int userId);
    int getApplicationEnabledSetting(String packageName);


    boolean setApplicationHiddenSettingAsUser(String packageName, boolean hidden, int userId);
    boolean getApplicationHiddenSettingAsUser(String packageName, int userId);

    int installExistingPackageAsUser(String packageName, int userId);

    void installPackageAsUser(String originPath,
                               IPackageInstallObserver2 observer,
                              int flags,
                               String installerPackageName,
                               VerificationParams verificationParams,
                               String packageAbiOverride,
                              int userId);

    //android 4.1
    List<UserInfo> getUsers();
    UserInfo getUser(int userId);

    abstract class Stub extends Binder implements IPackageManager {

        public static IPackageManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}

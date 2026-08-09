package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

public interface IPackageInstallObserver extends IInterface {

    void packageInstalled( String packageName, int returnCode);

    abstract class Stub extends Binder implements IPackageInstallObserver {

        public static IPackageInstallObserver asInterface(IBinder binder) {
            throw new UnsupportedOperationException();
        }
    }

}

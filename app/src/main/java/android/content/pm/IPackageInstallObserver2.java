package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

public interface  IPackageInstallObserver2 extends IInterface {

    abstract class Stub extends Binder implements IPackageInstallObserver2 {

        public static IPackageInstallObserver2 asInterface(IBinder binder) {
            throw new UnsupportedOperationException();
        }
    }

}

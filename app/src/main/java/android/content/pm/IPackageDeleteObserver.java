package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

public interface  IPackageDeleteObserver  extends IInterface {
    void packageDeleted( String packageName,  int returnCode);

    abstract class Stub extends Binder implements IPackageDeleteObserver {

        public static IPackageDeleteObserver asInterface(IBinder binder) {
            throw new UnsupportedOperationException();
        }
    }

}

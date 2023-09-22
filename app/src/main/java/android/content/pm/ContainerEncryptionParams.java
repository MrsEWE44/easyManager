package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

public interface ContainerEncryptionParams extends IInterface {

    abstract class Stub extends Binder implements ContainerEncryptionParams {

        public static ContainerEncryptionParams asInterface(IBinder binder) {
            throw new UnsupportedOperationException();
        }
    }

}

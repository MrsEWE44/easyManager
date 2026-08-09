package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

public interface ManifestDigest extends IInterface {

    abstract class Stub extends Binder implements ManifestDigest {

        public static ManifestDigest asInterface(IBinder binder) {
            throw new UnsupportedOperationException();
        }
    }

}

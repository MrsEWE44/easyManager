package android.app;

import android.os.Binder;
import android.os.IBinder;

public abstract class ActivityManagerNative extends Binder implements IActivityManager {

    public static IActivityManager asInterface(IBinder obj) {
        throw new UnsupportedOperationException();
    }

}

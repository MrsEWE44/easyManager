package android.internal.os;

import android.compat.annotation.UnsupportedAppUsage;
import android.os.IBinder;

public class BinderInternal {

    @UnsupportedAppUsage
    public static final native IBinder getContextObject();

}

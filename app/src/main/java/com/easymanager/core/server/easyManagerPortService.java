package com.easymanager.core.server;

import android.os.IBinder;
import android.os.Parcel;
import android.text.TextUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class easyManagerPortService implements Serializable {

    private static final Map<String, IBinder> SYSTEM_SERVICE_CACHE = new HashMap<>();
    private static final Map<String, Integer> TRANSACT_CODE_CACHE = new HashMap<>();

    private static Method getService;

    static {
        Class<?> sm = null;
        try {
            sm = Class.forName("android.os.ServiceManager");
            getService = sm.getMethod("getService", String.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Returns a reference to a service with the given name.
     *
     * @param name the name of the service to get such as "package" for android.content.pm.IPackageManager
     * @return a reference to the service, or <code>null</code> if the service doesn't exist
     */
    public static IBinder getSystemService(String name) {
        IBinder binder = SYSTEM_SERVICE_CACHE.get(name);
        if (binder == null) {
            try {
                binder = (IBinder) getService.invoke(null, name);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            SYSTEM_SERVICE_CACHE.put(name, binder);
        }
        return binder;
    }

    /**
     * Returns transaction code from given class and method name.
     *
     * @param className  class name such as "android.content.pm.IPackageManager$Stub"
     * @param methodName method name such as "getInstalledPackages"
     * @return transaction code, or <code>null</code> if the class or the method doesn't exist
     * @deprecated Use {@link easyManagerBinderWrapper} instead
     */
    @Deprecated
    public static Integer getTransactionCode( String className, String methodName) {
        final String fieldName = "TRANSACTION_" + methodName;
        final String key = className + "." + fieldName;

        Integer value = TRANSACT_CODE_CACHE.get(key);
        if (value != null) return value;

        try {
            final Class<?> cls = Class.forName(className);
            Field declaredField = null;
            try {
                declaredField = cls.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                for (Field f : cls.getDeclaredFields()) {
                    if (f.getType() != int.class)
                        continue;

                    String name = f.getName();
                    if (name.startsWith(fieldName + "_")
                            && TextUtils.isDigitsOnly(name.substring(fieldName.length() + 1))) {
                        declaredField = f;
                        break;
                    }
                }
            }
            if (declaredField == null) {
                return null;
            }

            declaredField.setAccessible(true);
            value = declaredField.getInt(cls);

            TRANSACT_CODE_CACHE.put(key, value);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Deprecated
    public static Parcel obtainParcel( String serviceName,  String interfaceName,  String methodName) {
        return obtainParcel(serviceName, interfaceName, interfaceName + "$Stub", methodName);
    }

    @Deprecated
    public static Parcel obtainParcel( String serviceName, String interfaceName,  String className,  String methodName) {
        throw new UnsupportedOperationException("Direct use of Shizuku#transactRemote is no longer supported, please use ShizukuBinderWrapper");
    }
}

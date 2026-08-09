package android.content;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface  IContentProvider extends IInterface {


    //android11+
    Bundle call( AttributionSource attributionSource, String authority,
                String method, String arg,  Bundle extras) throws RemoteException;


    //android10
    public Bundle call(String callingPkg, String authority, String method,
                        String arg,  Bundle extras) throws RemoteException;


    //android4-9
    public Bundle call(
            String callingPkg, String method, String arg,  Bundle extras)
            throws RemoteException;


    abstract class Stub extends Binder implements IContentProvider {

        public static IContentProvider asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }

}

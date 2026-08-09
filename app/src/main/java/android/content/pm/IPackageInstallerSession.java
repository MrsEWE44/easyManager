package android.content.pm;

import android.content.IntentSender;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;

public interface IPackageInstallerSession extends IInterface {

    ParcelFileDescriptor openWrite(String name, long offsetBytes, long lengthBytes);
    ParcelFileDescriptor openRead(String name);

    void write(String name, long offsetBytes, long lengthBytes,  ParcelFileDescriptor fd);
    void close();
    void commit(IntentSender statusReceiver, boolean forTransferred);
    void transfer(String packageName);
    void abandon();

    abstract class Stub extends Binder implements IPackageInstallerSession {

        public static IPackageInstallerSession asInterface(IBinder binder) {
            throw new UnsupportedOperationException();
        }
    }
}

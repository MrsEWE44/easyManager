package android.os;

public interface IRemoteCallback extends IInterface{
    void sendResult( Bundle data);
    abstract class Stub extends Binder implements IRemoteCallback {
        public static IRemoteCallback asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}

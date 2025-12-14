package android.os;

public interface IProgressListener extends IInterface {

    abstract class Stub extends Binder implements IProgressListener {
        public static IProgressListener asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }

}

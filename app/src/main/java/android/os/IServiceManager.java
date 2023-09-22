package android.os;

public interface IServiceManager extends IInterface{

    public static final String descriptor = "android.os.IServiceManager";

    public IBinder getService(String name) throws RemoteException;

    /**
     * Return a list of all currently running services.
     */
    public String[] listServices() throws RemoteException;
    

}
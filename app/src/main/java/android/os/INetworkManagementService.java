package android.os;

public interface INetworkManagementService extends IInterface {

    void setFirewallUidRule(int chain, int uid, int rule);
    void setFirewallUidRules(int chain, int[] uids, int[] rules);


    abstract class Stub extends Binder implements INetworkManagementService {
        public static INetworkManagementService asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}

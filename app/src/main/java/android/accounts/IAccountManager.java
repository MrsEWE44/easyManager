package android.accounts;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

public interface IAccountManager extends IInterface {

    Account[] getAccountsAsUser(String accountType, int userId, String opPackageName);

    //Android6+
    boolean removeAccountExplicitly(Account account);


    abstract class Stub extends Binder implements IAccountManager {
        public static IAccountManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}

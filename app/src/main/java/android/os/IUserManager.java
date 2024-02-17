package android.os;

import android.content.pm.UserInfo;

import java.util.List;

public interface IUserManager extends IInterface {

    List<UserInfo> getUsers(boolean excludeDying);

    List<UserInfo> getUsers(boolean excludePartial, boolean excludeDying, boolean excludePreCreated);


    UserInfo createUser( String name, int flags);
    UserInfo createProfileForUser( String name, int flags, int userHandle);
    UserInfo createProfileForUser(String name, int flags, int userHandle, String[] disallowedPackages);
    UserInfo createProfileForUserWithThrow(String name, String userType, int flags, int userId, String[] disallowedPackages);
    void setUserEnabled(int userHandle);
    boolean removeUser(int userHandle);
    UserInfo getUserInfo(int userHandle);

    abstract class Stub extends Binder implements IUserManager {
        public static IUserManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}

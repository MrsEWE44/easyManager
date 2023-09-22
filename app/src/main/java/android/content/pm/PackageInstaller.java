package android.content.pm;


import android.content.IntentSender;

import java.io.IOException;
import java.io.OutputStream;

public class PackageInstaller {

    public static final String ACTION_SESSION_COMMITTED = "android.content.pm.action.SESSION_COMMITTED";
    public static final String ACTION_SESSION_DETAILS = "android.content.pm.action.SESSION_DETAILS";
    public static final String ACTION_SESSION_UPDATED = "android.content.pm.action.SESSION_UPDATED";
    public static final String EXTRA_OTHER_PACKAGE_NAME = "android.content.pm.extra.OTHER_PACKAGE_NAME";
    public static final String EXTRA_PACKAGE_NAME = "android.content.pm.extra.PACKAGE_NAME";
    public static final String EXTRA_SESSION = "android.content.pm.extra.SESSION";
    public static final String EXTRA_SESSION_ID = "android.content.pm.extra.SESSION_ID";
    public static final String EXTRA_STATUS = "android.content.pm.extra.STATUS";
    public static final String EXTRA_STATUS_MESSAGE = "android.content.pm.extra.STATUS_MESSAGE";
    public static final String EXTRA_STORAGE_PATH = "android.content.pm.extra.STORAGE_PATH";
    public static final int PACKAGE_SOURCE_DOWNLOADED_FILE = 4;
    public static final int PACKAGE_SOURCE_LOCAL_FILE = 3;
    public static final int PACKAGE_SOURCE_OTHER = 1;
    public static final int PACKAGE_SOURCE_STORE = 2;
    public static final int PACKAGE_SOURCE_UNSPECIFIED = 0;
    public static final int STATUS_FAILURE = 1;
    public static final int STATUS_FAILURE_ABORTED = 3;
    public static final int STATUS_FAILURE_BLOCKED = 2;
    public static final int STATUS_FAILURE_CONFLICT = 5;
    public static final int STATUS_FAILURE_INCOMPATIBLE = 7;
    public static final int STATUS_FAILURE_INVALID = 4;
    public static final int STATUS_FAILURE_STORAGE = 6;
    public static final int STATUS_PENDING_USER_ACTION = -1;
    public static final int STATUS_SUCCESS = 0;


    public static class SessionParams {

        public int installFlags = 0;
        public long sizeBytes = -1;
        public static final int MODE_INVALID = -1;

        public static final int MODE_FULL_INSTALL = 1;

        public static final int MODE_INHERIT_EXISTING = 2;

        public static final int UID_UNKNOWN = -1;


        public static final int MAX_PACKAGE_NAME_LENGTH = 255;


        public static final int USER_ACTION_UNSPECIFIED = 0;


        public static final int USER_ACTION_REQUIRED = 1;


        public static final int USER_ACTION_NOT_REQUIRED = 2;

        public Session openSession(int sessionId) throws IOException {
            return null;
        }
        public SessionParams(int mode) {
            throw new RuntimeException("Stub!");
        }
    }

    public static class Session{

        public void addProgress(float progress) {}

        public Session(IPackageInstallerSession session) { }

        public OutputStream openWrite( String name, long offsetBytes, long lengthBytes) throws IOException {
            throw new RuntimeException("Stub!");
        }

        public void fsync( OutputStream out) throws IOException {
            throw new RuntimeException("Stub!");
        }

        public void commit( IntentSender statusReceiver) {
            throw new RuntimeException("Stub!");
        }


        public void close() {
            throw new RuntimeException("Stub!");
        }

        public void abandon() {

        }
    }

    public static class SessionInfo {


        public int sessionId;

        public String installerPackageName;

        public String resolvedBaseCodePath;

        public float progress;

        public boolean sealed;

        public boolean active;


        public int mode;

        public long sizeBytes;

        public String appPackageName;

        public CharSequence appLabel;

        public SessionInfo() {
        }

    }


}

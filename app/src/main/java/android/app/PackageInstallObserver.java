package android.app;

import android.content.Intent;
import android.content.pm.IPackageInstallObserver2;
import android.os.Bundle;

public class PackageInstallObserver {
    public void onPackageInstalled(String basePackageName, int returnCode, String msg,
                                   Bundle extras) {
    }

    public IPackageInstallObserver2 getBinder() {
        return null;
    }

    public void onUserActionRequired(Intent intent) {
    }

}

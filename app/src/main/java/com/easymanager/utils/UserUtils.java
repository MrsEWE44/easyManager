package com.easymanager.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;

public class UserUtils {

    public int getCurrentUser(Activity activity){
        UserManager um = (UserManager) activity.getSystemService(Context.USER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (UserHandle userProfile : um.getUserProfiles()) {
                Log.d("userhandle",userProfile.toString() + " -- " + userProfile.hashCode());
            }
        }
        return -1;
    }

}

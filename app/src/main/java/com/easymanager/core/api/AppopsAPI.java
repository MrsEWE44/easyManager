package com.easymanager.core.api;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build;

import com.android.internal.app.IAppOpsService;
import com.easymanager.core.enums.AppopsPermissionStr;
import com.easymanager.core.server.Singleton;
import com.easymanager.core.server.easyManagerBinderWrapper;
import com.easymanager.core.server.easyManagerPortService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppopsAPI extends baseAPI{
    public final static int PhoneAndSMS = 0;
    public final static int Storage = 1;
    public final static int Clipboard = 2;
    public final static int RunAnyInBackgroud = 3;
    public final static int RunInBackgroud = 4;
    public final static int CameraAndAudio = 5;
    public final static int Location = 6;
    public final static int Calendar = 7;
    public final static int SENSORSSCAN = 8;
    public final static int Notification = 9;

    public final static int MODE_ALLOW = 0;
    public final static int MODE_DEFAULT = 1;
    public final static int MODE_IGNORE = 2;
    public final static int MODE_FOREGROUND = 3;

    private static final Map<String, IAppOpsService> I_APP_OPS_SERVICE_CACHE = new HashMap<>();
    private PackageAPI packageAPI = new PackageAPI();

    public void SetInactive(String pkgname,boolean b){
        packageAPI.SetInactive(pkgname,b);
    }

    public void SetStandbyBucket(String pkgname,String op){
        packageAPI.SetStandbyBucket(pkgname, op);
    }

    public void setModeCore(String pkgname,int mode,int mode2){
        String modestr = getSetModeStr(mode);
        setModeCore(pkgname,modestr,mode2);
    }

    public void setModeCore(String pkgname,String modestr,int mode2){
        for (String op : getOPS(getAppopsMode(mode2))) {
            SetMode(pkgname,op,modestr);
        }
    }


    public void setNotificationOnAllow(String pkgname){
        setModeCore(pkgname,0,Notification);
    }

    public void setNotificationOnDefault(String pkgname){
        setModeCore(pkgname,1,Notification);
    }

    public void setNotificationOnIgnore(String pkgname){
        setModeCore(pkgname,2,Notification);
    }

    public void setNotificationOnForeground(String pkgname){
        setModeCore(pkgname,3,Notification);
    }

    public void setSENSORSSCANOnAllow(String pkgname){
        setModeCore(pkgname,0,SENSORSSCAN);
    }

    public void setSENSORSSCANOnDefault(String pkgname){
        setModeCore(pkgname,1,SENSORSSCAN);
    }

    public void setSENSORSSCANOnIgnore(String pkgname){
        setModeCore(pkgname,2,SENSORSSCAN);
    }

    public void setSENSORSSCANOnForeground(String pkgname){
        setModeCore(pkgname,3,SENSORSSCAN);
    }

    public void setCalendarOnAllow(String pkgname){
        setModeCore(pkgname,0,Calendar);
    }

    public void setCalendarOnDefault(String pkgname){
        setModeCore(pkgname,1,Calendar);
    }

    public void setCalendarOnIgnore(String pkgname){
        setModeCore(pkgname,2,Calendar);
    }

    public void setCalendarOnForeground(String pkgname){
        setModeCore(pkgname,3,Calendar);
    }

    public void setLocationOnAllow(String pkgname){
        setModeCore(pkgname,0,Location);
    }

    public void setLocationOnDefault(String pkgname){
        setModeCore(pkgname,1,Location);
    }

    public void setLocationOnIgnore(String pkgname){
        setModeCore(pkgname,2,Location);
    }

    public void setLocationOnForeground(String pkgname){
        setModeCore(pkgname,3,Location);
    }

    public void setCameraAndAudioOnAllow(String pkgname){
        setModeCore(pkgname,0,CameraAndAudio);
    }

    public void setCameraAndAudioOnDefault(String pkgname){
        setModeCore(pkgname,1,CameraAndAudio);
    }

    public void setCameraAndAudioOnIgnore(String pkgname){
        setModeCore(pkgname,2,CameraAndAudio);
    }

    public void setCameraAndAudioOnForeground(String pkgname){
        setModeCore(pkgname,3,CameraAndAudio);
    }

    public void setRunInBackgroudOnAllow(String pkgname){
        setModeCore(pkgname,0,RunInBackgroud);
    }

    public void setRunInBackgroudOnDefault(String pkgname){
        setModeCore(pkgname,1,RunInBackgroud);
    }

    public void setRunInBackgroudOnIgnore(String pkgname){
        setModeCore(pkgname,2,RunInBackgroud);
    }

    public void setRunInBackgroudOnForeground(String pkgname){
        setModeCore(pkgname,3,RunInBackgroud);
    }

    public void setRunAnyInBackgroudOnAllow(String pkgname){
        setModeCore(pkgname,0,RunAnyInBackgroud);
    }

    public void setRunAnyInBackgroudOnDefault(String pkgname){
        setModeCore(pkgname,1,RunAnyInBackgroud);
    }

    public void setRunAnyInBackgroudOnIgnore(String pkgname){
        setModeCore(pkgname,2,RunAnyInBackgroud);
    }

    public void setRunAnyInBackgroudOnForeground(String pkgname){
        setModeCore(pkgname,3,RunAnyInBackgroud);
    }

    public void setClipboardOnAllow(String pkgname){
        setModeCore(pkgname,0,Clipboard);
    }

    public void setClipboardOnDefault(String pkgname){
        setModeCore(pkgname,1,Clipboard);
    }

    public void setClipboardOnIgnore(String pkgname){
        setModeCore(pkgname,2,Clipboard);
    }

    public void setClipboardOnForeground(String pkgname){
        setModeCore(pkgname,3,Clipboard);
    }

    public void setStorageOnAllow(String pkgname){
        setModeCore(pkgname,0,Storage);
    }

    public void setStorageOnDefault(String pkgname){
        setModeCore(pkgname,1,Storage);
    }

    public void setStorageOnIgnore(String pkgname){
        setModeCore(pkgname,2,Storage);
    }

    public void setStorageOnForeground(String pkgname){
        setModeCore(pkgname,3,Storage);
    }

    public void setPhoneAndSMSOnAllow(String pkgname){
        setModeCore(pkgname,0,PhoneAndSMS);
    }

    public void setPhoneAndSMSOnDefault(String pkgname){
        setModeCore(pkgname,1,PhoneAndSMS);
    }

    public void setPhoneAndSMSOnIgnore(String pkgname){
        setModeCore(pkgname,2,PhoneAndSMS);
    }

    public void setPhoneAndSMSOnForeground(String pkgname){
        setModeCore(pkgname,3,PhoneAndSMS);
    }


    public int getAppopsMode(int APP_PERMIS_INDEX){
        if(APP_PERMIS_INDEX == 0){
            return PhoneAndSMS;
        }

        if(APP_PERMIS_INDEX == 1){
            return Storage;
        }

        if(APP_PERMIS_INDEX == 2){
            return Clipboard;
        }

        if(APP_PERMIS_INDEX == 3){
            return RunAnyInBackgroud;
        }

        if(APP_PERMIS_INDEX == 4){
            return RunInBackgroud;
        }

        if(APP_PERMIS_INDEX == 5){
            return CameraAndAudio;
        }

        if(APP_PERMIS_INDEX == 6){
            return Location;
        }

        if(APP_PERMIS_INDEX == 7){
            return Calendar;
        }

        if(APP_PERMIS_INDEX == 8){
            return SENSORSSCAN;
        }

        if(APP_PERMIS_INDEX == 9){
            return Notification;
        }

        return -1;
    }


    public String getSetModeStr(int mode){
        switch (mode){
            case MODE_ALLOW:
                return "allow";
            case MODE_DEFAULT:
                return "default";
            case MODE_IGNORE:
                return "ignore";
            case MODE_FOREGROUND:
                return "foreground";
        }
        return null;
    }

    //通过调用appops系统api来修改应用权限
    public void SetMode(String pkgname , String opstr , String opmode){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            IAppOpsService iAppOpsService = I_APP_OPS_SERVICE_CACHE.get("iappservice");
            if(iAppOpsService == null){
                Singleton<IAppOpsService> iAppOpsServiceSingleton = new Singleton<IAppOpsService>() {
                    @Override
                    protected IAppOpsService create() {
                        return IAppOpsService.Stub.asInterface(new easyManagerBinderWrapper(easyManagerPortService.getSystemService(Context.APP_OPS_SERVICE)));
                    }
                };
                iAppOpsService = iAppOpsServiceSingleton.get();
                I_APP_OPS_SERVICE_CACHE.put("iappservice",iAppOpsService);
            }
            try {
                int opcode = AppOpsManager.strOpToOp(opstr);
                int uid = packageAPI.getPKGUID(pkgname);
                iAppOpsService.setMode(opcode, uid,pkgname,getModeInt(opmode));
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    iAppOpsService.setUidMode(opcode,uid,getModeInt(opmode));
                }
            }catch (Exception e){

            }

        }
    }


    public int getModeInt(String modestr){
        if(modestr.equals("allow")){
            return AppOpsManager.MODE_ALLOWED;
        }

        if(modestr.equals("ignore")){
            return AppOpsManager.MODE_IGNORED;
        }
        if(modestr.equals("default")){
            return AppOpsManager.MODE_DEFAULT;
        }
        if(modestr.equals("foreground")){
            return AppOpsManager.MODE_FOREGROUND;
        }
        return  -1;
    }

    public ArrayList<String> getOPS(int mode){
        ArrayList<String> ops = new ArrayList<>();
        AppopsPermissionStr permissionStr = new AppopsPermissionStr();
        if(mode == PhoneAndSMS){
            permissionStr.getPhoneAndSMSPermissonStr(ops);
        }

        if(mode == Storage){
            permissionStr.getStoragePermissionStr(ops);
        }

        if(mode == Clipboard){
            permissionStr.getClipboardPermissionStr(ops);
        }

        if(mode == RunAnyInBackgroud){
            permissionStr.getRunAnyInBackgroudPermissionStr(ops);
        }

        if(mode == RunInBackgroud){
            permissionStr.getRunInBackgroudPermissionStr(ops);
        }

        if(mode == CameraAndAudio){
            permissionStr.getCameraAndAudioPermissionStr(ops);
        }

        if(mode == Location){
            permissionStr.getLocationPermissionStr(ops);
        }

        if(mode == Calendar){
            permissionStr.getCalendarPermissionStr(ops);
        }

        if(mode == SENSORSSCAN){
            permissionStr.getSenSorsScanPermissionStr(ops);
        }

        if(mode == Notification){
            permissionStr.getNotificationPermissionStr(ops);
        }
        return ops;
    }

}

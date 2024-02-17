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
    public final static int Fingerprint = 10;
    public final static int AlertWindow = 11;
    public final static int Accessibility = 12;
    public final static int Account = 13;
    public final static int WriteSettings = 14;
    public final static int DeviceIdentifiers = 15;

    private static final Map<String, IAppOpsService> I_APP_OPS_SERVICE_CACHE = new HashMap<>();
    private PackageAPI packageAPI = new PackageAPI();

    public IAppOpsService getIAppOpsService(){
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
        return iAppOpsService;
    }


    public void setModeCore(String pkgname,String modestr,int mode2,int uid){
        for (String op : getOPS(getAppopsMode(mode2))) {
            SetMode(pkgname,op,modestr,uid);
        }
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

        if(APP_PERMIS_INDEX == 10){
            return Fingerprint;
        }

        if(APP_PERMIS_INDEX == 11){
            return AlertWindow;
        }

        if(APP_PERMIS_INDEX == 12){
            return Accessibility;
        }

        if(APP_PERMIS_INDEX == 13){
            return Account;
        }

        if(APP_PERMIS_INDEX == 14){
            return WriteSettings;
        }

        if(APP_PERMIS_INDEX == 15){
            return DeviceIdentifiers;
        }

        return -1;
    }

    public int checkOp(String opstr, String packageName, int uid){
        try {
            return getIAppOpsService().checkOperation(AppOpsManager.strOpToOp(opstr),uid,packageName);
        }catch (Exception e){
            return AppOpsManager.MODE_DEFAULT;
        }
    }

    //通过调用appops系统api来修改应用权限
    public void SetMode(String pkgname , String opstr , String opmode,int userid){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            IAppOpsService iAppOpsService = getIAppOpsService();
            try {
                int opcode = AppOpsManager.strOpToOp(opstr);
                int uid = packageAPI.getPKGUID(pkgname,userid);
                iAppOpsService.setMode(opcode, uid,pkgname,getModeInt(opmode));
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    iAppOpsService.setUidMode(opcode,uid,getModeInt(opmode));
                }
            }catch (Exception e){}

        }
    }


    public int getModeInt(String modestr){
        if(modestr.equals("allow")){
            return AppOpsManager.MODE_ALLOWED;
        }
        if(modestr.equals("deny")){
            return AppOpsManager.MODE_ERRORED;
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

        if(mode == Fingerprint){
            permissionStr.getFigerprintPermissionStr(ops);
        }
        if(mode == AlertWindow){
            permissionStr.getAlertWindowPermissionStr(ops);
        }

        if(mode == Accessibility){
            permissionStr.getAccessibilityPermissionStr(ops);
        }

        if(mode == Account){
            permissionStr.getAccountPermissionStr(ops);
        }

        if(mode == WriteSettings){
            permissionStr.getWriteSettingsPermissionStr(ops);
        }

        if(mode == DeviceIdentifiers){
            permissionStr.getDeviceIdentifiersPermissionStr(ops);
        }

        return ops;
    }


    public void setPermissionStr(String pkgname, String permission_str, boolean b,int userid) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int opCode = AppOpsManager.permissionToOpCode(permission_str);
            if(opCode > -1){
                SetMode(pkgname,AppOpsManager.opToPermission(opCode), b? "allow":"ignore",userid);
            }

        }

    }
}

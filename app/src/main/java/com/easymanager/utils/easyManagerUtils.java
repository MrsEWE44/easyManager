package com.easymanager.utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Process;

import com.easymanager.core.api.DhizukuSystemServerApi;
import com.easymanager.core.api.PackageAPI;
import com.easymanager.core.api.ShizukuSystemServerApi;
import com.easymanager.core.server.easyManagerAPI;
import com.easymanager.entitys.MyAppopsInfo;
import com.easymanager.entitys.MyPackageInfo;
import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.core.utils.CMD;
import com.easymanager.core.utils.FileUtils;
import com.easymanager.mylife.easyMDeviceAdminReceiver;


import java.util.List;


public class easyManagerUtils {

    private DevicePolicyManager easyMDPM = null;

    private boolean skipError = true;

    private ComponentName easyMDPMComName = null;

    private easyManagerAPI managerAPI = new easyManagerAPI();

    public void setSkipError(boolean state){
        skipError = state;
    }

    public boolean getSkipError(){
        return skipError;
    }



    public DevicePolicyManager getEasyMDPM(Context context){
        if(easyMDPM == null){
            easyMDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        }
        return easyMDPM;
    }

    public ComponentName getEasyMDPMComName(Context context){
        if(easyMDPMComName == null){
            easyMDPMComName = new ComponentName(context, easyMDeviceAdminReceiver.class);
        }
        return easyMDPMComName;
    }

    public boolean isDeviceOwner(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return getEasyMDPM(context).isDeviceOwnerApp(context.getPackageName());
        }
        return false;
    }

    public boolean isProfileOwner(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getEasyMDPM(context).isProfileOwnerApp(context.getPackageName());
        }
        return false;
    }

    public boolean isAdminActive(Context context){
        return getEasyMDPM(context).isAdminActive(getEasyMDPMComName(context));
    }

    public boolean isDeviceOwnerActive(Context context){
        return isDeviceOwner(context) && isAdminActive(context);
    }

    public void removeDeviceOwner(Context context){
        if(isDeviceOwnerActive(context)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getEasyMDPM(context).clearDeviceOwnerApp(context.getPackageName());
            }
        }
    }

    public void forceRemoveDeviceOwner(Context context){
        if(isDeviceOwnerActive(context)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getEasyMDPM(context).clearDeviceOwnerApp(context.getPackageName());
            }
        }

        if(isProfileOwner(context)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getEasyMDPM(context).clearProfileOwner(getEasyMDPMComName(context));
            }
        }

        if(isAdminActive(context)){
            getEasyMDPM(context).removeActiveAdmin(getEasyMDPMComName(context));
        }
    }

    public CMD runCMD(String cmdstr){
        return new CMD(cmdstr);
    }

    public void killpkg(TransmissionEntity entity){
        if(skipError){
            managerAPI.killpkg(entity.getPkgname(),entity.getUid());
        }
    }

    public void setAppopsMode(TransmissionEntity entity){
        if(skipError){
            managerAPI.setAppopsMode(entity);
        }
    }

    public void setAppopsModeCore(TransmissionEntity entity){
        if(skipError){
            String modestr = entity.getOpmodestr();
            String[] split = modestr.split("---");
            if(split  != null && split.length > 0){
                managerAPI.setAppopsModeCore(entity.getPkgname(), split[0], split[1],entity.getUid());
            }
        }
    }

    public void installAPK(TransmissionEntity entity){
        if(skipError){
            managerAPI.installAPK(entity.getPkgname(),entity.getUid());
        }
    }
    public void installExistingPKG(Context context , String pkgname,int uid){
        if(skipError){
            TransmissionEntity entity = new TransmissionEntity(pkgname, null, context.getPackageName(), 0, uid);
            managerAPI.installExistingPKG(entity.getPkgname(),entity.getUid());
        }
    }

    public void uninstallAPK(Context context , String pkgname,int uid){
        if (skipError) {
            TransmissionEntity entity = new TransmissionEntity(pkgname, null, context.getPackageName(), 0, uid);
            managerAPI.uninstallApp(entity.getPkgname(),entity.getUid());
        }
    }

    public void setComponentOrPackageEnabledState(TransmissionEntity entity){
        if(skipError){
            managerAPI.setComponentOrPackageEnabledState(entity.getPkgname(),entity.getOpsmode(),entity.getUid());
        }
    }

    public void setPackagesSuspend(Context context ,boolean status, String pkgname,int uid){
        int mode=  status ? 0 : -1;
        if(skipError){
            TransmissionEntity entity = new TransmissionEntity(pkgname,null,context.getPackageName(),mode,uid);
            managerAPI.setPackagesSuspendedAsUser(entity.getPkgname(),entity.getOpsmode()==0,entity.getUid());
        }
    }

    public void setEnablePKG(Context context , String pkgname,int uid){
        if(isDeviceOwnerActive(context)){
            setPackagesSuspend(context,false,pkgname,uid);
        }else{
            if(ShizukuSystemServerApi.isShizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU){
                int state = getComponentOrPackageEnabledState(context,pkgname,null,uid);
                if(state == PackageAPI.COMPONENT_ENABLED_STATE_DISABLED || state == PackageAPI.COMPONENT_ENABLED_STATE_DISABLED_USER || state == PackageAPI.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED){
                    setComponentOrPackageEnabledState(new TransmissionEntity(pkgname,null, context.getPackageName(), PackageAPI.COMPONENT_ENABLED_STATE_ENABLED,uid));
                }
                state = getPackageSuspend(context,pkgname,uid);
                if(state == 0){
                    setPackagesSuspend(context,false,pkgname,uid);
                    setPackageHideState(context,false,pkgname,uid);
                }
            }

            if(DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU){
                setPackagesSuspend(context,false,pkgname,uid);
            }

        }



    }

    public void setDisablePKG(Context context , String pkgname,int uid){
        if(isDeviceOwnerActive(context)){
            setPackagesSuspend(context,true,pkgname,uid);
        }else{
            if(ShizukuSystemServerApi.isShizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU){
                clearPackageData(context,pkgname,uid);
                setComponentOrPackageEnabledState(new TransmissionEntity(pkgname,null, context.getPackageName(), PackageAPI.COMPONENT_ENABLED_STATE_DISABLED_USER,uid));
                int state = getComponentOrPackageEnabledState(context,pkgname,null,uid);
                if(state != PackageAPI.COMPONENT_ENABLED_STATE_DISABLED_USER){
                    setPackagesSuspend(context,true,pkgname,uid);
                    setPackageHideState(context,true,pkgname,uid);
                }
            }

            if(DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU){
                setPackagesSuspend(context,true,pkgname,uid);
            }


        }
    }

    public void clearPackageData(Context context , String pkgname,int uid){
        if(skipError){
            TransmissionEntity entity = new TransmissionEntity(pkgname,null,context.getPackageName(),0,uid);
            managerAPI.clearPackageData(entity.getPkgname(), entity.getUid());
        }
    }

    public void setPackageHideState(Context context ,boolean status, String pkgname,int uid){
        int mode=  status ? 0 : -1;
        if(skipError){
            TransmissionEntity entity = new TransmissionEntity(pkgname,null,context.getPackageName(),mode,uid);
            managerAPI.setPackageHideState(entity.getPkgname(),entity.getOpsmode()==0,entity.getUid());

        }
    }

    public void revokeRuntimePermission(TransmissionEntity entity){
        if(skipError){
            managerAPI.revokeRuntimePermission(entity.getPkgname(), entity.getOpmodestr(),entity.getUid());
        }

    }

    public void grantRuntimePermission(TransmissionEntity entity){
        if(skipError){
            managerAPI.grantRuntimePermission(entity.getPkgname(),entity.getOpmodestr(),entity.getUid());
        }

    }
    public void dumpAPK(String apksourcepath,String outpath){
        FileUtils fu = new FileUtils();
        fu.copyFile(apksourcepath,outpath);
    }

    public String[] getDisallowedPackages(Context context) {
        if(skipError){
            return managerAPI.getDisallowedPackages();
        }
        return null;
    }

    public int getMaxSupportedUsers(Context context) {
        if(skipError){
            return managerAPI.getMaxSupportedUsers();
        }
        return -1;
    }

    // 读取属性值，无默认值
    public String getProp(Context context,String key) {
        if(skipError){
            TransmissionEntity entity = new TransmissionEntity(null,key,context.getPackageName(),0,getCurrentUserID());
            return managerAPI.getSYSProp(entity.getOpmodestr());
        }
        return "";
    }

    public void createAppClone(Context context) {
        if(skipError){
            managerAPI.createAppClone();
        }
    }

    public void removeAppClone(Context context , int removeuid) {
        if(skipError){
            TransmissionEntity entity = new TransmissionEntity(null,null,context.getPackageName(),0,removeuid);
            managerAPI.removeAppClone(entity.getUid());
        }
    }

    public void startAppClone(Context context , int startuid) {
        if(skipError){
            TransmissionEntity entity = new TransmissionEntity(null,null,context.getPackageName(),0,startuid);
            managerAPI.startUser(entity.getUid());
        }
    }

    public String[] getAppCloneUsers() {
        try {
            if(skipError){
                return managerAPI.getUsers();
            }
        }catch (Exception e){
            return new String[]{"0"};
        }
        return new String[]{"0"};
    }

    public List<MyPackageInfo> getInstalledPackages(TransmissionEntity entity){
        if(skipError){
            return managerAPI.getInstalledPackages(entity.getUid());
        }
        return null;
    }

    public MyPackageInfo getMyPackageInfo(TransmissionEntity entity){
        if(skipError){
            return managerAPI.getMyPackageInfo(entity.getPkgname(), entity.getUid());
        }
        return  null;
    }

    public int getCurrentUserID(){
        try{
            return managerAPI.getCurrentUserID();
        }catch (Throwable e){
            return Process.myUid();
        }
    }

    public int getComponentEnabledSetting(Context context , String pkgname,String componentName,int uid){
        if(skipError){
            if(componentName == null){
                componentName = "";
            }
            TransmissionEntity entity = new TransmissionEntity(pkgname, componentName, context.getPackageName(), 0, uid);
            return managerAPI.getComponentEnabledSetting(new ComponentName(entity.getPkgname(),entity.getOpmodestr()),entity.getUid());
        }
        return -1;
    }

    public int getComponentOrPackageEnabledState(Context context , String pkgname,String componentName,int uid){
        if(skipError){
            if(componentName != null){
                pkgname = pkgname +"/"+componentName;
            }
            TransmissionEntity entity = new TransmissionEntity(pkgname, null, context.getPackageName(), 0, uid);
            return managerAPI.getComponentOrPackageEnabledState(entity.getPkgname(),entity.getUid());
        }
        return -1;

    }

    public int getPackageSuspend(Context context , String pkgname,int uid){
        if(skipError){
            TransmissionEntity entity = new TransmissionEntity(pkgname, null, context.getPackageName(), 0, uid);
            return managerAPI.isPackageSuspendedForUser(entity.getPkgname(),entity.getUid());
        }
        return -1;
    }

    public int checkOp(Context context , String pkgname,String opstr,int uid){
        if(skipError){
            TransmissionEntity entity = new TransmissionEntity(pkgname, opstr, context.getPackageName(), 0, uid);
            return managerAPI.checkOp(entity.getOpmodestr(), entity.getPkgname(), entity.getUid());
        }
        return -1;
    }

    public int checkPermission(Context context , String pkgname,String opstr,int uid){
        if(skipError){
            TransmissionEntity entity = new TransmissionEntity(pkgname, opstr, context.getPackageName(), 0, uid);
            return managerAPI.checkPermission(entity.getPkgname(),entity.getOpmodestr(),entity.getUid());
        }
        return -1;
    }

    public List<MyAppopsInfo> getAppopsPKGPermissions(TransmissionEntity entity){
        if(skipError){
            return managerAPI.getAppopsPKGPermissions(entity.getPkgname(), entity.getUid());
        }
        return null;
    }

    public String permissionToOp(Context context, String permission, Integer uid) {
        if(skipError){
            TransmissionEntity entity = new TransmissionEntity(null, permission, context.getPackageName(), 0, uid);
            return managerAPI.permissionToOp(entity.getOpmodestr());
        }
        return null;
    }

    public int strOpToOp(Context context , String pkgname,String opstr,int uid){
        if(skipError){
            TransmissionEntity entity = new TransmissionEntity(pkgname, opstr, context.getPackageName(), 0, uid);
            return managerAPI.strOpToOp(entity.getOpmodestr());
        }
        return -1;
    }

    public void unlockMaxLimit(Context context, int maxNum) {
        if(skipError){
            TransmissionEntity entity = new TransmissionEntity(null,null,context.getPackageName(),maxNum,0);
            String cmdstr = String.format("resetprop --delete fw.max_users; resetprop ro.debuggable 1;setprop persist.sys.max_profiles %d ;setprop fw.show_multiuserui 1;am restart;",entity.getOpsmode());
            CMD cmd = new CMD(cmdstr);
            System.exit(0);
        }
    }

    public List<String> getActiveAdmins(Context context , int uid){
        if(skipError){
            TransmissionEntity entity = new TransmissionEntity(null, null, context.getPackageName(), 0, uid);
            return managerAPI.getActiveAdmins(entity.getUid());
        }
        return null;
    }

}

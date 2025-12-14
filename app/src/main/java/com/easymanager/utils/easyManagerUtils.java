package com.easymanager.utils;

import android.content.Context;
import android.os.Process;

import com.easymanager.entitys.MyAppopsInfo;
import com.easymanager.entitys.MyPackageInfo;
import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.core.entity.easyManagerClientEntity;
import com.easymanager.core.entity.easyManagerServiceEntity;
import com.easymanager.core.utils.CMD;
import com.easymanager.core.utils.FileUtils;
import com.easymanager.enums.easyManagerEnums;
import com.easymanager.mylife.adbClient;
import com.easymanager.mylife.startAdbService;

import java.util.HashMap;
import java.util.List;


public class easyManagerUtils {

    private easyManagerServiceEntity ee = null;

    private boolean skipError = true;
    private boolean isDead = false;
    private String currentErrorStr ;

    private void putOptionOnServer(easyManagerClientEntity adben2){
        adbClient ac = new adbClient(adben2, new adbClient.SocketListener() {
            @Override
            public easyManagerServiceEntity getAdbEntity(easyManagerServiceEntity adfb) {
                return null;
            }
        });
        ee = ac.getAdbEntity();
        isDead = ee.isDead();
        currentErrorStr = ee.getErrorMsg();
    }

    private easyManagerServiceEntity getEasyManagerServiceEntity(){
        if(ee == null){
            throw new NullPointerException();
        }
        return ee;
    }

    public void setSkipError(boolean state){
        skipError = state;
    }

    public boolean getSkipError(){
        return skipError;
    }

    public boolean isDead(){
        return isDead;
    }

    public String getERRORMSG(){
        return currentErrorStr;
    }

    public void activeRoot(Context context) {
        String cmdstr ="killall EAMADB\n" +
                "exec app_process -Djava.class.path=\""+context.getApplicationInfo().sourceDir+"\" /system/bin --nice-name=EAMADB "+ startAdbService.class.getName()+" >>/dev/null 2>&1 &\n" +
                "echo \"run EAMADB ok [root]\"";
        CMD cmd = new CMD(cmdstr);
    }

    public void dead(){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.DEAD);
        putOptionOnServer(adben2);
    }


    public boolean isADB(){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.IS_ADB);
        return checkBool(adben2);
    }

    public boolean isROOT(){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.IS_ROOT);
        return checkBool(adben2);
    }

    public boolean checkBool(easyManagerClientEntity adben2){
        try{
            putOptionOnServer(adben2);
            return  (boolean) getEasyManagerServiceEntity().getObject();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public CMD runCMD(String cmdstr){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(cmdstr,null,-1);
        putOptionOnServer(adben2);
        return getEasyManagerServiceEntity().getCmd();
    }

    public void killpkg(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.KILL_PROCESS);
            putOptionOnServer(adben2);
        }
    }

    public void setAppopsMode(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_APPOPS);
            putOptionOnServer(adben2);
        }
    }

    public void setAppopsModeCore(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_APPOPS_CORE);
            putOptionOnServer(adben2);
        }
    }

    public void installAPK(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.INSTALL_APK);
            putOptionOnServer(adben2);
        }
    }
    public void installExistingPKG(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.INSTALL_EXISTING_APK);
            putOptionOnServer(adben2);
        }
    }

    public void uninstallAPK(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.UNINSTALL_APK);
            putOptionOnServer(adben2);
        }
    }

    public void setComponentOrPackageEnabledState(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_COMPONENT_OR_PACKAGE_ENABLE_STATE);
            putOptionOnServer(adben2);
        }
    }

    public void setPackagesSuspend(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_PACKAGE_SUSPEND);
            putOptionOnServer(adben2);
        }
    }

    public void clearPackageData(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.CLEAR_PACKAGE_DATA);
            putOptionOnServer(adben2);
        }
    }

    public void addRunningAPPS(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.ADD_RUNNING_PACKAGE);
            putOptionOnServer(adben2);
        }
    }

    public void startStopRunningAPP(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.START_STOP_RUNNING_PACKAGE);
            putOptionOnServer(adben2);
        }
    }

    public void setPackageHideState(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_PACKAGE_HIDE_STATE);
            putOptionOnServer(adben2);
        }
    }

    public void revokeRuntimePermission(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_PACKAGE_REVOKE_RUNTIME_PERMISSION);
            putOptionOnServer(adben2);
        }

    }

    public void grantRuntimePermission(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_PACKAGE_GRANT_RUNTIME_PERMISSION);
            putOptionOnServer(adben2);
        }

    }

    public void setFirewallState(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.APP_FIREWALL);
            putOptionOnServer(adben2);
        }

    }

    public void dumpAPK(String apksourcepath,String outpath){
        FileUtils fu = new FileUtils();
        fu.copyFile(apksourcepath,outpath);
    }

    public void backupApk(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.BACKUP_APK);
            putOptionOnServer(adben2);
        }
    }

    public void restoryApp(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.RESTORY_APK);
            putOptionOnServer(adben2);
        }
    }

    public void deleteCleanAPPConfig(){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.DELETE_CLEAN_APP_CONFIG);
            putOptionOnServer(adben2);
        }

    }

    public void requestGrantUser(Context context){
        TransmissionEntity entity = new TransmissionEntity(context.getPackageName(), context.getFilesDir().toString(), context.getPackageName(), -1,getCurrentUserID());
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.GRANT_USER);
        putOptionOnServer(adben2);
    }

    public void changeGrantUserState(String pkg){
        TransmissionEntity entity = new TransmissionEntity(pkg, null, "com.easymanager", -1,getCurrentUserID());
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.CHANGE_USER);
        putOptionOnServer(adben2);
    }

    public Boolean getServerStatus(){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.GET_SERVER_STATUS);
        return checkBool(adben2);
    }

    public HashMap<String,Integer> getGrantUsers(Context context){
        TransmissionEntity entity = new TransmissionEntity(context.getPackageName(), context.getFilesDir().toString(), context.getPackageName(), -1,getCurrentUserID());
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.GET_GRANT_USERS);
        putOptionOnServer(adben2);
        return (HashMap<String,Integer>) getEasyManagerServiceEntity().getObject();
    }

    public int getMaxSupportedUsers(Context context) {
        if(!isDead && skipError){
            TransmissionEntity entity = new TransmissionEntity(null,null,context.getPackageName(),0,getCurrentUserID());
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.GET_MAX_USERS);
            putOptionOnServer(adben2);
            return (int) getEasyManagerServiceEntity().getObject();
        }
        return -1;
    }

    public void createAppClone(Context context) {
        if(!isDead && skipError){
            TransmissionEntity entity = new TransmissionEntity(null,null,context.getPackageName(),0,getCurrentUserID());
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.APP_CLONE);
            putOptionOnServer(adben2);
        }
    }

    public void removeAppClone(Context context , int removeuid) {
        if(!isDead && skipError){
            TransmissionEntity entity = new TransmissionEntity(null,null,context.getPackageName(),0,removeuid);
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.APP_CLONE_REMOVE);
            putOptionOnServer(adben2);
        }
    }

    public void startAppClone(Context context , int startuid) {
        if(!isDead && skipError){
            TransmissionEntity entity = new TransmissionEntity(null,null,context.getPackageName(),0,startuid);
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.START_USER_ID);
            putOptionOnServer(adben2);
        }
    }

    public String[] getAppCloneUsers() {
        try {
            if(!isDead && skipError){
                easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.APP_CLONE_GETUSERS);
                putOptionOnServer(adben2);
                return (String[]) getEasyManagerServiceEntity().getObject();
            }
        }catch (Exception e){
            return new String[]{"0"};
        }
        return new String[]{"0"};
    }

    public List<MyPackageInfo> getInstalledPackages(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.QUERY_PACKAGES_UID);
            putOptionOnServer(adben2);
            return (List<MyPackageInfo>) getEasyManagerServiceEntity().getObject();
        }
        return null;
    }

    public MyPackageInfo getMyPackageInfo(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.GET_PACKAGEINFO_UID);
            putOptionOnServer(adben2);
            return (MyPackageInfo) getEasyManagerServiceEntity().getObject();
        }
        return  null;
    }

    public int getCurrentUserID(){
        try{
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.GET_CURRENT_USER_ID);
            putOptionOnServer(adben2);
            return (Integer) getEasyManagerServiceEntity().getObject();
        }catch (Throwable e){
            return Process.myUid();
        }
    }

    public int getComponentEnabledSetting(Context context , String pkgname,String componentName,int uid){
        if(!isDead && skipError){
            if(componentName == null){
                componentName = "";
            }
            TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, componentName, context.getPackageName(), 0, uid);
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.GET_COMPONENT_ENABLED_SETTING);
            putOptionOnServer(adben2);
            return (Integer) getEasyManagerServiceEntity().getObject();
        }
        return -1;
    }

    public int getComponentOrPackageEnabledState(Context context , String pkgname,String componentName,int uid){
        if(!isDead && skipError){
            if(componentName != null){
                pkgname = pkgname +"/"+componentName;
            }
            TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, null, context.getPackageName(), 0, uid);
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.GET_COMPONENT_OR_PACKAGE_ENABLE_STATE);
            putOptionOnServer(adben2);
            return (Integer) getEasyManagerServiceEntity().getObject();
        }
        return -1;

    }

    public int getPackageSuspend(Context context , String pkgname,int uid){
        if(!isDead && skipError){
            TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, null, context.getPackageName(), 0, uid);
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.GET_PACKAGE_SUSPEND);
            putOptionOnServer(adben2);
            return (Integer) getEasyManagerServiceEntity().getObject();
        }
        return -1;
    }

    public int checkOp(Context context , String pkgname,String opstr,int uid){
        if(!isDead && skipError){
            TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, opstr, context.getPackageName(), 0, uid);
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.CHECK_OP);
            putOptionOnServer(adben2);
            return (Integer) getEasyManagerServiceEntity().getObject();
        }
        return -1;
    }

    public int checkPermission(Context context , String pkgname,String opstr,int uid){
        if(!isDead && skipError){
            TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, opstr, context.getPackageName(), 0, uid);
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.CHECK_PM_PERMISSION_CODE);
            putOptionOnServer(adben2);
            return (Integer) getEasyManagerServiceEntity().getObject();
        }
        return -1;
    }

    public List<MyAppopsInfo> getAppopsPKGPermissions(TransmissionEntity entity){
        if(!isDead && skipError){
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.GET_APPOPS_PERMISSIONS);
            putOptionOnServer(adben2);
            return (List<MyAppopsInfo>) getEasyManagerServiceEntity().getObject();
        }
        return null;
    }

    public String permissionToOp(Context context, String permission, Integer uid) {
        if(!isDead && skipError){
            TransmissionEntity transmissionEntity = new TransmissionEntity(null, permission, context.getPackageName(), 0, uid);
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.GET_APPOPS_PERMISSION_TO_OP);
            putOptionOnServer(adben2);
            return (String) getEasyManagerServiceEntity().getObject();
        }
        return null;
    }

    public int strOpToOp(Context context , String pkgname,String opstr,int uid){
        if(!isDead && skipError){
            TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, opstr, context.getPackageName(), 0, uid);
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.GET_APPOPS_PERMISSION_TO_OP_CODE);
            putOptionOnServer(adben2);
            return (Integer) getEasyManagerServiceEntity().getObject();
        }
        return -1;
    }

    public List<String> getPathALLFiles(Context context,String path,int uid){
        if(!isDead && skipError){
            TransmissionEntity transmissionEntity = new TransmissionEntity(null, path, context.getPackageName(), 0, uid);
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.GET_PATH_ALL_FILES);
            putOptionOnServer(adben2);
            return (List<String>) getEasyManagerServiceEntity().getObject();
        }
        return null;

    }

}

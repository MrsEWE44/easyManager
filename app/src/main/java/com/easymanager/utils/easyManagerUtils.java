package com.easymanager.utils;

import android.content.Context;
import android.os.IBinder;
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

    public easyManagerServiceEntity putOptionOnServer(easyManagerClientEntity adben2){
        adbClient ac = new adbClient(adben2, new adbClient.SocketListener() {
            @Override
            public easyManagerServiceEntity getAdbEntity(easyManagerServiceEntity adfb) {
                return null;
            }
        });
        return ac.getAdbEntity();
    }

    public void activeRoot(Context context) {
        String cmdstr ="killall EAMADB\n" +
                "exec app_process -Djava.class.path=\""+context.getApplicationInfo().sourceDir+"\" /system/bin --nice-name=EAMADB "+ startAdbService.class.getName()+" >>/dev/null 2>&1 &\n" +
                "echo \"run EAMADB ok [root]\"";
        CMD cmd = new CMD(cmdstr);
    }

    public void dead(){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.DEAD);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }


    public boolean isADB(){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.IS_ADB);
        return checkBool(adben2);
    }

    public boolean isROOT(){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.IS_ROOT);
        return checkBool(adben2);
    }

    public IBinder getSystemServer(String serverName,Context context){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,new TransmissionEntity(serverName,null,context.getPackageName(),0,getCurrentUserID()),easyManagerEnums.GET_SYSTEM_SERVICE);
        return (IBinder) putOptionOnServer(adben2).getObject();
    }

    public boolean checkBool(easyManagerClientEntity adben2){
        try{
            easyManagerServiceEntity serviceEntity = putOptionOnServer(adben2);
            return  (boolean) serviceEntity.getObject();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public CMD runCMD(String cmdstr){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(cmdstr,null,-1);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        return eee.getCmd();
    }

    public void killpkg(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.KILL_PROCESS);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void setAppopsMode(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_APPOPS);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void setAppopsModeCore(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_APPOPS_CORE);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void installAPK(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.INSTALL_APK);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void uninstallAPK(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.UNINSTALL_APK);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void setComponentOrPackageEnabledState(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_COMPONENT_OR_PACKAGE_ENABLE_STATE);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void setPackageHideState(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_PACKAGE_HIDE_STATE);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void revokeRuntimePermission(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_PACKAGE_REVOKE_RUNTIME_PERMISSION);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void grantRuntimePermission(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_PACKAGE_GRANT_RUNTIME_PERMISSION);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void setFirewallState(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.APP_FIREWALL);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void dumpAPK(String apksourcepath,String outpath){
        FileUtils fu = new FileUtils();
        fu.copyFile(apksourcepath,outpath);
    }

    public void backupApk(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.BACKUP_APK);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void restoryApp(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.RESTORY_APK);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void requestGrantUser(Context context){
        TransmissionEntity entity = new TransmissionEntity(context.getPackageName(), context.getFilesDir().toString(), context.getPackageName(), -1,getCurrentUserID());
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.GRANT_USER);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void changeGrantUserState(String pkg){
        TransmissionEntity entity = new TransmissionEntity(pkg, null, "com.easymanager", -1,getCurrentUserID());
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.CHANGE_USER);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public Boolean getServerStatus(){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.GET_SERVER_STATUS);
        return checkBool(adben2);
    }

    public HashMap<String,Integer> getGrantUsers(Context context){
        TransmissionEntity entity = new TransmissionEntity(context.getPackageName(), context.getFilesDir().toString(), context.getPackageName(), -1,getCurrentUserID());
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.GET_GRANT_USERS);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        return (HashMap<String,Integer>) eee.getObject();
    }


    public void createAppClone(Context context) {
        TransmissionEntity entity = new TransmissionEntity(null,null,context.getPackageName(),0,getCurrentUserID());
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.APP_CLONE);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void removeAppClone(Context context , int removeuid) {
        TransmissionEntity entity = new TransmissionEntity(null,null,context.getPackageName(),0,removeuid);
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.APP_CLONE_REMOVE);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public void startAppClone(Context context , int startuid) {
        TransmissionEntity entity = new TransmissionEntity(null,null,context.getPackageName(),0,startuid);
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.START_USER_ID);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }

    public String[] getAppCloneUsers() {
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.APP_CLONE_GETUSERS);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        try {
            return (String[]) eee.getObject();
        }catch (Exception e){
            return new String[]{"0"};
        }
    }

    public List<MyPackageInfo> getInstalledPackages(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.QUERY_PACKAGES_UID);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        return (List<MyPackageInfo>) eee.getObject();
    }

    public MyPackageInfo getMyPackageInfo(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.GET_PACKAGEINFO_UID);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        return (MyPackageInfo) eee.getObject();
    }

    public int getCurrentUserID(){
        try{
            easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.GET_CURRENT_USER_ID);
            easyManagerServiceEntity eee = putOptionOnServer(adben2);
            return (Integer) eee.getObject();
        }catch (Throwable e){
            return Process.myUid();
        }
    }

    public int getComponentEnabledSetting(Context context , String pkgname,String componentName,int uid){
        TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, componentName, context.getPackageName(), 0, uid);
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.GET_COMPONENT_ENABLED_SETTING);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        return (Integer) eee.getObject();
    }

    public int checkOp(Context context , String pkgname,String opstr,int uid){
        TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, opstr, context.getPackageName(), 0, uid);
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.CHECK_OP);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        return (Integer) eee.getObject();
    }

    public int checkPermission(Context context , String pkgname,String opstr,int uid){
        TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, opstr, context.getPackageName(), 0, uid);
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.CHECK_PM_PERMISSION_CODE);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        return (Integer) eee.getObject();
    }

    public List<MyAppopsInfo> getAppopsPKGPermissions(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.GET_APPOPS_PERMISSIONS);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        return (List<MyAppopsInfo>) eee.getObject();
    }

    public String permissionToOp(Context context, String permission, Integer uid) {
        TransmissionEntity transmissionEntity = new TransmissionEntity(null, permission, context.getPackageName(), 0, uid);
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.GET_APPOPS_PERMISSION_TO_OP);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        return (String) eee.getObject();
    }

    public int strOpToOp(Context context , String pkgname,String opstr,int uid){
        TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, opstr, context.getPackageName(), 0, uid);
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.GET_APPOPS_PERMISSION_TO_OP_CODE);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        return (Integer) eee.getObject();
    }

    public List<String> getPathALLFiles(Context context,String path,int uid){
        TransmissionEntity transmissionEntity = new TransmissionEntity(null, path, context.getPackageName(), 0, uid);
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.GET_PATH_ALL_FILES);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        return (List<String>) eee.getObject();
    }

}

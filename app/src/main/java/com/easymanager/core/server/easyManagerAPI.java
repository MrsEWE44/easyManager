package com.easymanager.core.server;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.os.Build;
import android.os.IBinder;

import com.easymanager.core.api.AppopsAPI;
import com.easymanager.core.api.FileCompressApi;
import com.easymanager.core.api.PackageAPI;
import com.easymanager.core.api.baseAPI;
import com.easymanager.core.utils.MyConfigUtils;
import com.easymanager.entitys.MyAppopsInfo;
import com.easymanager.entitys.MyPackageInfo;
import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.core.utils.CMD;
import com.easymanager.utils.FileTools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class easyManagerAPI extends baseAPI {

    private PackageAPI packageAPI = new PackageAPI();
    private AppopsAPI appopsAPI = new AppopsAPI();

    private FileCompressApi fileCompressApi = new FileCompressApi();
    private MyConfigUtils myConfigUtils = new MyConfigUtils();
    private FileTools ft = myConfigUtils.ft;

    public IBinder getSystemService( String name) {
        return easyManagerPortService.getSystemService(name);
    }

    public PackageAPI getPackageAPI(){
        return packageAPI;
    }


    public void killpkg(String pkgname,int uid){
        packageAPI.killApp(pkgname,uid);
    }

    public void setAppopsModeCore(String pkgname , String opstr , String opmode,int uid){
        killpkg(pkgname, uid);
        appopsAPI.SetMode(pkgname, opstr, opmode,uid);
    }

    public void setAppopsMode(TransmissionEntity te){
        int opsmode = te.getOpsmode();
        String pkgname = te.getPkgname();
        String opmodestr = te.getOpmodestr();
        int uid = te.getUid();
        killpkg(pkgname,uid);
        switch (opsmode){
            case 16:
                packageAPI.SetInactive(pkgname,opmodestr.equals("true"),uid);
                break;
            case 17:
                packageAPI.SetStandbyBucket(pkgname, opmodestr,uid);
                break;
            default:
                appopsAPI.setModeCore(pkgname,opmodestr,opsmode,uid);
                break;
        }
    }
    public int getPKGUID(String pkgname , int uid){return packageAPI.getPKGUID(pkgname, uid);}
    public void installAPK(String apkpath){packageAPI.InstallAPK(apkpath);}
    public void installAPK(String apkpath,int uid){packageAPI.InstallAPK(apkpath,uid);}
    public void uninstallApp(String pkgname,int uid){
        killpkg(pkgname, uid);
        packageAPI.UninstallPKG(pkgname,uid);
    }
    public void setComponentOrPackageEnabledState(String pkgname_or_compname,int state,int uid){
        killpkg(pkgname_or_compname, uid);
        packageAPI.setComponentOrPackageEnabledState(pkgname_or_compname, state,uid);
    }
    public void setPackageHideState(String pkgname,boolean hide,int uid){
        killpkg(pkgname, uid);
        packageAPI.setPackageHideState(pkgname, hide , uid);
    }

    public int checkPermission(String pkgname , String permission_str, int uid){return packageAPI.checkPermission(pkgname, permission_str, uid);}

    public void setPackagesSuspendedAsUser(String pkgname, boolean suspended,int uid) {
        killpkg(pkgname, uid);
        packageAPI.setPackagesSuspendedAsUser(pkgname, suspended, uid);
    }
    public boolean isPackageSuspendedForUser(String packageName,int uid){return packageAPI.isPackageSuspendedForUser(packageName, uid);}

    public void clearPackageData(String packageName,int uid){
        killpkg(packageName, uid);
        packageAPI.clearPackageData(packageName, uid);
    }

    public void revokeRuntimePermission(String pkgname , String permission_str, int uid){
        killpkg(pkgname, uid);
        try {
            packageAPI.revokeRuntimePermission(pkgname,permission_str,packageAPI.getTranslatedUserId());
        }catch (Throwable e){
            appopsAPI.setPermissionStr(pkgname,permission_str,false,uid);
        }
    }

    public void grantRuntimePermission(String pkgname , String permission_str , int uid){
        killpkg(pkgname, uid);
        try {
            packageAPI.grantRuntimePermission(pkgname,permission_str,packageAPI.getTranslatedUserId());
        }catch (Throwable e){
            appopsAPI.setPermissionStr(pkgname,permission_str,true,uid);
        }
    }

    public void addRunningApps(String pkgname , String opstr , int uid){
        killpkg(pkgname,uid);
        String[] split = opstr.split("-");
        if(split != null){
            String timeStr = split[0];
            String timeTypeStr = split[1];
            int time = Integer.parseInt(timeStr.trim());
            int timeType = Integer.parseInt(timeTypeStr.trim());
            if(timeType == 1){
                time = time * 60;
            }

            if(timeType == 2){
                time = time * 60 * 60;
            }

            if(timeType == 3){
                time = time * 60 * 60 * 24;
            }

            if(myConfigUtils.getCleanAPPConfigTime(pkgname) != -1){
                myConfigUtils.changeCleanAPPConfigTime(pkgname,time);
            }else{
                myConfigUtils.addCleanAPPConfigTime(pkgname,time);
            }
        }

    }

    public void startStopRunningAPP(int uid){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    HashMap<String, Integer> cleanAPPs = myConfigUtils.getCleanAPPs();
                    int time = 0;
                    if(cleanAPPs.size() > 0){
                        for (Map.Entry<String, Integer> entry : cleanAPPs.entrySet()) {
                            time = entry.getValue();
                            break;
                        }
                        try {
                            Thread.sleep(time > 0 ?time * 1000:100);
                        } catch (InterruptedException e) {
                            System.err.println("startStopRunningAPP error :::  " + e.toString());
                        }

                        for (Map.Entry<String, Integer> entry : cleanAPPs.entrySet()) {
                            String pkgname = entry.getKey();
                            List<ActivityManager.RunningAppProcessInfo> runningApps = packageAPI.getRunningApps(0);
                            for (ActivityManager.RunningAppProcessInfo runningApp : runningApps) {
                                if(runningApp.processName.equals(pkgname) && runningApp.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                                    killpkg(pkgname,uid);
                                }
                            }

                        }
                    }


                }
            }
        }).start();
    }

    public void deleteCleanAPPConfig(){
        myConfigUtils.deleteCleanAPPConfig();
    }

    public void CompressOrDecompressFile(String dirPath , String outPath , int mode){
        fileCompressApi.CompressOrDecompressFile(dirPath, outPath, mode);
    }

    //应用备份
    public void backupApk(String pkgname , int uid, String parm){
        killpkg(pkgname, uid);
        String[] s = parm.split("---");
        if(s != null){
            String mode = s[0].trim();
            String fileEnd = s[1].trim();
            String apkPath = s[2].trim();
            String sdpath = s[3].trim();
            String outdir=sdpath+"/easyManager/backup";
            String outbackuppkgdir=outdir+"/"+pkgname;
            File file1 = new File(outbackuppkgdir);
            if(!file1.exists()){
                file1.mkdirs();
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                apkPath = new File(apkPath).getParent();
            }
            if(mode.equals("full")){
                compressFileByBackup(fileEnd,apkPath,outbackuppkgdir,"file",uid);
                backupData(uid,pkgname,fileEnd,outbackuppkgdir,sdpath);
            }else if(mode.equals("data")){
                backupData(uid,pkgname,fileEnd,outbackuppkgdir,sdpath);
            }else if(mode.equals("apk")){
                compressFileByBackup(fileEnd,apkPath,outbackuppkgdir,"file",uid);
            }
            compressFileByBackup(fileEnd,outbackuppkgdir,outdir,pkgname,uid);
            ft.deleteFile(outbackuppkgdir);
        }
    }

    //恢复已经备份的应用
    public void restoryApp(String pkgname , int uid, String parm) {
        String[] s = parm.split("---");
        pkgname=pkgname.replaceAll("-"+uid,"");
        if(s != null){
            String mode = s[0].trim();
            String fileEnd = s[1].trim();
            String sdpath = s[2].trim();
            String backup_dir_path = sdpath+"/easyManager/backup";
            String pkg_out_dir_path = backup_dir_path+"/"+pkgname;
            decompressFileOnBackup(fileEnd,backup_dir_path,backup_dir_path,pkgname,uid);
            if(mode.equals("full")){
                decompressFileOnBackup(fileEnd,pkg_out_dir_path,pkg_out_dir_path,"file",uid);
                String baseAPk = getBaseAPk(pkg_out_dir_path);
                if(baseAPk != null){
                    if(uid > 1){
                        installAPK(baseAPk,uid);
                    }else{
                        installAPK(baseAPk);
                    }
                    restoryData(uid,pkgname,fileEnd,sdpath,pkg_out_dir_path);
                }else{
                    System.out.println("baseAPK is null!!!");
                }
            }else if(mode.equals("data")){
                restoryData(uid,pkgname,fileEnd,sdpath,pkg_out_dir_path);
            }else if(mode.equals("apk")){
                decompressFileOnBackup(fileEnd,pkg_out_dir_path,pkg_out_dir_path,"file",uid);
                String baseAPk =getBaseAPk(pkg_out_dir_path);
                if(baseAPk != null){
                    if(uid > 0){
                        installAPK(baseAPk,uid);
                    }else{
                        installAPK(baseAPk);
                    }
                }else{
                    System.out.println("baseAPK is null!!!");
                }
            }
            ft.deleteFile(pkg_out_dir_path);
        }
    }

    public ArrayList<String> getPathALLFiles(String path){
        ArrayList<File> s = new ArrayList<>();
        ArrayList<String> s2 = new ArrayList<>();
        ft.getAllFileByEndName(path,null,s);
        for (File file : s) {
            s2.add(file.toString());
        }
        return s2;
    }

    public String getBaseAPk(String dirPath){
        File file = new File(dirPath);
        return ft.findFiles(file,".apk");
    }

    public void restoryData(int uid , String pkgname , String fileEnd , String sdpath , String pkg_out_dir_path ){
        String data_path1="/data/data";
        String data_path2="/proc/1/cwd/data/data";
        String data_user_path = "/data/user/"+uid;
        String sdandroidpath=sdpath+"/Android";
        String sddatapath = sdandroidpath+"/data";
        String sdobbpath = sdandroidpath+"/obb";
        String pkg_data_path1 = data_path1+"/"+pkgname;
        String pkg_data_path2 = data_path2+"/"+pkgname;
        String pkg_data_user_path = data_user_path+"/"+pkgname;
        int pkguid = packageAPI.getPKGUID(pkgname,uid);
        if(uid > 0){
            decompressFileOnBackup(fileEnd,pkg_out_dir_path,data_user_path,"data",uid);
            setMode(data_user_path+"/"+pkgname,pkguid);
        }else {
            File file = new File(pkg_data_path1);
            File file2 = new File(pkg_data_path2);
            File file3 = new File(pkg_data_user_path);
            if(file.exists()){
                decompressFileOnBackup(fileEnd,pkg_out_dir_path,data_path1,"data",uid);
                setMode(pkg_data_path1,pkguid);
            } else if(file2.exists()){
                decompressFileOnBackup(fileEnd,pkg_out_dir_path,data_path2,"data",uid);
                setMode(pkg_data_path2,pkguid);
            } else if(file3.exists()){
                decompressFileOnBackup(fileEnd,pkg_out_dir_path,data_user_path,"data",uid);
                setMode(pkg_data_user_path,pkguid);
            }else{
                System.err.println("restoryData Error : " + uid + " -- " + pkguid + " -- " + pkgname + " -- " + fileEnd + " -- " + new File(data_path1).exists());
            }
        }
        File file = new File(sdandroidpath);
        if(file.exists()){
            decompressFileOnBackup(fileEnd,pkg_out_dir_path,sddatapath,"sddata",uid);
            setMode(sddatapath+"/"+pkgname,pkguid);
            decompressFileOnBackup(fileEnd,pkg_out_dir_path,sdobbpath,"sdobb",uid);
            setMode(sdobbpath+"/"+pkgname,pkguid);
        }
    }

    public void backupData(int uid ,String pkgname , String fileEnd, String outbackuppkgdir  , String sdpath){
        String data_path1="/data/data/"+pkgname;
        String data_path2="/proc/1/cwd/data/data/"+pkgname;
        String data_user_path = "/data/user/"+uid+"/"+pkgname;
        String sdandroidpath=sdpath+"/Android";
        String sddatapath = sdandroidpath+"/data/"+pkgname;
        String sdobbpath = sdandroidpath+"/obb/"+pkgname;
        if(uid > 1){
            compressFileByBackup(fileEnd,data_user_path,outbackuppkgdir,"data",uid);
        }else{
            File file = new File(data_path1);
            if(file.exists()){
                compressFileByBackup(fileEnd,data_path1,outbackuppkgdir,"data",uid);
            }else{
                compressFileByBackup(fileEnd,data_path2,outbackuppkgdir,"data",uid);
            }
        }
        File file = new File(sdandroidpath);
        if(file.exists()){
            File sddatapathFile = new File(sddatapath);
            if(sddatapathFile.exists()){
                compressFileByBackup(fileEnd,sddatapath,outbackuppkgdir,"sddata",uid);
            }
            File sdobbpathFile = new File(sdobbpath);
            if(sdobbpathFile.exists()){
                compressFileByBackup(fileEnd,sdobbpath,outbackuppkgdir,"sdobb",uid);
            }
        }
    }

    public void setMode(String dirPath , int pkguid){
        CMD cmd = new CMD("chown -R " + pkguid + ":" + pkguid + " " + dirPath, false);
    }

    public void compressFileByBackup(String fileEnd,String dirPath , String outPath ,String name ,Integer uid){
        String head_path = outPath+"/"+name+"-"+uid;
        if(fileEnd.equals("txz")){
            CompressOrDecompressFile(dirPath,head_path+".tar.xz",fileCompressApi.TAR_XZ_COMPRESS_TYPE);
        }else if(fileEnd.equals("tbz")){
            CompressOrDecompressFile(dirPath,head_path+".tar.bz",fileCompressApi.TAR_BZIP_COMPRESS_TYPE);
        }else if(fileEnd.equals("tgz")){
            CompressOrDecompressFile(dirPath,head_path+".tar.gz",fileCompressApi.TAR_GZIP_COMPRESS_TYPE);
        }
    }

    public void decompressFileOnBackup(String fileEnd,String dirPath , String outPath ,String name , Integer uid){
        String head_path = dirPath+"/"+name;
        if(fileEnd.equals("txz")){
            String path = head_path+"-"+uid + ".tar.xz";
            File file = new File(path);
            if(!file.exists()){
                path = head_path+".tar.xz";
            }
            CompressOrDecompressFile(path,outPath,fileCompressApi.TAR_XZ_DECOMPRESS_TYPE);
        }else if(fileEnd.equals("tbz")){
            String path = head_path+"-"+uid + ".tar.bz";
            File file = new File(path);
            if(!file.exists()){
                path = head_path+".tar.bz";
            }
            CompressOrDecompressFile(path,outPath,fileCompressApi.TAR_BZIP_DECOMPRESS_TYPE);
        }else if(fileEnd.equals("tgz")){
            String path = head_path+"-"+uid + ".tar.gz";
            File file = new File(path);
            if(!file.exists()){
                path = head_path+".tar.gz";
            }
            CompressOrDecompressFile(path,outPath,fileCompressApi.TAR_GZIP_DECOMPRESS_TYPE);
        }
    }



    public void deleteConfig(String requpkg,int state,String path,String filename){
        myConfigUtils.deleteConfig(requpkg, state, path, filename);
    }

    public void changeConfig(String requpkg,int newstate,String path,String filename){
        myConfigUtils.changeConfig(requpkg, newstate, path, filename);
    }

    public void writeConfig(String requpkg,int state,String path,String filename){
        myConfigUtils.writeConfig(requpkg, state, path, filename);
    }


    public HashMap<String,Integer> getGrantUsers(){
        return myConfigUtils.getGrantUsers();
    }

    public Integer getGrantUserState(String requestpkg) {
        return myConfigUtils.getGrantUserState(requestpkg);
    }

    public int changeGrantUserState(String requestpkg){
        return myConfigUtils.changeGrantUserState(requestpkg);
    }

    public int requestGrantUserState(String requestpkg){
        return myConfigUtils.requestGrantUserState(requestpkg);
    }

    public void createAppClone(){
        packageAPI.createUser();
    }

    public void startUser(int uid){
        packageAPI.startUser(uid);
    }

    public int getCurrentUserID(){return packageAPI.getCurrentUser();}

    public void removeAppClone(int userid){
        packageAPI.removeUser(userid);
    }

    public String[] getUsers(){
        return packageAPI.getUsers();
    }

    public List<MyPackageInfo> getInstalledPackages(int uid){return packageAPI.getInstalledPackages(uid);}

    public MyPackageInfo getMyPackageInfo(String pkgname , int uid){return packageAPI.getMyPackageInfo(pkgname, uid);}

    public int getComponentEnabledSetting(ComponentName componentName, int userId){return packageAPI.getComponentEnabledSetting(componentName,userId);}

    public int checkOp(String opstr, String packageName, int uid){return appopsAPI.checkOp(opstr, packageName, uid);}

    public List<MyAppopsInfo> getAppopsPKGPermissions(String pkgname, int userid){return appopsAPI.getAppopsPKGPermissions(pkgname,userid);}

    public String permissionToOp(String permission_str){return appopsAPI.permissionToOp(permission_str);}

    public int strOpToOp(String op){return appopsAPI.strOpToOp(op);}



}

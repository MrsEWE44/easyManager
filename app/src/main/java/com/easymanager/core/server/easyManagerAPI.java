package com.easymanager.core.server;

import android.content.ComponentName;
import android.os.Build;
import android.os.IBinder;
import android.util.Xml;

import com.easymanager.core.api.AppopsAPI;
import com.easymanager.core.api.FileCompressApi;
import com.easymanager.core.api.PackageAPI;
import com.easymanager.core.api.baseAPI;
import com.easymanager.entitys.MyPackageInfo;
import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.core.utils.CMD;
import com.easymanager.utils.FileTools;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class easyManagerAPI extends baseAPI {

    private PackageAPI packageAPI = new PackageAPI();
    private AppopsAPI appopsAPI = new AppopsAPI();

    private FileCompressApi fileCompressApi = new FileCompressApi();

    private FileTools ft = new FileTools();

    public IBinder getSystemService( String name) {
        return easyManagerPortService.getSystemService(name);
    }

    public PackageAPI getPackageAPI(){
        return new PackageAPI();
    }


    public void killpkg(String pkgname,int uid){
        packageAPI.killApp(pkgname,uid);
    }

    public void setAppopsModeCore(String pkgname , String opstr , String opmode,int uid){appopsAPI.SetMode(pkgname, opstr, opmode,uid);}

    public void setAppopsMode(TransmissionEntity te){
        int opsmode = te.getOpsmode();
        String pkgname = te.getPkgname();
        String opmodestr = te.getOpmodestr();
        int uid = te.getUid();
        killpkg(pkgname,uid);
        switch (opsmode){
            case 16:
                packageAPI.SetInactive(pkgname,opmodestr.equals("true")?true:false,uid);
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
    public void uninstallApp(String pkgname,int uid){packageAPI.UninstallPKG(pkgname,uid);}
    public void setComponentOrPackageEnabledState(String pkgname_or_compname,int state,int uid){packageAPI.setComponentOrPackageEnabledState(pkgname_or_compname, state,uid);}
    public void setPackageHideState(String pkgname,boolean hide,int uid){packageAPI.setPackageHideState(pkgname, hide , uid);}

    public void revokeRuntimePermission(String pkgname , String permission_str, int uid){
        try {
            packageAPI.revokeRuntimePermission(pkgname,permission_str,uid);
        }catch (Exception e){
            appopsAPI.setPermissionStr(pkgname,permission_str,false,uid);
        }
    }

    public void grantRuntimePermission(String pkgname , String permission_str , int uid){
        try {
            packageAPI.grantRuntimePermission(pkgname,permission_str,uid);
        }catch (Exception e){
            appopsAPI.setPermissionStr(pkgname,permission_str,true,uid);
        }
    }

    public void CompressOrDecompressFile(String dirPath , String outPath , int mode){
        fileCompressApi.CompressOrDecompressFile(dirPath, outPath, mode);
    }

    //应用备份
    public void backupApk(String pkgname , int uid, String parm){
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
        try {
            // 创建 XmlPullParser 对象
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            File file1 = getConfigFile(path,filename);
            FileInputStream fis = new FileInputStream(file1);
            // 设置输入流
            parser.setInput(fis,"UTF-8");
            // 开始解析文档
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    // 判断是否是要删除的元素
                    if (parser.getName().equals("config") && parser.getAttributeValue(null, "pkg").equals(requpkg) && parser.getAttributeValue(null,"state").equals(state)) {
                        // 删除该元素
                        parser.next();
                        continue;
                    }
                }
                eventType = parser.next();
            }
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public File getConfigFile(String path,String filename){
        String config_path = path+"/"+filename;
        File file1 = new File(path);
        if(!file1.exists()){
            file1.mkdirs();
        }
        return new File(config_path);
    }

    public void changeConfig(String requpkg,int newstate,String path,String filename){
        try {
            // 创建 XmlPullParser 和 XmlSerializer 对象，以及输入输出流
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            File file1 = getConfigFile(path,filename);
            FileInputStream fis = new FileInputStream(file1);
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            serializer.setOutput(writer);
            parser.setInput(fis,"UTF-8");
            // 开始解析文档
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    // 判断是否是要修改的元素
                    if (parser.getName().equals("config")) {
                        serializer.startTag("", "config");
                        if(parser.getAttributeValue(null, "pkg").equals(requpkg)){
                            // 修改元素内容
                            serializer.attribute("", "pkg", requpkg);
                            serializer.attribute("", "state", newstate+"");
                        }else{
                            serializer.attribute("", "pkg", parser.getAttributeValue(null,"pkg"));
                            serializer.attribute("", "state", parser.getAttributeValue(null, "state"));
                        }
                        serializer.endTag("", "config");
                        // 跳过原始元素
                        parser.next();
                    }
                }
                eventType = parser.next();
            }
            fis.close();
            serializer.endDocument();
            ft.writeDataToPath(writer.toString(),getCachePathOnXML()+"/"+getGrantUserConfigName(),false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeConfig(String requpkg,int state,String path,String filename){
        File file = getConfigFile(path,filename);
        try {
            XmlSerializer serializer = Xml.newSerializer();
            FileOutputStream fos = new FileOutputStream(file,true);
            serializer.setOutput(fos, "utf-8");
            serializer.startTag(null, "config");
            serializer.attribute("", "pkg", requpkg);
            serializer.attribute("", "state", state+"");
            serializer.endTag(null, "config");
            serializer.endDocument();
            fos.close();
        }catch (IOException e){
            System.out.println("write error  ::: " + e.toString());
        }
    }

    public HashMap<String,Integer> getConfigs(String cachePath,String filename){
        File file1 = getConfigFile(cachePath,filename);
        HashMap<String,Integer> hashMap = new HashMap<String,Integer>();
        if(file1.exists()){
            try {
                FileInputStream fis = new FileInputStream(file1);
                // 创建 XmlPullParser 对象，并设置输入流
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(fis, "UTF-8");
                int eventType=parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.START_TAG){
                        if(parser.getName().equals("config")){
                            String key = parser.getAttributeValue(null,"state");
                            String value = parser.getAttributeValue(null,"pkg");
                            hashMap.put(value,Integer.valueOf(key.trim()));
                        }
                    }
                    eventType = parser.next();
                }
                fis.close();
            } catch (Exception e) {
                System.out.println("read error ::: " + e.toString());
                e.printStackTrace();
            }
        }
        return hashMap;
    }

    public String getConfig(String requestpkg,String cachePath,String filename){
        HashMap<String, Integer> configs = getConfigs(cachePath, filename);
        for (Map.Entry<String, Integer> entry : configs.entrySet()) {
            if(entry.getKey().equals(requestpkg)){
                return entry.getValue().toString();
            }
        }
        return null;
    }

    public String getCachePathOnXML(){
        return  "/data/local/tmp/easyManager";
    }

    public String getGrantUserConfigName(){
        return "grantuser.xml";
    }

    public HashMap<String,Integer> getGrantUsers(){
        return getConfigs(getCachePathOnXML(),getGrantUserConfigName());
    }

    public Integer getGrantUserState(String requestpkg) {
        String value = getConfig(requestpkg,getCachePathOnXML(),getGrantUserConfigName());
        try {
            Integer integer = Integer.valueOf(value.trim());
            return integer;
        }catch (Exception e){

        }
        return -1;
    }

    public int changeGrantUserState(String requestpkg){
        String config = getConfig(requestpkg, getCachePathOnXML(), getGrantUserConfigName());
        if(config != null){
            changeConfig(requestpkg,config.equals("0")?1:0,getCachePathOnXML(),getGrantUserConfigName());
            return 0;
        }
        return -1;
    }

    public int requestGrantUserState(String requestpkg){
        if(getConfig(requestpkg,getCachePathOnXML(),getGrantUserConfigName()) == null){
            writeConfig(requestpkg,requestpkg.equals("com.easymanager")?0:1,getCachePathOnXML(),getGrantUserConfigName());
        }
        return getConfig(requestpkg,getCachePathOnXML(),getGrantUserConfigName()) != null ? 1:-1;
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


}

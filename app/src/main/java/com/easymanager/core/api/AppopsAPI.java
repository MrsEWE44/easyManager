package com.easymanager.core.api;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build;

import com.android.internal.app.IAppOpsService;
import com.easymanager.core.enums.AppopsPermissionStr;
import com.easymanager.core.server.Singleton;
import com.easymanager.core.server.easyManagerBinderWrapper;
import com.easymanager.core.server.easyManagerPortService;
import com.easymanager.entitys.MyAppopsInfo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppopsAPI extends baseAPI{

    private static final Map<String, IAppOpsService> I_APP_OPS_SERVICE_CACHE = new HashMap<>();
    private PackageAPI packageAPI = new PackageAPI();
    private AppopsPermissionStr aps = new AppopsPermissionStr();
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
        int appmode = aps.getAppopsMode(mode2);
        for (String op : aps.getOPS2(appmode)) {
            String op2 = op.substring(op.lastIndexOf(".")+1);
            try{
                if(getModeInt(modestr) == 0){
                    packageAPI.grantRuntimePermission(pkgname,op,uid);
                }else{
                    packageAPI.revokeRuntimePermission(pkgname,op,uid);
                }
                SetMode(pkgname,op2,modestr,uid);
            }catch (Throwable e){
//                StringWriter sw = new StringWriter();
//                e.printStackTrace(new PrintWriter(sw));
//                System.out.println("setModeCore :::: " + sw.toString());
                SetMode(pkgname,op2,modestr,uid);
            }
        }
        for (String op : aps.getOPS(appmode)) {
            SetMode(pkgname,op,modestr,uid);
        }
    }


    public int checkOp(String opstr, String packageName, int userid){
        try {
            int uid = packageAPI.getPKGUID(packageName,userid);
            return getIAppOpsService().checkOperation(strOpToOp(opstr),uid,packageName);
        }catch (Throwable e){
            return AppOpsManager.MODE_DEFAULT;
        }
    }

    //通过调用appops系统api来修改应用权限
    public void SetMode(String pkgname , String opstr , String opmode,int userid){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            IAppOpsService iAppOpsService = getIAppOpsService();
            try {
                int opcode = strOpToOp(opstr);
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

    public void setPermissionStr(String pkgname, String permission_str, boolean b,int userid) {
        String opStr = permissionToOp(permission_str);
        if(opStr != null){
            SetMode(pkgname,opStr, b? "allow":"ignore",userid);
        }else{
            SetMode(pkgname,permission_str.substring(permission_str.lastIndexOf(".")+1), b? "allow":"ignore",userid);
        }
    }

    public List<MyAppopsInfo> getAppopsPKGPermissions(String pkgname, int userid){
        List<MyAppopsInfo> myAppopsInfoList = new ArrayList<>();
        try {
            List<AppOpsManager.PackageOps> uidOps2 = getIAppOpsService().getOpsForPackage(packageAPI.getPKGUID(pkgname, userid),pkgname, null);
            if(uidOps2 != null){
                for (AppOpsManager.PackageOps uidOp : uidOps2) {
                    List<AppOpsManager.OpEntry> entries = uidOp.getOps();
                    for (AppOpsManager.OpEntry entry : entries) {
                        MyAppopsInfo info = new MyAppopsInfo(pkgname, AppOpsManager.opToName(entry.getOp()), AppOpsManager.opToPermission(entry.getOp()), modeToName(entry.getMode()), entry.getOp(), entry.getMode());
                        myAppopsInfoList.add(info);
                    }
                }
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        return myAppopsInfoList;
    }

    public int strOpToOp(String op) {
        return AppOpsManager.strOpToOp(op);
    }

    public String permissionToOp(String permission_str){
        return AppOpsManager.permissionToOp(permission_str);
    }

    public String modeToName(int mode){
        if(mode == AppOpsManager.MODE_ALLOWED){
            return "allow";
        }

        if(mode == AppOpsManager.MODE_IGNORED){
            return "ignore";
        }

        if(mode == AppOpsManager.MODE_ERRORED){
            return "deny";
        }

        if(mode == AppOpsManager.MODE_DEFAULT){
            return "default";
        }

        if(mode == AppOpsManager.MODE_FOREGROUND){
            return "foreground";
        }

        return "mode=" + mode;

    }


}

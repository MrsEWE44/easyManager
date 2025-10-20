package com.easymanager.utils.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.easymanager.R;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.utils.base.DialogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class QueryDialog extends DialogUtils {

    public void queryAllPKGSProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, int uid) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_all_apps),lv1,pkginfos,checkboxs,0,uid);
    }

    public void queryAllPKGSByAppCloneProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        queryAppClonePKGProcessDialog(context,activity,"正在获取分身用户所有的应用列表(包括禁用,请稍后...)",lv1,pkginfos,checkboxs,0);
    }

    public void queryAllEnablePKGSProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, int uid) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_all_apps2),lv1,pkginfos,checkboxs,1,uid);
    }

    public void queryUserEnablePKGSProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, int uid) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_user_apps2),lv1,pkginfos,checkboxs,2,uid);
    }

    public void queryUserAllPKGSProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, int uid) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_user_apps),lv1,pkginfos,checkboxs,3,uid);
    }

    public void queryUserAllPKGSByAppCloneProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        queryAppClonePKGProcessDialog(context,activity,"正在获取分身用户的应用列表(包括禁用,请稍后...)",lv1,pkginfos,checkboxs,3);
    }
    public void queryLocalBackupProcessDialog(Context context, Activity activity, ListView apllv1, ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, int uid) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context, R.string.scan_local_backup_file_msg),apllv1,pkginfos,checkboxs,7,uid);
    }

    public void queryAllDisablePKGSProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, int uid) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_all_disable_apps),lv1,pkginfos,checkboxs,4,uid);
    }

    public void queryPKGProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, Integer mode, int uid) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_local_apps),lv1,pkginfos,checkboxs,mode,uid);
    }

    public void queryAllProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, int uid) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_all_process_app),lv1,pkginfos,checkboxs,5,uid);
    }

    public void queryAllUserProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, int uid) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_user_process_app),lv1,pkginfos,checkboxs,6,uid);
    }

    public void queryAppClonePKGProcessDialog(Context context, Activity activity, String msg, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, Integer mode){
        ProgressDialog show = showMyDialog(context,msg);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0){
                    show.dismiss();
                    showPKGS(context,lv1,pkginfos,checkboxs);
                }
            }
        };
        int currentUserID = easyMUtils.getCurrentUserID();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] users = easyMUtils.getAppCloneUsers();
                ArrayList<PKGINFO> list = new ArrayList<>();
                for (String user : users) {
                    if(!user.equals(String.valueOf(currentUserID))){
                        queryPKGProcessDialogCore(mode,Integer.valueOf(user),currentUserID,context,activity,pkginfos,checkboxs);
                        list.addAll(pkginfos);
                    }
                }
                HashSet<String> set = new HashSet<>();
                for (PKGINFO pkginfo : list) {
                    set.add(pkginfo.getPkgname());
                }
                packageUtils.clearList(pkginfos,checkboxs);
                list.clear();
                for (String s : set) {
                    pkginfos.add(packageUtils.getPKGINFO(context,s));
                    checkboxs.add(false);
                }
                sendHandlerMSG(handler,0);
            }
        }).start();

    }

    public void queryPKGProcessDialogCore(Integer mode , Integer uid , Integer currentUserID,Context context , Activity activity ,ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs ){
        if(mode != null){
            switch (mode){
                case 0:
                    if(uid==currentUserID || Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                        packageUtils.queryEnablePKGS(activity,pkginfos,checkboxs,0);
                    }else {
                        packageUtils.queryEnablePKGSByUID(uid,activity,pkginfos,checkboxs);
                    }
                    break;
                case 1:
                    if(uid == currentUserID || Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                        packageUtils.queryPKGS(activity,pkginfos,checkboxs,0);
                    }else{
                        packageUtils.queryPKGSByUID(uid,activity,pkginfos,checkboxs);
                    }
                    break;
                case 2:
                    if(uid == currentUserID || Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                        packageUtils.queryUserEnablePKGS(activity,pkginfos,checkboxs,0);
                    }else {
                        packageUtils.queryUserEnablePKGSByUID(uid,activity,pkginfos,checkboxs);
                    }
                    break;
                case 3:
                    if(uid == currentUserID || Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                        packageUtils.queryUserPKGS(activity,pkginfos,checkboxs,0);
                    }else{
                        packageUtils.queryUserPKGSByUID(uid,activity,pkginfos,checkboxs);
                    }
                    break;
                case 4:
                    if(uid == currentUserID || Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                        packageUtils.queryDisablePKGS(activity,pkginfos,checkboxs,0);
                    }else {
                        packageUtils.queryDisablePKGSByUID(uid,activity,pkginfos,checkboxs);
                    }
                    break;
                case 5:
                    if(uid == currentUserID || Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                        processUtils.queryAllRunningPKGS(activity,pkginfos,checkboxs,0);
                    }else {
                        processUtils.queryAllRunningPKGSByUID(activity,pkginfos,checkboxs,uid);
                    }
                    break;
                case 6:
                    if(uid == currentUserID || Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                        processUtils.queryRunningPKGS(activity,pkginfos,checkboxs,0);
                    }else {
                        processUtils.queryRunningPKGSByUID(activity,pkginfos,checkboxs,uid);
                    }
                    break;
                case 7:
                    String s = ft.getSDPath(currentUserID);
                    String localBackupDir= s+"/easyManager/backup";
                    File file = new File(localBackupDir);
                    packageUtils.clearList(pkginfos,checkboxs);
                    File[] listFiles = file.listFiles();
                    if(listFiles != null && listFiles.length > 0){
                        for (File f : listFiles) {
                            String name = f.getName();
                            name=name.replaceAll(".tar.gz","").replaceAll(".tar.xz","").replaceAll(".tar.bz","");
                            PKGINFO pkginfo = new PKGINFO(name, f.getName(), f.getPath(), "-1", "-1", f.length());
                            pkginfo.setIconmode(1);
                            pkginfos.add(pkginfo);
                            checkboxs.add(false);
                        }
                        packageUtils.sortPKGINFOS(pkginfos);
                    }
                    break;
            }
        }

        packageUtils.sortPKGINFOS(pkginfos);
    }

    public void queryPKGProcessDialog(Context context, Activity activity, String msg, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, Integer mode, Integer uid){
        ProgressDialog show = showMyDialog(context,msg);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0){
                    show.dismiss();
                    showPKGS(context,lv1,pkginfos,checkboxs);
                }
            }
        };
        int currentUserID = easyMUtils.getCurrentUserID();
        new Thread(new Runnable() {
            @Override
            public void run() {
                queryPKGProcessDialogCore(mode,uid,currentUserID,context,activity,pkginfos,checkboxs);
                sendHandlerMSG(handler,0);
            }
        }).start();

    }

}

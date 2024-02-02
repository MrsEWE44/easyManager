package com.easymanager.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.adapters.AppInfoAdapter;
import com.easymanager.adapters.GeneralAdapter;
import com.easymanager.adapters.PKGINFOAdapter;
import com.easymanager.core.api.PackageAPI;
import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.core.utils.CMD;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.enums.AppInfoEnums;
import com.easymanager.enums.AppManagerEnum;
import com.easymanager.enums.easyManagerEnums;
import com.easymanager.utils.base.DialogBaseUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DialogUtils extends DialogBaseUtils {

    private PackageUtils packageUtils = new PackageUtils();
    private ProcessUtils processUtils = new ProcessUtils();

    private FileTools ft = new FileTools();

    public DialogUtils(){}

    public void queryAllPKGSProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_all_apps),lv1,pkginfos,checkboxs,0);
    }

    public void queryAllEnablePKGSProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_all_apps2),lv1,pkginfos,checkboxs,1);
    }

    public void queryUserEnablePKGSProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_user_apps2),lv1,pkginfos,checkboxs,2);
    }

    public void queryUserAllPKGSProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_user_apps),lv1,pkginfos,checkboxs,3);
    }

    public void queryAllDisablePKGSProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_all_disable_apps),lv1,pkginfos,checkboxs,4);
    }

    public void queryPKGProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, Integer mode) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_local_apps),lv1,pkginfos,checkboxs,mode);
    }

    public void queryAllProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_all_process_app),lv1,pkginfos,checkboxs,5);
    }

    public void queryAllUserProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_user_process_app),lv1,pkginfos,checkboxs,6);
    }

    public void findLocalImgDialog(Context context, Activity activity, ListView lv1 , ArrayList<String> strings, ArrayList<Boolean> checkboxs){
        ProgressDialog show = showMyDialog(context,tu.getLanguageString(context,R.string.scanner_local_img));
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0){
                    show.dismiss();
                    showUsers(context,lv1,strings,checkboxs);
                }
            }
        };
        strings.clear();
        checkboxs.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<File> files = new ArrayList<>();
                ft.getAllFileByEndName(Environment.getExternalStorageDirectory().getPath(),".img",files);
                ft.getAllFileByEndName(Environment.getExternalStorageDirectory().getPath(),".iso",files);
                for (File file : files) {
                    strings.add(file.toString());
                    checkboxs.add(false);
                }
                sendHandlerMSG(handler,0);
            }
        }).start();
    }

    public void runCMDDialog(Context context, String cmdstr){
        ProgressDialog show = showMyDialog(context,tu.getLanguageString(context,R.string.execute_cmd));
        Handler handler = dismissDialogHandler(0,show);
        new Thread(new Runnable() {
            @Override
            public void run() {
                CMD cmd = new easyManagerUtils().runCMD(cmdstr);
                sendHandlerMSG(handler,0);
            }
        }).start();
    }

    public void queryPKGProcessDialog(Context context, Activity activity, String msg, ListView lv1 , ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, Integer mode){
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
        new Thread(new Runnable() {
            @Override
            public void run() {

                if(mode != null){
                    switch (mode){
                        case 0:
                            packageUtils.queryEnablePKGS(activity,pkginfos,checkboxs,0);
                            break;
                        case 1:
                            packageUtils.queryPKGS(activity,pkginfos,checkboxs,0);
                            break;
                        case 2:
                            packageUtils.queryUserEnablePKGS(activity,pkginfos,checkboxs,0);
                            break;
                        case 3:
                            packageUtils.queryUserPKGS(activity,pkginfos,checkboxs,0);
                            break;
                        case 4:
                            packageUtils.queryDisablePKGS(activity,pkginfos,checkboxs,0);
                            break;
                        case 5:
                            processUtils.queryAllRunningPKGS(activity,pkginfos,checkboxs,0);
                            break;
                        case 6:
                            processUtils.queryRunningPKGS(activity,pkginfos,checkboxs,0);
                            break;
                        case 7:
                            String s = ft.getSDPath();
                            String localBackupDir= s+"/easyManager/backup";
                            File file = new File(localBackupDir);
                            packageUtils.clearList(pkginfos,checkboxs);
                            File[] listFiles = file.listFiles();
                            if(listFiles != null && listFiles.length > 0){
                                for (File f : listFiles) {
                                    String name = f.getName();
                                    name=name.replaceAll(".tar.gz","").replaceAll(".tar.xz","").replaceAll(".tar.bz","");
                                    PKGINFO pkginfo = new PKGINFO(name, f.getName(), f.getPath(), "-1", "-1", context.getResources().getDrawable(R.drawable.manager_grant_app_foreground), f.length());
                                    pkginfos.add(pkginfo);
                                    checkboxs.add(false);
                                }
                                packageUtils.sortPKGINFOS(pkginfos);
                            }
                            break;
                    }
                }

                packageUtils.sortPKGINFOS(pkginfos);
                sendHandlerMSG(handler,0);
            }
        }).start();

    }

    public void showAppInfoPermissionProcessDialog(Context context,Activity activity,ListView aillv1,ArrayList<String> list ,ArrayList<Boolean> checkboxs , ArrayList<Boolean> switbs,String pkgname, String uid){
        showAppInfoProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_app_permission),aillv1,list,checkboxs,switbs,pkgname, AppInfoEnums.IS_PERMISSION,uid,AppInfoEnums.GET_PERMISSIOSN);
    }

    public void showAppInfoServiceProcessDialog(Context context,Activity activity,ListView aillv1,ArrayList<String> list ,ArrayList<Boolean> checkboxs , ArrayList<Boolean> switbs,String pkgname, String uid){
        showAppInfoProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_app_service),aillv1,list,checkboxs,switbs,pkgname, AppInfoEnums.IS_COMPENT_OR_PACKAGE,uid,AppInfoEnums.GET_SERVICES);
    }

    public void showAppInfoActivityProcessDialog(Context context,Activity activity,ListView aillv1,ArrayList<String> list ,ArrayList<Boolean> checkboxs , ArrayList<Boolean> switbs,String pkgname, String uid){
        showAppInfoProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_app_activity),aillv1,list,checkboxs,switbs,pkgname, AppInfoEnums.IS_COMPENT_OR_PACKAGE,uid,AppInfoEnums.GET_ACTIVITYS);
    }

    public void showAppInfoReceiverProcessDialog(Context context,Activity activity,ListView aillv1,ArrayList<String> list ,ArrayList<Boolean> checkboxs , ArrayList<Boolean> switbs,String pkgname, String uid){
        showAppInfoProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_app_receiver),aillv1,list,checkboxs,switbs,pkgname, AppInfoEnums.IS_COMPENT_OR_PACKAGE,uid,AppInfoEnums.GET_RECEIVERS);
    }

    public void showAppInfoProcessDialog(Context context , Activity activity , String msg , ListView lv1,ArrayList<String> list ,ArrayList<Boolean> checkboxs , ArrayList<Boolean> switbs , String pkgname , int mode , String uid,int mode2){
        ProgressDialog show = showMyDialog(context,msg);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0){
                    showAppInfoListView(context,lv1,list,checkboxs,switbs,pkgname,mode,uid);
                    show.dismiss();
                }else{
                    show.dismiss();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(mode2 == AppInfoEnums.GET_ACTIVITYS){
                    packageUtils.getPKGActivitys(context,pkgname,list,checkboxs,switbs);
                }

                if(mode2 == AppInfoEnums.GET_SERVICES){
                    packageUtils.getPKGServices(context,pkgname,list,checkboxs,switbs);
                }

                if(mode2 == AppInfoEnums.GET_PERMISSIOSN){
                    packageUtils.getPKGPermission(context,pkgname,list,checkboxs,switbs);
                }

                if(mode2 == AppInfoEnums.GET_RECEIVERS){
                    packageUtils.getPKGReceivers(context,pkgname,list,checkboxs,switbs);
                }
                sendHandlerMSG(handler,0);
            }
        }).start();


    }

    public void sendHandlerMSG(Handler handler , int value){
        Message msg = new Message();
        msg.what=value;
        handler.sendMessage(msg);
    }

    public void sendHandlerMSG(Handler handler , int value,Object obj){
        Message msg = new Message();
        msg.what=value;
        msg.obj=obj;
        handler.sendMessage(msg);
    }


    //显示提示框
    public ProgressDialog showMyDialog(Context context, String msg){
        ProgressDialog pd = new ProgressDialog(context);//初始化等待条
        pd.setMessage(msg);//等待显示条的信息
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.show();//等待显示条
        return pd;
    }

    private void sort(ArrayList<String> list , ArrayList<Boolean> switbs ){
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();
        for (int i = 0; i < switbs.size(); i++) {
            if(switbs.get(i)){
                list2.add(list.get(i));
            }else{
                list3.add(list.get(i));
            }
        }
        list.clear();
        switbs.clear();
        for (int i = 0; i < list3.size(); i++) {
            list.add(list3.get(i));
            switbs.add(false);
        }
        for (int i = 0; i < list2.size(); i++) {
            list.add(list2.get(i));
            switbs.add(true);
        }

    }


    public void showAppInfoListView(Context context , ListView listView ,ArrayList<String> list ,ArrayList<Boolean> checkboxs , ArrayList<Boolean> switbs , String pkgname , int mode , String uid ){
        sort(list,switbs);
        AppInfoAdapter adapter = new AppInfoAdapter(list, context, checkboxs, switbs,pkgname,mode,uid);
        listView.setAdapter(adapter);
    }

    //显示检索完毕后的应用列表
    public void showPKGS(Context context , ListView listView, ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        PKGINFOAdapter pkginfoAdapter = new PKGINFOAdapter(pkginfos, context, checkboxs);
        listView.setAdapter(pkginfoAdapter);
    }


    //显示检索完毕后的字符串列表
    public void showUsers(Context context ,ListView listView,ArrayList<String> userList,ArrayList<Boolean> checkboxsByUser) {
        GeneralAdapter userAdapter = new GeneralAdapter(userList, context, checkboxsByUser);
        listView.setAdapter(userAdapter);
    }

    public void showProcessBarDialogByCMD(Context context, ArrayList<PKGINFO> list, int appPermission, int APP_PERMIS_INDEX, String opt_str){

        String title = "title" , msg = "msg";
        if(appPermission == AppManagerEnum.APP_PERMISSION){
            title=tu.getLanguageString(context,R.string.apply_app_permission_title);
            msg=tu.getLanguageString(context,R.string.apply_app_permission_msg);
        }

        if(appPermission == AppManagerEnum.APP_DISABLE_COMPENT){
            title=tu.getLanguageString(context,R.string.apply_app_compat_title);
            msg=tu.getLanguageString(context,R.string.apply_app_compat_msg);
        }

        if(appPermission == AppManagerEnum.APP_FIREWALL){
            title=tu.getLanguageString(context,R.string.apply_app_network_title);
            msg=tu.getLanguageString(context,R.string.apply_app_network_msg);
        }

        if(appPermission == AppManagerEnum.APP_INSTALL_LOCAL_FILE){
            title=tu.getLanguageString(context,R.string.apply_app_install_title);
            msg=tu.getLanguageString(context,R.string.apply_app_install_msg);
        }

        if(appPermission == AppManagerEnum.APP_UNINSTALL){
            title=tu.getLanguageString(context,R.string.apply_app_uninstall_title);
            msg=tu.getLanguageString(context,R.string.apply_app_uninstall_msg);
        }

        if(appPermission == AppManagerEnum.APP_RESTORY){
            title=tu.getLanguageString(context,R.string.apply_app_restory_title);
            msg=tu.getLanguageString(context,R.string.apply_app_restory_msg);
        }

        if(appPermission == AppManagerEnum.APP_BACKUP){
            title=tu.getLanguageString(context,R.string.apply_app_backup_title);
            msg=tu.getLanguageString(context,R.string.apply_app_backup_msg);
        }

        if(appPermission == AppManagerEnum.APP_CLEAN_PROCESS){
            title=tu.getLanguageString(context,R.string.apply_app_process_title);
            msg=tu.getLanguageString(context,R.string.apply_app_process_msg);
        }

        if(appPermission == AppManagerEnum.APP_DUMP){
            title=tu.getLanguageString(context,R.string.apply_app_dump_title);
            msg=tu.getLanguageString(context,R.string.apply_app_dump_msg);
        }

        showProcessBarDialogByCMD(context,list,appPermission,APP_PERMIS_INDEX,opt_str,title,msg);
    }

    public void showProcessBarDialogByCMD(Context context, ArrayList<PKGINFO> list, int appPermission, int APP_PERMIS_INDEX, String opt_str, String title, String text ){
        int size = list.size();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        View vvv = LayoutInflater.from(context).inflate(R.layout.download_process_bar, null);
        ProgressBar mProgressBar = (ProgressBar) vvv.findViewById(R.id.dpbpb);
        TextView dpbtv1 = vvv.findViewById(R.id.dpbtv1);
        TextView dpbtv2 = vvv.findViewById(R.id.dpbtv2);
        TextView dpbtv3 = vvv.findViewById(R.id.dpbtv3);
        builder.setView(vvv);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        preventDismissDialog(alertDialog);
        dpbtv2.setText(size+"");
        Handler mUpdateProgressHandler = getProcessBarDialogHandler(context,mProgressBar,alertDialog,dpbtv1,dpbtv2,dpbtv3,text);
        easyManagerUtils easyMUtils = new easyManagerUtils();
        String sdpath = ft.getSDPath();
        String reqpkg = context.getPackageName();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= size; i++) {
                    PKGINFO pkginfo = list.get(i - 1);
                    sendProcessBarHandlerSum(mUpdateProgressHandler,i,size,pkginfo);
                    switch (appPermission){
                        case AppManagerEnum.APP_PERMISSION:
                            if(!pkginfo.getPkgname().equals(context.getPackageName())){
                                easyMUtils.setAppopsMode(new TransmissionEntity(pkginfo.getPkgname(), opt_str,reqpkg,APP_PERMIS_INDEX));
                            }
                            break;
                        case AppManagerEnum.APP_DISABLE_COMPENT:
                            easyMUtils.setComponentOrPackageEnabledState(new TransmissionEntity(pkginfo.getPkgname(),null,reqpkg,APP_PERMIS_INDEX==0? PackageAPI.COMPONENT_ENABLED_STATE_ENABLED:PackageAPI.COMPONENT_ENABLED_STATE_DISABLED_USER));
                            break;
                        case AppManagerEnum.APP_INFO_LAYOUT:
                            if(APP_PERMIS_INDEX == AppInfoEnums.IS_PERMISSION){
                                if(pkginfo.getApkpath().equals("true")){
                                    easyMUtils.revokeRuntimePermission(new TransmissionEntity(pkginfo.getPkgname(),pkginfo.getAppname(),reqpkg,0));
                                }else{
                                    easyMUtils.grantRuntimePermission(new TransmissionEntity(pkginfo.getPkgname(), pkginfo.getAppname(),reqpkg, 0));
                                }
                            }
                            if(APP_PERMIS_INDEX == AppInfoEnums.IS_COMPENT_OR_PACKAGE){
                                easyMUtils.setComponentOrPackageEnabledState(new TransmissionEntity(pkginfo.getPkgname()+"/"+pkginfo.getAppname(),null,reqpkg,pkginfo.getApkpath().equals("true")?PackageAPI.COMPONENT_ENABLED_STATE_DISABLED:PackageAPI.COMPONENT_ENABLED_STATE_ENABLED));
                            }
                            break;
                        case AppManagerEnum.APP_FIREWALL:
                            easyMUtils.setFirewallState(packageUtils.getPkgUid(context, pkginfo.getPkgname()), APP_PERMIS_INDEX==0);
                            break;
                        case AppManagerEnum.APP_INSTALL_LOCAL_FILE:
                            easyMUtils.installAPK(new TransmissionEntity(pkginfo.getApkpath(),null,reqpkg,0));
                            break;
                        case AppManagerEnum.APP_UNINSTALL:
                            if(!pkginfo.getPkgname().equals(context.getPackageName())){
                                easyMUtils.uninstallAPK(new TransmissionEntity(pkginfo.getPkgname(),null,reqpkg,0));
                            }
                            break;
                        case AppManagerEnum.APP_DUMP:
                            String s = sdpath+"/easyManager/dump";
                            File file = new File(s);
                            if(!file.exists()){
                                file.mkdirs();
                            }
                            if(APP_PERMIS_INDEX == 0){
                                s=s+"/"+pkginfo.getPkgname()+".apk";
                            }
                            if(APP_PERMIS_INDEX == 1){
                                s=s+"/"+pkginfo.getAppname()+".apk";
                            }
                            if(APP_PERMIS_INDEX == 2){
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SS");
                                String format = formatter.format(calendar.getTime());
                                s=s+"/"+format+".apk";
                            }
                            easyMUtils.dumpAPK(pkginfo.getApkpath(),s);
                            break;
                        case AppManagerEnum.APP_CLEAN_PROCESS:
                            if(!pkginfo.getPkgname().equals(context.getPackageName())){
                                easyMUtils.killpkg(new TransmissionEntity(pkginfo.getPkgname(),null,reqpkg, easyManagerEnums.KILL_PROCESS));
                            }
                            break;
                        case AppManagerEnum.APP_BACKUP:
                            String oop = opt_str+"---"+pkginfo.getApkpath()+"---"+sdpath;
                            easyMUtils.backupApk(new TransmissionEntity(pkginfo.getPkgname(), oop,reqpkg,APP_PERMIS_INDEX));
                            break;
                        case AppManagerEnum.APP_RESTORY:
                            String oop2 = opt_str+"---"+sdpath;
                            easyMUtils.restoryApp(new TransmissionEntity(pkginfo.getPkgname(), oop2,reqpkg,APP_PERMIS_INDEX));
                            break;
                    }
                }
                mUpdateProgressHandler.sendEmptyMessage(1);
            }
        }).start();
    }

    public void showIndexOfPKGSDialog(Context context , Activity activity, ListView lv1 , EditText editText , ArrayList<PKGINFO> pkginfos, ArrayList<String> strList, ArrayList<Boolean> checkboxs) {
        showIndexOfPKGSDialog(context,activity,lv1,tu.getLanguageString(context,R.string.now_search_ing_msg),editText,pkginfos,strList,checkboxs);
    }

    public void showIndexOfPKGSDialog(Context context , Activity activity,ListView lv1,String msg , EditText editText , ArrayList<PKGINFO> pkginfos,ArrayList<String> strList,ArrayList<Boolean> checkboxs) {
        showIndexOfPKGSDialog(context,activity,lv1,msg,editText.getText().toString(),pkginfos,strList,checkboxs);
    }

    public void showIndexOfPKGSDialog(Context context , Activity activity,ListView lv1,String msg , String searchStr , ArrayList<PKGINFO> pkginfos,ArrayList<String> strList,ArrayList<Boolean> checkboxs){
        ProgressDialog show = showMyDialog(context,msg);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0){
                    if(strList == null){
                        showPKGS(context,lv1,pkginfos,checkboxs);
                    }else{
                        showUsers(context,lv1,strList,checkboxs);
                    }
                    show.dismiss();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(strList == null){
                    pkginfos.addAll(tu.indexOfPKGS(activity,searchStr,pkginfos,checkboxs,0));
                }else{
                    tu.indexOfLIST(strList,checkboxs,null,searchStr);
                }
                sendHandlerMSG(handler,0);
            }
        }).start();

    }

    public void showProcessSearchViewDialog(Context context,Activity activity,ListView lv,ArrayList<PKGINFO> pkginfos,ArrayList<String> strList,ArrayList<Boolean> checkboxs){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(tu.getLanguageString(context,R.string.tips));
        View vvv = LayoutInflater.from(context).inflate(R.layout.search_view_layout, null);
        SearchView svlsv = vvv.findViewById(R.id.svlsv);
        ListView svllv = vvv.findViewById(R.id.svllv);
        svllv.setTextFilterEnabled(true);
        svlsv.setSubmitButtonEnabled(true);
        builder.setView(vvv);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        preventDismissDialog(alertDialog);
        svlsv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                showPKGS(context,lv,pkginfos,checkboxs);
                permittedDismissDialog(alertDialog);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!s.isEmpty()){
                    showIndexOfPKGSDialog(context,activity,svllv,tu.getLanguageString(context,R.string.now_search_ing_msg),s,pkginfos,strList,checkboxs);
                }else{
                    queryAllProcessDialog(context,activity,svllv,pkginfos,checkboxs);
                }
                return false;
            }
        });
    }

    public void showSearchViewDialog(Context context,Activity activity,ListView lv,ArrayList<PKGINFO> pkginfos,ArrayList<String> strList,ArrayList<Boolean> checkboxs){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(tu.getLanguageString(context,R.string.tips));
        View vvv = LayoutInflater.from(context).inflate(R.layout.search_view_layout, null);
        SearchView svlsv = vvv.findViewById(R.id.svlsv);
        ListView svllv = vvv.findViewById(R.id.svllv);
        svllv.setTextFilterEnabled(true);
        svlsv.setSubmitButtonEnabled(true);
        builder.setView(vvv);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        preventDismissDialog(alertDialog);
        svlsv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                showPKGS(context,lv,pkginfos,checkboxs);
                permittedDismissDialog(alertDialog);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!s.isEmpty()){
                    showIndexOfPKGSDialog(context,activity,svllv,tu.getLanguageString(context,R.string.now_search_ing_msg),s,pkginfos,strList,checkboxs);
                }else{
                    queryAllPKGSProcessDialog(context,activity,svllv,pkginfos,checkboxs);
                }
                return false;
            }
        });
    }

    public Handler getProcessBarDialogHandler(Context context , ProgressBar mProgressBar, AlertDialog alertDialog, TextView dpbtv1, TextView dpbtv2, TextView dpbtv3, String text){
        return new Handler() {

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        // 设置进度条
                        mProgressBar.setProgress((int)msg.obj);
                        break;
                    case 1:
                        // 隐藏当前下载对话框
                        permittedDismissDialog(alertDialog);
                        showInfoMsg(context,tu.getLanguageString(context,R.string.tips),tu.getLanguageString(context,R.string.its_ok_msg));
                        break;
                    case 2:
                        PKGINFO pkginfo = (PKGINFO) msg.obj;
                        dpbtv3.setText(text+(pkginfo.getAppname()==null?pkginfo.getPkgname():pkginfo.getAppname()));
                        break;
                    case 3:
                        dpbtv1.setText(((int)(msg.obj))+"");
                        break;
                    case 4:
                        alertDialog.setTitle(msg.obj.toString());
                        break;
                    case 5:
                        dpbtv2.setText(msg.obj.toString());
                        break;
                    case 6:
                        permittedDismissDialog(alertDialog);
                        showInfoMsg(context,tu.getLanguageString(context,R.string.error_tips),msg.obj.toString());
                        break;
                }
            }
        };
    }



    public void sendHandlerMsg(Handler handler,int n,Object obj){
        Message message = new Message();
        message.what=n;
        message.obj=obj;
        handler.sendMessage(message);
    }


    public void sendProcessBarHandlerSum(Handler handler,int i , int size , PKGINFO pkginfo){
        // 计算进度条当前位置
        sendHandlerMsg(handler,0,(int) (((float) i / size) * 100));
        sendHandlerMsg(handler,3,i);
        sendHandlerMsg(handler,2,pkginfo);
    }

    public void showAppInfoSearchViewDialog(Context context, Activity activity, int app_info_mode,int app_info_mode2, String pkgname, String uid, ListView aillv1, ArrayList<String> list, ArrayList<Boolean> checkboxs, ArrayList<Boolean> switbs) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(tu.getLanguageString(context,R.string.tips));
        View vvv = LayoutInflater.from(context).inflate(R.layout.search_view_layout, null);
        SearchView svlsv = vvv.findViewById(R.id.svlsv);
        ListView svllv = vvv.findViewById(R.id.svllv);
        svllv.setTextFilterEnabled(true);
        svlsv.setSubmitButtonEnabled(true);
        builder.setView(vvv);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        preventDismissDialog(alertDialog);
        svlsv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                showAppInfoListView(context,aillv1,list,checkboxs,switbs,pkgname,app_info_mode,uid);
                permittedDismissDialog(alertDialog);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!s.isEmpty()){
                    showIndexOfAppInfoDialog(context,activity,svllv,tu.getLanguageString(context,R.string.now_search_ing_msg),s,pkgname,app_info_mode,uid,list,checkboxs,switbs);
                }else{
                    if(app_info_mode2 == AppInfoEnums.GET_ACTIVITYS){
                        showAppInfoActivityProcessDialog(context,activity,aillv1,list,checkboxs,switbs,pkgname,uid);
                    }

                    if(app_info_mode2 == AppInfoEnums.GET_SERVICES){
                        showAppInfoServiceProcessDialog(context,activity,aillv1,list,checkboxs,switbs,pkgname,uid);
                    }

                    if(app_info_mode2 == AppInfoEnums.GET_PERMISSIOSN){
                        showAppInfoPermissionProcessDialog(context,activity,aillv1,list,checkboxs,switbs,pkgname,uid);
                    }

                    if(app_info_mode2 == AppInfoEnums.GET_RECEIVERS){
                        showAppInfoReceiverProcessDialog(context,activity,aillv1,list,checkboxs,switbs,pkgname,uid);
                    }
                }
                return false;
            }
        });


    }

    public void showIndexOfAppInfoDialog(Context context, Activity activity, ListView svllv, String msg, String s1,String pkgname, int app_info_mode, String uid, ArrayList<String> list, ArrayList<Boolean> checkboxs, ArrayList<Boolean> switbs) {

        ProgressDialog show = showMyDialog(context,msg);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0){
                    showAppInfoListView(context,svllv,list,checkboxs,switbs,pkgname,app_info_mode,uid);
                    show.dismiss();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                tu.indexOfLIST(list,checkboxs,switbs,s1);
                sendHandlerMSG(handler,0);
            }
        }).start();

    }

    public void queryLocalBackupProcessDialog(Context context, Activity activity, ListView apllv1, ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        queryPKGProcessDialog(context,activity,tu.getLanguageString(context,R.string.scan_local_backup_file_msg),apllv1,pkginfos,checkboxs,7);
    }

    public void showRestorySearchViewDialog(Context context, Activity activity, ListView apllv1, ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(tu.getLanguageString(context,R.string.tips));
        View vvv = LayoutInflater.from(context).inflate(R.layout.search_view_layout, null);
        SearchView svlsv = vvv.findViewById(R.id.svlsv);
        ListView svllv = vvv.findViewById(R.id.svllv);
        svllv.setTextFilterEnabled(true);
        svlsv.setSubmitButtonEnabled(true);
        builder.setView(vvv);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        preventDismissDialog(alertDialog);
        svlsv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                showPKGS(context,apllv1,pkginfos,checkboxs);
                permittedDismissDialog(alertDialog);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!s.isEmpty()){
                    showIndexOfPKGSDialog(context,activity,svllv,tu.getLanguageString(context,R.string.now_search_ing_msg),s,pkginfos,null,checkboxs);
                }else{
                    queryLocalBackupProcessDialog(context,activity,svllv,pkginfos,checkboxs);
                }
                return false;
            }
        });


    }


    public Handler dismissDialogHandler(int value,ProgressDialog show){
        return new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==value){
                    show.dismiss();
                }
            }
        };
    }

}

package com.easymanager.utils.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.core.api.PackageAPI;
import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.enums.AppInfoEnums;
import com.easymanager.enums.AppManagerEnum;
import com.easymanager.enums.easyManagerEnums;
import com.easymanager.utils.base.DialogUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PackageDialog extends DialogUtils {

    public void showProcessBarDialogByCMD(Context context, ArrayList<PKGINFO> list, int appPermission, int APP_PERMIS_INDEX, String opt_str, String title, String text,int uid ){
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
        String sdpath = ft.getSDPath(easyMUtils.getCurrentUserID());
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
                                easyMUtils.setAppopsMode(new TransmissionEntity(pkginfo.getPkgname(), opt_str,reqpkg,APP_PERMIS_INDEX,uid));
                            }
                            break;
                        case AppManagerEnum.APP_DISABLE_COMPENT:
                            easyMUtils.setComponentOrPackageEnabledState(new TransmissionEntity(pkginfo.getPkgname(),null,reqpkg,APP_PERMIS_INDEX==0? PackageAPI.COMPONENT_ENABLED_STATE_ENABLED:PackageAPI.COMPONENT_ENABLED_STATE_DISABLED_USER,uid));
                            easyMUtils.setPackagesSuspend(new TransmissionEntity(pkginfo.getPkgname(),null,reqpkg,APP_PERMIS_INDEX==0? -1:0,uid));
                            easyMUtils.clearPackageData(new TransmissionEntity(pkginfo.getPkgname(),null,reqpkg,0,uid));
                            break;
                        case AppManagerEnum.APP_INFO_LAYOUT:
                            if(APP_PERMIS_INDEX == AppInfoEnums.IS_PERMISSION){
                                if(pkginfo.getApkpath().equals("true")){
                                    easyMUtils.revokeRuntimePermission(new TransmissionEntity(pkginfo.getPkgname(),pkginfo.getAppname(),reqpkg,0,uid));
                                }else{
                                    easyMUtils.grantRuntimePermission(new TransmissionEntity(pkginfo.getPkgname(), pkginfo.getAppname(),reqpkg, 0,uid));
                                }
                            }
                            if(APP_PERMIS_INDEX == AppInfoEnums.IS_COMPENT_OR_PACKAGE){
                                easyMUtils.setComponentOrPackageEnabledState(new TransmissionEntity(pkginfo.getPkgname()+"/"+pkginfo.getAppname(),null,reqpkg,pkginfo.getApkpath().equals("true")?PackageAPI.COMPONENT_ENABLED_STATE_DISABLED:PackageAPI.COMPONENT_ENABLED_STATE_ENABLED,uid));
                            }
                            break;
                        case AppManagerEnum.APP_FIREWALL:
                            easyMUtils.setFirewallState(new TransmissionEntity(pkginfo.getPkgname(),null,reqpkg,APP_PERMIS_INDEX,uid));
                            break;
                        case AppManagerEnum.APP_INSTALL_LOCAL_FILE:
                            easyMUtils.installAPK(new TransmissionEntity(pkginfo.getApkpath(),null,reqpkg,0,uid));
                            break;
                        case AppManagerEnum.APP_UNINSTALL:
                            if(!pkginfo.getPkgname().equals(context.getPackageName())){
                                easyMUtils.uninstallAPK(new TransmissionEntity(pkginfo.getPkgname(),null,reqpkg,0,uid));
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
                                if(APP_PERMIS_INDEX == 0){
                                    easyMUtils.killpkg(new TransmissionEntity(pkginfo.getPkgname(),null,reqpkg, easyManagerEnums.KILL_PROCESS,uid));
                                }

                                if(APP_PERMIS_INDEX == 1){
                                    easyMUtils.addRunningAPPS(new TransmissionEntity(pkginfo.getPkgname(),opt_str,reqpkg,easyManagerEnums.ADD_RUNNING_PACKAGE,uid));
                                }

                            }
                            break;
                        case AppManagerEnum.APP_BACKUP:
                            String oop = opt_str+"---"+pkginfo.getApkpath()+"---"+sdpath;
                            easyMUtils.backupApk(new TransmissionEntity(pkginfo.getPkgname(), oop,reqpkg,APP_PERMIS_INDEX,uid));
                            break;
                        case AppManagerEnum.APP_RESTORY:
                            String oop2 = opt_str+"---"+sdpath;
                            easyMUtils.restoryApp(new TransmissionEntity(pkginfo.getPkgname(), oop2,reqpkg,APP_PERMIS_INDEX,uid));
                            break;
                        case AppManagerEnum.APP_CLONE_REMOVE:
                            easyMUtils.removeAppClone(context,Integer.valueOf(pkginfo.getPkgname()));
                            break;
                        case AppManagerEnum.APP_CLONE_MANAGE:
                            if(APP_PERMIS_INDEX == 0){
                                easyMUtils.installAPK(new TransmissionEntity(packageUtils.getPKGINFO(context,pkginfo.getPkgname()).getApkpath(),null,reqpkg,0,uid));
                            }else{
                                easyMUtils.uninstallAPK(new TransmissionEntity(pkginfo.getPkgname(),null,reqpkg,0,uid));
                            }
                            break;
                    }
                }
                mUpdateProgressHandler.sendEmptyMessage(1);
            }
        }).start();
    }



    public void showProcessBarDialogByCMD(Context context, ArrayList<PKGINFO> list, int appPermission, int APP_PERMIS_INDEX, String opt_str,int uid){

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

        if(appPermission == AppManagerEnum.APP_CLONE_REMOVE){
            title=tu.getLanguageString(context,R.string.app_clone_delete_clone_title);
            msg=tu.getLanguageString(context,R.string.app_clone_delete_clone_msg);
        }
        showProcessBarDialogByCMD(context,list,appPermission,APP_PERMIS_INDEX,opt_str,title,msg,uid);
    }

    public void showIndexOfAppInfoDialog(Context context, Activity activity, ListView svllv, String msg, String s1,String pkgname, int app_info_mode, Integer uid, ArrayList<String> list, ArrayList<Boolean> checkboxs, ArrayList<Boolean> switbs) {

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

    public void showAppInfoSearchViewDialog(Context context, Activity activity, int app_info_mode, int app_info_mode2, String pkgname, Integer uid, ListView aillv1, ArrayList<String> list, ArrayList<Boolean> checkboxs, ArrayList<Boolean> switbs) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(tu.getLanguageString(context, R.string.tips));
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



    public void showAppInfoPermissionProcessDialog(Context context,Activity activity,ListView aillv1,ArrayList<String> list ,ArrayList<Boolean> checkboxs , ArrayList<Boolean> switbs,String pkgname, Integer uid){
        showAppInfoProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_app_permission),aillv1,list,checkboxs,switbs,pkgname, AppInfoEnums.IS_PERMISSION,uid,AppInfoEnums.GET_PERMISSIOSN);
    }

    public void showAppInfoServiceProcessDialog(Context context,Activity activity,ListView aillv1,ArrayList<String> list ,ArrayList<Boolean> checkboxs , ArrayList<Boolean> switbs,String pkgname, Integer uid){
        showAppInfoProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_app_service),aillv1,list,checkboxs,switbs,pkgname, AppInfoEnums.IS_COMPENT_OR_PACKAGE,uid,AppInfoEnums.GET_SERVICES);
    }

    public void showAppInfoActivityProcessDialog(Context context,Activity activity,ListView aillv1,ArrayList<String> list ,ArrayList<Boolean> checkboxs , ArrayList<Boolean> switbs,String pkgname, Integer uid){
        showAppInfoProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_app_activity),aillv1,list,checkboxs,switbs,pkgname, AppInfoEnums.IS_COMPENT_OR_PACKAGE,uid,AppInfoEnums.GET_ACTIVITYS);
    }

    public void showAppInfoReceiverProcessDialog(Context context,Activity activity,ListView aillv1,ArrayList<String> list ,ArrayList<Boolean> checkboxs , ArrayList<Boolean> switbs,String pkgname, Integer uid){
        showAppInfoProcessDialog(context,activity,tu.getLanguageString(context,R.string.get_app_receiver),aillv1,list,checkboxs,switbs,pkgname, AppInfoEnums.IS_COMPENT_OR_PACKAGE,uid,AppInfoEnums.GET_RECEIVERS);
    }

    public void showAppInfoProcessDialog(Context context , Activity activity , String msg , ListView lv1,ArrayList<String> list ,ArrayList<Boolean> checkboxs , ArrayList<Boolean> switbs , String pkgname , int mode , Integer uid,int mode2){
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
                    packageUtils.getPKGActivitys(context,pkgname,list,checkboxs,switbs,uid);
                }

                if(mode2 == AppInfoEnums.GET_SERVICES){
                    packageUtils.getPKGServices(context,pkgname,list,checkboxs,switbs,uid);
                }

                if(mode2 == AppInfoEnums.GET_PERMISSIOSN){
                    packageUtils.getPKGPermission(context,pkgname,list,checkboxs,switbs,uid);
                }

                if(mode2 == AppInfoEnums.GET_RECEIVERS){
                    packageUtils.getPKGReceivers(context,pkgname,list,checkboxs,switbs,uid);
                }
                sendHandlerMSG(handler,0);
            }
        }).start();


    }

}
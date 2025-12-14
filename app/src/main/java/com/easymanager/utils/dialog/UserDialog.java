package com.easymanager.utils.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.os.UserManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.utils.base.DialogUtils;

import java.util.ArrayList;

public class UserDialog extends DialogUtils {

    public void showAppCloneManagerProcessBarDialog(Context context, ArrayList<PKGINFO> list, ArrayList<String> strlist, int APP_PERMIS_INDEX){
        int size = list.size();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String tmp = APP_PERMIS_INDEX ==0 ? tu.getLanguageString(context, R.string.app_clone_manager_clone_mode_str1):tu.getLanguageString(context,R.string.app_clone_manager_clone_mode_str2);
        builder.setTitle(String.format(tu.getLanguageString(context,R.string.app_clone_manager_clone_title),tmp));
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
        Handler mUpdateProgressHandler = getProcessBarDialogHandler(context,mProgressBar,alertDialog,dpbtv1,dpbtv2,dpbtv3,tu.getLanguageString(context,R.string.app_clone_manager_clone_msg));
        String reqpkg = context.getPackageName();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= list.size(); i++) {
                    PKGINFO pkginfo = list.get(i-1);
                    sendProcessBarHandlerSum(mUpdateProgressHandler,i,size,pkginfo);
                    for (String s : strlist) {
                        int uid = Integer.valueOf(s);
                        if(APP_PERMIS_INDEX == 0){
                            easyMUtils.installExistingPKG(new TransmissionEntity(pkginfo.getPkgname(),null,reqpkg,0,uid));
                        }else{
                            easyMUtils.uninstallAPK(new TransmissionEntity(pkginfo.getPkgname(),null,reqpkg,0,uid));
                        }
                    }
                }
                mUpdateProgressHandler.sendEmptyMessage(1);
            }
        }).start();
    }
    public boolean isFirstUser(String[] s1 , String s2){
        for (String s : s1) {
            if(s2.equals(s)){
                return true;
            }
        }
        return false;
    }

    public void showAppClone(Context context, ArrayList<PKGINFO> list,Integer count,String title,String text){
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
        dpbtv2.setText(count+"");
        Handler mUpdateProgressHandler = getProcessBarDialogHandler(context,mProgressBar,alertDialog,dpbtv1,dpbtv2,dpbtv3,text);
        String reqpkg = context.getPackageName();
        easyMUtils.setSkipError(true);
        int currentUser = easyMUtils.getCurrentUserID();
        new Thread(new Runnable() {
            @Override
            public void run() {

                String[] firstUsers = easyMUtils.getAppCloneUsers();
                for (int i = 1; i <= count; i++) {
                    easyMUtils.createAppClone(context);
                    sendProcessBarHandlerSum(mUpdateProgressHandler,i,count,new PKGINFO(i+"",i+"",null,null,null,null));
                }


                String[] users = easyMUtils.getAppCloneUsers();
                for (int j = 0; j < list.size(); j++) {
                    PKGINFO pkginfo = list.get(j);
                    for (String s : users) {
                        if(!isFirstUser(firstUsers,s)){
                            easyMUtils.installExistingPKG(new TransmissionEntity(pkginfo.getPkgname(),null,reqpkg,0,Integer.valueOf(s)));
                        }
                    }
                }


                for (String user : users) {
                    int uid = Integer.valueOf(user);
                    if(!user.equals(String.valueOf(currentUser)) && uid < 900){
                        easyMUtils.startAppClone(context,uid);
                    }
                }

                mUpdateProgressHandler.sendEmptyMessage(1);
            }
        }).start();
    }
    public void startAppCloneUsers(Context context, Activity activity) {
        ProgressDialog show = showMyDialog(context,tu.getLanguageString(context,R.string.app_clone_manager_clone_start_users_msg));
        Handler handler = dismissDialogHandler(0,show);
        int currentUserID = easyMUtils.getCurrentUserID();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] users = easyMUtils.getAppCloneUsers();
                for (String user : users) {
                    int uid = Integer.valueOf(user);
                    if(!user.equals(String.valueOf(currentUserID)) && uid < 900){
                        easyMUtils.startAppClone(context,uid);
                    }
                }
                sendHandlerMSG(handler,0);
            }
        }).start();
    }

    public void unlockMaxLimit(Context context, Activity activity) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(tu.getLanguageString(context,R.string.tips));
        ab.setMessage(tu.getLanguageString(context,R.string.show_clone_unock_max_user_tips_msg));
        ab.setNegativeButton(tu.getLanguageString(context, R.string.dialog_sure_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                ProgressDialog show = showMyDialog(context,tu.getLanguageString(context,R.string.execute_cmd));
                Handler handler = dismissDialogHandler(0,show);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String cmdstr = "resetprop ro.debuggable 1;setprop persist.sys.max_profiles 1024 ; setprop fw.max_users 1024 ; setprop fw.show_multiuserui 1;am restart;";
                        easyMUtils.runCMD(cmdstr);
                        sendHandlerMSG(handler,0);
                    }
                }).start();
            }
        });

        AlertDialog alertDialog = ab.create();
        alertDialog.show();
        TextView tv = alertDialog.getWindow().getDecorView().findViewById(android.R.id.message);
        tv.setTextIsSelectable(true);

    }

    public void queryLocalAppCloneUserProcessDialog(Context context, Activity activity, ListView lv1 , ArrayList<String> strings, ArrayList<Boolean> checkboxs){
        ProgressDialog show = showMyDialog(context,tu.getLanguageString(context,R.string.app_clone_manager_clone_query_users_msg));
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
                String[] appCloneUsers = easyMUtils.getAppCloneUsers();
                for (String appCloneUser : appCloneUsers) {
                    strings.add(appCloneUser);
                    checkboxs.add(false);
                }
                sendHandlerMSG(handler,0);
            }
        }).start();
    }


}

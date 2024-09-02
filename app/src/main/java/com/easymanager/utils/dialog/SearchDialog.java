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
import android.widget.SearchView;

import com.easymanager.R;
import com.easymanager.entitys.PKGINFO;

import java.util.ArrayList;

public class SearchDialog extends QueryDialog {

    public void showRestorySearchViewDialog(Context context, Activity activity, ListView apllv1, ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, int uid) {
        showSearchViewDialogCore(context,activity,apllv1,pkginfos,null,checkboxs,uid,2);
    }

    public void showIndexOfPKGSDialog(Context context , Activity activity,ListView lv1,String msg , String searchStr , ArrayList<PKGINFO> pkginfos,ArrayList<String> strList,ArrayList<Boolean> checkboxs){
        try {
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
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(strList == null){
                        pkginfos.addAll(tu.indexOfPKGS(activity,searchStr,pkginfos,checkboxs,0));
                    }else{
                        tu.indexOfLIST(strList,checkboxs,null,searchStr);
                    }
                    sendHandlerMSG(handler,0);
                }
            });
            t.start();
            //加入这个，阻塞线程，等待线程运行结束后，才允许下一个线程启动
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void showProcessSearchViewDialog(Context context,Activity activity,ListView lv,ArrayList<PKGINFO> pkginfos,ArrayList<String> strList,ArrayList<Boolean> checkboxs,int uid){
        showSearchViewDialogCore(context,activity,lv,pkginfos,strList,checkboxs,uid,1);
    }

    public void showSearchViewDialogCore(Context context,Activity activity,ListView lv,ArrayList<PKGINFO> pkginfos,ArrayList<String> strList,ArrayList<Boolean> checkboxs,int uid,int mode){
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
        if(pkginfos != null && pkginfos.size() == 0) {
            if(mode == 0){
                queryAllPKGSProcessDialog(context,activity,svllv,pkginfos,checkboxs,uid);
            }

            if(mode == 1){
                queryAllProcessDialog(context,activity,svllv,pkginfos,checkboxs,uid);
            }

            if(mode == 2){
                queryLocalBackupProcessDialog(context,activity,svllv,pkginfos,checkboxs,uid);
            }

        }
        svlsv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!s.isEmpty()){
                    showIndexOfPKGSDialog(context,activity,svllv,tu.getLanguageString(context,R.string.now_search_ing_msg),s,pkginfos,strList,checkboxs);
                    showPKGS(context,lv,pkginfos,checkboxs);
                }
                permittedDismissDialog(alertDialog);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    public void showSearchViewDialog(Context context,Activity activity,ListView lv,ArrayList<PKGINFO> pkginfos,ArrayList<String> strList,ArrayList<Boolean> checkboxs,int uid){
        showSearchViewDialogCore(context,activity,lv,pkginfos,strList,checkboxs,uid,0);
    }

}

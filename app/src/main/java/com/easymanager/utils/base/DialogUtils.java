package com.easymanager.utils.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.adapters.AppInfoAdapter;
import com.easymanager.adapters.GeneralAdapter;
import com.easymanager.adapters.PKGINFOAdapter;
import com.easymanager.core.utils.CMD;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.utils.FileTools;
import com.easymanager.utils.PackageUtils;
import com.easymanager.utils.ProcessUtils;
import com.easymanager.utils.easyManagerUtils;

import java.io.File;
import java.util.ArrayList;

public class DialogUtils extends DialogBaseUtils {
    public FileTools ft = new FileTools();
    public PackageUtils packageUtils = new PackageUtils();

    public ProcessUtils processUtils = new ProcessUtils();

    public easyManagerUtils easyMUtils = new easyManagerUtils();


    public DialogUtils(){}

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

    //显示检索完毕后的字符串列表
    public void showUsers(Context context , ListView listView, ArrayList<String> userList, ArrayList<Boolean> checkboxsByUser) {
        GeneralAdapter userAdapter = new GeneralAdapter(userList, context, checkboxsByUser);
        listView.setAdapter(userAdapter);
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

    public void showAppInfoListView(Context context , ListView listView ,ArrayList<String> list ,ArrayList<Boolean> checkboxs , ArrayList<Boolean> switbs , String pkgname , int mode , Integer uid ){
        sort(list,switbs);
        AppInfoAdapter adapter = new AppInfoAdapter(list, context, checkboxs, switbs,pkgname,mode,uid);
        listView.setAdapter(adapter);
    }

    //显示检索完毕后的应用列表
    public void showPKGS(Context context , ListView listView, ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs) {
        PKGINFOAdapter pkginfoAdapter = new PKGINFOAdapter(pkginfos, context, checkboxs);
        listView.setAdapter(pkginfoAdapter);
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
                        dpbtv3.setText(String.format(text,(pkginfo.getAppname()==null?pkginfo.getPkgname():pkginfo.getAppname())));
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

package com.easymanager.utils.dialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.easymanager.R;
import com.easymanager.utils.NetUtils;
import com.easymanager.utils.base.DialogUtils;

public class NetUtilsDialog extends DialogUtils {

    private NetUtils netUtils = new NetUtils();

    public void checkupdate(Context context){
        ProgressDialog show = showMyDialog(context,context.getString(R.string.check_app_update));
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                show.dismiss();
                showUpdateDialog(context,msg.what==0);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = netUtils.checkUpdate(context);
                sendHandlerMSG(handler, b ? 0 : 1);
            }
        }).start();
    }

    public void showUpdateDialog(Context con,boolean isUpdate){

        View customeDialog = getCustomeDialog(con, tu.getLanguageString(con,R.string.check_app_update_title), isUpdate?con.getString(R.string.check_app_update_is_ok):con.getString(R.string.check_app_update_is_no));
        AlertDialog.Builder ab = new AlertDialog.Builder(con);
        ab.setView(customeDialog);
        ab.setNegativeButton(tu.getLanguageString(con, R.string.dialog_sure_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(isUpdate){
                    openUrlWithBrowser(con);
                }
                dialogInterface.dismiss();
            }
        });

        if(isUpdate){
            ab.setNeutralButton(tu.getLanguageString(con, R.string.dialog_cancel_text), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    dialogInterface.dismiss();
                }
            });
        }

        AlertDialog alertDialog = ab.create();
        alertDialog.show();

    }

    public void openUrlWithBrowser(Context context){
        String cn_download_link = "https://gitee.com/SorryMyLife/easyManager/raw/master/app/release/easyManager_release.apk";
        String global_download_link = "https://github.com/MrsEWE44/easyManager/raw/master/app/release/easyManager_release.apk";
        String language = context.getResources().getConfiguration().locale.getLanguage();
        openUrlWithBrowser(context,language.equals("zh")?cn_download_link:global_download_link);
    }

    public void openUrlWithBrowser(Context context,String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

}

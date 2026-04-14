package com.easymanager.utils.dialog;

import androidx.appcompat.app.AlertDialog;
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class NetUtilsDialog extends DialogUtils {

    private NetUtils netUtils = new NetUtils();

    public void checkupdate(Context context) {
        AlertDialog show = showMyDialog(context, tu.getLanguageString(context, R.string.general_loading));

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                show.dismiss();
                showUpdateDialog(context, msg.what == 0);
            }
        };
        new Thread(() -> {
            boolean b = netUtils.checkUpdate(context);
            sendHandlerMSG(handler, b ? 0 : 1);
        }).start();
    }

    public void showUpdateDialog(Context con, boolean isUpdate) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(con)
                .setTitle(R.string.check_app_update_title)
                .setMessage(isUpdate ? R.string.check_app_update_is_ok : R.string.check_app_update_is_no)
                .setPositiveButton(R.string.dialog_sure_text, (dialog, which) -> {
                    if (isUpdate) {
                        openUrlWithBrowser(con);
                    }
                    dialog.dismiss();
                });

        if (isUpdate) {
            builder.setNegativeButton(R.string.dialog_cancel_text, (dialog, which) -> dialog.dismiss());
        }

        builder.show();
    }

    public void openUrlWithBrowser(Context context){
        String cn_download_link = "https://gitee.com/SorryMyLife/easyManager/releases";
        String global_download_link = "https://github.com/MrsEWE44/easyManager/releases";
        String language = context.getResources().getConfiguration().locale.getLanguage();
        openUrlWithBrowser(context,language.equals("zh")?cn_download_link:global_download_link);
    }

    public void openUrlWithBrowser(Context context,String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

}

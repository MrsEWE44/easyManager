package com.easymanager.utils.base;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.utils.TextUtils;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogBaseUtils {

    public TextUtils tu = new TextUtils();

    public View getCustomeDialog(Context con, String title, String msg) {
        View view = LayoutInflater.from(con).inflate(R.layout.custom_alert_dialog, null);
        TextView titleView = view.findViewById(R.id.dialog_title);
        TextView msgView = view.findViewById(R.id.dialog_message);
        if (titleView != null) titleView.setText(title);
        if (msgView != null) msgView.setText(msg);
        return view;
    }

    //显示一个弹窗
    public void showInfoMsg(Context con, String title, String msg) {
        new MaterialAlertDialogBuilder(con)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.dialog_sure_text, (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * 阻止关闭对话框
     */
    public void preventDismissDialog(AlertDialog ddd) {
        if (ddd != null) {
            ddd.setCancelable(false);
            ddd.setCanceledOnTouchOutside(false);
        }
    }

    /**
     * 关闭对话框
     */
    public void permittedDismissDialog(AlertDialog ddd) {
        if (ddd != null) {
            try {
                ddd.setCancelable(true);
                if (ddd.isShowing()) {
                    ddd.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

package com.easymanager.utils.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.utils.TextUtils;

import java.lang.reflect.Field;

public class DialogBaseUtils {

    public TextUtils tu = new TextUtils();

    public View getCustomeDialog(Context con,String title , String msg){
        // 创建自定义布局的 AlertDialog
        LayoutInflater inflater = LayoutInflater.from(con);
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);

// 设置标题
        TextView titleView = dialogView.findViewById(R.id.dialog_title);
        titleView.setText(title);

// 设置消息
        TextView messageView = dialogView.findViewById(R.id.dialog_message);
        messageView.setText(msg);
        messageView.setTextIsSelectable(true);
        return dialogView;
    }

    //显示一个弹窗
    public void showInfoMsg(Context con,String title , String msg){
        View customeDialog = getCustomeDialog(con, title, msg);
        AlertDialog.Builder ab = new AlertDialog.Builder(con);
        ab.setView(customeDialog);
        ab.setNegativeButton(tu.getLanguageString(con, R.string.dialog_sure_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = ab.create();
        alertDialog.show();

    }

    /**
     * 通过反射 阻止关闭对话框
     */
    public void preventDismissDialog(AlertDialog ddd) {
        try {
            Field field = ddd.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            //设置mShowing值，欺骗android系统
            field.set(ddd, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭对话框
     */
    public void permittedDismissDialog(AlertDialog ddd) {
        try {
            Field field = ddd.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(ddd, true);
        } catch (Exception e) {
        }
        ddd.dismiss();
    }


}

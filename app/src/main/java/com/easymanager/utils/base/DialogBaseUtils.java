package com.easymanager.utils.base;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.utils.TextUtils;

import java.lang.reflect.Field;

public class DialogBaseUtils {

    public TextUtils tu = new TextUtils();
    //显示一个弹窗
    public void showInfoMsg(Context con,String title , String msg){
        AlertDialog.Builder ab = new AlertDialog.Builder(con);
        ab.setTitle(title);
        ab.setMessage(msg);
        ab.setNegativeButton(tu.getLanguageString(con, R.string.dialog_sure_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = ab.create();
        alertDialog.show();
        TextView tv = alertDialog.getWindow().getDecorView().findViewById(android.R.id.message);
        tv.setTextIsSelectable(true);
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

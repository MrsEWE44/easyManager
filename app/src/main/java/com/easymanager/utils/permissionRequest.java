package com.easymanager.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.provider.Settings;
import android.widget.Toast;

import com.easymanager.R;


public class permissionRequest {

    public static void intentExternal(Context context){
        intentPKG(context, Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
    }

    public static void intentPKG(Context context,String action){
        Intent intent = new Intent(action);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    public static void getExternalStorageManager(Context context,Activity activity){
        // 通过api判断手机当前版本号
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 安卓11，判断有没有“所有文件访问权限”权限
            if (!Environment.isExternalStorageManager()) {
                intentExternal(context);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 安卓6 判断有没有读写权限权限
            if (checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, R.string.permission_storage_grand, Toast.LENGTH_SHORT).show();
            }else{
                try {
                    intentExternal(context);
                }catch (Exception e){
                    requestExternalStoragePermission(activity);
                }
            }
        }
    }

    public static void requestExternalStoragePermission(Activity activity){
        String p[] = {Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission_group.STORAGE,Manifest.permission.INSTALL_PACKAGES, Manifest.permission.CHANGE_COMPONENT_ENABLED_STATE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE,Manifest.permission.REQUEST_COMPANION_PROFILE_WATCH};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(p,0);
        }
    }

    public static void requestInstallLocalProgram(Activity activity){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            boolean isGranted = activity.getPackageManager().canRequestPackageInstalls();
            if(!isGranted){
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                activity.startActivityForResult(intent, 98);
            }
        }else{
            if(!(checkSelfPermission(activity, Manifest.permission.REQUEST_INSTALL_PACKAGES) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(activity, Manifest.permission.INSTALL_PACKAGES) == PackageManager.PERMISSION_GRANTED)){
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                activity.startActivityForResult(intent, 98);
            }
        }
    }

    public static int checkSelfPermission(Context context , String permission){
        ContextWrapper c = new ContextWrapper(context);
        return c.checkPermission(permission, Process.myUid(),Process.myUid());
    }

}

package com.easymanager.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.easymanager.core.utils.FileUtils;
import com.easymanager.core.utils.MyConfigUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileTools extends FileUtils {

    //调用系统文件选择器选择一个文件夹
    public void execDirSelect(Context context, Activity activity, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT){
            execFileSelect(context,activity,msg,43);
        }else{
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            activity.startActivityForResult(intent, 43);
        }
    }

    public void execFileSelect(Context context, Activity activity, String msg){
        execFileSelect(context,activity,msg,0);
    }
    //调用系统文件选择器
    public void execFileSelect(Context context, Activity activity, String msg,int code) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);//打开多个文件
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivityForResult(intent, code);
    }

    //获取在内部存储的自家路径
    public String getMyStorageHomePath(Context context) {
        return context.getExternalFilesDir(null).getParent();
    }

    //获取自家data的files路径
    public String getMyHomeFilesPath(Context context) {
        return context.getFilesDir().toString();
    }

    //选择文件时，判断是否为理想类型
    public void selectFile(Context context, String storage, Uri uri, ArrayList<String> list, ArrayList<Boolean> checkboxs, String msg, String equalstr) {
        String filePath = storage + "/" + uri.getPath().replaceAll("/document/primary:", "");
        String fileName = new StringTools().getPathByLastNameType(filePath);
        if (fileName.equals(equalstr)) {
//            filePath=filePath.substring(0,filePath.lastIndexOf("/"));
            list.add(filePath);
            checkboxs.add(false);
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    // 目标SD路径：/storage/emulated/0
    public String getSDPath(Integer uid){
        String sdPath = "";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String path = "/storage/emulated/"+uid+"/Android";
            File file = new File(path);
            if(file.exists()){
                sdPath = "/storage/emulated/"+uid;
            }
        }else {
            String path = "/storage/emulated/legacy/Android";
            File file = new File(path);
            if(file.exists()){
                sdPath = "/storage/emulated/legacy";
            }else{
                sdPath = Environment.getExternalStorageDirectory().toString();
            }
        }
        return sdPath;
    }

    public void getAllFileByEndName(String filePath, String file_end_name, List<File> files){
        //获取指定目录下的所有文件或者目录的File数组
        File[] fileArray = new File(filePath).listFiles();
        //遍历该File数组，得到每一个File对象
        if(fileArray != null){
            for (File file :fileArray){
                //判断file对象是否为目录
                if (file.isDirectory()){
                    //是：递归调用
                    getAllFileByEndName(file.getAbsolutePath(),file_end_name,files);
                }else{
                    //否：获取绝对路径输出在控制台
                    String filepath = file.getAbsolutePath();
                    if(file_end_name == null || filepath.indexOf(file_end_name) != -1){
                        files.add(file);
                    }
                }
            }
        }
    }

    //获取文件大小，带单位
    public String getSize(double size, int count) {
        String size_type[] = {"b", "KB", "MB", "GB", "TB", "PB"};
        if (size > 1024) {
            double d_size = size / 1024;
            count = count + 1;
            return getSize(d_size, count);
        }
        String sizestr = String.format("%.2f", size) + size_type[count];
        return sizestr;
    }

    public String uriToFilePath(Uri uri, Context context) {
        File file = null;
        if(uri == null) return file.toString();
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            String displayName = System.currentTimeMillis()+ Math.round((Math.random() + 1) * 1000)
                    +"."+ MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri));

//            注释掉的方法可以获取到原文件的文件名，但是比较耗时
//            Cursor cursor = contentResolver.query(uri, null, null, null, null);
//            if (cursor.moveToFirst()) {
//                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));}

            try {
                InputStream is = contentResolver.openInputStream(uri);
                File cache = new File(context.getExternalCacheDir().getAbsolutePath(), displayName);
                FileOutputStream fos = new FileOutputStream(cache);
                file = cache;
                copyFile(is, fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.toString();
    }

    public String fileUriToRawFullPath(Uri uri,String storage){
        String path = uri.getPath();
        String filePath=null;
        if(path.indexOf("tree/primary") != -1){
            filePath = storage + "/" +path.replaceAll("/tree/primary:","");
        }else if(path.indexOf("document/primary") != -1){
            filePath = storage + "/" +path.replaceAll("/document/primary:","");
        }else{
            filePath = path;
        }
        return filePath;
    }

    public String dirUriToRawFullPath(Uri uri,String storage){
        String path = uri.getPath();
        String filePath=null;
        if(path.indexOf("tree/primary") != -1){
            filePath = storage + "/" +path.replaceAll("/tree/primary:","");
        }else if(path.indexOf("document/primary") != -1){
            filePath = storage + "/" +path.replaceAll("/document/primary:","");
            filePath = new File(filePath).getParent();
        }else{
            filePath = new File(path).getParent();
        }
        return filePath;
    }

    public void clearAppFiles(Context context,int uid){
        String local_adb_path = new MyConfigUtils().getCachePathOnXML();
        String storage_path = getSDPath(uid)+"/easyManager";
        for(String s : new String[]{local_adb_path,storage_path}){
            File file = new File(s);
            if(file.isDirectory()){
                deleteFile(s);
            }else{
                file.delete();
            }
        }
    }


}

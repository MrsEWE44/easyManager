package com.easymanager.core.utils;

import android.content.Context;
import android.os.Environment;

import com.easymanager.mylife.startAdbService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    //读取文件
    public String readFileToPath(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    //读取文件
    public Boolean writeDataToPath(String data, String filePath, Boolean isApp) {
        try {
            FileWriter writer = null;
            if (isApp) {
                writer = new FileWriter(filePath, true);
            } else {
                writer = new FileWriter(filePath);
            }
            writer.write(data, 0, data.length());
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //复制文件
    public Boolean copyFile(InputStream is, String outfile) {
        return copyFile(is, new File(outfile));
    }

    //复制文件
    public Boolean copyFile(InputStream is, File outFile) {
        try {
            return copyFile(is, new FileOutputStream(outFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    //复制文件
    public Boolean copyFile(InputStream is, OutputStream os) {
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.close();
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //复制文件
    public Boolean copyFile(String srcFile, String outFile) {
        return copyFile(new File(srcFile), new File(outFile));
    }

    //复制文件
    public Boolean copyFile(File srcFile, File outFile) {
        try {
            return copyFile(new FileInputStream(srcFile), outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getActiveADBScript(Context context){
//                String str = "killall EAMADB\n" +
//                "exec app_process -Djava.class.path=\""+context.getApplicationInfo().sourceDir+"\" /system/bin --nice-name=EAMADB "+ startAdbService.class.getName()+" >>/dev/null 2>&1 &\n" +
//                "echo \"running ok [adb]\"";
        String str = "killall EAMADB\n" +
                "exec app_process -Djava.class.path=\""+context.getApplicationInfo().sourceDir+"\" /system/bin --nice-name=EAMADB "+ startAdbService.class.getName()+"\n" +
                "echo \"running ok [adb]\"";
        return str;
    }

    public Boolean writeActiveADBScript(Context context,String path,String name){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        File file1 = new File(path + "/" + name);
        if(file1.exists()){
            file1.delete();
        }
        return writeDataToPath(getActiveADBScript(context),file1.getPath(),false);
    }


    public String findFiles(File file, String fileName) {
        //定义一个返回值
        String path = "no";
        //如果传进来的是目录，并且存在
        if (file.exists() && file.isDirectory()) {
            //遍历文件夹中的各个文件
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f1 : files) {
                    //如果path的值没有变化
                    if (path.equals("no")) {
                        if (f1.isFile() && f1.getName().contains(fileName)) {
//                            System.out.println(f1.getName());
                            path = f1.getAbsolutePath();
                        } else {
                            path = findFiles(f1, fileName);
                        }
                    } else {;
                        break;//跳出循环，增加性能
                    }

                }
            }
        }
        return path;
    }

    public void deleteFile(String dirPath){
        File file = new File(dirPath);
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            System.out.println("file delete error");
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f: files){
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()){
                deleteFile(f.toString());
            }else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }

}

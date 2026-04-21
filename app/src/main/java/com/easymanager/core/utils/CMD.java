package com.easymanager.core.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;

/**
 *
 * 调用系统命令功能实现
 *
 * */

public class CMD implements Serializable {

    private  static final long serialVersionUID =8365470672561029830L;

    private StringBuilder sb = new StringBuilder();
    private Integer resultCode=-1;

    /**
     * @param cmd 你要執行的命令
     * @param root 是否以root權限運行命令
     * */
    public CMD(String cmd , Boolean root){
        Log.i("cmdstr ::: ",cmd);
        try{
            ProcessBuilder pb;
            if (root) {
                // 尝试使用 --mount-master 进入全局命名空间，这样才能看到完整的 /data/data
                // 如果 su 版本不支持 --mount-master，它会自动忽略或报错，所以我们优先尝试
                pb = new ProcessBuilder("su", "--mount-master", "-c", cmd);
            } else {
                pb = new ProcessBuilder("/system/bin/sh", "-c", cmd);
            }
            // 将错误流重定向到标准输出流，这样可以通过 getInputStream() 获取到所有输出（包括错误信息）
            pb.redirectErrorStream(true);
            processBuilder(pb.start());
        }catch (Exception e){
            // 如果 --mount-master 失败（例如旧版 su），降级使用普通 su
            if (root) {
                try {
                    ProcessBuilder pb = new ProcessBuilder("su", "-c", cmd);
                    pb.redirectErrorStream(true);
                    processBuilder(pb.start());
                } catch (Exception e2) {
                    e2.printStackTrace();
                    sb.append("Exception: ").append(e2.toString()).append("\n");
                }
            } else {
                e.printStackTrace();
                sb.append("Exception: ").append(e.toString()).append("\n");
            }
        }
    }

    private void processBuilder(Process exec) throws Exception {
        processBuilder(exec, "UTF-8");
    }

    private void processBuilder(Process exec, String charset) throws Exception {
        // 读取标准输出和重定向后的错误输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream(), charset));
        String line = "";
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }

        // 尝试读取错误流（如果重定向失败或未生效，这里可以捕获剩余内容）
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(exec.getErrorStream(), charset));
        while ((line = errorReader.readLine()) != null) {
            sb.append(line + "\n");
        }

        resultCode = exec.waitFor();
        reader.close();
        errorReader.close();
    }

    //默认以root身份运行命令
    public CMD(String cmd){
        this(cmd,true);
    }

    //获取执行完命令后的状态码
    public Integer getResultCode(){
        return resultCode;
    }

    //获取命令返回结果
    public String getResult(){
        return sb.toString();
    }

    public String toString(){
        return resultCode+" -- "+sb.toString();
    }

}

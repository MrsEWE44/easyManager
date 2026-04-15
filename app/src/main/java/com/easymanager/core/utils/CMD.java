package com.easymanager.core.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
            Process exec;
            if (root) {
                // 尝试使用 --mount-master 进入全局命名空间，这样才能看到完整的 /data/data
                // 如果 su 版本不支持 --mount-master，它会自动忽略或报错，所以我们优先尝试
                exec = Runtime.getRuntime().exec(new String[]{"su", "--mount-master", "-c", cmd});
            } else {
                exec = Runtime.getRuntime().exec(new String[]{"/system/bin/sh", "-c", cmd});
            }

            processBuilder(exec);
        }catch (Exception e){
            // 如果 --mount-master 失败（例如旧版 su），降级使用普通 su
            if (root) {
                try {
                    Process exec = Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});
                    processBuilder(exec);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
    }

    private void processBuilder(Process exec) throws Exception {
        processBuilder(exec, "UTF-8");
    }

    private void processBuilder(Process exec, String charset) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream(), charset));
        String line = "";
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        resultCode = exec.waitFor();
        reader.close();
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


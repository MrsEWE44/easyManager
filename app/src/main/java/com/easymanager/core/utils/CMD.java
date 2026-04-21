package com.easymanager.core.utils;

import android.util.Log;

import com.easymanager.core.api.DhizukuSystemServerApi;
import com.easymanager.core.api.ShizukuSystemServerApi;
import com.rosan.dhizuku.api.Dhizuku;

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
        Log.i("cmdstr ::: ", cmd);
        try{
            Process exec;
            boolean isShizuku = ShizukuSystemServerApi.isShizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU;
            boolean isDhizuku = DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU;
            if (isDhizuku) {
                exec = Dhizuku.newProcess(new String[]{"sh", "-c", cmd}, null, null);
            } else if (isShizuku) {
                exec = ShizukuSystemServerApi.newProcess(new String[]{"sh", "-c", cmd}, null, null);
            } else {
                // Fallback logic if no runtime mode is set or selected service is not running
                if (isDhizuku) {
                    exec = Dhizuku.newProcess(new String[]{"sh", "-c", cmd}, null, null);
                } else if (isShizuku) {
                    exec = ShizukuSystemServerApi.newProcess(new String[]{"sh", "-c", cmd}, null, null);
                } else {
                    String cmdhead = root ? "su" : "/system/bin/sh";
                    String cmds[] = {cmdhead, "-c", cmd};
                    ProcessBuilder processBuilder = new ProcessBuilder(cmds);
                    processBuilder.redirectErrorStream(true);
                    exec = processBuilder.start();
                    DataOutputStream dos = new DataOutputStream(exec.getOutputStream());
                    dos.writeBytes(cmd + "\n");
                    dos.flush();
                    dos.writeBytes("exit\n");
                    dos.flush();
                }
            }
            
            final BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream(),"UTF-8"));
            final BufferedReader errorReader = new BufferedReader(new InputStreamReader(exec.getErrorStream(),"UTF-8"));
            
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            synchronized (sb) {
                                sb.append(line).append("\n");
                            }
                        }
                    } catch (Exception e) {}
                }
            });
            
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String line;
                        while ((line = errorReader.readLine()) != null) {
                            synchronized (sb) {
                                sb.append(line).append("\n");
                            }
                        }
                    } catch (Exception e) {}
                }
            });
            
            t1.start();
            t2.start();
            
            resultCode = exec.waitFor();
            t1.join();
            t2.join();
            
            reader.close();
            errorReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //默认以root身份运行命令
    public CMD(String cmd){
        this(cmd,false);
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

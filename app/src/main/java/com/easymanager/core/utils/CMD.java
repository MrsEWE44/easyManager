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
        String cmdhead = root ?"su":"/system/bin/sh" ;
        Log.i("cmdstr ::: ",cmd);
        try{
            String cmds[] = {cmdhead,"-c",cmd};
            ProcessBuilder processBuilder = new ProcessBuilder(cmds);
            processBuilder.redirectErrorStream(true);
            Process exec = processBuilder.start();
            DataOutputStream dos  = new DataOutputStream(exec.getOutputStream());
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream(),"UTF-8"));
            String line="";
            while((line=reader.readLine()) != null){
                sb.append(line+"\n");
            }
            resultCode = exec.waitFor();
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
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


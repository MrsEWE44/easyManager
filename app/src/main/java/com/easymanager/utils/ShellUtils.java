package com.easymanager.utils;

import com.easymanager.core.entity.easyManagerClientEntity;
import com.easymanager.core.entity.easyManagerServiceEntity;
import com.easymanager.core.utils.CMD;
import com.easymanager.mylife.adbClient;

import java.io.File;

public class ShellUtils {

    public ShellUtils(){}

    public CMD runADBCmd(String cmdstr){

        easyManagerClientEntity adben2 = new easyManagerClientEntity(cmdstr,null,-1);

        adbClient adbSocketClient2 = new adbClient(adben2,new adbClient.SocketListener() {

            @Override
            public easyManagerServiceEntity getAdbEntity(easyManagerServiceEntity adfb) {
                return null;
            }
        });
        return adbSocketClient2.getAdbEntity().getCmd();
    }

    /**
     * 是否存在su命令，并且有执行权限
     *
     * @return 存在su命令，并且有执行权限返回true
     */
    public boolean isSuEnable() {
        File file = null;
        String[] paths = {"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/", "/su/bin/"};
        try {
            for (String path : paths) {
                file = new File(path + "su");
                if (file.exists() && file.canExecute()) {
                    return testRoot();
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return false;
    }

    public boolean testRoot(){
        CMD cmd = new CMD("id");
        return cmd.getResultCode() == 0;
    }

    public boolean isADB(){
        CMD cmd = runADBCmd("id|grep shell");
        return !cmd.toString().isEmpty();
    }

    public CMD getCMD(String cmdstr,Boolean isRoot){
        if(isRoot){
            return new CMD(cmdstr);
        }else{
            return runADBCmd(cmdstr);
        }
    }

}


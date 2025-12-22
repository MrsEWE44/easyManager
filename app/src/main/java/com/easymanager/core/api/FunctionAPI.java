package com.easymanager.core.api;

import android.app.admin.IDevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

import com.easymanager.core.server.Singleton;
import com.easymanager.core.server.easyManagerBinderWrapper;
import com.easymanager.core.server.easyManagerPortService;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionAPI extends baseAPI implements Serializable {

    public static final int STATE_USER_SETUP_FINALIZED = 3;

    private static final Map<String, IDevicePolicyManager> I_DEVICE_POLICY_MANAGER_CACHE = new HashMap<>();


    public IDevicePolicyManager getIDevicePolicyManager(){
        IDevicePolicyManager iDevicePolicyManager = I_DEVICE_POLICY_MANAGER_CACHE.get("idpmservice");
        if(iDevicePolicyManager != null && iDevicePolicyManager.asBinder().isBinderAlive()){
            return iDevicePolicyManager;
        }
        Singleton<IDevicePolicyManager> iUserManagerSingleton = new Singleton<IDevicePolicyManager>() {
            @Override
            protected IDevicePolicyManager create() {
                return IDevicePolicyManager.Stub.asInterface(new easyManagerBinderWrapper(easyManagerPortService.getSystemService(Context.DEVICE_POLICY_SERVICE)));
            }
        };
        iDevicePolicyManager = iUserManagerSingleton.get();
        I_DEVICE_POLICY_MANAGER_CACHE.put("idpmservice",iDevicePolicyManager);
        return iDevicePolicyManager;
    }

    //暂时有问题,不再调用2025年12月21日17点03分
    public void setDeviceOwner(String componentstr , int userid){
        System.out.println("setDeviceOwner ::: " + componentstr + " --- " + userid);
        IDevicePolicyManager iDevicePolicyManager = getIDevicePolicyManager();
        boolean isAdminAdded = false;
        StringWriter sw = new StringWriter();
        ComponentName component = ComponentName.unflattenFromString(componentstr);
        System.out.println("is ComponentName ..."+(component == null));
        if(component != null){
            try {
                System.out.println("start setActiveAdmin ...");
                iDevicePolicyManager.setActiveAdmin(component,false,userid,null);
                System.out.println("start setActiveAdmin end ...");
                isAdminAdded = true;
                System.out.println("is added .... ");
            }catch (Exception  e){
                System.out.println("no added");
                e.printStackTrace(new PrintWriter(sw));
                System.out.println(sw);
//                e.printStackTrace();
            }

            try {
                if(!iDevicePolicyManager.setDeviceOwner(component,userid,false)){
                    System.err.println("Can't set owner.... ");
                }
                System.out.println("is set device owner");
            }catch (Exception e){
                System.out.println("is not set device owner");
                if(isAdminAdded){
                    iDevicePolicyManager.removeActiveAdmin(component,userid);
                    System.out.println("is removeActiveAdmin");
                }

                e.printStackTrace(new PrintWriter(sw));
                System.out.println(sw);
                e.printStackTrace();
            }

            iDevicePolicyManager.setUserProvisioningState(STATE_USER_SETUP_FINALIZED,userid);
            System.out.println("is setUserProvisioningState ");
        }
    }

    public void removeActiveDeviceOwner(String componentstr, int userid){
        IDevicePolicyManager iDevicePolicyManager = getIDevicePolicyManager();
        ComponentName component = ComponentName.unflattenFromString(componentstr);
        iDevicePolicyManager.forceRemoveActiveAdmin(component,userid);
    }


    public List<String> getActiveAdmins(int userid){
        ArrayList<String> strings = new ArrayList<>();
        IDevicePolicyManager iDevicePolicyManager = getIDevicePolicyManager();
        List<ComponentName> activeAdmins = iDevicePolicyManager.getActiveAdmins(userid);
        if(activeAdmins != null && activeAdmins.size() > 0){
            for (ComponentName activeAdmin : activeAdmins) {
                strings.add(activeAdmin.flattenToShortString());
            }
        }
        return strings;
    }


}

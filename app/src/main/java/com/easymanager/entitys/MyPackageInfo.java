package com.easymanager.entitys;

import java.io.Serializable;
import java.util.Arrays;

public class MyPackageInfo implements Serializable {

    public MyPackageInfo(String packageName, int versionCode, String versionName, MyApplicationInfo myapplicationInfo, MyActivityInfo[] myActivityInfos, MyActivityInfo[] myServices, MyActivityInfo[] myReceivers, String[] requestedPermissions) {
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.myapplicationInfo = myapplicationInfo;
        this.myActivityInfos = myActivityInfos;
        this.myServices = myServices;
        this.myReceivers = myReceivers;
        this.requestedPermissions = requestedPermissions;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public MyApplicationInfo getMyapplicationInfo() {
        return myapplicationInfo;
    }

    public void setMyapplicationInfo(MyApplicationInfo myapplicationInfo) {
        this.myapplicationInfo = myapplicationInfo;
    }

    public MyActivityInfo[] getMyActivityInfos() {
        return myActivityInfos;
    }

    public void setMyActivityInfos(MyActivityInfo[] myActivityInfos) {
        this.myActivityInfos = myActivityInfos;
    }

    public MyActivityInfo[] getMyServices() {
        return myServices;
    }

    public void setMyServices(MyActivityInfo[] myServices) {
        this.myServices = myServices;
    }

    public MyActivityInfo[] getMyReceivers() {
        return myReceivers;
    }

    public void setMyReceivers(MyActivityInfo[] myReceivers) {
        this.myReceivers = myReceivers;
    }

    public String[] getRequestedPermissions() {
        return requestedPermissions;
    }

    public void setRequestedPermissions(String[] requestedPermissions) {
        this.requestedPermissions = requestedPermissions;
    }

    @Override
    public String toString() {
        return "MyPackageInfo{" +
                "packageName='" + packageName + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", myapplicationInfo=" + myapplicationInfo +
                ", myActivityInfos=" + Arrays.toString(myActivityInfos) +
                ", myServices=" + Arrays.toString(myServices) +
                ", myReceivers=" + Arrays.toString(myReceivers) +
                ", requestedPermissions=" + Arrays.toString(requestedPermissions) +
                '}';
    }

    public String packageName;
    public int versionCode;
    public String versionName;
    public MyApplicationInfo myapplicationInfo;

    public MyActivityInfo[] myActivityInfos,myServices,myReceivers;

    public String[] requestedPermissions;


}

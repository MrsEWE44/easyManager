package com.easymanager.core.server;

import android.content.pm.PackageManager;
import android.os.RemoteException;

import com.easymanager.BuildConfig;
import com.rosan.dhizuku.api.Dhizuku;
import com.rosan.dhizuku.api.DhizukuRequestPermissionListener;

import rikka.shizuku.Shizuku;
import rikka.sui.Sui;

public class easyManagerBinderWrapper {

    public static boolean isShizuku(){
        Sui.init(BuildConfig.APPLICATION_ID);
        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            int requestCode1 = 6667;
            Shizuku.OnRequestPermissionResultListener listener = new Shizuku.OnRequestPermissionResultListener() {
                @Override
                public void onRequestPermissionResult(int requestCode, int grantResult) {
                    if(requestCode1 != requestCode){
                        return;
                    }else{
                        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED){
                            System.out.println("sui/shizuku permission accept");
                        } else {
                            System.out.println("sui/shizuku permission denied");
                        }
                    }
                }
            };
            Shizuku.addRequestPermissionResultListener(listener);
            try {
                Shizuku.requestPermission(requestCode1);
            }catch (Exception e){
                Shizuku.removeRequestPermissionResultListener(listener);
            }


        }


        return false;
    }

    public static boolean isDhizuku(){
        Dhizuku.init();
        if(Dhizuku.isPermissionGranted()){
            return true;
        }else{
            Dhizuku.requestPermission(new DhizukuRequestPermissionListener() {
                @Override
                public void onRequestPermission(int grantResult) throws RemoteException {
                    if (grantResult == PackageManager.PERMISSION_GRANTED){
                        System.out.println("dhizuku is granted");
                    }else{
                        System.out.println("dhizuku is not granted");
                    }
                }
            });
        }
        return false;
    }

}

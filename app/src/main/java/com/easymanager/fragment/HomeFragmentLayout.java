package com.easymanager.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.easymanager.R;
import com.easymanager.activitys.AppCloneLayoutActivity;
import com.easymanager.activitys.AppManagerLayoutActivity;
import com.easymanager.activitys.CreateImgLayoutActivity;
import com.easymanager.activitys.FileSharedLayoutActivity;
import com.easymanager.activitys.RunCommandLayoutActivity;
import com.easymanager.activitys.UsbModeLayoutActivity;
import com.easymanager.enums.AppManagerEnum;

public class HomeFragmentLayout extends Fragment {

    private Button hflinlocalapk,hflappmanagerbt;
    private Button hflbackupapp,hflrestoryapp;
    private Button hflappclone,hflappclonemanage,hflappcloneremove;
    private Button hflmountlocalimg,hflcreateimg;
    private Button hflsetrate,hfldelx,hflsetntp,hflfileshared,hflruncmd;

    private int uid;

    public HomeFragmentLayout(){}

    public HomeFragmentLayout(Boolean isRoot, Boolean isADB, int uid) {
        this.isRoot = isRoot;
        this.isADB = isADB;
        this.uid = uid;
    }

    private Boolean isRoot,isADB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.home_fragment_layout, container, false);
        initBt(inflate);

        return inflate;
    }

    private void initBt(View view){
        hflinlocalapk = view.findViewById(R.id.hflinlocalapk);
        hflbackupapp = view.findViewById(R.id.hflbackupapp);
        hflrestoryapp = view.findViewById(R.id.hflrestoryapp);
        hflmountlocalimg = view.findViewById(R.id.hflmountlocalimg);
        hflcreateimg = view.findViewById(R.id.hflcreateimg);
        hflsetrate = view.findViewById(R.id.hflsetrate);
        hfldelx = view.findViewById(R.id.hfldelx);
        hflsetntp = view.findViewById(R.id.hflsetntp);
        hflfileshared = view.findViewById(R.id.hflfileshared);
        hflappclone = view.findViewById(R.id.hflappclone);
        hflappclonemanage = view.findViewById(R.id.hflappclonemanage);
        hflappcloneremove = view.findViewById(R.id.hflappcloneremove);
        hflappmanagerbt = view.findViewById(R.id.hflappmanagerbt);
        hflruncmd = view.findViewById(R.id.hflruncmd);
        btClick();
    }

    private void btClick(){

        hflinlocalapk.setOnClickListener(clickListener);
        hflbackupapp.setOnClickListener(clickListener);
        hflrestoryapp.setOnClickListener(clickListener);
        hflmountlocalimg.setOnClickListener(clickListener);
        hflcreateimg.setOnClickListener(clickListener);
        hflsetrate.setOnClickListener(clickListener);
        hfldelx.setOnClickListener(clickListener);
        hflsetntp.setOnClickListener(clickListener);
        hflfileshared.setOnClickListener(clickListener);
        hflappclone.setOnClickListener(clickListener);
        hflappclonemanage.setOnClickListener(clickListener);
        hflappcloneremove.setOnClickListener(clickListener);
        hflappmanagerbt.setOnClickListener(clickListener);
        hflruncmd.setOnClickListener(clickListener);
        initBtColor();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            hflappclonemanage.setEnabled(false);
            hflappcloneremove.setEnabled(false);
            hflappclone.setEnabled(false);
        }
    }

    private void initBtColor(){

        setBtColor(hflinlocalapk,true,true);
        setBtColor(hflbackupapp,true,false);
        setBtColor(hflrestoryapp,true,false);
        setBtColor(hflmountlocalimg,true,false);
        setBtColor(hflcreateimg,true,false);
        setBtColor(hflsetrate,true,true);
        setBtColor(hfldelx,true,true);
        setBtColor(hflsetntp,true,true);
        setBtColor(hflfileshared,false,false);
        setBtColor(hflappclone,true,true);
        setBtColor(hflappclonemanage,true,true);
        setBtColor(hflappcloneremove,true,true);
        setBtColor(hflappmanagerbt,true,true);
        setBtColor(hflruncmd,false,false);

    }

    private void jump(int mode){
        Class clazz = AppManagerLayoutActivity.class;

        if(mode == AppManagerEnum.RUN_CMD){
            clazz = RunCommandLayoutActivity.class;
        }

        if(mode == AppManagerEnum.FILE_SHARED){
            clazz = FileSharedLayoutActivity.class;
        }
        if(mode == AppManagerEnum.CREATE_IMG || mode == AppManagerEnum.SET_RATE || mode == AppManagerEnum.SET_NTP || mode == AppManagerEnum.DEL_X){
            clazz = CreateImgLayoutActivity.class;
        }

        if(mode == AppManagerEnum.MOUNT_LOCAL_IMG){
            clazz = UsbModeLayoutActivity.class;
        }
        if(mode == AppManagerEnum.APP_CLONE || mode == AppManagerEnum.APP_CLONE_REMOVE || mode == AppManagerEnum.APP_CLONE_MANAGE){
            clazz = AppCloneLayoutActivity.class;
        }
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra("mode",mode);
        intent.putExtra("isRoot",isRoot);
        intent.putExtra("isADB",isADB);
        intent.putExtra("uid",uid);
        startActivity(intent);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if(id == R.id.hflinlocalapk){
                jump(AppManagerEnum.APP_INSTALL_LOCAL_FILE);
            }

            if(id == R.id.hflbackupapp){
                jump(AppManagerEnum.APP_BACKUP);
            }
            if(id == R.id.hflrestoryapp){
                jump(AppManagerEnum.APP_RESTORY);
            }

            if(id == R.id.hflmountlocalimg){
                jump(AppManagerEnum.MOUNT_LOCAL_IMG);
            }

            if(id == R.id.hflcreateimg){
                jump(AppManagerEnum.CREATE_IMG);
            }

            if(id == R.id.hflsetntp){
                jump(AppManagerEnum.SET_NTP);
            }

            if(id == R.id.hflsetrate){
                jump(AppManagerEnum.SET_RATE);
            }

            if(id == R.id.hfldelx){
                jump(AppManagerEnum.DEL_X);
            }

            if(id == R.id.hflfileshared){
                jump(AppManagerEnum.FILE_SHARED);
            }

            if(id == R.id.hflappclone){
                jump(AppManagerEnum.APP_CLONE);
            }

            if(id == R.id.hflappclonemanage){
                jump(AppManagerEnum.APP_CLONE_MANAGE);
            }

            if(id == R.id.hflappcloneremove){
                jump(AppManagerEnum.APP_CLONE_REMOVE);
            }

            if(id == R.id.hflappmanagerbt){
                jump(AppManagerEnum.APP_MANAGER);
            }

            if(id == R.id.hflruncmd){
                jump(AppManagerEnum.RUN_CMD);
            }


        }
    };

    private void setBtColor(Button b , boolean needRoot , boolean needADB){
        if(isADB != null && isADB){
            if(needRoot && needADB){
                b.setBackgroundColor(Color.YELLOW);
            }

            if(needRoot && !needADB){
                b.setBackgroundColor(Color.RED);
                b.setEnabled(false);
            }

        }

        if(isRoot != null && !isRoot && isADB != null && !isADB){
            if(needADB || needRoot){
               b.setBackgroundColor(Color.RED);
               b.setEnabled(false);
            }
        }




    }


}





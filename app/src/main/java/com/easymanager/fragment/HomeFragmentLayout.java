package com.easymanager.fragment;

import androidx.fragment.app.Fragment;
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
import com.easymanager.core.api.DhizukuSystemServerApi;
import com.easymanager.core.api.ShizukuSystemServerApi;
import com.easymanager.enums.AppManagerEnum;
import com.easymanager.utils.OtherTools;

import com.google.android.material.appbar.MaterialToolbar;

public class HomeFragmentLayout extends Fragment {

    private Button hflinlocalapk,hflpermissionbt,hfldisablebt,hfldumpbt,hfluninstallbt,hflcleanbt,hflrestorebt;
    //    private Button hflappclone,hflappclonemanage,hflappcloneremove;
    private Button hflsetrate,hfldelx,hflsetntp,hflfileshared,hflruncmd;

    private int uid;

    private OtherTools ot = new OtherTools();


    public HomeFragmentLayout(){}

    public HomeFragmentLayout(Boolean isShizuku, Boolean isDhizuku ,int uid) {
        this.isShizuku = isShizuku != null && (isShizuku && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU);
        this.isDhizuku = isDhizuku != null && (isDhizuku && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU);
        this.uid = uid;
    }

    private boolean isShizuku = false, isDhizuku = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.home_fragment_layout, container, false);
        MaterialToolbar toolbar = inflate.findViewById(R.id.toolbar);
        if (toolbar != null) {
            String modeText = "[ General ]";
            if (isShizuku) {
                modeText = "[ SHIZUKU ]";
            } else if (isDhizuku) {
                modeText = "[ DHIZUKU ]";
            }
            toolbar.setTitle("easyManager " + modeText);
        }
        initBt(inflate);
        return inflate;
    }

    public void updateAuthStatus(Boolean isShizuku, Boolean isDhizuku) {
        this.isShizuku = isShizuku != null && isShizuku && ShizukuSystemServerApi.isShizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU;
        this.isDhizuku = isDhizuku != null && isDhizuku && DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU;
        if (getView() != null) {
            MaterialToolbar toolbar = getView().findViewById(R.id.toolbar);
            if (toolbar != null) {
                String modeText = "[ General ]";
                if (this.isShizuku) {
                    modeText = "[ SHIZUKU ]";
                } else if (this.isDhizuku) {
                    modeText = "[ DHIZUKU ]";
                }
                toolbar.setTitle("easyManager " + modeText);
            }
            initBtColor();
            // Re-run btClick logic to update enabled state
            btClick();
        }
    }

    private void initBt(View view){
        hflinlocalapk = view.findViewById(R.id.hflinlocalapk);
        hflsetrate = view.findViewById(R.id.hflsetrate);
        hfldelx = view.findViewById(R.id.hfldelx);
        hflsetntp = view.findViewById(R.id.hflsetntp);
        hflfileshared = view.findViewById(R.id.hflfileshared);
//        hflappclone = view.findViewById(R.id.hflappclone);
//        hflappclonemanage = view.findViewById(R.id.hflappclonemanage);
//        hflappcloneremove = view.findViewById(R.id.hflappcloneremove);
        hflpermissionbt = view.findViewById(R.id.hflpermissionbt);
        hfldisablebt = view.findViewById(R.id.hfldisablebt);
        hfldumpbt = view.findViewById(R.id.hfldumpbt);
        hfluninstallbt = view.findViewById(R.id.hfluninstallbt);
        hflcleanbt = view.findViewById(R.id.hflcleanbt);
        hflrestorebt = view.findViewById(R.id.hflrestorebt);
        hflruncmd = view.findViewById(R.id.hflruncmd);
        btClick();
    }

    private void btClick(){

        hflinlocalapk.setOnClickListener(clickListener);
        hflsetrate.setOnClickListener(clickListener);
        hfldelx.setOnClickListener(clickListener);
        hflsetntp.setOnClickListener(clickListener);
        hflfileshared.setOnClickListener(clickListener);
//        hflappclone.setOnClickListener(clickListener);
//        hflappclonemanage.setOnClickListener(clickListener);
//        hflappcloneremove.setOnClickListener(clickListener);
        hflpermissionbt.setOnClickListener(clickListener);
        hfldisablebt.setOnClickListener(clickListener);
        hfldumpbt.setOnClickListener(clickListener);
        hfluninstallbt.setOnClickListener(clickListener);
        hflcleanbt.setOnClickListener(clickListener);
        hflrestorebt.setOnClickListener(clickListener);
        hflruncmd.setOnClickListener(clickListener);
        initBtColor();

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
            hflsetrate.setEnabled(false);
        }

//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
//            hflappclonemanage.setEnabled(false);
//            hflappcloneremove.setEnabled(false);
//            hflappclone.setEnabled(false);
//        }

        // 功能随激活模式动态变化
        if (isShizuku && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU) {
            // Shizuku 模式下的功能可用性
            hflsetrate.setEnabled(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P);
//            hflappclone.setEnabled(true);
//            hflappclonemanage.setEnabled(true);
//            hflappcloneremove.setEnabled(true);
            hfldelx.setEnabled(true);
            hflsetntp.setEnabled(true);
            hflinlocalapk.setEnabled(true);
            hflpermissionbt.setEnabled(true);
            hfldisablebt.setEnabled(true);
            hfldumpbt.setEnabled(true);
            hfluninstallbt.setEnabled(true);
            hflcleanbt.setEnabled(true);
            hflrestorebt.setEnabled(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O);
        } else if (isDhizuku && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU) {
            // Dhizuku 模式下的功能限制 (根据 API 能力调整)
            hflsetrate.setEnabled(false);
//            hflappclone.setEnabled(false);
//            hflappclonemanage.setEnabled(false);
//            hflappcloneremove.setEnabled(false);
            hfldelx.setEnabled(false);
            hflsetntp.setEnabled(false);
            hflinlocalapk.setEnabled(true);
            hflpermissionbt.setEnabled(false);
            hfldisablebt.setEnabled(true);
            hfldumpbt.setEnabled(true);
            hfluninstallbt.setEnabled(true);
            hflcleanbt.setEnabled(false);
            hflrestorebt.setEnabled(true);
        } else {
            // 未激活任何模式，基本全部禁用
            hflsetrate.setEnabled(false);
//            hflappclone.setEnabled(false);
//            hflappclonemanage.setEnabled(false);
//            hflappcloneremove.setEnabled(false);
            hfldelx.setEnabled(false);
            hflsetntp.setEnabled(false);
            hflinlocalapk.setEnabled(false);
            hflpermissionbt.setEnabled(false);
            hfldisablebt.setEnabled(false);
            hfldumpbt.setEnabled(false);
            hfluninstallbt.setEnabled(false);
            hflcleanbt.setEnabled(false);
            hflrestorebt.setEnabled(false);
        }

    }

    private void initBtColor(){

        ot.setBtColor(hflinlocalapk,true,true,isShizuku,isDhizuku);
        ot.setBtColor(hflsetrate,true,false,isShizuku,isDhizuku);
        ot.setBtColor(hfldelx,true,false,isShizuku,isDhizuku);
        ot.setBtColor(hflsetntp,true,false,isShizuku,isDhizuku);
        ot.setBtColor(hflfileshared,false,false,isShizuku,isDhizuku);
//        ot.setBtColor(hflappclone,true,false,isShizuku,isDhizuku);
//        ot.setBtColor(hflappclonemanage,true,false,isShizuku,isDhizuku);
//        ot.setBtColor(hflappcloneremove,true,false,isShizuku,isDhizuku);
        ot.setBtColor(hflpermissionbt,true,true,isShizuku,isDhizuku);
        ot.setBtColor(hfldisablebt,true,true,isShizuku,isDhizuku);
        ot.setBtColor(hfldumpbt,true,true,isShizuku,isDhizuku);
        ot.setBtColor(hfluninstallbt,true,true,isShizuku,isDhizuku);
        ot.setBtColor(hflcleanbt,true,true,isShizuku,isDhizuku);
        ot.setBtColor(hflrestorebt,true,true,isShizuku,isDhizuku);
        ot.setBtColor(hflruncmd,false,false,isShizuku,isDhizuku);

    }

    private void jump(int mode){
        Class clazz = AppManagerLayoutActivity.class;

        if(mode == AppManagerEnum.RUN_CMD){
            clazz = RunCommandLayoutActivity.class;
        }

        if(mode == AppManagerEnum.FILE_SHARED){
            clazz = FileSharedLayoutActivity.class;
        }

        if(mode == AppManagerEnum.SET_RATE || mode == AppManagerEnum.SET_NTP || mode == AppManagerEnum.DEL_X){
            clazz = CreateImgLayoutActivity.class;
        }

        if(mode == AppManagerEnum.APP_CLONE || mode == AppManagerEnum.APP_CLONE_REMOVE || mode == AppManagerEnum.APP_CLONE_MANAGE){
            clazz = AppCloneLayoutActivity.class;
        }

        ot.jump(getActivity(),clazz,mode,isShizuku,isDhizuku,uid);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if(id == R.id.hflinlocalapk){
                jump(AppManagerEnum.APP_INSTALL_LOCAL_FILE);
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

//            if(id == R.id.hflappclone){
//                jump(AppManagerEnum.APP_CLONE);
//            }
//
//            if(id == R.id.hflappclonemanage){
//                jump(AppManagerEnum.APP_CLONE_MANAGE);
//            }
//
//            if(id == R.id.hflappcloneremove){
//                jump(AppManagerEnum.APP_CLONE_REMOVE);
//            }

            if(id == R.id.hflpermissionbt){
                jump(AppManagerEnum.APP_PERMISSION);
            }

            if(id == R.id.hfldisablebt){
                jump(AppManagerEnum.APP_DISABLE_COMPENT);
            }

            if(id == R.id.hfldumpbt){
                jump(AppManagerEnum.APP_DUMP);
            }

            if(id == R.id.hfluninstallbt){
                jump(AppManagerEnum.APP_UNINSTALL);
            }

            if(id == R.id.hflcleanbt){
                jump(AppManagerEnum.APP_CLEAN_PROCESS);
            }

            if(id == R.id.hflrestorebt){
                jump(AppManagerEnum.APP_RESTORE_UNINSTALLED);
            }

            if(id == R.id.hflruncmd){
                jump(AppManagerEnum.RUN_CMD);
            }


        }
    };

}





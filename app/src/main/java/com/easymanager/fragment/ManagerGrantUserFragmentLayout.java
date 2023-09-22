package com.easymanager.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.adapters.ManagerGrantUserAdapter;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.utils.DialogUtils;
import com.easymanager.utils.PackageUtils;
import com.easymanager.utils.easyManagerUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManagerGrantUserFragmentLayout extends Fragment {

    private ArrayList<PKGINFO> pkginfos  =new ArrayList<>();
    private ArrayList<Boolean> swibts = new ArrayList<>();

    private Boolean isRoot, isADB;
    private TextView mguflpeekservice;
    private ListView mgufllv;

    public ManagerGrantUserFragmentLayout(Boolean isRoot, Boolean isADB) {
        this.isRoot = isRoot;
        this.isADB = isADB;
    }

    public ManagerGrantUserFragmentLayout(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vie = inflater.inflate(R.layout.manager_grant_user_fragment_layout,container,false);
        initBt(vie);
        return vie;
    }

    private void initBt(View vie) {
        mguflpeekservice = vie.findViewById(R.id.mguflpeekservice);
        mgufllv = vie.findViewById(R.id.mgufllv);
        easyManagerUtils ee = new easyManagerUtils();
        mguflpeekservice.setText("当前服务 " +(ee.getServerStatus()?"正在运行"+(ee.isROOT()?" [ ROOT ] ":" [ ADB ] ")+"...":"还没启动"));
        if(ee.getServerStatus()){
            Context context = getActivity().getApplicationContext();
            PackageUtils pkgutils = new PackageUtils();
            DialogUtils du  = new DialogUtils();
            ProgressDialog show = du.showMyDialog(getActivity(),"正在读取授权信息");
            Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what==0){
                        show.dismiss();
                        ManagerGrantUserAdapter managerGrantUserAdapter = new ManagerGrantUserAdapter(pkginfos, swibts, context);
                        mgufllv.setAdapter(managerGrantUserAdapter);
                    }
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HashMap<String,Integer> grantUsers = ee.getGrantUsers(context);
                    for (Map.Entry<String, Integer> entry : grantUsers.entrySet()) {
                        try {
                            pkginfos.add(pkgutils.getPKGINFO(context,entry.getKey()));
                            swibts.add(entry.getValue()==0);
                        }catch (Exception e){

                        }
                    }
                    du.sendHandlerMSG(handler,0);
                }
            }).start();

        }



    }
}

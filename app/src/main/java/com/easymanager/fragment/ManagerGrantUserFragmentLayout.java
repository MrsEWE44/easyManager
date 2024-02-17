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
import com.easymanager.utils.base.DialogUtils;
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
        PackageUtils pkgutils = new PackageUtils();
        DialogUtils du  = new DialogUtils();
        Context context = getActivity().getApplicationContext();
        mguflpeekservice = vie.findViewById(R.id.mguflpeekservice);
        mgufllv = vie.findViewById(R.id.mgufllv);
        easyManagerUtils ee = new easyManagerUtils();
        String msg = String.format(du.tu.getLanguageString(context,R.string.now_server_check_msg),(ee.getServerStatus()?String.format(du.tu.getLanguageString(context,R.string.now_server_check_running_msg),(ee.isROOT()?" [ ROOT ] ":" [ ADB ] ")):du.tu.getLanguageString(context,R.string.now_server_check_dead_msg)));
        mguflpeekservice.setText(msg);
        if(ee.getServerStatus()){
            ProgressDialog show = du.showMyDialog(getActivity(),du.tu.getLanguageString(context,R.string.now_server_read_auth_msg));
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

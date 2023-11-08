package com.easymanager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


import com.easymanager.R;
import com.easymanager.core.api.PackageAPI;
import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.enums.AppInfoEnums;
import com.easymanager.utils.easyManagerUtils;

import java.util.ArrayList;

public class AppInfoAdapter extends BaseAdapter {


    public AppInfoAdapter(ArrayList<String> list, Context context, ArrayList<Boolean> checkboxs, ArrayList<Boolean> switbs, String pkgname, int mode,String uid) {
        this.list = (ArrayList<String>) list.clone();
        this.context = context;
        this.checkboxs = checkboxs;
        this.switbs = switbs;
        this.pkgname = pkgname;
        this.mode = mode;
        notifyDataSetChanged();
    }

    private ArrayList<String> list;
    private Context context;
    private ArrayList<Boolean> checkboxs,switbs;
    private String pkgname,uid ;
    private int mode;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.app_info_item_layout,null);
        CheckBox checkBox=view.findViewById(R.id.aiilcb1);
        TextView text = view.findViewById(R.id.aiiltv1);
        Switch switchbbb = view.findViewById(R.id.aiilsb1);
        text.setText(list.get(i));
        switchbbb.setChecked(switbs.get(i));
        switchbbb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switbs.set(i,b);
                switchbbb.setChecked(switbs.get(i));
                String pkgcate = list.get(i);
//                CMD cmd = new CMD(new appopsCmdStr().getRunAppopsBySwtichCMD(b, mode, pkgname, pkgcate, uid));
                easyManagerUtils eu = new easyManagerUtils();
                if(mode == AppInfoEnums.IS_PERMISSION){
                    TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, pkgcate, context.getPackageName(), 0);
                    if(b){
                        eu.grantRuntimePermission(transmissionEntity);
                    }else {
                        eu.revokeRuntimePermission(transmissionEntity);
                    }
                }
                if(mode == AppInfoEnums.IS_COMPENT_OR_PACKAGE){
                    eu.setComponentOrPackageEnabledState(new TransmissionEntity(pkgname+"/"+pkgcate,pkgcate, context.getPackageName(), b? PackageAPI.COMPONENT_ENABLED_STATE_ENABLED:PackageAPI.COMPONENT_ENABLED_STATE_DISABLED));
                }

            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkboxs.set(i,b);
            }
        });
        checkBox.setChecked(checkboxs.get(i));
        return view;
    }
}


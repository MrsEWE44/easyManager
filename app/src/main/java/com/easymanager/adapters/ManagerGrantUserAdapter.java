package com.easymanager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.utils.FileTools;
import com.easymanager.utils.easyManagerUtils;

import java.util.ArrayList;

public class ManagerGrantUserAdapter extends BaseAdapter {
    public ManagerGrantUserAdapter(ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> switbs, Context context) {
        this.pkginfos = (ArrayList<PKGINFO>) pkginfos.clone();
        this.switbs = (ArrayList<Boolean>) switbs.clone();
        this.context = context;
        notifyDataSetChanged();
    }

    private ArrayList<PKGINFO> pkginfos;
    private ArrayList<Boolean> switbs;
    private Context context;

    @Override
    public int getCount() {
        return pkginfos.size();
    }

    @Override
    public Object getItem(int i) {
        return pkginfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.manager_grant_user_item_layout,null);
        ImageView mguilappicon = convertView.findViewById(R.id.mguilappicon);
        TextView mguilappname = convertView.findViewById(R.id.mguilappname);
        TextView mguilapppkgname = convertView.findViewById(R.id.mguilapppkgname);
        TextView mguilappversion = convertView.findViewById(R.id.mguilappversion);
        TextView mguilappsize = convertView.findViewById(R.id.mguilappsize);
        TextView mguilappuid = convertView.findViewById(R.id.mguilappuid);
        Switch mguilsb = convertView.findViewById(R.id.mguilsb);
        int size = pkginfos.size();
        if(position < size){
            PKGINFO pkginfo = pkginfos.get(position);
            mguilappname.setText(pkginfo.getAppname());
            mguilapppkgname.setText(pkginfo.getPkgname());
            mguilappversion.setText(pkginfo.getAppversionname());
            mguilappsize.setText(new FileTools().getSize(pkginfo.getFilesize(),0));
            mguilappuid.setText(pkginfo.getApkuid());
            mguilappicon.setImageDrawable(pkginfos.get(position).getAppicon());
            mguilsb.setChecked(switbs.get(position));
            mguilsb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    switbs.set(position,b);
                    mguilsb.setChecked(switbs.get(position));
                    new easyManagerUtils().changeGrantUserState(pkginfo.getPkgname());
                }
            });
        }
        return convertView;
    }
}

package com.easymanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.utils.FileTools;
import com.easymanager.utils.PackageUtils;

import java.util.ArrayList;


public class PKGINFOAdapter extends BaseAdapter {

    public PKGINFOAdapter(ArrayList<PKGINFO> pkginfos, Context context, ArrayList<Boolean> checkboxs, PackageUtils pu) {
        this.pkginfos = (ArrayList<PKGINFO>) pkginfos.clone();
        this.context = context;
        this.checkboxs = checkboxs;
        this.pu = pu;
        notifyDataSetChanged();
    }

    private ArrayList<PKGINFO> pkginfos;
    private Context context;
    private ArrayList<Boolean> checkboxs;
    private PackageUtils pu;


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
        convertView = LayoutInflater.from(context).inflate(R.layout.app_list_view_item_layout,null);
        ImageView imageView = convertView.findViewById(R.id.alvilappicon);
        CheckBox checkBox=convertView.findViewById(R.id.alvilcb1);
        TextView text = convertView.findViewById(R.id.alvilappname);
        TextView text2 = convertView.findViewById(R.id.alvilapppkgname);
        TextView text3 = convertView.findViewById(R.id.alvilappversion);
        TextView text4 = convertView.findViewById(R.id.alvilappsize);
        TextView text5 = convertView.findViewById(R.id.alvilappuid);
        int size = pkginfos.size();
        int size1 = checkboxs.size();
        if(size == size1 && position < size && size > 0 ){
            PKGINFO pkginfo = pkginfos.get(position);
            text.setText(pkginfo.getAppname());
            text2.setText(pkginfo.getPkgname());
            text3.setText(pkginfo.getAppversionname());
            text4.setText(new FileTools().getSize(pkginfo.getFilesize(),0));
            text5.setText(pkginfo.getApkuid());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    checkboxs.set(position,b);
                }
            });
            checkBox.setChecked(checkboxs.get(position));
            int mode=  pkginfo.getIconmode();
            if(mode == 0){
                imageView.setImageDrawable(pu.getPKGIcon(context,pkginfo.getPkgname()));
            }
            if(mode == 1){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.manager_grant_app_foreground));
            }
            if(mode == 2){
                imageView.setImageDrawable(pu.getPKGFileIcon(context,pkginfo.getApkpath()));
            }

            notifyDataSetChanged();
        }
        return convertView;
    }

}

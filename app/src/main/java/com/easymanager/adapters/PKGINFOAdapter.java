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
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.utils.FileTools;

import java.util.ArrayList;


public class PKGINFOAdapter extends BaseAdapter {

    public PKGINFOAdapter(ArrayList<PKGINFO> pkginfos, Context context, ArrayList<Boolean> checkboxs) {
        this.pkginfos = (ArrayList<PKGINFO>) pkginfos.clone();
        this.context = context;
        this.checkboxs = (ArrayList<Boolean>) checkboxs.clone();
        notifyDataSetChanged();
    }

    private ArrayList<PKGINFO> pkginfos;
    private Context context;
    private ArrayList<Boolean> checkboxs;

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
        if(position < size){
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
            imageView.setImageDrawable(pkginfos.get(position).getAppicon());
            notifyDataSetChanged();
        }
        return convertView;
    }

}

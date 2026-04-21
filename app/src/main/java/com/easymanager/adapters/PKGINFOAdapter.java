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

import com.google.android.material.checkbox.MaterialCheckBox;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.app_list_view_item_layout, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.alvilappicon);
            holder.checkBox = convertView.findViewById(R.id.alvilcb1);
            holder.text = convertView.findViewById(R.id.alvilappname);
            holder.text2 = convertView.findViewById(R.id.alvilapppkgname);
            holder.text3 = convertView.findViewById(R.id.alvilappversion);
            holder.text4 = convertView.findViewById(R.id.alvilappsize);
            holder.text5 = convertView.findViewById(R.id.alvilappuid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int size = pkginfos.size();
        int size1 = checkboxs.size();
        if (size == size1 && position < size && size > 0) {
            PKGINFO pkginfo = pkginfos.get(position);
            holder.text.setText(pkginfo.getAppname());
            holder.text2.setText(pkginfo.getPkgname());
            holder.text3.setText(pkginfo.getAppversionname());
            holder.text4.setText(new FileTools().getSize(pkginfo.getFilesize(), 0));
            holder.text5.setText(pkginfo.getApkuid());

            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(checkboxs.get(position));
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    checkboxs.set(position, b);
                }
            });

            int mode = pkginfo.getIconmode();
            holder.imageView.setTag(pkginfo.getPkgname());
            if (mode == 1) {
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.manager_grant_app_foreground));
            } else {
                // For mode 0 and 2, we should ideally load asynchronously.
                // For now, let's at least keep the existing logic but wrapped in a check to avoid flickering
                // and removing notifyDataSetChanged()
                try {
                    if (mode == 0) {
                        holder.imageView.setImageDrawable(pu.getPKGIcon(context, pkginfo.getPkgname()));
                    } else if (mode == 2) {
                        holder.imageView.setImageDrawable(pu.getPKGFileIcon(context, pkginfo.getApkpath()));
                    }
                } catch (Exception e) {
                    holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.manager_grant_app_foreground));
                }
            }
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        MaterialCheckBox checkBox;
        TextView text;
        TextView text2;
        TextView text3;
        TextView text4;
        TextView text5;
    }
}

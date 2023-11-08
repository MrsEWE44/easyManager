package com.easymanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.easymanager.R;

import java.io.File;
import java.util.ArrayList;

public class GeneralAdapter extends BaseAdapter {

    public GeneralAdapter(ArrayList<String> list, Context context, ArrayList<Boolean> checkboxs) {
        this.list = (ArrayList<String>) list.clone();
        this.context = context;
        this.checkboxs = checkboxs;
        notifyDataSetChanged();
    }

    private ArrayList<String> list;
    private Context context;
    private ArrayList<Boolean> checkboxs;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    private String getSize(double size,int count){
        String size_type[] = {"b","KB","MB","GB","TB","PB"};
        if(size > 1024){
            double d_size= size/1024;
            count = count + 1;
            return getSize(d_size,count);
        }
        String sizestr=String.format("%.2f",size)+size_type[count];
        return sizestr;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int size = list.size();
        int size1 = checkboxs.size();
        view = LayoutInflater.from(context).inflate(R.layout.general_listview_item_layout,null);
        CheckBox checkBox=view.findViewById(R.id.glilcb);
        TextView text = view.findViewById(R.id.gliltv);
        TextView text2 = view.findViewById(R.id.gliltv2);
        if(i < size){
            text.setText(list.get(i));
            File file = new File(list.get(i));
            if(file == null){
                text2.setText("");
            }else{
                text2.setText(getSize(file.length(),0));
            }

            if(i < size1){
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        checkboxs.set(i,b);
                    }
                });
                checkBox.setChecked(checkboxs.get(i));
            }


        }
        return view;
    }
}

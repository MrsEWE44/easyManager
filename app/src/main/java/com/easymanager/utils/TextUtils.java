package com.easymanager.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;


import com.easymanager.entitys.PKGINFO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TextUtils {

    public TextUtils(){}

    public void copyText(Context context, String str){
        ClipboardManager cpm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cpm.setText(str);
        Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show();
    }


    //搜索列表匹配项
    public ArrayList<PKGINFO> indexOfPKGS(Activity activity, String findStr, ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs, Integer types){
        if(pkginfos.size() == 0){
            PackageUtils packageUtils = new PackageUtils();
            packageUtils.queryPKGS(activity,pkginfos,checkboxs,types);
        }
        return indexOfPKGS(pkginfos,checkboxs,findStr);
    }


    public Boolean isIndexOfStr(String str,String instr){
        return str.toLowerCase(Locale.ROOT).indexOf(instr.toLowerCase(Locale.ROOT)) != -1;
    }


    //搜索列表匹配项
    public ArrayList<PKGINFO> indexOfPKGS(ArrayList<PKGINFO> pkginfos , ArrayList<Boolean> checkboxs,String findStr){
        checkboxs.clear();
        ArrayList<PKGINFO> pkginfos2 = new ArrayList<>();
        for (PKGINFO pkginfo : pkginfos) {
            if(isIndexOfStr(pkginfo.getAppname(),findStr) || isIndexOfStr(pkginfo.getPkgname(),findStr)){
                pkginfos2.add(pkginfo);
                checkboxs.add(false);
            }
        }
        pkginfos.clear();
        return pkginfos2;
    }

    //搜索列表匹配项
    public void indexOfLIST(ArrayList<String> list , ArrayList<Boolean> checkboxs,ArrayList<Boolean> switbs,String findStr){
        if(findStr!=null || !findStr.isEmpty()){
            checkboxs.clear();
            ArrayList<String> strings = new ArrayList<>();
            ArrayList<Boolean> switbs2 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);
                if(isIndexOfStr(s,findStr)){
                    strings.add(s);
                    checkboxs.add(false);
                    if(switbs !=null){
                        switbs2.add(switbs.get(i));
                    }
                }
            }
            list.clear();
            switbs.clear();
            switbs.addAll(switbs2);
            list.addAll(strings);
        }
    }



}

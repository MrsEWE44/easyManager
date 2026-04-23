package com.easymanager.utils.dialog;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.easymanager.R;
import com.easymanager.entitys.PKGINFO;

import java.util.ArrayList;

public class SearchDialog extends QueryDialog {

    public void showRestorySearchViewDialog(Context context, Activity activity, ListView apllv1, ArrayList<PKGINFO> pkginfos, ArrayList<Boolean> checkboxs, int uid) {
        showSearchViewDialogCore(context,activity,apllv1,pkginfos,null,checkboxs,uid,2);
    }

    public void showIndexOfPKGSDialog(Context context , Activity activity,ListView lv1,String msg , String searchStr , ArrayList<PKGINFO> pkginfos,ArrayList<String> strList,ArrayList<Boolean> checkboxs){
        try {
            AlertDialog show = showMyDialog(context, msg);
            Handler handler = new Handler(android.os.Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what==0){
                        if(strList == null){
                            showPKGS(context,lv1,pkginfos,checkboxs);
                        }else{
                            showUsers(context,lv1,strList,checkboxs);
                        }
                        permittedDismissDialog(show);
                    }
                }
            };
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(strList == null){
                        pkginfos.addAll(tu.indexOfPKGS(activity,searchStr,pkginfos,checkboxs,0));
                    }else{
                        tu.indexOfLIST(strList,checkboxs,null,searchStr);
                    }
                    sendHandlerMSG(handler,0);
                }
            });
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showProcessSearchViewDialog(Context context,Activity activity,ListView lv,ArrayList<PKGINFO> pkginfos,ArrayList<String> strList,ArrayList<Boolean> checkboxs,int uid){
        showSearchViewDialogCore(context,activity,lv,pkginfos,strList,checkboxs,uid,1);
    }

    public void showUninstalledSearchViewDialog(Context context,Activity activity,ListView lv,ArrayList<PKGINFO> pkginfos,ArrayList<String> strList,ArrayList<Boolean> checkboxs,int uid){
        showSearchViewDialogCore(context,activity,lv,pkginfos,strList,checkboxs,uid,3);
    }

    public void showSearchViewDialogCore(Context context, Activity activity, ListView lv, ArrayList<PKGINFO> pkginfos, ArrayList<String> strList, ArrayList<Boolean> checkboxs, int uid, int mode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(tu.getLanguageString(context, R.string.tips));
        View vvv = LayoutInflater.from(context).inflate(R.layout.search_view_layout, null);
        SearchView svlsv = vvv.findViewById(R.id.svlsv);
        ListView svllv = vvv.findViewById(R.id.svllv);
        svllv.setTextFilterEnabled(true);
        svlsv.setSubmitButtonEnabled(true);

        builder.setView(vvv);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        preventDismissDialog(alertDialog);

        // 继承主Activity列表的点击监听器
        if (lv != null) {
            svllv.setOnItemClickListener(lv.getOnItemClickListener());
            svllv.setOnItemLongClickListener(lv.getOnItemLongClickListener());
        }

        if (pkginfos != null && pkginfos.size() == 0 && strList == null) {
            if (mode == 0) {
                queryAllPKGSProcessDialog(context, activity, svllv, pkginfos, checkboxs, uid);
            } else if (mode == 1) {
                queryAllProcessDialog(context, activity, svllv, pkginfos, checkboxs, uid);
            } else if (mode == 2) {
                queryLocalBackupProcessDialog(context, activity, svllv, pkginfos, checkboxs, uid);
            } else if (mode == 3) {
                queryUninstalledPKGSProcessDialog(context, activity, svllv, pkginfos, checkboxs, uid);
            }
        } else {
            // 如果已有数据，直接在对话框列表中显示
            if (strList == null) {
                showPKGS(context, svllv, pkginfos, checkboxs);
            } else {
                showUsers(context, svllv, strList, checkboxs);
            }
        }

        // 备份原始列表数据，用于在清空搜索框时恢复
        ArrayList<PKGINFO> originalPkginfos = (pkginfos != null) ? new ArrayList<>(pkginfos) : new ArrayList<>();
        ArrayList<String> originalStrList = (strList != null) ? new ArrayList<>(strList) : new ArrayList<>();
        ArrayList<Boolean> originalCheckboxs = (checkboxs != null) ? new ArrayList<>(checkboxs) : new ArrayList<>();

        svlsv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // 如果原始列表为空，但在后台加载了数据，则在此同步
                if (pkginfos != null && originalPkginfos.isEmpty() && !pkginfos.isEmpty()) {
                    originalPkginfos.addAll(pkginfos);
                    originalCheckboxs.addAll(checkboxs);
                }
                if (strList != null && originalStrList.isEmpty() && !strList.isEmpty()) {
                    originalStrList.addAll(strList);
                    originalCheckboxs.addAll(checkboxs);
                }

                if (s.isEmpty()) {
                    // 恢复原始列表
                    if (pkginfos != null) {
                        pkginfos.clear();
                        pkginfos.addAll(originalPkginfos);
                    }
                    if (strList != null) {
                        strList.clear();
                        strList.addAll(originalStrList);
                    }
                    if (checkboxs != null) {
                        checkboxs.clear();
                        checkboxs.addAll(originalCheckboxs);
                    }
                } else {
                    // 执行搜索逻辑
                    if (strList == null) {
                        if (originalPkginfos != null && originalCheckboxs != null) {
                            ArrayList<PKGINFO> filtered = tu.indexOfPKGS(originalPkginfos, originalCheckboxs, s);
                            pkginfos.clear();
                            pkginfos.addAll(filtered);
                            checkboxs.clear();
                            for (int i = 0; i < filtered.size(); i++) {
                                checkboxs.add(false);
                            }
                        }
                    } else {
                        if (originalStrList != null && originalCheckboxs != null) {
                            strList.clear();
                            strList.addAll(originalStrList);
                            checkboxs.clear();
                            checkboxs.addAll(originalCheckboxs);
                            tu.indexOfLIST(strList, checkboxs, null, s);
                        }
                    }
                }

                // 更新界面
                updateUI(context, svllv, lv, pkginfos, strList, checkboxs);
                permittedDismissDialog(alertDialog);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    // 如果原始列表为空，尝试同步（可能刚加载完）
                    if (pkginfos != null && originalPkginfos.isEmpty() && !pkginfos.isEmpty()) {
                        originalPkginfos.addAll(pkginfos);
                        originalCheckboxs.addAll(checkboxs);
                    }
                    if (strList != null && originalStrList.isEmpty() && !strList.isEmpty()) {
                        originalStrList.addAll(strList);
                        originalCheckboxs.addAll(checkboxs);
                    }

                    // 恢复原始列表并更新 UI
                    if (pkginfos != null && !originalPkginfos.isEmpty()) {
                        pkginfos.clear();
                        pkginfos.addAll(originalPkginfos);
                    }
                    if (strList != null && !originalStrList.isEmpty()) {
                        strList.clear();
                        strList.addAll(originalStrList);
                    }
                    if (checkboxs != null && !originalCheckboxs.isEmpty()) {
                        checkboxs.clear();
                        checkboxs.addAll(originalCheckboxs);
                    }
                    updateUI(context, svllv, lv, pkginfos, strList, checkboxs);
                }
                return true;
            }
        });

        // 监听对话框关闭，确保对话框消失时能够正确处理
        vvv.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {}

            @Override
            public void onViewDetachedFromWindow(View v) {
                // Remove this to avoid potential crash during activity destruction or redundant dismiss calls
                // permittedDismissDialog(alertDialog);
            }
        });
    }

    private void updateUI(Context context, ListView svllv, ListView lv, ArrayList<PKGINFO> pkginfos, ArrayList<String> strList, ArrayList<Boolean> checkboxs) {
        if (strList == null) {
            showPKGS(context, svllv, pkginfos, checkboxs);
            if (lv != null) showPKGS(context, lv, pkginfos, checkboxs);
        } else {
            showUsers(context, svllv, strList, checkboxs);
            if (lv != null) showUsers(context, lv, strList, checkboxs);
        }
    }

    public void showSearchViewDialog(Context context,Activity activity,ListView lv,ArrayList<PKGINFO> pkginfos,ArrayList<String> strList,ArrayList<Boolean> checkboxs,int uid){
        showSearchViewDialogCore(context,activity,lv,pkginfos,strList,checkboxs,uid,0);
    }

}

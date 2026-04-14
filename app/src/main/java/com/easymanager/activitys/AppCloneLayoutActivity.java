package com.easymanager.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.easymanager.MainActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.easymanager.R;
import com.easymanager.core.utils.CMD;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.enums.AppManagerEnum;
import com.easymanager.enums.easyManagerEnums;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.OtherTools;
import com.easymanager.utils.base.AppCloneUtils;
import com.easymanager.utils.base.DialogBaseUtils;
import com.easymanager.utils.dialog.HelpDialogUtils;

import java.util.ArrayList;

public class AppCloneLayoutActivity extends AppCompatActivity {

    private ArrayList<String> strlist = new ArrayList<>();
    private ArrayList<Boolean> strcheckboxs = new ArrayList<>();
    private ArrayList<PKGINFO> pkginfos = new ArrayList<>();
    private ArrayList<Boolean> pkgcheckboxs = new ArrayList<>();
    private EditText acmet1;
    private Spinner acmsp1 , acmsp2;
    private Button clmbt1;
    private ListView acmlv1 , acmlv2;
    private Context context;
    private Activity activity;
    private Boolean isRoot , isADB;
    private int mode,APP_CHOICES_INDEX,MANAGER_INDEX;

    private AppCloneUtils acu = new AppCloneUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_clone_manager);
        MyActivityManager.getIns().add(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Intent intent = getIntent();
            boolean isShizuku = intent.getBooleanExtra("isShizuku", false);
            boolean isDhizuku = intent.getBooleanExtra("isDhizuku", false);
            String modeName = "[ General ]";
            if (isShizuku) modeName = "[ SHIZUKU ]";
            else if (isDhizuku) modeName = "[ DHIZUKU ]";

            getSupportActionBar().setTitle(getTitle() + " " + modeName);
        }

        context = this;
        activity = this;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            new DialogBaseUtils().showInfoMsg(context,getLanStr(R.string.tips),getLanStr(R.string.show_app_clone_low_version_msg));
        }else {
            initBt();
        }

    }

    private void initBt() {
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode",-1);
        isRoot = intent.getBooleanExtra("isRoot",false);
        isADB = intent.getBooleanExtra("isADB",false);
        acmet1 = findViewById(R.id.acmet1);
        acmsp1 = findViewById(R.id.acmsp1);
        acmsp2 = findViewById(R.id.acmsp2);
        clmbt1 = findViewById(R.id.clmbt1);
        acmlv1 = findViewById(R.id.acmlv1);
        acmlv2 = findViewById(R.id.acmlv2);
        acmsp1.setAdapter(getSpinnerAdapter(getManagerMode()));
        acmsp2.setAdapter(getSpinnerAdapter(getAppChoicesOPT()));
        if(mode == AppManagerEnum.APP_CLONE){
            acmsp1.setEnabled(false);
            clmbt1.setText(getLanStr(R.string.app_clone_button_start_clone_str));
        }

        if(mode == AppManagerEnum.APP_CLONE_MANAGE){
            acmet1.setEnabled(false);
            acmet1.setHint(getLanStr(R.string.stop_edit));
            clmbt1.setText(getLanStr(R.string.app_clone_button_manager_clone_change_str));
        }

        if(mode == AppManagerEnum.APP_CLONE_REMOVE){
            acmsp1.setEnabled(false);
            acmet1.setEnabled(false);
            acmet1.setHint(getLanStr(R.string.stop_edit));
            clmbt1.setText(getLanStr(R.string.app_clone_button_delete_clone_str));
        }
        btClicked();
        String maxuser = acu.getEasyManagerUtils().getProp(context,"persist.sys.max_profiles");
        if(maxuser.equals(String.valueOf(easyManagerEnums.ulock_max_user_size))){
            String cmdstr = String.format("setprop fw.max_users %d ;",easyManagerEnums.ulock_max_user_size);
            CMD cmd = acu.getEasyManagerUtils().runCMD(cmdstr);
            cmd.getResult();
        }
        acmet1.setHint(String.format(getLanStr(R.string.app_clone_input_size_hint_str),acu.getPd().easyMUtils.getMaxSupportedUsers(context)));

        // 自动触发初始加载，解决进入页面空白的问题
        int defaultUid = acu.getCurrentUserID();
        if (mode == AppManagerEnum.APP_CLONE) {
            // 克隆模式：显示当前主用户的应用作为克隆模板
            acu.getSd().queryUserAllPKGSProcessDialog(context, activity, acmlv1, pkginfos, pkgcheckboxs, defaultUid);
        } else if (mode == AppManagerEnum.APP_CLONE_REMOVE || mode == AppManagerEnum.APP_CLONE_MANAGE) {
            // 管理/删除模式：显示已存在的所有分身用户中的应用
            acu.getSd().queryAllPKGSByAppCloneProcessDialog(context, activity, acmlv1, pkginfos, pkgcheckboxs);
        }

        new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_MANAGE_HELP,mode);
    }

    private void btClicked() {
        int currentUserID = acu.getCurrentUserID();
        spinnerChange(acmsp1,0);
        spinnerChange(acmsp2,1);
        clmbt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode == AppManagerEnum.APP_CLONE_REMOVE){
                    ArrayList<PKGINFO> list = new ArrayList<>();
                    for (int i = 0; i < strlist.size(); i++) {
                        PKGINFO pkginfo = new PKGINFO(strlist.get(i),null,null,null,null,null);
                        if(APP_CHOICES_INDEX == 0){
                            if(strcheckboxs.get(i)){
                                list.add(pkginfo);
                            }
                        }

                        if(APP_CHOICES_INDEX == 1){
                            if(!strcheckboxs.get(i)){
                                list.add(pkginfo);
                            }
                        }

                        if(APP_CHOICES_INDEX == 2){
                            list.add(pkginfo);
                        }
                    }
                    acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_CLONE_REMOVE,-1,null,currentUserID);
                }else{
                    ArrayList<PKGINFO> list = new ArrayList<>();
                    for (int i = 0; i < pkgcheckboxs.size(); i++) {
                        PKGINFO pkginfo = pkginfos.get(i);
                        if(APP_CHOICES_INDEX == 0){
                            if(pkgcheckboxs.get(i)){
                                list.add(pkginfo);
                            }
                        }

                        if(APP_CHOICES_INDEX == 1){
                            if(!pkgcheckboxs.get(i)){
                                list.add(pkginfo);
                            }
                        }

                        if(APP_CHOICES_INDEX == 2){
                            list.add(pkginfo);
                        }

                    }
                    if(mode == AppManagerEnum.APP_CLONE){
                        String s = acmet1.getText().toString();
                        int count =(s==null || s.isEmpty())?1:Integer.valueOf(s);
                        acu.getUd().showAppClone(context,list,count,getLanStr(R.string.app_clone_create_clone_title),getLanStr(R.string.app_clone_create_clone_msg));
                    }

                    if(mode == AppManagerEnum.APP_CLONE_MANAGE){
                        ArrayList<String> strings = new ArrayList<>();
                        for (int i = 0; i < strcheckboxs.size(); i++) {
                            if(strcheckboxs.get(i)){
                                strings.add(strlist.get(i));
                            }
                        }
                        acu.getUd().showAppCloneManagerProcessBarDialog(context,list,strings,MANAGER_INDEX);
                    }
                }

            }
        });

        acmlv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PKGINFO pkginfo = pkginfos.get(i);
                Intent intent = new Intent(activity, AppInfoLayoutActivity.class);
                intent.putExtra("pkgname", pkginfo.getPkgname());
                intent.putExtra("uid", acu.getCurrentUserID());
                intent.putExtra("isRoot", isRoot);
                intent.putExtra("isADB", isADB);
                startActivity(intent);
            }
        });

        acmlv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                nowItemIndex = i;
                return false;
            }
        });
        registerForContextMenu(acmlv1);

        acmlv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("mode",mode);
                intent.putExtra("isRoot",isRoot);
                intent.putExtra("isADB",isADB);
                intent.putExtra("uid",Integer.valueOf(strlist.get(i)));
                startActivity(intent);
            }
        });

    }

    private int nowItemIndex = -1;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (nowItemIndex != -1) {
            PKGINFO pkg = pkginfos.get(nowItemIndex);
            menu.setHeaderTitle(pkg.getAppname());
            menu.add(0, 0, 0, R.string.copy_app_detail);
            menu.add(0, 1, 0, R.string.jump_app_detail);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (nowItemIndex == -1) return super.onContextItemSelected(item);
        PKGINFO pkg = pkginfos.get(nowItemIndex);
        int itemId = item.getItemId();
        if (itemId == 0) {
            String detail = String.format("App Name: %s\nPackage Name: %s\nVersion: %s\nPath: %s",
                    pkg.getAppname(), pkg.getPkgname(), pkg.getAppversionname(), pkg.getApkpath());
            acu.getUd().tu.copyText(context, detail);
            return true;
        } else if (itemId == 1) {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(android.net.Uri.parse("package:" + pkg.getPkgname()));
            startActivity(intent);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private String[] getManagerMode() {
        return new String[]{getLanStr(R.string.app_clone_manager_clone_mode_str1),getLanStr(R.string.app_clone_manager_clone_mode_str2)};
    }

    private String getLanStr(int id){
        return acu.getTU().getLanguageString(context,id);
    }

    public ArrayAdapter getSpinnerAdapter(String s[]){
        return new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, s);
    }

    private String[] getAppChoicesOPT(){
        return new String[]{getLanStr(R.string.spin_item_selected),getLanStr(R.string.spin_item_no_selected),getLanStr(R.string.spin_item_all_selected)};
    }


    private void spinnerChange(Spinner s,int app_opt_mode){
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (app_opt_mode){
                    case 0:
                        MANAGER_INDEX = i;
                        break;
                    case 1:
                        APP_CHOICES_INDEX = i;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        OtherTools otherTools = new OtherTools();
        otherTools.addMenuBase(this,menu,mode);
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        int itemId = item.getItemId();
        int defaultuid=acu.getCurrentUserID();
        if(itemId == R.id.actionbarsearch){
            acu.getSd().showSearchViewDialog(context,activity,acmlv1,pkginfos,null,pkgcheckboxs,defaultuid);
        }
        if(itemId == android.R.id.home){
            activity.onBackPressed();
        }
        if(itemId == 0){
            acu.getSd().queryAllPKGSProcessDialog(context,activity,acmlv1,pkginfos,pkgcheckboxs,defaultuid);
        }

        if(itemId == 1){
            acu.getSd().queryAllEnablePKGSProcessDialog(context,activity,acmlv1,pkginfos,pkgcheckboxs,defaultuid);
        }

        if(itemId == 2){
            acu.getSd().queryUserAllPKGSProcessDialog(context,activity,acmlv1,pkginfos,pkgcheckboxs,defaultuid);
        }

        if(itemId == 3){
            acu.getSd().queryUserEnablePKGSProcessDialog(context,activity,acmlv1,pkginfos,pkgcheckboxs,defaultuid);
        }

        if(itemId == 4){
            acu.getSd().queryAllDisablePKGSProcessDialog(context,activity,acmlv1,pkginfos,pkgcheckboxs,defaultuid);
        }

        if(itemId == 5){
            new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_MANAGE_HELP,mode);
        }

        if(itemId == 10){
            acu.getSd().queryUserAllPKGSByAppCloneProcessDialog(context,activity,acmlv1,pkginfos,pkgcheckboxs);
        }

        if(itemId == 11){
            acu.getSd().queryAllPKGSByAppCloneProcessDialog(context,activity,acmlv1,pkginfos,pkgcheckboxs);
        }

        if(itemId == 12){
            acu.getUd().queryLocalAppCloneUserProcessDialog(context,activity,acmlv2,strlist,strcheckboxs);
        }

        if(itemId == 13){
            acu.getUd().startAppCloneUsers(context,activity);
        }

        if(itemId == 14){
            if(isRoot){
                acu.getUd().unlockMaxLimit(context,activity);
            }else{
                acu.getUd().showInfoMsg(context,getLanStr(R.string.tips),getLanStr(R.string.need_root_msg));
            }
        }

        if(itemId == 6){
            MyActivityManager.getIns().killall();
        }
        return super.onOptionsItemSelected(item);
    }

}

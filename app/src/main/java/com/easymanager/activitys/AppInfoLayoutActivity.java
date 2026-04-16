package com.easymanager.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.easymanager.R;
import com.easymanager.core.api.ShizukuSystemServerApi;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.enums.AppInfoEnums;
import com.easymanager.enums.AppManagerEnum;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.OtherTools;
import com.easymanager.utils.base.AppCloneUtils;
import com.easymanager.utils.dialog.HelpDialogUtils;

import java.util.ArrayList;

public class AppInfoLayoutActivity extends AppCompatActivity {

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Boolean> checkboxs = new ArrayList<>();
    private ArrayList<Boolean> switbs = new ArrayList<>();

    private ImageView ailappicon;

    private TextView ailappname , ailapppkgname , ailappversion , ailappsize;

    private Spinner ailsp1;

    private Button ailbt1, ailbt_details;

    private ListView aillv1;

    private Context context;

    private Activity activity;

    private Boolean isRoot , isADB;

    private String pkgname;
    private int uid;
    private int menus_index = 0;
    private int mode =-1;
    private int app_info_mode=-1;
    private int app_info_mode2=-1;

    private AppCloneUtils acu = new AppCloneUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_info_layout);
        MyActivityManager.getIns().add(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Intent intent = getIntent();
            boolean isShizuku = intent.getBooleanExtra("isShizuku", false);
            boolean isDhizuku = intent.getBooleanExtra("isDhizuku", false);
            String modeName = "[ General ]";
            if (isShizuku && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU) modeName = "[ SHIZUKU ]";
            else if (isDhizuku && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU) modeName = "[ DHIZUKU ]";

            getSupportActionBar().setTitle(getTitle() + " " + modeName);
        }

        initBt();
    }

    private void initBt() {
        context = this;
        activity = this;
        Intent intent = getIntent();
        pkgname = (String) intent.getSerializableExtra("pkgname");
        isADB = intent.getBooleanExtra("isADB",false);
        isRoot = intent.getBooleanExtra("isRoot",false);
        uid = intent.getIntExtra("uid",0);
        mode = intent.getIntExtra("mode",-1);
        ailappicon = findViewById(R.id.ailappicon);
        ailappname = findViewById(R.id.ailappname);
        ailapppkgname = findViewById(R.id.ailapppkgname);
        ailappversion = findViewById(R.id.ailappversion);
        ailappsize = findViewById(R.id.ailappsize);
        ailsp1 = findViewById(R.id.ailsp1);
        ailbt1 = findViewById(R.id.ailbt1);
        ailbt_details = findViewById(R.id.ailbt_details);
        aillv1 = findViewById(R.id.aillv1);
        ailsp1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getAppChoicesOPT()));
        PKGINFO pkginfo = acu.getUd().packageUtils.getPKGINFO(context, pkgname);
        Drawable pkginfoicon = acu.getUd().packageUtils.getPKGIcon(context,pkgname);
        ailappicon.setImageDrawable(pkginfoicon);
        ailappname.setText(pkginfo.getAppname());
        ailapppkgname.setText(pkginfo.getPkgname());
        ailappversion.setText(pkginfo.getAppversionname());
        ailappsize.setText(acu.getUd().ft.getSize(pkginfo.getFilesize(),0));
        if(isRoot || isADB){
            btClicked();
            new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_INFO_HELP,mode);
        }else{
            acu.getUd().showInfoMsg(context,getLanStr(R.string.warning_tips),getLanStr(R.string.need_root_msg));
        }
    }


    private void btClicked() {

        ailsp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                menus_index = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ailbt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<PKGINFO> pkginfos = new ArrayList<>();
                for (int i = 0; i < checkboxs.size(); i++) {
                    String s = list.get(i);
                    PKGINFO pkginfo = new PKGINFO(pkgname,s,  (switbs.get(i) ? "true" : "false"), null, null, null);
                    if(menus_index == 0){
                        if(checkboxs.get(i)){
                            pkginfos.add(pkginfo);
                        }
                    }

                    if(menus_index == 1){
                        if(!checkboxs.get(i)){
                            pkginfos.add(pkginfo);
                        }
                    }

                    if(menus_index == 2){
                        pkginfos.add(pkginfo);
                    }
                }
                acu.getPd().showProcessBarDialogByCMD(context,pkginfos, AppManagerEnum.APP_INFO_LAYOUT,app_info_mode,null,Integer.valueOf(uid));
            }
        });

        ailbt_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailedAppInfo();
            }
        });

    }

    private void showDetailedAppInfo() {
        try {
            android.content.pm.PackageManager pm = getPackageManager();
            android.content.pm.PackageInfo pi = pm.getPackageInfo(pkgname, android.content.pm.PackageManager.GET_PERMISSIONS);
            android.content.pm.ApplicationInfo ai = pi.applicationInfo;

            com.google.android.material.dialog.MaterialAlertDialogBuilder builder = new com.google.android.material.dialog.MaterialAlertDialogBuilder(context);
            View customView = android.view.LayoutInflater.from(context).inflate(R.layout.dialog_app_details, null);
            
            android.widget.ImageView iconView = customView.findViewById(R.id.detail_app_icon);
            android.widget.TextView nameView = customView.findViewById(R.id.detail_app_name);
            android.widget.TextView pkgView = customView.findViewById(R.id.detail_app_pkg);
            android.widget.TextView infoView = customView.findViewById(R.id.detail_info_text);

            iconView.setImageDrawable(ai.loadIcon(pm));
            nameView.setText(ai.loadLabel(pm));
            pkgView.setText(String.format("%s (%s)", pkgname, pi.versionName));

            StringBuilder sb = new StringBuilder();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());

            sb.append(String.format(getLanStr(R.string.app_details_uid), ai.uid)).append("\n");
            sb.append(String.format(getLanStr(R.string.app_details_install_time), sdf.format(new java.util.Date(pi.firstInstallTime)))).append("\n");
            sb.append(String.format(getLanStr(R.string.app_details_update_time), sdf.format(new java.util.Date(pi.lastUpdateTime)))).append("\n");
            
            sb.append(String.format(getLanStr(R.string.app_details_min_sdk), ai.minSdkVersion)).append("\n");
            sb.append(String.format(getLanStr(R.string.app_details_target_sdk), ai.targetSdkVersion)).append("\n");
            
            String abi = "Unknown";
            try {
                java.lang.reflect.Field primaryCpuAbiField = android.content.pm.ApplicationInfo.class.getDeclaredField("primaryCpuAbi");
                primaryCpuAbiField.setAccessible(true);
                abi = (String) primaryCpuAbiField.get(ai);
            } catch (Exception e) {
                // Fallback for some versions
            }
            sb.append(String.format(getLanStr(R.string.app_details_abi), abi)).append("\n");

            sb.append(String.format(getLanStr(R.string.app_details_data_dir), ai.dataDir)).append("\n");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                sb.append(String.format(getLanStr(R.string.app_details_protected_data_dir), ai.deviceProtectedDataDir)).append("\n");
            }
            
            sb.append(String.format(getLanStr(R.string.app_details_source_dir), ai.sourceDir)).append("\n");
            if (ai.publicSourceDir != null && !ai.publicSourceDir.equals(ai.sourceDir)) {
                sb.append(String.format(getLanStr(R.string.app_details_public_source_dir), ai.publicSourceDir)).append("\n");
            }
            
            if (ai.splitSourceDirs != null && ai.splitSourceDirs.length > 0) {
                sb.append(getLanStr(R.string.app_details_split_source_dirs)).append("\n");
                for (String split : ai.splitSourceDirs) {
                    sb.append("  - ").append(split).append("\n");
                }
            }

            sb.append(String.format(getLanStr(R.string.app_details_process_name), ai.processName)).append("\n");
            if (ai.taskAffinity != null) {
                sb.append(String.format(getLanStr(R.string.app_details_task_affinity), ai.taskAffinity)).append("\n");
            }

            infoView.setText(sb.toString());

            android.widget.Button btnCopy = customView.findViewById(R.id.btn_copy_details);
            btnCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acu.getPd().tu.copyText(context, sb.toString());
                }
            });

            builder.setTitle(getLanStr(R.string.app_details_title))
                    .setView(customView)
                    .setPositiveButton(R.string.dialog_sure_text, null)
                    .show();

        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new OtherTools().addMenuBase(this,menu,AppManagerEnum.APP_INFO_LAYOUT);
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

        if(itemId == R.id.actionbarsearch){
            acu.getPd().showAppInfoSearchViewDialog(context,activity,app_info_mode,app_info_mode2,pkgname,uid,aillv1,list,checkboxs,switbs);
        }

        if(itemId == android.R.id.home){
            activity.onBackPressed();
        }

        if(itemId == 0){
            app_info_mode=AppInfoEnums.IS_PERMISSION;
            app_info_mode2=AppInfoEnums.GET_PERMISSIOSN;
            acu.getPd().showAppInfoPermissionProcessDialog(context,activity,aillv1,list,checkboxs,switbs,pkgname,uid);
        }

        if(itemId == 1){
            app_info_mode=AppInfoEnums.IS_COMPENT_OR_PACKAGE;
            app_info_mode2=AppInfoEnums.GET_SERVICES;
            acu.getPd().showAppInfoServiceProcessDialog(context,activity,aillv1,list,checkboxs,switbs,pkgname,uid);
        }

        if(itemId == 2){
            app_info_mode=AppInfoEnums.IS_COMPENT_OR_PACKAGE;
            app_info_mode2=AppInfoEnums.GET_ACTIVITYS;
            acu.getPd().showAppInfoActivityProcessDialog(context,activity,aillv1,list,checkboxs,switbs,pkgname,uid);
        }

        if(itemId == 3){
            app_info_mode=AppInfoEnums.IS_COMPENT_OR_PACKAGE;
            app_info_mode2=AppInfoEnums.GET_RECEIVERS;
            acu.getPd().showAppInfoReceiverProcessDialog(context,activity,aillv1,list,checkboxs,switbs,pkgname,uid);
        }

        if(itemId == 4){
            new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_INFO_HELP,mode);
        }

        if(itemId == 5){
            MyActivityManager.getIns().killall();
        }

        return super.onOptionsItemSelected(item);
    }
    private String getLanStr(int id){
        return acu.getTU().getLanguageString(context,id);
    }
    private String[] getAppChoicesOPT(){
        return new String[]{getLanStr(R.string.spin_item_selected),getLanStr(R.string.spin_item_no_selected),getLanStr(R.string.spin_item_all_selected)};
    }

}

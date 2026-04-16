package com.easymanager.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

import com.easymanager.R;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.enums.AppInfoEnums;
import com.easymanager.enums.AppManagerEnum;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.OtherTools;
import com.easymanager.utils.base.AppCloneUtils;
import com.easymanager.utils.dialog.HelpDialogUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AppInfoLayoutActivity extends BaseActivity {

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Boolean> checkboxs = new ArrayList<>();
    private ArrayList<Boolean> switbs = new ArrayList<>();

    private ImageView ailappicon;

    private TextView ailappname, ailapppkgname, ailappversion, ailappsize, ailappuid, ailappabi;

    private String targetSdk, minSdk, installTime, updateTime, dataPath, installer, nativeLib, externalDataPath, appPath;

    private Spinner ailsp1;

    private Button ailbt1, ail_show_details;

    private ListView aillv1;

    private Context context;

    private Activity activity;

    private Boolean isRoot , isADB, isDevice;

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
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initBt();
    }

    private void initBt() {
        context = this;
        activity = this;
        Intent intent = getIntent();
        pkgname = (String) intent.getSerializableExtra("pkgname");
        isADB = intent.getBooleanExtra("isADB",false);
        isRoot = intent.getBooleanExtra("isRoot",false);
        isDevice = intent.getBooleanExtra("isDevice",false);
        uid = intent.getIntExtra("uid",0);
        mode = intent.getIntExtra("mode",-1);
        ailappicon = findViewById(R.id.ailappicon);
        ailappname = findViewById(R.id.ailappname);
        ailapppkgname = findViewById(R.id.ailapppkgname);
        ailappversion = findViewById(R.id.ailappversion);
        ailappsize = findViewById(R.id.ailappsize);
        ailappuid = findViewById(R.id.ailappuid);
        ailappabi = findViewById(R.id.ailappabi);
        ailsp1 = findViewById(R.id.ailsp1);
        ailbt1 = findViewById(R.id.ailbt1);
        ail_show_details = findViewById(R.id.ail_show_details);
        aillv1 = findViewById(R.id.aillv1);
        ailsp1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getAppChoicesOPT()));
        PKGINFO pkginfo = acu.getUd().packageUtils.getPKGINFO(context, pkgname);
        Drawable pkginfoicon = acu.getUd().packageUtils.getPKGIcon(context,pkgname);
        ailappicon.setImageDrawable(pkginfoicon);
        ailappname.setText(pkginfo.getAppname());
        ailapppkgname.setText(pkginfo.getPkgname());
        ailappversion.setText(pkginfo.getAppversionname());
        ailappsize.setText(acu.getUd().ft.getSize(pkginfo.getFilesize(),0));
        ailappuid.setText(pkginfo.getApkuid());
        appPath = pkginfo.getApkpath();

        // Additional detailed info
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(pkgname, PackageManager.GET_PERMISSIONS);
            if (packageInfo != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    installTime = sdf.format(new Date(packageInfo.firstInstallTime));
                    updateTime = sdf.format(new Date(packageInfo.lastUpdateTime));
                }
                if (packageInfo.applicationInfo != null) {
                    targetSdk = String.valueOf(packageInfo.applicationInfo.targetSdkVersion);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        minSdk = String.valueOf(packageInfo.applicationInfo.minSdkVersion);
                    } else {
                        minSdk = "N/A";
                    }
                    dataPath = packageInfo.applicationInfo.dataDir;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String[] abis = Build.SUPPORTED_ABIS;
                        if (abis != null && abis.length > 0) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < abis.length; i++) {
                                sb.append(abis[i]);
                                if (i < abis.length - 1) sb.append(", ");
                            }
                            ailappabi.setText(sb.toString());
                        } else {
                            ailappabi.setText("N/A");
                        }
                    } else {
                        ailappabi.setText(Build.CPU_ABI + (Build.CPU_ABI2.equals("unknown") ? "" : ", " + Build.CPU_ABI2));
                    }

                    String inst = getPackageManager().getInstallerPackageName(pkgname);
                    installer = inst != null ? inst : "System/Direct";
                    nativeLib = packageInfo.applicationInfo.nativeLibraryDir;
                    
                    // Construct external data path: /sdcard/Android/data/[pkgname]
                    externalDataPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + pkgname;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(isRoot || isADB || isDevice){
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

        ail_show_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAppDetailsDialog();
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



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new OtherTools().addMenuBase(this,menu,AppManagerEnum.APP_INFO_LAYOUT);
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

        if(itemId == 100){
            copyAppDetail();
        }

        if(itemId == 5){
            new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_INFO_HELP,mode);
        }

        if(itemId == 6){
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

    private void showAppDetailsDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        View dialogView = android.view.LayoutInflater.from(context).inflate(R.layout.dialog_app_detail, null);

        // Set app icon and title in dialog header
        ImageView dialogIcon = dialogView.findViewById(R.id.dialog_app_icon);
        if (dialogIcon != null) {
            dialogIcon.setImageDrawable(ailappicon.getDrawable());
        }
        TextView headerTitle = dialogView.findViewById(R.id.dialog_header_title);
        if (headerTitle != null) {
            headerTitle.setText(ailappname.getText());
        }

        // Setup rows using item_detail_row.xml
        setupDialogRow(dialogView, R.id.dialog_name_row, getLanStr(R.string.app_info_name_label), ailappname.getText().toString());
        setupDialogRow(dialogView, R.id.dialog_pkg_row, getLanStr(R.string.app_info_package_label), ailapppkgname.getText().toString());
        setupDialogRow(dialogView, R.id.dialog_version_row, getLanStr(R.string.app_info_version_label), ailappversion.getText().toString());
        setupDialogRow(dialogView, R.id.dialog_size_row, getLanStr(R.string.app_info_size_label), ailappsize.getText().toString());
        setupDialogRow(dialogView, R.id.dialog_uid_row, getLanStr(R.string.app_info_uid_label), ailappuid.getText().toString());
        setupDialogRow(dialogView, R.id.dialog_abi_row, getLanStr(R.string.app_info_abi_label), ailappabi.getText().toString());
        
        setupDialogRow(dialogView, R.id.dialog_target_sdk_row, getLanStr(R.string.app_info_target_sdk_label), targetSdk);
        setupDialogRow(dialogView, R.id.dialog_min_sdk_row, getLanStr(R.string.app_info_min_sdk_label), minSdk);
        setupDialogRow(dialogView, R.id.dialog_installer_row, getLanStr(R.string.app_info_installer_label), installer);
        setupDialogRow(dialogView, R.id.dialog_install_time_row, getLanStr(R.string.app_info_install_label), installTime);
        setupDialogRow(dialogView, R.id.dialog_update_time_row, getLanStr(R.string.app_info_update_label), updateTime);

        // Setup paths
        ((TextView) dialogView.findViewById(R.id.dialog_app_path)).setText(appPath);
        ((TextView) dialogView.findViewById(R.id.dialog_data_path)).setText(dataPath);
        ((TextView) dialogView.findViewById(R.id.dialog_external_path)).setText(externalDataPath);
        ((TextView) dialogView.findViewById(R.id.dialog_native_lib_path)).setText(nativeLib);

        builder.setView(dialogView);
        builder.setPositiveButton(R.string.dialog_sure_text, null);
        builder.setNeutralButton(R.string.copy_app_detail, new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialogInterface, int i) {
                copyAppDetail();
            }
        });
        builder.show();
    }

    private void setupDialogRow(View root, int rowId, String label, String value) {
        View row = root.findViewById(rowId);
        ((TextView) row.findViewById(R.id.item_label)).setText(label);
        ((TextView) row.findViewById(R.id.item_value)).setText(value);
    }

    private void copyAppDetail() {
        StringBuilder sb = new StringBuilder();
        sb.append(getLanStr(R.string.app_info_name_label)).append(": ").append(ailappname.getText()).append("\n");
        sb.append(getLanStr(R.string.app_info_package_label)).append(": ").append(ailapppkgname.getText()).append("\n");
        sb.append(getLanStr(R.string.app_info_version)).append(": ").append(ailappversion.getText()).append("\n");
        sb.append(getLanStr(R.string.app_info_uid)).append(": ").append(ailappuid.getText()).append("\n");
        sb.append(getLanStr(R.string.app_info_size)).append(": ").append(ailappsize.getText()).append("\n");
        sb.append(getLanStr(R.string.app_info_target_sdk)).append(": ").append(targetSdk).append("\n");
        sb.append(getLanStr(R.string.app_info_min_sdk)).append(": ").append(minSdk).append("\n");
        sb.append(getLanStr(R.string.app_info_abi)).append(": ").append(ailappabi.getText()).append("\n");
        sb.append(getLanStr(R.string.app_info_installer)).append(": ").append(installer).append("\n");
        sb.append(getLanStr(R.string.app_info_install_time)).append(": ").append(installTime).append("\n");
        sb.append(getLanStr(R.string.app_info_update_time)).append(": ").append(updateTime).append("\n");
        sb.append(getLanStr(R.string.app_info_path)).append(": ").append(appPath).append("\n");
        sb.append(getLanStr(R.string.app_info_data_path)).append(": ").append(dataPath).append("\n");
        sb.append(getLanStr(R.string.app_info_external_data_path)).append(": ").append(externalDataPath).append("\n");
        sb.append(getLanStr(R.string.app_info_native_lib)).append(": ").append(nativeLib);

        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("App Detail", sb.toString());
        clipboard.setPrimaryClip(clip);
        acu.getUd().showInfoMsg(context, getLanStr(R.string.tips), getLanStr(R.string.is_copy_ok));
    }

}

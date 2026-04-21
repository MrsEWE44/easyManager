package com.easymanager.activitys;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.easymanager.R;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.enums.AppManagerEnum;
import com.easymanager.utils.ConfigUtils;
import com.easymanager.utils.FileTools;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.OtherTools;
import com.easymanager.utils.PackageUtils;
import com.easymanager.utils.StringTools;
import com.easymanager.utils.base.AppCloneUtils;
import com.easymanager.utils.dialog.HelpDialogUtils;
import com.easymanager.utils.easyManagerUtils;
import com.easymanager.utils.permissionRequest;

import java.io.File;
import java.util.ArrayList;

public class AppManagerLayoutActivity extends AppCompatActivity {

    private ArrayList<PKGINFO> pkginfos = new ArrayList<>();
    private ArrayList<Boolean> checkboxs = new ArrayList<>();
    private Button amlapplybt;
    private Spinner amlsp1,amlsp2,amlsp3,amlsp4;
    private ListView apllv1;

    private int mode;

    private Context context;
    private Activity activity;
    private Boolean isRoot , isADB,isDevice;
    private Integer uid;
    private String APP_PERMIS_OPT1_VALUES[] = {"default", "ignore","deny","allow","foreground"};
    private String APP_PERMIS_OPT2_VALUES[] = {"active", "working_set","frequent","rare","restricted"};
    private String APP_PERMIS_OPT3_VALUES[] = {"true","false"};
    private String APP_ALL_UERS[] = null;
    private int nowItemIndex=-1;
    private int APP_UID_INDEX=0,APP_CHOICES_INDEX = 0, APP_PERMIS_INDEX = 0 , APP_PERMIS_OPT_INDEX = 0, APP_PERMIS_MODE= 0 ;
    private int APP_CLEAN_TIME = 2,APP_CLEAN_TIME_TYPE = 0;

    private boolean install_mode = false;
    private AppCloneUtils acu = new AppCloneUtils();
    private PackageUtils packageUtils = acu.getPd().packageUtils;
    private FileTools ft = acu.getPd().ft;
    private ConfigUtils configUtils = new ConfigUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_manager_layout);
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

        initBt();
    }

    private void initBt(){
        easyManagerUtils ee = acu.getEasyManagerUtils();
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode",-1);
        isRoot = intent.getBooleanExtra("isRoot",false);
        isADB = intent.getBooleanExtra("isADB",false);
        isDevice = intent.getBooleanExtra("isDevice",false);
        uid = intent.getIntExtra("uid",0);
        context = this;
        activity = this;
        amlapplybt = findViewById(R.id.amlapplybt);
        amlsp1 = findViewById(R.id.amlsp1);
        amlsp2 = findViewById(R.id.amlsp2);
        amlsp3 = findViewById(R.id.amlsp3);
        amlsp4 = findViewById(R.id.amlsp4);
        apllv1 = findViewById(R.id.apllv1);
        amlsp1.setAdapter(getSpinnerAdapter(getAppPermis()));
        amlsp3.setAdapter(getSpinnerAdapter(getAppChoicesOPT()));
        String[] appCloneUsers = ee.getAppCloneUsers();
        APP_ALL_UERS = new String[appCloneUsers.length];
        APP_ALL_UERS[0]=String.valueOf(uid);
        int n = 1;
        for (int i = 0; i < appCloneUsers.length; i++) {
            if(!appCloneUsers[i].equals(String.valueOf(uid))){
                APP_ALL_UERS[n]=appCloneUsers[i];
                n++;
            }
        }

        amlsp4.setAdapter(getSpinnerAdapter(APP_ALL_UERS));
        btClicked();

        if(mode == AppManagerEnum.APP_PERMISSION){
            amlsp1.setEnabled(true);
            amlsp2.setEnabled(true);
            amlsp2.setAdapter(getSpinnerAdapter(getAppPermisOPT1()));
            amlapplybt.setText(getLanStr(R.string.button_apply));
        }

        if(mode == AppManagerEnum.APP_DISABLE_COMPENT){
            amlsp1.setEnabled(false);
            amlsp2.setEnabled(true);
            amlsp2.setAdapter(getSpinnerAdapter(getAppDisableOrEnableOPT()));
            amlapplybt.setText(getLanStr(R.string.button_apply));
        }

        if(mode == AppManagerEnum.APP_DUMP){
            amlsp1.setEnabled(false);
            amlsp2.setEnabled(true);
            amlsp2.setAdapter(getSpinnerAdapter(getAppDumpOPT()));
            amlapplybt.setText(getLanStr(R.string.button_dump_app));
        }


        if(mode == AppManagerEnum.APP_FIREWALL){
            amlsp1.setEnabled(false);
            amlsp2.setEnabled(true);
            amlsp2.setAdapter(getSpinnerAdapter(getAppFirewallOPT()));
            if(isADB && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                String test_result = acu.getEasyManagerUtils().runCMD("cmd connectivity get-chain3-enabled").toString();
                if(test_result.indexOf("disable") != -1){
                    //启用防火墙
                    String enable_firaw = acu.getEasyManagerUtils().runCMD("cmd connectivity set-chain3-enabled true").toString();
                }
            }
        }

        if(mode == AppManagerEnum.APP_UNINSTALL){
            amlsp1.setEnabled(false);
            amlsp2.setEnabled(false);
            amlapplybt.setText(getLanStr(R.string.button_del_app));
        }

        if(mode == AppManagerEnum.APP_CLEAN_PROCESS){
            amlsp1.setEnabled(false);
            amlsp2.setEnabled(false);
            amlsp2.setAdapter(getSpinnerAdapter(getAppCleanOPT()));
            amlapplybt.setText(getLanStr(R.string.button_clean));
        }

        if(mode == AppManagerEnum.APP_RESTORE_UNINSTALLED){
            amlsp1.setEnabled(false);
            amlsp2.setEnabled(false);
            amlapplybt.setText(getLanStr(R.string.button_restore));
        }

        if(mode == AppManagerEnum.APP_INSTALL_LOCAL_FILE){
            amlsp1.setEnabled(false);
            amlsp2.setAdapter(getSpinnerAdapter(getAppInstallLocalFileOPT()));
            amlapplybt.setText(getLanStr(R.string.button_select));
            install_mode=true;
        }

        if (mode == AppManagerEnum.APP_RESTORE_UNINSTALLED) {
            acu.getSd().queryUninstalledPKGSProcessDialog(context, activity, apllv1, pkginfos, checkboxs, uid);
        } else {
            acu.getUd().showPKGS(context, apllv1, pkginfos, checkboxs);
        }
        new HelpDialogUtils().showHelp(context, HelpDialogUtils.APP_MANAGE_HELP, mode);
    }

    private void btClicked(){

        spinnerChange(amlsp1,0);
        spinnerChange(amlsp2,1);
        spinnerChange(amlsp3,2);
        spinnerChange(amlsp4,3);

        amlapplybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(install_mode){
                    if(APP_PERMIS_OPT_INDEX == 0){
                        selectLocalFile();
                    }

                    if(APP_PERMIS_OPT_INDEX == 1){
                        selectLocalDir();
                    }

                }else{
                    ArrayList<PKGINFO> list = getAddedPKGList();
                    if(mode == AppManagerEnum.APP_PERMISSION){
                        String opt_str = null;
                        if(APP_PERMIS_MODE == 0){
                            opt_str = APP_PERMIS_OPT1_VALUES[APP_PERMIS_OPT_INDEX];
                        }

                        if(APP_PERMIS_MODE == 1){
                            opt_str = APP_PERMIS_OPT3_VALUES[APP_PERMIS_OPT_INDEX];
                        }

                        if(APP_PERMIS_MODE == 2){
                            opt_str = APP_PERMIS_OPT2_VALUES[APP_PERMIS_OPT_INDEX];
                        }
                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                            acu.getUd().showInfoMsg(context,getLanStr(R.string.tips),getLanStr(R.string.not_support_5));
                        }else{
                            acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_PERMISSION,APP_PERMIS_INDEX,opt_str,uid);
                        }
                    }

                    if(mode == AppManagerEnum.APP_DISABLE_COMPENT){
                        acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_DISABLE_COMPENT,APP_PERMIS_OPT_INDEX,null,uid);
                        if (APP_PERMIS_OPT_INDEX == 1) { // 1 is Disable
                            configUtils.savePkgs(context, list, mode);
                        }
                    }

                    if(mode == AppManagerEnum.APP_INSTALL_LOCAL_FILE){
                        acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_INSTALL_LOCAL_FILE,APP_PERMIS_OPT_INDEX,null,uid);
                    }

                    if(mode == AppManagerEnum.APP_DUMP){
                        acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_DUMP,APP_PERMIS_OPT_INDEX,null,uid);
                    }

                    if(mode == AppManagerEnum.APP_UNINSTALL){
                        acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_UNINSTALL,APP_PERMIS_OPT_INDEX,null,uid);
                        configUtils.savePkgs(context, list, mode);
                    }

                    if(mode == AppManagerEnum.APP_CLEAN_PROCESS){
                        acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_CLEAN_PROCESS,APP_PERMIS_OPT_INDEX,getADDCleanRunningAPPOPTSTR(),uid);
                    }

                    if(mode == AppManagerEnum.APP_RESTORE_UNINSTALLED){
                        acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_RESTORE_UNINSTALLED,APP_PERMIS_OPT_INDEX,null,uid);
                    }

                    if(mode == AppManagerEnum.APP_FIREWALL){
                        acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_FIREWALL,APP_PERMIS_OPT_INDEX,null,uid);
                    }

                }

            }
        });

        apllv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PKGINFO pkginfo = pkginfos.get(i);
                try {
                    PackageInfo packageInfo = getPackageManager().getPackageInfo(pkginfo.getPkgname(), 0);
                    Intent intent = new Intent(AppManagerLayoutActivity.this,AppInfoLayoutActivity.class);
                    intent.putExtra("pkgname",packageInfo.packageName);
                    intent.putExtra("uid",uid);
                    intent.putExtra("isRoot",isRoot);
                    intent.putExtra("isADB",isADB);
                    intent.putExtra("isDevice",isDevice);
                    startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(context, getLanStr(R.string.not_installed_app), Toast.LENGTH_SHORT).show();
                }
            }
        });

        apllv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                nowItemIndex = i;
                return false;
            }
        });
        registerForContextMenu(apllv1);
    }

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
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + pkg.getPkgname()));
            startActivity(intent);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createLVMenu() {
        // Method replaced by registerForContextMenu
    }

    private void spinnerChange(Spinner s, int app_opt_mode){
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (app_opt_mode){
                    case 0:
                        APP_PERMIS_INDEX = i;
                        if(mode == AppManagerEnum.APP_PERMISSION){
                            if(APP_PERMIS_INDEX == 16){
                                APP_PERMIS_MODE=1;
                                amlsp2.setAdapter(getSpinnerAdapter(getAppPermisOPT3()));
                            }else if(APP_PERMIS_INDEX == 17){
                                APP_PERMIS_MODE=2;
                                amlsp2.setAdapter(getSpinnerAdapter(getAppPermisOPT2()));
                            }else{
                                APP_PERMIS_MODE=0;
                                amlsp2.setAdapter(getSpinnerAdapter(getAppPermisOPT1()));
                            }
                        }
                        break;
                    case 1:
                        APP_PERMIS_OPT_INDEX = i;
//                        if(mode == AppManagerEnum.APP_INSTALL_LOCAL_FILE){
//                            amlsp1.setEnabled(false);
//                        }else{
//                            amlsp1.setEnabled((i == 0)?false:true);
//                        }

                        break;
                    case 2:
                        APP_CHOICES_INDEX = i;
                        break;
                    case 3:
                        APP_UID_INDEX = i;
                        uid = Integer.valueOf(APP_ALL_UERS[APP_UID_INDEX]);
                        break;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        OtherTools otherTools = new OtherTools();
        if (mode == AppManagerEnum.APP_RESTORE_UNINSTALLED) {
            menu.add(0, 10, 0, getLanStr(R.string.menu_get_uninstalled_app));
            menu.add(0, 5, 0, getLanStr(R.string.options_menu_help_str));
            menu.add(0, 6, 0, getLanStr(R.string.options_menu_exit));
        } else {
            otherTools.addMenuBase(this, menu, mode);
            if (mode == AppManagerEnum.APP_DISABLE_COMPENT || mode == AppManagerEnum.APP_UNINSTALL) {
                menu.add(0, 101, 0, getLanStr(R.string.menu_import_config));
                menu.add(0, 102, 0, getLanStr(R.string.menu_export_config));
            }
        }
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        int itemId = item.getItemId();
        if (itemId == 101) {
            ft.execFileSelect(context, activity, getLanStr(R.string.menu_import_config), 101);
            return true;
        }
        if (itemId == 102) {
            permissionRequest.getExternalStorageManager(context, activity);
            configUtils.exportConfig(context);
            Toast.makeText(context, "Exported to /easyManager/", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(itemId == R.id.actionbarsearch){
            if(mode == AppManagerEnum.APP_CLEAN_PROCESS){
                acu.getSd().showProcessSearchViewDialog(context,activity,apllv1,pkginfos,null,checkboxs,uid);
            }else {
                acu.getSd().showSearchViewDialog(context,activity,apllv1,pkginfos,null,checkboxs,uid);
            }

        }
        if(itemId == android.R.id.home){
            activity.onBackPressed();
        }
        if(itemId == 0){
            acu.getSd().queryAllPKGSProcessDialog(context,activity,apllv1,pkginfos,checkboxs,uid);
        }

        if(itemId == 1){
            acu.getSd().queryAllEnablePKGSProcessDialog(context,activity,apllv1,pkginfos,checkboxs,uid);
        }

        if(itemId == 2){
            acu.getSd().queryUserAllPKGSProcessDialog(context,activity,apllv1,pkginfos,checkboxs,uid);
        }

        if(itemId == 3){
            acu.getSd().queryUserEnablePKGSProcessDialog(context,activity,apllv1,pkginfos,checkboxs,uid);
        }

        if(itemId == 4){
            acu.getSd().queryAllDisablePKGSProcessDialog(context,activity,apllv1,pkginfos,checkboxs,uid);
        }

        if(itemId == 7){
            acu.getSd().queryAllProcessDialog(context,activity,apllv1,pkginfos,checkboxs,uid);
        }

        if(itemId == 8){
            acu.getSd().queryAllUserProcessDialog(context,activity,apllv1,pkginfos,checkboxs,uid);
        }

        if(itemId == 10){
            acu.getSd().queryUninstalledPKGSProcessDialog(context,activity,apllv1,pkginfos,checkboxs,uid);
        }

        if(itemId == 9){
            acu.getSd().queryLocalBackupProcessDialog(context,activity,apllv1,pkginfos,checkboxs,uid);
        }

        if(itemId == 5){
            new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_MANAGE_HELP,mode);
        }

        if(itemId == 6){
            MyActivityManager.getIns().killall();
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayAdapter getSpinnerAdapter(String s[]){
        return new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, s);
    }

    private void selectLocalFile(){
        permissionRequest.getExternalStorageManager(context,activity);
        ft.execFileSelect(context,activity,getLanStr(R.string.choice_install_file));
    }

    private void selectLocalDir(){
        permissionRequest.getExternalStorageManager(context,activity);
        ft.execDirSelect(context,activity,getLanStr(R.string.choice_install_file));
    }

    private ArrayList<PKGINFO> getAddedPKGList(){
        ArrayList<PKGINFO> list = new ArrayList<>();
        for (int i = 0; i < checkboxs.size(); i++) {
            PKGINFO pkginfo = pkginfos.get(i);
            if(APP_CHOICES_INDEX == 0){
                if(checkboxs.get(i)){
                    addPKGS(pkginfo,list);
                }
            }

            if(APP_CHOICES_INDEX == 1){
                if(!checkboxs.get(i)){
                    addPKGS(pkginfo,list);
                }
            }

            if(APP_CHOICES_INDEX == 2){
                addPKGS(pkginfo,list);
            }

        }
        return list;
    }

    private void addPKGINFO(PackageManager pm, Uri uri , String storage){
        String path = uri.getPath();
        String filePath = null;
        if(path.indexOf("document/msf") != -1){
            filePath = ft.uriToFilePath(uri,context);
        }else if(path.indexOf("document/primary") != -1){
            filePath = storage + "/" +uri.getPath().replaceAll("/document/primary:","");
        }else{
            filePath=uri.getPath();
        }
        StringTools st = new StringTools();
        String nameType = st.getPathByLastNameType(filePath);
        if(nameType.equals("apk")){
            PackageInfo packageInfo = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            PKGINFO pkginfo = packageUtils.getPKGINFO(pm, packageInfo,filePath);
            if(pkginfo == null){
                File file = new File(filePath);
                if(file.exists()){
                    pkginfo = new PKGINFO(file.getName(),file.getName(),filePath,"-1","-1",file.length());
                    pkginfo.setIconmode(1);
                }
            }else{
                pkginfo.setIconmode(2);
            }
            pkginfo.setApkpath(filePath);
            pkginfo.setFilesize(new File(filePath).length());
            pkginfos.add(pkginfo) ;
            checkboxs.add(false);
        }else if(nameType.equals("apks")){
            String ss = getLanStr(R.string.unknow_msg);
            PKGINFO pkginfo = new PKGINFO(st.getPathByLastName(filePath),ss,filePath,ss,ss, new File(filePath).length());
            pkginfo.setIconmode(1);
            pkginfos.add(pkginfo);
            checkboxs.add(false);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PackageUtils pkgutils = new PackageUtils();
        String storage = ft.getSDPath(uid);
        PackageManager pm = getPackageManager();
        if(requestCode == 0){
            pkgutils.clearList(pkginfos,checkboxs);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && data != null && data.getClipData() != null ) {//有选择多个文件
                ClipData clipData =  data.getClipData();
                int count = clipData.getItemCount();
                for(int i =0;i<count;i++){
                    Uri uri = clipData.getItemAt(i).getUri();
                    addPKGINFO(pm,uri,storage);
                }
            } else if(data != null && data.getData() != null) {//只有一个文件咯
                Uri uri = data.getData();
                addPKGINFO(pm,uri,storage);
            }
            acu.getSd().showPKGS(context,apllv1,pkginfos,checkboxs);
            if(pkginfos.size() > 0){
                install_mode = false;
                amlapplybt.setText(getLanStr(R.string.try_install_app));
            }
        }

        if (requestCode == 101 && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String path = ft.uriToFilePath(uri, context);
            if (path != null) {
                ArrayList<String> importedPkgs = configUtils.importConfig(context, new File(path), mode);
                pkgutils.clearList(pkginfos, checkboxs);
                for (String pkgName : importedPkgs) {
                    try {
                        PKGINFO pkginfo = null;
                        if (mode == AppManagerEnum.APP_RESTORE_UNINSTALLED) {
                            // For uninstalled apps, we can't get full info from PM if they are truly gone, 
                            // but usually they are "kept" or we just need the pkg name for the list.
                            pkginfo = new PKGINFO(pkgName, pkgName, "", "", "", 0L);
                        } else {
                            pkginfo = packageUtils.getPKGINFO(context, pkgName);
                        }

                        if (pkginfo != null) {
                            pkginfos.add(pkginfo);
                            checkboxs.add(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                acu.getSd().showPKGS(context, apllv1, pkginfos, checkboxs);
            }
        }

        //安装文件夹里面所有apk文件
        if(requestCode == 43){
            if(data !=null && data.getData() != null) {//只有一个文件咯
                Uri uri = data.getData();
                String filePath=ft.dirUriToRawFullPath(uri,storage);

                if(isRoot || isADB){
                    pkgutils.clearList(pkginfos,checkboxs);
                    try {
                        ArrayList<File> files = new ArrayList<>();
                        ft.getAllFileByEndName(filePath,".apk",files);
                        for (File listFile : files) {
                            addPKGINFO(pm,Uri.fromFile(listFile),storage);
                        }
                        acu.getSd().showPKGS(context,apllv1,pkginfos,checkboxs);
//                        "正在安装 [ "+filePath+" ] 文件夹里面的内容...","当前正在安装: ";

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    acu.getSd().showInfoMsg(context,getLanStr(R.string.error_tips),getLanStr(R.string.need_root_or_adb_msg));
                }
                if(pkginfos.size() > 0){
                    install_mode = false;
                    amlapplybt.setText(getLanStr(R.string.try_install_app));
                }
            }
        }
    }

    private String getLanStr(int id){
        return acu.getTU().getLanguageString(context,id);
    }

    private String[] getAppPermis(){
        return new String[]{getLanStr(R.string.spin_item_phone_sms)
                ,getLanStr(R.string.spin_item_storage),getLanStr(R.string.spin_item_clipboard)
                ,getLanStr(R.string.spin_item_battery),getLanStr(R.string.spin_item_background)
                ,getLanStr(R.string.spin_item_camera_microphone),getLanStr(R.string.spin_item_location)
                ,getLanStr(R.string.spin_item_calendar),getLanStr(R.string.spin_item_sensor)
                ,getLanStr(R.string.spin_item_notify),getLanStr(R.string.spin_item_biometrics)
                ,getLanStr(R.string.spin_item_alert),getLanStr(R.string.spin_item_accessibility)
                ,getLanStr(R.string.spin_item_account),getLanStr(R.string.spin_item_write_settings)
                ,getLanStr(R.string.spin_item_read_device_id),getLanStr(R.string.spin_item_app_standby)
                ,getLanStr(R.string.spin_item_app_standby_activity)};
    }

    private String[] getAppPermisOPT1(){
        return new String[]{getLanStr(R.string.spin_item_opt1_default),getLanStr(R.string.spin_item_opt1_ignore)
                ,getLanStr(R.string.spin_item_opt1_deny),getLanStr(R.string.spin_item_opt1_allow),getLanStr(R.string.spin_item_opt1_foreground)};
    }

    private String[] getAppPermisOPT2(){
        return new String[]{getLanStr(R.string.spin_item_opt2_active),getLanStr(R.string.spin_item_opt2_work)
                ,getLanStr(R.string.spin_item_opt2_frequent),getLanStr(R.string.spin_item_opt2_rare),getLanStr(R.string.spin_item_opt2_restricted)};
    }

    private String[] getAppPermisOPT3(){
        return new String[]{getLanStr(R.string.spin_item_opt3_allow),getLanStr(R.string.spin_item_opt3_denial)};
    }

    private String[] getAppCleanOPT() {
        return new String[]{getString(R.string.clean_opt_item_clean),getString(R.string.clean_opt_item_background_clean)};
    }

    private String[] getAppDisableOrEnableOPT(){
        return new String[]{getLanStr(R.string.spin_item_enable),getLanStr(R.string.spin_item_disable)};
    }
    private String[] getAppFirewallOPT(){
        return new String[]{getLanStr(R.string.spin_item_network_enable),getLanStr(R.string.spin_item_network_disable)};
    }
    private String[] getAppInstallLocalFileOPT(){
        return new String[]{getLanStr(R.string.spin_item_select_local_file),getLanStr(R.string.spin_item_select_local_dir)};
    }
    private String[] getAppDumpOPT(){
        return new String[]{getLanStr(R.string.spin_item_dump_pkgname),getLanStr(R.string.spin_item_dump_appname),getLanStr(R.string.spin_item_dump_timename)};
    }
    private String[] getAppChoicesOPT(){
        return new String[]{getLanStr(R.string.spin_item_selected),getLanStr(R.string.spin_item_no_selected),getLanStr(R.string.spin_item_all_selected)};
    }


    private void addPKGS(PKGINFO pkginfo,ArrayList<PKGINFO> list){
        list.add(pkginfo);
    }

    private String getADDCleanRunningAPPOPTSTR(){
        return APP_CLEAN_TIME + "-" + APP_CLEAN_TIME_TYPE;
    }

}
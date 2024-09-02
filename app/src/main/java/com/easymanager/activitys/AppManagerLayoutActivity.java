package com.easymanager.activitys;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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

import com.easymanager.R;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.enums.AppManagerEnum;
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

public class AppManagerLayoutActivity extends Activity {

    private ArrayList<PKGINFO> pkginfos = new ArrayList<>();
    private ArrayList<Boolean> checkboxs = new ArrayList<>();
    private Button amlapplybt;
    private Spinner amlsp1,amlsp2,amlsp3,amlsp4,amlsp5;
    private ListView apllv1;

    private int mode;

    private Context context;
    private Activity activity;
    private Boolean isRoot , isADB;
    private Integer uid;
    private String APP_PERMIS_OPT1_VALUES[] = {"default", "ignore","deny","allow","foreground"};
    private String APP_PERMIS_OPT2_VALUES[] = {"active", "working_set","frequent","rare","restricted"};
    private String APP_PERMIS_OPT3_VALUES[] = {"true","false"};
    private String APP_BACKUP_AND_RESTORY_OPT2[] = {"tar.xz","tar.gz","tar.bz"};
    private String APP_BACKUP_AND_RESTORY_OPT_VALUES[] = {"full","data","apk"};
    private String APP_BACKUP_AND_RESTORY_OPT2_VALUES[] = {"txz","tgz","tbz"};
    private String APP_ALL_UERS[] = null;
    private int nowItemIndex=-1;
    private int APP_UID_INDEX=0,APP_CHOICES_INDEX = 0, APP_PERMIS_INDEX = 0 , APP_PERMIS_OPT_INDEX = 0, APP_PERMIS_MODE= 0 ;

    private boolean install_mode = false;
    private AppCloneUtils acu = new AppCloneUtils();
    private PackageUtils packageUtils = acu.getPd().packageUtils;
    private FileTools ft = acu.getPd().ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_manager_layout);
        MyActivityManager.getIns().add(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initBt();
    }

    private void initBt(){
        easyManagerUtils ee = acu.getEasyManagerUtils();
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode",-1);
        isRoot = intent.getBooleanExtra("isRoot",false);
        isADB = intent.getBooleanExtra("isADB",false);
        uid = intent.getIntExtra("uid",0);
        context = this;
        activity = this;
        amlapplybt = findViewById(R.id.amlapplybt);
        amlsp1 = findViewById(R.id.amlsp1);
        amlsp2 = findViewById(R.id.amlsp2);
        amlsp3 = findViewById(R.id.amlsp3);
        amlsp4 = findViewById(R.id.amlsp4);
        amlsp5 = findViewById(R.id.amlsp5);
        apllv1 = findViewById(R.id.apllv1);
        amlsp1.setAdapter(getSpinnerAdapter(getAppPermis()));
        amlsp3.setAdapter(getSpinnerAdapter(getAppChoicesOPT()));
        amlsp5.setAdapter(getSpinnerAdapter(getAppManagerOPT()));
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

        if(mode == AppManagerEnum.APP_INSTALL_LOCAL_FILE){
            amlsp1.setEnabled(false);
            amlsp5.setEnabled(false);
            amlsp2.setAdapter(getSpinnerAdapter(getAppInstallLocalFileOPT()));
            amlapplybt.setText(getLanStr(R.string.button_select));
            install_mode=true;
        }


        if(mode == AppManagerEnum.APP_BACKUP){
            amlsp5.setEnabled(false);
            amlsp1.setAdapter(getSpinnerAdapter(getAppBackupAndRestoryOPT()));
            amlsp2.setAdapter(getSpinnerAdapter(APP_BACKUP_AND_RESTORY_OPT2));
            amlapplybt.setText(getLanStr(R.string.button_backup));
        }

        if(mode == AppManagerEnum.APP_RESTORY){
            amlsp5.setEnabled(false);
            amlsp1.setAdapter(getSpinnerAdapter(getAppBackupAndRestoryOPT()));
            amlsp2.setAdapter(getSpinnerAdapter(APP_BACKUP_AND_RESTORY_OPT2));
            amlapplybt.setText(getLanStr(R.string.button_restory));
        }
        btClicked();

    }

    private void btClicked(){

        spinnerChange(amlsp1,0);
        spinnerChange(amlsp2,1);
        spinnerChange(amlsp3,2);
        spinnerChange(amlsp4,3);
        spinnerChange(amlsp5,4);

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
                    }

                    if(mode == AppManagerEnum.APP_FIREWALL){
                        acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_FIREWALL,APP_PERMIS_OPT_INDEX,null,uid);
                    }

                    if(mode == AppManagerEnum.APP_INSTALL_LOCAL_FILE){
                        acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_INSTALL_LOCAL_FILE,APP_PERMIS_OPT_INDEX,null,uid);
                    }

                    if(mode == AppManagerEnum.APP_DUMP){
                        acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_DUMP,APP_PERMIS_OPT_INDEX,null,uid);
                    }

                    if(mode == AppManagerEnum.APP_UNINSTALL){
                        acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_UNINSTALL,APP_PERMIS_OPT_INDEX,null,uid);
                        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
                            easyManagerUtils ee = new easyManagerUtils();
                            if(ee.isROOT()){
                                ee.activeRoot(context);
                            }else {
                                acu.getPd().showInfoMsg(context,getLanStr(R.string.error_tips),getLanStr(R.string.del_app_tips_6));
                            }
                        }
                    }

                    if(mode == AppManagerEnum.APP_CLEAN_PROCESS){
                        acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_CLEAN_PROCESS,APP_PERMIS_OPT_INDEX,null,uid);
                    }

                    if(mode == AppManagerEnum.APP_BACKUP){
                        String opt_str = APP_BACKUP_AND_RESTORY_OPT_VALUES[APP_PERMIS_INDEX]+"---"+APP_BACKUP_AND_RESTORY_OPT2_VALUES[APP_PERMIS_OPT_INDEX];
                        acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_BACKUP,(uid == null  ? 0 : uid),opt_str,uid);
                    }

                    if(mode == AppManagerEnum.APP_RESTORY){
                        String opt_str = APP_BACKUP_AND_RESTORY_OPT_VALUES[APP_PERMIS_INDEX]+"---"+APP_BACKUP_AND_RESTORY_OPT2_VALUES[APP_PERMIS_OPT_INDEX];
                        acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_RESTORY,(uid == null ? 0 : uid),opt_str,uid);
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
                    startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(context, getLanStr(R.string.not_installed_app), Toast.LENGTH_SHORT).show();
                }
            }
        });

        apllv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                nowItemIndex=i;
                createLVMenu();
                return false;
            }
        });


    }


    private void createLVMenu(){
        apllv1.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0,0,0,"复制信息");
                contextMenu.add(0,1,0,"跳转至应用详情");
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case 0:
                acu.getUd().tu.copyText(context,pkginfos.get(nowItemIndex).toString());
                break;
            case 1:
                Intent intent2 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent2.setData(Uri.parse("package:" + pkginfos.get(nowItemIndex).getPkgname()));
                startActivity(intent2);
                break;
        }

        return super.onContextItemSelected(item);
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
                        break;
                    case 2:
                        APP_CHOICES_INDEX = i;
                        break;
                    case 3:
                        APP_UID_INDEX = i;
                        uid = Integer.valueOf(APP_ALL_UERS[APP_UID_INDEX]);
                        break;
                    case 4:
                        if(amlsp5.isEnabled()){
                            switch (i){
                                case 0:
                                    mode = AppManagerEnum.APP_PERMISSION;
                                    amlsp1.setEnabled(true);
                                    amlsp2.setEnabled(true);
                                    amlsp2.setAdapter(getSpinnerAdapter(getAppPermisOPT1()));
                                    break;
                                case 1:
                                    mode = AppManagerEnum.APP_DISABLE_COMPENT;
                                    amlsp1.setEnabled(false);
                                    amlsp2.setEnabled(true);
                                    amlsp2.setAdapter(getSpinnerAdapter(getAppDisableOrEnableOPT()));
                                    break;
                                case 2:
                                    mode = AppManagerEnum.APP_FIREWALL;
                                    amlsp1.setEnabled(false);
                                    amlsp2.setEnabled(true);
                                    amlsp2.setAdapter(getSpinnerAdapter(getAppFirewallOPT()));
                                    break;
                                case 3:
                                    mode = AppManagerEnum.APP_DUMP;
                                    amlsp1.setEnabled(false);
                                    amlsp2.setEnabled(true);
                                    amlsp2.setAdapter(getSpinnerAdapter(getAppDumpOPT()));
                                    amlapplybt.setText(getLanStr(R.string.button_dump_app));
                                    break;
                                case 4:
                                    mode = AppManagerEnum.APP_UNINSTALL;
                                    amlsp1.setEnabled(false);
                                    amlsp2.setEnabled(false);
                                    amlapplybt.setText(getLanStr(R.string.button_del_app));
                                    break;
                                case 5:
                                    mode = AppManagerEnum.APP_CLEAN_PROCESS;
                                    amlsp1.setEnabled(false);
                                    amlsp2.setEnabled(false);
                                    amlapplybt.setText(getLanStr(R.string.button_clean));
                                    break;
                            }
                            packageUtils.clearList(pkginfos,checkboxs);
                            acu.getUd().showPKGS(context,apllv1,pkginfos,checkboxs);
                            new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_MANAGE_HELP,mode);
                        }
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
        OtherTools otherTools = new OtherTools();
        otherTools.addMenuBase(this,menu,mode);
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.actionbarsearch){
            if(mode == AppManagerEnum.APP_CLEAN_PROCESS){
                acu.getSd().showProcessSearchViewDialog(context,activity,apllv1,pkginfos,null,checkboxs,uid);
            }else if(mode == AppManagerEnum.APP_RESTORY){
                acu.getSd().showRestorySearchViewDialog(context,activity,apllv1,pkginfos,checkboxs,uid);
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
                    pkginfo = new PKGINFO(file.getName(),file.getName(),filePath,"-1","-1",context.getResources().getDrawable(R.drawable.manager_grant_app_foreground),file.length());
                }
            }
            pkginfo.setApkpath(filePath);
            pkginfo.setFilesize(new File(filePath).length());
            pkginfos.add(pkginfo) ;
            checkboxs.add(false);
        }else if(nameType.equals("apks")){
            String ss = getLanStr(R.string.unknow_msg);
            Drawable d = context.getResources().getDrawable(R.drawable.manager_grant_app_foreground);
            pkginfos.add(new PKGINFO(st.getPathByLastName(filePath),ss,filePath,ss,ss, d,new File(filePath).length()));
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
    private String[] getAppBackupAndRestoryOPT(){
        return new String[]{getLanStr(R.string.spin_item_app_and_data),getLanStr(R.string.spin_item_data),getLanStr(R.string.spin_item_app)};
    }
    private String[] getAppChoicesOPT(){
        return new String[]{getLanStr(R.string.spin_item_selected),getLanStr(R.string.spin_item_no_selected),getLanStr(R.string.spin_item_all_selected)};
    }

    private String[] getAppManagerOPT(){
        return new String[]{getLanStr(R.string.button_home_permission),getLanStr(R.string.button_home_disable),getLanStr(R.string.button_home_network),getLanStr(R.string.button_home_dump_app),getLanStr(R.string.button_home_uninstall),getLanStr(R.string.button_home_process_clean)};
    }

    private void addPKGS(PKGINFO pkginfo,ArrayList<PKGINFO> list){
        if(mode == AppManagerEnum.APP_RESTORY){
            String appname = pkginfo.getAppname();
            String trim = pkginfo.getPkgname().trim();
            String end = APP_BACKUP_AND_RESTORY_OPT2[APP_PERMIS_OPT_INDEX];
            String old_backup_file = trim+"."+end;
            String user_backup_file = trim+"-"+uid+"."+end;
            if(appname.equals(old_backup_file) || appname.equals(user_backup_file)){
                list.add(pkginfo);
            }
        }else {
            list.add(pkginfo);
        }
    }

}
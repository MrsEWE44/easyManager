package com.easymanager.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.easymanager.utils.DialogUtils;
import com.easymanager.utils.FileTools;
import com.easymanager.utils.HelpDialogUtils;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.OtherTools;
import com.easymanager.utils.PackageUtils;
import com.easymanager.utils.StringTools;
import com.easymanager.utils.TextUtils;
import com.easymanager.utils.easyManagerUtils;
import com.easymanager.utils.permissionRequest;

import java.io.File;
import java.util.ArrayList;

public class AppManagerLayoutActivity extends Activity {

    private ArrayList<PKGINFO> pkginfos = new ArrayList<>();
    private ArrayList<Boolean> checkboxs = new ArrayList<>();
    private Button amlapplybt;
    private Spinner amlsp1,amlsp2,amlsp3;
    private ListView apllv1;

    private int mode;

    private Context context;
    private Activity activity;
    private Boolean isRoot , isADB;
    private String uid;
    private String APP_PERMIS_OPT1_VALUES[] = {"default", "ignore","deny","allow","foreground"};
    private String APP_PERMIS_OPT2_VALUES[] = {"active", "working_set","frequent","rare","restricted"};
    private String APP_PERMIS_OPT3_VALUES[] = {"true","false"};
    private String APP_BACKUP_AND_RESTORY_OPT2[] = {"tar.xz","tar.gz","tar.bz"};
    private String APP_BACKUP_AND_RESTORY_OPT_VALUES[] = {"full","data","apk"};
    private String APP_BACKUP_AND_RESTORY_OPT2_VALUES[] = {"txz","tgz","tbz"};
    private int APP_CHOICES_INDEX = 0, APP_PERMIS_INDEX = 0 , APP_PERMIS_OPT_INDEX = 0, APP_PERMIS_MODE= 0 ;

    private boolean install_mode = false;
    private DialogUtils du = new DialogUtils();
    private FileTools ft = new FileTools();

    private PackageUtils packageUtils = new PackageUtils();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_manager_layout);
        MyActivityManager.getIns().add(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initBt();
    }

    private void initBt(){
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode",-1);
        isRoot = intent.getBooleanExtra("isRoot",false);
        isADB = intent.getBooleanExtra("isADB",false);
        uid = "0";
        context = this;
        activity = this;
        amlapplybt = findViewById(R.id.amlapplybt);
        amlsp1 = findViewById(R.id.amlsp1);
        amlsp2 = findViewById(R.id.amlsp2);
        amlsp3 = findViewById(R.id.amlsp3);
        apllv1 = findViewById(R.id.apllv1);
        amlsp1.setAdapter(getSpinnerAdapter(getAppPermis()));
        amlsp3.setAdapter(getSpinnerAdapter(getAppChoicesOPT()));
        if(mode == AppManagerEnum.APP_PERMISSION){
            amlsp2.setAdapter(getSpinnerAdapter(getAppPermisOPT1()));
        }
        if(mode == AppManagerEnum.APP_DISABLE_COMPENT){
            amlsp1.setEnabled(false);
            amlsp2.setAdapter(getSpinnerAdapter(getAppDisableOrEnableOPT()));
        }

        if(mode == AppManagerEnum.APP_FIREWALL){
            amlsp1.setEnabled(false);
            amlsp2.setAdapter(getSpinnerAdapter(getAppFirewallOPT()));
        }

        if(mode == AppManagerEnum.APP_INSTALL_LOCAL_FILE){
            amlsp1.setEnabled(false);
            amlsp2.setAdapter(getSpinnerAdapter(getAppInstallLocalFileOPT()));
            amlapplybt.setText(getLanStr(R.string.button_select));
            install_mode=true;
        }

        if(mode == AppManagerEnum.APP_DUMP){
            amlsp1.setEnabled(false);
            amlsp2.setAdapter(getSpinnerAdapter(getAppDumpOPT()));
            amlapplybt.setText(getLanStr(R.string.button_dump_app));
        }

        if(mode == AppManagerEnum.APP_UNINSTALL){
            amlsp1.setEnabled(false);
            amlsp2.setEnabled(false);
            amlapplybt.setText(getLanStr(R.string.button_del_app));
        }

        if(mode == AppManagerEnum.APP_CLEAN_PROCESS){
            amlsp1.setEnabled(false);
            amlsp2.setEnabled(false);
            amlapplybt.setText(getLanStr(R.string.button_clean));
        }

        if(mode == AppManagerEnum.APP_BACKUP){
            amlsp1.setAdapter(getSpinnerAdapter(getAppBackupAndRestoryOPT()));
            amlsp2.setAdapter(getSpinnerAdapter(APP_BACKUP_AND_RESTORY_OPT2));
            amlapplybt.setText(getLanStr(R.string.button_backup));
        }

        if(mode == AppManagerEnum.APP_RESTORY){
            amlsp1.setAdapter(getSpinnerAdapter(getAppBackupAndRestoryOPT()));
            amlsp2.setAdapter(getSpinnerAdapter(APP_BACKUP_AND_RESTORY_OPT2));
            amlapplybt.setText(getLanStr(R.string.button_restory));
        }
        btClicked();
        new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_MANAGE_HELP,mode);
    }

    private void btClicked(){

        spinnerChange(amlsp1,0);
        spinnerChange(amlsp2,1);
        spinnerChange(amlsp3,2);
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
                                if(mode == AppManagerEnum.APP_RESTORY){
                                    if(pkginfo.getAppname().equals(pkginfo.getPkgname().trim()+"."+APP_BACKUP_AND_RESTORY_OPT2[APP_PERMIS_OPT_INDEX])){
                                        list.add(pkginfo);
                                    }
                                }else {
                                    list.add(pkginfo);
                                }

                            }
                        }

                        if(APP_CHOICES_INDEX == 1){
                            if(!checkboxs.get(i)){
                                if(mode == AppManagerEnum.APP_RESTORY){
                                    if(pkginfo.getAppname().equals(pkginfo.getPkgname().trim()+"."+APP_BACKUP_AND_RESTORY_OPT2[APP_PERMIS_OPT_INDEX])){
                                        list.add(pkginfo);
                                    }
                                }else {
                                    list.add(pkginfo);
                                }
                            }
                        }

                        if(APP_CHOICES_INDEX == 2){
                            if(mode == AppManagerEnum.APP_RESTORY){
                                if(pkginfo.getAppname().equals(pkginfo.getPkgname().trim()+"."+APP_BACKUP_AND_RESTORY_OPT2[APP_PERMIS_OPT_INDEX])){
                                    list.add(pkginfo);
                                }
                            }else {
                                list.add(pkginfo);
                            }
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
                            du.showInfoMsg(context,getLanStr(R.string.tips),getLanStr(R.string.not_support_5));
                        }else{
                            du.showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_PERMISSION,APP_PERMIS_INDEX,opt_str);
                        }
                    }

                    if(mode == AppManagerEnum.APP_DISABLE_COMPENT){
                        du.showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_DISABLE_COMPENT,APP_PERMIS_OPT_INDEX,null);
                    }

                    if(mode == AppManagerEnum.APP_FIREWALL){
                        du.showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_FIREWALL,APP_PERMIS_OPT_INDEX,null);
                    }

                    if(mode == AppManagerEnum.APP_INSTALL_LOCAL_FILE){
                        du.showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_INSTALL_LOCAL_FILE,APP_PERMIS_OPT_INDEX,null);
                    }

                    if(mode == AppManagerEnum.APP_DUMP){
                        du.showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_DUMP,APP_PERMIS_OPT_INDEX,null);
                    }

                    if(mode == AppManagerEnum.APP_UNINSTALL){
                        du.showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_UNINSTALL,APP_PERMIS_OPT_INDEX,null);
                        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
                            easyManagerUtils ee = new easyManagerUtils();
                            if(ee.isROOT()){
                                ee.activeRoot(context);
                            }else {
                                du.showInfoMsg(context,getLanStr(R.string.error_tips),getLanStr(R.string.del_app_tips_6));
                            }
                        }
                    }

                    if(mode == AppManagerEnum.APP_CLEAN_PROCESS){
                        du.showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_CLEAN_PROCESS,APP_PERMIS_OPT_INDEX,null);
                    }

                    if(mode == AppManagerEnum.APP_BACKUP){
                        String opt_str = APP_BACKUP_AND_RESTORY_OPT_VALUES[APP_PERMIS_INDEX]+"---"+APP_BACKUP_AND_RESTORY_OPT2_VALUES[APP_PERMIS_OPT_INDEX];
                        du.showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_BACKUP,(uid == null || uid.isEmpty() ? 0 : Integer.valueOf(uid.trim())),opt_str);
                    }

                    if(mode == AppManagerEnum.APP_RESTORY){
                        String opt_str = APP_BACKUP_AND_RESTORY_OPT_VALUES[APP_PERMIS_INDEX]+"---"+APP_BACKUP_AND_RESTORY_OPT2_VALUES[APP_PERMIS_OPT_INDEX];
                        du.showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_RESTORY,(uid == null || uid.isEmpty() ? 0 : Integer.valueOf(uid.trim())),opt_str);
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


    }

    private void spinnerChange(Spinner s,int app_opt_mode){
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
        int itemId = item.getItemId();
        if(itemId == R.id.actionbarsearch){
            if(mode == AppManagerEnum.APP_CLEAN_PROCESS){
                du.showProcessSearchViewDialog(context,activity,apllv1,pkginfos,null,checkboxs);
            }else if(mode == AppManagerEnum.APP_RESTORY){
                du.showRestorySearchViewDialog(context,activity,apllv1,pkginfos,checkboxs);
            }else {
                du.showSearchViewDialog(context,activity,apllv1,pkginfos,null,checkboxs);
            }

        }
        if(itemId == android.R.id.home){
            activity.onBackPressed();
        }
        if(itemId == 0){
            du.queryAllPKGSProcessDialog(context,activity,apllv1,pkginfos,checkboxs);
        }

        if(itemId == 1){
            du.queryAllEnablePKGSProcessDialog(context,activity,apllv1,pkginfos,checkboxs);
        }

        if(itemId == 2){
            du.queryUserAllPKGSProcessDialog(context,activity,apllv1,pkginfos,checkboxs);
        }

        if(itemId == 3){
            du.queryUserEnablePKGSProcessDialog(context,activity,apllv1,pkginfos,checkboxs);
        }

        if(itemId == 4){
            du.queryAllDisablePKGSProcessDialog(context,activity,apllv1,pkginfos,checkboxs);
        }

        if(itemId == 7){
            du.queryAllProcessDialog(context,activity,apllv1,pkginfos,checkboxs);
        }

        if(itemId == 8){
            du.queryAllUserProcessDialog(context,activity,apllv1,pkginfos,checkboxs);
        }

        if(itemId == 9){
            du.queryLocalBackupProcessDialog(context,activity,apllv1,pkginfos,checkboxs);
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
        permissionRequest.getExternalStorageManager(context);
        ft.execFileSelect(context,activity,getLanStr(R.string.choice_install_file));
    }

    private void selectLocalDir(){
        permissionRequest.getExternalStorageManager(context);
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
        String storage = ft.getSDPath();
        PackageManager pm = getPackageManager();
        if(requestCode == 0){
            pkgutils.clearList(pkginfos,checkboxs);
            if(data != null && data.getClipData() != null) {//有选择多个文件
                int count = data.getClipData().getItemCount();
                for(int i =0;i<count;i++){
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    addPKGINFO(pm,uri,storage);
                }
            } else if(data != null && data.getData() != null) {//只有一个文件咯
                Uri uri = data.getData();
                addPKGINFO(pm,uri,storage);
            }
            du.showPKGS(context,apllv1,pkginfos,checkboxs);
            if(pkginfos.size() > 0){
                install_mode = false;
                amlapplybt.setText(getLanStr(R.string.try_install_app));
            }
        }

        //安装文件夹里面所有apk文件
        if(requestCode == 43){
            if(data !=null && data.getData() != null) {//只有一个文件咯
                Uri uri = data.getData();
                String path = uri.getPath();
                String filePath=null;
                if(path.indexOf("tree/primary") != -1){
                    filePath = storage + "/" +path.replaceAll("/tree/primary:","");
                }else if(path.indexOf("document/primary") != -1){
                    filePath = storage + "/" +path.replaceAll("/document/primary:","");
                    filePath = new File(filePath).getParent();
                }else{
                    filePath = new File(path).getParent();
                }
                if(isRoot || isADB){
                    pkgutils.clearList(pkginfos,checkboxs);
                    try {
                        ArrayList<File> files = new ArrayList<>();
                        ft.getAllFileByEndName(filePath,".apk",files);
                        for (File listFile : files) {
                            addPKGINFO(pm,Uri.fromFile(listFile),storage);
                        }
                        du.showPKGS(context,apllv1,pkginfos,checkboxs);
//                        "正在安装 [ "+filePath+" ] 文件夹里面的内容...","当前正在安装: ";

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    du.showInfoMsg(context,getLanStr(R.string.error_tips),getLanStr(R.string.need_root_or_adb_msg));
                }
                if(pkginfos.size() > 0){
                    install_mode = false;
                    amlapplybt.setText(getLanStr(R.string.try_install_app));
                }
            }
        }
    }

    private String getLanStr(int id){
        return du.tu.getLanguageString(context,id);
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

}

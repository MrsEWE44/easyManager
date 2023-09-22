package com.easymanager.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.easymanager.R;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.enums.AppInfoEnums;
import com.easymanager.enums.AppManagerEnum;
import com.easymanager.utils.DialogUtils;
import com.easymanager.utils.FileTools;
import com.easymanager.utils.HelpDialogUtils;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.OtherTools;
import com.easymanager.utils.PackageUtils;
import com.easymanager.utils.StringTools;
import com.easymanager.utils.easyManagerUtils;
import com.easymanager.utils.permissionRequest;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

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
    private String APP_PERMIS[] = {"通话/短信相关", "存储","剪切板","电池优化","后台运行","摄像头/麦克风","定位","日历","传感器扫描","通知","强制应用待机","应用待机活动"};
    private String APP_PERMIS_OPT1[] = {"默认", "拒绝","允许","仅在运行时允许"};
    private String APP_PERMIS_OPT1_VALUES[] = {"default", "ignore","allow","foreground"};

    private String APP_PERMIS_OPT2[] = {"活跃", "工作集","常用","极少使用","受限"};
    private String APP_PERMIS_OPT2_VALUES[] = {"active", "working_set","frequent","rare","restricted"};
    private String APP_PERMIS_OPT3[] = {"允许","拒绝"};
    private String APP_PERMIS_OPT3_VALUES[] = {"true","false"};
    private String APP_DISABLE_OR_ENABLE_OPT[] = {"启用","禁用"};
    private String APP_FIREWALL_OPT[] = {"允许联网","禁止联网"};
    private String APP_INSTALL_LOCAL_FILE_OPT[] = {"选择本地安装包文件","选择安装包本地文件夹"};
    private String APP_DUMP_OPT[] = {"以包名保存","以应用名字保存","以当前时间保存"};
    private String APP_BACKUP_AND_RESTORY_OPT[] = {"数据+应用","仅数据","仅应用"};
    private String APP_BACKUP_AND_RESTORY_OPT2[] = {"tar.xz","tar.gz","tar.bz"};
    private String APP_BACKUP_AND_RESTORY_OPT_VALUES[] = {"full","data","apk"};
    private String APP_BACKUP_AND_RESTORY_OPT2_VALUES[] = {"txz","tgz","tbz"};

    private String APP_CHOICES[] = {"勾选","未勾选","全选"};

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
        amlsp1.setAdapter(getSpinnerAdapter(APP_PERMIS));
        amlsp3.setAdapter(getSpinnerAdapter(APP_CHOICES));
        if(mode == AppManagerEnum.APP_PERMISSION){
            amlsp2.setAdapter(getSpinnerAdapter(APP_PERMIS_OPT1));
        }
        if(mode == AppManagerEnum.APP_DISABLE_COMPENT){
            amlsp1.setEnabled(false);
            amlsp2.setAdapter(getSpinnerAdapter(APP_DISABLE_OR_ENABLE_OPT));
        }

        if(mode == AppManagerEnum.APP_FIREWALL){
            amlsp1.setEnabled(false);
            amlsp2.setAdapter(getSpinnerAdapter(APP_FIREWALL_OPT));
        }

        if(mode == AppManagerEnum.APP_INSTALL_LOCAL_FILE){
            amlsp1.setEnabled(false);
            amlsp2.setAdapter(getSpinnerAdapter(APP_INSTALL_LOCAL_FILE_OPT));
            amlapplybt.setText("选择");
            install_mode=true;
        }

        if(mode == AppManagerEnum.APP_DUMP){
            amlsp1.setEnabled(false);
            amlsp2.setAdapter(getSpinnerAdapter(APP_DUMP_OPT));
            amlapplybt.setText("提取应用");
        }

        if(mode == AppManagerEnum.APP_UNINSTALL){
            amlsp1.setEnabled(false);
            amlsp2.setEnabled(false);
            amlapplybt.setText("卸载应用");
        }

        if(mode == AppManagerEnum.APP_CLEAN_PROCESS){
            amlsp1.setEnabled(false);
            amlsp2.setEnabled(false);
            amlapplybt.setText("清理");
        }

        if(mode == AppManagerEnum.APP_BACKUP){
            amlsp1.setAdapter(getSpinnerAdapter(APP_BACKUP_AND_RESTORY_OPT));
            amlsp2.setAdapter(getSpinnerAdapter(APP_BACKUP_AND_RESTORY_OPT2));
            amlapplybt.setText("备份");
        }

        if(mode == AppManagerEnum.APP_RESTORY){
            amlsp1.setAdapter(getSpinnerAdapter(APP_BACKUP_AND_RESTORY_OPT));
            amlsp2.setAdapter(getSpinnerAdapter(APP_BACKUP_AND_RESTORY_OPT2));
            amlapplybt.setText("恢复");
        }
        btClicked();
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
                            du.showInfoMsg(context,"提示","不支持安卓5以下设备");
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
                                du.showInfoMsg(context,"错误","当前是安卓6以下，卸载软件会被终止掉后台服务，请手动重新激活(root模式下可以忽略该问题)");
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
                    Toast.makeText(context, "未安装该应用", Toast.LENGTH_SHORT).show();
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
                            if(APP_PERMIS_INDEX == 10){
                                APP_PERMIS_MODE=1;
                                amlsp2.setAdapter(getSpinnerAdapter(APP_PERMIS_OPT3));
                            }else if(APP_PERMIS_INDEX == 11){
                                APP_PERMIS_MODE=2;
                                amlsp2.setAdapter(getSpinnerAdapter(APP_PERMIS_OPT2));
                            }else{
                                APP_PERMIS_MODE=0;
                                amlsp2.setAdapter(getSpinnerAdapter(APP_PERMIS_OPT1));
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
        otherTools.addMenuBase(menu,mode);
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
        ft.execFileSelect(context,activity,"请选择要安装的文件");
    }

    private void selectLocalDir(){
        permissionRequest.getExternalStorageManager(context);
        ft.execDirSelect(context,activity,"请选择要安装的文件");
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
            PKGINFO pkginfo = packageUtils.getPKGINFO(pm, packageInfo);
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
            Drawable d = context.getResources().getDrawable(R.drawable.manager_grant_app_foreground);
            pkginfos.add(new PKGINFO(st.getPathByLastName(filePath),"未知",filePath,"未知","未知", d,new File(filePath).length()));
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
                amlapplybt.setText("安装");
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
                    du.showInfoMsg(context,"错误","该功能需要adb或者root权限才能使用!!!!");
                }
                if(pkginfos.size() > 0){
                    install_mode = false;
                    amlapplybt.setText("安装");
                }
            }
        }
    }
}

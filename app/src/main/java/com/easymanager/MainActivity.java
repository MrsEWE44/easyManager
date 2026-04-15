package com.easymanager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.core.utils.CMD;
import com.easymanager.fragment.HelpFragmentLayout;
import com.easymanager.fragment.HomeFragmentLayout;
import com.easymanager.fragment.ManagerGrantUserFragmentLayout;
import com.easymanager.utils.FileTools;
import com.easymanager.utils.ShellUtils;
import com.easymanager.utils.dialog.HelpDialogUtils;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.TextUtils;
import com.easymanager.utils.easyManagerUtils;
import com.easymanager.utils.permissionRequest;
import com.easymanager.activitys.BaseActivity;

public class MainActivity extends BaseActivity {

    private View amv1,amv2,amv3;
    private FragmentManager fragmentManager;
    private Fragment homeFragment , helpFragment , manageFragment , currentFragment;
    private boolean isRoot,isADB,isDevice;
    private int uid;
    private easyManagerUtils ee = new easyManagerUtils();
    private HelpDialogUtils dialogUtils = new HelpDialogUtils();
    private TextUtils tu = dialogUtils.tu;
    private android.app.AlertDialog currentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //修复软件崩溃后，出现页面重叠的问题
        fragmentManager = getFragmentManager();
        if(savedInstanceState != null){
            helpFragment = fragmentManager.findFragmentByTag("help");
            manageFragment = fragmentManager.findFragmentByTag("manager");
            homeFragment = fragmentManager.findFragmentByTag("home");
            showFragment(helpFragment);
        }
        initView();
        if (permissionRequest.hasStoragePermission(this)) {
            checkInitialStatus();
        } else {
            showInitialDialogs();
        }
//        new NetUtilsDialog().checkupdate(this);
    }

    private void showInitialDialogs() {
        // 1. 显示帮助弹窗
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(R.string.show_help_title);
        builder.setMessage(R.string.show_help_getMainHelp);
        builder.setPositiveButton(R.string.dialog_sure_text, new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                showPermissionGuideDialog();
            }
        });
        builder.setCancelable(false);
        currentDialog = builder.show();
    }

    private void showPermissionGuideDialog() {
        // 2. 显示权限引导弹窗
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_guide_title);
        builder.setMessage(R.string.permission_guide_msg);
        builder.setPositiveButton(R.string.dialog_sure_text, new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                // 3. 执行权限申请
                permissionRequest.requestExternalStoragePermission(MainActivity.this);
                permissionRequest.getExternalStorageManager(MainActivity.this, MainActivity.this);
                // 4. 权限申请弹窗消失后（此处由于系统权限弹窗是异步的，通常引导弹窗消失后系统弹窗就会出现）
                // 激活脚本弹窗在权限处理后通过 initView 中的逻辑触发，或者在此处衔接
                checkInitialStatus();
            }
        });
        builder.setCancelable(false);
        currentDialog = builder.show();
    }

    private void checkInitialStatus() {
        ShellUtils shellUtils = new ShellUtils();
        FileTools fileUtils = new FileTools();
        isRoot = false;
        isADB = false;
        isDevice = ee.isDeviceOwnerActive(this);
        if(!isDevice){

            isRoot = shellUtils.testRoot();
            if (isRoot) {
                if (!ee.isROOT()) {
                    ee.activeRoot(this);
                    // 循环判断服务是否启动，最多等待2秒
                    for (int i = 0; i < 10; i++) {
                        if (ee.getServerStatus()) {
                            break;
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            isRoot = ee.isROOT();

            if (!isRoot) {
                if(isDevice && !ee.getServerStatus()){
                    isDevice = ee.isDeviceOwnerActive(this);
                }else if(isDevice && ee.getServerStatus()){
                    isRoot = ee.isROOT();
                    isADB = ee.isADB();
                    isDevice = ee.isDeviceOwnerActive(this);
                }else if(ee.getServerStatus()){
                    isRoot = ee.isROOT();
                    isADB = ee.isADB();
                }else {
                    // 5. 显示激活脚本弹窗
                    String name = "start.sh";
                    String path = null;
                    try {
                        path = this.getExternalCacheDir().toString();
                        fileUtils.writeActiveADBScript(this,path,name);

                    }catch (Exception e){
                        try {
                            path = Environment.getExternalStorageDirectory().toString();
                            fileUtils.writeDataToPath(fileUtils.getActiveADBScript(this),path+"/start.sh",false);
                        }catch (Exception e2){
                            path=fileUtils.getActiveADBScript(this);
                            name="";
                        }
                    }
                    currentDialog = dialogUtils.showInfoMsg(this,tu.getLanguageString(this,R.string.general_tips),tu.getLanguageString(this,R.string.general_tips_str_head)+path+"/"+name+"\n\n"+tu.getLanguageString(this,R.string.general_tips_str_end));
                }
            }
        }
        
        updateUIStatus();
        if(isRoot || isADB){
            ee.requestGrantUser(this);
            ee.startStopRunningAPP(new TransmissionEntity(null,null,this.getPackageName(),-1,uid));
        }
    }

    private boolean isRunning = true;

    private void startStatusUpdateTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    checkAndUpdateStatus();
                    try {
                        Thread.sleep(3000); // 每3秒检查一次
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void checkAndUpdateStatus() {
        boolean serverStatus = ee.getServerStatus();

        // 如果检测到服务掉线，且设备具备 Root 权限，则尝试自动重新激活
        if (!serverStatus) {
            ShellUtils shellUtils = new ShellUtils();
            if (shellUtils.testRoot()) {
                ee.activeRoot(this);
                // 激活后循环检测，最多等待 1 秒 (5次 * 200ms)
                for (int i = 0; i < 5; i++) {
                    if (ee.getServerStatus()) {
                        serverStatus = true;
                        break;
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        boolean newIsDevice = ee.isDeviceOwnerActive(this);
        boolean newIsRoot = false;
        boolean newIsADB = false;

        // 仅在服务运行时更新状态
        if (serverStatus) {
            newIsRoot = ee.isROOT();
            newIsADB = ee.isADB();
        }

        final boolean finalIsRoot = newIsRoot;
        final boolean finalIsADB = newIsADB;
        final boolean finalIsDevice = newIsDevice;

        if (finalIsRoot != isRoot || finalIsADB != isADB || finalIsDevice != isDevice) {
            isRoot = finalIsRoot;
            isADB = finalIsADB;
            isDevice = finalIsDevice;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateUIStatus();
                }
            });
        }
    }

    private void updateUIStatus() {
        String title = "";
        String name = tu.getLanguageString(this,R.string.app_name);
        if (isRoot) {
            title = name+" [ ROOT ] [ " + uid + " ]";
        } else if (isADB) {
            title = name+" [ ADB ] [ " + uid + " ]";
        } else if (isDevice) {
            title = name+" [ DEVICE ] [ " + uid + " ]";
        } else {
            title = name+" [ General ] [ " + uid + " ]";
        }
        setTitle(title);

        if (homeFragment instanceof HomeFragmentLayout) {
            ((HomeFragmentLayout) homeFragment).updateStatus(isRoot, isADB, isDevice);
        }
        if (helpFragment instanceof HelpFragmentLayout) {
            ((HelpFragmentLayout) helpFragment).updateStatus(isRoot, isADB, isDevice);
        }
        
        if (manageFragment instanceof ManagerGrantUserFragmentLayout) {
            ((ManagerGrantUserFragmentLayout) manageFragment).updateStatus(isRoot, isADB, isDevice);
        }
        // 如果有其他 Fragment 需要更新也可以在这里添加
        invalidateOptionsMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }
    }

    public void initView(){
        setContentView(R.layout.activity_main);
        MyActivityManager.getIns().add(this);
        uid = getIntent().getIntExtra("uid",0);
        amv1 =findViewById(R.id.amv1);
        amv2 =findViewById(R.id.amv2);
        amv3 =findViewById(R.id.amv3);


        findViewById(R.id.tab1_container).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(1);
            }
        });
        findViewById(R.id.tab2_container).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(2);
            }
        });
        findViewById(R.id.tab3_container).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(3);
            }
        });

        try {
            this.getExternalCacheDir().mkdirs();
            this.getCacheDir().mkdirs();
            this.getFilesDir().mkdirs();
        }catch (Exception e){}

        if (homeFragment == null) {
            homeFragment = new HomeFragmentLayout(isRoot,isADB,isDevice,uid);
        }
        if (helpFragment == null) {
            helpFragment =  new HelpFragmentLayout(isRoot,isADB,isDevice,uid);
        }

        if(manageFragment == null){
            manageFragment = new ManagerGrantUserFragmentLayout(isRoot,isADB,isDevice);
        }

        showFragment(helpFragment);
        updateTabUI(3);
        startStatusUpdateTask();
    }

    private void selectTab(int index) {
        Fragment targetFragment = null;
        switch (index) {
            case 1:
                if (manageFragment == null) manageFragment = new ManagerGrantUserFragmentLayout(isRoot, isADB, isDevice);
                targetFragment = manageFragment;
                break;
            case 2:
                if (homeFragment == null) homeFragment = new HomeFragmentLayout(isRoot, isADB, isDevice, uid);
                targetFragment = homeFragment;
                break;
            case 3:
                if (helpFragment == null) helpFragment = new HelpFragmentLayout(isRoot, isADB, isDevice, uid);
                targetFragment = helpFragment;
                break;
        }
        showFragment(targetFragment);
        updateTabUI(index);
    }

    private void showFragment(Fragment fragment) {
        if (currentFragment == fragment) return;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        String tag = "";
        if (fragment == homeFragment) tag = "home";
        else if (fragment == helpFragment) tag = "help";
        else if (fragment == manageFragment) tag = "manager";

        if (!fragment.isAdded()) {
            transaction.add(R.id.amfl1, fragment, tag);
        } else {
            transaction.show(fragment);
        }

        currentFragment = fragment;
        transaction.commitAllowingStateLoss();
    }

    private void updateTabUI(int index) {
        amv1.setVisibility(index == 1 ? View.VISIBLE : View.INVISIBLE);
        amv2.setVisibility(index == 2 ? View.VISIBLE : View.INVISIBLE);
        amv3.setVisibility(index == 3 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,0,0,tu.getLanguageString(this,R.string.options_menu_help_str));
        if(ee.getServerStatus()){
            if(isDevice){
                menu.add(Menu.NONE,3,0,tu.getLanguageString(this,R.string.options_menu_remove_device_str));
            }else{
                menu.add(Menu.NONE,3,0,tu.getLanguageString(this,R.string.options_menu_active_device_str));
            }
        }else{
            menu.add(Menu.NONE,3,0,tu.getLanguageString(this,isDevice ? R.string.options_menu_remove_device_str : R.string.options_menu_active_device_str));
        }

        menu.add(Menu.NONE,1,0,tu.getLanguageString(this,R.string.options_menu_full_exit));
        menu.add(Menu.NONE,2,0,tu.getLanguageString(this,R.string.options_menu_exit));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case 0:
                currentDialog = dialogUtils.showHelp(this,HelpDialogUtils.MAIN_HELP,0);
                break;
            case 1:
                if(ee.isDeviceOwnerActive(this)){
                    ee.forceRemoveDeviceOwner(this);
                }
                if(ee.getServerStatus()){
                    ee.dead();
                }
                MyActivityManager.getIns().killall();
                break;
            case 2:
                MyActivityManager.getIns().killall();
                break;
            case 3:
                if(!isDevice && !ee.getServerStatus()){
                    currentDialog = dialogUtils.showInfoMsg(this,tu.getLanguageString(this,R.string.tips),tu.getLanguageString(this,R.string.menu_active_device_help_str));
                }else{
                    if(ee.getServerStatus()){
                        if(isDevice){
//                            for (String activeAdmin : activeAdmins) {
//                                ee.removeDeviceOwner(this,activeAdmin,ee.getCurrentUserID());
//                            }
                            if(ee.isDeviceOwnerActive(this)){
                                ee.removeDeviceOwner(this);
                                MyActivityManager.getIns().killall();
                            }
                        }else{
                            //暂时存在问题,不能调用
//                            ee.setDeviceOwner(this,ee.getEasyMDPMComName(this).flattenToShortString(),ee.getCurrentUserID());

                            //暂替方案
                            String activeDeviceScriptStr = "dpm set-device-owner " + ee.getEasyMDPMComName(this).flattenToShortString();
                            System.out.println(activeDeviceScriptStr);
                            CMD cmd = ee.runCMD(activeDeviceScriptStr);
                            System.out.println(cmd.toString());
                            if(cmd.getResultCode() == 0){
                                ee.dead();
                                MyActivityManager.getIns().killall();
                            }else{
                                currentDialog = dialogUtils.showInfoMsg(this,tu.getLanguageString(this,R.string.error_tips), activeDeviceScriptStr +"\n\n" +cmd.getResult());
                            }

                        }

                    }else{
                        if(ee.isDeviceOwnerActive(this)){
                            ee.removeDeviceOwner(this);
                            MyActivityManager.getIns().killall();
                        }
                    }


                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

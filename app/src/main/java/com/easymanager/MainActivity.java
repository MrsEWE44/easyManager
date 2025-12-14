package com.easymanager;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.fragment.HelpFragmentLayout;
import com.easymanager.fragment.HomeFragmentLayout;
import com.easymanager.fragment.ManagerGrantUserFragmentLayout;
import com.easymanager.utils.FileTools;
import com.easymanager.utils.dialog.HelpDialogUtils;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.ShellUtils;
import com.easymanager.utils.TextUtils;
import com.easymanager.utils.dialog.NetUtilsDialog;
import com.easymanager.utils.easyManagerUtils;
import com.easymanager.utils.permissionRequest;

public class MainActivity extends Activity {
    private ImageView amiv1,amiv2,amiv3;
    private FragmentManager fragmentManager;
    private Fragment homeFragment , helpFragment , manageFragment , currentFragment;
    private Boolean isRoot,isADB;
    private int stop_time = 5000;
    private int uid;
    private easyManagerUtils ee = new easyManagerUtils();
    private HelpDialogUtils dialogUtils = new HelpDialogUtils();
    private TextUtils tu = dialogUtils.tu;

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
        dialogUtils.showHelp(this,HelpDialogUtils.MAIN_HELP,0);
//        new NetUtilsDialog().checkupdate(this);
    }

    public void initView(){
        setContentView(R.layout.activity_main);
        MyActivityManager.getIns().add(this);
        uid = getIntent().getIntExtra("uid",0);
        amiv1 =findViewById(R.id.amiv1);
        amiv2 =findViewById(R.id.amiv2);
        amiv3 =findViewById(R.id.amiv3);
        imclick(amiv1);
        imclick(amiv2);
        imclick(amiv3);
        try {
            this.getExternalCacheDir().mkdirs();
            this.getCacheDir().mkdirs();
            this.getFilesDir().mkdirs();
        }catch (Exception e){}
        ShellUtils shellUtils = new ShellUtils();
        FileTools fileUtils = new FileTools();
        permissionRequest.requestExternalStoragePermission(this);
        permissionRequest.getExternalStorageManager(this,this);
        isRoot = shellUtils.testRoot();
        isADB = false;
        if(isRoot){
            isRoot = ee.isROOT();
            if(!isRoot){
                ee.activeRoot(this);
                // 开始时间
                long stime = System.currentTimeMillis();
                while(true){
                    if(ee.isROOT()){
                        isRoot = true;
                        dialogUtils.showInfoMsg(this,tu.getLanguageString(this,R.string.tips),tu.getLanguageString(this,R.string.isrootmodestr));
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    // 结束时间
                    long etime = System.currentTimeMillis();
                    if((etime - stime) > stop_time){
                        break;
                    }
                }
            }
        }
        if(!isRoot){
            if(ee.getServerStatus()){
                isRoot = ee.isROOT();
                isADB = ee.isADB();
            }else {
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
                dialogUtils.showInfoMsg(this,tu.getLanguageString(this,R.string.general_tips),tu.getLanguageString(this,R.string.general_tips_str_head)+path+"/"+name+tu.getLanguageString(this,R.string.general_tips_str_end));
            }
        }

        if(isRoot!=null && isRoot){
            setTitle("easyManager [ ROOT ] [ "+uid+" ]");
        }else if(isADB!=null && isADB){
            setTitle("easyManager [ ADB ] [ "+uid+" ]");
        }else{
            setTitle("easyManager [ General ] [ "+uid+" ]");
        }

        if(isRoot || isADB){
            ee.requestGrantUser(this);
            ee.startStopRunningAPP(new TransmissionEntity(null,null,this.getPackageName(),-1,uid));
        }
        if (homeFragment == null) {
            homeFragment = new HomeFragmentLayout(isRoot,isADB,uid);
        }
        if (helpFragment == null) {
            helpFragment =  new HelpFragmentLayout(isRoot,isADB,uid);
        }

        if(manageFragment == null){
            manageFragment = new ManagerGrantUserFragmentLayout(isRoot,isADB);
        }

        showFragment(helpFragment);

    }

    public void showFragment(Fragment fragment) {
        String tag = null;
        if (fragment instanceof HelpFragmentLayout) {
            tag = "help";
        }

        if(fragment instanceof  HomeFragmentLayout){
            tag = "home";
        }

        if(fragment instanceof  ManagerGrantUserFragmentLayout){
            tag = "manager";
        }

        //开启事务 创建事务对象
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //如果之前没有添加过
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.amfl1,fragment,tag);
            if (currentFragment != null) {
                //隐藏fragment
                fragmentTransaction.hide(currentFragment);
            }
        } else {
            if (currentFragment != null) {
                fragmentTransaction.hide(currentFragment);
            }
            fragmentTransaction.show(fragment);
        }
        //全局变量，记录当前显示的fragment
        currentFragment = fragment;
        fragmentTransaction.commit();
    }

    private void imclick(ImageView im){
        im.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                im.setSelected(true);
                int id = view.getId();
                if(id == R.id.amiv1){
                    currentFragment = new ManagerGrantUserFragmentLayout(isRoot,isADB);
                    amiv2.setSelected(false);
                    amiv3.setSelected(false);
                }
                if(id == R.id.amiv2){
                    currentFragment = new HomeFragmentLayout(isRoot,isADB,uid);
                    amiv1.setSelected(false);
                    amiv3.setSelected(false);
                }
                if(id == R.id.amiv3){
                    currentFragment = new HelpFragmentLayout(isRoot,isADB,uid);
                    amiv1.setSelected(false);
                    amiv2.setSelected(false);
                }
                fragmentTransaction.replace(R.id.amfl1, currentFragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        MyActivityManager.getIns().killall();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,0,0,tu.getLanguageString(this,R.string.options_menu_help_str));
        menu.add(Menu.NONE,1,0,tu.getLanguageString(this,R.string.options_menu_full_exit));
        menu.add(Menu.NONE,2,0,tu.getLanguageString(this,R.string.options_menu_exit));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case 0:
                dialogUtils.showHelp(this,HelpDialogUtils.MAIN_HELP,0);
                break;
            case 1:
                ee.dead();
                MyActivityManager.getIns().killall();
                break;
            case 2:
                MyActivityManager.getIns().killall();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
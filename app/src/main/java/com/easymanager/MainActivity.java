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

import com.easymanager.fragment.HelpFragmentLayout;
import com.easymanager.fragment.HomeFragmentLayout;
import com.easymanager.fragment.ManagerGrantUserFragmentLayout;
import com.easymanager.utils.FileTools;
import com.easymanager.utils.HelpDialogUtils;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.ShellUtils;
import com.easymanager.utils.TextUtils;
import com.easymanager.utils.easyManagerUtils;
import com.easymanager.utils.permissionRequest;

public class MainActivity extends Activity {
    private ImageView amiv1,amiv2,amiv3;

    private Boolean isRoot,isADB;

    private easyManagerUtils ee = new easyManagerUtils();
    private HelpDialogUtils dialogUtils = new HelpDialogUtils();

    private TextUtils tu = new TextUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyActivityManager.getIns().add(this);
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
        }catch (Exception e){

        }
        ShellUtils shellUtils = new ShellUtils();
        FileTools fileUtils = new FileTools();
        permissionRequest.requestExternalStoragePermission(this);
        permissionRequest.getExternalStorageManager(this);
        isRoot = shellUtils.testRoot();
        isADB = false;
        if(isRoot){
            isRoot = ee.isROOT();
            if(!isRoot){
                ee.activeRoot(this);
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
            setTitle("easyManager [ ROOT ] ");
        }else if(isADB!=null && isADB){
            setTitle("easyManager [ ADB ] ");
        }else{
            setTitle("easyManager [ General ] ");
        }

        if(isRoot || isADB){
            ee.requestGrantUser(this);
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.amfl1,new HelpFragmentLayout(isRoot,isADB)).commit();

        dialogUtils.showHelp(this,HelpDialogUtils.MAIN_HELP,0);

    }

    private void imclick(ImageView im){
        im.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                Fragment fragment = null;
                im.setSelected(true);
                int id = view.getId();
                if(id == R.id.amiv1){
                    fragment = new ManagerGrantUserFragmentLayout(isRoot,isADB);
                    amiv2.setSelected(false);
                    amiv3.setSelected(false);
                }
                if(id == R.id.amiv2){
                    fragment = new HomeFragmentLayout(isRoot,isADB);
                    amiv1.setSelected(false);
                    amiv3.setSelected(false);
                }
                if(id == R.id.amiv3){
                    fragment = new HelpFragmentLayout(isRoot,isADB);
                    amiv1.setSelected(false);
                    amiv2.setSelected(false);
                }
                fragmentTransaction.replace(R.id.amfl1, fragment);
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
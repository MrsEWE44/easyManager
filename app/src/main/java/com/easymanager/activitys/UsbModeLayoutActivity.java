package com.easymanager.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.easymanager.R;
import com.easymanager.core.utils.CMD;
import com.easymanager.enums.AppManagerEnum;
import com.easymanager.utils.DialogUtils;
import com.easymanager.utils.HelpDialogUtils;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.OtherTools;
import com.easymanager.utils.easyManagerUtils;

import java.util.ArrayList;

public class UsbModeLayoutActivity extends Activity {

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Boolean> checkboxs = new ArrayList<>();
    private Button umlscanbt,umlmountbt;
    private Spinner umlsp;
    private ListView umllv;

    private Context context;

    private Activity activity;

    private boolean isRoot,isADB;

    private int mode;

    private String MOUNT_MODE[] = {"自动挂载模式","模式1","模式2","模式3"};

    private int MOUNT_MODE_INDEX=0;

    private DialogUtils du = new DialogUtils();

    private final static String config_path1 = "/config/usb_gadget/g1";
    private final static String config_path2="/sys/class/android_usb/android0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usb_mode_layout);
        MyActivityManager.getIns().add(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initBt();
    }

    private void initBt() {
        context=this;
        activity=this;
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode",-1);
        isRoot = intent.getBooleanExtra("isRoot",false);
        isADB = intent.getBooleanExtra("isADB",false);
        umlscanbt = findViewById(R.id.umlscanbt);
        umlmountbt = findViewById(R.id.umlmountbt);
        umlsp = findViewById(R.id.umlsp);
        umllv = findViewById(R.id.umllv);
        umlsp.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, MOUNT_MODE));
        btClicked();

    }

    private void btClicked() {
        easyManagerUtils eu = new easyManagerUtils();
        umlmountbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < checkboxs.size(); i++) {
                    if(checkboxs.get(i)){
                        String s = list.get(i);
                        switch (MOUNT_MODE_INDEX){
                            case 0 :
                                eu.runCMD(autoMode(s));
                                break;
                            case 1:
                                eu.runCMD(mode1(s));
                                break;
                            case 2:
                                eu.runCMD(mode2(s));
                                break;
                            case 3:
                                eu.runCMD(mode3(s));
                                break;
                        }
                        break;
                    }
                }


            }
        });

        umlscanbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                du.findLocalImgDialog(context,activity,umllv,list,checkboxs);
            }
        });

        umlsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MOUNT_MODE_INDEX=i;
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
            du.showSearchViewDialog(context,activity,umllv,null,list,checkboxs);
        }
        if(itemId == android.R.id.home){
            activity.onBackPressed();
        }

        if(itemId == 5){
            new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_MANAGE_HELP,mode);
        }

        if(itemId == 6){
            MyActivityManager.getIns().killall();
        }

        return super.onOptionsItemSelected(item);
    }

    //Android9以上同时开启mtp实现
    private String mode1(String filePath){
        return  "cd "+config_path1+" && echo -n 'msc' >configs/b.1/strings/0x409/configuration &&  rm -rf configs/b.1/f* && echo  0x05C6 > idVendor && echo 0x9015 > idProduct && echo -n '"+filePath+"' > functions/mass_storage.0/lun.0/file && ln -s functions/mtp.gs0 configs/b.1/f1 && ln -s functions/mass_storage.0 configs/b.1/f2 && ln -s functions/ffs.adb configs/b.1/f3 ";
    }

    //Android9-10实现
    private String mode2(String filePath){
        return "cd "+config_path1+" && echo -n 'msc' >configs/b.1/strings/0x409/configuration &&  rm -rf configs/b.1/f* && ln -s functions/mass_storage.0 configs/b.1/f1 && echo -n '"+filePath+"' >configs/b.1/f1/lun.0/file && setprop sys.usb.config mass_storage ";
    }

    //Android8以前实现，未测试
    private String mode3(String filePath){
        return "cd "+config_path2 +" && echo -n 0 > enable && echo -n '"+filePath+"' > f_mass_storage/lun/file && echo -n 'mass_storage' >functions && echo -n 1 >enable";
    }


    private String autoMode(String filePath){
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.P) {
            return "cd "+config_path1+" && echo -n 'msc' >configs/b.1/strings/0x409/configuration &&  rm -rf configs/b.1/f* && ln -s functions/mass_storage.0 configs/b.1/f1 && echo -n '"+filePath+"' >configs/b.1/f1/lun.0/file && setprop sys.usb.config mass_storage ";
        }else {
            return "cd "+config_path2 +" && echo -n 0 > enable && echo -n '"+filePath+"' > f_mass_storage/lun/file && echo -n 'mass_storage' >functions && echo -n 1 >enable";

        }
    }


}

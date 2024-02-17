package com.easymanager.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.easymanager.utils.PackageUtils;
import com.easymanager.utils.base.AppCloneUtils;
import com.easymanager.utils.dialog.HelpDialogUtils;

import java.util.ArrayList;

public class AppInfoLayoutActivity extends Activity {

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Boolean> checkboxs = new ArrayList<>();
    private ArrayList<Boolean> switbs = new ArrayList<>();

    private ImageView ailappicon;

    private TextView ailappname , ailapppkgname , ailappversion , ailappsize;

    private Spinner ailsp1;

    private Button ailbt1;

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
        uid = intent.getIntExtra("uid",0);
        mode = intent.getIntExtra("mode",-1);
        ailappicon = findViewById(R.id.ailappicon);
        ailappname = findViewById(R.id.ailappname);
        ailapppkgname = findViewById(R.id.ailapppkgname);
        ailappversion = findViewById(R.id.ailappversion);
        ailappsize = findViewById(R.id.ailappsize);
        ailsp1 = findViewById(R.id.ailsp1);
        ailbt1 = findViewById(R.id.ailbt1);
        aillv1 = findViewById(R.id.aillv1);
        ailsp1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getAppChoicesOPT()));
        PKGINFO pkginfo = new PackageUtils().getPKGINFO(context, pkgname);
        ailappicon.setImageDrawable(pkginfo.getAppicon());
        ailappname.setText(pkginfo.getAppname());
        ailapppkgname.setText(pkginfo.getPkgname());
        ailappversion.setText(pkginfo.getAppversionname());
        ailappsize.setText(getSize(pkginfo.getFilesize(),0));
        if(!isRoot){
            acu.getUd().showInfoMsg(context,getLanStr(R.string.warning_tips),getLanStr(R.string.need_root_msg));
        }else{
            btClicked();
            new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_INFO_HELP,mode);
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
                    PKGINFO pkginfo = new PKGINFO(pkgname,s,  (switbs.get(i) ? "true" : "false"), null, null, null, null);
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



    private String getSize(double size,int count){
        String size_type[] = {"b","KB","MB","GB","TB","PB"};
        if(size > 1024){
            double d_size= size/1024;
            count = count + 1;
            return getSize(d_size,count);
        }
        String sizestr=String.format("%.2f",size)+size_type[count];
        return sizestr;
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

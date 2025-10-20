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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.easymanager.MainActivity;
import com.easymanager.R;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.enums.AppManagerEnum;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.OtherTools;
import com.easymanager.utils.base.AppCloneUtils;
import com.easymanager.utils.base.DialogBaseUtils;
import com.easymanager.utils.dialog.HelpDialogUtils;

import java.util.ArrayList;

public class AppCloneLayoutActivity extends Activity {

    private ArrayList<String> strlist = new ArrayList<>();
    private ArrayList<Boolean> strcheckboxs = new ArrayList<>();
    private ArrayList<PKGINFO> pkginfos = new ArrayList<>();
    private ArrayList<Boolean> pkgcheckboxs = new ArrayList<>();
    private EditText acmet1;
    private Spinner acmsp1 , acmsp2;
    private Button clmbt1;
    private ListView acmlv1 , acmlv2;
    private Context context;
    private Activity activity;
    private Boolean isRoot , isADB;
    private int mode,APP_CHOICES_INDEX,MANAGER_INDEX;

    private AppCloneUtils acu = new AppCloneUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_clone_manager);
        MyActivityManager.getIns().add(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        activity = this;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            new DialogBaseUtils().showInfoMsg(context,getLanStr(R.string.tips),getLanStr(R.string.show_app_clone_low_version_msg));
        }else {
            initBt();
        }
    }

    private void initBt() {
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode",-1);
        isRoot = intent.getBooleanExtra("isRoot",false);
        isADB = intent.getBooleanExtra("isADB",false);
        acmet1 = findViewById(R.id.acmet1);
        acmsp1 = findViewById(R.id.acmsp1);
        acmsp2 = findViewById(R.id.acmsp2);
        clmbt1 = findViewById(R.id.clmbt1);
        acmlv1 = findViewById(R.id.acmlv1);
        acmlv2 = findViewById(R.id.acmlv2);
        acmsp1.setAdapter(getSpinnerAdapter(getManagerMode()));
        acmsp2.setAdapter(getSpinnerAdapter(getAppChoicesOPT()));
        if(mode == AppManagerEnum.APP_CLONE){
            acmsp1.setEnabled(false);
            clmbt1.setText(getLanStr(R.string.app_clone_button_start_clone_str));
        }

        if(mode == AppManagerEnum.APP_CLONE_MANAGE){
            acmet1.setEnabled(false);
            acmet1.setHint(getLanStr(R.string.stop_edit));
            clmbt1.setText(getLanStr(R.string.app_clone_button_manager_clone_change_str));
        }

        if(mode == AppManagerEnum.APP_CLONE_REMOVE){
            acmsp1.setEnabled(false);
            acmet1.setEnabled(false);
            acmet1.setHint(getLanStr(R.string.stop_edit));
            clmbt1.setText(getLanStr(R.string.app_clone_button_delete_clone_str));
        }
        btClicked();
        new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_MANAGE_HELP,mode);
    }

    private void btClicked() {
        int currentUserID = acu.getCurrentUserID();
        spinnerChange(acmsp1,0);
        spinnerChange(acmsp2,1);
        clmbt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode == AppManagerEnum.APP_CLONE_REMOVE){
                    ArrayList<PKGINFO> list = new ArrayList<>();
                    for (int i = 0; i < strlist.size(); i++) {
                        PKGINFO pkginfo = new PKGINFO(strlist.get(i),null,null,null,null,null);
                        if(APP_CHOICES_INDEX == 0){
                            if(strcheckboxs.get(i)){
                                list.add(pkginfo);
                            }
                        }

                        if(APP_CHOICES_INDEX == 1){
                            if(!strcheckboxs.get(i)){
                                list.add(pkginfo);
                            }
                        }

                        if(APP_CHOICES_INDEX == 2){
                            list.add(pkginfo);
                        }
                    }
                    acu.getPd().showProcessBarDialogByCMD(context,list,AppManagerEnum.APP_CLONE_REMOVE,-1,null,currentUserID);
                }else{
                    ArrayList<PKGINFO> list = new ArrayList<>();
                    for (int i = 0; i < pkgcheckboxs.size(); i++) {
                        PKGINFO pkginfo = pkginfos.get(i);
                        if(APP_CHOICES_INDEX == 0){
                            if(pkgcheckboxs.get(i)){
                                list.add(pkginfo);
                            }
                        }

                        if(APP_CHOICES_INDEX == 1){
                            if(!pkgcheckboxs.get(i)){
                                list.add(pkginfo);
                            }
                        }

                        if(APP_CHOICES_INDEX == 2){
                            list.add(pkginfo);
                        }

                    }
                    if(mode == AppManagerEnum.APP_CLONE){
                        String s = acmet1.getText().toString();
                        int count =(s==null || s.isEmpty())?1:Integer.valueOf(s);
                        acu.getUd().showAppClone(context,list,count,getLanStr(R.string.app_clone_create_clone_title),getLanStr(R.string.app_clone_create_clone_msg));
                    }

                    if(mode == AppManagerEnum.APP_CLONE_MANAGE){
                        ArrayList<String> strings = new ArrayList<>();
                        for (int i = 0; i < strcheckboxs.size(); i++) {
                            if(strcheckboxs.get(i)){
                               strings.add(strlist.get(i));
                            }
                        }
                        acu.getUd().showAppCloneManagerProcessBarDialog(context,list,strings,MANAGER_INDEX);
                    }
                }

            }
        });

        acmlv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("mode",mode);
                intent.putExtra("isRoot",isRoot);
                intent.putExtra("isADB",isADB);
                intent.putExtra("uid",Integer.valueOf(strlist.get(i)));
                startActivity(intent);
            }
        });

    }


    private String[] getManagerMode() {
        return new String[]{getLanStr(R.string.app_clone_manager_clone_mode_str1),getLanStr(R.string.app_clone_manager_clone_mode_str2)};
    }

    private String getLanStr(int id){
        return acu.getTU().getLanguageString(context,id);
    }

    public ArrayAdapter getSpinnerAdapter(String s[]){
        return new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, s);
    }

    private String[] getAppChoicesOPT(){
        return new String[]{getLanStr(R.string.spin_item_selected),getLanStr(R.string.spin_item_no_selected),getLanStr(R.string.spin_item_all_selected)};
    }


    private void spinnerChange(Spinner s,int app_opt_mode){
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (app_opt_mode){
                    case 0:
                        MANAGER_INDEX = i;
                        break;
                    case 1:
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
        int defaultuid=acu.getCurrentUserID();
        if(itemId == R.id.actionbarsearch){
            acu.getSd().showSearchViewDialog(context,activity,acmlv1,pkginfos,null,pkgcheckboxs,defaultuid);
        }
        if(itemId == android.R.id.home){
            activity.onBackPressed();
        }
        if(itemId == 0){
            acu.getSd().queryAllPKGSProcessDialog(context,activity,acmlv1,pkginfos,pkgcheckboxs,defaultuid);
        }

        if(itemId == 1){
            acu.getSd().queryAllEnablePKGSProcessDialog(context,activity,acmlv1,pkginfos,pkgcheckboxs,defaultuid);
        }

        if(itemId == 2){
            acu.getSd().queryUserAllPKGSProcessDialog(context,activity,acmlv1,pkginfos,pkgcheckboxs,defaultuid);
        }

        if(itemId == 3){
            acu.getSd().queryUserEnablePKGSProcessDialog(context,activity,acmlv1,pkginfos,pkgcheckboxs,defaultuid);
        }

        if(itemId == 4){
            acu.getSd().queryAllDisablePKGSProcessDialog(context,activity,acmlv1,pkginfos,pkgcheckboxs,defaultuid);
        }

        if(itemId == 5){
            new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_MANAGE_HELP,mode);
        }

        if(itemId == 10){
            acu.getSd().queryUserAllPKGSByAppCloneProcessDialog(context,activity,acmlv1,pkginfos,pkgcheckboxs);
        }

        if(itemId == 11){
            acu.getSd().queryAllPKGSByAppCloneProcessDialog(context,activity,acmlv1,pkginfos,pkgcheckboxs);
        }

        if(itemId == 12){
            acu.getUd().queryLocalAppCloneUserProcessDialog(context,activity,acmlv2,strlist,strcheckboxs);
        }

        if(itemId == 13){
            acu.getUd().startAppCloneUsers(context,activity);
        }

        if(itemId == 6){
            MyActivityManager.getIns().killall();
        }
        return super.onOptionsItemSelected(item);
    }

}

package com.easymanager.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.adapters.RunCMDFilterAdapter;
import com.easymanager.core.utils.CMD;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.OtherTools;
import com.easymanager.utils.base.DialogUtils;
import com.easymanager.utils.dialog.HelpDialogUtils;
import com.easymanager.utils.easyManagerUtils;

import java.util.ArrayList;

public class RunCommandLayoutActivity extends Activity {

    private Context context;
    private Activity activity;

    private ArrayList<String> mycmd = new ArrayList<>();

    private String cmdresult;
    private boolean isRoot,isADB;
    private int mode,uid;
    private AutoCompleteTextView rclactv1;
    private Button rclruncmdbt;
    private EditText rclet1;
    private TextView rcltv1;
    private DialogUtils du = new DialogUtils();
    private easyManagerUtils eu = du.easyMUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_command_layout);
        MyActivityManager.getIns().add(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initBt();
    }

    private void initBt() {
        Intent intent = getIntent();
        context=this;
        activity=this;
        mode = intent.getIntExtra("mode",-1);
        isRoot = intent.getBooleanExtra("isRoot",false);
        isADB = intent.getBooleanExtra("isADB",false);
        uid = intent.getIntExtra("uid",0);
        rclactv1 = findViewById(R.id.rclactv1);
        rclruncmdbt = findViewById(R.id.rclruncmdbt);
        rclet1 = findViewById(R.id.rclet1);
        rcltv1 = findViewById(R.id.rcltv1);
        rcltv1.setText(eu.isROOT()?"# ":"$ ");
        rclet1.setKeyListener(null);
        btClicked();
        scanLocalCMDFile();
        new HelpDialogUtils().showHelp(context,HelpDialogUtils.RUN_COMMAND_HELP,mode);
    }

    private void btClicked() {
        rclruncmdbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmdstr =  rclactv1.getText().toString();
                if(cmdstr != null && !cmdstr.isEmpty()){
                    ProgressDialog show = du.showMyDialog(context,du.tu.getLanguageString(context,R.string.execute_cmd));
                    Handler handler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            if(msg.what==0){
                                show.dismiss();
                                rclet1.append(cmdresult+"\n");
                            }

                        }
                    };
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(cmdstr.equals("clear") || cmdstr.equals("cls")){
                                cmdresult = "";
                            }else{
                                CMD cmd = eu.getServerStatus() ? eu.runCMD(cmdstr) : new CMD(cmdstr,false);
                                cmdresult = cmd.getResult();
                                du.sendHandlerMSG(handler,0);
                            }
                        }
                    }).start();
                }
            }
        });

    }

    private void scanLocalCMDFile(){
        try {
            //需要实现自定义规则匹配,匹配自动列出来的内容
            ProgressDialog myDialog = du.showMyDialog(context, getString(R.string.general_loading));
            Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what==0){
                        RunCMDFilterAdapter adapter = new RunCMDFilterAdapter(mycmd,context);
                        rclactv1.setAdapter(adapter);
                        myDialog.dismiss();
                    }
                }
            };
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    String getenv = System.getenv("PATH");
                    if(getenv != null){
                        String[] split = getenv.split(":");
                        if(split != null && split.length > 0){
                            for (String s : split) {
                                mycmd.addAll(eu.getPathALLFiles(context, s, uid));
                            }
                        }
                    }
                    du.sendHandlerMSG(handler,0);
                }
            });
            t.start();
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        OtherTools otherTools = new OtherTools();
        otherTools.addMenuBase(this,menu,mode);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if(itemId == android.R.id.home){
            activity.onBackPressed();
        }

        if(itemId == 5){
            new HelpDialogUtils().showHelp(context,HelpDialogUtils.RUN_COMMAND_HELP,mode);
        }

        if(itemId == 6){
            MyActivityManager.getIns().killall();
        }

        return super.onOptionsItemSelected(item);
    }

}

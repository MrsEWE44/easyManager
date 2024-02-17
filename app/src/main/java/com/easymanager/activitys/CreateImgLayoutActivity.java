package com.easymanager.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.easymanager.R;
import com.easymanager.enums.AppManagerEnum;
import com.easymanager.utils.base.DialogUtils;
import com.easymanager.utils.dialog.HelpDialogUtils;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.OtherTools;

public class CreateImgLayoutActivity extends Activity {

    private EditText cilfilename , cilsize;
    private Spinner cilnametype , cilsizetype;
    private Button cilcreateimg;

    private Context context;

    private Activity activity;

    private boolean isRoot,isADB;

    private int mode;


    private String FILE_NAME_TYPE[] = {"img","iso"};
    private String FILE_SIZE_TYPE[] = {"byte", "KB", "MB", "GB"};
    private String FILE_SIZE_TYPE_VALUES[] = {"", "K", "M", "G"};

    private String ntp_services[] = {"dns1.synet.edu.cn","news.neu.edu.cn","dns.sjtu.edu.cn","dns2.synet.edu.cn","ntp.glnet.edu.cn","ntp-sz.chl.la","ntp.gwadar.cn","cn.pool.ntp.org","自定义"};
    private String refresh_rates[] = {"48","60","90","120","144","165","自定义"};



    private int FILE_NAME_TYPE_INDEX=0,FILE_SIZE_TYPE_INDEX=0;


    private DialogUtils du = new DialogUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_img_layout);
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
        cilfilename = findViewById(R.id.cilfilename);
        cilsize = findViewById(R.id.cilsize);
        cilnametype = findViewById(R.id.cilnametype);
        cilsizetype = findViewById(R.id.cilsizetype);
        cilcreateimg = findViewById(R.id.cilcreateimg);
        String disable_msg = getLanStr(R.string.stop_edit);
        if(mode == AppManagerEnum.CREATE_IMG){
            cilnametype.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, FILE_NAME_TYPE));
            cilsizetype.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, FILE_SIZE_TYPE));
        }

        if(mode == AppManagerEnum.SET_NTP){
            cilsizetype.setEnabled(false);
            cilsize.setEnabled(false);
            cilsize.setText(disable_msg);
            cilfilename.setHint(getLanStr(R.string.input_custom_ntp_service));
            cilcreateimg.setText(getLanStr(R.string.apply_ntp_service));
            cilnametype.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, ntp_services));
        }

        if(mode == AppManagerEnum.SET_RATE){
            cilnametype.setEnabled(false);
            cilfilename.setEnabled(false);
            cilfilename.setText(disable_msg);
            cilsize.setHint(getLanStr(R.string.input_custom_rate));
            cilcreateimg.setText(getLanStr(R.string.apply_rate));
            cilsizetype.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, refresh_rates));
        }

        if(mode == AppManagerEnum.DEL_X){
            cilsizetype.setEnabled(false);
            cilsize.setEnabled(false);
            cilsize.setText(disable_msg);
            cilfilename.setText(disable_msg);
            cilnametype.setEnabled(false);
            cilfilename.setEnabled(false);
            cilcreateimg.setText(getLanStr(R.string.apply_del_x));
        }
        btClicked();
        new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_MANAGE_HELP,mode);
    }

    private void btClicked() {

        cilcreateimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode == AppManagerEnum.CREATE_IMG){
                    String filename = cilfilename.getText().toString();
                    String filesize = cilsize.getText().toString();
                    if(filename.isEmpty() || filesize.isEmpty()){
                        du.showInfoMsg(context,getLanStr(R.string.warning_tips),getLanStr(R.string.fix_vacant));
                    }else{
                        String outpath = Environment.getExternalStorageDirectory().toString()+"/"+filename+"."+FILE_NAME_TYPE[FILE_NAME_TYPE_INDEX];
                        String cmdstr = "dd if=/dev/zero of="+  outpath+" bs=1"+FILE_SIZE_TYPE_VALUES[FILE_SIZE_TYPE_INDEX]+" count="+filesize;
                        du.runCMDDialog(context,cmdstr);
                    }
                }

                if(mode == AppManagerEnum.SET_NTP){
                    String cmdstr = null;
                    String s = cilfilename.getText().toString().trim();
                    if(s==null || s.isEmpty()){
                        String ntp_service_name=ntp_services[FILE_NAME_TYPE_INDEX];
                        cmdstr = "setprop persist.sys.timezone Asia/Shanghai && settings put global ntp_server "+ntp_service_name;
                    }else{
                        cmdstr = "setprop persist.sys.timezone Asia/Shanghai && settings put global ntp_server "+s;
                    }
                    du.runCMDDialog(context,cmdstr);
                }

                if(mode == AppManagerEnum.SET_RATE){
                    String cmdstr = null;
                    String s = cilsize.getText().toString().trim();
                    if(s==null || s.isEmpty()){
                        String rate_peek =refresh_rates[FILE_NAME_TYPE_INDEX];
                        cmdstr = "settings put system peak_refresh_rate "+Float.valueOf(rate_peek)+" && settings put system min_refresh_rate "+Float.valueOf(rate_peek);
                    }else{
                        cmdstr = "settings put system peak_refresh_rate "+Float.valueOf(s)+" && settings put system min_refresh_rate "+Float.valueOf(s);
                    }
                    du.runCMDDialog(context,cmdstr);
                }

                if(mode == AppManagerEnum.DEL_X){
                    String cmdstr = null;
                    int sdkInt = Build.VERSION.SDK_INT;
                    if(sdkInt <= Build.VERSION_CODES.N){
                        cmdstr = "settings delete global captive_portal_server && settings put global captive_portal_detection_enabled 0";
                    }
                    if(sdkInt >= Build.VERSION_CODES.N_MR1 && sdkInt <= Build.VERSION_CODES.Q){
                        cmdstr = "settings put global captive_portal_https_url https://www.google.cn/generate_204";
                    }

                    if(sdkInt >=Build.VERSION_CODES.R){
                        cmdstr = "settings put global captive_portal_mode 0 && settings put global captive_portal_https_url https://www.google.cn/generate_204";
                    }
                    du.runCMDDialog(context,cmdstr);
                }



            }

        });


        cilsizetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                FILE_SIZE_TYPE_INDEX = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cilnametype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                FILE_NAME_TYPE_INDEX = i;
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

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

    private String getLanStr(int id){
        return du.tu.getLanguageString(context,id);
    }

}

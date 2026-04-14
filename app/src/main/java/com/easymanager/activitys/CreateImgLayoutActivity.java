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
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.easymanager.R;
import com.easymanager.enums.AppManagerEnum;
import com.easymanager.utils.base.DialogUtils;
import com.easymanager.utils.dialog.HelpDialogUtils;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.OtherTools;

public class CreateImgLayoutActivity extends AppCompatActivity {

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Intent intent = getIntent();
            boolean isShizuku = intent.getBooleanExtra("isShizuku", false);
            boolean isDhizuku = intent.getBooleanExtra("isDhizuku", false);
            String modeName = "[ General ]";
            if (isShizuku) modeName = "[ SHIZUKU ]";
            else if (isDhizuku) modeName = "[ DHIZUKU ]";

            getSupportActionBar().setTitle(getTitle() + " " + modeName);
        }

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

        View nameCard = findViewById(R.id.name_card);
        View sizeCard = findViewById(R.id.size_card);

        String title = "";

        if(mode == AppManagerEnum.SET_NTP){
            title = getLanStr(R.string.button_home_set_ntp);
            sizeCard.setVisibility(View.GONE);
            cilfilename.setHint(getLanStr(R.string.input_custom_ntp_service));
            cilcreateimg.setText(getLanStr(R.string.apply_ntp_service));
            cilnametype.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, ntp_services));
        }

        else if(mode == AppManagerEnum.SET_RATE){
            title = getLanStr(R.string.button_home_set_rate);
            nameCard.setVisibility(View.GONE);
            cilsize.setHint(getLanStr(R.string.input_custom_rate));
            cilcreateimg.setText(getLanStr(R.string.apply_rate));
            cilsizetype.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, refresh_rates));
        }

        else if(mode == AppManagerEnum.DEL_X){
            title = getLanStr(R.string.button_home_del_x);
            nameCard.setVisibility(View.GONE);
            sizeCard.setVisibility(View.GONE);
            cilcreateimg.setText(getLanStr(R.string.apply_del_x));
        }

        if (getSupportActionBar() != null && !title.isEmpty()) {
            boolean isShizuku = intent.getBooleanExtra("isShizuku", false);
            boolean isDhizuku = intent.getBooleanExtra("isDhizuku", false);
            String modeName = "[ General ]";
            if (isShizuku) modeName = "[ SHIZUKU ]";
            else if (isDhizuku) modeName = "[ DHIZUKU ]";
            getSupportActionBar().setTitle(title + " " + modeName);
        }

        btClicked();
        new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_MANAGE_HELP,mode);
    }

    private void btClicked() {

        cilcreateimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                        String rate_peek =refresh_rates[FILE_SIZE_TYPE_INDEX];
                        cmdstr = "settings put system peak_refresh_rate "+Float.valueOf(rate_peek)+" && settings put system min_refresh_rate "+Float.valueOf(rate_peek);
                    }else{
                        try {
                            float rateValue = Float.parseFloat(s);
                            cmdstr = "settings put system peak_refresh_rate " + rateValue + " && settings put system min_refresh_rate " + rateValue;
                        } catch (NumberFormatException e) {
                            du.showInfoMsg(context, getLanStr(R.string.error_tips), getLanStr(R.string.clean_input_hint_tips));
                            return;
                        }
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
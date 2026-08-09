package com.easymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private Activity activity;

    //你的闲鱼主页地址
    private static final String IDLE_FISH_URL =
            "【闲鱼】https://m.tb.cn/h.8g4tznm?tk=zhoogAYa6GR CZ356 「这是我的闲鱼号，快来看看吧～」\n" +
                    "点击链接直接打开";


    //备用网页
    private static final String WEB_URL = "https://m.tb.cn/h.8g4tznm?tk=zhoogAYa6GR";

    private static final String pkgname = "com.taobao.idlefish";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        activity = this;

        Button btn = findViewById(R.id.btn_open);


        btn.setOnClickListener(v -> {

            if (isAppInstalled()) {
                copyText(context, IDLE_FISH_URL);
                openIdleFish();

            } else {
                copyText(context, IDLE_FISH_URL);
                openWeb();

            }

        });

    }

    public boolean isAppInstalled(){
        PackageManager packageManager = activity.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0 | PackageManager.MATCH_DISABLED_COMPONENTS | PackageManager.MATCH_UNINSTALLED_PACKAGES);
        if(installedPackages.size() == 1){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
            for (ResolveInfo info : list) {
                String packageName = info.activityInfo.packageName;
                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
                    ApplicationInfo appInfo = packageInfo.applicationInfo;
                    boolean isUserApp = (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 || (appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
                    if(packageName.equals(pkgname)){
                        return true;
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else{
            for (PackageInfo packageInfo : installedPackages) {
                if(packageInfo.packageName.equals(pkgname)){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 打开闲鱼App
     */
    private void openIdleFish(){


        try {


            Intent intent = getPackageManager()
                    .getLaunchIntentForPackage(
                            "com.taobao.idlefish"
                    );


            if(intent != null){

                intent.setData(
                        Uri.parse(IDLE_FISH_URL)
                );


                startActivity(intent);


            }else{

                openWeb();

            }



        }catch(Exception e){

            openWeb();

        }


    }





    /**
     * 打开网页
     */
    private void openWeb(){


        Intent intent =
                new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(WEB_URL)
                );


        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_exit) {
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    public void copyText(Context context, String str){
        android.content.ClipboardManager cpm = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("label", str);
        cpm.setPrimaryClip(clip);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {
            Toast.makeText(context,"已复制闲鱼主页链接可以去闲鱼APP打开", Toast.LENGTH_LONG).show();
        }
    }
}
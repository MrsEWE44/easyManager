package com.easymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.easymanager.core.api.DhizukuSystemServerApi;
import com.easymanager.core.api.ShizukuSystemServerApi;
import com.easymanager.core.utils.FileUtils;
import com.easymanager.fragment.HelpFragmentLayout;
import com.easymanager.fragment.HomeFragmentLayout;
import com.easymanager.fragment.ManagerGrantUserFragmentLayout;
import com.easymanager.utils.FileTools;
import com.easymanager.utils.base.DialogBaseUtils;
import com.easymanager.utils.dialog.HelpDialogUtils;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.TextUtils;
import com.easymanager.utils.easyManagerUtils;
import com.easymanager.utils.permissionRequest;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Fragment homeFragment , helpFragment , manageFragment , currentFragment;
    private volatile boolean isShizuku = false;
    private volatile boolean isDhizuku = false;
    private Thread statusThread;
    private int stop_time = 5000;
    private int uid;
    private easyManagerUtils ee = new easyManagerUtils();
    private HelpDialogUtils dialogUtils = new HelpDialogUtils();
    private TextUtils tu = dialogUtils.tu;
    private Handler statusHandler = new Handler(Looper.getMainLooper());
    private Runnable statusRunnable;
    private boolean lastShizuku = false;
    private boolean lastDhizuku = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 恢复持久化的运行时模式
        ShizukuSystemServerApi.initMode(this);

        //修复软件崩溃后，出现页面重叠的问题
        fragmentManager = getSupportFragmentManager();
        if(savedInstanceState != null){
            helpFragment = fragmentManager.findFragmentByTag("help");
            manageFragment = fragmentManager.findFragmentByTag("manager");
            homeFragment = fragmentManager.findFragmentByTag("home");
        }
        initView();
        checkPermissionsAndActivationFlow();
        startStatusPolling();
    }

    private void checkPermissionsAndActivationFlow() {
        if (!hasSystemPermissions()) {
            showPermissionGuidanceDialog();
        } else {
            checkActivationFlow();
        }
    }

    private boolean hasSystemPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return android.os.Environment.isExternalStorageManager();
        } else {
            return checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_GRANTED;
        }
    }

    private void showPermissionGuidanceDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.warning_tips)
                .setMessage(tu.getLanguageString(this,R.string.grant_permission_msg))
                .setPositiveButton(R.string.dialog_sure_text, (dialog, which) -> {
                    requestPermissionsFlow();
                })
                .setNegativeButton(R.string.dialog_cancel_text, (dialog, which) -> {
                    checkActivationFlow();
                })
                .setCancelable(false)
                .show();
    }

    public void requestPermissionsFlow() {
        permissionRequest.requestExternalStoragePermission(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            permissionRequest.getExternalStorageManager(this, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkActivationFlow();
    }

    private void checkActivationFlow() {
        if (!ShizukuSystemServerApi.isShizuku() && !DhizukuSystemServerApi.isDhizuku()) {
            showModeDialog(this);
        }
    }

    private void startStatusPolling() {
        if (statusThread != null) statusThread.interrupt();
        statusThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() && !isFinishing()) {
                // 获取当前设定的运行时模式
                int mode = ShizukuSystemServerApi.runtimeMode;
                boolean currentShizuku = false;
                boolean currentDhizuku = false;

                // 核心互锁逻辑：根据 runtimeMode 决定哪个状态有效
                if (mode == ShizukuSystemServerApi.MODE_SHIZUKU) {
                    currentShizuku = ShizukuSystemServerApi.isShizuku();
                } else if (mode == ShizukuSystemServerApi.MODE_DHIZUKU) {
                    currentDhizuku = DhizukuSystemServerApi.isDhizuku();
                } else {
                    // 默认模式下，优先显示已激活的
                    currentShizuku = ShizukuSystemServerApi.isShizuku();
                    currentDhizuku = !currentShizuku && DhizukuSystemServerApi.isDhizuku();
                }

                if (currentShizuku != isShizuku || currentDhizuku != isDhizuku) {
                    isShizuku = currentShizuku;
                    isDhizuku = currentDhizuku;
                    runOnUiThread(() -> {
                        updateTitle();
                        updateAllFragments();
                        if (!isShizuku && !isDhizuku && (lastShizuku || lastDhizuku)) {
                            showAuthLostDialog();
                        }
                        lastShizuku = isShizuku;
                        lastDhizuku = isDhizuku;
                    });
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        statusThread.start();
    }

    private void showAuthLostDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.warning_tips)
                .setMessage(tu.getLanguageString(this,R.string.grant_dead))
                .setPositiveButton(R.string.dialog_sure_text, (dialog, which) -> {
                    showFragment(manageFragment);
                })
                .setCancelable(false)
                .show();
    }

    private void updateTitle() {
        if (isShizuku) {
            setTitle("easyManager [ SHIZUKU ] [ " + uid + " ]");
        } else if (isDhizuku) {
            setTitle("easyManager [ DHIZUKU ] [ " + uid + " ]");
        } else {
            setTitle("easyManager [ General ] [ " + uid + " ]");
        }
    }

    private void updateAllFragments() {
        if (homeFragment instanceof HomeFragmentLayout) {
            ((HomeFragmentLayout) homeFragment).updateAuthStatus(isShizuku, isDhizuku);
        }
        if (helpFragment instanceof HelpFragmentLayout) {
            ((HelpFragmentLayout) helpFragment).updateAuthStatus(isShizuku, isDhizuku);
        }
        if (manageFragment instanceof ManagerGrantUserFragmentLayout) {
            ((ManagerGrantUserFragmentLayout) manageFragment).updateAuthStatus(isShizuku, isDhizuku);
        }
    }

    public void initView(){
        setContentView(R.layout.activity_main);
        MyActivityManager.getIns().add(this);
        uid = getIntent().getIntExtra("uid",0);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        int toolbarHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.topAppBar), (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;

            ViewGroup.LayoutParams lp = v.getLayoutParams();
            lp.height = toolbarHeight + top;
            v.setLayoutParams(lp);

            v.setPadding(0, top, 0, 0);

            return insets;
        });
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_manager) {
                    showFragment(manageFragment, false);
                    return true;
                } else if (id == R.id.nav_home) {
                    showFragment(homeFragment, false);
                    return true;
                } else if (id == R.id.nav_help) {
                    showFragment(helpFragment, false);
                    return true;
                }
                return false;
            }
        });

        try {
            this.getExternalCacheDir().mkdirs();
            this.getCacheDir().mkdirs();
            this.getFilesDir().mkdirs();
        }catch (Exception e){}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L");
        }
        isShizuku = false;
        isDhizuku = false;
        // showModeDialog(this); // Moved to checkActivation()

        String path = this.getExternalCacheDir().toString();



        if(isShizuku){
            setTitle("easyManager [ SHIZUKU ] [ "+uid+" ]");
        }else if(isDhizuku){
            setTitle("easyManager [ DHIZUKU ] [ "+uid+" ]");
        }else{
            setTitle("easyManager [ General ] [ "+uid+" ]");
        }
        updateTitle();

        if (homeFragment == null) {
            homeFragment = new HomeFragmentLayout(isShizuku,isDhizuku,uid);
        }
        if (helpFragment == null) {
            helpFragment =  new HelpFragmentLayout(isShizuku,isDhizuku,uid);
        }

        if(manageFragment == null){
            manageFragment = new ManagerGrantUserFragmentLayout(isShizuku,isDhizuku);
        }

        showFragment(helpFragment);

    }

    public void showFragment(Fragment fragment) {
        showFragment(fragment, true);
    }

    private void showFragment(Fragment fragment, boolean updateNav) {
        if (fragment == null || fragment == currentFragment) return;
        String tag = null;
        if (fragment instanceof HelpFragmentLayout) {
            tag = "help";
            if (updateNav && bottomNavigationView.getSelectedItemId() != R.id.nav_help) {
                bottomNavigationView.setSelectedItemId(R.id.nav_help);
                return;
            }
        } else if (fragment instanceof HomeFragmentLayout) {
            tag = "home";
            if (updateNav && bottomNavigationView.getSelectedItemId() != R.id.nav_home) {
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
                return;
            }
        } else if (fragment instanceof ManagerGrantUserFragmentLayout) {
            tag = "manager";
            if (updateNav && bottomNavigationView.getSelectedItemId() != R.id.nav_manager) {
                bottomNavigationView.setSelectedItemId(R.id.nav_manager);
                return;
            }
        }

        //开启事务 创建事务对象
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //如果之前没有添加过
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.amfl1, fragment, tag);
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


    public void showModeDialog(Context con) {
        // Material Design 风格选择弹窗
        View customView = new DialogBaseUtils().getCustomeDialog(con,
                tu.getLanguageString(con, R.string.tips),
                con.getString(R.string.select_mode_msg));

        new MaterialAlertDialogBuilder(con)
                .setView(customView)
                .setPositiveButton("Shizuku", (dialogInterface, i) -> {
                    ShizukuSystemServerApi.saveMode(con, ShizukuSystemServerApi.MODE_SHIZUKU);
                    ShizukuSystemServerApi.bindRequestPermission();
                    ShizukuSystemServerApi.check(0);
                    runOnUiThread(this::updateTitle);
                })
                .setNegativeButton("Dhizuku", (dialogInterface, i) -> {
                    ShizukuSystemServerApi.saveMode(con, ShizukuSystemServerApi.MODE_DHIZUKU);
                    DhizukuSystemServerApi.check(0);
                    runOnUiThread(this::updateTitle);
                })
                .setNeutralButton(R.string.dialog_cancel_text, null)
                .setCancelable(false)
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyActivityManager.getIns().killall();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_help) {
            dialogUtils.showHelp(this, HelpDialogUtils.MAIN_HELP, 0);
        } else if (itemId == R.id.menu_full_exit) {
            if (ee.isDeviceOwnerActive(this)) {
                ee.forceRemoveDeviceOwner(this);
            }
            if (isShizuku) ShizukuSystemServerApi.dead();
            if (isDhizuku) DhizukuSystemServerApi.dead();
            MyActivityManager.getIns().killall();
        } else if (itemId == R.id.menu_exit) {
            if (isShizuku) ShizukuSystemServerApi.dead();
            if (isDhizuku) DhizukuSystemServerApi.dead();
            MyActivityManager.getIns().killall();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (statusThread != null) {
            statusThread.interrupt();
        }
        if (statusHandler != null && statusRunnable != null) {
            statusHandler.removeCallbacks(statusRunnable);
        }
        if (isShizuku) {
            ShizukuSystemServerApi.dead();
        }

        if (isDhizuku) {
            DhizukuSystemServerApi.dead();
        }

    }
}
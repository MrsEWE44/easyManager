package com.easymanager.fragment;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easymanager.R;
import com.easymanager.core.api.DhizukuSystemServerApi;
import com.easymanager.core.api.ShizukuSystemServerApi;

import com.google.android.material.appbar.MaterialToolbar;

public class ManagerGrantUserFragmentLayout extends Fragment {

    private Boolean isShizuku, isDhizuku;
    private TextView mguflpeekservice;
    private Button btnAuthShizuku, btnAuthDhizuku;
    private Handler pollingHandler = new Handler(Looper.getMainLooper());
    private static final int MAX_POLLING_TIME = 3000;
    private static final int POLLING_INTERVAL = 100;
    private long pollingStartTime = 0;

    public ManagerGrantUserFragmentLayout(Boolean isShizuku, Boolean isDhizuku) {
        this.isShizuku = isShizuku;
        this.isDhizuku = isDhizuku;
    }

    public ManagerGrantUserFragmentLayout() {}

    public void updateAuthStatus(Boolean isShizuku, Boolean isDhizuku) {
        this.isShizuku = isShizuku;
        this.isDhizuku = isDhizuku;
        if (getView() != null) {
            MaterialToolbar toolbar = getView().findViewById(R.id.toolbar);
            if (toolbar != null) {
                updateToolbarTitle(toolbar);
            }
            updateStatus();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vie = inflater.inflate(R.layout.manager_grant_user_fragment_layout, container, false);
        MaterialToolbar toolbar = vie.findViewById(R.id.toolbar);
        if (toolbar != null) {
            updateToolbarTitle(toolbar);
        }
        initViews(vie);
        return vie;
    }

    private void updateToolbarTitle(MaterialToolbar toolbar) {
        String modeText = "[ General ]";
        // 只要获得了授权，就在标题显示对应模式
        if (ShizukuSystemServerApi.isShizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU) modeText = "[ SHIZUKU ]";
        else if (DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU) modeText = "[ DHIZUKU ]";
        toolbar.setTitle(getString(R.string.auth_title) + " " + modeText);
    }

    private void initViews(View vie) {
        mguflpeekservice = vie.findViewById(R.id.mguflpeekservice);
        btnAuthShizuku = vie.findViewById(R.id.btn_auth_shizuku);
        btnAuthDhizuku = vie.findViewById(R.id.btn_auth_dhizuku);

        btnAuthShizuku.setOnClickListener(v -> {
            try {
                // 停掉 Dhizuku 监听
                DhizukuSystemServerApi.dead();

                // 持久化切换到 Shizuku 模式
                ShizukuSystemServerApi.saveMode(getActivity(), ShizukuSystemServerApi.MODE_SHIZUKU);

                // 请求 Shizuku 权限
                ShizukuSystemServerApi.bindRequestPermission();
                ShizukuSystemServerApi.check(0);

                Toast.makeText(getActivity(), "Switching to Shizuku...", Toast.LENGTH_SHORT).show();
                startPollingStatus();
                updateStatus(); // 立即更新 UI 响应点击
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Shizuku switch error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnAuthDhizuku.setOnClickListener(v -> {
            try {
                // 停掉 Shizuku 监听
                ShizukuSystemServerApi.dead();

                // 持久化切换到 Dhizuku 模式
                ShizukuSystemServerApi.saveMode(getActivity(), ShizukuSystemServerApi.MODE_DHIZUKU);

                // 请求 Dhizuku 权限
                DhizukuSystemServerApi.check(0);

                Toast.makeText(getActivity(), "Switching to Dhizuku...", Toast.LENGTH_SHORT).show();
                startPollingStatus();
                updateStatus(); // 立即更新 UI 响应点击
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Dhizuku switch error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        updateStatus();
    }

    private void startPollingStatus() {
        pollingStartTime = System.currentTimeMillis();
        pollingHandler.removeCallbacks(pollingRunnable);
        pollingHandler.post(pollingRunnable);
    }

    private Runnable pollingRunnable = new Runnable() {
        @Override
        public void run() {
            // 关键修复：根据运行时选定的模式来确定活跃状态
            int mode = ShizukuSystemServerApi.runtimeMode;
            boolean currentShizukuAuth = ShizukuSystemServerApi.isShizuku();
            boolean currentShizukuAlive = ShizukuSystemServerApi.isShizukuAlive();
            boolean currentDhizuku = DhizukuSystemServerApi.isDhizuku();

            // 兼容性处理：如果尚未选定模式但服务已运行
            if (mode == ShizukuSystemServerApi.MODE_NONE) {
                currentShizukuAuth = ShizukuSystemServerApi.isShizuku();
                currentDhizuku = !currentShizukuAuth && DhizukuSystemServerApi.isDhizuku();
            }

            // 如果授权状态改变，或者在 Shizuku 模式下存活状态改变，则更新 UI
            if (currentShizukuAuth != Boolean.TRUE.equals(isShizuku) ||
                    currentDhizuku != Boolean.TRUE.equals(isDhizuku) ||
                    (mode == ShizukuSystemServerApi.MODE_SHIZUKU && currentShizukuAuth && currentShizukuAlive != (btnAuthShizuku.getAlpha() < 1.0f))) {

                isShizuku = currentShizukuAuth;
                isDhizuku = currentDhizuku;
                updateStatus();
                if (getView() != null) {
                    updateToolbarTitle(getView().findViewById(R.id.toolbar));
                }
            }

            pollingHandler.postDelayed(this, POLLING_INTERVAL);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        pollingHandler.removeCallbacks(pollingRunnable);
    }

    private void updateStatus() {
        if (getActivity() == null) return;
        String runningMode = "[ NONE ]";
        boolean actualShizukuAuth = ShizukuSystemServerApi.isShizuku();
        boolean actualShizukuAlive = ShizukuSystemServerApi.isShizukuAlive();
        boolean actualDhizuku = DhizukuSystemServerApi.isDhizuku();
        int mode = ShizukuSystemServerApi.runtimeMode;

        // 严格 UI 互锁逻辑：激活后禁用对应按钮，允许切换另一个
        if (mode == ShizukuSystemServerApi.MODE_SHIZUKU) {
            // 当前为 Shizuku 模式
            if (actualShizukuAuth) {
                if (actualShizukuAlive) {
                    runningMode = "[ SHIZUKU ACTIVE ]";
                    btnAuthShizuku.setText("Shizuku: Running");
                    btnAuthShizuku.setEnabled(false); // 已运行，禁用
                    btnAuthShizuku.setAlpha(0.5f);
                } else {
                    runningMode = "[ SHIZUKU (Wait Service) ]";
                    btnAuthShizuku.setText("Shizuku: Auth OK, Wake up?");
                    btnAuthShizuku.setEnabled(true); // 授权OK但服务没起，允许点击唤醒
                    btnAuthShizuku.setAlpha(1.0f);
                }
            } else {
                btnAuthShizuku.setText(R.string.activate_shizuku);
                btnAuthShizuku.setEnabled(true);
                btnAuthShizuku.setAlpha(1.0f);
            }

            // 允许随时切换到 Dhizuku
            btnAuthDhizuku.setText(R.string.switch_to_dhizuku);
            btnAuthDhizuku.setEnabled(true);
            btnAuthDhizuku.setAlpha(1.0f);

        } else if (mode == ShizukuSystemServerApi.MODE_DHIZUKU) {
            // 当前为 Dhizuku 模式
            if (actualDhizuku) {
                runningMode = "[ DHIZUKU ACTIVE ]";
                btnAuthDhizuku.setText("Dhizuku: Running");
                btnAuthDhizuku.setEnabled(false); // 已运行，禁用
                btnAuthDhizuku.setAlpha(0.5f);
            } else {
                btnAuthDhizuku.setText(R.string.activate_dhizuku);
                btnAuthDhizuku.setEnabled(true);
                btnAuthDhizuku.setAlpha(1.0f);
            }

            // 允许随时切换到 Shizuku
            btnAuthShizuku.setText(R.string.switch_to_shizuku);
            btnAuthShizuku.setEnabled(true);
            btnAuthShizuku.setAlpha(1.0f);

        } else {
            // 初始未选择模式 (MODE_NONE)
            btnAuthShizuku.setText(actualShizukuAuth ? "Shizuku Authorized" : "Authorize Shizuku");
            btnAuthDhizuku.setText(actualDhizuku ? "Dhizuku Authorized" : "Authorize Dhizuku");
            btnAuthShizuku.setEnabled(true);
            btnAuthDhizuku.setEnabled(true);
            btnAuthShizuku.setAlpha(1.0f);
            btnAuthDhizuku.setAlpha(1.0f);
        }

        String statusMsg = (actualShizukuAuth && actualShizukuAlive || actualDhizuku)
                ? getString(R.string.server_is_running) + runningMode
                : (actualShizukuAuth ? getString(R.string.service_connecting) + runningMode : getString(R.string.server_is_not_running)+" [ DEAD ]");

        mguflpeekservice.setText(statusMsg);
    }
}

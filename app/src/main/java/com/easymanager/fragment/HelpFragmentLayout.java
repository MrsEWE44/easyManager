package com.easymanager.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easymanager.R;
import com.easymanager.utils.NonScrollExpandableListView;
import com.easymanager.utils.OtherTools;
import com.easymanager.utils.TextUtils;
import com.easymanager.utils.dialog.NetUtilsDialog;

public class HelpFragmentLayout extends Fragment {

    public Boolean isRoot, isADB, isDevice;

    public void updateStatus(Boolean isRoot, Boolean isADB, Boolean isDevice) {
        this.isRoot = isRoot;
        this.isADB = isADB;
        this.isDevice = isDevice;
        if (getView() != null) {
            initBtColor();
        }
    }
    private int uid;

    private Context context;
    private NonScrollExpandableListView hflelv;
    private Button hflcheckupdate,hfl_theme_mode,hflcleanfile,hflopengithub,hflopengitee,hfl_donate;
    private TextView hfl_app_version;
    private NetUtilsDialog nu = new NetUtilsDialog();
    private OtherTools ot = new OtherTools();
    private TextUtils tvvv = nu.tu;

    public HelpFragmentLayout() {
    }

    public HelpFragmentLayout(Boolean isRoot, Boolean isADB ,Boolean isDevice, int uid) {
        this.isRoot = isRoot;
        this.isADB = isADB;
        this.isDevice = isDevice;
        this.uid = uid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        View inflate = inflater.inflate(R.layout.help_fragment_layout, container, false);
        hflelv = inflate.findViewById(R.id.hflelv);
        hflcheckupdate = inflate.findViewById(R.id.hflcheckupdate);
        hfl_theme_mode = inflate.findViewById(R.id.hfl_theme_mode);
        hflcleanfile = inflate.findViewById(R.id.hflcleanfile);
        hflopengithub = inflate.findViewById(R.id.hflopengithub);
        hflopengitee = inflate.findViewById(R.id.hflopengitee);
        hfl_donate = inflate.findViewById(R.id.hfl_donate);
        hfl_app_version = inflate.findViewById(R.id.hfl_app_version);

        updateThemeButtonText();
        initAppInfo();
        hflelv.setAdapter(getadapter());
        btClicked();
        initBtColor();
        return inflate;
    }

    private void initAppInfo() {
        try {
            android.content.pm.PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String versionStr = getString(R.string.help_app_version_format, pInfo.versionName);
            hfl_app_version.setText(versionStr);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void btClicked() {
        hflcheckupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nu.checkupdate(context);
            }
        });

        hfl_theme_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleTheme();
            }
        });

        hflcleanfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nu.ft.clearAppFiles(context, uid);
                Toast.makeText(context, R.string.app_clean_toast_msg,Toast.LENGTH_SHORT).show();
            }
        });

        hflopengithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nu.openUrlWithBrowser(context,"https://github.com/MrsEWE44/easyManager");
            }
        });

        hflopengitee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nu.openUrlWithBrowser(context,"https://gitee.com/SorryMyLife/easyManager");
            }
        });

        hfl_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDonateDialog();
            }
        });


    }

    private void toggleTheme() {
        int currentMode = com.easymanager.utils.ThemeUtils.getThemeMode(context);
        int nextMode;
        if (currentMode == com.easymanager.utils.ThemeUtils.MODE_SYSTEM) {
            nextMode = com.easymanager.utils.ThemeUtils.MODE_LIGHT;
        } else if (currentMode == com.easymanager.utils.ThemeUtils.MODE_LIGHT) {
            nextMode = com.easymanager.utils.ThemeUtils.MODE_DARK;
        } else {
            nextMode = com.easymanager.utils.ThemeUtils.MODE_SYSTEM;
        }
        com.easymanager.utils.ThemeUtils.setThemeMode(context, nextMode);
        getActivity().recreate();
    }

    private void updateThemeButtonText() {
        int mode = com.easymanager.utils.ThemeUtils.getThemeMode(context);
        String text;
        if (mode == com.easymanager.utils.ThemeUtils.MODE_LIGHT) {
            text = getString(R.string.help_theme_mode_button) + ": " + getString(R.string.theme_mode_light);
        } else if (mode == com.easymanager.utils.ThemeUtils.MODE_DARK) {
            text = getString(R.string.help_theme_mode_button) + ": " + getString(R.string.theme_mode_dark);
        } else {
            text = getString(R.string.help_theme_mode_button) + ": " + getString(R.string.theme_mode_system);
        }
        hfl_theme_mode.setText(text);
    }

    private void showDonateDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(R.string.help_donate_title);
        
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);
        layout.setGravity(android.view.Gravity.CENTER);

        TextView tip = new TextView(context);
        tip.setText(R.string.help_donate_thanks);
        tip.setGravity(android.view.Gravity.CENTER);
        tip.setPadding(0, 0, 0, 20);
        layout.addView(tip);

        LinearLayout qrLayout = new LinearLayout(context);
        qrLayout.setOrientation(LinearLayout.HORIZONTAL);
        qrLayout.setGravity(android.view.Gravity.CENTER);

        // 微信二维码 (暂用系统图标代替)
        LinearLayout wxLayout = new LinearLayout(context);
        wxLayout.setOrientation(LinearLayout.VERTICAL);
        wxLayout.setGravity(android.view.Gravity.CENTER);
        ImageView wxImg = new ImageView(context);
        wxImg.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        nu.ft.setImageViewImg(context,"wechatqr.jpg",wxImg);
        TextView wxTv = new TextView(context);
        wxTv.setText(R.string.help_donate_wechat);
        wxLayout.addView(wxImg);
        wxLayout.addView(wxTv);

        // 支付宝二维码
        LinearLayout aliLayout = new LinearLayout(context);
        aliLayout.setOrientation(LinearLayout.VERTICAL);
        aliLayout.setGravity(android.view.Gravity.CENTER);
        aliLayout.setPadding(20, 0, 0, 0);
        ImageView aliImg = new ImageView(context);
        aliImg.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        nu.ft.setImageViewImg(context,"aliqr.jpg",aliImg);
        TextView aliTv = new TextView(context);
        aliTv.setText(R.string.help_donate_alipay);
        aliLayout.addView(aliImg);
        aliLayout.addView(aliTv);

        qrLayout.addView(wxLayout);
        qrLayout.addView(aliLayout);
        layout.addView(qrLayout);

        builder.setView(layout);
        builder.setPositiveButton(R.string.dialog_sure_text, null);
        builder.show();
    }

    private void initBtColor(){

        ot.setBtColor(hflcheckupdate,false,false,false,isRoot,isADB,isDevice);
        ot.setBtColor(hfl_theme_mode,false,false,false,isRoot,isADB,isDevice);
        ot.setBtColor(hflcleanfile,true,true,false,isRoot,isADB,isDevice);
        ot.setBtColor(hflopengithub,false,false,false,isRoot,isADB,isDevice);
        ot.setBtColor(hflopengitee,false,false,false,isRoot,isADB,isDevice);
        ot.setBtColor(hfl_donate, false, false, false, isRoot, isADB, isDevice);
    }


    private ExpandableListAdapter getadapter() {

        return  new BaseExpandableListAdapter() {
            private String[] parn = new String[]{getLanStr(R.string.help_question_1), getLanStr(R.string.help_question_2), getLanStr(R.string.help_question_3)};
            // 每个列表下面的子列表字符数组
            private String[] child = new String[]{getLanStr(R.string.help_question_1_reply), getLanStr(R.string.help_question_2_reply), getLanStr(R.string.help_question_3_reply)};

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            // 构建每个父列表组件
            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                View inflate = View.inflate(HelpFragmentLayout.this.getActivity(), R.layout.help_ex_item_parent_layout, null);
                TextView exparent = inflate.findViewById(R.id.exparent);
                exparent.setText(parn[groupPosition]);
                return inflate;
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public int getGroupCount() {
                return parn.length;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return parn[groupPosition];
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return 1;
            }

            // 构建子列表的数据
            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                View inflate = null;
                if (convertView == null) {
                    inflate = View.inflate(HelpFragmentLayout.this.getActivity(), R.layout.help_ex_item_child_layout, null);
                } else {
                    inflate = convertView;
                }
                TextView exparent = inflate.findViewById(R.id.exchild);
                exparent.setText(child[groupPosition]);
                return inflate;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return child[childPosition];
            }
        };



    }

    private String getLanStr(int id) {
        return tvvv.getLanguageString(context, id);
    }


}



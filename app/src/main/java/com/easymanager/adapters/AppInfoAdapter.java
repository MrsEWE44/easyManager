package com.easymanager.adapters;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.easymanager.R;
import com.easymanager.core.api.PackageAPI;
import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.enums.AppInfoEnums;
import com.easymanager.utils.OtherTools;
import com.easymanager.utils.PermissionUtils;
import com.easymanager.utils.easyManagerUtils;

import java.util.ArrayList;

public class AppInfoAdapter extends BaseAdapter {

    private ArrayList<String> list;
    private Context context;
    private ArrayList<Boolean> checkboxs, switbs;
    private String pkgname;
    private int mode;
    private Integer uid;
    private int type;

    public AppInfoAdapter(ArrayList<String> list, Context context, ArrayList<Boolean> checkboxs, ArrayList<Boolean> switbs, String pkgname, int mode, Integer uid, int type) {
        this.list = (ArrayList<String>) list.clone();
        this.context = context;
        this.checkboxs = checkboxs;
        this.switbs = switbs;
        this.pkgname = pkgname;
        this.mode = mode;
        this.uid = uid;
        this.type = type;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.app_info_item_layout, null);
        CheckBox checkBox = view.findViewById(R.id.aiilcb1);
        TextView text = view.findViewById(R.id.aiiltv1_title);
        TextView subText = view.findViewById(R.id.aiiltv1_sub);
        Switch switchbbb = view.findViewById(R.id.aiilsb1);

        String rawName = list.get(i);
        String label = rawName;
        if (mode == AppInfoEnums.IS_PERMISSION) {
            label = PermissionUtils.getTranslatePermissionName(rawName);
            if (label.equals(rawName)) {
                label = PermissionUtils.getPermissionLabel(context, rawName);
            }
            text.setText(label);
            subText.setText(rawName);
            subText.setVisibility(View.VISIBLE);
        } else {
            text.setText(rawName);
            subText.setVisibility(View.GONE);
        }

        final String finalLabel = label;
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String[] options = {
                        "生成 ADB 命令(带 adb shell)",
                        "生成 ADB 命令(不带 adb shell)",
                        "生成命令 (root)"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(finalLabel);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder sb = new StringBuilder();
                        boolean isEnabled = switbs.get(i);
                        
                        // Add comment with translated name/type
                        if (mode == AppInfoEnums.IS_PERMISSION) {
                            sb.append("# 权限：").append(finalLabel).append("\n");
                        } else {
                            String typeStr = "组件";
                            if (type == AppInfoEnums.GET_SERVICES) typeStr = "服务";
                            else if (type == AppInfoEnums.GET_ACTIVITYS) typeStr = "活动";
                            else if (type == AppInfoEnums.GET_RECEIVERS) typeStr = "广播接收器";
                            sb.append("# ").append(typeStr).append("：").append(rawName).append("\n");
                        }

                        if (which == 0) {
                            sb.append("adb shell ");
                        }

                        if (mode == AppInfoEnums.IS_PERMISSION) {
                            easyManagerUtils eu = new easyManagerUtils();
                            String op = eu.permissionToOp(context, rawName, uid);
                            if (op != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                String appopsAction = isEnabled ? "deny" : "allow";
                                sb.append("appops set --user ").append(uid).append(" ").append(pkgname).append(" ").append(op).append(" ").append(appopsAction);
                            } else {
                                String pmAction = isEnabled ? "revoke" : "grant";
                                sb.append("pm ").append(pmAction).append(" --user ").append(uid).append(" ").append(pkgname).append(" ").append(rawName);
                            }
                        } else {
                            String pmState = isEnabled ? "disable" : "enable";
                            sb.append("pm ").append(pmState).append(" --user ").append(uid).append(" ").append(pkgname).append("/").append(rawName);
                        }

                        String result = sb.toString();
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("ADB Command", result);
                        if (clipboard != null) {
                            clipboard.setPrimaryClip(clip);
                            OtherTools.showInfoMsg(context, context.getString(R.string.tips), context.getString(R.string.is_copy_ok) + "\n\n" + result);
                        }
                    }
                });
                builder.show();
                return true;
            }
        });

        switchbbb.setChecked(switbs.get(i));
        switchbbb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switbs.set(i, b);
                easyManagerUtils eu = new easyManagerUtils();
                if (mode == AppInfoEnums.IS_PERMISSION) {
                    TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, rawName, context.getPackageName(), 0, uid);
                    if (b) {
                        eu.grantRuntimePermission(transmissionEntity);
                    } else {
                        eu.revokeRuntimePermission(transmissionEntity);
                    }
                }
                if (mode == AppInfoEnums.IS_COMPENT_OR_PACKAGE) {
                    eu.setComponentOrPackageEnabledState(new TransmissionEntity(pkgname + "/" + rawName, rawName, context.getPackageName(), b ? PackageAPI.COMPONENT_ENABLED_STATE_ENABLED : PackageAPI.COMPONENT_ENABLED_STATE_DISABLED, uid));
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkboxs.set(i, b);
            }
        });
        checkBox.setChecked(checkboxs.get(i));
        return view;
    }
}

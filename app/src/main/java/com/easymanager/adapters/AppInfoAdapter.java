package com.easymanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.easymanager.R;
import com.easymanager.core.api.PackageAPI;
import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.enums.AppInfoEnums;
import com.easymanager.utils.easyManagerUtils;

import java.util.ArrayList;

public class AppInfoAdapter extends BaseAdapter {


    public AppInfoAdapter(ArrayList<String> list, Context context, ArrayList<Boolean> checkboxs, ArrayList<Boolean> switbs, String pkgname, int mode,Integer uid) {
        this.list = (ArrayList<String>) list.clone();
        this.context = context;
        this.checkboxs = (ArrayList<Boolean>) checkboxs.clone();
        this.switbs = (ArrayList<Boolean>) switbs.clone();
        this.pkgname = pkgname;
        this.mode = mode;
        this.uid = uid;
        notifyDataSetChanged();
    }

    private ArrayList<String> list;
    private Context context;
    private ArrayList<Boolean> checkboxs,switbs;
    private String pkgname ;
    private int mode;
    private Integer uid;

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
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.app_info_item_layout, viewGroup, false);
        }
        MaterialCheckBox checkBox = view.findViewById(R.id.aiilcb1);
        TextView text = view.findViewById(R.id.aiiltv1);
        MaterialSwitch switchbbb = view.findViewById(R.id.aiilsb1);

        if (i >= list.size() || i >= switbs.size() || i >= checkboxs.size()) {
            return view;
        }
        
        String originalItem = list.get(i);
        String displayItem = originalItem;
        if (mode == AppInfoEnums.IS_PERMISSION) {
            displayItem = translatePermission(context, originalItem);
        }
        text.setText(displayItem);

        switchbbb.setOnCheckedChangeListener(null);
        switchbbb.setChecked(switbs.get(i));
        switchbbb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switbs.set(i,b);
                switchbbb.setChecked(switbs.get(i));
                String pkgcate = originalItem;
//                CMD cmd = new CMD(new appopsCmdStr().getRunAppopsBySwtichCMD(b, mode, pkgname, pkgcate, uid));
                easyManagerUtils eu = new easyManagerUtils();
                if(mode == AppInfoEnums.IS_PERMISSION){
                    TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, pkgcate, context.getPackageName(), 0,uid);
                    if(b){
                        eu.grantRuntimePermission(transmissionEntity);
                    }else {
                        eu.revokeRuntimePermission(transmissionEntity);
                    }
                }
                if(mode == AppInfoEnums.IS_COMPENT_OR_PACKAGE){
                    eu.setComponentOrPackageEnabledState(new TransmissionEntity(pkgname+"/"+pkgcate,pkgcate, context.getPackageName(), b? PackageAPI.COMPONENT_ENABLED_STATE_ENABLED:PackageAPI.COMPONENT_ENABLED_STATE_DISABLED,Integer.valueOf(uid)));
                }

            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkboxs.set(i,b);
            }
        });
        checkBox.setChecked(checkboxs.get(i));

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showContextMenu(i, v);
                return true;
            }
        });

        return view;
    }

    private void showContextMenu(int position, View v) {
        android.widget.PopupMenu popup = new android.widget.PopupMenu(context, v);
        popup.getMenu().add(0, 1, 0, getLanStr(R.string.menu_copy_adb_pc));
        popup.getMenu().add(0, 2, 1, getLanStr(R.string.menu_copy_adb));
        popup.getMenu().add(0, 3, 2, getLanStr(R.string.menu_copy_text_permission));

        popup.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                String originalItem = list.get(position);
                boolean isEnabled = switbs.get(position);
                String command = "";
                switch (item.getItemId()) {
                    case 1: // Copy as PC ADB
                        command = generateAdbCommand(originalItem, isEnabled, true);
                        copyText(command);
                        break;
                    case 2: // Copy as ADB
                        command = generateAdbCommand(originalItem, isEnabled, false);
                        copyText(command);
                        break;
                    case 3: // Copy text and permission
                        String translated = translatePermission(context, originalItem);
                        copyText(translated + "\n" + originalItem);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    private String generateAdbCommand(String item, boolean currentEnabled, boolean isPc) {
        StringBuilder sb = new StringBuilder();
        if (isPc) sb.append("adb shell ");
        
        String action = currentEnabled ? "revoke" : "grant"; // We want to toggle, or if enabled, provide command to disable
        // User asked: "如果右边switch按钮的状态是enable状态，那命令就是disable的"
        // For permissions: grant/revoke
        // For components: enable/disable
        
        if (mode == AppInfoEnums.IS_PERMISSION) {
            String op = new easyManagerUtils().permissionToOp(context, item, uid);
            if (op != null) {
                // appops set <pkg> <OP> <mode>
                String modeStr = currentEnabled ? "deny" : "allow";
                sb.append("appops set --user ").append(uid / 100000).append(" ").append(pkgname).append(" ").append(op).append(" ").append(modeStr);
            } else {
                // pm grant/revoke
                String pmAction = currentEnabled ? "revoke" : "grant";
                sb.append("pm ").append(pmAction).append(" --user ").append(uid / 100000).append(" ").append(pkgname).append(" ").append(item);
            }
        } else if (mode == AppInfoEnums.IS_COMPENT_OR_PACKAGE) {
            String pmAction = currentEnabled ? "disable" : "enable";
            sb.append("pm ").append(pmAction).append(" --user ").append(uid / 100000).append(" ").append(pkgname).append("/").append(item);
        }
        
        return sb.toString();
    }

    private String translatePermission(Context context, String permission) {
        return new com.easymanager.utils.TextUtils().translatePermission(context, permission);
    }

    private String getLanStr(int id) {
        return context.getResources().getString(id);
    }

    private void copyText(String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        android.widget.Toast.makeText(context, getLanStr(R.string.is_copy_ok), android.widget.Toast.LENGTH_SHORT).show();
    }
}


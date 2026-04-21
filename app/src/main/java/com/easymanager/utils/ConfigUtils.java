package com.easymanager.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.easymanager.entitys.PKGINFO;
import com.easymanager.enums.AppManagerEnum;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ConfigUtils {
    public static final String CONFIG_FILE_NAME = "lightBreeze_pkg.json";
    public static final String FIELD_DISABLED = "disabled_pkgs";
    public static final String FIELD_UNINSTALLED = "uninstalled_pkgs";

    private JSONObject readConfig(File file) {
        JSONObject root = new JSONObject();
        try {
            // Pre-fill with empty arrays to ensure they always exist
            root.put(FIELD_DISABLED, new JSONArray());
            root.put(FIELD_UNINSTALLED, new JSONArray());

            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] data = new byte[(int) file.length()];
                    int read = fis.read(data);
                    if (read > 0) {
                        JSONObject existing = new JSONObject(new String(data, 0, read, "UTF-8"));
                        if (existing.has(FIELD_DISABLED)) {
                            root.put(FIELD_DISABLED, existing.getJSONArray(FIELD_DISABLED));
                        }
                        if (existing.has(FIELD_UNINSTALLED)) {
                            root.put(FIELD_UNINSTALLED, existing.getJSONArray(FIELD_UNINSTALLED));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    private void writeConfig(File file, JSONObject root) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(root.toString().getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePkgs(Context context, ArrayList<PKGINFO> list, int mode) {
        if (list == null || list.isEmpty()) return;
        try {
            File file = new File(context.getFilesDir(), CONFIG_FILE_NAME);
            JSONObject root = readConfig(file);
            
            String field;
            if (mode == AppManagerEnum.APP_DISABLE_COMPENT) {
                field = FIELD_DISABLED;
            } else if (mode == AppManagerEnum.APP_UNINSTALL || mode == AppManagerEnum.APP_RESTORE_UNINSTALLED) {
                field = FIELD_UNINSTALLED;
            } else {
                return; // Do nothing for other modes
            }

            JSONArray jsonArray = root.getJSONArray(field);
            Set<String> pkgSet = new HashSet<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                pkgSet.add(jsonArray.getString(i));
            }

            for (PKGINFO pkg : list) {
                pkgSet.add(pkg.getPkgname());
            }

            JSONArray newArray = new JSONArray();
            for (String pkgName : pkgSet) {
                newArray.put(pkgName);
            }
            root.put(field, newArray);
            
            // Re-verify that both fields exist before writing to avoid accidental data loss
            if (!root.has(FIELD_DISABLED)) root.put(FIELD_DISABLED, new JSONArray());
            if (!root.has(FIELD_UNINSTALLED)) root.put(FIELD_UNINSTALLED, new JSONArray());
            
            writeConfig(file, root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removePkgs(Context context, ArrayList<PKGINFO> list, int mode) {
        if (list == null || list.isEmpty()) return;
        try {
            File file = new File(context.getFilesDir(), CONFIG_FILE_NAME);
            if (!file.exists()) return;

            JSONObject root = readConfig(file);
            String field;
            if (mode == AppManagerEnum.APP_DISABLE_COMPENT) {
                field = FIELD_DISABLED;
            } else if (mode == AppManagerEnum.APP_UNINSTALL || mode == AppManagerEnum.APP_RESTORE_UNINSTALLED) {
                field = FIELD_UNINSTALLED;
            } else {
                return;
            }

            if (!root.has(field)) return;

            JSONArray jsonArray = root.getJSONArray(field);
            Set<String> pkgSet = new HashSet<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                pkgSet.add(jsonArray.getString(i));
            }

            for (PKGINFO pkg : list) {
                pkgSet.remove(pkg.getPkgname());
            }

            JSONArray newArray = new JSONArray();
            for (String pkgName : pkgSet) {
                newArray.put(pkgName);
            }
            root.put(field, newArray);
            writeConfig(file, root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean exportConfig(Context context) {
        try {
            File srcFile = new File(context.getFilesDir(), CONFIG_FILE_NAME);
            if (!srcFile.exists()) return false;

            File destDir = new File(Environment.getExternalStorageDirectory(), "easyManager");
            if (!destDir.exists()) destDir.mkdirs();

            File destFile = new File(destDir, CONFIG_FILE_NAME);
            copyFile(srcFile, destFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String> importConfig(Context context, File selectedFile, int mode) {
        Set<String> pkgSet = new HashSet<>();
        try (FileInputStream fis = new FileInputStream(selectedFile)) {
            byte[] data = new byte[(int) selectedFile.length()];
            int read = fis.read(data);
            if (read <= 0) return new ArrayList<>();

            JSONObject root = new JSONObject(new String(data, 0, read, "UTF-8"));
            PackageManager pm = context.getPackageManager();

            String[] fields = {FIELD_DISABLED, FIELD_UNINSTALLED};
            for (String field : fields) {
                if (root.has(field)) {
                    JSONArray jsonArray = root.getJSONArray(field);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String pkgName = jsonArray.getString(i);
                        if (mode == AppManagerEnum.APP_RESTORE_UNINSTALLED) {
                            if (!isPackageInstalled(pm, pkgName)) {
                                pkgSet.add(pkgName);
                            }
                        } else {
                            if (isPackageInstalled(pm, pkgName)) {
                                pkgSet.add(pkgName);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>(pkgSet);
    }

    private boolean isPackageInstalled(PackageManager pm, String packageName) {
        try {
            pm.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void copyFile(File src, File dst) throws Exception {
        try (FileInputStream inStream = new FileInputStream(src);
             FileOutputStream outStream = new FileOutputStream(dst)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
        }
    }
}

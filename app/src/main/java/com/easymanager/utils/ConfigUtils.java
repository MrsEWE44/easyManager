package com.easymanager.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Xml;
import android.widget.Toast;

import com.easymanager.R;
import com.easymanager.entitys.LightBreezeConfig;
import com.easymanager.entitys.PKGINFO;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConfigUtils {

    public static final String CONFIG_FILE_NAME = "lightBreeze_pkg.json";
    public static final int MENU_IMPORT_CONFIG = 1001;
    public static final int MENU_EXPORT_CONFIG = 1002;

    public FileTools ft = new FileTools();

    // --- JSON Config (LightBreeze) ---

    public void savePkgsToConfig(Context context, ArrayList<PKGINFO> list, boolean isDisable) {
        try {
            File configFile = new File(context.getFilesDir(), CONFIG_FILE_NAME);
            LightBreezeConfig config = new LightBreezeConfig();

            if (configFile.exists()) {
                FileInputStream fis = new FileInputStream(configFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(sb.toString());
                if (jsonObject.has("disablePkgs")) {
                    JSONArray disableArray = jsonObject.getJSONArray("disablePkgs");
                    ArrayList<String> disablePkgs = new ArrayList<>();
                    for (int i = 0; i < disableArray.length(); i++) {
                        disablePkgs.add(disableArray.getString(i));
                    }
                    config.setDisablePkgs(disablePkgs);
                }
                if (jsonObject.has("uninstallPkgs")) {
                    JSONArray uninstallArray = jsonObject.getJSONArray("uninstallPkgs");
                    ArrayList<String> uninstallPkgs = new ArrayList<>();
                    for (int i = 0; i < uninstallArray.length(); i++) {
                        uninstallPkgs.add(uninstallArray.getString(i));
                    }
                    config.setUninstallPkgs(uninstallPkgs);
                }
            }

            Set<String> pkgSet;
            if (isDisable) {
                pkgSet = new HashSet<>(config.getDisablePkgs());
                for (PKGINFO info : list) pkgSet.add(info.getPkgname());
                config.setDisablePkgs(new ArrayList<>(pkgSet));
            } else {
                pkgSet = new HashSet<>(config.getUninstallPkgs());
                for (PKGINFO info : list) pkgSet.add(info.getPkgname());
                config.setUninstallPkgs(new ArrayList<>(pkgSet));
            }

            JSONObject outJson = new JSONObject();
            JSONArray disableArray = new JSONArray(config.getDisablePkgs());
            JSONArray uninstallArray = new JSONArray(config.getUninstallPkgs());
            outJson.put("disablePkgs", disableArray);
            outJson.put("uninstallPkgs", uninstallArray);

            FileOutputStream fos = new FileOutputStream(configFile);
            fos.write(outJson.toString().getBytes("UTF-8"));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportConfig(Context context) {
        try {
            File srcFile = new File(context.getFilesDir(), CONFIG_FILE_NAME);
            if (!srcFile.exists()) return;
            String sdPath = ft.getExternalStorageDirectory() + "/easyManager/";
            File dir = new File(sdPath);
            if (!dir.exists()) dir.mkdirs();
            File destFile = new File(dir, CONFIG_FILE_NAME);

            FileChannel srcChannel = new FileInputStream(srcFile).getChannel();
            FileChannel destChannel = new FileOutputStream(destFile).getChannel();
            destChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            destChannel.close();
            Toast.makeText(context, context.getString(R.string.export_config_ok), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.export_config_error), Toast.LENGTH_SHORT).show();
        }
    }

    public LightBreezeConfig loadJSONConfig(Context context, Uri uri) {
        try {
            String path = ft.uriToFilePath(uri, context);
            if (path == null) return null;
            File file = new File(path);
            if (!file.exists()) return null;

            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(sb.toString());
            LightBreezeConfig config = new LightBreezeConfig();
            if (jsonObject.has("disablePkgs")) {
                JSONArray disableArray = jsonObject.getJSONArray("disablePkgs");
                ArrayList<String> disablePkgs = new ArrayList<>();
                for (int i = 0; i < disableArray.length(); i++) {
                    disablePkgs.add(disableArray.getString(i));
                }
                config.setDisablePkgs(disablePkgs);
            }
            if (jsonObject.has("uninstallPkgs")) {
                JSONArray uninstallArray = jsonObject.getJSONArray("uninstallPkgs");
                ArrayList<String> uninstallPkgs = new ArrayList<>();
                for (int i = 0; i < uninstallArray.length(); i++) {
                    uninstallPkgs.add(uninstallArray.getString(i));
                }
                config.setUninstallPkgs(uninstallPkgs);
            }
            return config;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // --- XML Config (Legacy/Core) ---

    public void deleteConfig(String requpkg, int state, String path, String filename) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            File file1 = getConfigFile(path, filename);
            if (!file1.exists()) return;
            FileInputStream fis = new FileInputStream(file1);
            parser.setInput(fis, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("config") && parser.getAttributeValue(null, "pkg").equals(requpkg) && parser.getAttributeValue(null, "state").equals(String.valueOf(state))) {
                        parser.next();
                        continue;
                    }
                }
                eventType = parser.next();
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getConfigFile(String path, String filename) {
        String config_path = path + "/" + filename;
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        return new File(config_path);
    }

    public void changeConfig(String requpkg, int newstate, String path, String filename) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            File file1 = getConfigFile(path, filename);
            if (!file1.exists()) return;
            FileInputStream fis = new FileInputStream(file1);
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            serializer.setOutput(writer);
            parser.setInput(fis, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("config")) {
                        serializer.startTag("", "config");
                        if (parser.getAttributeValue(null, "pkg").equals(requpkg)) {
                            serializer.attribute("", "pkg", requpkg);
                            serializer.attribute("", "state", newstate + "");
                        } else {
                            serializer.attribute("", "pkg", parser.getAttributeValue(null, "pkg"));
                            serializer.attribute("", "state", parser.getAttributeValue(null, "state"));
                        }
                        serializer.endTag("", "config");
                        parser.next();
                    }
                }
                eventType = parser.next();
            }
            fis.close();
            serializer.endDocument();
            ft.writeDataToPath(writer.toString(), path + "/" + filename, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeConfig(String requpkg, int state, String path, String filename) {
        File file = getConfigFile(path, filename);
        try {
            XmlSerializer serializer = Xml.newSerializer();
            FileOutputStream fos = new FileOutputStream(file, true);
            serializer.setOutput(fos, "utf-8");
            serializer.startTag(null, "config");
            serializer.attribute("", "pkg", requpkg);
            serializer.attribute("", "state", state + "");
            serializer.endTag(null, "config");
            serializer.endDocument();
            fos.close();
        } catch (IOException e) {
            System.out.println("write error  ::: " + e.toString());
        }
    }

    public HashMap<String, Integer> getConfigs(String cachePath, String filename) {
        File file1 = getConfigFile(cachePath, filename);
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        if (file1.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file1);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(fis, "UTF-8");
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (parser.getName().equals("config")) {
                            String key = parser.getAttributeValue(null, "state");
                            String value = parser.getAttributeValue(null, "pkg");
                            hashMap.put(value, Integer.valueOf(key.trim()));
                        }
                    }
                    eventType = parser.next();
                }
                fis.close();
            } catch (Exception e) {
                System.out.println("read error ::: " + e.toString());
                e.printStackTrace();
            }
        }
        return hashMap;
    }

    public String getConfig(String requestpkg, String cachePath, String filename) {
        HashMap<String, Integer> configs = getConfigs(cachePath, filename);
        for (Map.Entry<String, Integer> entry : configs.entrySet()) {
            if (entry.getKey().equals(requestpkg)) {
                return entry.getValue().toString();
            }
        }
        return null;
    }

    public String getCachePathOnXML() {
        return "/data/local/tmp/easyManager";
    }

    public String getGrantUserConfigName() {
        return "grantuser.xml";
    }

    public String getCleanAPPConfigName() {
        return "cleanapp.xml";
    }

    public HashMap<String, Integer> getGrantUsers() {
        return getConfigs(getCachePathOnXML(), getGrantUserConfigName());
    }

    public HashMap<String, Integer> getCleanAPPs() {
        return getConfigs(getCachePathOnXML(), getCleanAPPConfigName());
    }

    public Integer getGrantUserState(String requestpkg) {
        String value = getConfig(requestpkg, getCachePathOnXML(), getGrantUserConfigName());
        try {
            return Integer.valueOf(value.trim());
        } catch (Exception e) {
        }
        return -1;
    }

    public Integer getCleanAPPConfigTime(String requestpkg) {
        String value = getConfig(requestpkg, getCachePathOnXML(), getCleanAPPConfigName());
        try {
            return Integer.valueOf(value.trim());
        } catch (Exception e) {
        }
        return -1;
    }

    public int changeGrantUserState(String requestpkg) {
        String config = getConfig(requestpkg, getCachePathOnXML(), getGrantUserConfigName());
        if (config != null) {
            changeConfig(requestpkg, config.equals("0") ? 1 : 0, getCachePathOnXML(), getGrantUserConfigName());
            return 0;
        }
        return -1;
    }

    public int changeCleanAPPConfigTime(String requestpkg, int time) {
        String config = getConfig(requestpkg, getCachePathOnXML(), getCleanAPPConfigName());
        if (config != null) {
            changeConfig(requestpkg, time, getCachePathOnXML(), getCleanAPPConfigName());
            return 0;
        }
        return -1;
    }

    public int requestGrantUserState(String requestpkg) {
        if (getConfig(requestpkg, getCachePathOnXML(), getGrantUserConfigName()) == null) {
            writeConfig(requestpkg, requestpkg.equals("com.easymanager") ? 0 : 1, getCachePathOnXML(), getGrantUserConfigName());
        }
        return getConfig(requestpkg, getCachePathOnXML(), getGrantUserConfigName()) != null ? 1 : -1;
    }

    public int addCleanAPPConfigTime(String requestpkg, int time) {
        if (getConfig(requestpkg, getCachePathOnXML(), getCleanAPPConfigName()) == null) {
            writeConfig(requestpkg, time, getCachePathOnXML(), getCleanAPPConfigName());
        }
        return getConfig(requestpkg, getCachePathOnXML(), getCleanAPPConfigName()) != null ? 1 : -1;
    }

    public void deleteCleanAPPConfig() {
        ft.deleteFile(getCachePathOnXML() + "/" + getCleanAPPConfigName());
    }
}

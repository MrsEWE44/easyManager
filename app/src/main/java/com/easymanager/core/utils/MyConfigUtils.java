package com.easymanager.core.utils;

import android.util.Xml;

import com.easymanager.utils.FileTools;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class MyConfigUtils {

    public FileTools ft = new FileTools();

    public void deleteConfig(String requpkg,int state,String path,String filename){
        try {
            // 创建 XmlPullParser 对象
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            File file1 = getConfigFile(path,filename);
            FileInputStream fis = new FileInputStream(file1);
            // 设置输入流
            parser.setInput(fis,"UTF-8");
            // 开始解析文档
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    // 判断是否是要删除的元素
                    if (parser.getName().equals("config") && parser.getAttributeValue(null, "pkg").equals(requpkg) && parser.getAttributeValue(null,"state").equals(state)) {
                        // 删除该元素
                        parser.next();
                        continue;
                    }
                }
                eventType = parser.next();
            }
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public File getConfigFile(String path,String filename){
        String config_path = path+"/"+filename;
        File file1 = new File(path);
        if(!file1.exists()){
            file1.mkdirs();
        }
        return new File(config_path);
    }

    public void changeConfig(String requpkg,int newstate,String path,String filename){
        try {
            // 创建 XmlPullParser 和 XmlSerializer 对象，以及输入输出流
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            File file1 = getConfigFile(path,filename);
            FileInputStream fis = new FileInputStream(file1);
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            serializer.setOutput(writer);
            parser.setInput(fis,"UTF-8");
            // 开始解析文档
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    // 判断是否是要修改的元素
                    if (parser.getName().equals("config")) {
                        serializer.startTag("", "config");
                        if(parser.getAttributeValue(null, "pkg").equals(requpkg)){
                            // 修改元素内容
                            serializer.attribute("", "pkg", requpkg);
                            serializer.attribute("", "state", newstate+"");
                        }else{
                            serializer.attribute("", "pkg", parser.getAttributeValue(null,"pkg"));
                            serializer.attribute("", "state", parser.getAttributeValue(null, "state"));
                        }
                        serializer.endTag("", "config");
                        // 跳过原始元素
                        parser.next();
                    }
                }
                eventType = parser.next();
            }
            fis.close();
            serializer.endDocument();
            ft.writeDataToPath(writer.toString(),path+"/"+filename,false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeConfig(String requpkg,int state,String path,String filename){
        File file = getConfigFile(path,filename);
        try {
            XmlSerializer serializer = Xml.newSerializer();
            FileOutputStream fos = new FileOutputStream(file,true);
            serializer.setOutput(fos, "utf-8");
            serializer.startTag(null, "config");
            serializer.attribute("", "pkg", requpkg);
            serializer.attribute("", "state", state+"");
            serializer.endTag(null, "config");
            serializer.endDocument();
            fos.close();
        }catch (IOException e){
            System.out.println("write error  ::: " + e.toString());
        }
    }

    public HashMap<String,Integer> getConfigs(String cachePath, String filename){
        File file1 = getConfigFile(cachePath,filename);
        HashMap<String,Integer> hashMap = new HashMap<String,Integer>();
        if(file1.exists()){
            try {
                FileInputStream fis = new FileInputStream(file1);
                // 创建 XmlPullParser 对象，并设置输入流
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(fis, "UTF-8");
                int eventType=parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.START_TAG){
                        if(parser.getName().equals("config")){
                            String key = parser.getAttributeValue(null,"state");
                            String value = parser.getAttributeValue(null,"pkg");
                            hashMap.put(value,Integer.valueOf(key.trim()));
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

    public String getConfig(String requestpkg,String cachePath,String filename){
        HashMap<String, Integer> configs = getConfigs(cachePath, filename);
        for (Map.Entry<String, Integer> entry : configs.entrySet()) {
            if(entry.getKey().equals(requestpkg)){
                return entry.getValue().toString();
            }
        }
        return null;
    }

    public String getCachePathOnXML(){
        return  "/data/local/tmp/easyManager";
    }

    public String getGrantUserConfigName(){
        return "grantuser.xml";
    }

    public String getCleanAPPConfigName(){
        return "cleanapp.xml";
    }


    public HashMap<String,Integer> getGrantUsers(){
        return getConfigs(getCachePathOnXML(),getGrantUserConfigName());
    }

    public HashMap<String,Integer> getCleanAPPs(){
        return getConfigs(getCachePathOnXML(),getCleanAPPConfigName());
    }

    public Integer getGrantUserState(String requestpkg) {
        String value = getConfig(requestpkg,getCachePathOnXML(),getGrantUserConfigName());
        try {
            Integer integer = Integer.valueOf(value.trim());
            return integer;
        }catch (Exception e){

        }
        return -1;
    }

    public Integer getCleanAPPConfigTime(String requestpkg) {
        String value = getConfig(requestpkg,getCachePathOnXML(),getCleanAPPConfigName());
        try {
            Integer integer = Integer.valueOf(value.trim());
            return integer;
        }catch (Exception e){

        }
        return -1;
    }

    public int changeGrantUserState(String requestpkg){
        String config = getConfig(requestpkg, getCachePathOnXML(), getGrantUserConfigName());
        if(config != null){
            changeConfig(requestpkg,config.equals("0")?1:0,getCachePathOnXML(),getGrantUserConfigName());
            return 0;
        }
        return -1;
    }

    public int changeCleanAPPConfigTime(String requestpkg,int time){
        String config = getConfig(requestpkg, getCachePathOnXML(), getCleanAPPConfigName());
        if(config != null){
            changeConfig(requestpkg,time,getCachePathOnXML(),getCleanAPPConfigName());
            return 0;
        }
        return -1;
    }

    public int requestGrantUserState(String requestpkg){
        if(getConfig(requestpkg,getCachePathOnXML(),getGrantUserConfigName()) == null){
            writeConfig(requestpkg,requestpkg.equals("com.easymanager")?0:1,getCachePathOnXML(),getGrantUserConfigName());
        }
        return getConfig(requestpkg,getCachePathOnXML(),getGrantUserConfigName()) != null ? 1:-1;
    }

    public int addCleanAPPConfigTime(String requestpkg,int time){
        if(getConfig(requestpkg,getCachePathOnXML(),getCleanAPPConfigName()) == null){
            writeConfig(requestpkg,time,getCachePathOnXML(),getCleanAPPConfigName());
        }
        return getConfig(requestpkg,getCachePathOnXML(),getCleanAPPConfigName()) != null ? 1:-1;
    }

    public void deleteCleanAPPConfig(){
        ft.deleteFile(getCachePathOnXML()+"/"+getCleanAPPConfigName());
    }




}

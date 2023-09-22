package com.easymanager.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTools {

    public StringTools(){}

    /**
     * <p>
     * 进行字符串正则提取
     */
    public String getByString(String src, String regex, String re_str) {
        StringBuilder tmp = new StringBuilder();
        Matcher m = Pattern.compile(regex).matcher(src);
        if (m.find()) {
            tmp.append(m.group().replaceAll(re_str, "") + "\n");
        }
        return tmp.toString();
    }

    /**
     * <p>
     * 进行字符串正则提取
     */
    public String getByAllString(String src, String regex, String re_str) {
        StringBuilder tmp = new StringBuilder();
        Matcher m = Pattern.compile(regex).matcher(src);
        while (m.find()) {
            tmp.append(m.group().replaceAll(re_str, "") + "\n");
        }
        return tmp.toString();
    }

    //获取文件结尾类型
    public String getPathByLastNameType(String filePath) {
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }

    //获取路径文件名称
    public String getPathByLastName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

}

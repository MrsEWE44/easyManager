package com.easymanager.entitys;

import java.io.Serializable;

public class MyActivityInfo implements Serializable {
    public MyActivityInfo(String name, boolean enable, boolean exported) {
        this.name = name;
        this.enable = enable;
        this.exported = exported;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isExported() {
        return exported;
    }

    public void setExported(boolean exported) {
        this.exported = exported;
    }

    @Override
    public String toString() {
        return "MyActivityInfo{" +
                "name='" + name + '\'' +
                ", enable=" + enable +
                ", exported=" + exported +
                '}';
    }

    public String name;
    public boolean enable;
    public boolean exported;
}

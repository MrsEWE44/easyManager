package com.easymanager.entitys;

import java.io.Serializable;

public class MyApplicationInfo implements Serializable {
    public MyApplicationInfo(int flags, boolean enabled, String sourceDir) {
        this.flags = flags;
        this.enabled = enabled;
        this.sourceDir = sourceDir;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    @Override
    public String toString() {
        return "MyApplicationInfo{" +
                "flags=" + flags +
                ", enabled=" + enabled +
                ", sourceDir='" + sourceDir + '\'' +
                '}';
    }

    public int flags = 0;
    public boolean enabled;
    public String sourceDir;




}

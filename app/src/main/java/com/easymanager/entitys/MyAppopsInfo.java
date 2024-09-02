package com.easymanager.entitys;

import java.io.Serializable;

public class MyAppopsInfo implements Serializable {
    public MyAppopsInfo(String pkgname, String OPS_PER_STR, String OPS_TO_PER_STR, String OPS_MODE_STR, int OPS_PER_CODE, int OPS_PER_MODE_CODE) {
        this.pkgname = pkgname;
        this.OPS_PER_STR = OPS_PER_STR;
        this.OPS_TO_PER_STR = OPS_TO_PER_STR;
        this.OPS_MODE_STR = OPS_MODE_STR;
        this.OPS_PER_CODE = OPS_PER_CODE;
        this.OPS_PER_MODE_CODE = OPS_PER_MODE_CODE;
    }

    public String getPkgname() {
        return pkgname;
    }

    public void setPkgname(String pkgname) {
        this.pkgname = pkgname;
    }

    public String getOPS_PER_STR() {
        return OPS_PER_STR;
    }

    public void setOPS_PER_STR(String OPS_PER_STR) {
        this.OPS_PER_STR = OPS_PER_STR;
    }

    public String getOPS_TO_PER_STR() {
        return OPS_TO_PER_STR;
    }

    public void setOPS_TO_PER_STR(String OPS_TO_PER_STR) {
        this.OPS_TO_PER_STR = OPS_TO_PER_STR;
    }

    public String getOPS_MODE_STR() {
        return OPS_MODE_STR;
    }

    public void setOPS_MODE_STR(String OPS_MODE_STR) {
        this.OPS_MODE_STR = OPS_MODE_STR;
    }

    public int getOPS_PER_CODE() {
        return OPS_PER_CODE;
    }

    public void setOPS_PER_CODE(int OPS_PER_CODE) {
        this.OPS_PER_CODE = OPS_PER_CODE;
    }

    public int getOPS_PER_MODE_CODE() {
        return OPS_PER_MODE_CODE;
    }

    public void setOPS_PER_MODE_CODE(int OPS_PER_MODE_CODE) {
        this.OPS_PER_MODE_CODE = OPS_PER_MODE_CODE;
    }

    @Override
    public String toString() {
        return "MyAppopsInfo{" +
                "pkgname='" + pkgname + '\'' +
                ", OPS_PER_STR='" + OPS_PER_STR + '\'' +
                ", OPS_TO_PER_STR='" + OPS_TO_PER_STR + '\'' +
                ", OPS_MODE_STR='" + OPS_MODE_STR + '\'' +
                ", OPS_PER_CODE=" + OPS_PER_CODE +
                ", OPS_PER_MODE_CODE=" + OPS_PER_MODE_CODE +
                '}';
    }

    private String pkgname,OPS_PER_STR,OPS_TO_PER_STR,OPS_MODE_STR;
    private int OPS_PER_CODE,OPS_PER_MODE_CODE;
}

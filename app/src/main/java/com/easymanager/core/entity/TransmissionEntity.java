package com.easymanager.core.entity;

import java.io.Serializable;

public class TransmissionEntity implements Serializable {
    public TransmissionEntity(String pkgname, String opmodestr, String requestpkg, int opsmode) {
        this.pkgname = pkgname;
        this.opmodestr = opmodestr;
        this.requestpkg = requestpkg;
        this.opsmode = opsmode;
    }

    public String getPkgname() {
        return pkgname;
    }

    public void setPkgname(String pkgname) {
        this.pkgname = pkgname;
    }

    public String getOpmodestr() {
        return opmodestr;
    }

    public void setOpmodestr(String opmodestr) {
        this.opmodestr = opmodestr;
    }

    public String getRequestpkg() {
        return requestpkg;
    }

    public void setRequestpkg(String requestpkg) {
        this.requestpkg = requestpkg;
    }

    public int getOpsmode() {
        return opsmode;
    }

    public void setOpsmode(int opsmode) {
        this.opsmode = opsmode;
    }

    @Override
    public String toString() {
        return "TransmissionEntity{" +
                "pkgname='" + pkgname + '\'' +
                ", opmodestr='" + opmodestr + '\'' +
                ", requestpkg='" + requestpkg + '\'' +
                ", opsmode=" + opsmode +
                '}';
    }

    private String pkgname;
    private String opmodestr;
    private String requestpkg;
    private int opsmode;

}

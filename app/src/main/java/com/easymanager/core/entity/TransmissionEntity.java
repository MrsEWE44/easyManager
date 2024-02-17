package com.easymanager.core.entity;

import java.io.Serializable;

public class TransmissionEntity implements Serializable {

    public TransmissionEntity(String pkgname, String opmodestr, String requestpkg, int opsmode, int uid) {
        this.pkgname = pkgname;
        this.opmodestr = opmodestr;
        this.requestpkg = requestpkg;
        this.opsmode = opsmode;
        this.uid = uid;
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

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "TransmissionEntity{" +
                "pkgname='" + pkgname + '\'' +
                ", opmodestr='" + opmodestr + '\'' +
                ", requestpkg='" + requestpkg + '\'' +
                ", opsmode=" + opsmode +
                ", uid=" + uid +
                '}';
    }

    private String pkgname;
    private String opmodestr;
    private String requestpkg;
    private int opsmode,uid;

}

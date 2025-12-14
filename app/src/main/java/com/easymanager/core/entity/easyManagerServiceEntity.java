package com.easymanager.core.entity;

import com.easymanager.core.utils.CMD;

import java.io.Serializable;

public class easyManagerServiceEntity implements Serializable {

    private  static final long serialVersionUID = 5459317133440313261L;


    public easyManagerServiceEntity(CMD cmd, Object object, boolean isDead, String errorMsg) {
        this.cmd = cmd;
        this.object = object;
        this.isDead = isDead;
        this.errorMsg = errorMsg;
    }

    public CMD getCmd() {
        return cmd;
    }

    public void setCmd(CMD cmd) {
        this.cmd = cmd;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "easyManagerServiceEntity{" +
                "cmd=" + cmd +
                ", object=" + object +
                ", isDead=" + isDead +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }

    private CMD cmd;
    private Object object;

    private boolean isDead;

    private  String errorMsg;


}

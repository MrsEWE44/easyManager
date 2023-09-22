package com.easymanager.core.entity;

import com.easymanager.core.utils.CMD;

import java.io.Serializable;

public class easyManagerServiceEntity implements Serializable {

    private  static final long serialVersionUID = 5459317133440313261L;

    public easyManagerServiceEntity(CMD cmd, Object object) {
        this.cmd = cmd;
        this.object = object;
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

    @Override
    public String toString() {
        return "adfbEntity{" +
                "cmd=" + (cmd==null?"null":cmd.toString()) +
                ", object=" + (object==null?"null":object.toString()) +
                '}';
    }

    private CMD cmd;
    private Object object;

}

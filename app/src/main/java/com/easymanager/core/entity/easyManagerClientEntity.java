package com.easymanager.core.entity;

import java.io.Serializable;

public class easyManagerClientEntity implements Serializable {


    public easyManagerClientEntity(String cmdstr, TransmissionEntity transmissionEntity, int easyManagerMode) {
        this.cmdstr = cmdstr;
        this.transmissionEntity = transmissionEntity;
        this.easyManagerMode = easyManagerMode;
    }

    public String getCmdstr() {
        return cmdstr;
    }

    public void setCmdstr(String cmdstr) {
        this.cmdstr = cmdstr;
    }

    public TransmissionEntity getTransmissionEntity() {
        return transmissionEntity;
    }

    public void setTransmissionEntity(TransmissionEntity transmissionEntity) {
        this.transmissionEntity = transmissionEntity;
    }

    public int getEasyManagerMode() {
        return easyManagerMode;
    }

    public void setEasyManagerMode(int easyManagerMode) {
        this.easyManagerMode = easyManagerMode;
    }

    @Override
    public String toString() {
        return "easyManagerClientEntity{" +
                "cmdstr='" + cmdstr + '\'' +
                ", transmissionEntity=" + transmissionEntity.toString() +
                ", easyManagerMode=" + easyManagerMode +
                '}';
    }

    private String cmdstr ;
    private TransmissionEntity transmissionEntity;
    private int easyManagerMode;


}

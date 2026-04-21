package com.easymanager.entitys;

import java.io.Serializable;

public class MyAccountInfo implements Serializable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MyAccountInfo(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "MyAccountInfo{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    private String name ,type;

}

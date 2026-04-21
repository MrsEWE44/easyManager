package com.easymanager.entitys;

import java.io.Serializable;
import java.util.ArrayList;

public class LightBreezeConfig implements Serializable {
    private ArrayList<String> disablePkgs = new ArrayList<>();
    private ArrayList<String> uninstallPkgs = new ArrayList<>();

    public ArrayList<String> getDisablePkgs() {
        return disablePkgs;
    }

    public void setDisablePkgs(ArrayList<String> disablePkgs) {
        this.disablePkgs = disablePkgs;
    }

    public ArrayList<String> getUninstallPkgs() {
        return uninstallPkgs;
    }

    public void setUninstallPkgs(ArrayList<String> uninstallPkgs) {
        this.uninstallPkgs = uninstallPkgs;
    }
}

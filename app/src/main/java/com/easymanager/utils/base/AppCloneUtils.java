package com.easymanager.utils.base;

import com.easymanager.core.utils.CMD;
import com.easymanager.utils.TextUtils;
import com.easymanager.utils.dialog.PackageDialog;
import com.easymanager.utils.dialog.SearchDialog;
import com.easymanager.utils.dialog.UserDialog;
import com.easymanager.utils.easyManagerUtils;

public class AppCloneUtils {

    public UserDialog getUd() {
        return ud;
    }

    public void setUd(UserDialog ud) {
        this.ud = ud;
    }

    public PackageDialog getPd() {
        return pd;
    }

    public void setPd(PackageDialog pd) {
        this.pd = pd;
    }

    public SearchDialog getSd() {
        return sd;
    }

    public void setSd(SearchDialog sd) {
        this.sd = sd;
    }

    private UserDialog ud  =new UserDialog();
    private PackageDialog pd = new PackageDialog();
    private SearchDialog sd = new SearchDialog();

    public int getCurrentUserID(){
        return getEasyManagerUtils().getCurrentUserID();
    }

    public easyManagerUtils getEasyManagerUtils(){
        return ud.easyMUtils;
    }

    public CMD runCMD(String cmdstr){
        return getEasyManagerUtils().runCMD(cmdstr);
    }

    public TextUtils getTU(){
        return ud.tu;
    }

}

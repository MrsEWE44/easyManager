package com.easymanager.utils.dialog;

import android.content.Context;

import com.easymanager.R;
import com.easymanager.enums.AppManagerEnum;
import com.easymanager.utils.base.DialogBaseUtils;

public class HelpDialogUtils extends DialogBaseUtils {

    public final static int MAIN_HELP=0;
    public final static int APP_MANAGE_HELP=1;
    public final static int APP_INFO_HELP=2;

    public void showHelp(Context  context, int mode1 , int mode2){
        if(mode1 == MAIN_HELP){
            showInfoMsg(context,tu.getLanguageString(context, R.string.show_help_title),tu.getLanguageString(context, R.string.show_help_getMainHelp));
        }

        if(mode1 == APP_MANAGE_HELP){
            String msg = "msg";
            switch (mode2){
                case AppManagerEnum.APP_PERMISSION:
                    msg = tu.getLanguageString(context,R.string.show_help_getAppPermissonHELP);
                    break;
                case AppManagerEnum.APP_DISABLE_COMPENT:
                    msg = tu.getLanguageString(context,R.string.show_help_getAppDsiableCompentHELP);
                    break;
                case AppManagerEnum.APP_FIREWALL:
                    msg = tu.getLanguageString(context,R.string.show_help_getAppFirewallHELP);
                    break;
                case AppManagerEnum.APP_INSTALL_LOCAL_FILE:
                    msg = tu.getLanguageString(context,R.string.show_help_getAppInstallLocalFileHELP);
                    break;
                case AppManagerEnum.APP_DUMP:
                    msg = tu.getLanguageString(context,R.string.show_help_getAppDumpHELP);
                    break;
                case AppManagerEnum.APP_UNINSTALL:
                    msg = tu.getLanguageString(context,R.string.show_help_getUninstallHELP);
                    break;
                case AppManagerEnum.APP_CLEAN_PROCESS:
                    msg = tu.getLanguageString(context,R.string.show_help_getAppClenProcessHELP);
                    break;
                case AppManagerEnum.APP_BACKUP:
                    msg = tu.getLanguageString(context,R.string.show_help_getAppBackupHELP);
                    break;
                case AppManagerEnum.APP_RESTORY:
                    msg = tu.getLanguageString(context,R.string.show_help_getAppRestoryHELP);
                    break;
                case AppManagerEnum.MOUNT_LOCAL_IMG:
                    msg = tu.getLanguageString(context,R.string.show_help_getMountLocalImgHELP);
                    break;
                case AppManagerEnum.CREATE_IMG:
                    msg = tu.getLanguageString(context,R.string.show_help_getCreateImgHELP);
                    break;
                case AppManagerEnum.SET_NTP:
                    msg = tu.getLanguageString(context,R.string.show_help_getSetNTPHELP);
                    break;
                case AppManagerEnum.SET_RATE:
                    msg = tu.getLanguageString(context,R.string.show_help_getSetRateHELP);
                    break;
                case AppManagerEnum.DEL_X:
                    msg = tu.getLanguageString(context,R.string.show_help_getDelXHELP);
                    break;
                case AppManagerEnum.FILE_SHARED:
                    msg = tu.getLanguageString(context,R.string.show_help_getFileSharedHELP);
                    break;
                case AppManagerEnum.APP_CLONE:
                    msg = tu.getLanguageString(context,R.string.show_help_getAppCloneHELP);
                    break;
                case AppManagerEnum.APP_CLONE_MANAGE:
                    msg = tu.getLanguageString(context,R.string.show_help_getAppCloneManagerHELP);
                    break;
                case AppManagerEnum.APP_CLONE_REMOVE:
                    msg = tu.getLanguageString(context,R.string.show_help_getAppCloneRemoveHELP);
                    break;
            }
            showInfoMsg(context,tu.getLanguageString(context, R.string.show_help_title),msg);
        }

        if(mode1 == APP_INFO_HELP){
            showInfoMsg(context,tu.getLanguageString(context, R.string.show_help_title),tu.getLanguageString(context,R.string.show_help_getAppInfoHELP));
        }

    }

}

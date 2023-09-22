package com.easymanager.utils;

import android.content.Context;

import com.easymanager.enums.AppManagerEnum;
import com.easymanager.utils.base.DialogBaseUtils;

public class HelpDialogUtils extends DialogBaseUtils {

    public final static int MAIN_HELP=0;
    public final static int APP_MANAGE_HELP=1;
    public final static int APP_INFO_HELP=2;


    public void showHelp(Context  context, int mode1 , int mode2){
        if(mode1 == MAIN_HELP){
            showInfoMsg(context,"帮助说明",getMainHelp());
        }

        if(mode1 == APP_MANAGE_HELP){
            String msg = "msg";
            switch (mode2){
                case AppManagerEnum.APP_PERMISSION:
                    msg = getAppPermissonHELP();
                    break;
                case AppManagerEnum.APP_DISABLE_COMPENT:
                    msg = getAppDsiableCompentHELP();
                    break;
                case AppManagerEnum.APP_FIREWALL:
                    msg = getAppFirewallHELP();
                    break;
                case AppManagerEnum.APP_INSTALL_LOCAL_FILE:
                    msg = getAppInstallLocalFileHELP();
                    break;
                case AppManagerEnum.APP_DUMP:
                    msg = getAppDumpHELP();
                    break;
                case AppManagerEnum.APP_UNINSTALL:
                    msg = getUninstallHELP();
                    break;
                case AppManagerEnum.APP_CLEAN_PROCESS:
                    msg = getAppClenProcessHELP();
                    break;
                case AppManagerEnum.APP_BACKUP:
                    msg = getAppBackupHELP();
                    break;
                case AppManagerEnum.APP_RESTORY:
                    msg = getAppRestoryHELP();
                    break;
                case AppManagerEnum.MOUNT_LOCAL_IMG:
                    msg = getMountLocalImgHELP();
                    break;
                case AppManagerEnum.CREATE_IMG:
                    msg = getCreateImgHELP();
                    break;
                case AppManagerEnum.SET_NTP:
                    msg = getSetNTPHELP();
                    break;
                case AppManagerEnum.SET_RATE:
                    msg = getSetRateHELP();
                    break;
                case AppManagerEnum.DEL_X:
                    msg = getDelXHELP();
                    break;
                case AppManagerEnum.FILE_SHARED:
                    msg = getFileSharedHELP();
                    break;
            }
            showInfoMsg(context,"帮助说明",msg);
        }

        if(mode1 == APP_INFO_HELP){
            showInfoMsg(context,"帮助说明",getAppInfoHELP());
        }

    }

    public String getAppPermissonHELP(){
        return "该界面主要是用来管控应用权限的,因为自安卓5以后,应用的权限都由appops来管控,所以,这里做的一切操作,都是在IAppOpsService服务实现.\r\n\r\n" +
                "1.右上角的菜单栏,可以获取到当前用户对应状态下的应用.\r\n" +
                "2.左边显示权限名称,虽然都是中文,但是都是一个集合,可以一次操作多个权限.\r\n" +
                "3.右边则是你想要配置的权限情况,可以选择默认、拒绝、允许等规则.看系统情况,不一定是即时生效.\r\n" +
                "4.左下角有个选项,你可以批量选择或者单选应用来进行权限管控.\r\n" +
                "5.右下角是生效用的按钮,你点击它就可以让勾选或者未勾选中的应用生效你选择的规则.\r\n" +
                "6.当你点击应用的时候,它会跳转到应用详情界面,这部分要root模式才能使用.\r\n" +
                "7.上面菜单旁边是一个搜索按钮,可以搜索当前检索出来的应用列表,按回车就会把搜索结果同步到检索列表.\r\n" +
                "8.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }


    public String getAppInfoHELP(){
        return "该界面主要是用来管控应用服务、活动项、广播接收器组件的,这里做的一切操作,都是在IPackageManager服务实现.需要ROOT模式.\r\n\r\n" +
                "1.右上角的菜单栏,可以获取到当前应用对应的组件信息.\r\n" +
                "2.左下角有个选项,你可以批量选择或者单选应用来进行权限管控.\r\n" +
                "3.右下角是生效用的按钮,你点击它就可以让勾选或者未勾选中的应用生效你选择的规则.\r\n" +
                "4.上面菜单旁边是一个搜索按钮,可以搜索当前检索出来的组件列表,按回车就会把搜索结果同步到检索列表.\r\n" +
                "5.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }

    public String getAppDsiableCompentHELP(){
        return "该界面主要是用来禁用或者冻结应用的,这里做的一切操作,都是在IPackageManager服务实现.\r\n\r\n" +
                "1.右上角的菜单栏,可以获取到当前用户对应状态下的应用.\r\n" +
                "2.右边有个选项,可以选择启用、禁用应用.看系统情况,不一定是即时生效.\r\n" +
                "3.左下角有个选项,你可以批量选择或者单选应用来进行权限管控.\r\n" +
                "4.右下角是生效用的按钮,你点击它就可以让勾选或者未勾选中的应用生效你选择的规则.\r\n" +
                "5.当你点击应用的时候,它会跳转到应用详情界面,这部分要root模式才能使用.\r\n" +
                "6.上面菜单旁边是一个搜索按钮,可以搜索当前检索出来的应用列表,按回车就会把搜索结果同步到检索列表.\r\n" +
                "7.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }

    public String getAppFirewallHELP(){
        return "该界面主要是用来管理应用联网的,这里做的一切操作,都是调用命令实现.需要ROOT模式\r\n\r\n" +
                "1.右上角的菜单栏,可以获取到当前用户对应状态下的应用.\r\n" +
                "2.右边有个选项,可以选择允许、禁用应用联网.看系统情况,不一定是即时生效.\r\n" +
                "3.左下角有个选项,你可以批量选择或者单选应用来进行权限管控.\r\n" +
                "4.右下角是生效用的按钮,你点击它就可以让勾选或者未勾选中的应用生效你选择的规则.\r\n" +
                "5.当你点击应用的时候,它会跳转到应用详情界面,这部分要root模式才能使用.\r\n" +
                "6.上面菜单旁边是一个搜索按钮,可以搜索当前检索出来的应用列表,按回车就会把搜索结果同步到检索列表.\r\n" +
                "7.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }

    public String getAppInstallLocalFileHELP(){
        return "该界面主要是用来安装本地apk文件的,这里做的一切操作,都是在IPackageManager服务实现.\r\n\r\n" +
                "1.右上角的菜单栏,可以获取到当前用户对应状态下的应用.\r\n" +
                "2.右边有个选项,可以选择本地安装包文件,自己手动选择安装,也可以选择本地文件夹,自动批量加载,进行安装.看系统情况,不一定是即时生效.\r\n" +
                "3.左下角有个选项,你可以批量选择或者单选应用来进行操作.\r\n" +
                "4.右下角是生效用的按钮,你点击它就可以让勾选或者未勾选中的应用生效你选择的规则.\r\n" +
                "5.当你点击应用的时候,它会跳转到应用详情界面,这部分要root模式才能使用.\r\n" +
                "6.上面菜单旁边是一个搜索按钮,可以搜索当前检索出来的应用列表,按回车就会把搜索结果同步到检索列表.\r\n" +
                "7.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }

    public String getAppDumpHELP(){
        return "该界面主要是用来提取本地应用安装文件的,这里做的一切操作,都是在shell服务实现.\r\n\r\n" +
                "1.右上角的菜单栏,可以获取到当前用户对应状态下的应用.\r\n" +
                "2.右边有个选项,可以选择以包名、应用名字、当前时间来保存提取出来的文件.提取出来的文件默认放在内部存储下的/Android/data/com.easymanager/cache/dump文件夹里面.看系统情况,不一定是即时生效.\r\n" +
                "3.左下角有个选项,你可以批量选择或者单选应用来进行操作.\r\n" +
                "4.右下角是生效用的按钮,你点击它就可以让勾选或者未勾选中的应用生效你选择的规则.\r\n" +
                "5.当你点击应用的时候,它会跳转到应用详情界面,这部分要root模式才能使用.\r\n" +
                "6.上面菜单旁边是一个搜索按钮,可以搜索当前检索出来的应用列表,按回车就会把搜索结果同步到检索列表.\r\n" +
                "7.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }

    public String getUninstallHELP(){
        return "该界面主要是用来卸载本地应用的,这里做的一切操作,都是在IPackageManager服务实现.\r\n\r\n" +
                "1.右上角的菜单栏,可以获取到当前用户对应状态下的应用.\r\n" +
                "2.左下角有个选项,你可以批量选择或者单选应用来进行操作.\r\n" +
                "3.右下角是生效用的按钮,你点击它就可以让勾选或者未勾选中的应用生效你选择的规则.\r\n" +
                "4.当你点击应用的时候,它会跳转到应用详情界面,这部分要root模式才能使用.\r\n" +
                "5.上面菜单旁边是一个搜索按钮,可以搜索当前检索出来的应用列表,按回车就会把搜索结果同步到检索列表.\r\n" +
                "6.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }

    public String getAppClenProcessHELP(){
        return "该界面主要是用来终止后台应用的,这里做的一切操作,都是在IActivityManager服务实现.\r\n\r\n" +
                "1.右上角的菜单栏,可以获取到当前用户对应状态下的应用.\r\n" +
                "2.左下角有个选项,你可以批量选择或者单选应用来进行操作.\r\n" +
                "3.右下角是生效用的按钮,你点击它就可以让勾选或者未勾选中的应用生效你选择的规则.\r\n" +
                "4.当你点击应用的时候,它会跳转到应用详情界面,这部分要root模式才能使用.\r\n" +
                "5.上面菜单旁边是一个搜索按钮,可以搜索当前检索出来的应用列表,按回车就会把搜索结果同步到检索列表.\r\n" +
                "6.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }

    public String getAppBackupHELP(){
        return "该界面主要是用来备份应用的,这里做的一切操作,都是在shell服务实现.\r\n\r\n" +
                "1.右上角的菜单栏,可以获取到当前用户对应状态下的应用.\r\n" +
                "2.左边有三个选项,数据+应用、仅数据、仅应用,这是备份方式.\r\n" +
                "3.右边有三个选项,tar.xz/tar.gz/tar.bz,这个是压缩模式,备份的时候,会按照选中的压缩模式进行备份.\r\n" +
                "4.左下角有个选项,你可以批量选择或者单选应用来进行操作.\r\n" +
                "5.右下角是生效用的按钮,你点击它就可以让勾选或者未勾选中的应用生效你选择的规则.\r\n" +
                "6.当你点击应用的时候,它会跳转到应用详情界面,这部分要root模式才能使用.\r\n" +
                "7.上面菜单旁边是一个搜索按钮,可以搜索当前检索出来的应用列表,按回车就会把搜索结果同步到检索列表.\r\n" +
                "8.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }

    public String getAppRestoryHELP(){
        return "该界面主要是用来恢复已备份应用的,这里做的一切操作,都是在shell服务实现.\r\n\r\n" +
                "1.右上角的菜单栏,可以获取到之前备份的应用压缩包.\r\n" +
                "2.左边有三个选项,数据+应用、仅数据、仅应用,这是恢复方式.\r\n" +
                "3.右边有三个选项,tar.xz/tar.gz/tar.bz,这个是压缩模式,恢复的时候,会按照选中的压缩模式进行恢复,如果没有该压缩类型的文件,默认会跳过.\r\n" +
                "4.左下角有个选项,你可以批量选择或者单选应用来进行操作.\r\n" +
                "5.右下角是生效用的按钮,你点击它就可以让勾选或者未勾选中的应用生效你选择的规则.\r\n" +
                "6.当你点击应用的时候,它会跳转到应用详情界面,这部分要root模式才能使用.\r\n" +
                "7.上面菜单旁边是一个搜索按钮,可以搜索当前检索出来的应用列表,按回车就会把搜索结果同步到检索列表.\r\n" +
                "8.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }

    public String getMountLocalImgHELP(){
        return "该界面主要是用来挂载本地镜像文件到电脑上的,这里做的一切操作,都是在shell服务实现.需要root模式.\r\n\r\n" +
                "1.点击扫描本地镜像按钮,可以获取到当前设备下的镜像文件信息.\r\n" +
                "2.左下角有个选项,你可以选择自动模式或者模式1、2、3来进行挂载操作.\r\n" +
                "3.右下角是挂载用的按钮,你点击它就会开始挂载本地镜像到电脑上使用.只会挂载第一个被勾选中的文件.\r\n" +
                "4.上面菜单旁边是一个搜索按钮,可以搜索当前检索出来的文件列表,按回车就会把搜索结果同步到检索列表.\r\n" +
                "5.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }

    public String getCreateImgHELP(){
        return "该界面主要是用来创建本地镜像文件的,这里做的一切操作,都是在shell服务实现.需要root模式.\r\n\r\n" +
                "1.左边第一个编辑框是用来输入镜像文件名字的.\r\n" +
                "2.右边是选择生成的文件后缀名类型.\r\n" +
                "3.左下角需要你输入文件大小.尽量填写整数.\r\n" +
                "4.右下角有文件大小单位,你需要选择一个,以生成对应大小的镜像文件.\r\n" +
                "5.点击开始创建按钮,则会开始创建镜像文件.创建好的文件会在内部存储根路径下.\r\n" +
                "6.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }

    public String getSetNTPHELP(){
        return "该界面主要是用来配置本地NTP服务器地址的,这里做的一切操作,都是在shell服务实现.\r\n\r\n" +
                "1.左边第一个编辑框是用来输入自定义ntp服务器地址的.\r\n" +
                "2.右边是选择自带提供的ntp服务器地址.\r\n" +
                "3.点击按钮,则会生效更改,重启设备即可生效.\r\n" +
                "4.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }

    public String getSetRateHELP(){
        return "该界面主要是用来配置设备刷新率数值的,这里做的一切操作,都是在shell服务实现.\r\n\r\n" +
                "1.左边第二个编辑框是用来输入自定义刷新率数值的.\r\n" +
                "2.右边是选择自带提供的刷新率数值.\r\n" +
                "3.点击按钮,则会生效更改.\r\n" +
                "4.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }

    public String getDelXHELP(){
        return "该界面主要是用来去除WiFi或者移动数据上网时,一直出现一个X标记问题的,这里做的一切操作,都是在shell服务实现.\r\n\r\n" +
                "1.点击按钮,则会生效更改,重启设备以生效.\r\n" +
                "2.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }


    public String getFileSharedHELP(){
        return "该界面主要是用来共享设备文件的.\r\n\r\n" +
                "1.点击添加文件按钮,则会列出本地文件,选择需要共享的文件或者文件夹,点击确定即可添加到共享列表上.\r\n" +
                "2.点击添加共享应用按钮,则会列出本地已安装的应用,选择需要共享的应用,点击确定即可添加到共享列表上.\r\n" +
                "3.右上角有个数字编辑框,如果默认端口被占用,无法开启共享服务,你应该填入一个其它数值,以避免端口被占用的问题.\r\n" +
                "4.点击开始共享按钮,即可开始共享列表里的文件.\r\n" +
                "5.只需要在浏览器里面输入: \"共享地址:端口\", 即可访问共享的内容.\r\n" +
                "6.点击最左上角的图标,会回退到上一级菜单.\r\n\r\n";
    }

    public String getMainHelp(){
        return "easyManager是一个安卓系统管理工具，具备轻量化、功能核心化、简洁化的优点，同时也允许与第三方应用授权共享api接口.\r\n" +
                "easyManager只做该做的事情，不会一直驻留在后台服务当中，仅保留核心服务运行.\r\n\r\n";
    }

}

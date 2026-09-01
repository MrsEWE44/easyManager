<h1 align="center">轻风 | Light Breeze</h1>

<p align="center">
一款轻量化、核心化、简洁易用的 Android 系统工具箱
</p>

<p align="center">
🇺🇸 <a href="./README_EN.md">English</a> |
🇨🇳 <a href="./README.md">简体中文</a>
</p>

<p align="center">

<a href="https://github.com/MrsEWE44/easyManager/releases">
<img alt="GitHub Release" src="https://img.shields.io/github/v/release/MrsEWE44/easyManager">
</a>

<img alt="GitHub Stars" src="https://img.shields.io/github/stars/MrsEWE44/easyManager">

<img alt="GitHub Forks" src="https://img.shields.io/github/forks/MrsEWE44/easyManager">

<img alt="GitHub Downloads" src="https://img.shields.io/github/downloads/MrsEWE44/easyManager/total">

<img alt="GitHub Watchers" src="https://img.shields.io/github/watchers/MrsEWE44/easyManager">

</p>


# 简介

轻风（Light Breeze）是一款面向 Android 高级用户的系统管理工具。

它专注于中国 OEM 定制系统环境，提供 **ADB、ROOT、设备管理员** 三种运行模式，让用户能够更加自由、安全、高效地管理自己的设备。

轻风不会主动修改系统核心文件，也不会强制获取用户权限。

所有高级功能均基于用户主动授权，在权限允许范围内完成对应操作。

正如它的名字：

> 轻风轻拂而来，悄然离去。  
> 不强行改变，不越权干预。  
> 只做好自己的事情，保持克制，保持纯粹。


---

# 功能特点

轻风目前集成：

- 应用批量权限管理
- 应用冻结 / 解冻 / 禁用
- 批量卸载与静默安装
- 应用分身管理
- 后台进程清理
- 网络访问控制
- 应用组件管理
- 应用备份与恢复
- 文件压缩与恢复
- 局域网文件共享
- ADB / Root Shell 命令执行


同时针对部分 Android 系统提供：

- 修复类原生系统异常信号图标
- 自定义 NTP 时间服务器
- 国内时间同步优化
- 系统刷新率调节
- 系统限制调整


轻风将复杂的系统操作封装为简单的一键功能。

用户只需要选择对应规则，即可完成相关操作。


---

# 运行模式

## ADB 模式

通过 ADB Shell 权限运行。

无需 Root，即可实现部分高级系统管理能力。

支持：

- 应用管理
- 权限调整
- 系统命令执行
- 部分 Hidden API 调用


---

## ROOT 模式

拥有 Root 权限后，可开启更多高级功能。

支持：

- 系统级备份恢复
- 网络管控
- 应用组件控制
- 分身限制调整
- 系统启动环境制作
- 更深层系统管理


---

## 设备管理员模式

通过 Android Device Admin 能力运行。

无需 Root。

支持：

- 应用卸载
- 应用安装管理
- 应用冻结 / 解冻
- 应用图标隐藏 / 显示


---

# 使用技术

轻风基于 Android 原生技术开发。

主要涉及：

## Android 开发

- Java
- Android SDK
- Android Framework API
- Binder IPC
- AIDL
- PackageManager
- ActivityManager
- UserManager
- AppOpsManager
- DevicePolicyManager


## 系统能力

- Hidden API 调用
- SystemService 调用
- Framework 内部接口调用
- Shell Command 执行
- ADB Shell
- Root Shell


## 文件处理

- Apache Commons Compress
- XZ 压缩算法
- 文件备份恢复
- 压缩文件处理


## 兼容技术

- Hidden API bypass
- Android 多版本适配
- OEM ROM 兼容优化


支持 Android 4.0+ 系统环境。


---

# 项目架构

轻风采用前后台分离架构，通过独立后台服务处理系统级操作。

```text
                         用户操作

                            │

                            ▼

              ┌────────────────────────┐
              │     轻风主程序 App      │
              │ UI / 功能配置 / 请求管理 │
              └───────────┬────────────┘
                          │
                          │ Binder IPC
                          │ AIDL 接口通信
                          ▼

              ┌────────────────────────┐
              │    轻风核心服务         │
              │   LightBreeze Service  │
              │                        │
              │  权限管理 / 任务调度     │
              │  系统能力调用            │
              └───────────┬────────────┘
                          │


        ┌─────────────────┼─────────────────┐
        │                 │                 │


        ▼                 ▼                 ▼


 ┌──────────┐      ┌──────────┐      ┌────────────┐
 │ ADB模式  │      │ ROOT模式 │      │设备管理员模式│
 │ ADB      │      │ Root     │      │Device Admin│
 └────┬─────┘      └────┬─────┘      └─────┬──────┘


      └─────────────────┼──────────────────┘

                        │

                        ▼


        ┌─────────────────────────────────┐
        │        Android Framework        │
        │                                 │
        │ Binder IPC                     │
        │ AIDL Interface                 │
        │ Hidden API                     │
        │                                 │
        │ IPackageManager                │
        │ IActivityManager               │
        │ IUserManager                   │
        │ IAppOpsService                 │
        └───────────────┬─────────────────┘


                        │

                        ▼


        ┌─────────────────────────────────┐
        │            系统能力             │
        │                                 │
        │ 应用管理                        │
        │ 权限控制                        │
        │ 用户与分身管理                  │
        │ 网络控制                        │
        │ 文件处理                        │
        │ 系统设置调整                    │
        └─────────────────────────────────┘
```

设计特点：

- 前台 UI 与核心执行逻辑分离
- 高权限操作运行于独立服务模块
- 通过 Binder IPC / AIDL 进行模块通信
- 根据 ADB、ROOT、设备管理员权限选择执行能力
- 提升稳定性与可维护性


---

# 工作原理

轻风参考了多个优秀开源项目：

- Shizuku
- AppOpsX
- Hail
- Dhizuku
- InstallerX


运行流程：

1. 用户选择运行模式
2. 用户完成权限授权
3. 后台服务启动
4. 前台发送操作请求
5. 后台调用对应系统能力完成任务


---

# 轻风可以做什么？

## 无 Root 卸载系统应用

通过设备管理员模式，可以卸载部分普通 ADB Shell 无法处理的系统应用。

支持：

- VIVO
- iQOO
- OPPO
- OnePlus
- realme
- HONOR
- Samsung


---

## 应用传感器权限管理

Android 10 以下系统：

- 管理应用传感器权限
- 禁止部分应用访问传感器

减少摇一摇广告等行为带来的干扰。


---

## 手机变身系统启动设备

Root 环境下：

- 创建启动镜像
- 制作启动盘
- 辅助安装 Windows
- 辅助安装 Linux


---

## 应用分身管理

Root 环境下：

- 调整系统分身限制
- 创建更多独立用户环境
- 管理应用分身


---

## 局域网文件共享

通过 WiFi：

- 共享指定文件夹
- 局域网访问文件
- 快速传输数据


---

# 软件截图

<p align="center">

<img src="images/1.png" width="250">
<img src="images/2.png" width="250">
<img src="images/3.png" width="250">

</p>

<p align="center">

<img src="images/4.png" width="250">
<img src="images/5.png" width="250">
<img src="images/6.png" width="250">

</p>


---

# 更新日志
从V1.2.8b版本开始，该项目分成两个版本，分开维护和更新，分别是 [轻风免root版本](https://github.com/MrsEWE44/easyManager/tree/md5) 和 [轻风完整版](https://github.com/MrsEWE44/easyManager/tree/master)

## V1.3.5a

1.优化一键自动授权功能。

2.优化获取应用列表功能。

3.修改版本号为1.3.5a.



---

# 开源致谢

轻风参考以下优秀开源项目：

- [Shizuku](https://github.com/rikkaapps/shizuku)
- [AppOpsX](https://github.com/8enet/AppOpsX)
- [Hail](https://github.com/aistra0528/Hail)
- [Dhizuku](https://github.com/iamr0s/Dhizuku)
- [InstallerX](https://github.com/wxxsfxyzm/InstallerX-Revived)


感谢这些项目为 Android 高级权限管理领域提供的探索与实践。


---

# 注意事项

轻风属于高级系统工具。

实际功能可能受到以下因素影响：

- Android 系统版本
- OEM 定制策略
- Root 环境
- 系统安全限制


不同设备支持情况可能存在差异。

使用高级功能前，请确认了解相关操作影响。


---

# 支持作者

如果轻风帮助到了你，欢迎支持项目开发。


<p align="center">

<img src="app/src/main/assets/wechatqr.jpg" width="200">

<img src="app/src/main/assets/aliqr.jpg" width="200">

</p>

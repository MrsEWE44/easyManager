<h1 align="center">轻风免ROOT版 | Light Breeze No ROOT</h1>

<p align="center">
一款轻量化、核心化、专注于免 ROOT 环境的 Android 系统工具箱
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

轻风免ROOT版（Light Breeze No ROOT）是专为无需 Root 权限的用户设计的系统管理工具。

它专注于中国 OEM 定制系统环境，通过 **Shizuku** 与 **Dhizuku** 双模式运行，在不破坏系统完整性的前提下，为用户提供高效、安全、克制的系统管理能力。

正如它的名字：

> 轻风轻拂而来，悄然离去。  
> 不强行改变，不越权干预。  
> 只做好自己的事情，保持克制，保持纯粹。


---

# 功能特点

轻风免特权版目前集成：

- **系统应用管理**：强力卸载/禁用预装应用，支持卸载后恢复。
- **传感器权限管理**：精准控制单个应用的传感器权限，减缓“摇一摇”广告干扰。
- **局域网文件共享**：通过 WiFi 快速共享文件夹，实现多端数据传输。
- **应用自动化**：支持一键静默安装、批量权限调整、后台进程清理。
- **应用分身管理**：深度管理系统分身与多用户环境。
- **网络访问控制**：在免 Root 环境下实现基础的网络管控功能。
- **系统工具集**：包含应用组件管理、NTP 时间同步优化等实用功能。


---

# 运行模式

## Shizuku 模式

通过 Shizuku 提供的 ADB Shell 权限运行。

无需 Root，即可实现大部分系统级管理功能。

支持：
- 基础权限调整
- 应用冻结 / 解冻
- 系统命令执行
- 传感器权限管控（特定版本）


---

## Dhizuku 模式

通过 Android Device Owner（设备管理员）权限运行。

提供比普通 ADB 更高级的系统控制能力，无需 Root 即可深度管理系统软件。

支持：
- 强力卸载 OEM 预装应用
- 静默安装与卸载
- 跨用户应用管理
- 更深层级的系统 API 调用


---

# 使用技术

轻风基于现代 Android 技术栈开发，确保在免 Root 环境下的高性能与高稳定性。

## 核心框架
- **Java 21**: 使用现代 Java 特性进行逻辑开发。
- **Android SDK 37**: 紧跟最新 Android 系统版本适配。
- **Shizuku / Dhizuku API**: 核心跨进程通信与权限调用方案。
- **Hidden API Bypass**: 突破 Android 系统隐藏接口限制。

## 系统交互
- **Binder IPC / AIDL**: 前后台分离架构，通过高效的进程间通信处理任务。
- **Framework API**: 深度调用 PackageManager, UserManager, AppOpsManager 等核心服务。
- **Shell Command**: 在受控环境下执行优化的 Shell 指令。

## 界面与交互
- **Material Design 3**: 采用 Google 最新的设计规范，提供简洁流畅的 UI 体验。


---

# 项目架构

轻风采用前后台分离架构，通过独立服务模块处理高权限操作。

```text
                         用户操作界面 (App)
                            │
                            ▼
              ┌────────────────────────┐
              │     轻风控制中心        │
              │ UI / 任务调度 / 请求管理 │
              └───────────┬────────────┘
                          │
                   Binder / AIDL 通信
                          │
          ┌───────────────┴───────────────┐
          ▼                               ▼
    ┌────────────┐                  ┌────────────┐
    │  Shizuku   │                  │  Dhizuku   │
    │ (ADB 权限)  │                  │(Admin 权限) │
    └─────┬──────┘                  └─────┬──────┘
          │                               │
          └───────────────┬───────────────┘
                          │
                          ▼
        ┌─────────────────────────────────┐
        │        Android Framework        │
        │                                 │
        │ PackageManager / ActivityManager│
        │ AppOpsService  / UserManager    │
        └───────────────┬─────────────────┘
                        │
                        ▼
        ┌─────────────────────────────────┐
        │            底层功能实现          │
        │                                 │
        │ [卸载/恢复] [传感器控制] [文件共享]  │
        └─────────────────────────────────┘
```


---

# 轻风免特权版能做什么？

## 1. 强力卸载系统应用
在 Dhizuku 模式下，可以彻底卸载部分 ADB 无法处理的系统预装软件。深度适配 VIVO, iQOO, OPPO, OnePlus, realme, HONOR, Samsung 等机型。

## 2. 治理“摇一摇”广告
支持在 Android 10 及以下系统（以及部分高版本受控模式）中管理传感器权限，从根源上减少误触发广告的行为。

## 3. 极简文件共享
一键开启局域网共享服务器，无需数据线即可在电脑或其他移动设备上访问手机内的文件。

## 4. 应用分身与多用户管理
打破系统限制，管理和创建独立的用户空间，实现应用的多开与隔离。


---

# 软件截图

<p align="center">
<img src="images/3.png" width="250">
<img src="images/4.png" width="250">
<img src="images/1.png" width="250">
</p>

<p align="center">
<img src="images/2.png" width="250">
<img src="images/5.png" width="250">
<img src="images/6.png" width="250">
</p>


---

# 更新日志

> [!IMPORTANT]
> 从 V1.2.8b 版本开始，项目分为 [免 ROOT 版本](https://github.com/MrsEWE44/easyManager/tree/md5) 和 [完整版(Root)](https://github.com/MrsEWE44/easyManager/tree/master) 分开维护。

## V2.0.5

1.添加Dhizuku环境检测跟激活的功能。

2.优化帮助信息。

3.完善获取本地应用的功能。

4.提升target为37，支持安卓17系统。

5.修改版本号为2.0.5.


---

# 开源致谢

本项目的开发参考并借鉴了以下优秀的开源项目，在此表示诚挚的感谢：

- [Shizuku](https://github.com/rikkaapps/shizuku)
- [Dhizuku](https://github.com/iamr0s/Dhizuku)
- [AndroidHiddenApiBypass](https://github.com/LSPosed/AndroidHiddenApiBypass)
- [AppOpsX](https://github.com/8enet/AppOpsX)
- [Hail](https://github.com/aistra0528/Hail)


---

# 捐赠与支持

如果轻风帮助到了你，欢迎支持作者继续开发更多实用的功能。

<p align="center">
<img src="app/src/main/assets/wechatqr.jpg" width="200">
&nbsp;&nbsp;&nbsp;&nbsp;
<img src="app/src/main/assets/aliqr.jpg" width="200">
</p>

---
<p align="center">Made with ❤️ for Android Enthusiasts</p>

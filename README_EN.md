<h1 align="center">Light Breeze</h1>

<p align="center">
A lightweight, core-focused, simple and easy-to-use Android system toolbox.
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


# Introduction

Light Breeze is a system management tool for advanced Android users.

It focuses on Chinese OEM customized system environments and provides three operation modes: **ADB, ROOT, and Device Administrator**, allowing users to manage their devices more freely, safely, and efficiently.

Light Breeze does not actively modify system core files, nor does it force user permissions.

All advanced features are based on active user authorization and are performed within the scope of permitted permissions.

Just like its name:

> Light breeze blows gently and leaves quietly.  
> No forced changes, no unauthorized intervention.  
> Just do your own thing, stay restrained, and stay pure.


---

# Features

Light Breeze currently integrates:

- Batch App Permission Management
- App Freeze / Unfreeze / Disable
- Batch Uninstall and Silent Install
- App Clone Management
- Background Process Cleanup
- Network Access Control
- App Component Management
- App Backup and Restore
- File Compression and Recovery
- LAN File Sharing
- ADB / Root Shell Command Execution


Also provided for some Android systems:

- Fix abnormal signal icons in AOSP-like systems
- Custom NTP Time Server
- Domestic Time Synchronization Optimization
- System Refresh Rate Adjustment
- System Limit Adjustment


Light Breeze encapsulates complex system operations into simple one-click functions.

Users only need to select the corresponding rules to complete the relevant operations.


---

# Operation Modes

## ADB Mode

Runs via ADB Shell permissions.

Enables some advanced system management capabilities without Root.

Supports:

- App Management
- Permission Adjustment
- System Command Execution
- Some Hidden API calls


---

## ROOT Mode

Unlock more advanced features with Root privileges.

Supports:

- System-level Backup and Restore
- Network Control
- App Component Control
- Clone Limit Adjustment
- System Boot Environment Creation
- Deeper System Management


---

## Device Administrator Mode

Runs via Android Device Admin capability.

No Root required.

Supports:

- App Uninstall
- App Install Management
- App Freeze / Unfreeze
- Hide / Show App Icons


---

# Technologies Used

Light Breeze is developed based on native Android technologies.

Mainly involving:

## Android Development

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


## System Capabilities

- Hidden API calls
- SystemService calls
- Framework internal interface calls
- Shell Command execution
- ADB Shell
- Root Shell


## File Processing

- Apache Commons Compress
- XZ compression algorithm
- File backup and restore
- Compressed file processing


## Compatibility Technologies

- Hidden API bypass
- Android multi-version adaptation
- OEM ROM compatibility optimization


Supports Android 4.0+ system environments.


---

# Project Architecture

Light Breeze adopts a front-end and back-end separated architecture.
System-level operations are handled by an independent background service.

```text
                         User Operation

                            │

                            ▼

              ┌────────────────────────┐
              │    LightBreeze App      │
              │ UI / Configuration /    │
              │ Request Management      │
              └───────────┬────────────┘
                          │
                          │ Binder IPC
                          │ AIDL Communication
                          ▼

              ┌────────────────────────┐
              │  LightBreeze Service   │
              │    Core Service        │
              │                        │
              │ Permission Management  │
              │ Task Scheduling        │
              │ System Capability Call │
              └───────────┬────────────┘
                          │


        ┌─────────────────┼─────────────────┐
        │                 │                 │


        ▼                 ▼                 ▼


 ┌──────────┐      ┌──────────┐      ┌──────────────┐
 │ ADB Mode │      │ Root Mode│      │ Device Admin │
 │ ADB      │      │ Root     │      │              │
 └────┬─────┘      └────┬─────┘      └─────┬────────┘


      └─────────────────┼──────────────────┘

                        │

                        ▼


        ┌─────────────────────────────────┐
        │        Android Framework        │
        │                                 │
        │ Binder IPC                      │
        │ AIDL Interface                  │
        │ Hidden API                      │
        │                                 │
        │ IPackageManager                 │
        │ IActivityManager                │
        │ IUserManager                    │
        │ IAppOpsService                  │
        └───────────────┬─────────────────┘


                        │

                        ▼


        ┌─────────────────────────────────┐
        │          System Capability      │
        │                                 │
        │ Application Management          │
        │ Permission Control              │
        │ User & Clone Management         │
        │ Network Control                 │
        │ File Processing                 │
        │ System Settings Adjustment      │
        └─────────────────────────────────┘
```

Design Features:

- Separates the UI layer from core execution logic
- Runs privileged operations inside an independent service module
- Uses Binder IPC / AIDL for module communication
- Selects execution capabilities based on ADB, Root, or Device Admin permissions
- Improves stability and maintainability


---

# How it Works

Light Breeze refers to several excellent open-source projects:

- Shizuku
- AppOps
- Hail
- Dhizuku
- InstallerX


Running Flow:

1. User selects operation mode
2. User completes permission authorization
3. Background service starts
4. Front-end sends operation requests
5. Background calls corresponding system capabilities to complete tasks


---

# What can Light Breeze do?

## Uninstall System Apps without Root

Through Device Administrator mode, you can uninstall some system apps that cannot be handled by ordinary ADB Shell.

Supports:

- VIVO
- iQOO
- OPPO
- OnePlus
- realme
- HONOR
- Samsung


---

## App Sensor Permission Management

For systems below Android 10:

- Manage app sensor permissions
- Disable sensor access for some apps

Reduce interference caused by "Shake" advertisements and other behaviors.


---

## Turn Phone into a System Boot Device

In Root environment:

- Create boot images
- Create boot disks
- Assist in Windows installation
- Assist in Linux installation


---

## App Clone Management

In Root environment:

- Adjust system clone limits
- Create more independent user environments
- Manage app clones


---

## LAN File Sharing

Via WiFi:

- Share specified folders
- Access files via LAN
- Transfer data quickly


---

# Screenshots

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

# Changelog
Starting from version V1.2.8b, the project is divided into two versions, maintained and updated separately: [Light Breeze No-root Version](https://github.com/MrsEWE44/lightBreeze/tree/md5) and [Light Breeze Full Version](https://github.com/MrsEWE44/lightBreeze/tree/master)

## V1.3.5a

1. Optimized the one-click automatic authorization function.

2. Optimized the app list retrieval function.

3. Updated the version number to 1.3.5a.


---

# Open Source Acknowledgements

Light Breeze refers to the following excellent open-source projects:

- [Shizuku](https://github.com/rikkaapps/shizuku)
- [AppOpsX](https://github.com/8enet/AppOpsX)
- [Hail](https://github.com/aistra0528/Hail)
- [Dhizuku](https://github.com/iamr0s/Dhizuku)
- [InstallerX](https://github.com/wxxsfxyzm/InstallerX-Revived)


Thanks to these projects for their exploration and practice in the field of advanced Android permission management.


---

# Precautions

Light Breeze is an advanced system tool.

Actual functionality may be affected by the following factors:

- Android version
- OEM customization strategy
- Root environment
- System security restrictions


Support may vary across different devices.

Before using advanced features, please ensure you understand the impact of the relevant operations.


---

# Support the Author

If Light Breeze has helped you, you are welcome to support the project development.


<p align="center">

<img src="app/src/main/assets/wechatqr.jpg" width="200">

<img src="app/src/main/assets/aliqr.jpg" width="200">

</p>

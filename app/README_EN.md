easyManager was born based on the FQAOSP project by cutting out some old content and coupling its new content. It eliminated many design flaws, improved execution and development efficiency, and opened many external development interfaces.

It has permission authorization management for any third-party application connected to the easyManagerSDK development kit.

It provides basic functions such as application permission management, application activity/service component management, apk application file installation, application uninstallation, application network control, application background cleanup, and application backup and recovery.

It also provides the external development interface API for the above functions. The client only needs to introduce easyManagerSDK and call the methods in the easyManagerClient object to use these functions.

easyMangaer completely uses system-level APIs to implement the above functions, directly calling services and functions in the framwork.jar framework to operate applications. Its efficiency is second only to system partition applications, and far more efficient than root shell.

easyManager is more recommended to run in ROOT mode.

[easyMangerSDK Development Kit](https://github.com/MrsEWE44/easyManagerSDK)


- V1.0

First build.
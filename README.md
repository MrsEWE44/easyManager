
easyManager是基于FQAOSP项目削减部分旧内容并耦合其新内容而诞生的,摈弃了众多设计缺陷,提升执行与开发效率,开放许多对外开发接口.

它具备给任何接入easyManagerSDK开发包的第三方应用进行权限授权管理.

它提供应用权限管理、应用活动/服务组件管理、apk应用文件安装、应用卸载、应用联网控制、应用后台清理、应用备份与恢复的基本功能使用.

它也提供上述功能的对外开发接口api,客户端只需要引入easyManagerSDK,并且调用easyManagerClient对象里的方法,即可使用这些功能.

easyMangaer完全采用系统级api实现上述功能,直接调用framwork.jar框架里的服务与函数来操作应用,效率仅次于system分区的应用,效率远超root shell这种方式.

easyManager更推荐在ROOT模式下运行.

[easyMangerSDK开发工具包](https://github.com/MrsEWE44/easyManagerSDK)


- V1.0

初次构建.

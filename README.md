# catconfig 配置中心
持久化配置中心中间件（参考Apollo、Nacos）

## 项目包括如下几个部分

* [catconfig-server](./catconfig-server)：配置中心服务端，负责配置的存储、发布、同步等。
* [catconfig-client](./catconfig-client)：配置中心客户端，负责配置的获取、更新等。
* [catconfig-demo](./catconfig-demo)：catconfig-demo: 配置中心客户端demo。

## 当前进展
* 初始化项目骨架
* 添加 server 端基本DB配置操作，实现配置增删改查
* 添加 client 端基本框架，将自定义配置加载到 Spring ENV PropertySources 中
* 添加 demo 模块，测试 client 自定义配置获取
* 增加定时刷新机制，实现配置自动更新
* 通过 EnvironmentChangeEvent 事件机制，自动实现 ConfigurationProperties 配置更新
* 接收到 EnvironmentChangeEvent 配置更新事件后，手动更新 spring value 注解配置值
* 增加基于DB的分布式锁，集群环境多节点时确定唯一主节点

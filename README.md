<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [READ MORE](#read-more)
- [数据采集,处理,监控,调度,管理一体化平台](#%E6%95%B0%E6%8D%AE%E9%87%87%E9%9B%86%E5%A4%84%E7%90%86%E7%9B%91%E6%8E%A7%E8%B0%83%E5%BA%A6%E7%AE%A1%E7%90%86%E4%B8%80%E4%BD%93%E5%8C%96%E5%B9%B3%E5%8F%B0)
- [开源/闭源版本](#%E5%BC%80%E6%BA%90%E9%97%AD%E6%BA%90%E7%89%88%E6%9C%AC)
- [下载编译包](#%E4%B8%8B%E8%BD%BD%E7%BC%96%E8%AF%91%E5%8C%85)
- [在线预览](#%E5%9C%A8%E7%BA%BF%E9%A2%84%E8%A7%88)
- [提示](#%E6%8F%90%E7%A4%BA)
- [特色](#%E7%89%B9%E8%89%B2)
- [使用场景](#%E4%BD%BF%E7%94%A8%E5%9C%BA%E6%99%AF)
- [主要功能](#%E4%B8%BB%E8%A6%81%E5%8A%9F%E8%83%BD)
- [用到的技术体系](#%E7%94%A8%E5%88%B0%E7%9A%84%E6%8A%80%E6%9C%AF%E4%BD%93%E7%B3%BB)
- [下载修改基础配置](#%E4%B8%8B%E8%BD%BD%E4%BF%AE%E6%94%B9%E5%9F%BA%E7%A1%80%E9%85%8D%E7%BD%AE)
- [下载编译好的包](#%E4%B8%8B%E8%BD%BD%E7%BC%96%E8%AF%91%E5%A5%BD%E7%9A%84%E5%8C%85)
- [源码自定义打包](#%E6%BA%90%E7%A0%81%E8%87%AA%E5%AE%9A%E4%B9%89%E6%89%93%E5%8C%85)
- [运行](#%E8%BF%90%E8%A1%8C)
- [FAQ](#faq)
- [功能图](#%E5%8A%9F%E8%83%BD%E5%9B%BE)
- [版本更新说明](#%E7%89%88%E6%9C%AC%E6%9B%B4%E6%96%B0%E8%AF%B4%E6%98%8E)
- [4.7.15迁移4.7.16](#4715%E8%BF%81%E7%A7%BB4716)
- [4.7.16迁移4.7.17](#4716%E8%BF%81%E7%A7%BB4717)
- [4.7.17迁移4.7.18](#4717%E8%BF%81%E7%A7%BB4718)
- [4.7.18迁移5.0.0](#4718%E8%BF%81%E7%A7%BB500)
- [未完成的功能](#%E6%9C%AA%E5%AE%8C%E6%88%90%E7%9A%84%E5%8A%9F%E8%83%BD)
- [支持的数据源](#%E6%94%AF%E6%8C%81%E7%9A%84%E6%95%B0%E6%8D%AE%E6%BA%90)
- [支持的调度对象](#%E6%94%AF%E6%8C%81%E7%9A%84%E8%B0%83%E5%BA%A6%E5%AF%B9%E8%B1%A1)
- [支持的调度器模式](#%E6%94%AF%E6%8C%81%E7%9A%84%E8%B0%83%E5%BA%A6%E5%99%A8%E6%A8%A1%E5%BC%8F)
- [支持调度动态日期参数](#%E6%94%AF%E6%8C%81%E8%B0%83%E5%BA%A6%E5%8A%A8%E6%80%81%E6%97%A5%E6%9C%9F%E5%8F%82%E6%95%B0)
- [版本计划](#%E7%89%88%E6%9C%AC%E8%AE%A1%E5%88%92)
- [个人联系方式](#%E4%B8%AA%E4%BA%BA%E8%81%94%E7%B3%BB%E6%96%B9%E5%BC%8F)
- [界面预览](#%E7%95%8C%E9%9D%A2%E9%A2%84%E8%A7%88)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# READ MORE
[English description](README_en.md)

# 数据采集,处理,监控,调度,管理一体化平台
    因本项目没有其他文档,请使用者及开发者详细阅读完本readme 文档,后期会以博客的形式对本项目进行详细功能说明
   博客目录地址：  [https://blog.csdn.net/zhaoyachao123/article/details/113913947](https://blog.csdn.net/zhaoyachao123/article/details/113913947)
   

   欢迎你对本项目提出相关issue
   
   本平台主要目的
   + 高效的进行数据采集,构建自己的大数据平台
   + 统一数据管理,对数据进行高效分析及对外输出
   + 通过工具完成大部分工作,减少开发者的工作量
   + 降低使用者标准,通过拖拉拽实现数据的采集(任务依赖关系由自带调度完成-优势)
   + 本平台的初衷及目的尽量减少开发者的工作量及降低数据开发者的使用门槛
   
# 开源/闭源版本
   + 4.7.10以及之前的版本-代码开源且免费
   + 4.7.11以及之后的版本-提供编译好的包免费使用,但是不开放源码
   
# 下载编译包   
   + 编译包下载地址(只提供4.7.11之后的版本,因4.7.11之后不提供源码下载)：
   + 执行编译好的包需要提前安装mysql8,redis
   
   + 历史版本(>4.7.11)只提供最近2个版本目录下载,下载历史版本,点击下方下载连接,修改链接中的版本号,可下载历史版本
     建议使用最新版本(功能更多)
     
   + 4.7.18版本全部采用maven管理,并重构quartz源码,4.7.18不可和之前任何历史版本重用
   + 4.7.18为4.x最后一个版本,5.x版本会重构所有代码,自2022-02-06~2022-06-01不进行新功能开发,此段时间会完善使用文档
 
   + 4.7.18
     +  [zdh_web_4.7.18](http://zycblog.cn:8080/zdh/download/4.7.18/zdh_web.tar)
     +  [zdh_server_4.7.18](http://zycblog.cn:8080/zdh/download/4.7.18/zdh_server.tar)
     +  [zdh_flink_4.7.18](http://zycblog.cn:8080/zdh/download/4.7.18/zdh_flink.tar)     
      
   + 5.0.0
     +  [zdh_web_5.0.0](http://zycblog.cn:8080/zdh/download/5.0.0/zdh_web.tar)
     +  [zdh_server_5.0.0](http://zycblog.cn:8080/zdh/download/5.0.0/zdh_server.tar)
     +  [zdh_flink_5.0.0](http://zycblog.cn:8080/zdh/download/5.0.0/zdh_flink.tar)
           
   + 如果链接失效,可通过邮件方式(见底部)通知作者,作者会通过邮件发送编译包,也可登陆ZDH预览页面下载

#  在线预览
   [http://zycblog.cn:8081/login](http://zycblog.cn:8081/login)
   
    用户名：zyc
    密码：123456
    
    服务器资源有限,界面只供预览,不包含数据处理部分,谢码友们手下留情

# 提示
   
    zdh 分2部分,前端配置+后端数据ETL处理,此部分只包含前端配置
    后端数据etl 请参见项目 https://github.com/zhaoyachao/zdh_server.git
    zdh_web 和zdh_server 保持同步 大版本会同步兼容 如果zdh_web 选择版本1.0 ,zdh_server 使用1.x 都可兼容
    二次开发同学 请选择dev 分支,dev 分支只有测试通过才会合并master,所以master 可能不是最新的,但是可保证可用性

# 特色
    开箱即用
    支持多数据源
    高性能数据采集
    单独的调度器,调度也可和三方调度器对接airflow,azkaban
    二次开发
    
   
# 使用场景
  + 数据采集(本地上传数据,hdfs,jdbc,http,cassandra,mongodb,redis,kafka,hbase,es,sftp,hive)
  + 数据加密
  + 数据转换,数据离线同步,实时数据同步
  + 数据迁移
  + 质量检测
  + 元数据,指标管理
  + drools灵活动态的数据清洗
  
  
  
# 主要功能
 zdh 主要的作用 是从hdfs,hive,jdbc,http-json接口 等数据源拉取数据,并转存到hdfs,hive,jdbc等其他数据源
 支持集群式部署
 
 
  + 支持sql标准函数
  + 支持界面选择配置
  + 支持快速复制已有任务
  + 支持外部调度工具(需要修改,新增特定接口)
  + 弹性扩展(可单机,可集群)
  + 支持客户级权限
  + 简单易用支持二次开发
  + 自带简单调度工具,可配置定时任务,时间序列任务,设定次数
  + 调度依赖
  + SQL数据仓库数据处理(单一数仓)
  + 质量检测,及对应报告
  + 支持SHELL 命令,SHELL 脚本,JDBC查询调度,HDFS查询调度
  + 支持本地上传,下载文件
  + 支持多源ETL
  + 任务监控
  + 灵活动态drools规则清理
  
# 用到的技术体系

    前端：Bootstrap
    后端：Springboot+shiro+redis+mybatis
    数据ETL引擎:Spark(hadoop,hive 可选择部署)
    
# 下载修改基础配置

    打开resources/application-dev.properties
     1 修改服务器端口默认8081
     2 修改数据源连接(默认支持mysql8),外部数据库必须引入
     3 修改redis配置
     4 修改application-*配置文件中myid(多个集群依次1,2,3,...)

    创建需要的数据库配置
     1 执行sql脚本db.sql
     
    依赖
     1 必须提前安装redis 

# 下载编译好的包

    1 找到项目目录下的release 目录 直接将release 目录拷贝
    2 到relase的bin 目录下执行start 脚本(启动脚本必须到bin 目录下执行)
    3 执行编译好的包需要提前安装mysql8,redis

# 源码自定义打包
    
    清理命令 mvn clean
    打包命令 mvn package -Dmaven.test.skip=true

# 运行
    4.7.13 之前
      在target 目录下找到zdh_web.jar
      执行 java  -Dfile.encoding=utf-8 -jar zdh_web.jar  
    4.7.13及之后
      进入在xxx-RELEASE 目录下
      执行 java -jar -Dfile.encoding=utf-8 -Dloader.path=libs/,conf zdh_web.jar     

# FAQ

   + sql结构报错
     遇到sql 结构报错,可直接在resource目录下找db.sql 文件对比是否增加了字段,db.sql 文件会使用alter 方式增加字段
     quartz 相关的表必须大写
     
   + 日志级别修改
     修改日志文件logback 相关等级即可
     
   + 调度串行并行模式
     串行模式:会判断上次任务运行状态
     并行模式:不判断上次任务状态,时间会自动生成 
     
   + 数据表结构以src/main/resources/db.sql 为准
   
   + 暂不支持读取kerberos 认证的hadoop,hive,hbase 服务,预计在5.x 版本实现kerberos 认证
  
# 功能图
![功能图](img/zdh_web.png)  
  
# 版本更新说明
  + v1.0 支持常用数据jdbc,hive,kafka,http,flume,redis,es,kudu,mongodb,hbase,cassandra,hdfs(csv,json,orc,parquet,xml,excel...),本地上传数据(csv)
  + v1.0 调度支持任务依赖等
 
  + v1.1 支持clickhouse-jdbc
  
  + v1.2 支持外部jar etl任务(任务状态需要外部jar 自己跟踪)
  
  + v1.3 支持drools 数据清理
  
  + v1.4 支持greenplum-jdbc
  
  + v2.0 删除外部jar 任务使用ssh 任务代替,ssh任务功能新增
  + v2.0 drools 任务增加支持多源和sql任务
  + v2.0 clickhouse,hive spark数据源优化
  + v2.0 spark sftp数据框架改动,增加sftp excel 和多分隔符支持
  + v2.0 调度重试机制优化,增加节点失败重发功能(任务重启)
  + v2.0 增加调度单独告警机制
  + v2.0 server 模块高可用机制改动为负载高可用
  + v2.0 hbase,drools jar冲突bug 修复
  + v2.0 支持ssh 任务静态脚本,动态脚本
  + v2.0 kafka,flume实时数据源删除必须使用jdbc输出源限制
  + v2.0 修复spark 监控bug,移动spark监控到总监控

  + v2.1 zdh_web 添加redis cluster 支持
  + v2.1 增加jdbc支持presto,mariadb,memsql,华为dws,阿里AnalyticDB,kylin,gbase,kingbase,redshift
  
  + v2.2 调度机制增加ack,无感故障转移
  + v2.2 优化所有前端界面,增加状态高亮
  + v2.2 sql 编辑支持高亮显示
  + v2.2 手动执行调度改为异步执行
  + v2.2 任务日志获取方式改变（时间获取改为标识符+时间获取）
  
  + v3.0 前端界面状态可视化优化
  + v3.0 去除task_logs 任务日志表,增加task_log_instance 表作为替换(大变动2.0 版本和3.0 完全不兼容)
  + v3.0 修复favicon显示bug
  + v3.0 增加调度器端的故障转移
  + v3.0 监控界面增加手动重试功能
  + v3.0 拆分quartz_job_info 调度任务表,每次执行调度任务会生成一个当前状态的实例表(重试,故障转移,ack等都基于实例表完成逻辑操作)
  + v3.0 增加单任务并行处理机制接口(只留了实现接口,并未做具体实现,暂不支持单任务并行)
  + v3.0 zdh_web项目增加调度器id(主要用做故障转移,判断任务是否是故障转移触发)
  + v3.0 手动执行删除重置功能(会生成一个实例,所以去掉重置功能)
  + v3.0 手动执行,调度执行都会删除之前的实例依赖(手动执行后,必须手动设置正确的调度时间)
  + v3.0 增加数据采集后端任务杀死功能
  + v3.0 增加超时告警任务-只告警不杀死
  + v3.0 hbase 删除jersey相关jar,解决jar冲突
  + v3.0 批量删除增加确认删除弹框
  + v3.0 修改spark 任务job组说明格式
  
  + v3.1 任务依赖检查实现方式修改
  + v3.1 增加cron 表达式生成页面
  + v3.1 文件名增加动态设置(根据时间生成特定规则文件名)
  + v3.1 启动时基础参数校验
  + v3.1 quartz 任务优先级设定
  + v3.1 增加支持quartz时间触发(串行+并行方式都支持)
  + v3.1 新增dag 工具类(计划3.2版本支持dag调度)
  
  + v4.0 实现调度拖拉拽
  + v4.0 重新实现任务发现机制
  + v4.0 增加任务组,子任务概念实现组任务
  + v4.0 实现dag调度
  + v4.0 实现调度流程图
  + v4.0 重新实现任务类型(4.x之前版本不兼容)
  + v4.0 增加greenplum-spark连接器
  + v4.0 全新逻辑,值得一试
  
  + v4.1 修复4.0 调度重试bug
  + v4.1 修复4.0 调度界面bug
  + v4.1 重新实现时间选取机制,调度性能提高
  + v4.1 修改监控界面去除组任务监控按钮添加子任务监控界面
  
  + v4.2 修复4.1bug
  + v4.2 sql脚本增加中文说明
  
  + v4.3 增加部分子任务重试机制
  + v4.3 增加手动执行部分子任务机制
  + v4.3 增加子任务运行时依赖图展示
  + v4.3 任务状态增加跳过状态
  + v4.3 优化logback日志配置
  + v4.3 修复手动重试新版bug
  + v4.3 删除zookeeper工具包
  + v4.3 删除[重复执行]调度任务模式
  + v4.3 删除mq配置信息
  + v4.3 删除netty工具包
  
  + v4.4 优化前端界面,日志界面每次打开新标签页
  + v4.4 优化超时告警
  + v4.4 修复SSH未结束异常
  + v4.4 删除多余的debug
  + v4.4 调整quartz延迟启动60s->20s
  + v4.4 删除线程池(影响SSH任务-待优化)
  
  + v4.5 修复检查任务组时空类型异常
  
  + v4.6 优化日志记录界面,增加日志下载,优化日志展示风格
  + v4.6 补充tidb任务说明
  + v4.6 增加自定义时间参数(高级使用)
  + v4.6 优化调度拖拽逻辑,简化数据结构
  + v4.6 修改jinjava 版本为2.5.6
  + v4.6 增加调度任务禁用功能
  + v4.6 增加调度任务组模块
  + v4.6 增加权限管理功能,功能菜单权限
  + v4.6 增加外部jdbc任务依赖检查

  + v4.7 修复4.6jdbc依赖检查杀死问题
  + v4.7 修复执行器死亡检测不到问题
  + v4.7 修复监控主界面杀死任务组问题
  + v4.7 计划支持滚动升级(目前滚动升级只完成了一键部署和扩容功能,未实现动态管理web节点上下线)
  + v4.7 升级jsch 版本为0.1.55
  + v4.7 优化任务界面模糊查询
  + v4.7 优化日志界面时间
  + v4.7 优化shell脚本特殊符号
  
  + v4.7.1 新增一键部署及优化
  + v4.7.2 修复js中多余alert
  + v4.7.2 修复jdbc任务依赖时无效bug
  + v4.7.3 增加任务触发模式(上游成功触发?上游失败触发)
  + v4.7.3 task_log_instance 增加depend_level 字段(作用判断任务触发状态)
  + v4.7.3 优化任务组监控界面进度中文注释信息
  + v4.7.4 修复任务组杀死时部分任务依旧显示运行中bug
  + v4.7.5 增加采集任务指定集群执行(仅支持etl采集任务,对jdbc,shell,group等检查依赖任务无此功能,切换调度集群更方便如yarn)
  + v4.7.6 无大修改,优化logback 配置文件启用滚动日志
  + v4.7.7 优化jdbc任务禁用按钮
  + v4.7.7 增加jdbc依赖任务写入功能(之前版本只支持查询不支持写入-指标完成标识功能衍化)
  + v4.7.8 增加给作者提bug(联系作者)功能
  + v4.7.9 废弃JdbcJob,HdfsJob,以任务流依赖检查模块实现依赖检查
  + v4.7.9 废弃InstanceStatus,统一使用JobStatus 管理任务状态
  + v4.7.9 优化jinjava 参数,统一时间参数
  + v4.7.9 优化shell job 时间参数,增加实时日志展示
  + v4.7.10 优化数据表字段注释
  + v4.7.10 优化生成server端任务日志
  + v4.7.10 前端增加数据源可空功能
  + v4.7.10 新增数据源逻辑删除功能(数据源表结构变动)
  
  + v4.7.11 优化spark-jdbc性能增加提前过滤功能
  + v4.7.11 增加请求版本号url(http://xxx:port/version)
  + v4.7.11 调度界面优化-增加默认值,及不可为空校验
  + v4.7.11 增加hdfs调度组件解决检查hdfs文件是否存在
  + v4.7.11 优化通知界面,显示通知信息
  + v4.7.11 优化杀死任务时异常
  + v4.7.11 增加数据发布,申请,审批功能
  + v4.7.11 新增申请源ETL(使用他人发布的数据作为输入数据源)
  + v4.7.11 增加新的表结构apply_info,etl_apply_task_info
  + v4.7.11 优化串行作业组杀死功能,上游杀死,则对应下游任务直接杀死
  + v4.7.11 增加发布数据-增删改查功能
  + v4.7.11 增加quartz并发参数配置
  + v4.7.11 开启调度时超过最大调度时间弹框提示
  + v4.7.11 作业组为空时,优化作业组结束状态
  + v4.7.11 告警作业限制只检查最近2天内的失败作业
  + v4.7.11 优化shell脚本前端编辑器
  + v4.7.11 修复调度自定义表达式天级表达式
  + v4.7.11 修复空作业组杀死时状态未改变bug
  + v4.7.11 修复shell任务ping命令杀死异常
  + v4.7.11 手动执行任务,重试任务不限制调度日期检查
  + v4.7.11 db.sql脚本增加菜单表,权限表数据
  + v4.7.11 task_log_instance,task_group_log_instance 增加调度来源字段标识任务触发来源
  + v4.7.11 数据源逻辑删除修复更新时异常bug
  + v4.7.11 优化日志界面状态外观
  + v4.7.11 增加etl任务配置时,数据倾斜,分区,合并小文件,写入模式参数
  + v4.7.11 修复etl任务表达式列表多次增加问题(前端优化)
  + v4.7.11 修复shell任务调度死掉自动重试
  + v4.7.11 子任务界面显示job_id
  + v4.7.11 zdh_server增加hdfs输出数据源zk路径解析功能
  + v4.7.11 修改多源任务临时表实现
  + v4.7.11 修复多源任务前端bug(表头信息更新不成功)
  + v4.7.11 优化子任务界面状态显示
  + v4.7.11 优化权限界面统一返回值数据结构
  + v4.7.11 优化etl任务界面统一返回值数据结构
  + v4.7.11 优化告警模块-增加失败告警,完成通知,超时通知
  + v4.7.11 平台消息通知重构,统一增加消息管理模块
  + v4.7.11 修复shiro 获取用户信息异常
  + v4.7.11 统一返回数据结构,并重构controller层
  + v4.7.11 增加FLINK采集模块
  + v4.7.11 优化登陆模块-增加验证码shiro整合
  + v4.7.11 FLINK增加checkpoint机制
  + v4.7.11 重构html引入项目根目录
  + v4.7.11 升级springboot版本1.5.7至1.5.22
  + v4.7.11 重构登陆页面,注册页面
  + v4.7.11 增加账户禁用启用功能
  + v4.7.11 废弃旧版权限模块-改动为 用户->角色->资源 模型模式
  + v4.7.11 调度任务配置首页增加cron表达式生成链接
  + v4.7.11 首页去除角色显示,使用签名替换
  + v4.7.11 菜单管理-新增资源类型,支持接口权限
  + v4.7.11 重构审批流-支持多人审批,删除旧版数据审批模块
  + v4.7.11 修复shiro 查询用户信息时无效bug
  + v4.7.11 增加我发起的流程,流程进度信息
  + v4.7.11 数据申请使用审批流重构
  + v4.7.11 修复通知信息-重复显示问题(删除页面下拉面板)
  + v4.7.11 禁用shiro授权缓存
  + v4.7.11 更新发布数据时,通知下游具体修改内容
  + v4.7.11 修改菜单类型,增加内置页面类型
  + v4.7.11 统一controller层映射规则
  + v4.7.11 开启接口权限验证
  + v4.7.11 flink状态监控及更新
  + v4.7.11 修复server_ack机制
  + v4.7.11 修复重试-显示禁用任务bug
  + v4.7.11 web支持flink增加杀死,重试机制
  + v4.7.11 重试时,至少有一个子任务,否则无法重试
  + v4.7.11 flink端判定任务状态检查
  
  + v4.7.12 优化quartz 超大量任务引起的重复执行问题
  + v4.7.12 增加【jdbc引擎ETL】,使用presto,clickhouse等jdbc引擎ETL时,可单独使用zdh_web完成,不集成zdh_server
  + v4.7.12 历史【jdbc依赖】优化
  
  + v4.7.13 springboot jar 分离
  
  + v4.7.14 增加【datax引擎】,通过调度实现可视化配置
  + v4.7.14 调度页面告警用户账户由输入框改为多选下拉框(模糊匹配)
  + v4.7.14 优化任务组更新时间,任务更新时间
  + v4.7.14 优化mybatis-generator插件配置,启用自动生成
  + v4.7.14 修复数据源新增权限问题
  
  + v4.7.15 修复数据源字段获取权限,上传文件权限
  + v4.7.15 单独拆分数据质量检测模块,新增数据质量规则
  + v4.7.15 修复v4.7.12至4.7.14中发送任务模块到server(spark)失败bug
  + v4.7.15 优化加载通知有每10秒改为每60秒加载
  
  + v4.7.16 检查是否存在log4j2漏洞(无)
  + v4.7.16 优化调度新增页面,增加告警账号不可为空限制
  + v4.7.16 增加血源分析(目前只支持1层上下游关系-待优化)
  + v4.7.16 修复task_group_logs_delete权限
  + v4.7.16 新增权限dispatch_task_execute_time
  + v4.7.16 新增调度时间预查看
  + v4.7.16 增加数据源逻辑删除
  + v4.7.16 菜单资源页面,角色配置页面 增加树查询功能
  + v4.7.16 调度页面-复制功能bug修复
  + v4.7.16 数据源增加更新字段
  + v4.7.16 删除历史数据源逻辑层,使用Dao层代替
  + v4.7.16 修复ssh页面关闭无效bug
  + v4.7.16 单源ETL,多源ETL,SSH,SQL,FLINK_SQL,JDBC_SQL,申请源ETL 增加逻辑删除和更新时间
  
  + v4.7.17 接口无权限-增加ZDH告警
  + v4.7.17 修复every_day_notice接口,无数据异常bug
  + v4.7.17 修复操作日志写入字符个数
  + v4.7.17 增加批量任务生成工具
  + v4.7.17 优化ZDH目录生成,支持多层级目录生成
  + v4.7.17 新增工具箱目录,迁移Cron,操作日志,整库迁移到工具箱下
  + v4.7.17 取消所有dataSourcesService层,直接使用dao层代替
  + v4.7.17 优化通知信息,增加逻辑删除
  + v4.7.17 优化目录-增加提示语
  + v4.7.17 修复调度页面Shell显示bug
  + v4.7.17 替换全局editor为monaco editor(界面更加清晰美观)
  + v4.7.17 优化调度新增页面js(删除无用js)
  + v4.7.17 申请源ETL增加明细查询权限
  + v4.7.17 删除所有日志打印,统一使用logger管理
  + v4.7.17 增加ZDH项目下载地址
  
  + v4.7.18 替换编辑器为monaco editor
  + v4.7.18 修改fastjson:1.2.44升级为1.2.69
  + v4.7.18 修改shiro:1.4.0升级1.7.1
  + v4.7.18 mysql:8.0.11升级为8.0.13
  + v4.7.18 commons-io:2.6升级2.7
  + v4.7.18 httpclient:4.5.5升级4.5.13
  + v4.7.18 删除shiro自动session验证,采用zdh自带quartz任务重写session校验
  + v4.7.18 增加调度器管理-可对线上已开启的调度器-自动上线下线
  + v4.7.18 新增菜单时-增加图标查看网址
  + v4.7.18 修复flink任务杀死bug
  + v4.7.18 zdh_server更名为zdh_spark,zdh_flink更名为zdh_flinkx 项目管理工具使用maven代替gradle
  + v4.7.18 重构zdh_flinkx 项目架构
  + v4.7.18 调度任务-调度时间策略-使用quartz触发时间为默认策略
  + v4.7.18 实现自定义quartz instance_id 生成策略
  + v4.7.18 调度任务增加quartz miss策略
  + v4.7.18 调度任务修改quartz触发逻辑-修复quartz触发时间和quartz逻辑调度时间
  + v4.7.18 增加系统监控api
  + v4.7.18 优化审批流-动态使用审批节点
  + v4.7.18 增加枚举管理
  + v4.7.18 SSH,SFTP增加免密登录
  + v4.7.18 修复数据质量检测提示错误
  + v4.7.18 废弃js/jsplumb/index2.js
  + v4.7.18 增加tomcat线程配置参数
  + v4.7.18 修改mysql8默认密码验证为mysql_native_password模式
  + v4.7.18 修复用户中心接口地址请求,支持多项目名
  + v4.7.18 修复数据仓库信息更新时,发布状态异常bug
  + v4.7.18 优化资源配置页面,交互更加友好
  + v4.7.18 修复增加根项目名,部分页面无法获取数据bug
  + v4.7.18 数据源增加权限控制
  + v4.7.18 系统限流等动态参数配置
  
  + v5.0.0 增加数据资产导出功能
  + v5.0.0 增加调度器自动启动配置
  + v5.0.0 增加数据资产查询
  + v5.0.0 日志下载增加白名单
  + v5.0.0 实现非结构化数据录入-并自动生成对应关系
  + v5.0.0 实现非结构化数据录入数据源-只支持sftp,ftp,hdfs
  + v5.0.0 [zdh_spark]修复hbase写入动态更新进度bug
  + v5.0.0 [zdh_spark]修复hbase写入时,原始数据为空时无法写入bug
  + v5.0.0 [zdh_spark]优化kafka数据写入
  + v5.0.0 优化404页面,增加403无权限页面
  + v5.0.0 功能权限(角色,资源)-增加产品绑定
  + v5.0.0 [zdh_spark]升级hudi-spark版本0.5.1==>0.10.1
  + v5.0.0 重构zdh_spark 进度监控
  + v5.0.0 优化登录页面-验证码,及验证码无效提示
  + v5.0.0 优化列表页面无权限提示
  + v5.0.0 修复web端登录后,再次使用login页面无跳转首页bug
  + v5.0.0 删除自定义StringUtils使用common-lang3.StringUtils
  + v5.0.0 requestmapping注解中补全请求类型
  + v5.0.0 整合springdoc,当前不完善,因json解析冲突,会出现json错误日志(不影响系统)
  + v5.0.0 优化枚举,增加枚举类别显示
  + v5.0.0 增加系统监控任务列表,启用,禁用
  + v5.0.0 优化日志打印
  + v5.0.0 数据资产页面增加分页大小
  + v5.0.0 优化数据资产获取标签逻辑,改用通过获取枚举类别获取数据资产的标签
  + v5.0.0 迁移验证码接口到LoginController层
  + v5.0.0 操作日志增加耗时
  + v5.0.0 无权限增加操作日志
  + v5.0.0 日志打开时间条件查询,修复监控页面组日志日期无效bug
  + v5.0.0 审批页面-增加跳过流程,申请页面-增加跳过流程
  + v5.0.0 申请页面bug修复
  + v5.0.0 优化登录页面背景
  + v5.0.0 appication相关配置文件修改编码为utf-8
  + v5.0.0 增加spark数据源解析
  + v5.0.0 pom增加screw数据库文档生成插件
  + v5.0.0 增加系统访问限制
  + v5.0.0 优化系统参数配置,同步redis增加失效限制
  + v5.0.0 优化mybatis like 查询
  + v5.0.0 修复部分页面查询条件无效
  + v5.0.0 修复审批流配置-查询条件无效
  + v5.0.0 数据库timestamp类型改为datetime类型
  + v5.0.0 优化数据资产分页
  + v5.0.0 增加首页公告弹框,及展示类型(弹框,文字)
  + v5.0.0 增加ip黑名单限制访问
  + v5.0.0 操作日志增加ip记录
  + v5.0.0 增加系统名可修改
  
  + v5.0.1 增加用户黑名单限制【开发中】
  + v5.0.1 增加接口黑名单限制【开发中】
  + v5.0.1 权限单独出SDK及分配身份码(身份码和产品id加密解密绑定)【开发中】
  + v5.0.1 数据资产-申请组信息【开发中】
  + v5.0.1 增加HTTP,EMAIL调度对象 【开发中】
  + v5.0.1 wemock 【开发中】
  + v5.0.1 flink增加手动checkpoint功能 【开发中】
  
  
  
  
# 4.7.15迁移4.7.16
    alter table data_sources_info add column update_time timestamp default null comment '更新时间';
    alter table etl_task_info add column update_time timestamp default current_timestamp() comment '更新时间';
    alter table etl_task_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';
    
    alter table etl_more_task_info add column update_time timestamp default current_timestamp() comment '更新时间';
    alter table etl_more_task_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';
    
    alter table sql_task_info add column update_time timestamp default current_timestamp() comment '更新时间';
    alter table sql_task_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';
    
    alter table ssh_task_info add column update_time timestamp default current_timestamp() comment '更新时间';
    alter table ssh_task_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';
    
    
    alter table etl_task_flink_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';
    
    alter table etl_task_jdbc_info add column update_time timestamp default current_timestamp() comment '更新时间';
    alter table etl_task_jdbc_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';
    
    alter table etl_task_datax_info add column update_time timestamp default current_timestamp() comment '更新时间';
    alter table etl_task_datax_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';
    
    alter table etl_apply_task_info add column update_time timestamp default current_timestamp() comment '更新时间';
    
    CREATE TABLE `alarm_sms_info` (
      `id` bigint NOT NULL AUTO_INCREMENT,
      `title` varchar(500) DEFAULT NULL COMMENT '任务说明',
      `msg` text COMMENT '信息',
      `msg_type` varchar(100) DEFAULT NULL COMMENT '信息类型，通知,营销',
      `msg_url` varchar(500) DEFAULT NULL COMMENT '短信附带连接',
      `phone` varchar(100) DEFAULT NULL COMMENT '手机号',
      `status` varchar(8) DEFAULT '0' COMMENT '状态,0:未处理,1:处理中,2:失败,3:成功,4:不处理',
      `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
      `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;
    
    CREATE TABLE `user_operate_log` (
      `id` bigint NOT NULL AUTO_INCREMENT,
      `owner` varchar(500) DEFAULT NULL COMMENT '账号',
      `user_name` varchar(500) DEFAULT NULL COMMENT '用户名',
      `operate_url` varchar(500) COMMENT '操作url',
      `operate_context` varchar(500) COMMENT '操作说明',
      `operate_input` text  COMMENT '输入参数',
      `operate_output` text COMMENT '输出结果',
      `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
      `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;
  
  
# 4.7.16迁移4.7.17
     CREATE TABLE `etl_task_batch_info` (
       `id` bigint NOT NULL AUTO_INCREMENT,
       `etl_pre_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
       `etl_suffix_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
       `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
       `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
       `data_sources_table_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源表名',
       `data_sources_file_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源文件名',
       `data_sources_params_input` varchar(500) DEFAULT NULL COMMENT '输入数据源参数',
       `data_sources_filter_input` varchar(500) DEFAULT NULL COMMENT '输入数据源过滤条件',
       `data_sources_choose_output` varchar(100) DEFAULT NULL COMMENT '输出数据源id',
       `data_source_type_output` varchar(100) DEFAULT NULL COMMENT '输出数据源类型',
       `data_sources_table_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源表名',
       `data_sources_file_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源文件名',
       `data_sources_params_output` varchar(500) DEFAULT NULL COMMENT '输出数据源参数',
       `data_sources_clear_output` varchar(500) DEFAULT NULL COMMENT '数据源数据源删除条件',
       `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
       `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
       `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
       `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
       `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
       `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
       `file_type_input` varchar(10) DEFAULT NULL COMMENT '输入文件类型',
       `encoding_input` varchar(10) DEFAULT NULL COMMENT '输入文件编码',
       `sep_input` varchar(10) DEFAULT NULL COMMENT '输入分割符',
       `file_type_output` varchar(10) DEFAULT NULL COMMENT '输出文件类型',
       `encoding_output` varchar(10) DEFAULT NULL COMMENT '输出文件编码',
       `sep_output` varchar(10) DEFAULT NULL COMMENT '输出文件分割符',
       `header_input` varchar(10) DEFAULT NULL COMMENT '输入是否包含表头',
       `header_output` varchar(10) DEFAULT NULL COMMENT '输出是否包含表头',
       `repartition_num_input` varchar(64) NOT NULL DEFAULT '' COMMENT '洗牌个数默认空',
       `repartition_cols_input` varchar(256) NOT NULL DEFAULT '' COMMENT '洗牌字段默认空',
       `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
       `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
       `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
       `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
       `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
       `status` varchar(8) not null default '0' comment '0:未执行,1:执行中,2:执行失败,3:执行成功',
       PRIMARY KEY (`id`)
     ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

    alter table resource_tree_info add column notice_title varchar(8) not null default '' comment '提示语';
  
# 4.7.17迁移4.7.18
    CREATE TABLE `quartz_executor_info` (
      `id` bigint NOT NULL AUTO_INCREMENT,
      `instance_name` varchar(512) DEFAULT NULL COMMENT '调度器唯一实例名',
      `status` varchar(200) DEFAULT NULL COMMENT '任务说明',
      `is_handle` varchar(100) DEFAULT NULL COMMENT '是否处理过,true/false',
      `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
      `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
    
    -- 此处qrtz表名确定是否大写,如果数据库开启不区分大小写,可小写,否则必须使用QRTZ_SCHEDULER_STATE
    alter table qrtz_scheduler_state add column STATUS varchar(16) not null default 'online' comment '状态，下线offline,上线online';

    -- 调度增加策略模式
    alter table quartz_job_info add column misfire varchar(8) not null default '0' comment '恢复策略,0:无操作,1:所有历史重新执行,2:最近一次历史重新执行';
    -- 增加审批流优化
    alter table resource_tree_info add column event_code varchar(64) not null default '' comment '绑定事件';
    update resource_tree_info set event_code='data_pub' where url='issue_data_add';
    update resource_tree_info set event_code='data_apply' where url='data_apply_add';
    
    -- 增加枚举管理
    CREATE TABLE `enum_info` (
      `id` bigint NOT NULL AUTO_INCREMENT,
      `enum_code` varchar(512) DEFAULT NULL COMMENT '枚举标识',
      `enum_context` varchar(200) DEFAULT NULL COMMENT '枚举说明',
      `enum_type` varchar(100) DEFAULT NULL COMMENT '枚举类型',
      `enum_json` text comment '枚举明细',
      `owner` varchar(64) default null COMMENT '拥有者',
      `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
      `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
      `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
   
    INSERT INTO resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(934820298253930496, '802848818109353984', '枚举管理', '2', '1', 'fa fa-cubes', '', '10', '1', '2022-01-23 14:42:07', '2022-01-23 14:42:41', 'enum_index', '2', '', '');
    INSERT INTOresource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(934820739683454976, '934820298253930496', '查询', '3', '1', 'fa fa-coffee', '', '1', '1', '2022-01-23 14:43:52', '2022-01-23 14:43:52', 'enum_list', '5', '', '');
    INSERT INTO resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(934899005698084864, '934820298253930496', '新增页面', '3', '1', 'fa fa-coffee', '', '2', '1', '2022-01-23 19:54:53', '2022-01-23 19:54:53', 'enum_add_index', '3', '', '');
    INSERT INTO resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(934899134614212608, '934820298253930496', '新增', '3', '1', 'fa fa-coffee', '', '3', '1', '2022-01-23 19:55:23', '2022-01-23 19:55:23', 'enum_add', '5', '', '');
    INSERT INTO resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(934899250242785280, '934820298253930496', '删除', '3', '1', 'fa fa-coffee', '', '4', '1', '2022-01-23 19:55:51', '2022-01-23 19:55:51', 'enum_delete', '5', '', '');
    INSERT INTO resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(934899352286007296, '934820298253930496', '更新', '3', '1', 'fa fa-coffee', '', '5', '1', '2022-01-23 19:56:15', '2022-01-23 19:56:15', 'enum_update', '5', '', '');
    INSERT INTO resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(934926419119575040, '934820298253930496', '明细', '3', '1', 'fa fa-coffee', '', '6', '1', '2022-01-23 21:43:48', '2022-01-23 21:43:48', 'enum_detail', '5', '', '');

    CREATE TABLE `param_info` (
      `id` bigint NOT NULL AUTO_INCREMENT,
      `param_name` varchar(512) DEFAULT NULL COMMENT '参数名称',
      `param_value` text COMMENT '参数名称',
      `param_context` varchar(200) DEFAULT NULL COMMENT '参数说明',
      `param_type` varchar(100) DEFAULT NULL COMMENT '参数类型',
      `param_timeout` varchar(200) DEFAULT NULL COMMENT '缓存超时时间,单位秒',
      `owner` varchar(64) default null COMMENT '拥有者',
      `status` varchar(64) default null COMMENT '状态,启用:on, 关闭:off',
      `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
      `create_time` timestamp  DEFAULT NULL COMMENT '创建时间',
      `update_time` timestamp  DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
    
    
    ALTER USER 'zyc'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
    ALTER USER 'zyc'@'127.0.0.1' IDENTIFIED WITH mysql_native_password BY '123456';
    
    FLUSH PRIVILEGES;
    
    alter table issue_data_info add column label_params text  comment '数据标签,多个逗号分割';
    
    CREATE TABLE `data_tag_info` (
      `id` bigint NOT NULL AUTO_INCREMENT,
      `tag_code` varchar(512) DEFAULT NULL COMMENT '标识code',
      `tag_name` varchar(200) DEFAULT NULL COMMENT '标识名称',
      `product_code` varchar(100) DEFAULT NULL COMMENT '产品code',
      `owner` varchar(64) default null COMMENT '拥有者',
      `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
      `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
      `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
    
    CREATE TABLE `product_tag_info` (
      `id` bigint NOT NULL AUTO_INCREMENT,
      `product_code` varchar(512) DEFAULT NULL COMMENT '产品标识code',
      `product_name` varchar(200) DEFAULT NULL COMMENT '产品名称',
      `owner` varchar(64) default null COMMENT '拥有者',
      `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
      `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
      `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
    
    CREATE TABLE `data_tag_group_info` (
      `id` bigint NOT NULL AUTO_INCREMENT,
      `tag_group_code` varchar(512) DEFAULT NULL COMMENT '标识组code',
      `tag_group_name` varchar(200) DEFAULT NULL COMMENT '标识组名称',
      `tag_codes` varchar(512) DEFAULT NULL COMMENT '标识code列表,逗号分割',
      `product_code` varchar(100) DEFAULT NULL COMMENT '产品code',
      `owner` varchar(64) default null COMMENT '拥有者',
      `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
      `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
      `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
    
    alter table account_info add column tag_group_code varchar(512) not null default ''  comment '数据组标识,多个逗号分割';
    alter table data_sources_info add column tag_group_code varchar(512) not null default ''  comment '数据组标识,多个逗号分割';
    
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(934940905796800512, '802848818109353984', 'WEMOCK', '2', '1', 'fa fa-cube', '', '150', '1', '2022-01-23 22:41:22', '2022-01-23 22:41:22', 'abc.html', '2', '开发中', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(936723230184443904, '802919157430489088', 'SparkSql查询表结构', '4', '1', 'fa fa-coffee', '', '7', '1', '2022-01-28 20:43:42', '2022-01-28 20:43:42', 'desc_table', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(936943441596649472, '926763179978002432', '参数配置', '3', '1', 'fa fa-diamond', '', '4', '1', '2022-01-29 11:18:44', '2022-01-29 11:18:44', 'param_index', '2', '管理员', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(936943543832809472, '936943441596649472', '查询', '4', '1', 'fa fa-coffee', '', '1', '1', '2022-01-29 11:19:08', '2022-01-29 11:19:08', 'param_list', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(936943806161358848, '936943441596649472', '更新页面', '4', '1', 'fa fa-coffee', '', '2', '1', '2022-01-29 11:20:11', '2022-01-29 11:20:11', 'param_add_index', '3', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(936943897400053760, '936943441596649472', '新增', '4', '1', 'fa fa-coffee', '', '3', '1', '2022-01-29 11:20:33', '2022-01-29 11:20:33', 'param_add', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(936943979792961536, '936943441596649472', '更新', '4', '1', 'fa fa-coffee', '', '4', '1', '2022-01-29 11:20:52', '2022-01-29 11:20:52', 'param_update', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(936944089763418112, '936943441596649472', '删除', '4', '1', 'fa fa-coffee', '', '5', '1', '2022-01-29 11:21:19', '2022-01-29 11:21:19', 'param_delete', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(936956093823717376, '936943441596649472', '查询明细', '4', '1', 'fa fa-coffee', '', '6', '1', '2022-01-29 12:09:01', '2022-01-29 12:09:01', 'param_detail', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(937123552732123136, '810817759893000192', '数据资产', '3', '1', 'fa fa-coffee', '', '4', '1', '2022-01-29 23:14:26', '2022-01-29 23:14:26', 'data_ware_house_index_plus', '2', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938782846959489024, '802932548165439488', '功能权限', '3', '1', 'fa fa-cog', '', '1', '1', '2022-02-03 13:07:52', '2022-02-03 13:08:07', '', '1', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938783170151583744, '802932548165439488', '数据权限', '3', '1', 'fa fa-cog', '', '2', '1', '2022-02-03 13:09:10', '2022-02-03 13:09:10', '', '1', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938783478181269504, '938783170151583744', '数据标识', '4', '1', 'fa fa-cog', '', '1', '1', '2022-02-03 13:10:23', '2022-02-03 13:11:33', 'data_tag_index', '2', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938783882944188416, '938783478181269504', '查询', '5', '1', 'fa fa-coffee', '', '1', '1', '2022-02-03 13:11:59', '2022-02-03 13:11:59', 'data_tag_list', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938784014171377664, '938783478181269504', '新增页面', '5', '1', 'fa fa-coffee', '', '2', '1', '2022-02-03 13:12:31', '2022-02-03 13:12:31', 'data_tag_add_index', '3', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938784115174412288, '938783478181269504', '新增', '5', '1', 'fa fa-coffee', '', '3', '1', '2022-02-03 13:12:55', '2022-02-03 13:12:55', 'data_tag_add', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938784216877895680, '938783478181269504', '更新', '5', '1', 'fa fa-coffee', '', '4', '1', '2022-02-03 13:13:19', '2022-02-03 13:13:19', 'data_tag_update', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938784333840257024, '938783478181269504', '删除', '5', '1', 'fa fa-coffee', '', '5', '1', '2022-02-03 13:13:47', '2022-02-03 13:13:47', 'data_tag_delete', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938785006883442688, '938783170151583744', '产品标识', '4', '1', 'fa fa-cog', '', '2', '1', '2022-02-03 13:16:27', '2022-02-03 13:19:15', 'product_tag_index', '2', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938785227138928640, '938785006883442688', '查询', '5', '1', 'fa fa-coffee', '', '1', '1', '2022-02-03 13:17:20', '2022-02-03 13:17:20', 'product_tag_list', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938785661765292032, '938785006883442688', '新增页面', '5', '1', 'fa fa-coffee', '', '2', '1', '2022-02-03 13:19:04', '2022-02-03 13:19:04', 'product_tag_add_index', '3', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938785831097733120, '938785006883442688', '新增', '5', '1', 'fa fa-coffee', '', '3', '1', '2022-02-03 13:19:44', '2022-02-03 13:19:44', 'product_tag_add', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938785942548779008, '938785006883442688', '更新', '5', '1', 'fa fa-coffee', '', '4', '1', '2022-02-03 13:20:11', '2022-02-03 13:20:11', 'product_tag_update', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938786051592294400, '938785006883442688', '删除', '5', '1', 'fa fa-coffee', '', '5', '1', '2022-02-03 13:20:37', '2022-02-03 13:20:37', 'product_tag_delete', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938879140692496384, '938783478181269504', '明细', '5', '1', 'fa fa-coffee', '', '6', '1', '2022-02-03 19:30:31', '2022-02-03 19:30:31', 'data_tag_detail', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(938879269428269056, '938785006883442688', '明细', '5', '1', 'fa fa-coffee', '', '6', '1', '2022-02-03 19:31:01', '2022-02-03 19:31:01', 'product_tag_detail', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(939102606079299584, '938783170151583744', '标识组', '4', '1', 'fa fa-cog', '', '3', '1', '2022-02-04 10:18:29', '2022-02-04 10:18:29', 'data_tag_group_index', '2', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(939102720999034880, '939102606079299584', '查询', '5', '1', 'fa fa-coffee', '', '1', '1', '2022-02-04 10:18:56', '2022-02-04 10:18:56', 'data_tag_group_list', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(939102954936340480, '939102606079299584', '新增页面', '5', '1', 'fa fa-coffee', '', '2', '1', '2022-02-04 10:19:52', '2022-02-04 10:19:52', 'data_tag_group_add_index', '3', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(939103070598467584, '939102606079299584', '新增', '5', '1', 'fa fa-coffee', '', '3', '1', '2022-02-04 10:20:20', '2022-02-04 10:20:20', 'data_tag_group_add', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(939103467824222208, '939102606079299584', '更新', '5', '1', 'fa fa-coffee', '', '4', '1', '2022-02-04 10:21:54', '2022-02-04 10:21:54', 'data_tag_group_update', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(939103572845400064, '939102606079299584', '删除', '5', '1', 'fa fa-coffee', '', '5', '1', '2022-02-04 10:22:19', '2022-02-04 10:22:19', 'data_tag_group_delete', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(939103663475920896, '939102606079299584', '明细', '5', '1', 'fa fa-coffee', '', '6', '1', '2022-02-04 10:22:41', '2022-02-04 10:22:41', 'data_tag_group_detail', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(939113343006806016, '938783478181269504', '产品code查询', '5', '1', 'fa fa-coffee', '', '7', '1', '2022-02-04 11:01:09', '2022-02-04 11:01:09', 'data_tag_by_product_code', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(939830350585008128, '802918652050411520', '查询数据组标识', '3', '1', 'fa fa-coffee', '', '10', '1', '2022-02-06 10:30:17', '2022-02-06 10:30:17', 'user_tag_group_code', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(939848526425231360, '936943441596649472', '同步redis', '4', '1', 'fa fa-coffee', '', '7', '1', '2022-02-06 11:42:30', '2022-02-06 11:42:30', 'param_to_redis', '5', '', '');
# 4.7.18迁移5.0.0

    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(942052856712663040, '937123552732123136', '查询', '4', '1', 'fa fa-coffee', '', '1', '1', '2022-02-12 13:41:44', '2022-02-12 13:41:44', 'data_ware_house_list6', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(942052978712383488, '937123552732123136', '标签查询', '4', '1', 'fa fa-coffee', '', '2', '1', '2022-02-12 13:42:13', '2022-02-12 13:42:13', 'data_ware_house_label', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(942053081317642240, '937123552732123136', '样本数据查询', '4', '1', 'fa fa-coffee', '', '3', '1', '2022-02-12 13:42:37', '2022-02-12 13:43:08', 'data_ware_house_sample', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(942053175202942976, '937123552732123136', '样本数据导出', '4', '1', 'fa fa-coffee', '', '4', '1', '2022-02-12 13:42:59', '2022-02-12 13:42:59', 'data_ware_house_export', '5', '', '');

    CREATE TABLE `etl_task_unstructure_info` (
      `id` bigint NOT NULL AUTO_INCREMENT,
      `unstructure_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
      `project_id` varchar(100) DEFAULT NULL,
      `data_sources_choose_file_input` varchar(100) DEFAULT null comment '输入数据源',
      `input_path` varchar(512) DEFAULT null comment '文件读取路径',
      `data_sources_choose_file_output` varchar(100) DEFAULT NULL,
      `data_sources_choose_jdbc_output` varchar(100) DEFAULT NULL,
      `output_path` varchar(512) DEFAULT null comment '文件写入路径',
      `etl_sql` text,
      `unstructure_params_output` varchar(500) DEFAULT NULL,
      `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
      `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
      `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
      `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
      `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
      `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
      `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
      `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

    CREATE TABLE `etl_task_unstructure_log_info` (
      `id` bigint NOT NULL AUTO_INCREMENT,
      `unstructure_id` varchar(100) DEFAULT null comment '非结构化任务ID',
      `unstructure_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
      `project_id` varchar(100) DEFAULT NULL,
      `data_sources_choose_file_input` varchar(100) DEFAULT null comment '输入数据源',
      `input_path` varchar(512) DEFAULT null comment '文件读取路径',
      `data_sources_choose_file_output` varchar(100) DEFAULT NULL,
      `data_sources_choose_jdbc_output` varchar(100) DEFAULT NULL,
      `output_path` varchar(512) DEFAULT null comment '文件写入路径',
      `etl_sql` text,
      `unstructure_params_output` varchar(500) DEFAULT NULL,
      `msg` text comment '执行日志',
      `status` varchar(64) default null COMMENT '状态,true/false',
      `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
      `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
      `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
      `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
      `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
      `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
      `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
    
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(943266657919307776, '802852358580080640', '非结构化采集', '3', '1', 'non', '', '11', '1', '2022-02-15 22:04:56', '2022-02-15 22:05:37', 'etl_task_unstructure_index', '2', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(943266905408409600, '943266657919307776', '查询', '4', '1', 'fa fa-coffee', '', '1', '1', '2022-02-15 22:05:55', '2022-02-15 22:05:55', 'etl_task_unstructure_list', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(943267015571804160, '943266657919307776', '新增页面', '4', '1', 'fa fa-coffee', '', '2', '1', '2022-02-15 22:06:22', '2022-02-15 22:06:22', 'etl_task_unstructure_add_index', '3', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(943267126561476608, '943266657919307776', '新增', '4', '1', 'fa fa-coffee', '', '3', '1', '2022-02-15 22:06:48', '2022-02-15 22:06:48', 'etl_task_unstructure_add', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(943267228420149248, '943266657919307776', '更新', '4', '1', 'fa fa-coffee', '', '4', '1', '2022-02-15 22:07:12', '2022-02-15 22:07:12', 'etl_task_unstructure_update', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(943267317154844672, '943266657919307776', '删除', '4', '1', 'fa fa-coffee', '', '5', '1', '2022-02-15 22:07:33', '2022-02-15 22:07:33', 'etl_task_unstructure_delete', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(943633847528984576, '943266657919307776', '上传文件页面', '4', '1', 'fa fa-coffee', '', '6', '1', '2022-02-16 22:24:01', '2022-02-16 22:24:01', 'etl_task_unstructure_upload_index', '3', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(943633913723490304, '943266657919307776', '上传文件', '4', '1', 'fa fa-coffee', '', '7', '1', '2022-02-16 22:24:17', '2022-02-16 22:24:17', 'etl_task_unstructure_upload', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(944568364485840896, '943266657919307776', '日志页面', '4', '1', 'fa fa-coffee', '', '8', '1', '2022-02-19 12:17:27', '2022-02-19 12:17:27', 'etl_task_unstructure_log_index', '3', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(944568478025650176, '943266657919307776', '日志查询', '4', '1', 'fa fa-coffee', '', '9', '1', '2022-02-19 12:17:54', '2022-02-19 12:17:54', 'etl_task_unstructure_log_list', '5', '', '');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code)
    VALUES(944718135569682432, '943266657919307776', '日志删除', '4', '1', 'fa fa-coffee', '', '10', '1', '2022-02-19 22:12:36', '2022-02-19 22:12:36', 'etl_task_unstructure_log_delete', '5', '', '');

    alter table resource_tree_info add column product_code varchar(100) not null default ''  comment '产品code';
    
    update resource_tree_info set product_code='zdh';
    
    alter table role_info add column product_code varchar(100) not null default ''  comment '产品code';
    
    update role_info set product_code='zdh';

    INSERT INTO zdh.resource_tree_info
    (id, parent, text, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
    VALUES(958858421291978752, '930966518835974144', '查询系统任务', '4', '1', 'fa fa-coffee', '', '3', '1', '2022-03-30 22:41:02', '2022-03-30 22:41:02', 'dispatch_system_task_list', '5', '', '', 'zdh');
    INSERT INTO zdh.resource_tree_info
    (id, parent, text, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
    VALUES(958858522265653248, '930966518835974144', '启用系统任务', '4', '1', 'fa fa-coffee', '', '4', '1', '2022-03-30 22:41:26', '2022-03-30 22:41:26', 'dispatch_system_task_create', '5', '', '', 'zdh');
    INSERT INTO zdh.resource_tree_info
    (id, parent, text, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
    VALUES(958858665077510144, '930966518835974144', '禁用系统任务', '4', '1', 'fa fa-coffee', '', '5', '1', '2022-03-30 22:42:00', '2022-03-30 22:42:00', 'dispatch_system_task_delete', '5', '', '', 'zdh');

    alter table user_operate_log add column `time` varchar(100) not null default ''  comment '请求响应耗时';
   
    alter table QRTZ_SCHEDULER_STATE add column RUNNING varchar(16) not null default '' comment '正在执行中的任务数';
  
    alter table approval_event_info add column `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间'
    alter table approval_event_info add column `skip_account` text  comment '可跳过审批的用户';
    
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
    VALUES(960131981750833152, '930966518835974144', '新增系统任务页面', '4', '1', 'fa fa-coffee', '', '6', '1', '2022-04-03 11:01:43', '2022-04-03 11:02:13', 'dispatch_system_task_add_index', '3', '', '', 'zdh');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
    VALUES(960132057952948224, '930966518835974144', '新增系统任务', '4', '1', 'fa fa-coffee', '', '7', '1', '2022-04-03 11:02:01', '2022-04-03 11:02:01', 'dispatch_system_task_add', '5', '', '', 'zdh');

    alter table blood_source_info add column `input_json` text  comment '输入源配置';
    alter table blood_source_info add column `output_json` text  comment '输出源配置';
    
    INSERT INTO zdh.param_info
    (id, param_name, param_value, param_context, param_type, param_timeout, owner, status, is_delete, create_time, update_time)
    VALUES(4, 'zdh_is_pass', 'false', '系统访问控制', '1', '120', '1', 'on', '0', '2022-04-05 18:27:30', '2022-04-05 19:57:47');

    ALTER TABLE apply_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE apply_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    
    ALTER TABLE approval_auditor_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    
    ALTER TABLE approval_config_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    
    ALTER TABLE approval_event_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE approval_event_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE blood_source_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    
    ALTER TABLE data_tag_group_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE data_tag_group_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    
    ALTER TABLE data_tag_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE data_tag_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE enum_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE enum_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE etl_task_update_logs MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE issue_data_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    
    ALTER TABLE jar_file_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    
    ALTER TABLE jar_task_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    
    ALTER TABLE notice_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE notice_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE param_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE param_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    
    ALTER TABLE process_flow_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    
    ALTER TABLE product_tag_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE product_tag_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE quality MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    
    ALTER TABLE quality_rule_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE quality_rule_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE quality_task_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE quality_task_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE quartz_executor_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE quartz_executor_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE quartz_job_info MODIFY COLUMN start_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '开始时间';
    ALTER TABLE quartz_job_info MODIFY COLUMN end_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '结束时间';
    ALTER TABLE quartz_job_info MODIFY COLUMN last_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '上次调度时间';
    ALTER TABLE quartz_job_info MODIFY COLUMN next_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '下次调度时间';
    ALTER TABLE quartz_job_info MODIFY COLUMN quartz_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例触发时间';
    
    ALTER TABLE resource_tree_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE resource_tree_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE role_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE role_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE role_resource_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE role_resource_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE server_task_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE server_task_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE server_task_instance MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE server_task_instance MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE task_group_log_instance MODIFY COLUMN cur_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例逻辑调度时间';
    ALTER TABLE task_group_log_instance MODIFY COLUMN run_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例开始执行时间';
    ALTER TABLE task_group_log_instance MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例更新时间';
    ALTER TABLE task_group_log_instance MODIFY COLUMN start_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '开始时间';
    ALTER TABLE task_group_log_instance MODIFY COLUMN end_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '结束时间';
    ALTER TABLE task_group_log_instance MODIFY COLUMN last_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '上次调度时间';
    ALTER TABLE task_group_log_instance MODIFY COLUMN next_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '下次调度时间';
    ALTER TABLE task_group_log_instance MODIFY COLUMN quartz_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例触发时间';
    
    ALTER TABLE task_log_instance MODIFY COLUMN cur_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例逻辑调度时间';
    ALTER TABLE task_log_instance MODIFY COLUMN run_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例开始执行时间';
    ALTER TABLE task_log_instance MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例更新时间';
    ALTER TABLE task_log_instance MODIFY COLUMN start_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '开始时间';
    ALTER TABLE task_log_instance MODIFY COLUMN end_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '结束时间';
    ALTER TABLE task_log_instance MODIFY COLUMN last_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '上次调度时间';
    ALTER TABLE task_log_instance MODIFY COLUMN next_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '下次调度时间';
    ALTER TABLE task_log_instance MODIFY COLUMN quartz_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例触发时间';
    
    ALTER TABLE user_group_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE user_group_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    
    ALTER TABLE user_operate_log MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    ALTER TABLE user_operate_log MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';
    
    ALTER TABLE zdh_download_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
    
    ALTER TABLE zdh_logs MODIFY COLUMN log_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '日志生成时间';
  
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
    VALUES(962422175220895744, '937123552732123136', '申请人查询', '4', '1', 'fa fa-coffee', '', '5', '1', '2022-04-09 18:42:08', '2022-04-09 18:42:08', 'data_ware_house_apply', '5', '', '', 'zdh');


# 未完成的功能
  + v4.7.x 增加数据源共享功能(组内共享,单成员共享,为血缘分析做基础) 开发中
  + v4.7.x 接入flink引擎,实现流采集 已完成
  + v4.8 优化滚动升级部分 开发中
  + v4.8 计划支持血缘分析 已完成(4.7.x版本以支持)
  
  

 # 支持的数据源
   + 本地文件
   + hive(单集群使用多个远程hive,以及内外部表)
   + hdfs(csv,txt,json,orc,parquet,avro)
   + jdbc (所有的jdbc,包含特殊jdbc如hbase-phoenix,spark-jdbc,click-house,greenplum,presto,mariadb,memsql,华为dws,阿里AnalyticDB,kylin,gbase,kingbase,redshift)
   + hbase
   + mongodb
   + es
   + kafka
   + http
   + sftp
   + cassandra
   + redis
   + flume
   + greenplum
   + kudu
   + tidb

# 支持的调度对象
   + shell 命令
   + 数据库查询
   + 特色开发jar
   
# 支持的调度器模式
   + 时间序列(时间限制,次数限制)
   + 单次执行
   
# 支持调度动态日期参数   
   详见说明文档
 


# 版本计划
  + 1.1 计划支持FTP 调度
  + 1.1 计划支持HFILE 直接读取功能
  + 1.1 docker 部署
  + 2.X 单任务多数据源处理
  + 5.X 计划支持kerberos 认证
    

 
# 个人联系方式
    邮件：1209687056@qq.com
    
    
# 界面预览

![登陆界面](img/login.jpg)

![功能预览](img/index.jpg)

![数据源界面](img/sources_list.jpg)

![增加数据源界面](img/sources_add.jpg)

![ETL任务界面](img/etl_list.jpg)

![ETL任务配置界面](img/etl_add.jpg)

![调度任务界面](img/dispatch_list.jpg)

![调度任务配置界面](img/dispatch_add.jpg)
 
 
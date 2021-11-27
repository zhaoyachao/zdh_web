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
   
   +  [zdh_web_4.7.11](http://zycblog.cn:8080/zdh/download/4.7.11/zdh_web.tar)
   +  [zdh_server_4.7.11](http://zycblog.cn:8080/zdh/download/4.7.11/zdh_server.tar)
   +  [zdh_flink_4.7.11](http://zycblog.cn:8080/zdh/download/4.7.11/zdh_flink.tar)
   
   +  [zdh_web_4.7.12](http://zycblog.cn:8080/zdh/download/4.7.12/zdh_web.tar)
   +  [zdh_server_4.7.12](http://zycblog.cn:8080/zdh/download/4.7.12/zdh_server.tar)
   +  [zdh_flink_4.7.12](http://zycblog.cn:8080/zdh/download/4.7.12/zdh_flink.tar)   
    
   + 如果链接失效,可通过邮件方式(见底部)通知作者,作者会通过邮件发送编译包

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
    在target 目录下找到zdh_web.jar
    执行 java  -Dfile.encoding=utf-8 -jar zdh_web.jar  

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
  
  
  
  
  
# 未完成的功能
  + v4.7.x 增加数据源共享功能(组内共享,单成员共享,为血缘分析做基础) 开发中
  + v4.7.x 接入flink引擎,实现流采集 已完成
  + v4.8 优化滚动升级部分 开发中
  + v4.8 计划支持血缘分析 开发中
  
  

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
 
 
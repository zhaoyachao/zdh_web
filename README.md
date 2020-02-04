# 主要功能
 zdh 主要的作用 是从hdfs,hive,jdbc 等数据源拉取数据,并转存到hdfs,hive,jdbc等其他数据源
 支持集群式部署
 
 
  + 支持sql标准函数
  + 支持界面选择配置
  + 支持外部调度工具(需要修改,新增特定接口)
  + 弹性扩展(可单机,可集群)
  + 支持客户级权限
 
# 用到的技术体系

    前端：Bootstrap
    后端：Springboot+shiro+redis+mybatis
    数据ETL引擎:Spark(hadoop,hive 可选择部署)
    

# 提示
   
    zdh 分2部分,前端配置+后端数据ETL处理,此部分只包含前端配置,有意者联系1299898281@qq.com
   
   

   
# 界面预览   

![login](img/login.jpg)

![sources](img/sources_list.jpg)

![sources](img/sources_add.jpg)

![etl](img/etl_list.jpg)

![etl](img/etl_add.jpg)

![dispatch](img/dispatch.jpg)
 
 
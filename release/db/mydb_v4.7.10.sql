-- MySQL dump 10.16  Distrib 10.2.14-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	10.2.14-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account_info`
--

DROP TABLE IF EXISTS `account_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `user_password` varchar(100) DEFAULT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `is_use_email` varchar(10) DEFAULT NULL COMMENT '是否开启邮箱告警',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `is_use_phone` varchar(10) DEFAULT NULL COMMENT '是否开启手机告警',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_info`
--

LOCK TABLES `account_info` WRITE;
/*!40000 ALTER TABLE `account_info` DISABLE KEYS */;
INSERT INTO `account_info` VALUES (1,'zyc','123456','1209687056@qq.com','on','',NULL),(2,'admin','123456','1209687056@qq.com',NULL,NULL,NULL);
/*!40000 ALTER TABLE `account_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `act2`
--

DROP TABLE IF EXISTS `act2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `act2` (
  `name` text DEFAULT NULL,
  `sex` text DEFAULT NULL,
  `job` text DEFAULT NULL,
  `addr` text DEFAULT NULL,
  `age` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `act2`
--

LOCK TABLES `act2` WRITE;
/*!40000 ALTER TABLE `act2` DISABLE KEYS */;
INSERT INTO `act2` VALUES ('zhaoyachao','man','it','北京','20'),('zhaoyachao1','man','it','北京','20'),('zhaoyachao2','man','it','北京','20'),('zhaoyachao3','man','it','北京','20'),('zhaoyachao4','man','it','北京','20'),('zhaoyachao5','man','it','北京','20'),('zhaoyachao6','man','it','北京','20'),('zhaoyachao7','man','it','北京','20'),('zhaoyachao8','man','it','北京','20');
/*!40000 ALTER TABLE `act2` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data_sources_info`
--

DROP TABLE IF EXISTS `data_sources_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_sources_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_source_context` varchar(100) DEFAULT NULL,
  `data_source_type` varchar(100) DEFAULT NULL,
  `driver` varchar(100) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `is_delete` varchar(10) NOT NULL DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_sources_info`
--

LOCK TABLES `data_sources_info` WRITE;
/*!40000 ALTER TABLE `data_sources_info` DISABLE KEYS */;
INSERT INTO `data_sources_info` VALUES (53,'mydb','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','2','0'),(54,'csv','HDFS','','','zyc@qq.com','123456','2','0'),(55,'mydb2','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','2','0'),(56,'HIVE1','HIVE','','','','','2','0'),(57,'第一个hive','HIVE','','','','','1','0'),(60,'zdh_test','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/zdh_test?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','1','0'),(61,'个人测试外部下载','外部下载','','','','','1','0'),(64,'第一个外部上传','外部上传','','','','','1','0'),(65,'第一个sftp','SFTP','','127.0.0.1:22','zyc','123456','1','0'),(66,'4123412341','JDBC','12341234','12341234132','wwh','123456','1','0'),(67,'aba','REDIS','','','','','1','0'),(68,'123445','SFTP','','','','','1','0'),(69,'11111','HDFS','','hdfs://ip:port, hadoop ha example: ip1:port1,ip2:port2,ip3:port3/clustername','','','1','0'),(70,'xxx','KAFKA','','','zyc','123456','1','0'),(72,'测试001','外部上传','','','','','1','0'),(73,'测试002','外部下载','','','','','1','0'),(74,'test','KAFKA','','','','','1','0'),(75,'jdbc_12','JDBC','com.mysql.cj.jdbc.Driver','j','18559756159','1','1','0');
/*!40000 ALTER TABLE `data_sources_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data_sources_type_info`
--

DROP TABLE IF EXISTS `data_sources_type_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_sources_type_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sources_type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_sources_type_info`
--

LOCK TABLES `data_sources_type_info` WRITE;
/*!40000 ALTER TABLE `data_sources_type_info` DISABLE KEYS */;
INSERT INTO `data_sources_type_info` VALUES (1,'JDBC'),(2,'HDFS'),(3,'HBASE'),(4,'MONGODB'),(5,'ES'),(6,'HIVE'),(7,'KAFKA'),(8,'HTTP'),(9,'REDIS'),(10,'CASSANDRA'),(11,'SFTP'),(12,'KUDU'),(13,'外部上传'),(14,'FLUME'),(15,'外部下载'),(16,'TIDB');
/*!40000 ALTER TABLE `data_sources_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dispatch_task_info`
--

DROP TABLE IF EXISTS `dispatch_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dispatch_task_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dispatch_context` varchar(100) DEFAULT NULL,
  `etl_task_id` varchar(100) DEFAULT NULL,
  `etl_context` varchar(100) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispatch_task_info`
--

LOCK TABLES `dispatch_task_info` WRITE;
/*!40000 ALTER TABLE `dispatch_task_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dispatch_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etl_drools_task_info`
--

DROP TABLE IF EXISTS `etl_drools_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `etl_drools_task_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `etl_id` varchar(200) DEFAULT NULL COMMENT '任务id',
  `etl_drools` text DEFAULT NULL COMMENT 'drools任务逻辑',
  `data_sources_filter_input` varchar(100) DEFAULT NULL COMMENT '数据源输入过滤条件',
  `data_sources_choose_output` varchar(100) DEFAULT NULL COMMENT '输出数据源id',
  `data_source_type_output` varchar(100) DEFAULT NULL COMMENT '输出数据源类型',
  `data_sources_table_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源表名',
  `data_sources_file_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源文件名',
  `data_sources_params_output` varchar(500) DEFAULT NULL COMMENT '输出数据源参数',
  `data_sources_clear_output` varchar(500) DEFAULT NULL COMMENT '数据源数据源删除条件',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `drop_tmp_tables` varchar(500) DEFAULT NULL COMMENT '删除临时表名',
  `file_type_output` varchar(10) DEFAULT NULL COMMENT '输出文件类型',
  `encoding_output` varchar(10) DEFAULT NULL COMMENT '输出文件编码',
  `sep_output` varchar(10) DEFAULT NULL COMMENT '输出文件分割符',
  `header_output` varchar(10) DEFAULT NULL COMMENT '输出文件是否带有表头',
  `more_task` varchar(100) DEFAULT NULL COMMENT 'ETL任务类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=749279630074056705 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_drools_task_info`
--

LOCK TABLES `etl_drools_task_info` WRITE;
/*!40000 ALTER TABLE `etl_drools_task_info` DISABLE KEYS */;
INSERT INTO `etl_drools_task_info` VALUES (2,'第一个drools','719619870378954752','package rules\r\nimport java.util.HashMap\r\nrule \"alarm2\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") != \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"false\");\r\nend\r\n\r\nrule \"alarm\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") == \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"true\");\r\nend','dsts=true','60','JDBC','d1','d1','','drop table d1','1','2020-07-24 20:38:41',NULL,'','','',NULL,'单源ETL'),(749258437296132096,'hive->mysql->drools','718814940856586240','package rules\r\nimport java.util.HashMap\r\nrule \"alarm2\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") != \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"false\");\r\nend\r\n\r\nrule \"alarm\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") == \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"true\");\r\nend','','60','JDBC','act2','act2','','drop table act2','1','2020-08-28 21:25:32',NULL,'','','',NULL,'SQL'),(749279630074056704,'more_test_account_info','2','package rules\r\nimport java.util.HashMap\r\nrule \"alarm2\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") != \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"false\");\r\nend\r\n\r\nrule \"alarm\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") == \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"true\");\r\nend','','60','JDBC','act2','act2','','drop table act2','1','2020-08-28 22:49:45',NULL,'','','',NULL,'多源ETL');
/*!40000 ALTER TABLE `etl_drools_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etl_more_task_info`
--

DROP TABLE IF EXISTS `etl_more_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `etl_more_task_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `etl_ids` varchar(200) DEFAULT NULL COMMENT '任务id列表',
  `etl_sql` text DEFAULT NULL COMMENT '多源任务逻辑',
  `data_sources_choose_output` varchar(100) DEFAULT NULL COMMENT '输出数据源id',
  `data_source_type_output` varchar(100) DEFAULT NULL COMMENT '输出数据源类型',
  `data_sources_table_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源表名',
  `data_sources_file_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源文件名',
  `data_sources_params_output` varchar(500) DEFAULT NULL COMMENT '输出数据源参数',
  `data_sources_clear_output` varchar(500) DEFAULT NULL COMMENT '数据源数据源删除条件',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `drop_tmp_tables` varchar(500) DEFAULT NULL COMMENT '删除临时表名',
  `file_type_output` varchar(10) DEFAULT NULL COMMENT '输出文件类型',
  `encoding_output` varchar(10) DEFAULT NULL COMMENT '输出文件编码',
  `sep_output` varchar(10) DEFAULT NULL COMMENT '输出文件分割符',
  `header_output` varchar(10) DEFAULT NULL COMMENT '输出文件是否带有表头',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_more_task_info`
--

LOCK TABLES `etl_more_task_info` WRITE;
/*!40000 ALTER TABLE `etl_more_task_info` DISABLE KEYS */;
INSERT INTO `etl_more_task_info` VALUES (1,'ddd','719619870378954752,719629297702146048','','59','HDFS','','','','','1','2020-06-08 06:19:59','','csv','UTF-8','|',NULL),(2,'more_mydb_account_info','749279343477264384','select * from account_info','','','','','','','1','2020-08-28 22:49:19','','','','',NULL);
/*!40000 ALTER TABLE `etl_more_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etl_task_info`
--

DROP TABLE IF EXISTS `etl_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `etl_task_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
  `data_sources_table_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源表名',
  `data_sources_file_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源文件名',
  `data_sources_file_columns` text DEFAULT NULL COMMENT '输入数据源文件列名',
  `data_sources_table_columns` text DEFAULT NULL COMMENT '输入数据源表列名',
  `data_sources_params_input` varchar(500) DEFAULT NULL COMMENT '输入数据源参数',
  `data_sources_filter_input` varchar(500) DEFAULT NULL COMMENT '输入数据源过滤条件',
  `data_sources_choose_output` varchar(100) DEFAULT NULL COMMENT '输出数据源id',
  `data_source_type_output` varchar(100) DEFAULT NULL COMMENT '输出数据源类型',
  `data_sources_table_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源表名',
  `data_sources_file_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源文件名',
  `data_sources_params_output` varchar(500) DEFAULT NULL COMMENT '输出数据源参数',
  `column_datas` text DEFAULT NULL COMMENT '输入输出自定映射关系',
  `data_sources_clear_output` varchar(500) DEFAULT NULL COMMENT '数据源数据源删除条件',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
  `column_size` varchar(100) DEFAULT NULL COMMENT '字段个数',
  `rows_range` varchar(100) DEFAULT NULL COMMENT '行数范围',
  `error_rate` varchar(10) DEFAULT NULL COMMENT '错误率',
  `enable_quality` varchar(10) DEFAULT NULL COMMENT '是否开启质量检测',
  `duplicate_columns` varchar(200) DEFAULT NULL COMMENT '去重字段',
  `primary_columns` varchar(100) DEFAULT NULL COMMENT '不可重复字段',
  `file_type_input` varchar(10) DEFAULT NULL COMMENT '输入文件类型',
  `encoding_input` varchar(10) DEFAULT NULL COMMENT '输入文件编码',
  `sep_input` varchar(10) DEFAULT NULL COMMENT '输入分割符',
  `file_type_output` varchar(10) DEFAULT NULL COMMENT '输出文件类型',
  `encoding_output` varchar(10) DEFAULT NULL COMMENT '输出文件编码',
  `sep_output` varchar(10) DEFAULT NULL COMMENT '输出文件分割符',
  `header_input` varchar(10) DEFAULT NULL COMMENT '输入是否包含表头',
  `header_output` varchar(10) DEFAULT NULL COMMENT '输出是否包含表头',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=835512027848904705 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_task_info`
--

LOCK TABLES `etl_task_info` WRITE;
/*!40000 ALTER TABLE `etl_task_info` DISABLE KEYS */;
INSERT INTO `etl_task_info` VALUES (719619870378954752,'单分割符无标题','59','HDFS','','/data/csv/t1.txt','name,sex,job,addr,age','','','','60','JDBC','t1','t1','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_desc\":\"姓名\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','drop table t1','1','2020-06-08 02:32:27','','','','','','1-100','',NULL,'','','csv','GBK','|','','','','false',''),(719629297702146048,'单分割符自带标题','59','HDFS','','/data/csv/h1.txt','','','','','60','JDBC','h1','h1','','[]','delete from h1','1','2020-06-08 03:09:55','','','','','','','',NULL,'','','csv','GBK','|','','','','true',''),(719630143433216000,'多分割符无标题','59','HDFS','','/data/csv/t2.txt','name,sex,job,addr,age','','','','60','JDBC','t2','t2','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"},{\"column_md5\":\"462-4b77-a7ac-48\",\"column_name\":\"\",\"column_expr\":\"$zdh_etl_date\",\"column_type\":\"string\",\"column_alias\":\"etl_date\"}]','drop table t2','1','2020-06-08 03:13:16','','','','','','','',NULL,'','','csv','GBK','|+','','','',NULL,NULL),(719630908637843456,'tab分割无标题','59','HDFS','','/data/csv/t3.txt','name,sex,job,addr,age','','{\"sep\":\"\",\"encoding\":\"GBK\"}','','60','JDBC','t3','t3','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','delete from t3','1','2020-06-08 03:16:19','','','','','','','',NULL,'','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(724291182208749568,'单分割符无标题输出hudi','59','HDFS','','/data/csv/t1.txt','name,sex,job,addr,age','','','','59','HDFS','','/data/hudi/t1','{\"precombine_field_opt_key\":\"name\",\"recordkey_field_opt_key\":\"name\"}','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','','1','2020-06-20 23:54:35','','','','','','1-100','',NULL,'','','csv','GBK','|','hudi','','',NULL,NULL),(724312011898359808,'hudi输出多分割符无标题','59','HDFS','','/data/hudi/t1','name,sex,job,addr,age','','','','59','HDFS','','/data/csv/hudi_t1','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','','1','2020-06-21 01:17:21','','','','','','1-100','',NULL,'','','hudi','','','csv','UTF-8','+-',NULL,NULL),(728251791409418240,'kafka接收客户信息存mydb','62','KAFKA','','m1','name,age','','','','60','JDBC','m1','m1','','[{\"column_md5\":\"2c5-46c3-b4e7-96\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"a82-409a-b944-e2\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','','1','2020-07-01 22:12:37','','','','','','','',NULL,'','','','',',','','','','',''),(728647407415332864,'单分割符无标题2','59','HDFS','','/data/csv/t4.txt','name,sex,job,addr,age','','','','60','JDBC','t4','t4','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','delete from t4','1','2020-07-03 00:24:40','','','','','','1-100','',NULL,'','','csv','UTF-8','\\\\','','','','false',''),(728996795115376640,'单分割符无标题3','59','HDFS','','/data/csv/t5.txt','name,sex,job,addr,age','','','','60','JDBC','t5','t5','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','delete from t5','1','2020-07-03 23:33:00','','','','','','1-100','',NULL,'','','csv','UTF-8','//','','','','false',''),(731444359840403456,'clickhouse_datasets.z1转zdh_test.z1','63','JDBC','datasets.z1','','','name,age,sex,money','','','60','JDBC','z1','z1','','[{\"column_md5\":\"80c-4162-bf9a-59\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"fd7-4632-9e75-78\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"},{\"column_md5\":\"bbf-439a-8bfa-19\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"8ef-4d54-84d3-9d\",\"column_name\":\"money\",\"column_expr\":\"money\",\"column_type\":\"string\",\"column_alias\":\"money\"}]','','1','2020-07-10 17:38:45','','','','','','','',NULL,'','','','','','','','','',''),(746140955731562496,'sftp测试','65','SFTP','','/home/zyc/work/t1.txt','name,age,sex','','','','60','JDBC','t1','t1','','[{\"column_md5\":\"3fa-444c-9d88-45\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"7bc-44e2-896d-1c\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"},{\"column_md5\":\"ebc-4288-acf9-5e\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"}]','drop table t1','1','2020-08-20 06:57:46','','','','','','','',NULL,'','','csv','UTF-8',',','','','','false',''),(749279343477264384,'mydb#account_info','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','','','','','','[{\"column_md5\":\"212-4fac-b1df-1a\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"12f-4e82-8a9c-e3\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"user_name\"},{\"column_md5\":\"116-44b0-b692-15\",\"column_name\":\"user_password\",\"column_expr\":\"user_password\",\"column_type\":\"string\",\"column_alias\":\"user_password\"},{\"column_md5\":\"7eb-4515-9718-98\",\"column_name\":\"email\",\"column_expr\":\"email\",\"column_type\":\"string\",\"column_alias\":\"email\"},{\"column_md5\":\"4b2-45ed-9654-9f\",\"column_name\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_type\":\"string\",\"column_alias\":\"is_use_email\"},{\"column_md5\":\"c1c-44d4-8a8e-35\",\"column_name\":\"phone\",\"column_expr\":\"phone\",\"column_type\":\"string\",\"column_alias\":\"phone\"},{\"column_md5\":\"474-43a6-af20-3b\",\"column_name\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_type\":\"string\",\"column_alias\":\"is_use_phone\"}]','','1','2020-08-28 22:48:36','','','','','','','',NULL,'','','','','','','','','',''),(797802796177952768,'测试tidb写入','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','66','TIDB','','d1.t3','','[{\"column_md5\":\"898-4656-945f-00\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"name\"}]','delete from d1.t3','1','2021-01-09 20:23:29','','','','','','','',NULL,'','','','','','','','','',''),(797819041442959360,'第一个tidb读取','66','TIDB','','d1.t3','name','','','name=\'{{zdh_date}}\'','60','JDBC','tidb_t3','tidb_t3','','[{\"column_md5\":\"059-44b9-83ea-07\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"}]','delete from tidb_t3','1','2021-01-09 21:28:02','','','','','','','',NULL,'','','','','','','','','',''),(835512027848904704,'mysql2mysql','60','JDBC','t1','','','name,sex,job,addr,age','','','60','JDBC','t2','t2','','[{\"column_md5\":\"fe2-44e5-bcd4-2d\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"0bb-4391-8f75-cf\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"ced-4702-81b6-0b\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"301-4ace-89b7-34\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"9f9-437d-80e7-b1\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','drop table t2','1','2021-04-24 05:46:30','','','','','','','',NULL,'','','','','','','','','','');
/*!40000 ALTER TABLE `etl_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etl_task_meta`
--

DROP TABLE IF EXISTS `etl_task_meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `etl_task_meta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `context` varchar(100) DEFAULT NULL,
  `parent_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_task_meta`
--

LOCK TABLES `etl_task_meta` WRITE;
/*!40000 ALTER TABLE `etl_task_meta` DISABLE KEYS */;
INSERT INTO `etl_task_meta` VALUES (1,'ZDH总公司','0'),(2,'ZDH分公司','0'),(3,'采购部','1'),(4,'财务部','1');
/*!40000 ALTER TABLE `etl_task_meta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etl_task_update_logs`
--

DROP TABLE IF EXISTS `etl_task_update_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `etl_task_update_logs` (
  `id` varchar(100) DEFAULT NULL,
  `update_context` varchar(100) DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_task_update_logs`
--

LOCK TABLES `etl_task_update_logs` WRITE;
/*!40000 ALTER TABLE `etl_task_update_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `etl_task_update_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `every_day_notice`
--

DROP TABLE IF EXISTS `every_day_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `every_day_notice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `msg` text DEFAULT NULL COMMENT '通知消息',
  `is_delete` varchar(10) DEFAULT NULL COMMENT '是否删除消息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=835511556606267393 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `every_day_notice`
--

LOCK TABLES `every_day_notice` WRITE;
/*!40000 ALTER TABLE `every_day_notice` DISABLE KEYS */;
INSERT INTO `every_day_notice` VALUES (835511556606267392,'4.7.10版本更新如下,1:优化数据表字段注释,2:优化生成server端任务日志,3:前端增加数据源可空功能,4:新增数据源逻辑删除功能','false');
/*!40000 ALTER TABLE `every_day_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issue_data_info`
--

DROP TABLE IF EXISTS `issue_data_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issue_data_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `issue_context` varchar(200) DEFAULT NULL,
  `data_sources_choose_input` varchar(100) DEFAULT NULL,
  `data_source_type_input` varchar(100) DEFAULT NULL,
  `data_sources_table_name_input` varchar(100) DEFAULT NULL,
  `data_sources_file_name_input` varchar(100) DEFAULT NULL,
  `data_sources_file_columns` text DEFAULT NULL,
  `data_sources_table_columns` text DEFAULT NULL,
  `column_datas` text DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT current_timestamp(),
  `company` varchar(100) DEFAULT NULL,
  `section` varchar(100) DEFAULT NULL,
  `service` varchar(100) DEFAULT NULL,
  `update_context` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issue_data_info`
--

LOCK TABLES `issue_data_info` WRITE;
/*!40000 ALTER TABLE `issue_data_info` DISABLE KEYS */;
INSERT INTO `issue_data_info` VALUES (3,'临时表1','58','JDBC','zdh_ha_info','','','id,zdh_instance,zdh_url,zdh_host,zdh_port,web_port,zdh_status,application_id,history_server,master,create_time,update_time','[{\"column_md5\":\"edf-4a8f-b903-a2\",\"column_name\":\"id\",\"column_type\":\"string\"},{\"column_md5\":\"2a7-488c-a8a7-0b\",\"column_name\":\"zdh_instance\",\"column_type\":\"string\"},{\"column_md5\":\"9d5-4acf-9968-6c\",\"column_name\":\"zdh_url\",\"column_type\":\"string\"},{\"column_md5\":\"321-4319-b2ef-ca\",\"column_name\":\"zdh_host\",\"column_type\":\"string\"},{\"column_md5\":\"a9f-4945-843d-19\",\"column_name\":\"zdh_port\",\"column_type\":\"string\"},{\"column_md5\":\"8ad-4a27-8c7f-26\",\"column_name\":\"web_port\",\"column_type\":\"string\"},{\"column_md5\":\"be4-499f-81ae-db\",\"column_name\":\"zdh_status\",\"column_type\":\"string\"},{\"column_md5\":\"f0f-4a8c-bd89-c2\",\"column_name\":\"application_id\",\"column_type\":\"string\"},{\"column_md5\":\"b42-4498-8dd2-ea\",\"column_name\":\"history_server\",\"column_type\":\"string\"},{\"column_md5\":\"0aa-402d-8ff5-77\",\"column_name\":\"master\",\"column_type\":\"string\"},{\"column_md5\":\"fbc-4e3e-9f50-70\",\"column_name\":\"create_time\",\"column_type\":\"string\"},{\"column_md5\":\"7bf-4e99-8540-aa\",\"column_name\":\"update_time\",\"column_type\":\"string\"}]','1','2020-11-14 06:54:51','','','',''),(4,'drool任务表','58','JDBC','etl_drools_task_info','','','id,etl_context,etl_id,etl_drools,data_sources_filter_input,data_sources_choose_output,data_source_type_output,data_sources_table_name_output,data_sources_file_name_output,data_sources_params_output,data_sources_clear_output,owner,create_time,drop_tmp_tables,file_type_output,encoding_output,sep_output,header_output,more_task','[{\"column_md5\":\"509-48bc-aafd-76\",\"column_name\":\"id\",\"column_type\":\"string\"},{\"column_md5\":\"98c-4112-977b-4a\",\"column_name\":\"etl_context\",\"column_type\":\"string\"},{\"column_md5\":\"580-4ec4-95ab-79\",\"column_name\":\"etl_id\",\"column_type\":\"string\"},{\"column_md5\":\"9cd-4890-aca2-57\",\"column_name\":\"etl_drools\",\"column_type\":\"string\"},{\"column_md5\":\"4ae-4ece-be45-c6\",\"column_name\":\"data_sources_filter_input\",\"column_type\":\"string\"},{\"column_md5\":\"9ae-4632-b0ac-0e\",\"column_name\":\"data_sources_choose_output\",\"column_type\":\"string\"},{\"column_md5\":\"463-4ba9-92f1-ba\",\"column_name\":\"data_source_type_output\",\"column_type\":\"string\"},{\"column_md5\":\"883-4835-9f54-39\",\"column_name\":\"data_sources_table_name_output\",\"column_type\":\"string\"},{\"column_md5\":\"be4-4c6f-b86a-49\",\"column_name\":\"data_sources_file_name_output\",\"column_type\":\"string\"},{\"column_md5\":\"9e5-4773-bed4-e4\",\"column_name\":\"data_sources_params_output\",\"column_type\":\"string\"},{\"column_md5\":\"5e9-4409-a564-12\",\"column_name\":\"data_sources_clear_output\",\"column_type\":\"string\"},{\"column_md5\":\"95b-475c-a436-11\",\"column_name\":\"owner\",\"column_type\":\"string\"},{\"column_md5\":\"c35-4827-8408-4a\",\"column_name\":\"create_time\",\"column_type\":\"string\"},{\"column_md5\":\"adc-4c7a-acac-a3\",\"column_name\":\"drop_tmp_tables\",\"column_type\":\"string\"},{\"column_md5\":\"68f-451e-9134-27\",\"column_name\":\"file_type_output\",\"column_type\":\"string\"},{\"column_md5\":\"2ee-4d29-8b64-38\",\"column_name\":\"encoding_output\",\"column_type\":\"string\"},{\"column_md5\":\"45c-436e-9c70-9c\",\"column_name\":\"sep_output\",\"column_type\":\"string\"},{\"column_md5\":\"0dc-46ca-ad3e-4d\",\"column_name\":\"header_output\",\"column_type\":\"string\"},{\"column_md5\":\"3dd-4a2e-b17b-18\",\"column_name\":\"more_task\",\"column_type\":\"string\"}]','1','2020-11-15 02:06:43','','','',''),(6,'数据源','58','JDBC','data_sources_type_info','','','id,sources_type','[{\"column_md5\":\"56b-448a-95a5-fe\",\"column_name\":\"id\",\"column_type\":\"string\",\"column_desc\":\"id\"},{\"column_md5\":\"973-46ff-ba8d-97\",\"column_name\":\"sources_type\",\"column_type\":\"string\",\"column_desc\":\"数据源类型\"}]','1','2020-11-15 02:16:51','','','','');
/*!40000 ALTER TABLE `issue_data_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jar_file_info`
--

DROP TABLE IF EXISTS `jar_file_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jar_file_info` (
  `id` varchar(20) DEFAULT NULL,
  `file_name` varchar(100) DEFAULT NULL COMMENT '文件名称',
  `path` varchar(100) DEFAULT NULL COMMENT '文件目录',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `jar_etl_id` varchar(20) DEFAULT NULL COMMENT '任务id',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `status` varchar(10) DEFAULT NULL COMMENT '文件状态是否删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jar_file_info`
--

LOCK TABLES `jar_file_info` WRITE;
/*!40000 ALTER TABLE `jar_file_info` DISABLE KEYS */;
INSERT INTO `jar_file_info` VALUES ('732534262112194560','rdp-core-2.0.jar',NULL,'2020-07-13 17:49:37','732534261759873024','1','success'),('732534576089403392','rdp-core-2.0.jar',NULL,'2020-07-13 17:50:52','732534575875493888','1','success'),('732534576756297728','report-common-1.0.7.jar',NULL,'2020-07-13 17:50:52','732534575875493888','1','success'),('732589310183739392','zdh_server.jar',NULL,'2020-07-13 21:28:22','732538726244159488','1','success'),('732643701108510720','log4j.properties',NULL,'2020-07-14 01:04:30','732538726244159488','1','success'),('749042705316712448','zdh_server.jar',NULL,'2020-08-28 07:08:17','749041924744155136','1',NULL),('749052568860102656','zdh_server.jar',NULL,'2020-08-28 07:47:29','749051365933715456','1','success'),('749054743187296256','zdh_server.jar',NULL,'2020-08-28 07:56:07','749054742851751936','1',NULL),('749058489606737920','log4j.properties',NULL,'2020-08-28 08:11:00','749055828161466368','1','success'),('749060185443536896','log4j.properties',NULL,'2020-08-28 08:17:45','749060184990552064','1',NULL),('749060348916535296','log4j.properties',NULL,'2020-08-28 08:18:24','749060348564213760','1',NULL),('749062114471055360','zdh_server.jar',NULL,'2020-08-28 08:25:24','749062114156482560','1',NULL),('749063562575482880','zdh_server.jar',NULL,'2020-08-28 08:31:10','749063562055389184','1','success'),('794984305888595968','',NULL,'2021-01-02 01:43:48','794984305481748480','1',NULL),('794985297841491968','',NULL,'2021-01-02 01:47:45','794985297623388160','1',NULL),('794985444797321216','',NULL,'2021-01-02 01:48:20','794985443983626240','1',NULL),('795224647019794432','',NULL,'2021-01-02 17:38:50','795224646118019072','1',NULL);
/*!40000 ALTER TABLE `jar_file_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jar_task_info`
--

DROP TABLE IF EXISTS `jar_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jar_task_info` (
  `id` varchar(20) DEFAULT NULL,
  `etl_context` varchar(100) DEFAULT NULL,
  `files` varchar(100) DEFAULT NULL,
  `master` varchar(100) DEFAULT NULL,
  `deploy_mode` varchar(20) DEFAULT NULL,
  `cpu` varchar(100) DEFAULT NULL,
  `memory` varchar(100) DEFAULT NULL,
  `main_class` varchar(100) DEFAULT NULL,
  `spark_submit_params` text DEFAULT NULL,
  `owner` varchar(10) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jar_task_info`
--

LOCK TABLES `jar_task_info` WRITE;
/*!40000 ALTER TABLE `jar_task_info` DISABLE KEYS */;
INSERT INTO `jar_task_info` VALUES ('732538726244159488','第一个jar',NULL,'local[2]','',NULL,'','','D:/spark-2.4.4-bin-hadoop2.7/bin/spark-submit.cmd --master local[2] --class com.zyc.SystemInit --conf spark.driver.extraJavaOptions=\"-Dlog4j.configuration=file:$zdh_jar_path/log4j.properties\" $zdh_jar_path/zdh_server.jar','1','2020-07-14 02:07:22');
/*!40000 ALTER TABLE `jar_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meta_database_info`
--

DROP TABLE IF EXISTS `meta_database_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meta_database_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `db_name` varchar(200) DEFAULT NULL,
  `tb_name` varchar(100) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_database_info`
--

LOCK TABLES `meta_database_info` WRITE;
/*!40000 ALTER TABLE `meta_database_info` DISABLE KEYS */;
INSERT INTO `meta_database_info` VALUES (12,'default','hive_t1','3','2020-12-24 14:26:01'),(34,'default','hive_t1','1','2021-04-23 08:43:49');
/*!40000 ALTER TABLE `meta_database_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meta_table_info`
--

DROP TABLE IF EXISTS `meta_table_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meta_table_info` (
  `tb_id` varchar(20) DEFAULT NULL,
  `col_name` varchar(100) DEFAULT NULL,
  `data_type` varchar(100) DEFAULT NULL,
  `comment` varchar(100) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `order` varchar(5) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_table_info`
--

LOCK TABLES `meta_table_info` WRITE;
/*!40000 ALTER TABLE `meta_table_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `meta_table_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality`
--

DROP TABLE IF EXISTS `quality`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quality` (
  `id` varchar(100) DEFAULT NULL,
  `dispatch_task_id` varchar(100) DEFAULT NULL COMMENT '调度任务id',
  `etl_task_id` varchar(100) DEFAULT NULL COMMENT '任务id',
  `etl_date` varchar(20) DEFAULT NULL COMMENT '数据处理日期',
  `status` varchar(20) DEFAULT NULL COMMENT '报告状态',
  `report` varchar(500) DEFAULT NULL COMMENT '报告信息',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality`
--

LOCK TABLES `quality` WRITE;
/*!40000 ALTER TABLE `quality` DISABLE KEYS */;
INSERT INTO `quality` VALUES ('720677954291503104','719620564695650304','719619870378954752','2020-06-12 00:00:00','通过','{\"result\":\"通过\",\"rows_range\":\"数据行数检测通过\"}','2020-06-11 00:36:57','1'),('720679166206283776','719620564695650304','719619870378954752','2020-06-13 00:00:00','通过','{\"result\":\"通过\",\"总行数\":\"9\",\"rows_range\":\"数据行数检测通过\"}','2020-06-11 00:41:48','1');
/*!40000 ALTER TABLE `quality` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quartz_job_info`
--

DROP TABLE IF EXISTS `quartz_job_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quartz_job_info` (
  `job_id` varchar(100) NOT NULL COMMENT '调度任务id,主键',
  `job_context` varchar(100) DEFAULT NULL COMMENT '调度任务说明',
  `more_task` varchar(20) DEFAULT NULL COMMENT '废弃字段,不用',
  `job_type` varchar(100) DEFAULT NULL COMMENT '数据处理都为ETL,告警EMAIL,重试RETRY,检测依赖CHECK',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '数据处理开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '数据处理结束时间',
  `step_size` varchar(100) DEFAULT NULL COMMENT '步长,自定义时间使用',
  `job_model` varchar(2) DEFAULT NULL COMMENT '顺时间执行1,执行一次2,重复执行3',
  `plan_count` varchar(5) DEFAULT NULL COMMENT '计划执行次数',
  `count` int(11) DEFAULT NULL COMMENT '当前任务执行次数,只做说明,具体判定使用实例表判断',
  `command` text DEFAULT NULL COMMENT 'shell命令,jdbc命令,当前字段以废弃',
  `params` text DEFAULT NULL COMMENT '自定义参数',
  `last_status` varchar(100) DEFAULT NULL COMMENT '上次任务执行状态,已废弃',
  `last_time` timestamp NULL DEFAULT NULL COMMENT '上次任务执行数据处理时间',
  `next_time` timestamp NULL DEFAULT NULL COMMENT '下次任务执行数据处理时间',
  `expr` varchar(100) DEFAULT NULL COMMENT 'cron表达式/自定义表达式',
  `status` varchar(100) DEFAULT NULL COMMENT '调度任务状态,create,running,pause,finish,remove,error',
  `ip` varchar(100) DEFAULT NULL COMMENT '服务器ip地址',
  `user` varchar(100) DEFAULT NULL COMMENT '服务器用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '服务器密码',
  `etl_task_id` varchar(100) DEFAULT NULL COMMENT '具体ETL任务id,已废弃',
  `etl_context` varchar(100) DEFAULT NULL COMMENT '具体ETL任务说明,已废弃',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `is_script` varchar(10) DEFAULT NULL COMMENT '是否以脚本方式执行command,已废弃',
  `job_ids` varchar(500) DEFAULT NULL COMMENT '依赖的调度任务id',
  `jump_dep` varchar(10) DEFAULT NULL COMMENT '是否跳过依赖',
  `jump_script` varchar(10) DEFAULT NULL COMMENT '是否跳过脚本,已废弃',
  `interval_time` varchar(20) DEFAULT NULL COMMENT '重试时间间隔',
  `alarm_enabled` varchar(10) DEFAULT NULL COMMENT '启用告警',
  `email_and_sms` varchar(10) DEFAULT NULL COMMENT '启用邮箱+短信告警',
  `alarm_account` varchar(500) DEFAULT NULL COMMENT '告警zdh账户',
  `task_log_id` varchar(100) DEFAULT NULL COMMENT '上次任务id,已废弃',
  `time_out` varchar(100) DEFAULT NULL COMMENT '超时时间',
  `priority` varchar(4) DEFAULT NULL COMMENT '任务优先级',
  `quartz_time` timestamp NULL DEFAULT NULL COMMENT 'quartz调度时间',
  `use_quartz_time` varchar(5) DEFAULT NULL COMMENT '是否使用quartz 调度时间',
  `time_diff` varchar(50) DEFAULT NULL COMMENT '后退时间差',
  `jsmind_data` mediumtext DEFAULT NULL COMMENT '任务血源关系',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quartz_job_info`
--

LOCK TABLES `quartz_job_info` WRITE;
/*!40000 ALTER TABLE `quartz_job_info` DISABLE KEYS */;
INSERT INTO `quartz_job_info` VALUES ('835512161869500416','测试专用',NULL,'ETL','2021-04-24 05:44:59','2021-04-24 05:44:59','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"e51_50c_bd84_f7\",\"etl_task_id\":\"835512027848904704\",\"etl_context\":\"mysql2mysql\",\"more_task\":\"单源ETL\",\"divId\":\"e51_50c_bd84_f7\",\"name\":\"mysql2mysql\",\"positionX\":283,\"positionY\":124,\"type\":\"tasks\"}],\"line\":[]}'),('835512553818820609','',NULL,'EMAIL','2021-04-24 05:48:35','2021-04-24 05:48:35',NULL,'3','-1',0,'',NULL,NULL,NULL,NULL,'15s','running',NULL,NULL,NULL,'email',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'86400','5',NULL,'off',NULL,NULL),('835512553848180737','',NULL,'RETRY','2021-04-24 05:48:35','2021-04-24 05:48:35',NULL,'3','-1',0,'',NULL,NULL,NULL,NULL,'1s','running',NULL,NULL,NULL,'retry',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'86400','5',NULL,'off',NULL,NULL),('835512553864957953','',NULL,'CHECK','2021-04-24 05:48:35','2021-04-24 05:48:35',NULL,'3','-1',0,'',NULL,NULL,NULL,NULL,'5s','running',NULL,NULL,NULL,'check',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'86400','5',NULL,'off',NULL,NULL);
/*!40000 ALTER TABLE `quartz_job_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource_tree_info`
--

DROP TABLE IF EXISTS `resource_tree_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource_tree_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent` varchar(100) DEFAULT NULL,
  `text` varchar(10) DEFAULT NULL COMMENT '节点名称',
  `level` varchar(10) DEFAULT NULL COMMENT '层级',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `icon` varchar(200) DEFAULT NULL,
  `resource_desc` varchar(10) DEFAULT NULL COMMENT '资源说明',
  `order` varchar(200) DEFAULT NULL,
  `is_enable` varchar(10) DEFAULT NULL COMMENT '是否启用',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `url` text DEFAULT NULL COMMENT 'url链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=830752386808025089 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource_tree_info`
--

LOCK TABLES `resource_tree_info` WRITE;
/*!40000 ALTER TABLE `resource_tree_info` DISABLE KEYS */;
INSERT INTO `resource_tree_info` VALUES (802848818109353984,'#','ZDH','1','1','fa fa-folder',NULL,'1',NULL,'2021-01-23 10:34:34','2021-01-30 08:43:35',''),(802850170428461056,'802848818109353984','总监控','2','1','fa fa-bar-chart',NULL,'1',NULL,'2021-01-23 10:34:34','2021-01-29 21:56:12','monitor.html'),(802852358580080640,'802848818109353984','ETL采集','2','1','fa fa-tasks',NULL,'2',NULL,'2021-01-23 10:34:34','2021-01-30 14:31:56',''),(802876953722884096,'802848818109353984','数据质量报告','2','1','fa fa-hourglass-half',NULL,'4',NULL,'2021-01-23 10:34:34','2021-01-29 22:24:43','quality_index.html'),(802918652050411520,'802852358580080640','数据源管理','3','1','non',NULL,'1',NULL,'2021-01-23 10:34:34','2021-01-29 22:31:11','data_sources_index.html'),(802918760057933824,'802852358580080640','ETL任务','3','1','non',NULL,'2',NULL,'2021-01-23 10:34:34','2021-01-29 22:31:14','etl_task_index.html'),(802919044364636160,'802852358580080640','多源ETL任务','3','1','non',NULL,'3',NULL,'2021-01-23 10:34:34','2021-01-29 22:31:17','etl_task_more_sources_index.html'),(802919157430489088,'802852358580080640','SQL任务','3','1','non',NULL,'4',NULL,'2021-01-23 10:34:34','2021-01-29 22:31:20','sql_task_index.html'),(802930870510948352,'802852358580080640','Drools任务','3','1','non',NULL,'5',NULL,'2021-01-23 10:34:34','2021-01-29 22:31:30','etl_task_drools_index.html'),(802931116691427328,'802852358580080640','SSH任务','3','1','non',NULL,'6',NULL,'2021-01-23 10:34:34','2021-01-29 22:31:33','etl_task_ssh_index.html'),(802931308593418240,'802852358580080640','调度ETL','3','1','non',NULL,'7',NULL,'2021-01-23 10:34:34','2021-01-29 22:31:36','dispatch_task_index.html'),(802931697527033856,'802848818109353984','下载管理','2','1','fa fa-download',NULL,'3',NULL,'2021-01-23 10:34:34','2021-01-29 22:24:39','download_index.html'),(802932157390524416,'802848818109353984','指标查询','2','1','fa fa-columns',NULL,'5',NULL,'2021-01-23 10:34:34','2021-01-29 22:24:46','quota_index.html'),(802932355596554240,'802848818109353984','血缘分析','2','1','fa fa-asterisk',NULL,'6',NULL,'2021-01-23 10:34:34','2021-01-29 22:24:49','consanguin_index.html'),(802932548165439488,'802848818109353984','权限管理','2','1','fa fa-cog',NULL,'7',NULL,'2021-01-23 10:34:34','2021-01-29 22:24:52','permission_index.html'),(802932890089295872,'802848818109353984','使用技巧','2','1','fa fa-hand-o-right',NULL,'17',NULL,'2021-01-23 10:34:34','2021-02-12 19:24:01','zdh_help.index'),(802933021148712960,'802848818109353984','Cron','2','1','fa fa-glass',NULL,'9',NULL,'2021-01-23 10:34:34','2021-01-29 22:24:58','cron.html'),(802933165302747136,'802848818109353984','README','2','1','fa fa-info',NULL,'18',NULL,'2021-01-23 10:34:34','2021-02-12 19:24:11','readme.html'),(805193674668380160,'802848818109353984','设置','2','1','fa fa-user',NULL,'15',NULL,'2021-01-29 21:52:12','2021-01-30 08:39:40',''),(805357642351382528,'802848818109353984','测试2','2','1','',NULL,'',NULL,'2021-01-30 08:43:45','2021-01-30 09:26:36',''),(805369519538180096,'805193674668380160','文件服务器','3','1','fa fa-file',NULL,'1',NULL,'2021-01-30 09:30:57','2021-01-30 09:47:06','file_manager.html'),(805372907965386752,'805193674668380160','同步元数据','3','1','fa fa-circle-o',NULL,'3',NULL,'2021-01-30 09:44:24','2021-01-30 13:51:20','function:load_meta_databases()'),(805374183432261632,'805193674668380160','用户中心','3','1','','','0','1','2021-01-30 09:49:28','2021-01-30 09:49:28','user.html'),(805431924678987776,'805193674668380160','生成监控任务','3','1','fa fa-gavel',NULL,'7',NULL,'2021-01-30 13:38:55','2021-01-30 13:52:20','function:del_system_job()'),(805531084459610112,'802848818109353984','升级扩容','2','1','fa fa-cloud',NULL,'16',NULL,'2021-01-30 20:12:57','2021-02-12 19:22:22',''),(808616077255774208,'805531084459610112','部署管理','3','1','fa fa-linux',NULL,'3',NULL,'2021-02-08 08:31:36','2021-02-11 20:23:56','server_manager_index.html'),(809886572093640704,'805193674668380160','通知管理','3','1','fa fa-comments',NULL,'9',NULL,'2021-02-11 20:40:06','2021-02-11 20:40:21','notice_update_index.html'),(812083004473085952,'802848818109353984','数仓管理','2','1','fa fa-database','','16','1','2021-02-18 06:07:56','2021-02-18 06:07:56',''),(812083162921308160,'812083004473085952','数据集市','3','1','fa fa-database',NULL,'1',NULL,'2021-02-18 06:08:33','2021-02-18 06:08:49','consanguin_index.html'),(830752386808025088,'802848818109353984','联系作者','2','1','fa fa-pencil','','20','1','2021-04-10 18:33:23','2021-04-10 18:33:23','mail_compose.html');
/*!40000 ALTER TABLE `resource_tree_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `server_task_info`
--

DROP TABLE IF EXISTS `server_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `server_task_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `build_task` varchar(200) DEFAULT NULL COMMENT '构建任务说明',
  `build_ip` varchar(200) DEFAULT NULL COMMENT '构建服务器',
  `git_url` varchar(500) DEFAULT NULL COMMENT 'git地址',
  `build_type` varchar(10) DEFAULT NULL COMMENT '构建工具类型,GRADLE/MAVEN',
  `build_command` text DEFAULT NULL COMMENT '构建命令',
  `remote_ip` varchar(200) DEFAULT NULL COMMENT '部署服务器',
  `remote_path` varchar(200) DEFAULT NULL COMMENT '部署路径',
  `create_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `build_branch` varchar(200) DEFAULT NULL COMMENT '分支名',
  `build_username` varchar(100) DEFAULT NULL COMMENT '构建用户',
  `build_privatekey` text DEFAULT NULL COMMENT '构建服务器密钥地址',
  `build_path` varchar(500) DEFAULT NULL COMMENT '构建地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `server_task_info`
--

LOCK TABLES `server_task_info` WRITE;
/*!40000 ALTER TABLE `server_task_info` DISABLE KEYS */;
INSERT INTO `server_task_info` VALUES (1,'server构建任务','192.168.110.10','','GRADLE','sh gradlew release -x test','127.0.0.1','/home/zyc/zdh_server_build','2021-02-12 01:02:11','2021-02-12 01:02:11','1','master','zyc','123456','/home/zyc/zdh_server');
/*!40000 ALTER TABLE `server_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `server_task_instance`
--

DROP TABLE IF EXISTS `server_task_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `server_task_instance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `templete_id` varchar(100) DEFAULT NULL,
  `build_task` varchar(200) DEFAULT NULL COMMENT '构建任务说明',
  `build_ip` varchar(200) DEFAULT NULL COMMENT '构建服务器',
  `git_url` varchar(500) DEFAULT NULL COMMENT 'git地址',
  `build_type` varchar(10) DEFAULT NULL COMMENT '构建工具类型,GRADLE/MAVEN',
  `build_command` text DEFAULT NULL COMMENT '构建命令',
  `remote_ip` varchar(200) DEFAULT NULL COMMENT '部署服务器',
  `remote_path` varchar(200) DEFAULT NULL COMMENT '部署路径',
  `create_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `version_type` varchar(200) DEFAULT NULL COMMENT '部署类型BRANCH/TAG',
  `version` varchar(200) DEFAULT NULL COMMENT '版本',
  `build_branch` varchar(200) DEFAULT NULL COMMENT '分支/标签',
  `status` varchar(200) DEFAULT NULL COMMENT '部署状态',
  `build_username` varchar(100) DEFAULT NULL COMMENT '构建用户',
  `build_privatekey` text DEFAULT NULL COMMENT '构建服务器密钥地址',
  `build_path` varchar(500) DEFAULT NULL COMMENT '构建地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `server_task_instance`
--

LOCK TABLES `server_task_instance` WRITE;
/*!40000 ALTER TABLE `server_task_instance` DISABLE KEYS */;
/*!40000 ALTER TABLE `server_task_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sql_task_info`
--

DROP TABLE IF EXISTS `sql_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sql_task_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sql_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
  `data_sources_params_input` varchar(500) DEFAULT NULL COMMENT '输入数据源参数',
  `etl_sql` text DEFAULT NULL COMMENT 'sql任务处理逻辑',
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
  `file_type_output` varchar(10) DEFAULT NULL COMMENT '输出文件类型',
  `encoding_output` varchar(10) DEFAULT NULL COMMENT '输出文件编码',
  `sep_output` varchar(10) DEFAULT NULL COMMENT '输出文件分割符',
  `header_output` varchar(10) DEFAULT NULL COMMENT '输出是否包含表头',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=756934940771225601 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sql_task_info`
--

LOCK TABLES `sql_task_info` WRITE;
/*!40000 ALTER TABLE `sql_task_info` DISABLE KEYS */;
INSERT INTO `sql_task_info` VALUES (718814940856586240,'读取hive转mysql',NULL,'','','select * from act','58','JDBC','act2','act2','','','1','2020-06-05 21:13:57','','','','',NULL,NULL,NULL,NULL),(720684174964428800,'读取hive转外部下载',NULL,'','','select * from act','58','JDBC','act2','act2','','','1','2020-06-05 21:13:57','','','','','','','',''),(756925994555674624,'fddfdfs',NULL,'','','select * from a2','','','','','','','1','2020-09-19 01:13:40','','','','','','','',''),(756926234339840000,'fdfdfd',NULL,'','','select 2','','','','','','','1','2020-09-19 01:14:37','','','','','','','',''),(756934940771225600,'读取hive转mysql',NULL,'','','select * from act bb','58','JDBC','act2','act2','','','1','2020-09-19 01:49:13','','','','','','','','');
/*!40000 ALTER TABLE `sql_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ssh_task_info`
--

DROP TABLE IF EXISTS `ssh_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ssh_task_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ssh_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `host` varchar(100) DEFAULT NULL,
  `port` varchar(100) DEFAULT NULL,
  `user_name` varchar(500) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `ssh_cmd` text DEFAULT NULL,
  `ssh_script_path` varchar(100) DEFAULT NULL,
  `ssh_script_context` text DEFAULT NULL,
  `ssh_params_input` varchar(500) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=794986268751564801 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ssh_task_info`
--

LOCK TABLES `ssh_task_info` WRITE;
/*!40000 ALTER TABLE `ssh_task_info` DISABLE KEYS */;
INSERT INTO `ssh_task_info` VALUES (749064500069535744,'123','127.0.0.1','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc/z2','ls','','1','2020-08-28 08:34:54','','','',''),(794986268751564800,'暂停5分钟','127.0.0.1','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc','echo \"hello\" >> a.log\r\nsleep 1m\r\necho \"word\" >> a.log','','1','2021-01-02 01:51:37','','','','');
/*!40000 ALTER TABLE `ssh_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_group_log_instance`
--

DROP TABLE IF EXISTS `task_group_log_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_group_log_instance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` varchar(100) DEFAULT NULL COMMENT '调度任务id',
  `job_context` varchar(100) DEFAULT NULL COMMENT '调度任务说明',
  `etl_date` varchar(30) DEFAULT NULL COMMENT '数据处理日期',
  `status` varchar(50) DEFAULT NULL COMMENT '当前任务实例状态',
  `run_time` timestamp NULL DEFAULT NULL COMMENT '实例开始执行时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '实例更新时间',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `is_notice` varchar(10) DEFAULT NULL COMMENT '是否通知过标识',
  `process` varchar(10) DEFAULT NULL COMMENT '进度',
  `thread_id` varchar(100) DEFAULT NULL COMMENT '线程id',
  `retry_time` timestamp NULL DEFAULT NULL COMMENT '重试时间',
  `executor` varchar(100) DEFAULT NULL COMMENT '执行器id',
  `etl_info` text DEFAULT NULL COMMENT '具体执行任务信息,已废弃',
  `url` varchar(100) DEFAULT NULL COMMENT '发送执行url,已废弃',
  `application_id` varchar(100) DEFAULT NULL COMMENT '数据采集端执行器标识',
  `history_server` varchar(100) DEFAULT NULL COMMENT '历史服务器',
  `master` varchar(100) DEFAULT NULL COMMENT 'spark 任务模式',
  `server_ack` varchar(100) DEFAULT NULL COMMENT '数据采集端确认标识',
  `concurrency` varchar(100) DEFAULT NULL COMMENT '是否并行0:串行,1:并行',
  `last_task_log_id` varchar(100) DEFAULT NULL COMMENT '上次任务id,已废弃',
  `more_task` varchar(20) DEFAULT NULL COMMENT '任务类型,已废弃',
  `job_type` varchar(100) DEFAULT NULL COMMENT '数据处理都为ETL,告警EMAIL,重试RETRY,检测依赖CHECK,',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  `step_size` varchar(100) DEFAULT NULL COMMENT '步长',
  `job_model` varchar(2) DEFAULT NULL COMMENT '顺时间执行1,执行一次2,重复执行3',
  `plan_count` varchar(5) DEFAULT NULL COMMENT '计划执行次数',
  `count` int(11) DEFAULT NULL COMMENT '当前任务执行次数,只做说明,具体判定使用实例表判断',
  `command` text DEFAULT NULL COMMENT 'shell命令,jdbc命令,当前字段以废弃',
  `params` text DEFAULT NULL COMMENT '自定义参数',
  `last_status` varchar(100) DEFAULT NULL COMMENT '上次任务执行状态,已废弃',
  `last_time` timestamp NULL DEFAULT NULL COMMENT '上次任务执行数据处理时间',
  `next_time` timestamp NULL DEFAULT NULL COMMENT '下次任务执行数据处理时间',
  `expr` varchar(100) DEFAULT NULL COMMENT 'cron表达式/自定义表达式',
  `ip` varchar(100) DEFAULT NULL COMMENT '服务器ip地址',
  `user` varchar(100) DEFAULT NULL COMMENT '服务器用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '服务器密码',
  `etl_task_id` varchar(100) DEFAULT NULL COMMENT '具体ETL任务id,已废弃',
  `etl_context` varchar(100) DEFAULT NULL COMMENT '具体ETL任务说明,已废弃',
  `is_script` varchar(10) DEFAULT NULL COMMENT '是否以脚本方式执行command,已废弃',
  `job_ids` varchar(500) DEFAULT NULL COMMENT '依赖的调度任务id',
  `jump_dep` varchar(10) DEFAULT NULL COMMENT '是否跳过依赖',
  `jump_script` varchar(10) DEFAULT NULL COMMENT '是否跳过脚本,已废弃',
  `interval_time` varchar(20) DEFAULT NULL COMMENT '重试时间间隔',
  `alarm_enabled` varchar(10) DEFAULT NULL COMMENT '启用告警',
  `email_and_sms` varchar(10) DEFAULT NULL COMMENT '启用邮箱+短信告警',
  `alarm_account` varchar(500) DEFAULT NULL COMMENT '告警zdh账户',
  `cur_time` timestamp NULL DEFAULT NULL COMMENT '当前实例数据时间',
  `is_retryed` varchar(4) DEFAULT NULL COMMENT '是否重试过',
  `server_id` varchar(100) DEFAULT NULL COMMENT '调度执行端执行器标识',
  `time_out` varchar(100) DEFAULT NULL COMMENT '超时时间',
  `process_time` text DEFAULT NULL COMMENT '流程时间',
  `priority` varchar(4) DEFAULT NULL COMMENT '任务优先级',
  `quartz_time` timestamp NULL DEFAULT NULL COMMENT 'quartz调度时间',
  `use_quartz_time` varchar(5) DEFAULT NULL COMMENT '是否使用quartz 调度时间',
  `time_diff` varchar(50) DEFAULT NULL COMMENT '后退时间差',
  `jsmind_data` text DEFAULT NULL COMMENT '任务血源关系',
  `run_jsmind_data` text DEFAULT NULL COMMENT '生成实例血源关系',
  `next_tasks` text DEFAULT NULL COMMENT '下游任务组实例id',
  `pre_tasks` text DEFAULT NULL COMMENT '上游任务组实例id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=835512629425344513 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_group_log_instance`
--

LOCK TABLES `task_group_log_instance` WRITE;
/*!40000 ALTER TABLE `task_group_log_instance` DISABLE KEYS */;
INSERT INTO `task_group_log_instance` VALUES (835512629425344512,'835512161869500416','测试专用','2021-04-24 13:48:53','error','2021-04-24 05:48:53','2021-04-24 05:48:53','1','false','100',NULL,'1999-12-31 16:00:00',NULL,NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'ETL',NULL,NULL,'1d','1','0',0,NULL,'',NULL,'2021-04-24 05:48:53',NULL,'1d',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,'5',NULL,NULL,'','2021-04-24 05:48:53','0','zdh_web_1:835510610677469184','86400','{\"check_dep_time\":\"2021-04-24 13:48:55\"}','5',NULL,'off','','{\"tasks\":[{\"id\":\"e51_50c_bd84_f7\",\"etl_task_id\":\"835512027848904704\",\"etl_context\":\"mysql2mysql\",\"more_task\":\"单源ETL\",\"divId\":\"e51_50c_bd84_f7\",\"name\":\"mysql2mysql\",\"positionX\":283,\"positionY\":124,\"type\":\"tasks\"}],\"line\":[]}','{\"run_data\":[{\"job_type\":\"ETL\",\"task_log_instance_id\":\"835512629526007808\",\"more_task\":\"单源ETL\",\"etl_context\":\"mysql2mysql\",\"etl_task_id\":\"835512027848904704\",\"divId\":\"e51_50c_bd84_f7\"}],\"line\":[],\"run_line\":[],\"tasks\":[{\"positionY\":124,\"name\":\"mysql2mysql\",\"more_task\":\"单源ETL\",\"etl_context\":\"mysql2mysql\",\"id\":\"e51_50c_bd84_f7\",\"etl_task_id\":\"835512027848904704\",\"type\":\"tasks\",\"divId\":\"e51_50c_bd84_f7\",\"positionX\":283}]}',NULL,NULL);
/*!40000 ALTER TABLE `task_group_log_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_log_instance`
--

DROP TABLE IF EXISTS `task_log_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_log_instance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` varchar(100) DEFAULT NULL COMMENT '调度任务id',
  `job_context` varchar(100) DEFAULT NULL COMMENT '调度任务说明',
  `group_id` varchar(100) DEFAULT NULL COMMENT '调度任务id',
  `group_context` varchar(100) DEFAULT NULL COMMENT '调度任务说明',
  `etl_date` varchar(30) DEFAULT NULL COMMENT '数据处理日期',
  `status` varchar(50) DEFAULT NULL COMMENT '当前任务实例状态',
  `run_time` timestamp NULL DEFAULT NULL COMMENT '实例开始执行时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '实例更新时间',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `is_notice` varchar(10) DEFAULT NULL COMMENT '是否通知过标识',
  `process` varchar(10) DEFAULT NULL COMMENT '进度',
  `thread_id` varchar(100) DEFAULT NULL COMMENT '线程id',
  `retry_time` timestamp NULL DEFAULT NULL COMMENT '重试时间',
  `executor` varchar(100) DEFAULT NULL COMMENT '执行器id',
  `etl_info` text DEFAULT NULL COMMENT '具体执行任务信息,已废弃',
  `url` varchar(100) DEFAULT NULL COMMENT '发送执行url,已废弃',
  `application_id` varchar(100) DEFAULT NULL COMMENT '数据采集端执行器标识',
  `history_server` varchar(100) DEFAULT NULL COMMENT '历史服务器',
  `master` varchar(100) DEFAULT NULL COMMENT 'spark 任务模式',
  `server_ack` varchar(100) DEFAULT NULL COMMENT '数据采集端确认标识',
  `concurrency` varchar(100) DEFAULT NULL COMMENT '是否并行0:串行,1:并行',
  `last_task_log_id` varchar(100) DEFAULT NULL COMMENT '上次任务id,已废弃',
  `more_task` varchar(20) DEFAULT NULL COMMENT '任务类型,已废弃',
  `job_type` varchar(100) DEFAULT NULL COMMENT '数据处理都为ETL,告警EMAIL,重试RETRY,检测依赖CHECK,',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  `step_size` varchar(100) DEFAULT NULL COMMENT '步长',
  `job_model` varchar(2) DEFAULT NULL COMMENT '顺时间执行1,执行一次2,重复执行3',
  `plan_count` varchar(5) DEFAULT NULL COMMENT '计划执行次数',
  `count` int(11) DEFAULT NULL COMMENT '当前任务执行次数,只做说明,具体判定使用实例表判断',
  `command` text DEFAULT NULL COMMENT 'shell命令,jdbc命令,当前字段以废弃',
  `params` text DEFAULT NULL COMMENT '自定义参数',
  `last_status` varchar(100) DEFAULT NULL COMMENT '上次任务执行状态,已废弃',
  `last_time` timestamp NULL DEFAULT NULL COMMENT '上次任务执行数据处理时间',
  `next_time` timestamp NULL DEFAULT NULL COMMENT '下次任务执行数据处理时间',
  `expr` varchar(100) DEFAULT NULL COMMENT 'cron表达式/自定义表达式',
  `ip` varchar(100) DEFAULT NULL COMMENT '服务器ip地址',
  `user` varchar(100) DEFAULT NULL COMMENT '服务器用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '服务器密码',
  `etl_task_id` varchar(100) DEFAULT NULL COMMENT '具体ETL任务id',
  `etl_context` varchar(100) DEFAULT NULL COMMENT '具体ETL任务说明',
  `is_script` varchar(10) DEFAULT NULL COMMENT '是否以脚本方式执行command',
  `job_ids` varchar(500) DEFAULT NULL COMMENT '依赖的调度任务id',
  `jump_dep` varchar(10) DEFAULT NULL COMMENT '是否跳过依赖',
  `jump_script` varchar(10) DEFAULT NULL COMMENT '是否跳过脚本,已废弃',
  `interval_time` varchar(20) DEFAULT NULL COMMENT '重试时间间隔',
  `alarm_enabled` varchar(10) DEFAULT NULL COMMENT '启用告警',
  `email_and_sms` varchar(10) DEFAULT NULL COMMENT '启用邮箱+短信告警',
  `alarm_account` varchar(500) DEFAULT NULL COMMENT '告警zdh账户',
  `cur_time` timestamp NULL DEFAULT NULL COMMENT '当前实例数据时间',
  `is_retryed` varchar(4) DEFAULT NULL COMMENT '是否重试过',
  `server_id` varchar(100) DEFAULT NULL COMMENT '调度执行端执行器标识',
  `time_out` varchar(100) DEFAULT NULL COMMENT '超时时间',
  `process_time` text DEFAULT NULL COMMENT '流程时间',
  `priority` varchar(4) DEFAULT NULL COMMENT '任务优先级',
  `quartz_time` timestamp NULL DEFAULT NULL COMMENT 'quartz调度时间',
  `use_quartz_time` varchar(5) DEFAULT NULL COMMENT '是否使用quartz 调度时间',
  `time_diff` varchar(50) DEFAULT NULL COMMENT '后退时间差',
  `jsmind_data` text DEFAULT NULL COMMENT '任务血源关系',
  `run_jsmind_data` text DEFAULT NULL COMMENT '生成实例血源关系',
  `next_tasks` text DEFAULT NULL COMMENT '下游任务实例id',
  `pre_tasks` text DEFAULT NULL COMMENT '上游任务实例id',
  `is_disenable` varchar(10) DEFAULT NULL COMMENT '是否禁用true:禁用,false:启用',
  `depend_level` varchar(10) NOT NULL DEFAULT '0' COMMENT '判定级别0：成功时运行,1:杀死时运行,2:失败时运行,默认成功时运行',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=835512629526007809 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_log_instance`
--

LOCK TABLES `task_log_instance` WRITE;
/*!40000 ALTER TABLE `task_log_instance` DISABLE KEYS */;
INSERT INTO `task_log_instance` VALUES (835512629526007808,'835512161869500416','测试专用','835512629425344512','测试专用','2021-04-24 13:48:53','error','2021-04-24 05:48:53','2021-04-24 05:48:56','1','true','17','1_54_835512629526007808','2021-04-24 05:49:01','9','{\"dsi_Input\":{\"data_source_context\":\"zdh_test\",\"data_source_type\":\"JDBC\",\"dbtable\":\"t1\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"id\":\"60\",\"password\":\"123456\",\"paths\":\"\",\"url\":\"jdbc:mysql://127.0.0.1:3306/zdh_test?serverTimezone=GMT%2B8&useSSL=false\",\"user\":\"zyc\"},\"dsi_Output\":{\"data_source_context\":\"zdh_test\",\"data_source_type\":\"JDBC\",\"dbtable\":\"t2\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"id\":\"60\",\"password\":\"123456\",\"paths\":\"t2\",\"url\":\"jdbc:mysql://127.0.0.1:3306/zdh_test?serverTimezone=GMT%2B8&useSSL=false\",\"user\":\"zyc\"},\"etlTaskInfo\":{\"column_data_list\":[{\"column_alias\":\"name\",\"column_expr\":\"name\",\"column_md5\":\"fe2-44e5-bcd4-2d\",\"column_name\":\"name\",\"column_type\":\"string\"},{\"column_alias\":\"sex\",\"column_expr\":\"sex\",\"column_md5\":\"0bb-4391-8f75-cf\",\"column_name\":\"sex\",\"column_type\":\"string\"},{\"column_alias\":\"job\",\"column_expr\":\"job\",\"column_md5\":\"ced-4702-81b6-0b\",\"column_name\":\"job\",\"column_type\":\"string\"},{\"column_alias\":\"addr\",\"column_expr\":\"addr\",\"column_md5\":\"301-4ace-89b7-34\",\"column_name\":\"addr\",\"column_type\":\"string\"},{\"column_alias\":\"age\",\"column_expr\":\"age\",\"column_md5\":\"9f9-437d-80e7-b1\",\"column_name\":\"age\",\"column_type\":\"string\"}],\"column_datas\":\"[{\\\"column_md5\\\":\\\"fe2-44e5-bcd4-2d\\\",\\\"column_name\\\":\\\"name\\\",\\\"column_expr\\\":\\\"name\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"name\\\"},{\\\"column_md5\\\":\\\"0bb-4391-8f75-cf\\\",\\\"column_name\\\":\\\"sex\\\",\\\"column_expr\\\":\\\"sex\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"sex\\\"},{\\\"column_md5\\\":\\\"ced-4702-81b6-0b\\\",\\\"column_name\\\":\\\"job\\\",\\\"column_expr\\\":\\\"job\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"job\\\"},{\\\"column_md5\\\":\\\"301-4ace-89b7-34\\\",\\\"column_name\\\":\\\"addr\\\",\\\"column_expr\\\":\\\"addr\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"addr\\\"},{\\\"column_md5\\\":\\\"9f9-437d-80e7-b1\\\",\\\"column_name\\\":\\\"age\\\",\\\"column_expr\\\":\\\"age\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"age\\\"}]\",\"column_size\":\"\",\"company\":\"\",\"create_time\":1619243190000,\"data_source_type_input\":\"JDBC\",\"data_source_type_output\":\"JDBC\",\"data_sources_choose_input\":\"60\",\"data_sources_choose_output\":\"60\",\"data_sources_clear_output\":\"drop table t2\",\"data_sources_file_columns\":\"\",\"data_sources_file_name_input\":\"\",\"data_sources_file_name_output\":\"t2\",\"data_sources_filter_input\":\"\",\"data_sources_params_input\":\"\",\"data_sources_params_output\":\"\",\"data_sources_table_columns\":\"name,sex,job,addr,age\",\"data_sources_table_name_input\":\"t1\",\"data_sources_table_name_output\":\"t2\",\"duplicate_columns\":\"\",\"encoding_input\":\"\",\"encoding_output\":\"\",\"error_rate\":\"\",\"etl_context\":\"mysql2mysql\",\"file_type_input\":\"\",\"file_type_output\":\"\",\"header_input\":\"\",\"header_output\":\"\",\"id\":\"835512027848904704\",\"owner\":\"1\",\"primary_columns\":\"\",\"rows_range\":\"\",\"section\":\"\",\"sep_input\":\"\",\"sep_output\":\"\",\"service\":\"\",\"update_context\":\"\"},\"task_logs_id\":\"835512629526007808\",\"tli\":{\"alarm_account\":\"\",\"concurrency\":\"0\",\"count\":1,\"cur_time\":1619243333000,\"depend_level\":\"0\",\"etl_context\":\"mysql2mysql\",\"etl_date\":\"2021-04-24 13:48:53\",\"etl_task_id\":\"835512027848904704\",\"expr\":\"1d\",\"group_context\":\"测试专用\",\"group_id\":\"835512629425344512\",\"id\":\"835512629526007808\",\"interval_time\":\"5\",\"is_disenable\":\"false\",\"is_notice\":\"false\",\"is_retryed\":\"0\",\"job_context\":\"测试专用\",\"job_id\":\"835512161869500416\",\"job_model\":\"1\",\"job_type\":\"ETL\",\"jsmind_data\":\"\",\"last_time\":1619243333000,\"more_task\":\"单源ETL\",\"next_tasks\":\"\",\"owner\":\"1\",\"params\":\"{\\\"ETL_DATE\\\":\\\"2021-04-24 13:48:53\\\"}\",\"plan_count\":\"0\",\"pre_tasks\":\"\",\"priority\":\"5\",\"process\":\"10\",\"process_msg\":\"组装ETL信息\",\"process_time\":\"{\\\"check_dep_time\\\":\\\"2021-04-24 13:48:55\\\"}\",\"process_time2\":{\"check_dep_time\":\"2021-04-24 13:48:55\"},\"retry_time\":946656000000,\"run_jsmind_data\":\"\",\"run_time\":1619243333000,\"server_ack\":\"0\",\"server_id\":\"zdh_web_1:835510610677469184\",\"status\":\"dispatch\",\"step_size\":\"1d\",\"thread_id\":\"1_54_835512629526007808\",\"time_diff\":\"\",\"time_out\":\"86400\",\"update_time\":1619243336064,\"use_quartz_time\":\"off\"}}','http://localhost:60001/api/v1/zdh','local-1619085259288','http://127.0.0.1:18080/api/v1','local[*]','0','0',NULL,'单源ETL','ETL',NULL,NULL,'1d','1','0',1,NULL,'{\"ETL_DATE\":\"2021-04-24 13:48:53\"}','error','2021-04-24 05:48:53',NULL,'1d',NULL,NULL,NULL,'835512027848904704','mysql2mysql',NULL,NULL,NULL,NULL,'5',NULL,NULL,'','2021-04-24 05:48:53','0','zdh_web_1:835510610677469184','86400','','5',NULL,'off','','','','','','false','0');
/*!40000 ALTER TABLE `task_log_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_resource_info`
--

DROP TABLE IF EXISTS `user_resource_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_resource_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户id',
  `resource_id` varchar(100) DEFAULT NULL COMMENT '资源id',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=294 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_resource_info`
--

LOCK TABLES `user_resource_info` WRITE;
/*!40000 ALTER TABLE `user_resource_info` DISABLE KEYS */;
INSERT INTO `user_resource_info` VALUES (120,'2','802850170428461056',NULL,NULL),(121,'2','802852358580080640',NULL,NULL),(122,'2','802931697527033856',NULL,NULL),(123,'2','802876953722884096',NULL,NULL),(124,'2','802932157390524416',NULL,NULL),(125,'2','802932355596554240',NULL,NULL),(126,'2','802932548165439488',NULL,NULL),(127,'2','802932890089295872',NULL,NULL),(128,'2','802933021148712960',NULL,NULL),(129,'2','802933165302747136',NULL,NULL),(130,'2','805193674668380160',NULL,NULL),(131,'2','805374183432261632',NULL,NULL),(132,'2','805369519538180096',NULL,NULL),(133,'2','805372907965386752',NULL,NULL),(134,'2','802918652050411520',NULL,NULL),(135,'2','802918760057933824',NULL,NULL),(136,'2','802919044364636160',NULL,NULL),(137,'2','802919157430489088',NULL,NULL),(138,'2','802930870510948352',NULL,NULL),(139,'2','802931116691427328',NULL,NULL),(140,'2','802931308593418240',NULL,NULL),(265,'1','805374183432261632',NULL,NULL),(266,'1','802848818109353984',NULL,NULL),(267,'1','802850170428461056',NULL,NULL),(268,'1','802918652050411520',NULL,NULL),(269,'1','805369519538180096',NULL,NULL),(270,'1','802852358580080640',NULL,NULL),(271,'1','802918760057933824',NULL,NULL),(272,'1','802919044364636160',NULL,NULL),(273,'1','802931697527033856',NULL,NULL),(274,'1','805372907965386752',NULL,NULL),(275,'1','808616077255774208',NULL,NULL),(276,'1','802876953722884096',NULL,NULL),(277,'1','802919157430489088',NULL,NULL),(278,'1','802930870510948352',NULL,NULL),(279,'1','802932157390524416',NULL,NULL),(280,'1','802931116691427328',NULL,NULL),(281,'1','802932355596554240',NULL,NULL),(282,'1','802931308593418240',NULL,NULL),(283,'1','802932548165439488',NULL,NULL),(284,'1','805431924678987776',NULL,NULL),(285,'1','802932890089295872',NULL,NULL),(286,'1','802933021148712960',NULL,NULL),(287,'1','802933165302747136',NULL,NULL),(288,'1','805193674668380160',NULL,NULL),(289,'1','805531084459610112',NULL,NULL),(290,'1','809886572093640704',NULL,NULL),(291,'1','812083162921308160',NULL,NULL),(292,'1','812083004473085952',NULL,NULL),(293,'1','830752386808025088',NULL,NULL);
/*!40000 ALTER TABLE `user_resource_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zdh_download_info`
--

DROP TABLE IF EXISTS `zdh_download_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zdh_download_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) DEFAULT NULL,
  `file_name` varchar(200) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `down_count` int(11) DEFAULT NULL,
  `etl_date` timestamp NULL DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `job_context` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zdh_download_info`
--

LOCK TABLES `zdh_download_info` WRITE;
/*!40000 ALTER TABLE `zdh_download_info` DISABLE KEYS */;
INSERT INTO `zdh_download_info` VALUES (1,NULL,'G:/data/1/act2.csv','2020-06-11 09:15:36',1,'2020-06-12 16:00:00','1','读取hive并下载文件');
/*!40000 ALTER TABLE `zdh_download_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zdh_ha_info`
--

DROP TABLE IF EXISTS `zdh_ha_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zdh_ha_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `zdh_instance` varchar(128) DEFAULT NULL,
  `zdh_url` varchar(500) DEFAULT NULL,
  `zdh_host` varchar(128) DEFAULT NULL,
  `zdh_port` varchar(5) DEFAULT NULL,
  `web_port` varchar(100) DEFAULT NULL,
  `zdh_status` varchar(10) DEFAULT NULL,
  `application_id` varchar(500) DEFAULT NULL,
  `history_server` varchar(500) DEFAULT NULL,
  `master` varchar(500) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT current_timestamp(),
  `update_time` timestamp NULL DEFAULT current_timestamp(),
  `online` varchar(10) DEFAULT NULL COMMENT '是否上线1:上线,0:逻辑下线2:物理下线',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zdh_ha_info`
--

LOCK TABLES `zdh_ha_info` WRITE;
/*!40000 ALTER TABLE `zdh_ha_info` DISABLE KEYS */;
INSERT INTO `zdh_ha_info` VALUES (6,'zdh','http://izm5eexscw3e0firjnqdlzz:60001/api/v1/zdh','izm5eexscw3e0firjnqdlzz','60001','4040','enabled','local-1618927494746','http://127.0.0.1:18080/api/v1','local[*]','2021-04-20 14:04:55','2021-04-23 14:01:31','1'),(9,'zdh','http://localhost:60001/api/v1/zdh','localhost','60001','4040','enabled','local-1619085259288','http://127.0.0.1:18080/api/v1','local[*]','2021-04-22 09:54:19','2021-04-23 14:01:31','1');
/*!40000 ALTER TABLE `zdh_ha_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zdh_logs`
--

DROP TABLE IF EXISTS `zdh_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zdh_logs` (
  `job_id` varchar(100) DEFAULT NULL,
  `log_time` timestamp NULL DEFAULT NULL,
  `msg` text DEFAULT NULL,
  `level` varchar(10) DEFAULT NULL,
  `task_logs_id` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zdh_logs`
--

LOCK TABLES `zdh_logs` WRITE;
/*!40000 ALTER TABLE `zdh_logs` DISABLE KEYS */;
INSERT INTO `zdh_logs` VALUES ('835512161869500416','2021-04-24 05:47:23','[ETL] JOB ,开始检查当前任务上游任务依赖','INFO','835512199047811072'),('835512161869500416','2021-04-24 05:47:23','[ETL] JOB ,是根节点任务,无依赖,直接执行','INFO','835512199047811072'),('835512161869500416','2021-04-24 05:47:23','开始执行[ETL] JOB','INFO','835512199047811072'),('835512161869500416','2021-04-24 05:47:23','[ETL] JOB ,开始检查任务次数限制','INFO','835512199047811072'),('835512161869500416','2021-04-24 05:47:23','[ETL] JOB,任务模式为[时间序列]','INFO','835512199047811072'),('835512161869500416','2021-04-24 05:47:23','[ETL] JOB ,调度命令执行成功,准备发往任务到后台ETL执行','INFO','835512199047811072'),('835512161869500416','2021-04-24 05:47:23','获取服务端url,指定参数:','INFO','835512199047811072'),('835512161869500416','2021-04-24 05:47:23','[ETL] JOB ,获取当前的[url]:http://izm5eexscw3e0firjnqdlzz:60001/api/v1/zdh','INFO','835512199047811072'),('835512161869500416','2021-04-24 05:47:23','目前支持日期参数以下模式: {{zdh_date}} => yyyy-MM-dd ,{{zdh_date_nodash}}=> yyyyMMdd ,{{zdh_date_time}}=> yyyy-MM-dd HH:mm:ss,{{zdh_year}}=> 年,{{zdh_month}}=> 月,{{zdh_day}}=> 日,{{zdh_hour}}=>24小时制,{{zdh_minute}}=>分钟,{{zdh_second}}=>秒,{{zdh_time}}=>时间戳','INFO','835512199047811072'),('835512161869500416','2021-04-24 05:47:23','[调度平台]:[ETL] JOB ,开始发送ETL处理请求','DEBUG','835512199047811072'),('835512161869500416','2021-04-24 05:47:23','[调度平台]:http://izm5eexscw3e0firjnqdlzz:60001/api/v1/zdh ,参数:{\"dsi_Input\":{\"data_source_context\":\"zdh_test\",\"data_source_type\":\"JDBC\",\"dbtable\":\"t1\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"id\":\"60\",\"password\":\"123456\",\"paths\":\"\",\"url\":\"jdbc:mysql://127.0.0.1:3306/zdh_test?serverTimezone=GMT%2B8&useSSL=false\",\"user\":\"zyc\"},\"dsi_Output\":{\"data_source_context\":\"zdh_test\",\"data_source_type\":\"JDBC\",\"dbtable\":\"t2\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"id\":\"60\",\"password\":\"123456\",\"paths\":\"t2\",\"url\":\"jdbc:mysql://127.0.0.1:3306/zdh_test?serverTimezone=GMT%2B8&useSSL=false\",\"user\":\"zyc\"},\"etlTaskInfo\":{\"column_data_list\":[{\"column_alias\":\"name\",\"column_expr\":\"name\",\"column_md5\":\"fe2-44e5-bcd4-2d\",\"column_name\":\"name\",\"column_type\":\"string\"},{\"column_alias\":\"sex\",\"column_expr\":\"sex\",\"column_md5\":\"0bb-4391-8f75-cf\",\"column_name\":\"sex\",\"column_type\":\"string\"},{\"column_alias\":\"job\",\"column_expr\":\"job\",\"column_md5\":\"ced-4702-81b6-0b\",\"column_name\":\"job\",\"column_type\":\"string\"},{\"column_alias\":\"addr\",\"column_expr\":\"addr\",\"column_md5\":\"301-4ace-89b7-34\",\"column_name\":\"addr\",\"column_type\":\"string\"},{\"column_alias\":\"age\",\"column_expr\":\"age\",\"column_md5\":\"9f9-437d-80e7-b1\",\"column_name\":\"age\",\"column_type\":\"string\"}],\"column_datas\":\"[{\\\"column_md5\\\":\\\"fe2-44e5-bcd4-2d\\\",\\\"column_name\\\":\\\"name\\\",\\\"column_expr\\\":\\\"name\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"name\\\"},{\\\"column_md5\\\":\\\"0bb-4391-8f75-cf\\\",\\\"column_name\\\":\\\"sex\\\",\\\"column_expr\\\":\\\"sex\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"sex\\\"},{\\\"column_md5\\\":\\\"ced-4702-81b6-0b\\\",\\\"column_name\\\":\\\"job\\\",\\\"column_expr\\\":\\\"job\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"job\\\"},{\\\"column_md5\\\":\\\"301-4ace-89b7-34\\\",\\\"column_name\\\":\\\"addr\\\",\\\"column_expr\\\":\\\"addr\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"addr\\\"},{\\\"column_md5\\\":\\\"9f9-437d-80e7-b1\\\",\\\"column_name\\\":\\\"age\\\",\\\"column_expr\\\":\\\"age\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"age\\\"}]\",\"column_size\":\"\",\"company\":\"\",\"create_time\":1619243190000,\"data_source_type_input\":\"JDBC\",\"data_source_type_output\":\"JDBC\",\"data_sources_choose_input\":\"60\",\"data_sources_choose_output\":\"60\",\"data_sources_clear_output\":\"drop table t2\",\"data_sources_file_columns\":\"\",\"data_sources_file_name_input\":\"\",\"data_sources_file_name_output\":\"t2\",\"data_sources_filter_input\":\"\",\"data_sources_params_input\":\"\",\"data_sources_params_output\":\"\",\"data_sources_table_columns\":\"name,sex,job,addr,age\",\"data_sources_table_name_input\":\"t1\",\"data_sources_table_name_output\":\"t2\",\"duplicate_columns\":\"\",\"encoding_input\":\"\",\"encoding_output\":\"\",\"error_rate\":\"\",\"etl_context\":\"mysql2mysql\",\"file_type_input\":\"\",\"file_type_output\":\"\",\"header_input\":\"\",\"header_output\":\"\",\"id\":\"835512027848904704\",\"owner\":\"1\",\"primary_columns\":\"\",\"rows_range\":\"\",\"section\":\"\",\"sep_input\":\"\",\"sep_output\":\"\",\"service\":\"\",\"update_context\":\"\"},\"task_logs_id\":\"835512199047811072\",\"tli\":{\"alarm_account\":\"\",\"concurrency\":\"0\",\"count\":1,\"cur_time\":1619243230000,\"depend_level\":\"0\",\"etl_context\":\"mysql2mysql\",\"etl_date\":\"2021-04-24 13:47:10\",\"etl_task_id\":\"835512027848904704\",\"expr\":\"1d\",\"group_context\":\"测试专用\",\"group_id\":\"835512198695489536\",\"id\":\"835512199047811072\",\"interval_time\":\"5\",\"is_disenable\":\"false\",\"is_notice\":\"false\",\"is_retryed\":\"0\",\"job_context\":\"测试专用\",\"job_id\":\"835512161869500416\",\"job_model\":\"1\",\"job_type\":\"ETL\",\"jsmind_data\":\"\",\"last_time\":1619243230000,\"more_task\":\"单源ETL\",\"next_tasks\":\"\",\"owner\":\"1\",\"params\":\"{\\\"ETL_DATE\\\":\\\"2021-04-24 13:47:10\\\"}\",\"plan_count\":\"0\",\"pre_tasks\":\"\",\"priority\":\"5\",\"process\":\"10\",\"process_msg\":\"组装ETL信息\",\"process_time\":\"{\\\"check_dep_time\\\":\\\"2021-04-24 13:47:23\\\"}\",\"process_time2\":{\"check_dep_time\":\"2021-04-24 13:47:23\"},\"retry_time\":946656000000,\"run_jsmind_data\":\"\",\"run_time\":1619243231000,\"server_ack\":\"0\",\"server_id\":\"zdh_web_1:835510610677469184\",\"status\":\"dispatch\",\"step_size\":\"1d\",\"thread_id\":\"1_53_835512199047811072\",\"time_diff\":\"\",\"time_out\":\"86400\",\"update_time\":1619243243617,\"use_quartz_time\":\"off\"}}','DEBUG','835512199047811072'),('835512161869500416','2021-04-24 05:47:23','[调度平台]:[ETL] JOB ,开始发送单源ETL 处理请求,异常请检查zdh_server服务是否正常运行,或者检查网络情况,postRequest -- IO error!','ERROR','835512199047811072'),('835512161869500416','2021-04-24 05:47:23','发送ETL任务到zdh处理引擎,存在问题,重试次数已达到最大,状态设置为error','ERROR','835512199047811072'),('835512161869500416','2021-04-24 05:48:55','[ETL] JOB ,开始检查当前任务上游任务依赖','INFO','835512629526007808'),('835512161869500416','2021-04-24 05:48:55','[ETL] JOB ,是根节点任务,无依赖,直接执行','INFO','835512629526007808'),('835512161869500416','2021-04-24 05:48:56','开始执行[ETL] JOB','INFO','835512629526007808'),('835512161869500416','2021-04-24 05:48:56','[ETL] JOB ,开始检查任务次数限制','INFO','835512629526007808'),('835512161869500416','2021-04-24 05:48:56','[ETL] JOB,任务模式为[时间序列]','INFO','835512629526007808'),('835512161869500416','2021-04-24 05:48:56','[ETL] JOB ,调度命令执行成功,准备发往任务到后台ETL执行','INFO','835512629526007808'),('835512161869500416','2021-04-24 05:48:56','获取服务端url,指定参数:','INFO','835512629526007808'),('835512161869500416','2021-04-24 05:48:56','[ETL] JOB ,获取当前的[url]:http://localhost:60001/api/v1/zdh','INFO','835512629526007808'),('835512161869500416','2021-04-24 05:48:56','目前支持日期参数以下模式: {{zdh_date}} => yyyy-MM-dd ,{{zdh_date_nodash}}=> yyyyMMdd ,{{zdh_date_time}}=> yyyy-MM-dd HH:mm:ss,{{zdh_year}}=> 年,{{zdh_month}}=> 月,{{zdh_day}}=> 日,{{zdh_hour}}=>24小时制,{{zdh_minute}}=>分钟,{{zdh_second}}=>秒,{{zdh_time}}=>时间戳','INFO','835512629526007808'),('835512161869500416','2021-04-24 05:48:56','[调度平台]:[ETL] JOB ,开始发送ETL处理请求','DEBUG','835512629526007808'),('835512161869500416','2021-04-24 05:48:56','[调度平台]:http://localhost:60001/api/v1/zdh ,参数:{\"dsi_Input\":{\"data_source_context\":\"zdh_test\",\"data_source_type\":\"JDBC\",\"dbtable\":\"t1\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"id\":\"60\",\"password\":\"123456\",\"paths\":\"\",\"url\":\"jdbc:mysql://127.0.0.1:3306/zdh_test?serverTimezone=GMT%2B8&useSSL=false\",\"user\":\"zyc\"},\"dsi_Output\":{\"data_source_context\":\"zdh_test\",\"data_source_type\":\"JDBC\",\"dbtable\":\"t2\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"id\":\"60\",\"password\":\"123456\",\"paths\":\"t2\",\"url\":\"jdbc:mysql://127.0.0.1:3306/zdh_test?serverTimezone=GMT%2B8&useSSL=false\",\"user\":\"zyc\"},\"etlTaskInfo\":{\"column_data_list\":[{\"column_alias\":\"name\",\"column_expr\":\"name\",\"column_md5\":\"fe2-44e5-bcd4-2d\",\"column_name\":\"name\",\"column_type\":\"string\"},{\"column_alias\":\"sex\",\"column_expr\":\"sex\",\"column_md5\":\"0bb-4391-8f75-cf\",\"column_name\":\"sex\",\"column_type\":\"string\"},{\"column_alias\":\"job\",\"column_expr\":\"job\",\"column_md5\":\"ced-4702-81b6-0b\",\"column_name\":\"job\",\"column_type\":\"string\"},{\"column_alias\":\"addr\",\"column_expr\":\"addr\",\"column_md5\":\"301-4ace-89b7-34\",\"column_name\":\"addr\",\"column_type\":\"string\"},{\"column_alias\":\"age\",\"column_expr\":\"age\",\"column_md5\":\"9f9-437d-80e7-b1\",\"column_name\":\"age\",\"column_type\":\"string\"}],\"column_datas\":\"[{\\\"column_md5\\\":\\\"fe2-44e5-bcd4-2d\\\",\\\"column_name\\\":\\\"name\\\",\\\"column_expr\\\":\\\"name\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"name\\\"},{\\\"column_md5\\\":\\\"0bb-4391-8f75-cf\\\",\\\"column_name\\\":\\\"sex\\\",\\\"column_expr\\\":\\\"sex\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"sex\\\"},{\\\"column_md5\\\":\\\"ced-4702-81b6-0b\\\",\\\"column_name\\\":\\\"job\\\",\\\"column_expr\\\":\\\"job\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"job\\\"},{\\\"column_md5\\\":\\\"301-4ace-89b7-34\\\",\\\"column_name\\\":\\\"addr\\\",\\\"column_expr\\\":\\\"addr\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"addr\\\"},{\\\"column_md5\\\":\\\"9f9-437d-80e7-b1\\\",\\\"column_name\\\":\\\"age\\\",\\\"column_expr\\\":\\\"age\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"age\\\"}]\",\"column_size\":\"\",\"company\":\"\",\"create_time\":1619243190000,\"data_source_type_input\":\"JDBC\",\"data_source_type_output\":\"JDBC\",\"data_sources_choose_input\":\"60\",\"data_sources_choose_output\":\"60\",\"data_sources_clear_output\":\"drop table t2\",\"data_sources_file_columns\":\"\",\"data_sources_file_name_input\":\"\",\"data_sources_file_name_output\":\"t2\",\"data_sources_filter_input\":\"\",\"data_sources_params_input\":\"\",\"data_sources_params_output\":\"\",\"data_sources_table_columns\":\"name,sex,job,addr,age\",\"data_sources_table_name_input\":\"t1\",\"data_sources_table_name_output\":\"t2\",\"duplicate_columns\":\"\",\"encoding_input\":\"\",\"encoding_output\":\"\",\"error_rate\":\"\",\"etl_context\":\"mysql2mysql\",\"file_type_input\":\"\",\"file_type_output\":\"\",\"header_input\":\"\",\"header_output\":\"\",\"id\":\"835512027848904704\",\"owner\":\"1\",\"primary_columns\":\"\",\"rows_range\":\"\",\"section\":\"\",\"sep_input\":\"\",\"sep_output\":\"\",\"service\":\"\",\"update_context\":\"\"},\"task_logs_id\":\"835512629526007808\",\"tli\":{\"alarm_account\":\"\",\"concurrency\":\"0\",\"count\":1,\"cur_time\":1619243333000,\"depend_level\":\"0\",\"etl_context\":\"mysql2mysql\",\"etl_date\":\"2021-04-24 13:48:53\",\"etl_task_id\":\"835512027848904704\",\"expr\":\"1d\",\"group_context\":\"测试专用\",\"group_id\":\"835512629425344512\",\"id\":\"835512629526007808\",\"interval_time\":\"5\",\"is_disenable\":\"false\",\"is_notice\":\"false\",\"is_retryed\":\"0\",\"job_context\":\"测试专用\",\"job_id\":\"835512161869500416\",\"job_model\":\"1\",\"job_type\":\"ETL\",\"jsmind_data\":\"\",\"last_time\":1619243333000,\"more_task\":\"单源ETL\",\"next_tasks\":\"\",\"owner\":\"1\",\"params\":\"{\\\"ETL_DATE\\\":\\\"2021-04-24 13:48:53\\\"}\",\"plan_count\":\"0\",\"pre_tasks\":\"\",\"priority\":\"5\",\"process\":\"10\",\"process_msg\":\"组装ETL信息\",\"process_time\":\"{\\\"check_dep_time\\\":\\\"2021-04-24 13:48:55\\\"}\",\"process_time2\":{\"check_dep_time\":\"2021-04-24 13:48:55\"},\"retry_time\":946656000000,\"run_jsmind_data\":\"\",\"run_time\":1619243333000,\"server_ack\":\"0\",\"server_id\":\"zdh_web_1:835510610677469184\",\"status\":\"dispatch\",\"step_size\":\"1d\",\"thread_id\":\"1_54_835512629526007808\",\"time_diff\":\"\",\"time_out\":\"86400\",\"update_time\":1619243336064,\"use_quartz_time\":\"off\"}}','DEBUG','835512629526007808'),('835512161869500416','2021-04-24 05:48:56','[调度平台]:[ETL] JOB ,开始发送单源ETL 处理请求,异常请检查zdh_server服务是否正常运行,或者检查网络情况,postRequest -- IO error!','ERROR','835512629526007808'),('835512161869500416','2021-04-24 05:48:56','发送ETL任务到zdh处理引擎,存在问题,重试次数已达到最大,状态设置为error','ERROR','835512629526007808'),('835512161869500416','2021-04-24 05:49:00','更新进度为:100','INFO','835512629425344512'),('835512161869500416','2021-04-24 05:49:00','任务组以失败','INFO','835512629425344512');
/*!40000 ALTER TABLE `zdh_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zdh_nginx`
--

DROP TABLE IF EXISTS `zdh_nginx`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zdh_nginx` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `host` varchar(15) DEFAULT NULL,
  `port` varchar(5) DEFAULT NULL,
  `owner` varchar(10) DEFAULT NULL,
  `tmp_dir` varchar(100) DEFAULT NULL,
  `nginx_dir` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zdh_nginx`
--

LOCK TABLES `zdh_nginx` WRITE;
/*!40000 ALTER TABLE `zdh_nginx` DISABLE KEYS */;
INSERT INTO `zdh_nginx` VALUES (1,'zyc','123456','127.0.0.1','22','1','','/home/zyc/work');
/*!40000 ALTER TABLE `zdh_nginx` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-24 13:53:18
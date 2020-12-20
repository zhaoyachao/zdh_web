-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
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
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT NULL,
  `user_password` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `is_use_email` varchar(10) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `is_use_phone` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
ALTER TABLE account_info MODIFY COLUMN user_name VARCHAR(50) DEFAULT NULL COMMENT '用户名';
ALTER TABLE account_info MODIFY COLUMN user_password VARCHAR(100) DEFAULT NULL COMMENT '密码';
ALTER TABLE account_info MODIFY COLUMN email VARCHAR(100) DEFAULT NULL COMMENT '电子邮箱';
ALTER TABLE account_info MODIFY COLUMN is_use_email VARCHAR(10) DEFAULT NULL COMMENT '是否开启邮箱告警';
ALTER TABLE account_info MODIFY COLUMN phone VARCHAR(11) DEFAULT NULL COMMENT '手机号';
ALTER TABLE account_info MODIFY COLUMN is_use_phone VARCHAR(10) DEFAULT NULL COMMENT '是否开启手机告警';
--
-- Dumping data for table `account_info`
--

LOCK TABLES `account_info` WRITE;
/*!40000 ALTER TABLE `account_info` DISABLE KEYS */;
INSERT INTO `account_info` VALUES (1,'zyc','123456','1209687056@qq.com',NULL,NULL,NULL),(2,'admin','123456','1209687056@qq.com',NULL,NULL,NULL);
/*!40000 ALTER TABLE `account_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `act2`
--

DROP TABLE IF EXISTS `act2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `act2` (
  `id` text,
  `user_name` text,
  `user_password` text,
  `email` text,
  `is_use_email` text,
  `phone` text,
  `is_use_phone` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `act2`
--

--
-- Table structure for table `data_sources_info`
--

DROP TABLE IF EXISTS `data_sources_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `data_sources_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `data_source_context` varchar(100) DEFAULT NULL,
  `data_source_type` varchar(100) DEFAULT NULL,
  `driver` varchar(100) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

ALTER TABLE data_sources_info MODIFY COLUMN data_source_context VARCHAR(100) DEFAULT NULL COMMENT '数据源说明';
ALTER TABLE data_sources_info MODIFY COLUMN data_source_type VARCHAR(100) DEFAULT NULL COMMENT '数据源类型';
ALTER TABLE data_sources_info MODIFY COLUMN driver VARCHAR(100) DEFAULT NULL COMMENT '驱动连接串';
ALTER TABLE data_sources_info MODIFY COLUMN url VARCHAR(100) DEFAULT NULL COMMENT '连接url';
ALTER TABLE data_sources_info MODIFY COLUMN username VARCHAR(100) DEFAULT NULL COMMENT '用户名';
ALTER TABLE data_sources_info MODIFY COLUMN password VARCHAR(100) DEFAULT NULL COMMENT '密码';
ALTER TABLE data_sources_info MODIFY COLUMN owner VARCHAR(100) DEFAULT NULL COMMENT '拥有者';
--
-- Dumping data for table `data_sources_info`
--


--
-- Table structure for table `data_sources_type_info`
--

DROP TABLE IF EXISTS `data_sources_type_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `data_sources_type_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sources_type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

ALTER TABLE data_sources_type_info MODIFY COLUMN sources_type VARCHAR(100) DEFAULT NULL COMMENT '数据源类型';
--
-- Dumping data for table `data_sources_type_info`
--


--
-- Table structure for table `dispatch_task_info`
--

DROP TABLE IF EXISTS `dispatch_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dispatch_task_info` (
  `id` int NOT NULL AUTO_INCREMENT,
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
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etl_drools_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL,
  `etl_id` varchar(200) DEFAULT NULL,
  `etl_drools` text,
  `data_sources_filter_input` text,
  `data_sources_choose_output` varchar(100) DEFAULT NULL,
  `data_source_type_output` varchar(100) DEFAULT NULL,
  `data_sources_table_name_output` varchar(100) DEFAULT NULL,
  `data_sources_file_name_output` varchar(100) DEFAULT NULL,
  `data_sources_params_output` varchar(500) DEFAULT NULL,
  `data_sources_clear_output` varchar(500) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `drop_tmp_tables` varchar(500) DEFAULT NULL,
  `file_type_output` varchar(10) DEFAULT NULL,
  `encoding_output` varchar(10) DEFAULT NULL,
  `sep_output` varchar(10) DEFAULT NULL,
  `header_output` varchar(10) DEFAULT NULL,
  `more_task` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=749279630074056705 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

ALTER TABLE etl_drools_task_info MODIFY COLUMN etl_context VARCHAR(200) DEFAULT NULL COMMENT '任务说明';
ALTER TABLE etl_drools_task_info MODIFY COLUMN etl_id VARCHAR(200) DEFAULT NULL COMMENT '任务id';
ALTER TABLE etl_drools_task_info MODIFY COLUMN etl_drools text COMMENT 'drools任务逻辑';
ALTER TABLE etl_drools_task_info MODIFY COLUMN data_sources_filter_input VARCHAR(100) DEFAULT NULL COMMENT '数据源输入过滤条件';
ALTER TABLE etl_drools_task_info MODIFY COLUMN data_sources_choose_output VARCHAR(100) DEFAULT NULL COMMENT '输出数据源id';
ALTER TABLE etl_drools_task_info MODIFY COLUMN data_source_type_output VARCHAR(100) DEFAULT NULL COMMENT '输出数据源类型';
ALTER TABLE etl_drools_task_info MODIFY COLUMN data_sources_table_name_output VARCHAR(100) DEFAULT NULL COMMENT '输出数据源表名';
ALTER TABLE etl_drools_task_info MODIFY COLUMN data_sources_file_name_output VARCHAR(100) DEFAULT NULL COMMENT '输出数据源文件名';
ALTER TABLE etl_drools_task_info MODIFY COLUMN data_sources_params_output VARCHAR(500) DEFAULT NULL COMMENT '输出数据源参数';
ALTER TABLE etl_drools_task_info MODIFY COLUMN data_sources_clear_output VARCHAR(500) DEFAULT NULL COMMENT '数据源数据源删除条件';
ALTER TABLE etl_drools_task_info MODIFY COLUMN owner VARCHAR(100) DEFAULT NULL COMMENT '拥有者';
ALTER TABLE etl_drools_task_info MODIFY COLUMN create_time timestamp DEFAULT NULL COMMENT '创建时间';
ALTER TABLE etl_drools_task_info MODIFY COLUMN drop_tmp_tables VARCHAR(500) DEFAULT NULL COMMENT '删除临时表名';
ALTER TABLE etl_drools_task_info MODIFY COLUMN file_type_output VARCHAR(10) DEFAULT NULL COMMENT '输出文件类型';
ALTER TABLE etl_drools_task_info MODIFY COLUMN encoding_output VARCHAR(10) DEFAULT NULL COMMENT '输出文件编码';
ALTER TABLE etl_drools_task_info MODIFY COLUMN sep_output VARCHAR(10) DEFAULT NULL COMMENT '输出文件分割符';
ALTER TABLE etl_drools_task_info MODIFY COLUMN header_output VARCHAR(10) DEFAULT NULL COMMENT '输出文件是否带有表头';
ALTER TABLE etl_drools_task_info MODIFY COLUMN more_task VARCHAR(100) DEFAULT NULL COMMENT 'ETL任务类型';

--
-- Dumping data for table `etl_drools_task_info`
--


--
-- Table structure for table `etl_more_task_info`
--

DROP TABLE IF EXISTS `etl_more_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etl_more_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL,
  `etl_ids` varchar(200) DEFAULT NULL,
  `etl_sql` text,
  `data_sources_choose_output` varchar(100) DEFAULT NULL,
  `data_source_type_output` varchar(100) DEFAULT NULL,
  `data_sources_table_name_output` varchar(100) DEFAULT NULL,
  `data_sources_file_name_output` varchar(100) DEFAULT NULL,
  `data_sources_params_output` varchar(500) DEFAULT NULL,
  `data_sources_clear_output` varchar(500) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `drop_tmp_tables` varchar(500) DEFAULT NULL,
  `file_type_output` varchar(10) DEFAULT NULL,
  `encoding_output` varchar(10) DEFAULT NULL,
  `sep_output` varchar(10) DEFAULT NULL,
  `header_output` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

ALTER TABLE etl_more_task_info MODIFY COLUMN etl_context VARCHAR(200) DEFAULT NULL COMMENT '任务说明';
ALTER TABLE etl_more_task_info MODIFY COLUMN etl_ids VARCHAR(200) DEFAULT NULL COMMENT '任务id列表';
ALTER TABLE etl_more_task_info MODIFY COLUMN etl_sql text COMMENT '多源任务逻辑';
ALTER TABLE etl_more_task_info MODIFY COLUMN data_sources_filter_input VARCHAR(100) DEFAULT NULL COMMENT '数据源输入过滤条件';
ALTER TABLE etl_more_task_info MODIFY COLUMN data_sources_choose_output VARCHAR(100) DEFAULT NULL COMMENT '输出数据源id';
ALTER TABLE etl_more_task_info MODIFY COLUMN data_source_type_output VARCHAR(100) DEFAULT NULL COMMENT '输出数据源类型';
ALTER TABLE etl_more_task_info MODIFY COLUMN data_sources_table_name_output VARCHAR(100) DEFAULT NULL COMMENT '输出数据源表名';
ALTER TABLE etl_more_task_info MODIFY COLUMN data_sources_file_name_output VARCHAR(100) DEFAULT NULL COMMENT '输出数据源文件名';
ALTER TABLE etl_more_task_info MODIFY COLUMN data_sources_params_output VARCHAR(500) DEFAULT NULL COMMENT '输出数据源参数';
ALTER TABLE etl_more_task_info MODIFY COLUMN data_sources_clear_output VARCHAR(500) DEFAULT NULL COMMENT '数据源数据源删除条件';
ALTER TABLE etl_more_task_info MODIFY COLUMN owner VARCHAR(100) DEFAULT NULL COMMENT '拥有者';
ALTER TABLE etl_more_task_info MODIFY COLUMN create_time timestamp DEFAULT NULL COMMENT '创建时间';
ALTER TABLE etl_more_task_info MODIFY COLUMN drop_tmp_tables VARCHAR(500) DEFAULT NULL COMMENT '删除临时表名';
ALTER TABLE etl_more_task_info MODIFY COLUMN file_type_output VARCHAR(10) DEFAULT NULL COMMENT '输出文件类型';
ALTER TABLE etl_more_task_info MODIFY COLUMN encoding_output VARCHAR(10) DEFAULT NULL COMMENT '输出文件编码';
ALTER TABLE etl_more_task_info MODIFY COLUMN sep_output VARCHAR(10) DEFAULT NULL COMMENT '输出文件分割符';
ALTER TABLE etl_more_task_info MODIFY COLUMN header_output VARCHAR(10) DEFAULT NULL COMMENT '输出文件是否带有表头';
--
-- Dumping data for table `etl_more_task_info`
--



--
-- Table structure for table `etl_task_info`
--

DROP TABLE IF EXISTS `etl_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etl_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL,
  `data_sources_choose_input` varchar(100) DEFAULT NULL,
  `data_source_type_input` varchar(100) DEFAULT NULL,
  `data_sources_table_name_input` varchar(100) DEFAULT NULL,
  `data_sources_file_name_input` varchar(100) DEFAULT NULL,
  `data_sources_file_columns` text,
  `data_sources_table_columns` text,
  `data_sources_params_input` varchar(500) DEFAULT NULL,
  `data_sources_filter_input` varchar(500) DEFAULT NULL,
  `data_sources_choose_output` varchar(100) DEFAULT NULL,
  `data_source_type_output` varchar(100) DEFAULT NULL,
  `data_sources_table_name_output` varchar(100) DEFAULT NULL,
  `data_sources_file_name_output` varchar(100) DEFAULT NULL,
  `data_sources_params_output` varchar(500) DEFAULT NULL,
  `column_datas` text,
  `data_sources_clear_output` varchar(500) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `company` varchar(100) DEFAULT NULL,
  `section` varchar(100) DEFAULT NULL,
  `service` varchar(100) DEFAULT NULL,
  `update_context` varchar(100) DEFAULT NULL,
  `column_size` varchar(100) DEFAULT NULL,
  `rows_range` varchar(100) DEFAULT NULL,
  `error_rate` varchar(10) DEFAULT NULL,
  `enable_quality` varchar(10) DEFAULT NULL,
  `duplicate_columns` varchar(200) DEFAULT NULL,
  `primary_columns` varchar(100) DEFAULT NULL,
  `file_type_input` varchar(10) DEFAULT NULL,
  `encoding_input` varchar(10) DEFAULT NULL,
  `sep_input` varchar(10) DEFAULT NULL,
  `file_type_output` varchar(10) DEFAULT NULL,
  `encoding_output` varchar(10) DEFAULT NULL,
  `sep_output` varchar(10) DEFAULT NULL,
  `header_input` varchar(10) DEFAULT NULL,
  `header_output` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=749279343477264385 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;


alter table etl_task_info modify column etl_context varchar(200) DEFAULT NULL comment '任务说明';
alter table etl_task_info modify column data_sources_choose_input varchar(100) DEFAULT NULL comment '输入数据源id';
alter table etl_task_info modify column data_source_type_input varchar(100) DEFAULT NULL comment '输入数据源类型';
alter table etl_task_info modify column data_sources_table_name_input varchar(100) DEFAULT NULL comment '输入数据源表名';
alter table etl_task_info modify column data_sources_file_name_input varchar(100) DEFAULT NULL comment '输入数据源文件名';
alter table etl_task_info modify column data_sources_file_columns text comment '输入数据源文件列名';
alter table etl_task_info modify column data_sources_table_columns text comment '输入数据源表列名';
alter table etl_task_info modify column data_sources_params_input varchar(500) DEFAULT NULL comment '输入数据源参数';
alter table etl_task_info modify column data_sources_filter_input varchar(500) DEFAULT NULL comment '输入数据源过滤条件';
alter table etl_task_info modify column data_sources_choose_output varchar(100) DEFAULT NULL comment '输出数据源id';
alter table etl_task_info modify column data_source_type_output varchar(100) DEFAULT NULL comment '输出数据源类型';
alter table etl_task_info modify column data_sources_table_name_output varchar(100) DEFAULT NULL comment '输出数据源表名';
alter table etl_task_info modify column data_sources_file_name_output varchar(100) DEFAULT NULL comment '输出数据源文件名';
alter table etl_task_info modify column data_sources_params_output varchar(500) DEFAULT NULL comment '输出数据源参数';
alter table etl_task_info modify column column_datas text comment '输入输出自定映射关系';
alter table etl_task_info modify column data_sources_clear_output varchar(500) DEFAULT NULL comment '数据源数据源删除条件';
alter table etl_task_info modify column owner varchar(100) DEFAULT NULL comment '拥有者';
alter table etl_task_info modify column create_time timestamp NULL comment '创建时间';
alter table etl_task_info modify column company varchar(100) DEFAULT NULL comment '表所属公司';
alter table etl_task_info modify column section varchar(100) DEFAULT NULL comment '表所属部门';
alter table etl_task_info modify column service varchar(100) DEFAULT NULL comment '表所属服务';
alter table etl_task_info modify column update_context varchar(100) DEFAULT NULL comment '更新说明';
alter table etl_task_info modify column column_size varchar(100) DEFAULT NULL comment '字段个数';
alter table etl_task_info modify column rows_range varchar(100) DEFAULT NULL comment '行数范围';
alter table etl_task_info modify column error_rate varchar(10) DEFAULT NULL comment '错误率';
alter table etl_task_info modify column enable_quality varchar(10) DEFAULT NULL comment '是否开启质量检测';
alter table etl_task_info modify column duplicate_columns varchar(200) DEFAULT NULL comment '去重字段';
alter table etl_task_info modify column primary_columns varchar(100) DEFAULT NULL comment '不可重复字段';
alter table etl_task_info modify column file_type_input varchar(10) DEFAULT NULL comment '输入文件类型';
alter table etl_task_info modify column encoding_input varchar(10) DEFAULT NULL comment '输入文件编码';
alter table etl_task_info modify column sep_input varchar(10) DEFAULT NULL comment '输入分割符';
alter table etl_task_info modify column file_type_output varchar(10) DEFAULT NULL comment '输出文件类型';
alter table etl_task_info modify column encoding_output varchar(10) DEFAULT NULL comment '输出文件编码';
alter table etl_task_info modify column sep_output varchar(10) DEFAULT NULL comment '输出文件分割符';
alter table etl_task_info modify column header_input varchar(10) DEFAULT NULL comment '输入是否包含表头';
alter table etl_task_info modify column header_output varchar(10) DEFAULT NULL comment '输出是否包含表头';

--
-- Dumping data for table `etl_task_info`
--


--
-- Table structure for table `etl_task_meta`
--

DROP TABLE IF EXISTS `etl_task_meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etl_task_meta` (
  `id` int NOT NULL AUTO_INCREMENT,
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
/*!50503 SET character_set_client = utf8mb4 */;
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
-- Table structure for table `issue_data_info`
--

DROP TABLE IF EXISTS `issue_data_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `issue_data_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `issue_context` varchar(200) DEFAULT NULL,
  `data_sources_choose_input` varchar(100) DEFAULT NULL,
  `data_source_type_input` varchar(100) DEFAULT NULL,
  `data_sources_table_name_input` varchar(100) DEFAULT NULL,
  `data_sources_file_name_input` varchar(100) DEFAULT NULL,
  `data_sources_file_columns` text,
  `data_sources_table_columns` text,
  `column_datas` text,
  `owner` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
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



--
-- Table structure for table `jar_file_info`
--

DROP TABLE IF EXISTS `jar_file_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jar_file_info` (
  `id` varchar(20) DEFAULT NULL,
  `file_name` varchar(100) DEFAULT NULL,
  `path` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `jar_etl_id` varchar(20) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

alter table jar_file_info modify column file_name varchar(100) DEFAULT NULL comment '文件名称';
alter table jar_file_info modify column path varchar(100) DEFAULT NULL comment '文件目录';
alter table jar_file_info modify column create_time timestamp NULL comment '创建时间';
alter table jar_file_info modify column jar_etl_id varchar(20) DEFAULT NULL comment '任务id';
alter table jar_file_info modify column owner varchar(100) DEFAULT NULL comment '拥有者';
alter table jar_file_info modify column status varchar(10) DEFAULT NULL comment '文件状态是否删除';

--
-- Dumping data for table `jar_file_info`
--

--
-- Table structure for table `jar_task_info`
--

DROP TABLE IF EXISTS `jar_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jar_task_info` (
  `id` varchar(20) DEFAULT NULL,
  `etl_context` varchar(100) DEFAULT NULL,
  `files` varchar(100) DEFAULT NULL,
  `master` varchar(100) DEFAULT NULL,
  `deploy_mode` varchar(20) DEFAULT NULL,
  `cpu` varchar(100) DEFAULT NULL,
  `memory` varchar(100) DEFAULT NULL,
  `main_class` varchar(100) DEFAULT NULL,
  `spark_submit_params` text,
  `owner` varchar(10) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jar_task_info`
--


--
-- Table structure for table `meta_database_info`
--

DROP TABLE IF EXISTS `meta_database_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meta_database_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `db_name` varchar(200) DEFAULT NULL,
  `tb_name` varchar(100) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_database_info`
--


--
-- Table structure for table `quality`
--

DROP TABLE IF EXISTS `quality`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality` (
  `id` varchar(100) DEFAULT NULL,
  `dispatch_task_id` varchar(100) DEFAULT NULL,
  `etl_task_id` varchar(100) DEFAULT NULL,
  `etl_date` varchar(20) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `report` varchar(500) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

alter table quality modify column dispatch_task_id varchar(100) DEFAULT NULL comment '调度任务id';
alter table quality modify column etl_task_id varchar(100) DEFAULT NULL comment '任务id';
alter table quality modify column etl_date varchar(20) DEFAULT NULL comment '数据处理日期';
alter table quality modify column status varchar(20) DEFAULT NULL comment '报告状态';
alter table quality modify column report varchar(500) DEFAULT NULL comment '报告信息';
alter table quality modify column create_time timestamp NULL DEFAULT NULL comment '创建时间';
alter table quality modify column owner varchar(100) DEFAULT NULL comment '拥有者';

--
-- Dumping data for table quality`
--


--
-- Table structure for table `quartz_job_info`
--

DROP TABLE IF EXISTS `quartz_job_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quartz_job_info` (
  `job_id` varchar(100) NOT NULL,
  `job_context` varchar(100) DEFAULT NULL,
  `more_task` varchar(20) DEFAULT NULL,
  `job_type` varchar(100) DEFAULT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `step_size` varchar(100) DEFAULT NULL,
  `job_model` varchar(2) DEFAULT NULL,
  `plan_count` varchar(5) DEFAULT NULL,
  `count` int DEFAULT NULL,
  `command` text,
  `params` text,
  `last_status` varchar(100) DEFAULT NULL,
  `last_time` timestamp NULL DEFAULT NULL,
  `next_time` timestamp NULL DEFAULT NULL,
  `expr` varchar(100) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `ip` varchar(100) DEFAULT NULL,
  `user` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `etl_task_id` varchar(100) DEFAULT NULL,
  `etl_context` varchar(100) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `is_script` varchar(10) DEFAULT NULL,
  `job_ids` varchar(500) DEFAULT NULL,
  `jump_dep` varchar(10) DEFAULT NULL,
  `jump_script` varchar(10) DEFAULT NULL,
  `interval_time` varchar(20) DEFAULT NULL,
  `alarm_enabled` varchar(10) DEFAULT NULL,
  `email_and_sms` varchar(10) DEFAULT NULL,
  `alarm_account` varchar(500) DEFAULT NULL,
  `task_log_id` varchar(100) DEFAULT NULL,
  `time_out` varchar(100) DEFAULT NULL,
  `priority` varchar(4) DEFAULT NULL,
  `quartz_time` timestamp NULL DEFAULT NULL,
  `use_quartz_time` varchar(5) DEFAULT NULL,
  `time_diff` varchar(50) DEFAULT NULL,
  `jsmind_data` text,
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

alter table quartz_job_info modify column job_id varchar(100) NOT NULL comment '调度任务id,主键';
alter table quartz_job_info modify column job_context varchar(100) DEFAULT NULL comment '调度任务说明';
alter table quartz_job_info modify column more_task varchar(20) DEFAULT NULL comment '废弃字段,不用';
alter table quartz_job_info modify column job_type varchar(100) DEFAULT NULL comment '数据处理都为ETL,告警EMAIL,重试RETRY,检测依赖CHECK';
alter table quartz_job_info modify column start_time timestamp NULL DEFAULT NULL comment '数据处理开始时间';
alter table quartz_job_info modify column end_time timestamp NULL DEFAULT NULL comment '数据处理结束时间';
alter table quartz_job_info modify column step_size varchar(100) DEFAULT NULL comment '步长,自定义时间使用';
alter table quartz_job_info modify column job_model varchar(2) DEFAULT NULL comment '顺时间执行1,执行一次2,重复执行3';
alter table quartz_job_info modify column plan_count varchar(5) DEFAULT NULL comment '计划执行次数';
alter table quartz_job_info modify column count int DEFAULT NULL comment '当前任务执行次数,只做说明,具体判定使用实例表判断';
alter table quartz_job_info modify column command text comment 'shell命令,jdbc命令,当前字段以废弃';
alter table quartz_job_info modify column params text comment '自定义参数';
alter table quartz_job_info modify column last_status varchar(100) DEFAULT NULL comment '上次任务执行状态,已废弃';
alter table quartz_job_info modify column last_time timestamp NULL DEFAULT NULL comment '上次任务执行数据处理时间';
alter table quartz_job_info modify column next_time timestamp NULL DEFAULT NULL comment '下次任务执行数据处理时间';
alter table quartz_job_info modify column expr varchar(100) DEFAULT NULL comment 'cron表达式/自定义表达式';
alter table quartz_job_info modify column status varchar(100) DEFAULT NULL comment '调度任务状态,create,running,pause,finish,remove,error';
alter table quartz_job_info modify column ip varchar(100) DEFAULT NULL comment '服务器ip地址';
alter table quartz_job_info modify column user varchar(100) DEFAULT NULL comment '服务器用户名';
alter table quartz_job_info modify column password varchar(100) DEFAULT NULL comment '服务器密码';
alter table quartz_job_info modify column etl_task_id varchar(100) DEFAULT NULL comment '具体ETL任务id,已废弃';
alter table quartz_job_info modify column etl_context varchar(100) DEFAULT NULL comment '具体ETL任务说明,已废弃';
alter table quartz_job_info modify column owner varchar(100) DEFAULT NULL comment '拥有者';
alter table quartz_job_info modify column is_script varchar(10) DEFAULT NULL comment '是否以脚本方式执行command,已废弃';
alter table quartz_job_info modify column job_ids varchar(500) DEFAULT NULL comment '依赖的调度任务id';
alter table quartz_job_info modify column jump_dep varchar(10) DEFAULT NULL comment '是否跳过依赖';
alter table quartz_job_info modify column jump_script varchar(10) DEFAULT NULL comment '是否跳过脚本,已废弃';
alter table quartz_job_info modify column interval_time varchar(20) DEFAULT NULL comment '重试时间间隔';
alter table quartz_job_info modify column alarm_enabled varchar(10) DEFAULT NULL comment '启用告警';
alter table quartz_job_info modify column email_and_sms varchar(10) DEFAULT NULL comment '启用邮箱+短信告警';
alter table quartz_job_info modify column alarm_account varchar(500) DEFAULT NULL comment '告警zdh账户';
alter table quartz_job_info modify column task_log_id varchar(100) DEFAULT NULL comment '上次任务id,已废弃';
alter table quartz_job_info modify column time_out varchar(100) DEFAULT NULL comment '超时时间';
alter table quartz_job_info modify column priority varchar(4) DEFAULT NULL comment '任务优先级';
alter table quartz_job_info modify column quartz_time timestamp NULL DEFAULT NULL comment 'quartz调度时间';
alter table quartz_job_info modify column use_quartz_time varchar(5) DEFAULT NULL comment '是否使用quartz 调度时间';
alter table quartz_job_info modify column time_diff varchar(50) DEFAULT NULL comment '后退时间差';
alter table quartz_job_info modify column jsmind_data text comment '任务血源关系';
--
-- Dumping data for table `quartz_job_info`
--


--
-- Table structure for table `sql_task_info`
--

DROP TABLE IF EXISTS `sql_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sql_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sql_context` varchar(200) DEFAULT NULL,
  `data_sources_choose_input` varchar(100) DEFAULT NULL,
  `data_source_type_input` varchar(100) DEFAULT NULL,
  `data_sources_params_input` varchar(500) DEFAULT NULL,
  `etl_sql` text,
  `data_sources_choose_output` varchar(100) DEFAULT NULL,
  `data_source_type_output` varchar(100) DEFAULT NULL,
  `data_sources_table_name_output` varchar(100) DEFAULT NULL,
  `data_sources_file_name_output` varchar(100) DEFAULT NULL,
  `data_sources_params_output` varchar(500) DEFAULT NULL,
  `data_sources_clear_output` varchar(500) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `company` varchar(100) DEFAULT NULL,
  `section` varchar(100) DEFAULT NULL,
  `service` varchar(100) DEFAULT NULL,
  `update_context` varchar(100) DEFAULT NULL,
  `file_type_output` varchar(10) DEFAULT NULL,
  `encoding_output` varchar(10) DEFAULT NULL,
  `sep_output` varchar(10) DEFAULT NULL,
  `header_output` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=756934940771225601 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

alter table sql_task_info modify column sql_context varchar(200) comment '任务说明';
alter table sql_task_info modify column data_sources_choose_input varchar(100) comment '输入数据源id';
alter table sql_task_info modify column data_source_type_input varchar(100) comment '输入数据源类型';
alter table sql_task_info modify column data_sources_params_input varchar(500) comment '输入数据源参数';
alter table sql_task_info modify column etl_sql text comment 'sql任务处理逻辑';
alter table sql_task_info modify column data_sources_choose_output varchar(100) comment '输出数据源id';
alter table sql_task_info modify column data_source_type_output varchar(100) comment '输出数据源类型';
alter table sql_task_info modify column data_sources_table_name_output varchar(100) comment '输出数据源表名';
alter table sql_task_info modify column data_sources_file_name_output varchar(100) comment '输出数据源文件名';
alter table sql_task_info modify column data_sources_params_output varchar(500) comment '输出数据源参数';
alter table sql_task_info modify column data_sources_clear_output varchar(500) comment '数据源数据源删除条件';
alter table sql_task_info modify column owner varchar(100) comment '拥有者';
alter table sql_task_info modify column create_time timestamp NULL comment '创建时间';
alter table sql_task_info modify column company varchar(100) comment '表所属公司';
alter table sql_task_info modify column section varchar(100) comment '表所属部门';
alter table sql_task_info modify column service varchar(100) comment '表所属服务';
alter table sql_task_info modify column update_context varchar(100) comment '更新说明';
alter table sql_task_info modify column file_type_output varchar(10) comment '输出文件类型';
alter table sql_task_info modify column encoding_output varchar(10) comment '输出文件编码';
alter table sql_task_info modify column sep_output varchar(10) comment '输出文件分割符';
alter table sql_task_info modify column header_output varchar(10) comment '输出是否包含表头';

--
-- Dumping data for table `sql_task_info`
--


--
-- Table structure for table `ssh_task_info`
--

DROP TABLE IF EXISTS `ssh_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ssh_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ssh_context` varchar(200) DEFAULT NULL,
  `host` varchar(100) DEFAULT NULL,
  `port` varchar(100) DEFAULT NULL,
  `user_name` varchar(500) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `ssh_cmd` text,
  `ssh_script_path` varchar(100) DEFAULT NULL,
  `ssh_script_context` text,
  `ssh_params_input` varchar(500) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `company` varchar(100) DEFAULT NULL,
  `section` varchar(100) DEFAULT NULL,
  `service` varchar(100) DEFAULT NULL,
  `update_context` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=749064500069535745 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

alter table ssh_task_info modify column ssh_context varchar(200) comment '任务说明';
alter table ssh_task_info host varchar(100) DEFAULT NULL comment '远程服务器地址';
alter table ssh_task_info port varchar(100) DEFAULT NULL comment '远程服务器端口';
alter table ssh_task_info user_name varchar(500) DEFAULT NULL comment '远程服务器用户名';
alter table ssh_task_info password varchar(100) DEFAULT NULL comment '远程服务器密码';
alter table ssh_task_info ssh_cmd text comment 'shell命令';
alter table ssh_task_info ssh_script_path varchar(100) DEFAULT NULL comment 'shell 执行命令目录';
alter table ssh_task_info ssh_script_context text comment '在线shell 脚本';
alter table ssh_task_info ssh_params_input varchar(500) DEFAULT NULL comment '自定义参数输入';
alter table ssh_task_info modify column owner varchar(100) comment '拥有者';
alter table ssh_task_info modify column create_time timestamp NULL comment '创建时间';
alter table ssh_task_info modify column company varchar(100) comment '表所属公司';
alter table ssh_task_info modify column section varchar(100) comment '表所属部门';
alter table ssh_task_info modify column service varchar(100) comment '表所属服务';
alter table ssh_task_info modify column update_context varchar(100) comment '更新说明';

--
-- Dumping data for table `ssh_task_info`
--


--
-- Table structure for table `task_group_log_instance`
--

DROP TABLE IF EXISTS `task_group_log_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_group_log_instance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `job_id` varchar(100) DEFAULT NULL,
  `job_context` varchar(100) DEFAULT NULL,
  `etl_date` varchar(30) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `run_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `is_notice` varchar(10) DEFAULT NULL,
  `process` varchar(10) DEFAULT NULL,
  `thread_id` varchar(100) DEFAULT NULL,
  `retry_time` timestamp NULL DEFAULT NULL,
  `executor` varchar(100) DEFAULT NULL,
  `etl_info` text,
  `url` varchar(100) DEFAULT NULL,
  `application_id` varchar(100) DEFAULT NULL,
  `history_server` varchar(100) DEFAULT NULL,
  `master` varchar(100) DEFAULT NULL,
  `server_ack` varchar(100) DEFAULT NULL,
  `concurrency` varchar(100) DEFAULT NULL,
  `last_task_log_id` varchar(100) DEFAULT NULL,
  `more_task` varchar(20) DEFAULT NULL,
  `job_type` varchar(100) DEFAULT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `step_size` varchar(100) DEFAULT NULL,
  `job_model` varchar(2) DEFAULT NULL,
  `plan_count` varchar(5) DEFAULT NULL,
  `count` int DEFAULT NULL,
  `command` varchar(100) DEFAULT NULL,
  `params` text,
  `last_status` varchar(100) DEFAULT NULL,
  `last_time` timestamp NULL DEFAULT NULL,
  `next_time` timestamp NULL DEFAULT NULL,
  `expr` varchar(100) DEFAULT NULL,
  `ip` varchar(100) DEFAULT NULL,
  `user` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `etl_task_id` varchar(100) DEFAULT NULL,
  `etl_context` varchar(100) DEFAULT NULL,
  `is_script` varchar(100) DEFAULT NULL,
  `job_ids` varchar(100) DEFAULT NULL,
  `jump_dep` varchar(100) DEFAULT NULL,
  `jump_script` varchar(100) DEFAULT NULL,
  `interval_time` varchar(100) DEFAULT NULL,
  `alarm_enabled` varchar(100) DEFAULT NULL,
  `email_and_sms` varchar(100) DEFAULT NULL,
  `alarm_account` varchar(200) DEFAULT NULL,
  `cur_time` timestamp NULL DEFAULT NULL,
  `is_retryed` varchar(4) DEFAULT NULL,
  `server_id` varchar(100) DEFAULT NULL,
  `time_out` varchar(100) DEFAULT NULL,
  `process_time` text,
  `priority` varchar(4) DEFAULT NULL,
  `quartz_time` timestamp NULL DEFAULT NULL,
  `use_quartz_time` varchar(5) DEFAULT NULL,
  `time_diff` varchar(50) DEFAULT NULL,
  `jsmind_data` text,
  `run_jsmind_data` text,
   `next_tasks` text,
  `pre_tasks` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=789666625656721409 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;


alter table task_group_log_instance modify column job_id varchar(100) DEFAULT NULL comment '调度任务id';
alter table task_group_log_instance modify column job_context varchar(100) DEFAULT NULL comment '调度任务说明';
alter table task_group_log_instance modify column etl_date varchar(30) DEFAULT NULL comment '数据处理日期';
alter table task_group_log_instance modify column status varchar(50) DEFAULT NULL comment '当前任务实例状态';
alter table task_group_log_instance modify column run_time timestamp NULL DEFAULT NULL comment '实例开始执行时间';
alter table task_group_log_instance modify column update_time timestamp NULL DEFAULT NULL comment '实例更新时间';
alter table task_group_log_instance modify column owner varchar(100) DEFAULT NULL comment '拥有者';
alter table task_group_log_instance modify column is_notice varchar(10) DEFAULT NULL comment '是否通知过标识';
alter table task_group_log_instance modify column process varchar(10) DEFAULT NULL comment '进度';
alter table task_group_log_instance modify column thread_id varchar(100) DEFAULT NULL comment '线程id';
alter table task_group_log_instance modify column retry_time timestamp NULL DEFAULT NULL comment '重试时间';
alter table task_group_log_instance modify column executor varchar(100) DEFAULT NULL comment '执行器id';
alter table task_group_log_instance modify column etl_info text comment '具体执行任务信息,已废弃';
alter table task_group_log_instance modify column url varchar(100) DEFAULT NULL comment '发送执行url,已废弃';
alter table task_group_log_instance modify column application_id varchar(100) DEFAULT NULL comment '数据采集端执行器标识';
alter table task_group_log_instance modify column history_server varchar(100) DEFAULT NULL comment '历史服务器';
alter table task_group_log_instance modify column master varchar(100) DEFAULT NULL comment 'spark 任务模式';
alter table task_group_log_instance modify column server_ack varchar(100) DEFAULT NULL comment '数据采集端确认标识';
alter table task_group_log_instance modify column concurrency varchar(100) DEFAULT NULL comment '是否并行0:串行,1:并行';
alter table task_group_log_instance modify column last_task_log_id varchar(100) DEFAULT NULL comment '上次任务id,已废弃';
alter table task_group_log_instance modify column more_task varchar(20) DEFAULT NULL comment '任务类型,已废弃';
alter table task_group_log_instance modify column job_type varchar(100) DEFAULT NULL comment '数据处理都为ETL,告警EMAIL,重试RETRY,检测依赖CHECK,';
alter table task_group_log_instance modify column start_time timestamp NULL DEFAULT NULL comment '开始时间';
alter table task_group_log_instance modify column end_time timestamp NULL DEFAULT NULL comment '结束时间';
alter table task_group_log_instance modify column step_size varchar(100) DEFAULT NULL comment '步长';
alter table task_group_log_instance modify column job_model varchar(2) DEFAULT NULL comment '顺时间执行1,执行一次2,重复执行3';
alter table task_group_log_instance modify column plan_count varchar(5) DEFAULT NULL comment '计划执行次数';
alter table task_group_log_instance modify column count int DEFAULT NULL comment '当前任务执行次数,只做说明,具体判定使用实例表判断';
alter table task_group_log_instance modify column command text comment 'shell命令,jdbc命令,当前字段以废弃';
alter table task_group_log_instance modify column params text comment '自定义参数';
alter table task_group_log_instance modify column last_status varchar(100) DEFAULT NULL comment '上次任务执行状态,已废弃';
alter table task_group_log_instance modify column last_time timestamp NULL DEFAULT NULL comment '上次任务执行数据处理时间';
alter table task_group_log_instance modify column next_time timestamp NULL DEFAULT NULL comment '下次任务执行数据处理时间';
alter table task_group_log_instance modify column expr varchar(100) DEFAULT NULL comment 'cron表达式/自定义表达式';
alter table task_group_log_instance modify column ip varchar(100) DEFAULT NULL comment '服务器ip地址';
alter table task_group_log_instance modify column user varchar(100) DEFAULT NULL comment '服务器用户名';
alter table task_group_log_instance modify column password varchar(100) DEFAULT NULL comment '服务器密码';
alter table task_group_log_instance modify column etl_task_id varchar(100) DEFAULT NULL comment '具体ETL任务id,已废弃';
alter table task_group_log_instance modify column etl_context varchar(100) DEFAULT NULL comment '具体ETL任务说明,已废弃';
alter table task_group_log_instance modify column is_script varchar(10) DEFAULT NULL comment '是否以脚本方式执行command,已废弃';
alter table task_group_log_instance modify column job_ids varchar(500) DEFAULT NULL comment '依赖的调度任务id';
alter table task_group_log_instance modify column jump_dep varchar(10) DEFAULT NULL comment '是否跳过依赖';
alter table task_group_log_instance modify column jump_script varchar(10) DEFAULT NULL comment '是否跳过脚本,已废弃';
alter table task_group_log_instance modify column interval_time varchar(20) DEFAULT NULL comment '重试时间间隔';
alter table task_group_log_instance modify column alarm_enabled varchar(10) DEFAULT NULL comment '启用告警';
alter table task_group_log_instance modify column email_and_sms varchar(10) DEFAULT NULL comment '启用邮箱+短信告警';
alter table task_group_log_instance modify column alarm_account varchar(500) DEFAULT NULL comment '告警zdh账户';
alter table task_group_log_instance modify column cur_time timestamp NULL DEFAULT NULL comment '当前实例数据时间';
alter table task_group_log_instance modify column is_retryed varchar(4) DEFAULT NULL comment '是否重试过';
alter table task_group_log_instance modify column server_id varchar(100) DEFAULT NULL comment '调度执行端执行器标识';
alter table task_group_log_instance modify column time_out varchar(100) DEFAULT NULL comment '超时时间';
alter table task_group_log_instance modify column process_time text comment '流程时间';
alter table task_group_log_instance modify column priority varchar(4) DEFAULT NULL comment '任务优先级';
alter table task_group_log_instance modify column quartz_time timestamp NULL DEFAULT NULL comment 'quartz调度时间';
alter table task_group_log_instance modify column use_quartz_time varchar(5) DEFAULT NULL comment '是否使用quartz 调度时间';
alter table task_group_log_instance modify column time_diff varchar(50) DEFAULT NULL comment '后退时间差';
alter table task_group_log_instance modify column jsmind_data text comment '任务血源关系';
alter table task_group_log_instance modify column run_jsmind_data text comment '生成实例血源关系';
alter table task_group_log_instance modify column next_tasks text comment '下游任务组实例id';
alter table task_group_log_instance modify column pre_tasks text comment '上游任务组实例id';

--
-- Dumping data for table `task_group_log_instance`
--

--
-- Table structure for table `task_log_instance`
--

DROP TABLE IF EXISTS `task_log_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_log_instance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `job_id` varchar(100) DEFAULT NULL,
  `job_context` varchar(100) DEFAULT NULL,
  `group_id` varchar(100) DEFAULT NULL,
  `group_context` varchar(500) DEFAULT NULL,
  `etl_date` varchar(30) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `run_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `is_notice` varchar(10) DEFAULT NULL,
  `process` varchar(10) DEFAULT NULL,
  `thread_id` varchar(100) DEFAULT NULL,
  `retry_time` timestamp NULL DEFAULT NULL,
  `executor` varchar(100) DEFAULT NULL,
  `etl_info` text,
  `url` varchar(100) DEFAULT NULL,
  `application_id` varchar(100) DEFAULT NULL,
  `history_server` varchar(100) DEFAULT NULL,
  `master` varchar(100) DEFAULT NULL,
  `server_ack` varchar(100) DEFAULT NULL,
  `concurrency` varchar(100) DEFAULT NULL,
  `last_task_log_id` varchar(100) DEFAULT NULL,
  `more_task` varchar(20) DEFAULT NULL,
  `job_type` varchar(100) DEFAULT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `step_size` varchar(100) DEFAULT NULL,
  `job_model` varchar(2) DEFAULT NULL,
  `plan_count` varchar(5) DEFAULT NULL,
  `count` int DEFAULT NULL,
  `command` varchar(100) DEFAULT NULL,
  `params` text,
  `last_status` varchar(100) DEFAULT NULL,
  `last_time` timestamp NULL DEFAULT NULL,
  `next_time` timestamp NULL DEFAULT NULL,
  `expr` varchar(100) DEFAULT NULL,
  `ip` varchar(100) DEFAULT NULL,
  `user` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `etl_task_id` varchar(100) DEFAULT NULL,
  `etl_context` varchar(100) DEFAULT NULL,
  `is_script` varchar(100) DEFAULT NULL,
  `job_ids` varchar(100) DEFAULT NULL,
  `jump_dep` varchar(100) DEFAULT NULL,
  `jump_script` varchar(100) DEFAULT NULL,
  `interval_time` varchar(100) DEFAULT NULL,
  `alarm_enabled` varchar(100) DEFAULT NULL,
  `email_and_sms` varchar(100) DEFAULT NULL,
  `alarm_account` varchar(200) DEFAULT NULL,
  `cur_time` timestamp NULL DEFAULT NULL,
  `is_retryed` varchar(4) DEFAULT NULL,
  `server_id` varchar(100) DEFAULT NULL,
  `time_out` varchar(100) DEFAULT NULL,
  `process_time` text,
  `priority` varchar(4) DEFAULT NULL,
  `quartz_time` timestamp NULL DEFAULT NULL,
  `use_quartz_time` varchar(5) DEFAULT NULL,
  `time_diff` varchar(50) DEFAULT NULL,
  `jsmind_data` text,
  `run_jsmind_data` text,
  `next_tasks` text,
  `pre_tasks` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=789666629087662082 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

alter table task_log_instance modify column job_id varchar(100) DEFAULT NULL comment '调度任务id';
alter table task_log_instance modify column job_context varchar(100) DEFAULT NULL comment '调度任务说明';
alter table task_log_instance modify column group_id varchar(100) DEFAULT NULL comment '调度任务id';
alter table task_log_instance modify column group_context varchar(100) DEFAULT NULL comment '调度任务说明';
alter table task_log_instance modify column etl_date varchar(30) DEFAULT NULL comment '数据处理日期';
alter table task_log_instance modify column status varchar(50) DEFAULT NULL comment '当前任务实例状态';
alter table task_log_instance modify column run_time timestamp NULL DEFAULT NULL comment '实例开始执行时间';
alter table task_log_instance modify column update_time timestamp NULL DEFAULT NULL comment '实例更新时间';
alter table task_log_instance modify column owner varchar(100) DEFAULT NULL comment '拥有者';
alter table task_log_instance modify column is_notice varchar(10) DEFAULT NULL comment '是否通知过标识';
alter table task_log_instance modify column process varchar(10) DEFAULT NULL comment '进度';
alter table task_log_instance modify column thread_id varchar(100) DEFAULT NULL comment '线程id';
alter table task_log_instance modify column retry_time timestamp NULL DEFAULT NULL comment '重试时间';
alter table task_log_instance modify column executor varchar(100) DEFAULT NULL comment '执行器id';
alter table task_log_instance modify column etl_info text comment '具体执行任务信息,已废弃';
alter table task_log_instance modify column url varchar(100) DEFAULT NULL comment '发送执行url,已废弃';
alter table task_log_instance modify column application_id varchar(100) DEFAULT NULL comment '数据采集端执行器标识';
alter table task_log_instance modify column history_server varchar(100) DEFAULT NULL comment '历史服务器';
alter table task_log_instance modify column master varchar(100) DEFAULT NULL comment 'spark 任务模式';
alter table task_log_instance modify column server_ack varchar(100) DEFAULT NULL comment '数据采集端确认标识';
alter table task_log_instance modify column concurrency varchar(100) DEFAULT NULL comment '是否并行0:串行,1:并行';
alter table task_log_instance modify column last_task_log_id varchar(100) DEFAULT NULL comment '上次任务id,已废弃';
alter table task_log_instance modify column more_task varchar(20) DEFAULT NULL comment '任务类型,已废弃';
alter table task_log_instance modify column job_type varchar(100) DEFAULT NULL comment '数据处理都为ETL,告警EMAIL,重试RETRY,检测依赖CHECK,';
alter table task_log_instance modify column start_time timestamp NULL DEFAULT NULL comment '开始时间';
alter table task_log_instance modify column end_time timestamp NULL DEFAULT NULL comment '结束时间';
alter table task_log_instance modify column step_size varchar(100) DEFAULT NULL comment '步长';
alter table task_log_instance modify column job_model varchar(2) DEFAULT NULL comment '顺时间执行1,执行一次2,重复执行3';
alter table task_log_instance modify column plan_count varchar(5) DEFAULT NULL comment '计划执行次数';
alter table task_log_instance modify column count int DEFAULT NULL comment '当前任务执行次数,只做说明,具体判定使用实例表判断';
alter table task_log_instance modify column command text comment 'shell命令,jdbc命令,当前字段以废弃';
alter table task_log_instance modify column params text comment '自定义参数';
alter table task_log_instance modify column last_status varchar(100) DEFAULT NULL comment '上次任务执行状态,已废弃';
alter table task_log_instance modify column last_time timestamp NULL DEFAULT NULL comment '上次任务执行数据处理时间';
alter table task_log_instance modify column next_time timestamp NULL DEFAULT NULL comment '下次任务执行数据处理时间';
alter table task_log_instance modify column expr varchar(100) DEFAULT NULL comment 'cron表达式/自定义表达式';
alter table task_log_instance modify column ip varchar(100) DEFAULT NULL comment '服务器ip地址';
alter table task_log_instance modify column user varchar(100) DEFAULT NULL comment '服务器用户名';
alter table task_log_instance modify column password varchar(100) DEFAULT NULL comment '服务器密码';
alter table task_log_instance modify column etl_task_id varchar(100) DEFAULT NULL comment '具体ETL任务id';
alter table task_log_instance modify column etl_context varchar(100) DEFAULT NULL comment '具体ETL任务说明';
alter table task_log_instance modify column is_script varchar(10) DEFAULT NULL comment '是否以脚本方式执行command';
alter table task_log_instance modify column job_ids varchar(500) DEFAULT NULL comment '依赖的调度任务id';
alter table task_log_instance modify column jump_dep varchar(10) DEFAULT NULL comment '是否跳过依赖';
alter table task_log_instance modify column jump_script varchar(10) DEFAULT NULL comment '是否跳过脚本,已废弃';
alter table task_log_instance modify column interval_time varchar(20) DEFAULT NULL comment '重试时间间隔';
alter table task_log_instance modify column alarm_enabled varchar(10) DEFAULT NULL comment '启用告警';
alter table task_log_instance modify column email_and_sms varchar(10) DEFAULT NULL comment '启用邮箱+短信告警';
alter table task_log_instance modify column alarm_account varchar(500) DEFAULT NULL comment '告警zdh账户';
alter table task_log_instance modify column cur_time timestamp NULL DEFAULT NULL comment '当前实例数据时间';
alter table task_log_instance modify column is_retryed varchar(4) DEFAULT NULL comment '是否重试过';
alter table task_log_instance modify column server_id varchar(100) DEFAULT NULL comment '调度执行端执行器标识';
alter table task_log_instance modify column time_out varchar(100) DEFAULT NULL comment '超时时间';
alter table task_log_instance modify column process_time text comment '流程时间';
alter table task_log_instance modify column priority varchar(4) DEFAULT NULL comment '任务优先级';
alter table task_log_instance modify column quartz_time timestamp NULL DEFAULT NULL comment 'quartz调度时间';
alter table task_log_instance modify column use_quartz_time varchar(5) DEFAULT NULL comment '是否使用quartz 调度时间';
alter table task_log_instance modify column time_diff varchar(50) DEFAULT NULL comment '后退时间差';
alter table task_log_instance modify column jsmind_data text comment '任务血源关系';
alter table task_log_instance modify column run_jsmind_data text comment '生成实例血源关系';
alter table task_log_instance modify column next_tasks text comment '下游任务实例id';
alter table task_log_instance modify column pre_tasks text comment '上游任务实例id';

--
-- Dumping data for table `task_log_instance`
--
--
-- Table structure for table `zdh_nginx`
--

DROP TABLE IF EXISTS `zdh_nginx`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zdh_nginx` (
  `id` int NOT NULL AUTO_INCREMENT,
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

-- Dump completed on 2020-12-19  1:57:29

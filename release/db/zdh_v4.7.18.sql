-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: zdh
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
-- Table structure for table `etl_apply_task_info`
--

DROP TABLE IF EXISTS `etl_apply_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etl_apply_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
  `data_sources_table_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源表名',
  `data_sources_file_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源文件名',
  `data_sources_file_columns` text COMMENT '输入数据源文件列名',
  `data_sources_table_columns` text COMMENT '输入数据源表列名',
  `data_sources_params_input` varchar(500) DEFAULT NULL COMMENT '输入数据源参数',
  `data_sources_filter_input` varchar(500) DEFAULT NULL COMMENT '输入数据源过滤条件',
  `data_sources_choose_output` varchar(100) DEFAULT NULL COMMENT '输出数据源id',
  `data_source_type_output` varchar(100) DEFAULT NULL COMMENT '输出数据源类型',
  `data_sources_table_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源表名',
  `data_sources_file_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源文件名',
  `data_sources_params_output` varchar(500) DEFAULT NULL COMMENT '输出数据源参数',
  `column_datas` text COMMENT '输入输出自定映射关系',
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
  `is_delete` varchar(10) NOT NULL DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `repartition_num_input` varchar(64) NOT NULL DEFAULT '' COMMENT '洗牌个数默认空',
  `repartition_cols_input` varchar(256) NOT NULL DEFAULT '' COMMENT '洗牌字段默认空',
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=924296352974770177 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_apply_task_info`
--

LOCK TABLES `etl_apply_task_info` WRITE;
/*!40000 ALTER TABLE `etl_apply_task_info` DISABLE KEYS */;
INSERT INTO `etl_apply_task_info` VALUES (924296352974770176,'测试日志表','839147351305097216','JDBC','zdh_logs','','','job_id,log_time,msg,level,task_logs_id','','','','','','','','[{\"column_md5\":\"882-42ec-bf2c-64\",\"column_name\":\"job_id\",\"column_expr\":\"job_id\",\"column_type\":\"string\",\"column_alias\":\"job_id\"},{\"column_md5\":\"846-4268-8e3c-31\",\"column_name\":\"log_time\",\"column_expr\":\"log_time\",\"column_type\":\"string\",\"column_alias\":\"log_time\"},{\"column_md5\":\"000-4c9a-a8e8-79\",\"column_name\":\"msg\",\"column_expr\":\"msg\",\"column_type\":\"string\",\"column_alias\":\"msg\"},{\"column_md5\":\"321-47bd-9175-ee\",\"column_name\":\"level\",\"column_expr\":\"level\",\"column_type\":\"string\",\"column_alias\":\"level\"},{\"column_md5\":\"e3c-44a9-bf6d-4f\",\"column_name\":\"task_logs_id\",\"column_expr\":\"task_logs_id\",\"column_type\":\"string\",\"column_alias\":\"task_logs_id\"}]','delete from aaaa','1','2021-12-25 05:43:43','','','','','','','',NULL,'','','','','','','','','','','0','','','','','-1','2022-01-08 03:00:53');
/*!40000 ALTER TABLE `etl_apply_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `server_task_info`
--

DROP TABLE IF EXISTS `server_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `server_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `build_task` varchar(200) DEFAULT NULL COMMENT '构建任务说明',
  `build_ip` varchar(200) DEFAULT NULL COMMENT '构建服务器',
  `git_url` varchar(500) DEFAULT NULL COMMENT 'git地址',
  `build_type` varchar(10) DEFAULT NULL COMMENT '构建工具类型,GRADLE/MAVEN',
  `build_command` text COMMENT '构建命令',
  `remote_ip` varchar(200) DEFAULT NULL COMMENT '部署服务器',
  `remote_path` varchar(200) DEFAULT NULL COMMENT '部署路径',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `build_branch` varchar(200) DEFAULT NULL COMMENT '分支名',
  `build_username` varchar(100) DEFAULT NULL COMMENT '构建用户',
  `build_privatekey` text COMMENT '构建服务器密钥地址',
  `build_path` varchar(500) DEFAULT NULL COMMENT '构建地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `server_task_info`
--

LOCK TABLES `server_task_info` WRITE;
/*!40000 ALTER TABLE `server_task_info` DISABLE KEYS */;
INSERT INTO `server_task_info` VALUES (1,'server构建任务','192.168.110.10','','GRADLE','sh gradlew release -x test','127.0.0.1','/home/zyc/zdh_server_build','2021-10-16 15:26:37','2021-10-16 15:26:37','1','master','zyc','123456','/home/zyc/zdh_server');
/*!40000 ALTER TABLE `server_task_info` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;
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
-- Table structure for table `alarm_sms_info`
--

DROP TABLE IF EXISTS `alarm_sms_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alarm_sms_info`
--

LOCK TABLES `alarm_sms_info` WRITE;
/*!40000 ALTER TABLE `alarm_sms_info` DISABLE KEYS */;
INSERT INTO `alarm_sms_info` VALUES (1,'任务失败通知: 第一个数据质量检测','失败任务:\r\n调度任务:919317519804665856,调度名:第一个数据质量检测\r\n任务组:924357657697980416,任务组名:第一个数据质量检测\r\nETL任务:919326325259374592,ETL任务名:第一个数据质量检测\r\nETL任务类型:QUALITY\r\n任务实例id:924357657869946880,任务实例名:第一个数据质量检测\r\nETL日期:2021-12-25 17:47:12\r\n开始时间:2021-12-25 17:47:19\r\n','通知','log_txt.html?job_id=919317519804665856&task_log_id=924357657869946880',NULL,'0',NULL,NULL),(2,'任务失败通知: 第一个数据质量检测','失败任务:\r\n调度任务:919317519804665856,调度名:第一个数据质量检测\r\n任务组:924387101082914816,任务组名:第一个数据质量检测\r\nETL任务:919326325259374592,ETL任务名:第一个数据质量检测\r\nETL任务类型:QUALITY\r\n任务实例id:924387101267464192,任务实例名:第一个数据质量检测\r\nETL日期:2021-12-25 19:44:13\r\n开始时间:2021-12-25 19:44:19\r\n','通知','log_txt.html?job_id=919317519804665856&task_log_id=924387101267464192','15236479806','0',NULL,NULL),(3,'任务失败通知: 第一个数据质量检测','失败任务:\r\n调度任务:919317519804665856,调度名:第一个数据质量检测\r\n任务组:924619803774160896,任务组名:第一个数据质量检测\r\nETL任务:919326325259374592,ETL任务名:第一个数据质量检测\r\nETL任务类型:QUALITY\r\n任务实例id:924619803946127360,任务实例名:第一个数据质量检测\r\nETL日期:2021-12-26 11:08:58\r\n开始时间:2021-12-26 11:09:00\r\n','通知','log_txt.html?job_id=919317519804665856&task_log_id=924619803946127360','15236479806','0','2021-12-26 03:09:22','2021-12-26 03:09:22');
/*!40000 ALTER TABLE `alarm_sms_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etl_task_jdbc_info`
--

DROP TABLE IF EXISTS `etl_task_jdbc_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etl_task_jdbc_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `engine_type` varchar(200) DEFAULT NULL COMMENT '计算引擎,spark,local',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
  `etl_sql` text COMMENT 'sql任务处理逻辑',
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
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=914511685971087361 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_task_jdbc_info`
--

LOCK TABLES `etl_task_jdbc_info` WRITE;
/*!40000 ALTER TABLE `etl_task_jdbc_info` DISABLE KEYS */;
INSERT INTO `etl_task_jdbc_info` VALUES (914511685971087360,'第一个jdbc','local','58','JDBC','insert into notice select a.* from notice2 where event_day={{zdh_day}}','','','','','','','1','2021-11-28 05:42:57','','','','','','','','','','','-1','2022-01-15 15:15:52','0');
/*!40000 ALTER TABLE `etl_task_jdbc_info` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_database_info`
--

LOCK TABLES `meta_database_info` WRITE;
/*!40000 ALTER TABLE `meta_database_info` DISABLE KEYS */;
INSERT INTO `meta_database_info` VALUES (10,'default','act','1','2021-01-31 06:27:37');
/*!40000 ALTER TABLE `meta_database_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_group_log_instance`
--

DROP TABLE IF EXISTS `task_group_log_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_group_log_instance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
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
  `etl_info` text COMMENT '具体执行任务信息,已废弃',
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
  `count` int DEFAULT NULL COMMENT '当前任务执行次数,只做说明,具体判定使用实例表判断',
  `command` text COMMENT 'shell命令,jdbc命令,当前字段以废弃',
  `params` text COMMENT '自定义参数',
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
  `process_time` text COMMENT '流程时间',
  `priority` varchar(4) DEFAULT NULL COMMENT '任务优先级',
  `quartz_time` timestamp NULL DEFAULT NULL COMMENT 'quartz调度时间',
  `use_quartz_time` varchar(5) DEFAULT NULL COMMENT '是否使用quartz 调度时间',
  `time_diff` varchar(50) DEFAULT NULL COMMENT '后退时间差',
  `jsmind_data` text COMMENT '任务血源关系',
  `run_jsmind_data` text COMMENT '生成实例血源关系',
  `next_tasks` text COMMENT '下游任务组实例id',
  `pre_tasks` text COMMENT '上游任务组实例id',
  `schedule_source` varchar(64) NOT NULL DEFAULT '1' COMMENT '调度来源,1:例行,2:手动',
  `alarm_email` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `alarm_sms` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `alarm_zdh` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_error` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_finish` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_timeout` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=936974829544804353 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_group_log_instance`
--

LOCK TABLES `task_group_log_instance` WRITE;
/*!40000 ALTER TABLE `task_group_log_instance` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_group_log_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quartz_executor_info`
--

DROP TABLE IF EXISTS `quartz_executor_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quartz_executor_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `instance_name` varchar(200) DEFAULT NULL COMMENT '调度器唯一实例名',
  `status` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `is_handle` varchar(100) DEFAULT NULL COMMENT '是否处理过,true/false',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quartz_executor_info`
--

LOCK TABLES `quartz_executor_info` WRITE;
/*!40000 ALTER TABLE `quartz_executor_info` DISABLE KEYS */;
INSERT INTO `quartz_executor_info` VALUES (1,'scheduler_zdh_DEEP-2020KLZJDI_1','offline','true','2022-01-15 06:32:10','2022-01-15 06:39:47'),(2,'scheduler_zdh_DEEP-2020KLZJDI_1','offline','true','2022-01-15 06:41:11','2022-01-15 06:41:11'),(3,'scheduler_zdh_DEEP-2020KLZJDI_1','online','true','2022-01-15 06:44:45','2022-01-15 06:44:46'),(4,'scheduler_zdh_DEEP-2020KLZJDI_1','offline','true','2022-01-15 07:01:01','2022-01-15 07:01:01'),(5,'scheduler_zdh_DEEP-2020KLZJDI_1','offline','true','2022-01-15 14:45:44','2022-01-15 14:45:44'),(6,'scheduler_zdh_DEEP-2020KLZJDI_1','online','true','2022-01-15 14:46:58','2022-01-15 14:46:59'),(7,'scheduler_zdh_DEEP-2020KLZJDI_1','offline','true','2022-01-18 07:54:38','2022-01-18 07:54:39'),(8,'scheduler_zdh_DEEP-2020KLZJDI_1','online','true','2022-01-18 08:41:18','2022-01-18 08:41:21'),(9,'scheduler_zdh_DEEP-2020KLZJDI_1','offline','true','2022-01-18 08:56:30','2022-01-18 08:56:32'),(10,'scheduler_zdh_DEEP-2020KLZJDI_1','online','true','2022-01-18 08:58:11','2022-01-18 08:58:12');
/*!40000 ALTER TABLE `quartz_executor_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zdh_ha_info`
--

DROP TABLE IF EXISTS `zdh_ha_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zdh_ha_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `zdh_instance` varchar(100) DEFAULT NULL,
  `zdh_url` varchar(100) DEFAULT NULL,
  `zdh_host` varchar(15) DEFAULT NULL,
  `zdh_port` varchar(5) DEFAULT NULL,
  `web_port` varchar(100) DEFAULT NULL,
  `zdh_status` varchar(10) DEFAULT NULL,
  `application_id` varchar(100) DEFAULT NULL,
  `history_server` varchar(100) DEFAULT NULL,
  `master` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `online` varchar(10) DEFAULT NULL COMMENT '是否上线true:上线,false:未上线',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=379 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zdh_ha_info`
--

LOCK TABLES `zdh_ha_info` WRITE;
/*!40000 ALTER TABLE `zdh_ha_info` DISABLE KEYS */;
INSERT INTO `zdh_ha_info` VALUES (372,'zdh_server','http://localhost:60001/api/v1/zdh','localhost','60001','4040','enabled','local-1641913474698','http://127.0.0.1:18080/api/v1','local[*]','2022-01-11 15:04:32','2022-01-29 05:27:28','2'),(378,'zdh_server','http://DEEP-2020KLZJDI:60001/api/v1/zdh','DEEP-2020KLZJDI','60001','4040','enabled','local-1643432946014','http://127.0.0.1:18080/api/v1','local[*]','2022-01-29 05:09:06','2022-01-29 05:27:28','1');
/*!40000 ALTER TABLE `zdh_ha_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `approval_config_info`
--

DROP TABLE IF EXISTS `approval_config_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `approval_config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL DEFAULT '0' COMMENT '审批流程code',
  `code_name` varchar(128) NOT NULL DEFAULT '' COMMENT '审批流程名称',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0 单人审批；1 多人审批。单人审批，意思是同一级审批只要有审批人审批后，其他人默认审批。多人审批，必须是同一级所以人审批，才进行下一步审批节点',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `employee_id` varchar(64) NOT NULL DEFAULT '' COMMENT '发布人id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `approval_config_info`
--

LOCK TABLES `approval_config_info` WRITE;
/*!40000 ALTER TABLE `approval_config_info` DISABLE KEYS */;
INSERT INTO `approval_config_info` VALUES (1,'first_code2','第一个审批节点',0,NULL,'1'),(2,'data','数据审批',1,'2021-10-04 11:23:58','1'),(3,'a','d',1,NULL,'1');
/*!40000 ALTER TABLE `approval_config_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_operate_log`
--

DROP TABLE IF EXISTS `user_operate_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_operate_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `user_name` varchar(500) DEFAULT NULL COMMENT '用户名',
  `operate_url` varchar(500) DEFAULT NULL COMMENT '操作url',
  `operate_context` varchar(500) DEFAULT NULL COMMENT '操作说明',
  `operate_input` text COMMENT '输入参数',
  `operate_output` text COMMENT '输出结果',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11313 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_operate_log`
--

LOCK TABLES `user_operate_log` WRITE;
/*!40000 ALTER TABLE `user_operate_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_operate_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etl_task_info`
--

DROP TABLE IF EXISTS `etl_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etl_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
  `data_sources_table_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源表名',
  `data_sources_file_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源文件名',
  `data_sources_file_columns` text COMMENT '输入数据源文件列名',
  `data_sources_table_columns` text COMMENT '输入数据源表列名',
  `data_sources_params_input` varchar(500) DEFAULT NULL COMMENT '输入数据源参数',
  `data_sources_filter_input` varchar(500) DEFAULT NULL COMMENT '输入数据源过滤条件',
  `data_sources_choose_output` varchar(100) DEFAULT NULL COMMENT '输出数据源id',
  `data_source_type_output` varchar(100) DEFAULT NULL COMMENT '输出数据源类型',
  `data_sources_table_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源表名',
  `data_sources_file_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源文件名',
  `data_sources_params_output` varchar(500) DEFAULT NULL COMMENT '输出数据源参数',
  `column_datas` text COMMENT '输入输出自定映射关系',
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
  `repartition_num_input` varchar(64) NOT NULL DEFAULT '' COMMENT '洗牌个数默认空',
  `repartition_cols_input` varchar(256) NOT NULL DEFAULT '' COMMENT '洗牌字段默认空',
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=936967307782000641 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_task_info`
--

LOCK TABLES `etl_task_info` WRITE;
/*!40000 ALTER TABLE `etl_task_info` DISABLE KEYS */;
INSERT INTO `etl_task_info` VALUES (719619870378954752,'单分割符无标题','59','HDFS','','/data/csv/t1.txt','name,sex,job,addr,age','','','','60','JDBC','t1','t1','','[{\"column_md5\":\"88c-4d33-9cbb-e3\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"ede-4385-b7ff-7f\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"9e6-442d-9a86-04\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"42c-43f9-9077-97\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"723-4b22-a043-1c\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','drop table t1','1','2020-06-08 10:32:27','','','','','','1-100','',NULL,'','','csv','GBK','|','','','','false','','','','','','-1','2021-12-25 00:42:20','1'),(719629297702146048,'单分割符自带标题','59','HDFS','','/data/csv/h1.txt','','','{\r\n    \"zdh\":\"2.4.4\"\r\n}','name=\'zyc\'','60','JDBC','h1','h1','{\r\n    \"a\":12\r\n}','[]','delete from h1','1','2020-06-08 11:09:55','','','','','','','',NULL,'','','csv','GBK','|','','','','true','','','','','','-1','2022-01-29 04:16:41','0'),(719630143433216000,'多分割符无标题','59','HDFS','','/data/csv/t2.txt','name,sex,job,addr,age','','','','60','JDBC','t2','t2','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"},{\"column_md5\":\"462-4b77-a7ac-48\",\"column_name\":\"\",\"column_expr\":\"$zdh_etl_date\",\"column_type\":\"string\",\"column_alias\":\"etl_date\"}]','drop table t2','1','2020-06-08 11:13:16','','','','','','','',NULL,'','','csv','GBK','|+','','','','','','','','','','-1','2022-01-02 08:46:34','0'),(719630908637843456,'tab分割无标题','59','HDFS','','/data/csv/t3.txt','name,sex,job,addr,age','','{\"sep\":\"	\",\"encoding\":\"GBK\"}','','60','JDBC','t3','t3','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','delete from t3','1','2020-06-08 11:16:19','','','','','','','',NULL,'','','','','','','','','','','','','','','-1','2022-01-08 02:37:00','0'),(724291182208749568,'单分割符无标题输出hudi','59','HDFS','','/data/csv/t1.txt','name,sex,job,addr,age','','','','59','HDFS','','/data/hudi/t1','{\"precombine_field_opt_key\":\"name\",\"recordkey_field_opt_key\":\"name\"}','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','','1','2020-06-21 07:54:35','','','','','','1-100','',NULL,'','','csv','GBK','|','hudi','','','','','','','','','-1','2022-01-29 07:26:10','0'),(724312011898359808,'hudi输出多分割符无标题','59','HDFS','','/data/hudi/t1','name,sex,job,addr,age','','','','59','HDFS','','/data/csv/hudi_t1','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','','1','2020-06-21 09:17:21','','','','','','1-100','',NULL,'','','hudi','','','csv','UTF-8','+-',NULL,NULL,'','','','','-1',NULL,'0'),(728251791409418240,'kafka接收客户信息存mydb','62','KAFKA','','m1','name,age','','','','60','JDBC','m1','m1','','[{\"column_md5\":\"2c5-46c3-b4e7-96\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"a82-409a-b944-e2\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','','1','2020-07-02 06:12:37','','','','','','','',NULL,'','','','',',','','','','','','','','','','-1',NULL,'0'),(728647407415332864,'单分割符无标题2','59','HDFS','','/data/csv/t4.txt','name,sex,job,addr,age','','','','60','JDBC','t4','t4','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','delete from t4','1','2020-07-03 08:24:40','','','','','','1-100','',NULL,'','','csv','UTF-8','\\\\','','','','false','','','','','','-1',NULL,'0'),(728996795115376640,'单分割符无标题3','59','HDFS','','/data/csv/t5.txt','name,sex,job,addr,age','','','','60','JDBC','t5','t5','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','delete from t5','1','2020-07-04 07:33:00','','','','','','1-100','',NULL,'','','csv','UTF-8','//','','','','false','','','','','','-1',NULL,'0'),(731444359840403456,'clickhouse_datasets.z1转zdh_test.z1','63','JDBC','datasets.z1','','','name,age,sex,money','','','60','JDBC','z1','z1','','[{\"column_md5\":\"80c-4162-bf9a-59\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"fd7-4632-9e75-78\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"},{\"column_md5\":\"bbf-439a-8bfa-19\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"8ef-4d54-84d3-9d\",\"column_name\":\"money\",\"column_expr\":\"money\",\"column_type\":\"string\",\"column_alias\":\"money\"}]','','1','2020-07-11 01:38:45','','','','','','','',NULL,'','','','','','','','','','','','','','','-1',NULL,'0'),(746140955731562496,'sftp测试','65','SFTP','','/home/zyc/work/t1.txt','name,age,sex','','','','60','JDBC','t1','t1','','[{\"column_md5\":\"3fa-444c-9d88-45\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"7bc-44e2-896d-1c\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"},{\"column_md5\":\"ebc-4288-acf9-5e\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"}]','drop table t1','1','2020-08-20 14:57:46','','','','','','','',NULL,'','','csv','UTF-8',',','','','','false','','','','','','-1',NULL,'0'),(749279343477264384,'mydb#account_info','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','','','','','','[{\"column_md5\":\"212-4fac-b1df-1a\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"12f-4e82-8a9c-e3\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"user_name\"},{\"column_md5\":\"116-44b0-b692-15\",\"column_name\":\"user_password\",\"column_expr\":\"user_password\",\"column_type\":\"string\",\"column_alias\":\"user_password\"},{\"column_md5\":\"7eb-4515-9718-98\",\"column_name\":\"email\",\"column_expr\":\"email\",\"column_type\":\"string\",\"column_alias\":\"email\"},{\"column_md5\":\"4b2-45ed-9654-9f\",\"column_name\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_type\":\"string\",\"column_alias\":\"is_use_email\"},{\"column_md5\":\"c1c-44d4-8a8e-35\",\"column_name\":\"phone\",\"column_expr\":\"phone\",\"column_type\":\"string\",\"column_alias\":\"phone\"},{\"column_md5\":\"474-43a6-af20-3b\",\"column_name\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_type\":\"string\",\"column_alias\":\"is_use_phone\"}]','','1','2020-08-29 06:48:36','','','','','','','',NULL,'','','','','','','','','','','','','','','-1',NULL,'0'),(797802796177952768,'测试tidb写入','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','66','TIDB','','d1.t3','','[{\"column_md5\":\"898-4656-945f-00\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"name\"}]','delete from d1.t3','1','2021-01-10 04:23:29','','','','','','','',NULL,'','','','','','','','','','','','','','','-1',NULL,'0'),(797819041442959360,'第一个tidb读取','66','TIDB','','d1.t3','name','','','name=\'{{zdh_date}}\'','60','JDBC','tidb_t3','tidb_t3','','[{\"column_md5\":\"059-44b9-83ea-07\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"}]','delete from tidb_t3','1','2021-01-10 05:28:02','','','','','','','',NULL,'','','','','','','','','','','','','','','-1',NULL,'0'),(818059052008345600,'mysql转hfds测试','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','59','HDFS','','/zdh/data/account_info','','[{\"column_md5\":\"138-46ec-ae83-22\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"9fa-45a3-8834-34\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"user_name\"},{\"column_md5\":\"c2c-432f-a698-13\",\"column_name\":\"user_password\",\"column_expr\":\"user_password\",\"column_type\":\"string\",\"column_alias\":\"user_password\"},{\"column_md5\":\"53a-4f0e-b798-3a\",\"column_name\":\"email\",\"column_expr\":\"email\",\"column_type\":\"string\",\"column_alias\":\"email\"},{\"column_md5\":\"354-41c2-96bc-9c\",\"column_name\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_type\":\"string\",\"column_alias\":\"is_use_email\"},{\"column_md5\":\"b17-4d90-a66c-57\",\"column_name\":\"phone\",\"column_expr\":\"phone\",\"column_type\":\"string\",\"column_alias\":\"phone\"},{\"column_md5\":\"0c4-46ef-8fff-ec\",\"column_name\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_type\":\"string\",\"column_alias\":\"is_use_phone\"}]','','1','2021-03-07 01:54:37','','','','','','','',NULL,'','','','','','csv','UTF-8','|','','true','','','','','-1',NULL,'0'),(837799577204559872,'jdbc优化过滤','58','JDBC','data_sources_type_info','','','id,sources_type','{\"filter.optimize\":\"true\"}','sources_type=\'JDBC\'','60','JDBC','ds2','ds2','','[{\"column_md5\":\"a88-4ab0-be18-9c\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"372-4c7e-8e2c-f6\",\"column_name\":\"sources_type\",\"column_expr\":\"sources_type\",\"column_type\":\"string\",\"column_alias\":\"sources_type\"}]','delete from ds2','1','2021-04-30 13:16:25','','','','','','','',NULL,'','','','','','','','','','','','','','','-1','2022-01-08 13:32:22','0'),(843090296215441408,'mydb->iceberg','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','68','ICEBERG','','test.account_info','','[{\"column_md5\":\"705-4789-ad8d-2d\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"247-4c66-8b25-76\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"user_name\"},{\"column_md5\":\"2e0-4d88-8862-39\",\"column_name\":\"user_password\",\"column_expr\":\"user_password\",\"column_type\":\"string\",\"column_alias\":\"user_password\"},{\"column_md5\":\"4ad-45d6-92b6-db\",\"column_name\":\"email\",\"column_expr\":\"email\",\"column_type\":\"string\",\"column_alias\":\"email\"},{\"column_md5\":\"891-4c38-9e16-a3\",\"column_name\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_type\":\"string\",\"column_alias\":\"is_use_email\"},{\"column_md5\":\"7e2-4f9c-bc1d-74\",\"column_name\":\"phone\",\"column_expr\":\"phone\",\"column_type\":\"string\",\"column_alias\":\"phone\"},{\"column_md5\":\"87c-46d2-8b84-a2\",\"column_name\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_type\":\"string\",\"column_alias\":\"is_use_phone\"}]','','1','2021-05-15 03:39:50','','','','','','','',NULL,'','','','','','','','','','','','','','','-1',NULL,'0'),(853275616093409280,'mysql2oracle','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','69','JDBC','account_info','account_info','','[{\"column_md5\":\"c4b-4997-a502-4b\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"c2c-4471-9632-cb\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"user_name\"},{\"column_md5\":\"33c-4cd8-874f-d1\",\"column_name\":\"user_password\",\"column_expr\":\"user_password\",\"column_type\":\"string\",\"column_alias\":\"user_password\"},{\"column_md5\":\"95e-49c9-92ac-46\",\"column_name\":\"email\",\"column_expr\":\"email\",\"column_type\":\"string\",\"column_alias\":\"email\"},{\"column_md5\":\"6c5-400d-abd0-ac\",\"column_name\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_type\":\"string\",\"column_alias\":\"is_use_email\"},{\"column_md5\":\"372-42e9-8e74-50\",\"column_name\":\"phone\",\"column_expr\":\"phone\",\"column_type\":\"string\",\"column_alias\":\"phone\"},{\"column_md5\":\"412-4dd3-ba89-6e\",\"column_name\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_type\":\"string\",\"column_alias\":\"is_use_phone\"}]','','1','2021-06-12 06:12:40','','','','','','','',NULL,'','','','','','','','','','','','','','','-1',NULL,'0'),(853938731688660992,'测试新增参数','59','HDFS','','/data/csv/t1.txt','name,sex,job,addr,age','','','','59','HDFS','','/data/output/t1','','[{\"column_md5\":\"799-4d88-8bf9-f6\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"37b-4f66-8367-d8\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"540-49c6-ad42-47\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"d9e-4a98-9374-7f\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"25d-4892-a2a9-32\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','','1','2021-06-14 02:07:39','','','','','','','',NULL,'','','csv','GBK','|','csv','UTF-8',',','false','true','2','name','overwrite','','1',NULL,'0'),(855227983957331968,'测试hdfs写入单文件','58','JDBC','zdh_logs','','','job_id,log_time,msg,level,task_logs_id','','','59','HDFS','','/data/output/zdh_ha.txt','','[{\"column_md5\":\"5e0-4fd2-ae28-e2\",\"column_name\":\"job_id\",\"column_expr\":\"job_id\",\"column_type\":\"string\",\"column_alias\":\"job_id\"},{\"column_md5\":\"3ec-48d1-a596-2e\",\"column_name\":\"log_time\",\"column_expr\":\"log_time\",\"column_type\":\"string\",\"column_alias\":\"log_time\"},{\"column_md5\":\"06f-48c6-9736-50\",\"column_name\":\"msg\",\"column_expr\":\"msg\",\"column_type\":\"string\",\"column_alias\":\"msg\"},{\"column_md5\":\"99d-4480-b019-40\",\"column_name\":\"level\",\"column_expr\":\"level\",\"column_type\":\"string\",\"column_alias\":\"level\"},{\"column_md5\":\"c02-4719-a3fd-8c\",\"column_name\":\"task_logs_id\",\"column_expr\":\"task_logs_id\",\"column_type\":\"string\",\"column_alias\":\"task_logs_id\"}]','','1','2021-06-17 15:30:41','','','','','','','',NULL,'','','','','','csv','UTF-8','||','','true','','','append','','-1',NULL,'0'),(889232742397513728,'tttt','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','61','外部下载','','aa','','[{\"column_md5\":\"fd1-4963-be81-68\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"e4a-41ee-8702-9c\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"user_name\"},{\"column_md5\":\"f62-46ed-a1fa-21\",\"column_name\":\"user_password\",\"column_expr\":\"user_password\",\"column_type\":\"string\",\"column_alias\":\"user_password\"},{\"column_md5\":\"45f-4629-b4ff-9c\",\"column_name\":\"email\",\"column_expr\":\"email\",\"column_type\":\"string\",\"column_alias\":\"email\"},{\"column_md5\":\"b65-4bcd-a199-eb\",\"column_name\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_type\":\"string\",\"column_alias\":\"is_use_email\"},{\"column_md5\":\"e96-426c-bfdd-6a\",\"column_name\":\"phone\",\"column_expr\":\"phone\",\"column_type\":\"string\",\"column_alias\":\"phone\"},{\"column_md5\":\"dc6-466b-8645-d4\",\"column_name\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_type\":\"string\",\"column_alias\":\"is_use_phone\"}]','','1','2021-09-19 11:33:27','','','','','','','',NULL,'','','','','','csv','GBK','','','true','','','append','','-1',NULL,'0'),(889473341604237312,'测试异常','58','JDBC','','','','','','','','','','','','[]','','1','2021-09-20 03:29:30','','','','','','','',NULL,'','','','','','','','','','','','','','','-1',NULL,'0'),(927246265836638208,'batch_account_info_123','58','JDBC','account_info','account_info','id,user_name,user_password,email,is_use_email,phone,is_use_phone,enable,user_group,roles,signature','id,user_name,user_password,email,is_use_email,phone,is_use_phone,enable,user_group,roles,signature','','','60','JDBC','account_info','account_info','','[{\"column_alias\":\"id\",\"column_expr\":\"id\",\"column_md5\":\"927246265849221120\",\"column_type\":\"string\"},{\"column_alias\":\"user_name\",\"column_expr\":\"user_name\",\"column_md5\":\"927246265849221121\",\"column_type\":\"string\"},{\"column_alias\":\"user_password\",\"column_expr\":\"user_password\",\"column_md5\":\"927246265849221122\",\"column_type\":\"string\"},{\"column_alias\":\"email\",\"column_expr\":\"email\",\"column_md5\":\"927246265849221123\",\"column_type\":\"string\"},{\"column_alias\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_md5\":\"927246265849221124\",\"column_type\":\"string\"},{\"column_alias\":\"phone\",\"column_expr\":\"phone\",\"column_md5\":\"927246265849221125\",\"column_type\":\"string\"},{\"column_alias\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_md5\":\"927246265849221126\",\"column_type\":\"string\"},{\"column_alias\":\"enable\",\"column_expr\":\"enable\",\"column_md5\":\"927246265849221127\",\"column_type\":\"string\"},{\"column_alias\":\"user_group\",\"column_expr\":\"user_group\",\"column_md5\":\"927246265849221128\",\"column_type\":\"string\"},{\"column_alias\":\"roles\",\"column_expr\":\"roles\",\"column_md5\":\"927246265849221129\",\"column_type\":\"string\"},{\"column_alias\":\"signature\",\"column_expr\":\"signature\",\"column_md5\":\"927246265849221130\",\"column_type\":\"string\"}]','','1','2022-01-02 07:28:43','','','','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','','',NULL,'','','','','','-1','2022-01-02 09:05:37','0'),(927246265891164160,'batch_act2_123','58','JDBC','act2','act2','id,user_name,user_password,email,is_use_email,phone,is_use_phone','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','60','JDBC','act2','act2','','[{\"column_alias\":\"id\",\"column_expr\":\"id\",\"column_md5\":\"927246265899552768\",\"column_type\":\"string\"},{\"column_alias\":\"user_name\",\"column_expr\":\"user_name\",\"column_md5\":\"927246265899552769\",\"column_type\":\"string\"},{\"column_alias\":\"user_password\",\"column_expr\":\"user_password\",\"column_md5\":\"927246265899552770\",\"column_type\":\"string\"},{\"column_alias\":\"email\",\"column_expr\":\"email\",\"column_md5\":\"927246265899552771\",\"column_type\":\"string\"},{\"column_alias\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_md5\":\"927246265899552772\",\"column_type\":\"string\"},{\"column_alias\":\"phone\",\"column_expr\":\"phone\",\"column_md5\":\"927246265899552773\",\"column_type\":\"string\"},{\"column_alias\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_md5\":\"927246265899552774\",\"column_type\":\"string\"}]','','1','2022-01-02 07:28:43','','','','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','','',NULL,'','','','','','-1','2022-01-02 09:05:37','0'),(927246265907941376,'batch_apply_info_123','58','JDBC','apply_info','apply_info','id,apply_context,issue_id,approve_id,status,create_time,update_time,owner,reason,is_notice','id,apply_context,issue_id,approve_id,status,create_time,update_time,owner,reason,is_notice','','','60','JDBC','apply_info','apply_info','','[{\"column_alias\":\"id\",\"column_expr\":\"id\",\"column_md5\":\"927246265916329984\",\"column_type\":\"string\"},{\"column_alias\":\"apply_context\",\"column_expr\":\"apply_context\",\"column_md5\":\"927246265916329985\",\"column_type\":\"string\"},{\"column_alias\":\"issue_id\",\"column_expr\":\"issue_id\",\"column_md5\":\"927246265916329986\",\"column_type\":\"string\"},{\"column_alias\":\"approve_id\",\"column_expr\":\"approve_id\",\"column_md5\":\"927246265916329987\",\"column_type\":\"string\"},{\"column_alias\":\"status\",\"column_expr\":\"status\",\"column_md5\":\"927246265916329988\",\"column_type\":\"string\"},{\"column_alias\":\"create_time\",\"column_expr\":\"create_time\",\"column_md5\":\"927246265916329989\",\"column_type\":\"string\"},{\"column_alias\":\"update_time\",\"column_expr\":\"update_time\",\"column_md5\":\"927246265916329990\",\"column_type\":\"string\"},{\"column_alias\":\"owner\",\"column_expr\":\"owner\",\"column_md5\":\"927246265916329991\",\"column_type\":\"string\"},{\"column_alias\":\"reason\",\"column_expr\":\"reason\",\"column_md5\":\"927246265916329992\",\"column_type\":\"string\"},{\"column_alias\":\"is_notice\",\"column_expr\":\"is_notice\",\"column_md5\":\"927246265916329993\",\"column_type\":\"string\"}]','','1','2022-01-02 07:28:43','','','','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','','',NULL,'','','','','','-1','2022-01-02 09:05:37','0'),(927246265924718592,'batch_approval_auditor_info_123','58','JDBC','approval_auditor_info','approval_auditor_info','id,code,level,auditor_id,auditor_group,create_time','id,code,level,auditor_id,auditor_group,create_time','','','60','JDBC','approval_auditor_info','approval_auditor_info','','[{\"column_alias\":\"id\",\"column_expr\":\"id\",\"column_md5\":\"927246265937301504\",\"column_type\":\"string\"},{\"column_alias\":\"code\",\"column_expr\":\"code\",\"column_md5\":\"927246265937301505\",\"column_type\":\"string\"},{\"column_alias\":\"level\",\"column_expr\":\"level\",\"column_md5\":\"927246265937301506\",\"column_type\":\"string\"},{\"column_alias\":\"auditor_id\",\"column_expr\":\"auditor_id\",\"column_md5\":\"927246265937301507\",\"column_type\":\"string\"},{\"column_alias\":\"auditor_group\",\"column_expr\":\"auditor_group\",\"column_md5\":\"927246265937301508\",\"column_type\":\"string\"},{\"column_alias\":\"create_time\",\"column_expr\":\"create_time\",\"column_md5\":\"927246265937301509\",\"column_type\":\"string\"}]','','1','2022-01-02 07:28:43','','','','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','','',NULL,'','','','','','-1','2022-01-02 09:05:37','0'),(927246265945690112,'batch_approval_config_info_123','58','JDBC','approval_config_info','approval_config_info','id,code,code_name,type,create_time,employee_id','id,code,code_name,type,create_time,employee_id','','','60','JDBC','approval_config_info','approval_config_info','','[{\"column_alias\":\"id\",\"column_expr\":\"id\",\"column_md5\":\"927246265954078720\",\"column_type\":\"string\"},{\"column_alias\":\"code\",\"column_expr\":\"code\",\"column_md5\":\"927246265954078721\",\"column_type\":\"string\"},{\"column_alias\":\"code_name\",\"column_expr\":\"code_name\",\"column_md5\":\"927246265954078722\",\"column_type\":\"string\"},{\"column_alias\":\"type\",\"column_expr\":\"type\",\"column_md5\":\"927246265954078723\",\"column_type\":\"string\"},{\"column_alias\":\"create_time\",\"column_expr\":\"create_time\",\"column_md5\":\"927246265954078724\",\"column_type\":\"string\"},{\"column_alias\":\"employee_id\",\"column_expr\":\"employee_id\",\"column_md5\":\"927246265954078725\",\"column_type\":\"string\"}]','','1','2022-01-02 07:28:43','','','','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','','',NULL,'','','','','','-1','2022-01-08 03:50:51','1'),(927246265962467328,'batch_approval_event_info_123','58','JDBC','approval_event_info','approval_event_info','id,code,event_code,event_context,create_time','id,code,event_code,event_context,create_time','','','60','JDBC','approval_event_info','approval_event_info','','[{\"column_alias\":\"id\",\"column_expr\":\"id\",\"column_md5\":\"927246265970855936\",\"column_type\":\"string\"},{\"column_alias\":\"code\",\"column_expr\":\"code\",\"column_md5\":\"927246265970855937\",\"column_type\":\"string\"},{\"column_alias\":\"event_code\",\"column_expr\":\"event_code\",\"column_md5\":\"927246265970855938\",\"column_type\":\"string\"},{\"column_alias\":\"event_context\",\"column_expr\":\"event_context\",\"column_md5\":\"927246265970855939\",\"column_type\":\"string\"},{\"column_alias\":\"create_time\",\"column_expr\":\"create_time\",\"column_md5\":\"927246265970855940\",\"column_type\":\"string\"}]','','1','2022-01-02 07:28:43','','','','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','','',NULL,'','','','','','-1','2022-01-08 03:47:28','1'),(936967307782000640,'测试privateKey','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone,enable,user_group,roles,signature','','','59','HDFS','','/data/abc/1','','[{\"column_md5\":\"2d9-4975-b83c-04\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"701-460a-bdbc-40\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"user_name\"},{\"column_md5\":\"a1b-496b-ba68-6b\",\"column_name\":\"user_password\",\"column_expr\":\"user_password\",\"column_type\":\"string\",\"column_alias\":\"user_password\"},{\"column_md5\":\"dbb-47c3-ba58-1d\",\"column_name\":\"email\",\"column_expr\":\"email\",\"column_type\":\"string\",\"column_alias\":\"email\"},{\"column_md5\":\"ae9-4eca-892d-aa\",\"column_name\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_type\":\"string\",\"column_alias\":\"is_use_email\"},{\"column_md5\":\"8bb-4f23-9d33-5a\",\"column_name\":\"phone\",\"column_expr\":\"phone\",\"column_type\":\"string\",\"column_alias\":\"phone\"},{\"column_md5\":\"3cc-4135-a2d0-19\",\"column_name\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_type\":\"string\",\"column_alias\":\"is_use_phone\"},{\"column_md5\":\"b6d-485a-a3c9-d9\",\"column_name\":\"enable\",\"column_expr\":\"enable\",\"column_type\":\"string\",\"column_alias\":\"enable\"},{\"column_md5\":\"356-4e8c-b4b5-98\",\"column_name\":\"user_group\",\"column_expr\":\"user_group\",\"column_type\":\"string\",\"column_alias\":\"user_group\"},{\"column_md5\":\"010-484c-86cf-de\",\"column_name\":\"roles\",\"column_expr\":\"roles\",\"column_type\":\"string\",\"column_alias\":\"roles\"},{\"column_md5\":\"632-4825-927c-d4\",\"column_name\":\"signature\",\"column_expr\":\"signature\",\"column_type\":\"string\",\"column_alias\":\"signature\"}]','','1','2022-01-29 04:53:34','','','','','','','',NULL,'','','','','','csv','GBK','|','','true','','','','','-1','2022-01-29 05:23:24','0');
/*!40000 ALTER TABLE `etl_task_info` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zdh_nginx`
--

LOCK TABLES `zdh_nginx` WRITE;
/*!40000 ALTER TABLE `zdh_nginx` DISABLE KEYS */;
INSERT INTO `zdh_nginx` VALUES (1,'zyc','','','22','1','/home/zyc/download','/home/zyc/work');
/*!40000 ALTER TABLE `zdh_nginx` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality`
--

DROP TABLE IF EXISTS `quality`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality` (
  `id` varchar(100) DEFAULT NULL,
  `dispatch_task_id` varchar(100) DEFAULT NULL COMMENT '调度任务id',
  `etl_task_id` varchar(100) DEFAULT NULL COMMENT '任务id',
  `etl_date` varchar(20) DEFAULT NULL COMMENT '数据处理日期',
  `status` varchar(20) DEFAULT NULL COMMENT '报告状态',
  `report` varchar(500) DEFAULT NULL COMMENT '报告信息',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `job_context` varchar(256) DEFAULT NULL COMMENT '调度任务名称',
  `etl_context` varchar(256) DEFAULT NULL COMMENT '质量任务名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality`
--

LOCK TABLES `quality` WRITE;
/*!40000 ALTER TABLE `quality` DISABLE KEYS */;
INSERT INTO `quality` VALUES ('720677954291503104','719620564695650304','719619870378954752','2020-06-12 00:00:00','通过','{\"result\":\"通过\",\"rows_range\":\"数据行数检测通过\"}','2020-06-11 08:36:57','1',NULL,NULL),('720679166206283776','719620564695650304','719619870378954752','2020-06-13 00:00:00','通过','{\"result\":\"通过\",\"总行数\":\"9\",\"rows_range\":\"数据行数检测通过\"}','2020-06-11 08:41:48','1',NULL,NULL),('919655058193256448','919317519804665856','919326325259374592','2021-12-12 18:20:50','','{\"0\":\"指标:email ,在主键校验规则下,有3条数据不满足\",\"1\":\"指标:phone ,在长度校验规则下,有9条数据不满足\",\"2\":\"指标:phone ,在手机号验证规则下,有9条数据不满足\"}','2021-12-12 10:21:10','1',NULL,NULL),('919671360433688576','919317519804665856','919326325259374592','2021-12-12 19:25:37','','{\"0\":\"指标:email ,在主键校验规则下,有3条数据不满足\",\"1\":\"指标:phone ,在长度校验规则下,有9条数据不满足\",\"2\":\"指标:phone ,在手机号验证规则下,有9条数据不满足\"}','2021-12-12 11:26:10','1','第一个数据质量检测','第一个数据质量检测'),('919673694719053824','919317519804665856','919326325259374592','2021-12-12 19:34:54','不通过','{\"0\":\"指标:email ,在主键校验规则下,有3条数据不满足\",\"1\":\"指标:phone ,在长度校验规则下,有9条数据不满足\",\"2\":\"指标:phone ,在手机号验证规则下,有9条数据不满足\",\"result\":\"不通过\"}','2021-12-12 11:35:13','1','第一个数据质量检测','第一个数据质量检测'),('919673914701910016','919317519804665856','919326325259374592','2021-12-13 19:35:37','不通过','{\"0\":\"指标:email ,在主键校验规则下,有3条数据不满足\",\"1\":\"指标:phone ,在长度校验规则下,有9条数据不满足\",\"2\":\"指标:phone ,在手机号验证规则下,有9条数据不满足\",\"result\":\"不通过\"}','2021-12-12 11:36:19','1','第一个数据质量检测','第一个数据质量检测'),('919673915255558144','919317519804665856','919326325259374592','2021-12-15 19:35:37','不通过','{\"0\":\"指标:email ,在主键校验规则下,有3条数据不满足\",\"1\":\"指标:phone ,在长度校验规则下,有9条数据不满足\",\"2\":\"指标:phone ,在手机号验证规则下,有9条数据不满足\",\"result\":\"不通过\"}','2021-12-12 11:36:22','1','第一个数据质量检测','第一个数据质量检测'),('919673915352027136','919317519804665856','919326325259374592','2021-12-17 19:35:37','不通过','{\"0\":\"指标:email ,在主键校验规则下,有3条数据不满足\",\"1\":\"指标:phone ,在长度校验规则下,有9条数据不满足\",\"2\":\"指标:phone ,在手机号验证规则下,有9条数据不满足\",\"result\":\"不通过\"}','2021-12-12 11:36:25','1','第一个数据质量检测','第一个数据质量检测'),('919673915108757504','919317519804665856','919326325259374592','2021-12-14 19:35:37','不通过','{\"0\":\"指标:email ,在主键校验规则下,有3条数据不满足\",\"1\":\"指标:phone ,在长度校验规则下,有9条数据不满足\",\"2\":\"指标:phone ,在手机号验证规则下,有9条数据不满足\",\"result\":\"不通过\"}','2021-12-12 11:36:26','1','第一个数据质量检测','第一个数据质量检测'),('919673915372998656','919317519804665856','919326325259374592','2021-12-18 19:35:37','不通过','{\"0\":\"指标:email ,在主键校验规则下,有3条数据不满足\",\"1\":\"指标:phone ,在长度校验规则下,有9条数据不满足\",\"2\":\"指标:phone ,在手机号验证规则下,有9条数据不满足\",\"result\":\"不通过\"}','2021-12-12 11:36:29','1','第一个数据质量检测','第一个数据质量检测'),('919673915314278400','919317519804665856','919326325259374592','2021-12-16 19:35:37','不通过','{\"0\":\"指标:email ,在主键校验规则下,有3条数据不满足\",\"1\":\"指标:phone ,在长度校验规则下,有9条数据不满足\",\"2\":\"指标:phone ,在手机号验证规则下,有9条数据不满足\",\"result\":\"不通过\"}','2021-12-12 11:36:30','1','第一个数据质量检测','第一个数据质量检测'),('919673914676744192','919317519804665856','919326325259374592','2021-12-12 19:35:37','不通过','{\"0\":\"指标:email ,在主键校验规则下,有3条数据不满足\",\"1\":\"指标:phone ,在长度校验规则下,有9条数据不满足\",\"2\":\"指标:phone ,在手机号验证规则下,有9条数据不满足\",\"result\":\"不通过\"}','2021-12-12 11:36:33','1','第一个数据质量检测','第一个数据质量检测'),('919674543440662528','919317519804665856','919326325259374592','2021-12-12 19:38:17','不通过','{\"0\":\"指标:email ,在主键校验规则下,有3条数据不满足\",\"1\":\"指标:phone ,在长度校验规则下,有9条数据不满足\",\"2\":\"指标:phone ,在手机号验证规则下,有9条数据不满足\",\"result\":\"不通过\"}','2021-12-12 11:38:31','1','第一个数据质量检测','第一个数据质量检测'),('919675368372178944','919317519804665856','919326325259374592','2021-12-12 19:41:33','不通过','{\"0\":\"指标:email ,在主键校验规则下,有3条数据不满足\",\"1\":\"指标:phone ,在长度校验规则下,有9条数据不满足\",\"2\":\"指标:phone ,在手机号验证规则下,有9条数据不满足\",\"result\":\"不通过\"}','2021-12-12 11:41:46','1','第一个数据质量检测','第一个数据质量检测');
/*!40000 ALTER TABLE `quality` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etl_more_task_info`
--

DROP TABLE IF EXISTS `etl_more_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etl_more_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `etl_ids` varchar(200) DEFAULT NULL COMMENT '任务id列表',
  `etl_sql` text COMMENT '多源任务逻辑',
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
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_more_task_info`
--

LOCK TABLES `etl_more_task_info` WRITE;
/*!40000 ALTER TABLE `etl_more_task_info` DISABLE KEYS */;
INSERT INTO `etl_more_task_info` VALUES (1,'ddd','719629297702146048','-- ETL逻辑,使用sql语法 example select a.* ,b.* from a,b where a.id=b.id\r\nselect a from user a where name=\'abc\'','58','JDBC','act2','act2','{\r\n    \"zdh_version\": 123\r\n}','delete from act2','1','2020-06-08 14:19:59','','','','','','','','-1','2022-01-15 15:02:44','0'),(2,'more_mydb_account_info','749279343477264384','select * from account_info','59','HDFS','','/data/output/account_info','','','1','2020-08-29 06:49:19','','csv','UTF-8','|',NULL,'overwrite','user_name','2','2021-12-25 01:02:15','0'),(3,'优化多源任务测试','719619870378954752','--table 单分割符无标题=dt1;\r\nselect a.name,a.sex  from dt1 a\r\n','59','HDFS','','/data/output/dt1','','','1','2021-06-21 14:37:20','','csv','UTF-8',',','true','overwrite','','-1',NULL,'0');
/*!40000 ALTER TABLE `etl_more_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_log_instance`
--

DROP TABLE IF EXISTS `task_log_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_log_instance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
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
  `etl_info` text COMMENT '具体执行任务信息,已废弃',
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
  `count` int DEFAULT NULL COMMENT '当前任务执行次数,只做说明,具体判定使用实例表判断',
  `command` text COMMENT 'shell命令,jdbc命令,当前字段以废弃',
  `params` text COMMENT '自定义参数',
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
  `process_time` text COMMENT '流程时间',
  `priority` varchar(4) DEFAULT NULL COMMENT '任务优先级',
  `quartz_time` timestamp NULL DEFAULT NULL COMMENT 'quartz调度时间',
  `use_quartz_time` varchar(5) DEFAULT NULL COMMENT '是否使用quartz 调度时间',
  `time_diff` varchar(50) DEFAULT NULL COMMENT '后退时间差',
  `jsmind_data` text COMMENT '任务血源关系',
  `run_jsmind_data` text COMMENT '生成实例血源关系',
  `next_tasks` text COMMENT '下游任务实例id',
  `pre_tasks` text COMMENT '上游任务实例id',
  `is_disenable` varchar(10) DEFAULT NULL COMMENT '是否禁用true:禁用,false:启用',
  `depend_level` varchar(10) NOT NULL DEFAULT '0' COMMENT '判定级别0：成功时运行,1:杀死时运行,2:失败时运行,默认成功时运行',
  `schedule_source` varchar(64) NOT NULL DEFAULT '1' COMMENT '调度来源,1:例行,2:手动',
  `alarm_email` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `alarm_sms` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `alarm_zdh` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_error` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_finish` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_timeout` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=936974829569970177 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_log_instance`
--

LOCK TABLES `task_log_instance` WRITE;
/*!40000 ALTER TABLE `task_log_instance` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_log_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jar_file_info`
--

DROP TABLE IF EXISTS `jar_file_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jar_file_info` (
  `id` varchar(20) DEFAULT NULL,
  `file_name` varchar(100) DEFAULT NULL COMMENT '文件名称',
  `path` varchar(100) DEFAULT NULL COMMENT '文件目录',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `jar_etl_id` varchar(20) DEFAULT NULL COMMENT '任务id',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `status` varchar(10) DEFAULT NULL COMMENT '文件状态是否删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jar_file_info`
--

LOCK TABLES `jar_file_info` WRITE;
/*!40000 ALTER TABLE `jar_file_info` DISABLE KEYS */;
INSERT INTO `jar_file_info` VALUES ('732534262112194560','rdp-core-2.0.jar',NULL,'2020-07-14 01:49:37','732534261759873024','1','success'),('732534576089403392','rdp-core-2.0.jar',NULL,'2020-07-14 01:50:52','732534575875493888','1','success'),('732534576756297728','report-common-1.0.7.jar',NULL,'2020-07-14 01:50:52','732534575875493888','1','success'),('732589310183739392','zdh_server.jar',NULL,'2020-07-14 05:28:22','732538726244159488','1','success'),('732643701108510720','log4j.properties',NULL,'2020-07-14 09:04:30','732538726244159488','1','success'),('749042705316712448','zdh_server.jar',NULL,'2020-08-28 15:08:17','749041924744155136','1',NULL),('749052568860102656','zdh_server.jar',NULL,'2020-08-28 15:47:29','749051365933715456','1','success'),('749054743187296256','zdh_server.jar',NULL,'2020-08-28 15:56:07','749054742851751936','1',NULL),('749058489606737920','log4j.properties',NULL,'2020-08-28 16:11:00','749055828161466368','1','success'),('749060185443536896','log4j.properties',NULL,'2020-08-28 16:17:45','749060184990552064','1',NULL),('749060348916535296','log4j.properties',NULL,'2020-08-28 16:18:24','749060348564213760','1',NULL),('749062114471055360','zdh_server.jar',NULL,'2020-08-28 16:25:24','749062114156482560','1',NULL),('749063562575482880','zdh_server.jar',NULL,'2020-08-28 16:31:10','749063562055389184','1','success'),('794984305888595968','',NULL,'2021-01-02 09:43:48','794984305481748480','1',NULL),('794985297841491968','',NULL,'2021-01-02 09:47:45','794985297623388160','1',NULL),('794985444797321216','',NULL,'2021-01-02 09:48:20','794985443983626240','1',NULL),('795224647019794432','',NULL,'2021-01-03 01:38:50','795224646118019072','1',NULL),('904048696486793216','mydb.sql',NULL,'2021-10-30 08:46:45','749064500069535744','1','success');
/*!40000 ALTER TABLE `jar_file_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zdh_logs`
--

DROP TABLE IF EXISTS `zdh_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zdh_logs` (
  `job_id` varchar(100) DEFAULT NULL,
  `log_time` timestamp NULL DEFAULT NULL,
  `msg` text,
  `level` varchar(10) DEFAULT NULL,
  `task_logs_id` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zdh_logs`
--

LOCK TABLES `zdh_logs` WRITE;
/*!40000 ALTER TABLE `zdh_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `zdh_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sql_task_info`
--

DROP TABLE IF EXISTS `sql_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sql_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sql_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
  `data_sources_params_input` varchar(500) DEFAULT NULL COMMENT '输入数据源参数',
  `etl_sql` text COMMENT 'sql任务处理逻辑',
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
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=924230036783894529 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sql_task_info`
--

LOCK TABLES `sql_task_info` WRITE;
/*!40000 ALTER TABLE `sql_task_info` DISABLE KEYS */;
INSERT INTO `sql_task_info` VALUES (718814940856586240,'读取hive转mysql',NULL,NULL,'{\r\n    \"merge\": 50\r\n}','select * from act','58','JDBC','act2','act2','{\r\n    \"merge\": 52\r\n}','delete from act2','1','2020-06-06 05:13:57','','','','','','','','','','','-1','2022-01-15 15:08:25','0'),(720684174964428800,'读取hive转外部下载',NULL,NULL,'','select * from act','58','JDBC','act2','act2','','delete from act22','1','2020-06-06 05:13:57','','','','','','','','','','','-1','2022-01-08 02:54:54','0'),(756925994555674624,'fddfdfs',NULL,'','','select * from a2','','','','','','','1','2020-09-19 09:13:40','','','','','','','','','','','-1',NULL,'0'),(756926234339840000,'fdfdfd',NULL,'','','select 2','','','','','','','1','2020-09-19 09:14:37','','','','','','','','','','','-1','2021-12-25 01:13:11','1'),(756934940771225600,'读取hive转mysql',NULL,NULL,'','select * from act bb','','','act2','','','','1','2020-09-19 09:49:13','','','','','','','','','','','-1',NULL,'0'),(924230036783894528,'读取hive转mysql',NULL,NULL,'','select * from act bb','','','','','','','1','2021-12-25 01:20:12','','','','','','','','','','','-1','2021-12-25 01:20:12','0');
/*!40000 ALTER TABLE `sql_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource_tree_info`
--

DROP TABLE IF EXISTS `resource_tree_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resource_tree_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent` varchar(100) DEFAULT NULL,
  `text` varchar(1024) DEFAULT NULL,
  `level` varchar(10) DEFAULT NULL COMMENT '层级',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `icon` varchar(200) DEFAULT NULL,
  `resource_desc` varchar(10) DEFAULT NULL COMMENT '资源说明',
  `order` varchar(200) DEFAULT NULL,
  `is_enable` varchar(10) DEFAULT NULL COMMENT '是否启用',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `url` text COMMENT 'url链接',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '1:目录,2:菜单,3:函数',
  `notice_title` varchar(8) NOT NULL DEFAULT '' COMMENT '提示语',
  `event_code` varchar(64) NOT NULL DEFAULT '' COMMENT '绑定事件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=939848526425231361 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource_tree_info`
--

LOCK TABLES `resource_tree_info` WRITE;
/*!40000 ALTER TABLE `resource_tree_info` DISABLE KEYS */;
INSERT INTO `resource_tree_info` VALUES (802848818109353984,'#','ZDH','1','1','fa fa-folder',NULL,'1',NULL,'2021-01-24 02:34:34','2021-01-31 00:43:35','','1','',''),(802850170428461056,'802848818109353984','总监控','2','1','fa fa-bar-chart',NULL,'1',NULL,'2021-01-24 02:34:34','2021-01-30 13:56:12','monitor.html','2','',''),(802852358580080640,'802848818109353984','ETL采集','2','1','fa fa-tasks','','2','1','2021-01-24 02:34:34','2022-01-04 16:04:37','','1','高级',''),(802876953722884096,'917918003188731904','数据质量报告','3','1','fa fa-hourglass-half',NULL,'4',NULL,'2021-01-24 02:34:34','2021-01-30 14:24:43','quality_index.html','2','',''),(802918652050411520,'802848818109353984','数据源管理','2','1','fa fa-circle-o','','1','1','2021-01-24 02:34:34','2022-01-03 03:02:51','data_sources_index.html','2','',''),(802918760057933824,'802852358580080640','ETL任务','3','1','non',NULL,'2',NULL,'2021-01-24 02:34:34','2021-01-30 14:31:14','etl_task_index.html','2','',''),(802919044364636160,'802852358580080640','多源ETL任务','3','1','non',NULL,'3',NULL,'2021-01-24 02:34:34','2021-01-30 14:31:17','etl_task_more_sources_index.html','2','',''),(802919157430489088,'802852358580080640','SPARK_SQL任务','3','1','non',NULL,'4',NULL,'2021-01-24 02:34:34','2021-06-26 02:33:48','sql_task_index.html','2','',''),(802930870510948352,'802852358580080640','Drools任务','3','1','non',NULL,'5',NULL,'2021-01-24 02:34:34','2021-01-30 14:31:30','etl_task_drools_index.html','2','',''),(802931116691427328,'802852358580080640','SSH任务','3','1','non',NULL,'6',NULL,'2021-01-24 02:34:34','2021-01-30 14:31:33','etl_task_ssh_index.html','2','',''),(802931308593418240,'930966011266469888','调度ETL','3','1','fa fa-bus','','1','1','2021-01-24 02:34:34','2022-01-12 15:28:41','dispatch_task_index.html','2','',''),(802931697527033856,'802848818109353984','下载管理','2','1','fa fa-download','','4','1','2021-01-24 02:34:34','2021-12-26 03:42:30','download_index.html','2','',''),(802932157390524416,'802848818109353984','指标查询','2','1','fa fa-columns',NULL,'5',NULL,'2021-01-24 02:34:34','2021-01-30 14:24:46','quota_index.html','2','',''),(802932355596554240,'927514031458095104','血缘分析','3','1','fa fa-asterisk','','6','1','2021-01-24 02:34:34','2021-12-18 10:20:44','blood_source_index','2','',''),(802932548165439488,'802848818109353984','权限管理','2','1','fa fa-cog',NULL,'7',NULL,'2021-01-24 02:34:34','2021-10-02 02:49:34','','1','',''),(802932890089295872,'802848818109353984','使用技巧','2','1','fa fa-hand-o-right',NULL,'17',NULL,'2021-01-24 02:34:34','2021-02-13 05:21:29','zdh_help.index','2','',''),(802933021148712960,'926763179978002432','Cron','3','1','fa fa-glass',NULL,'9',NULL,'2021-01-24 02:34:34','2021-01-30 14:24:58','cron.html','2','',''),(802933165302747136,'802848818109353984','README','2','1','fa fa-info',NULL,'18',NULL,'2021-01-24 02:34:34','2021-02-13 05:21:37','readme.html','2','',''),(805193674668380160,'802848818109353984','设置','2','1','fa fa-user',NULL,'15',NULL,'2021-01-30 13:52:12','2021-01-31 00:39:40','','1','',''),(805357642351382528,'802848818109353984','测试2','2','1','','','100','1','2021-01-31 00:43:45','2022-02-04 02:04:36','','1','测试',''),(805369519538180096,'805193674668380160','文件服务器','3','1','fa fa-file',NULL,'1',NULL,'2021-01-31 01:30:57','2021-01-31 01:47:06','file_manager.html','2','',''),(805372907965386752,'805193674668380160','同步元数据','3','1','fa fa-circle-o',NULL,'3',NULL,'2021-01-31 01:44:24','2021-01-31 05:51:20','function:load_meta_databases()','4','',''),(805374183432261632,'805193674668380160','用户中心','3','1','fa fa-user',NULL,'0',NULL,'2021-01-31 01:49:28','2021-06-12 00:37:08','user.html','2','',''),(805431924678987776,'805193674668380160','生成监控任务','3','1','fa fa-gavel',NULL,'7',NULL,'2021-01-31 05:38:55','2021-01-31 05:52:20','function:del_system_job()','4','',''),(805531084459610112,'802848818109353984','升级扩容','2','1','fa fa-cloud',NULL,'16',NULL,'2021-01-31 12:12:57','2021-02-14 14:31:08','','1','',''),(808616077255774208,'805531084459610112','部署管理','3','1','fa fa-linux',NULL,'3',NULL,'2021-02-09 00:31:36','2021-02-12 12:23:56','server_manager_index.html','2','',''),(808616254788079616,'805531084459610112','WEB管理','3','1','fa fa-windows',NULL,'1',NULL,'2021-02-09 00:32:19','2021-02-09 00:32:35','consanguin_index.html','2','',''),(809886572093640704,'805193674668380160','通知管理','3','1','fa fa-comments',NULL,'9',NULL,'2021-02-12 12:40:06','2021-02-12 12:40:21','notice_update_index.html','2','',''),(810816897514737664,'810817759893000192','数据集市','3','1','fa fa-database',NULL,'1',NULL,'2021-02-15 02:16:53','2021-02-15 06:54:32','data_ware_house_index.html','2','',''),(810817759893000192,'802848818109353984','数仓管理','2','1','fa fa-database','','16','1','2021-02-15 02:20:18','2022-01-29 14:59:58','','1','数据资产',''),(810825569494110208,'810817759893000192','数据发布','3','1','fa fa-send','','3','1','2021-02-15 02:51:20','2021-02-15 02:51:20','data_issue_index.html','2','',''),(830556376391487488,'802848818109353984','提BUG','2','1','fa fa-bug','','20','1','2021-04-10 13:34:31','2022-01-03 03:05:03','mail_compose.html','2','',''),(838335003375964160,'810817759893000192','申请明细','3','1','fa fa-truck','','3','1','2021-05-02 00:44:00','2021-10-30 10:55:46','data_apply_index','2','',''),(839059609225269248,'891283647431184384','我的审批','3','1','fa fa-gg','','5','1','2021-05-04 00:43:20','2021-10-20 14:58:41','process_flow_index','2','',''),(839152432125579264,'802852358580080640','发布源ETL','3','1','non',NULL,'7',NULL,'2021-05-04 06:52:11','2021-05-04 06:54:19','etl_task_apply_index.html','2','',''),(858320387178500096,'802852358580080640','FLINK_SQL','3','1','non','','8','1','2021-06-26 04:18:47','2021-06-26 04:18:47','etl_task_flink_index.html','2','',''),(889247298196869120,'802848818109353984','信息管理','2','1','fa fa-envelope',NULL,'4',NULL,'2021-09-19 12:31:17','2021-09-19 12:32:09','notice_index.html','2','',''),(891283647431184384,'802848818109353984','审批管理','2','1','fa fa-exchange',NULL,'6',NULL,'2021-09-25 03:23:01','2021-10-04 02:01:54','','1','',''),(893810193274507264,'938782846959489024','菜单配置','4','1','fa fa-cog',NULL,'1',NULL,'2021-10-02 02:42:36','2021-10-03 08:00:22','permission_add_index.html','2','',''),(893816925920956416,'938782846959489024','角色配置','4','1','fa fa-cog',NULL,'2',NULL,'2021-10-02 03:09:21','2021-10-03 03:03:05','role_index.html','2','',''),(893817125867622400,'802932548165439488','用户配置','3','1','fa fa-cog',NULL,'3',NULL,'2021-10-02 03:10:09','2021-10-02 03:39:25','user_index.html','2','',''),(894524363561242624,'891283647431184384','审批节点','3','1','fa fa-exchange',NULL,'1',NULL,'2021-10-04 02:00:28','2021-10-04 03:05:34','approval_config_index','2','',''),(894896876246011904,'891283647431184384','审批人配置','3','1','fa fa-exchange',NULL,'2',NULL,'2021-10-05 02:40:42','2021-10-05 02:41:15','approval_auditor_index','2','',''),(896164931303378944,'891283647431184384','事件配置','3','1','fa fa-exchange',NULL,'3',NULL,'2021-10-08 14:39:30','2021-10-12 14:33:02','approval_event_index','2','',''),(898333103779483648,'802918652050411520','查询当前用户数据源','4','1','fa fa-coffee',NULL,'1',NULL,'2021-10-14 14:15:02','2021-10-14 14:22:21','data_sources_list','5','',''),(898334091781345280,'802918652050411520','新增数据源','4','1','fa fa-coffee','','2','1','2021-10-14 14:18:58','2021-12-03 14:33:12','data_sources_add','5','',''),(898334268604813312,'802918652050411520','删除数据源','4','1','fa fa-coffee','','3','1','2021-10-14 14:19:40','2021-10-14 14:19:40','data_sources_delete','5','',''),(898334486809284608,'802918652050411520','更新数据源','4','1','fa fa-coffee','','2','1','2021-10-14 14:20:32','2021-10-18 15:13:54','data_sources_update','5','',''),(898335485523398656,'802918652050411520','查询按钮','4','1','fa fa-coffee','','5','1','2021-10-14 14:24:30','2021-10-14 14:24:30','data_sources_list2','5','',''),(898335839925309440,'802918760057933824','查询按钮','4','1','fa fa-coffee','','1','1','2021-10-14 14:25:55','2021-10-14 14:25:55','etl_task_list2','5','',''),(898336112890613760,'802918760057933824','新增ETL任务','4','1','fa fa-coffee','','2','1','2021-10-14 14:27:00','2021-10-14 14:27:00','etl_task_add','5','',''),(898336252711931904,'802918760057933824','删除ETL任务','4','1','fa fa-coffee','','3','1','2021-10-14 14:27:33','2021-10-14 14:27:33','etl_task_delete','5','',''),(898336446329393152,'802918760057933824','更新ETL任务','4','1','fa fa-coffee','','4','1','2021-10-14 14:28:19','2021-10-14 14:28:19','etl_task_update','5','',''),(898336838400348160,'802919044364636160','查询按钮','4','1','fa fa-coffee','','1','1','2021-10-14 14:29:53','2021-10-14 14:29:53','etl_task_more_list2','5','',''),(898337064490110976,'802919044364636160','新增多源ETL任务','4','1','fa fa-coffee','','2','1','2021-10-14 14:30:46','2021-10-14 14:30:46','etl_task_more_sources_add','5','',''),(898337228843913216,'802919044364636160','删除多源ETL任务','4','1','fa fa-coffee','','3','1','2021-10-14 14:31:26','2021-10-14 14:31:26','etl_task_more_sources_delete','5','',''),(898337404199374848,'802919044364636160','更新多源ETL任务','4','1','fa fa-coffee','','4','1','2021-10-14 14:32:07','2021-10-14 14:32:07','etl_task_more_update','5','',''),(898337767392546816,'802919157430489088','SparkSql任务查询按钮','4','1','fa fa-coffee','','1','1','2021-10-14 14:33:34','2021-10-14 14:33:34','sql_task_list','5','',''),(898337973207044096,'802919157430489088','新增SparkSql任务','4','1','fa fa-coffee','','2','1','2021-10-14 14:34:23','2021-10-14 14:34:23','sql_task_add','5','',''),(898338119047188480,'802919157430489088','删除SparkSql任务','4','1','fa fa-coffee','','3','1','2021-10-14 14:34:58','2021-10-14 14:34:58','sql_task_delete','5','',''),(898338251511697408,'802919157430489088','更新SparkSql任务','4','1','fa fa-coffee','','4','1','2021-10-14 14:35:29','2021-10-16 15:54:00','sql_task_update','5','',''),(898338570987638784,'802931116691427328','SSH任务查询按钮','4','1','fa fa-coffee','','1','1','2021-10-14 14:36:46','2021-10-14 14:36:46','etl_task_ssh_list','5','',''),(898338819265269760,'802931116691427328','新增SSH任务','4','1','fa fa-coffee','','2','1','2021-10-14 14:37:45','2021-10-14 14:37:45','etl_task_ssh_add','5','',''),(898338990552256512,'802931116691427328','删除SSH任务','4','1','fa fa-coffee','','3','1','2021-10-14 14:38:26','2021-10-14 14:38:26','etl_task_ssh_delete','5','',''),(898339248917188608,'802931116691427328','更新SSH任务','4','1','fa fa-coffee','','4','1','2021-10-14 14:39:27','2021-10-14 14:39:27','etl_task_ssh_update','5','',''),(898339478811185152,'858320387178500096','FlinkSql任务查询按钮','4','1','fa fa-coffee','','1','1','2021-10-14 14:40:22','2021-10-14 14:40:22','etl_task_flink_list','5','',''),(898339672948740096,'858320387178500096','新增FlinkSql任务','4','1','fa fa-coffee','','2','1','2021-10-14 14:41:08','2021-10-14 14:41:08','etl_task_flink_add','5','',''),(898339790284394496,'858320387178500096','删除FlinkSql任务','4','1','fa fa-coffee','','3','1','2021-10-14 14:41:36','2021-10-14 14:41:36','etl_task_flink_delete','5','',''),(898339903127949312,'858320387178500096','更新FlinkSql任务','4','1','fa fa-coffee','','4','1','2021-10-14 14:42:03','2021-10-14 14:42:03','etl_task_flink_update','5','',''),(898340104257409024,'802931308593418240','调度任务查询按钮','4','1','fa fa-coffee','','1','1','2021-10-14 14:42:51','2021-10-14 14:42:51','dispatch_task_list2','5','',''),(898340234041757696,'802931308593418240','新增调度任务','4','1','fa fa-coffee',NULL,'2',NULL,'2021-10-14 14:43:22','2021-10-14 14:44:39','dispatch_task_group_add','5','',''),(898340346117754880,'802931308593418240','删除调度任务','4','1','fa fa-coffee','','3','1','2021-10-14 14:43:49','2021-10-30 09:27:43','dispatch_task_group_delete','5','',''),(898341132600086528,'802931308593418240','更新调度任务','4','1','fa fa-coffee',NULL,'4',NULL,'2021-10-14 14:46:56','2021-10-14 14:55:04','dispatch_task_group_update','5','',''),(898919578183143424,'802931697527033856','下载管理-查询按钮','3','1','fa fa-coffee','','1','1','2021-10-16 05:05:29','2021-10-16 05:05:29','download_list','5','',''),(898919751642779648,'802931697527033856','下载管理-删除','3','1','fa fa-coffee','','2','1','2021-10-16 05:06:10','2021-10-16 05:06:10','download_delete','5','',''),(898919884019208192,'802931697527033856','下载管理-下载文件','3','1','fa fa-coffee','','3','1','2021-10-16 05:06:41','2021-10-16 05:06:41','download_file','5','',''),(898921890200948736,'802876953722884096','质量报告-查询按钮','3','1','fa fa-coffee','','1','1','2021-10-16 05:14:40','2021-10-16 05:14:40','quality_list','5','',''),(898922054793826304,'802876953722884096','质量报告-删除','3','1','fa fa-coffee','','2','1','2021-10-16 05:15:19','2021-10-16 05:15:19','quality_delete','5','',''),(898922698124562432,'889247298196869120','信息管理-查询','3','1','fa fa-coffee','','1','1','2021-10-16 05:17:52','2021-10-16 05:18:12','notice_list2','5','',''),(898923212249763840,'889247298196869120','信息管理-删除','3','1','fa fa-coffee','','2','1','2021-10-16 05:19:55','2021-10-16 05:19:55','notice_delete','5','',''),(898923484506230784,'889247298196869120','信息管理-信息内容','3','1','fa fa-coffee','','2','1','2021-10-16 05:21:00','2021-10-16 05:21:00','notice_message','5','',''),(898923625090912256,'889247298196869120','信息管理-明细页面','3','1','fa fa-coffee','','4','1','2021-10-16 05:21:33','2021-10-16 05:21:33','notice_detail_index','3','',''),(898924030910795776,'894524363561242624','审批节点-查询','4','1','fa fa-coffee','','1','1','2021-10-16 05:23:10','2021-10-16 05:23:10','approval_config_list','5','',''),(898924424034521088,'894524363561242624','审批节点-新增','4','1','fa fa-coffee','','2','1','2021-10-16 05:24:44','2021-10-16 05:24:44','approval_config_add','5','',''),(898924549347741696,'894524363561242624','审批节点-更新','4','1','fa fa-coffee','','3','1','2021-10-16 05:25:14','2021-10-16 05:25:14','approval_config_update','5','',''),(898924812993302528,'894524363561242624','审批节点-新增页面','4','1','fa fa-coffee','','4','1','2021-10-16 05:26:17','2021-10-16 05:26:17','approval_config_add_index','3','',''),(898925461227180032,'894896876246011904','审批人配置-查询','4','1','fa fa-coffee','','1','1','2021-10-16 05:28:51','2021-10-16 05:28:51','approval_auditor_list','5','',''),(898925640273629184,'894896876246011904','审批人配置-新增','4','1','fa fa-coffee','','2','1','2021-10-16 05:29:34','2021-10-16 05:29:34','approval_auditor_add','5','',''),(898925753096212480,'894896876246011904','审批人配置-更新','4','1','fa fa-coffee','','3','1','2021-10-16 05:30:01','2021-10-16 05:30:01','approval_auditor_update','5','',''),(898926933432078336,'894896876246011904','审批人配置-新增页面','4','1','fa fa-coffee','','4','1','2021-10-16 05:34:42','2021-10-16 05:34:42','approval_auditor_add_index','3','',''),(898992327668797440,'896164931303378944','事件配置-查询','4','1','fa fa-coffee','','1','1','2021-10-16 09:54:33','2021-10-16 09:54:33','approval_event_list','5','',''),(898992469134282752,'896164931303378944','事件配置-新增','4','1','fa fa-coffee','','2','1','2021-10-16 09:55:07','2021-10-16 09:55:07','approval_event_add','5','',''),(898992569562697728,'896164931303378944','事件配置-更新','4','1','fa fa-coffee','','3','1','2021-10-16 09:55:31','2021-10-16 09:55:31','approval_event_update','5','',''),(898992688609628160,'896164931303378944','事件配置-新增页面','4','1','fa fa-coffee','','4','1','2021-10-16 09:55:59','2021-10-16 09:55:59','approval_event_add_index','3','',''),(898994298257674240,'893810193274507264','菜单配置-查询','4','1','fa fa-coffee','','1','1','2021-10-16 10:02:23','2021-10-16 10:02:23','jstree_node','5','',''),(898994643360813056,'893810193274507264','菜单配置-双击目录','4','1','fa fa-coffee','','2','1','2021-10-16 10:03:45','2021-10-16 10:03:45','jstree_get_node','5','',''),(898994850660093952,'893810193274507264','菜单配置-节点更新','4','1','fa fa-coffee','','3','1','2021-10-16 10:04:35','2021-10-16 10:04:35','jstree_update_node','5','',''),(898995002837831680,'893810193274507264','菜单配置-新增节点','4','1','fa fa-coffee','','4','1','2021-10-16 10:05:11','2021-10-16 10:05:11','jstree_add_node','5','',''),(898995219188420608,'893810193274507264','菜单配置-节点删除','4','1','fa fa-coffee','','5','1','2021-10-16 10:06:03','2021-10-16 10:06:03','jstree_del_node','5','',''),(898995513087496192,'893810193274507264','菜单配置-父节点更新','4','1','fa fa-coffee','','6','1','2021-10-16 10:07:13','2021-10-16 10:07:13','jstree_update_parent','5','',''),(898996218540068864,'893816925920956416','角色配置-查询','4','1','fa fa-coffee','','1','1','2021-10-16 10:10:01','2021-10-16 10:10:01','role_list','5','',''),(898996945933045760,'893816925920956416','角色配置-新增页面','4','1','fa fa-coffee','','2','1','2021-10-16 10:12:54','2021-10-16 10:12:54','role_add_index','3','',''),(898997770537406464,'893816925920956416','角色配置-新增','4','1','fa fa-coffee','','3','1','2021-10-16 10:16:11','2021-10-16 10:16:11','jstree_add_permission','5','',''),(898998045511782400,'893816925920956416','角色配置-禁用/启用','4','1','fa fa-coffee','','4','1','2021-10-16 10:17:17','2021-10-16 10:17:33','role_enable','5','',''),(898998299615301632,'893816925920956416','角色配置-明细','4','1','fa fa-coffee','','5','1','2021-10-16 10:18:17','2021-10-16 10:18:17','role_detail','5','',''),(898998799848968192,'893817125867622400','用户配置-查询','4','1','fa fa-coffee','','1','1','2021-10-16 10:20:16','2021-10-16 10:20:16','user_list','5','',''),(898999065830756352,'893817125867622400','用户配置-启用/禁用','4','1','fa fa-coffee','','2','1','2021-10-16 10:21:20','2021-10-16 10:21:20','user_enable','5','',''),(898999773028159488,'893817125867622400','用户配置-新增/更新','4','1','fa fa-coffee','','3','1','2021-10-16 10:24:08','2021-10-16 10:24:08','user_update','5','',''),(898999958684831744,'893817125867622400','用户配置-新增用户组','4','1','fa fa-coffee','','4','1','2021-10-16 10:24:53','2021-10-16 10:24:53','user_group_add','5','',''),(899000154885984256,'893817125867622400','用户配置-用户明细页面','4','1','fa fa-coffee','','5','1','2021-10-16 10:25:40','2021-10-16 10:25:40','user_add_index','3','',''),(899000274331373568,'893817125867622400','用户配置-用户明细','4','1','fa fa-coffee','','6','1','2021-10-16 10:26:08','2021-10-16 10:26:08','user_detail','5','',''),(899000507769556992,'893817125867622400','用户配置-新增页面','4','1','fa fa-coffee','','7','1','2021-10-16 10:27:04','2021-10-16 10:27:04','user_add_index','3','',''),(899000798854254592,'802933021148712960','Cron查询','3','1','fa fa-coffee','','1','1','2021-10-16 10:28:13','2021-10-16 10:28:13','quartz-cron.json','5','',''),(899001510107549696,'805374183432261632','用户中心-更新','4','1','fa fa-coffee','','1','1','2021-10-16 10:31:03','2021-10-16 10:31:03','user','5','',''),(899001733512957952,'805374183432261632','用户中心-查询','4','1','fa fa-coffee','','2','1','2021-10-16 10:31:56','2021-10-16 10:31:56','getUserInfo','5','',''),(899001982079995904,'805369519538180096','文件服务器-查询','4','1','fa fa-coffee','','1','1','2021-10-16 10:32:55','2021-10-16 10:32:55','getFileManager','5','',''),(899002142675701760,'805369519538180096','文件服务器-更新','4','1','fa fa-coffee','','2','1','2021-10-16 10:33:33','2021-10-16 10:33:33','file_manager_up','5','',''),(899059721481228288,'809886572093640704','通知管理-新增','4','1','fa fa-coffee','','1','1','2021-10-16 14:22:21','2021-10-16 14:22:21','notice_update','5','',''),(899076126133981184,'808616077255774208','部署管理-新增模板','4','1','fa fa-coffee','','1','1','2021-10-16 15:27:32','2021-10-16 15:27:32','server_add','5','',''),(899076266156625920,'808616077255774208','部署管理-更新模板','4','1','fa fa-coffee','','2','1','2021-10-16 15:28:06','2021-10-16 15:28:06','server_update','5','',''),(899076393432780800,'808616077255774208','部署管理-新增模板页面','4','1','fa fa-coffee','','3','1','2021-10-16 15:28:36','2021-10-16 15:28:36','server_add_index','3','',''),(899076708504702976,'808616077255774208','部署管理-一键部署页面','4','1','fa fa-coffee','','4','1','2021-10-16 15:29:51','2021-10-16 15:29:51','server_build_exe_detail_index','3','',''),(899076830349234176,'808616077255774208','部署管理-一键部署','4','1','fa fa-coffee','','5','1','2021-10-16 15:30:20','2021-10-16 15:30:20','server_setup','5','',''),(899077318159372288,'808616077255774208','部署管理-server查询','4','1','fa fa-coffee','','6','1','2021-10-16 15:32:17','2021-10-16 15:32:17','server_manager_online_list','5','',''),(899077560292347904,'808616077255774208','部署管理-逻辑上线/下线','4','1','fa fa-coffee','','8','1','2021-10-16 15:33:14','2021-10-16 15:33:14','server_manager_online_update','5','',''),(899078834731618304,'810816897514737664','数据集市-查询','4','1','fa fa-coffee','','1','1','2021-10-16 15:38:18','2021-10-16 15:38:18','data_ware_house_list2','5','',''),(899079195127189504,'810816897514737664','数据集市-明细页面','4','1','fa fa-coffee','','2','1','2021-10-16 15:39:44','2021-10-30 11:15:12','data_ware_house_detail_index','3','',''),(899079347393007616,'810816897514737664','数据集市-明细','4','1','fa fa-coffee','','3','1','2021-10-16 15:40:21','2021-10-16 15:40:21','data_ware_house_list','5','',''),(899079618982580224,'810816897514737664','数据集市-申请','4','1','fa fa-coffee','','4','1','2021-10-16 15:41:25','2022-01-23 03:58:09','data_apply_add','5','','data_apply'),(899079911518507008,'810825569494110208','数据发布-查询','4','1','fa fa-coffee','','1','1','2021-10-16 15:42:35','2021-10-16 15:42:35','data_ware_house_list3','5','',''),(899080146999316480,'810825569494110208','数据发布-发布页面','4','1','fa fa-coffee','','2','1','2021-10-16 15:43:31','2021-10-16 15:43:31','data_issue_add_index','3','',''),(899081132492984320,'810825569494110208','数据发布-发布','4','1','fa fa-coffee','','3','1','2021-10-16 15:47:26','2022-01-23 02:14:43','issue_data_add','5','','data_pub'),(899081270330396672,'810825569494110208','数据发布-更新','4','1','fa fa-coffee','','4','1','2021-10-16 15:47:59','2022-01-23 04:31:32','issue_data_update','5','','data_pub'),(899081439826415616,'810825569494110208','数据发布-删除','4','1','fa fa-coffee','','5','1','2021-10-16 15:48:39','2021-10-16 15:48:39','issue_data_delete','5','',''),(899083124191793152,'802850170428461056','总监控-任务状态查询','3','1','fa fa-coffee','','1','1','2021-10-16 15:55:21','2021-10-16 15:55:21','task_group_log_instance_list2','5','',''),(899083287929032704,'802850170428461056','总监控-调度任务查询','3','1','fa fa-coffee','','2','1','2021-10-16 15:56:00','2021-10-16 15:56:00','getScheduleTask','5','',''),(899083494326538240,'802850170428461056','总监控-调度任务总计查询','3','1','fa fa-coffee','','3','1','2021-10-16 15:56:49','2021-10-16 15:56:49','etlEcharts','5','',''),(899083652330164224,'802850170428461056','总监控-调度总计','3','1','fa fa-coffee','','4','1','2021-10-16 15:57:27','2021-10-16 15:57:27','getTotalNum','5','',''),(899793046982365184,'802848818109353984','查询通知','2','1','fa fa-coffee','','110','1','2021-10-18 14:56:20','2021-10-18 15:57:02','notice_list','5','',''),(899794270502785024,'893810193274507264','菜单配置-新增节点页面','4','1','fa fa-coffee','','7','1','2021-10-18 15:01:11','2021-10-18 15:01:11','jstree_add_index','3','',''),(899794938370199552,'893816925920956416','角色配置-查询菜单','4','1','fa fa-coffee','','6','1','2021-10-18 15:03:51','2021-10-18 15:03:51','jstree_permission_list','5','',''),(899796100964159488,'893816925920956416','角色配置-用户拥有权菜单列表','4','1','fa fa-coffee','','7','1','2021-10-18 15:08:28','2021-10-18 15:08:28','jstree_permission_list2','5','',''),(899796275644338176,'802848818109353984','系统通知','2','1','fa fa-coffee','','111','1','2021-10-18 15:09:10','2021-10-18 15:09:10','every_day_notice','5','',''),(899796487305695232,'802848818109353984','首页','2','1','fa fa-coffee','','112','1','2021-10-18 15:10:00','2021-10-18 15:10:00','index','3','',''),(899796627491917824,'802848818109353984','验证码','2','1','fa fa-coffee','','113','1','2021-10-18 15:10:33','2021-10-18 15:10:33','captcha','5','',''),(899796760371662848,'802848818109353984','登陆','2','1','fa fa-coffee','','115','1','2021-10-18 15:11:05','2021-10-18 15:11:05','login','5','',''),(899796895541497856,'802848818109353984','退出登陆','2','1','fa fa-coffee','','116','1','2021-10-18 15:11:37','2021-10-18 15:11:37','logout','5','',''),(899797407535992832,'802918652050411520','数据源管理-新增页面','4','1','fa fa-coffee','','1','1','2021-10-18 15:13:39','2021-10-18 15:22:27','data_sources_add_index','3','',''),(899797834717466624,'802918652050411520','数据源管理-数据源类型查询','4','1','fa fa-coffee','','7','1','2021-10-18 15:15:21','2021-10-18 15:15:21','data_sources_type','5','',''),(899799748200894464,'802918652050411520','数据源管理-查询明细','4','1','fa fa-coffee','','8','1','2021-10-18 15:22:57','2021-10-18 15:22:57','data_sources_info','5','',''),(899804862013771776,'802918760057933824','ETL任务-新增页面','4','1','fa fa-coffee','','5','1','2021-10-18 15:43:17','2021-10-18 15:43:17','etl_task_add_index','3','',''),(899805009363865600,'802918760057933824','ETL任务-查询明细','4','1','fa fa-coffee','','6','1','2021-10-18 15:43:52','2021-10-18 15:43:52','etl_task_detail','5','',''),(899805166738345984,'802918760057933824','ETL任务-查询数据库表','4','1','fa fa-coffee','','8','1','2021-10-18 15:44:29','2021-10-18 15:44:29','etl_task_tables','5','',''),(899807422577643520,'802919044364636160','多源ETL任务-新增页面','4','1','fa fa-coffee','','5','1','2021-10-18 15:53:27','2021-10-18 15:53:27','etl_task_more_sources_add_index','3','',''),(899807641243488256,'802919044364636160','多源ETL任务-查询明细','4','1','fa fa-coffee','','6','1','2021-10-18 15:54:19','2021-10-18 15:54:19','etl_task_more_detail','5','',''),(899808823739420672,'802919157430489088','SparkSql任务-查询数据仓库','4','1','fa fa-coffee','','5','1','2021-10-18 15:59:01','2021-10-18 15:59:01','show_databases','5','',''),(899810180206694400,'802930870510948352','Drools任务-查询','4','1','fa fa-coffee','','1','1','2021-10-18 16:04:25','2021-10-18 16:04:25','etl_task_drools_list2','5','',''),(899810791648137216,'802930870510948352','Drools任务-新增页面','4','1','fa fa-coffee','','2','1','2021-10-18 16:06:50','2021-10-18 16:06:50','etl_task_drools_add_index','3','',''),(899810992530132992,'802930870510948352','Drools任务-明细','4','1','fa fa-coffee','','3','1','2021-10-18 16:07:38','2021-10-18 16:07:38','etl_task_drools_detail','5','',''),(899811121018441728,'802930870510948352','Drools任务-新增','4','1','fa fa-coffee','','4','1','2021-10-18 16:08:09','2021-10-18 16:08:09','etl_task_drools_add','5','',''),(899811229017575424,'802930870510948352','Drools任务-删除','4','1','fa fa-coffee','','5','1','2021-10-18 16:08:35','2021-10-18 16:08:35','etl_task_drools_delete','5','',''),(899811331564113920,'802930870510948352','Drools任务-更新','4','1','fa fa-coffee','','6','1','2021-10-18 16:08:59','2021-10-18 16:08:59','etl_task_drools_update','5','',''),(899811720153796608,'802931116691427328','SSH任务-新增页面','4','1','fa fa-coffee','','5','1','2021-10-18 16:10:32','2021-10-18 16:10:32','etl_task_ssh_add_index','3','',''),(899813283056324608,'839152432125579264','发布源ETL-查询','4','1','fa fa-coffee','','1','1','2021-10-18 16:16:44','2021-10-18 16:16:44','etl_task_apply_list2','5','',''),(899814315572334592,'839152432125579264','发布源ETL-新增页面','4','1','fa fa-coffee','','2','1','2021-10-18 16:20:51','2021-10-18 16:20:51','etl_task_apply_add_index','3','',''),(899814524809383936,'839152432125579264','发布源ETL-新增','4','1','fa fa-coffee','','3','1','2021-10-18 16:21:40','2021-10-18 16:21:40','etl_task_apply_add','5','',''),(899814663515017216,'839152432125579264','发布源ETL-删除','4','1','fa fa-coffee','','4','1','2021-10-18 16:22:14','2021-10-18 16:22:14','etl_task_apply_delete','5','',''),(899814772944408576,'839152432125579264','发布源ETL-更新','4','1','fa fa-coffee','','5','1','2021-10-18 16:22:40','2021-10-18 16:22:40','etl_task_apply_update','5','',''),(899815335576735744,'858320387178500096','FlinkSql新增页面','4','1','fa fa-coffee','','5','1','2021-10-18 16:24:54','2021-10-18 16:24:54','etl_task_flink_add_index','3','',''),(899816557880807424,'808616077255774208','部署管理-查询','4','1','fa fa-coffee','','1','1','2021-10-18 16:29:45','2021-10-18 16:29:45','server_manager_list','5','',''),(900519809114968064,'839059609225269248','我的审批-查询','4','1','fa fa-coffee','','1','1','2021-10-20 15:04:13','2021-10-20 15:04:13','process_flow_list','5','',''),(902346927830470656,'891283647431184384','我的流程','3','1','fa fa-gg','','5','1','2021-10-25 16:04:32','2021-10-25 16:06:50','process_flow_index2','2','',''),(902685738875752448,'902346927830470656','我的流程-查询','4','1','fa fa-coffee','','1','1','2021-10-26 14:30:51','2021-10-26 14:30:51','process_flow_list2','5','',''),(902710107161235456,'902346927830470656','我的流程-撤销','4','1','fa fa-coffee','','2','1','2021-10-26 16:07:41','2021-10-26 16:07:41','process_flow_status2','3','',''),(903063574157463552,'902346927830470656','我的流程-进度页面','4','1','fa fa-coffee','','3','1','2021-10-27 15:32:14','2021-10-27 15:32:51','process_flow_detail_index','3','',''),(903063690486484992,'902346927830470656','我的流程-进度查询','4','1','fa fa-coffee','','4','1','2021-10-27 15:32:42','2021-10-27 15:32:42','process_flow_detail','5','',''),(903065878872985600,'802848818109353984','404','2','1','fa fa-coffee','','99','1','2021-10-27 15:41:24','2021-10-30 05:40:20','404','3','',''),(903066735052066816,'838335003375964160','申请明细-查询','4','1','fa fa-coffee','','1','1','2021-10-27 15:44:48','2021-10-27 15:44:48','data_apply_list','5','',''),(904012775699779584,'893817125867622400','用户配置-用户组查询','4','1','fa fa-coffee','','7','1','2021-10-30 06:24:01','2021-10-30 06:24:01','user_group_list','5','',''),(904015493940121600,'802848818109353984','REDIS接口','2','1','fa fa-coffee','','105','1','2021-10-30 06:34:50','2021-10-30 06:34:50','','5','',''),(904015737297833984,'904015493940121600','REDIS接口-查询','3','1','fa fa-coffee','','1','1','2021-10-30 06:35:48','2021-10-30 06:35:48','/redis/get','5','',''),(904015873528827904,'904015493940121600','REDIS接口-删除','3','1','fa fa-coffee','','2','1','2021-10-30 06:36:20','2021-10-30 06:36:20','redis/del','5','',''),(904016015229194240,'904015493940121600','REDIS接口-所有KEY','3','1','fa fa-coffee','','3','1','2021-10-30 06:36:54','2021-10-30 06:36:54','redis/keys','5','',''),(904017457503539200,'830556376391487488','提BUG-发送邮件','3','1','fa fa-coffee','','1','1','2021-10-30 06:42:38','2021-10-30 06:42:38','send_email','5','',''),(904042210536722432,'805372907965386752','同步元数据-同步','4','1','fa fa-coffee','','1','1','2021-10-30 08:20:59','2021-10-30 08:20:59','load_meta_databases','5','',''),(904042889393213440,'805431924678987776','生成监控任务-生成','4','1','fa fa-coffee','','1','1','2021-10-30 08:23:41','2021-10-30 08:23:41','del_system_job','5','',''),(904044939199909888,'898340104257409024','调度任务-主键查询','5','1','fa fa-coffee','','1','1','2021-10-30 08:31:50','2021-10-30 08:31:50','dispatch_task_list','5','',''),(904046610378395648,'802931116691427328','SSH任务-文件查询','4','1','fa fa-coffee','','1','1','2021-10-30 08:38:28','2021-10-30 08:38:28','etl_task_ssh_file_list','5','',''),(904047129859723264,'802931116691427328','SSH任务-删除文件','4','1','fa fa-coffee','','7','1','2021-10-30 08:40:32','2021-10-30 08:40:32','etl_task_ssh_del_file','5','',''),(904050101738016768,'802931308593418240','调度ETL-任务组执行记录页面','4','1','fa fa-coffee','','5','1','2021-10-30 08:52:21','2021-10-30 08:54:58','task_group_log_instance_index','3','',''),(904051931872235520,'904050101738016768','任务组记录-查询','5','1','fa fa-coffee','','1','1','2021-10-30 08:59:37','2021-10-30 08:59:37','task_group_log_instance_list','5','',''),(904052710129537024,'904050101738016768','日志页面','5','1','fa fa-coffee','','2','1','2021-10-30 09:02:43','2021-10-30 09:02:43','log_txt','3','',''),(904053017009983488,'904052710129537024','查询/删除','6','1','fa fa-coffee','','1','1','2021-10-30 09:03:56','2021-10-30 09:04:21','zdh_logs','5','',''),(904053585896017920,'904050101738016768','任务组-子任务页面','5','1','fa fa-coffee','','2','1','2021-10-30 09:06:11','2021-10-30 09:06:11','task_log_instance_index','3','',''),(904055132549812224,'904053585896017920','子任务-查询','6','1','fa fa-coffee','','1','1','2021-10-30 09:12:20','2021-10-30 09:12:20','task_log_instance_list','5','',''),(904055741369815040,'904053585896017920','子任务-组查询','6','1','fa fa-coffee','','2','1','2021-10-30 09:14:45','2021-10-30 09:14:45','task_group_log_instance_list3','5','',''),(904056255981555712,'904053585896017920','子任务-删除','6','1','fa fa-coffee','','3','1','2021-10-30 09:16:48','2021-10-30 09:16:48','task_logs_delete','5','',''),(904057211481755648,'802931308593418240','调度ETL-手动执行页面','4','1','fa fa-coffee','','6','1','2021-10-30 09:20:36','2021-10-30 09:20:36','task_group_exe_detail_index','5','',''),(904058054746574848,'802931308593418240','调度ETL-新增任务页面','4','1','fa fa-coffee','','8','1','2021-10-30 09:23:57','2021-10-30 09:23:57','dispatch_task_group_add_index','5','',''),(904059356146831360,'904057211481755648','执行','5','1','fa fa-coffee','','1','1','2021-10-30 09:29:07','2021-10-30 09:29:07','dispatch_task_execute','5','',''),(904060028510539776,'904053585896017920','子任务-杀死','6','1','fa fa-coffee','','4','1','2021-10-30 09:31:47','2021-10-30 09:31:47','kill','5','',''),(904060576521523200,'904050101738016768','任务组-杀死','5','1','fa fa-coffee','','1','1','2021-10-30 09:33:58','2021-10-30 09:34:13','killJobGroup','5','',''),(904061109097467904,'904050101738016768','任务组-重试页面','5','1','fa fa-coffee','','1','1','2021-10-30 09:36:05','2021-10-30 09:36:05','task_group_retry_detail_index','3','',''),(904061224998670336,'904061109097467904','重试','6','1','fa fa-coffee','','1','1','2021-10-30 09:36:33','2021-10-30 09:40:46','retryJobGroup','5','',''),(904063054986088448,'904053585896017920','子任务-获取监控链接','6','1','fa fa-coffee','','5','1','2021-10-30 09:43:49','2021-10-30 09:43:49','getSparkMonitor','5','',''),(904065292424974336,'802850170428461056','总监控-开启的调度任务','3','1','fa fa-coffee','','4','1','2021-10-30 09:52:42','2021-10-30 09:52:42','getScheduleTask','5','',''),(904065881678548992,'802931308593418240','调度ETL-调度按钮','4','1','fa fa-coffee','','8','1','2021-10-30 09:55:03','2021-10-30 09:55:03','dispatch_task_execute_quartz','5','',''),(904066172054409216,'802931308593418240','调度ETL-暂停按钮','4','1','fa fa-coffee','','9','1','2021-10-30 09:56:12','2021-10-30 09:56:12','dispatch_task_quartz_pause','5','',''),(904066367945183232,'802931308593418240','调度ETL-停用按钮','4','1','fa fa-coffee','','10','1','2021-10-30 09:56:59','2021-10-30 09:56:59','dispatch_task_quartz_del','5','',''),(904068373158039552,'894524363561242624','审批节点-明细','4','1','fa fa-coffee','','5','1','2021-10-30 10:04:57','2021-10-30 10:04:57','approval_config_detail','5','',''),(904069900111187968,'894896876246011904','审批人配置-明细','4','1','fa fa-coffee','','5','1','2021-10-30 10:11:01','2021-10-30 10:11:01','approval_auditor_detail','5','',''),(904070354979262464,'894896876246011904','审批人配置-删除','4','1','fa fa-coffee','','6','1','2021-10-30 10:12:49','2021-10-30 10:12:49','approval_auditor_delete','5','',''),(904070687604346880,'896164931303378944','事件配置-明细','4','1','fa fa-coffee','','5','1','2021-10-30 10:14:09','2021-10-30 10:14:09','approval_event_detail','5','',''),(904071524649013248,'839059609225269248','我的审批-审批','4','1','fa fa-coffee','','2','1','2021-10-30 10:17:28','2021-10-30 10:17:28','process_flow_status','5','',''),(904073001346011136,'893817125867622400','用户配置-新增用户组页面','4','1','fa fa-coffee','','8','1','2021-10-30 10:23:20','2021-10-30 10:23:20','user_group_add_index','3','',''),(904117946815614976,'904058054746574848','SHELL页面','5','1','fa fa-coffee','','1','1','2021-10-30 13:21:56','2021-10-30 13:21:56','shell_detail','5','',''),(904118233940889600,'904058054746574848','ETL页面','5','1','fa fa-coffee','','2','1','2021-10-30 13:23:05','2021-10-30 13:23:05','job_detail','3','',''),(904118420969099264,'904058054746574848','任务组页面','5','1','fa fa-coffee','','3','1','2021-10-30 13:23:49','2021-10-30 13:23:49','group_detail','3','',''),(904118636598267904,'904058054746574848','JDBC页面','5','1','fa fa-coffee','','4','1','2021-10-30 13:24:41','2021-10-30 13:24:41','jdbc_detail','3','',''),(904118843020939264,'904058054746574848','HDFS页面','5','1','fa fa-coffee','','5','1','2021-10-30 13:25:30','2021-10-30 13:25:30','hdfs_detail','3','',''),(904120248553181184,'904058054746574848','计算集群-查询','5','1','fa fa-coffee','','6','1','2021-10-30 13:31:05','2021-10-30 13:31:05','zdh_instance_list','5','',''),(904134748278886400,'899814315572334592','发布源-查询','5','1','fa fa-coffee','','1','1','2021-10-30 14:28:42','2021-10-30 14:28:42','data_apply_list2','5','',''),(904135062444838912,'899814315572334592','发布源-查询2','5','1','fa fa-coffee','','2','1','2021-10-30 14:29:57','2021-10-30 14:29:57','data_apply_list3','5','',''),(904875825113862144,'889247298196869120','信息管理-已读','3','1','fa fa-coffee','','5','1','2021-11-01 15:33:28','2021-11-01 15:33:28','notice_update_see','5','',''),(906703041556647936,'802919157430489088','新增SparkSql任务首页','4','1','fa fa-coffee','','7','1','2021-11-06 16:34:10','2021-11-06 16:34:10','sql_task_add_index','3','',''),(906964874968436736,'802932157390524416','指标查询-查询','3','1','fa fa-coffee','','1','1','2021-11-07 09:54:36','2021-11-07 09:54:36','quota_list','5','',''),(913940784099627008,'802852358580080640','JDBC_SQL','3','1','non','','9','1','2021-11-26 23:54:23','2022-01-18 05:32:33','etl_task_jdbc_index.html','2','',''),(913947870334291968,'913940784099627008','查询','4','1','fa fa-coffee','','1','1','2021-11-27 00:22:33','2021-11-27 00:22:33','etl_task_jdbc_list','5','',''),(913949817086939136,'913940784099627008','新增页面','4','1','fa fa-coffee','','2','1','2021-11-27 00:30:17','2021-11-27 00:32:02','etl_task_jdbc_add_index','5','',''),(913949953242435584,'913940784099627008','删除','4','1','fa fa-coffee','','3','1','2021-11-27 00:30:49','2021-11-27 00:30:49','etl_task_jdbc_delete','5','',''),(913950163700027392,'913940784099627008','新增','4','1','fa fa-coffee','','4','1','2021-11-27 00:31:39','2021-11-27 00:53:12','etl_task_jdbc_add','5','',''),(913955680715542528,'913940784099627008','更新','4','1','fa fa-coffee','','5','1','2021-11-27 00:53:35','2021-11-27 00:53:35','etl_task_jdbc_update','5','',''),(916107476942721024,'802852358580080640','DATAX','3','1','non','','10','1','2021-12-02 15:24:03','2021-12-02 15:24:03','etl_task_datax_index.html','2','',''),(916457943107375104,'916107476942721024','新增页面','4','1','fa fa-coffee','','1','1','2021-12-03 14:36:41','2021-12-03 14:36:41','etl_task_datax_add_index.html','3','',''),(916458081179668480,'916107476942721024','查询','4','1','fa fa-coffee','','2','1','2021-12-03 14:37:14','2021-12-03 14:37:14','etl_task_datax_list','5','',''),(916458334754705408,'916107476942721024','新增','4','1','fa fa-coffee','','3','1','2021-12-03 14:38:14','2021-12-03 14:38:14','etl_task_datax_add','5','',''),(916458466950778880,'916107476942721024','更新','4','1','fa fa-coffee','','4','1','2021-12-03 14:38:45','2021-12-03 14:38:45','etl_task_datax_update','5','',''),(916458561943375872,'916107476942721024','删除','4','1','fa fa-coffee','','5','1','2021-12-03 14:39:08','2021-12-03 14:39:08','etl_task_datax_delete','5','',''),(916768297586790400,'805374183432261632','用户名查询','4','1','fa fa-coffee','','3','1','2021-12-04 11:09:55','2021-12-04 11:09:55','user_names','5','',''),(917918003188731904,'802848818109353984','数据质量管理','2','1','fa fa-hourglass-half','','4','1','2021-12-07 15:18:26','2021-12-26 07:24:48','','1','',''),(917918741411401728,'917918003188731904','数据规则','3','1','fa fa-eye','','1','1','2021-12-07 15:21:22','2021-12-07 16:07:23','quality_rule_index.html','2','',''),(917919433773551616,'917918003188731904','数据监控','3','1','fa fa-tasks','','2','1','2021-12-07 15:24:07','2021-12-08 16:11:40','quality_task_index.html','2','',''),(917930429175042048,'917918741411401728','查询','4','1','fa fa-coffee','','1','1','2021-12-07 16:07:49','2021-12-07 16:07:49','quality_rule_list','5','',''),(917936957084930048,'917918741411401728','新增首页','4','1','fa fa-coffee','','1','1','2021-12-07 16:33:45','2021-12-07 16:33:45','quality_rule_add_index.html','3','',''),(917937043621810176,'917918741411401728','新增','4','1','fa fa-coffee','','2','1','2021-12-07 16:34:06','2021-12-07 16:34:06','quality_rule_add','5','',''),(918269583171784704,'917918741411401728','更新','4','1','fa fa-coffee','','4','1','2021-12-08 14:35:29','2021-12-08 14:35:29','quality_rule_update','5','',''),(918293897011007488,'917919433773551616','查询','4','1','fa fa-coffee','','1','1','2021-12-08 16:12:06','2021-12-08 16:12:06','quality_task_list','5','',''),(918294064137244672,'917919433773551616','新增页面','4','1','fa fa-coffee','','2','1','2021-12-08 16:12:46','2021-12-08 16:12:46','quality_task_add_index.html','3','',''),(918634471165530112,'802918760057933824','ETL任务-上传文件','4','1','fa fa-coffee','','8','1','2021-12-09 14:45:25','2021-12-09 14:45:25','etl_task_add_file','5','',''),(918646782911582208,'917919433773551616','新增','4','1','fa fa-coffee','','3','1','2021-12-09 15:34:21','2021-12-09 15:34:21','quality_task_add','5','',''),(918646874980749312,'917919433773551616','更新','4','1','fa fa-coffee','','4','1','2021-12-09 15:34:43','2021-12-09 15:34:43','quality_task_update','5','',''),(918646989531385856,'917919433773551616','删除','4','1','fa fa-coffee','','5','1','2021-12-09 15:35:10','2021-12-09 15:35:10','quality_task_delete','5','',''),(919324529707192320,'802918760057933824','查询数据表SCHEMA','4','1','fa fa-coffee','','9','1','2021-12-11 12:27:28','2021-12-11 12:27:28','etl_task_schema','5','',''),(921829181543682048,'802932355596554240','数据源查询','3','1','fa fa-coffee','','1','1','2021-12-18 10:20:04','2021-12-18 10:20:04','blood_source_list','5','',''),(921829301463027712,'802932355596554240','上下游查询','3','1','fa fa-coffee','','2','1','2021-12-18 10:20:32','2021-12-18 10:20:32','blood_source_detail','5','',''),(922098350709280768,'802932355596554240','血源页面','3','1','fa fa-coffee','','1','1','2021-12-19 04:09:39','2021-12-19 04:09:39','blood_source_detail_index','5','',''),(922626072145563648,'802918652050411520','测试连接','4','1','fa fa-coffee','','9','1','2021-12-20 15:06:37','2021-12-20 15:06:37','test_connect','5','',''),(923706124102799360,'904057211481755648','查询执行时间','4','1','fa fa-coffee','','2','1','2021-12-23 14:38:22','2021-12-23 14:38:22','dispatch_task_execute_time','5','',''),(923714155901358080,'904050101738016768','任务组记录-删除','4','1','fa fa-coffee','','2','1','2021-12-23 15:10:17','2021-12-23 15:10:17','task_group_logs_delete','5','',''),(924405157259644928,'926763179978002432','操作日志','3','1','fa fa-history','','20','1','2021-12-25 12:56:04','2021-12-25 12:56:04','user_operate_log_index.html','2','',''),(924405330996105216,'924405157259644928','查询','3','1','fa fa-coffee','','1','1','2021-12-25 12:56:46','2021-12-25 12:56:46','user_operate_log_list','5','',''),(926763179978002432,'802848818109353984','工具箱','2','1','fa fa-gavel','','4','1','2022-01-01 01:06:01','2022-01-12 15:27:05','','1','',''),(926763439244709888,'927138424895311872','整库任务新增页面','4','1','fa fa-coffee','','2','1','2022-01-01 01:07:02','2022-01-02 05:47:29','etl_task_batch_add_index','3','',''),(927138093289443328,'927138424895311872','首页','4','1','fa fa-coffee','','1','1','2022-01-02 01:55:47','2022-01-04 16:00:41','etl_task_batch_index','2','新增',''),(927138424895311872,'926763179978002432','整库任务','3','1','fa fa-database','','1','1','2022-01-02 01:57:06','2022-01-02 02:02:13','','1','',''),(927189274061705216,'927138424895311872','整库任务查询','4','1','fa fa-coffee','','1','1','2022-01-02 05:19:09','2022-01-02 05:19:09','etl_task_batch_list','5','',''),(927194809708318720,'927138424895311872','整库任务新增','4','1','fa fa-coffee','','4','1','2022-01-02 05:41:09','2022-01-02 05:41:09','etl_task_batch_add','5','',''),(927198208109580288,'927138424895311872','整库任务明细','4','1','fa fa-coffee','','5','1','2022-01-02 05:54:39','2022-01-02 05:54:39','etl_task_batch_detail','5','',''),(927201827349336064,'927138424895311872','整库任务更新','4','1','fa fa-coffee','','6','1','2022-01-02 06:09:02','2022-01-02 06:09:02','etl_task_batch_update','5','',''),(927202104341172224,'927138424895311872','整库任务删除','4','1','fa fa-coffee','','7','1','2022-01-02 06:10:08','2022-01-02 06:10:08','etl_task_batch_delete','5','',''),(927224599626649600,'927138424895311872','整库任务生成','4','1','fa fa-coffee','','7','1','2022-01-02 07:39:32','2022-01-02 07:39:32','etl_task_batch_create','5','',''),(927514031458095104,'802848818109353984','血缘管理','2','1','fa fa-asterisk','','4','1','2022-01-03 02:49:37','2022-01-03 02:52:02','','1','',''),(927514524737605632,'927514031458095104','血缘生成','3','1','fa fa-coffee','','1','1','2022-01-03 02:51:35','2022-01-03 03:09:40','blood_source_create','2','',''),(927681598093004800,'802848818109353984','版本信息查询','2','1','fa fa-coffee','','100','1','2022-01-03 13:55:28','2022-01-03 13:55:28','version','5','',''),(929314447095238656,'839152432125579264','发布源ETL_明细','4','1','fa fa-coffee','','4','1','2022-01-08 02:03:50','2022-01-08 02:03:50','etl_task_apply_detail','5','',''),(929349427703844864,'802848818109353984','下载ZDH','2','1','fa fa-cloud-download','','0','1','2022-01-08 04:22:50','2022-02-04 02:07:10','zdh_download_index','2','下载',''),(930966011266469888,'802848818109353984','调度管理','2','1','fa fa-bus','','3','1','2022-01-12 15:26:34','2022-01-12 15:26:34','','1','',''),(930966518835974144,'930966011266469888','调度器','3','1','fa fa-ship','','2','1','2022-01-12 15:28:35','2022-01-18 05:33:08','dispatch_executor_index','2','管理员用',''),(931332510242054144,'930966518835974144','调度器查询','4','1','fa fa-coffee','','1','1','2022-01-13 15:42:54','2022-01-13 15:42:54','dispatch_executor_list','5','',''),(931918590498574336,'930966518835974144','调度器上下线','4','1','fa fa-coffee','','2','1','2022-01-15 06:31:46','2022-01-15 06:31:46','dispatch_executor_status','5','',''),(934820298253930496,'802848818109353984','枚举管理','2','1','fa fa-cubes','','10','1','2022-01-23 06:42:07','2022-01-23 06:42:41','enum_index','2','',''),(934820739683454976,'934820298253930496','查询','3','1','fa fa-coffee','','1','1','2022-01-23 06:43:52','2022-01-23 06:43:52','enum_list','5','',''),(934899005698084864,'934820298253930496','新增页面','3','1','fa fa-coffee','','2','1','2022-01-23 11:54:53','2022-01-23 11:54:53','enum_add_index','3','',''),(934899134614212608,'934820298253930496','新增','3','1','fa fa-coffee','','3','1','2022-01-23 11:55:23','2022-01-23 11:55:23','enum_add','5','',''),(934899250242785280,'934820298253930496','删除','3','1','fa fa-coffee','','4','1','2022-01-23 11:55:51','2022-01-23 11:55:51','enum_delete','5','',''),(934899352286007296,'934820298253930496','更新','3','1','fa fa-coffee','','5','1','2022-01-23 11:56:15','2022-01-23 11:56:15','enum_update','5','',''),(934926419119575040,'934820298253930496','明细','3','1','fa fa-coffee','','6','1','2022-01-23 13:43:48','2022-01-23 13:43:48','enum_detail','5','',''),(934940905796800512,'802848818109353984','WEMOCK','2','1','fa fa-cube','','150','1','2022-01-23 14:41:22','2022-01-23 14:41:22','abc.html','2','开发中',''),(936723230184443904,'802919157430489088','SparkSql查询表结构','4','1','fa fa-coffee','','7','1','2022-01-28 12:43:42','2022-01-28 12:43:42','desc_table','5','',''),(936943441596649472,'926763179978002432','参数配置','3','1','fa fa-diamond','','4','1','2022-01-29 03:18:44','2022-01-29 03:18:44','param_index','2','管理员',''),(936943543832809472,'936943441596649472','查询','4','1','fa fa-coffee','','1','1','2022-01-29 03:19:08','2022-01-29 03:19:08','param_list','5','',''),(936943806161358848,'936943441596649472','更新页面','4','1','fa fa-coffee','','2','1','2022-01-29 03:20:11','2022-01-29 03:20:11','param_add_index','3','',''),(936943897400053760,'936943441596649472','新增','4','1','fa fa-coffee','','3','1','2022-01-29 03:20:33','2022-01-29 03:20:33','param_add','5','',''),(936943979792961536,'936943441596649472','更新','4','1','fa fa-coffee','','4','1','2022-01-29 03:20:52','2022-01-29 03:20:52','param_update','5','',''),(936944089763418112,'936943441596649472','删除','4','1','fa fa-coffee','','5','1','2022-01-29 03:21:19','2022-01-29 03:21:19','param_delete','5','',''),(936956093823717376,'936943441596649472','查询明细','4','1','fa fa-coffee','','6','1','2022-01-29 04:09:01','2022-01-29 04:09:01','param_detail','5','',''),(937123552732123136,'810817759893000192','数据资产','3','1','fa fa-coffee','','4','1','2022-01-29 15:14:26','2022-01-29 15:14:26','data_ware_house_index_plus','2','',''),(938782846959489024,'802932548165439488','功能权限','3','1','fa fa-cog','','1','1','2022-02-03 05:07:52','2022-02-03 05:08:07','','1','',''),(938783170151583744,'802932548165439488','数据权限','3','1','fa fa-cog','','2','1','2022-02-03 05:09:10','2022-02-03 05:09:10','','1','',''),(938783478181269504,'938783170151583744','数据标识','4','1','fa fa-cog','','1','1','2022-02-03 05:10:23','2022-02-03 05:11:33','data_tag_index','2','',''),(938783882944188416,'938783478181269504','查询','5','1','fa fa-coffee','','1','1','2022-02-03 05:11:59','2022-02-03 05:11:59','data_tag_list','5','',''),(938784014171377664,'938783478181269504','新增页面','5','1','fa fa-coffee','','2','1','2022-02-03 05:12:31','2022-02-03 05:12:31','data_tag_add_index','3','',''),(938784115174412288,'938783478181269504','新增','5','1','fa fa-coffee','','3','1','2022-02-03 05:12:55','2022-02-03 05:12:55','data_tag_add','5','',''),(938784216877895680,'938783478181269504','更新','5','1','fa fa-coffee','','4','1','2022-02-03 05:13:19','2022-02-03 05:13:19','data_tag_update','5','',''),(938784333840257024,'938783478181269504','删除','5','1','fa fa-coffee','','5','1','2022-02-03 05:13:47','2022-02-03 05:13:47','data_tag_delete','5','',''),(938785006883442688,'938783170151583744','产品标识','4','1','fa fa-cog','','2','1','2022-02-03 05:16:27','2022-02-03 05:19:15','product_tag_index','2','',''),(938785227138928640,'938785006883442688','查询','5','1','fa fa-coffee','','1','1','2022-02-03 05:17:20','2022-02-03 05:17:20','product_tag_list','5','',''),(938785661765292032,'938785006883442688','新增页面','5','1','fa fa-coffee','','2','1','2022-02-03 05:19:04','2022-02-03 05:19:04','product_tag_add_index','3','',''),(938785831097733120,'938785006883442688','新增','5','1','fa fa-coffee','','3','1','2022-02-03 05:19:44','2022-02-03 05:19:44','product_tag_add','5','',''),(938785942548779008,'938785006883442688','更新','5','1','fa fa-coffee','','4','1','2022-02-03 05:20:11','2022-02-03 05:20:11','product_tag_update','5','',''),(938786051592294400,'938785006883442688','删除','5','1','fa fa-coffee','','5','1','2022-02-03 05:20:37','2022-02-03 05:20:37','product_tag_delete','5','',''),(938879140692496384,'938783478181269504','明细','5','1','fa fa-coffee','','6','1','2022-02-03 11:30:31','2022-02-03 11:30:31','data_tag_detail','5','',''),(938879269428269056,'938785006883442688','明细','5','1','fa fa-coffee','','6','1','2022-02-03 11:31:01','2022-02-03 11:31:01','product_tag_detail','5','',''),(939102606079299584,'938783170151583744','标识组','4','1','fa fa-cog','','3','1','2022-02-04 02:18:29','2022-02-04 02:18:29','data_tag_group_index','2','',''),(939102720999034880,'939102606079299584','查询','5','1','fa fa-coffee','','1','1','2022-02-04 02:18:56','2022-02-04 02:18:56','data_tag_group_list','5','',''),(939102954936340480,'939102606079299584','新增页面','5','1','fa fa-coffee','','2','1','2022-02-04 02:19:52','2022-02-04 02:19:52','data_tag_group_add_index','3','',''),(939103070598467584,'939102606079299584','新增','5','1','fa fa-coffee','','3','1','2022-02-04 02:20:20','2022-02-04 02:20:20','data_tag_group_add','5','',''),(939103467824222208,'939102606079299584','更新','5','1','fa fa-coffee','','4','1','2022-02-04 02:21:54','2022-02-04 02:21:54','data_tag_group_update','5','',''),(939103572845400064,'939102606079299584','删除','5','1','fa fa-coffee','','5','1','2022-02-04 02:22:19','2022-02-04 02:22:19','data_tag_group_delete','5','',''),(939103663475920896,'939102606079299584','明细','5','1','fa fa-coffee','','6','1','2022-02-04 02:22:41','2022-02-04 02:22:41','data_tag_group_detail','5','',''),(939113343006806016,'938783478181269504','产品code查询','5','1','fa fa-coffee','','7','1','2022-02-04 03:01:09','2022-02-04 03:01:09','data_tag_by_product_code','5','',''),(939830350585008128,'802918652050411520','查询数据组标识','3','1','fa fa-coffee','','10','1','2022-02-06 02:30:17','2022-02-06 02:30:17','user_tag_group_code','5','',''),(939848526425231360,'936943441596649472','同步redis','4','1','fa fa-coffee','','7','1','2022-02-06 03:42:30','2022-02-06 03:42:30','param_to_redis','5','','');
/*!40000 ALTER TABLE `resource_tree_info` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispatch_task_info`
--

LOCK TABLES `dispatch_task_info` WRITE;
/*!40000 ALTER TABLE `dispatch_task_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dispatch_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_tag_info`
--

DROP TABLE IF EXISTS `product_tag_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_tag_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_code` varchar(512) DEFAULT NULL COMMENT '产品标识code',
  `product_name` varchar(200) DEFAULT NULL COMMENT '产品名称',
  `owner` varchar(64) DEFAULT NULL COMMENT '拥有者',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=939075768552525825 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_tag_info`
--

LOCK TABLES `product_tag_info` WRITE;
/*!40000 ALTER TABLE `product_tag_info` DISABLE KEYS */;
INSERT INTO `product_tag_info` VALUES (938885094720933888,'zdh','ZDH产品','1','0','2022-02-03 11:54:10','2022-02-03 11:58:09'),(939075768552525824,'spark','spark','1','1','2022-02-04 00:31:50','2022-02-04 00:32:02');
/*!40000 ALTER TABLE `product_tag_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ssh_task_info`
--

DROP TABLE IF EXISTS `ssh_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ssh_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ssh_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `host` varchar(100) DEFAULT NULL,
  `port` varchar(100) DEFAULT NULL,
  `user_name` varchar(500) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `ssh_cmd` text,
  `ssh_script_path` varchar(100) DEFAULT NULL,
  `ssh_script_context` text,
  `ssh_params_input` varchar(500) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=924234428954185729 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ssh_task_info`
--

LOCK TABLES `ssh_task_info` WRITE;
/*!40000 ALTER TABLE `ssh_task_info` DISABLE KEYS */;
INSERT INTO `ssh_task_info` VALUES (749064500069535744,'123','127.0.0.1','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc/z2','ls','','1','2020-08-28 16:34:54','','','','','2022-01-08 01:59:30','0'),(794986268751564800,'暂停5分钟','127.0.0.1','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc','echo \"hello\" >> a.log\r\nsleep 10m\r\necho \"word\" >> a.log','','1','2021-01-02 09:51:37','','','','',NULL,'0'),(924228505527390208,'暂停5分钟222','127.0.0.1','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc','echo \"hello\" >> a.log\r\nsleep 10m\r\necho \"word\" >> a.log','','1','2021-12-25 01:14:07','','','','','2021-12-25 01:14:07','0'),(924229143766241280,'暂停5分钟222','127.0.0.1','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc','echo \"hello\" >> a.log\r\nsleep 10m\r\necho \"word\" >> a.log','','1','2021-12-25 01:16:39','','','','','2021-12-25 01:38:15','1'),(924229512823050240,'暂停5分钟222','127.0.0.1','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc','echo \"hello\" >> a.log\r\nsleep 10m\r\necho \"word\" >> a.log','','1','2021-12-25 01:18:07','','','','','2021-12-25 01:18:07','0'),(924230760599130112,'暂停5分钟222','127.0.0.1','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc','echo \"hello\" >> a.log\r\nsleep 10m\r\necho \"word\" >> a.log','','1','2021-12-25 01:23:05','','','','','2021-12-25 01:23:05','0'),(924231076753182720,'暂停5分钟222','127.0.0.1','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc','echo \"hello\" >> a.log\r\nsleep 10m\r\necho \"word\" >> a.log','','1','2021-12-25 01:24:20','','','','','2021-12-25 01:38:15','1'),(924231704590159872,'暂停5分钟222','127.0.0.1','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc','echo \"hello\" >> a.log\r\nsleep 10m\r\necho \"word\" >> a.log','','1','2021-12-25 01:26:50','','','','','2021-12-25 01:37:59','1'),(924233476058648576,'暂停5分钟222','127.0.0.1','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc','echo \"hello\" >> a.log\r\nsleep 10m\r\necho \"word\" >> a.log','','1','2021-12-25 01:33:52','','','','','2021-12-25 01:37:59','1'),(924233523236179968,'暂停5分钟222','127.0.0.1','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc','echo \"hello\" >> a.log\r\nsleep 10m\r\necho \"word\" >> a.log','','1','2021-12-25 01:34:03','','','','','2021-12-25 01:37:59','1'),(924234428954185728,'暂停5分钟222','127.0.0.1','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc','echo \"hello\" >> a.log\r\nsleep 10m\r\necho \"word\" >> a.log','','1','2021-12-25 01:37:39','','','','','2021-12-25 01:38:15','1');
/*!40000 ALTER TABLE `ssh_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data_sources_info`
--

DROP TABLE IF EXISTS `data_sources_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `data_sources_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `data_source_context` varchar(100) DEFAULT NULL COMMENT '数据源说明',
  `data_source_type` varchar(100) DEFAULT NULL COMMENT '数据源类型',
  `driver` varchar(100) DEFAULT NULL COMMENT '驱动连接串',
  `url` varchar(100) DEFAULT NULL COMMENT '连接url',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `is_delete` varchar(10) NOT NULL DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `tag_group_code` varchar(512) NOT NULL DEFAULT '' COMMENT '数据组标识,多个逗号分割',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_sources_info`
--

LOCK TABLES `data_sources_info` WRITE;
/*!40000 ALTER TABLE `data_sources_info` DISABLE KEYS */;
INSERT INTO `data_sources_info` VALUES (53,'mydb','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','2','0',NULL,''),(54,'csv','HDFS','','','zyc@qq.com','123456','2','0',NULL,''),(55,'mydb2','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','2','0',NULL,''),(56,'HIVE1','HIVE','','','','','2','0',NULL,''),(57,'第一个hive','HIVE','','','','','1','0','2022-02-06 02:37:38','test_group1'),(58,'mydb','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://192.168.110.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','1','0','2022-01-08 15:33:17',''),(59,'本地HDFS','HDFS','','','','','1','0',NULL,''),(60,'zdh_test','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://192.168.110.1:3306/zdh_test?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','1','0','2022-01-08 15:33:50',''),(61,'个人测试外部下载','外部下载','','','','','1','0',NULL,''),(62,'本地kafka','KAFKA','','127.0.0.1:9092','','','1','0',NULL,''),(63,'第一个clickhouse','JDBC','com.github.housepower.jdbc.ClickHouseDriver','jdbc:clickhouse://192.168.110.10:9000/datasets','default','','1','0','2022-02-06 02:38:02','test_group1'),(64,'第一个外部上传','外部上传','','','','','1','0',NULL,''),(65,'第一个sftp','SFTP','','127.0.0.1:22','zyc','123456','1','0',NULL,''),(66,'第一个tidb','TIDB','','192.168.110.10:4000','root','','1','0',NULL,''),(67,'dddd','JDBC','dddd','dddd','','','1','1',NULL,''),(68,'第一个iceberg','ICEBERG','','','','','1','0',NULL,''),(69,'11-oracle','JDBC','oracle.jdbc.driver.OracleDriver','jdbc:oracle:thin:@192.168.110.11:1521:XE','zyc','123456','1','0',NULL,''),(70,'测试a','HDFS','aaaa','','','','1','1',NULL,''),(71,'afafa','HDFS','','','','','1','1',NULL,''),(72,'aaaa','HIVE','','','','','1','1',NULL,''),(73,'sdffasdf','HDFS','','','','','1','1',NULL,''),(74,'第一个datax','DATAX','D:/Python27/python.exe D:/datax','','','','1','0',NULL,''),(75,'测试','HDFS','','','','','1','1',NULL,'');
/*!40000 ALTER TABLE `data_sources_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `every_day_notice`
--

DROP TABLE IF EXISTS `every_day_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `every_day_notice` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `msg` text COMMENT '通知消息',
  `is_delete` varchar(10) DEFAULT NULL COMMENT '是否删除消息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=932950125926420481 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `every_day_notice`
--

LOCK TABLES `every_day_notice` WRITE;
/*!40000 ALTER TABLE `every_day_notice` DISABLE KEYS */;
INSERT INTO `every_day_notice` VALUES (927677730881802240,NULL,'1'),(928067270716952576,'请不要删除任何数据！ 当前为4.7.17测试预览版-（预计2022-01-10可开放4.7.17安装包下载）-(4.7.11及之后重构整个项目,预览版和4.7.10差异较大,请从github readme 下载安装包,4.7.11及之后版本不在开放源代码，只提供安装包，免费使用)，1 优化血缘分析，2 新增整库迁移任务-并新增工具箱目录,3 修复历史版本接口权限及通知接口异常bug，4 新增接口无权限ZDH告警通知 ,值得尝试！，勿修改此通知,当前源码开源最新4.7.10版本,安装包开发至4.7.16,唯一下载地址:https://github.com/zhaoyachao/zdh_web ,其他下载均非作者提供','1'),(928067990857977856,'请不要删除任何数据！ 当前为4.7.17测试预览版-（预计2022-01-10可开放4.7.17安装包下载）-(4.7.11及之后重构整个项目,预览版和4.7.10差异较大,请从github readme 下载安装包,4.7.11及之后版本不在开放源代码，只提供安装包，免费使用)，1 优化血缘分析，2 新增整库迁移任务-并新增工具箱目录,3 修复历史版本接口权限及通知接口异常bug，4 新增接口无权限ZDH告警通知 ,值得尝试！，勿修改此通知,当前源码开源最新4.7.10版本,安装包开发至4.7.16,唯一下载地址:https://github.com/zhaoyachao/zdh_web ,其他下载均非作者提供','1'),(932950125926420480,'请不要删除任何数据！ 当前为4.7.18测试预览版-（预计2022-01-24可开放4.7.18安装包下载）-(4.7.11及之后重构整个项目,预览版和4.7.10差异较大,请从github readme 下载安装包,4.7.11及之后版本不在开放源代码，只提供安装包，免费使用)，1重构web页面, 2 重构zdh_server更名为zdh_spark, 3重构zdh_flink 更名为zdh_flinkx 4 优化系统引用三方开源项目版本-增加系统稳定性 ,值得尝试！，勿修改此通知,当前源码开源最新4.7.10版本,安装包开放至4.7.17,唯一下载地址:https://github.com/zhaoyachao/zdh_web ,其他下载均非作者提供','0');
/*!40000 ALTER TABLE `every_day_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data_tag_info`
--

DROP TABLE IF EXISTS `data_tag_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `data_tag_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tag_code` varchar(512) DEFAULT NULL COMMENT '标识code',
  `tag_name` varchar(200) DEFAULT NULL COMMENT '标识名称',
  `product_code` varchar(100) DEFAULT NULL COMMENT '产品code',
  `owner` varchar(64) DEFAULT NULL COMMENT '拥有者',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=939144017667428353 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_tag_info`
--

LOCK TABLES `data_tag_info` WRITE;
/*!40000 ALTER TABLE `data_tag_info` DISABLE KEYS */;
INSERT INTO `data_tag_info` VALUES (938864412842790912,'tag1','测试标识','zdh','1','0','2022-02-03 10:31:59','2022-02-03 11:58:25'),(939144017667428352,'sjyy1','数据应用一部','zdh','1','0','2022-02-04 05:03:02','2022-02-04 05:08:51');
/*!40000 ALTER TABLE `data_tag_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enum_info`
--

DROP TABLE IF EXISTS `enum_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enum_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `enum_code` varchar(512) DEFAULT NULL COMMENT '枚举标识',
  `enum_context` varchar(200) DEFAULT NULL COMMENT '枚举说明',
  `enum_type` varchar(100) DEFAULT NULL COMMENT '枚举类型',
  `enum_json` text COMMENT '枚举明细',
  `owner` varchar(64) DEFAULT NULL COMMENT '拥有者',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=938744488266829825 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enum_info`
--

LOCK TABLES `enum_info` WRITE;
/*!40000 ALTER TABLE `enum_info` DISABLE KEYS */;
INSERT INTO `enum_info` VALUES (934925449484570624,'sex','测试枚举-性别',NULL,'[{\"enum_value_context\":\"男性\",\"enum_value\":\"1\"},{\"enum_value_context\":\"女性\",\"enum_value\":\"2\"}]','1','0','2022-01-23 13:39:57','2022-01-23 13:52:06'),(934928928378720256,'label','测试人群标签枚举',NULL,'[{\"enum_value_context\":\"有车\",\"enum_value\":\"1\"},{\"enum_value_context\":\"有房\",\"enum_value\":\"2\"},{\"enum_value_context\":\"有女朋友\",\"enum_value\":\"3\"},{\"enum_value_context\":\"无车\",\"enum_value\":\"4\"},{\"enum_value_context\":\"无房\",\"enum_value\":\"5\"},{\"enum_value_context\":\"无女朋友\",\"enum_value\":\"6\"}]','1','0','2022-01-23 13:53:47','2022-01-23 13:53:47'),(938734652716224512,'data_ware_house_label_type','数据分类',NULL,'[{\"enum_value_context\":\"原始数据\",\"enum_value\":\"1\"},{\"enum_value_context\":\"业务数据\",\"enum_value\":\"2\"},{\"enum_value_context\":\"指标数据\",\"enum_value\":\"3\"}]','1','0','2022-02-03 01:56:22','2022-02-03 01:56:22'),(938743881074216960,'data_ware_house_label_type2','数据分类2',NULL,'[{\"enum_value_context\":\"原始数据2\",\"enum_value\":\"11\"},{\"enum_value_context\":\"业务数据2\",\"enum_value\":\"12\"},{\"enum_value_context\":\"指标数据2\",\"enum_value\":\"13\"}]','1','0','2022-02-03 02:33:02','2022-02-03 03:35:52'),(938744488266829824,'data_ware_house_label_type3','数据分类2',NULL,'[{\"enum_value_context\":\"原始数据2\",\"enum_value\":\"21\"},{\"enum_value_context\":\"业务数据2\",\"enum_value\":\"22\"},{\"enum_value_context\":\"指标数据2\",\"enum_value\":\"23\"}]','1','0','2022-02-03 02:35:27','2022-02-03 03:35:59');
/*!40000 ALTER TABLE `enum_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etl_task_flink_info`
--

DROP TABLE IF EXISTS `etl_task_flink_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etl_task_flink_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sql_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_params_input` varchar(500) DEFAULT NULL COMMENT '输入参数',
  `etl_sql` text COMMENT 'sql任务处理逻辑',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
  `host` varchar(100) DEFAULT NULL,
  `port` varchar(100) DEFAULT NULL,
  `user_name` varchar(500) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `checkpoint` varchar(256) NOT NULL DEFAULT '' COMMENT 'checkpoint地址',
  `server_type` varchar(64) DEFAULT NULL COMMENT '服务类型:windows/linux',
  `command` text COMMENT '命令',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=904464410402099201 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_task_flink_info`
--

LOCK TABLES `etl_task_flink_info` WRITE;
/*!40000 ALTER TABLE `etl_task_flink_info` DISABLE KEYS */;
INSERT INTO `etl_task_flink_info` VALUES (858391270026907648,'第一个FLINK_SQL','','CREATE TABLE user_log (\r\n  user_id VARCHAR\r\n) WITH (\r\n\'connector.type\' = \'kafka\',\r\n\'connector.version\' = \'universal\',\r\n\'connector.topic\' = \'m1\',\r\n\'connector.startup-mode\' = \'latest-offset\',\r\n\'connector.properties.0.key\' = \'zookeeper.connect\',\r\n\'connector.properties.0.value\' = \'localhost:2181\',\r\n\'connector.properties.1.key\' = \'bootstrap.servers\',\r\n\'connector.properties.1.value\' = \'localhost:9092\',\r\n\'update-mode\' = \'append\',\r\n\'format.type\' = \'csv\'\r\n);\r\n  \r\n CREATE TABLE t1 (\r\n user_id VARCHAR\r\n ) WITH (\r\n \'connector.type\' = \'jdbc\',\r\n \'connector.url\' = \'jdbc:mysql://192.168.110.1:3306/flink_test?serverTimezone=GMT%2B8\',\r\n \'connector.table\' = \'t1\',\r\n \'connector.username\' = \'zyc\',\r\n \'connector.password\' = \'123456\',\r\n \'connector.write.flush.max-rows\' = \'1\'\r\n );\r\n \r\nINSERT INTO t1 SELECT user_id FROM user_log\r\n','1','2021-06-26 09:00:27','2022-01-08 01:06:29','','','','','192.168.110.10','22','zyc','123456','hdfs://192.168.110.10/f1',NULL,'','0'),(886575336517537792,'第二个FLINK_SQL','',' CREATE TABLE d1 (\r\n addr VARCHAR,\r\n age VARCHAR,\r\n dsts VARCHAR,\r\n job VARCHAR,\r\n name VARCHAR,\r\n sex VARCHAR\r\n ) WITH (\r\n \'connector.type\' = \'jdbc\',\r\n \'connector.url\' = \'jdbc:mysql://192.168.110.1:3306/zdh_test?serverTimezone=GMT%2B8\',\r\n \'connector.table\' = \'d1\',\r\n \'connector.username\' = \'zyc\',\r\n \'connector.password\' = \'123456\',\r\n \'connector.write.flush.max-rows\' = \'1\'\r\n );\r\n  \r\n CREATE TABLE t1 (\r\n user_id VARCHAR\r\n ) WITH (\r\n \'connector.type\' = \'jdbc\',\r\n \'connector.url\' = \'jdbc:mysql://192.168.110.1:3306/flink_test?serverTimezone=GMT%2B8\',\r\n \'connector.table\' = \'t1\',\r\n \'connector.username\' = \'zyc\',\r\n \'connector.password\' = \'123456\',\r\n \'connector.write.flush.max-rows\' = \'1\'\r\n );\r\n \r\nINSERT INTO t1 SELECT age FROM d1\r\n','1','2021-09-12 03:33:52',NULL,'','','','','192.168.110.10','22','zyc','123456','',NULL,NULL,'0'),(904327604331352064,'第三个FLINK_SQL_CDC','','CREATE TABLE mysql_binlog (\r\n id int not null,\r\n job_id STRING,\r\n log_time TIMESTAMP,\r\n msg STRING,\r\n level STRING,\r\n primary key(id)  NOT ENFORCED\r\n) WITH (\r\n \'connector\' = \'mysql-cdc\',\r\n \'hostname\' = \'192.168.110.1\',\r\n \'port\' = \'3306\',\r\n \'username\' = \'zyc\',\r\n \'password\' = \'123456\',\r\n \'database-name\' = \'zdh_test\',\r\n \'table-name\' = \'zdh_logs\',\r\n \'server-time-zone\' = \'Asia/Shanghai\'\r\n);\r\n\r\nCREATE TABLE zdh_logs2 (\r\n id int not null,\r\n job_id STRING,\r\n log_time TIMESTAMP,\r\n msg STRING,\r\n level STRING,\r\n primary key(id)  NOT ENFORCED\r\n) WITH (\r\n \'connector\' = \'jdbc\',\r\n \'url\' = \'jdbc:mysql://192.168.110.1:3306/zdh_test?serverTimezone=GMT%2B8\',\r\n \'username\' = \'zyc\',\r\n \'password\' = \'123456\',\r\n \'table-name\' = \'zdh_logs2\'\r\n);\r\n\r\ninsert into zdh_logs2 (id,job_id,log_time,msg,level)\r\nselect \r\n  id,job_id,log_time,msg,level\r\nfrom mysql_binlog','1','2021-10-31 03:15:02','2022-01-16 05:18:46','','','','','192.168.110.10','22','zyc','123456','','linux','/home/zyc/zdh_flinkx-4.7.18-RELEASE/bin/start_flink.sh {{zdh_task_log_id}}','0'),(904448033733742592,'第二个FLINK_SQL_Window版本','',' CREATE TABLE d1 (\r\n addr VARCHAR,\r\n age VARCHAR,\r\n dsts VARCHAR,\r\n job VARCHAR,\r\n name VARCHAR,\r\n sex VARCHAR\r\n ) WITH (\r\n \'connector.type\' = \'jdbc\',\r\n \'connector.url\' = \'jdbc:mysql://192.168.110.1:3306/zdh_test?serverTimezone=GMT%2B8\',\r\n \'connector.table\' = \'d1\',\r\n \'connector.username\' = \'zyc\',\r\n \'connector.password\' = \'123456\',\r\n \'connector.write.flush.max-rows\' = \'1\'\r\n );\r\n  \r\n CREATE TABLE t1 (\r\n user_id VARCHAR\r\n ) WITH (\r\n \'connector.type\' = \'jdbc\',\r\n \'connector.url\' = \'jdbc:mysql://192.168.110.1:3306/flink_test?serverTimezone=GMT%2B8\',\r\n \'connector.table\' = \'t1\',\r\n \'connector.username\' = \'zyc\',\r\n \'connector.password\' = \'123456\',\r\n \'connector.write.flush.max-rows\' = \'1\'\r\n );\r\n \r\nINSERT INTO t1 SELECT age FROM d1\r\n','1','2021-10-31 11:13:35',NULL,'','','','','192.168.31.81','22','ADMINISTRATOR','199517','','windows','cd /FirefoxDownload/flink-1.12.4-bin-scala_2.11/flink-1.12.4/bin  && flink.bat run -c com.zyc.SystemInit -p 1 /home/zyc/zdh_flink.jar {{zdh_task_log_id}} --zdh_config=/home/zyc/conf FLINK_CONF_DIR= D:/flink-1.12.4/conf','0'),(904464410402099200,'第三个FLINK_SQL_CDC_Windows版本','','CREATE TABLE mysql_binlog (\r\n id int not null,\r\n job_id STRING,\r\n log_time TIMESTAMP,\r\n msg STRING,\r\n level STRING,\r\n primary key(id)  NOT ENFORCED\r\n) WITH (\r\n \'connector\' = \'mysql-cdc\',\r\n \'hostname\' = \'192.168.110.1\',\r\n \'port\' = \'3306\',\r\n \'username\' = \'zyc\',\r\n \'password\' = \'123456\',\r\n \'database-name\' = \'zdh_test\',\r\n \'table-name\' = \'zdh_logs\',\r\n \'server-time-zone\' = \'Asia/Shanghai\'\r\n);\r\n\r\nCREATE TABLE zdh_logs2 (\r\n id int not null,\r\n job_id STRING,\r\n log_time TIMESTAMP,\r\n msg STRING,\r\n level STRING,\r\n primary key(id)  NOT ENFORCED\r\n) WITH (\r\n \'connector\' = \'jdbc\',\r\n \'url\' = \'jdbc:mysql://192.168.110.1:3306/zdh_test?serverTimezone=GMT%2B8\',\r\n \'username\' = \'zyc\',\r\n \'password\' = \'123456\',\r\n \'table-name\' = \'zdh_logs2\'\r\n);\r\n\r\ninsert INTO zdh_logs2 (id,job_id,log_time,msg,level)\r\nselect \r\n  id,job_id,log_time,msg,level\r\nfrom mysql_binlog','1','2021-10-31 12:18:40',NULL,'','','','','192.168.31.81','22','ADMINISTRATOR','199517','','windows','cd /FirefoxDownload/flink-1.12.4-bin-scala_2.11/flink-1.12.4/bin  && flink.bat run -c com.zyc.SystemInit -p 1 /home/zyc/zdh_flink.jar {{zdh_task_log_id}} --zdh_config=/home/zyc/conf FLINK_CONF_DIR= D:/flink-1.12.4/conf','0');
/*!40000 ALTER TABLE `etl_task_flink_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `approval_auditor_info`
--

DROP TABLE IF EXISTS `approval_auditor_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `approval_auditor_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL DEFAULT '0' COMMENT '审批流程code',
  `level` int NOT NULL DEFAULT '0' COMMENT '审批节点',
  `auditor_id` varchar(64) NOT NULL DEFAULT '' COMMENT '审批人id',
  `auditor_group` varchar(64) NOT NULL DEFAULT '' COMMENT '审批人所在组',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `approval_auditor_info`
--

LOCK TABLES `approval_auditor_info` WRITE;
/*!40000 ALTER TABLE `approval_auditor_info` DISABLE KEYS */;
INSERT INTO `approval_auditor_info` VALUES (1,'data',1,'1','6,7,8',NULL),(3,'data',7,'1','6,7,8',NULL);
/*!40000 ALTER TABLE `approval_auditor_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `server_task_instance`
--

DROP TABLE IF EXISTS `server_task_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `server_task_instance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `templete_id` varchar(100) DEFAULT NULL,
  `build_task` varchar(200) DEFAULT NULL COMMENT '构建任务说明',
  `build_ip` varchar(200) DEFAULT NULL COMMENT '构建服务器',
  `git_url` varchar(500) DEFAULT NULL COMMENT 'git地址',
  `build_type` varchar(10) DEFAULT NULL COMMENT '构建工具类型,GRADLE/MAVEN',
  `build_command` text COMMENT '构建命令',
  `remote_ip` varchar(200) DEFAULT NULL COMMENT '部署服务器',
  `remote_path` varchar(200) DEFAULT NULL COMMENT '部署路径',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `version_type` varchar(200) DEFAULT NULL COMMENT '部署类型BRANCH/TAG',
  `version` varchar(200) DEFAULT NULL COMMENT '版本',
  `build_branch` varchar(200) DEFAULT NULL COMMENT '分支/标签',
  `status` varchar(200) DEFAULT NULL COMMENT '部署状态',
  `build_username` varchar(100) DEFAULT NULL COMMENT '构建用户',
  `build_privatekey` text COMMENT '构建服务器密钥地址',
  `build_path` varchar(500) DEFAULT NULL COMMENT '构建地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=809858569640873985 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `server_task_instance`
--

LOCK TABLES `server_task_instance` WRITE;
/*!40000 ALTER TABLE `server_task_instance` DISABLE KEYS */;
INSERT INTO `server_task_instance` VALUES (809855523523399680,'1','server构建任务','192.168.110.10','','GRADLE','sh gradlew release -x test','127.0.0.1','/home/zyc/zdh_server_build','2021-02-12 10:36:43','2021-02-12 10:36:43','1','branch','20210212','master','1','zyc','123456','/home/zyc/zdh_server'),(809858569640873984,'1','server构建任务','192.168.110.10','','GRADLE','sh gradlew release -x test','127.0.0.1','/home/zyc/zdh_server_build','2021-02-12 10:48:50','2021-02-12 10:48:50','1','branch','20210212','master','1','zyc','123456','/home/zyc/zdh_server');
/*!40000 ALTER TABLE `server_task_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blood_source_info`
--

DROP TABLE IF EXISTS `blood_source_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blood_source_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `input_type` varchar(200) DEFAULT NULL,
  `input_md5` varchar(100) DEFAULT NULL COMMENT '输入源唯一标识，数据源类型+数据源url 组合生成的md5',
  `input` varchar(100) DEFAULT NULL COMMENT '数据库名称+表名/远程文件路径',
  `output_type` text COMMENT '输出数据源类型',
  `output_md5` varchar(100) DEFAULT NULL COMMENT '输入源唯一标识，数据源类型+数据源url 组合生成的md5',
  `output` varchar(100) DEFAULT NULL COMMENT '数据库名称+表名/远程文件路径',
  `version` varchar(100) DEFAULT NULL COMMENT 'version',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=229 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blood_source_info`
--

LOCK TABLES `blood_source_info` WRITE;
/*!40000 ALTER TABLE `blood_source_info` DISABLE KEYS */;
INSERT INTO `blood_source_info` VALUES (107,'单分割符无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t1.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t1','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(108,'单分割符自带标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/h1.txt','JDBC','f25bd7722f1648b4f34471d76bade553','h1','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(109,'多分割符无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t2.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t2','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(110,'tab分割无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t3.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t3','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(111,'单分割符无标题输出hudi','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t1.txt','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/hudi/t1','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(112,'hudi输出多分割符无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/hudi/t1','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/hudi_t1','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(113,'kafka接收客户信息存mydb','KAFKA','c3e818491c9c88fc1f97156229a0b34c','m1','JDBC','f25bd7722f1648b4f34471d76bade553','m1','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(114,'单分割符无标题2','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t4.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t4','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(115,'单分割符无标题3','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t5.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t5','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(116,'clickhouse_datasets.z1转zdh_test.z1','JDBC','8a7f599d76f3dac984153f0c48301bd6','datasets.z1','JDBC','f25bd7722f1648b4f34471d76bade553','z1','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(117,'sftp测试','SFTP','ab8c56e2fa237db723762700b9169e31','/home/zyc/work/t1.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t1','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(118,'mydb#account_info','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','',NULL,NULL,NULL,'1','2022-01-03 03:15:46'),(119,'测试tidb写入','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','TIDB','abe5422ed42fbb1f886eead20f0cf29c','d1.t3','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(120,'第一个tidb读取','TIDB','abe5422ed42fbb1f886eead20f0cf29c','d1.t3','JDBC','f25bd7722f1648b4f34471d76bade553','tidb_t3','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(121,'mysql转hfds测试','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/zdh/data/account_info','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(122,'jdbc优化过滤','JDBC','3674d0bb8ca93d624efe38918cd277c4','data_sources_type_info','JDBC','f25bd7722f1648b4f34471d76bade553','ds2','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(123,'mydb->iceberg','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','ICEBERG','88b3090dd30be9bcc9f5f172dfb10fc3','test.account_info','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(124,'mysql2oracle','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','JDBC','18c3c2d9337b6192018637552e373cd6','account_info','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(125,'测试新增参数','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t1.txt','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/output/t1','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(126,'测试hdfs写入单文件','JDBC','3674d0bb8ca93d624efe38918cd277c4','zdh_logs','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/output/zdh_ha.txt','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(127,'tttt','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','外部下载','059c770dd92c016a28acd469af8e170f','aa','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(128,'batch_account_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','JDBC','f25bd7722f1648b4f34471d76bade553','account_info','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(129,'batch_act2_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','act2','JDBC','f25bd7722f1648b4f34471d76bade553','act2','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(130,'batch_apply_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','apply_info','JDBC','f25bd7722f1648b4f34471d76bade553','apply_info','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(131,'batch_approval_auditor_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','approval_auditor_info','JDBC','f25bd7722f1648b4f34471d76bade553','approval_auditor_info','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(132,'batch_approval_config_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','approval_config_info','JDBC','f25bd7722f1648b4f34471d76bade553','approval_config_info','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(133,'batch_approval_event_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','approval_event_info','JDBC','f25bd7722f1648b4f34471d76bade553','approval_event_info','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(134,'单分割符自带标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/h1.txt','HDFS','99c35850ff7cf7e436b03acedd4c59b3','','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(135,'mydb#account_info','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/output/account_info','2022-01-03 11:15:45','1','2022-01-03 03:15:46'),(136,'第一个jdbc','JDBC','3674d0bb8ca93d624efe38918cd277c4','notice2','JDBC','3674d0bb8ca93d624efe38918cd277c4','notice','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(137,'单分割符无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t1.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t1','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(138,'单分割符自带标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/h1.txt','JDBC','f25bd7722f1648b4f34471d76bade553','h1','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(139,'多分割符无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t2.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t2','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(140,'tab分割无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t3.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t3','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(141,'单分割符无标题输出hudi','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t1.txt','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/hudi/t1','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(142,'hudi输出多分割符无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/hudi/t1','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/hudi_t1','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(143,'kafka接收客户信息存mydb','KAFKA','c3e818491c9c88fc1f97156229a0b34c','m1','JDBC','f25bd7722f1648b4f34471d76bade553','m1','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(144,'单分割符无标题2','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t4.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t4','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(145,'单分割符无标题3','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t5.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t5','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(146,'clickhouse_datasets.z1转zdh_test.z1','JDBC','8a7f599d76f3dac984153f0c48301bd6','datasets.z1','JDBC','f25bd7722f1648b4f34471d76bade553','z1','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(147,'sftp测试','SFTP','ab8c56e2fa237db723762700b9169e31','/home/zyc/work/t1.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t1','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(148,'mydb#account_info','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','',NULL,NULL,NULL,'1','2022-01-03 03:39:28'),(149,'测试tidb写入','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','TIDB','abe5422ed42fbb1f886eead20f0cf29c','d1.t3','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(150,'第一个tidb读取','TIDB','abe5422ed42fbb1f886eead20f0cf29c','d1.t3','JDBC','f25bd7722f1648b4f34471d76bade553','tidb_t3','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(151,'mysql转hfds测试','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/zdh/data/account_info','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(152,'jdbc优化过滤','JDBC','3674d0bb8ca93d624efe38918cd277c4','data_sources_type_info','JDBC','f25bd7722f1648b4f34471d76bade553','ds2','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(153,'mydb->iceberg','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','ICEBERG','88b3090dd30be9bcc9f5f172dfb10fc3','test.account_info','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(154,'mysql2oracle','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','JDBC','18c3c2d9337b6192018637552e373cd6','account_info','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(155,'测试新增参数','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t1.txt','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/output/t1','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(156,'测试hdfs写入单文件','JDBC','3674d0bb8ca93d624efe38918cd277c4','zdh_logs','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/output/zdh_ha.txt','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(157,'tttt','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','外部下载','059c770dd92c016a28acd469af8e170f','aa','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(158,'batch_account_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','JDBC','f25bd7722f1648b4f34471d76bade553','account_info','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(159,'batch_act2_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','act2','JDBC','f25bd7722f1648b4f34471d76bade553','act2','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(160,'batch_apply_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','apply_info','JDBC','f25bd7722f1648b4f34471d76bade553','apply_info','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(161,'batch_approval_auditor_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','approval_auditor_info','JDBC','f25bd7722f1648b4f34471d76bade553','approval_auditor_info','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(162,'batch_approval_config_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','approval_config_info','JDBC','f25bd7722f1648b4f34471d76bade553','approval_config_info','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(163,'batch_approval_event_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','approval_event_info','JDBC','f25bd7722f1648b4f34471d76bade553','approval_event_info','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(164,'单分割符自带标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/h1.txt','HDFS','99c35850ff7cf7e436b03acedd4c59b3','','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(165,'mydb#account_info','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/output/account_info','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(166,'第一个jdbc','JDBC','3674d0bb8ca93d624efe38918cd277c4','notice2','JDBC','3674d0bb8ca93d624efe38918cd277c4','notice','2022-01-03 11:39:27','1','2022-01-03 03:39:28'),(167,'第一个jdbc','JDBC','3674d0bb8ca93d624efe38918cd277c4','notice2','JDBC','3674d0bb8ca93d624efe38918cd277c4','notice','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(168,'单分割符无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t1.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t1','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(169,'单分割符自带标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/h1.txt','JDBC','f25bd7722f1648b4f34471d76bade553','h1','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(170,'多分割符无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t2.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t2','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(171,'tab分割无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t3.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t3','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(172,'单分割符无标题输出hudi','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t1.txt','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/hudi/t1','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(173,'hudi输出多分割符无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/hudi/t1','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/hudi_t1','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(174,'kafka接收客户信息存mydb','KAFKA','c3e818491c9c88fc1f97156229a0b34c','m1','JDBC','f25bd7722f1648b4f34471d76bade553','m1','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(175,'单分割符无标题2','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t4.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t4','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(176,'单分割符无标题3','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t5.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t5','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(177,'clickhouse_datasets.z1转zdh_test.z1','JDBC','8a7f599d76f3dac984153f0c48301bd6','datasets.z1','JDBC','f25bd7722f1648b4f34471d76bade553','z1','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(178,'sftp测试','SFTP','ab8c56e2fa237db723762700b9169e31','/home/zyc/work/t1.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t1','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(179,'mydb#account_info','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','',NULL,NULL,NULL,'1','2022-01-03 03:41:33'),(180,'测试tidb写入','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','TIDB','abe5422ed42fbb1f886eead20f0cf29c','d1.t3','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(181,'第一个tidb读取','TIDB','abe5422ed42fbb1f886eead20f0cf29c','d1.t3','JDBC','f25bd7722f1648b4f34471d76bade553','tidb_t3','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(182,'mysql转hfds测试','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/zdh/data/account_info','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(183,'jdbc优化过滤','JDBC','3674d0bb8ca93d624efe38918cd277c4','data_sources_type_info','JDBC','f25bd7722f1648b4f34471d76bade553','ds2','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(184,'mydb->iceberg','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','ICEBERG','88b3090dd30be9bcc9f5f172dfb10fc3','test.account_info','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(185,'mysql2oracle','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','JDBC','18c3c2d9337b6192018637552e373cd6','account_info','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(186,'测试新增参数','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t1.txt','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/output/t1','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(187,'测试hdfs写入单文件','JDBC','3674d0bb8ca93d624efe38918cd277c4','zdh_logs','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/output/zdh_ha.txt','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(188,'tttt','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','外部下载','059c770dd92c016a28acd469af8e170f','aa','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(189,'batch_account_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','JDBC','f25bd7722f1648b4f34471d76bade553','account_info','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(190,'batch_act2_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','act2','JDBC','f25bd7722f1648b4f34471d76bade553','act2','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(191,'batch_apply_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','apply_info','JDBC','f25bd7722f1648b4f34471d76bade553','apply_info','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(192,'batch_approval_auditor_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','approval_auditor_info','JDBC','f25bd7722f1648b4f34471d76bade553','approval_auditor_info','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(193,'batch_approval_config_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','approval_config_info','JDBC','f25bd7722f1648b4f34471d76bade553','approval_config_info','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(194,'batch_approval_event_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','approval_event_info','JDBC','f25bd7722f1648b4f34471d76bade553','approval_event_info','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(195,'单分割符自带标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/h1.txt','HDFS','99c35850ff7cf7e436b03acedd4c59b3','','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(196,'mydb#account_info','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/output/account_info','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(197,'第一个jdbc','JDBC','3674d0bb8ca93d624efe38918cd277c4','notice2','JDBC','3674d0bb8ca93d624efe38918cd277c4','notice','2022-01-03 11:41:32','1','2022-01-03 03:41:33'),(198,'第一个jdbc','JDBC','3674d0bb8ca93d624efe38918cd277c4','notice2','JDBC','3674d0bb8ca93d624efe38918cd277c4','notice','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(199,'单分割符无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t1.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t1','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(200,'单分割符自带标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/h1.txt','JDBC','f25bd7722f1648b4f34471d76bade553','h1','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(201,'多分割符无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t2.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t2','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(202,'tab分割无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t3.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t3','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(203,'单分割符无标题输出hudi','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t1.txt','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/hudi/t1','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(204,'hudi输出多分割符无标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/hudi/t1','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/hudi_t1','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(205,'kafka接收客户信息存mydb','KAFKA','c3e818491c9c88fc1f97156229a0b34c','m1','JDBC','f25bd7722f1648b4f34471d76bade553','m1','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(206,'单分割符无标题2','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t4.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t4','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(207,'单分割符无标题3','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t5.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t5','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(208,'clickhouse_datasets.z1转zdh_test.z1','JDBC','8a7f599d76f3dac984153f0c48301bd6','datasets.z1','JDBC','f25bd7722f1648b4f34471d76bade553','z1','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(209,'sftp测试','SFTP','ab8c56e2fa237db723762700b9169e31','/home/zyc/work/t1.txt','JDBC','f25bd7722f1648b4f34471d76bade553','t1','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(210,'mydb#account_info','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','',NULL,NULL,NULL,'1','2022-01-03 05:05:11'),(211,'测试tidb写入','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','TIDB','abe5422ed42fbb1f886eead20f0cf29c','d1.t3','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(212,'第一个tidb读取','TIDB','abe5422ed42fbb1f886eead20f0cf29c','d1.t3','JDBC','f25bd7722f1648b4f34471d76bade553','tidb_t3','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(213,'mysql转hfds测试','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/zdh/data/account_info','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(214,'jdbc优化过滤','JDBC','3674d0bb8ca93d624efe38918cd277c4','data_sources_type_info','JDBC','f25bd7722f1648b4f34471d76bade553','ds2','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(215,'mydb->iceberg','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','ICEBERG','88b3090dd30be9bcc9f5f172dfb10fc3','test.account_info','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(216,'mysql2oracle','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','JDBC','18c3c2d9337b6192018637552e373cd6','account_info','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(217,'测试新增参数','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/t1.txt','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/output/t1','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(218,'测试hdfs写入单文件','JDBC','3674d0bb8ca93d624efe38918cd277c4','zdh_logs','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/output/zdh_ha.txt','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(219,'tttt','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','外部下载','059c770dd92c016a28acd469af8e170f','aa','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(220,'batch_account_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','JDBC','f25bd7722f1648b4f34471d76bade553','account_info','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(221,'batch_act2_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','act2','JDBC','f25bd7722f1648b4f34471d76bade553','act2','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(222,'batch_apply_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','apply_info','JDBC','f25bd7722f1648b4f34471d76bade553','apply_info','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(223,'batch_approval_auditor_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','approval_auditor_info','JDBC','f25bd7722f1648b4f34471d76bade553','approval_auditor_info','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(224,'batch_approval_config_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','approval_config_info','JDBC','f25bd7722f1648b4f34471d76bade553','approval_config_info','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(225,'batch_approval_event_info_123','JDBC','3674d0bb8ca93d624efe38918cd277c4','approval_event_info','JDBC','f25bd7722f1648b4f34471d76bade553','approval_event_info','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(226,'单分割符自带标题','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/csv/h1.txt','HDFS','99c35850ff7cf7e436b03acedd4c59b3','','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(227,'mydb#account_info','JDBC','3674d0bb8ca93d624efe38918cd277c4','account_info','HDFS','99c35850ff7cf7e436b03acedd4c59b3','/data/output/account_info','2022-01-03 13:05:10','1','2022-01-03 05:05:11'),(228,'第一个jdbc','JDBC','3674d0bb8ca93d624efe38918cd277c4','notice2','JDBC','3674d0bb8ca93d624efe38918cd277c4','notice','2022-01-03 13:05:10','1','2022-01-03 05:05:11');
/*!40000 ALTER TABLE `blood_source_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `apply_info`
--

DROP TABLE IF EXISTS `apply_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apply_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apply_context` varchar(500) DEFAULT NULL,
  `issue_id` varchar(200) DEFAULT NULL COMMENT '数据发布id',
  `approve_id` varchar(200) DEFAULT NULL COMMENT '审批人id',
  `status` varchar(10) DEFAULT NULL COMMENT '状态0:初始态,1:通过,2:未通过',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `reason` text COMMENT '原因',
  `is_notice` varchar(8) NOT NULL DEFAULT 'false' COMMENT '是否已经通知true/false',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=934780940612276225 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apply_info`
--

LOCK TABLES `apply_info` WRITE;
/*!40000 ALTER TABLE `apply_info` DISABLE KEYS */;
INSERT INTO `apply_info` VALUES (838713411268251648,'account_info','7','1','1','2021-05-03 01:47:40','2021-05-03 01:47:40','2',NULL,'false'),(839147351305097216,'zdh_logs','8','2','1','2021-05-04 06:31:59','2021-05-04 06:31:59','1',NULL,'false'),(853599398993596416,'hive_t1','10','1','1','2021-06-13 03:39:16','2021-06-13 03:39:16','2',NULL,'false'),(903082514808049664,'account_info','903076295573770240','1','1','2021-10-27 16:47:30','2021-10-27 16:47:30','2',NULL,'false'),(903085573202251776,'account_info','903076295573770240','1','0','2021-10-27 16:59:39','2021-10-27 16:59:39','2',NULL,'true'),(934780940612276224,'server_task_info','934780735389175808','2','1','2022-01-23 04:05:44','2022-01-23 04:05:44','1',NULL,'true');
/*!40000 ALTER TABLE `apply_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etl_task_batch_info`
--

DROP TABLE IF EXISTS `etl_task_batch_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
  `status` varchar(8) NOT NULL DEFAULT '0' COMMENT '0:未执行,1:执行中,2:执行失败,3:执行成功',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=927221878378991617 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_task_batch_info`
--

LOCK TABLES `etl_task_batch_info` WRITE;
/*!40000 ALTER TABLE `etl_task_batch_info` DISABLE KEYS */;
INSERT INTO `etl_task_batch_info` VALUES (927221878378991616,'batch','123','58','JDBC',NULL,'a.*','','','60','JDBC',NULL,NULL,'','','1','2022-01-02 07:28:43','','','','',NULL,NULL,NULL,'','','',NULL,'','','','','','-1','2022-01-02 11:16:12','0','3');
/*!40000 ALTER TABLE `etl_task_batch_info` ENABLE KEYS */;
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
  `status` varchar(64) DEFAULT NULL COMMENT '1:发布,2:未发布',
  `label_params` text COMMENT '数据标签,多个逗号分割',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=934780735389175809 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issue_data_info`
--

LOCK TABLES `issue_data_info` WRITE;
/*!40000 ALTER TABLE `issue_data_info` DISABLE KEYS */;
INSERT INTO `issue_data_info` VALUES (8,'测试日志表','53','JDBC','zdh_logs','zdh_logs','','job_id,log_time,msg,level,task_logs_id','[{\"column_md5\":\"5b5-4cc6-94dd-d9\",\"column_name\":\"job_id\",\"column_type\":\"string\"},{\"column_md5\":\"f27-4b43-9cb1-95\",\"column_name\":\"log_time\",\"column_type\":\"string\"},{\"column_md5\":\"41f-4c05-8026-98\",\"column_name\":\"msg\",\"column_type\":\"string\"},{\"column_md5\":\"de8-4d75-9a40-e8\",\"column_name\":\"level\",\"column_type\":\"string\"},{\"column_md5\":\"690-4941-88bd-1a\",\"column_name\":\"task_logs_id\",\"column_type\":\"string\"}]','2','2021-05-04 01:04:44','A-公司','','','','1','1'),(903073104664727552,'第二个发布task_logs','58','JDBC','','','','id,job_id,job_context,etl_date,status,start_time,update_time,owner,is_notice,process,thread_id,retry_time,executor,url,etl_info,application_id,history_server,master,server_ack','[{\"column_md5\":\"e5d-4eee-993d-8f\",\"column_name\":\"id\",\"column_type\":\"string\",\"column_desc\":\"主键\"},{\"column_md5\":\"73b-4297-97c4-c2\",\"column_name\":\"job_id\",\"column_type\":\"string\"},{\"column_md5\":\"c48-40e3-9e0d-a1\",\"column_name\":\"job_context\",\"column_type\":\"string\"},{\"column_md5\":\"839-4e62-97cf-d4\",\"column_name\":\"etl_date\",\"column_type\":\"string\"},{\"column_md5\":\"db6-46c6-99d7-b2\",\"column_name\":\"status\",\"column_type\":\"string\"},{\"column_md5\":\"2aa-40ba-8bb9-d1\",\"column_name\":\"start_time\",\"column_type\":\"string\"},{\"column_md5\":\"72c-49f9-9461-41\",\"column_name\":\"update_time\",\"column_type\":\"string\"},{\"column_md5\":\"ed2-4ff5-9884-80\",\"column_name\":\"owner\",\"column_type\":\"string\"},{\"column_md5\":\"14e-47d6-85cb-b5\",\"column_name\":\"is_notice\",\"column_type\":\"string\"},{\"column_md5\":\"8ec-43ab-960b-bf\",\"column_name\":\"process\",\"column_type\":\"string\"},{\"column_md5\":\"794-4581-b05a-65\",\"column_name\":\"thread_id\",\"column_type\":\"string\"},{\"column_md5\":\"12d-4a5b-b933-a6\",\"column_name\":\"retry_time\",\"column_type\":\"string\"},{\"column_md5\":\"cf0-49d1-a29b-29\",\"column_name\":\"executor\",\"column_type\":\"string\"},{\"column_md5\":\"8c4-453e-aa7c-1e\",\"column_name\":\"url\",\"column_type\":\"string\"},{\"column_md5\":\"962-454b-8a68-7f\",\"column_name\":\"etl_info\",\"column_type\":\"string\"},{\"column_md5\":\"f08-4933-af5b-fe\",\"column_name\":\"application_id\",\"column_type\":\"string\"},{\"column_md5\":\"9e6-4322-a676-a8\",\"column_name\":\"history_server\",\"column_type\":\"string\"},{\"column_md5\":\"b0d-4c6d-8c02-33\",\"column_name\":\"master\",\"column_type\":\"string\"},{\"column_md5\":\"587-43c9-a3b1-2c\",\"column_name\":\"server_ack\",\"column_type\":\"string\"}]','1','2021-10-27 16:10:06','d','ddd12','avc','','1','1,3,23'),(903076295573770240,'第3个发布','58','JDBC','account_info','account_info','','id,user_name,user_password,email,is_use_email,phone,is_use_phone,enable,user_group,roles,signature','[{\"column_md5\":\"762-4308-a4a3-38\",\"column_name\":\"id\",\"column_type\":\"string\",\"column_desc\":\"主键2\"},{\"column_md5\":\"c52-4928-9586-5b\",\"column_name\":\"user_name\",\"column_type\":\"string\"},{\"column_md5\":\"771-45c6-832c-df\",\"column_name\":\"user_password\",\"column_type\":\"string\"},{\"column_md5\":\"374-4b11-8964-9d\",\"column_name\":\"email\",\"column_type\":\"string\"},{\"column_md5\":\"126-45b5-b1fc-7c\",\"column_name\":\"is_use_email\",\"column_type\":\"string\"},{\"column_md5\":\"197-4bdc-9b61-94\",\"column_name\":\"phone\",\"column_type\":\"string\"},{\"column_md5\":\"92d-4b03-9e1c-ac\",\"column_name\":\"is_use_phone\",\"column_type\":\"string\"},{\"column_md5\":\"d32-4f2e-a100-2b\",\"column_name\":\"enable\",\"column_type\":\"string\"},{\"column_md5\":\"c40-45cd-94ff-ad\",\"column_name\":\"user_group\",\"column_type\":\"string\"},{\"column_md5\":\"b77-478d-8ddf-4f\",\"column_name\":\"roles\",\"column_type\":\"string\"},{\"column_md5\":\"28a-4858-977b-28\",\"column_name\":\"signature\",\"column_type\":\"string\"}]','1','2021-10-27 16:22:47','','','','','1','1'),(934765595805618176,'测试新版审批','58','JDBC','notice_info','notice_info','','id,msg_type,msg_title,msg_url,msg,is_see,owner,create_time,update_time','[{\"column_md5\":\"f28-4cae-bf38-13\",\"column_name\":\"id\",\"column_type\":\"string\"},{\"column_md5\":\"ef9-4a7e-ac67-a8\",\"column_name\":\"msg_type\",\"column_type\":\"string\"},{\"column_md5\":\"9dc-4189-a280-a2\",\"column_name\":\"msg_title\",\"column_type\":\"string\"},{\"column_md5\":\"7f4-466c-a2a7-b4\",\"column_name\":\"msg_url\",\"column_type\":\"string\"},{\"column_md5\":\"72a-42cc-ad66-bf\",\"column_name\":\"msg\",\"column_type\":\"string\"},{\"column_md5\":\"ad6-47d1-a218-14\",\"column_name\":\"is_see\",\"column_type\":\"string\"},{\"column_md5\":\"c6a-4c76-b536-90\",\"column_name\":\"owner\",\"column_type\":\"string\"},{\"column_md5\":\"be4-4826-86a4-ed\",\"column_name\":\"create_time\",\"column_type\":\"string\"},{\"column_md5\":\"d5a-4a83-9ad0-25\",\"column_name\":\"update_time\",\"column_type\":\"string\"}]','1','2022-01-23 03:04:45','','','','','1','1'),(934779987913871360,'admin发布测试','58','JDBC','every_day_notice','every_day_notice','','id,msg,is_delete','[{\"column_md5\":\"dcf-46bf-8fb8-7c\",\"column_name\":\"id\",\"column_type\":\"string\"},{\"column_md5\":\"035-4fb9-823e-89\",\"column_name\":\"msg\",\"column_type\":\"string\"},{\"column_md5\":\"2c3-4c18-a6f1-c2\",\"column_name\":\"is_delete\",\"column_type\":\"string\"}]','1','2022-01-23 04:01:56','','','','','1','1,22'),(934780735389175808,'admin用户发布测试','53','JDBC','server_task_info','','','id,build_task,build_ip,git_url,build_type,build_command,remote_ip,remote_path,create_time,update_time,owner,build_branch,build_username,build_privatekey,build_path','[{\"column_md5\":\"22c-434c-98d7-90\",\"column_name\":\"id\",\"column_type\":\"string\"},{\"column_md5\":\"e7f-4c10-932d-f9\",\"column_name\":\"build_task\",\"column_type\":\"string\"},{\"column_md5\":\"00b-4db3-a9ae-5f\",\"column_name\":\"build_ip\",\"column_type\":\"string\"},{\"column_md5\":\"755-42be-89fd-d9\",\"column_name\":\"git_url\",\"column_type\":\"string\"},{\"column_md5\":\"0a0-412d-919e-90\",\"column_name\":\"build_type\",\"column_type\":\"string\"},{\"column_md5\":\"45c-4925-b03c-e4\",\"column_name\":\"build_command\",\"column_type\":\"string\"},{\"column_md5\":\"d22-4878-85aa-27\",\"column_name\":\"remote_ip\",\"column_type\":\"string\"},{\"column_md5\":\"f55-421e-8466-a8\",\"column_name\":\"remote_path\",\"column_type\":\"string\"},{\"column_md5\":\"ddb-4e0c-b179-a4\",\"column_name\":\"create_time\",\"column_type\":\"string\"},{\"column_md5\":\"fd8-49ef-9e8b-df\",\"column_name\":\"update_time\",\"column_type\":\"string\"},{\"column_md5\":\"f3d-4203-ad56-fc\",\"column_name\":\"owner\",\"column_type\":\"string\"},{\"column_md5\":\"731-4295-89f0-ba\",\"column_name\":\"build_branch\",\"column_type\":\"string\"},{\"column_md5\":\"1fc-4006-bc3f-4d\",\"column_name\":\"build_username\",\"column_type\":\"string\"},{\"column_md5\":\"da8-4ebc-aa1c-42\",\"column_name\":\"build_privatekey\",\"column_type\":\"string\"},{\"column_md5\":\"0c1-4771-8cfb-72\",\"column_name\":\"build_path\",\"column_type\":\"string\"}]','2','2022-01-23 04:04:55','','','','','1','1');
/*!40000 ALTER TABLE `issue_data_info` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_task_update_logs`
--

LOCK TABLES `etl_task_update_logs` WRITE;
/*!40000 ALTER TABLE `etl_task_update_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `etl_task_update_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality_rule_info`
--

DROP TABLE IF EXISTS `quality_rule_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality_rule_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rule_code` varchar(200) DEFAULT NULL COMMENT '规则code',
  `rule_name` varchar(100) DEFAULT NULL COMMENT '规则名称',
  `rule_type` varchar(100) DEFAULT NULL COMMENT '规则类型,1:sql表达式,2:正则',
  `rule_expr` text COMMENT '规则内容',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=919638908919091201 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality_rule_info`
--

LOCK TABLES `quality_rule_info` WRITE;
/*!40000 ALTER TABLE `quality_rule_info` DISABLE KEYS */;
INSERT INTO `quality_rule_info` VALUES (917938534663327744,'aaa','123','1','1243',NULL,NULL,NULL),(917940616715833344,'phone','手机号验证','2','^1(3|4|5|6|7|8|9)\\d{9}$',NULL,'2021-12-07 16:48:18','2021-12-11 02:30:30'),(919571916811931648,'substring','长度校验','1','length($column) = 11','1','2021-12-12 04:50:30','2021-12-12 04:50:30'),(919638908919091200,'primary_key','主键校验','3','primary_key_check','1','2021-12-12 09:16:42','2021-12-12 09:16:42');
/*!40000 ALTER TABLE `quality_rule_info` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 ;
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
-- Table structure for table `notice_info`
--

DROP TABLE IF EXISTS `notice_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `msg_type` varchar(200) DEFAULT NULL COMMENT '消息类型',
  `msg_title` varchar(500) DEFAULT NULL COMMENT '主题',
  `msg_url` varchar(500) DEFAULT NULL COMMENT '消息连接',
  `msg` text COMMENT '消息',
  `is_see` varchar(100) DEFAULT NULL COMMENT '是否查看,true/false',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=883 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice_info`
--

LOCK TABLES `notice_info` WRITE;
/*!40000 ALTER TABLE `notice_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `notice_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_group_info`
--

DROP TABLE IF EXISTS `user_group_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_group_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '组名',
  `enable` varchar(8) DEFAULT 'false' COMMENT '是否启用true/false',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_group_info`
--

LOCK TABLES `user_group_info` WRITE;
/*!40000 ALTER TABLE `user_group_info` DISABLE KEYS */;
INSERT INTO `user_group_info` VALUES (6,'大数据一部','true',NULL,NULL),(7,'大数据二部','true',NULL,NULL),(8,'大数据三部','true',NULL,NULL),(9,'大数据5部','true','2021-10-03 13:27:45','2021-10-03 13:27:45'),(10,'g1','true','2021-10-30 10:24:08','2021-10-30 10:24:08');
/*!40000 ALTER TABLE `user_group_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etl_drools_task_info`
--

DROP TABLE IF EXISTS `etl_drools_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etl_drools_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `etl_id` varchar(200) DEFAULT NULL COMMENT '任务id',
  `etl_drools` text COMMENT 'drools任务逻辑',
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
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=749279630074056705 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_drools_task_info`
--

LOCK TABLES `etl_drools_task_info` WRITE;
/*!40000 ALTER TABLE `etl_drools_task_info` DISABLE KEYS */;
INSERT INTO `etl_drools_task_info` VALUES (2,'第一个drools','719619870378954752','package rules\r\nimport java.util.HashMap\r\nrule \"alarm2\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") != \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"false\");\r\nend\r\n\r\nrule \"alarm\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") == \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"true\");\r\nend','dsts=true','60','JDBC','d1','d1','','drop table d1','1','2020-07-25 04:38:41',NULL,'','','',NULL,'单源ETL','','','-1'),(749258437296132096,'hive->mysql->drools','718814940856586240','package rules\r\nimport java.util.HashMap\r\nrule \"alarm2\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") != \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"false\");\r\nend\r\n\r\nrule \"alarm\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") == \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"true\");\r\nend','','60','JDBC','act2','act2','','drop table act2','1','2020-08-29 05:25:32',NULL,'','','',NULL,'SQL','','','-1'),(749279630074056704,'more_test_account_info','2','package rules\r\nimport java.util.HashMap\r\nrule \"alarm2\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") != \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"false\");\r\nend\r\n\r\nrule \"alarm\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") == \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"true\");\r\nend','','60','JDBC','act2','act2','','drop table act2','1','2020-08-29 06:49:45',NULL,'','','',NULL,'多源ETL','','','-1');
/*!40000 ALTER TABLE `etl_drools_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etl_task_datax_info`
--

DROP TABLE IF EXISTS `etl_task_datax_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etl_task_datax_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `datax_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
  `datax_json` text,
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=916709157153804289 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_task_datax_info`
--

LOCK TABLES `etl_task_datax_info` WRITE;
/*!40000 ALTER TABLE `etl_task_datax_info` DISABLE KEYS */;
INSERT INTO `etl_task_datax_info` VALUES (916652415447470080,'mysql2mysql_datax','74','DATAX','{\r\n    \"job\": {\r\n        \"content\": [\r\n            {\r\n                \"reader\": {\r\n                    \"name\": \"mysqlreader\",\r\n                    \"parameter\": {\r\n                        \"column\": [\r\n                            \"id\",\r\n                            \"user_name\",\r\n                            \"email\"\r\n                         ],\r\n                        \"connection\": [\r\n                            {\r\n                                \"jdbcUrl\": [\"jdbc:mysql://127.0.0.1:3306/mydb\"],\r\n                                \"table\": [\"account_info\"]\r\n                            }\r\n                        ],\r\n                        \"password\": \"zyc\",\r\n                        \"username\": \"123456\"\r\n                    }\r\n                },\r\n                \"writer\": {\r\n                    \"name\": \"mysqlwriter\",\r\n                    \"parameter\": {\r\n                        \"writeMode\": \"insert\",\r\n                        \"username\": \"zyc\",\r\n                        \"password\": \"123456\",\r\n                        \"column\": [\r\n                            \"id\",\r\n							\"user_name\",\r\n							\"email\"\r\n                        ],\r\n                        \"session\": [\r\n                        	\"set session sql_mode=\'ANSI\'\"\r\n                        ],\r\n                        \"preSql\": [\r\n                         \r\n                        ],\r\n                        \"connection\": [\r\n                            {\r\n                                \"jdbcUrl\": \"jdbc:mysql://127.0.0.1:3306/mydb\",\r\n                                \"table\": [\r\n                                    \"dim_etf_info\"\r\n                                ]\r\n                            }\r\n                        ]\r\n                    }\r\n                }\r\n            }\r\n        ],\r\n        \"setting\": {\r\n            \"speed\": {\r\n                \"channel\": \"1\"\r\n            }\r\n        }\r\n    }\r\n}','1','2021-12-04 03:29:26','','','','','2021-12-25 02:50:48','0'),(916709157153804288,'datax_csv','74','DATAX','{\r\n    \"setting\": {},\r\n    \"job\": {\r\n        \"setting\": {\r\n            \"speed\": {\r\n                \"channel\": 2\r\n            }\r\n        },\r\n        \"content\": [\r\n            {\r\n                \"reader\": {\r\n                    \"name\": \"txtfilereader\",\r\n                    \"parameter\": {\r\n                        \"path\": [\"F:\\\\data\\\\csv\\\\h1.txt\"],\r\n                        \"encoding\": \"GBK\",\r\n                        \"column\": [\r\n                            {\r\n                                \"index\": 0,\r\n                                \"type\": \"string\"\r\n                            },\r\n                            {\r\n                                \"index\": 1,\r\n                                \"type\": \"string\"\r\n                            },\r\n                            {\r\n                                \"index\": 2,\r\n                                \"type\": \"string\"\r\n                            },\r\n                            {\r\n                                \"index\": 3,\r\n                                \"type\": \"string\"\r\n                            },\r\n                            {\r\n                                \"index\": 4,\r\n                                \"type\": \"string\"\r\n                            },                          \r\n                        ],\r\n                        \"fieldDelimiter\": \"|\",\r\n						\"skipHeader\": \"True\"\r\n                    }\r\n                },\r\n                \"writer\": {\r\n                    \"name\": \"txtfilewriter\",\r\n                    \"parameter\": {\r\n                        \"path\": \"F:\\\\test\",\r\n                        \"fileName\": \"test\",\r\n                        \"writeMode\": \"truncate\",\r\n                        \"format\": \"yyyy-MM-dd\",\r\n						\"header\":[\'name\', \'sex\', \'job\',\'addr\',\'age\']\r\n                    }\r\n                }\r\n            }\r\n        ]\r\n    }\r\n}','1','2021-12-04 07:14:55','','','','','2021-12-25 02:50:56','0');
/*!40000 ALTER TABLE `etl_task_datax_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_info`
--

DROP TABLE IF EXISTS `role_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(200) DEFAULT NULL COMMENT '角色code',
  `name` varchar(500) DEFAULT NULL COMMENT '角色名',
  `enable` varchar(8) DEFAULT 'false' COMMENT '是否启用true/false',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=904072220689567745 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_info`
--

LOCK TABLES `role_info` WRITE;
/*!40000 ALTER TABLE `role_info` DISABLE KEYS */;
INSERT INTO `role_info` VALUES (894201076759138304,'admin','管理员','true',NULL,NULL),(894254232000008192,'super_admin','超级管理员','true',NULL,NULL),(898997645559730176,'role_a','role_a','true',NULL,NULL),(904072220689567744,'abc','aaaa','false',NULL,NULL);
/*!40000 ALTER TABLE `role_info` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_sources_type_info`
--

LOCK TABLES `data_sources_type_info` WRITE;
/*!40000 ALTER TABLE `data_sources_type_info` DISABLE KEYS */;
INSERT INTO `data_sources_type_info` VALUES (1,'JDBC'),(2,'HDFS'),(3,'HBASE'),(4,'MONGODB'),(5,'ES'),(6,'HIVE'),(7,'KAFKA'),(8,'HTTP'),(9,'REDIS'),(10,'CASSANDRA'),(11,'SFTP'),(12,'KUDU'),(13,'外部上传'),(14,'FLUME'),(15,'外部下载'),(16,'TIDB'),(17,'ICEBERG'),(18,'DATAX');
/*!40000 ALTER TABLE `data_sources_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quartz_job_info`
--

DROP TABLE IF EXISTS `quartz_job_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
  `count` int DEFAULT NULL COMMENT '当前任务执行次数,只做说明,具体判定使用实例表判断',
  `command` text COMMENT 'shell命令,jdbc命令,当前字段以废弃',
  `params` text COMMENT '自定义参数',
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
  `jsmind_data` mediumtext COMMENT '任务血源关系',
  `alarm_email` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `alarm_sms` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `alarm_zdh` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_error` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_finish` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_timeout` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `misfire` varchar(8) NOT NULL DEFAULT '0' COMMENT '恢复策略，0:无操作,1:所有历史重新执行,2:最近一次历史重新执行',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quartz_job_info`
--

LOCK TABLES `quartz_job_info` WRITE;
/*!40000 ALTER TABLE `quartz_job_info` DISABLE KEYS */;
INSERT INTO `quartz_job_info` VALUES ('719630230595047424',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'finish',NULL,NULL,NULL,'719630143433216000',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off','0'),('719631150493995008',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'finish',NULL,NULL,NULL,'719630908637843456',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off','0'),('720684617849376768','读取hive并下载文件',NULL,'ETL','2020-12-13 16:00:00','2020-12-15 16:00:00','1d','1','2',0,NULL,'',NULL,'2020-12-15 16:00:00',NULL,'1m','finish',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"d6f_96e_8be0_bb\",\"etl_task_id\":\"720684174964428800\",\"etl_context\":\"读取hive转外部下载\",\"more_task\":\"SQL\",\"divId\":\"d6f_96e_8be0_bb\",\"name\":\"读取hive转外部下载\",\"positionX\":382,\"positionY\":297,\"type\":\"tasks\"},{\"id\":\"fb9_0cc_b891_be\",\"etl_context\":\"hostname\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"fb9_0cc_b891_be\",\"name\":\"hostname\",\"positionX\":141,\"positionY\":177,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_12\",\"pageSourceId\":\"fb9_0cc_b891_be\",\"pageTargetId\":\"d6f_96e_8be0_bb\"}]}','off','off','off','off','off','off','0'),('724291412505399296',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'finish',NULL,NULL,NULL,'724291182208749568',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off','0'),('724312221676474368',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'finish',NULL,NULL,NULL,'724312011898359808',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off','0'),('728252156099956736','kafka接收客户信息存mydb',NULL,'ETL','2020-07-01 16:00:00','2020-07-30 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-07-08 00:00:00\"}','etl','2020-07-07 16:00:00','2020-07-08 16:00:00','100s','remove',NULL,NULL,NULL,'728251791409418240','kafka接收客户信息存mydb','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off','0'),('728647515297026048','单分割符无标题2',NULL,'ETL','2021-09-18 14:29:29','2021-09-18 14:29:29','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"96e_16f_b1f6_62\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"96e_16f_b1f6_62\",\"name\":\"单分割符无标题\",\"positionX\":229,\"positionY\":100,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('728996892721025024','单分割符无标题3',NULL,'ETL','2020-10-28 16:00:00','2020-12-22 16:00:00','','1','5',0,'','','finish','2020-10-31 16:00:00','2020-11-01 16:00:00','100s','create',NULL,NULL,NULL,'728996795115376640','单分割符无标题3','1',NULL,'728647515297026048',NULL,NULL,'',NULL,NULL,'','772119486072360960','86400',NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off','0'),('731444485837295616','clickhouse_datasets.z1转zdh_test.z1',NULL,'ETL','2020-07-10 16:00:00','2020-09-10 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-07-11 00:00:00\"}','finish','2020-07-10 16:00:00',NULL,'100s','create',NULL,NULL,NULL,'731444359840403456','clickhouse_datasets.z1转zdh_test.z1','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off','0'),('732547003703103488','第一个jar',NULL,'ETL','2020-07-13 16:00:00','2020-08-07 16:00:00','','1','-1',1,'','{\"ETL_DATE\":\"2020-07-15 00:00:00\"}','finish','2020-07-14 16:00:00','2020-07-15 16:00:00','100s','create',NULL,NULL,NULL,'732538726244159488','第一个jar','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off','0'),('736528842050506752','第一个drools处理',NULL,'ETL','2020-12-31 16:00:00','2021-01-30 16:00:00','1d','1','1',0,NULL,'',NULL,'2021-01-05 16:00:00',NULL,'2h','remove',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5','on',NULL,'zyc',NULL,'20','5',NULL,'off','','{\"tasks\":[{\"id\":\"2\",\"etl_task_id\":\"2\",\"etl_context\":\"第一个drools\",\"more_task\":\"Drools\",\"divId\":\"9ff_457_aa3d_03\",\"name\":\"第一个drools\",\"positionX\":246,\"positionY\":149,\"type\":\"tasks\"},{\"id\":\"794986268751564800\",\"etl_task_id\":\"794986268751564800\",\"etl_context\":\"暂停5分钟\",\"more_task\":\"SSH\",\"divId\":\"e3c_33f_872d_84\",\"name\":\"暂停5分钟\",\"positionX\":464,\"positionY\":42,\"type\":\"tasks\"}],\"shell\":[],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"e3c_33f_872d_84\",\"pageTargetId\":\"9ff_457_aa3d_03\"}]}','off','off','off','off','off','off','0'),('746141094323949568','sftp测试',NULL,'ETL','2020-12-31 16:00:00','2021-01-30 16:00:00','1d','1','1',0,NULL,'',NULL,NULL,NULL,'100h','',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'on','1s','{\"tasks\":[{\"id\":\"b2a_c26_8c0d_07\",\"etl_task_id\":\"746140955731562496\",\"etl_context\":\"sftp测试\",\"more_task\":\"单源ETL\",\"divId\":\"b2a_c26_8c0d_07\",\"name\":\"sftp测试\",\"positionX\":250,\"positionY\":111,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('749203382681473024','123',NULL,'ETL','2020-08-28 16:00:00','2020-09-04 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-08-30 00:00:00\"}','finish','2020-08-29 16:00:00','2020-08-30 16:00:00','100s','finish',NULL,NULL,NULL,'749064500069535744','123','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off','0'),('749255207426199552','第一个drools',NULL,'ETL','2020-08-21 16:00:00','2020-08-28 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-08-23 00:00:00\"}','finish','2020-08-22 16:00:00','2020-08-23 16:00:00','100s','create',NULL,NULL,NULL,'2','第一个drools','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off','0'),('749258659757821952','hive->mysql->drools',NULL,'ETL','2020-08-27 16:00:00','2020-09-04 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-08-30 00:00:00\"}','finish','2020-08-29 16:00:00','2020-08-30 16:00:00','100s','create',NULL,NULL,NULL,'749258437296132096','hive->mysql->drools','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off','0'),('749280046954319872','more_test_account_info',NULL,'ETL','2020-08-27 16:00:00','2020-09-04 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-08-31 00:00:00\"}','finish','2020-08-30 16:00:00','2020-08-31 16:00:00','100s','create',NULL,NULL,NULL,'749279630074056704','more_test_account_info','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off','0'),('758090172859420672','hudi输出多分割符无标题',NULL,'ETL','2020-09-21 16:00:00','2020-10-01 16:00:00','','1','2',0,'','','error',NULL,NULL,'100s','finish',NULL,NULL,NULL,'724312011898359808','hudi输出多分割符无标题','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'','758101270681620480',NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off','0'),('785083582216409088','第一个多任务调度',NULL,'ETL','2020-12-04 16:00:00','2021-01-08 16:00:00','1d','1','1',0,'','','error','2020-12-04 16:00:00','2020-12-05 16:00:00','100s','finish',NULL,NULL,NULL,'719619870378954752','单分割符无标题','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'','785105270991753216','86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719619870378954752\",\"topic\":\"单分割符无标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}','off','off','off','off','off','off','0'),('785139391885479936','第二个多任务调度',NULL,'ETL','2020-12-04 16:00:00','2021-01-08 16:00:00','1d','1','1',0,'','','finish','2020-12-04 16:00:00','2020-12-05 16:00:00','100s','finish',NULL,NULL,NULL,'756934940771225600','读取hive转mysql','1',NULL,NULL,NULL,NULL,'5','on',NULL,'','785140586985295873','86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719619870378954752\",\"topic\":\"单分割符无标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"719630143433216000\",\"topic\":\"多分割符无标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"756934940771225600\",\"topic\":\"读取hive转mysql\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"SQL\"}]}','off','off','off','off','off','off','0'),('785144554482307072','第3个调度',NULL,'ETL','2020-12-05 16:00:00','2021-01-08 16:00:00','1d','1','3',0,'','','',NULL,NULL,'1000s','create',NULL,NULL,NULL,'728647407415332864','单分割符无标题2','1',NULL,NULL,NULL,NULL,'5','on',NULL,'',NULL,'86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719619870378954752\",\"topic\":\"单分割符无标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"728647407415332864\",\"topic\":\"单分割符无标题2\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}','off','off','off','off','off','off','0'),('785144896708153344','第5个调度',NULL,'ETL','2020-12-05 16:00:00','2020-12-25 16:00:00','1d','1','1',0,'','','',NULL,NULL,'1000s','create',NULL,NULL,NULL,'728996795115376640','单分割符无标题3','1',NULL,NULL,NULL,NULL,'5','on',NULL,'',NULL,'86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719629297702146048\",\"topic\":\"单分割符自带标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"728996795115376640\",\"topic\":\"单分割符无标题3\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}','off','off','off','off','off','off','0'),('785145024143691776','第5个调度',NULL,'ETL','2020-12-05 16:00:00','2020-12-25 16:00:00','1d','1','1',0,'','','',NULL,NULL,'1000s','create',NULL,NULL,NULL,'728996795115376640','单分割符无标题3','1',NULL,NULL,NULL,NULL,'5','on',NULL,'',NULL,'86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719629297702146048\",\"topic\":\"单分割符自带标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"728996795115376640\",\"topic\":\"单分割符无标题3\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}','off','off','off','off','off','off','0'),('785145538403110912','test1',NULL,'ETL','2020-12-05 16:00:00','2021-01-01 16:00:00','1d','1','3',0,'','','error','2020-12-06 16:00:00','2020-12-07 16:00:00','1000s','finish',NULL,NULL,NULL,'728996795115376640','单分割符无标题3','1',NULL,NULL,NULL,NULL,'5','on',NULL,'','785224041974730752','86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind\",\"author\":\"hizzgdev@163.com\",\"version\":\"0.4.6\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"728647407415332864\",\"topic\":\"单分割符无标题2\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"728996795115376640\",\"topic\":\"单分割符无标题3\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}','off','off','off','off','off','off','0'),('787419291484950528','第一个测试',NULL,'ETL','2021-01-31 11:48:27','2021-01-31 11:48:27','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"5f5_754_bfb5_e4\",\"etl_context\":\"第一个jdbc任务\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"url\":\"jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"username\":\"zyc\",\"password\":\"123456\",\"jdbc_sql\":\"select * from account_info where \'2021-01-15\'={{zdh_date}} limit 1\",\"divId\":\"5f5_754_bfb5_e4\",\"name\":\"第一个jdbc任务\",\"positionX\":252,\"positionY\":282,\"type\":\"jdbc\"},{\"id\":\"be4_e13_bf4f_ac\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"divId\":\"be4_e13_bf4f_ac\",\"name\":\"单分割符无标题\",\"positionX\":94,\"positionY\":37,\"type\":\"tasks\"},{\"id\":\"8ca_667_8bb5_18\",\"etl_context\":\"hostname\",\"depend_level\":\"2\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"hostname\",\"divId\":\"8ca_667_8bb5_18\",\"name\":\"hostname\",\"positionX\":511,\"positionY\":84,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_13\",\"pageSourceId\":\"be4_e13_bf4f_ac\",\"pageTargetId\":\"8ca_667_8bb5_18\"},{\"connectionId\":\"con_15\",\"pageSourceId\":\"8ca_667_8bb5_18\",\"pageTargetId\":\"5f5_754_bfb5_e4\"}]}','off','off','off','off','off','off','0'),('789654690387202048','shell测试',NULL,'ETL','2021-02-08 02:14:07','2021-02-08 02:14:07','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"d31_88b_a993_0f\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"zdh_instance\":\"zdh_server\",\"is_disenable\":\"false\",\"divId\":\"d31_88b_a993_0f\",\"name\":\"单分割符无标题\",\"positionX\":283,\"positionY\":246,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('797802941498003456','调度tidb写入',NULL,'ETL','2021-01-09 16:00:00','2021-01-22 16:00:00','1d','1','1',0,NULL,'',NULL,NULL,NULL,'100s','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"797802796177952768\",\"etl_task_id\":\"797802796177952768\",\"etl_context\":\"测试tidb写入\",\"more_task\":\"单源ETL\",\"divId\":\"650_e6a_ba23_51\",\"name\":\"测试tidb写入\",\"positionX\":198,\"positionY\":68,\"type\":\"tasks\"}],\"shell\":[],\"line\":[]}','off','off','off','off','off','off','0'),('797819166902980608','tidb读取测试',NULL,'ETL','2021-01-09 16:00:00','2021-01-15 16:00:00','1d','1','1',0,NULL,'',NULL,NULL,NULL,'100s','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"797819041442959360\",\"etl_task_id\":\"797819041442959360\",\"etl_context\":\"第一个tidb读取\",\"more_task\":\"单源ETL\",\"divId\":\"e62_601_b1da_d0\",\"name\":\"第一个tidb读取\",\"positionX\":239,\"positionY\":126,\"type\":\"tasks\"}],\"shell\":[],\"line\":[]}','off','off','off','off','off','off','0'),('810198240522670080','测试shell同步异步',NULL,'ETL','2021-02-13 09:12:58','2021-03-31 09:12:58','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"6b2_454_9db3_74\",\"etl_context\":\"hello_world\",\"is_script\":\"true\",\"is_disenable\":\"false\",\"command\":\"for %%I in (A,B,C) do (timeout /t 50 & echo %%I)\",\"divId\":\"6b2_454_9db3_74\",\"name\":\"hello_world\",\"positionX\":202,\"positionY\":121,\"type\":\"shell\"},{\"id\":\"95e_e44_ac5e_bc\",\"etl_context\":\"测试A\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"url\":\"jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"username\":\"zyc\",\"password\":\"123456\",\"jdbc_sql\":\" insert into zdh_test.d1 values(\'北京\',\'22\',\'false\',\'it\',\'zyc\',\'man\');\",\"divId\":\"95e_e44_ac5e_bc\",\"name\":\"测试A\",\"positionX\":189,\"positionY\":250,\"type\":\"jdbc\"},{\"id\":\"230_c8e_b219_bd\",\"etl_context\":\"hostname\",\"is_script\":\"false\",\"is_disenable\":\"true\",\"command\":\"hostname\",\"divId\":\"230_c8e_b219_bd\",\"name\":\"hostname\",\"positionX\":360,\"positionY\":200,\"type\":\"shell\"},{\"id\":\"4b1_4fa_9c76_91\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"divId\":\"4b1_4fa_9c76_91\",\"name\":\"单分割符无标题\",\"positionX\":325,\"positionY\":358,\"type\":\"tasks\"}],\"line\":[{\"connectionId\":\"con_17\",\"pageSourceId\":\"6b2_454_9db3_74\",\"pageTargetId\":\"230_c8e_b219_bd\"},{\"connectionId\":\"con_19\",\"pageSourceId\":\"230_c8e_b219_bd\",\"pageTargetId\":\"95e_e44_ac5e_bc\"},{\"connectionId\":\"con_21\",\"pageSourceId\":\"95e_e44_ac5e_bc\",\"pageTargetId\":\"4b1_4fa_9c76_91\"}]}','off','off','off','off','off','off','0'),('810209693258485760','测试SSH任务实时日志',NULL,'ETL','2021-02-13 10:03:35','2021-02-13 10:03:35','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"d00_338_b9c2_9a\",\"etl_task_id\":\"749064500069535744\",\"etl_context\":\"123\",\"more_task\":\"SSH\",\"divId\":\"d00_338_b9c2_9a\",\"name\":\"123\",\"positionX\":245,\"positionY\":181,\"type\":\"tasks\"},{\"id\":\"b0f_25b_8e49_ce\",\"etl_context\":\"java-version\",\"is_script\":\"true\",\"is_disenable\":\"false\",\"command\":\"java -version\",\"divId\":\"b0f_25b_8e49_ce\",\"name\":\"java-version\",\"positionX\":256,\"positionY\":21,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"b0f_25b_8e49_ce\",\"pageTargetId\":\"d00_338_b9c2_9a\"}]}','off','off','off','off','off','off','0'),('818061528178626560','调度mysql转hdfs',NULL,'ETL','2021-03-07 01:55:04','2021-03-07 01:55:04','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"9f4_667_9e1e_de\",\"etl_task_id\":\"818059052008345600\",\"etl_context\":\"mysql转hfds测试\",\"more_task\":\"单源ETL\",\"divId\":\"9f4_667_9e1e_de\",\"name\":\"mysql转hfds测试\",\"positionX\":159,\"positionY\":99,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('824405424496185344','测试回退时间差',NULL,'ETL','2021-03-24 13:57:24','2021-03-24 13:57:24','1d','1','0',0,NULL,'',NULL,NULL,NULL,'0/5 * * * * ? *','remove',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'on','86400','{\"tasks\":[{\"id\":\"b25_ff7_be83_d8\",\"etl_context\":\"hostname\",\"depend_level\":\"0\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"hostname && echo \'{{zdh_date}}\'\",\"divId\":\"b25_ff7_be83_d8\",\"name\":\"hostname\",\"positionX\":189,\"positionY\":59,\"type\":\"shell\"}],\"line\":[]}','off','off','off','off','off','off','0'),('835448468066537472','测试多源任务',NULL,'ETL','2021-04-24 01:33:21','2021-04-30 01:33:21','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"280_398_9c8e_88\",\"etl_task_id\":\"2\",\"etl_context\":\"more_mydb_account_info\",\"more_task\":\"多源ETL\",\"divId\":\"280_398_9c8e_88\",\"name\":\"more_mydb_account_info\",\"positionX\":175,\"positionY\":104,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('835460442355666944','测试sql任务',NULL,'ETL','2021-04-24 02:20:58','2021-04-24 02:20:58','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"cc9_56e_89e1_39\",\"etl_task_id\":\"756934940771225600\",\"etl_context\":\"读取hive转mysql\",\"more_task\":\"SQL\",\"divId\":\"cc9_56e_89e1_39\",\"name\":\"读取hive转mysql\",\"positionX\":116,\"positionY\":96,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('837799765528809472','jdbc优化过滤测试',NULL,'ETL','2021-04-30 13:16:31','2021-05-04 13:16:31','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"c2c_4cd_9b81_5e\",\"etl_task_id\":\"837799577204559872\",\"etl_context\":\"jdbc优化过滤\",\"more_task\":\"单源ETL\",\"divId\":\"c2c_4cd_9b81_5e\",\"name\":\"jdbc优化过滤\",\"positionX\":214,\"positionY\":78,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('837982149905747968','shell异步测试',NULL,'ETL','2021-05-01 01:21:04','2021-05-10 01:21:04','1d','1','0',0,NULL,'',NULL,'2021-05-10 01:21:04',NULL,'1d','finish',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5','on',NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"fe3_577_b886_2f\",\"etl_context\":\"shell异步测试\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"exit -2\",\"divId\":\"fe3_577_b886_2f\",\"name\":\"shell异步测试\",\"positionX\":330,\"positionY\":77,\"type\":\"shell\"},{\"id\":\"7b7_39f_9bbe_44\",\"etl_context\":\"SHELL成功测试\",\"depend_level\":\"0\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"exit 0\",\"divId\":\"7b7_39f_9bbe_44\",\"name\":\"SHELL成功测试\",\"positionX\":533,\"positionY\":74,\"type\":\"shell\"},{\"id\":\"6bd_8ff_aeb1_04\",\"etl_context\":\"ping\",\"depend_level\":\"0\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"ping -t 127.0.0.1\",\"divId\":\"6bd_8ff_aeb1_04\",\"name\":\"ping\",\"positionX\":132,\"positionY\":76,\"type\":\"shell\"}],\"line\":[]}','off','off','off','off','off','off','0'),('838011667739578368','jdbc检查测试调度',NULL,'ETL','2021-05-01 03:16:23','2021-05-01 03:16:23','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"623_1f1_9679_6a\",\"etl_context\":\"jdbc查询\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"url\":\"jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"username\":\"zyc\",\"password\":\"123456\",\"jdbc_sql\":\"select * from account_info\",\"divId\":\"623_1f1_9679_6a\",\"name\":\"jdbc查询\",\"positionX\":369,\"positionY\":162,\"type\":\"jdbc\"},{\"id\":\"07e_84a_9393_11\",\"etl_context\":\"hdfs查询\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"url\":\"\",\"username\":\"zyc\",\"password\":\"\",\"url_type\":\"0\",\"hdfs_path\":\"F://data/csv/h1.txt\",\"hdfs_mode\":\"0\",\"divId\":\"07e_84a_9393_11\",\"name\":\"hdfs查询\",\"positionX\":390,\"positionY\":302,\"type\":\"hdfs\"}],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"623_1f1_9679_6a\",\"pageTargetId\":\"07e_84a_9393_11\"}]}','off','off','off','off','off','off','0'),('839467560947683328','第一个申请源调度',NULL,'ETL','2021-05-05 03:43:58','2021-05-05 03:43:58','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"67c_0d6_855f_db\",\"etl_task_id\":\"839443568224374784\",\"etl_context\":\"测试日志表\",\"more_task\":\"APPLY\",\"depend_level\":\"0\",\"divId\":\"67c_0d6_855f_db\",\"name\":\"测试日志表\",\"positionX\":298,\"positionY\":106,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('843090443133521920','第一个iceberg写入测试',NULL,'ETL','2021-05-15 03:39:56','2021-05-15 03:39:56','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"315_8ec_9318_e5\",\"etl_task_id\":\"843090296215441408\",\"etl_context\":\"mydb->iceberg\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"divId\":\"315_8ec_9318_e5\",\"name\":\"mydb->iceberg\",\"positionX\":192,\"positionY\":82,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('851042826887106560','测试空任务组',NULL,'ETL','2021-06-06 02:20:05','2021-06-06 02:20:05','1d','1','0',0,NULL,'',NULL,'2021-06-06 02:20:05',NULL,'1d','finish',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5','2021-06-08 14:52:51','off','','{\"tasks\":[],\"line\":[]}','off','off','off','off','off','off','0'),('853053085579218944','测试循环调度',NULL,'ETL','2021-06-11 15:26:21','2021-06-11 15:26:21','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"dd2_470_9884_2b\",\"etl_context\":\"h1\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"dd2_470_9884_2b\",\"name\":\"h1\",\"positionX\":214,\"positionY\":38,\"type\":\"shell\"},{\"id\":\"fcd_d27_82a8_5c\",\"etl_context\":\"h2\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"fcd_d27_82a8_5c\",\"name\":\"h2\",\"positionX\":152,\"positionY\":185,\"type\":\"shell\"},{\"id\":\"26a_603_aec0_42\",\"etl_context\":\"h3\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"26a_603_aec0_42\",\"name\":\"h3\",\"positionX\":287,\"positionY\":180,\"type\":\"shell\"},{\"id\":\"65b_e7a_beea_cd\",\"etl_context\":\"h4\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"65b_e7a_beea_cd\",\"name\":\"h4\",\"positionX\":149,\"positionY\":304,\"type\":\"shell\"},{\"id\":\"fd6_7e2_8376_79\",\"etl_context\":\"h5\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"fd6_7e2_8376_79\",\"name\":\"h5\",\"positionX\":220,\"positionY\":404,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_13\",\"pageSourceId\":\"dd2_470_9884_2b\",\"pageTargetId\":\"fcd_d27_82a8_5c\"},{\"connectionId\":\"con_22\",\"pageSourceId\":\"dd2_470_9884_2b\",\"pageTargetId\":\"26a_603_aec0_42\"},{\"connectionId\":\"con_31\",\"pageSourceId\":\"fcd_d27_82a8_5c\",\"pageTargetId\":\"65b_e7a_beea_cd\"},{\"connectionId\":\"con_40\",\"pageSourceId\":\"26a_603_aec0_42\",\"pageTargetId\":\"fd6_7e2_8376_79\"},{\"connectionId\":\"con_45\",\"pageSourceId\":\"65b_e7a_beea_cd\",\"pageTargetId\":\"fd6_7e2_8376_79\"}]}','off','off','off','off','off','off','0'),('853195794658889728','测试顺序依赖',NULL,'ETL','2021-06-12 00:54:22','2021-06-12 00:54:22','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"3b5_996_ac89_af\",\"etl_context\":\"hostname\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"3b5_996_ac89_af\",\"name\":\"hostname\",\"positionX\":289,\"positionY\":225,\"type\":\"shell\"},{\"id\":\"784_4e0_a932_83\",\"etl_task_id\":\"732547003703103488\",\"etl_context\":\"第一个jar\",\"depend_level\":\"0\",\"divId\":\"784_4e0_a932_83\",\"name\":\"第一个jar\",\"positionX\":287,\"positionY\":70,\"type\":\"group\"}],\"line\":[{\"connectionId\":\"con_16\",\"pageSourceId\":\"784_4e0_a932_83\",\"pageTargetId\":\"3b5_996_ac89_af\"}]}','off','off','off','off','off','off','0'),('853275752651558912','mysql2oracle测试',NULL,'ETL','2021-06-12 06:12:45','2021-06-12 06:12:45','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"1ec_d28_9984_32\",\"etl_task_id\":\"853275616093409280\",\"etl_context\":\"mysql2oracle\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"divId\":\"1ec_d28_9984_32\",\"name\":\"mysql2oracle\",\"positionX\":245,\"positionY\":89,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('853940428813111296','测试新增参数',NULL,'ETL','2021-06-14 02:14:00','2021-06-14 02:14:00','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"dea_a57_9621_93\",\"etl_task_id\":\"853938731688660992\",\"etl_context\":\"测试新增参数\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"divId\":\"dea_a57_9621_93\",\"name\":\"测试新增参数\",\"positionX\":380,\"positionY\":62,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('853947647352901632','测试多源ETL参数',NULL,'ETL','2021-06-14 02:42:34','2021-06-14 02:42:34','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"02f_d76_bcc4_cf\",\"etl_task_id\":\"2\",\"etl_context\":\"more_mydb_account_info\",\"more_task\":\"多源ETL\",\"depend_level\":\"0\",\"divId\":\"02f_d76_bcc4_cf\",\"name\":\"more_mydb_account_info\",\"positionX\":173,\"positionY\":83,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('853958344086392832','测试日志表2',NULL,'ETL','2021-06-14 03:25:10','2021-06-14 03:25:10','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"2af_425_b322_23\",\"etl_task_id\":\"853958150766727168\",\"etl_context\":\"测试日志表2\",\"more_task\":\"APPLY\",\"depend_level\":\"0\",\"divId\":\"2af_425_b322_23\",\"name\":\"测试日志表2\",\"positionX\":246,\"positionY\":80,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('855228103851511808','测试hdfs写入单文件',NULL,'ETL','2021-06-17 15:30:45','2021-06-17 15:30:45','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"8ef_c51_b118_fe\",\"etl_task_id\":\"855227983957331968\",\"etl_context\":\"测试hdfs写入单文件\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"divId\":\"8ef_c51_b118_fe\",\"name\":\"测试hdfs写入单文件\",\"positionX\":245,\"positionY\":78,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('856664204910792704','多源任务优化测试',NULL,'ETL','2021-06-21 14:37:24','2021-06-21 14:37:24','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"dc7_f00_ac0d_97\",\"etl_task_id\":\"3\",\"etl_context\":\"优化多源任务测试\",\"more_task\":\"多源ETL\",\"depend_level\":\"0\",\"divId\":\"dc7_f00_ac0d_97\",\"name\":\"优化多源任务测试\",\"positionX\":162,\"positionY\":71,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('857389943222177792','失败依赖测试',NULL,'ETL','2021-06-23 14:40:05','2021-06-23 14:40:05','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"d46_62c_9ebc_93\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"divId\":\"d46_62c_9ebc_93\",\"name\":\"单分割符无标题\",\"positionX\":290,\"positionY\":74,\"type\":\"tasks\"},{\"id\":\"8a0_80a_a3ff_9c\",\"etl_task_id\":\"3\",\"etl_context\":\"优化多源任务测试\",\"more_task\":\"多源ETL\",\"depend_level\":\"2\",\"divId\":\"8a0_80a_a3ff_9c\",\"name\":\"优化多源任务测试\",\"positionX\":304,\"positionY\":206,\"type\":\"tasks\"},{\"id\":\"777_16e_9ae3_c1\",\"etl_context\":\"hostname\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"777_16e_9ae3_c1\",\"name\":\"hostname\",\"positionX\":593,\"positionY\":175,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_12\",\"pageSourceId\":\"d46_62c_9ebc_93\",\"pageTargetId\":\"8a0_80a_a3ff_9c\"},{\"connectionId\":\"con_21\",\"pageSourceId\":\"d46_62c_9ebc_93\",\"pageTargetId\":\"777_16e_9ae3_c1\"}]}','off','off','off','off','off','off','0'),('858395317454770176','测试flink',NULL,'ETL','2021-06-26 09:12:08','2021-06-26 09:12:08','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"1d5_4fd_bb4a_6e\",\"etl_task_id\":\"858391270026907648\",\"etl_context\":\"第一个FLINK_SQL\",\"more_task\":\"FLINK\",\"depend_level\":\"0\",\"divId\":\"1d5_4fd_bb4a_6e\",\"name\":\"第一个FLINK_SQL\",\"positionX\":318,\"positionY\":113,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('874623112065323008','测试禁用rm',NULL,'ETL','2021-08-10 03:59:09','2021-08-10 03:59:09','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5','on',NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"ee9_cdf_a2f7_6e\",\"etl_context\":\"测试删除\",\"depend_level\":\"0\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"rm -rf /home/zyc/aaaaaaaaaa\",\"time_out\":\"86400\",\"divId\":\"ee9_cdf_a2f7_6e\",\"name\":\"测试删除\",\"positionX\":280,\"positionY\":265,\"type\":\"shell\"},{\"id\":\"cda_df4_b62a_38\",\"etl_context\":\"ping 127.0.0.1\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"ping 127.0.0.1 -n 10000\",\"time_out\":\"10\",\"divId\":\"cda_df4_b62a_38\",\"name\":\"ping 127.0.0.1\",\"positionX\":249,\"positionY\":70,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"cda_df4_b62a_38\",\"pageTargetId\":\"ee9_cdf_a2f7_6e\"}]}','off','off','off','off','off','off','0'),('886576411345686528','离线flink测试',NULL,'ETL','2021-09-12 03:37:21','2021-09-12 03:37:21','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"69b_003_b845_0a\",\"etl_task_id\":\"886575336517537792\",\"etl_context\":\"第二个FLINK_SQL\",\"more_task\":\"FLINK\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"69b_003_b845_0a\",\"name\":\"第二个FLINK_SQL\",\"positionX\":274,\"positionY\":82,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('887459213607964672','测试ssh',NULL,'ETL','2021-09-14 14:05:34','2021-09-14 14:05:34','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"aa5_671_8827_a1\",\"etl_task_id\":\"749064500069535744\",\"etl_context\":\"123\",\"more_task\":\"SSH\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"time_out\":\"86400\",\"divId\":\"aa5_671_8827_a1\",\"name\":\"123\",\"positionX\":354,\"positionY\":86,\"type\":\"tasks\"}],\"line\":[]}','on','off','on','on','on','on','0'),('889178494473342976','本地shell',NULL,'ETL','2021-09-19 07:57:22','2021-09-19 07:57:22','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"7c0_dc9_ba05_e1\",\"etl_context\":\"hostname\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"time_out\":\"86400\",\"divId\":\"7c0_dc9_ba05_e1\",\"name\":\"hostname\",\"positionX\":330,\"positionY\":80,\"type\":\"shell\"}],\"line\":[]}','on','off','on','on','on','on','0'),('889233499129647104','tttt',NULL,'ETL','2021-09-19 11:35:54','2021-09-19 11:35:54','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"4d3_df1_93ac_b9\",\"etl_task_id\":\"889232742397513728\",\"etl_context\":\"tttt\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"4d3_df1_93ac_b9\",\"name\":\"tttt\",\"positionX\":360,\"positionY\":112,\"type\":\"tasks\"}],\"line\":[]}','on','off','on','on','on','on','0'),('890303240988528640','测试aaa',NULL,'ETL','2021-09-22 10:25:53','2021-09-22 10:25:53','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"d78_85c_899f_dc\",\"etl_task_id\":\"719629297702146048\",\"etl_context\":\"单分割符自带标题\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"d78_85c_899f_dc\",\"name\":\"单分割符自带标题\",\"positionX\":294,\"positionY\":337,\"type\":\"tasks\"},{\"id\":\"39e_1e4_bd61_b0\",\"etl_context\":\"hostname\",\"depend_level\":\"0\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"hostname\",\"time_out\":\"86400\",\"divId\":\"39e_1e4_bd61_b0\",\"name\":\"hostname\",\"positionX\":273,\"positionY\":155,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"39e_1e4_bd61_b0\",\"pageTargetId\":\"d78_85c_899f_dc\"}]}','off','off','on','on','on','off','0'),('903440241472311296','测试ssh',NULL,'ETL','2021-10-28 16:18:45','2021-10-28 16:18:45','1d','1','0',0,NULL,'',NULL,'2021-10-28 16:18:45',NULL,'1d','remove',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"541_dc1_810c_e9\",\"etl_context\":\"ping 3\",\"depend_level\":\"0\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"ping -n 10 127.0.0.1\",\"time_out\":\"86400\",\"divId\":\"541_dc1_810c_e9\",\"name\":\"ping 3\",\"positionX\":448,\"positionY\":87,\"type\":\"shell\"}],\"line\":[]}','off','off','on','on','on','on','0'),('904327925656981504','FLINK_SQL_CDC测试',NULL,'ETL','2021-10-31 03:15:49','2021-10-31 03:15:49','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"64a_b73_9baa_54\",\"etl_task_id\":\"904327604331352064\",\"etl_context\":\"第三个FLINK_SQL_CDC\",\"more_task\":\"FLINK\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"64a_b73_9baa_54\",\"name\":\"第三个FLINK_SQL_CDC\",\"positionX\":81,\"positionY\":221,\"type\":\"tasks\"},{\"id\":\"7db_633_a824_43\",\"etl_task_id\":\"886575336517537792\",\"etl_context\":\"第二个FLINK_SQL\",\"more_task\":\"FLINK\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"7db_633_a824_43\",\"name\":\"第二个FLINK_SQL\",\"positionX\":78,\"positionY\":46,\"type\":\"tasks\"},{\"id\":\"a91_fe7_8ee4_91\",\"etl_context\":\"hostname\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"time_out\":\"86400\",\"divId\":\"a91_fe7_8ee4_91\",\"name\":\"hostname\",\"positionX\":128,\"positionY\":363,\"type\":\"shell\"},{\"id\":\"33f_843_bd51_42\",\"etl_context\":\"hostname2\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"time_out\":\"86400\",\"divId\":\"33f_843_bd51_42\",\"name\":\"hostname2\",\"positionX\":429,\"positionY\":416,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_13\",\"pageSourceId\":\"7db_633_a824_43\",\"pageTargetId\":\"64a_b73_9baa_54\"},{\"connectionId\":\"con_15\",\"pageSourceId\":\"64a_b73_9baa_54\",\"pageTargetId\":\"a91_fe7_8ee4_91\"},{\"connectionId\":\"con_24\",\"pageSourceId\":\"a91_fe7_8ee4_91\",\"pageTargetId\":\"33f_843_bd51_42\"}]}','off','off','on','on','on','on','0'),('904448275493425152','windows_flink_集群测试',NULL,'ETL','2021-10-31 11:13:46','2021-10-31 11:13:46','1d','1','1',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"07a_5c8_92de_1d\",\"etl_task_id\":\"904448033733742592\",\"etl_context\":\"第二个FLINK_SQL_Window版本\",\"more_task\":\"FLINK\",\"depend_level\":\"0\",\"is_disenable\":\"true\",\"time_out\":\"86400\",\"divId\":\"07a_5c8_92de_1d\",\"name\":\"第二个FLINK_SQL_Window版本\",\"positionX\":278,\"positionY\":68,\"type\":\"tasks\"},{\"id\":\"46e_182_ab5a_ba\",\"etl_task_id\":\"904464410402099200\",\"etl_context\":\"第三个FLINK_SQL_CDC_Windows版本\",\"more_task\":\"FLINK\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"time_out\":\"86400\",\"divId\":\"46e_182_ab5a_ba\",\"name\":\"第三个FLINK_SQL_CDC_Windows版本\",\"positionX\":341,\"positionY\":208,\"type\":\"tasks\"}],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"07a_5c8_92de_1d\",\"pageTargetId\":\"46e_182_ab5a_ba\"}]}','off','off','on','on','on','on','0'),('906543220496797697','',NULL,'EMAIL','2021-11-06 05:59:06','2021-11-06 05:59:06',NULL,'3','-1',0,'',NULL,NULL,NULL,NULL,'15s','running',NULL,NULL,NULL,'email',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'86400','5',NULL,'off',NULL,NULL,'off','off','off','off','off','off','0'),('906543220572295169','',NULL,'RETRY','2021-11-06 05:59:06','2021-11-06 05:59:06',NULL,'3','-1',0,'',NULL,NULL,NULL,NULL,'1s','running',NULL,NULL,NULL,'retry',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'86400','5',NULL,'off',NULL,NULL,'off','off','off','off','off','off','0'),('906543220626821121','',NULL,'CHECK','2021-11-06 05:59:06','2021-11-06 05:59:06',NULL,'3','-1',0,'',NULL,NULL,NULL,NULL,'5s','running',NULL,NULL,NULL,'check',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'86400','5',NULL,'off',NULL,NULL,'off','off','off','off','off','off','0'),('906547945187315712','测试专用',NULL,'ETL','2021-11-06 06:16:11','2024-11-06 06:20:11','1d','1','0',0,NULL,'',NULL,NULL,NULL,'0 0 0 * * ? *','running',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5','2022-01-10 16:00:01','on','','{\"tasks\":[{\"id\":\"420_a9f_9e80_ae\",\"etl_context\":\"测试shell\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"time_out\":\"86400\",\"divId\":\"420_a9f_9e80_ae\",\"name\":\"测试shell\",\"positionX\":258,\"positionY\":60,\"type\":\"shell\"},{\"id\":\"c04_40a_83c7_ee\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"c04_40a_83c7_ee\",\"name\":\"单分割符无标题\",\"positionX\":256,\"positionY\":159,\"type\":\"tasks\"}],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"420_a9f_9e80_ae\",\"pageTargetId\":\"c04_40a_83c7_ee\"}]}','off','off','on','on','on','on','0'),('916660297979138048','datax调度',NULL,'ETL','2021-12-04 03:59:48','2021-12-04 03:59:48','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"acc_e1d_add7_c1\",\"etl_task_id\":\"916652415447470080\",\"etl_context\":\"mysql2mysql_datax\",\"more_task\":\"DATAX\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"acc_e1d_add7_c1\",\"name\":\"mysql2mysql_datax\",\"positionX\":209,\"positionY\":68,\"type\":\"tasks\"}],\"line\":[]}','off','off','on','on','on','on','0'),('916709259801006080','datax_csv',NULL,'ETL','2021-12-04 07:15:01','2021-12-04 07:15:01','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc,admin,root',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"b2a_fb6_8cfc_4f\",\"etl_task_id\":\"916709157153804288\",\"etl_context\":\"datax_csv\",\"more_task\":\"DATAX\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"b2a_fb6_8cfc_4f\",\"name\":\"datax_csv\",\"positionX\":253,\"positionY\":56,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('916807521035882496','ddd',NULL,'ETL','2021-12-04 13:39:14','2021-12-04 13:39:14','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'admin,zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"cd2_0c6_9251_43\",\"etl_task_id\":\"914511685971087360\",\"etl_context\":\"第一个jdbc\",\"more_task\":\"JDBC\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"cd2_0c6_9251_43\",\"name\":\"第一个jdbc\",\"positionX\":238,\"positionY\":99,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off','0'),('919317519804665856','第一个数据质量检测',NULL,'ETL','2021-12-11 11:59:04','2021-12-25 11:59:04','1d','1','0',0,NULL,'',NULL,'2021-12-11 11:59:04',NULL,'0 0 0 * * ? *','remove',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"2bd_985_8def_29\",\"etl_task_id\":\"919326325259374592\",\"etl_context\":\"第一个数据质量检测\",\"more_task\":\"QUALITY\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"time_out\":\"86400\",\"divId\":\"2bd_985_8def_29\",\"name\":\"第一个数据质量检测\",\"positionX\":276,\"positionY\":68,\"type\":\"tasks\"}],\"line\":[]}','off','on','on','on','on','off','0'),('924057362715643904','第一个数据质量检测-复制',NULL,'ETL','2021-12-11 11:59:04','2021-12-25 11:59:04','1d','1','0',0,NULL,'',NULL,'2021-12-11 11:59:04',NULL,'0 0 0 * * ? *','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"2bd_985_8def_29\",\"etl_task_id\":\"919326325259374592\",\"etl_context\":\"第一个数据质量检测\",\"more_task\":\"QUALITY\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"time_out\":\"86400\",\"divId\":\"2bd_985_8def_29\",\"name\":\"第一个数据质量检测\",\"positionX\":276,\"positionY\":68,\"type\":\"tasks\"}],\"line\":[]}','off','off','on','on','on','off','0'),('927243951918813184','测试批量生成',NULL,'ETL','2022-01-02 08:55:33','2022-01-02 08:55:33','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"490_a24_b216_7e\",\"etl_task_id\":\"927243236525740032\",\"etl_context\":\"batch_account_info_123\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"490_a24_b216_7e\",\"name\":\"batch_account_info_123\",\"positionX\":310,\"positionY\":59,\"type\":\"tasks\"}],\"line\":[]}','off','off','on','on','off','off','0'),('929154459550879744','测试vscode',NULL,'ETL','2022-01-07 15:25:44','2022-01-07 15:25:44','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"adc_5df_8990_ef\",\"etl_context\":\"fsdafsfs\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\\r\\nping 127.0.0.1\",\"time_out\":\"86400\",\"divId\":\"adc_5df_8990_ef\",\"name\":\"fsdafsfs\",\"positionX\":261,\"positionY\":92,\"type\":\"shell\"}],\"line\":[]}','off','off','off','off','off','off','0'),('929487962419236864','测试windows上server',NULL,'ETL','2022-01-08 13:32:45','2022-01-11 13:32:45','1d','1','0',0,NULL,'',NULL,'2022-01-08 13:32:45',NULL,'0 * * * * ? *','remove',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'on','','{\"tasks\":[{\"id\":\"b34_ceb_a1a2_c3\",\"etl_task_id\":\"837799577204559872\",\"etl_context\":\"jdbc优化过滤\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"zdh_instance\":\"zdh_server\",\"is_disenable\":\"false\",\"time_out\":\"86400\",\"divId\":\"b34_ceb_a1a2_c3\",\"name\":\"jdbc优化过滤\",\"positionX\":328,\"positionY\":78,\"type\":\"tasks\"}],\"line\":[]}','off','off','on','on','on','on','0'),('932253122644938752','测试flinkCDC',NULL,'ETL','2022-01-16 04:39:56','2022-01-16 04:39:56','1d','1','0',0,NULL,'',NULL,NULL,NULL,'0 0/1 * * * ? *','remove',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5','2022-01-18 09:56:00','on','','{\"tasks\":[{\"id\":\"edf_6d8_832c_de\",\"etl_task_id\":\"904327604331352064\",\"etl_context\":\"第三个FLINK_SQL_CDC\",\"more_task\":\"FLINK\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"edf_6d8_832c_de\",\"name\":\"第三个FLINK_SQL_CDC\",\"positionX\":216,\"positionY\":75,\"type\":\"tasks\"}],\"line\":[]}','off','off','on','on','on','on','1'),('936967626691710976','测试privateKey',NULL,'ETL','2022-01-29 04:53:40','2022-01-29 04:53:40','1d','1','0',0,NULL,'',NULL,NULL,NULL,'0 0 * * * ? *','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'on','','{\"tasks\":[{\"id\":\"856_f58_8b26_e1\",\"etl_task_id\":\"936967307782000640\",\"etl_context\":\"测试privateKey\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"856_f58_8b26_e1\",\"name\":\"测试privateKey\",\"positionX\":302,\"positionY\":93,\"type\":\"tasks\"}],\"line\":[]}','off','off','on','on','on','on','0');
/*!40000 ALTER TABLE `quartz_job_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process_flow_info`
--

DROP TABLE IF EXISTS `process_flow_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `process_flow_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `flow_id` varchar(64) NOT NULL DEFAULT '0' COMMENT '流程标识id',
  `event_code` varchar(64) NOT NULL DEFAULT '' COMMENT '事件code',
  `config_code` varchar(64) NOT NULL DEFAULT '0' COMMENT '审批流程code',
  `context` varchar(128) NOT NULL DEFAULT '' COMMENT '事件说明',
  `auditor_id` text COMMENT '审批人id,逗号分割',
  `is_show` varchar(128) NOT NULL DEFAULT '0' COMMENT '1:可见,0:不可见',
  `owner` varchar(100) NOT NULL DEFAULT '' COMMENT '拥有者',
  `pre_id` varchar(100) NOT NULL DEFAULT '' COMMENT '流程id',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `status` varchar(10) NOT NULL DEFAULT '0' COMMENT '流程状态,0:未审批,1:审批完成,2:不通过,3:撤销',
  `is_end` varchar(64) NOT NULL DEFAULT '0' COMMENT '0:非最后一个节点,1:最后一个节点',
  `level` varchar(64) NOT NULL DEFAULT '0' COMMENT '审批节点环节',
  `event_id` varchar(64) NOT NULL DEFAULT '' COMMENT '发起流程的具体事件唯一键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=934780942097059841 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process_flow_info`
--

LOCK TABLES `process_flow_info` WRITE;
/*!40000 ALTER TABLE `process_flow_info` DISABLE KEYS */;
INSERT INTO `process_flow_info` VALUES (903073104698281985,'903073104698281984','data_pub','data','发布数据-第二个发布task_logs','1','1','1','','2021-10-27 16:10:06','1','0','1','903073104664727552'),(903073104727642112,'903073104698281984','data_pub','data','发布数据-第二个发布task_logs','1','1','1','903073104698281985','2021-10-27 16:10:06','1','0','2','903073104664727552'),(903073104736030722,'903073104698281984','data_pub','data','发布数据-第二个发布task_logs','1','1','1','903073104727642112','2021-10-27 16:10:06','1','1','7','903073104664727552'),(903076295632490497,'903076295632490496','data_pub','data','发布数据-第3个发布','1','1','1','','2021-10-27 16:22:47','1','0','1','903076295573770240'),(903076295661850624,'903076295632490496','data_pub','data','发布数据-第3个发布','1','1','1','903076295632490497','2021-10-27 16:22:47','1','0','2','903076295573770240'),(903076295674433537,'903076295632490496','data_pub','data','发布数据-第3个发布','1','1','1','903076295661850624','2021-10-27 16:22:47','1','1','7','903076295573770240'),(903082515185537025,'903082515185537024','data_apply','data','申请数据-account_info','1','1','2','','2021-10-27 16:47:30','1','0','1','903082514808049664'),(903082515911151616,'903082515185537024','data_apply','data','申请数据-account_info','1','1','2','903082515185537025','2021-10-27 16:47:30','1','0','2','903082514808049664'),(903082516624183296,'903082515185537024','data_apply','data','申请数据-account_info','1','1','2','903082515911151616','2021-10-27 16:47:30','1','1','7','903082514808049664'),(903085573625876481,'903085573625876480','data_apply','data','申请数据-account_info','1','1','2','','2021-10-27 16:59:39','1','0','1','903085573202251776'),(903085573877534720,'903085573625876480','data_apply','data','申请数据-account_info','1','1','2','903085573625876481','2021-10-27 16:59:39','0','0','2','903085573202251776'),(903085574162747393,'903085573625876480','data_apply','data','申请数据-account_info','1','0','2','903085573877534720','2021-10-27 16:59:39','0','1','7','903085573202251776'),(934765595923058689,'934765595923058688','data_pub','data','发布数据-测试新版审批','1','1','1','','2022-01-23 03:04:45','1','0','1','934765595805618176'),(934765595956613121,'934765595923058688','data_pub','data','发布数据-测试新版审批','1','1','1','934765595923058689','2022-01-23 03:04:45','1','1','7','934765595805618176'),(934779987989368833,'934779987989368832','data_pub','data','发布数据-admin发布测试','1','1','1','','2022-01-23 04:01:57','1','0','1','934779987913871360'),(934779988014534656,'934779987989368832','data_pub','data','发布数据-admin发布测试','1','1','1','934779987989368833','2022-01-23 04:01:57','1','1','7','934779987913871360'),(934780735405953025,'934780735405953024','data_pub','data','发布数据-admin用户发布测试','1','1','2','','2022-01-23 04:04:55','1','0','1','934780735389175808'),(934780735414341635,'934780735405953024','data_pub','data','发布数据-admin用户发布测试','1','1','2','934780735405953025','2022-01-23 04:04:55','1','1','7','934780735389175808'),(934780941404999681,'934780941404999680','data_apply','data','申请数据-server_task_info','1','1','1','','2022-01-23 04:05:44','1','0','1','934780940612276224'),(934780942097059840,'934780941404999680','data_apply','data','申请数据-server_task_info','1','1','1','934780941404999681','2022-01-23 04:05:44','1','1','7','934780940612276224');
/*!40000 ALTER TABLE `process_flow_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality_task_info`
--

DROP TABLE IF EXISTS `quality_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quality_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
  `data_sources_table_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源表名',
  `data_sources_file_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源文件名',
  `data_sources_params_input` varchar(500) DEFAULT NULL COMMENT '输入数据源参数',
  `data_sources_filter_input` varchar(500) DEFAULT NULL COMMENT '输入数据源过滤条件',
  `file_type_input` varchar(10) DEFAULT NULL COMMENT '输入文件类型',
  `encoding_input` varchar(10) DEFAULT NULL COMMENT '输入文件编码',
  `sep_input` varchar(10) DEFAULT NULL COMMENT '输入分割符',
  `header_input` varchar(10) DEFAULT NULL COMMENT '输入是否包含表头',
  `data_sources_table_columns` text COMMENT '输入表字段名',
  `data_sources_file_columns` text COMMENT '输入文件字段名',
  `repartition_num_input` varchar(64) NOT NULL DEFAULT '' COMMENT '洗牌个数默认空',
  `repartition_cols_input` varchar(256) NOT NULL DEFAULT '' COMMENT '洗牌字段默认空',
  `quality_rule_config` text COMMENT '规则配置json',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=919326325259374593 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality_task_info`
--

LOCK TABLES `quality_task_info` WRITE;
/*!40000 ALTER TABLE `quality_task_info` DISABLE KEYS */;
INSERT INTO `quality_task_info` VALUES (919326325259374592,'第一个数据质量检测','58','JDBC','account_info','','','','','','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone,enable,user_group,roles,signature','','','','[{\"quality_rule\":\"917940616715833344\",\"quality_columns\":\"phone\"},{\"quality_rule\":\"919571916811931648\",\"quality_columns\":\"phone\"},{\"quality_rule\":\"919638908919091200\",\"quality_columns\":\"email\"}]','1','2021-12-11 12:34:36','2021-12-12 09:54:45','','','','');
/*!40000 ALTER TABLE `quality_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `param_info`
--

DROP TABLE IF EXISTS `param_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `param_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `param_name` varchar(512) DEFAULT NULL COMMENT '参数名称',
  `param_value` text COMMENT '参数名称',
  `param_context` varchar(200) DEFAULT NULL COMMENT '参数说明',
  `param_type` varchar(100) DEFAULT NULL COMMENT '参数类型',
  `param_timeout` varchar(200) DEFAULT NULL COMMENT '缓存超时时间,单位秒',
  `owner` varchar(64) DEFAULT NULL COMMENT '拥有者',
  `status` varchar(64) DEFAULT NULL COMMENT '状态,启用:on, 关闭:off',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `param_info`
--

LOCK TABLES `param_info` WRITE;
/*!40000 ALTER TABLE `param_info` DISABLE KEYS */;
INSERT INTO `param_info` VALUES (1,'zdh_ssh_max_thread','100','SHELL最大执行限制','1','100','1','on','0','2022-01-29 10:19:39','2022-02-06 03:11:55'),(2,'a','100','测试入Redis','1','100','1','on','0','2022-02-06 03:41:12','2022-02-06 03:41:12');
/*!40000 ALTER TABLE `param_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data_tag_group_info`
--

DROP TABLE IF EXISTS `data_tag_group_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `data_tag_group_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tag_group_code` varchar(512) DEFAULT NULL COMMENT '标识组code',
  `tag_group_name` varchar(200) DEFAULT NULL COMMENT '标识组名称',
  `tag_codes` varchar(512) DEFAULT NULL COMMENT '标识code列表,逗号分割',
  `product_code` varchar(100) DEFAULT NULL COMMENT '产品code',
  `owner` varchar(64) DEFAULT NULL COMMENT '拥有者',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=939114806642741249 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_tag_group_info`
--

LOCK TABLES `data_tag_group_info` WRITE;
/*!40000 ALTER TABLE `data_tag_group_info` DISABLE KEYS */;
INSERT INTO `data_tag_group_info` VALUES (939114806642741248,'test_group1','测试标识组','tag1,sjyy1','zdh','1','0','2022-02-04 03:06:58','2022-02-04 05:26:19');
/*!40000 ALTER TABLE `data_tag_group_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zdh_download_info`
--

DROP TABLE IF EXISTS `zdh_download_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zdh_download_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) DEFAULT NULL,
  `file_name` varchar(200) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `down_count` int DEFAULT NULL,
  `etl_date` timestamp NULL DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `job_context` varchar(100) DEFAULT NULL,
  `is_notice` varchar(8) DEFAULT 'false' COMMENT '是否已经通知true/false',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zdh_download_info`
--

LOCK TABLES `zdh_download_info` WRITE;
/*!40000 ALTER TABLE `zdh_download_info` DISABLE KEYS */;
INSERT INTO `zdh_download_info` VALUES (1,NULL,'G:/data/1/act2.csv','2020-06-11 09:15:36',2,'2020-06-12 16:00:00','1','读取hive并下载文件','true'),(2,NULL,'/home/zyc/download/1/aa.csv','2021-09-19 11:43:05',0,'2021-09-19 11:36:35','1','tttt','true'),(3,NULL,'/home/zyc/download/1/aa.csv','2021-09-19 12:47:43',0,'2021-09-19 12:47:15','1','tttt','true'),(4,NULL,'/home/zyc/download/1/aa.csv','2021-09-19 12:59:55',0,'2021-09-19 12:59:38','1','tttt','true');
/*!40000 ALTER TABLE `zdh_download_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_resource_info`
--

DROP TABLE IF EXISTS `user_resource_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_resource_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户id',
  `resource_id` varchar(100) DEFAULT NULL COMMENT '资源id',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=943 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_resource_info`
--

LOCK TABLES `user_resource_info` WRITE;
/*!40000 ALTER TABLE `user_resource_info` DISABLE KEYS */;
INSERT INTO `user_resource_info` VALUES (863,'1','805374183432261632',NULL,NULL),(864,'1','802848818109353984',NULL,NULL),(865,'1','802850170428461056',NULL,NULL),(866,'1','802918652050411520',NULL,NULL),(867,'1','805357642351382528',NULL,NULL),(868,'1','805369519538180096',NULL,NULL),(869,'1','808616254788079616',NULL,NULL),(870,'1','810816897514737664',NULL,NULL),(871,'1','893810193274507264',NULL,NULL),(872,'1','802852358580080640',NULL,NULL),(873,'1','802918760057933824',NULL,NULL),(874,'1','802919044364636160',NULL,NULL),(875,'1','802931697527033856',NULL,NULL),(876,'1','805372907965386752',NULL,NULL),(877,'1','808616077255774208',NULL,NULL),(878,'1','810825569494110208',NULL,NULL),(879,'1','838335003375964160',NULL,NULL),(880,'1','802876953722884096',NULL,NULL),(881,'1','802919157430489088',NULL,NULL),(882,'1','889247298196869120',NULL,NULL),(883,'1','802930870510948352',NULL,NULL),(884,'1','802932157390524416',NULL,NULL),(885,'1','839059609225269248',NULL,NULL),(886,'1','802931116691427328',NULL,NULL),(887,'1','802932355596554240',NULL,NULL),(888,'1','891283647431184384',NULL,NULL),(889,'1','802932548165439488',NULL,NULL),(890,'1','805431924678987776',NULL,NULL),(891,'1','839152432125579264',NULL,NULL),(892,'1','858320387178500096',NULL,NULL),(893,'1','802933021148712960',NULL,NULL),(894,'1','809886572093640704',NULL,NULL),(895,'1','805193674668380160',NULL,NULL),(896,'1','805531084459610112',NULL,NULL),(897,'1','810817759893000192',NULL,NULL),(898,'1','802932890089295872',NULL,NULL),(899,'1','802933165302747136',NULL,NULL),(900,'1','830556376391487488',NULL,NULL),(901,'1','802931308593418240',NULL,NULL),(902,'1','893816925920956416',NULL,NULL),(903,'1','893817125867622400',NULL,NULL),(904,'2','802850170428461056',NULL,NULL),(905,'2','802852358580080640',NULL,NULL),(906,'2','802931697527033856',NULL,NULL),(907,'2','802876953722884096',NULL,NULL),(908,'2','802932157390524416',NULL,NULL),(909,'2','802932355596554240',NULL,NULL),(910,'2','802932548165439488',NULL,NULL),(911,'2','802932890089295872',NULL,NULL),(912,'2','802933021148712960',NULL,NULL),(913,'2','802933165302747136',NULL,NULL),(914,'2','805193674668380160',NULL,NULL),(915,'2','805374183432261632',NULL,NULL),(916,'2','805369519538180096',NULL,NULL),(917,'2','805372907965386752',NULL,NULL),(918,'2','802918652050411520',NULL,NULL),(919,'2','802918760057933824',NULL,NULL),(920,'2','802919044364636160',NULL,NULL),(921,'2','802919157430489088',NULL,NULL),(922,'2','802930870510948352',NULL,NULL),(923,'2','802931116691427328',NULL,NULL),(924,'2','802931308593418240',NULL,NULL),(925,'2','810817759893000192',NULL,NULL),(926,'2','810816897514737664',NULL,NULL),(927,'2','810825569494110208',NULL,NULL),(928,'2','838335003375964160',NULL,NULL),(929,'2','839059609225269248',NULL,NULL),(930,'2','839152432125579264',NULL,NULL),(931,'2','858320387178500096',NULL,NULL),(932,'2','891283647431184384',NULL,NULL),(933,'2','889247298196869120',NULL,NULL),(934,'2','893816925920956416',NULL,NULL),(935,'2','893810193274507264',NULL,NULL),(936,'2','893817125867622400',NULL,NULL),(937,'2','805431924678987776',NULL,NULL),(938,'2','809886572093640704',NULL,NULL),(939,'2','808616254788079616',NULL,NULL),(940,'2','808616077255774208',NULL,NULL),(941,'2','805531084459610112',NULL,NULL),(942,'2','830556376391487488',NULL,NULL);
/*!40000 ALTER TABLE `user_resource_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `approval_event_info`
--

DROP TABLE IF EXISTS `approval_event_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `approval_event_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL DEFAULT '0' COMMENT '审批流程code',
  `event_code` varchar(64) NOT NULL DEFAULT '' COMMENT '事件code',
  `event_context` varchar(128) NOT NULL DEFAULT '' COMMENT '事件说明',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `approval_event_info`
--

LOCK TABLES `approval_event_info` WRITE;
/*!40000 ALTER TABLE `approval_event_info` DISABLE KEYS */;
INSERT INTO `approval_event_info` VALUES (1,'data','data_pub','数据发布事件','2021-10-12 15:02:09'),(2,'data','data_apply','数据申请','2021-10-12 15:02:09');
/*!40000 ALTER TABLE `approval_event_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_info`
--

DROP TABLE IF EXISTS `account_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `user_password` varchar(100) DEFAULT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `is_use_email` varchar(10) DEFAULT NULL COMMENT '是否开启邮箱告警',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `is_use_phone` varchar(10) DEFAULT NULL COMMENT '是否开启手机告警',
  `enable` varchar(8) DEFAULT 'false' COMMENT '是否启用true/false',
  `user_group` varchar(8) DEFAULT '' COMMENT '用户所在组',
  `roles` text COMMENT '角色列表',
  `signature` varchar(64) DEFAULT NULL COMMENT '签名',
  `tag_group_code` varchar(512) NOT NULL DEFAULT '' COMMENT '数据组标识,多个逗号分割',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_info`
--

LOCK TABLES `account_info` WRITE;
/*!40000 ALTER TABLE `account_info` DISABLE KEYS */;
INSERT INTO `account_info` VALUES (1,'zyc','123456','1209687056@qq.com','on','15236479806',NULL,'true','6','894201076759138304','大数据先行者','test_group1'),(2,'admin','123456','1209687056@qq.com',NULL,'',NULL,'true','8','894201076759138304,894254232000008192','','test_group1');
/*!40000 ALTER TABLE `account_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_resource_info`
--

DROP TABLE IF EXISTS `role_resource_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_resource_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` varchar(100) DEFAULT NULL COMMENT 'role id',
  `resource_id` varchar(100) DEFAULT NULL COMMENT '资源id',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29405 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_resource_info`
--

LOCK TABLES `role_resource_info` WRITE;
/*!40000 ALTER TABLE `role_resource_info` DISABLE KEYS */;
INSERT INTO `role_resource_info` VALUES (274,'894254232000008192','802848818109353984',NULL,NULL),(275,'894254232000008192','802850170428461056',NULL,NULL),(276,'894254232000008192','802852358580080640',NULL,NULL),(277,'894254232000008192','802918652050411520',NULL,NULL),(278,'894254232000008192','802918760057933824',NULL,NULL),(279,'894254232000008192','802919044364636160',NULL,NULL),(280,'894254232000008192','802919157430489088',NULL,NULL),(281,'894254232000008192','802930870510948352',NULL,NULL),(282,'894254232000008192','802931116691427328',NULL,NULL),(283,'894254232000008192','839152432125579264',NULL,NULL),(284,'894254232000008192','858320387178500096',NULL,NULL),(285,'894254232000008192','802931308593418240',NULL,NULL),(286,'894254232000008192','802931697527033856',NULL,NULL),(287,'894254232000008192','802876953722884096',NULL,NULL),(288,'894254232000008192','889247298196869120',NULL,NULL),(289,'894254232000008192','802932157390524416',NULL,NULL),(290,'894254232000008192','802932355596554240',NULL,NULL),(291,'894254232000008192','891283647431184384',NULL,NULL),(292,'894254232000008192','802932548165439488',NULL,NULL),(293,'894254232000008192','802933021148712960',NULL,NULL),(294,'894254232000008192','805193674668380160',NULL,NULL),(295,'894254232000008192','805531084459610112',NULL,NULL),(296,'894254232000008192','810817759893000192',NULL,NULL),(297,'894254232000008192','802932890089295872',NULL,NULL),(298,'894254232000008192','802933165302747136',NULL,NULL),(299,'894254232000008192','830556376391487488',NULL,NULL),(9826,'904072220689567744','802850170428461056',NULL,NULL),(9827,'904072220689567744','802848818109353984',NULL,NULL),(9828,'904072220689567744','802852358580080640',NULL,NULL),(9829,'904072220689567744','802918652050411520',NULL,NULL),(11561,'898997645559730176','802848818109353984',NULL,NULL),(11562,'898997645559730176','802850170428461056',NULL,NULL),(11563,'898997645559730176','899083124191793152',NULL,NULL),(11564,'898997645559730176','899083287929032704',NULL,NULL),(11565,'898997645559730176','899083494326538240',NULL,NULL),(11566,'898997645559730176','899083652330164224',NULL,NULL),(11567,'898997645559730176','904065292424974336',NULL,NULL),(11568,'898997645559730176','802852358580080640',NULL,NULL),(11569,'898997645559730176','802918652050411520',NULL,NULL),(11570,'898997645559730176','898333103779483648',NULL,NULL),(11571,'898997645559730176','899797407535992832',NULL,NULL),(11572,'898997645559730176','898334091781345280',NULL,NULL),(11573,'898997645559730176','898334486809284608',NULL,NULL),(11574,'898997645559730176','898334268604813312',NULL,NULL),(11575,'898997645559730176','898335485523398656',NULL,NULL),(11576,'898997645559730176','899797834717466624',NULL,NULL),(11577,'898997645559730176','899799748200894464',NULL,NULL),(11578,'898997645559730176','802918760057933824',NULL,NULL),(11579,'898997645559730176','898335839925309440',NULL,NULL),(11580,'898997645559730176','898336112890613760',NULL,NULL),(11581,'898997645559730176','898336252711931904',NULL,NULL),(11582,'898997645559730176','898336446329393152',NULL,NULL),(11583,'898997645559730176','899804862013771776',NULL,NULL),(11584,'898997645559730176','899805009363865600',NULL,NULL),(11585,'898997645559730176','899805166738345984',NULL,NULL),(11586,'898997645559730176','802919044364636160',NULL,NULL),(11587,'898997645559730176','898336838400348160',NULL,NULL),(11588,'898997645559730176','898337064490110976',NULL,NULL),(11589,'898997645559730176','898337228843913216',NULL,NULL),(11590,'898997645559730176','898337404199374848',NULL,NULL),(11591,'898997645559730176','899807422577643520',NULL,NULL),(11592,'898997645559730176','899807641243488256',NULL,NULL),(11593,'898997645559730176','802919157430489088',NULL,NULL),(11594,'898997645559730176','898337767392546816',NULL,NULL),(11595,'898997645559730176','898337973207044096',NULL,NULL),(11596,'898997645559730176','898338119047188480',NULL,NULL),(11597,'898997645559730176','898338251511697408',NULL,NULL),(11598,'898997645559730176','899808823739420672',NULL,NULL),(11599,'898997645559730176','802930870510948352',NULL,NULL),(11600,'898997645559730176','899810180206694400',NULL,NULL),(11601,'898997645559730176','899810791648137216',NULL,NULL),(11602,'898997645559730176','899810992530132992',NULL,NULL),(11603,'898997645559730176','899811121018441728',NULL,NULL),(11604,'898997645559730176','899811229017575424',NULL,NULL),(11605,'898997645559730176','899811331564113920',NULL,NULL),(11606,'898997645559730176','802931116691427328',NULL,NULL),(11607,'898997645559730176','898338570987638784',NULL,NULL),(11608,'898997645559730176','904046610378395648',NULL,NULL),(11609,'898997645559730176','898338819265269760',NULL,NULL),(11610,'898997645559730176','898338990552256512',NULL,NULL),(11611,'898997645559730176','898339248917188608',NULL,NULL),(11612,'898997645559730176','899811720153796608',NULL,NULL),(11613,'898997645559730176','904047129859723264',NULL,NULL),(11614,'898997645559730176','839152432125579264',NULL,NULL),(11615,'898997645559730176','899813283056324608',NULL,NULL),(11616,'898997645559730176','899814315572334592',NULL,NULL),(11617,'898997645559730176','904134748278886400',NULL,NULL),(11618,'898997645559730176','904135062444838912',NULL,NULL),(11619,'898997645559730176','899814524809383936',NULL,NULL),(11620,'898997645559730176','899814663515017216',NULL,NULL),(11621,'898997645559730176','899814772944408576',NULL,NULL),(11622,'898997645559730176','858320387178500096',NULL,NULL),(11623,'898997645559730176','898339478811185152',NULL,NULL),(11624,'898997645559730176','898339672948740096',NULL,NULL),(11625,'898997645559730176','898339790284394496',NULL,NULL),(11626,'898997645559730176','898339903127949312',NULL,NULL),(11627,'898997645559730176','899815335576735744',NULL,NULL),(11628,'898997645559730176','802931308593418240',NULL,NULL),(11629,'898997645559730176','898340104257409024',NULL,NULL),(11630,'898997645559730176','904044939199909888',NULL,NULL),(11631,'898997645559730176','898340234041757696',NULL,NULL),(11632,'898997645559730176','898340346117754880',NULL,NULL),(11633,'898997645559730176','898341132600086528',NULL,NULL),(11634,'898997645559730176','904050101738016768',NULL,NULL),(11635,'898997645559730176','904051931872235520',NULL,NULL),(11636,'898997645559730176','904060576521523200',NULL,NULL),(11637,'898997645559730176','904061109097467904',NULL,NULL),(11638,'898997645559730176','904061224998670336',NULL,NULL),(11639,'898997645559730176','904052710129537024',NULL,NULL),(11640,'898997645559730176','904053017009983488',NULL,NULL),(11641,'898997645559730176','904053585896017920',NULL,NULL),(11642,'898997645559730176','904055132549812224',NULL,NULL),(11643,'898997645559730176','904055741369815040',NULL,NULL),(11644,'898997645559730176','904056255981555712',NULL,NULL),(11645,'898997645559730176','904060028510539776',NULL,NULL),(11646,'898997645559730176','904063054986088448',NULL,NULL),(11647,'898997645559730176','904057211481755648',NULL,NULL),(11648,'898997645559730176','904059356146831360',NULL,NULL),(11649,'898997645559730176','904058054746574848',NULL,NULL),(11650,'898997645559730176','904117946815614976',NULL,NULL),(11651,'898997645559730176','904118233940889600',NULL,NULL),(11652,'898997645559730176','904118420969099264',NULL,NULL),(11653,'898997645559730176','904118636598267904',NULL,NULL),(11654,'898997645559730176','904118843020939264',NULL,NULL),(11655,'898997645559730176','904120248553181184',NULL,NULL),(11656,'898997645559730176','904065881678548992',NULL,NULL),(11657,'898997645559730176','904066172054409216',NULL,NULL),(11658,'898997645559730176','904066367945183232',NULL,NULL),(11659,'898997645559730176','802931697527033856',NULL,NULL),(11660,'898997645559730176','898919578183143424',NULL,NULL),(11661,'898997645559730176','898919751642779648',NULL,NULL),(11662,'898997645559730176','898919884019208192',NULL,NULL),(11663,'898997645559730176','898921890200948736',NULL,NULL),(11664,'898997645559730176','898922054793826304',NULL,NULL),(11665,'898997645559730176','802876953722884096',NULL,NULL),(11666,'898997645559730176','898922698124562432',NULL,NULL),(11667,'898997645559730176','898923212249763840',NULL,NULL),(11668,'898997645559730176','898923484506230784',NULL,NULL),(11669,'898997645559730176','898923625090912256',NULL,NULL),(11670,'898997645559730176','904875825113862144',NULL,NULL),(11671,'898997645559730176','889247298196869120',NULL,NULL),(11672,'898997645559730176','802932355596554240',NULL,NULL),(11673,'898997645559730176','802932157390524416',NULL,NULL),(11674,'898997645559730176','891283647431184384',NULL,NULL),(11675,'898997645559730176','894524363561242624',NULL,NULL),(11676,'898997645559730176','898924030910795776',NULL,NULL),(11677,'898997645559730176','898924424034521088',NULL,NULL),(11678,'898997645559730176','898924549347741696',NULL,NULL),(11679,'898997645559730176','898924812993302528',NULL,NULL),(11680,'898997645559730176','904068373158039552',NULL,NULL),(11681,'898997645559730176','894896876246011904',NULL,NULL),(11682,'898997645559730176','898925461227180032',NULL,NULL),(11683,'898997645559730176','898925640273629184',NULL,NULL),(11684,'898997645559730176','898925753096212480',NULL,NULL),(11685,'898997645559730176','898926933432078336',NULL,NULL),(11686,'898997645559730176','904069900111187968',NULL,NULL),(11687,'898997645559730176','904070354979262464',NULL,NULL),(11688,'898997645559730176','896164931303378944',NULL,NULL),(11689,'898997645559730176','898992327668797440',NULL,NULL),(11690,'898997645559730176','898992469134282752',NULL,NULL),(11691,'898997645559730176','898992569562697728',NULL,NULL),(11692,'898997645559730176','898992688609628160',NULL,NULL),(11693,'898997645559730176','904070687604346880',NULL,NULL),(11694,'898997645559730176','839059609225269248',NULL,NULL),(11695,'898997645559730176','900519809114968064',NULL,NULL),(11696,'898997645559730176','904071524649013248',NULL,NULL),(11697,'898997645559730176','902346927830470656',NULL,NULL),(11698,'898997645559730176','902685738875752448',NULL,NULL),(11699,'898997645559730176','902710107161235456',NULL,NULL),(11700,'898997645559730176','903063574157463552',NULL,NULL),(11701,'898997645559730176','903063690486484992',NULL,NULL),(11702,'898997645559730176','802933021148712960',NULL,NULL),(11703,'898997645559730176','899000798854254592',NULL,NULL),(11704,'898997645559730176','805193674668380160',NULL,NULL),(11705,'898997645559730176','805374183432261632',NULL,NULL),(11706,'898997645559730176','899001510107549696',NULL,NULL),(11707,'898997645559730176','899001733512957952',NULL,NULL),(11708,'898997645559730176','805369519538180096',NULL,NULL),(11709,'898997645559730176','899001982079995904',NULL,NULL),(11710,'898997645559730176','899002142675701760',NULL,NULL),(11711,'898997645559730176','808616077255774208',NULL,NULL),(11712,'898997645559730176','899076126133981184',NULL,NULL),(11713,'898997645559730176','899816557880807424',NULL,NULL),(11714,'898997645559730176','899076266156625920',NULL,NULL),(11715,'898997645559730176','899076393432780800',NULL,NULL),(11716,'898997645559730176','899076708504702976',NULL,NULL),(11717,'898997645559730176','899076830349234176',NULL,NULL),(11718,'898997645559730176','899077318159372288',NULL,NULL),(11719,'898997645559730176','899077560292347904',NULL,NULL),(11720,'898997645559730176','810816897514737664',NULL,NULL),(11721,'898997645559730176','810817759893000192',NULL,NULL),(11722,'898997645559730176','899078834731618304',NULL,NULL),(11723,'898997645559730176','899079195127189504',NULL,NULL),(11724,'898997645559730176','899079347393007616',NULL,NULL),(11725,'898997645559730176','899079618982580224',NULL,NULL),(11726,'898997645559730176','810825569494110208',NULL,NULL),(11727,'898997645559730176','899079911518507008',NULL,NULL),(11728,'898997645559730176','899080146999316480',NULL,NULL),(11729,'898997645559730176','899081132492984320',NULL,NULL),(11730,'898997645559730176','899081270330396672',NULL,NULL),(11731,'898997645559730176','899081439826415616',NULL,NULL),(11732,'898997645559730176','838335003375964160',NULL,NULL),(11733,'898997645559730176','903066735052066816',NULL,NULL),(11734,'898997645559730176','802932890089295872',NULL,NULL),(11735,'898997645559730176','802933165302747136',NULL,NULL),(11736,'898997645559730176','830556376391487488',NULL,NULL),(11737,'898997645559730176','904017457503539200',NULL,NULL),(11738,'898997645559730176','903065878872985600',NULL,NULL),(11739,'898997645559730176','904015493940121600',NULL,NULL),(11740,'898997645559730176','904015737297833984',NULL,NULL),(11741,'898997645559730176','904015873528827904',NULL,NULL),(11742,'898997645559730176','904016015229194240',NULL,NULL),(11743,'898997645559730176','899793046982365184',NULL,NULL),(11744,'898997645559730176','899796275644338176',NULL,NULL),(11745,'898997645559730176','899796487305695232',NULL,NULL),(11746,'898997645559730176','899796627491917824',NULL,NULL),(11747,'898997645559730176','899796760371662848',NULL,NULL),(11748,'898997645559730176','899796895541497856',NULL,NULL),(11749,'898997645559730176','906703041556647936',NULL,NULL),(29085,'894201076759138304','805374183432261632',NULL,NULL),(29086,'894201076759138304','929349427703844864',NULL,NULL),(29087,'894201076759138304','802848818109353984',NULL,NULL),(29088,'894201076759138304','802850170428461056',NULL,NULL),(29089,'894201076759138304','802918652050411520',NULL,NULL),(29090,'894201076759138304','802931308593418240',NULL,NULL),(29091,'894201076759138304','805369519538180096',NULL,NULL),(29092,'894201076759138304','808616254788079616',NULL,NULL),(29093,'894201076759138304','810816897514737664',NULL,NULL),(29094,'894201076759138304','893810193274507264',NULL,NULL),(29095,'894201076759138304','894524363561242624',NULL,NULL),(29096,'894201076759138304','898333103779483648',NULL,NULL),(29097,'894201076759138304','898335839925309440',NULL,NULL),(29098,'894201076759138304','898336838400348160',NULL,NULL),(29099,'894201076759138304','898337767392546816',NULL,NULL),(29100,'894201076759138304','898338570987638784',NULL,NULL),(29101,'894201076759138304','898339478811185152',NULL,NULL),(29102,'894201076759138304','898340104257409024',NULL,NULL),(29103,'894201076759138304','898919578183143424',NULL,NULL),(29104,'894201076759138304','898921890200948736',NULL,NULL),(29105,'894201076759138304','898922698124562432',NULL,NULL),(29106,'894201076759138304','898924030910795776',NULL,NULL),(29107,'894201076759138304','898925461227180032',NULL,NULL),(29108,'894201076759138304','898992327668797440',NULL,NULL),(29109,'894201076759138304','898994298257674240',NULL,NULL),(29110,'894201076759138304','898996218540068864',NULL,NULL),(29111,'894201076759138304','898998799848968192',NULL,NULL),(29112,'894201076759138304','899000798854254592',NULL,NULL),(29113,'894201076759138304','899001510107549696',NULL,NULL),(29114,'894201076759138304','899001982079995904',NULL,NULL),(29115,'894201076759138304','899059721481228288',NULL,NULL),(29116,'894201076759138304','899076126133981184',NULL,NULL),(29117,'894201076759138304','899078834731618304',NULL,NULL),(29118,'894201076759138304','899079911518507008',NULL,NULL),(29119,'894201076759138304','899083124191793152',NULL,NULL),(29120,'894201076759138304','899797407535992832',NULL,NULL),(29121,'894201076759138304','899810180206694400',NULL,NULL),(29122,'894201076759138304','899813283056324608',NULL,NULL),(29123,'894201076759138304','899816557880807424',NULL,NULL),(29124,'894201076759138304','900519809114968064',NULL,NULL),(29125,'894201076759138304','902685738875752448',NULL,NULL),(29126,'894201076759138304','903066735052066816',NULL,NULL),(29127,'894201076759138304','904015737297833984',NULL,NULL),(29128,'894201076759138304','904017457503539200',NULL,NULL),(29129,'894201076759138304','904042210536722432',NULL,NULL),(29130,'894201076759138304','904042889393213440',NULL,NULL),(29131,'894201076759138304','904044939199909888',NULL,NULL),(29132,'894201076759138304','904046610378395648',NULL,NULL),(29133,'894201076759138304','904051931872235520',NULL,NULL),(29134,'894201076759138304','904053017009983488',NULL,NULL),(29135,'894201076759138304','904055132549812224',NULL,NULL),(29136,'894201076759138304','904059356146831360',NULL,NULL),(29137,'894201076759138304','904060576521523200',NULL,NULL),(29138,'894201076759138304','904061109097467904',NULL,NULL),(29139,'894201076759138304','904061224998670336',NULL,NULL),(29140,'894201076759138304','904117946815614976',NULL,NULL),(29141,'894201076759138304','904134748278886400',NULL,NULL),(29142,'894201076759138304','913947870334291968',NULL,NULL),(29143,'894201076759138304','916457943107375104',NULL,NULL),(29144,'894201076759138304','917918741411401728',NULL,NULL),(29145,'894201076759138304','917930429175042048',NULL,NULL),(29146,'894201076759138304','917936957084930048',NULL,NULL),(29147,'894201076759138304','918293897011007488',NULL,NULL),(29148,'894201076759138304','921829181543682048',NULL,NULL),(29149,'894201076759138304','922098350709280768',NULL,NULL),(29150,'894201076759138304','924405330996105216',NULL,NULL),(29151,'894201076759138304','927138093289443328',NULL,NULL),(29152,'894201076759138304','927138424895311872',NULL,NULL),(29153,'894201076759138304','927189274061705216',NULL,NULL),(29154,'894201076759138304','927514524737605632',NULL,NULL),(29155,'894201076759138304','931332510242054144',NULL,NULL),(29156,'894201076759138304','934820739683454976',NULL,NULL),(29157,'894201076759138304','936943543832809472',NULL,NULL),(29158,'894201076759138304','802852358580080640',NULL,NULL),(29159,'894201076759138304','802918760057933824',NULL,NULL),(29160,'894201076759138304','893816925920956416',NULL,NULL),(29161,'894201076759138304','894896876246011904',NULL,NULL),(29162,'894201076759138304','898334091781345280',NULL,NULL),(29163,'894201076759138304','898334486809284608',NULL,NULL),(29164,'894201076759138304','898336112890613760',NULL,NULL),(29165,'894201076759138304','898337064490110976',NULL,NULL),(29166,'894201076759138304','898337973207044096',NULL,NULL),(29167,'894201076759138304','898338819265269760',NULL,NULL),(29168,'894201076759138304','898339672948740096',NULL,NULL),(29169,'894201076759138304','898340234041757696',NULL,NULL),(29170,'894201076759138304','898919751642779648',NULL,NULL),(29171,'894201076759138304','898922054793826304',NULL,NULL),(29172,'894201076759138304','898923212249763840',NULL,NULL),(29173,'894201076759138304','898923484506230784',NULL,NULL),(29174,'894201076759138304','898924424034521088',NULL,NULL),(29175,'894201076759138304','898925640273629184',NULL,NULL),(29176,'894201076759138304','898992469134282752',NULL,NULL),(29177,'894201076759138304','898994643360813056',NULL,NULL),(29178,'894201076759138304','898996945933045760',NULL,NULL),(29179,'894201076759138304','898999065830756352',NULL,NULL),(29180,'894201076759138304','899001733512957952',NULL,NULL),(29181,'894201076759138304','899002142675701760',NULL,NULL),(29182,'894201076759138304','899076266156625920',NULL,NULL),(29183,'894201076759138304','899079195127189504',NULL,NULL),(29184,'894201076759138304','899080146999316480',NULL,NULL),(29185,'894201076759138304','899083287929032704',NULL,NULL),(29186,'894201076759138304','899810791648137216',NULL,NULL),(29187,'894201076759138304','899814315572334592',NULL,NULL),(29188,'894201076759138304','902710107161235456',NULL,NULL),(29189,'894201076759138304','904015873528827904',NULL,NULL),(29190,'894201076759138304','904052710129537024',NULL,NULL),(29191,'894201076759138304','904053585896017920',NULL,NULL),(29192,'894201076759138304','904055741369815040',NULL,NULL),(29193,'894201076759138304','904071524649013248',NULL,NULL),(29194,'894201076759138304','904118233940889600',NULL,NULL),(29195,'894201076759138304','904135062444838912',NULL,NULL),(29196,'894201076759138304','913949817086939136',NULL,NULL),(29197,'894201076759138304','916458081179668480',NULL,NULL),(29198,'894201076759138304','917919433773551616',NULL,NULL),(29199,'894201076759138304','917937043621810176',NULL,NULL),(29200,'894201076759138304','918294064137244672',NULL,NULL),(29201,'894201076759138304','921829301463027712',NULL,NULL),(29202,'894201076759138304','923706124102799360',NULL,NULL),(29203,'894201076759138304','923714155901358080',NULL,NULL),(29204,'894201076759138304','926763439244709888',NULL,NULL),(29205,'894201076759138304','930966518835974144',NULL,NULL),(29206,'894201076759138304','931918590498574336',NULL,NULL),(29207,'894201076759138304','934899005698084864',NULL,NULL),(29208,'894201076759138304','802919044364636160',NULL,NULL),(29209,'894201076759138304','805372907965386752',NULL,NULL),(29210,'894201076759138304','808616077255774208',NULL,NULL),(29211,'894201076759138304','810825569494110208',NULL,NULL),(29212,'894201076759138304','838335003375964160',NULL,NULL),(29213,'894201076759138304','893817125867622400',NULL,NULL),(29214,'894201076759138304','896164931303378944',NULL,NULL),(29215,'894201076759138304','898334268604813312',NULL,NULL),(29216,'894201076759138304','898336252711931904',NULL,NULL),(29217,'894201076759138304','898337228843913216',NULL,NULL),(29218,'894201076759138304','898338119047188480',NULL,NULL),(29219,'894201076759138304','898338990552256512',NULL,NULL),(29220,'894201076759138304','898339790284394496',NULL,NULL),(29221,'894201076759138304','898340346117754880',NULL,NULL),(29222,'894201076759138304','898919884019208192',NULL,NULL),(29223,'894201076759138304','898924549347741696',NULL,NULL),(29224,'894201076759138304','898925753096212480',NULL,NULL),(29225,'894201076759138304','898992569562697728',NULL,NULL),(29226,'894201076759138304','898994850660093952',NULL,NULL),(29227,'894201076759138304','898997770537406464',NULL,NULL),(29228,'894201076759138304','898999773028159488',NULL,NULL),(29229,'894201076759138304','899076393432780800',NULL,NULL),(29230,'894201076759138304','899079347393007616',NULL,NULL),(29231,'894201076759138304','899081132492984320',NULL,NULL),(29232,'894201076759138304','899083494326538240',NULL,NULL),(29233,'894201076759138304','899810992530132992',NULL,NULL),(29234,'894201076759138304','899814524809383936',NULL,NULL),(29235,'894201076759138304','903063574157463552',NULL,NULL),(29236,'894201076759138304','904016015229194240',NULL,NULL),(29237,'894201076759138304','904056255981555712',NULL,NULL),(29238,'894201076759138304','904118420969099264',NULL,NULL),(29239,'894201076759138304','913949953242435584',NULL,NULL),(29240,'894201076759138304','916458334754705408',NULL,NULL),(29241,'894201076759138304','916768297586790400',NULL,NULL),(29242,'894201076759138304','918646782911582208',NULL,NULL),(29243,'894201076759138304','930966011266469888',NULL,NULL),(29244,'894201076759138304','934899134614212608',NULL,NULL),(29245,'894201076759138304','802876953722884096',NULL,NULL),(29246,'894201076759138304','802919157430489088',NULL,NULL),(29247,'894201076759138304','802931697527033856',NULL,NULL),(29248,'894201076759138304','889247298196869120',NULL,NULL),(29249,'894201076759138304','898336446329393152',NULL,NULL),(29250,'894201076759138304','898337404199374848',NULL,NULL),(29251,'894201076759138304','898338251511697408',NULL,NULL),(29252,'894201076759138304','898339248917188608',NULL,NULL),(29253,'894201076759138304','898339903127949312',NULL,NULL),(29254,'894201076759138304','898341132600086528',NULL,NULL),(29255,'894201076759138304','898923625090912256',NULL,NULL),(29256,'894201076759138304','898924812993302528',NULL,NULL),(29257,'894201076759138304','898926933432078336',NULL,NULL),(29258,'894201076759138304','898992688609628160',NULL,NULL),(29259,'894201076759138304','898995002837831680',NULL,NULL),(29260,'894201076759138304','898998045511782400',NULL,NULL),(29261,'894201076759138304','898999958684831744',NULL,NULL),(29262,'894201076759138304','899076708504702976',NULL,NULL),(29263,'894201076759138304','899079618982580224',NULL,NULL),(29264,'894201076759138304','899081270330396672',NULL,NULL),(29265,'894201076759138304','899083652330164224',NULL,NULL),(29266,'894201076759138304','899811121018441728',NULL,NULL),(29267,'894201076759138304','899814663515017216',NULL,NULL),(29268,'894201076759138304','903063690486484992',NULL,NULL),(29269,'894201076759138304','904060028510539776',NULL,NULL),(29270,'894201076759138304','904065292424974336',NULL,NULL),(29271,'894201076759138304','904118636598267904',NULL,NULL),(29272,'894201076759138304','913950163700027392',NULL,NULL),(29273,'894201076759138304','916458466950778880',NULL,NULL),(29274,'894201076759138304','917918003188731904',NULL,NULL),(29275,'894201076759138304','918269583171784704',NULL,NULL),(29276,'894201076759138304','918646874980749312',NULL,NULL),(29277,'894201076759138304','926763179978002432',NULL,NULL),(29278,'894201076759138304','927194809708318720',NULL,NULL),(29279,'894201076759138304','927514031458095104',NULL,NULL),(29280,'894201076759138304','929314447095238656',NULL,NULL),(29281,'894201076759138304','934899250242785280',NULL,NULL),(29282,'894201076759138304','936943441596649472',NULL,NULL),(29283,'894201076759138304','802930870510948352',NULL,NULL),(29284,'894201076759138304','839059609225269248',NULL,NULL),(29285,'894201076759138304','898335485523398656',NULL,NULL),(29286,'894201076759138304','898995219188420608',NULL,NULL),(29287,'894201076759138304','898998299615301632',NULL,NULL),(29288,'894201076759138304','899000154885984256',NULL,NULL),(29289,'894201076759138304','899076830349234176',NULL,NULL),(29290,'894201076759138304','899081439826415616',NULL,NULL),(29291,'894201076759138304','899804862013771776',NULL,NULL),(29292,'894201076759138304','899807422577643520',NULL,NULL),(29293,'894201076759138304','899808823739420672',NULL,NULL),(29294,'894201076759138304','899811229017575424',NULL,NULL),(29295,'894201076759138304','899811720153796608',NULL,NULL),(29296,'894201076759138304','899814772944408576',NULL,NULL),(29297,'894201076759138304','899815335576735744',NULL,NULL),(29298,'894201076759138304','902346927830470656',NULL,NULL),(29299,'894201076759138304','904050101738016768',NULL,NULL),(29300,'894201076759138304','904063054986088448',NULL,NULL),(29301,'894201076759138304','904068373158039552',NULL,NULL),(29302,'894201076759138304','904069900111187968',NULL,NULL),(29303,'894201076759138304','904070687604346880',NULL,NULL),(29304,'894201076759138304','904118843020939264',NULL,NULL),(29305,'894201076759138304','904875825113862144',NULL,NULL),(29306,'894201076759138304','913955680715542528',NULL,NULL),(29307,'894201076759138304','916458561943375872',NULL,NULL),(29308,'894201076759138304','918646989531385856',NULL,NULL),(29309,'894201076759138304','927198208109580288',NULL,NULL),(29310,'894201076759138304','934899352286007296',NULL,NULL),(29311,'894201076759138304','802931116691427328',NULL,NULL),(29312,'894201076759138304','802932355596554240',NULL,NULL),(29313,'894201076759138304','891283647431184384',NULL,NULL),(29314,'894201076759138304','898995513087496192',NULL,NULL),(29315,'894201076759138304','899000274331373568',NULL,NULL),(29316,'894201076759138304','899077318159372288',NULL,NULL),(29317,'894201076759138304','899794938370199552',NULL,NULL),(29318,'894201076759138304','899805009363865600',NULL,NULL),(29319,'894201076759138304','899807641243488256',NULL,NULL),(29320,'894201076759138304','899811331564113920',NULL,NULL),(29321,'894201076759138304','904057211481755648',NULL,NULL),(29322,'894201076759138304','904070354979262464',NULL,NULL),(29323,'894201076759138304','904120248553181184',NULL,NULL),(29324,'894201076759138304','927201827349336064',NULL,NULL),(29325,'894201076759138304','934926419119575040',NULL,NULL),(29326,'894201076759138304','802932548165439488',NULL,NULL),(29327,'894201076759138304','805431924678987776',NULL,NULL),(29328,'894201076759138304','839152432125579264',NULL,NULL),(29329,'894201076759138304','899000507769556992',NULL,NULL),(29330,'894201076759138304','899794270502785024',NULL,NULL),(29331,'894201076759138304','899796100964159488',NULL,NULL),(29332,'894201076759138304','899797834717466624',NULL,NULL),(29333,'894201076759138304','904012775699779584',NULL,NULL),(29334,'894201076759138304','904047129859723264',NULL,NULL),(29335,'894201076759138304','906703041556647936',NULL,NULL),(29336,'894201076759138304','927202104341172224',NULL,NULL),(29337,'894201076759138304','927224599626649600',NULL,NULL),(29338,'894201076759138304','936723230184443904',NULL,NULL),(29339,'894201076759138304','858320387178500096',NULL,NULL),(29340,'894201076759138304','899077560292347904',NULL,NULL),(29341,'894201076759138304','899799748200894464',NULL,NULL),(29342,'894201076759138304','899805166738345984',NULL,NULL),(29343,'894201076759138304','904058054746574848',NULL,NULL),(29344,'894201076759138304','904065881678548992',NULL,NULL),(29345,'894201076759138304','904073001346011136',NULL,NULL),(29346,'894201076759138304','918634471165530112',NULL,NULL),(29347,'894201076759138304','802933021148712960',NULL,NULL),(29348,'894201076759138304','809886572093640704',NULL,NULL),(29349,'894201076759138304','904066172054409216',NULL,NULL),(29350,'894201076759138304','913940784099627008',NULL,NULL),(29351,'894201076759138304','919324529707192320',NULL,NULL),(29352,'894201076759138304','922626072145563648',NULL,NULL),(29353,'894201076759138304','904066367945183232',NULL,NULL),(29354,'894201076759138304','916107476942721024',NULL,NULL),(29355,'894201076759138304','934820298253930496',NULL,NULL),(29356,'894201076759138304','805193674668380160',NULL,NULL),(29357,'894201076759138304','805531084459610112',NULL,NULL),(29358,'894201076759138304','810817759893000192',NULL,NULL),(29359,'894201076759138304','802932890089295872',NULL,NULL),(29360,'894201076759138304','802933165302747136',NULL,NULL),(29361,'894201076759138304','830556376391487488',NULL,NULL),(29362,'894201076759138304','924405157259644928',NULL,NULL),(29363,'894201076759138304','903065878872985600',NULL,NULL),(29364,'894201076759138304','927681598093004800',NULL,NULL),(29365,'894201076759138304','904015493940121600',NULL,NULL),(29366,'894201076759138304','899793046982365184',NULL,NULL),(29367,'894201076759138304','899796275644338176',NULL,NULL),(29368,'894201076759138304','899796487305695232',NULL,NULL),(29369,'894201076759138304','899796627491917824',NULL,NULL),(29370,'894201076759138304','899796760371662848',NULL,NULL),(29371,'894201076759138304','899796895541497856',NULL,NULL),(29372,'894201076759138304','934940905796800512',NULL,NULL),(29373,'894201076759138304','936943806161358848',NULL,NULL),(29374,'894201076759138304','936943897400053760',NULL,NULL),(29375,'894201076759138304','936943979792961536',NULL,NULL),(29376,'894201076759138304','936944089763418112',NULL,NULL),(29377,'894201076759138304','936956093823717376',NULL,NULL),(29378,'894201076759138304','937123552732123136',NULL,NULL),(29379,'894201076759138304','938782846959489024',NULL,NULL),(29380,'894201076759138304','938783170151583744',NULL,NULL),(29381,'894201076759138304','938783478181269504',NULL,NULL),(29382,'894201076759138304','938783882944188416',NULL,NULL),(29383,'894201076759138304','938784014171377664',NULL,NULL),(29384,'894201076759138304','938784115174412288',NULL,NULL),(29385,'894201076759138304','938784216877895680',NULL,NULL),(29386,'894201076759138304','938784333840257024',NULL,NULL),(29387,'894201076759138304','938785006883442688',NULL,NULL),(29388,'894201076759138304','938785227138928640',NULL,NULL),(29389,'894201076759138304','938785661765292032',NULL,NULL),(29390,'894201076759138304','938785831097733120',NULL,NULL),(29391,'894201076759138304','938785942548779008',NULL,NULL),(29392,'894201076759138304','938786051592294400',NULL,NULL),(29393,'894201076759138304','938879140692496384',NULL,NULL),(29394,'894201076759138304','938879269428269056',NULL,NULL),(29395,'894201076759138304','939102606079299584',NULL,NULL),(29396,'894201076759138304','939102720999034880',NULL,NULL),(29397,'894201076759138304','939102954936340480',NULL,NULL),(29398,'894201076759138304','939103070598467584',NULL,NULL),(29399,'894201076759138304','939103467824222208',NULL,NULL),(29400,'894201076759138304','939103572845400064',NULL,NULL),(29401,'894201076759138304','939103663475920896',NULL,NULL),(29402,'894201076759138304','939113343006806016',NULL,NULL),(29403,'894201076759138304','939830350585008128',NULL,NULL),(29404,'894201076759138304','939848526425231360',NULL,NULL);
/*!40000 ALTER TABLE `role_resource_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-06 18:33:45


DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;

CREATE TABLE QRTZ_JOB_DETAILS(
SCHED_NAME VARCHAR(120) NOT NULL,
JOB_NAME VARCHAR(200) NOT NULL,
JOB_GROUP VARCHAR(200) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
JOB_CLASS_NAME VARCHAR(250) NOT NULL,
IS_DURABLE VARCHAR(1) NOT NULL,
IS_NONCONCURRENT VARCHAR(1) NOT NULL,
IS_UPDATE_DATA VARCHAR(1) NOT NULL,
REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
JOB_NAME VARCHAR(200) NOT NULL,
JOB_GROUP VARCHAR(200) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
NEXT_FIRE_TIME BIGINT(13) NULL,
PREV_FIRE_TIME BIGINT(13) NULL,
PRIORITY INTEGER NULL,
TRIGGER_STATE VARCHAR(16) NOT NULL,
TRIGGER_TYPE VARCHAR(8) NOT NULL,
START_TIME BIGINT(13) NOT NULL,
END_TIME BIGINT(13) NULL,
CALENDAR_NAME VARCHAR(200) NULL,
MISFIRE_INSTR SMALLINT(2) NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
REPEAT_COUNT BIGINT(7) NOT NULL,
REPEAT_INTERVAL BIGINT(12) NOT NULL,
TIMES_TRIGGERED BIGINT(10) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_CRON_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
CRON_EXPRESSION VARCHAR(120) NOT NULL,
TIME_ZONE_ID VARCHAR(80),
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_BLOB_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
BLOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
INDEX (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_CALENDARS (
SCHED_NAME VARCHAR(120) NOT NULL,
CALENDAR_NAME VARCHAR(200) NOT NULL,
CALENDAR BLOB NOT NULL,
PRIMARY KEY (SCHED_NAME,CALENDAR_NAME))
ENGINE=InnoDB;

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_FIRED_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
ENTRY_ID VARCHAR(95) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
INSTANCE_NAME VARCHAR(200) NOT NULL,
FIRED_TIME BIGINT(13) NOT NULL,
SCHED_TIME BIGINT(13) NOT NULL,
PRIORITY INTEGER NOT NULL,
STATE VARCHAR(16) NOT NULL,
JOB_NAME VARCHAR(200) NULL,
JOB_GROUP VARCHAR(200) NULL,
IS_NONCONCURRENT VARCHAR(1) NULL,
REQUESTS_RECOVERY VARCHAR(1) NULL,
PRIMARY KEY (SCHED_NAME,ENTRY_ID))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SCHEDULER_STATE (
SCHED_NAME VARCHAR(120) NOT NULL,
INSTANCE_NAME VARCHAR(200) NOT NULL,
LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
CHECKIN_INTERVAL BIGINT(13) NOT NULL,
PRIMARY KEY (SCHED_NAME,INSTANCE_NAME))
ENGINE=InnoDB;

-- 2022-01-16;
alter table QRTZ_SCHEDULER_STATE add column STATUS varchar(16) not null default 'online' comment '状态，下线offline,上线online';

CREATE TABLE QRTZ_LOCKS (
SCHED_NAME VARCHAR(120) NOT NULL,
LOCK_NAME VARCHAR(40) NOT NULL,
PRIMARY KEY (SCHED_NAME,LOCK_NAME))
ENGINE=InnoDB;

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY ON QRTZ_JOB_DETAILS(SCHED_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP ON QRTZ_JOB_DETAILS(SCHED_NAME,JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J ON QRTZ_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG ON QRTZ_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C ON QRTZ_TRIGGERS(SCHED_NAME,CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME ON QRTZ_TRIGGERS(SCHED_NAME,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
-- MySQL dump 10.16  Distrib 10.2.14-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: zdh
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
-- Table structure for table `QRTZ_BLOB_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_BLOB_TRIGGERS`
--

LOCK TABLES `QRTZ_BLOB_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_BLOB_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_BLOB_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_CALENDARS`
--

DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_CALENDARS`
--

LOCK TABLES `QRTZ_CALENDARS` WRITE;
/*!40000 ALTER TABLE `QRTZ_CALENDARS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_CALENDARS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_CRON_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_CRON_TRIGGERS`
--

LOCK TABLES `QRTZ_CRON_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_CRON_TRIGGERS` DISABLE KEYS */;
INSERT INTO `QRTZ_CRON_TRIGGERS` VALUES ('schedulerFactoryBean','906547945187315712','DEFAULT','0 0 0 * * ? *','Asia/Shanghai');
/*!40000 ALTER TABLE `QRTZ_CRON_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_FIRED_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(20) NOT NULL,
  `SCHED_TIME` bigint(20) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_FIRED_TRIGGERS`
--

LOCK TABLES `QRTZ_FIRED_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_FIRED_TRIGGERS` DISABLE KEYS */;
INSERT INTO `QRTZ_FIRED_TRIGGERS` VALUES ('schedulerFactoryBean','izm5eexscw3e0firjnqdlzz16362787806531636278781595','906543220572295169','retry','izm5eexscw3e0firjnqdlzz1636278780653',1636279401669,1636279402609,5,'ACQUIRED',NULL,NULL,'0','0');
/*!40000 ALTER TABLE `QRTZ_FIRED_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_JOB_DETAILS`
--

DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_JOB_DETAILS`
--

LOCK TABLES `QRTZ_JOB_DETAILS` WRITE;
/*!40000 ALTER TABLE `QRTZ_JOB_DETAILS` DISABLE KEYS */;
INSERT INTO `QRTZ_JOB_DETAILS` VALUES ('schedulerFactoryBean','906543220496797697','email','','com.zyc.zdh.job.MyJobBean','0','1','1','1','#\n#Sat Nov 06 13:59:06 CST 2021\n'),('schedulerFactoryBean','906543220572295169','retry','','com.zyc.zdh.job.MyJobBean','0','1','1','1','#\n#Sat Nov 06 13:59:06 CST 2021\n'),('schedulerFactoryBean','906543220626821121','check','','com.zyc.zdh.job.MyJobBean','0','1','1','1','#\n#Sat Nov 06 13:59:06 CST 2021\n'),('schedulerFactoryBean','906547945187315712','DEFAULT','测试专用','com.zyc.zdh.job.MyJobBean','0','1','1','0','#\n#Sat Nov 06 23:34:43 CST 2021\n');
/*!40000 ALTER TABLE `QRTZ_JOB_DETAILS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_LOCKS`
--

DROP TABLE IF EXISTS `QRTZ_LOCKS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_LOCKS`
--

LOCK TABLES `QRTZ_LOCKS` WRITE;
/*!40000 ALTER TABLE `QRTZ_LOCKS` DISABLE KEYS */;
INSERT INTO `QRTZ_LOCKS` VALUES ('schedulerFactoryBean','STATE_ACCESS'),('schedulerFactoryBean','TRIGGER_ACCESS');
/*!40000 ALTER TABLE `QRTZ_LOCKS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

LOCK TABLES `QRTZ_PAUSED_TRIGGER_GRPS` WRITE;
/*!40000 ALTER TABLE `QRTZ_PAUSED_TRIGGER_GRPS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_PAUSED_TRIGGER_GRPS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_SCHEDULER_STATE`
--

DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(20) NOT NULL,
  `CHECKIN_INTERVAL` bigint(20) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_SCHEDULER_STATE`
--

LOCK TABLES `QRTZ_SCHEDULER_STATE` WRITE;
/*!40000 ALTER TABLE `QRTZ_SCHEDULER_STATE` DISABLE KEYS */;
INSERT INTO `QRTZ_SCHEDULER_STATE` VALUES ('schedulerFactoryBean','izm5eexscw3e0firjnqdlzz1636278780653',1636279384740,30000);
/*!40000 ALTER TABLE `QRTZ_SCHEDULER_STATE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_SIMPLE_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(20) NOT NULL,
  `REPEAT_INTERVAL` bigint(20) NOT NULL,
  `TIMES_TRIGGERED` bigint(20) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_SIMPLE_TRIGGERS`
--

LOCK TABLES `QRTZ_SIMPLE_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_SIMPLE_TRIGGERS` DISABLE KEYS */;
INSERT INTO `QRTZ_SIMPLE_TRIGGERS` VALUES ('schedulerFactoryBean','906543220496797697','email',-1,15000,6738),('schedulerFactoryBean','906543220572295169','retry',-1,1000,101056),('schedulerFactoryBean','906543220626821121','check',-1,5000,20212);
/*!40000 ALTER TABLE `QRTZ_SIMPLE_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_SIMPROP_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_SIMPROP_TRIGGERS`
--

LOCK TABLES `QRTZ_SIMPROP_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_SIMPROP_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_SIMPROP_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(20) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(20) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(20) NOT NULL,
  `END_TIME` bigint(20) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(6) DEFAULT NULL,
  `JOB_DATA` blob DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_TRIGGERS`
--

LOCK TABLES `QRTZ_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_TRIGGERS` DISABLE KEYS */;
INSERT INTO `QRTZ_TRIGGERS` VALUES ('schedulerFactoryBean','906543220496797697','email','906543220496797697','email',NULL,1636279416574,1636279401574,5,'WAITING','SIMPLE',1636178346574,0,NULL,4,'#\n#Sat Nov 06 13:59:06 CST 2021\ntask_id=906543220496797697\n'),('schedulerFactoryBean','906543220572295169','retry','906543220572295169','retry',NULL,1636279402609,1636279401609,5,'ACQUIRED','SIMPLE',1636178346609,0,NULL,4,'#\n#Sat Nov 06 13:59:06 CST 2021\ntask_id=906543220572295169\n'),('schedulerFactoryBean','906543220626821121','check','906543220626821121','check',NULL,1636279406638,1636279401638,5,'WAITING','SIMPLE',1636178346638,0,NULL,4,'#\n#Sat Nov 06 13:59:06 CST 2021\ntask_id=906543220626821121\n'),('schedulerFactoryBean','906547945187315712','DEFAULT','906547945187315712','DEFAULT',NULL,1636300800000,1636214400000,5,'WAITING','CRON',1636212883000,0,NULL,2,'#\n#Sat Nov 06 23:34:43 CST 2021\ntask_id=906547945187315712\n');
/*!40000 ALTER TABLE `QRTZ_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

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
  `enable` varchar(8) DEFAULT 'false' COMMENT '是否启用true/false',
  `user_group` varchar(8) DEFAULT '' COMMENT '用户所在组',
  `roles` text DEFAULT NULL COMMENT '角色列表',
  `signature` varchar(64) DEFAULT NULL COMMENT '签名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_info`
--

LOCK TABLES `account_info` WRITE;
/*!40000 ALTER TABLE `account_info` DISABLE KEYS */;
INSERT INTO `account_info` VALUES (1,'zyc','123456','1209687056@qq.com','on','',NULL,'true','6','894201076759138304,894254232000008192','abc'),(2,'admin','123456','1209687056@qq.com',NULL,'',NULL,'true','8','894201076759138304,894254232000008192',''),(3,'carl','Hhz6NcEiGYvG6Bk','carl.don.it@gmail.com',NULL,'',NULL,'true','','898997645559730176',''),(4,'kelaode','123456','550426843@qq.com',NULL,'',NULL,'true','','898997645559730176',''),(5,'roohom','roohom123','roohom@qq.com',NULL,'',NULL,'true','','898997645559730176',''),(6,'yfy','yfyyfy','869687428@qq.com',NULL,'',NULL,'true','','898997645559730176',''),(7,'yy','123','yy@163.com',NULL,NULL,NULL,'true','','898997645559730176',NULL),(8,'hongyg','aaaaaa','hongyonggan@126.com',NULL,NULL,NULL,'true','','898997645559730176',NULL),(9,'root','root','624554039@qq.com',NULL,NULL,NULL,'true','','898997645559730176',NULL),(14,'abc','123456','1209687056@qq.com','on','','on','true','','898997645559730176',NULL);
/*!40000 ALTER TABLE `account_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `apply_info`
--

DROP TABLE IF EXISTS `apply_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `apply_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `apply_context` varchar(500) DEFAULT NULL,
  `issue_id` varchar(200) DEFAULT NULL COMMENT '数据发布id',
  `approve_id` varchar(200) DEFAULT NULL COMMENT '审批人id',
  `status` varchar(10) DEFAULT NULL COMMENT '状态0:初始态,1:通过,2:未通过',
  `create_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `reason` text DEFAULT NULL COMMENT '原因',
  `is_notice` varchar(8) NOT NULL DEFAULT 'false' COMMENT '是否已经通知true/false',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=903085573202251777 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apply_info`
--

LOCK TABLES `apply_info` WRITE;
/*!40000 ALTER TABLE `apply_info` DISABLE KEYS */;
INSERT INTO `apply_info` VALUES (838713411268251648,'account_info','7','1','1','2021-05-03 01:47:40','2021-05-03 01:47:40','2',NULL,'false'),(839147351305097216,'zdh_logs','8','2','1','2021-05-04 06:31:59','2021-05-04 06:31:59','1',NULL,'false'),(853599398993596416,'hive_t1','10','1','1','2021-06-13 03:39:16','2021-06-13 03:39:16','2',NULL,'false'),(903082514808049664,'account_info','903076295573770240','1','1','2021-10-27 16:47:30','2021-10-27 16:47:30','2',NULL,'false'),(903085573202251776,'account_info','903076295573770240','1','0','2021-10-27 16:59:39','2021-10-27 16:59:39','2',NULL,'true');
/*!40000 ALTER TABLE `apply_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `approval_auditor_info`
--

DROP TABLE IF EXISTS `approval_auditor_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `approval_auditor_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL DEFAULT '0' COMMENT '审批流程code',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT '审批节点',
  `auditor_id` varchar(64) NOT NULL DEFAULT '' COMMENT '审批人id',
  `auditor_group` varchar(64) NOT NULL DEFAULT '' COMMENT '审批人所在组',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;
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
-- Table structure for table `approval_config_info`
--

DROP TABLE IF EXISTS `approval_config_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `approval_config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL DEFAULT '0' COMMENT '审批流程code',
  `code_name` varchar(128) NOT NULL DEFAULT '' COMMENT '审批流程名称',
  `type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0 单人审批；1 多人审批。单人审批，意思是同一级审批只要有审批人审批后，其他人默认审批。多人审批，必须是同一级所以人审批，才进行下一步审批节点',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `employee_id` varchar(64) NOT NULL DEFAULT '' COMMENT '发布人id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
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
-- Table structure for table `approval_event_info`
--

DROP TABLE IF EXISTS `approval_event_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `approval_event_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL DEFAULT '0' COMMENT '审批流程code',
  `event_code` varchar(64) NOT NULL DEFAULT '' COMMENT '事件code',
  `event_context` varchar(128) NOT NULL DEFAULT '' COMMENT '事件说明',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `approval_event_info`
--

LOCK TABLES `approval_event_info` WRITE;
/*!40000 ALTER TABLE `approval_event_info` DISABLE KEYS */;
INSERT INTO `approval_event_info` VALUES (1,'data','data_pub','数据发布事件',NULL),(2,'data','data_apply','数据审批','2021-10-12 15:02:09');
/*!40000 ALTER TABLE `approval_event_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data_sources_info`
--

DROP TABLE IF EXISTS `data_sources_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_sources_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_source_context` varchar(100) DEFAULT NULL COMMENT '数据源说明',
  `data_source_type` varchar(100) DEFAULT NULL COMMENT '数据源类型',
  `driver` varchar(100) DEFAULT NULL COMMENT '驱动连接串',
  `url` varchar(100) DEFAULT NULL COMMENT '连接url',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `is_delete` varchar(10) NOT NULL DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_sources_info`
--

LOCK TABLES `data_sources_info` WRITE;
/*!40000 ALTER TABLE `data_sources_info` DISABLE KEYS */;
INSERT INTO `data_sources_info` VALUES (53,'mydb','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','2','0'),(54,'csv','HDFS','','','zyc@qq.com','123456','2','0'),(55,'mydb2','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','2','0'),(56,'HIVE1','HIVE','','','','','2','0'),(57,'第一个hive','HIVE','','','','','1','0'),(58,'mydb','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','1','0'),(59,'本地HDFS','HDFS','','','','','1','0'),(60,'zdh_test','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/zdh_test?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','1','0'),(61,'个人测试外部下载','外部下载','','','','','1','0'),(62,'本地kafka','KAFKA','','127.0.0.1:9092','','','1','0'),(63,'第一个clickhouse','JDBC','com.github.housepower.jdbc.ClickHouseDriver','jdbc:clickhouse://192.168.110.10:9000/datasets','default','','1','0'),(64,'第一个外部上传','外部上传','','','','','1','0'),(65,'第一个sftp','SFTP','','47.105.50.94:22','zyc','123456','1','0'),(66,'第一个tidb','TIDB','','192.168.110.10:4000','root','','1','0'),(67,'dddd','JDBC','dddd','dddd','','','1','1'),(68,'第一个iceberg','ICEBERG','','','','','1','0'),(69,'11-oracle','JDBC','oracle.jdbc.driver.OracleDriver','jdbc:oracle:thin:@192.168.110.11:1521:XE','zyc','123456','1','0'),(70,'测试a','HDFS','aaaa','','','','1','1'),(71,'afafa','HDFS','','','','','1','1'),(72,'aaaa','HIVE','','','','','1','1'),(73,'sdffasdf','HDFS','','','','','1','1');
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
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_sources_type_info`
--

LOCK TABLES `data_sources_type_info` WRITE;
/*!40000 ALTER TABLE `data_sources_type_info` DISABLE KEYS */;
INSERT INTO `data_sources_type_info` VALUES (1,'JDBC'),(2,'HDFS'),(3,'HBASE'),(4,'MONGODB'),(5,'ES'),(6,'HIVE'),(7,'KAFKA'),(8,'HTTP'),(9,'REDIS'),(10,'CASSANDRA'),(11,'SFTP'),(12,'KUDU'),(13,'外部上传'),(14,'FLUME'),(15,'外部下载'),(16,'TIDB'),(17,'ICEBERG');
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
-- Table structure for table `etl_apply_task_info`
--

DROP TABLE IF EXISTS `etl_apply_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `etl_apply_task_info` (
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
  `is_delete` varchar(10) NOT NULL DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `repartition_num_input` varchar(64) NOT NULL DEFAULT '' COMMENT '洗牌个数默认空',
  `repartition_cols_input` varchar(256) NOT NULL DEFAULT '' COMMENT '洗牌字段默认空',
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=853958150766727169 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_apply_task_info`
--

LOCK TABLES `etl_apply_task_info` WRITE;
/*!40000 ALTER TABLE `etl_apply_task_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `etl_apply_task_info` ENABLE KEYS */;
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
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=749279630074056705 DEFAULT CHARSET=utf8mb4;
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
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_more_task_info`
--

LOCK TABLES `etl_more_task_info` WRITE;
/*!40000 ALTER TABLE `etl_more_task_info` DISABLE KEYS */;
INSERT INTO `etl_more_task_info` VALUES (1,'ddd','719619870378954752,719629297702146048','','59','HDFS','','','','','1','2020-06-08 14:19:59','','csv','UTF-8','|','','','','-1'),(2,'more_mydb_account_info','749279343477264384','select * from account_info','59','HDFS','','/data/output/account_info','','','1','2020-08-29 06:49:19','','csv','UTF-8','|',NULL,'overwrite','user_name','2'),(3,'优化多源任务测试','719619870378954752','--table 单分割符无标题=dt1;\r\nselect a.name,a.sex  from dt1 a\r\n','59','HDFS','','/data/output/dt1','','','1','2021-06-21 14:37:20','','csv','UTF-8',',','true','overwrite','','-1');
/*!40000 ALTER TABLE `etl_more_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etl_task_flink_info`
--

DROP TABLE IF EXISTS `etl_task_flink_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `etl_task_flink_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sql_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_params_input` varchar(500) DEFAULT NULL COMMENT '输入参数',
  `etl_sql` text DEFAULT NULL COMMENT 'sql任务处理逻辑',
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
  `command` text DEFAULT NULL COMMENT '命令',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=904464410402099201 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_task_flink_info`
--

LOCK TABLES `etl_task_flink_info` WRITE;
/*!40000 ALTER TABLE `etl_task_flink_info` DISABLE KEYS */;
INSERT INTO `etl_task_flink_info` VALUES (858391270026907648,'第一个FLINK_SQL','','CREATE TABLE user_log (\r\n  user_id VARCHAR\r\n) WITH (\r\n\'connector.type\' = \'kafka\',\r\n\'connector.version\' = \'universal\',\r\n\'connector.topic\' = \'m1\',\r\n\'connector.startup-mode\' = \'latest-offset\',\r\n\'connector.properties.0.key\' = \'zookeeper.connect\',\r\n\'connector.properties.0.value\' = \'localhost:2181\',\r\n\'connector.properties.1.key\' = \'bootstrap.servers\',\r\n\'connector.properties.1.value\' = \'localhost:9092\',\r\n\'update-mode\' = \'append\',\r\n\'format.type\' = \'csv\'\r\n);\r\n  \r\n CREATE TABLE t1 (\r\n user_id VARCHAR\r\n ) WITH (\r\n \'connector.type\' = \'jdbc\',\r\n \'connector.url\' = \'jdbc:mysql://192.168.110.1:3306/flink_test?serverTimezone=GMT%2B8\',\r\n \'connector.table\' = \'t1\',\r\n \'connector.username\' = \'zyc\',\r\n \'connector.password\' = \'123456\',\r\n \'connector.write.flush.max-rows\' = \'1\'\r\n );\r\n \r\nINSERT INTO t1 SELECT user_id FROM user_log\r\n','1','2021-06-26 09:00:27',NULL,'','','','','192.168.110.10','22','zyc','123456','hdfs://192.168.110.10/f1',NULL,NULL),(886575336517537792,'第二个FLINK_SQL','',' CREATE TABLE d1 (\r\n addr VARCHAR,\r\n age VARCHAR,\r\n dsts VARCHAR,\r\n job VARCHAR,\r\n name VARCHAR,\r\n sex VARCHAR\r\n ) WITH (\r\n \'connector.type\' = \'jdbc\',\r\n \'connector.url\' = \'jdbc:mysql://192.168.110.1:3306/zdh_test?serverTimezone=GMT%2B8\',\r\n \'connector.table\' = \'d1\',\r\n \'connector.username\' = \'zyc\',\r\n \'connector.password\' = \'123456\',\r\n \'connector.write.flush.max-rows\' = \'1\'\r\n );\r\n  \r\n CREATE TABLE t1 (\r\n user_id VARCHAR\r\n ) WITH (\r\n \'connector.type\' = \'jdbc\',\r\n \'connector.url\' = \'jdbc:mysql://192.168.110.1:3306/flink_test?serverTimezone=GMT%2B8\',\r\n \'connector.table\' = \'t1\',\r\n \'connector.username\' = \'zyc\',\r\n \'connector.password\' = \'123456\',\r\n \'connector.write.flush.max-rows\' = \'1\'\r\n );\r\n \r\nINSERT INTO t1 SELECT age FROM d1\r\n','1','2021-09-12 03:33:52',NULL,'','','','','192.168.110.10','22','zyc','123456','',NULL,NULL),(904327604331352064,'第三个FLINK_SQL_CDC','','CREATE TABLE mysql_binlog (\r\n id int not null,\r\n job_id STRING,\r\n log_time TIMESTAMP,\r\n msg STRING,\r\n level STRING,\r\n primary key(id)  NOT ENFORCED\r\n) WITH (\r\n \'connector\' = \'mysql-cdc\',\r\n \'hostname\' = \'192.168.110.1\',\r\n \'port\' = \'3306\',\r\n \'username\' = \'zyc\',\r\n \'password\' = \'123456\',\r\n \'database-name\' = \'zdh_test\',\r\n \'table-name\' = \'zdh_logs\',\r\n \'server-time-zone\' = \'Asia/Shanghai\'\r\n);\r\n\r\nCREATE TABLE zdh_logs2 (\r\n id int not null,\r\n job_id STRING,\r\n log_time TIMESTAMP,\r\n msg STRING,\r\n level STRING,\r\n primary key(id)  NOT ENFORCED\r\n) WITH (\r\n \'connector\' = \'jdbc\',\r\n \'url\' = \'jdbc:mysql://192.168.110.1:3306/zdh_test?serverTimezone=GMT%2B8\',\r\n \'username\' = \'zyc\',\r\n \'password\' = \'123456\',\r\n \'table-name\' = \'zdh_logs2\'\r\n);\r\n\r\ninsert into zdh_logs2 (id,job_id,log_time,msg,level)\r\nselect \r\n  id,job_id,log_time,msg,level\r\nfrom mysql_binlog','1','2021-10-31 03:15:02',NULL,'','','','','192.168.110.10','22','zyc','123456','',NULL,NULL),(904448033733742592,'第二个FLINK_SQL_Window版本','',' CREATE TABLE d1 (\r\n addr VARCHAR,\r\n age VARCHAR,\r\n dsts VARCHAR,\r\n job VARCHAR,\r\n name VARCHAR,\r\n sex VARCHAR\r\n ) WITH (\r\n \'connector.type\' = \'jdbc\',\r\n \'connector.url\' = \'jdbc:mysql://192.168.110.1:3306/zdh_test?serverTimezone=GMT%2B8\',\r\n \'connector.table\' = \'d1\',\r\n \'connector.username\' = \'zyc\',\r\n \'connector.password\' = \'123456\',\r\n \'connector.write.flush.max-rows\' = \'1\'\r\n );\r\n  \r\n CREATE TABLE t1 (\r\n user_id VARCHAR\r\n ) WITH (\r\n \'connector.type\' = \'jdbc\',\r\n \'connector.url\' = \'jdbc:mysql://192.168.110.1:3306/flink_test?serverTimezone=GMT%2B8\',\r\n \'connector.table\' = \'t1\',\r\n \'connector.username\' = \'zyc\',\r\n \'connector.password\' = \'123456\',\r\n \'connector.write.flush.max-rows\' = \'1\'\r\n );\r\n \r\nINSERT INTO t1 SELECT age FROM d1\r\n','1','2021-10-31 11:13:35',NULL,'','','','','192.168.31.81','22','ADMINISTRATOR','199517','','windows','cd /FirefoxDownload/flink-1.12.4-bin-scala_2.11/flink-1.12.4/bin  && flink.bat run -c com.zyc.SystemInit -p 1 /home/zyc/zdh_flink.jar {{zdh_task_log_id}} --zdh_config=/home/zyc/conf FLINK_CONF_DIR= D:/flink-1.12.4/conf'),(904464410402099200,'第三个FLINK_SQL_CDC_Windows版本','','CREATE TABLE mysql_binlog (\r\n id int not null,\r\n job_id STRING,\r\n log_time TIMESTAMP,\r\n msg STRING,\r\n level STRING,\r\n primary key(id)  NOT ENFORCED\r\n) WITH (\r\n \'connector\' = \'mysql-cdc\',\r\n \'hostname\' = \'192.168.110.1\',\r\n \'port\' = \'3306\',\r\n \'username\' = \'zyc\',\r\n \'password\' = \'123456\',\r\n \'database-name\' = \'zdh_test\',\r\n \'table-name\' = \'zdh_logs\',\r\n \'server-time-zone\' = \'Asia/Shanghai\'\r\n);\r\n\r\nCREATE TABLE zdh_logs2 (\r\n id int not null,\r\n job_id STRING,\r\n log_time TIMESTAMP,\r\n msg STRING,\r\n level STRING,\r\n primary key(id)  NOT ENFORCED\r\n) WITH (\r\n \'connector\' = \'jdbc\',\r\n \'url\' = \'jdbc:mysql://192.168.110.1:3306/zdh_test?serverTimezone=GMT%2B8\',\r\n \'username\' = \'zyc\',\r\n \'password\' = \'123456\',\r\n \'table-name\' = \'zdh_logs2\'\r\n);\r\n\r\ninsert INTO zdh_logs2 (id,job_id,log_time,msg,level)\r\nselect \r\n  id,job_id,log_time,msg,level\r\nfrom mysql_binlog','1','2021-10-31 12:18:40',NULL,'','','','','192.168.31.81','22','ADMINISTRATOR','199517','','windows','cd /FirefoxDownload/flink-1.12.4-bin-scala_2.11/flink-1.12.4/bin  && flink.bat run -c com.zyc.SystemInit -p 1 /home/zyc/zdh_flink.jar {{zdh_task_log_id}} --zdh_config=/home/zyc/conf FLINK_CONF_DIR= D:/flink-1.12.4/conf');
/*!40000 ALTER TABLE `etl_task_flink_info` ENABLE KEYS */;
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
  `repartition_num_input` varchar(64) NOT NULL DEFAULT '' COMMENT '洗牌个数默认空',
  `repartition_cols_input` varchar(256) NOT NULL DEFAULT '' COMMENT '洗牌字段默认空',
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=889473341604237313 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_task_info`
--

LOCK TABLES `etl_task_info` WRITE;
/*!40000 ALTER TABLE `etl_task_info` DISABLE KEYS */;
INSERT INTO `etl_task_info` VALUES (719619870378954752,'单分割符无标题','59','HDFS','','/data/csv/t1.txt','name,sex,job,addr,age','','','','60','JDBC','t1','t1','','[{\"column_md5\":\"88c-4d33-9cbb-e3\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"ede-4385-b7ff-7f\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"9e6-442d-9a86-04\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"42c-43f9-9077-97\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"723-4b22-a043-1c\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','drop table t1','1','2020-06-08 10:32:27','','','','','','1-100','',NULL,'','','csv','GBK','|','','','','false','','','','','','-1'),(719629297702146048,'单分割符自带标题','59','HDFS','','/data/csv/h1.txt','','','','','60','JDBC','h1','h1','','[]','delete from h1','1','2020-06-08 11:09:55','','','','','','','',NULL,'','','csv','GBK','|','','','','true','','','','','','-1'),(719630143433216000,'多分割符无标题','59','HDFS','','/data/csv/t2.txt','name,sex,job,addr,age','','','','60','JDBC','t2','t2','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"},{\"column_md5\":\"462-4b77-a7ac-48\",\"column_name\":\"\",\"column_expr\":\"$zdh_etl_date\",\"column_type\":\"string\",\"column_alias\":\"etl_date\"}]','drop table t2','1','2020-06-08 11:13:16','','','','','','','',NULL,'','','csv','GBK','|+','','','','','','','','','','-1'),(719630908637843456,'tab分割无标题','59','HDFS','','/data/csv/t3.txt','name,sex,job,addr,age','','{\"sep\":\"	\",\"encoding\":\"GBK\"}','','60','JDBC','t3','t3','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','delete from t3','1','2020-06-08 11:16:19','','','','','','','',NULL,'','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','','','','-1'),(724291182208749568,'单分割符无标题输出hudi','59','HDFS','','/data/csv/t1.txt','name,sex,job,addr,age','','','','59','HDFS','','/data/hudi/t1','{\"precombine_field_opt_key\":\"name\",\"recordkey_field_opt_key\":\"name\"}','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','','1','2020-06-21 07:54:35','','','','','','1-100','',NULL,'','','csv','GBK','|','hudi','','',NULL,NULL,'','','','','-1'),(724312011898359808,'hudi输出多分割符无标题','59','HDFS','','/data/hudi/t1','name,sex,job,addr,age','','','','59','HDFS','','/data/csv/hudi_t1','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','','1','2020-06-21 09:17:21','','','','','','1-100','',NULL,'','','hudi','','','csv','UTF-8','+-',NULL,NULL,'','','','','-1'),(728251791409418240,'kafka接收客户信息存mydb','62','KAFKA','','m1','name,age','','','','60','JDBC','m1','m1','','[{\"column_md5\":\"2c5-46c3-b4e7-96\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"a82-409a-b944-e2\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','','1','2020-07-02 06:12:37','','','','','','','',NULL,'','','','',',','','','','','','','','','','-1'),(728647407415332864,'单分割符无标题2','59','HDFS','','/data/csv/t4.txt','name,sex,job,addr,age','','','','60','JDBC','t4','t4','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','delete from t4','1','2020-07-03 08:24:40','','','','','','1-100','',NULL,'','','csv','UTF-8','\\\\','','','','false','','','','','','-1'),(728996795115376640,'单分割符无标题3','59','HDFS','','/data/csv/t5.txt','name,sex,job,addr,age','','','','60','JDBC','t5','t5','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','delete from t5','1','2020-07-04 07:33:00','','','','','','1-100','',NULL,'','','csv','UTF-8','//','','','','false','','','','','','-1'),(731444359840403456,'clickhouse_datasets.z1转zdh_test.z1','63','JDBC','datasets.z1','','','name,age,sex,money','','','60','JDBC','z1','z1','','[{\"column_md5\":\"80c-4162-bf9a-59\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"fd7-4632-9e75-78\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"},{\"column_md5\":\"bbf-439a-8bfa-19\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"8ef-4d54-84d3-9d\",\"column_name\":\"money\",\"column_expr\":\"money\",\"column_type\":\"string\",\"column_alias\":\"money\"}]','','1','2020-07-11 01:38:45','','','','','','','',NULL,'','','','','','','','','','','','','','','-1'),(746140955731562496,'sftp测试','65','SFTP','','/home/zyc/work/t1.txt','name,age,sex','','','','60','JDBC','t1','t1','','[{\"column_md5\":\"3fa-444c-9d88-45\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"7bc-44e2-896d-1c\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"},{\"column_md5\":\"ebc-4288-acf9-5e\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"}]','drop table t1','1','2020-08-20 14:57:46','','','','','','','',NULL,'','','csv','UTF-8',',','','','','false','','','','','','-1'),(749279343477264384,'mydb#account_info','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','','','','','','[{\"column_md5\":\"212-4fac-b1df-1a\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"12f-4e82-8a9c-e3\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"user_name\"},{\"column_md5\":\"116-44b0-b692-15\",\"column_name\":\"user_password\",\"column_expr\":\"user_password\",\"column_type\":\"string\",\"column_alias\":\"user_password\"},{\"column_md5\":\"7eb-4515-9718-98\",\"column_name\":\"email\",\"column_expr\":\"email\",\"column_type\":\"string\",\"column_alias\":\"email\"},{\"column_md5\":\"4b2-45ed-9654-9f\",\"column_name\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_type\":\"string\",\"column_alias\":\"is_use_email\"},{\"column_md5\":\"c1c-44d4-8a8e-35\",\"column_name\":\"phone\",\"column_expr\":\"phone\",\"column_type\":\"string\",\"column_alias\":\"phone\"},{\"column_md5\":\"474-43a6-af20-3b\",\"column_name\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_type\":\"string\",\"column_alias\":\"is_use_phone\"}]','','1','2020-08-29 06:48:36','','','','','','','',NULL,'','','','','','','','','','','','','','','-1'),(797802796177952768,'测试tidb写入','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','66','TIDB','','d1.t3','','[{\"column_md5\":\"898-4656-945f-00\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"name\"}]','delete from d1.t3','1','2021-01-10 04:23:29','','','','','','','',NULL,'','','','','','','','','','','','','','','-1'),(797819041442959360,'第一个tidb读取','66','TIDB','','d1.t3','name','','','name=\'{{zdh_date}}\'','60','JDBC','tidb_t3','tidb_t3','','[{\"column_md5\":\"059-44b9-83ea-07\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"}]','delete from tidb_t3','1','2021-01-10 05:28:02','','','','','','','',NULL,'','','','','','','','','','','','','','','-1'),(818059052008345600,'mysql转hfds测试','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','59','HDFS','','/zdh/data/account_info','','[{\"column_md5\":\"138-46ec-ae83-22\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"9fa-45a3-8834-34\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"user_name\"},{\"column_md5\":\"c2c-432f-a698-13\",\"column_name\":\"user_password\",\"column_expr\":\"user_password\",\"column_type\":\"string\",\"column_alias\":\"user_password\"},{\"column_md5\":\"53a-4f0e-b798-3a\",\"column_name\":\"email\",\"column_expr\":\"email\",\"column_type\":\"string\",\"column_alias\":\"email\"},{\"column_md5\":\"354-41c2-96bc-9c\",\"column_name\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_type\":\"string\",\"column_alias\":\"is_use_email\"},{\"column_md5\":\"b17-4d90-a66c-57\",\"column_name\":\"phone\",\"column_expr\":\"phone\",\"column_type\":\"string\",\"column_alias\":\"phone\"},{\"column_md5\":\"0c4-46ef-8fff-ec\",\"column_name\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_type\":\"string\",\"column_alias\":\"is_use_phone\"}]','','1','2021-03-07 01:54:37','','','','','','','',NULL,'','','','','','csv','UTF-8','|','','true','','','','','-1'),(837799577204559872,'jdbc优化过滤','58','JDBC','data_sources_type_info','','','id,sources_type','{\"filter.optimize\":\"true\"}','sources_type=\'JDBC\'','60','JDBC','ds2','ds2','','[{\"column_md5\":\"a88-4ab0-be18-9c\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"372-4c7e-8e2c-f6\",\"column_name\":\"sources_type\",\"column_expr\":\"sources_type\",\"column_type\":\"string\",\"column_alias\":\"sources_type\"}]','','1','2021-04-30 13:16:25','','','','','','','',NULL,'','','','','','','','','','','','','','','-1'),(843090296215441408,'mydb->iceberg','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','68','ICEBERG','','test.account_info','','[{\"column_md5\":\"705-4789-ad8d-2d\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"247-4c66-8b25-76\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"user_name\"},{\"column_md5\":\"2e0-4d88-8862-39\",\"column_name\":\"user_password\",\"column_expr\":\"user_password\",\"column_type\":\"string\",\"column_alias\":\"user_password\"},{\"column_md5\":\"4ad-45d6-92b6-db\",\"column_name\":\"email\",\"column_expr\":\"email\",\"column_type\":\"string\",\"column_alias\":\"email\"},{\"column_md5\":\"891-4c38-9e16-a3\",\"column_name\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_type\":\"string\",\"column_alias\":\"is_use_email\"},{\"column_md5\":\"7e2-4f9c-bc1d-74\",\"column_name\":\"phone\",\"column_expr\":\"phone\",\"column_type\":\"string\",\"column_alias\":\"phone\"},{\"column_md5\":\"87c-46d2-8b84-a2\",\"column_name\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_type\":\"string\",\"column_alias\":\"is_use_phone\"}]','','1','2021-05-15 03:39:50','','','','','','','',NULL,'','','','','','','','','','','','','','','-1'),(853275616093409280,'mysql2oracle','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','69','JDBC','account_info','account_info','','[{\"column_md5\":\"c4b-4997-a502-4b\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"c2c-4471-9632-cb\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"user_name\"},{\"column_md5\":\"33c-4cd8-874f-d1\",\"column_name\":\"user_password\",\"column_expr\":\"user_password\",\"column_type\":\"string\",\"column_alias\":\"user_password\"},{\"column_md5\":\"95e-49c9-92ac-46\",\"column_name\":\"email\",\"column_expr\":\"email\",\"column_type\":\"string\",\"column_alias\":\"email\"},{\"column_md5\":\"6c5-400d-abd0-ac\",\"column_name\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_type\":\"string\",\"column_alias\":\"is_use_email\"},{\"column_md5\":\"372-42e9-8e74-50\",\"column_name\":\"phone\",\"column_expr\":\"phone\",\"column_type\":\"string\",\"column_alias\":\"phone\"},{\"column_md5\":\"412-4dd3-ba89-6e\",\"column_name\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_type\":\"string\",\"column_alias\":\"is_use_phone\"}]','','1','2021-06-12 06:12:40','','','','','','','',NULL,'','','','','','','','','','','','','','','-1'),(853938731688660992,'测试新增参数','59','HDFS','','/data/csv/t1.txt','name,sex,job,addr,age','','','','59','HDFS','','/data/output/t1','','[{\"column_md5\":\"799-4d88-8bf9-f6\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"37b-4f66-8367-d8\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"540-49c6-ad42-47\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"d9e-4a98-9374-7f\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"25d-4892-a2a9-32\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','','1','2021-06-14 02:07:39','','','','','','','',NULL,'','','csv','GBK','|','csv','UTF-8',',','false','true','2','name','overwrite','','1'),(855227983957331968,'测试hdfs写入单文件','58','JDBC','zdh_logs','','','job_id,log_time,msg,level,task_logs_id','','','59','HDFS','','/data/output/zdh_ha.txt','','[{\"column_md5\":\"5e0-4fd2-ae28-e2\",\"column_name\":\"job_id\",\"column_expr\":\"job_id\",\"column_type\":\"string\",\"column_alias\":\"job_id\"},{\"column_md5\":\"3ec-48d1-a596-2e\",\"column_name\":\"log_time\",\"column_expr\":\"log_time\",\"column_type\":\"string\",\"column_alias\":\"log_time\"},{\"column_md5\":\"06f-48c6-9736-50\",\"column_name\":\"msg\",\"column_expr\":\"msg\",\"column_type\":\"string\",\"column_alias\":\"msg\"},{\"column_md5\":\"99d-4480-b019-40\",\"column_name\":\"level\",\"column_expr\":\"level\",\"column_type\":\"string\",\"column_alias\":\"level\"},{\"column_md5\":\"c02-4719-a3fd-8c\",\"column_name\":\"task_logs_id\",\"column_expr\":\"task_logs_id\",\"column_type\":\"string\",\"column_alias\":\"task_logs_id\"}]','','1','2021-06-17 15:30:41','','','','','','','',NULL,'','','','','','csv','UTF-8','||','','true','','','append','','-1'),(889232742397513728,'tttt','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','61','外部下载','','aa','','[{\"column_md5\":\"fd1-4963-be81-68\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"e4a-41ee-8702-9c\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"user_name\"},{\"column_md5\":\"f62-46ed-a1fa-21\",\"column_name\":\"user_password\",\"column_expr\":\"user_password\",\"column_type\":\"string\",\"column_alias\":\"user_password\"},{\"column_md5\":\"45f-4629-b4ff-9c\",\"column_name\":\"email\",\"column_expr\":\"email\",\"column_type\":\"string\",\"column_alias\":\"email\"},{\"column_md5\":\"b65-4bcd-a199-eb\",\"column_name\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_type\":\"string\",\"column_alias\":\"is_use_email\"},{\"column_md5\":\"e96-426c-bfdd-6a\",\"column_name\":\"phone\",\"column_expr\":\"phone\",\"column_type\":\"string\",\"column_alias\":\"phone\"},{\"column_md5\":\"dc6-466b-8645-d4\",\"column_name\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_type\":\"string\",\"column_alias\":\"is_use_phone\"}]','','1','2021-09-19 11:33:27','','','','','','','',NULL,'','','','','','csv','GBK','','','true','','','append','','-1'),(889473341604237312,'测试异常','58','JDBC','','','','','','','','','','','','[]','','1','2021-09-20 03:29:30','','','','','','','',NULL,'','','','','','','','','','','','','','','-1');
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
) ENGINE=InnoDB AUTO_INCREMENT=906960111979728897 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `every_day_notice`
--

LOCK TABLES `every_day_notice` WRITE;
/*!40000 ALTER TABLE `every_day_notice` DISABLE KEYS */;
INSERT INTO `every_day_notice` VALUES (906960111979728896,'4.7.11测试预览版-(重构整个项目,请从github readme 下载安装包,4.7.11及之后版本不在开放源代码，只提供安装包，免费使用)，1:增加数仓管理模块,数据发布,申请,审批功能 2:优化jdbc性能 3:增加hdfs依赖组件 4:增加申请源ETL任务 5:重构权限模块-增加接口权限 6:新增flink离线在线采集, 7:重构信息管理+告警模块 8:版本大更新-修复历史版本bug优化dag调度逻辑,值得尝试！，勿修改此通知,当前开源最新4.7.10版本,唯一下载地址:https://github.com/zhaoyachao/zdh_web ,其他下载均非本人提供','false');
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
  `status` varchar(64) DEFAULT NULL COMMENT '1:发布,2:未发布',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=903076295573770241 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issue_data_info`
--

LOCK TABLES `issue_data_info` WRITE;
/*!40000 ALTER TABLE `issue_data_info` DISABLE KEYS */;
INSERT INTO `issue_data_info` VALUES (8,'测试日志表','53','JDBC','zdh_logs','zdh_logs','','job_id,log_time,msg,level,task_logs_id','[{\"column_md5\":\"5b5-4cc6-94dd-d9\",\"column_name\":\"job_id\",\"column_type\":\"string\"},{\"column_md5\":\"f27-4b43-9cb1-95\",\"column_name\":\"log_time\",\"column_type\":\"string\"},{\"column_md5\":\"41f-4c05-8026-98\",\"column_name\":\"msg\",\"column_type\":\"string\"},{\"column_md5\":\"de8-4d75-9a40-e8\",\"column_name\":\"level\",\"column_type\":\"string\"},{\"column_md5\":\"690-4941-88bd-1a\",\"column_name\":\"task_logs_id\",\"column_type\":\"string\"}]','2','2021-05-04 01:04:44','A-公司','','','','1'),(903073104664727552,'第二个发布task_logs','58','JDBC','task_logs','task_logs','','id,job_id,job_context,etl_date,status,start_time,update_time,owner,is_notice,process,thread_id,retry_time,executor,url,etl_info,application_id,history_server,master,server_ack','[{\"column_md5\":\"e5d-4eee-993d-8f\",\"column_name\":\"id\",\"column_type\":\"string\",\"column_desc\":\"主键\"},{\"column_md5\":\"73b-4297-97c4-c2\",\"column_name\":\"job_id\",\"column_type\":\"string\"},{\"column_md5\":\"c48-40e3-9e0d-a1\",\"column_name\":\"job_context\",\"column_type\":\"string\"},{\"column_md5\":\"839-4e62-97cf-d4\",\"column_name\":\"etl_date\",\"column_type\":\"string\"},{\"column_md5\":\"db6-46c6-99d7-b2\",\"column_name\":\"status\",\"column_type\":\"string\"},{\"column_md5\":\"2aa-40ba-8bb9-d1\",\"column_name\":\"start_time\",\"column_type\":\"string\"},{\"column_md5\":\"72c-49f9-9461-41\",\"column_name\":\"update_time\",\"column_type\":\"string\"},{\"column_md5\":\"ed2-4ff5-9884-80\",\"column_name\":\"owner\",\"column_type\":\"string\"},{\"column_md5\":\"14e-47d6-85cb-b5\",\"column_name\":\"is_notice\",\"column_type\":\"string\"},{\"column_md5\":\"8ec-43ab-960b-bf\",\"column_name\":\"process\",\"column_type\":\"string\"},{\"column_md5\":\"794-4581-b05a-65\",\"column_name\":\"thread_id\",\"column_type\":\"string\"},{\"column_md5\":\"12d-4a5b-b933-a6\",\"column_name\":\"retry_time\",\"column_type\":\"string\"},{\"column_md5\":\"cf0-49d1-a29b-29\",\"column_name\":\"executor\",\"column_type\":\"string\"},{\"column_md5\":\"8c4-453e-aa7c-1e\",\"column_name\":\"url\",\"column_type\":\"string\"},{\"column_md5\":\"962-454b-8a68-7f\",\"column_name\":\"etl_info\",\"column_type\":\"string\"},{\"column_md5\":\"f08-4933-af5b-fe\",\"column_name\":\"application_id\",\"column_type\":\"string\"},{\"column_md5\":\"9e6-4322-a676-a8\",\"column_name\":\"history_server\",\"column_type\":\"string\"},{\"column_md5\":\"b0d-4c6d-8c02-33\",\"column_name\":\"master\",\"column_type\":\"string\"},{\"column_md5\":\"587-43c9-a3b1-2c\",\"column_name\":\"server_ack\",\"column_type\":\"string\"}]','1','2021-10-27 16:10:06','d','ddd12','avc','','2'),(903076295573770240,'第3个发布','58','JDBC','account_info','account_info','','id,user_name,user_password,email,is_use_email,phone,is_use_phone,enable,user_group,roles,signature','[{\"column_md5\":\"762-4308-a4a3-38\",\"column_name\":\"id\",\"column_type\":\"string\",\"column_desc\":\"主键2\"},{\"column_md5\":\"c52-4928-9586-5b\",\"column_name\":\"user_name\",\"column_type\":\"string\"},{\"column_md5\":\"771-45c6-832c-df\",\"column_name\":\"user_password\",\"column_type\":\"string\"},{\"column_md5\":\"374-4b11-8964-9d\",\"column_name\":\"email\",\"column_type\":\"string\"},{\"column_md5\":\"126-45b5-b1fc-7c\",\"column_name\":\"is_use_email\",\"column_type\":\"string\"},{\"column_md5\":\"197-4bdc-9b61-94\",\"column_name\":\"phone\",\"column_type\":\"string\"},{\"column_md5\":\"92d-4b03-9e1c-ac\",\"column_name\":\"is_use_phone\",\"column_type\":\"string\"},{\"column_md5\":\"d32-4f2e-a100-2b\",\"column_name\":\"enable\",\"column_type\":\"string\"},{\"column_md5\":\"c40-45cd-94ff-ad\",\"column_name\":\"user_group\",\"column_type\":\"string\"},{\"column_md5\":\"b77-478d-8ddf-4f\",\"column_name\":\"roles\",\"column_type\":\"string\"},{\"column_md5\":\"28a-4858-977b-28\",\"column_name\":\"signature\",\"column_type\":\"string\"}]','1','2021-10-27 16:22:47','','','','','1');
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
INSERT INTO `jar_file_info` VALUES ('732534262112194560','rdp-core-2.0.jar',NULL,'2020-07-14 01:49:37','732534261759873024','1','success'),('732534576089403392','rdp-core-2.0.jar',NULL,'2020-07-14 01:50:52','732534575875493888','1','success'),('732534576756297728','report-common-1.0.7.jar',NULL,'2020-07-14 01:50:52','732534575875493888','1','success'),('732589310183739392','zdh_server.jar',NULL,'2020-07-14 05:28:22','732538726244159488','1','success'),('732643701108510720','log4j.properties',NULL,'2020-07-14 09:04:30','732538726244159488','1','success'),('749042705316712448','zdh_server.jar',NULL,'2020-08-28 15:08:17','749041924744155136','1',NULL),('749052568860102656','zdh_server.jar',NULL,'2020-08-28 15:47:29','749051365933715456','1','success'),('749054743187296256','zdh_server.jar',NULL,'2020-08-28 15:56:07','749054742851751936','1',NULL),('749058489606737920','log4j.properties',NULL,'2020-08-28 16:11:00','749055828161466368','1','success'),('749060185443536896','log4j.properties',NULL,'2020-08-28 16:17:45','749060184990552064','1',NULL),('749060348916535296','log4j.properties',NULL,'2020-08-28 16:18:24','749060348564213760','1',NULL),('749062114471055360','zdh_server.jar',NULL,'2020-08-28 16:25:24','749062114156482560','1',NULL),('749063562575482880','zdh_server.jar',NULL,'2020-08-28 16:31:10','749063562055389184','1','success'),('794984305888595968','',NULL,'2021-01-02 09:43:48','794984305481748480','1',NULL),('794985297841491968','',NULL,'2021-01-02 09:47:45','794985297623388160','1',NULL),('794985444797321216','',NULL,'2021-01-02 09:48:20','794985443983626240','1',NULL),('795224647019794432','',NULL,'2021-01-03 01:38:50','795224646118019072','1',NULL),('904048696486793216','mydb.sql',NULL,'2021-10-30 08:46:45','749064500069535744','1','success');
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;
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
-- Table structure for table `notice_info`
--

DROP TABLE IF EXISTS `notice_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notice_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `msg_type` varchar(200) DEFAULT NULL COMMENT '消息类型',
  `msg_title` varchar(500) DEFAULT NULL COMMENT '主题',
  `msg_url` varchar(500) DEFAULT NULL COMMENT '消息连接',
  `msg` text DEFAULT NULL COMMENT '消息',
  `is_see` varchar(100) DEFAULT NULL COMMENT '是否查看,true/false',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice_info`
--

LOCK TABLES `notice_info` WRITE;
/*!40000 ALTER TABLE `notice_info` DISABLE KEYS */;
INSERT INTO `notice_info` VALUES (152,'通知','任务失败通知: 测试专用','log_txt.html?job_id=906547945187315712&task_log_id=906548124246347776','失败任务:\r\n调度任务:906547945187315712,调度名:测试专用\r\n任务组:906548123663339520,任务组名:测试专用\r\nETL任务:719619870378954752,ETL任务名:单分割符无标题\r\nETL任务类型:单源ETL\r\n任务实例id:906548124246347776,任务实例名:单分割符无标题\r\nETL日期:2021-11-06 14:18:34\r\n开始时间:2021-11-06 14:18:35\r\n','true','1','2021-11-06 06:18:51','2021-11-06 06:20:37'),(153,'通知','任务完成通知: 测试专用','log_txt.html?job_id=906547945187315712&task_log_id=906548124233764864','任务完成通知:\r\n调度任务:906547945187315712,调度名:测试专用\r\n任务组:906548123663339520,任务组名:测试专用\r\nETL任务:null,ETL任务名:测试shell\r\nETL任务类型:\r\n任务实例id:906548124233764864,任务实例名:测试shell\r\nETL日期:2021-11-06 14:18:34\r\n开始时间:2021-11-06 14:18:35\r\n完成时间:2021-11-06 14:18:35','true','1','2021-11-06 06:18:51','2021-11-06 06:20:45'),(154,'通知','任务完成通知: 测试专用','log_txt.html?job_id=906547945187315712&task_log_id=906694440016416768','任务完成通知:\r\n调度任务:906547945187315712,调度名:测试专用\r\n任务组:906694439831867392,任务组名:测试专用\r\nETL任务:null,ETL任务名:测试shell\r\nETL任务类型:\r\n任务实例id:906694440016416768,任务实例名:测试shell\r\nETL日期:2021-11-06 14:16:11\r\n开始时间:2021-11-07 00:00:00\r\n完成时间:2021-11-07 00:00:00','false','1','2021-11-06 16:00:06','2021-11-06 16:00:06'),(155,'通知','任务失败通知: 测试专用','log_txt.html?job_id=906547945187315712&task_log_id=906694440020611072','失败任务:\r\n调度任务:906547945187315712,调度名:测试专用\r\n任务组:906694439831867392,任务组名:测试专用\r\nETL任务:719619870378954752,ETL任务名:单分割符无标题\r\nETL任务类型:单源ETL\r\n任务实例id:906694440020611072,任务实例名:单分割符无标题\r\nETL日期:2021-11-06 14:16:11\r\n开始时间:2021-11-07 00:00:00\r\n','false','1','2021-11-06 16:00:21','2021-11-06 16:00:21');
/*!40000 ALTER TABLE `notice_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process_flow_info`
--

DROP TABLE IF EXISTS `process_flow_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `process_flow_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `flow_id` varchar(64) NOT NULL DEFAULT '0' COMMENT '流程标识id',
  `event_code` varchar(64) NOT NULL DEFAULT '' COMMENT '事件code',
  `config_code` varchar(64) NOT NULL DEFAULT '0' COMMENT '审批流程code',
  `context` varchar(128) NOT NULL DEFAULT '' COMMENT '事件说明',
  `auditor_id` text DEFAULT NULL COMMENT '审批人id,逗号分割',
  `is_show` varchar(128) NOT NULL DEFAULT '0' COMMENT '1:可见,0:不可见',
  `owner` varchar(100) NOT NULL DEFAULT '' COMMENT '拥有者',
  `pre_id` varchar(100) NOT NULL DEFAULT '' COMMENT '流程id',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `status` varchar(10) NOT NULL DEFAULT '0' COMMENT '流程状态,0:未审批,1:审批完成,2:不通过,3:撤销',
  `is_end` varchar(64) NOT NULL DEFAULT '0' COMMENT '0:非最后一个节点,1:最后一个节点',
  `level` varchar(64) NOT NULL DEFAULT '0' COMMENT '审批节点环节',
  `event_id` varchar(64) NOT NULL DEFAULT '' COMMENT '发起流程的具体事件唯一键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=903085574162747394 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process_flow_info`
--

LOCK TABLES `process_flow_info` WRITE;
/*!40000 ALTER TABLE `process_flow_info` DISABLE KEYS */;
INSERT INTO `process_flow_info` VALUES (903073104698281985,'903073104698281984','data_pub','data','发布数据-第二个发布task_logs','1','1','1','','2021-10-27 16:10:06','1','0','1','903073104664727552'),(903073104727642112,'903073104698281984','data_pub','data','发布数据-第二个发布task_logs','1','1','1','903073104698281985','2021-10-27 16:10:06','1','0','2','903073104664727552'),(903073104736030722,'903073104698281984','data_pub','data','发布数据-第二个发布task_logs','1','1','1','903073104727642112','2021-10-27 16:10:06','1','1','7','903073104664727552'),(903076295632490497,'903076295632490496','data_pub','data','发布数据-第3个发布','1','1','1','','2021-10-27 16:22:47','1','0','1','903076295573770240'),(903076295661850624,'903076295632490496','data_pub','data','发布数据-第3个发布','1','1','1','903076295632490497','2021-10-27 16:22:47','1','0','2','903076295573770240'),(903076295674433537,'903076295632490496','data_pub','data','发布数据-第3个发布','1','1','1','903076295661850624','2021-10-27 16:22:47','1','1','7','903076295573770240'),(903082515185537025,'903082515185537024','data_apply','data','申请数据-account_info','1','1','2','','2021-10-27 16:47:30','1','0','1','903082514808049664'),(903082515911151616,'903082515185537024','data_apply','data','申请数据-account_info','1','1','2','903082515185537025','2021-10-27 16:47:30','1','0','2','903082514808049664'),(903082516624183296,'903082515185537024','data_apply','data','申请数据-account_info','1','1','2','903082515911151616','2021-10-27 16:47:30','1','1','7','903082514808049664'),(903085573625876481,'903085573625876480','data_apply','data','申请数据-account_info','1','1','2','','2021-10-27 16:59:39','1','0','1','903085573202251776'),(903085573877534720,'903085573625876480','data_apply','data','申请数据-account_info','1','1','2','903085573625876481','2021-10-27 16:59:39','0','0','2','903085573202251776'),(903085574162747393,'903085573625876480','data_apply','data','申请数据-account_info','1','0','2','903085573877534720','2021-10-27 16:59:39','0','1','7','903085573202251776');
/*!40000 ALTER TABLE `process_flow_info` ENABLE KEYS */;
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
INSERT INTO `quality` VALUES ('720677954291503104','719620564695650304','719619870378954752','2020-06-12 00:00:00','通过','{\"result\":\"通过\",\"rows_range\":\"数据行数检测通过\"}','2020-06-11 08:36:57','1'),('720679166206283776','719620564695650304','719619870378954752','2020-06-13 00:00:00','通过','{\"result\":\"通过\",\"总行数\":\"9\",\"rows_range\":\"数据行数检测通过\"}','2020-06-11 08:41:48','1');
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
  `alarm_email` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `alarm_sms` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `alarm_zdh` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_error` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_finish` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_timeout` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quartz_job_info`
--

LOCK TABLES `quartz_job_info` WRITE;
/*!40000 ALTER TABLE `quartz_job_info` DISABLE KEYS */;
INSERT INTO `quartz_job_info` VALUES ('719630230595047424',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'finish',NULL,NULL,NULL,'719630143433216000',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off'),('719631150493995008',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'finish',NULL,NULL,NULL,'719630908637843456',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off'),('720684617849376768','读取hive并下载文件',NULL,'ETL','2020-12-13 16:00:00','2020-12-15 16:00:00','1d','1','2',0,NULL,'',NULL,'2020-12-15 16:00:00',NULL,'1m','finish',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"d6f_96e_8be0_bb\",\"etl_task_id\":\"720684174964428800\",\"etl_context\":\"读取hive转外部下载\",\"more_task\":\"SQL\",\"divId\":\"d6f_96e_8be0_bb\",\"name\":\"读取hive转外部下载\",\"positionX\":382,\"positionY\":297,\"type\":\"tasks\"},{\"id\":\"fb9_0cc_b891_be\",\"etl_context\":\"hostname\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"fb9_0cc_b891_be\",\"name\":\"hostname\",\"positionX\":141,\"positionY\":177,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_12\",\"pageSourceId\":\"fb9_0cc_b891_be\",\"pageTargetId\":\"d6f_96e_8be0_bb\"}]}','off','off','off','off','off','off'),('724291412505399296',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'finish',NULL,NULL,NULL,'724291182208749568',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off'),('724312221676474368',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'finish',NULL,NULL,NULL,'724312011898359808',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off'),('728252156099956736','kafka接收客户信息存mydb',NULL,'ETL','2020-07-01 16:00:00','2020-07-30 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-07-08 00:00:00\"}','etl','2020-07-07 16:00:00','2020-07-08 16:00:00','100s','remove',NULL,NULL,NULL,'728251791409418240','kafka接收客户信息存mydb','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off'),('728647515297026048','单分割符无标题2',NULL,'ETL','2021-09-18 14:29:29','2021-09-18 14:29:29','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"96e_16f_b1f6_62\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"96e_16f_b1f6_62\",\"name\":\"单分割符无标题\",\"positionX\":229,\"positionY\":100,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('728996892721025024','单分割符无标题3',NULL,'ETL','2020-10-28 16:00:00','2020-12-22 16:00:00','','1','5',0,'','','finish','2020-10-31 16:00:00','2020-11-01 16:00:00','100s','create',NULL,NULL,NULL,'728996795115376640','单分割符无标题3','1',NULL,'728647515297026048',NULL,NULL,'',NULL,NULL,'','772119486072360960','86400',NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off'),('731444485837295616','clickhouse_datasets.z1转zdh_test.z1',NULL,'ETL','2020-07-10 16:00:00','2020-09-10 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-07-11 00:00:00\"}','finish','2020-07-10 16:00:00',NULL,'100s','create',NULL,NULL,NULL,'731444359840403456','clickhouse_datasets.z1转zdh_test.z1','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off'),('732547003703103488','第一个jar',NULL,'ETL','2020-07-13 16:00:00','2020-08-07 16:00:00','','1','-1',1,'','{\"ETL_DATE\":\"2020-07-15 00:00:00\"}','finish','2020-07-14 16:00:00','2020-07-15 16:00:00','100s','create',NULL,NULL,NULL,'732538726244159488','第一个jar','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off'),('736528842050506752','第一个drools处理',NULL,'ETL','2020-12-31 16:00:00','2021-01-30 16:00:00','1d','1','1',0,NULL,'',NULL,'2021-01-05 16:00:00',NULL,'2h','remove',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5','on',NULL,'zyc',NULL,'20','5',NULL,'off','','{\"tasks\":[{\"id\":\"2\",\"etl_task_id\":\"2\",\"etl_context\":\"第一个drools\",\"more_task\":\"Drools\",\"divId\":\"9ff_457_aa3d_03\",\"name\":\"第一个drools\",\"positionX\":246,\"positionY\":149,\"type\":\"tasks\"},{\"id\":\"794986268751564800\",\"etl_task_id\":\"794986268751564800\",\"etl_context\":\"暂停5分钟\",\"more_task\":\"SSH\",\"divId\":\"e3c_33f_872d_84\",\"name\":\"暂停5分钟\",\"positionX\":464,\"positionY\":42,\"type\":\"tasks\"}],\"shell\":[],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"e3c_33f_872d_84\",\"pageTargetId\":\"9ff_457_aa3d_03\"}]}','off','off','off','off','off','off'),('746141094323949568','sftp测试',NULL,'ETL','2020-12-31 16:00:00','2021-01-30 16:00:00','1d','1','1',0,NULL,'',NULL,NULL,NULL,'100h','',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5','on','on','zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"746140955731562496\",\"etl_task_id\":\"746140955731562496\",\"etl_context\":\"sftp测试\",\"more_task\":\"单源ETL\",\"divId\":\"b2a_c26_8c0d_07\",\"name\":\"sftp测试\",\"positionX\":250,\"positionY\":111,\"type\":\"tasks\"}],\"shell\":[],\"line\":[]}','off','off','off','off','off','off'),('749203382681473024','123',NULL,'ETL','2020-08-28 16:00:00','2020-09-04 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-08-30 00:00:00\"}','finish','2020-08-29 16:00:00','2020-08-30 16:00:00','100s','finish',NULL,NULL,NULL,'749064500069535744','123','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off'),('749255207426199552','第一个drools',NULL,'ETL','2020-08-21 16:00:00','2020-08-28 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-08-23 00:00:00\"}','finish','2020-08-22 16:00:00','2020-08-23 16:00:00','100s','create',NULL,NULL,NULL,'2','第一个drools','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off'),('749258659757821952','hive->mysql->drools',NULL,'ETL','2020-08-27 16:00:00','2020-09-04 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-08-30 00:00:00\"}','finish','2020-08-29 16:00:00','2020-08-30 16:00:00','100s','create',NULL,NULL,NULL,'749258437296132096','hive->mysql->drools','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off'),('749280046954319872','more_test_account_info',NULL,'ETL','2020-08-27 16:00:00','2020-09-04 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-08-31 00:00:00\"}','finish','2020-08-30 16:00:00','2020-08-31 16:00:00','100s','create',NULL,NULL,NULL,'749279630074056704','more_test_account_info','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off'),('758090172859420672','hudi输出多分割符无标题',NULL,'ETL','2020-09-21 16:00:00','2020-10-01 16:00:00','','1','2',0,'','','error',NULL,NULL,'100s','finish',NULL,NULL,NULL,'724312011898359808','hudi输出多分割符无标题','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'','758101270681620480',NULL,NULL,NULL,NULL,NULL,NULL,'off','off','off','off','off','off'),('785083582216409088','第一个多任务调度',NULL,'ETL','2020-12-04 16:00:00','2021-01-08 16:00:00','1d','1','1',0,'','','error','2020-12-04 16:00:00','2020-12-05 16:00:00','100s','finish',NULL,NULL,NULL,'719619870378954752','单分割符无标题','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'','785105270991753216','86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719619870378954752\",\"topic\":\"单分割符无标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}','off','off','off','off','off','off'),('785139391885479936','第二个多任务调度',NULL,'ETL','2020-12-04 16:00:00','2021-01-08 16:00:00','1d','1','1',0,'','','finish','2020-12-04 16:00:00','2020-12-05 16:00:00','100s','finish',NULL,NULL,NULL,'756934940771225600','读取hive转mysql','1',NULL,NULL,NULL,NULL,'5','on',NULL,'','785140586985295873','86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719619870378954752\",\"topic\":\"单分割符无标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"719630143433216000\",\"topic\":\"多分割符无标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"756934940771225600\",\"topic\":\"读取hive转mysql\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"SQL\"}]}','off','off','off','off','off','off'),('785144554482307072','第3个调度',NULL,'ETL','2020-12-05 16:00:00','2021-01-08 16:00:00','1d','1','3',0,'','','',NULL,NULL,'1000s','create',NULL,NULL,NULL,'728647407415332864','单分割符无标题2','1',NULL,NULL,NULL,NULL,'5','on',NULL,'',NULL,'86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719619870378954752\",\"topic\":\"单分割符无标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"728647407415332864\",\"topic\":\"单分割符无标题2\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}','off','off','off','off','off','off'),('785144896708153344','第5个调度',NULL,'ETL','2020-12-05 16:00:00','2020-12-25 16:00:00','1d','1','1',0,'','','',NULL,NULL,'1000s','create',NULL,NULL,NULL,'728996795115376640','单分割符无标题3','1',NULL,NULL,NULL,NULL,'5','on',NULL,'',NULL,'86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719629297702146048\",\"topic\":\"单分割符自带标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"728996795115376640\",\"topic\":\"单分割符无标题3\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}','off','off','off','off','off','off'),('785145024143691776','第5个调度',NULL,'ETL','2020-12-05 16:00:00','2020-12-25 16:00:00','1d','1','1',0,'','','',NULL,NULL,'1000s','create',NULL,NULL,NULL,'728996795115376640','单分割符无标题3','1',NULL,NULL,NULL,NULL,'5','on',NULL,'',NULL,'86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719629297702146048\",\"topic\":\"单分割符自带标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"728996795115376640\",\"topic\":\"单分割符无标题3\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}','off','off','off','off','off','off'),('785145538403110912','test1',NULL,'ETL','2020-12-05 16:00:00','2021-01-01 16:00:00','1d','1','3',0,'','','error','2020-12-06 16:00:00','2020-12-07 16:00:00','1000s','finish',NULL,NULL,NULL,'728996795115376640','单分割符无标题3','1',NULL,NULL,NULL,NULL,'5','on',NULL,'','785224041974730752','86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind\",\"author\":\"hizzgdev@163.com\",\"version\":\"0.4.6\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"728647407415332864\",\"topic\":\"单分割符无标题2\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"728996795115376640\",\"topic\":\"单分割符无标题3\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}','off','off','off','off','off','off'),('787419291484950528','第一个测试',NULL,'ETL','2021-01-31 11:48:27','2021-01-31 11:48:27','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"5f5_754_bfb5_e4\",\"etl_context\":\"第一个jdbc任务\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"url\":\"jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"username\":\"zyc\",\"password\":\"123456\",\"jdbc_sql\":\"select * from account_info where \'2021-01-15\'={{zdh_date}} limit 1\",\"divId\":\"5f5_754_bfb5_e4\",\"name\":\"第一个jdbc任务\",\"positionX\":252,\"positionY\":282,\"type\":\"jdbc\"},{\"id\":\"be4_e13_bf4f_ac\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"divId\":\"be4_e13_bf4f_ac\",\"name\":\"单分割符无标题\",\"positionX\":94,\"positionY\":37,\"type\":\"tasks\"},{\"id\":\"8ca_667_8bb5_18\",\"etl_context\":\"hostname\",\"depend_level\":\"2\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"hostname\",\"divId\":\"8ca_667_8bb5_18\",\"name\":\"hostname\",\"positionX\":511,\"positionY\":84,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_13\",\"pageSourceId\":\"be4_e13_bf4f_ac\",\"pageTargetId\":\"8ca_667_8bb5_18\"},{\"connectionId\":\"con_15\",\"pageSourceId\":\"8ca_667_8bb5_18\",\"pageTargetId\":\"5f5_754_bfb5_e4\"}]}','off','off','off','off','off','off'),('789654690387202048','shell测试',NULL,'ETL','2021-02-08 02:14:07','2021-02-08 02:14:07','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"d31_88b_a993_0f\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"zdh_instance\":\"zdh_server\",\"is_disenable\":\"false\",\"divId\":\"d31_88b_a993_0f\",\"name\":\"单分割符无标题\",\"positionX\":283,\"positionY\":246,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('797802941498003456','调度tidb写入',NULL,'ETL','2021-01-09 16:00:00','2021-01-22 16:00:00','1d','1','1',0,NULL,'',NULL,NULL,NULL,'100s','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"797802796177952768\",\"etl_task_id\":\"797802796177952768\",\"etl_context\":\"测试tidb写入\",\"more_task\":\"单源ETL\",\"divId\":\"650_e6a_ba23_51\",\"name\":\"测试tidb写入\",\"positionX\":198,\"positionY\":68,\"type\":\"tasks\"}],\"shell\":[],\"line\":[]}','off','off','off','off','off','off'),('797819166902980608','tidb读取测试',NULL,'ETL','2021-01-09 16:00:00','2021-01-15 16:00:00','1d','1','1',0,NULL,'',NULL,NULL,NULL,'100s','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"797819041442959360\",\"etl_task_id\":\"797819041442959360\",\"etl_context\":\"第一个tidb读取\",\"more_task\":\"单源ETL\",\"divId\":\"e62_601_b1da_d0\",\"name\":\"第一个tidb读取\",\"positionX\":239,\"positionY\":126,\"type\":\"tasks\"}],\"shell\":[],\"line\":[]}','off','off','off','off','off','off'),('810198240522670080','测试shell同步异步',NULL,'ETL','2021-02-13 09:12:58','2021-03-31 09:12:58','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"6b2_454_9db3_74\",\"etl_context\":\"hello_world\",\"is_script\":\"true\",\"is_disenable\":\"false\",\"command\":\"for %%I in (A,B,C) do (timeout /t 50 & echo %%I)\",\"divId\":\"6b2_454_9db3_74\",\"name\":\"hello_world\",\"positionX\":202,\"positionY\":121,\"type\":\"shell\"},{\"id\":\"95e_e44_ac5e_bc\",\"etl_context\":\"测试A\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"url\":\"jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"username\":\"zyc\",\"password\":\"123456\",\"jdbc_sql\":\" insert into zdh_test.d1 values(\'北京\',\'22\',\'false\',\'it\',\'zyc\',\'man\');\",\"divId\":\"95e_e44_ac5e_bc\",\"name\":\"测试A\",\"positionX\":189,\"positionY\":250,\"type\":\"jdbc\"},{\"id\":\"230_c8e_b219_bd\",\"etl_context\":\"hostname\",\"is_script\":\"false\",\"is_disenable\":\"true\",\"command\":\"hostname\",\"divId\":\"230_c8e_b219_bd\",\"name\":\"hostname\",\"positionX\":360,\"positionY\":200,\"type\":\"shell\"},{\"id\":\"4b1_4fa_9c76_91\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"divId\":\"4b1_4fa_9c76_91\",\"name\":\"单分割符无标题\",\"positionX\":325,\"positionY\":358,\"type\":\"tasks\"}],\"line\":[{\"connectionId\":\"con_17\",\"pageSourceId\":\"6b2_454_9db3_74\",\"pageTargetId\":\"230_c8e_b219_bd\"},{\"connectionId\":\"con_19\",\"pageSourceId\":\"230_c8e_b219_bd\",\"pageTargetId\":\"95e_e44_ac5e_bc\"},{\"connectionId\":\"con_21\",\"pageSourceId\":\"95e_e44_ac5e_bc\",\"pageTargetId\":\"4b1_4fa_9c76_91\"}]}','off','off','off','off','off','off'),('810209693258485760','测试SSH任务实时日志',NULL,'ETL','2021-02-13 10:03:35','2021-02-13 10:03:35','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"d00_338_b9c2_9a\",\"etl_task_id\":\"749064500069535744\",\"etl_context\":\"123\",\"more_task\":\"SSH\",\"divId\":\"d00_338_b9c2_9a\",\"name\":\"123\",\"positionX\":245,\"positionY\":181,\"type\":\"tasks\"},{\"id\":\"b0f_25b_8e49_ce\",\"etl_context\":\"java-version\",\"is_script\":\"true\",\"is_disenable\":\"false\",\"command\":\"java -version\",\"divId\":\"b0f_25b_8e49_ce\",\"name\":\"java-version\",\"positionX\":256,\"positionY\":21,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"b0f_25b_8e49_ce\",\"pageTargetId\":\"d00_338_b9c2_9a\"}]}','off','off','off','off','off','off'),('818061528178626560','调度mysql转hdfs',NULL,'ETL','2021-03-07 01:55:04','2021-03-07 01:55:04','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"9f4_667_9e1e_de\",\"etl_task_id\":\"818059052008345600\",\"etl_context\":\"mysql转hfds测试\",\"more_task\":\"单源ETL\",\"divId\":\"9f4_667_9e1e_de\",\"name\":\"mysql转hfds测试\",\"positionX\":159,\"positionY\":99,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('824405424496185344','测试回退时间差',NULL,'ETL','2021-03-24 13:57:24','2021-03-24 13:57:24','1d','1','0',0,NULL,'',NULL,NULL,NULL,'0/5 * * * * ? *','remove',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'on','86400','{\"tasks\":[{\"id\":\"b25_ff7_be83_d8\",\"etl_context\":\"hostname\",\"depend_level\":\"0\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"hostname && echo \'{{zdh_date}}\'\",\"divId\":\"b25_ff7_be83_d8\",\"name\":\"hostname\",\"positionX\":189,\"positionY\":59,\"type\":\"shell\"}],\"line\":[]}','off','off','off','off','off','off'),('835448468066537472','测试多源任务',NULL,'ETL','2021-04-24 01:33:21','2021-04-30 01:33:21','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"280_398_9c8e_88\",\"etl_task_id\":\"2\",\"etl_context\":\"more_mydb_account_info\",\"more_task\":\"多源ETL\",\"divId\":\"280_398_9c8e_88\",\"name\":\"more_mydb_account_info\",\"positionX\":175,\"positionY\":104,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('835460442355666944','测试sql任务',NULL,'ETL','2021-04-24 02:20:58','2021-04-24 02:20:58','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"cc9_56e_89e1_39\",\"etl_task_id\":\"756934940771225600\",\"etl_context\":\"读取hive转mysql\",\"more_task\":\"SQL\",\"divId\":\"cc9_56e_89e1_39\",\"name\":\"读取hive转mysql\",\"positionX\":116,\"positionY\":96,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('837799765528809472','jdbc优化过滤测试',NULL,'ETL','2021-04-30 13:16:31','2021-05-04 13:16:31','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"c2c_4cd_9b81_5e\",\"etl_task_id\":\"837799577204559872\",\"etl_context\":\"jdbc优化过滤\",\"more_task\":\"单源ETL\",\"divId\":\"c2c_4cd_9b81_5e\",\"name\":\"jdbc优化过滤\",\"positionX\":214,\"positionY\":78,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('837982149905747968','shell异步测试',NULL,'ETL','2021-05-01 01:21:04','2021-05-10 01:21:04','1d','1','0',0,NULL,'',NULL,'2021-05-10 01:21:04',NULL,'1d','finish',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5','on',NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"fe3_577_b886_2f\",\"etl_context\":\"shell异步测试\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"exit -2\",\"divId\":\"fe3_577_b886_2f\",\"name\":\"shell异步测试\",\"positionX\":330,\"positionY\":77,\"type\":\"shell\"},{\"id\":\"7b7_39f_9bbe_44\",\"etl_context\":\"SHELL成功测试\",\"depend_level\":\"0\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"exit 0\",\"divId\":\"7b7_39f_9bbe_44\",\"name\":\"SHELL成功测试\",\"positionX\":533,\"positionY\":74,\"type\":\"shell\"},{\"id\":\"6bd_8ff_aeb1_04\",\"etl_context\":\"ping\",\"depend_level\":\"0\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"ping -t 127.0.0.1\",\"divId\":\"6bd_8ff_aeb1_04\",\"name\":\"ping\",\"positionX\":132,\"positionY\":76,\"type\":\"shell\"}],\"line\":[]}','off','off','off','off','off','off'),('838011667739578368','jdbc检查测试调度',NULL,'ETL','2021-05-01 03:16:23','2021-05-01 03:16:23','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"623_1f1_9679_6a\",\"etl_context\":\"jdbc查询\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"url\":\"jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"username\":\"zyc\",\"password\":\"123456\",\"jdbc_sql\":\"select * from account_info\",\"divId\":\"623_1f1_9679_6a\",\"name\":\"jdbc查询\",\"positionX\":369,\"positionY\":162,\"type\":\"jdbc\"},{\"id\":\"07e_84a_9393_11\",\"etl_context\":\"hdfs查询\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"url\":\"\",\"username\":\"zyc\",\"password\":\"\",\"url_type\":\"0\",\"hdfs_path\":\"F://data/csv/h1.txt\",\"hdfs_mode\":\"0\",\"divId\":\"07e_84a_9393_11\",\"name\":\"hdfs查询\",\"positionX\":390,\"positionY\":302,\"type\":\"hdfs\"}],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"623_1f1_9679_6a\",\"pageTargetId\":\"07e_84a_9393_11\"}]}','off','off','off','off','off','off'),('839467560947683328','第一个申请源调度',NULL,'ETL','2021-05-05 03:43:58','2021-05-05 03:43:58','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"67c_0d6_855f_db\",\"etl_task_id\":\"839443568224374784\",\"etl_context\":\"测试日志表\",\"more_task\":\"APPLY\",\"depend_level\":\"0\",\"divId\":\"67c_0d6_855f_db\",\"name\":\"测试日志表\",\"positionX\":298,\"positionY\":106,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('843090443133521920','第一个iceberg写入测试',NULL,'ETL','2021-05-15 03:39:56','2021-05-15 03:39:56','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"315_8ec_9318_e5\",\"etl_task_id\":\"843090296215441408\",\"etl_context\":\"mydb->iceberg\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"divId\":\"315_8ec_9318_e5\",\"name\":\"mydb->iceberg\",\"positionX\":192,\"positionY\":82,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('851042826887106560','测试空任务组',NULL,'ETL','2021-06-06 02:20:05','2021-06-06 02:20:05','1d','1','0',0,NULL,'',NULL,'2021-06-06 02:20:05',NULL,'1d','finish',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5','2021-06-08 14:52:51','off','','{\"tasks\":[],\"line\":[]}','off','off','off','off','off','off'),('853053085579218944','测试循环调度',NULL,'ETL','2021-06-11 15:26:21','2021-06-11 15:26:21','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"dd2_470_9884_2b\",\"etl_context\":\"h1\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"dd2_470_9884_2b\",\"name\":\"h1\",\"positionX\":214,\"positionY\":38,\"type\":\"shell\"},{\"id\":\"fcd_d27_82a8_5c\",\"etl_context\":\"h2\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"fcd_d27_82a8_5c\",\"name\":\"h2\",\"positionX\":152,\"positionY\":185,\"type\":\"shell\"},{\"id\":\"26a_603_aec0_42\",\"etl_context\":\"h3\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"26a_603_aec0_42\",\"name\":\"h3\",\"positionX\":287,\"positionY\":180,\"type\":\"shell\"},{\"id\":\"65b_e7a_beea_cd\",\"etl_context\":\"h4\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"65b_e7a_beea_cd\",\"name\":\"h4\",\"positionX\":149,\"positionY\":304,\"type\":\"shell\"},{\"id\":\"fd6_7e2_8376_79\",\"etl_context\":\"h5\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"fd6_7e2_8376_79\",\"name\":\"h5\",\"positionX\":220,\"positionY\":404,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_13\",\"pageSourceId\":\"dd2_470_9884_2b\",\"pageTargetId\":\"fcd_d27_82a8_5c\"},{\"connectionId\":\"con_22\",\"pageSourceId\":\"dd2_470_9884_2b\",\"pageTargetId\":\"26a_603_aec0_42\"},{\"connectionId\":\"con_31\",\"pageSourceId\":\"fcd_d27_82a8_5c\",\"pageTargetId\":\"65b_e7a_beea_cd\"},{\"connectionId\":\"con_40\",\"pageSourceId\":\"26a_603_aec0_42\",\"pageTargetId\":\"fd6_7e2_8376_79\"},{\"connectionId\":\"con_45\",\"pageSourceId\":\"65b_e7a_beea_cd\",\"pageTargetId\":\"fd6_7e2_8376_79\"}]}','off','off','off','off','off','off'),('853195794658889728','测试顺序依赖',NULL,'ETL','2021-06-12 00:54:22','2021-06-12 00:54:22','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"3b5_996_ac89_af\",\"etl_context\":\"hostname\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"3b5_996_ac89_af\",\"name\":\"hostname\",\"positionX\":289,\"positionY\":225,\"type\":\"shell\"},{\"id\":\"784_4e0_a932_83\",\"etl_task_id\":\"732547003703103488\",\"etl_context\":\"第一个jar\",\"depend_level\":\"0\",\"divId\":\"784_4e0_a932_83\",\"name\":\"第一个jar\",\"positionX\":287,\"positionY\":70,\"type\":\"group\"}],\"line\":[{\"connectionId\":\"con_16\",\"pageSourceId\":\"784_4e0_a932_83\",\"pageTargetId\":\"3b5_996_ac89_af\"}]}','off','off','off','off','off','off'),('853275752651558912','mysql2oracle测试',NULL,'ETL','2021-06-12 06:12:45','2021-06-12 06:12:45','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"1ec_d28_9984_32\",\"etl_task_id\":\"853275616093409280\",\"etl_context\":\"mysql2oracle\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"divId\":\"1ec_d28_9984_32\",\"name\":\"mysql2oracle\",\"positionX\":245,\"positionY\":89,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('853940428813111296','测试新增参数',NULL,'ETL','2021-06-14 02:14:00','2021-06-14 02:14:00','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"dea_a57_9621_93\",\"etl_task_id\":\"853938731688660992\",\"etl_context\":\"测试新增参数\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"divId\":\"dea_a57_9621_93\",\"name\":\"测试新增参数\",\"positionX\":380,\"positionY\":62,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('853947647352901632','测试多源ETL参数',NULL,'ETL','2021-06-14 02:42:34','2021-06-14 02:42:34','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"02f_d76_bcc4_cf\",\"etl_task_id\":\"2\",\"etl_context\":\"more_mydb_account_info\",\"more_task\":\"多源ETL\",\"depend_level\":\"0\",\"divId\":\"02f_d76_bcc4_cf\",\"name\":\"more_mydb_account_info\",\"positionX\":173,\"positionY\":83,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('853958344086392832','测试日志表2',NULL,'ETL','2021-06-14 03:25:10','2021-06-14 03:25:10','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"2af_425_b322_23\",\"etl_task_id\":\"853958150766727168\",\"etl_context\":\"测试日志表2\",\"more_task\":\"APPLY\",\"depend_level\":\"0\",\"divId\":\"2af_425_b322_23\",\"name\":\"测试日志表2\",\"positionX\":246,\"positionY\":80,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('855228103851511808','测试hdfs写入单文件',NULL,'ETL','2021-06-17 15:30:45','2021-06-17 15:30:45','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"8ef_c51_b118_fe\",\"etl_task_id\":\"855227983957331968\",\"etl_context\":\"测试hdfs写入单文件\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"divId\":\"8ef_c51_b118_fe\",\"name\":\"测试hdfs写入单文件\",\"positionX\":245,\"positionY\":78,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('856664204910792704','多源任务优化测试',NULL,'ETL','2021-06-21 14:37:24','2021-06-21 14:37:24','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"dc7_f00_ac0d_97\",\"etl_task_id\":\"3\",\"etl_context\":\"优化多源任务测试\",\"more_task\":\"多源ETL\",\"depend_level\":\"0\",\"divId\":\"dc7_f00_ac0d_97\",\"name\":\"优化多源任务测试\",\"positionX\":162,\"positionY\":71,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('857389943222177792','失败依赖测试',NULL,'ETL','2021-06-23 14:40:05','2021-06-23 14:40:05','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"d46_62c_9ebc_93\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"divId\":\"d46_62c_9ebc_93\",\"name\":\"单分割符无标题\",\"positionX\":290,\"positionY\":74,\"type\":\"tasks\"},{\"id\":\"8a0_80a_a3ff_9c\",\"etl_task_id\":\"3\",\"etl_context\":\"优化多源任务测试\",\"more_task\":\"多源ETL\",\"depend_level\":\"2\",\"divId\":\"8a0_80a_a3ff_9c\",\"name\":\"优化多源任务测试\",\"positionX\":304,\"positionY\":206,\"type\":\"tasks\"},{\"id\":\"777_16e_9ae3_c1\",\"etl_context\":\"hostname\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"777_16e_9ae3_c1\",\"name\":\"hostname\",\"positionX\":593,\"positionY\":175,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_12\",\"pageSourceId\":\"d46_62c_9ebc_93\",\"pageTargetId\":\"8a0_80a_a3ff_9c\"},{\"connectionId\":\"con_21\",\"pageSourceId\":\"d46_62c_9ebc_93\",\"pageTargetId\":\"777_16e_9ae3_c1\"}]}','off','off','off','off','off','off'),('858395317454770176','测试flink',NULL,'ETL','2021-06-26 09:12:08','2021-06-26 09:12:08','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"1d5_4fd_bb4a_6e\",\"etl_task_id\":\"858391270026907648\",\"etl_context\":\"第一个FLINK_SQL\",\"more_task\":\"FLINK\",\"depend_level\":\"0\",\"divId\":\"1d5_4fd_bb4a_6e\",\"name\":\"第一个FLINK_SQL\",\"positionX\":318,\"positionY\":113,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('874623112065323008','测试禁用rm',NULL,'ETL','2021-08-10 03:59:09','2021-08-10 03:59:09','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5','on',NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"ee9_cdf_a2f7_6e\",\"etl_context\":\"测试删除\",\"depend_level\":\"0\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"rm -rf /home/zyc/aaaaaaaaaa\",\"time_out\":\"86400\",\"divId\":\"ee9_cdf_a2f7_6e\",\"name\":\"测试删除\",\"positionX\":280,\"positionY\":265,\"type\":\"shell\"},{\"id\":\"cda_df4_b62a_38\",\"etl_context\":\"ping 127.0.0.1\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"ping 127.0.0.1 -n 10000\",\"time_out\":\"10\",\"divId\":\"cda_df4_b62a_38\",\"name\":\"ping 127.0.0.1\",\"positionX\":249,\"positionY\":70,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"cda_df4_b62a_38\",\"pageTargetId\":\"ee9_cdf_a2f7_6e\"}]}','off','off','off','off','off','off'),('886576411345686528','离线flink测试',NULL,'ETL','2021-09-12 03:37:21','2021-09-12 03:37:21','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"69b_003_b845_0a\",\"etl_task_id\":\"886575336517537792\",\"etl_context\":\"第二个FLINK_SQL\",\"more_task\":\"FLINK\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"69b_003_b845_0a\",\"name\":\"第二个FLINK_SQL\",\"positionX\":274,\"positionY\":82,\"type\":\"tasks\"}],\"line\":[]}','off','off','off','off','off','off'),('887459213607964672','测试ssh',NULL,'ETL','2021-09-14 14:05:34','2021-09-14 14:05:34','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"aa5_671_8827_a1\",\"etl_task_id\":\"749064500069535744\",\"etl_context\":\"123\",\"more_task\":\"SSH\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"time_out\":\"86400\",\"divId\":\"aa5_671_8827_a1\",\"name\":\"123\",\"positionX\":354,\"positionY\":86,\"type\":\"tasks\"}],\"line\":[]}','on','off','on','on','on','on'),('889178494473342976','本地shell',NULL,'ETL','2021-09-19 07:57:22','2021-09-19 07:57:22','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"7c0_dc9_ba05_e1\",\"etl_context\":\"hostname\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"time_out\":\"86400\",\"divId\":\"7c0_dc9_ba05_e1\",\"name\":\"hostname\",\"positionX\":330,\"positionY\":80,\"type\":\"shell\"}],\"line\":[]}','on','off','on','on','on','on'),('889233499129647104','tttt',NULL,'ETL','2021-09-19 11:35:54','2021-09-19 11:35:54','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"4d3_df1_93ac_b9\",\"etl_task_id\":\"889232742397513728\",\"etl_context\":\"tttt\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"4d3_df1_93ac_b9\",\"name\":\"tttt\",\"positionX\":360,\"positionY\":112,\"type\":\"tasks\"}],\"line\":[]}','on','off','on','on','on','on'),('890303240988528640','测试aaa',NULL,'ETL','2021-09-22 10:25:53','2021-09-22 10:25:53','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"d78_85c_899f_dc\",\"etl_task_id\":\"719629297702146048\",\"etl_context\":\"单分割符自带标题\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"d78_85c_899f_dc\",\"name\":\"单分割符自带标题\",\"positionX\":294,\"positionY\":337,\"type\":\"tasks\"},{\"id\":\"39e_1e4_bd61_b0\",\"etl_context\":\"hostname\",\"depend_level\":\"0\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"hostname\",\"time_out\":\"86400\",\"divId\":\"39e_1e4_bd61_b0\",\"name\":\"hostname\",\"positionX\":273,\"positionY\":155,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"39e_1e4_bd61_b0\",\"pageTargetId\":\"d78_85c_899f_dc\"}]}','off','off','on','on','on','off'),('903440241472311296','测试ssh',NULL,'ETL','2021-10-28 16:18:45','2021-10-28 16:18:45','1d','1','0',0,NULL,'',NULL,'2021-10-28 16:18:45',NULL,'1d','remove',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"541_dc1_810c_e9\",\"etl_context\":\"ping 3\",\"depend_level\":\"0\",\"is_script\":\"false\",\"is_disenable\":\"false\",\"command\":\"ping -n 10 127.0.0.1\",\"time_out\":\"86400\",\"divId\":\"541_dc1_810c_e9\",\"name\":\"ping 3\",\"positionX\":448,\"positionY\":87,\"type\":\"shell\"}],\"line\":[]}','off','off','on','on','on','on'),('904327925656981504','FLINK_SQL_CDC测试',NULL,'ETL','2021-10-31 03:15:49','2021-10-31 03:15:49','1d','1','0',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"64a_b73_9baa_54\",\"etl_task_id\":\"904327604331352064\",\"etl_context\":\"第三个FLINK_SQL_CDC\",\"more_task\":\"FLINK\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"64a_b73_9baa_54\",\"name\":\"第三个FLINK_SQL_CDC\",\"positionX\":81,\"positionY\":221,\"type\":\"tasks\"},{\"id\":\"7db_633_a824_43\",\"etl_task_id\":\"886575336517537792\",\"etl_context\":\"第二个FLINK_SQL\",\"more_task\":\"FLINK\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"7db_633_a824_43\",\"name\":\"第二个FLINK_SQL\",\"positionX\":78,\"positionY\":46,\"type\":\"tasks\"},{\"id\":\"a91_fe7_8ee4_91\",\"etl_context\":\"hostname\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"time_out\":\"86400\",\"divId\":\"a91_fe7_8ee4_91\",\"name\":\"hostname\",\"positionX\":128,\"positionY\":363,\"type\":\"shell\"},{\"id\":\"33f_843_bd51_42\",\"etl_context\":\"hostname2\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"time_out\":\"86400\",\"divId\":\"33f_843_bd51_42\",\"name\":\"hostname2\",\"positionX\":429,\"positionY\":416,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_13\",\"pageSourceId\":\"7db_633_a824_43\",\"pageTargetId\":\"64a_b73_9baa_54\"},{\"connectionId\":\"con_15\",\"pageSourceId\":\"64a_b73_9baa_54\",\"pageTargetId\":\"a91_fe7_8ee4_91\"},{\"connectionId\":\"con_24\",\"pageSourceId\":\"a91_fe7_8ee4_91\",\"pageTargetId\":\"33f_843_bd51_42\"}]}','off','off','on','on','on','on'),('904448275493425152','windows_flink_集群测试',NULL,'ETL','2021-10-31 11:13:46','2021-10-31 11:13:46','1d','1','1',0,NULL,'',NULL,NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"07a_5c8_92de_1d\",\"etl_task_id\":\"904448033733742592\",\"etl_context\":\"第二个FLINK_SQL_Window版本\",\"more_task\":\"FLINK\",\"depend_level\":\"0\",\"is_disenable\":\"true\",\"time_out\":\"86400\",\"divId\":\"07a_5c8_92de_1d\",\"name\":\"第二个FLINK_SQL_Window版本\",\"positionX\":278,\"positionY\":68,\"type\":\"tasks\"},{\"id\":\"46e_182_ab5a_ba\",\"etl_task_id\":\"904464410402099200\",\"etl_context\":\"第三个FLINK_SQL_CDC_Windows版本\",\"more_task\":\"FLINK\",\"depend_level\":\"0\",\"is_disenable\":\"false\",\"time_out\":\"86400\",\"divId\":\"46e_182_ab5a_ba\",\"name\":\"第三个FLINK_SQL_CDC_Windows版本\",\"positionX\":341,\"positionY\":208,\"type\":\"tasks\"}],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"07a_5c8_92de_1d\",\"pageTargetId\":\"46e_182_ab5a_ba\"}]}','off','off','on','on','on','on'),('906543220496797697','',NULL,'EMAIL','2021-11-06 05:59:06','2021-11-06 05:59:06',NULL,'3','-1',0,'',NULL,NULL,NULL,NULL,'15s','running',NULL,NULL,NULL,'email',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'86400','5',NULL,'off',NULL,NULL,'off','off','off','off','off','off'),('906543220572295169','',NULL,'RETRY','2021-11-06 05:59:06','2021-11-06 05:59:06',NULL,'3','-1',0,'',NULL,NULL,NULL,NULL,'1s','running',NULL,NULL,NULL,'retry',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'86400','5',NULL,'off',NULL,NULL,'off','off','off','off','off','off'),('906543220626821121','',NULL,'CHECK','2021-11-06 05:59:06','2021-11-06 05:59:06',NULL,'3','-1',0,'',NULL,NULL,NULL,NULL,'5s','running',NULL,NULL,NULL,'check',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'86400','5',NULL,'off',NULL,NULL,'off','off','off','off','off','off'),('906547945187315712','测试专用',NULL,'ETL','2021-11-06 06:16:11','2024-11-06 06:16:11','1d','1','0',0,NULL,'',NULL,'2021-11-06 06:16:11',NULL,'0 0 0 * * ? *','running',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5','2021-11-06 16:00:00','off','','{\"tasks\":[{\"id\":\"420_a9f_9e80_ae\",\"etl_context\":\"测试shell\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"time_out\":\"86400\",\"divId\":\"420_a9f_9e80_ae\",\"name\":\"测试shell\",\"positionX\":258,\"positionY\":60,\"type\":\"shell\"},{\"id\":\"c04_40a_83c7_ee\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"c04_40a_83c7_ee\",\"name\":\"单分割符无标题\",\"positionX\":256,\"positionY\":159,\"type\":\"tasks\"}],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"420_a9f_9e80_ae\",\"pageTargetId\":\"c04_40a_83c7_ee\"}]}','off','off','on','on','on','on');
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
  `text` varchar(1024) DEFAULT NULL,
  `level` varchar(10) DEFAULT NULL COMMENT '层级',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `icon` varchar(200) DEFAULT NULL,
  `resource_desc` varchar(10) DEFAULT NULL COMMENT '资源说明',
  `order` varchar(200) DEFAULT NULL,
  `is_enable` varchar(10) DEFAULT NULL COMMENT '是否启用',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `url` text DEFAULT NULL COMMENT 'url链接',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '1:目录,2:菜单,3:函数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=906964874968436737 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource_tree_info`
--

LOCK TABLES `resource_tree_info` WRITE;
/*!40000 ALTER TABLE `resource_tree_info` DISABLE KEYS */;
INSERT INTO `resource_tree_info` VALUES (802848818109353984,'#','ZDH','1','1','fa fa-folder',NULL,'1',NULL,'2021-01-24 02:34:34','2021-01-31 00:43:35','','1'),(802850170428461056,'802848818109353984','总监控','2','1','fa fa-bar-chart',NULL,'1',NULL,'2021-01-24 02:34:34','2021-01-30 13:56:12','monitor.html','2'),(802852358580080640,'802848818109353984','ETL采集','2','1','fa fa-tasks',NULL,'2',NULL,'2021-01-24 02:34:34','2021-10-13 16:31:36','','1'),(802876953722884096,'802848818109353984','数据质量报告','2','1','fa fa-hourglass-half',NULL,'4',NULL,'2021-01-24 02:34:34','2021-01-30 14:24:43','quality_index.html','2'),(802918652050411520,'802852358580080640','数据源管理','3','1','non',NULL,'1',NULL,'2021-01-24 02:34:34','2021-01-30 14:31:11','data_sources_index.html','2'),(802918760057933824,'802852358580080640','ETL任务','3','1','non',NULL,'2',NULL,'2021-01-24 02:34:34','2021-01-30 14:31:14','etl_task_index.html','2'),(802919044364636160,'802852358580080640','多源ETL任务','3','1','non',NULL,'3',NULL,'2021-01-24 02:34:34','2021-01-30 14:31:17','etl_task_more_sources_index.html','2'),(802919157430489088,'802852358580080640','SPARK_SQL任务','3','1','non',NULL,'4',NULL,'2021-01-24 02:34:34','2021-06-26 02:33:48','sql_task_index.html','2'),(802930870510948352,'802852358580080640','Drools任务','3','1','non',NULL,'5',NULL,'2021-01-24 02:34:34','2021-01-30 14:31:30','etl_task_drools_index.html','2'),(802931116691427328,'802852358580080640','SSH任务','3','1','non',NULL,'6',NULL,'2021-01-24 02:34:34','2021-01-30 14:31:33','etl_task_ssh_index.html','2'),(802931308593418240,'802852358580080640','调度ETL','3','1','non',NULL,'100',NULL,'2021-01-24 02:34:34','2021-06-26 07:05:40','dispatch_task_index.html','2'),(802931697527033856,'802848818109353984','下载管理','2','1','fa fa-download',NULL,'3',NULL,'2021-01-24 02:34:34','2021-01-30 14:24:39','download_index.html','2'),(802932157390524416,'802848818109353984','指标查询','2','1','fa fa-columns',NULL,'5',NULL,'2021-01-24 02:34:34','2021-01-30 14:24:46','quota_index.html','2'),(802932355596554240,'802848818109353984','血缘分析','2','1','fa fa-asterisk',NULL,'6',NULL,'2021-01-24 02:34:34','2021-02-14 13:50:12','search_data_index.html','2'),(802932548165439488,'802848818109353984','权限管理','2','1','fa fa-cog',NULL,'7',NULL,'2021-01-24 02:34:34','2021-10-02 02:49:34','','1'),(802932890089295872,'802848818109353984','使用技巧','2','1','fa fa-hand-o-right',NULL,'17',NULL,'2021-01-24 02:34:34','2021-02-13 05:21:29','zdh_help.index','2'),(802933021148712960,'802848818109353984','Cron','2','1','fa fa-glass',NULL,'9',NULL,'2021-01-24 02:34:34','2021-01-30 14:24:58','cron.html','2'),(802933165302747136,'802848818109353984','README','2','1','fa fa-info',NULL,'18',NULL,'2021-01-24 02:34:34','2021-02-13 05:21:37','readme.html','2'),(805193674668380160,'802848818109353984','设置','2','1','fa fa-user',NULL,'15',NULL,'2021-01-30 13:52:12','2021-01-31 00:39:40','','1'),(805357642351382528,'802848818109353984','测试2','2','1','',NULL,'',NULL,'2021-01-31 00:43:45','2021-01-31 01:26:36','','1'),(805369519538180096,'805193674668380160','文件服务器','3','1','fa fa-file',NULL,'1',NULL,'2021-01-31 01:30:57','2021-01-31 01:47:06','file_manager.html','2'),(805372907965386752,'805193674668380160','同步元数据','3','1','fa fa-circle-o',NULL,'3',NULL,'2021-01-31 01:44:24','2021-01-31 05:51:20','function:load_meta_databases()','4'),(805374183432261632,'805193674668380160','用户中心','3','1','fa fa-user',NULL,'0',NULL,'2021-01-31 01:49:28','2021-06-12 00:37:08','user.html','2'),(805431924678987776,'805193674668380160','生成监控任务','3','1','fa fa-gavel',NULL,'7',NULL,'2021-01-31 05:38:55','2021-01-31 05:52:20','function:del_system_job()','4'),(805531084459610112,'802848818109353984','升级扩容','2','1','fa fa-cloud',NULL,'16',NULL,'2021-01-31 12:12:57','2021-02-14 14:31:08','','1'),(808616077255774208,'805531084459610112','部署管理','3','1','fa fa-linux',NULL,'3',NULL,'2021-02-09 00:31:36','2021-02-12 12:23:56','server_manager_index.html','2'),(808616254788079616,'805531084459610112','WEB管理','3','1','fa fa-windows',NULL,'1',NULL,'2021-02-09 00:32:19','2021-02-09 00:32:35','consanguin_index.html','2'),(809886572093640704,'805193674668380160','通知管理','3','1','fa fa-comments',NULL,'9',NULL,'2021-02-12 12:40:06','2021-02-12 12:40:21','notice_update_index.html','2'),(810816897514737664,'810817759893000192','数据集市','3','1','fa fa-database',NULL,'1',NULL,'2021-02-15 02:16:53','2021-02-15 06:54:32','data_ware_house_index.html','2'),(810817759893000192,'802848818109353984','数仓管理','2','1','fa fa-database','','16','1','2021-02-15 02:20:18','2021-02-15 02:20:18','','1'),(810825569494110208,'810817759893000192','数据发布','3','1','fa fa-send','','3','1','2021-02-15 02:51:20','2021-02-15 02:51:20','data_issue_index.html','2'),(830556376391487488,'802848818109353984','提BUG','2','1','fa fa-pencil','','20','1','2021-04-10 13:34:31','2021-10-30 05:39:10','mail_compose.html','2'),(838335003375964160,'810817759893000192','申请明细','3','1','fa fa-truck','','3','1','2021-05-02 00:44:00','2021-10-30 10:55:46','data_apply_index','2'),(839059609225269248,'891283647431184384','我的审批','3','1','fa fa-gg','','5','1','2021-05-04 00:43:20','2021-10-20 14:58:41','process_flow_index','2'),(839152432125579264,'802852358580080640','发布源ETL','3','1','non',NULL,'7',NULL,'2021-05-04 06:52:11','2021-05-04 06:54:19','etl_task_apply_index.html','2'),(858320387178500096,'802852358580080640','FLINK_SQL','3','1','non','','8','1','2021-06-26 04:18:47','2021-06-26 04:18:47','etl_task_flink_index.html','2'),(889247298196869120,'802848818109353984','信息管理','2','1','fa fa-envelope',NULL,'4',NULL,'2021-09-19 12:31:17','2021-09-19 12:32:09','notice_index.html','2'),(891283647431184384,'802848818109353984','审批管理','2','1','fa fa-exchange',NULL,'6',NULL,'2021-09-25 03:23:01','2021-10-04 02:01:54','','1'),(893810193274507264,'802932548165439488','菜单配置','3','1','fa fa-cog',NULL,'1',NULL,'2021-10-02 02:42:36','2021-10-03 08:00:22','permission_add_index.html','2'),(893816925920956416,'802932548165439488','角色配置','3','1','fa fa-cog',NULL,'2',NULL,'2021-10-02 03:09:21','2021-10-03 03:03:05','role_index.html','2'),(893817125867622400,'802932548165439488','用户配置','3','1','fa fa-cog',NULL,'3',NULL,'2021-10-02 03:10:09','2021-10-02 03:39:25','user_index.html','2'),(894524363561242624,'891283647431184384','审批节点','3','1','fa fa-exchange',NULL,'1',NULL,'2021-10-04 02:00:28','2021-10-04 03:05:34','approval_config_index','2'),(894896876246011904,'891283647431184384','审批人配置','3','1','fa fa-exchange',NULL,'2',NULL,'2021-10-05 02:40:42','2021-10-05 02:41:15','approval_auditor_index','2'),(896164931303378944,'891283647431184384','事件配置','3','1','fa fa-exchange',NULL,'3',NULL,'2021-10-08 14:39:30','2021-10-12 14:33:02','approval_event_index','2'),(898333103779483648,'802918652050411520','查询当前用户数据源','4','1','fa fa-coffee',NULL,'1',NULL,'2021-10-14 14:15:02','2021-10-14 14:22:21','data_sources_list','5'),(898334091781345280,'802918652050411520','新增数据源','4','1','fa fa-coffee','','2','1','2021-10-14 14:18:58','2021-10-14 14:18:58','add_data_sources','5'),(898334268604813312,'802918652050411520','删除数据源','4','1','fa fa-coffee','','3','1','2021-10-14 14:19:40','2021-10-14 14:19:40','data_sources_delete','5'),(898334486809284608,'802918652050411520','更新数据源','4','1','fa fa-coffee','','2','1','2021-10-14 14:20:32','2021-10-18 15:13:54','data_sources_update','5'),(898335485523398656,'802918652050411520','查询按钮','4','1','fa fa-coffee','','5','1','2021-10-14 14:24:30','2021-10-14 14:24:30','data_sources_list2','5'),(898335839925309440,'802918760057933824','查询按钮','4','1','fa fa-coffee','','1','1','2021-10-14 14:25:55','2021-10-14 14:25:55','etl_task_list2','5'),(898336112890613760,'802918760057933824','新增ETL任务','4','1','fa fa-coffee','','2','1','2021-10-14 14:27:00','2021-10-14 14:27:00','etl_task_add','5'),(898336252711931904,'802918760057933824','删除ETL任务','4','1','fa fa-coffee','','3','1','2021-10-14 14:27:33','2021-10-14 14:27:33','etl_task_delete','5'),(898336446329393152,'802918760057933824','更新ETL任务','4','1','fa fa-coffee','','4','1','2021-10-14 14:28:19','2021-10-14 14:28:19','etl_task_update','5'),(898336838400348160,'802919044364636160','查询按钮','4','1','fa fa-coffee','','1','1','2021-10-14 14:29:53','2021-10-14 14:29:53','etl_task_more_list2','5'),(898337064490110976,'802919044364636160','新增多源ETL任务','4','1','fa fa-coffee','','2','1','2021-10-14 14:30:46','2021-10-14 14:30:46','etl_task_more_sources_add','5'),(898337228843913216,'802919044364636160','删除多源ETL任务','4','1','fa fa-coffee','','3','1','2021-10-14 14:31:26','2021-10-14 14:31:26','etl_task_more_sources_delete','5'),(898337404199374848,'802919044364636160','更新多源ETL任务','4','1','fa fa-coffee','','4','1','2021-10-14 14:32:07','2021-10-14 14:32:07','etl_task_more_update','5'),(898337767392546816,'802919157430489088','SparkSql任务查询按钮','4','1','fa fa-coffee','','1','1','2021-10-14 14:33:34','2021-10-14 14:33:34','sql_task_list','5'),(898337973207044096,'802919157430489088','新增SparkSql任务','4','1','fa fa-coffee','','2','1','2021-10-14 14:34:23','2021-10-14 14:34:23','sql_task_add','5'),(898338119047188480,'802919157430489088','删除SparkSql任务','4','1','fa fa-coffee','','3','1','2021-10-14 14:34:58','2021-10-14 14:34:58','sql_task_delete','5'),(898338251511697408,'802919157430489088','更新SparkSql任务','4','1','fa fa-coffee','','4','1','2021-10-14 14:35:29','2021-10-16 15:54:00','sql_task_update','5'),(898338570987638784,'802931116691427328','SSH任务查询按钮','4','1','fa fa-coffee','','1','1','2021-10-14 14:36:46','2021-10-14 14:36:46','etl_task_ssh_list','5'),(898338819265269760,'802931116691427328','新增SSH任务','4','1','fa fa-coffee','','2','1','2021-10-14 14:37:45','2021-10-14 14:37:45','etl_task_ssh_add','5'),(898338990552256512,'802931116691427328','删除SSH任务','4','1','fa fa-coffee','','3','1','2021-10-14 14:38:26','2021-10-14 14:38:26','etl_task_ssh_delete','5'),(898339248917188608,'802931116691427328','更新SSH任务','4','1','fa fa-coffee','','4','1','2021-10-14 14:39:27','2021-10-14 14:39:27','etl_task_ssh_update','5'),(898339478811185152,'858320387178500096','FlinkSql任务查询按钮','4','1','fa fa-coffee','','1','1','2021-10-14 14:40:22','2021-10-14 14:40:22','etl_task_flink_list','5'),(898339672948740096,'858320387178500096','新增FlinkSql任务','4','1','fa fa-coffee','','2','1','2021-10-14 14:41:08','2021-10-14 14:41:08','etl_task_flink_add','5'),(898339790284394496,'858320387178500096','删除FlinkSql任务','4','1','fa fa-coffee','','3','1','2021-10-14 14:41:36','2021-10-14 14:41:36','etl_task_flink_delete','5'),(898339903127949312,'858320387178500096','更新FlinkSql任务','4','1','fa fa-coffee','','4','1','2021-10-14 14:42:03','2021-10-14 14:42:03','etl_task_flink_update','5'),(898340104257409024,'802931308593418240','调度任务查询按钮','4','1','fa fa-coffee','','1','1','2021-10-14 14:42:51','2021-10-14 14:42:51','dispatch_task_list2','5'),(898340234041757696,'802931308593418240','新增调度任务','4','1','fa fa-coffee',NULL,'2',NULL,'2021-10-14 14:43:22','2021-10-14 14:44:39','dispatch_task_group_add','5'),(898340346117754880,'802931308593418240','删除调度任务','4','1','fa fa-coffee','','3','1','2021-10-14 14:43:49','2021-10-30 09:27:43','dispatch_task_group_delete','5'),(898341132600086528,'802931308593418240','更新调度任务','4','1','fa fa-coffee',NULL,'4',NULL,'2021-10-14 14:46:56','2021-10-14 14:55:04','dispatch_task_group_update','5'),(898919578183143424,'802931697527033856','下载管理-查询按钮','3','1','fa fa-coffee','','1','1','2021-10-16 05:05:29','2021-10-16 05:05:29','download_list','5'),(898919751642779648,'802931697527033856','下载管理-删除','3','1','fa fa-coffee','','2','1','2021-10-16 05:06:10','2021-10-16 05:06:10','download_delete','5'),(898919884019208192,'802931697527033856','下载管理-下载文件','3','1','fa fa-coffee','','3','1','2021-10-16 05:06:41','2021-10-16 05:06:41','download_file','5'),(898921890200948736,'802876953722884096','质量报告-查询按钮','3','1','fa fa-coffee','','1','1','2021-10-16 05:14:40','2021-10-16 05:14:40','quality_list','5'),(898922054793826304,'802876953722884096','质量报告-删除','3','1','fa fa-coffee','','2','1','2021-10-16 05:15:19','2021-10-16 05:15:19','quality_delete','5'),(898922698124562432,'889247298196869120','信息管理-查询','3','1','fa fa-coffee','','1','1','2021-10-16 05:17:52','2021-10-16 05:18:12','notice_list2','5'),(898923212249763840,'889247298196869120','信息管理-删除','3','1','fa fa-coffee','','2','1','2021-10-16 05:19:55','2021-10-16 05:19:55','notice_delete','5'),(898923484506230784,'889247298196869120','信息管理-信息内容','3','1','fa fa-coffee','','2','1','2021-10-16 05:21:00','2021-10-16 05:21:00','notice_message','5'),(898923625090912256,'889247298196869120','信息管理-明细页面','3','1','fa fa-coffee','','4','1','2021-10-16 05:21:33','2021-10-16 05:21:33','notice_detail_index','3'),(898924030910795776,'894524363561242624','审批节点-查询','4','1','fa fa-coffee','','1','1','2021-10-16 05:23:10','2021-10-16 05:23:10','approval_config_list','5'),(898924424034521088,'894524363561242624','审批节点-新增','4','1','fa fa-coffee','','2','1','2021-10-16 05:24:44','2021-10-16 05:24:44','approval_config_add','5'),(898924549347741696,'894524363561242624','审批节点-更新','4','1','fa fa-coffee','','3','1','2021-10-16 05:25:14','2021-10-16 05:25:14','approval_config_update','5'),(898924812993302528,'894524363561242624','审批节点-新增页面','4','1','fa fa-coffee','','4','1','2021-10-16 05:26:17','2021-10-16 05:26:17','approval_config_add_index','3'),(898925461227180032,'894896876246011904','审批人配置-查询','4','1','fa fa-coffee','','1','1','2021-10-16 05:28:51','2021-10-16 05:28:51','approval_auditor_list','5'),(898925640273629184,'894896876246011904','审批人配置-新增','4','1','fa fa-coffee','','2','1','2021-10-16 05:29:34','2021-10-16 05:29:34','approval_auditor_add','5'),(898925753096212480,'894896876246011904','审批人配置-更新','4','1','fa fa-coffee','','3','1','2021-10-16 05:30:01','2021-10-16 05:30:01','approval_auditor_update','5'),(898926933432078336,'894896876246011904','审批人配置-新增页面','4','1','fa fa-coffee','','4','1','2021-10-16 05:34:42','2021-10-16 05:34:42','approval_auditor_add_index','3'),(898992327668797440,'896164931303378944','事件配置-查询','4','1','fa fa-coffee','','1','1','2021-10-16 09:54:33','2021-10-16 09:54:33','approval_event_list','5'),(898992469134282752,'896164931303378944','事件配置-新增','4','1','fa fa-coffee','','2','1','2021-10-16 09:55:07','2021-10-16 09:55:07','approval_event_add','5'),(898992569562697728,'896164931303378944','事件配置-更新','4','1','fa fa-coffee','','3','1','2021-10-16 09:55:31','2021-10-16 09:55:31','approval_event_update','5'),(898992688609628160,'896164931303378944','事件配置-新增页面','4','1','fa fa-coffee','','4','1','2021-10-16 09:55:59','2021-10-16 09:55:59','approval_event_add_index','3'),(898994298257674240,'893810193274507264','菜单配置-查询','4','1','fa fa-coffee','','1','1','2021-10-16 10:02:23','2021-10-16 10:02:23','jstree_node','5'),(898994643360813056,'893810193274507264','菜单配置-双击目录','4','1','fa fa-coffee','','2','1','2021-10-16 10:03:45','2021-10-16 10:03:45','jstree_get_node','5'),(898994850660093952,'893810193274507264','菜单配置-节点更新','4','1','fa fa-coffee','','3','1','2021-10-16 10:04:35','2021-10-16 10:04:35','jstree_update_node','5'),(898995002837831680,'893810193274507264','菜单配置-新增节点','4','1','fa fa-coffee','','4','1','2021-10-16 10:05:11','2021-10-16 10:05:11','jstree_add_node','5'),(898995219188420608,'893810193274507264','菜单配置-节点删除','4','1','fa fa-coffee','','5','1','2021-10-16 10:06:03','2021-10-16 10:06:03','jstree_del_node','5'),(898995513087496192,'893810193274507264','菜单配置-父节点更新','4','1','fa fa-coffee','','6','1','2021-10-16 10:07:13','2021-10-16 10:07:13','jstree_update_parent','5'),(898996218540068864,'893816925920956416','角色配置-查询','4','1','fa fa-coffee','','1','1','2021-10-16 10:10:01','2021-10-16 10:10:01','role_list','5'),(898996945933045760,'893816925920956416','角色配置-新增页面','4','1','fa fa-coffee','','2','1','2021-10-16 10:12:54','2021-10-16 10:12:54','role_add_index','3'),(898997770537406464,'893816925920956416','角色配置-新增','4','1','fa fa-coffee','','3','1','2021-10-16 10:16:11','2021-10-16 10:16:11','jstree_add_permission','5'),(898998045511782400,'893816925920956416','角色配置-禁用/启用','4','1','fa fa-coffee','','4','1','2021-10-16 10:17:17','2021-10-16 10:17:33','role_enable','5'),(898998299615301632,'893816925920956416','角色配置-明细','4','1','fa fa-coffee','','5','1','2021-10-16 10:18:17','2021-10-16 10:18:17','role_detail','5'),(898998799848968192,'893817125867622400','用户配置-查询','4','1','fa fa-coffee','','1','1','2021-10-16 10:20:16','2021-10-16 10:20:16','user_list','5'),(898999065830756352,'893817125867622400','用户配置-启用/禁用','4','1','fa fa-coffee','','2','1','2021-10-16 10:21:20','2021-10-16 10:21:20','user_enable','5'),(898999773028159488,'893817125867622400','用户配置-新增/更新','4','1','fa fa-coffee','','3','1','2021-10-16 10:24:08','2021-10-16 10:24:08','user_update','5'),(898999958684831744,'893817125867622400','用户配置-新增用户组','4','1','fa fa-coffee','','4','1','2021-10-16 10:24:53','2021-10-16 10:24:53','user_group_add','5'),(899000154885984256,'893817125867622400','用户配置-用户明细页面','4','1','fa fa-coffee','','5','1','2021-10-16 10:25:40','2021-10-16 10:25:40','user_add_index','3'),(899000274331373568,'893817125867622400','用户配置-用户明细','4','1','fa fa-coffee','','6','1','2021-10-16 10:26:08','2021-10-16 10:26:08','user_detail','5'),(899000507769556992,'893817125867622400','用户配置-新增页面','4','1','fa fa-coffee','','7','1','2021-10-16 10:27:04','2021-10-16 10:27:04','user_add_index','3'),(899000798854254592,'802933021148712960','Cron查询','3','1','fa fa-coffee','','1','1','2021-10-16 10:28:13','2021-10-16 10:28:13','quartz-cron.json','5'),(899001510107549696,'805374183432261632','用户中心-更新','4','1','fa fa-coffee','','1','1','2021-10-16 10:31:03','2021-10-16 10:31:03','user','5'),(899001733512957952,'805374183432261632','用户中心-查询','4','1','fa fa-coffee','','2','1','2021-10-16 10:31:56','2021-10-16 10:31:56','getUserInfo','5'),(899001982079995904,'805369519538180096','文件服务器-查询','4','1','fa fa-coffee','','1','1','2021-10-16 10:32:55','2021-10-16 10:32:55','getFileManager','5'),(899002142675701760,'805369519538180096','文件服务器-更新','4','1','fa fa-coffee','','2','1','2021-10-16 10:33:33','2021-10-16 10:33:33','file_manager_up','5'),(899059721481228288,'809886572093640704','通知管理-新增','4','1','fa fa-coffee','','1','1','2021-10-16 14:22:21','2021-10-16 14:22:21','notice_update','5'),(899076126133981184,'808616077255774208','部署管理-新增模板','4','1','fa fa-coffee','','1','1','2021-10-16 15:27:32','2021-10-16 15:27:32','server_add','5'),(899076266156625920,'808616077255774208','部署管理-更新模板','4','1','fa fa-coffee','','2','1','2021-10-16 15:28:06','2021-10-16 15:28:06','server_update','5'),(899076393432780800,'808616077255774208','部署管理-新增模板页面','4','1','fa fa-coffee','','3','1','2021-10-16 15:28:36','2021-10-16 15:28:36','server_add_index','3'),(899076708504702976,'808616077255774208','部署管理-一键部署页面','4','1','fa fa-coffee','','4','1','2021-10-16 15:29:51','2021-10-16 15:29:51','server_build_exe_detail_index','3'),(899076830349234176,'808616077255774208','部署管理-一键部署','4','1','fa fa-coffee','','5','1','2021-10-16 15:30:20','2021-10-16 15:30:20','server_setup','5'),(899077318159372288,'808616077255774208','部署管理-server查询','4','1','fa fa-coffee','','6','1','2021-10-16 15:32:17','2021-10-16 15:32:17','server_manager_online_list','5'),(899077560292347904,'808616077255774208','部署管理-逻辑上线/下线','4','1','fa fa-coffee','','8','1','2021-10-16 15:33:14','2021-10-16 15:33:14','server_manager_online_update','5'),(899078834731618304,'810816897514737664','数据集市-查询','4','1','fa fa-coffee','','1','1','2021-10-16 15:38:18','2021-10-16 15:38:18','data_ware_house_list2','5'),(899079195127189504,'810816897514737664','数据集市-明细页面','4','1','fa fa-coffee','','2','1','2021-10-16 15:39:44','2021-10-30 11:15:12','data_ware_house_detail_index','3'),(899079347393007616,'810816897514737664','数据集市-明细','4','1','fa fa-coffee','','3','1','2021-10-16 15:40:21','2021-10-16 15:40:21','data_ware_house_list','5'),(899079618982580224,'810816897514737664','数据集市-申请','4','1','fa fa-coffee','','4','1','2021-10-16 15:41:25','2021-10-16 15:41:25','data_apply_add','5'),(899079911518507008,'810825569494110208','数据发布-查询','4','1','fa fa-coffee','','1','1','2021-10-16 15:42:35','2021-10-16 15:42:35','data_ware_house_list3','5'),(899080146999316480,'810825569494110208','数据发布-发布页面','4','1','fa fa-coffee','','2','1','2021-10-16 15:43:31','2021-10-16 15:43:31','data_issue_add_index','3'),(899081132492984320,'810825569494110208','数据发布-发布','4','1','fa fa-coffee','','3','1','2021-10-16 15:47:26','2021-10-16 15:47:26','issue_data_add','5'),(899081270330396672,'810825569494110208','数据发布-更新','4','1','fa fa-coffee','','4','1','2021-10-16 15:47:59','2021-10-16 15:47:59','issue_data_update','5'),(899081439826415616,'810825569494110208','数据发布-删除','4','1','fa fa-coffee','','5','1','2021-10-16 15:48:39','2021-10-16 15:48:39','issue_data_delete','5'),(899083124191793152,'802850170428461056','总监控-任务状态查询','3','1','fa fa-coffee','','1','1','2021-10-16 15:55:21','2021-10-16 15:55:21','task_group_log_instance_list2','5'),(899083287929032704,'802850170428461056','总监控-调度任务查询','3','1','fa fa-coffee','','2','1','2021-10-16 15:56:00','2021-10-16 15:56:00','getScheduleTask','5'),(899083494326538240,'802850170428461056','总监控-调度任务总计查询','3','1','fa fa-coffee','','3','1','2021-10-16 15:56:49','2021-10-16 15:56:49','etlEcharts','5'),(899083652330164224,'802850170428461056','总监控-调度总计','3','1','fa fa-coffee','','4','1','2021-10-16 15:57:27','2021-10-16 15:57:27','getTotalNum','5'),(899793046982365184,'802848818109353984','查询通知','2','1','fa fa-coffee','','110','1','2021-10-18 14:56:20','2021-10-18 15:57:02','notice_list','5'),(899794270502785024,'893810193274507264','菜单配置-新增节点页面','4','1','fa fa-coffee','','7','1','2021-10-18 15:01:11','2021-10-18 15:01:11','jstree_add_index','3'),(899794938370199552,'893816925920956416','角色配置-查询菜单','4','1','fa fa-coffee','','6','1','2021-10-18 15:03:51','2021-10-18 15:03:51','jstree_permission_list','5'),(899796100964159488,'893816925920956416','角色配置-用户拥有权菜单列表','4','1','fa fa-coffee','','7','1','2021-10-18 15:08:28','2021-10-18 15:08:28','jstree_permission_list2','5'),(899796275644338176,'802848818109353984','系统通知','2','1','fa fa-coffee','','111','1','2021-10-18 15:09:10','2021-10-18 15:09:10','every_day_notice','5'),(899796487305695232,'802848818109353984','首页','2','1','fa fa-coffee','','112','1','2021-10-18 15:10:00','2021-10-18 15:10:00','index','3'),(899796627491917824,'802848818109353984','验证码','2','1','fa fa-coffee','','113','1','2021-10-18 15:10:33','2021-10-18 15:10:33','captcha','5'),(899796760371662848,'802848818109353984','登陆','2','1','fa fa-coffee','','115','1','2021-10-18 15:11:05','2021-10-18 15:11:05','login','5'),(899796895541497856,'802848818109353984','退出登陆','2','1','fa fa-coffee','','116','1','2021-10-18 15:11:37','2021-10-18 15:11:37','logout','5'),(899797407535992832,'802918652050411520','数据源管理-新增页面','4','1','fa fa-coffee','','1','1','2021-10-18 15:13:39','2021-10-18 15:22:27','data_sources_add_index','3'),(899797834717466624,'802918652050411520','数据源管理-数据源类型查询','4','1','fa fa-coffee','','7','1','2021-10-18 15:15:21','2021-10-18 15:15:21','data_sources_type','5'),(899799748200894464,'802918652050411520','数据源管理-查询明细','4','1','fa fa-coffee','','8','1','2021-10-18 15:22:57','2021-10-18 15:22:57','data_sources_info','5'),(899804862013771776,'802918760057933824','ETL任务-新增页面','4','1','fa fa-coffee','','5','1','2021-10-18 15:43:17','2021-10-18 15:43:17','etl_task_add_index','3'),(899805009363865600,'802918760057933824','ETL任务-查询明细','4','1','fa fa-coffee','','6','1','2021-10-18 15:43:52','2021-10-18 15:43:52','etl_task_detail','5'),(899805166738345984,'802918760057933824','ETL任务-查询数据库表','4','1','fa fa-coffee','','8','1','2021-10-18 15:44:29','2021-10-18 15:44:29','etl_task_tables','5'),(899807422577643520,'802919044364636160','多源ETL任务-新增页面','4','1','fa fa-coffee','','5','1','2021-10-18 15:53:27','2021-10-18 15:53:27','etl_task_more_sources_add_index','3'),(899807641243488256,'802919044364636160','多源ETL任务-查询明细','4','1','fa fa-coffee','','6','1','2021-10-18 15:54:19','2021-10-18 15:54:19','etl_task_more_detail','5'),(899808823739420672,'802919157430489088','SparkSql任务-查询数据仓库','4','1','fa fa-coffee','','5','1','2021-10-18 15:59:01','2021-10-18 15:59:01','show_databases','5'),(899810180206694400,'802930870510948352','Drools任务-查询','4','1','fa fa-coffee','','1','1','2021-10-18 16:04:25','2021-10-18 16:04:25','etl_task_drools_list2','5'),(899810791648137216,'802930870510948352','Drools任务-新增页面','4','1','fa fa-coffee','','2','1','2021-10-18 16:06:50','2021-10-18 16:06:50','etl_task_drools_add_index','3'),(899810992530132992,'802930870510948352','Drools任务-明细','4','1','fa fa-coffee','','3','1','2021-10-18 16:07:38','2021-10-18 16:07:38','etl_task_drools_detail','5'),(899811121018441728,'802930870510948352','Drools任务-新增','4','1','fa fa-coffee','','4','1','2021-10-18 16:08:09','2021-10-18 16:08:09','etl_task_drools_add','5'),(899811229017575424,'802930870510948352','Drools任务-删除','4','1','fa fa-coffee','','5','1','2021-10-18 16:08:35','2021-10-18 16:08:35','etl_task_drools_delete','5'),(899811331564113920,'802930870510948352','Drools任务-更新','4','1','fa fa-coffee','','6','1','2021-10-18 16:08:59','2021-10-18 16:08:59','etl_task_drools_update','5'),(899811720153796608,'802931116691427328','SSH任务-新增页面','4','1','fa fa-coffee','','5','1','2021-10-18 16:10:32','2021-10-18 16:10:32','etl_task_ssh_add_index','3'),(899813283056324608,'839152432125579264','发布源ETL-查询','4','1','fa fa-coffee','','1','1','2021-10-18 16:16:44','2021-10-18 16:16:44','etl_task_apply_list2','5'),(899814315572334592,'839152432125579264','发布源ETL-新增页面','4','1','fa fa-coffee','','2','1','2021-10-18 16:20:51','2021-10-18 16:20:51','etl_task_apply_add_index','3'),(899814524809383936,'839152432125579264','发布源ETL-新增','4','1','fa fa-coffee','','3','1','2021-10-18 16:21:40','2021-10-18 16:21:40','etl_task_apply_add','5'),(899814663515017216,'839152432125579264','发布源ETL-删除','4','1','fa fa-coffee','','4','1','2021-10-18 16:22:14','2021-10-18 16:22:14','etl_task_apply_delete','5'),(899814772944408576,'839152432125579264','发布源ETL-更新','4','1','fa fa-coffee','','5','1','2021-10-18 16:22:40','2021-10-18 16:22:40','etl_task_apply_update','5'),(899815335576735744,'858320387178500096','FlinkSql新增页面','4','1','fa fa-coffee','','5','1','2021-10-18 16:24:54','2021-10-18 16:24:54','etl_task_flink_add_index','3'),(899816557880807424,'808616077255774208','部署管理-查询','4','1','fa fa-coffee','','1','1','2021-10-18 16:29:45','2021-10-18 16:29:45','server_manager_list','5'),(900519809114968064,'839059609225269248','我的审批-查询','4','1','fa fa-coffee','','1','1','2021-10-20 15:04:13','2021-10-20 15:04:13','process_flow_list','5'),(902346927830470656,'891283647431184384','我的流程','3','1','fa fa-gg','','5','1','2021-10-25 16:04:32','2021-10-25 16:06:50','process_flow_index2','2'),(902685738875752448,'902346927830470656','我的流程-查询','4','1','fa fa-coffee','','1','1','2021-10-26 14:30:51','2021-10-26 14:30:51','process_flow_list2','5'),(902710107161235456,'902346927830470656','我的流程-撤销','4','1','fa fa-coffee','','2','1','2021-10-26 16:07:41','2021-10-26 16:07:41','process_flow_status2','3'),(903063574157463552,'902346927830470656','我的流程-进度页面','4','1','fa fa-coffee','','3','1','2021-10-27 15:32:14','2021-10-27 15:32:51','process_flow_detail_index','3'),(903063690486484992,'902346927830470656','我的流程-进度查询','4','1','fa fa-coffee','','4','1','2021-10-27 15:32:42','2021-10-27 15:32:42','process_flow_detail','5'),(903065878872985600,'802848818109353984','404','2','1','fa fa-coffee','','99','1','2021-10-27 15:41:24','2021-10-30 05:40:20','404','3'),(903066735052066816,'838335003375964160','申请明细-查询','4','1','fa fa-coffee','','1','1','2021-10-27 15:44:48','2021-10-27 15:44:48','data_apply_list','5'),(904012775699779584,'893817125867622400','用户配置-用户组查询','4','1','fa fa-coffee','','7','1','2021-10-30 06:24:01','2021-10-30 06:24:01','user_group_list','5'),(904015493940121600,'802848818109353984','REDIS接口','2','1','fa fa-coffee','','105','1','2021-10-30 06:34:50','2021-10-30 06:34:50','','5'),(904015737297833984,'904015493940121600','REDIS接口-查询','3','1','fa fa-coffee','','1','1','2021-10-30 06:35:48','2021-10-30 06:35:48','/redis/get','5'),(904015873528827904,'904015493940121600','REDIS接口-删除','3','1','fa fa-coffee','','2','1','2021-10-30 06:36:20','2021-10-30 06:36:20','redis/del','5'),(904016015229194240,'904015493940121600','REDIS接口-所有KEY','3','1','fa fa-coffee','','3','1','2021-10-30 06:36:54','2021-10-30 06:36:54','redis/keys','5'),(904017457503539200,'830556376391487488','提BUG-发送邮件','3','1','fa fa-coffee','','1','1','2021-10-30 06:42:38','2021-10-30 06:42:38','send_email','5'),(904042210536722432,'805372907965386752','同步元数据-同步','4','1','fa fa-coffee','','1','1','2021-10-30 08:20:59','2021-10-30 08:20:59','load_meta_databases','5'),(904042889393213440,'805431924678987776','生成监控任务-生成','4','1','fa fa-coffee','','1','1','2021-10-30 08:23:41','2021-10-30 08:23:41','del_system_job','5'),(904044939199909888,'898340104257409024','调度任务-主键查询','5','1','fa fa-coffee','','1','1','2021-10-30 08:31:50','2021-10-30 08:31:50','dispatch_task_list','5'),(904046610378395648,'802931116691427328','SSH任务-文件查询','4','1','fa fa-coffee','','1','1','2021-10-30 08:38:28','2021-10-30 08:38:28','etl_task_ssh_file_list','5'),(904047129859723264,'802931116691427328','SSH任务-删除文件','4','1','fa fa-coffee','','7','1','2021-10-30 08:40:32','2021-10-30 08:40:32','etl_task_ssh_del_file','5'),(904050101738016768,'802931308593418240','调度ETL-任务组执行记录页面','4','1','fa fa-coffee','','5','1','2021-10-30 08:52:21','2021-10-30 08:54:58','task_group_log_instance_index','3'),(904051931872235520,'904050101738016768','任务组记录-查询','5','1','fa fa-coffee','','1','1','2021-10-30 08:59:37','2021-10-30 08:59:37','task_group_log_instance_list','5'),(904052710129537024,'904050101738016768','日志页面','5','1','fa fa-coffee','','2','1','2021-10-30 09:02:43','2021-10-30 09:02:43','log_txt','3'),(904053017009983488,'904052710129537024','查询/删除','6','1','fa fa-coffee','','1','1','2021-10-30 09:03:56','2021-10-30 09:04:21','zdh_logs','5'),(904053585896017920,'904050101738016768','任务组-子任务页面','5','1','fa fa-coffee','','2','1','2021-10-30 09:06:11','2021-10-30 09:06:11','task_log_instance_index','3'),(904055132549812224,'904053585896017920','子任务-查询','6','1','fa fa-coffee','','1','1','2021-10-30 09:12:20','2021-10-30 09:12:20','task_log_instance_list','5'),(904055741369815040,'904053585896017920','子任务-组查询','6','1','fa fa-coffee','','2','1','2021-10-30 09:14:45','2021-10-30 09:14:45','task_group_log_instance_list3','5'),(904056255981555712,'904053585896017920','子任务-删除','6','1','fa fa-coffee','','3','1','2021-10-30 09:16:48','2021-10-30 09:16:48','task_logs_delete','5'),(904057211481755648,'802931308593418240','调度ETL-手动执行页面','4','1','fa fa-coffee','','6','1','2021-10-30 09:20:36','2021-10-30 09:20:36','task_group_exe_detail_index','5'),(904058054746574848,'802931308593418240','调度ETL-新增任务页面','4','1','fa fa-coffee','','8','1','2021-10-30 09:23:57','2021-10-30 09:23:57','dispatch_task_group_add_index','5'),(904059356146831360,'904057211481755648','执行','5','1','fa fa-coffee','','1','1','2021-10-30 09:29:07','2021-10-30 09:29:07','dispatch_task_execute','5'),(904060028510539776,'904053585896017920','子任务-杀死','6','1','fa fa-coffee','','4','1','2021-10-30 09:31:47','2021-10-30 09:31:47','kill','5'),(904060576521523200,'904050101738016768','任务组-杀死','5','1','fa fa-coffee','','1','1','2021-10-30 09:33:58','2021-10-30 09:34:13','killJobGroup','5'),(904061109097467904,'904050101738016768','任务组-重试页面','5','1','fa fa-coffee','','1','1','2021-10-30 09:36:05','2021-10-30 09:36:05','task_group_retry_detail_index','3'),(904061224998670336,'904061109097467904','重试','6','1','fa fa-coffee','','1','1','2021-10-30 09:36:33','2021-10-30 09:40:46','retryJobGroup','5'),(904063054986088448,'904053585896017920','子任务-获取监控链接','6','1','fa fa-coffee','','5','1','2021-10-30 09:43:49','2021-10-30 09:43:49','getSparkMonitor','5'),(904065292424974336,'802850170428461056','总监控-开启的调度任务','3','1','fa fa-coffee','','4','1','2021-10-30 09:52:42','2021-10-30 09:52:42','getScheduleTask','5'),(904065881678548992,'802931308593418240','调度ETL-调度按钮','4','1','fa fa-coffee','','8','1','2021-10-30 09:55:03','2021-10-30 09:55:03','dispatch_task_execute_quartz','5'),(904066172054409216,'802931308593418240','调度ETL-暂停按钮','4','1','fa fa-coffee','','9','1','2021-10-30 09:56:12','2021-10-30 09:56:12','dispatch_task_quartz_pause','5'),(904066367945183232,'802931308593418240','调度ETL-停用按钮','4','1','fa fa-coffee','','10','1','2021-10-30 09:56:59','2021-10-30 09:56:59','dispatch_task_quartz_del','5'),(904068373158039552,'894524363561242624','审批节点-明细','4','1','fa fa-coffee','','5','1','2021-10-30 10:04:57','2021-10-30 10:04:57','approval_config_detail','5'),(904069900111187968,'894896876246011904','审批人配置-明细','4','1','fa fa-coffee','','5','1','2021-10-30 10:11:01','2021-10-30 10:11:01','approval_auditor_detail','5'),(904070354979262464,'894896876246011904','审批人配置-删除','4','1','fa fa-coffee','','6','1','2021-10-30 10:12:49','2021-10-30 10:12:49','approval_auditor_delete','5'),(904070687604346880,'896164931303378944','事件配置-明细','4','1','fa fa-coffee','','5','1','2021-10-30 10:14:09','2021-10-30 10:14:09','approval_event_detail','5'),(904071524649013248,'839059609225269248','我的审批-审批','4','1','fa fa-coffee','','2','1','2021-10-30 10:17:28','2021-10-30 10:17:28','process_flow_status','5'),(904073001346011136,'893817125867622400','用户配置-新增用户组页面','4','1','fa fa-coffee','','8','1','2021-10-30 10:23:20','2021-10-30 10:23:20','user_group_add_index','3'),(904117946815614976,'904058054746574848','SHELL页面','5','1','fa fa-coffee','','1','1','2021-10-30 13:21:56','2021-10-30 13:21:56','shell_detail','5'),(904118233940889600,'904058054746574848','ETL页面','5','1','fa fa-coffee','','2','1','2021-10-30 13:23:05','2021-10-30 13:23:05','job_detail','3'),(904118420969099264,'904058054746574848','任务组页面','5','1','fa fa-coffee','','3','1','2021-10-30 13:23:49','2021-10-30 13:23:49','group_detail','3'),(904118636598267904,'904058054746574848','JDBC页面','5','1','fa fa-coffee','','4','1','2021-10-30 13:24:41','2021-10-30 13:24:41','jdbc_detail','3'),(904118843020939264,'904058054746574848','HDFS页面','5','1','fa fa-coffee','','5','1','2021-10-30 13:25:30','2021-10-30 13:25:30','hdfs_detail','3'),(904120248553181184,'904058054746574848','计算集群-查询','5','1','fa fa-coffee','','6','1','2021-10-30 13:31:05','2021-10-30 13:31:05','zdh_instance_list','5'),(904134748278886400,'899814315572334592','发布源-查询','5','1','fa fa-coffee','','1','1','2021-10-30 14:28:42','2021-10-30 14:28:42','data_apply_list2','5'),(904135062444838912,'899814315572334592','发布源-查询2','5','1','fa fa-coffee','','2','1','2021-10-30 14:29:57','2021-10-30 14:29:57','data_apply_list3','5'),(904875825113862144,'889247298196869120','信息管理-已读','3','1','fa fa-coffee','','5','1','2021-11-01 15:33:28','2021-11-01 15:33:28','notice_update_see','5'),(906703041556647936,'802919157430489088','新增SparkSql任务首页','4','1','fa fa-coffee','','7','1','2021-11-06 16:34:10','2021-11-06 16:34:10','sql_task_add_index','3'),(906964874968436736,'802932157390524416','指标查询-查询','3','1','fa fa-coffee','','1','1','2021-11-07 09:54:36','2021-11-07 09:54:36','quota_list','5');
INSERT INTO `resource_tree_info`
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type)
VALUES(913940784099627008, '802852358580080640', 'JDBC_SQL', '3', '1', 'non', '', '9', '1', '2021-11-26 23:54:23', '2021-11-27 00:20:28', 'etl_task_jdbc_index.html', '2');
INSERT INTO resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type)
VALUES(913947870334291968, '913940784099627008', '查询', '4', '1', 'fa fa-coffee', '', '1', '1', '2021-11-27 00:22:33', '2021-11-27 00:22:33', 'etl_task_jdbc_list', '5');
INSERT INTO resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type)
VALUES(913949817086939136, '913940784099627008', '新增页面', '4', '1', 'fa fa-coffee', '', '2', '1', '2021-11-27 00:30:17', '2021-11-27 00:32:02', 'etl_task_jdbc_add_index', '5');
INSERT INTO resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type)
VALUES(913949953242435584, '913940784099627008', '删除', '4', '1', 'fa fa-coffee', '', '3', '1', '2021-11-27 00:30:49', '2021-11-27 00:30:49', 'etl_task_jdbc_delete', '5');
INSERT INTO resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type)
VALUES(913950163700027392, '913940784099627008', '新增', '4', '1', 'fa fa-coffee', '', '4', '1', '2021-11-27 00:31:39', '2021-11-27 00:53:12', 'etl_task_jdbc_add', '5');
INSERT INTO resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type)
VALUES(913955680715542528, '913940784099627008', '更新', '4', '1', 'fa fa-coffee', '', '5', '1', '2021-11-27 00:53:35', '2021-11-27 00:53:35', 'etl_task_jdbc_update', '5');

/*!40000 ALTER TABLE `resource_tree_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_info`
--

DROP TABLE IF EXISTS `role_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(200) DEFAULT NULL COMMENT '角色code',
  `name` varchar(500) DEFAULT NULL COMMENT '角色名',
  `enable` varchar(8) DEFAULT 'false' COMMENT '是否启用true/false',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=904072220689567745 DEFAULT CHARSET=utf8mb4;
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
-- Table structure for table `role_resource_info`
--

DROP TABLE IF EXISTS `role_resource_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_resource_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` varchar(100) DEFAULT NULL COMMENT 'role id',
  `resource_id` varchar(100) DEFAULT NULL COMMENT '资源id',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11975 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_resource_info`
--

LOCK TABLES `role_resource_info` WRITE;
/*!40000 ALTER TABLE `role_resource_info` DISABLE KEYS */;
INSERT INTO `role_resource_info` VALUES (274,'894254232000008192','802848818109353984',NULL,NULL),(275,'894254232000008192','802850170428461056',NULL,NULL),(276,'894254232000008192','802852358580080640',NULL,NULL),(277,'894254232000008192','802918652050411520',NULL,NULL),(278,'894254232000008192','802918760057933824',NULL,NULL),(279,'894254232000008192','802919044364636160',NULL,NULL),(280,'894254232000008192','802919157430489088',NULL,NULL),(281,'894254232000008192','802930870510948352',NULL,NULL),(282,'894254232000008192','802931116691427328',NULL,NULL),(283,'894254232000008192','839152432125579264',NULL,NULL),(284,'894254232000008192','858320387178500096',NULL,NULL),(285,'894254232000008192','802931308593418240',NULL,NULL),(286,'894254232000008192','802931697527033856',NULL,NULL),(287,'894254232000008192','802876953722884096',NULL,NULL),(288,'894254232000008192','889247298196869120',NULL,NULL),(289,'894254232000008192','802932157390524416',NULL,NULL),(290,'894254232000008192','802932355596554240',NULL,NULL),(291,'894254232000008192','891283647431184384',NULL,NULL),(292,'894254232000008192','802932548165439488',NULL,NULL),(293,'894254232000008192','802933021148712960',NULL,NULL),(294,'894254232000008192','805193674668380160',NULL,NULL),(295,'894254232000008192','805531084459610112',NULL,NULL),(296,'894254232000008192','810817759893000192',NULL,NULL),(297,'894254232000008192','802932890089295872',NULL,NULL),(298,'894254232000008192','802933165302747136',NULL,NULL),(299,'894254232000008192','830556376391487488',NULL,NULL),(9826,'904072220689567744','802850170428461056',NULL,NULL),(9827,'904072220689567744','802848818109353984',NULL,NULL),(9828,'904072220689567744','802852358580080640',NULL,NULL),(9829,'904072220689567744','802918652050411520',NULL,NULL),(11561,'898997645559730176','802848818109353984',NULL,NULL),(11562,'898997645559730176','802850170428461056',NULL,NULL),(11563,'898997645559730176','899083124191793152',NULL,NULL),(11564,'898997645559730176','899083287929032704',NULL,NULL),(11565,'898997645559730176','899083494326538240',NULL,NULL),(11566,'898997645559730176','899083652330164224',NULL,NULL),(11567,'898997645559730176','904065292424974336',NULL,NULL),(11568,'898997645559730176','802852358580080640',NULL,NULL),(11569,'898997645559730176','802918652050411520',NULL,NULL),(11570,'898997645559730176','898333103779483648',NULL,NULL),(11571,'898997645559730176','899797407535992832',NULL,NULL),(11572,'898997645559730176','898334091781345280',NULL,NULL),(11573,'898997645559730176','898334486809284608',NULL,NULL),(11574,'898997645559730176','898334268604813312',NULL,NULL),(11575,'898997645559730176','898335485523398656',NULL,NULL),(11576,'898997645559730176','899797834717466624',NULL,NULL),(11577,'898997645559730176','899799748200894464',NULL,NULL),(11578,'898997645559730176','802918760057933824',NULL,NULL),(11579,'898997645559730176','898335839925309440',NULL,NULL),(11580,'898997645559730176','898336112890613760',NULL,NULL),(11581,'898997645559730176','898336252711931904',NULL,NULL),(11582,'898997645559730176','898336446329393152',NULL,NULL),(11583,'898997645559730176','899804862013771776',NULL,NULL),(11584,'898997645559730176','899805009363865600',NULL,NULL),(11585,'898997645559730176','899805166738345984',NULL,NULL),(11586,'898997645559730176','802919044364636160',NULL,NULL),(11587,'898997645559730176','898336838400348160',NULL,NULL),(11588,'898997645559730176','898337064490110976',NULL,NULL),(11589,'898997645559730176','898337228843913216',NULL,NULL),(11590,'898997645559730176','898337404199374848',NULL,NULL),(11591,'898997645559730176','899807422577643520',NULL,NULL),(11592,'898997645559730176','899807641243488256',NULL,NULL),(11593,'898997645559730176','802919157430489088',NULL,NULL),(11594,'898997645559730176','898337767392546816',NULL,NULL),(11595,'898997645559730176','898337973207044096',NULL,NULL),(11596,'898997645559730176','898338119047188480',NULL,NULL),(11597,'898997645559730176','898338251511697408',NULL,NULL),(11598,'898997645559730176','899808823739420672',NULL,NULL),(11599,'898997645559730176','802930870510948352',NULL,NULL),(11600,'898997645559730176','899810180206694400',NULL,NULL),(11601,'898997645559730176','899810791648137216',NULL,NULL),(11602,'898997645559730176','899810992530132992',NULL,NULL),(11603,'898997645559730176','899811121018441728',NULL,NULL),(11604,'898997645559730176','899811229017575424',NULL,NULL),(11605,'898997645559730176','899811331564113920',NULL,NULL),(11606,'898997645559730176','802931116691427328',NULL,NULL),(11607,'898997645559730176','898338570987638784',NULL,NULL),(11608,'898997645559730176','904046610378395648',NULL,NULL),(11609,'898997645559730176','898338819265269760',NULL,NULL),(11610,'898997645559730176','898338990552256512',NULL,NULL),(11611,'898997645559730176','898339248917188608',NULL,NULL),(11612,'898997645559730176','899811720153796608',NULL,NULL),(11613,'898997645559730176','904047129859723264',NULL,NULL),(11614,'898997645559730176','839152432125579264',NULL,NULL),(11615,'898997645559730176','899813283056324608',NULL,NULL),(11616,'898997645559730176','899814315572334592',NULL,NULL),(11617,'898997645559730176','904134748278886400',NULL,NULL),(11618,'898997645559730176','904135062444838912',NULL,NULL),(11619,'898997645559730176','899814524809383936',NULL,NULL),(11620,'898997645559730176','899814663515017216',NULL,NULL),(11621,'898997645559730176','899814772944408576',NULL,NULL),(11622,'898997645559730176','858320387178500096',NULL,NULL),(11623,'898997645559730176','898339478811185152',NULL,NULL),(11624,'898997645559730176','898339672948740096',NULL,NULL),(11625,'898997645559730176','898339790284394496',NULL,NULL),(11626,'898997645559730176','898339903127949312',NULL,NULL),(11627,'898997645559730176','899815335576735744',NULL,NULL),(11628,'898997645559730176','802931308593418240',NULL,NULL),(11629,'898997645559730176','898340104257409024',NULL,NULL),(11630,'898997645559730176','904044939199909888',NULL,NULL),(11631,'898997645559730176','898340234041757696',NULL,NULL),(11632,'898997645559730176','898340346117754880',NULL,NULL),(11633,'898997645559730176','898341132600086528',NULL,NULL),(11634,'898997645559730176','904050101738016768',NULL,NULL),(11635,'898997645559730176','904051931872235520',NULL,NULL),(11636,'898997645559730176','904060576521523200',NULL,NULL),(11637,'898997645559730176','904061109097467904',NULL,NULL),(11638,'898997645559730176','904061224998670336',NULL,NULL),(11639,'898997645559730176','904052710129537024',NULL,NULL),(11640,'898997645559730176','904053017009983488',NULL,NULL),(11641,'898997645559730176','904053585896017920',NULL,NULL),(11642,'898997645559730176','904055132549812224',NULL,NULL),(11643,'898997645559730176','904055741369815040',NULL,NULL),(11644,'898997645559730176','904056255981555712',NULL,NULL),(11645,'898997645559730176','904060028510539776',NULL,NULL),(11646,'898997645559730176','904063054986088448',NULL,NULL),(11647,'898997645559730176','904057211481755648',NULL,NULL),(11648,'898997645559730176','904059356146831360',NULL,NULL),(11649,'898997645559730176','904058054746574848',NULL,NULL),(11650,'898997645559730176','904117946815614976',NULL,NULL),(11651,'898997645559730176','904118233940889600',NULL,NULL),(11652,'898997645559730176','904118420969099264',NULL,NULL),(11653,'898997645559730176','904118636598267904',NULL,NULL),(11654,'898997645559730176','904118843020939264',NULL,NULL),(11655,'898997645559730176','904120248553181184',NULL,NULL),(11656,'898997645559730176','904065881678548992',NULL,NULL),(11657,'898997645559730176','904066172054409216',NULL,NULL),(11658,'898997645559730176','904066367945183232',NULL,NULL),(11659,'898997645559730176','802931697527033856',NULL,NULL),(11660,'898997645559730176','898919578183143424',NULL,NULL),(11661,'898997645559730176','898919751642779648',NULL,NULL),(11662,'898997645559730176','898919884019208192',NULL,NULL),(11663,'898997645559730176','898921890200948736',NULL,NULL),(11664,'898997645559730176','898922054793826304',NULL,NULL),(11665,'898997645559730176','802876953722884096',NULL,NULL),(11666,'898997645559730176','898922698124562432',NULL,NULL),(11667,'898997645559730176','898923212249763840',NULL,NULL),(11668,'898997645559730176','898923484506230784',NULL,NULL),(11669,'898997645559730176','898923625090912256',NULL,NULL),(11670,'898997645559730176','904875825113862144',NULL,NULL),(11671,'898997645559730176','889247298196869120',NULL,NULL),(11672,'898997645559730176','802932355596554240',NULL,NULL),(11673,'898997645559730176','802932157390524416',NULL,NULL),(11674,'898997645559730176','891283647431184384',NULL,NULL),(11675,'898997645559730176','894524363561242624',NULL,NULL),(11676,'898997645559730176','898924030910795776',NULL,NULL),(11677,'898997645559730176','898924424034521088',NULL,NULL),(11678,'898997645559730176','898924549347741696',NULL,NULL),(11679,'898997645559730176','898924812993302528',NULL,NULL),(11680,'898997645559730176','904068373158039552',NULL,NULL),(11681,'898997645559730176','894896876246011904',NULL,NULL),(11682,'898997645559730176','898925461227180032',NULL,NULL),(11683,'898997645559730176','898925640273629184',NULL,NULL),(11684,'898997645559730176','898925753096212480',NULL,NULL),(11685,'898997645559730176','898926933432078336',NULL,NULL),(11686,'898997645559730176','904069900111187968',NULL,NULL),(11687,'898997645559730176','904070354979262464',NULL,NULL),(11688,'898997645559730176','896164931303378944',NULL,NULL),(11689,'898997645559730176','898992327668797440',NULL,NULL),(11690,'898997645559730176','898992469134282752',NULL,NULL),(11691,'898997645559730176','898992569562697728',NULL,NULL),(11692,'898997645559730176','898992688609628160',NULL,NULL),(11693,'898997645559730176','904070687604346880',NULL,NULL),(11694,'898997645559730176','839059609225269248',NULL,NULL),(11695,'898997645559730176','900519809114968064',NULL,NULL),(11696,'898997645559730176','904071524649013248',NULL,NULL),(11697,'898997645559730176','902346927830470656',NULL,NULL),(11698,'898997645559730176','902685738875752448',NULL,NULL),(11699,'898997645559730176','902710107161235456',NULL,NULL),(11700,'898997645559730176','903063574157463552',NULL,NULL),(11701,'898997645559730176','903063690486484992',NULL,NULL),(11702,'898997645559730176','802933021148712960',NULL,NULL),(11703,'898997645559730176','899000798854254592',NULL,NULL),(11704,'898997645559730176','805193674668380160',NULL,NULL),(11705,'898997645559730176','805374183432261632',NULL,NULL),(11706,'898997645559730176','899001510107549696',NULL,NULL),(11707,'898997645559730176','899001733512957952',NULL,NULL),(11708,'898997645559730176','805369519538180096',NULL,NULL),(11709,'898997645559730176','899001982079995904',NULL,NULL),(11710,'898997645559730176','899002142675701760',NULL,NULL),(11711,'898997645559730176','808616077255774208',NULL,NULL),(11712,'898997645559730176','899076126133981184',NULL,NULL),(11713,'898997645559730176','899816557880807424',NULL,NULL),(11714,'898997645559730176','899076266156625920',NULL,NULL),(11715,'898997645559730176','899076393432780800',NULL,NULL),(11716,'898997645559730176','899076708504702976',NULL,NULL),(11717,'898997645559730176','899076830349234176',NULL,NULL),(11718,'898997645559730176','899077318159372288',NULL,NULL),(11719,'898997645559730176','899077560292347904',NULL,NULL),(11720,'898997645559730176','810816897514737664',NULL,NULL),(11721,'898997645559730176','810817759893000192',NULL,NULL),(11722,'898997645559730176','899078834731618304',NULL,NULL),(11723,'898997645559730176','899079195127189504',NULL,NULL),(11724,'898997645559730176','899079347393007616',NULL,NULL),(11725,'898997645559730176','899079618982580224',NULL,NULL),(11726,'898997645559730176','810825569494110208',NULL,NULL),(11727,'898997645559730176','899079911518507008',NULL,NULL),(11728,'898997645559730176','899080146999316480',NULL,NULL),(11729,'898997645559730176','899081132492984320',NULL,NULL),(11730,'898997645559730176','899081270330396672',NULL,NULL),(11731,'898997645559730176','899081439826415616',NULL,NULL),(11732,'898997645559730176','838335003375964160',NULL,NULL),(11733,'898997645559730176','903066735052066816',NULL,NULL),(11734,'898997645559730176','802932890089295872',NULL,NULL),(11735,'898997645559730176','802933165302747136',NULL,NULL),(11736,'898997645559730176','830556376391487488',NULL,NULL),(11737,'898997645559730176','904017457503539200',NULL,NULL),(11738,'898997645559730176','903065878872985600',NULL,NULL),(11739,'898997645559730176','904015493940121600',NULL,NULL),(11740,'898997645559730176','904015737297833984',NULL,NULL),(11741,'898997645559730176','904015873528827904',NULL,NULL),(11742,'898997645559730176','904016015229194240',NULL,NULL),(11743,'898997645559730176','899793046982365184',NULL,NULL),(11744,'898997645559730176','899796275644338176',NULL,NULL),(11745,'898997645559730176','899796487305695232',NULL,NULL),(11746,'898997645559730176','899796627491917824',NULL,NULL),(11747,'898997645559730176','899796760371662848',NULL,NULL),(11748,'898997645559730176','899796895541497856',NULL,NULL),(11749,'898997645559730176','906703041556647936',NULL,NULL),(11750,'894201076759138304','805374183432261632',NULL,NULL),(11751,'894201076759138304','802848818109353984',NULL,NULL),(11752,'894201076759138304','802850170428461056',NULL,NULL),(11753,'894201076759138304','802918652050411520',NULL,NULL),(11754,'894201076759138304','805369519538180096',NULL,NULL),(11755,'894201076759138304','808616254788079616',NULL,NULL),(11756,'894201076759138304','810816897514737664',NULL,NULL),(11757,'894201076759138304','893810193274507264',NULL,NULL),(11758,'894201076759138304','894524363561242624',NULL,NULL),(11759,'894201076759138304','898333103779483648',NULL,NULL),(11760,'894201076759138304','898335839925309440',NULL,NULL),(11761,'894201076759138304','898336838400348160',NULL,NULL),(11762,'894201076759138304','898337767392546816',NULL,NULL),(11763,'894201076759138304','898338570987638784',NULL,NULL),(11764,'894201076759138304','898339478811185152',NULL,NULL),(11765,'894201076759138304','898340104257409024',NULL,NULL),(11766,'894201076759138304','898919578183143424',NULL,NULL),(11767,'894201076759138304','898921890200948736',NULL,NULL),(11768,'894201076759138304','898922698124562432',NULL,NULL),(11769,'894201076759138304','898924030910795776',NULL,NULL),(11770,'894201076759138304','898925461227180032',NULL,NULL),(11771,'894201076759138304','898992327668797440',NULL,NULL),(11772,'894201076759138304','898994298257674240',NULL,NULL),(11773,'894201076759138304','898996218540068864',NULL,NULL),(11774,'894201076759138304','898998799848968192',NULL,NULL),(11775,'894201076759138304','899000798854254592',NULL,NULL),(11776,'894201076759138304','899001510107549696',NULL,NULL),(11777,'894201076759138304','899001982079995904',NULL,NULL),(11778,'894201076759138304','899059721481228288',NULL,NULL),(11779,'894201076759138304','899076126133981184',NULL,NULL),(11780,'894201076759138304','899078834731618304',NULL,NULL),(11781,'894201076759138304','899079911518507008',NULL,NULL),(11782,'894201076759138304','899083124191793152',NULL,NULL),(11783,'894201076759138304','899797407535992832',NULL,NULL),(11784,'894201076759138304','899810180206694400',NULL,NULL),(11785,'894201076759138304','899813283056324608',NULL,NULL),(11786,'894201076759138304','899816557880807424',NULL,NULL),(11787,'894201076759138304','900519809114968064',NULL,NULL),(11788,'894201076759138304','902685738875752448',NULL,NULL),(11789,'894201076759138304','903066735052066816',NULL,NULL),(11790,'894201076759138304','904015737297833984',NULL,NULL),(11791,'894201076759138304','904017457503539200',NULL,NULL),(11792,'894201076759138304','904042210536722432',NULL,NULL),(11793,'894201076759138304','904042889393213440',NULL,NULL),(11794,'894201076759138304','904044939199909888',NULL,NULL),(11795,'894201076759138304','904046610378395648',NULL,NULL),(11796,'894201076759138304','904051931872235520',NULL,NULL),(11797,'894201076759138304','904053017009983488',NULL,NULL),(11798,'894201076759138304','904055132549812224',NULL,NULL),(11799,'894201076759138304','904059356146831360',NULL,NULL),(11800,'894201076759138304','904060576521523200',NULL,NULL),(11801,'894201076759138304','904061109097467904',NULL,NULL),(11802,'894201076759138304','904061224998670336',NULL,NULL),(11803,'894201076759138304','904117946815614976',NULL,NULL),(11804,'894201076759138304','904134748278886400',NULL,NULL),(11805,'894201076759138304','802852358580080640',NULL,NULL),(11806,'894201076759138304','802918760057933824',NULL,NULL),(11807,'894201076759138304','893816925920956416',NULL,NULL),(11808,'894201076759138304','894896876246011904',NULL,NULL),(11809,'894201076759138304','898334091781345280',NULL,NULL),(11810,'894201076759138304','898334486809284608',NULL,NULL),(11811,'894201076759138304','898336112890613760',NULL,NULL),(11812,'894201076759138304','898337064490110976',NULL,NULL),(11813,'894201076759138304','898337973207044096',NULL,NULL),(11814,'894201076759138304','898338819265269760',NULL,NULL),(11815,'894201076759138304','898339672948740096',NULL,NULL),(11816,'894201076759138304','898340234041757696',NULL,NULL),(11817,'894201076759138304','898919751642779648',NULL,NULL),(11818,'894201076759138304','898922054793826304',NULL,NULL),(11819,'894201076759138304','898923212249763840',NULL,NULL),(11820,'894201076759138304','898923484506230784',NULL,NULL),(11821,'894201076759138304','898924424034521088',NULL,NULL),(11822,'894201076759138304','898925640273629184',NULL,NULL),(11823,'894201076759138304','898992469134282752',NULL,NULL),(11824,'894201076759138304','898994643360813056',NULL,NULL),(11825,'894201076759138304','898996945933045760',NULL,NULL),(11826,'894201076759138304','898999065830756352',NULL,NULL),(11827,'894201076759138304','899001733512957952',NULL,NULL),(11828,'894201076759138304','899002142675701760',NULL,NULL),(11829,'894201076759138304','899076266156625920',NULL,NULL),(11830,'894201076759138304','899079195127189504',NULL,NULL),(11831,'894201076759138304','899080146999316480',NULL,NULL),(11832,'894201076759138304','899083287929032704',NULL,NULL),(11833,'894201076759138304','899810791648137216',NULL,NULL),(11834,'894201076759138304','899814315572334592',NULL,NULL),(11835,'894201076759138304','902710107161235456',NULL,NULL),(11836,'894201076759138304','904015873528827904',NULL,NULL),(11837,'894201076759138304','904052710129537024',NULL,NULL),(11838,'894201076759138304','904053585896017920',NULL,NULL),(11839,'894201076759138304','904055741369815040',NULL,NULL),(11840,'894201076759138304','904071524649013248',NULL,NULL),(11841,'894201076759138304','904118233940889600',NULL,NULL),(11842,'894201076759138304','802919044364636160',NULL,NULL),(11843,'894201076759138304','802931697527033856',NULL,NULL),(11844,'894201076759138304','805372907965386752',NULL,NULL),(11845,'894201076759138304','808616077255774208',NULL,NULL),(11846,'894201076759138304','810825569494110208',NULL,NULL),(11847,'894201076759138304','838335003375964160',NULL,NULL),(11848,'894201076759138304','893817125867622400',NULL,NULL),(11849,'894201076759138304','896164931303378944',NULL,NULL),(11850,'894201076759138304','898334268604813312',NULL,NULL),(11851,'894201076759138304','898336252711931904',NULL,NULL),(11852,'894201076759138304','898337228843913216',NULL,NULL),(11853,'894201076759138304','898338119047188480',NULL,NULL),(11854,'894201076759138304','898338990552256512',NULL,NULL),(11855,'894201076759138304','898339790284394496',NULL,NULL),(11856,'894201076759138304','898340346117754880',NULL,NULL),(11857,'894201076759138304','898919884019208192',NULL,NULL),(11858,'894201076759138304','898924549347741696',NULL,NULL),(11859,'894201076759138304','898925753096212480',NULL,NULL),(11860,'894201076759138304','898992569562697728',NULL,NULL),(11861,'894201076759138304','898994850660093952',NULL,NULL),(11862,'894201076759138304','898997770537406464',NULL,NULL),(11863,'894201076759138304','898999773028159488',NULL,NULL),(11864,'894201076759138304','899076393432780800',NULL,NULL),(11865,'894201076759138304','899079347393007616',NULL,NULL),(11866,'894201076759138304','899081132492984320',NULL,NULL),(11867,'894201076759138304','899083494326538240',NULL,NULL),(11868,'894201076759138304','899810992530132992',NULL,NULL),(11869,'894201076759138304','899814524809383936',NULL,NULL),(11870,'894201076759138304','903063574157463552',NULL,NULL),(11871,'894201076759138304','904016015229194240',NULL,NULL),(11872,'894201076759138304','904056255981555712',NULL,NULL),(11873,'894201076759138304','904118420969099264',NULL,NULL),(11874,'894201076759138304','802876953722884096',NULL,NULL),(11875,'894201076759138304','802919157430489088',NULL,NULL),(11876,'894201076759138304','889247298196869120',NULL,NULL),(11877,'894201076759138304','898336446329393152',NULL,NULL),(11878,'894201076759138304','898337404199374848',NULL,NULL),(11879,'894201076759138304','898338251511697408',NULL,NULL),(11880,'894201076759138304','898339248917188608',NULL,NULL),(11881,'894201076759138304','898339903127949312',NULL,NULL),(11882,'894201076759138304','898341132600086528',NULL,NULL),(11883,'894201076759138304','898923625090912256',NULL,NULL),(11884,'894201076759138304','898924812993302528',NULL,NULL),(11885,'894201076759138304','898926933432078336',NULL,NULL),(11886,'894201076759138304','898992688609628160',NULL,NULL),(11887,'894201076759138304','898995002837831680',NULL,NULL),(11888,'894201076759138304','898998045511782400',NULL,NULL),(11889,'894201076759138304','898999958684831744',NULL,NULL),(11890,'894201076759138304','899076708504702976',NULL,NULL),(11891,'894201076759138304','899079618982580224',NULL,NULL),(11892,'894201076759138304','899081270330396672',NULL,NULL),(11893,'894201076759138304','899083652330164224',NULL,NULL),(11894,'894201076759138304','899811121018441728',NULL,NULL),(11895,'894201076759138304','899814663515017216',NULL,NULL),(11896,'894201076759138304','903063690486484992',NULL,NULL),(11897,'894201076759138304','904060028510539776',NULL,NULL),(11898,'894201076759138304','904065292424974336',NULL,NULL),(11899,'894201076759138304','904118636598267904',NULL,NULL),(11900,'894201076759138304','802930870510948352',NULL,NULL),(11901,'894201076759138304','802932157390524416',NULL,NULL),(11902,'894201076759138304','839059609225269248',NULL,NULL),(11903,'894201076759138304','898335485523398656',NULL,NULL),(11904,'894201076759138304','898995219188420608',NULL,NULL),(11905,'894201076759138304','898998299615301632',NULL,NULL),(11906,'894201076759138304','899000154885984256',NULL,NULL),(11907,'894201076759138304','899076830349234176',NULL,NULL),(11908,'894201076759138304','899081439826415616',NULL,NULL),(11909,'894201076759138304','899804862013771776',NULL,NULL),(11910,'894201076759138304','899807422577643520',NULL,NULL),(11911,'894201076759138304','899808823739420672',NULL,NULL),(11912,'894201076759138304','899811229017575424',NULL,NULL),(11913,'894201076759138304','899811720153796608',NULL,NULL),(11914,'894201076759138304','899814772944408576',NULL,NULL),(11915,'894201076759138304','899815335576735744',NULL,NULL),(11916,'894201076759138304','902346927830470656',NULL,NULL),(11917,'894201076759138304','904050101738016768',NULL,NULL),(11918,'894201076759138304','904063054986088448',NULL,NULL),(11919,'894201076759138304','904068373158039552',NULL,NULL),(11920,'894201076759138304','904069900111187968',NULL,NULL),(11921,'894201076759138304','904070687604346880',NULL,NULL),(11922,'894201076759138304','904118843020939264',NULL,NULL),(11923,'894201076759138304','802931116691427328',NULL,NULL),(11924,'894201076759138304','802932355596554240',NULL,NULL),(11925,'894201076759138304','891283647431184384',NULL,NULL),(11926,'894201076759138304','898995513087496192',NULL,NULL),(11927,'894201076759138304','899000274331373568',NULL,NULL),(11928,'894201076759138304','899077318159372288',NULL,NULL),(11929,'894201076759138304','899794938370199552',NULL,NULL),(11930,'894201076759138304','899805009363865600',NULL,NULL),(11931,'894201076759138304','899807641243488256',NULL,NULL),(11932,'894201076759138304','899811331564113920',NULL,NULL),(11933,'894201076759138304','904057211481755648',NULL,NULL),(11934,'894201076759138304','904070354979262464',NULL,NULL),(11935,'894201076759138304','904120248553181184',NULL,NULL),(11936,'894201076759138304','802932548165439488',NULL,NULL),(11937,'894201076759138304','805431924678987776',NULL,NULL),(11938,'894201076759138304','839152432125579264',NULL,NULL),(11939,'894201076759138304','899000507769556992',NULL,NULL),(11940,'894201076759138304','899794270502785024',NULL,NULL),(11941,'894201076759138304','899796100964159488',NULL,NULL),(11942,'894201076759138304','899797834717466624',NULL,NULL),(11943,'894201076759138304','904012775699779584',NULL,NULL),(11944,'894201076759138304','904047129859723264',NULL,NULL),(11945,'894201076759138304','858320387178500096',NULL,NULL),(11946,'894201076759138304','899077560292347904',NULL,NULL),(11947,'894201076759138304','899799748200894464',NULL,NULL),(11948,'894201076759138304','899805166738345984',NULL,NULL),(11949,'894201076759138304','904058054746574848',NULL,NULL),(11950,'894201076759138304','904065881678548992',NULL,NULL),(11951,'894201076759138304','904073001346011136',NULL,NULL),(11952,'894201076759138304','802933021148712960',NULL,NULL),(11953,'894201076759138304','809886572093640704',NULL,NULL),(11954,'894201076759138304','904066172054409216',NULL,NULL),(11955,'894201076759138304','904066367945183232',NULL,NULL),(11956,'894201076759138304','805193674668380160',NULL,NULL),(11957,'894201076759138304','805531084459610112',NULL,NULL),(11958,'894201076759138304','810817759893000192',NULL,NULL),(11959,'894201076759138304','802932890089295872',NULL,NULL),(11960,'894201076759138304','802933165302747136',NULL,NULL),(11961,'894201076759138304','830556376391487488',NULL,NULL),(11962,'894201076759138304','903065878872985600',NULL,NULL),(11963,'894201076759138304','802931308593418240',NULL,NULL),(11964,'894201076759138304','904015493940121600',NULL,NULL),(11965,'894201076759138304','899793046982365184',NULL,NULL),(11966,'894201076759138304','899796275644338176',NULL,NULL),(11967,'894201076759138304','899796487305695232',NULL,NULL),(11968,'894201076759138304','899796627491917824',NULL,NULL),(11969,'894201076759138304','899796760371662848',NULL,NULL),(11970,'894201076759138304','899796895541497856',NULL,NULL),(11971,'894201076759138304','904135062444838912',NULL,NULL),(11972,'894201076759138304','904875825113862144',NULL,NULL),(11973,'894201076759138304','906703041556647936',NULL,NULL),(11974,'894201076759138304','906964874968436736',NULL,NULL);
/*!40000 ALTER TABLE `role_resource_info` ENABLE KEYS */;
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
INSERT INTO `server_task_info` VALUES (1,'server构建任务','192.168.110.10','','GRADLE','sh gradlew release -x test','47.105.50.94','/home/zyc/zdh_server_build','2021-10-16 15:26:37','2021-10-16 15:26:37','1','master','zyc','123456','/home/zyc/zdh_server');
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
) ENGINE=InnoDB AUTO_INCREMENT=809858569640873985 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `server_task_instance`
--

LOCK TABLES `server_task_instance` WRITE;
/*!40000 ALTER TABLE `server_task_instance` DISABLE KEYS */;
INSERT INTO `server_task_instance` VALUES (809855523523399680,'1','server构建任务','192.168.110.10','','GRADLE','sh gradlew release -x test','47.105.50.94','/home/zyc/zdh_server_build','2021-02-12 10:36:43','2021-02-12 10:36:43','1','branch','20210212','master','1','zyc','123456','/home/zyc/zdh_server'),(809858569640873984,'1','server构建任务','192.168.110.10','','GRADLE','sh gradlew release -x test','47.105.50.94','/home/zyc/zdh_server_build','2021-02-12 10:48:50','2021-02-12 10:48:50','1','branch','20210212','master','1','zyc','123456','/home/zyc/zdh_server');
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
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=756934940771225601 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sql_task_info`
--

LOCK TABLES `sql_task_info` WRITE;
/*!40000 ALTER TABLE `sql_task_info` DISABLE KEYS */;
INSERT INTO `sql_task_info` VALUES (718814940856586240,'读取hive转mysql',NULL,NULL,'','select * from act','58','JDBC','act2','act2','','','1','2020-06-06 05:13:57','','','','','','','','','','','-1'),(720684174964428800,'读取hive转外部下载',NULL,'','','select * from act','58','JDBC','act2','act2','','','1','2020-06-06 05:13:57','','','','','','','','','','','-1'),(756925994555674624,'fddfdfs',NULL,'','','select * from a2','','','','','','','1','2020-09-19 09:13:40','','','','','','','','','','','-1'),(756926234339840000,'fdfdfd',NULL,'','','select 2','','','','','','','1','2020-09-19 09:14:37','','','','','','','','','','','-1'),(756934940771225600,'读取hive转mysql',NULL,NULL,'','select * from act bb','','','act2','','','','1','2020-09-19 09:49:13','','','','','','','','','','','-1');
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
) ENGINE=InnoDB AUTO_INCREMENT=795225380238659585 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ssh_task_info`
--

LOCK TABLES `ssh_task_info` WRITE;
/*!40000 ALTER TABLE `ssh_task_info` DISABLE KEYS */;
INSERT INTO `ssh_task_info` VALUES (749064500069535744,'123','47.105.50.94','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc/z2','ls','','1','2020-08-28 16:34:54','','','',''),(794986268751564800,'暂停5分钟','47.105.50.94','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc','echo \"hello\" >> a.log\r\nsleep 10m\r\necho \"word\" >> a.log','','1','2021-01-02 09:51:37','','','','');
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
  `schedule_source` varchar(64) NOT NULL DEFAULT '1' COMMENT '调度来源,1:例行,2:手动',
  `alarm_email` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `alarm_sms` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `alarm_zdh` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_error` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_finish` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_timeout` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=906694439831867393 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_group_log_instance`
--

LOCK TABLES `task_group_log_instance` WRITE;
/*!40000 ALTER TABLE `task_group_log_instance` DISABLE KEYS */;
INSERT INTO `task_group_log_instance` VALUES (906548123663339520,'906547945187315712','测试专用','2021-11-06 14:18:34','error','2021-11-06 06:18:35','2021-11-06 06:18:35','1','false','100',NULL,'1999-12-31 16:00:00',NULL,NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'ETL',NULL,NULL,'1d','1','0',0,NULL,'',NULL,'2021-11-06 06:18:34',NULL,'0 0 0 * * ? *',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc','2021-11-06 06:18:34','0','zdh_web_1:906541082584551424','86400','{\"check_dep_time\":\"2021-11-06 14:18:36\"}','5',NULL,'off','','{\"tasks\":[{\"id\":\"420_a9f_9e80_ae\",\"etl_context\":\"测试shell\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"time_out\":\"86400\",\"divId\":\"420_a9f_9e80_ae\",\"name\":\"测试shell\",\"positionX\":258,\"positionY\":60,\"type\":\"shell\"},{\"id\":\"c04_40a_83c7_ee\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"c04_40a_83c7_ee\",\"name\":\"单分割符无标题\",\"positionX\":256,\"positionY\":159,\"type\":\"tasks\"}],\"line\":[{\"connectionId\":\"con_12\",\"pageSourceId\":\"420_a9f_9e80_ae\",\"pageTargetId\":\"c04_40a_83c7_ee\"}]}','{\"run_data\":[{\"job_type\":\"SHELL\",\"task_log_instance_id\":\"906548124233764864\",\"more_task\":\"\",\"etl_context\":\"测试shell\",\"divId\":\"420_a9f_9e80_ae\"},{\"job_type\":\"ETL\",\"task_log_instance_id\":\"906548124246347776\",\"more_task\":\"单源ETL\",\"etl_context\":\"单分割符无标题\",\"etl_task_id\":\"719619870378954752\",\"divId\":\"c04_40a_83c7_ee\"}],\"line\":[{\"pageTargetId\":\"c04_40a_83c7_ee\",\"pageSourceId\":\"420_a9f_9e80_ae\",\"connectionId\":\"con_12\"}],\"run_line\":[{\"from\":\"906548124233764864\",\"to\":\"906548124246347776\"}],\"tasks\":[{\"positionY\":60,\"name\":\"测试shell\",\"etl_context\":\"测试shell\",\"id\":\"420_a9f_9e80_ae\",\"type\":\"shell\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"420_a9f_9e80_ae\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"positionX\":258},{\"positionY\":159,\"name\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"etl_context\":\"单分割符无标题\",\"id\":\"c04_40a_83c7_ee\",\"etl_task_id\":\"719619870378954752\",\"type\":\"tasks\",\"divId\":\"c04_40a_83c7_ee\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"positionX\":256}]}',NULL,NULL,'2','off','off','on','on','on','on'),(906694439831867392,'906547945187315712','测试专用','2021-11-06 14:16:11','error','2021-11-06 16:00:00','2021-11-06 16:00:00','1','false','100',NULL,'1999-12-31 16:00:00',NULL,NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'ETL','2021-11-06 06:16:11','2024-11-06 06:16:11','1d','1','0',0,NULL,'',NULL,'2021-11-06 06:16:11',NULL,'0 0 0 * * ? *',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc','2021-11-06 06:16:11','0','zdh_web_1:906541082584551424','86400','{\"check_dep_time\":\"2021-11-07 00:00:00\"}','5','2021-11-06 16:00:00','off','','{\"tasks\":[{\"id\":\"420_a9f_9e80_ae\",\"etl_context\":\"测试shell\",\"depend_level\":\"0\",\"is_script\":\"false\",\"command\":\"hostname\",\"time_out\":\"86400\",\"divId\":\"420_a9f_9e80_ae\",\"name\":\"测试shell\",\"positionX\":258,\"positionY\":60,\"type\":\"shell\"},{\"id\":\"c04_40a_83c7_ee\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"divId\":\"c04_40a_83c7_ee\",\"name\":\"单分割符无标题\",\"positionX\":256,\"positionY\":159,\"type\":\"tasks\"}],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"420_a9f_9e80_ae\",\"pageTargetId\":\"c04_40a_83c7_ee\"}]}','{\"run_data\":[{\"job_type\":\"SHELL\",\"task_log_instance_id\":\"906694440016416768\",\"more_task\":\"\",\"etl_context\":\"测试shell\",\"divId\":\"420_a9f_9e80_ae\"},{\"job_type\":\"ETL\",\"task_log_instance_id\":\"906694440020611072\",\"more_task\":\"单源ETL\",\"etl_context\":\"单分割符无标题\",\"etl_task_id\":\"719619870378954752\",\"divId\":\"c04_40a_83c7_ee\"}],\"line\":[{\"pageTargetId\":\"c04_40a_83c7_ee\",\"pageSourceId\":\"420_a9f_9e80_ae\",\"connectionId\":\"con_9\"}],\"run_line\":[{\"from\":\"906694440016416768\",\"to\":\"906694440020611072\"}],\"tasks\":[{\"positionY\":60,\"name\":\"测试shell\",\"etl_context\":\"测试shell\",\"id\":\"420_a9f_9e80_ae\",\"type\":\"shell\",\"is_script\":\"false\",\"command\":\"hostname\",\"divId\":\"420_a9f_9e80_ae\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"positionX\":258},{\"positionY\":159,\"name\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"etl_context\":\"单分割符无标题\",\"id\":\"c04_40a_83c7_ee\",\"etl_task_id\":\"719619870378954752\",\"type\":\"tasks\",\"divId\":\"c04_40a_83c7_ee\",\"depend_level\":\"0\",\"time_out\":\"86400\",\"positionX\":256}]}',NULL,NULL,'1','off','off','on','on','on','on');
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
  `schedule_source` varchar(64) NOT NULL DEFAULT '1' COMMENT '调度来源,1:例行,2:手动',
  `alarm_email` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `alarm_sms` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `alarm_zdh` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_error` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_finish` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  `notice_timeout` varchar(8) NOT NULL DEFAULT 'off' COMMENT '启用邮箱告警 on:启用,off:禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=906694440020611073 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_log_instance`
--

LOCK TABLES `task_log_instance` WRITE;
/*!40000 ALTER TABLE `task_log_instance` DISABLE KEYS */;
INSERT INTO `task_log_instance` VALUES (906548124233764864,'906547945187315712','测试专用','906548123663339520','测试专用','2021-11-06 14:18:34','finish','2021-11-06 06:18:35','2021-11-06 06:18:35','1','true','100','1_56_906548124233764864','1999-12-31 16:00:00',NULL,NULL,NULL,NULL,NULL,NULL,'0','0',NULL,'','SHELL',NULL,NULL,'1d','1','0',1,'hostname','',NULL,'2021-11-06 06:18:34',NULL,'0 0 0 * * ? *',NULL,NULL,NULL,NULL,'测试shell','false',NULL,NULL,NULL,'5',NULL,NULL,'zyc','2021-11-06 06:18:34','0','zdh_web_1:906541082584551424','86400','{\"check_dep_time\":\"2021-11-06 14:18:36\"}','5',NULL,'off','','','','906548124246347776','','false','0','2','off','off','on','on','on','on'),(906548124246347776,'906547945187315712','测试专用','906548123663339520','测试专用','2021-11-06 14:18:34','error','2021-11-06 06:18:35','2021-11-06 06:18:42','1','true','17','1_58_906548124246347776','2021-11-06 06:18:47','323','{\"dsi_Input\":{\"data_source_context\":\"本地HDFS\",\"data_source_type\":\"HDFS\",\"dbtable\":\"\",\"driver\":\"\",\"id\":\"59\",\"password\":\"\",\"paths\":\"/data/csv/t1.txt\",\"url\":\"\",\"user\":\"\"},\"dsi_Output\":{\"data_source_context\":\"zdh_test\",\"data_source_type\":\"JDBC\",\"dbtable\":\"t1\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"id\":\"60\",\"password\":\"123456\",\"paths\":\"t1\",\"url\":\"jdbc:mysql://127.0.0.1:3306/zdh_test?serverTimezone=GMT%2B8&useSSL=false\",\"user\":\"zyc\"},\"etlTaskInfo\":{\"column_data_list\":[{\"column_alias\":\"name\",\"column_expr\":\"name\",\"column_md5\":\"88c-4d33-9cbb-e3\",\"column_name\":\"name\",\"column_type\":\"string\"},{\"column_alias\":\"sex\",\"column_expr\":\"sex\",\"column_md5\":\"ede-4385-b7ff-7f\",\"column_name\":\"sex\",\"column_type\":\"string\"},{\"column_alias\":\"job\",\"column_expr\":\"job\",\"column_md5\":\"9e6-442d-9a86-04\",\"column_name\":\"job\",\"column_type\":\"string\"},{\"column_alias\":\"addr\",\"column_expr\":\"addr\",\"column_md5\":\"42c-43f9-9077-97\",\"column_name\":\"addr\",\"column_type\":\"string\"},{\"column_alias\":\"age\",\"column_expr\":\"age\",\"column_md5\":\"723-4b22-a043-1c\",\"column_name\":\"age\",\"column_type\":\"string\"}],\"column_datas\":\"[{\\\"column_md5\\\":\\\"88c-4d33-9cbb-e3\\\",\\\"column_name\\\":\\\"name\\\",\\\"column_expr\\\":\\\"name\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"name\\\"},{\\\"column_md5\\\":\\\"ede-4385-b7ff-7f\\\",\\\"column_name\\\":\\\"sex\\\",\\\"column_expr\\\":\\\"sex\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"sex\\\"},{\\\"column_md5\\\":\\\"9e6-442d-9a86-04\\\",\\\"column_name\\\":\\\"job\\\",\\\"column_expr\\\":\\\"job\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"job\\\"},{\\\"column_md5\\\":\\\"42c-43f9-9077-97\\\",\\\"column_name\\\":\\\"addr\\\",\\\"column_expr\\\":\\\"addr\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"addr\\\"},{\\\"column_md5\\\":\\\"723-4b22-a043-1c\\\",\\\"column_name\\\":\\\"age\\\",\\\"column_expr\\\":\\\"age\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"age\\\"}]\",\"column_size\":\"\",\"company\":\"\",\"create_time\":1591612347000,\"data_source_type_input\":\"HDFS\",\"data_source_type_output\":\"JDBC\",\"data_sources_choose_input\":\"59\",\"data_sources_choose_output\":\"60\",\"data_sources_clear_output\":\"drop table t1\",\"data_sources_file_columns\":\"name,sex,job,addr,age\",\"data_sources_file_name_input\":\"/data/csv/t1.txt\",\"data_sources_file_name_output\":\"t1\",\"data_sources_filter_input\":\"\",\"data_sources_params_input\":\"\",\"data_sources_params_output\":\"\",\"data_sources_table_columns\":\"\",\"data_sources_table_name_input\":\"\",\"data_sources_table_name_output\":\"t1\",\"duplicate_columns\":\"\",\"encoding_input\":\"GBK\",\"encoding_output\":\"\",\"error_rate\":\"\",\"etl_context\":\"单分割符无标题\",\"file_type_input\":\"csv\",\"file_type_output\":\"\",\"header_input\":\"false\",\"header_output\":\"\",\"id\":\"719619870378954752\",\"merge_output\":\"-1\",\"model_output\":\"\",\"owner\":\"1\",\"partition_by_output\":\"\",\"primary_columns\":\"\",\"repartition_cols_input\":\"\",\"repartition_num_input\":\"\",\"rows_range\":\"1-100\",\"section\":\"\",\"sep_input\":\"|\",\"sep_output\":\"\",\"service\":\"\",\"update_context\":\"\"},\"task_logs_id\":\"906548124246347776\",\"tli\":{\"alarm_account\":\"zyc\",\"alarm_email\":\"off\",\"alarm_sms\":\"off\",\"alarm_zdh\":\"on\",\"concurrency\":\"0\",\"count\":1,\"cur_time\":1636179514000,\"depend_level\":\"0\",\"etl_context\":\"单分割符无标题\",\"etl_date\":\"2021-11-06 14:18:34\",\"etl_task_id\":\"719619870378954752\",\"expr\":\"0 0 0 * * ? *\",\"group_context\":\"测试专用\",\"group_id\":\"906548123663339520\",\"id\":\"906548124246347776\",\"interval_time\":\"5\",\"is_disenable\":\"false\",\"is_notice\":\"false\",\"is_retryed\":\"0\",\"job_context\":\"测试专用\",\"job_id\":\"906547945187315712\",\"job_model\":\"1\",\"job_type\":\"ETL\",\"jsmind_data\":\"\",\"last_time\":1636179514000,\"more_task\":\"单源ETL\",\"next_tasks\":\"\",\"notice_error\":\"on\",\"notice_finish\":\"on\",\"notice_timeout\":\"on\",\"owner\":\"1\",\"params\":\"{\\\"ETL_DATE\\\":\\\"2021-11-06 14:18:34\\\"}\",\"plan_count\":\"0\",\"pre_tasks\":\"906548124233764864\",\"priority\":\"5\",\"process\":\"10\",\"process_msg\":\"组装ETL信息\",\"process_time\":\"{\\\"check_dep_time\\\":\\\"2021-11-06 14:18:41\\\"}\",\"process_time2\":{\"check_dep_time\":\"2021-11-06 14:18:41\"},\"retry_time\":946656000000,\"run_jsmind_data\":\"\",\"run_time\":1636179515000,\"schedule_source\":\"2\",\"server_ack\":\"0\",\"server_id\":\"zdh_web_1:906541082584551424\",\"status\":\"dispatch\",\"step_size\":\"1d\",\"thread_id\":\"1_58_906548124246347776\",\"time_diff\":\"\",\"time_out\":\"86400\",\"update_time\":1636179521807,\"use_quartz_time\":\"off\"}}','http://DEEP-2020KLZJDI:60001/api/v1/zdh','local-1635869775600','http://127.0.0.1:18080/api/v1','local[*]','0','0',NULL,'单源ETL','ETL',NULL,NULL,'1d','1','0',1,NULL,'{\"ETL_DATE\":\"2021-11-06 14:18:34\"}','error','2021-11-06 06:18:34',NULL,'0 0 0 * * ? *',NULL,NULL,NULL,'719619870378954752','单分割符无标题',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc','2021-11-06 06:18:34','0','zdh_web_1:906541082584551424','86400','{\"check_dep_time\":\"2021-11-06 14:18:41\"}','5',NULL,'off','','','','','906548124233764864','false','0','2','off','off','on','on','on','on'),(906694440016416768,'906547945187315712','测试专用','906694439831867392','测试专用','2021-11-06 14:16:11','finish','2021-11-06 16:00:00','2021-11-06 16:00:00','1','true','100','1_68_906694440016416768','1999-12-31 16:00:00',NULL,NULL,NULL,NULL,NULL,NULL,'0','0',NULL,'','SHELL','2021-11-06 06:16:11','2024-11-06 06:16:11','1d','1','0',1,'hostname','',NULL,'2021-11-06 06:16:11',NULL,'0 0 0 * * ? *',NULL,NULL,NULL,NULL,'测试shell','false',NULL,NULL,NULL,'5',NULL,NULL,'zyc','2021-11-06 06:16:11','0','zdh_web_1:906541082584551424','86400','{\"check_dep_time\":\"2021-11-07 00:00:01\"}','5','2021-11-06 16:00:00','off','','','','906694440020611072','','false','0','1','off','off','on','on','on','on'),(906694440020611072,'906547945187315712','测试专用','906694439831867392','测试专用','2021-11-06 14:16:11','error','2021-11-06 16:00:00','2021-11-06 16:00:06','1','true','17','1_70_906694440020611072','2021-11-06 16:00:11','323','{\"dsi_Input\":{\"data_source_context\":\"本地HDFS\",\"data_source_type\":\"HDFS\",\"dbtable\":\"\",\"driver\":\"\",\"id\":\"59\",\"password\":\"\",\"paths\":\"/data/csv/t1.txt\",\"url\":\"\",\"user\":\"\"},\"dsi_Output\":{\"data_source_context\":\"zdh_test\",\"data_source_type\":\"JDBC\",\"dbtable\":\"t1\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"id\":\"60\",\"password\":\"123456\",\"paths\":\"t1\",\"url\":\"jdbc:mysql://127.0.0.1:3306/zdh_test?serverTimezone=GMT%2B8&useSSL=false\",\"user\":\"zyc\"},\"etlTaskInfo\":{\"column_data_list\":[{\"column_alias\":\"name\",\"column_expr\":\"name\",\"column_md5\":\"88c-4d33-9cbb-e3\",\"column_name\":\"name\",\"column_type\":\"string\"},{\"column_alias\":\"sex\",\"column_expr\":\"sex\",\"column_md5\":\"ede-4385-b7ff-7f\",\"column_name\":\"sex\",\"column_type\":\"string\"},{\"column_alias\":\"job\",\"column_expr\":\"job\",\"column_md5\":\"9e6-442d-9a86-04\",\"column_name\":\"job\",\"column_type\":\"string\"},{\"column_alias\":\"addr\",\"column_expr\":\"addr\",\"column_md5\":\"42c-43f9-9077-97\",\"column_name\":\"addr\",\"column_type\":\"string\"},{\"column_alias\":\"age\",\"column_expr\":\"age\",\"column_md5\":\"723-4b22-a043-1c\",\"column_name\":\"age\",\"column_type\":\"string\"}],\"column_datas\":\"[{\\\"column_md5\\\":\\\"88c-4d33-9cbb-e3\\\",\\\"column_name\\\":\\\"name\\\",\\\"column_expr\\\":\\\"name\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"name\\\"},{\\\"column_md5\\\":\\\"ede-4385-b7ff-7f\\\",\\\"column_name\\\":\\\"sex\\\",\\\"column_expr\\\":\\\"sex\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"sex\\\"},{\\\"column_md5\\\":\\\"9e6-442d-9a86-04\\\",\\\"column_name\\\":\\\"job\\\",\\\"column_expr\\\":\\\"job\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"job\\\"},{\\\"column_md5\\\":\\\"42c-43f9-9077-97\\\",\\\"column_name\\\":\\\"addr\\\",\\\"column_expr\\\":\\\"addr\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"addr\\\"},{\\\"column_md5\\\":\\\"723-4b22-a043-1c\\\",\\\"column_name\\\":\\\"age\\\",\\\"column_expr\\\":\\\"age\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"age\\\"}]\",\"column_size\":\"\",\"company\":\"\",\"create_time\":1591612347000,\"data_source_type_input\":\"HDFS\",\"data_source_type_output\":\"JDBC\",\"data_sources_choose_input\":\"59\",\"data_sources_choose_output\":\"60\",\"data_sources_clear_output\":\"drop table t1\",\"data_sources_file_columns\":\"name,sex,job,addr,age\",\"data_sources_file_name_input\":\"/data/csv/t1.txt\",\"data_sources_file_name_output\":\"t1\",\"data_sources_filter_input\":\"\",\"data_sources_params_input\":\"\",\"data_sources_params_output\":\"\",\"data_sources_table_columns\":\"\",\"data_sources_table_name_input\":\"\",\"data_sources_table_name_output\":\"t1\",\"duplicate_columns\":\"\",\"encoding_input\":\"GBK\",\"encoding_output\":\"\",\"error_rate\":\"\",\"etl_context\":\"单分割符无标题\",\"file_type_input\":\"csv\",\"file_type_output\":\"\",\"header_input\":\"false\",\"header_output\":\"\",\"id\":\"719619870378954752\",\"merge_output\":\"-1\",\"model_output\":\"\",\"owner\":\"1\",\"partition_by_output\":\"\",\"primary_columns\":\"\",\"repartition_cols_input\":\"\",\"repartition_num_input\":\"\",\"rows_range\":\"1-100\",\"section\":\"\",\"sep_input\":\"|\",\"sep_output\":\"\",\"service\":\"\",\"update_context\":\"\"},\"task_logs_id\":\"906694440020611072\",\"tli\":{\"alarm_account\":\"zyc\",\"alarm_email\":\"off\",\"alarm_sms\":\"off\",\"alarm_zdh\":\"on\",\"concurrency\":\"0\",\"count\":1,\"cur_time\":1636179371000,\"depend_level\":\"0\",\"end_time\":1730873771000,\"etl_context\":\"单分割符无标题\",\"etl_date\":\"2021-11-06 14:16:11\",\"etl_task_id\":\"719619870378954752\",\"expr\":\"0 0 0 * * ? *\",\"group_context\":\"测试专用\",\"group_id\":\"906694439831867392\",\"id\":\"906694440020611072\",\"interval_time\":\"5\",\"is_disenable\":\"false\",\"is_notice\":\"false\",\"is_retryed\":\"0\",\"job_context\":\"测试专用\",\"job_id\":\"906547945187315712\",\"job_model\":\"1\",\"job_type\":\"ETL\",\"jsmind_data\":\"\",\"last_time\":1636179371000,\"more_task\":\"单源ETL\",\"next_tasks\":\"\",\"notice_error\":\"on\",\"notice_finish\":\"on\",\"notice_timeout\":\"on\",\"owner\":\"1\",\"params\":\"{\\\"ETL_DATE\\\":\\\"2021-11-06 14:16:11\\\"}\",\"plan_count\":\"0\",\"pre_tasks\":\"906694440016416768\",\"priority\":\"5\",\"process\":\"10\",\"process_msg\":\"组装ETL信息\",\"process_time\":\"{\\\"check_dep_time\\\":\\\"2021-11-07 00:00:06\\\"}\",\"process_time2\":{\"check_dep_time\":\"2021-11-07 00:00:06\"},\"quartTime\":1636214400000,\"quartz_time\":1636214400000,\"retry_time\":946656000000,\"run_jsmind_data\":\"\",\"run_time\":1636214400000,\"schedule_source\":\"1\",\"server_ack\":\"0\",\"server_id\":\"zdh_web_1:906541082584551424\",\"start_time\":1636179371000,\"status\":\"dispatch\",\"step_size\":\"1d\",\"thread_id\":\"1_70_906694440020611072\",\"time_diff\":\"\",\"time_out\":\"86400\",\"update_time\":1636214406807,\"use_quartz_time\":\"off\"}}','http://DEEP-2020KLZJDI:60001/api/v1/zdh','local-1635869775600','http://127.0.0.1:18080/api/v1','local[*]','0','0',NULL,'单源ETL','ETL','2021-11-06 06:16:11','2024-11-06 06:16:11','1d','1','0',1,NULL,'{\"ETL_DATE\":\"2021-11-06 14:16:11\"}','error','2021-11-06 06:16:11',NULL,'0 0 0 * * ? *',NULL,NULL,NULL,'719619870378954752','单分割符无标题',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc','2021-11-06 06:16:11','0','zdh_web_1:906541082584551424','86400','{\"check_dep_time\":\"2021-11-07 00:00:06\"}','5','2021-11-06 16:00:00','off','','','','','906694440016416768','false','0','1','off','off','on','on','on','on');
/*!40000 ALTER TABLE `task_log_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_group_info`
--

DROP TABLE IF EXISTS `user_group_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_group_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '组名',
  `enable` varchar(8) DEFAULT 'false' COMMENT '是否启用true/false',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;
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
) ENGINE=InnoDB AUTO_INCREMENT=943 DEFAULT CHARSET=utf8mb4;
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
  `is_notice` varchar(8) DEFAULT 'false' COMMENT '是否已经通知true/false',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
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
-- Table structure for table `zdh_ha_info`
--

DROP TABLE IF EXISTS `zdh_ha_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zdh_ha_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `zdh_instance` varchar(100) DEFAULT NULL,
  `zdh_url` varchar(100) DEFAULT NULL,
  `zdh_host` varchar(15) DEFAULT NULL,
  `zdh_port` varchar(5) DEFAULT NULL,
  `web_port` varchar(100) DEFAULT NULL,
  `zdh_status` varchar(10) DEFAULT NULL,
  `application_id` varchar(100) DEFAULT NULL,
  `history_server` varchar(100) DEFAULT NULL,
  `master` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT current_timestamp(),
  `update_time` timestamp NULL DEFAULT current_timestamp(),
  `online` varchar(10) DEFAULT NULL COMMENT '是否上线true:上线,false:未上线',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=324 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zdh_ha_info`
--

LOCK TABLES `zdh_ha_info` WRITE;
/*!40000 ALTER TABLE `zdh_ha_info` DISABLE KEYS */;
INSERT INTO `zdh_ha_info` VALUES (323,'zdh_server','http://DEEP-2020KLZJDI:60001/api/v1/zdh','DEEP-2020KLZJDI','60001','4040','enabled','local-1635869775600','http://127.0.0.1:18080/api/v1','local[*]','2021-11-02 16:16:16','2021-11-02 16:27:54','1');
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
INSERT INTO `zdh_logs` VALUES ('904448275493425152','2021-11-06 05:51:04','[ETL] JOB ,调度命令执行失败未能发往任务到后台ETL执行','INFO','906319528634355712'),('904448275493425152','2021-11-06 05:51:04','[ETL] JOB ,调度命令执行失败未能发往任务到后台ETL执行,重试次数已达到最大,状态设置为error','ERROR','906319528634355712'),('904448275493425152','2021-11-06 05:51:09','更新进度为:100','INFO','906319528596606976'),('904448275493425152','2021-11-06 05:51:09','任务组以失败,具体信息请点击子任务查看','INFO','906319528596606976'),('906547945187315712','2021-11-06 06:18:35','生成任务组信息,任务组数据处理日期:2021-11-06 14:18:34','INFO','906548123663339520'),('906547945187315712','2021-11-06 06:18:36','[SHELL] JOB ,开始检查当前任务上游任务依赖','INFO','906548124233764864'),('906547945187315712','2021-11-06 06:18:36','[SHELL] JOB ,是根节点任务,无依赖,直接执行','INFO','906548124233764864'),('906547945187315712','2021-11-06 06:18:36','[ETL] JOB ,开始检查当前任务上游任务依赖','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:36','开始执行[SHELL] JOB','INFO','906548124233764864'),('906547945187315712','2021-11-06 06:18:36','所有的父级,祖先级任务存在未完成,跳过当前任务检查','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:36','[SHELL] JOB ,开始检查任务次数限制','INFO','906548124233764864'),('906547945187315712','2021-11-06 06:18:36','[SHELL] JOB ,完成检查任务次数限制,未超过限制次数','INFO','906548124233764864'),('906547945187315712','2021-11-06 06:18:36','shell任务当前只支持同步shell,异步shell暂不支持','INFO','906548124233764864'),('906547945187315712','2021-11-06 06:18:36','目前支持日期参数以下模式: {{zdh_date}} => yyyy-MM-dd ,{{zdh_date_nodash}}=> yyyyMMdd ,{{zdh_date_time}}=> yyyy-MM-dd HH:mm:ss,{{zdh_year}}=> 年,{{zdh_month}}=> 月,{{zdh_day}}=> 日,{{zdh_hour}}=>24小时制,{{zdh_minute}}=>分钟,{{zdh_second}}=>秒,{{zdh_time}}=>时间戳','INFO','906548124233764864'),('906547945187315712','2021-11-06 06:18:37','[SHELL] JOB ,COMMAND:hostname','INFO','906548124233764864'),('906547945187315712','2021-11-06 06:18:37','[SHELL] JOB ,以命令行方式执行','INFO','906548124233764864'),('906547945187315712','2021-11-06 06:18:37','[SHELL] JOB ,当前系统为:Linux,command:hostname,命令行方式执行;','INFO','906548124233764864'),('906547945187315712','2021-11-06 06:18:37','实时日志:izm5eexscw3e0firjnqdlzz','INFO','906548124233764864'),('906547945187315712','2021-11-06 06:18:37','[SHELL] JOB ,执行结果:success','INFO','906548124233764864'),('906547945187315712','2021-11-06 06:18:41','[ETL] JOB ,开始检查当前任务上游任务依赖','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','目前只判断父级任务是否完成,如果判断祖先级任务是否完成,可在此处做深度处理...','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','所有的父级,祖先级任务:[906548124233764864]','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','[ETL] JOB ,当前任务依赖上游任务状态:[成功],才会触发','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','已完成任务:906548124233764864','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','已杀死任务:','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','已失败任务:','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','未完成任务:','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','[ETL] JOB ,依赖任务状态,满足当前任务触发条件,ETL日期2021-11-06 14:18:34','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','开始执行[ETL] JOB','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','[ETL] JOB ,开始检查任务次数限制','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','[ETL] JOB ,完成检查任务次数限制,未超过限制次数','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','[ETL] JOB,任务模式为[时间序列]','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','[ETL] JOB ,调度命令执行成功,准备发往任务到后台ETL执行','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','获取服务端url,指定参数:','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','[ETL] JOB ,获取当前的[url]:http://DEEP-2020KLZJDI:60001/api/v1/zdh','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','目前支持日期参数以下模式: {{zdh_date}} => yyyy-MM-dd ,{{zdh_date_nodash}}=> yyyyMMdd ,{{zdh_date_time}}=> yyyy-MM-dd HH:mm:ss,{{zdh_year}}=> 年,{{zdh_month}}=> 月,{{zdh_day}}=> 日,{{zdh_hour}}=>24小时制,{{zdh_minute}}=>分钟,{{zdh_second}}=>秒,{{zdh_time}}=>时间戳','INFO','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','[调度平台]:[ETL] JOB ,开始发送ETL处理请求','DEBUG','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','[调度平台]:http://DEEP-2020KLZJDI:60001/api/v1/zdh ,参数:{\"dsi_Input\":{\"data_source_context\":\"本地HDFS\",\"data_source_type\":\"HDFS\",\"dbtable\":\"\",\"driver\":\"\",\"id\":\"59\",\"password\":\"\",\"paths\":\"/data/csv/t1.txt\",\"url\":\"\",\"user\":\"\"},\"dsi_Output\":{\"data_source_context\":\"zdh_test\",\"data_source_type\":\"JDBC\",\"dbtable\":\"t1\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"id\":\"60\",\"password\":\"123456\",\"paths\":\"t1\",\"url\":\"jdbc:mysql://127.0.0.1:3306/zdh_test?serverTimezone=GMT%2B8&useSSL=false\",\"user\":\"zyc\"},\"etlTaskInfo\":{\"column_data_list\":[{\"column_alias\":\"name\",\"column_expr\":\"name\",\"column_md5\":\"88c-4d33-9cbb-e3\",\"column_name\":\"name\",\"column_type\":\"string\"},{\"column_alias\":\"sex\",\"column_expr\":\"sex\",\"column_md5\":\"ede-4385-b7ff-7f\",\"column_name\":\"sex\",\"column_type\":\"string\"},{\"column_alias\":\"job\",\"column_expr\":\"job\",\"column_md5\":\"9e6-442d-9a86-04\",\"column_name\":\"job\",\"column_type\":\"string\"},{\"column_alias\":\"addr\",\"column_expr\":\"addr\",\"column_md5\":\"42c-43f9-9077-97\",\"column_name\":\"addr\",\"column_type\":\"string\"},{\"column_alias\":\"age\",\"column_expr\":\"age\",\"column_md5\":\"723-4b22-a043-1c\",\"column_name\":\"age\",\"column_type\":\"string\"}],\"column_datas\":\"[{\\\"column_md5\\\":\\\"88c-4d33-9cbb-e3\\\",\\\"column_name\\\":\\\"name\\\",\\\"column_expr\\\":\\\"name\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"name\\\"},{\\\"column_md5\\\":\\\"ede-4385-b7ff-7f\\\",\\\"column_name\\\":\\\"sex\\\",\\\"column_expr\\\":\\\"sex\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"sex\\\"},{\\\"column_md5\\\":\\\"9e6-442d-9a86-04\\\",\\\"column_name\\\":\\\"job\\\",\\\"column_expr\\\":\\\"job\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"job\\\"},{\\\"column_md5\\\":\\\"42c-43f9-9077-97\\\",\\\"column_name\\\":\\\"addr\\\",\\\"column_expr\\\":\\\"addr\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"addr\\\"},{\\\"column_md5\\\":\\\"723-4b22-a043-1c\\\",\\\"column_name\\\":\\\"age\\\",\\\"column_expr\\\":\\\"age\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"age\\\"}]\",\"column_size\":\"\",\"company\":\"\",\"create_time\":1591612347000,\"data_source_type_input\":\"HDFS\",\"data_source_type_output\":\"JDBC\",\"data_sources_choose_input\":\"59\",\"data_sources_choose_output\":\"60\",\"data_sources_clear_output\":\"drop table t1\",\"data_sources_file_columns\":\"name,sex,job,addr,age\",\"data_sources_file_name_input\":\"/data/csv/t1.txt\",\"data_sources_file_name_output\":\"t1\",\"data_sources_filter_input\":\"\",\"data_sources_params_input\":\"\",\"data_sources_params_output\":\"\",\"data_sources_table_columns\":\"\",\"data_sources_table_name_input\":\"\",\"data_sources_table_name_output\":\"t1\",\"duplicate_columns\":\"\",\"encoding_input\":\"GBK\",\"encoding_output\":\"\",\"error_rate\":\"\",\"etl_context\":\"单分割符无标题\",\"file_type_input\":\"csv\",\"file_type_output\":\"\",\"header_input\":\"false\",\"header_output\":\"\",\"id\":\"719619870378954752\",\"merge_output\":\"-1\",\"model_output\":\"\",\"owner\":\"1\",\"partition_by_output\":\"\",\"primary_columns\":\"\",\"repartition_cols_input\":\"\",\"repartition_num_input\":\"\",\"rows_range\":\"1-100\",\"section\":\"\",\"sep_input\":\"|\",\"sep_output\":\"\",\"service\":\"\",\"update_context\":\"\"},\"task_logs_id\":\"906548124246347776\",\"tli\":{\"alarm_account\":\"zyc\",\"alarm_email\":\"off\",\"alarm_sms\":\"off\",\"alarm_zdh\":\"on\",\"concurrency\":\"0\",\"count\":1,\"cur_time\":1636179514000,\"depend_level\":\"0\",\"etl_context\":\"单分割符无标题\",\"etl_date\":\"2021-11-06 14:18:34\",\"etl_task_id\":\"719619870378954752\",\"expr\":\"0 0 0 * * ? *\",\"group_context\":\"测试专用\",\"group_id\":\"906548123663339520\",\"id\":\"906548124246347776\",\"interval_time\":\"5\",\"is_disenable\":\"false\",\"is_notice\":\"false\",\"is_retryed\":\"0\",\"job_context\":\"测试专用\",\"job_id\":\"906547945187315712\",\"job_model\":\"1\",\"job_type\":\"ETL\",\"jsmind_data\":\"\",\"last_time\":1636179514000,\"more_task\":\"单源ETL\",\"next_tasks\":\"\",\"notice_error\":\"on\",\"notice_finish\":\"on\",\"notice_timeout\":\"on\",\"owner\":\"1\",\"params\":\"{\\\"ETL_DATE\\\":\\\"2021-11-06 14:18:34\\\"}\",\"plan_count\":\"0\",\"pre_tasks\":\"906548124233764864\",\"priority\":\"5\",\"process\":\"10\",\"process_msg\":\"组装ETL信息\",\"process_time\":\"{\\\"check_dep_time\\\":\\\"2021-11-06 14:18:41\\\"}\",\"process_time2\":{\"check_dep_time\":\"2021-11-06 14:18:41\"},\"retry_time\":946656000000,\"run_jsmind_data\":\"\",\"run_time\":1636179515000,\"schedule_source\":\"2\",\"server_ack\":\"0\",\"server_id\":\"zdh_web_1:906541082584551424\",\"status\":\"dispatch\",\"step_size\":\"1d\",\"thread_id\":\"1_58_906548124246347776\",\"time_diff\":\"\",\"time_out\":\"86400\",\"update_time\":1636179521807,\"use_quartz_time\":\"off\"}}','DEBUG','906548124246347776'),('906547945187315712','2021-11-06 06:18:41','[调度平台]:[ETL] JOB ,开始发送单源ETL 处理请求,异常请检查zdh_server服务是否正常运行,或者检查网络情况,postRequest -- IO error!','ERROR','906548124246347776'),('906547945187315712','2021-11-06 06:18:42','发送ETL任务到zdh处理引擎,存在问题,重试次数已达到最大,状态设置为error','ERROR','906548124246347776'),('906547945187315712','2021-11-06 06:18:46','更新进度为:100','INFO','906548123663339520'),('906547945187315712','2021-11-06 06:18:46','任务组以失败,具体信息请点击子任务查看','INFO','906548123663339520'),('906547945187315712','2021-11-06 16:00:00','生成任务组信息,任务组数据处理日期:2021-11-06 14:16:11','INFO','906694439831867392'),('906547945187315712','2021-11-06 16:00:01','[SHELL] JOB ,开始检查当前任务上游任务依赖','INFO','906694440016416768'),('906547945187315712','2021-11-06 16:00:01','[SHELL] JOB ,是根节点任务,无依赖,直接执行','INFO','906694440016416768'),('906547945187315712','2021-11-06 16:00:01','开始执行[SHELL] JOB','INFO','906694440016416768'),('906547945187315712','2021-11-06 16:00:01','[ETL] JOB ,开始检查当前任务上游任务依赖','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:01','[SHELL] JOB ,开始检查任务次数限制','INFO','906694440016416768'),('906547945187315712','2021-11-06 16:00:01','[SHELL] JOB ,完成检查任务次数限制,未超过限制次数','INFO','906694440016416768'),('906547945187315712','2021-11-06 16:00:01','所有的父级,祖先级任务存在未完成,跳过当前任务检查','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:01','shell任务当前只支持同步shell,异步shell暂不支持','INFO','906694440016416768'),('906547945187315712','2021-11-06 16:00:01','目前支持日期参数以下模式: {{zdh_date}} => yyyy-MM-dd ,{{zdh_date_nodash}}=> yyyyMMdd ,{{zdh_date_time}}=> yyyy-MM-dd HH:mm:ss,{{zdh_year}}=> 年,{{zdh_month}}=> 月,{{zdh_day}}=> 日,{{zdh_hour}}=>24小时制,{{zdh_minute}}=>分钟,{{zdh_second}}=>秒,{{zdh_time}}=>时间戳','INFO','906694440016416768'),('906547945187315712','2021-11-06 16:00:01','[SHELL] JOB ,COMMAND:hostname','INFO','906694440016416768'),('906547945187315712','2021-11-06 16:00:01','[SHELL] JOB ,以命令行方式执行','INFO','906694440016416768'),('906547945187315712','2021-11-06 16:00:01','[SHELL] JOB ,当前系统为:Linux,command:hostname,命令行方式执行;','INFO','906694440016416768'),('906547945187315712','2021-11-06 16:00:01','实时日志:izm5eexscw3e0firjnqdlzz','INFO','906694440016416768'),('906547945187315712','2021-11-06 16:00:01','[SHELL] JOB ,执行结果:success','INFO','906694440016416768'),('906547945187315712','2021-11-06 16:00:06','[ETL] JOB ,开始检查当前任务上游任务依赖','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','目前只判断父级任务是否完成,如果判断祖先级任务是否完成,可在此处做深度处理...','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','所有的父级,祖先级任务:[906694440016416768]','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','[ETL] JOB ,当前任务依赖上游任务状态:[成功],才会触发','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','已完成任务:906694440016416768','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','已杀死任务:','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','已失败任务:','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','未完成任务:','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','[ETL] JOB ,依赖任务状态,满足当前任务触发条件,ETL日期2021-11-06 14:16:11','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','开始执行[ETL] JOB','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','[ETL] JOB ,开始检查任务次数限制','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','[ETL] JOB ,完成检查任务次数限制,未超过限制次数','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','[ETL] JOB,任务模式为[时间序列]','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','[ETL] JOB ,调度命令执行成功,准备发往任务到后台ETL执行','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','获取服务端url,指定参数:','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','[ETL] JOB ,获取当前的[url]:http://DEEP-2020KLZJDI:60001/api/v1/zdh','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','目前支持日期参数以下模式: {{zdh_date}} => yyyy-MM-dd ,{{zdh_date_nodash}}=> yyyyMMdd ,{{zdh_date_time}}=> yyyy-MM-dd HH:mm:ss,{{zdh_year}}=> 年,{{zdh_month}}=> 月,{{zdh_day}}=> 日,{{zdh_hour}}=>24小时制,{{zdh_minute}}=>分钟,{{zdh_second}}=>秒,{{zdh_time}}=>时间戳','INFO','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','[调度平台]:[ETL] JOB ,开始发送ETL处理请求','DEBUG','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','[调度平台]:http://DEEP-2020KLZJDI:60001/api/v1/zdh ,参数:{\"dsi_Input\":{\"data_source_context\":\"本地HDFS\",\"data_source_type\":\"HDFS\",\"dbtable\":\"\",\"driver\":\"\",\"id\":\"59\",\"password\":\"\",\"paths\":\"/data/csv/t1.txt\",\"url\":\"\",\"user\":\"\"},\"dsi_Output\":{\"data_source_context\":\"zdh_test\",\"data_source_type\":\"JDBC\",\"dbtable\":\"t1\",\"driver\":\"com.mysql.cj.jdbc.Driver\",\"id\":\"60\",\"password\":\"123456\",\"paths\":\"t1\",\"url\":\"jdbc:mysql://127.0.0.1:3306/zdh_test?serverTimezone=GMT%2B8&useSSL=false\",\"user\":\"zyc\"},\"etlTaskInfo\":{\"column_data_list\":[{\"column_alias\":\"name\",\"column_expr\":\"name\",\"column_md5\":\"88c-4d33-9cbb-e3\",\"column_name\":\"name\",\"column_type\":\"string\"},{\"column_alias\":\"sex\",\"column_expr\":\"sex\",\"column_md5\":\"ede-4385-b7ff-7f\",\"column_name\":\"sex\",\"column_type\":\"string\"},{\"column_alias\":\"job\",\"column_expr\":\"job\",\"column_md5\":\"9e6-442d-9a86-04\",\"column_name\":\"job\",\"column_type\":\"string\"},{\"column_alias\":\"addr\",\"column_expr\":\"addr\",\"column_md5\":\"42c-43f9-9077-97\",\"column_name\":\"addr\",\"column_type\":\"string\"},{\"column_alias\":\"age\",\"column_expr\":\"age\",\"column_md5\":\"723-4b22-a043-1c\",\"column_name\":\"age\",\"column_type\":\"string\"}],\"column_datas\":\"[{\\\"column_md5\\\":\\\"88c-4d33-9cbb-e3\\\",\\\"column_name\\\":\\\"name\\\",\\\"column_expr\\\":\\\"name\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"name\\\"},{\\\"column_md5\\\":\\\"ede-4385-b7ff-7f\\\",\\\"column_name\\\":\\\"sex\\\",\\\"column_expr\\\":\\\"sex\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"sex\\\"},{\\\"column_md5\\\":\\\"9e6-442d-9a86-04\\\",\\\"column_name\\\":\\\"job\\\",\\\"column_expr\\\":\\\"job\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"job\\\"},{\\\"column_md5\\\":\\\"42c-43f9-9077-97\\\",\\\"column_name\\\":\\\"addr\\\",\\\"column_expr\\\":\\\"addr\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"addr\\\"},{\\\"column_md5\\\":\\\"723-4b22-a043-1c\\\",\\\"column_name\\\":\\\"age\\\",\\\"column_expr\\\":\\\"age\\\",\\\"column_type\\\":\\\"string\\\",\\\"column_alias\\\":\\\"age\\\"}]\",\"column_size\":\"\",\"company\":\"\",\"create_time\":1591612347000,\"data_source_type_input\":\"HDFS\",\"data_source_type_output\":\"JDBC\",\"data_sources_choose_input\":\"59\",\"data_sources_choose_output\":\"60\",\"data_sources_clear_output\":\"drop table t1\",\"data_sources_file_columns\":\"name,sex,job,addr,age\",\"data_sources_file_name_input\":\"/data/csv/t1.txt\",\"data_sources_file_name_output\":\"t1\",\"data_sources_filter_input\":\"\",\"data_sources_params_input\":\"\",\"data_sources_params_output\":\"\",\"data_sources_table_columns\":\"\",\"data_sources_table_name_input\":\"\",\"data_sources_table_name_output\":\"t1\",\"duplicate_columns\":\"\",\"encoding_input\":\"GBK\",\"encoding_output\":\"\",\"error_rate\":\"\",\"etl_context\":\"单分割符无标题\",\"file_type_input\":\"csv\",\"file_type_output\":\"\",\"header_input\":\"false\",\"header_output\":\"\",\"id\":\"719619870378954752\",\"merge_output\":\"-1\",\"model_output\":\"\",\"owner\":\"1\",\"partition_by_output\":\"\",\"primary_columns\":\"\",\"repartition_cols_input\":\"\",\"repartition_num_input\":\"\",\"rows_range\":\"1-100\",\"section\":\"\",\"sep_input\":\"|\",\"sep_output\":\"\",\"service\":\"\",\"update_context\":\"\"},\"task_logs_id\":\"906694440020611072\",\"tli\":{\"alarm_account\":\"zyc\",\"alarm_email\":\"off\",\"alarm_sms\":\"off\",\"alarm_zdh\":\"on\",\"concurrency\":\"0\",\"count\":1,\"cur_time\":1636179371000,\"depend_level\":\"0\",\"end_time\":1730873771000,\"etl_context\":\"单分割符无标题\",\"etl_date\":\"2021-11-06 14:16:11\",\"etl_task_id\":\"719619870378954752\",\"expr\":\"0 0 0 * * ? *\",\"group_context\":\"测试专用\",\"group_id\":\"906694439831867392\",\"id\":\"906694440020611072\",\"interval_time\":\"5\",\"is_disenable\":\"false\",\"is_notice\":\"false\",\"is_retryed\":\"0\",\"job_context\":\"测试专用\",\"job_id\":\"906547945187315712\",\"job_model\":\"1\",\"job_type\":\"ETL\",\"jsmind_data\":\"\",\"last_time\":1636179371000,\"more_task\":\"单源ETL\",\"next_tasks\":\"\",\"notice_error\":\"on\",\"notice_finish\":\"on\",\"notice_timeout\":\"on\",\"owner\":\"1\",\"params\":\"{\\\"ETL_DATE\\\":\\\"2021-11-06 14:16:11\\\"}\",\"plan_count\":\"0\",\"pre_tasks\":\"906694440016416768\",\"priority\":\"5\",\"process\":\"10\",\"process_msg\":\"组装ETL信息\",\"process_time\":\"{\\\"check_dep_time\\\":\\\"2021-11-07 00:00:06\\\"}\",\"process_time2\":{\"check_dep_time\":\"2021-11-07 00:00:06\"},\"quartTime\":1636214400000,\"quartz_time\":1636214400000,\"retry_time\":946656000000,\"run_jsmind_data\":\"\",\"run_time\":1636214400000,\"schedule_source\":\"1\",\"server_ack\":\"0\",\"server_id\":\"zdh_web_1:906541082584551424\",\"start_time\":1636179371000,\"status\":\"dispatch\",\"step_size\":\"1d\",\"thread_id\":\"1_70_906694440020611072\",\"time_diff\":\"\",\"time_out\":\"86400\",\"update_time\":1636214406807,\"use_quartz_time\":\"off\"}}','DEBUG','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','[调度平台]:[ETL] JOB ,开始发送单源ETL 处理请求,异常请检查zdh_server服务是否正常运行,或者检查网络情况,postRequest -- IO error!','ERROR','906694440020611072'),('906547945187315712','2021-11-06 16:00:06','发送ETL任务到zdh处理引擎,存在问题,重试次数已达到最大,状态设置为error','ERROR','906694440020611072'),('906547945187315712','2021-11-06 16:00:11','更新进度为:100','INFO','906694439831867392'),('906547945187315712','2021-11-06 16:00:11','任务组以失败,具体信息请点击子任务查看','INFO','906694439831867392');
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
INSERT INTO `zdh_nginx` VALUES (1,'zyc','','','22','1','/home/zyc/download','/home/zyc/work');
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

-- Dump completed on 2021-11-07 18:03:22

CREATE TABLE etl_task_jdbc_info (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `engine_type` varchar(200) DEFAULT null COMMENT '计算引擎,spark,local',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
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
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
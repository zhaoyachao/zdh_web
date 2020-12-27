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
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `user_password` varchar(100) DEFAULT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `is_use_email` varchar(10) DEFAULT NULL COMMENT '是否开启邮箱告警',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `is_use_phone` varchar(10) DEFAULT NULL COMMENT '是否开启手机告警',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `act2`
--

LOCK TABLES `act2` WRITE;
/*!40000 ALTER TABLE `act2` DISABLE KEYS */;
INSERT INTO `act2` VALUES ('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL),('1','zyc','123456','1209687056@qq.com',NULL,NULL,NULL);
/*!40000 ALTER TABLE `act2` ENABLE KEYS */;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_sources_info`
--

LOCK TABLES `data_sources_info` WRITE;
/*!40000 ALTER TABLE `data_sources_info` DISABLE KEYS */;
INSERT INTO `data_sources_info` VALUES (53,'mydb','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','2'),(54,'csv','HDFS','','','zyc@qq.com','123456','2'),(55,'mydb2','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','2'),(56,'HIVE1','HIVE','','','','','2'),(57,'第一个hive','HIVE','','','','','1'),(58,'mydb','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','1'),(59,'本地HDFS','HDFS','','','','','1'),(60,'zdh_test','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/zdh_test?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','1'),(61,'个人测试外部下载','外部下载','','','','','1'),(62,'本地kafka','KAFKA','','127.0.0.1:9092','','','1'),(63,'第一个clickhouse','JDBC','com.github.housepower.jdbc.ClickHouseDriver','jdbc:clickhouse://192.168.110.10:9000/datasets','default','','1'),(64,'第一个外部上传','外部上传','','','','','1'),(65,'第一个sftp','SFTP','','127.0.0.1:22','zyc','123456','1');
/*!40000 ALTER TABLE `data_sources_info` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_sources_type_info`
--

LOCK TABLES `data_sources_type_info` WRITE;
/*!40000 ALTER TABLE `data_sources_type_info` DISABLE KEYS */;
INSERT INTO `data_sources_type_info` VALUES (1,'JDBC'),(2,'HDFS'),(3,'HBASE'),(4,'MONGODB'),(5,'ES'),(6,'HIVE'),(7,'KAFKA'),(8,'HTTP'),(9,'REDIS'),(10,'CASSANDRA'),(11,'SFTP'),(12,'KUDU'),(13,'外部上传'),(14,'FLUME'),(15,'外部下载');
/*!40000 ALTER TABLE `data_sources_type_info` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=749279630074056705 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_drools_task_info`
--

LOCK TABLES `etl_drools_task_info` WRITE;
/*!40000 ALTER TABLE `etl_drools_task_info` DISABLE KEYS */;
INSERT INTO `etl_drools_task_info` VALUES (2,'第一个drools','719619870378954752','package rules\r\nimport java.util.HashMap\r\nrule \"alarm2\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") != \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"false\");\r\nend\r\n\r\nrule \"alarm\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") == \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"true\");\r\nend','dsts=true','60','JDBC','d1','d1','','drop table d1','1','2020-07-25 04:38:41',NULL,'','','',NULL,'单源ETL'),(749258437296132096,'hive->mysql->drools','718814940856586240','package rules\r\nimport java.util.HashMap\r\nrule \"alarm2\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") != \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"false\");\r\nend\r\n\r\nrule \"alarm\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") == \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"true\");\r\nend','','60','JDBC','act2','act2','','drop table act2','1','2020-08-29 05:25:32',NULL,'','','',NULL,'SQL'),(749279630074056704,'more_test_account_info','2','package rules\r\nimport java.util.HashMap\r\nrule \"alarm2\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") != \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"false\");\r\nend\r\n\r\nrule \"alarm\"\r\nwhen\r\nd1:HashMap(d1.get(\"name\") == \"zhaoyachao\")\r\nthen\r\nd1.put(\"dsts\",\"true\");\r\nend','','60','JDBC','act2','act2','','drop table act2','1','2020-08-29 06:49:45',NULL,'','','',NULL,'多源ETL');
/*!40000 ALTER TABLE `etl_drools_task_info` ENABLE KEYS */;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_more_task_info`
--

LOCK TABLES `etl_more_task_info` WRITE;
/*!40000 ALTER TABLE `etl_more_task_info` DISABLE KEYS */;
INSERT INTO `etl_more_task_info` VALUES (1,'ddd','719619870378954752,719629297702146048','','59','HDFS','','','','','1','2020-06-08 14:19:59','','csv','UTF-8','|',NULL),(2,'more_mydb_account_info','749279343477264384','select * from account_info','','','','','','','1','2020-08-29 06:49:19','','','','',NULL);
/*!40000 ALTER TABLE `etl_more_task_info` ENABLE KEYS */;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=749279343477264385 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etl_task_info`
--

LOCK TABLES `etl_task_info` WRITE;
/*!40000 ALTER TABLE `etl_task_info` DISABLE KEYS */;
INSERT INTO `etl_task_info` VALUES (719619870378954752,'单分割符无标题','59','HDFS','','/data/csv/t1.txt','name,sex,job,addr,age','','','','60','JDBC','t1','t1','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_desc\":\"姓名\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','drop table t1','1','2020-06-08 10:32:27','','','','','','1-100','',NULL,'','','csv','GBK','|','','','','false',''),(719629297702146048,'单分割符自带标题','59','HDFS','','/data/csv/h1.txt','','','','','60','JDBC','h1','h1','','[]','delete from h1','1','2020-06-08 11:09:55','','','','','','','',NULL,'','','csv','GBK','|','','','','true',''),(719630143433216000,'多分割符无标题','59','HDFS','','/data/csv/t2.txt','name,sex,job,addr,age','','','','60','JDBC','t2','t2','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"},{\"column_md5\":\"462-4b77-a7ac-48\",\"column_name\":\"\",\"column_expr\":\"$zdh_etl_date\",\"column_type\":\"string\",\"column_alias\":\"etl_date\"}]','drop table t2','1','2020-06-08 11:13:16','','','','','','','',NULL,'','','csv','GBK','|+','','','',NULL,NULL),(719630908637843456,'tab分割无标题','59','HDFS','','/data/csv/t3.txt','name,sex,job,addr,age','','{\"sep\":\"	\",\"encoding\":\"GBK\"}','','60','JDBC','t3','t3','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','delete from t3','1','2020-06-08 11:16:19','','','','','','','',NULL,'','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(724291182208749568,'单分割符无标题输出hudi','59','HDFS','','/data/csv/t1.txt','name,sex,job,addr,age','','','','59','HDFS','','/data/hudi/t1','{\"precombine_field_opt_key\":\"name\",\"recordkey_field_opt_key\":\"name\"}','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','','1','2020-06-21 07:54:35','','','','','','1-100','',NULL,'','','csv','GBK','|','hudi','','',NULL,NULL),(724312011898359808,'hudi输出多分割符无标题','59','HDFS','','/data/hudi/t1','name,sex,job,addr,age','','','','59','HDFS','','/data/csv/hudi_t1','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','','1','2020-06-21 09:17:21','','','','','','1-100','',NULL,'','','hudi','','','csv','UTF-8','+-',NULL,NULL),(728251791409418240,'kafka接收客户信息存mydb','62','KAFKA','','m1','name,age','','','','60','JDBC','m1','m1','','[{\"column_md5\":\"2c5-46c3-b4e7-96\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"a82-409a-b944-e2\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','','1','2020-07-02 06:12:37','','','','','','','',NULL,'','','','',',','','','','',''),(728647407415332864,'单分割符无标题2','59','HDFS','','/data/csv/t4.txt','name,sex,job,addr,age','','','','60','JDBC','t4','t4','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','delete from t4','1','2020-07-03 08:24:40','','','','','','1-100','',NULL,'','','csv','UTF-8','\\\\','','','','false',''),(728996795115376640,'单分割符无标题3','59','HDFS','','/data/csv/t5.txt','name,sex,job,addr,age','','','','60','JDBC','t5','t5','','[{\"column_md5\":\"d83-43ad-9bf4-b4\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"824-4631-8a86-da\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"eb4-45e3-a25a-fa\",\"column_name\":\"job\",\"column_expr\":\"job\",\"column_type\":\"string\",\"column_alias\":\"job\"},{\"column_md5\":\"a06-4fd0-8767-12\",\"column_name\":\"addr\",\"column_expr\":\"addr\",\"column_type\":\"string\",\"column_alias\":\"addr\"},{\"column_md5\":\"186-4777-9f13-00\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"}]','delete from t5','1','2020-07-04 07:33:00','','','','','','1-100','',NULL,'','','csv','UTF-8','//','','','','false',''),(731444359840403456,'clickhouse_datasets.z1转zdh_test.z1','63','JDBC','datasets.z1','','','name,age,sex,money','','','60','JDBC','z1','z1','','[{\"column_md5\":\"80c-4162-bf9a-59\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"fd7-4632-9e75-78\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"},{\"column_md5\":\"bbf-439a-8bfa-19\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"},{\"column_md5\":\"8ef-4d54-84d3-9d\",\"column_name\":\"money\",\"column_expr\":\"money\",\"column_type\":\"string\",\"column_alias\":\"money\"}]','','1','2020-07-11 01:38:45','','','','','','','',NULL,'','','','','','','','','',''),(746140955731562496,'sftp测试','65','SFTP','','/home/zyc/work/t1.txt','name,age,sex','','','','60','JDBC','t1','t1','','[{\"column_md5\":\"3fa-444c-9d88-45\",\"column_name\":\"name\",\"column_expr\":\"name\",\"column_type\":\"string\",\"column_alias\":\"name\"},{\"column_md5\":\"7bc-44e2-896d-1c\",\"column_name\":\"age\",\"column_expr\":\"age\",\"column_type\":\"string\",\"column_alias\":\"age\"},{\"column_md5\":\"ebc-4288-acf9-5e\",\"column_name\":\"sex\",\"column_expr\":\"sex\",\"column_type\":\"string\",\"column_alias\":\"sex\"}]','drop table t1','1','2020-08-20 14:57:46','','','','','','','',NULL,'','','csv','UTF-8',',','','','','false',''),(749279343477264384,'mydb#account_info','58','JDBC','account_info','','','id,user_name,user_password,email,is_use_email,phone,is_use_phone','','','','','','','','[{\"column_md5\":\"212-4fac-b1df-1a\",\"column_name\":\"id\",\"column_expr\":\"id\",\"column_type\":\"string\",\"column_alias\":\"id\"},{\"column_md5\":\"12f-4e82-8a9c-e3\",\"column_name\":\"user_name\",\"column_expr\":\"user_name\",\"column_type\":\"string\",\"column_alias\":\"user_name\"},{\"column_md5\":\"116-44b0-b692-15\",\"column_name\":\"user_password\",\"column_expr\":\"user_password\",\"column_type\":\"string\",\"column_alias\":\"user_password\"},{\"column_md5\":\"7eb-4515-9718-98\",\"column_name\":\"email\",\"column_expr\":\"email\",\"column_type\":\"string\",\"column_alias\":\"email\"},{\"column_md5\":\"4b2-45ed-9654-9f\",\"column_name\":\"is_use_email\",\"column_expr\":\"is_use_email\",\"column_type\":\"string\",\"column_alias\":\"is_use_email\"},{\"column_md5\":\"c1c-44d4-8a8e-35\",\"column_name\":\"phone\",\"column_expr\":\"phone\",\"column_type\":\"string\",\"column_alias\":\"phone\"},{\"column_md5\":\"474-43a6-af20-3b\",\"column_name\":\"is_use_phone\",\"column_expr\":\"is_use_phone\",\"column_type\":\"string\",\"column_alias\":\"is_use_phone\"}]','','1','2020-08-29 06:48:36','','','','','','','',NULL,'','','','','','','','','','');
/*!40000 ALTER TABLE `etl_task_info` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `every_day_notice` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `msg` text COMMENT '通知消息',
  `is_delete` varchar(10) DEFAULT NULL COMMENT '是否删除消息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `every_day_notice`
--

LOCK TABLES `every_day_notice` WRITE;
/*!40000 ALTER TABLE `every_day_notice` DISABLE KEYS */;
INSERT INTO `every_day_notice` VALUES (1,'当前版本v4.2,欢迎你提出建议','false');
/*!40000 ALTER TABLE `every_day_notice` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jar_file_info` (
  `id` varchar(20) DEFAULT NULL,
  `file_name` varchar(100) DEFAULT NULL COMMENT '文件名称',
  `path` varchar(100) DEFAULT NULL COMMENT '文件目录',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `jar_etl_id` varchar(20) DEFAULT NULL COMMENT '任务id',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `status` varchar(10) DEFAULT NULL COMMENT '文件状态是否删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jar_file_info`
--

LOCK TABLES `jar_file_info` WRITE;
/*!40000 ALTER TABLE `jar_file_info` DISABLE KEYS */;
INSERT INTO `jar_file_info` VALUES ('732534262112194560','rdp-core-2.0.jar',NULL,'2020-07-14 01:49:37','732534261759873024','1','success'),('732534576089403392','rdp-core-2.0.jar',NULL,'2020-07-14 01:50:52','732534575875493888','1','success'),('732534576756297728','report-common-1.0.7.jar',NULL,'2020-07-14 01:50:52','732534575875493888','1','success'),('732589310183739392','zdh_server.jar',NULL,'2020-07-14 05:28:22','732538726244159488','1','success'),('732643701108510720','log4j.properties',NULL,'2020-07-14 09:04:30','732538726244159488','1','success'),('749042705316712448','zdh_server.jar',NULL,'2020-08-28 15:08:17','749041924744155136','1',NULL),('749052568860102656','zdh_server.jar',NULL,'2020-08-28 15:47:29','749051365933715456','1','success'),('749054743187296256','zdh_server.jar',NULL,'2020-08-28 15:56:07','749054742851751936','1',NULL),('749058489606737920','log4j.properties',NULL,'2020-08-28 16:11:00','749055828161466368','1','success'),('749060185443536896','log4j.properties',NULL,'2020-08-28 16:17:45','749060184990552064','1',NULL),('749060348916535296','log4j.properties',NULL,'2020-08-28 16:18:24','749060348564213760','1',NULL),('749062114471055360','zdh_server.jar',NULL,'2020-08-28 16:25:24','749062114156482560','1',NULL),('749063562575482880','zdh_server.jar',NULL,'2020-08-28 16:31:10','749063562055389184','1','success');
/*!40000 ALTER TABLE `jar_file_info` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meta_database_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `db_name` varchar(200) DEFAULT NULL,
  `tb_name` varchar(100) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_database_info`
--

LOCK TABLES `meta_database_info` WRITE;
/*!40000 ALTER TABLE `meta_database_info` DISABLE KEYS */;
INSERT INTO `meta_database_info` VALUES (5,'default','act','1','2020-12-21 15:10:00');
/*!40000 ALTER TABLE `meta_database_info` ENABLE KEYS */;
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
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
  `jsmind_data` text COMMENT '任务血源关系',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quartz_job_info`
--

LOCK TABLES `quartz_job_info` WRITE;
/*!40000 ALTER TABLE `quartz_job_info` DISABLE KEYS */;
INSERT INTO `quartz_job_info` VALUES ('719630230595047424',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'finish',NULL,NULL,NULL,'719630143433216000',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('719631150493995008',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'finish',NULL,NULL,NULL,'719630908637843456',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('720684617849376768','读取hive并下载文件',NULL,'ETL','2020-12-13 16:00:00','2020-12-15 16:00:00','1d','1','2',0,NULL,'','error','2021-02-17 16:00:00',NULL,'1m','finish',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'zyc',NULL,'86400','5','2020-12-27 06:20:14','off','','{\"tasks\":[{\"id\":\"720684174964428800\",\"etl_task_id\":\"720684174964428800\",\"etl_context\":\"读取hive转外部下载\",\"more_task\":\"SQL\",\"divId\":\"d6f_96e_8be0_bb\",\"name\":\"读取hive转外部下载\",\"positionX\":352,\"positionY\":147,\"type\":\"tasks\"}],\"shell\":[],\"line\":[]}'),('724291412505399296',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'finish',NULL,NULL,NULL,'724291182208749568',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('724312221676474368',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'finish',NULL,NULL,NULL,'724312011898359808',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('728252156099956736','kafka接收客户信息存mydb',NULL,'ETL','2020-07-01 16:00:00','2020-07-30 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-07-08 00:00:00\"}','etl','2020-07-07 16:00:00','2020-07-08 16:00:00','100s','remove',NULL,NULL,NULL,'728251791409418240','kafka接收客户信息存mydb','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('728647515297026048','单分割符无标题2',NULL,'ETL','2020-10-28 16:00:00','2020-11-02 16:00:00','1d','1','1',0,'','','finish','2020-11-21 09:11:37','2020-11-21 09:13:17','100s','finish',NULL,NULL,NULL,'728647407415332864','单分割符无标题2','1',NULL,NULL,NULL,NULL,'','on',NULL,'zyc','779755492376317952','40','',NULL,'on','-10s',NULL),('728996892721025024','单分割符无标题3',NULL,'ETL','2020-10-28 16:00:00','2020-12-22 16:00:00','','1','5',0,'','','finish','2020-10-31 16:00:00','2020-11-01 16:00:00','100s','create',NULL,NULL,NULL,'728996795115376640','单分割符无标题3','1',NULL,'728647515297026048',NULL,NULL,'',NULL,NULL,'','772119486072360960','86400',NULL,NULL,NULL,NULL,NULL),('731444485837295616','clickhouse_datasets.z1转zdh_test.z1',NULL,'ETL','2020-07-10 16:00:00','2020-09-10 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-07-11 00:00:00\"}','finish','2020-07-10 16:00:00',NULL,'100s','create',NULL,NULL,NULL,'731444359840403456','clickhouse_datasets.z1转zdh_test.z1','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('732547003703103488','第一个jar',NULL,'ETL','2020-07-13 16:00:00','2020-08-07 16:00:00','','1','-1',1,'','{\"ETL_DATE\":\"2020-07-15 00:00:00\"}','finish','2020-07-14 16:00:00','2020-07-15 16:00:00','100s','create',NULL,NULL,NULL,'732538726244159488','第一个jar','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('736528842050506752','第一个drools处理',NULL,'ETL','2020-07-24 16:00:00','2020-09-24 16:00:00','','1','-1',1,'','{\"ETL_DATE\":\"2020-08-07 00:00:00\"}','finish','2020-08-06 16:00:00','2020-08-07 16:00:00','100s','finish',NULL,NULL,NULL,'2','第一个drools','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('746141094323949568','sftp测试',NULL,'ETL','2020-08-19 16:00:00','2020-09-19 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-08-25 00:00:00\"}','finish','2020-08-24 16:00:00','2020-08-25 16:00:00','100s','finish',NULL,NULL,NULL,'746140955731562496','sftp测试','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('749203382681473024','123',NULL,'ETL','2020-08-28 16:00:00','2020-09-04 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-08-30 00:00:00\"}','finish','2020-08-29 16:00:00','2020-08-30 16:00:00','100s','finish',NULL,NULL,NULL,'749064500069535744','123','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('749255207426199552','第一个drools',NULL,'ETL','2020-08-21 16:00:00','2020-08-28 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-08-23 00:00:00\"}','finish','2020-08-22 16:00:00','2020-08-23 16:00:00','100s','create',NULL,NULL,NULL,'2','第一个drools','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('749258659757821952','hive->mysql->drools',NULL,'ETL','2020-08-27 16:00:00','2020-09-04 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-08-30 00:00:00\"}','finish','2020-08-29 16:00:00','2020-08-30 16:00:00','100s','create',NULL,NULL,NULL,'749258437296132096','hive->mysql->drools','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('749280046954319872','more_test_account_info',NULL,'ETL','2020-08-27 16:00:00','2020-09-04 16:00:00','','1','3',1,'','{\"ETL_DATE\":\"2020-08-31 00:00:00\"}','finish','2020-08-30 16:00:00','2020-08-31 16:00:00','100s','create',NULL,NULL,NULL,'749279630074056704','more_test_account_info','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('758090172859420672','hudi输出多分割符无标题',NULL,'ETL','2020-09-21 16:00:00','2020-10-01 16:00:00','','1','2',0,'','','error',NULL,NULL,'100s','finish',NULL,NULL,NULL,'724312011898359808','hudi输出多分割符无标题','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'','758101270681620480',NULL,NULL,NULL,NULL,NULL,NULL),('785083582216409088','第一个多任务调度',NULL,'ETL','2020-12-04 16:00:00','2021-01-08 16:00:00','1d','1','1',0,'','','error','2020-12-04 16:00:00','2020-12-05 16:00:00','100s','finish',NULL,NULL,NULL,'719619870378954752','单分割符无标题','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'','785105270991753216','86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719619870378954752\",\"topic\":\"单分割符无标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}'),('785139391885479936','第二个多任务调度',NULL,'ETL','2020-12-04 16:00:00','2021-01-08 16:00:00','1d','1','1',0,'','','finish','2020-12-04 16:00:00','2020-12-05 16:00:00','100s','finish',NULL,NULL,NULL,'756934940771225600','读取hive转mysql','1',NULL,NULL,NULL,NULL,'5','on',NULL,'','785140586985295873','86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719619870378954752\",\"topic\":\"单分割符无标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"719630143433216000\",\"topic\":\"多分割符无标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"756934940771225600\",\"topic\":\"读取hive转mysql\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"SQL\"}]}'),('785144554482307072','第3个调度',NULL,'ETL','2020-12-05 16:00:00','2021-01-08 16:00:00','1d','1','3',0,'','','',NULL,NULL,'1000s','create',NULL,NULL,NULL,'728647407415332864','单分割符无标题2','1',NULL,NULL,NULL,NULL,'5','on',NULL,'',NULL,'86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719619870378954752\",\"topic\":\"单分割符无标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"728647407415332864\",\"topic\":\"单分割符无标题2\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}'),('785144896708153344','第5个调度',NULL,'ETL','2020-12-05 16:00:00','2020-12-25 16:00:00','1d','1','1',0,'','','',NULL,NULL,'1000s','create',NULL,NULL,NULL,'728996795115376640','单分割符无标题3','1',NULL,NULL,NULL,NULL,'5','on',NULL,'',NULL,'86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719629297702146048\",\"topic\":\"单分割符自带标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"728996795115376640\",\"topic\":\"单分割符无标题3\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}'),('785145024143691776','第5个调度',NULL,'ETL','2020-12-05 16:00:00','2020-12-25 16:00:00','1d','1','1',0,'','','',NULL,NULL,'1000s','create',NULL,NULL,NULL,'728996795115376640','单分割符无标题3','1',NULL,NULL,NULL,NULL,'5','on',NULL,'',NULL,'86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719629297702146048\",\"topic\":\"单分割符自带标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"728996795115376640\",\"topic\":\"单分割符无标题3\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}'),('785145538403110912','test1',NULL,'ETL','2020-12-05 16:00:00','2021-01-01 16:00:00','1d','1','3',0,'','','error','2020-12-06 16:00:00','2020-12-07 16:00:00','1000s','finish',NULL,NULL,NULL,'728996795115376640','单分割符无标题3','1',NULL,NULL,NULL,NULL,'5','on',NULL,'','785224041974730752','86400','5',NULL,'off','','{\"meta\":{\"name\":\"jsMind\",\"author\":\"hizzgdev@163.com\",\"version\":\"0.4.6\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"728647407415332864\",\"topic\":\"单分割符无标题2\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"728996795115376640\",\"topic\":\"单分割符无标题3\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}'),('787419291484950528','第一个测试',NULL,'ETL','2020-12-11 16:00:00','2020-12-19 16:00:00','1d','1','1',0,'','','finish','2020-12-11 16:00:00','2020-12-13 16:00:00','100s','finish',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'','788181262413074432','86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"719619870378954752\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"divId\":\"9cd_adb_b148_a0\",\"name\":\"单分割符无标题\",\"positionX\":243,\"positionY\":78,\"type\":\"tasks\"},{\"id\":\"719629297702146048\",\"etl_task_id\":\"719629297702146048\",\"etl_context\":\"单分割符自带标题\",\"more_task\":\"单源ETL\",\"divId\":\"974_5aa_8578_f1\",\"name\":\"单分割符自带标题\",\"positionX\":306,\"positionY\":261,\"type\":\"tasks\"}],\"line\":[{\"connectionId\":\"con_12\",\"pageSourceId\":\"9cd_adb_b148_a0\",\"pageTargetId\":\"974_5aa_8578_f1\"}]}'),('789654690387202048','shell测试',NULL,'ETL','2020-11-30 16:00:00','2020-12-18 16:00:00','1d','1','1',0,NULL,'','finish',NULL,NULL,'1d','remove',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5','on',NULL,'zyc',NULL,'86400','5',NULL,'off','','{\"tasks\":[{\"id\":\"719619870378954752\",\"etl_task_id\":\"719619870378954752\",\"etl_context\":\"单分割符无标题\",\"more_task\":\"单源ETL\",\"divId\":\"d31_88b_a993_0f\",\"name\":\"单分割符无标题\",\"positionX\":283,\"positionY\":246,\"type\":\"tasks\"}],\"shell\":[{\"id\":\"071_ea2_9056_58\",\"is_script\":\"false\",\"etl_context\":\"hostname\",\"command\":\"hostname\",\"divId\":\"071_ea2_9056_58\",\"name\":\"hostname\",\"positionX\":340,\"positionY\":78,\"type\":\"shell\"}],\"line\":[{\"connectionId\":\"con_9\",\"pageSourceId\":\"071_ea2_9056_58\",\"pageTargetId\":\"d31_88b_a993_0f\"}]}'),('789960126382477313','',NULL,'EMAIL','2020-12-19 12:59:31','2020-12-19 12:59:31',NULL,'3','-1',0,'',NULL,NULL,NULL,NULL,'15s','running',NULL,NULL,NULL,'email',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'86400','5',NULL,'off',NULL,NULL),('789960127024205825','',NULL,'RETRY','2020-12-19 12:59:31','2020-12-19 12:59:31',NULL,'3','-1',0,'',NULL,NULL,NULL,NULL,'1s','running',NULL,NULL,NULL,'retry',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'86400','5',NULL,'off',NULL,NULL),('789960127691100161','',NULL,'CHECK','2020-12-19 12:59:31','2020-12-19 12:59:31',NULL,'3','-1',0,'',NULL,NULL,NULL,NULL,'5s','running',NULL,NULL,NULL,'check',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'86400','5',NULL,'off',NULL,NULL),('792711397959012352','FIRST_LS',NULL,'ETL','2020-12-26 16:00:00','2020-12-27 16:00:00','1d','1','1',0,NULL,'','',NULL,NULL,'1d','create',NULL,NULL,NULL,NULL,'','1',NULL,NULL,NULL,NULL,'5',NULL,NULL,'',NULL,'86400','5',NULL,'on','','{\"tasks\":[{\"id\":\"749064500069535744\",\"etl_task_id\":\"749064500069535744\",\"etl_context\":\"123\",\"more_task\":\"SSH\",\"divId\":\"639_681_9009_98\",\"name\":\"123\",\"positionX\":232,\"positionY\":63,\"type\":\"tasks\"}],\"shell\":[],\"line\":[]}');
/*!40000 ALTER TABLE `quartz_job_info` ENABLE KEYS */;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=756934940771225601 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sql_task_info`
--

LOCK TABLES `sql_task_info` WRITE;
/*!40000 ALTER TABLE `sql_task_info` DISABLE KEYS */;
INSERT INTO `sql_task_info` VALUES (718814940856586240,'读取hive转mysql',NULL,'','','select * from act','58','JDBC','act2','act2','','','1','2020-06-06 05:13:57','','','','',NULL,NULL,NULL,NULL),(720684174964428800,'读取hive转外部下载',NULL,'','','select * from act','58','JDBC','act2','act2','','','1','2020-06-06 05:13:57','','','','','','','',''),(756925994555674624,'fddfdfs',NULL,'','','select * from a2','','','','','','','1','2020-09-19 09:13:40','','','','','','','',''),(756926234339840000,'fdfdfd',NULL,'','','select 2','','','','','','','1','2020-09-19 09:14:37','','','','','','','',''),(756934940771225600,'读取hive转mysql',NULL,'','','select * from act bb','58','JDBC','act2','act2','','','1','2020-09-19 09:49:13','','','','','','','','');
/*!40000 ALTER TABLE `sql_task_info` ENABLE KEYS */;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=749064500069535745 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ssh_task_info`
--

LOCK TABLES `ssh_task_info` WRITE;
/*!40000 ALTER TABLE `ssh_task_info` DISABLE KEYS */;
INSERT INTO `ssh_task_info` VALUES (749064500069535744,'123','127.0.0.1','22','zyc','123456','sh {{zdh_online_file}}','/home/zyc/z2','ls','','1','2020-08-28 16:34:54','','','','');
/*!40000 ALTER TABLE `ssh_task_info` ENABLE KEYS */;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=792758748119568385 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=792758748983595009 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_log_instance`
--


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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=211 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zdh_ha_info`
--

LOCK TABLES `zdh_ha_info` WRITE;
/*!40000 ALTER TABLE `zdh_ha_info` DISABLE KEYS */;
INSERT INTO `zdh_ha_info` VALUES (210,'zdh_server','http://DEEP-2020KLZJDI:60001/api/v1/zdh','DEEP-2020KLZJDI','60001','4040','enabled','local-1608563362567','http://127.0.0.1:18080/api/v1','local[7]','2020-12-21 15:09:23','2020-12-21 15:12:38');
/*!40000 ALTER TABLE `zdh_ha_info` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zdh_logs`
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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

-- Dump completed on 2020-12-27 14:56:37

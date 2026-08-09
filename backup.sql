-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: parttimego
-- ------------------------------------------------------
-- Server version	8.0.44

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
-- Table structure for table `application`
--

DROP TABLE IF EXISTS `application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `application` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `job_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `resume_id` bigint DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '0',
  `remark` varchar(500) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_job_user` (`job_id`,`user_id`),
  KEY `idx_job_id` (`job_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application`
--

LOCK TABLES `application` WRITE;
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` VALUES (1,1,26,1,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:08',0),(2,7,27,2,2,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:08',0),(3,5,28,3,0,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:08',0),(4,10,29,4,3,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:08',0),(5,1,28,3,0,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:08',0),(6,3,26,1,4,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:08',0),(7,27,32,NULL,4,'','2026-06-23 20:37:01','2026-06-23 20:37:01',0),(8,30,6,1,2,NULL,'2026-06-23 20:54:04','2026-06-23 20:54:04',0),(9,30,26,1,2,NULL,'2026-06-23 20:59:35','2026-06-23 20:59:35',0),(10,30,27,2,1,NULL,'2026-06-23 20:59:35','2026-06-23 20:59:35',0),(11,30,33,2,0,NULL,'2026-06-23 21:00:34','2026-06-23 21:00:34',0),(12,29,32,NULL,3,'','2026-06-23 23:13:11','2026-06-23 23:13:11',0),(13,1,32,NULL,4,'没姓名','2026-06-24 15:13:47','2026-06-24 15:13:47',0),(14,2,32,6,4,'不招大学生','2026-06-24 15:22:14','2026-06-24 15:22:14',0);
/*!40000 ALTER TABLE `application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorite`
--

DROP TABLE IF EXISTS `favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `job_id` bigint NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_job` (`user_id`,`job_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_favorite_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorite`
--

LOCK TABLES `favorite` WRITE;
/*!40000 ALTER TABLE `favorite` DISABLE KEYS */;
INSERT INTO `favorite` VALUES (1,26,3,'2026-06-23 17:54:08'),(2,26,7,'2026-06-23 17:54:08'),(3,27,1,'2026-06-23 17:54:08'),(4,28,10,'2026-06-23 17:54:08'),(5,29,11,'2026-06-23 17:54:08');
/*!40000 ALTER TABLE `favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_record`
--

DROP TABLE IF EXISTS `file_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_url` varchar(500) NOT NULL,
  `file_type` varchar(20) NOT NULL,
  `file_size` bigint DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_file_type` (`file_type`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_record`
--

LOCK TABLES `file_record` WRITE;
/*!40000 ALTER TABLE `file_record` DISABLE KEYS */;
INSERT INTO `file_record` VALUES (1,32,'简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/a3565662-c422-43a1-8b09-4c4faae74ae4.pdf','RESUME',204894,'2026-06-24 15:15:33',0),(2,32,'计算机二级.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/1e65ac53-635a-453d-9817-e89486549a8a.pdf','RESUME',777891,'2026-06-24 16:46:37',0),(3,32,'简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/849f9066-95b5-4932-ba63-b393a3305689.pdf','RESUME',204894,'2026-06-24 16:46:57',0),(4,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/15a40a95-87cb-436a-97e8-c517af89ac44.pdf','RESUME',182730,'2026-06-24 16:47:03',0),(5,32,'简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/c8eab295-fe70-476d-85a9-0abaea38e57f.pdf','RESUME',204894,'2026-06-24 17:28:52',0),(6,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/d380ec9f-e5c5-4299-af08-30848cfb4085.pdf','RESUME',182730,'2026-06-24 17:29:07',0),(7,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/c66bf228-0bd2-4d21-b1aa-ca4836b474d7.pdf','RESUME',182730,'2026-06-24 17:29:43',0),(8,32,'简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/7a853953-7192-4aed-b0a2-73405537cf4f.pdf','RESUME',204894,'2026-06-24 23:10:49',0),(9,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/dc2e8224-4903-4db6-b12f-3eff22fdad59.pdf','RESUME',182730,'2026-06-24 23:11:09',0),(10,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/3aaf30aa-18fc-43fb-a5d5-96d8e85be7dd.pdf','RESUME',182730,'2026-06-24 23:11:34',0),(11,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/0dd70caf-d552-4f2d-800d-7129ed6fc5ba.pdf','RESUME',182730,'2026-06-24 23:17:17',0),(12,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/5130d88d-c24b-4631-9aac-65880c1479e0.pdf','RESUME',182730,'2026-06-24 23:25:19',0),(13,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/774c6a9a-ce65-4cee-95f6-2ddea0b94bfd.pdf','RESUME',182730,'2026-06-24 23:34:49',0),(14,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/30a92061-b573-4c2d-9e67-06f74c5de358.pdf','RESUME',182730,'2026-06-24 23:42:58',0),(15,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/6adb9e64-7ee3-4eff-b79f-b52393fc4f58.pdf','RESUME',182730,'2026-06-24 23:43:13',0),(16,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/ccbcb352-6b26-4891-a2f8-ebde28761cf7.pdf','RESUME',182730,'2026-06-24 23:43:42',0),(17,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/58f1b943-d664-40c8-a832-7ac65a63560a.pdf','RESUME',182730,'2026-06-24 23:50:21',0),(18,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/41807345-7cf7-49cd-b3c5-bccf00dc042f.pdf','RESUME',182730,'2026-06-24 23:54:42',0),(19,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/4e18809f-b0b2-4f42-bc81-49a1c8ea1997.pdf','RESUME',182730,'2026-06-25 00:04:28',0),(20,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/10325872-1499-4722-8c84-e2914bba3f38.pdf','RESUME',182730,'2026-06-25 00:04:45',0),(21,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/51652d73-06d0-4b6c-be20-b78445b42fe6.pdf','RESUME',182730,'2026-06-25 00:21:08',0),(22,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/41d80825-a4fe-4eb1-b4bb-938cdaa149bc.pdf','RESUME',182730,'2026-06-25 00:30:31',0),(23,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/4d637e65-83a6-4f9b-948f-6cdd9298cd9e.pdf','RESUME',182730,'2026-06-25 00:30:48',0),(24,32,'黄宇枫简历.pdf','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/1cad2fc2-2d83-40b0-a9ae-03b08cfc0ff6.pdf','RESUME',182730,'2026-06-25 00:39:55',0);
/*!40000 ALTER TABLE `file_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interview`
--

DROP TABLE IF EXISTS `interview`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interview` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `application_id` bigint NOT NULL,
  `job_id` bigint NOT NULL,
  `employer_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  `interview_time` datetime NOT NULL,
  `interview_place` varchar(255) DEFAULT NULL,
  `interview_type` varchar(20) NOT NULL DEFAULT '线下',
  `interview_content` varchar(500) DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '0',
  `student_remark` varchar(500) DEFAULT NULL,
  `employer_remark` varchar(500) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_application_id` (`application_id`),
  KEY `idx_employer_id` (`employer_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_job_id` (`job_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interview`
--

LOCK TABLES `interview` WRITE;
/*!40000 ALTER TABLE `interview` DISABLE KEYS */;
INSERT INTO `interview` VALUES (1,8,30,31,6,'2026-06-25 14:00:00','test location','offline','test content',0,NULL,NULL,'2026-06-23 20:57:05','2026-06-23 20:57:05',0),(2,9,30,31,26,'2026-06-25 14:00:00','Beijing Haidian','offline','Please bring your resume',0,NULL,NULL,'2026-06-23 21:00:07','2026-06-23 21:00:07',0),(3,11,30,31,33,'2026-06-26 10:00:00','Shanghai Pudong','online','Online interview via Zoom',3,NULL,NULL,'2026-06-23 21:00:40','2026-06-23 21:00:40',0),(4,10,30,31,27,'2026-06-27 15:00:00','Guangzhou Tianhe','online','Online interview',0,NULL,NULL,'2026-06-23 22:31:07','2026-06-23 22:31:07',0),(5,12,30,31,28,'2026-06-28 10:00:00','Shenzhen Nanshan','offline','Face to face interview',0,NULL,NULL,'2026-06-23 22:31:07','2026-06-23 22:31:07',0),(6,12,29,21,32,'2026-06-24 02:00:00','','线上','',3,NULL,NULL,'2026-06-23 23:14:20','2026-06-23 23:14:20',0);
/*!40000 ALTER TABLE `interview` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(100) NOT NULL,
  `description` text,
  `salary_min` decimal(10,2) DEFAULT NULL,
  `salary_max` decimal(10,2) DEFAULT NULL,
  `salary_type` varchar(20) NOT NULL DEFAULT '日结',
  `city` varchar(50) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `work_time` varchar(100) DEFAULT NULL,
  `headcount` int DEFAULT '1',
  `status` tinyint NOT NULL DEFAULT '0',
  `reject_reason` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_city` (`city`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (1,21,'外卖骑手','负责区域内外卖配送，需要自备电动车，熟悉当地路线优先',150.00,200.00,'日结','北京','朝阳区望京SOHO','餐饮','10:00-22:00',10,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(2,21,'美团外卖分拣员','负责外卖订单分拣、打包，工作简单易上手',100.00,130.00,'日结','北京','朝阳区美团站点','餐饮','09:00-21:00',5,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(3,22,'星巴克兼职咖啡师','学习咖啡制作，负责门店日常运营，提供专业咖啡服务',25.00,30.00,'小时结','上海','静安区南京西路店','餐饮','灵活排班',3,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(4,22,'星巴克周末兼职','周末门店辅助工作，整理物料、清洁卫生',22.00,25.00,'小时结','上海','浦东新区陆家嘴店','餐饮','周六日 09:00-18:00',2,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(5,23,'肯德基服务员','负责点餐、配餐、清洁等工作，培训上岗',18.00,22.00,'小时结','广州','天河区正佳广场','服务员','排班制',8,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(6,23,'肯德基夜间兼职','夜间门店值守、清洁，适合夜猫子',25.00,30.00,'小时结','广州','越秀区北京路','服务员','22:00-06:00',3,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(7,24,'高中数学家教','辅导高一学生数学，需要数学专业或成绩优秀',120.00,150.00,'小时结','北京','海淀区学生家中','家教','周末 14:00-16:00',1,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(8,24,'小学英语家教','辅导小学三年级英语，需要英语专业',80.00,100.00,'小时结','北京','西城区学生家中','家教','周三周五 18:00-20:00',2,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(9,24,'钢琴陪练','陪练钢琴，需要钢琴基础',100.00,120.00,'小时结','上海','徐汇区','家教','每周二四 16:00-17:00',1,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(10,25,'超市促销员','负责商品促销推广，口才好、性格外向优先',150.00,180.00,'日结','深圳','南山区沃尔玛','促销','09:00-18:00',6,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(11,25,'周末传单派发','商场周边派发传单，工作轻松',100.00,120.00,'日结','深圳','福田区华强北','传单派发','周六日 10:00-17:00',10,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(12,25,'商场活动协助','配合商场活动执行，搬运物料、引导顾客',130.00,160.00,'日结','深圳','罗湖区东门','其他','活动期间 09:00-21:00',4,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(13,21,'便利店配送员','便利店订单配送，3公里范围内',120.00,150.00,'日结','北京','通州区','物流配送','灵活时间',5,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(14,22,'咖啡豆包装员','负责咖啡豆分装、贴标签',20.00,25.00,'小时结','上海','闵行区工厂','超市零售','周一至周五 08:00-17:00',4,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(15,23,'快餐店后厨帮工','协助后厨备料、清洁',18.00,22.00,'小时结','广州','番禺区','餐饮','午晚餐高峰期',3,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(16,21,'快递分拣员','负责快递包裹分拣、扫码',130.00,160.00,'日结','北京','大兴区物流园','物流配送','06:00-18:00',8,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(17,25,'超市收银员','负责收银、理货',15.00,18.00,'小时结','深圳','南山区盒马鲜生','超市零售','排班制',4,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(18,25,'展会协助','展会现场布置、接待',150.00,180.00,'日结','深圳','宝安区国际会展中心','活动执行','08:00-18:00',10,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(19,22,'小红书文案编辑','负责小红书内容创作',100.00,150.00,'日结','上海','远程办公','文案编辑','灵活时间',2,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(20,22,'电商美工','负责产品详情页设计',120.00,150.00,'日结','上海','浦东新区','设计美工','周一至周五 10:00-19:00',2,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(21,21,'小程序开发','微信小程序开发，需要有项目经验',200.00,300.00,'日结','北京','海淀区','IT技术','灵活时间',1,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(22,23,'在线客服','负责在线解答客户问题',15.00,18.00,'小时结','广州','天河区','客服','排班制',6,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(23,24,'行政助理','协助日常行政事务',100.00,120.00,'日结','北京','西城区','行政文员','周一至周五 09:00-18:00',2,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(24,24,'英语翻译','文档翻译，需要英语六级以上',150.00,200.00,'日结','北京','远程办公','翻译','灵活时间',1,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(25,25,'活动摄影','负责活动现场拍摄',200.00,300.00,'日结','深圳','福田区','摄影摄像','活动期间',2,1,NULL,'2026-06-23 17:54:08','2026-06-23 18:12:03',0),(26,21,'java后端开发','熟悉mysql，ssm，springboot',4000.00,5999.00,'月结','广州','琶洲','其他','周一至周五9：00-18：00',4,0,NULL,'2026-06-23 17:59:02','2026-06-23 17:59:02',0),(27,21,'java后端','',4000.00,6000.00,'月结','广州','琶洲','其他','',1,1,NULL,'2026-06-23 18:03:31','2026-06-23 18:03:31',0),(28,21,'1','1',12.00,22.00,'日结','广州','1','IT技术','',1,1,NULL,'2026-06-23 18:10:48','2026-06-23 18:10:48',0),(29,21,'java','',4000.00,5000.00,'日结','广州','1','IT技术','周六9-10',1,1,NULL,'2026-06-23 19:39:06','2026-06-23 19:39:06',0),(30,31,'test job','test description',100.00,200.00,'daily','beijing',NULL,'food','09:00-18:00',5,1,NULL,'2026-06-23 20:28:55','2026-06-23 20:28:55',0);
/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '接收者ID',
  `type` varchar(50) NOT NULL COMMENT '消息类型：APPLICATION/INTERVIEW/ACCEPTED/REJECTED/SYSTEM',
  `title` varchar(200) NOT NULL COMMENT '消息标题',
  `content` varchar(500) DEFAULT NULL COMMENT '消息内容',
  `related_id` bigint DEFAULT NULL COMMENT '关联ID（投递ID/面试ID等）',
  `is_read` tinyint NOT NULL DEFAULT '0' COMMENT '是否已读：0=未读 1=已读',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_user_read` (`user_id`,`is_read`),
  KEY `idx_user_time` (`user_id`,`create_time` DESC),
  KEY `idx_message_user_read` (`user_id`,`is_read`),
  KEY `idx_message_user_time` (`user_id`,`create_time` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息通知表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (1,21,'APPLICATION','收到新的投递',' 投递了您的岗位「外卖骑手」',13,1,'2026-06-24 15:13:47',0),(2,32,'ACCEPTED','恭喜您被录用','美团外卖 的岗位「java」已录用您',12,1,'2026-06-24 15:14:13',0),(3,32,'REJECTED','投递被拒绝','美团外卖 的岗位「外卖骑手」拒绝了您的投递',13,1,'2026-06-24 15:14:47',0),(4,21,'APPLICATION','收到新的投递',' 投递了您的岗位「美团外卖分拣员」',14,1,'2026-06-24 15:22:14',0),(5,32,'REJECTED','投递被拒绝','美团外卖 的岗位「美团外卖分拣员」拒绝了您的投递\n拒绝理由：不招大学生',14,1,'2026-06-24 15:56:24',0);
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oper_log`
--

DROP TABLE IF EXISTS `oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oper_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `username` varchar(50) DEFAULT NULL COMMENT '操作人用户名',
  `module` varchar(50) NOT NULL COMMENT '模块名称',
  `operation` varchar(100) NOT NULL COMMENT '操作描述',
  `method` varchar(200) NOT NULL COMMENT '请求方法',
  `params` text COMMENT '请求参数',
  `ip` varchar(50) DEFAULT NULL COMMENT '操作IP',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0=失败 1=成功',
  `error_msg` text COMMENT '错误信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oper_log`
--

LOCK TABLES `oper_log` WRITE;
/*!40000 ALTER TABLE `oper_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resume`
--

DROP TABLE IF EXISTS `resume`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resume` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `school` varchar(100) DEFAULT NULL,
  `major` varchar(100) DEFAULT NULL,
  `grade` varchar(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `skills` varchar(500) DEFAULT NULL,
  `experience` text,
  `self_intro` text,
  `project_experience` text COMMENT '项目经历',
  `expect_city` varchar(50) DEFAULT NULL,
  `expect_salary` varchar(50) DEFAULT NULL,
  `attachment_url` varchar(500) DEFAULT NULL COMMENT '简历附件URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_resume_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resume`
--

LOCK TABLES `resume` WRITE;
/*!40000 ALTER TABLE `resume` DISABLE KEYS */;
INSERT INTO `resume` VALUES (1,26,'张三','男','北京大学','计算机科学','大三','13900139001','zhangsan@example.com','Java,Python,Vue','曾在互联网公司实习2个月','热爱编程，学习能力强',NULL,'北京','3000-5000',NULL,'2026-06-23 17:54:08','2026-06-23 18:12:08',0),(2,27,'李四','女','复旦大学','英语专业','大二','13900139002','lisi@example.com','英语,日语,翻译','做过英语家教','性格开朗，善于沟通',NULL,'上海','2000-3000',NULL,'2026-06-23 17:54:08','2026-06-23 18:12:08',0),(3,28,'王五','男','中山大学','市场营销','大四','13900139003','wangwu@example.com','销售,PPT,数据分析','学生会外联部部长','有较强的组织协调能力',NULL,'广州','4000-6000',NULL,'2026-06-23 17:54:08','2026-06-23 18:12:08',0),(4,29,'赵六','女','深圳大学','设计专业','大三','13900139004','zhaoliu@example.com','PS,AI,Figma,视频剪辑',' freelance 设计师','创意十足，审美在线',NULL,'深圳','3000-5000',NULL,'2026-06-23 17:54:08','2026-06-23 18:12:08',0),(5,33,'Test Student','Male','Test University','CS','Junior','13900139010','student1@test.com','Java,Python','Internship','Good student',NULL,'Beijing','3000-5000',NULL,'2026-06-23 21:00:34','2026-06-23 21:00:34',0),(6,32,'黄宇枫','男','广东财经大学','软件工程','大三','17819299934','3094266842@qq.com','Java,JavaScript,C++,C,Vue,Spring,SpringBoot,SpringMVC,MyBatis,MySQL','','','项目名称：TLIAS 员工管理系统','','','https://java-ai-ffz01.oss-cn-beijing.aliyuncs.com/resume/1cad2fc2-2d83-40b0-a9ae-03b08cfc0ff6.pdf','2026-06-24 15:15:35','2026-06-24 15:15:35',0);
/*!40000 ALTER TABLE `resume` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `application_id` bigint NOT NULL,
  `from_user_id` bigint NOT NULL,
  `to_user_id` bigint NOT NULL,
  `job_id` bigint NOT NULL,
  `rating` tinyint NOT NULL DEFAULT '5',
  `content` varchar(500) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_application_id` (`application_id`),
  KEY `idx_to_user_id` (`to_user_id`),
  KEY `idx_job_id` (`job_id`),
  KEY `idx_review_job` (`job_id`),
  KEY `idx_review_to_user` (`to_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (1,4,29,25,10,5,'老板人很好，工资按时发放，工作氛围不错！','2026-06-23 17:54:08',0),(2,4,25,29,10,4,'赵六同学工作认真负责，推荐！','2026-06-23 17:54:08',0);
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '涓婚敭',
  `username` varchar(50) NOT NULL COMMENT '鐢ㄦ埛鍚',
  `password` varchar(255) NOT NULL COMMENT '瀵嗙爜锛圔Crypt锛',
  `role` varchar(20) NOT NULL COMMENT '瑙掕壊锛歋TUDENT/EMPLOYER/ADMIN',
  `nickname` varchar(50) DEFAULT NULL COMMENT '鏄电О',
  `avatar` varchar(255) DEFAULT NULL COMMENT '澶村儚URL',
  `phone` varchar(20) DEFAULT NULL COMMENT '鎵嬫満鍙',
  `email` varchar(100) DEFAULT NULL COMMENT '閭??',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '鐘舵?锛?=绂佺敤 1=姝ｅ父',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '閫昏緫鍒犻櫎锛?=鏈?垹闄?1=宸插垹闄',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='鐢ㄦ埛琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (21,'美团外卖','945f13f61b99449a5f470cca8d80a5f7','EMPLOYER','美团外卖',NULL,'13800138001','meituan@example.com',1,'2026-06-23 17:54:08','2026-06-23 17:54:08',0),(22,'星巴克','945f13f61b99449a5f470cca8d80a5f7','EMPLOYER','星巴克咖啡',NULL,'13800138002','starbucks@example.com',1,'2026-06-23 17:54:08','2026-06-23 17:54:08',0),(23,'肯德基','945f13f61b99449a5f470cca8d80a5f7','EMPLOYER','肯德基',NULL,'13800138003','kfc@example.com',1,'2026-06-23 17:54:08','2026-06-23 17:54:08',0),(24,'家教中心','945f13f61b99449a5f470cca8d80a5f7','EMPLOYER','大学生家教中心',NULL,'13800138004','jiajiao@example.com',1,'2026-06-23 17:54:08','2026-06-23 17:54:08',0),(25,'超市促销','945f13f61b99449a5f470cca8d80a5f7','EMPLOYER','大型超市',NULL,'13800138005','supermarket@example.com',1,'2026-06-23 17:54:08','2026-06-23 17:54:08',0),(26,'张三','945f13f61b99449a5f470cca8d80a5f7','STUDENT','张同学',NULL,'13900139001','zhangsan@example.com',1,'2026-06-23 17:54:08','2026-06-23 17:54:08',0),(27,'李四','945f13f61b99449a5f470cca8d80a5f7','STUDENT','李同学',NULL,'13900139002','lisi@example.com',1,'2026-06-23 17:54:08','2026-06-23 17:54:08',0),(28,'王五','945f13f61b99449a5f470cca8d80a5f7','STUDENT','王同学',NULL,'13900139003','wangwu@example.com',1,'2026-06-23 17:54:08','2026-06-23 17:54:08',0),(29,'赵六','945f13f61b99449a5f470cca8d80a5f7','STUDENT','赵同学',NULL,'13900139004','zhaoliu@example.com',1,'2026-06-23 17:54:08','2026-06-23 17:54:08',0),(30,'管理员','945f13f61b99449a5f470cca8d80a5f7','ADMIN','系统管理员',NULL,'13700137001','admin@example.com',1,'2026-06-23 17:54:08','2026-06-23 17:54:08',0),(31,'test','945f13f61b99449a5f470cca8d80a5f7','EMPLOYER','测试企业',NULL,'13800000001','test@test.com',1,'2026-06-23 20:28:18','2026-06-23 20:28:18',0),(32,'Mort1S','945f13f61b99449a5f470cca8d80a5f7','STUDENT','',NULL,NULL,NULL,1,'2026-06-23 20:36:51','2026-06-23 20:36:51',0),(33,'student1','945f13f61b99449a5f470cca8d80a5f7','STUDENT','Test Student',NULL,'13900139010','student1@test.com',1,'2026-06-23 21:00:34','2026-06-23 21:00:34',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-03 16:08:03

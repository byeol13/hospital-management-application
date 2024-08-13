CREATE DATABASE  IF NOT EXISTS `hospital` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `hospital`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: hospital
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admissionstate`
--

DROP TABLE IF EXISTS `admissionstate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admissionstate` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `enteringdate` timestamp NULL DEFAULT NULL,
  `exitingdate` timestamp NULL DEFAULT NULL,
  `cause` varchar(255) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `discharge` tinyint(1) DEFAULT NULL,
  `patientid` bigint DEFAULT NULL,
  `departmentid` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `admissionstateFK1` (`patientid`),
  KEY `admissionstateFK2` (`departmentid`),
  CONSTRAINT `admissionstateFK1` FOREIGN KEY (`patientid`) REFERENCES `patient` (`id`) ON DELETE CASCADE,
  CONSTRAINT `admissionstateFK2` FOREIGN KEY (`departmentid`) REFERENCES `department` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admissionstate`
--

LOCK TABLES `admissionstate` WRITE;
/*!40000 ALTER TABLE `admissionstate` DISABLE KEYS */;
INSERT INTO `admissionstate` VALUES (3,'2024-05-14 22:00:00','2024-05-25 21:03:09','problem me mushkeri','Dead',1,5,7),(11,'2024-04-05 22:00:00','2024-05-25 21:45:17','analiza','Transfer',1,3,7),(14,'2024-04-05 22:00:00','2024-05-25 22:01:53','analiza','Healthy',1,3,9),(22,'2024-04-05 22:00:00','2024-05-27 16:02:46','neurologji','Transfer',1,3,7),(24,NULL,NULL,'TestAdmit',NULL,0,NULL,NULL),(59,'2024-05-27 16:03:03','2024-06-12 00:58:42','analizat','Change department',1,3,13),(60,'2024-06-05 19:24:18','2024-06-05 19:24:34','analiza ','Transfer',1,5,13),(61,'2024-06-05 19:24:38','2024-06-05 19:25:00','surgery','Transfer',1,5,9),(62,'2024-06-05 19:25:12','2024-06-05 19:25:30','cardiology','Healthy',1,5,2),(66,'2024-06-12 01:02:13','2024-06-12 01:02:36','incident','Change department',1,1,1),(67,'2024-06-12 01:02:44','2024-06-12 01:03:09','intensive unit care','Healthy',1,1,7),(68,'2024-06-14 22:13:34','2024-06-15 14:21:40','emergency','Transfer',1,1,1),(69,'2024-06-14 22:19:16','2024-06-14 22:22:21','cardiology check','Change department',1,3,2),(70,'2024-06-14 22:24:00',NULL,'emergency',NULL,0,3,1),(71,'2024-06-15 14:22:39','2024-06-15 14:23:37','analiza','Change department',1,1,13),(72,'2024-06-15 14:23:42','2024-06-15 14:24:16','surgery','Healthy',1,1,9);
/*!40000 ALTER TABLE `admissionstate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clinicalrecord`
--

DROP TABLE IF EXISTS `clinicalrecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clinicalrecord` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `clinicaldata` varchar(500) DEFAULT NULL,
  `patientid` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `clinicalrecordFK` (`patientid`),
  CONSTRAINT `clinicalrecordFK` FOREIGN KEY (`patientid`) REFERENCES `patient` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clinicalrecord`
--

LOCK TABLES `clinicalrecord` WRITE;
/*!40000 ALTER TABLE `clinicalrecord` DISABLE KEYS */;
INSERT INTO `clinicalrecord` VALUES (1,'problem me veshkat',1),(2,'problem me veshken ana',1),(3,'problem me mushkeri andrea',3),(4,'problem me veshkat marjana',5),(5,'problem me mushkeri marjana',5),(7,'problzm me kycet ana',1),(8,'problem i ri',3),(9,'analiza laboratorike',5);
/*!40000 ALTER TABLE `clinicalrecord` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(10) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'EMERG','Emergency'),(2,'CARD','Cardiology'),(7,'ICU','Intensive Care Unit'),(8,'NEUR','Neurologu'),(9,'SURG','Surgery'),(13,'LAB','Laborator'),(19,'testDep ','testDep');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `birthdate` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (1,'ana','meta','2000-02-10 00:00:00.000000'),(3,'andrea','ramaj','2004-10-09 00:00:00.000000'),(5,'marjana','aliu','2002-10-09 00:00:00.000000');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'test@test.com','test'),(2,'user@test.com','user');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-17 22:09:19

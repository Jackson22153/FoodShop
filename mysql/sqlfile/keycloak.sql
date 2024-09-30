CREATE DATABASE  IF NOT EXISTS `keycloak` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `keycloak`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: keycloak
-- ------------------------------------------------------
-- Server version	9.0.1

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
-- Table structure for table `ADMIN_EVENT_ENTITY`
--

DROP TABLE IF EXISTS `ADMIN_EVENT_ENTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ADMIN_EVENT_ENTITY` (
  `ID` varchar(36) NOT NULL,
  `ADMIN_EVENT_TIME` bigint DEFAULT NULL,
  `REALM_ID` varchar(255) DEFAULT NULL,
  `OPERATION_TYPE` varchar(255) DEFAULT NULL,
  `AUTH_REALM_ID` varchar(255) DEFAULT NULL,
  `AUTH_CLIENT_ID` varchar(255) DEFAULT NULL,
  `AUTH_USER_ID` varchar(255) DEFAULT NULL,
  `IP_ADDRESS` varchar(255) DEFAULT NULL,
  `RESOURCE_PATH` text,
  `REPRESENTATION` text,
  `ERROR` varchar(255) DEFAULT NULL,
  `RESOURCE_TYPE` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_ADMIN_EVENT_TIME` (`REALM_ID`,`ADMIN_EVENT_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ADMIN_EVENT_ENTITY`
--

LOCK TABLES `ADMIN_EVENT_ENTITY` WRITE;
/*!40000 ALTER TABLE `ADMIN_EVENT_ENTITY` DISABLE KEYS */;
/*!40000 ALTER TABLE `ADMIN_EVENT_ENTITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASSOCIATED_POLICY`
--

DROP TABLE IF EXISTS `ASSOCIATED_POLICY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ASSOCIATED_POLICY` (
  `POLICY_ID` varchar(36) NOT NULL,
  `ASSOCIATED_POLICY_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`POLICY_ID`,`ASSOCIATED_POLICY_ID`),
  KEY `IDX_ASSOC_POL_ASSOC_POL_ID` (`ASSOCIATED_POLICY_ID`),
  CONSTRAINT `FK_FRSR5S213XCX4WNKOG82SSRFY` FOREIGN KEY (`ASSOCIATED_POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`),
  CONSTRAINT `FK_FRSRPAS14XCX4WNKOG82SSRFY` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASSOCIATED_POLICY`
--

LOCK TABLES `ASSOCIATED_POLICY` WRITE;
/*!40000 ALTER TABLE `ASSOCIATED_POLICY` DISABLE KEYS */;
INSERT INTO `ASSOCIATED_POLICY` VALUES ('18a31336-8c4e-4d22-bd5a-252a2498a8c4','96aef6b8-c5cb-4037-afa0-e2c85805836b');
/*!40000 ALTER TABLE `ASSOCIATED_POLICY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AUTHENTICATION_EXECUTION`
--

DROP TABLE IF EXISTS `AUTHENTICATION_EXECUTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUTHENTICATION_EXECUTION` (
  `ID` varchar(36) NOT NULL,
  `ALIAS` varchar(255) DEFAULT NULL,
  `AUTHENTICATOR` varchar(36) DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  `FLOW_ID` varchar(36) DEFAULT NULL,
  `REQUIREMENT` int DEFAULT NULL,
  `PRIORITY` int DEFAULT NULL,
  `AUTHENTICATOR_FLOW` bit(1) NOT NULL DEFAULT b'0',
  `AUTH_FLOW_ID` varchar(36) DEFAULT NULL,
  `AUTH_CONFIG` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_AUTH_EXEC_REALM_FLOW` (`REALM_ID`,`FLOW_ID`),
  KEY `IDX_AUTH_EXEC_FLOW` (`FLOW_ID`),
  CONSTRAINT `FK_AUTH_EXEC_FLOW` FOREIGN KEY (`FLOW_ID`) REFERENCES `AUTHENTICATION_FLOW` (`ID`),
  CONSTRAINT `FK_AUTH_EXEC_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AUTHENTICATION_EXECUTION`
--

LOCK TABLES `AUTHENTICATION_EXECUTION` WRITE;
/*!40000 ALTER TABLE `AUTHENTICATION_EXECUTION` DISABLE KEYS */;
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('083e74d6-cd2a-4e75-bc18-a33c0d497062',NULL,'auth-otp-form','8d500cba-ea97-4fd0-b969-91f305c70891','a2b172eb-02aa-463e-8f07-f4c5bf6e1c60',0,20,_binary '\0',NULL,NULL),('1840389d-6aef-4fe4-aa3e-f82536963f6d',NULL,'reset-credentials-choose-user','8d500cba-ea97-4fd0-b969-91f305c70891','541f3d44-53cb-439b-949d-b701210d1e7b',0,10,_binary '\0',NULL,NULL),('1b0a9df4-29bd-4633-911e-225a23fd83b1',NULL,'auth-otp-form','8d500cba-ea97-4fd0-b969-91f305c70891','2d117980-a8a7-4dcc-8ec9-8c3dd08bb33b',0,20,_binary '\0',NULL,NULL),('266cfb32-ab07-44d7-a502-a90de9dce4b8',NULL,'auth-username-password-form','8d500cba-ea97-4fd0-b969-91f305c70891','be4acd6a-2ef7-4027-a46c-b3b57b656030',0,10,_binary '\0',NULL,NULL),('2cc2f41d-40fa-44f9-883c-53be5fd0a1a4',NULL,'registration-page-form','8d500cba-ea97-4fd0-b969-91f305c70891','670cd7c2-219c-45c4-b7e0-7169bb82c932',0,10,_binary '','72e4aee9-2aff-4b65-95b5-52f24e7f6aa6',NULL),('320e9429-7a85-49a6-bb7b-1d080518d640',NULL,'idp-review-profile','8d500cba-ea97-4fd0-b969-91f305c70891','a4b6093e-e4bd-48bc-ad3e-bb8568d6517b',0,10,_binary '\0',NULL,'23b13b79-689e-485b-aa6f-979648f576f2'),('37434c81-6ae5-4713-9d74-8e345d621b72',NULL,NULL,'8d500cba-ea97-4fd0-b969-91f305c70891','d4922d03-1495-4786-94bc-29798e5fabba',2,30,_binary '','be4acd6a-2ef7-4027-a46c-b3b57b656030',NULL),('380c748b-e16e-478e-b203-89f51a8d17c2',NULL,NULL,'8d500cba-ea97-4fd0-b969-91f305c70891','a4b6093e-e4bd-48bc-ad3e-bb8568d6517b',0,20,_binary '','2f3c0d02-3a0d-476b-b30d-480d7ea562e5',NULL),('396694a2-2f94-4224-8acc-bfbdc43b1c3e',NULL,'http-basic-authenticator','8d500cba-ea97-4fd0-b969-91f305c70891','5b3483c5-0f4a-4b32-8ea3-9c95d5f288cf',0,10,_binary '\0',NULL,NULL),('40829574-5835-4e39-8eaf-f8a8f0806475',NULL,'docker-http-basic-authenticator','8d500cba-ea97-4fd0-b969-91f305c70891','e6831c4b-a6a0-4554-807a-3ac47ce86e2d',0,10,_binary '\0',NULL,NULL),('4852aed9-3d7e-48c6-9af8-7fa8d57ffe79',NULL,'reset-credential-email','8d500cba-ea97-4fd0-b969-91f305c70891','541f3d44-53cb-439b-949d-b701210d1e7b',0,20,_binary '\0',NULL,NULL),('4f4edf04-fa10-45d3-826a-e14744703ac0',NULL,NULL,'8d500cba-ea97-4fd0-b969-91f305c70891','32eac8bb-eb2b-4c12-b9e6-e6b6a6e172c2',2,20,_binary '','0e8c1bfe-07e3-4d89-96c4-50e85ed85bbc',NULL),('5bcafd8d-1fc0-4cf6-a4bf-757819a65221',NULL,'conditional-user-configured','8d500cba-ea97-4fd0-b969-91f305c70891','2d117980-a8a7-4dcc-8ec9-8c3dd08bb33b',0,10,_binary '\0',NULL,NULL),('5dddade3-c35e-4b6e-8484-015ad939486b',NULL,'client-secret-jwt','8d500cba-ea97-4fd0-b969-91f305c70891','6364010b-a2a2-42fc-81d0-b64d9aa2f80d',2,30,_binary '\0',NULL,NULL),('667cbbb4-d570-4e7f-b8ad-873e66158d3a',NULL,NULL,'8d500cba-ea97-4fd0-b969-91f305c70891','0e8c1bfe-07e3-4d89-96c4-50e85ed85bbc',1,20,_binary '','a2b172eb-02aa-463e-8f07-f4c5bf6e1c60',NULL),('733685ef-a431-4dbf-9f91-a244ce17136f',NULL,'client-jwt','8d500cba-ea97-4fd0-b969-91f305c70891','6364010b-a2a2-42fc-81d0-b64d9aa2f80d',2,20,_binary '\0',NULL,NULL),('7b86ea36-dc00-403e-b69e-1774ab95e969',NULL,NULL,'8d500cba-ea97-4fd0-b969-91f305c70891','be4acd6a-2ef7-4027-a46c-b3b57b656030',1,20,_binary '','2d117980-a8a7-4dcc-8ec9-8c3dd08bb33b',NULL),('8382ce6c-b72b-4006-a188-1d336bb6040e',NULL,'auth-cookie','8d500cba-ea97-4fd0-b969-91f305c70891','d4922d03-1495-4786-94bc-29798e5fabba',2,10,_binary '\0',NULL,NULL),('8a0ba222-2ecf-4320-a555-6c9bd6789aea',NULL,'conditional-user-configured','8d500cba-ea97-4fd0-b969-91f305c70891','1737ef65-d7c4-4b6d-9050-2477ed365f3d',0,10,_binary '\0',NULL,NULL),('8b6c41af-a66e-4bbf-bec1-12e59ede866f',NULL,'idp-username-password-form','8d500cba-ea97-4fd0-b969-91f305c70891','0e8c1bfe-07e3-4d89-96c4-50e85ed85bbc',0,10,_binary '\0',NULL,NULL),('8c94536d-8406-4664-a990-83e8ed04da1f',NULL,'registration-password-action','8d500cba-ea97-4fd0-b969-91f305c70891','72e4aee9-2aff-4b65-95b5-52f24e7f6aa6',0,50,_binary '\0',NULL,NULL),('8cb62f4f-0f5e-468a-a82d-b22f25b1db77',NULL,'direct-grant-validate-username','8d500cba-ea97-4fd0-b969-91f305c70891','0a656aa2-c9c4-46ec-a570-f51b934e2b96',0,10,_binary '\0',NULL,NULL),('93e07c67-cc18-44d6-ab28-2eba14390fc3',NULL,'client-secret','8d500cba-ea97-4fd0-b969-91f305c70891','6364010b-a2a2-42fc-81d0-b64d9aa2f80d',2,10,_binary '\0',NULL,NULL),('9c3212a9-571a-4f09-b7b1-8287da42076e',NULL,NULL,'8d500cba-ea97-4fd0-b969-91f305c70891','2f3c0d02-3a0d-476b-b30d-480d7ea562e5',2,20,_binary '','8b54403e-645a-4e22-ac77-608d48ee2a72',NULL),('a281d90d-a185-4cd3-b92a-f1d4b054b6cf',NULL,'direct-grant-validate-otp','8d500cba-ea97-4fd0-b969-91f305c70891','1737ef65-d7c4-4b6d-9050-2477ed365f3d',0,20,_binary '\0',NULL,NULL),('a3cf28fe-0d2b-4441-8429-ae2d3e2e6808',NULL,'idp-email-verification','8d500cba-ea97-4fd0-b969-91f305c70891','32eac8bb-eb2b-4c12-b9e6-e6b6a6e172c2',2,10,_binary '\0',NULL,NULL),('ac44db77-8b8e-4866-820a-afb45f9dad56',NULL,NULL,'8d500cba-ea97-4fd0-b969-91f305c70891','0a656aa2-c9c4-46ec-a570-f51b934e2b96',1,30,_binary '','1737ef65-d7c4-4b6d-9050-2477ed365f3d',NULL),('af52aa2d-bec2-4638-b067-ecdc58daa215',NULL,'registration-recaptcha-action','8d500cba-ea97-4fd0-b969-91f305c70891','72e4aee9-2aff-4b65-95b5-52f24e7f6aa6',3,60,_binary '\0',NULL,NULL),('b117b47f-57a6-48ce-98ea-90b9cae6aefe',NULL,'conditional-user-configured','8d500cba-ea97-4fd0-b969-91f305c70891','3b2c867c-7f57-415c-a77a-f840a0991013',0,10,_binary '\0',NULL,NULL),('b3063a7e-f8dd-4640-b66e-fcb3f4f04ca9',NULL,'client-x509','8d500cba-ea97-4fd0-b969-91f305c70891','6364010b-a2a2-42fc-81d0-b64d9aa2f80d',2,40,_binary '\0',NULL,NULL),('b568aef8-32b7-4583-8923-1751ccebb90f',NULL,'direct-grant-validate-password','8d500cba-ea97-4fd0-b969-91f305c70891','0a656aa2-c9c4-46ec-a570-f51b934e2b96',0,20,_binary '\0',NULL,NULL),('c1b2c5f9-a369-4150-a24b-1f417d0f0171',NULL,NULL,'8d500cba-ea97-4fd0-b969-91f305c70891','541f3d44-53cb-439b-949d-b701210d1e7b',1,40,_binary '','3b2c867c-7f57-415c-a77a-f840a0991013',NULL),('cac0d55c-334c-40b8-b7d3-060bf6dc9720',NULL,'idp-create-user-if-unique','8d500cba-ea97-4fd0-b969-91f305c70891','2f3c0d02-3a0d-476b-b30d-480d7ea562e5',2,10,_binary '\0',NULL,'31f35e64-82f0-4a59-8c2d-b957eb71cb30'),('cc9a52d8-ed69-47c5-9d8b-3eab0a7bb8d7',NULL,'identity-provider-redirector','8d500cba-ea97-4fd0-b969-91f305c70891','d4922d03-1495-4786-94bc-29798e5fabba',2,25,_binary '\0',NULL,NULL),('d00d5cf7-4447-4833-a009-bb78a8a08fa7',NULL,'idp-confirm-link','8d500cba-ea97-4fd0-b969-91f305c70891','8b54403e-645a-4e22-ac77-608d48ee2a72',0,10,_binary '\0',NULL,NULL),('de2a4933-336b-49d6-9e97-a143983c57c2',NULL,'reset-otp','8d500cba-ea97-4fd0-b969-91f305c70891','3b2c867c-7f57-415c-a77a-f840a0991013',0,20,_binary '\0',NULL,NULL),('e14cc9ef-ce14-44a8-a106-1af3ee3f9f47',NULL,NULL,'8d500cba-ea97-4fd0-b969-91f305c70891','8b54403e-645a-4e22-ac77-608d48ee2a72',0,20,_binary '','32eac8bb-eb2b-4c12-b9e6-e6b6a6e172c2',NULL),('e8e3355d-e8eb-4b8f-a774-4a975fab59b4',NULL,'auth-spnego','8d500cba-ea97-4fd0-b969-91f305c70891','d4922d03-1495-4786-94bc-29798e5fabba',3,20,_binary '\0',NULL,NULL),('f6bc78ab-2dbc-485f-9464-c91ccbfbad2a',NULL,'conditional-user-configured','8d500cba-ea97-4fd0-b969-91f305c70891','a2b172eb-02aa-463e-8f07-f4c5bf6e1c60',0,10,_binary '\0',NULL,NULL),('fb63a911-8f53-4665-8940-696f4573254e',NULL,'registration-user-creation','8d500cba-ea97-4fd0-b969-91f305c70891','72e4aee9-2aff-4b65-95b5-52f24e7f6aa6',0,20,_binary '\0',NULL,NULL),('fbf59303-3866-485f-9211-37c78a72d14f',NULL,'registration-terms-and-conditions','8d500cba-ea97-4fd0-b969-91f305c70891','72e4aee9-2aff-4b65-95b5-52f24e7f6aa6',3,70,_binary '\0',NULL,NULL),('fe74ce64-a500-4882-9eb4-e358d6f30b5f',NULL,'reset-password','8d500cba-ea97-4fd0-b969-91f305c70891','541f3d44-53cb-439b-949d-b701210d1e7b',0,30,_binary '\0',NULL,NULL);
/*!40000 ALTER TABLE `AUTHENTICATION_EXECUTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AUTHENTICATION_FLOW`
--

DROP TABLE IF EXISTS `AUTHENTICATION_FLOW`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUTHENTICATION_FLOW` (
  `ID` varchar(36) NOT NULL,
  `ALIAS` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  `PROVIDER_ID` varchar(36) NOT NULL DEFAULT 'basic-flow',
  `TOP_LEVEL` bit(1) NOT NULL DEFAULT b'0',
  `BUILT_IN` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`ID`),
  KEY `IDX_AUTH_FLOW_REALM` (`REALM_ID`),
  CONSTRAINT `FK_AUTH_FLOW_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AUTHENTICATION_FLOW`
--

LOCK TABLES `AUTHENTICATION_FLOW` WRITE;
/*!40000 ALTER TABLE `AUTHENTICATION_FLOW` DISABLE KEYS */;
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('0a656aa2-c9c4-46ec-a570-f51b934e2b96','direct grant','OpenID Connect Resource Owner Grant','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '',_binary ''),('0e8c1bfe-07e3-4d89-96c4-50e85ed85bbc','Verify Existing Account by Re-authentication','Reauthentication of existing account','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '\0',_binary ''),('1737ef65-d7c4-4b6d-9050-2477ed365f3d','Direct Grant - Conditional OTP','Flow to determine if the OTP is required for the authentication','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '\0',_binary ''),('2d117980-a8a7-4dcc-8ec9-8c3dd08bb33b','Browser - Conditional OTP','Flow to determine if the OTP is required for the authentication','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '\0',_binary ''),('2f3c0d02-3a0d-476b-b30d-480d7ea562e5','User creation or linking','Flow for the existing/non-existing user alternatives','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '\0',_binary ''),('32eac8bb-eb2b-4c12-b9e6-e6b6a6e172c2','Account verification options','Method with which to verity the existing account','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '\0',_binary ''),('3b2c867c-7f57-415c-a77a-f840a0991013','Reset - Conditional OTP','Flow to determine if the OTP should be reset or not. Set to REQUIRED to force.','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '\0',_binary ''),('541f3d44-53cb-439b-949d-b701210d1e7b','reset credentials','Reset credentials for a user if they forgot their password or something','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '',_binary ''),('5b3483c5-0f4a-4b32-8ea3-9c95d5f288cf','saml ecp','SAML ECP Profile Authentication Flow','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '',_binary ''),('6364010b-a2a2-42fc-81d0-b64d9aa2f80d','clients','Base authentication for clients','8d500cba-ea97-4fd0-b969-91f305c70891','client-flow',_binary '',_binary ''),('670cd7c2-219c-45c4-b7e0-7169bb82c932','registration','registration flow','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '',_binary ''),('72e4aee9-2aff-4b65-95b5-52f24e7f6aa6','registration form','registration form','8d500cba-ea97-4fd0-b969-91f305c70891','form-flow',_binary '\0',_binary ''),('8b54403e-645a-4e22-ac77-608d48ee2a72','Handle Existing Account','Handle what to do if there is existing account with same email/username like authenticated identity provider','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '\0',_binary ''),('a2b172eb-02aa-463e-8f07-f4c5bf6e1c60','First broker login - Conditional OTP','Flow to determine if the OTP is required for the authentication','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '\0',_binary ''),('a4b6093e-e4bd-48bc-ad3e-bb8568d6517b','first broker login','Actions taken after first broker login with identity provider account, which is not yet linked to any Keycloak account','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '',_binary ''),('be4acd6a-2ef7-4027-a46c-b3b57b656030','forms','Username, password, otp and other auth forms.','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '\0',_binary ''),('d4922d03-1495-4786-94bc-29798e5fabba','browser','browser based authentication','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '',_binary ''),('e6831c4b-a6a0-4554-807a-3ac47ce86e2d','docker auth','Used by Docker clients to authenticate against the IDP','8d500cba-ea97-4fd0-b969-91f305c70891','basic-flow',_binary '',_binary '');
/*!40000 ALTER TABLE `AUTHENTICATION_FLOW` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AUTHENTICATOR_CONFIG`
--

DROP TABLE IF EXISTS `AUTHENTICATOR_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUTHENTICATOR_CONFIG` (
  `ID` varchar(36) NOT NULL,
  `ALIAS` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_AUTH_CONFIG_REALM` (`REALM_ID`),
  CONSTRAINT `FK_AUTH_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AUTHENTICATOR_CONFIG`
--

LOCK TABLES `AUTHENTICATOR_CONFIG` WRITE;
/*!40000 ALTER TABLE `AUTHENTICATOR_CONFIG` DISABLE KEYS */;
INSERT INTO `AUTHENTICATOR_CONFIG` VALUES ('23b13b79-689e-485b-aa6f-979648f576f2','review profile config','8d500cba-ea97-4fd0-b969-91f305c70891'),('31f35e64-82f0-4a59-8c2d-b957eb71cb30','create unique user config','8d500cba-ea97-4fd0-b969-91f305c70891');
/*!40000 ALTER TABLE `AUTHENTICATOR_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AUTHENTICATOR_CONFIG_ENTRY`
--

DROP TABLE IF EXISTS `AUTHENTICATOR_CONFIG_ENTRY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUTHENTICATOR_CONFIG_ENTRY` (
  `AUTHENTICATOR_ID` varchar(36) NOT NULL,
  `VALUE` longtext,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`AUTHENTICATOR_ID`,`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AUTHENTICATOR_CONFIG_ENTRY`
--

LOCK TABLES `AUTHENTICATOR_CONFIG_ENTRY` WRITE;
/*!40000 ALTER TABLE `AUTHENTICATOR_CONFIG_ENTRY` DISABLE KEYS */;
INSERT INTO `AUTHENTICATOR_CONFIG_ENTRY` VALUES ('23b13b79-689e-485b-aa6f-979648f576f2','missing','update.profile.on.first.login'),('31f35e64-82f0-4a59-8c2d-b957eb71cb30','false','require.password.update.after.registration');
/*!40000 ALTER TABLE `AUTHENTICATOR_CONFIG_ENTRY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BROKER_LINK`
--

DROP TABLE IF EXISTS `BROKER_LINK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BROKER_LINK` (
  `IDENTITY_PROVIDER` varchar(255) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `BROKER_USER_ID` varchar(255) DEFAULT NULL,
  `BROKER_USERNAME` varchar(255) DEFAULT NULL,
  `TOKEN` text,
  `USER_ID` varchar(255) NOT NULL,
  PRIMARY KEY (`IDENTITY_PROVIDER`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BROKER_LINK`
--

LOCK TABLES `BROKER_LINK` WRITE;
/*!40000 ALTER TABLE `BROKER_LINK` DISABLE KEYS */;
/*!40000 ALTER TABLE `BROKER_LINK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT`
--

DROP TABLE IF EXISTS `CLIENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT` (
  `ID` varchar(36) NOT NULL,
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `FULL_SCOPE_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `CLIENT_ID` varchar(255) DEFAULT NULL,
  `NOT_BEFORE` int DEFAULT NULL,
  `PUBLIC_CLIENT` bit(1) NOT NULL DEFAULT b'0',
  `SECRET` varchar(255) DEFAULT NULL,
  `BASE_URL` varchar(255) DEFAULT NULL,
  `BEARER_ONLY` bit(1) NOT NULL DEFAULT b'0',
  `MANAGEMENT_URL` varchar(255) DEFAULT NULL,
  `SURROGATE_AUTH_REQUIRED` bit(1) NOT NULL DEFAULT b'0',
  `REALM_ID` varchar(36) DEFAULT NULL,
  `PROTOCOL` varchar(255) DEFAULT NULL,
  `NODE_REREG_TIMEOUT` int DEFAULT '0',
  `FRONTCHANNEL_LOGOUT` bit(1) NOT NULL DEFAULT b'0',
  `CONSENT_REQUIRED` bit(1) NOT NULL DEFAULT b'0',
  `NAME` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `SERVICE_ACCOUNTS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `CLIENT_AUTHENTICATOR_TYPE` varchar(255) DEFAULT NULL,
  `ROOT_URL` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `REGISTRATION_TOKEN` varchar(255) DEFAULT NULL,
  `STANDARD_FLOW_ENABLED` bit(1) NOT NULL DEFAULT b'1',
  `IMPLICIT_FLOW_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `DIRECT_ACCESS_GRANTS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `ALWAYS_DISPLAY_IN_CONSOLE` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_B71CJLBENV945RB6GCON438AT` (`REALM_ID`,`CLIENT_ID`),
  KEY `IDX_CLIENT_ID` (`CLIENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT`
--

LOCK TABLES `CLIENT` WRITE;
/*!40000 ALTER TABLE `CLIENT` DISABLE KEYS */;
INSERT INTO `CLIENT` VALUES ('5983633b-180b-4071-bcc5-58248f3ea1b4',_binary '',_binary '','b7afe9b7-6c53-44a1-8e76-bff80f45bead',0,_binary '\0','BFiwf7shEhqNNPcLLFaaBwB8oiVStDAi','http://localhost:5173',_binary '\0','',_binary '\0','8d500cba-ea97-4fd0-b969-91f305c70891','openid-connect',-1,_binary '',_binary '\0','Foodshop',_binary '','client-secret','','',NULL,_binary '',_binary '\0',_binary '',_binary '\0'),('724f9f53-5886-4a8b-b797-591ad05a9caa',_binary '',_binary '\0','security-admin-console',0,_binary '',NULL,'/admin/master/console/',_binary '\0',NULL,_binary '\0','8d500cba-ea97-4fd0-b969-91f305c70891','openid-connect',0,_binary '\0',_binary '\0','${client_security-admin-console}',_binary '\0','client-secret','${authAdminUrl}',NULL,NULL,_binary '',_binary '\0',_binary '\0',_binary '\0'),('7391f048-5a3e-480f-8cfc-5b8d90e27912',_binary '',_binary '\0','account-console',0,_binary '',NULL,'/realms/master/account/',_binary '\0',NULL,_binary '\0','8d500cba-ea97-4fd0-b969-91f305c70891','openid-connect',0,_binary '\0',_binary '\0','${client_account-console}',_binary '\0','client-secret','${authBaseUrl}',NULL,NULL,_binary '',_binary '\0',_binary '\0',_binary '\0'),('74fd16f8-3604-42d8-bf5f-078e3c4406d6',_binary '',_binary '\0','broker',0,_binary '\0',NULL,NULL,_binary '',NULL,_binary '\0','8d500cba-ea97-4fd0-b969-91f305c70891','openid-connect',0,_binary '\0',_binary '\0','${client_broker}',_binary '\0','client-secret',NULL,NULL,NULL,_binary '',_binary '\0',_binary '\0',_binary '\0'),('d372473f-7d72-44b8-8301-7369776129e3',_binary '',_binary '\0','master-realm',0,_binary '\0',NULL,NULL,_binary '',NULL,_binary '\0','8d500cba-ea97-4fd0-b969-91f305c70891',NULL,0,_binary '\0',_binary '\0','master Realm',_binary '\0','client-secret',NULL,NULL,NULL,_binary '',_binary '\0',_binary '\0',_binary '\0'),('e2ec7820-a817-4d92-8155-f9bdb1d7d13d',_binary '',_binary '\0','admin-cli',0,_binary '',NULL,NULL,_binary '\0',NULL,_binary '\0','8d500cba-ea97-4fd0-b969-91f305c70891','openid-connect',0,_binary '\0',_binary '\0','${client_admin-cli}',_binary '\0','client-secret',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '',_binary '\0'),('eb2edf3d-204a-41e7-aff2-87136e3038bb',_binary '',_binary '\0','account',0,_binary '',NULL,'/realms/master/account/',_binary '\0',NULL,_binary '\0','8d500cba-ea97-4fd0-b969-91f305c70891','openid-connect',0,_binary '\0',_binary '\0','${client_account}',_binary '\0','client-secret','${authBaseUrl}',NULL,NULL,_binary '',_binary '\0',_binary '\0',_binary '\0');
/*!40000 ALTER TABLE `CLIENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_ATTRIBUTES`
--

DROP TABLE IF EXISTS `CLIENT_ATTRIBUTES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT_ATTRIBUTES` (
  `CLIENT_ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `VALUE` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  PRIMARY KEY (`CLIENT_ID`,`NAME`),
  KEY `IDX_CLIENT_ATT_BY_NAME_VALUE` (`NAME`),
  CONSTRAINT `FK3C47C64BEACCA966` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_ATTRIBUTES`
--

LOCK TABLES `CLIENT_ATTRIBUTES` WRITE;
/*!40000 ALTER TABLE `CLIENT_ATTRIBUTES` DISABLE KEYS */;
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('5983633b-180b-4071-bcc5-58248f3ea1b4','backchannel.logout.revoke.offline.tokens','false'),('5983633b-180b-4071-bcc5-58248f3ea1b4','backchannel.logout.session.required','true'),('5983633b-180b-4071-bcc5-58248f3ea1b4','client.secret.creation.time','1725359487'),('5983633b-180b-4071-bcc5-58248f3ea1b4','display.on.consent.screen','false'),('5983633b-180b-4071-bcc5-58248f3ea1b4','oauth2.device.authorization.grant.enabled','false'),('5983633b-180b-4071-bcc5-58248f3ea1b4','oidc.ciba.grant.enabled','false'),('5983633b-180b-4071-bcc5-58248f3ea1b4','post.logout.redirect.uris','http://localhost:5173/*'),('724f9f53-5886-4a8b-b797-591ad05a9caa','pkce.code.challenge.method','S256'),('724f9f53-5886-4a8b-b797-591ad05a9caa','post.logout.redirect.uris','+'),('7391f048-5a3e-480f-8cfc-5b8d90e27912','pkce.code.challenge.method','S256'),('7391f048-5a3e-480f-8cfc-5b8d90e27912','post.logout.redirect.uris','+'),('eb2edf3d-204a-41e7-aff2-87136e3038bb','post.logout.redirect.uris','+');
/*!40000 ALTER TABLE `CLIENT_ATTRIBUTES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_AUTH_FLOW_BINDINGS`
--

DROP TABLE IF EXISTS `CLIENT_AUTH_FLOW_BINDINGS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT_AUTH_FLOW_BINDINGS` (
  `CLIENT_ID` varchar(36) NOT NULL,
  `FLOW_ID` varchar(36) DEFAULT NULL,
  `BINDING_NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`CLIENT_ID`,`BINDING_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_AUTH_FLOW_BINDINGS`
--

LOCK TABLES `CLIENT_AUTH_FLOW_BINDINGS` WRITE;
/*!40000 ALTER TABLE `CLIENT_AUTH_FLOW_BINDINGS` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_AUTH_FLOW_BINDINGS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_INITIAL_ACCESS`
--

DROP TABLE IF EXISTS `CLIENT_INITIAL_ACCESS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT_INITIAL_ACCESS` (
  `ID` varchar(36) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `TIMESTAMP` int DEFAULT NULL,
  `EXPIRATION` int DEFAULT NULL,
  `COUNT` int DEFAULT NULL,
  `REMAINING_COUNT` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_CLIENT_INIT_ACC_REALM` (`REALM_ID`),
  CONSTRAINT `FK_CLIENT_INIT_ACC_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_INITIAL_ACCESS`
--

LOCK TABLES `CLIENT_INITIAL_ACCESS` WRITE;
/*!40000 ALTER TABLE `CLIENT_INITIAL_ACCESS` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_INITIAL_ACCESS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_NODE_REGISTRATIONS`
--

DROP TABLE IF EXISTS `CLIENT_NODE_REGISTRATIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT_NODE_REGISTRATIONS` (
  `CLIENT_ID` varchar(36) NOT NULL,
  `VALUE` int DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`CLIENT_ID`,`NAME`),
  CONSTRAINT `FK4129723BA992F594` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_NODE_REGISTRATIONS`
--

LOCK TABLES `CLIENT_NODE_REGISTRATIONS` WRITE;
/*!40000 ALTER TABLE `CLIENT_NODE_REGISTRATIONS` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_NODE_REGISTRATIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SCOPE`
--

DROP TABLE IF EXISTS `CLIENT_SCOPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT_SCOPE` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `PROTOCOL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_CLI_SCOPE` (`REALM_ID`,`NAME`),
  KEY `IDX_REALM_CLSCOPE` (`REALM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SCOPE`
--

LOCK TABLES `CLIENT_SCOPE` WRITE;
/*!40000 ALTER TABLE `CLIENT_SCOPE` DISABLE KEYS */;
INSERT INTO `CLIENT_SCOPE` VALUES ('472f5149-2847-4275-9e0f-959d65d9f8d2','roles','8d500cba-ea97-4fd0-b969-91f305c70891','OpenID Connect scope for add user roles to the access token','openid-connect'),('49ffa9a5-32c1-4cbf-ae0b-58c398ecb849','microprofile-jwt','8d500cba-ea97-4fd0-b969-91f305c70891','Microprofile - JWT built-in scope','openid-connect'),('4cafa216-6510-43c7-a354-3f8a5d1792ea','acr','8d500cba-ea97-4fd0-b969-91f305c70891','OpenID Connect scope for add acr (authentication context class reference) to the token','openid-connect'),('6204369f-87f8-4310-853a-5d3e9178ce95','PhucxfoodsUserExternalIDSubClaim','8d500cba-ea97-4fd0-b969-91f305c70891','Add UserId to sub claim','openid-connect'),('7612a0ea-048b-46b4-bc4f-86064f7e8160','web-origins','8d500cba-ea97-4fd0-b969-91f305c70891','OpenID Connect scope for add allowed web origins to the access token','openid-connect'),('ad6a9df2-8f36-43e3-a531-ea0a483c908c','phone','8d500cba-ea97-4fd0-b969-91f305c70891','OpenID Connect built-in scope: phone','openid-connect'),('b41a813d-464e-49c5-92ed-17d64f973fe7','role_list','8d500cba-ea97-4fd0-b969-91f305c70891','SAML role list','saml'),('b79a92a3-e78e-4e06-8a83-0756522c1db7','email','8d500cba-ea97-4fd0-b969-91f305c70891','OpenID Connect built-in scope: email','openid-connect'),('c7194588-f594-4e51-a7e6-96c6a90a8382','address','8d500cba-ea97-4fd0-b969-91f305c70891','OpenID Connect built-in scope: address','openid-connect'),('cf449fb3-60d7-4249-bb77-38a1d145f4d0','profile','8d500cba-ea97-4fd0-b969-91f305c70891','OpenID Connect built-in scope: profile','openid-connect'),('de5efa5e-bd6b-4b76-8f99-dd221f0b620f','offline_access','8d500cba-ea97-4fd0-b969-91f305c70891','OpenID Connect built-in scope: offline_access','openid-connect'),('e8b60846-b837-4823-906b-13b707fc2e6d','phucxFoodShopAudClaim','8d500cba-ea97-4fd0-b969-91f305c70891','add client id to foodshop aud claim token','openid-connect');
/*!40000 ALTER TABLE `CLIENT_SCOPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SCOPE_ATTRIBUTES`
--

DROP TABLE IF EXISTS `CLIENT_SCOPE_ATTRIBUTES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT_SCOPE_ATTRIBUTES` (
  `SCOPE_ID` varchar(36) NOT NULL,
  `VALUE` text,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`SCOPE_ID`,`NAME`),
  KEY `IDX_CLSCOPE_ATTRS` (`SCOPE_ID`),
  CONSTRAINT `FK_CL_SCOPE_ATTR_SCOPE` FOREIGN KEY (`SCOPE_ID`) REFERENCES `CLIENT_SCOPE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SCOPE_ATTRIBUTES`
--

LOCK TABLES `CLIENT_SCOPE_ATTRIBUTES` WRITE;
/*!40000 ALTER TABLE `CLIENT_SCOPE_ATTRIBUTES` DISABLE KEYS */;
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('472f5149-2847-4275-9e0f-959d65d9f8d2','${rolesScopeConsentText}','consent.screen.text'),('472f5149-2847-4275-9e0f-959d65d9f8d2','true','display.on.consent.screen'),('472f5149-2847-4275-9e0f-959d65d9f8d2','false','include.in.token.scope'),('49ffa9a5-32c1-4cbf-ae0b-58c398ecb849','false','display.on.consent.screen'),('49ffa9a5-32c1-4cbf-ae0b-58c398ecb849','true','include.in.token.scope'),('4cafa216-6510-43c7-a354-3f8a5d1792ea','false','display.on.consent.screen'),('4cafa216-6510-43c7-a354-3f8a5d1792ea','false','include.in.token.scope'),('6204369f-87f8-4310-853a-5d3e9178ce95','','consent.screen.text'),('6204369f-87f8-4310-853a-5d3e9178ce95','true','display.on.consent.screen'),('6204369f-87f8-4310-853a-5d3e9178ce95','','gui.order'),('6204369f-87f8-4310-853a-5d3e9178ce95','true','include.in.token.scope'),('7612a0ea-048b-46b4-bc4f-86064f7e8160','','consent.screen.text'),('7612a0ea-048b-46b4-bc4f-86064f7e8160','false','display.on.consent.screen'),('7612a0ea-048b-46b4-bc4f-86064f7e8160','false','include.in.token.scope'),('ad6a9df2-8f36-43e3-a531-ea0a483c908c','${phoneScopeConsentText}','consent.screen.text'),('ad6a9df2-8f36-43e3-a531-ea0a483c908c','true','display.on.consent.screen'),('ad6a9df2-8f36-43e3-a531-ea0a483c908c','true','include.in.token.scope'),('b41a813d-464e-49c5-92ed-17d64f973fe7','${samlRoleListScopeConsentText}','consent.screen.text'),('b41a813d-464e-49c5-92ed-17d64f973fe7','true','display.on.consent.screen'),('b79a92a3-e78e-4e06-8a83-0756522c1db7','${emailScopeConsentText}','consent.screen.text'),('b79a92a3-e78e-4e06-8a83-0756522c1db7','true','display.on.consent.screen'),('b79a92a3-e78e-4e06-8a83-0756522c1db7','true','include.in.token.scope'),('c7194588-f594-4e51-a7e6-96c6a90a8382','${addressScopeConsentText}','consent.screen.text'),('c7194588-f594-4e51-a7e6-96c6a90a8382','true','display.on.consent.screen'),('c7194588-f594-4e51-a7e6-96c6a90a8382','true','include.in.token.scope'),('cf449fb3-60d7-4249-bb77-38a1d145f4d0','${profileScopeConsentText}','consent.screen.text'),('cf449fb3-60d7-4249-bb77-38a1d145f4d0','true','display.on.consent.screen'),('cf449fb3-60d7-4249-bb77-38a1d145f4d0','true','include.in.token.scope'),('de5efa5e-bd6b-4b76-8f99-dd221f0b620f','${offlineAccessScopeConsentText}','consent.screen.text'),('de5efa5e-bd6b-4b76-8f99-dd221f0b620f','true','display.on.consent.screen'),('e8b60846-b837-4823-906b-13b707fc2e6d','','consent.screen.text'),('e8b60846-b837-4823-906b-13b707fc2e6d','true','display.on.consent.screen'),('e8b60846-b837-4823-906b-13b707fc2e6d','','gui.order'),('e8b60846-b837-4823-906b-13b707fc2e6d','true','include.in.token.scope');
/*!40000 ALTER TABLE `CLIENT_SCOPE_ATTRIBUTES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SCOPE_CLIENT`
--

DROP TABLE IF EXISTS `CLIENT_SCOPE_CLIENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT_SCOPE_CLIENT` (
  `CLIENT_ID` varchar(255) NOT NULL,
  `SCOPE_ID` varchar(255) NOT NULL,
  `DEFAULT_SCOPE` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`CLIENT_ID`,`SCOPE_ID`),
  KEY `IDX_CLSCOPE_CL` (`CLIENT_ID`),
  KEY `IDX_CL_CLSCOPE` (`SCOPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SCOPE_CLIENT`
--

LOCK TABLES `CLIENT_SCOPE_CLIENT` WRITE;
/*!40000 ALTER TABLE `CLIENT_SCOPE_CLIENT` DISABLE KEYS */;
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('5983633b-180b-4071-bcc5-58248f3ea1b4','472f5149-2847-4275-9e0f-959d65d9f8d2',_binary ''),('5983633b-180b-4071-bcc5-58248f3ea1b4','49ffa9a5-32c1-4cbf-ae0b-58c398ecb849',_binary '\0'),('5983633b-180b-4071-bcc5-58248f3ea1b4','4cafa216-6510-43c7-a354-3f8a5d1792ea',_binary ''),('5983633b-180b-4071-bcc5-58248f3ea1b4','6204369f-87f8-4310-853a-5d3e9178ce95',_binary ''),('5983633b-180b-4071-bcc5-58248f3ea1b4','7612a0ea-048b-46b4-bc4f-86064f7e8160',_binary ''),('5983633b-180b-4071-bcc5-58248f3ea1b4','ad6a9df2-8f36-43e3-a531-ea0a483c908c',_binary '\0'),('5983633b-180b-4071-bcc5-58248f3ea1b4','b79a92a3-e78e-4e06-8a83-0756522c1db7',_binary ''),('5983633b-180b-4071-bcc5-58248f3ea1b4','c7194588-f594-4e51-a7e6-96c6a90a8382',_binary '\0'),('5983633b-180b-4071-bcc5-58248f3ea1b4','cf449fb3-60d7-4249-bb77-38a1d145f4d0',_binary ''),('5983633b-180b-4071-bcc5-58248f3ea1b4','de5efa5e-bd6b-4b76-8f99-dd221f0b620f',_binary '\0'),('5983633b-180b-4071-bcc5-58248f3ea1b4','e8b60846-b837-4823-906b-13b707fc2e6d',_binary ''),('724f9f53-5886-4a8b-b797-591ad05a9caa','472f5149-2847-4275-9e0f-959d65d9f8d2',_binary ''),('724f9f53-5886-4a8b-b797-591ad05a9caa','49ffa9a5-32c1-4cbf-ae0b-58c398ecb849',_binary '\0'),('724f9f53-5886-4a8b-b797-591ad05a9caa','4cafa216-6510-43c7-a354-3f8a5d1792ea',_binary ''),('724f9f53-5886-4a8b-b797-591ad05a9caa','7612a0ea-048b-46b4-bc4f-86064f7e8160',_binary ''),('724f9f53-5886-4a8b-b797-591ad05a9caa','ad6a9df2-8f36-43e3-a531-ea0a483c908c',_binary '\0'),('724f9f53-5886-4a8b-b797-591ad05a9caa','b79a92a3-e78e-4e06-8a83-0756522c1db7',_binary ''),('724f9f53-5886-4a8b-b797-591ad05a9caa','c7194588-f594-4e51-a7e6-96c6a90a8382',_binary '\0'),('724f9f53-5886-4a8b-b797-591ad05a9caa','cf449fb3-60d7-4249-bb77-38a1d145f4d0',_binary ''),('724f9f53-5886-4a8b-b797-591ad05a9caa','de5efa5e-bd6b-4b76-8f99-dd221f0b620f',_binary '\0'),('7391f048-5a3e-480f-8cfc-5b8d90e27912','472f5149-2847-4275-9e0f-959d65d9f8d2',_binary ''),('7391f048-5a3e-480f-8cfc-5b8d90e27912','49ffa9a5-32c1-4cbf-ae0b-58c398ecb849',_binary '\0'),('7391f048-5a3e-480f-8cfc-5b8d90e27912','4cafa216-6510-43c7-a354-3f8a5d1792ea',_binary ''),('7391f048-5a3e-480f-8cfc-5b8d90e27912','7612a0ea-048b-46b4-bc4f-86064f7e8160',_binary ''),('7391f048-5a3e-480f-8cfc-5b8d90e27912','ad6a9df2-8f36-43e3-a531-ea0a483c908c',_binary '\0'),('7391f048-5a3e-480f-8cfc-5b8d90e27912','b79a92a3-e78e-4e06-8a83-0756522c1db7',_binary ''),('7391f048-5a3e-480f-8cfc-5b8d90e27912','c7194588-f594-4e51-a7e6-96c6a90a8382',_binary '\0'),('7391f048-5a3e-480f-8cfc-5b8d90e27912','cf449fb3-60d7-4249-bb77-38a1d145f4d0',_binary ''),('7391f048-5a3e-480f-8cfc-5b8d90e27912','de5efa5e-bd6b-4b76-8f99-dd221f0b620f',_binary '\0'),('74fd16f8-3604-42d8-bf5f-078e3c4406d6','472f5149-2847-4275-9e0f-959d65d9f8d2',_binary ''),('74fd16f8-3604-42d8-bf5f-078e3c4406d6','49ffa9a5-32c1-4cbf-ae0b-58c398ecb849',_binary '\0'),('74fd16f8-3604-42d8-bf5f-078e3c4406d6','4cafa216-6510-43c7-a354-3f8a5d1792ea',_binary ''),('74fd16f8-3604-42d8-bf5f-078e3c4406d6','7612a0ea-048b-46b4-bc4f-86064f7e8160',_binary ''),('74fd16f8-3604-42d8-bf5f-078e3c4406d6','ad6a9df2-8f36-43e3-a531-ea0a483c908c',_binary '\0'),('74fd16f8-3604-42d8-bf5f-078e3c4406d6','b79a92a3-e78e-4e06-8a83-0756522c1db7',_binary ''),('74fd16f8-3604-42d8-bf5f-078e3c4406d6','c7194588-f594-4e51-a7e6-96c6a90a8382',_binary '\0'),('74fd16f8-3604-42d8-bf5f-078e3c4406d6','cf449fb3-60d7-4249-bb77-38a1d145f4d0',_binary ''),('74fd16f8-3604-42d8-bf5f-078e3c4406d6','de5efa5e-bd6b-4b76-8f99-dd221f0b620f',_binary '\0'),('d372473f-7d72-44b8-8301-7369776129e3','472f5149-2847-4275-9e0f-959d65d9f8d2',_binary ''),('d372473f-7d72-44b8-8301-7369776129e3','49ffa9a5-32c1-4cbf-ae0b-58c398ecb849',_binary '\0'),('d372473f-7d72-44b8-8301-7369776129e3','4cafa216-6510-43c7-a354-3f8a5d1792ea',_binary ''),('d372473f-7d72-44b8-8301-7369776129e3','7612a0ea-048b-46b4-bc4f-86064f7e8160',_binary ''),('d372473f-7d72-44b8-8301-7369776129e3','ad6a9df2-8f36-43e3-a531-ea0a483c908c',_binary '\0'),('d372473f-7d72-44b8-8301-7369776129e3','b79a92a3-e78e-4e06-8a83-0756522c1db7',_binary ''),('d372473f-7d72-44b8-8301-7369776129e3','c7194588-f594-4e51-a7e6-96c6a90a8382',_binary '\0'),('d372473f-7d72-44b8-8301-7369776129e3','cf449fb3-60d7-4249-bb77-38a1d145f4d0',_binary ''),('d372473f-7d72-44b8-8301-7369776129e3','de5efa5e-bd6b-4b76-8f99-dd221f0b620f',_binary '\0'),('e2ec7820-a817-4d92-8155-f9bdb1d7d13d','472f5149-2847-4275-9e0f-959d65d9f8d2',_binary ''),('e2ec7820-a817-4d92-8155-f9bdb1d7d13d','49ffa9a5-32c1-4cbf-ae0b-58c398ecb849',_binary '\0'),('e2ec7820-a817-4d92-8155-f9bdb1d7d13d','4cafa216-6510-43c7-a354-3f8a5d1792ea',_binary ''),('e2ec7820-a817-4d92-8155-f9bdb1d7d13d','7612a0ea-048b-46b4-bc4f-86064f7e8160',_binary ''),('e2ec7820-a817-4d92-8155-f9bdb1d7d13d','ad6a9df2-8f36-43e3-a531-ea0a483c908c',_binary '\0'),('e2ec7820-a817-4d92-8155-f9bdb1d7d13d','b79a92a3-e78e-4e06-8a83-0756522c1db7',_binary ''),('e2ec7820-a817-4d92-8155-f9bdb1d7d13d','c7194588-f594-4e51-a7e6-96c6a90a8382',_binary '\0'),('e2ec7820-a817-4d92-8155-f9bdb1d7d13d','cf449fb3-60d7-4249-bb77-38a1d145f4d0',_binary ''),('e2ec7820-a817-4d92-8155-f9bdb1d7d13d','de5efa5e-bd6b-4b76-8f99-dd221f0b620f',_binary '\0'),('eb2edf3d-204a-41e7-aff2-87136e3038bb','472f5149-2847-4275-9e0f-959d65d9f8d2',_binary ''),('eb2edf3d-204a-41e7-aff2-87136e3038bb','49ffa9a5-32c1-4cbf-ae0b-58c398ecb849',_binary '\0'),('eb2edf3d-204a-41e7-aff2-87136e3038bb','4cafa216-6510-43c7-a354-3f8a5d1792ea',_binary ''),('eb2edf3d-204a-41e7-aff2-87136e3038bb','7612a0ea-048b-46b4-bc4f-86064f7e8160',_binary ''),('eb2edf3d-204a-41e7-aff2-87136e3038bb','ad6a9df2-8f36-43e3-a531-ea0a483c908c',_binary '\0'),('eb2edf3d-204a-41e7-aff2-87136e3038bb','b79a92a3-e78e-4e06-8a83-0756522c1db7',_binary ''),('eb2edf3d-204a-41e7-aff2-87136e3038bb','c7194588-f594-4e51-a7e6-96c6a90a8382',_binary '\0'),('eb2edf3d-204a-41e7-aff2-87136e3038bb','cf449fb3-60d7-4249-bb77-38a1d145f4d0',_binary ''),('eb2edf3d-204a-41e7-aff2-87136e3038bb','de5efa5e-bd6b-4b76-8f99-dd221f0b620f',_binary '\0');
/*!40000 ALTER TABLE `CLIENT_SCOPE_CLIENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SCOPE_ROLE_MAPPING`
--

DROP TABLE IF EXISTS `CLIENT_SCOPE_ROLE_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT_SCOPE_ROLE_MAPPING` (
  `SCOPE_ID` varchar(36) NOT NULL,
  `ROLE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`SCOPE_ID`,`ROLE_ID`),
  KEY `IDX_CLSCOPE_ROLE` (`SCOPE_ID`),
  KEY `IDX_ROLE_CLSCOPE` (`ROLE_ID`),
  CONSTRAINT `FK_CL_SCOPE_RM_SCOPE` FOREIGN KEY (`SCOPE_ID`) REFERENCES `CLIENT_SCOPE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SCOPE_ROLE_MAPPING`
--

LOCK TABLES `CLIENT_SCOPE_ROLE_MAPPING` WRITE;
/*!40000 ALTER TABLE `CLIENT_SCOPE_ROLE_MAPPING` DISABLE KEYS */;
INSERT INTO `CLIENT_SCOPE_ROLE_MAPPING` VALUES ('de5efa5e-bd6b-4b76-8f99-dd221f0b620f','c8c13f65-335e-42e4-8022-c48ed31b6e10');
/*!40000 ALTER TABLE `CLIENT_SCOPE_ROLE_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SESSION`
--

DROP TABLE IF EXISTS `CLIENT_SESSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT_SESSION` (
  `ID` varchar(36) NOT NULL,
  `CLIENT_ID` varchar(36) DEFAULT NULL,
  `REDIRECT_URI` varchar(255) DEFAULT NULL,
  `STATE` varchar(255) DEFAULT NULL,
  `TIMESTAMP` int DEFAULT NULL,
  `SESSION_ID` varchar(36) DEFAULT NULL,
  `AUTH_METHOD` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(255) DEFAULT NULL,
  `AUTH_USER_ID` varchar(36) DEFAULT NULL,
  `CURRENT_ACTION` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_CLIENT_SESSION_SESSION` (`SESSION_ID`),
  CONSTRAINT `FK_B4AO2VCVAT6UKAU74WBWTFQO1` FOREIGN KEY (`SESSION_ID`) REFERENCES `USER_SESSION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SESSION`
--

LOCK TABLES `CLIENT_SESSION` WRITE;
/*!40000 ALTER TABLE `CLIENT_SESSION` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_SESSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SESSION_AUTH_STATUS`
--

DROP TABLE IF EXISTS `CLIENT_SESSION_AUTH_STATUS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT_SESSION_AUTH_STATUS` (
  `AUTHENTICATOR` varchar(36) NOT NULL,
  `STATUS` int DEFAULT NULL,
  `CLIENT_SESSION` varchar(36) NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`,`AUTHENTICATOR`),
  CONSTRAINT `AUTH_STATUS_CONSTRAINT` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SESSION_AUTH_STATUS`
--

LOCK TABLES `CLIENT_SESSION_AUTH_STATUS` WRITE;
/*!40000 ALTER TABLE `CLIENT_SESSION_AUTH_STATUS` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_SESSION_AUTH_STATUS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SESSION_NOTE`
--

DROP TABLE IF EXISTS `CLIENT_SESSION_NOTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT_SESSION_NOTE` (
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `CLIENT_SESSION` varchar(36) NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`,`NAME`),
  CONSTRAINT `FK5EDFB00FF51C2736` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SESSION_NOTE`
--

LOCK TABLES `CLIENT_SESSION_NOTE` WRITE;
/*!40000 ALTER TABLE `CLIENT_SESSION_NOTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_SESSION_NOTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SESSION_PROT_MAPPER`
--

DROP TABLE IF EXISTS `CLIENT_SESSION_PROT_MAPPER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT_SESSION_PROT_MAPPER` (
  `PROTOCOL_MAPPER_ID` varchar(36) NOT NULL,
  `CLIENT_SESSION` varchar(36) NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`,`PROTOCOL_MAPPER_ID`),
  CONSTRAINT `FK_33A8SGQW18I532811V7O2DK89` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SESSION_PROT_MAPPER`
--

LOCK TABLES `CLIENT_SESSION_PROT_MAPPER` WRITE;
/*!40000 ALTER TABLE `CLIENT_SESSION_PROT_MAPPER` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_SESSION_PROT_MAPPER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SESSION_ROLE`
--

DROP TABLE IF EXISTS `CLIENT_SESSION_ROLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT_SESSION_ROLE` (
  `ROLE_ID` varchar(255) NOT NULL,
  `CLIENT_SESSION` varchar(36) NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`,`ROLE_ID`),
  CONSTRAINT `FK_11B7SGQW18I532811V7O2DV76` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SESSION_ROLE`
--

LOCK TABLES `CLIENT_SESSION_ROLE` WRITE;
/*!40000 ALTER TABLE `CLIENT_SESSION_ROLE` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_SESSION_ROLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_USER_SESSION_NOTE`
--

DROP TABLE IF EXISTS `CLIENT_USER_SESSION_NOTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT_USER_SESSION_NOTE` (
  `NAME` varchar(255) NOT NULL,
  `VALUE` text,
  `CLIENT_SESSION` varchar(36) NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`,`NAME`),
  CONSTRAINT `FK_CL_USR_SES_NOTE` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_USER_SESSION_NOTE`
--

LOCK TABLES `CLIENT_USER_SESSION_NOTE` WRITE;
/*!40000 ALTER TABLE `CLIENT_USER_SESSION_NOTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_USER_SESSION_NOTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COMPONENT`
--

DROP TABLE IF EXISTS `COMPONENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `COMPONENT` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `PARENT_ID` varchar(36) DEFAULT NULL,
  `PROVIDER_ID` varchar(36) DEFAULT NULL,
  `PROVIDER_TYPE` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  `SUB_TYPE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_COMPONENT_REALM` (`REALM_ID`),
  KEY `IDX_COMPONENT_PROVIDER_TYPE` (`PROVIDER_TYPE`),
  CONSTRAINT `FK_COMPONENT_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COMPONENT`
--

LOCK TABLES `COMPONENT` WRITE;
/*!40000 ALTER TABLE `COMPONENT` DISABLE KEYS */;
INSERT INTO `COMPONENT` VALUES ('005e9a92-5989-481c-aa54-c8e3a9aeb607','Allowed Protocol Mapper Types','8d500cba-ea97-4fd0-b969-91f305c70891','allowed-protocol-mappers','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','8d500cba-ea97-4fd0-b969-91f305c70891','authenticated'),('186e2632-4ce9-4742-bcd2-71f85c3ded78','Allowed Client Scopes','8d500cba-ea97-4fd0-b969-91f305c70891','allowed-client-templates','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','8d500cba-ea97-4fd0-b969-91f305c70891','authenticated'),('1ca65256-907a-4079-97e9-66fa78c2dec3','Allowed Client Scopes','8d500cba-ea97-4fd0-b969-91f305c70891','allowed-client-templates','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','8d500cba-ea97-4fd0-b969-91f305c70891','anonymous'),('45ade63e-0f65-4761-bbc5-036888b899c1','Trusted Hosts','8d500cba-ea97-4fd0-b969-91f305c70891','trusted-hosts','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','8d500cba-ea97-4fd0-b969-91f305c70891','anonymous'),('482e33fd-e76c-4d30-a6e3-64e67c50d860','aes-generated','8d500cba-ea97-4fd0-b969-91f305c70891','aes-generated','org.keycloak.keys.KeyProvider','8d500cba-ea97-4fd0-b969-91f305c70891',NULL),('57660a85-a4c2-4b15-a619-dcd55ace62cd','rsa-enc-generated','8d500cba-ea97-4fd0-b969-91f305c70891','rsa-enc-generated','org.keycloak.keys.KeyProvider','8d500cba-ea97-4fd0-b969-91f305c70891',NULL),('59dc5732-b2cb-4918-942a-8bf77858894b','rsa-generated','8d500cba-ea97-4fd0-b969-91f305c70891','rsa-generated','org.keycloak.keys.KeyProvider','8d500cba-ea97-4fd0-b969-91f305c70891',NULL),('67f889c0-6cd9-4de0-bc48-f820a0642830','Consent Required','8d500cba-ea97-4fd0-b969-91f305c70891','consent-required','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','8d500cba-ea97-4fd0-b969-91f305c70891','anonymous'),('970b3d3c-cbf0-4b07-8940-4094eed14b03','Foodshop','8d500cba-ea97-4fd0-b969-91f305c70891','custom-user-provider','org.keycloak.storage.UserStorageProvider','8d500cba-ea97-4fd0-b969-91f305c70891',NULL),('9bc228b6-fdeb-4a54-9883-b958cc865e1d','hmac-generated','8d500cba-ea97-4fd0-b969-91f305c70891','hmac-generated','org.keycloak.keys.KeyProvider','8d500cba-ea97-4fd0-b969-91f305c70891',NULL),('dbc2aed6-9a10-4270-aebe-e64b36d63bdc','Max Clients Limit','8d500cba-ea97-4fd0-b969-91f305c70891','max-clients','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','8d500cba-ea97-4fd0-b969-91f305c70891','anonymous'),('dd2eae44-fc46-4533-a61c-68569d90b542','Full Scope Disabled','8d500cba-ea97-4fd0-b969-91f305c70891','scope','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','8d500cba-ea97-4fd0-b969-91f305c70891','anonymous'),('fee3b6f0-179d-4908-a926-ff0c94022211','Allowed Protocol Mapper Types','8d500cba-ea97-4fd0-b969-91f305c70891','allowed-protocol-mappers','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','8d500cba-ea97-4fd0-b969-91f305c70891','anonymous');
/*!40000 ALTER TABLE `COMPONENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COMPONENT_CONFIG`
--

DROP TABLE IF EXISTS `COMPONENT_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `COMPONENT_CONFIG` (
  `ID` varchar(36) NOT NULL,
  `COMPONENT_ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `VALUE` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  PRIMARY KEY (`ID`),
  KEY `IDX_COMPO_CONFIG_COMPO` (`COMPONENT_ID`),
  CONSTRAINT `FK_COMPONENT_CONFIG` FOREIGN KEY (`COMPONENT_ID`) REFERENCES `COMPONENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COMPONENT_CONFIG`
--

LOCK TABLES `COMPONENT_CONFIG` WRITE;
/*!40000 ALTER TABLE `COMPONENT_CONFIG` DISABLE KEYS */;
INSERT INTO `COMPONENT_CONFIG` VALUES ('1626a5c0-b327-4cc6-884f-7bc80bef8b4b','482e33fd-e76c-4d30-a6e3-64e67c50d860','secret','uDS6zIZtr02XzE6onpP_cQ'),('1930c746-e880-4af1-abac-5fdeac488410','9bc228b6-fdeb-4a54-9883-b958cc865e1d','kid','77e570d5-e37c-4aeb-aabb-fa6c03571faf'),('340b7432-fb95-4b47-b566-73978e08476a','fee3b6f0-179d-4908-a926-ff0c94022211','allowed-protocol-mapper-types','oidc-usermodel-property-mapper'),('3946e942-258f-4322-8d46-2ff2bbd8920d','970b3d3c-cbf0-4b07-8940-4094eed14b03','cachePolicy','DEFAULT'),('4240efb8-561d-42ec-a363-8d821abb20df','970b3d3c-cbf0-4b07-8940-4094eed14b03','jdbcUrl','jdbc:mysql://mysql:3306/user'),('4935986b-8fe4-48e2-b755-63eeadcc2e0d','005e9a92-5989-481c-aa54-c8e3a9aeb607','allowed-protocol-mapper-types','oidc-usermodel-property-mapper'),('49aa444e-1d2b-41a2-bcfc-b76047a2734e','57660a85-a4c2-4b15-a619-dcd55ace62cd','algorithm','RSA-OAEP'),('4f464d59-33bd-4c24-83be-bd6c7cce3571','fee3b6f0-179d-4908-a926-ff0c94022211','allowed-protocol-mapper-types','oidc-address-mapper'),('4f82d56d-4da9-418f-8276-d5119024d8e6','fee3b6f0-179d-4908-a926-ff0c94022211','allowed-protocol-mapper-types','saml-role-list-mapper'),('500ec066-38a2-4a0f-b9b7-0316f9469aa0','fee3b6f0-179d-4908-a926-ff0c94022211','allowed-protocol-mapper-types','saml-user-attribute-mapper'),('504ef635-6f25-4875-9ceb-5c6dec2f9c1a','005e9a92-5989-481c-aa54-c8e3a9aeb607','allowed-protocol-mapper-types','saml-user-property-mapper'),('52442236-11e4-42ae-9ffa-398b703e1a3e','005e9a92-5989-481c-aa54-c8e3a9aeb607','allowed-protocol-mapper-types','oidc-usermodel-attribute-mapper'),('5b515b94-6cb9-40b0-a1c6-3cc0a94ce6b3','9bc228b6-fdeb-4a54-9883-b958cc865e1d','algorithm','HS256'),('5bffd35e-ac44-4391-b1ec-92a79f11b813','9bc228b6-fdeb-4a54-9883-b958cc865e1d','priority','100'),('671fd0ec-8aaa-42dc-b8a1-5c616a627529','186e2632-4ce9-4742-bcd2-71f85c3ded78','allow-default-scopes','true'),('68cd0a50-b2d8-46bd-ae87-0bc7d6724579','fee3b6f0-179d-4908-a926-ff0c94022211','allowed-protocol-mapper-types','saml-user-property-mapper'),('6d41c3d7-f452-4f45-8eed-09c4441dd52a','970b3d3c-cbf0-4b07-8940-4094eed14b03','validationQuery','select 1'),('788b3d9b-b473-49d5-aaff-d6c8cb6e4ef3','dbc2aed6-9a10-4270-aebe-e64b36d63bdc','max-clients','200'),('7bf5e9ec-0601-48ef-aa0f-9a2fb0bd3cb3','005e9a92-5989-481c-aa54-c8e3a9aeb607','allowed-protocol-mapper-types','oidc-sha256-pairwise-sub-mapper'),('82dd8f57-c695-4668-8484-0b49289a88ac','482e33fd-e76c-4d30-a6e3-64e67c50d860','kid','15fc02c6-d5e9-4cc8-a814-9f5804d16c59'),('8949fe60-be67-4816-b61c-85bfae27b52c','9bc228b6-fdeb-4a54-9883-b958cc865e1d','secret','mKnDNwApgqynPrh2dRL9N2FdZLvPw0nT3PliFMQe22wfoaNNcMb5_z8zoSieS-oGAccNVPoug5FPTpyoqgeLtg'),('8d4a6d8f-2eae-4456-bb39-522bdeed9088','005e9a92-5989-481c-aa54-c8e3a9aeb607','allowed-protocol-mapper-types','saml-user-attribute-mapper'),('92da2890-6be1-4ac1-89ff-422850f68016','59dc5732-b2cb-4918-942a-8bf77858894b','certificate','MIICmzCCAYMCBgGRrJHpYjANBgkqhkiG9w0BAQsFADARMQ8wDQYDVQQDDAZtYXN0ZXIwHhcNMjQwOTAxMDc1MDI5WhcNMzQwOTAxMDc1MjA5WjARMQ8wDQYDVQQDDAZtYXN0ZXIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDT+lHd5csNzKCQzk9FUD9MbZuew9JKVGXqz5d/39j2MPvaZ+2QzF3ranhOdO15oB7QAe3Mb+haP1okx7lydCbY6ehErNfy2k53GemzWlBXnpAV4TM+9q6xHN3rDzTf04qIydlBi9mBBlni5mUCzCRIn9+D1NMOHIGco+24IDW4OnzaMHi9apwuukdBrzvEkUIiPUirj4oa6RHJeQTXnRkVN9FIBzyBQjhGk0DjOv03XJIcQzVM6s9ZWYYadenfWDTQcungMps5dgprDHl5tES1dtypvnw35rp66mbxUtp3R586L3VdJIP2V5BDl/D/Qs75rKYgk1ZCKYLQ0yF9IWitAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAGsrstJJW3yTxUZnkSs43VVSm7/AgsEhoSdMJFk297hPlAOBaQZEIsuuKb2J2yHMkrQFgX74kyv4p2ZBcfjfVQMQGCaQTENoqj8nbj8d/bCv2entM52rZI2OGb6C/Ld555+68J0V2IJ89IOEmv6FJJK5d9Cnu6QWIlihZmg36/dppUu/WTUZxp7Mhx69ZK/hITFLotre+sirecPqkz2hPMQMflXZ9uJAfhFy5Np6FFUQwL/yunW1O42/KPFAmcMNjg/rlBUMjDopyZSs9U0MT+uiAbaZnJrj823JRVVHiXotPxpXFhwfkZTbRwDLGjfnbefPaPat0emSfNEi5CAjkyY='),('9b564bf7-ad49-4fdf-b0ee-5c5b0332d4b3','59dc5732-b2cb-4918-942a-8bf77858894b','priority','100'),('a0cbc2e9-0aa6-460b-85f8-60484fcf45ef','482e33fd-e76c-4d30-a6e3-64e67c50d860','priority','100'),('a53f8625-0968-47e5-b83e-ac648b651ac7','fee3b6f0-179d-4908-a926-ff0c94022211','allowed-protocol-mapper-types','oidc-usermodel-attribute-mapper'),('a986085e-f946-445f-a3c2-f2a9aef7e14c','005e9a92-5989-481c-aa54-c8e3a9aeb607','allowed-protocol-mapper-types','oidc-address-mapper'),('aa907b8e-aa5b-4312-8aaf-e7a1ab97b740','45ade63e-0f65-4761-bbc5-036888b899c1','client-uris-must-match','true'),('b916530c-3a43-4cda-af61-8eef0e55b605','59dc5732-b2cb-4918-942a-8bf77858894b','privateKey','MIIEpQIBAAKCAQEA0/pR3eXLDcygkM5PRVA/TG2bnsPSSlRl6s+Xf9/Y9jD72mftkMxd62p4TnTteaAe0AHtzG/oWj9aJMe5cnQm2OnoRKzX8tpOdxnps1pQV56QFeEzPvausRzd6w8039OKiMnZQYvZgQZZ4uZlAswkSJ/fg9TTDhyBnKPtuCA1uDp82jB4vWqcLrpHQa87xJFCIj1Iq4+KGukRyXkE150ZFTfRSAc8gUI4RpNA4zr9N1ySHEM1TOrPWVmGGnXp31g00HLp4DKbOXYKawx5ebREtXbcqb58N+a6eupm8VLad0efOi91XSSD9leQQ5fw/0LO+aymIJNWQimC0NMhfSForQIDAQABAoIBAAfnR+EhnboEfKIZhxwa+1kypwA65d7OmcQw/EGQy4qWVfWVNWbWL9REsnqeYnJL9Yok4ERkkcG3pflEFxZ83SvuQLI6zYxo9k77gNqmjeNObqnaLSp2URoabO3EMeD8hqhF+ls2xyPH5FcQ5wg9K0cn6tID/+CBUJXSCN16Gfal6jqG5JHc3rGzQxfDtzuq32Tl35Eqa/rljWzdB8oDEDR7I5aa8JdfQtpAVnOt0R+eStm6KPvsXKBVG2TP+OD3ESd/XYUJ7hjpX/wx2BXMQhoU5Lfaw6kNJL2duw9LhPeh31VpOQHMC8EB5wnTdO9xURRLJQ3fsRhrjpe08EBnCRECgYEA8grZ7C/kbLtCd81fTY8Y/y1djhS5MwM1XaWc55ABSrx+p0B9y6QjcOfPFBijqErK0BDNvQotPPiHfnZnG//wcvmC/Cbie38Fw15fn1f7V5tqJPCtlyejJaGiEw3/EqZXTftZmk3OpZY4TIF61XksZze3zYN/8FvoRwNGhVzjOZkCgYEA4DOj9ldq/f5KNKg+pOI4gQDKGRXr9aANxSx0ln3XZWrCezLoZGqFD5syhsj90LbHqg0Hfm7qkHjXFl6BjqbjhxxKmnGNZhiuGfCQHcv8gwqLZyFqoHXAiez7W3sgoFrvRfQGef2SXd9LNb9xUDuE1AYIMRaHONZrPn/O7qWv3DUCgYEAktLoTPDkZNzXPiszs62pYSnYxZZmnb5FghAzdPiTAA4F2lkxtfVUlmnX4P3tUOpFyx9WXmUs+f5c2ubiN0bzSht3TJQd8QIlsdSuts89JXCeW9gvxTCDNBGvalWkNGD5NtHREdw/DY1pi2dD5vkBaX+QHLe3RUvLRECFZ+Ln54kCgYEAiHsHKdOnXvzogUuV7tuDWAx5+nFc1YZcO5ZHVsE+WQ93WnXwAAjT/V8Vj/VUhTRvMTaDRFUS12WoUYawSX2QblQrmf7b+7W0bjgCExF1IvRNTjeKaCmmTpADp3m5r8QLekMFmkdhTWG7r/ldamEBB+ywGenD4TatzDsTZjVYa60CgYEAn0ErFqJwZURz2HyS5BAY5BktwrSW7D1bt8YN7itslfGw1TL0TOdzw/E6qqb220h0EHB/c5JMMv4P7Cb66GZib8fQBapXaDtH14tIPz9pJus+Yw8vz7YXZNbPgystxH2E28nKzH9iEg5pIFwq75UP1ch3mTBPOAUpQ6bCLCsxObk='),('bbc6b24b-bcae-474a-8c5f-220c37ed78eb','005e9a92-5989-481c-aa54-c8e3a9aeb607','allowed-protocol-mapper-types','saml-role-list-mapper'),('bee22728-22d6-46b8-a832-223e01e5a15e','fee3b6f0-179d-4908-a926-ff0c94022211','allowed-protocol-mapper-types','oidc-full-name-mapper'),('c366e123-78ca-46e0-8cb4-9ed60b721561','57660a85-a4c2-4b15-a619-dcd55ace62cd','certificate','MIICmzCCAYMCBgGRrJHqJjANBgkqhkiG9w0BAQsFADARMQ8wDQYDVQQDDAZtYXN0ZXIwHhcNMjQwOTAxMDc1MDI5WhcNMzQwOTAxMDc1MjA5WjARMQ8wDQYDVQQDDAZtYXN0ZXIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDPjLkUOjWudDt1B4PAtxmAAcQR/W/EK8RkJ9mFGedmiqlHWa49gZztTnQrzF/oYwMjqlSoy4zBj20KrYiEprt/37MxlLVKSbd+IXVkOGtIFDcCwOtm+1ugbibwV5a002YDU7xIGjz10+bEO0zEGSHRiUJMEv6ZPK6AB4X1lCPk85gKIzBzRtC+lLdRzg2gKrpUuT2fYJm3JE/ezlSJBVY4EFgEltPZrCnLjzUKVO8ktbQQYeEzAUn/6Iy4+KZgNIhW3F36IgVbW7KxdxWkr23Is8gzEgvATPSTNvK1tGaAVgOKYU6Dy3O7acDJeaAQ+q7P9kOyL2O51d1Bl1IlSqwdAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAJkE3NN8nU+xua7o0eiIX12EjEu7mGWsTKHNZempgnyHAs8u4QgyCvkYnCSgbo7kpqryWrYT+8vHTgreTcuPJsMNvVXrOrQ4GztRE0dotqrBy88meDLRVEFYXu47QqhhuaplB1jF+YeN+Qtebioc5gCloNujURuGfTs/uv12gG0KUcPHmea5t/JZb2wuJFzi1Lw/XvO7noarxUanPRoZVzKhZBGVO48delc7kUX8PqYv8TtzBZI7Ka1f2tMTlUmFDtgZg7h7EOOwpU1636dNH7Tp5lXCo56KLKJ2055IbjmTl+RFiTIW51cJJLctMdG99LcKx5gZDJY81rTrrdDs/bc='),('c41c713f-bd7d-4484-b6e3-728847325d09','970b3d3c-cbf0-4b07-8940-4094eed14b03','DbUsername','root'),('c443eea2-a246-4beb-aeff-27e1ade4e78a','970b3d3c-cbf0-4b07-8940-4094eed14b03','DbPassword','10122003simp!simp'),('cc386cc6-d551-4e2a-a1cd-94989bec1775','970b3d3c-cbf0-4b07-8940-4094eed14b03','enabled','true'),('cd99e07c-45cd-462a-ac37-2a08f23aa46d','005e9a92-5989-481c-aa54-c8e3a9aeb607','allowed-protocol-mapper-types','oidc-full-name-mapper'),('d856c7c9-8e85-43f7-8267-313cb66baa19','45ade63e-0f65-4761-bbc5-036888b899c1','host-sending-registration-request-must-match','true'),('db3a7d56-41a5-46d4-86af-72807ee978dd','57660a85-a4c2-4b15-a619-dcd55ace62cd','priority','100'),('db71ce14-fe16-4da4-a048-2156ad158752','fee3b6f0-179d-4908-a926-ff0c94022211','allowed-protocol-mapper-types','oidc-sha256-pairwise-sub-mapper'),('e019e9e9-fee9-40d3-88fd-5c8f11ef316a','57660a85-a4c2-4b15-a619-dcd55ace62cd','privateKey','MIIEogIBAAKCAQEAz4y5FDo1rnQ7dQeDwLcZgAHEEf1vxCvEZCfZhRnnZoqpR1muPYGc7U50K8xf6GMDI6pUqMuMwY9tCq2IhKa7f9+zMZS1Skm3fiF1ZDhrSBQ3AsDrZvtboG4m8FeWtNNmA1O8SBo89dPmxDtMxBkh0YlCTBL+mTyugAeF9ZQj5POYCiMwc0bQvpS3Uc4NoCq6VLk9n2CZtyRP3s5UiQVWOBBYBJbT2awpy481ClTvJLW0EGHhMwFJ/+iMuPimYDSIVtxd+iIFW1uysXcVpK9tyLPIMxILwEz0kzbytbRmgFYDimFOg8tzu2nAyXmgEPquz/ZDsi9judXdQZdSJUqsHQIDAQABAoIBAAwqnePVaLWuCxk4ZE0M/rHuCJIcZuubPDM50cq3YsT0TmmIFIp2+V3A2lcB70JWYYX682UEr57eG0CDvSu3gWjE0oKOytTMvvSmHfVOyF0skC/5YqNCC1Q7eM1geQ1nEgUSaMhaP2YSsqWgq0aYiY64qtn+SA3wDHOlXUEw2qzOTr2c/OhEabtbPDQbNIFt265Z106Ungn0T0PUfZSfOJhazmyS+4FD+Y6k0429m9FilOSTh6Dp8km5JvHctS9TYL2cqVtJSiTj1pabnwe72xUPQOSsw7UxU78C+bh59Jz33lTe4/aIIIj4Ji1kqnGw4v4fsyUWLBBmHkNYrWubGxkCgYEA6a/rzk1DfoGTVDLxaPY8ocXQxSNdEPxXu4Nq5eyu+up9zK3aRiofzLrPQvn3V83hqDYEJNVwgiMnE1xfyGc0T7LBxpdt9rLuPJa721QiEP8OTaTfWGrsC6Qc/kTX76m77PEmphP04uQS2tVd/CkBOzyJ582e46RWeokWYAkWfckCgYEA413qhF2qj88XTnlPYishWTtC6zzDrAJCj6Zal5FfScGGsqD+BLLDzNLAodAM/ZwR8xZLQ47BOn+vfNvyEsNWej7BrxhLT0Q5dAJLkcjSV+D/KF1GyFOpDDqJGvn8qQU/oE2djqujYnj4XWm2/zldgqKNtYyu8W12R2s56QugVbUCgYAx2yR16SoDLWXfStcSS6Z35Ro7VOYgH5YgHVOLJknWonygVF8DJS2PojNh2K6afwQrvQp5SUgtm45UpRqSSOJyNnL9CYWeYmJNbuM5UePKuH+2tPwpgvZFLYg5/zoKma3MSU6zlAJk/b/ADFF24NCwcKQDXm0NqBwDHIg3iPjRcQKBgHEb+J4O5toSARs1+Ery/+0fMSRLn2BD448JCApc9JWBpBvbQWNe908/DTuyf7Pu33MaBSk/rFsIyGmKFbxF1xCLhdkwcc6mNxrCRYvp5tJ3CRiwhpUEIZYGzMxRzuLazJNX6WjfdDF50rzN6Y7AoAdsDKljqmhKt0vCSRXphcL9AoGABsrgolpYw+FXMKNI3qoTudl/aSNABArkoFQiqlUMVpTpEuV7QiMW9OFBAfDVqPNXP6LCsfuiEwxEDkugB7eKPj33z0AL8xiUq0q+qfrqEblUJl7YICB2e3M5N+2ilQgJJn9iPbR9zGbSDovyzTWM1B+cp6GyMdU0PzJwW+rQAFo='),('ed7b6f91-2337-4a58-ae03-bc742fcc8f61','1ca65256-907a-4079-97e9-66fa78c2dec3','allow-default-scopes','true'),('f083d75f-9339-4f2f-bf5c-25d2a6fac6db','57660a85-a4c2-4b15-a619-dcd55ace62cd','keyUse','ENC'),('f6cfc037-db2c-4f80-bf73-c22a51c56bca','59dc5732-b2cb-4918-942a-8bf77858894b','keyUse','SIG');
/*!40000 ALTER TABLE `COMPONENT_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COMPOSITE_ROLE`
--

DROP TABLE IF EXISTS `COMPOSITE_ROLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `COMPOSITE_ROLE` (
  `COMPOSITE` varchar(36) NOT NULL,
  `CHILD_ROLE` varchar(36) NOT NULL,
  PRIMARY KEY (`COMPOSITE`,`CHILD_ROLE`),
  KEY `IDX_COMPOSITE` (`COMPOSITE`),
  KEY `IDX_COMPOSITE_CHILD` (`CHILD_ROLE`),
  CONSTRAINT `FK_A63WVEKFTU8JO1PNJ81E7MCE2` FOREIGN KEY (`COMPOSITE`) REFERENCES `KEYCLOAK_ROLE` (`ID`),
  CONSTRAINT `FK_GR7THLLB9LU8Q4VQA4524JJY8` FOREIGN KEY (`CHILD_ROLE`) REFERENCES `KEYCLOAK_ROLE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COMPOSITE_ROLE`
--

LOCK TABLES `COMPOSITE_ROLE` WRITE;
/*!40000 ALTER TABLE `COMPOSITE_ROLE` DISABLE KEYS */;
INSERT INTO `COMPOSITE_ROLE` VALUES ('3334862e-1eee-49ec-a515-7d4b38d20da3','c99e8cf9-b90d-4e33-806a-ee1afac07845'),('53d6b454-e873-4aea-81e7-694212aa1f91','2a052686-e768-4ebc-85e3-070d543a16ca'),('53d6b454-e873-4aea-81e7-694212aa1f91','3334862e-1eee-49ec-a515-7d4b38d20da3'),('53d6b454-e873-4aea-81e7-694212aa1f91','8b043554-97bd-49b0-a634-ff084beb64d4'),('53d6b454-e873-4aea-81e7-694212aa1f91','c8c13f65-335e-42e4-8022-c48ed31b6e10'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','1967cce1-ee2c-4d35-be83-7322eb97f0f6'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','2e30030f-3a4d-4dcf-ae6c-ced8d873fc69'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','31efdf09-0385-4af7-a701-a60afaaaa7a7'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','3b9c83ef-7034-4522-9c13-96795fef9a79'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','412a0660-d29e-4316-ab35-c5321e466761'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','515278f9-0215-49d0-b922-701c623574ac'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','667bce8e-6351-4c58-86b3-a623425ebec9'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','6c15c69b-3e04-49de-8b71-a7b42f8ade9b'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','8e4709f6-d7c2-44f5-8614-990077e629c1'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','92c91c27-395f-42d2-a332-a94b7c5c4642'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','9772e785-e437-47ab-bd65-3deabc4a8e10'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','9ea2be33-5d1f-4d41-8e42-c79a33efb31c'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','a528c7bf-724f-4726-87ee-be1c7ec7d9e3'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','b24d7dcc-4bc8-406d-91fa-5b8b0d79c29f'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','e563cd41-b108-4b4b-8c22-317d7ffb823c'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','e69b2ebb-c8e3-4c09-814a-0eca30573455'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','ea2ff855-c87d-40a9-8bb9-9973ff77f87f'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','ed287a36-9002-48c9-bceb-b9dc1c1eab3e'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','fda8c8b6-9e45-4c6d-9290-bd5132b20ec7'),('a528c7bf-724f-4726-87ee-be1c7ec7d9e3','667bce8e-6351-4c58-86b3-a623425ebec9'),('a528c7bf-724f-4726-87ee-be1c7ec7d9e3','ed287a36-9002-48c9-bceb-b9dc1c1eab3e'),('b24d7dcc-4bc8-406d-91fa-5b8b0d79c29f','1967cce1-ee2c-4d35-be83-7322eb97f0f6'),('e94c4f60-ed37-491a-984b-532bb0a11f6b','5f771b98-94a8-4e2a-9ec7-0bf047c4d11c');
/*!40000 ALTER TABLE `COMPOSITE_ROLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CREDENTIAL`
--

DROP TABLE IF EXISTS `CREDENTIAL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CREDENTIAL` (
  `ID` varchar(36) NOT NULL,
  `SALT` tinyblob,
  `TYPE` varchar(255) DEFAULT NULL,
  `USER_ID` varchar(36) DEFAULT NULL,
  `CREATED_DATE` bigint DEFAULT NULL,
  `USER_LABEL` varchar(255) DEFAULT NULL,
  `SECRET_DATA` longtext,
  `CREDENTIAL_DATA` longtext,
  `PRIORITY` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_USER_CREDENTIAL` (`USER_ID`),
  CONSTRAINT `FK_PFYR0GLASQYL0DEI3KL69R6V0` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CREDENTIAL`
--

LOCK TABLES `CREDENTIAL` WRITE;
/*!40000 ALTER TABLE `CREDENTIAL` DISABLE KEYS */;
INSERT INTO `CREDENTIAL` VALUES ('b38f5218-1028-4c9c-ae72-0b4517c91d5c',NULL,'password','a659ef18-6b57-41fd-8a5a-f69d912943de',1725177130072,NULL,'{\"value\":\"$2a$10$//oJqZ36dkn1J5iQX9ek1e7ckWP0B01LDRb3sIv6Nht6ifewIxyo6\",\"salt\":\"\",\"additionalParameters\":{}}','{\"hashIterations\":-1,\"algorithm\":\"bcrypt\",\"additionalParameters\":{}}',10);
/*!40000 ALTER TABLE `CREDENTIAL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATABASECHANGELOG`
--

DROP TABLE IF EXISTS `DATABASECHANGELOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DATABASECHANGELOG` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOG`
--

LOCK TABLES `DATABASECHANGELOG` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOG` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOG` VALUES ('1.0.0.Final-KEYCLOAK-5461','sthorger@redhat.com','META-INF/jpa-changelog-1.0.0.Final.xml','2024-09-01 07:51:52',1,'EXECUTED','9:6f1016664e21e16d26517a4418f5e3df','createTable tableName=APPLICATION_DEFAULT_ROLES; createTable tableName=CLIENT; createTable tableName=CLIENT_SESSION; createTable tableName=CLIENT_SESSION_ROLE; createTable tableName=COMPOSITE_ROLE; createTable tableName=CREDENTIAL; createTable tab...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.0.0.Final-KEYCLOAK-5461','sthorger@redhat.com','META-INF/db2-jpa-changelog-1.0.0.Final.xml','2024-09-01 07:51:52',2,'MARK_RAN','9:828775b1596a07d1200ba1d49e5e3941','createTable tableName=APPLICATION_DEFAULT_ROLES; createTable tableName=CLIENT; createTable tableName=CLIENT_SESSION; createTable tableName=CLIENT_SESSION_ROLE; createTable tableName=COMPOSITE_ROLE; createTable tableName=CREDENTIAL; createTable tab...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.1.0.Beta1','sthorger@redhat.com','META-INF/jpa-changelog-1.1.0.Beta1.xml','2024-09-01 07:51:52',3,'EXECUTED','9:5f090e44a7d595883c1fb61f4b41fd38','delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=CLIENT_ATTRIBUTES; createTable tableName=CLIENT_SESSION_NOTE; createTable tableName=APP_NODE_REGISTRATIONS; addColumn table...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.1.0.Final','sthorger@redhat.com','META-INF/jpa-changelog-1.1.0.Final.xml','2024-09-01 07:51:52',4,'EXECUTED','9:c07e577387a3d2c04d1adc9aaad8730e','renameColumn newColumnName=EVENT_TIME, oldColumnName=TIME, tableName=EVENT_ENTITY','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.2.0.Beta1','psilva@redhat.com','META-INF/jpa-changelog-1.2.0.Beta1.xml','2024-09-01 07:51:53',5,'EXECUTED','9:b68ce996c655922dbcd2fe6b6ae72686','delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=PROTOCOL_MAPPER; createTable tableName=PROTOCOL_MAPPER_CONFIG; createTable tableName=...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.2.0.Beta1','psilva@redhat.com','META-INF/db2-jpa-changelog-1.2.0.Beta1.xml','2024-09-01 07:51:53',6,'MARK_RAN','9:543b5c9989f024fe35c6f6c5a97de88e','delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=PROTOCOL_MAPPER; createTable tableName=PROTOCOL_MAPPER_CONFIG; createTable tableName=...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.2.0.RC1','bburke@redhat.com','META-INF/jpa-changelog-1.2.0.CR1.xml','2024-09-01 07:51:54',7,'EXECUTED','9:765afebbe21cf5bbca048e632df38336','delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=MIGRATION_MODEL; createTable tableName=IDENTITY_P...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.2.0.RC1','bburke@redhat.com','META-INF/db2-jpa-changelog-1.2.0.CR1.xml','2024-09-01 07:51:54',8,'MARK_RAN','9:db4a145ba11a6fdaefb397f6dbf829a1','delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=MIGRATION_MODEL; createTable tableName=IDENTITY_P...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.2.0.Final','keycloak','META-INF/jpa-changelog-1.2.0.Final.xml','2024-09-01 07:51:54',9,'EXECUTED','9:9d05c7be10cdb873f8bcb41bc3a8ab23','update tableName=CLIENT; update tableName=CLIENT; update tableName=CLIENT','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.3.0','bburke@redhat.com','META-INF/jpa-changelog-1.3.0.xml','2024-09-01 07:51:55',10,'EXECUTED','9:18593702353128d53111f9b1ff0b82b8','delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=ADMI...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.4.0','bburke@redhat.com','META-INF/jpa-changelog-1.4.0.xml','2024-09-01 07:51:56',11,'EXECUTED','9:6122efe5f090e41a85c0f1c9e52cbb62','delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.4.0','bburke@redhat.com','META-INF/db2-jpa-changelog-1.4.0.xml','2024-09-01 07:51:56',12,'MARK_RAN','9:e1ff28bf7568451453f844c5d54bb0b5','delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.5.0','bburke@redhat.com','META-INF/jpa-changelog-1.5.0.xml','2024-09-01 07:51:56',13,'EXECUTED','9:7af32cd8957fbc069f796b61217483fd','delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.6.1_from15','mposolda@redhat.com','META-INF/jpa-changelog-1.6.1.xml','2024-09-01 07:51:56',14,'EXECUTED','9:6005e15e84714cd83226bf7879f54190','addColumn tableName=REALM; addColumn tableName=KEYCLOAK_ROLE; addColumn tableName=CLIENT; createTable tableName=OFFLINE_USER_SESSION; createTable tableName=OFFLINE_CLIENT_SESSION; addPrimaryKey constraintName=CONSTRAINT_OFFL_US_SES_PK2, tableName=...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.6.1_from16-pre','mposolda@redhat.com','META-INF/jpa-changelog-1.6.1.xml','2024-09-01 07:51:56',15,'MARK_RAN','9:bf656f5a2b055d07f314431cae76f06c','delete tableName=OFFLINE_CLIENT_SESSION; delete tableName=OFFLINE_USER_SESSION','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.6.1_from16','mposolda@redhat.com','META-INF/jpa-changelog-1.6.1.xml','2024-09-01 07:51:56',16,'MARK_RAN','9:f8dadc9284440469dcf71e25ca6ab99b','dropPrimaryKey constraintName=CONSTRAINT_OFFLINE_US_SES_PK, tableName=OFFLINE_USER_SESSION; dropPrimaryKey constraintName=CONSTRAINT_OFFLINE_CL_SES_PK, tableName=OFFLINE_CLIENT_SESSION; addColumn tableName=OFFLINE_USER_SESSION; update tableName=OF...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.6.1','mposolda@redhat.com','META-INF/jpa-changelog-1.6.1.xml','2024-09-01 07:51:56',17,'EXECUTED','9:d41d8cd98f00b204e9800998ecf8427e','empty','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.7.0','bburke@redhat.com','META-INF/jpa-changelog-1.7.0.xml','2024-09-01 07:51:57',18,'EXECUTED','9:3368ff0be4c2855ee2dd9ca813b38d8e','createTable tableName=KEYCLOAK_GROUP; createTable tableName=GROUP_ROLE_MAPPING; createTable tableName=GROUP_ATTRIBUTE; createTable tableName=USER_GROUP_MEMBERSHIP; createTable tableName=REALM_DEFAULT_GROUPS; addColumn tableName=IDENTITY_PROVIDER; ...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.8.0','mposolda@redhat.com','META-INF/jpa-changelog-1.8.0.xml','2024-09-01 07:51:57',19,'EXECUTED','9:8ac2fb5dd030b24c0570a763ed75ed20','addColumn tableName=IDENTITY_PROVIDER; createTable tableName=CLIENT_TEMPLATE; createTable tableName=CLIENT_TEMPLATE_ATTRIBUTES; createTable tableName=TEMPLATE_SCOPE_MAPPING; dropNotNullConstraint columnName=CLIENT_ID, tableName=PROTOCOL_MAPPER; ad...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.8.0-2','keycloak','META-INF/jpa-changelog-1.8.0.xml','2024-09-01 07:51:57',20,'EXECUTED','9:f91ddca9b19743db60e3057679810e6c','dropDefaultValue columnName=ALGORITHM, tableName=CREDENTIAL; update tableName=CREDENTIAL','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.8.0','mposolda@redhat.com','META-INF/db2-jpa-changelog-1.8.0.xml','2024-09-01 07:51:57',21,'MARK_RAN','9:831e82914316dc8a57dc09d755f23c51','addColumn tableName=IDENTITY_PROVIDER; createTable tableName=CLIENT_TEMPLATE; createTable tableName=CLIENT_TEMPLATE_ATTRIBUTES; createTable tableName=TEMPLATE_SCOPE_MAPPING; dropNotNullConstraint columnName=CLIENT_ID, tableName=PROTOCOL_MAPPER; ad...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.8.0-2','keycloak','META-INF/db2-jpa-changelog-1.8.0.xml','2024-09-01 07:51:57',22,'MARK_RAN','9:f91ddca9b19743db60e3057679810e6c','dropDefaultValue columnName=ALGORITHM, tableName=CREDENTIAL; update tableName=CREDENTIAL','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.9.0','mposolda@redhat.com','META-INF/jpa-changelog-1.9.0.xml','2024-09-01 07:51:57',23,'EXECUTED','9:bc3d0f9e823a69dc21e23e94c7a94bb1','update tableName=REALM; update tableName=REALM; update tableName=REALM; update tableName=REALM; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=REALM; update tableName=REALM; customChange; dr...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.9.1','keycloak','META-INF/jpa-changelog-1.9.1.xml','2024-09-01 07:51:57',24,'EXECUTED','9:c9999da42f543575ab790e76439a2679','modifyDataType columnName=PRIVATE_KEY, tableName=REALM; modifyDataType columnName=PUBLIC_KEY, tableName=REALM; modifyDataType columnName=CERTIFICATE, tableName=REALM','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.9.1','keycloak','META-INF/db2-jpa-changelog-1.9.1.xml','2024-09-01 07:51:57',25,'MARK_RAN','9:0d6c65c6f58732d81569e77b10ba301d','modifyDataType columnName=PRIVATE_KEY, tableName=REALM; modifyDataType columnName=CERTIFICATE, tableName=REALM','',NULL,'4.23.2',NULL,NULL,'5177109193'),('1.9.2','keycloak','META-INF/jpa-changelog-1.9.2.xml','2024-09-01 07:51:57',26,'EXECUTED','9:fc576660fc016ae53d2d4778d84d86d0','createIndex indexName=IDX_USER_EMAIL, tableName=USER_ENTITY; createIndex indexName=IDX_USER_ROLE_MAPPING, tableName=USER_ROLE_MAPPING; createIndex indexName=IDX_USER_GROUP_MAPPING, tableName=USER_GROUP_MEMBERSHIP; createIndex indexName=IDX_USER_CO...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('authz-2.0.0','psilva@redhat.com','META-INF/jpa-changelog-authz-2.0.0.xml','2024-09-01 07:51:58',27,'EXECUTED','9:43ed6b0da89ff77206289e87eaa9c024','createTable tableName=RESOURCE_SERVER; addPrimaryKey constraintName=CONSTRAINT_FARS, tableName=RESOURCE_SERVER; addUniqueConstraint constraintName=UK_AU8TT6T700S9V50BU18WS5HA6, tableName=RESOURCE_SERVER; createTable tableName=RESOURCE_SERVER_RESOU...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('authz-2.5.1','psilva@redhat.com','META-INF/jpa-changelog-authz-2.5.1.xml','2024-09-01 07:51:58',28,'EXECUTED','9:44bae577f551b3738740281eceb4ea70','update tableName=RESOURCE_SERVER_POLICY','',NULL,'4.23.2',NULL,NULL,'5177109193'),('2.1.0-KEYCLOAK-5461','bburke@redhat.com','META-INF/jpa-changelog-2.1.0.xml','2024-09-01 07:51:59',29,'EXECUTED','9:bd88e1f833df0420b01e114533aee5e8','createTable tableName=BROKER_LINK; createTable tableName=FED_USER_ATTRIBUTE; createTable tableName=FED_USER_CONSENT; createTable tableName=FED_USER_CONSENT_ROLE; createTable tableName=FED_USER_CONSENT_PROT_MAPPER; createTable tableName=FED_USER_CR...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('2.2.0','bburke@redhat.com','META-INF/jpa-changelog-2.2.0.xml','2024-09-01 07:51:59',30,'EXECUTED','9:a7022af5267f019d020edfe316ef4371','addColumn tableName=ADMIN_EVENT_ENTITY; createTable tableName=CREDENTIAL_ATTRIBUTE; createTable tableName=FED_CREDENTIAL_ATTRIBUTE; modifyDataType columnName=VALUE, tableName=CREDENTIAL; addForeignKeyConstraint baseTableName=FED_CREDENTIAL_ATTRIBU...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('2.3.0','bburke@redhat.com','META-INF/jpa-changelog-2.3.0.xml','2024-09-01 07:51:59',31,'EXECUTED','9:fc155c394040654d6a79227e56f5e25a','createTable tableName=FEDERATED_USER; addPrimaryKey constraintName=CONSTR_FEDERATED_USER, tableName=FEDERATED_USER; dropDefaultValue columnName=TOTP, tableName=USER_ENTITY; dropColumn columnName=TOTP, tableName=USER_ENTITY; addColumn tableName=IDE...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('2.4.0','bburke@redhat.com','META-INF/jpa-changelog-2.4.0.xml','2024-09-01 07:51:59',32,'EXECUTED','9:eac4ffb2a14795e5dc7b426063e54d88','customChange','',NULL,'4.23.2',NULL,NULL,'5177109193'),('2.5.0','bburke@redhat.com','META-INF/jpa-changelog-2.5.0.xml','2024-09-01 07:51:59',33,'EXECUTED','9:54937c05672568c4c64fc9524c1e9462','customChange; modifyDataType columnName=USER_ID, tableName=OFFLINE_USER_SESSION','',NULL,'4.23.2',NULL,NULL,'5177109193'),('2.5.0-unicode-oracle','hmlnarik@redhat.com','META-INF/jpa-changelog-2.5.0.xml','2024-09-01 07:51:59',34,'MARK_RAN','9:3a32bace77c84d7678d035a7f5a8084e','modifyDataType columnName=DESCRIPTION, tableName=AUTHENTICATION_FLOW; modifyDataType columnName=DESCRIPTION, tableName=CLIENT_TEMPLATE; modifyDataType columnName=DESCRIPTION, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=DESCRIPTION,...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('2.5.0-unicode-other-dbs','hmlnarik@redhat.com','META-INF/jpa-changelog-2.5.0.xml','2024-09-01 07:52:00',35,'EXECUTED','9:33d72168746f81f98ae3a1e8e0ca3554','modifyDataType columnName=DESCRIPTION, tableName=AUTHENTICATION_FLOW; modifyDataType columnName=DESCRIPTION, tableName=CLIENT_TEMPLATE; modifyDataType columnName=DESCRIPTION, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=DESCRIPTION,...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('2.5.0-duplicate-email-support','slawomir@dabek.name','META-INF/jpa-changelog-2.5.0.xml','2024-09-01 07:52:00',36,'EXECUTED','9:61b6d3d7a4c0e0024b0c839da283da0c','addColumn tableName=REALM','',NULL,'4.23.2',NULL,NULL,'5177109193'),('2.5.0-unique-group-names','hmlnarik@redhat.com','META-INF/jpa-changelog-2.5.0.xml','2024-09-01 07:52:00',37,'EXECUTED','9:8dcac7bdf7378e7d823cdfddebf72fda','addUniqueConstraint constraintName=SIBLING_NAMES, tableName=KEYCLOAK_GROUP','',NULL,'4.23.2',NULL,NULL,'5177109193'),('2.5.1','bburke@redhat.com','META-INF/jpa-changelog-2.5.1.xml','2024-09-01 07:52:00',38,'EXECUTED','9:a2b870802540cb3faa72098db5388af3','addColumn tableName=FED_USER_CONSENT','',NULL,'4.23.2',NULL,NULL,'5177109193'),('3.0.0','bburke@redhat.com','META-INF/jpa-changelog-3.0.0.xml','2024-09-01 07:52:00',39,'EXECUTED','9:132a67499ba24bcc54fb5cbdcfe7e4c0','addColumn tableName=IDENTITY_PROVIDER','',NULL,'4.23.2',NULL,NULL,'5177109193'),('3.2.0-fix','keycloak','META-INF/jpa-changelog-3.2.0.xml','2024-09-01 07:52:00',40,'MARK_RAN','9:938f894c032f5430f2b0fafb1a243462','addNotNullConstraint columnName=REALM_ID, tableName=CLIENT_INITIAL_ACCESS','',NULL,'4.23.2',NULL,NULL,'5177109193'),('3.2.0-fix-with-keycloak-5416','keycloak','META-INF/jpa-changelog-3.2.0.xml','2024-09-01 07:52:00',41,'MARK_RAN','9:845c332ff1874dc5d35974b0babf3006','dropIndex indexName=IDX_CLIENT_INIT_ACC_REALM, tableName=CLIENT_INITIAL_ACCESS; addNotNullConstraint columnName=REALM_ID, tableName=CLIENT_INITIAL_ACCESS; createIndex indexName=IDX_CLIENT_INIT_ACC_REALM, tableName=CLIENT_INITIAL_ACCESS','',NULL,'4.23.2',NULL,NULL,'5177109193'),('3.2.0-fix-offline-sessions','hmlnarik','META-INF/jpa-changelog-3.2.0.xml','2024-09-01 07:52:00',42,'EXECUTED','9:fc86359c079781adc577c5a217e4d04c','customChange','',NULL,'4.23.2',NULL,NULL,'5177109193'),('3.2.0-fixed','keycloak','META-INF/jpa-changelog-3.2.0.xml','2024-09-01 07:52:01',43,'EXECUTED','9:59a64800e3c0d09b825f8a3b444fa8f4','addColumn tableName=REALM; dropPrimaryKey constraintName=CONSTRAINT_OFFL_CL_SES_PK2, tableName=OFFLINE_CLIENT_SESSION; dropColumn columnName=CLIENT_SESSION_ID, tableName=OFFLINE_CLIENT_SESSION; addPrimaryKey constraintName=CONSTRAINT_OFFL_CL_SES_P...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('3.3.0','keycloak','META-INF/jpa-changelog-3.3.0.xml','2024-09-01 07:52:01',44,'EXECUTED','9:d48d6da5c6ccf667807f633fe489ce88','addColumn tableName=USER_ENTITY','',NULL,'4.23.2',NULL,NULL,'5177109193'),('authz-3.4.0.CR1-resource-server-pk-change-part1','glavoie@gmail.com','META-INF/jpa-changelog-authz-3.4.0.CR1.xml','2024-09-01 07:52:01',45,'EXECUTED','9:dde36f7973e80d71fceee683bc5d2951','addColumn tableName=RESOURCE_SERVER_POLICY; addColumn tableName=RESOURCE_SERVER_RESOURCE; addColumn tableName=RESOURCE_SERVER_SCOPE','',NULL,'4.23.2',NULL,NULL,'5177109193'),('authz-3.4.0.CR1-resource-server-pk-change-part2-KEYCLOAK-6095','hmlnarik@redhat.com','META-INF/jpa-changelog-authz-3.4.0.CR1.xml','2024-09-01 07:52:01',46,'EXECUTED','9:b855e9b0a406b34fa323235a0cf4f640','customChange','',NULL,'4.23.2',NULL,NULL,'5177109193'),('authz-3.4.0.CR1-resource-server-pk-change-part3-fixed','glavoie@gmail.com','META-INF/jpa-changelog-authz-3.4.0.CR1.xml','2024-09-01 07:52:01',47,'MARK_RAN','9:51abbacd7b416c50c4421a8cabf7927e','dropIndex indexName=IDX_RES_SERV_POL_RES_SERV, tableName=RESOURCE_SERVER_POLICY; dropIndex indexName=IDX_RES_SRV_RES_RES_SRV, tableName=RESOURCE_SERVER_RESOURCE; dropIndex indexName=IDX_RES_SRV_SCOPE_RES_SRV, tableName=RESOURCE_SERVER_SCOPE','',NULL,'4.23.2',NULL,NULL,'5177109193'),('authz-3.4.0.CR1-resource-server-pk-change-part3-fixed-nodropindex','glavoie@gmail.com','META-INF/jpa-changelog-authz-3.4.0.CR1.xml','2024-09-01 07:52:02',48,'EXECUTED','9:bdc99e567b3398bac83263d375aad143','addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, tableName=RESOURCE_SERVER_POLICY; addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, tableName=RESOURCE_SERVER_RESOURCE; addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, ...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('authn-3.4.0.CR1-refresh-token-max-reuse','glavoie@gmail.com','META-INF/jpa-changelog-authz-3.4.0.CR1.xml','2024-09-01 07:52:02',49,'EXECUTED','9:d198654156881c46bfba39abd7769e69','addColumn tableName=REALM','',NULL,'4.23.2',NULL,NULL,'5177109193'),('3.4.0','keycloak','META-INF/jpa-changelog-3.4.0.xml','2024-09-01 07:52:03',50,'EXECUTED','9:cfdd8736332ccdd72c5256ccb42335db','addPrimaryKey constraintName=CONSTRAINT_REALM_DEFAULT_ROLES, tableName=REALM_DEFAULT_ROLES; addPrimaryKey constraintName=CONSTRAINT_COMPOSITE_ROLE, tableName=COMPOSITE_ROLE; addPrimaryKey constraintName=CONSTR_REALM_DEFAULT_GROUPS, tableName=REALM...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('3.4.0-KEYCLOAK-5230','hmlnarik@redhat.com','META-INF/jpa-changelog-3.4.0.xml','2024-09-01 07:52:03',51,'EXECUTED','9:7c84de3d9bd84d7f077607c1a4dcb714','createIndex indexName=IDX_FU_ATTRIBUTE, tableName=FED_USER_ATTRIBUTE; createIndex indexName=IDX_FU_CONSENT, tableName=FED_USER_CONSENT; createIndex indexName=IDX_FU_CONSENT_RU, tableName=FED_USER_CONSENT; createIndex indexName=IDX_FU_CREDENTIAL, t...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('3.4.1','psilva@redhat.com','META-INF/jpa-changelog-3.4.1.xml','2024-09-01 07:52:03',52,'EXECUTED','9:5a6bb36cbefb6a9d6928452c0852af2d','modifyDataType columnName=VALUE, tableName=CLIENT_ATTRIBUTES','',NULL,'4.23.2',NULL,NULL,'5177109193'),('3.4.2','keycloak','META-INF/jpa-changelog-3.4.2.xml','2024-09-01 07:52:03',53,'EXECUTED','9:8f23e334dbc59f82e0a328373ca6ced0','update tableName=REALM','',NULL,'4.23.2',NULL,NULL,'5177109193'),('3.4.2-KEYCLOAK-5172','mkanis@redhat.com','META-INF/jpa-changelog-3.4.2.xml','2024-09-01 07:52:03',54,'EXECUTED','9:9156214268f09d970cdf0e1564d866af','update tableName=CLIENT','',NULL,'4.23.2',NULL,NULL,'5177109193'),('4.0.0-KEYCLOAK-6335','bburke@redhat.com','META-INF/jpa-changelog-4.0.0.xml','2024-09-01 07:52:03',55,'EXECUTED','9:db806613b1ed154826c02610b7dbdf74','createTable tableName=CLIENT_AUTH_FLOW_BINDINGS; addPrimaryKey constraintName=C_CLI_FLOW_BIND, tableName=CLIENT_AUTH_FLOW_BINDINGS','',NULL,'4.23.2',NULL,NULL,'5177109193'),('4.0.0-CLEANUP-UNUSED-TABLE','bburke@redhat.com','META-INF/jpa-changelog-4.0.0.xml','2024-09-01 07:52:03',56,'EXECUTED','9:229a041fb72d5beac76bb94a5fa709de','dropTable tableName=CLIENT_IDENTITY_PROV_MAPPING','',NULL,'4.23.2',NULL,NULL,'5177109193'),('4.0.0-KEYCLOAK-6228','bburke@redhat.com','META-INF/jpa-changelog-4.0.0.xml','2024-09-01 07:52:03',57,'EXECUTED','9:079899dade9c1e683f26b2aa9ca6ff04','dropUniqueConstraint constraintName=UK_JKUWUVD56ONTGSUHOGM8UEWRT, tableName=USER_CONSENT; dropNotNullConstraint columnName=CLIENT_ID, tableName=USER_CONSENT; addColumn tableName=USER_CONSENT; addUniqueConstraint constraintName=UK_JKUWUVD56ONTGSUHO...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('4.0.0-KEYCLOAK-5579-fixed','mposolda@redhat.com','META-INF/jpa-changelog-4.0.0.xml','2024-09-01 07:52:04',58,'EXECUTED','9:139b79bcbbfe903bb1c2d2a4dbf001d9','dropForeignKeyConstraint baseTableName=CLIENT_TEMPLATE_ATTRIBUTES, constraintName=FK_CL_TEMPL_ATTR_TEMPL; renameTable newTableName=CLIENT_SCOPE_ATTRIBUTES, oldTableName=CLIENT_TEMPLATE_ATTRIBUTES; renameColumn newColumnName=SCOPE_ID, oldColumnName...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('authz-4.0.0.CR1','psilva@redhat.com','META-INF/jpa-changelog-authz-4.0.0.CR1.xml','2024-09-01 07:52:05',59,'EXECUTED','9:b55738ad889860c625ba2bf483495a04','createTable tableName=RESOURCE_SERVER_PERM_TICKET; addPrimaryKey constraintName=CONSTRAINT_FAPMT, tableName=RESOURCE_SERVER_PERM_TICKET; addForeignKeyConstraint baseTableName=RESOURCE_SERVER_PERM_TICKET, constraintName=FK_FRSRHO213XCX4WNKOG82SSPMT...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('authz-4.0.0.Beta3','psilva@redhat.com','META-INF/jpa-changelog-authz-4.0.0.Beta3.xml','2024-09-01 07:52:05',60,'EXECUTED','9:e0057eac39aa8fc8e09ac6cfa4ae15fe','addColumn tableName=RESOURCE_SERVER_POLICY; addColumn tableName=RESOURCE_SERVER_PERM_TICKET; addForeignKeyConstraint baseTableName=RESOURCE_SERVER_PERM_TICKET, constraintName=FK_FRSRPO2128CX4WNKOG82SSRFY, referencedTableName=RESOURCE_SERVER_POLICY','',NULL,'4.23.2',NULL,NULL,'5177109193'),('authz-4.2.0.Final','mhajas@redhat.com','META-INF/jpa-changelog-authz-4.2.0.Final.xml','2024-09-01 07:52:05',61,'EXECUTED','9:42a33806f3a0443fe0e7feeec821326c','createTable tableName=RESOURCE_URIS; addForeignKeyConstraint baseTableName=RESOURCE_URIS, constraintName=FK_RESOURCE_SERVER_URIS, referencedTableName=RESOURCE_SERVER_RESOURCE; customChange; dropColumn columnName=URI, tableName=RESOURCE_SERVER_RESO...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('authz-4.2.0.Final-KEYCLOAK-9944','hmlnarik@redhat.com','META-INF/jpa-changelog-authz-4.2.0.Final.xml','2024-09-01 07:52:05',62,'EXECUTED','9:9968206fca46eecc1f51db9c024bfe56','addPrimaryKey constraintName=CONSTRAINT_RESOUR_URIS_PK, tableName=RESOURCE_URIS','',NULL,'4.23.2',NULL,NULL,'5177109193'),('4.2.0-KEYCLOAK-6313','wadahiro@gmail.com','META-INF/jpa-changelog-4.2.0.xml','2024-09-01 07:52:05',63,'EXECUTED','9:92143a6daea0a3f3b8f598c97ce55c3d','addColumn tableName=REQUIRED_ACTION_PROVIDER','',NULL,'4.23.2',NULL,NULL,'5177109193'),('4.3.0-KEYCLOAK-7984','wadahiro@gmail.com','META-INF/jpa-changelog-4.3.0.xml','2024-09-01 07:52:05',64,'EXECUTED','9:82bab26a27195d889fb0429003b18f40','update tableName=REQUIRED_ACTION_PROVIDER','',NULL,'4.23.2',NULL,NULL,'5177109193'),('4.6.0-KEYCLOAK-7950','psilva@redhat.com','META-INF/jpa-changelog-4.6.0.xml','2024-09-01 07:52:05',65,'EXECUTED','9:e590c88ddc0b38b0ae4249bbfcb5abc3','update tableName=RESOURCE_SERVER_RESOURCE','',NULL,'4.23.2',NULL,NULL,'5177109193'),('4.6.0-KEYCLOAK-8377','keycloak','META-INF/jpa-changelog-4.6.0.xml','2024-09-01 07:52:05',66,'EXECUTED','9:5c1f475536118dbdc38d5d7977950cc0','createTable tableName=ROLE_ATTRIBUTE; addPrimaryKey constraintName=CONSTRAINT_ROLE_ATTRIBUTE_PK, tableName=ROLE_ATTRIBUTE; addForeignKeyConstraint baseTableName=ROLE_ATTRIBUTE, constraintName=FK_ROLE_ATTRIBUTE_ID, referencedTableName=KEYCLOAK_ROLE...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('4.6.0-KEYCLOAK-8555','gideonray@gmail.com','META-INF/jpa-changelog-4.6.0.xml','2024-09-01 07:52:05',67,'EXECUTED','9:e7c9f5f9c4d67ccbbcc215440c718a17','createIndex indexName=IDX_COMPONENT_PROVIDER_TYPE, tableName=COMPONENT','',NULL,'4.23.2',NULL,NULL,'5177109193'),('4.7.0-KEYCLOAK-1267','sguilhen@redhat.com','META-INF/jpa-changelog-4.7.0.xml','2024-09-01 07:52:05',68,'EXECUTED','9:88e0bfdda924690d6f4e430c53447dd5','addColumn tableName=REALM','',NULL,'4.23.2',NULL,NULL,'5177109193'),('4.7.0-KEYCLOAK-7275','keycloak','META-INF/jpa-changelog-4.7.0.xml','2024-09-01 07:52:05',69,'EXECUTED','9:f53177f137e1c46b6a88c59ec1cb5218','renameColumn newColumnName=CREATED_ON, oldColumnName=LAST_SESSION_REFRESH, tableName=OFFLINE_USER_SESSION; addNotNullConstraint columnName=CREATED_ON, tableName=OFFLINE_USER_SESSION; addColumn tableName=OFFLINE_USER_SESSION; customChange; createIn...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('4.8.0-KEYCLOAK-8835','sguilhen@redhat.com','META-INF/jpa-changelog-4.8.0.xml','2024-09-01 07:52:05',70,'EXECUTED','9:a74d33da4dc42a37ec27121580d1459f','addNotNullConstraint columnName=SSO_MAX_LIFESPAN_REMEMBER_ME, tableName=REALM; addNotNullConstraint columnName=SSO_IDLE_TIMEOUT_REMEMBER_ME, tableName=REALM','',NULL,'4.23.2',NULL,NULL,'5177109193'),('authz-7.0.0-KEYCLOAK-10443','psilva@redhat.com','META-INF/jpa-changelog-authz-7.0.0.xml','2024-09-01 07:52:05',71,'EXECUTED','9:fd4ade7b90c3b67fae0bfcfcb42dfb5f','addColumn tableName=RESOURCE_SERVER','',NULL,'4.23.2',NULL,NULL,'5177109193'),('8.0.0-adding-credential-columns','keycloak','META-INF/jpa-changelog-8.0.0.xml','2024-09-01 07:52:05',72,'EXECUTED','9:aa072ad090bbba210d8f18781b8cebf4','addColumn tableName=CREDENTIAL; addColumn tableName=FED_USER_CREDENTIAL','',NULL,'4.23.2',NULL,NULL,'5177109193'),('8.0.0-updating-credential-data-not-oracle-fixed','keycloak','META-INF/jpa-changelog-8.0.0.xml','2024-09-01 07:52:05',73,'EXECUTED','9:1ae6be29bab7c2aa376f6983b932be37','update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL','',NULL,'4.23.2',NULL,NULL,'5177109193'),('8.0.0-updating-credential-data-oracle-fixed','keycloak','META-INF/jpa-changelog-8.0.0.xml','2024-09-01 07:52:05',74,'MARK_RAN','9:14706f286953fc9a25286dbd8fb30d97','update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL','',NULL,'4.23.2',NULL,NULL,'5177109193'),('8.0.0-credential-cleanup-fixed','keycloak','META-INF/jpa-changelog-8.0.0.xml','2024-09-01 07:52:06',75,'EXECUTED','9:2b9cc12779be32c5b40e2e67711a218b','dropDefaultValue columnName=COUNTER, tableName=CREDENTIAL; dropDefaultValue columnName=DIGITS, tableName=CREDENTIAL; dropDefaultValue columnName=PERIOD, tableName=CREDENTIAL; dropDefaultValue columnName=ALGORITHM, tableName=CREDENTIAL; dropColumn ...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('8.0.0-resource-tag-support','keycloak','META-INF/jpa-changelog-8.0.0.xml','2024-09-01 07:52:06',76,'EXECUTED','9:91fa186ce7a5af127a2d7a91ee083cc5','addColumn tableName=MIGRATION_MODEL; createIndex indexName=IDX_UPDATE_TIME, tableName=MIGRATION_MODEL','',NULL,'4.23.2',NULL,NULL,'5177109193'),('9.0.0-always-display-client','keycloak','META-INF/jpa-changelog-9.0.0.xml','2024-09-01 07:52:06',77,'EXECUTED','9:6335e5c94e83a2639ccd68dd24e2e5ad','addColumn tableName=CLIENT','',NULL,'4.23.2',NULL,NULL,'5177109193'),('9.0.0-drop-constraints-for-column-increase','keycloak','META-INF/jpa-changelog-9.0.0.xml','2024-09-01 07:52:06',78,'MARK_RAN','9:6bdb5658951e028bfe16fa0a8228b530','dropUniqueConstraint constraintName=UK_FRSR6T700S9V50BU18WS5PMT, tableName=RESOURCE_SERVER_PERM_TICKET; dropUniqueConstraint constraintName=UK_FRSR6T700S9V50BU18WS5HA6, tableName=RESOURCE_SERVER_RESOURCE; dropPrimaryKey constraintName=CONSTRAINT_O...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('9.0.0-increase-column-size-federated-fk','keycloak','META-INF/jpa-changelog-9.0.0.xml','2024-09-01 07:52:06',79,'EXECUTED','9:d5bc15a64117ccad481ce8792d4c608f','modifyDataType columnName=CLIENT_ID, tableName=FED_USER_CONSENT; modifyDataType columnName=CLIENT_REALM_CONSTRAINT, tableName=KEYCLOAK_ROLE; modifyDataType columnName=OWNER, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=CLIENT_ID, ta...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('9.0.0-recreate-constraints-after-column-increase','keycloak','META-INF/jpa-changelog-9.0.0.xml','2024-09-01 07:52:06',80,'MARK_RAN','9:077cba51999515f4d3e7ad5619ab592c','addNotNullConstraint columnName=CLIENT_ID, tableName=OFFLINE_CLIENT_SESSION; addNotNullConstraint columnName=OWNER, tableName=RESOURCE_SERVER_PERM_TICKET; addNotNullConstraint columnName=REQUESTER, tableName=RESOURCE_SERVER_PERM_TICKET; addNotNull...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('9.0.1-add-index-to-client.client_id','keycloak','META-INF/jpa-changelog-9.0.1.xml','2024-09-01 07:52:06',81,'EXECUTED','9:be969f08a163bf47c6b9e9ead8ac2afb','createIndex indexName=IDX_CLIENT_ID, tableName=CLIENT','',NULL,'4.23.2',NULL,NULL,'5177109193'),('9.0.1-KEYCLOAK-12579-drop-constraints','keycloak','META-INF/jpa-changelog-9.0.1.xml','2024-09-01 07:52:06',82,'MARK_RAN','9:6d3bb4408ba5a72f39bd8a0b301ec6e3','dropUniqueConstraint constraintName=SIBLING_NAMES, tableName=KEYCLOAK_GROUP','',NULL,'4.23.2',NULL,NULL,'5177109193'),('9.0.1-KEYCLOAK-12579-add-not-null-constraint','keycloak','META-INF/jpa-changelog-9.0.1.xml','2024-09-01 07:52:06',83,'EXECUTED','9:966bda61e46bebf3cc39518fbed52fa7','addNotNullConstraint columnName=PARENT_GROUP, tableName=KEYCLOAK_GROUP','',NULL,'4.23.2',NULL,NULL,'5177109193'),('9.0.1-KEYCLOAK-12579-recreate-constraints','keycloak','META-INF/jpa-changelog-9.0.1.xml','2024-09-01 07:52:06',84,'MARK_RAN','9:8dcac7bdf7378e7d823cdfddebf72fda','addUniqueConstraint constraintName=SIBLING_NAMES, tableName=KEYCLOAK_GROUP','',NULL,'4.23.2',NULL,NULL,'5177109193'),('9.0.1-add-index-to-events','keycloak','META-INF/jpa-changelog-9.0.1.xml','2024-09-01 07:52:06',85,'EXECUTED','9:7d93d602352a30c0c317e6a609b56599','createIndex indexName=IDX_EVENT_TIME, tableName=EVENT_ENTITY','',NULL,'4.23.2',NULL,NULL,'5177109193'),('map-remove-ri','keycloak','META-INF/jpa-changelog-11.0.0.xml','2024-09-01 07:52:06',86,'EXECUTED','9:71c5969e6cdd8d7b6f47cebc86d37627','dropForeignKeyConstraint baseTableName=REALM, constraintName=FK_TRAF444KK6QRKMS7N56AIWQ5Y; dropForeignKeyConstraint baseTableName=KEYCLOAK_ROLE, constraintName=FK_KJHO5LE2C0RAL09FL8CM9WFW9','',NULL,'4.23.2',NULL,NULL,'5177109193'),('map-remove-ri','keycloak','META-INF/jpa-changelog-12.0.0.xml','2024-09-01 07:52:06',87,'EXECUTED','9:a9ba7d47f065f041b7da856a81762021','dropForeignKeyConstraint baseTableName=REALM_DEFAULT_GROUPS, constraintName=FK_DEF_GROUPS_GROUP; dropForeignKeyConstraint baseTableName=REALM_DEFAULT_ROLES, constraintName=FK_H4WPD7W4HSOOLNI3H0SW7BTJE; dropForeignKeyConstraint baseTableName=CLIENT...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('12.1.0-add-realm-localization-table','keycloak','META-INF/jpa-changelog-12.0.0.xml','2024-09-01 07:52:06',88,'EXECUTED','9:fffabce2bc01e1a8f5110d5278500065','createTable tableName=REALM_LOCALIZATIONS; addPrimaryKey tableName=REALM_LOCALIZATIONS','',NULL,'4.23.2',NULL,NULL,'5177109193'),('default-roles','keycloak','META-INF/jpa-changelog-13.0.0.xml','2024-09-01 07:52:06',89,'EXECUTED','9:fa8a5b5445e3857f4b010bafb5009957','addColumn tableName=REALM; customChange','',NULL,'4.23.2',NULL,NULL,'5177109193'),('default-roles-cleanup','keycloak','META-INF/jpa-changelog-13.0.0.xml','2024-09-01 07:52:06',90,'EXECUTED','9:67ac3241df9a8582d591c5ed87125f39','dropTable tableName=REALM_DEFAULT_ROLES; dropTable tableName=CLIENT_DEFAULT_ROLES','',NULL,'4.23.2',NULL,NULL,'5177109193'),('13.0.0-KEYCLOAK-16844','keycloak','META-INF/jpa-changelog-13.0.0.xml','2024-09-01 07:52:06',91,'EXECUTED','9:ad1194d66c937e3ffc82386c050ba089','createIndex indexName=IDX_OFFLINE_USS_PRELOAD, tableName=OFFLINE_USER_SESSION','',NULL,'4.23.2',NULL,NULL,'5177109193'),('map-remove-ri-13.0.0','keycloak','META-INF/jpa-changelog-13.0.0.xml','2024-09-01 07:52:06',92,'EXECUTED','9:d9be619d94af5a2f5d07b9f003543b91','dropForeignKeyConstraint baseTableName=DEFAULT_CLIENT_SCOPE, constraintName=FK_R_DEF_CLI_SCOPE_SCOPE; dropForeignKeyConstraint baseTableName=CLIENT_SCOPE_CLIENT, constraintName=FK_C_CLI_SCOPE_SCOPE; dropForeignKeyConstraint baseTableName=CLIENT_SC...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('13.0.0-KEYCLOAK-17992-drop-constraints','keycloak','META-INF/jpa-changelog-13.0.0.xml','2024-09-01 07:52:06',93,'MARK_RAN','9:544d201116a0fcc5a5da0925fbbc3bde','dropPrimaryKey constraintName=C_CLI_SCOPE_BIND, tableName=CLIENT_SCOPE_CLIENT; dropIndex indexName=IDX_CLSCOPE_CL, tableName=CLIENT_SCOPE_CLIENT; dropIndex indexName=IDX_CL_CLSCOPE, tableName=CLIENT_SCOPE_CLIENT','',NULL,'4.23.2',NULL,NULL,'5177109193'),('13.0.0-increase-column-size-federated','keycloak','META-INF/jpa-changelog-13.0.0.xml','2024-09-01 07:52:07',94,'EXECUTED','9:43c0c1055b6761b4b3e89de76d612ccf','modifyDataType columnName=CLIENT_ID, tableName=CLIENT_SCOPE_CLIENT; modifyDataType columnName=SCOPE_ID, tableName=CLIENT_SCOPE_CLIENT','',NULL,'4.23.2',NULL,NULL,'5177109193'),('13.0.0-KEYCLOAK-17992-recreate-constraints','keycloak','META-INF/jpa-changelog-13.0.0.xml','2024-09-01 07:52:07',95,'MARK_RAN','9:8bd711fd0330f4fe980494ca43ab1139','addNotNullConstraint columnName=CLIENT_ID, tableName=CLIENT_SCOPE_CLIENT; addNotNullConstraint columnName=SCOPE_ID, tableName=CLIENT_SCOPE_CLIENT; addPrimaryKey constraintName=C_CLI_SCOPE_BIND, tableName=CLIENT_SCOPE_CLIENT; createIndex indexName=...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('json-string-accomodation-fixed','keycloak','META-INF/jpa-changelog-13.0.0.xml','2024-09-01 07:52:07',96,'EXECUTED','9:e07d2bc0970c348bb06fb63b1f82ddbf','addColumn tableName=REALM_ATTRIBUTE; update tableName=REALM_ATTRIBUTE; dropColumn columnName=VALUE, tableName=REALM_ATTRIBUTE; renameColumn newColumnName=VALUE, oldColumnName=VALUE_NEW, tableName=REALM_ATTRIBUTE','',NULL,'4.23.2',NULL,NULL,'5177109193'),('14.0.0-KEYCLOAK-11019','keycloak','META-INF/jpa-changelog-14.0.0.xml','2024-09-01 07:52:07',97,'EXECUTED','9:24fb8611e97f29989bea412aa38d12b7','createIndex indexName=IDX_OFFLINE_CSS_PRELOAD, tableName=OFFLINE_CLIENT_SESSION; createIndex indexName=IDX_OFFLINE_USS_BY_USER, tableName=OFFLINE_USER_SESSION; createIndex indexName=IDX_OFFLINE_USS_BY_USERSESS, tableName=OFFLINE_USER_SESSION','',NULL,'4.23.2',NULL,NULL,'5177109193'),('14.0.0-KEYCLOAK-18286','keycloak','META-INF/jpa-changelog-14.0.0.xml','2024-09-01 07:52:07',98,'MARK_RAN','9:259f89014ce2506ee84740cbf7163aa7','createIndex indexName=IDX_CLIENT_ATT_BY_NAME_VALUE, tableName=CLIENT_ATTRIBUTES','',NULL,'4.23.2',NULL,NULL,'5177109193'),('14.0.0-KEYCLOAK-18286-revert','keycloak','META-INF/jpa-changelog-14.0.0.xml','2024-09-01 07:52:07',99,'MARK_RAN','9:04baaf56c116ed19951cbc2cca584022','dropIndex indexName=IDX_CLIENT_ATT_BY_NAME_VALUE, tableName=CLIENT_ATTRIBUTES','',NULL,'4.23.2',NULL,NULL,'5177109193'),('14.0.0-KEYCLOAK-18286-supported-dbs','keycloak','META-INF/jpa-changelog-14.0.0.xml','2024-09-01 07:52:07',100,'EXECUTED','9:bd2bd0fc7768cf0845ac96a8786fa735','createIndex indexName=IDX_CLIENT_ATT_BY_NAME_VALUE, tableName=CLIENT_ATTRIBUTES','',NULL,'4.23.2',NULL,NULL,'5177109193'),('14.0.0-KEYCLOAK-18286-unsupported-dbs','keycloak','META-INF/jpa-changelog-14.0.0.xml','2024-09-01 07:52:07',101,'MARK_RAN','9:d3d977031d431db16e2c181ce49d73e9','createIndex indexName=IDX_CLIENT_ATT_BY_NAME_VALUE, tableName=CLIENT_ATTRIBUTES','',NULL,'4.23.2',NULL,NULL,'5177109193'),('KEYCLOAK-17267-add-index-to-user-attributes','keycloak','META-INF/jpa-changelog-14.0.0.xml','2024-09-01 07:52:07',102,'EXECUTED','9:0b305d8d1277f3a89a0a53a659ad274c','createIndex indexName=IDX_USER_ATTRIBUTE_NAME, tableName=USER_ATTRIBUTE','',NULL,'4.23.2',NULL,NULL,'5177109193'),('KEYCLOAK-18146-add-saml-art-binding-identifier','keycloak','META-INF/jpa-changelog-14.0.0.xml','2024-09-01 07:52:07',103,'EXECUTED','9:2c374ad2cdfe20e2905a84c8fac48460','customChange','',NULL,'4.23.2',NULL,NULL,'5177109193'),('15.0.0-KEYCLOAK-18467','keycloak','META-INF/jpa-changelog-15.0.0.xml','2024-09-01 07:52:07',104,'EXECUTED','9:47a760639ac597360a8219f5b768b4de','addColumn tableName=REALM_LOCALIZATIONS; update tableName=REALM_LOCALIZATIONS; dropColumn columnName=TEXTS, tableName=REALM_LOCALIZATIONS; renameColumn newColumnName=TEXTS, oldColumnName=TEXTS_NEW, tableName=REALM_LOCALIZATIONS; addNotNullConstrai...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('17.0.0-9562','keycloak','META-INF/jpa-changelog-17.0.0.xml','2024-09-01 07:52:07',105,'EXECUTED','9:a6272f0576727dd8cad2522335f5d99e','createIndex indexName=IDX_USER_SERVICE_ACCOUNT, tableName=USER_ENTITY','',NULL,'4.23.2',NULL,NULL,'5177109193'),('18.0.0-10625-IDX_ADMIN_EVENT_TIME','keycloak','META-INF/jpa-changelog-18.0.0.xml','2024-09-01 07:52:07',106,'EXECUTED','9:015479dbd691d9cc8669282f4828c41d','createIndex indexName=IDX_ADMIN_EVENT_TIME, tableName=ADMIN_EVENT_ENTITY','',NULL,'4.23.2',NULL,NULL,'5177109193'),('19.0.0-10135','keycloak','META-INF/jpa-changelog-19.0.0.xml','2024-09-01 07:52:07',107,'EXECUTED','9:9518e495fdd22f78ad6425cc30630221','customChange','',NULL,'4.23.2',NULL,NULL,'5177109193'),('20.0.0-12964-supported-dbs','keycloak','META-INF/jpa-changelog-20.0.0.xml','2024-09-01 07:52:07',108,'EXECUTED','9:f2e1331a71e0aa85e5608fe42f7f681c','createIndex indexName=IDX_GROUP_ATT_BY_NAME_VALUE, tableName=GROUP_ATTRIBUTE','',NULL,'4.23.2',NULL,NULL,'5177109193'),('20.0.0-12964-unsupported-dbs','keycloak','META-INF/jpa-changelog-20.0.0.xml','2024-09-01 07:52:07',109,'MARK_RAN','9:1a6fcaa85e20bdeae0a9ce49b41946a5','createIndex indexName=IDX_GROUP_ATT_BY_NAME_VALUE, tableName=GROUP_ATTRIBUTE','',NULL,'4.23.2',NULL,NULL,'5177109193'),('client-attributes-string-accomodation-fixed','keycloak','META-INF/jpa-changelog-20.0.0.xml','2024-09-01 07:52:07',110,'EXECUTED','9:3f332e13e90739ed0c35b0b25b7822ca','addColumn tableName=CLIENT_ATTRIBUTES; update tableName=CLIENT_ATTRIBUTES; dropColumn columnName=VALUE, tableName=CLIENT_ATTRIBUTES; renameColumn newColumnName=VALUE, oldColumnName=VALUE_NEW, tableName=CLIENT_ATTRIBUTES','',NULL,'4.23.2',NULL,NULL,'5177109193'),('21.0.2-17277','keycloak','META-INF/jpa-changelog-21.0.2.xml','2024-09-01 07:52:07',111,'EXECUTED','9:7ee1f7a3fb8f5588f171fb9a6ab623c0','customChange','',NULL,'4.23.2',NULL,NULL,'5177109193'),('21.1.0-19404','keycloak','META-INF/jpa-changelog-21.1.0.xml','2024-09-01 07:52:07',112,'EXECUTED','9:3d7e830b52f33676b9d64f7f2b2ea634','modifyDataType columnName=DECISION_STRATEGY, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=LOGIC, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=POLICY_ENFORCE_MODE, tableName=RESOURCE_SERVER','',NULL,'4.23.2',NULL,NULL,'5177109193'),('21.1.0-19404-2','keycloak','META-INF/jpa-changelog-21.1.0.xml','2024-09-01 07:52:07',113,'MARK_RAN','9:627d032e3ef2c06c0e1f73d2ae25c26c','addColumn tableName=RESOURCE_SERVER_POLICY; update tableName=RESOURCE_SERVER_POLICY; dropColumn columnName=DECISION_STRATEGY, tableName=RESOURCE_SERVER_POLICY; renameColumn newColumnName=DECISION_STRATEGY, oldColumnName=DECISION_STRATEGY_NEW, tabl...','',NULL,'4.23.2',NULL,NULL,'5177109193'),('22.0.0-17484-updated','keycloak','META-INF/jpa-changelog-22.0.0.xml','2024-09-01 07:52:07',114,'EXECUTED','9:90af0bfd30cafc17b9f4d6eccd92b8b3','customChange','',NULL,'4.23.2',NULL,NULL,'5177109193'),('22.0.5-24031','keycloak','META-INF/jpa-changelog-22.0.0.xml','2024-09-01 07:52:07',115,'MARK_RAN','9:a60d2d7b315ec2d3eba9e2f145f9df28','customChange','',NULL,'4.23.2',NULL,NULL,'5177109193'),('23.0.0-12062','keycloak','META-INF/jpa-changelog-23.0.0.xml','2024-09-01 07:52:07',116,'EXECUTED','9:2168fbe728fec46ae9baf15bf80927b8','addColumn tableName=COMPONENT_CONFIG; update tableName=COMPONENT_CONFIG; dropColumn columnName=VALUE, tableName=COMPONENT_CONFIG; renameColumn newColumnName=VALUE, oldColumnName=VALUE_NEW, tableName=COMPONENT_CONFIG','',NULL,'4.23.2',NULL,NULL,'5177109193'),('23.0.0-17258','keycloak','META-INF/jpa-changelog-23.0.0.xml','2024-09-01 07:52:07',117,'EXECUTED','9:36506d679a83bbfda85a27ea1864dca8','addColumn tableName=EVENT_ENTITY','',NULL,'4.23.2',NULL,NULL,'5177109193');
/*!40000 ALTER TABLE `DATABASECHANGELOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATABASECHANGELOGLOCK`
--

DROP TABLE IF EXISTS `DATABASECHANGELOGLOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DATABASECHANGELOGLOCK` (
  `ID` int NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOGLOCK`
--

LOCK TABLES `DATABASECHANGELOGLOCK` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOGLOCK` VALUES (1,_binary '\0',NULL,NULL),(1000,_binary '\0',NULL,NULL),(1001,_binary '\0',NULL,NULL);
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DEFAULT_CLIENT_SCOPE`
--

DROP TABLE IF EXISTS `DEFAULT_CLIENT_SCOPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DEFAULT_CLIENT_SCOPE` (
  `REALM_ID` varchar(36) NOT NULL,
  `SCOPE_ID` varchar(36) NOT NULL,
  `DEFAULT_SCOPE` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`REALM_ID`,`SCOPE_ID`),
  KEY `IDX_DEFCLS_REALM` (`REALM_ID`),
  KEY `IDX_DEFCLS_SCOPE` (`SCOPE_ID`),
  CONSTRAINT `FK_R_DEF_CLI_SCOPE_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DEFAULT_CLIENT_SCOPE`
--

LOCK TABLES `DEFAULT_CLIENT_SCOPE` WRITE;
/*!40000 ALTER TABLE `DEFAULT_CLIENT_SCOPE` DISABLE KEYS */;
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('8d500cba-ea97-4fd0-b969-91f305c70891','472f5149-2847-4275-9e0f-959d65d9f8d2',_binary ''),('8d500cba-ea97-4fd0-b969-91f305c70891','49ffa9a5-32c1-4cbf-ae0b-58c398ecb849',_binary '\0'),('8d500cba-ea97-4fd0-b969-91f305c70891','4cafa216-6510-43c7-a354-3f8a5d1792ea',_binary ''),('8d500cba-ea97-4fd0-b969-91f305c70891','6204369f-87f8-4310-853a-5d3e9178ce95',_binary ''),('8d500cba-ea97-4fd0-b969-91f305c70891','7612a0ea-048b-46b4-bc4f-86064f7e8160',_binary ''),('8d500cba-ea97-4fd0-b969-91f305c70891','ad6a9df2-8f36-43e3-a531-ea0a483c908c',_binary '\0'),('8d500cba-ea97-4fd0-b969-91f305c70891','b41a813d-464e-49c5-92ed-17d64f973fe7',_binary ''),('8d500cba-ea97-4fd0-b969-91f305c70891','b79a92a3-e78e-4e06-8a83-0756522c1db7',_binary ''),('8d500cba-ea97-4fd0-b969-91f305c70891','c7194588-f594-4e51-a7e6-96c6a90a8382',_binary '\0'),('8d500cba-ea97-4fd0-b969-91f305c70891','cf449fb3-60d7-4249-bb77-38a1d145f4d0',_binary ''),('8d500cba-ea97-4fd0-b969-91f305c70891','de5efa5e-bd6b-4b76-8f99-dd221f0b620f',_binary '\0'),('8d500cba-ea97-4fd0-b969-91f305c70891','e8b60846-b837-4823-906b-13b707fc2e6d',_binary '');
/*!40000 ALTER TABLE `DEFAULT_CLIENT_SCOPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EVENT_ENTITY`
--

DROP TABLE IF EXISTS `EVENT_ENTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EVENT_ENTITY` (
  `ID` varchar(36) NOT NULL,
  `CLIENT_ID` varchar(255) DEFAULT NULL,
  `DETAILS_JSON` text,
  `ERROR` varchar(255) DEFAULT NULL,
  `IP_ADDRESS` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(255) DEFAULT NULL,
  `SESSION_ID` varchar(255) DEFAULT NULL,
  `EVENT_TIME` bigint DEFAULT NULL,
  `TYPE` varchar(255) DEFAULT NULL,
  `USER_ID` varchar(255) DEFAULT NULL,
  `DETAILS_JSON_LONG_VALUE` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  PRIMARY KEY (`ID`),
  KEY `IDX_EVENT_TIME` (`REALM_ID`,`EVENT_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EVENT_ENTITY`
--

LOCK TABLES `EVENT_ENTITY` WRITE;
/*!40000 ALTER TABLE `EVENT_ENTITY` DISABLE KEYS */;
/*!40000 ALTER TABLE `EVENT_ENTITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_USER_ATTRIBUTE`
--

DROP TABLE IF EXISTS `FED_USER_ATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FED_USER_ATTRIBUTE` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) DEFAULT NULL,
  `VALUE` text,
  PRIMARY KEY (`ID`),
  KEY `IDX_FU_ATTRIBUTE` (`USER_ID`,`REALM_ID`,`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_USER_ATTRIBUTE`
--

LOCK TABLES `FED_USER_ATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `FED_USER_ATTRIBUTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_USER_ATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_USER_CONSENT`
--

DROP TABLE IF EXISTS `FED_USER_CONSENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FED_USER_CONSENT` (
  `ID` varchar(36) NOT NULL,
  `CLIENT_ID` varchar(255) DEFAULT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) DEFAULT NULL,
  `CREATED_DATE` bigint DEFAULT NULL,
  `LAST_UPDATED_DATE` bigint DEFAULT NULL,
  `CLIENT_STORAGE_PROVIDER` varchar(36) DEFAULT NULL,
  `EXTERNAL_CLIENT_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_FU_CONSENT` (`USER_ID`,`CLIENT_ID`),
  KEY `IDX_FU_CONSENT_RU` (`REALM_ID`,`USER_ID`),
  KEY `IDX_FU_CNSNT_EXT` (`USER_ID`,`CLIENT_STORAGE_PROVIDER`,`EXTERNAL_CLIENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_USER_CONSENT`
--

LOCK TABLES `FED_USER_CONSENT` WRITE;
/*!40000 ALTER TABLE `FED_USER_CONSENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_USER_CONSENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_USER_CONSENT_CL_SCOPE`
--

DROP TABLE IF EXISTS `FED_USER_CONSENT_CL_SCOPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FED_USER_CONSENT_CL_SCOPE` (
  `USER_CONSENT_ID` varchar(36) NOT NULL,
  `SCOPE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`USER_CONSENT_ID`,`SCOPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_USER_CONSENT_CL_SCOPE`
--

LOCK TABLES `FED_USER_CONSENT_CL_SCOPE` WRITE;
/*!40000 ALTER TABLE `FED_USER_CONSENT_CL_SCOPE` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_USER_CONSENT_CL_SCOPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_USER_CREDENTIAL`
--

DROP TABLE IF EXISTS `FED_USER_CREDENTIAL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FED_USER_CREDENTIAL` (
  `ID` varchar(36) NOT NULL,
  `SALT` tinyblob,
  `TYPE` varchar(255) DEFAULT NULL,
  `CREATED_DATE` bigint DEFAULT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) DEFAULT NULL,
  `USER_LABEL` varchar(255) DEFAULT NULL,
  `SECRET_DATA` longtext,
  `CREDENTIAL_DATA` longtext,
  `PRIORITY` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_FU_CREDENTIAL` (`USER_ID`,`TYPE`),
  KEY `IDX_FU_CREDENTIAL_RU` (`REALM_ID`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_USER_CREDENTIAL`
--

LOCK TABLES `FED_USER_CREDENTIAL` WRITE;
/*!40000 ALTER TABLE `FED_USER_CREDENTIAL` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_USER_CREDENTIAL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_USER_GROUP_MEMBERSHIP`
--

DROP TABLE IF EXISTS `FED_USER_GROUP_MEMBERSHIP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FED_USER_GROUP_MEMBERSHIP` (
  `GROUP_ID` varchar(36) NOT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`GROUP_ID`,`USER_ID`),
  KEY `IDX_FU_GROUP_MEMBERSHIP` (`USER_ID`,`GROUP_ID`),
  KEY `IDX_FU_GROUP_MEMBERSHIP_RU` (`REALM_ID`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_USER_GROUP_MEMBERSHIP`
--

LOCK TABLES `FED_USER_GROUP_MEMBERSHIP` WRITE;
/*!40000 ALTER TABLE `FED_USER_GROUP_MEMBERSHIP` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_USER_GROUP_MEMBERSHIP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_USER_REQUIRED_ACTION`
--

DROP TABLE IF EXISTS `FED_USER_REQUIRED_ACTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FED_USER_REQUIRED_ACTION` (
  `REQUIRED_ACTION` varchar(255) NOT NULL DEFAULT ' ',
  `USER_ID` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`REQUIRED_ACTION`,`USER_ID`),
  KEY `IDX_FU_REQUIRED_ACTION` (`USER_ID`,`REQUIRED_ACTION`),
  KEY `IDX_FU_REQUIRED_ACTION_RU` (`REALM_ID`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_USER_REQUIRED_ACTION`
--

LOCK TABLES `FED_USER_REQUIRED_ACTION` WRITE;
/*!40000 ALTER TABLE `FED_USER_REQUIRED_ACTION` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_USER_REQUIRED_ACTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_USER_ROLE_MAPPING`
--

DROP TABLE IF EXISTS `FED_USER_ROLE_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FED_USER_ROLE_MAPPING` (
  `ROLE_ID` varchar(36) NOT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ROLE_ID`,`USER_ID`),
  KEY `IDX_FU_ROLE_MAPPING` (`USER_ID`,`ROLE_ID`),
  KEY `IDX_FU_ROLE_MAPPING_RU` (`REALM_ID`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_USER_ROLE_MAPPING`
--

LOCK TABLES `FED_USER_ROLE_MAPPING` WRITE;
/*!40000 ALTER TABLE `FED_USER_ROLE_MAPPING` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_USER_ROLE_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FEDERATED_IDENTITY`
--

DROP TABLE IF EXISTS `FEDERATED_IDENTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FEDERATED_IDENTITY` (
  `IDENTITY_PROVIDER` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  `FEDERATED_USER_ID` varchar(255) DEFAULT NULL,
  `FEDERATED_USERNAME` varchar(255) DEFAULT NULL,
  `TOKEN` text,
  `USER_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`IDENTITY_PROVIDER`,`USER_ID`),
  KEY `IDX_FEDIDENTITY_USER` (`USER_ID`),
  KEY `IDX_FEDIDENTITY_FEDUSER` (`FEDERATED_USER_ID`),
  CONSTRAINT `FK404288B92EF007A6` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FEDERATED_IDENTITY`
--

LOCK TABLES `FEDERATED_IDENTITY` WRITE;
/*!40000 ALTER TABLE `FEDERATED_IDENTITY` DISABLE KEYS */;
/*!40000 ALTER TABLE `FEDERATED_IDENTITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FEDERATED_USER`
--

DROP TABLE IF EXISTS `FEDERATED_USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FEDERATED_USER` (
  `ID` varchar(255) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FEDERATED_USER`
--

LOCK TABLES `FEDERATED_USER` WRITE;
/*!40000 ALTER TABLE `FEDERATED_USER` DISABLE KEYS */;
INSERT INTO `FEDERATED_USER` VALUES ('f:970b3d3c-cbf0-4b07-8940-4094eed14b03:phucxauxi929','970b3d3c-cbf0-4b07-8940-4094eed14b03','8d500cba-ea97-4fd0-b969-91f305c70891'),('f:970b3d3c-cbf0-4b07-8940-4094eed14b03:trongphuc22153','970b3d3c-cbf0-4b07-8940-4094eed14b03','8d500cba-ea97-4fd0-b969-91f305c70891');
/*!40000 ALTER TABLE `FEDERATED_USER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GROUP_ATTRIBUTE`
--

DROP TABLE IF EXISTS `GROUP_ATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GROUP_ATTRIBUTE` (
  `ID` varchar(36) NOT NULL DEFAULT 'sybase-needs-something-here',
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `GROUP_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_GROUP_ATTR_GROUP` (`GROUP_ID`),
  KEY `IDX_GROUP_ATT_BY_NAME_VALUE` (`NAME`,`VALUE`),
  CONSTRAINT `FK_GROUP_ATTRIBUTE_GROUP` FOREIGN KEY (`GROUP_ID`) REFERENCES `KEYCLOAK_GROUP` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GROUP_ATTRIBUTE`
--

LOCK TABLES `GROUP_ATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `GROUP_ATTRIBUTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `GROUP_ATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GROUP_ROLE_MAPPING`
--

DROP TABLE IF EXISTS `GROUP_ROLE_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GROUP_ROLE_MAPPING` (
  `ROLE_ID` varchar(36) NOT NULL,
  `GROUP_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ROLE_ID`,`GROUP_ID`),
  KEY `IDX_GROUP_ROLE_MAPP_GROUP` (`GROUP_ID`),
  CONSTRAINT `FK_GROUP_ROLE_GROUP` FOREIGN KEY (`GROUP_ID`) REFERENCES `KEYCLOAK_GROUP` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GROUP_ROLE_MAPPING`
--

LOCK TABLES `GROUP_ROLE_MAPPING` WRITE;
/*!40000 ALTER TABLE `GROUP_ROLE_MAPPING` DISABLE KEYS */;
/*!40000 ALTER TABLE `GROUP_ROLE_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IDENTITY_PROVIDER`
--

DROP TABLE IF EXISTS `IDENTITY_PROVIDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IDENTITY_PROVIDER` (
  `INTERNAL_ID` varchar(36) NOT NULL,
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `PROVIDER_ALIAS` varchar(255) DEFAULT NULL,
  `PROVIDER_ID` varchar(255) DEFAULT NULL,
  `STORE_TOKEN` bit(1) NOT NULL DEFAULT b'0',
  `AUTHENTICATE_BY_DEFAULT` bit(1) NOT NULL DEFAULT b'0',
  `REALM_ID` varchar(36) DEFAULT NULL,
  `ADD_TOKEN_ROLE` bit(1) NOT NULL DEFAULT b'1',
  `TRUST_EMAIL` bit(1) NOT NULL DEFAULT b'0',
  `FIRST_BROKER_LOGIN_FLOW_ID` varchar(36) DEFAULT NULL,
  `POST_BROKER_LOGIN_FLOW_ID` varchar(36) DEFAULT NULL,
  `PROVIDER_DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `LINK_ONLY` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`INTERNAL_ID`),
  UNIQUE KEY `UK_2DAELWNIBJI49AVXSRTUF6XJ33` (`PROVIDER_ALIAS`,`REALM_ID`),
  KEY `IDX_IDENT_PROV_REALM` (`REALM_ID`),
  CONSTRAINT `FK2B4EBC52AE5C3B34` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IDENTITY_PROVIDER`
--

LOCK TABLES `IDENTITY_PROVIDER` WRITE;
/*!40000 ALTER TABLE `IDENTITY_PROVIDER` DISABLE KEYS */;
/*!40000 ALTER TABLE `IDENTITY_PROVIDER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IDENTITY_PROVIDER_CONFIG`
--

DROP TABLE IF EXISTS `IDENTITY_PROVIDER_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IDENTITY_PROVIDER_CONFIG` (
  `IDENTITY_PROVIDER_ID` varchar(36) NOT NULL,
  `VALUE` longtext,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`IDENTITY_PROVIDER_ID`,`NAME`),
  CONSTRAINT `FKDC4897CF864C4E43` FOREIGN KEY (`IDENTITY_PROVIDER_ID`) REFERENCES `IDENTITY_PROVIDER` (`INTERNAL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IDENTITY_PROVIDER_CONFIG`
--

LOCK TABLES `IDENTITY_PROVIDER_CONFIG` WRITE;
/*!40000 ALTER TABLE `IDENTITY_PROVIDER_CONFIG` DISABLE KEYS */;
/*!40000 ALTER TABLE `IDENTITY_PROVIDER_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IDENTITY_PROVIDER_MAPPER`
--

DROP TABLE IF EXISTS `IDENTITY_PROVIDER_MAPPER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IDENTITY_PROVIDER_MAPPER` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `IDP_ALIAS` varchar(255) NOT NULL,
  `IDP_MAPPER_NAME` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_ID_PROV_MAPP_REALM` (`REALM_ID`),
  CONSTRAINT `FK_IDPM_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IDENTITY_PROVIDER_MAPPER`
--

LOCK TABLES `IDENTITY_PROVIDER_MAPPER` WRITE;
/*!40000 ALTER TABLE `IDENTITY_PROVIDER_MAPPER` DISABLE KEYS */;
/*!40000 ALTER TABLE `IDENTITY_PROVIDER_MAPPER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IDP_MAPPER_CONFIG`
--

DROP TABLE IF EXISTS `IDP_MAPPER_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IDP_MAPPER_CONFIG` (
  `IDP_MAPPER_ID` varchar(36) NOT NULL,
  `VALUE` longtext,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`IDP_MAPPER_ID`,`NAME`),
  CONSTRAINT `FK_IDPMCONFIG` FOREIGN KEY (`IDP_MAPPER_ID`) REFERENCES `IDENTITY_PROVIDER_MAPPER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IDP_MAPPER_CONFIG`
--

LOCK TABLES `IDP_MAPPER_CONFIG` WRITE;
/*!40000 ALTER TABLE `IDP_MAPPER_CONFIG` DISABLE KEYS */;
/*!40000 ALTER TABLE `IDP_MAPPER_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KEYCLOAK_GROUP`
--

DROP TABLE IF EXISTS `KEYCLOAK_GROUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KEYCLOAK_GROUP` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `PARENT_GROUP` varchar(36) NOT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `SIBLING_NAMES` (`REALM_ID`,`PARENT_GROUP`,`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KEYCLOAK_GROUP`
--

LOCK TABLES `KEYCLOAK_GROUP` WRITE;
/*!40000 ALTER TABLE `KEYCLOAK_GROUP` DISABLE KEYS */;
/*!40000 ALTER TABLE `KEYCLOAK_GROUP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KEYCLOAK_ROLE`
--

DROP TABLE IF EXISTS `KEYCLOAK_ROLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KEYCLOAK_ROLE` (
  `ID` varchar(36) NOT NULL,
  `CLIENT_REALM_CONSTRAINT` varchar(255) DEFAULT NULL,
  `CLIENT_ROLE` bit(1) DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `REALM_ID` varchar(255) DEFAULT NULL,
  `CLIENT` varchar(36) DEFAULT NULL,
  `REALM` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_J3RWUVD56ONTGSUHOGM184WW2-2` (`NAME`,`CLIENT_REALM_CONSTRAINT`),
  KEY `IDX_KEYCLOAK_ROLE_CLIENT` (`CLIENT`),
  KEY `IDX_KEYCLOAK_ROLE_REALM` (`REALM`),
  CONSTRAINT `FK_6VYQFE4CN4WLQ8R6KT5VDSJ5C` FOREIGN KEY (`REALM`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KEYCLOAK_ROLE`
--

LOCK TABLES `KEYCLOAK_ROLE` WRITE;
/*!40000 ALTER TABLE `KEYCLOAK_ROLE` DISABLE KEYS */;
INSERT INTO `KEYCLOAK_ROLE` VALUES ('05465032-4fe7-4cff-8849-33351988d2d8','74fd16f8-3604-42d8-bf5f-078e3c4406d6',_binary '','${role_read-token}','read-token','8d500cba-ea97-4fd0-b969-91f305c70891','74fd16f8-3604-42d8-bf5f-078e3c4406d6',NULL),('1967cce1-ee2c-4d35-be83-7322eb97f0f6','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_query-clients}','query-clients','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('1ac7d27a-c0fb-4f69-a997-37952adfd110','eb2edf3d-204a-41e7-aff2-87136e3038bb',_binary '','${role_view-groups}','view-groups','8d500cba-ea97-4fd0-b969-91f305c70891','eb2edf3d-204a-41e7-aff2-87136e3038bb',NULL),('27a20330-784a-4d91-a48b-f9bfdf997804','eb2edf3d-204a-41e7-aff2-87136e3038bb',_binary '','${role_delete-account}','delete-account','8d500cba-ea97-4fd0-b969-91f305c70891','eb2edf3d-204a-41e7-aff2-87136e3038bb',NULL),('2a052686-e768-4ebc-85e3-070d543a16ca','eb2edf3d-204a-41e7-aff2-87136e3038bb',_binary '','${role_view-profile}','view-profile','8d500cba-ea97-4fd0-b969-91f305c70891','eb2edf3d-204a-41e7-aff2-87136e3038bb',NULL),('2e30030f-3a4d-4dcf-ae6c-ced8d873fc69','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_manage-realm}','manage-realm','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('31efdf09-0385-4af7-a701-a60afaaaa7a7','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_manage-events}','manage-events','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('3334862e-1eee-49ec-a515-7d4b38d20da3','eb2edf3d-204a-41e7-aff2-87136e3038bb',_binary '','${role_manage-account}','manage-account','8d500cba-ea97-4fd0-b969-91f305c70891','eb2edf3d-204a-41e7-aff2-87136e3038bb',NULL),('3b9c83ef-7034-4522-9c13-96795fef9a79','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_view-realm}','view-realm','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('412a0660-d29e-4316-ab35-c5321e466761','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_create-client}','create-client','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('515278f9-0215-49d0-b922-701c623574ac','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_manage-clients}','manage-clients','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('53d6b454-e873-4aea-81e7-694212aa1f91','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '\0','${role_default-roles}','default-roles-master','8d500cba-ea97-4fd0-b969-91f305c70891',NULL,NULL),('5f771b98-94a8-4e2a-9ec7-0bf047c4d11c','eb2edf3d-204a-41e7-aff2-87136e3038bb',_binary '','${role_view-consent}','view-consent','8d500cba-ea97-4fd0-b969-91f305c70891','eb2edf3d-204a-41e7-aff2-87136e3038bb',NULL),('667bce8e-6351-4c58-86b3-a623425ebec9','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_query-groups}','query-groups','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('6c15c69b-3e04-49de-8b71-a7b42f8ade9b','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_manage-identity-providers}','manage-identity-providers','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '\0','${role_admin}','admin','8d500cba-ea97-4fd0-b969-91f305c70891',NULL,NULL),('8b043554-97bd-49b0-a634-ff084beb64d4','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '\0','${role_uma_authorization}','uma_authorization','8d500cba-ea97-4fd0-b969-91f305c70891',NULL,NULL),('8e4709f6-d7c2-44f5-8614-990077e629c1','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '\0','${role_create-realm}','create-realm','8d500cba-ea97-4fd0-b969-91f305c70891',NULL,NULL),('92c91c27-395f-42d2-a332-a94b7c5c4642','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_view-identity-providers}','view-identity-providers','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('967c716d-288e-42f6-87c6-a338ab3d6cad','eb2edf3d-204a-41e7-aff2-87136e3038bb',_binary '','${role_view-applications}','view-applications','8d500cba-ea97-4fd0-b969-91f305c70891','eb2edf3d-204a-41e7-aff2-87136e3038bb',NULL),('9772e785-e437-47ab-bd65-3deabc4a8e10','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_impersonation}','impersonation','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('9ea2be33-5d1f-4d41-8e42-c79a33efb31c','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_view-authorization}','view-authorization','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('a4e23802-4deb-4217-8013-8dfe37e9c5bb','5983633b-180b-4071-bcc5-58248f3ea1b4',_binary '',NULL,'uma_protection','8d500cba-ea97-4fd0-b969-91f305c70891','5983633b-180b-4071-bcc5-58248f3ea1b4',NULL),('a528c7bf-724f-4726-87ee-be1c7ec7d9e3','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_view-users}','view-users','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('b24d7dcc-4bc8-406d-91fa-5b8b0d79c29f','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_view-clients}','view-clients','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('bd02d5d6-c7f5-4adf-9a05-f85391c8e8ce','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '\0',NULL,'CUSTOMER','8d500cba-ea97-4fd0-b969-91f305c70891',NULL,NULL),('c8c13f65-335e-42e4-8022-c48ed31b6e10','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '\0','${role_offline-access}','offline_access','8d500cba-ea97-4fd0-b969-91f305c70891',NULL,NULL),('c99e8cf9-b90d-4e33-806a-ee1afac07845','eb2edf3d-204a-41e7-aff2-87136e3038bb',_binary '','${role_manage-account-links}','manage-account-links','8d500cba-ea97-4fd0-b969-91f305c70891','eb2edf3d-204a-41e7-aff2-87136e3038bb',NULL),('e563cd41-b108-4b4b-8c22-317d7ffb823c','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_manage-users}','manage-users','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('e69b2ebb-c8e3-4c09-814a-0eca30573455','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_query-realms}','query-realms','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('e94c4f60-ed37-491a-984b-532bb0a11f6b','eb2edf3d-204a-41e7-aff2-87136e3038bb',_binary '','${role_manage-consent}','manage-consent','8d500cba-ea97-4fd0-b969-91f305c70891','eb2edf3d-204a-41e7-aff2-87136e3038bb',NULL),('ea2ff855-c87d-40a9-8bb9-9973ff77f87f','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_view-events}','view-events','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('ed287a36-9002-48c9-bceb-b9dc1c1eab3e','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_query-users}','query-users','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL),('eddeb0c7-6ec2-45c8-b29b-bc163287a89f','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '\0',NULL,'EMPLOYEE','8d500cba-ea97-4fd0-b969-91f305c70891',NULL,NULL),('fda8c8b6-9e45-4c6d-9290-bd5132b20ec7','d372473f-7d72-44b8-8301-7369776129e3',_binary '','${role_manage-authorization}','manage-authorization','8d500cba-ea97-4fd0-b969-91f305c70891','d372473f-7d72-44b8-8301-7369776129e3',NULL);
/*!40000 ALTER TABLE `KEYCLOAK_ROLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MIGRATION_MODEL`
--

DROP TABLE IF EXISTS `MIGRATION_MODEL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MIGRATION_MODEL` (
  `ID` varchar(36) NOT NULL,
  `VERSION` varchar(36) DEFAULT NULL,
  `UPDATE_TIME` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `IDX_UPDATE_TIME` (`UPDATE_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MIGRATION_MODEL`
--

LOCK TABLES `MIGRATION_MODEL` WRITE;
/*!40000 ALTER TABLE `MIGRATION_MODEL` DISABLE KEYS */;
INSERT INTO `MIGRATION_MODEL` VALUES ('uyb0z','23.0.7',1725177127);
/*!40000 ALTER TABLE `MIGRATION_MODEL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OFFLINE_CLIENT_SESSION`
--

DROP TABLE IF EXISTS `OFFLINE_CLIENT_SESSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OFFLINE_CLIENT_SESSION` (
  `USER_SESSION_ID` varchar(36) NOT NULL,
  `CLIENT_ID` varchar(255) NOT NULL,
  `OFFLINE_FLAG` varchar(4) NOT NULL,
  `TIMESTAMP` int DEFAULT NULL,
  `DATA` longtext,
  `CLIENT_STORAGE_PROVIDER` varchar(36) NOT NULL DEFAULT 'local',
  `EXTERNAL_CLIENT_ID` varchar(255) NOT NULL DEFAULT 'local',
  PRIMARY KEY (`USER_SESSION_ID`,`CLIENT_ID`,`CLIENT_STORAGE_PROVIDER`,`EXTERNAL_CLIENT_ID`,`OFFLINE_FLAG`),
  KEY `IDX_US_SESS_ID_ON_CL_SESS` (`USER_SESSION_ID`),
  KEY `IDX_OFFLINE_CSS_PRELOAD` (`CLIENT_ID`,`OFFLINE_FLAG`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OFFLINE_CLIENT_SESSION`
--

LOCK TABLES `OFFLINE_CLIENT_SESSION` WRITE;
/*!40000 ALTER TABLE `OFFLINE_CLIENT_SESSION` DISABLE KEYS */;
/*!40000 ALTER TABLE `OFFLINE_CLIENT_SESSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OFFLINE_USER_SESSION`
--

DROP TABLE IF EXISTS `OFFLINE_USER_SESSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OFFLINE_USER_SESSION` (
  `USER_SESSION_ID` varchar(36) NOT NULL,
  `USER_ID` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `CREATED_ON` int NOT NULL,
  `OFFLINE_FLAG` varchar(4) NOT NULL,
  `DATA` longtext,
  `LAST_SESSION_REFRESH` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`USER_SESSION_ID`,`OFFLINE_FLAG`),
  KEY `IDX_OFFLINE_USS_CREATEDON` (`CREATED_ON`),
  KEY `IDX_OFFLINE_USS_PRELOAD` (`OFFLINE_FLAG`,`CREATED_ON`,`USER_SESSION_ID`),
  KEY `IDX_OFFLINE_USS_BY_USER` (`USER_ID`,`REALM_ID`,`OFFLINE_FLAG`),
  KEY `IDX_OFFLINE_USS_BY_USERSESS` (`REALM_ID`,`OFFLINE_FLAG`,`USER_SESSION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OFFLINE_USER_SESSION`
--

LOCK TABLES `OFFLINE_USER_SESSION` WRITE;
/*!40000 ALTER TABLE `OFFLINE_USER_SESSION` DISABLE KEYS */;
/*!40000 ALTER TABLE `OFFLINE_USER_SESSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `POLICY_CONFIG`
--

DROP TABLE IF EXISTS `POLICY_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `POLICY_CONFIG` (
  `POLICY_ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `VALUE` longtext,
  PRIMARY KEY (`POLICY_ID`,`NAME`),
  CONSTRAINT `FKDC34197CF864C4E43` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `POLICY_CONFIG`
--

LOCK TABLES `POLICY_CONFIG` WRITE;
/*!40000 ALTER TABLE `POLICY_CONFIG` DISABLE KEYS */;
INSERT INTO `POLICY_CONFIG` VALUES ('18a31336-8c4e-4d22-bd5a-252a2498a8c4','defaultResourceType','urn:b7afe9b7-6c53-44a1-8e76-bff80f45bead:resources:default'),('96aef6b8-c5cb-4037-afa0-e2c85805836b','code','// by default, grants any permission associated with this policy\n$evaluation.grant();\n');
/*!40000 ALTER TABLE `POLICY_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROTOCOL_MAPPER`
--

DROP TABLE IF EXISTS `PROTOCOL_MAPPER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROTOCOL_MAPPER` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `PROTOCOL` varchar(255) NOT NULL,
  `PROTOCOL_MAPPER_NAME` varchar(255) NOT NULL,
  `CLIENT_ID` varchar(36) DEFAULT NULL,
  `CLIENT_SCOPE_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_PROTOCOL_MAPPER_CLIENT` (`CLIENT_ID`),
  KEY `IDX_CLSCOPE_PROTMAP` (`CLIENT_SCOPE_ID`),
  CONSTRAINT `FK_CLI_SCOPE_MAPPER` FOREIGN KEY (`CLIENT_SCOPE_ID`) REFERENCES `CLIENT_SCOPE` (`ID`),
  CONSTRAINT `FK_PCM_REALM` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROTOCOL_MAPPER`
--

LOCK TABLES `PROTOCOL_MAPPER` WRITE;
/*!40000 ALTER TABLE `PROTOCOL_MAPPER` DISABLE KEYS */;
INSERT INTO `PROTOCOL_MAPPER` VALUES ('0289dda9-3a93-4cb0-b261-aac66b50d68f','audience resolve','openid-connect','oidc-audience-resolve-mapper',NULL,'472f5149-2847-4275-9e0f-959d65d9f8d2'),('08d2155a-dc48-46ad-9a8b-1e61640c2efc','UserExternalID','openid-connect','oidc-usermodel-attribute-mapper',NULL,'6204369f-87f8-4310-853a-5d3e9178ce95'),('0d566167-0ebd-4c58-a0b6-c2c9d535b1d6','zoneinfo','openid-connect','oidc-usermodel-attribute-mapper',NULL,'cf449fb3-60d7-4249-bb77-38a1d145f4d0'),('2f899701-b61c-42ad-a8d3-bada9d42e20f','nickname','openid-connect','oidc-usermodel-attribute-mapper',NULL,'cf449fb3-60d7-4249-bb77-38a1d145f4d0'),('34d806a5-08a6-46e9-b8cc-df4392a3e833','groups','openid-connect','oidc-usermodel-realm-role-mapper',NULL,'49ffa9a5-32c1-4cbf-ae0b-58c398ecb849'),('40179a6d-5bf2-4c7b-9026-f9e868f67929','address','openid-connect','oidc-address-mapper',NULL,'c7194588-f594-4e51-a7e6-96c6a90a8382'),('4bd5b76f-6591-4c32-bc1b-10ec48f15891','Client Host','openid-connect','oidc-usersessionmodel-note-mapper','5983633b-180b-4071-bcc5-58248f3ea1b4',NULL),('50120d1f-8a19-4d43-ac40-fcd9f3c68648','audience resolve','openid-connect','oidc-audience-resolve-mapper','7391f048-5a3e-480f-8cfc-5b8d90e27912',NULL),('545cf2d3-6fc0-40d2-878e-a03264efc2ad','upn','openid-connect','oidc-usermodel-attribute-mapper',NULL,'49ffa9a5-32c1-4cbf-ae0b-58c398ecb849'),('55678a22-ec7f-4546-a599-9415dac3e08d','middle name','openid-connect','oidc-usermodel-attribute-mapper',NULL,'cf449fb3-60d7-4249-bb77-38a1d145f4d0'),('558f3fca-c32d-4879-bad3-3c6425a4cfca','updated at','openid-connect','oidc-usermodel-attribute-mapper',NULL,'cf449fb3-60d7-4249-bb77-38a1d145f4d0'),('65732c33-4a8f-4786-ad07-90ce311a04e8','gender','openid-connect','oidc-usermodel-attribute-mapper',NULL,'cf449fb3-60d7-4249-bb77-38a1d145f4d0'),('6c5ece79-2479-447e-94d7-810e3b00e661','given name','openid-connect','oidc-usermodel-attribute-mapper',NULL,'cf449fb3-60d7-4249-bb77-38a1d145f4d0'),('7e613a47-8699-4e34-90bc-d65d53b82077','client roles','openid-connect','oidc-usermodel-client-role-mapper',NULL,'472f5149-2847-4275-9e0f-959d65d9f8d2'),('a5ed6cff-6d9f-4546-897c-ffa9e0fc3c48','email','openid-connect','oidc-usermodel-attribute-mapper',NULL,'b79a92a3-e78e-4e06-8a83-0756522c1db7'),('adc2667b-1ad4-4aa6-bd54-a7ebec0de4d7','website','openid-connect','oidc-usermodel-attribute-mapper',NULL,'cf449fb3-60d7-4249-bb77-38a1d145f4d0'),('ae54dd49-188a-42e1-b997-7a661c763f5b','email verified','openid-connect','oidc-usermodel-property-mapper',NULL,'b79a92a3-e78e-4e06-8a83-0756522c1db7'),('b7695d06-a0aa-439a-a9d4-007912bd0065','locale','openid-connect','oidc-usermodel-attribute-mapper','724f9f53-5886-4a8b-b797-591ad05a9caa',NULL),('b9070393-3d14-4015-b192-04a9e9ea0c72','Client ID','openid-connect','oidc-usersessionmodel-note-mapper','5983633b-180b-4071-bcc5-58248f3ea1b4',NULL),('c0829cdb-949d-4216-bf95-f79e2070c6b9','locale','openid-connect','oidc-usermodel-attribute-mapper',NULL,'cf449fb3-60d7-4249-bb77-38a1d145f4d0'),('c244cc1c-e67e-46c3-b85e-ae2b4872336b','phone number verified','openid-connect','oidc-usermodel-attribute-mapper',NULL,'ad6a9df2-8f36-43e3-a531-ea0a483c908c'),('c653c342-316e-42b6-9089-ca960271d4e1','realm roles','openid-connect','oidc-usermodel-realm-role-mapper',NULL,'472f5149-2847-4275-9e0f-959d65d9f8d2'),('cd524eb7-0e39-4cac-aae3-b7eaaaed5d5a','picture','openid-connect','oidc-usermodel-attribute-mapper',NULL,'cf449fb3-60d7-4249-bb77-38a1d145f4d0'),('d5d7cc19-a8fc-4017-867a-fd163c8ce3ba','phone number','openid-connect','oidc-usermodel-attribute-mapper',NULL,'ad6a9df2-8f36-43e3-a531-ea0a483c908c'),('dc0a1e99-69b1-4d18-8894-4a23b090d6ae','foodshop-clientid','openid-connect','oidc-audience-mapper',NULL,'e8b60846-b837-4823-906b-13b707fc2e6d'),('e5e11c77-25b9-4afd-aac2-421f63fd760e','role list','saml','saml-role-list-mapper',NULL,'b41a813d-464e-49c5-92ed-17d64f973fe7'),('e97c8a1a-8a7e-42d0-ad87-dfe4cf6f1ecd','full name','openid-connect','oidc-full-name-mapper',NULL,'cf449fb3-60d7-4249-bb77-38a1d145f4d0'),('ea4b5fba-51b0-45ab-bd26-7af05b4ccb8d','username','openid-connect','oidc-usermodel-attribute-mapper',NULL,'cf449fb3-60d7-4249-bb77-38a1d145f4d0'),('eeaf8cbe-a7eb-45f8-a9fd-1be915bed763','profile','openid-connect','oidc-usermodel-attribute-mapper',NULL,'cf449fb3-60d7-4249-bb77-38a1d145f4d0'),('f279b2e5-17ed-4205-8a68-a1b1283896ef','family name','openid-connect','oidc-usermodel-attribute-mapper',NULL,'cf449fb3-60d7-4249-bb77-38a1d145f4d0'),('f432b31e-beb7-44d4-9218-e96754c8752a','Client IP Address','openid-connect','oidc-usersessionmodel-note-mapper','5983633b-180b-4071-bcc5-58248f3ea1b4',NULL),('fbc0de46-b3a8-4ee0-b93a-1df25120d772','birthdate','openid-connect','oidc-usermodel-attribute-mapper',NULL,'cf449fb3-60d7-4249-bb77-38a1d145f4d0'),('fd329ef4-f9dd-4ca4-af59-af90c06c8d31','acr loa level','openid-connect','oidc-acr-mapper',NULL,'4cafa216-6510-43c7-a354-3f8a5d1792ea'),('febdfd9a-350a-4896-a476-397c5b4b4a7e','allowed web origins','openid-connect','oidc-allowed-origins-mapper',NULL,'7612a0ea-048b-46b4-bc4f-86064f7e8160');
/*!40000 ALTER TABLE `PROTOCOL_MAPPER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROTOCOL_MAPPER_CONFIG`
--

DROP TABLE IF EXISTS `PROTOCOL_MAPPER_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROTOCOL_MAPPER_CONFIG` (
  `PROTOCOL_MAPPER_ID` varchar(36) NOT NULL,
  `VALUE` longtext,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`PROTOCOL_MAPPER_ID`,`NAME`),
  CONSTRAINT `FK_PMCONFIG` FOREIGN KEY (`PROTOCOL_MAPPER_ID`) REFERENCES `PROTOCOL_MAPPER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROTOCOL_MAPPER_CONFIG`
--

LOCK TABLES `PROTOCOL_MAPPER_CONFIG` WRITE;
/*!40000 ALTER TABLE `PROTOCOL_MAPPER_CONFIG` DISABLE KEYS */;
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('0289dda9-3a93-4cb0-b261-aac66b50d68f','true','access.token.claim'),('0289dda9-3a93-4cb0-b261-aac66b50d68f','true','introspection.token.claim'),('08d2155a-dc48-46ad-9a8b-1e61640c2efc','true','access.token.claim'),('08d2155a-dc48-46ad-9a8b-1e61640c2efc','sub','claim.name'),('08d2155a-dc48-46ad-9a8b-1e61640c2efc','true','id.token.claim'),('08d2155a-dc48-46ad-9a8b-1e61640c2efc','true','introspection.token.claim'),('08d2155a-dc48-46ad-9a8b-1e61640c2efc','String','jsonType.label'),('08d2155a-dc48-46ad-9a8b-1e61640c2efc','userexID','user.attribute'),('08d2155a-dc48-46ad-9a8b-1e61640c2efc','true','userinfo.token.claim'),('0d566167-0ebd-4c58-a0b6-c2c9d535b1d6','true','access.token.claim'),('0d566167-0ebd-4c58-a0b6-c2c9d535b1d6','zoneinfo','claim.name'),('0d566167-0ebd-4c58-a0b6-c2c9d535b1d6','true','id.token.claim'),('0d566167-0ebd-4c58-a0b6-c2c9d535b1d6','true','introspection.token.claim'),('0d566167-0ebd-4c58-a0b6-c2c9d535b1d6','String','jsonType.label'),('0d566167-0ebd-4c58-a0b6-c2c9d535b1d6','zoneinfo','user.attribute'),('0d566167-0ebd-4c58-a0b6-c2c9d535b1d6','true','userinfo.token.claim'),('2f899701-b61c-42ad-a8d3-bada9d42e20f','true','access.token.claim'),('2f899701-b61c-42ad-a8d3-bada9d42e20f','nickname','claim.name'),('2f899701-b61c-42ad-a8d3-bada9d42e20f','true','id.token.claim'),('2f899701-b61c-42ad-a8d3-bada9d42e20f','true','introspection.token.claim'),('2f899701-b61c-42ad-a8d3-bada9d42e20f','String','jsonType.label'),('2f899701-b61c-42ad-a8d3-bada9d42e20f','nickname','user.attribute'),('2f899701-b61c-42ad-a8d3-bada9d42e20f','true','userinfo.token.claim'),('34d806a5-08a6-46e9-b8cc-df4392a3e833','true','access.token.claim'),('34d806a5-08a6-46e9-b8cc-df4392a3e833','groups','claim.name'),('34d806a5-08a6-46e9-b8cc-df4392a3e833','true','id.token.claim'),('34d806a5-08a6-46e9-b8cc-df4392a3e833','true','introspection.token.claim'),('34d806a5-08a6-46e9-b8cc-df4392a3e833','String','jsonType.label'),('34d806a5-08a6-46e9-b8cc-df4392a3e833','true','multivalued'),('34d806a5-08a6-46e9-b8cc-df4392a3e833','foo','user.attribute'),('40179a6d-5bf2-4c7b-9026-f9e868f67929','true','access.token.claim'),('40179a6d-5bf2-4c7b-9026-f9e868f67929','true','id.token.claim'),('40179a6d-5bf2-4c7b-9026-f9e868f67929','true','introspection.token.claim'),('40179a6d-5bf2-4c7b-9026-f9e868f67929','country','user.attribute.country'),('40179a6d-5bf2-4c7b-9026-f9e868f67929','formatted','user.attribute.formatted'),('40179a6d-5bf2-4c7b-9026-f9e868f67929','locality','user.attribute.locality'),('40179a6d-5bf2-4c7b-9026-f9e868f67929','postal_code','user.attribute.postal_code'),('40179a6d-5bf2-4c7b-9026-f9e868f67929','region','user.attribute.region'),('40179a6d-5bf2-4c7b-9026-f9e868f67929','street','user.attribute.street'),('40179a6d-5bf2-4c7b-9026-f9e868f67929','true','userinfo.token.claim'),('4bd5b76f-6591-4c32-bc1b-10ec48f15891','true','access.token.claim'),('4bd5b76f-6591-4c32-bc1b-10ec48f15891','clientHost','claim.name'),('4bd5b76f-6591-4c32-bc1b-10ec48f15891','true','id.token.claim'),('4bd5b76f-6591-4c32-bc1b-10ec48f15891','true','introspection.token.claim'),('4bd5b76f-6591-4c32-bc1b-10ec48f15891','String','jsonType.label'),('4bd5b76f-6591-4c32-bc1b-10ec48f15891','clientHost','user.session.note'),('545cf2d3-6fc0-40d2-878e-a03264efc2ad','true','access.token.claim'),('545cf2d3-6fc0-40d2-878e-a03264efc2ad','upn','claim.name'),('545cf2d3-6fc0-40d2-878e-a03264efc2ad','true','id.token.claim'),('545cf2d3-6fc0-40d2-878e-a03264efc2ad','true','introspection.token.claim'),('545cf2d3-6fc0-40d2-878e-a03264efc2ad','String','jsonType.label'),('545cf2d3-6fc0-40d2-878e-a03264efc2ad','username','user.attribute'),('545cf2d3-6fc0-40d2-878e-a03264efc2ad','true','userinfo.token.claim'),('55678a22-ec7f-4546-a599-9415dac3e08d','true','access.token.claim'),('55678a22-ec7f-4546-a599-9415dac3e08d','middle_name','claim.name'),('55678a22-ec7f-4546-a599-9415dac3e08d','true','id.token.claim'),('55678a22-ec7f-4546-a599-9415dac3e08d','true','introspection.token.claim'),('55678a22-ec7f-4546-a599-9415dac3e08d','String','jsonType.label'),('55678a22-ec7f-4546-a599-9415dac3e08d','middleName','user.attribute'),('55678a22-ec7f-4546-a599-9415dac3e08d','true','userinfo.token.claim'),('558f3fca-c32d-4879-bad3-3c6425a4cfca','true','access.token.claim'),('558f3fca-c32d-4879-bad3-3c6425a4cfca','updated_at','claim.name'),('558f3fca-c32d-4879-bad3-3c6425a4cfca','true','id.token.claim'),('558f3fca-c32d-4879-bad3-3c6425a4cfca','true','introspection.token.claim'),('558f3fca-c32d-4879-bad3-3c6425a4cfca','long','jsonType.label'),('558f3fca-c32d-4879-bad3-3c6425a4cfca','updatedAt','user.attribute'),('558f3fca-c32d-4879-bad3-3c6425a4cfca','true','userinfo.token.claim'),('65732c33-4a8f-4786-ad07-90ce311a04e8','true','access.token.claim'),('65732c33-4a8f-4786-ad07-90ce311a04e8','gender','claim.name'),('65732c33-4a8f-4786-ad07-90ce311a04e8','true','id.token.claim'),('65732c33-4a8f-4786-ad07-90ce311a04e8','true','introspection.token.claim'),('65732c33-4a8f-4786-ad07-90ce311a04e8','String','jsonType.label'),('65732c33-4a8f-4786-ad07-90ce311a04e8','gender','user.attribute'),('65732c33-4a8f-4786-ad07-90ce311a04e8','true','userinfo.token.claim'),('6c5ece79-2479-447e-94d7-810e3b00e661','true','access.token.claim'),('6c5ece79-2479-447e-94d7-810e3b00e661','given_name','claim.name'),('6c5ece79-2479-447e-94d7-810e3b00e661','true','id.token.claim'),('6c5ece79-2479-447e-94d7-810e3b00e661','true','introspection.token.claim'),('6c5ece79-2479-447e-94d7-810e3b00e661','String','jsonType.label'),('6c5ece79-2479-447e-94d7-810e3b00e661','firstName','user.attribute'),('6c5ece79-2479-447e-94d7-810e3b00e661','true','userinfo.token.claim'),('7e613a47-8699-4e34-90bc-d65d53b82077','true','access.token.claim'),('7e613a47-8699-4e34-90bc-d65d53b82077','resource_access.${client_id}.roles','claim.name'),('7e613a47-8699-4e34-90bc-d65d53b82077','true','introspection.token.claim'),('7e613a47-8699-4e34-90bc-d65d53b82077','String','jsonType.label'),('7e613a47-8699-4e34-90bc-d65d53b82077','true','multivalued'),('7e613a47-8699-4e34-90bc-d65d53b82077','foo','user.attribute'),('a5ed6cff-6d9f-4546-897c-ffa9e0fc3c48','true','access.token.claim'),('a5ed6cff-6d9f-4546-897c-ffa9e0fc3c48','email','claim.name'),('a5ed6cff-6d9f-4546-897c-ffa9e0fc3c48','true','id.token.claim'),('a5ed6cff-6d9f-4546-897c-ffa9e0fc3c48','true','introspection.token.claim'),('a5ed6cff-6d9f-4546-897c-ffa9e0fc3c48','String','jsonType.label'),('a5ed6cff-6d9f-4546-897c-ffa9e0fc3c48','email','user.attribute'),('a5ed6cff-6d9f-4546-897c-ffa9e0fc3c48','true','userinfo.token.claim'),('adc2667b-1ad4-4aa6-bd54-a7ebec0de4d7','true','access.token.claim'),('adc2667b-1ad4-4aa6-bd54-a7ebec0de4d7','website','claim.name'),('adc2667b-1ad4-4aa6-bd54-a7ebec0de4d7','true','id.token.claim'),('adc2667b-1ad4-4aa6-bd54-a7ebec0de4d7','true','introspection.token.claim'),('adc2667b-1ad4-4aa6-bd54-a7ebec0de4d7','String','jsonType.label'),('adc2667b-1ad4-4aa6-bd54-a7ebec0de4d7','website','user.attribute'),('adc2667b-1ad4-4aa6-bd54-a7ebec0de4d7','true','userinfo.token.claim'),('ae54dd49-188a-42e1-b997-7a661c763f5b','true','access.token.claim'),('ae54dd49-188a-42e1-b997-7a661c763f5b','email_verified','claim.name'),('ae54dd49-188a-42e1-b997-7a661c763f5b','true','id.token.claim'),('ae54dd49-188a-42e1-b997-7a661c763f5b','true','introspection.token.claim'),('ae54dd49-188a-42e1-b997-7a661c763f5b','boolean','jsonType.label'),('ae54dd49-188a-42e1-b997-7a661c763f5b','emailVerified','user.attribute'),('ae54dd49-188a-42e1-b997-7a661c763f5b','true','userinfo.token.claim'),('b7695d06-a0aa-439a-a9d4-007912bd0065','true','access.token.claim'),('b7695d06-a0aa-439a-a9d4-007912bd0065','locale','claim.name'),('b7695d06-a0aa-439a-a9d4-007912bd0065','true','id.token.claim'),('b7695d06-a0aa-439a-a9d4-007912bd0065','true','introspection.token.claim'),('b7695d06-a0aa-439a-a9d4-007912bd0065','String','jsonType.label'),('b7695d06-a0aa-439a-a9d4-007912bd0065','locale','user.attribute'),('b7695d06-a0aa-439a-a9d4-007912bd0065','true','userinfo.token.claim'),('b9070393-3d14-4015-b192-04a9e9ea0c72','true','access.token.claim'),('b9070393-3d14-4015-b192-04a9e9ea0c72','client_id','claim.name'),('b9070393-3d14-4015-b192-04a9e9ea0c72','true','id.token.claim'),('b9070393-3d14-4015-b192-04a9e9ea0c72','true','introspection.token.claim'),('b9070393-3d14-4015-b192-04a9e9ea0c72','String','jsonType.label'),('b9070393-3d14-4015-b192-04a9e9ea0c72','client_id','user.session.note'),('c0829cdb-949d-4216-bf95-f79e2070c6b9','true','access.token.claim'),('c0829cdb-949d-4216-bf95-f79e2070c6b9','locale','claim.name'),('c0829cdb-949d-4216-bf95-f79e2070c6b9','true','id.token.claim'),('c0829cdb-949d-4216-bf95-f79e2070c6b9','true','introspection.token.claim'),('c0829cdb-949d-4216-bf95-f79e2070c6b9','String','jsonType.label'),('c0829cdb-949d-4216-bf95-f79e2070c6b9','locale','user.attribute'),('c0829cdb-949d-4216-bf95-f79e2070c6b9','true','userinfo.token.claim'),('c244cc1c-e67e-46c3-b85e-ae2b4872336b','true','access.token.claim'),('c244cc1c-e67e-46c3-b85e-ae2b4872336b','phone_number_verified','claim.name'),('c244cc1c-e67e-46c3-b85e-ae2b4872336b','true','id.token.claim'),('c244cc1c-e67e-46c3-b85e-ae2b4872336b','true','introspection.token.claim'),('c244cc1c-e67e-46c3-b85e-ae2b4872336b','boolean','jsonType.label'),('c244cc1c-e67e-46c3-b85e-ae2b4872336b','phoneNumberVerified','user.attribute'),('c244cc1c-e67e-46c3-b85e-ae2b4872336b','true','userinfo.token.claim'),('c653c342-316e-42b6-9089-ca960271d4e1','true','access.token.claim'),('c653c342-316e-42b6-9089-ca960271d4e1','realm_access.roles','claim.name'),('c653c342-316e-42b6-9089-ca960271d4e1','true','introspection.token.claim'),('c653c342-316e-42b6-9089-ca960271d4e1','String','jsonType.label'),('c653c342-316e-42b6-9089-ca960271d4e1','true','multivalued'),('c653c342-316e-42b6-9089-ca960271d4e1','foo','user.attribute'),('cd524eb7-0e39-4cac-aae3-b7eaaaed5d5a','true','access.token.claim'),('cd524eb7-0e39-4cac-aae3-b7eaaaed5d5a','picture','claim.name'),('cd524eb7-0e39-4cac-aae3-b7eaaaed5d5a','true','id.token.claim'),('cd524eb7-0e39-4cac-aae3-b7eaaaed5d5a','true','introspection.token.claim'),('cd524eb7-0e39-4cac-aae3-b7eaaaed5d5a','String','jsonType.label'),('cd524eb7-0e39-4cac-aae3-b7eaaaed5d5a','picture','user.attribute'),('cd524eb7-0e39-4cac-aae3-b7eaaaed5d5a','true','userinfo.token.claim'),('d5d7cc19-a8fc-4017-867a-fd163c8ce3ba','true','access.token.claim'),('d5d7cc19-a8fc-4017-867a-fd163c8ce3ba','phone_number','claim.name'),('d5d7cc19-a8fc-4017-867a-fd163c8ce3ba','true','id.token.claim'),('d5d7cc19-a8fc-4017-867a-fd163c8ce3ba','true','introspection.token.claim'),('d5d7cc19-a8fc-4017-867a-fd163c8ce3ba','String','jsonType.label'),('d5d7cc19-a8fc-4017-867a-fd163c8ce3ba','phoneNumber','user.attribute'),('d5d7cc19-a8fc-4017-867a-fd163c8ce3ba','true','userinfo.token.claim'),('dc0a1e99-69b1-4d18-8894-4a23b090d6ae','true','access.token.claim'),('dc0a1e99-69b1-4d18-8894-4a23b090d6ae','true','id.token.claim'),('dc0a1e99-69b1-4d18-8894-4a23b090d6ae','b7afe9b7-6c53-44a1-8e76-bff80f45bead','included.client.audience'),('dc0a1e99-69b1-4d18-8894-4a23b090d6ae','true','introspection.token.claim'),('e5e11c77-25b9-4afd-aac2-421f63fd760e','Role','attribute.name'),('e5e11c77-25b9-4afd-aac2-421f63fd760e','Basic','attribute.nameformat'),('e5e11c77-25b9-4afd-aac2-421f63fd760e','false','single'),('e97c8a1a-8a7e-42d0-ad87-dfe4cf6f1ecd','true','access.token.claim'),('e97c8a1a-8a7e-42d0-ad87-dfe4cf6f1ecd','true','id.token.claim'),('e97c8a1a-8a7e-42d0-ad87-dfe4cf6f1ecd','true','introspection.token.claim'),('e97c8a1a-8a7e-42d0-ad87-dfe4cf6f1ecd','true','userinfo.token.claim'),('ea4b5fba-51b0-45ab-bd26-7af05b4ccb8d','true','access.token.claim'),('ea4b5fba-51b0-45ab-bd26-7af05b4ccb8d','preferred_username','claim.name'),('ea4b5fba-51b0-45ab-bd26-7af05b4ccb8d','true','id.token.claim'),('ea4b5fba-51b0-45ab-bd26-7af05b4ccb8d','true','introspection.token.claim'),('ea4b5fba-51b0-45ab-bd26-7af05b4ccb8d','String','jsonType.label'),('ea4b5fba-51b0-45ab-bd26-7af05b4ccb8d','username','user.attribute'),('ea4b5fba-51b0-45ab-bd26-7af05b4ccb8d','true','userinfo.token.claim'),('eeaf8cbe-a7eb-45f8-a9fd-1be915bed763','true','access.token.claim'),('eeaf8cbe-a7eb-45f8-a9fd-1be915bed763','profile','claim.name'),('eeaf8cbe-a7eb-45f8-a9fd-1be915bed763','true','id.token.claim'),('eeaf8cbe-a7eb-45f8-a9fd-1be915bed763','true','introspection.token.claim'),('eeaf8cbe-a7eb-45f8-a9fd-1be915bed763','String','jsonType.label'),('eeaf8cbe-a7eb-45f8-a9fd-1be915bed763','profile','user.attribute'),('eeaf8cbe-a7eb-45f8-a9fd-1be915bed763','true','userinfo.token.claim'),('f279b2e5-17ed-4205-8a68-a1b1283896ef','true','access.token.claim'),('f279b2e5-17ed-4205-8a68-a1b1283896ef','family_name','claim.name'),('f279b2e5-17ed-4205-8a68-a1b1283896ef','true','id.token.claim'),('f279b2e5-17ed-4205-8a68-a1b1283896ef','true','introspection.token.claim'),('f279b2e5-17ed-4205-8a68-a1b1283896ef','String','jsonType.label'),('f279b2e5-17ed-4205-8a68-a1b1283896ef','lastName','user.attribute'),('f279b2e5-17ed-4205-8a68-a1b1283896ef','true','userinfo.token.claim'),('f432b31e-beb7-44d4-9218-e96754c8752a','true','access.token.claim'),('f432b31e-beb7-44d4-9218-e96754c8752a','clientAddress','claim.name'),('f432b31e-beb7-44d4-9218-e96754c8752a','true','id.token.claim'),('f432b31e-beb7-44d4-9218-e96754c8752a','true','introspection.token.claim'),('f432b31e-beb7-44d4-9218-e96754c8752a','String','jsonType.label'),('f432b31e-beb7-44d4-9218-e96754c8752a','clientAddress','user.session.note'),('fbc0de46-b3a8-4ee0-b93a-1df25120d772','true','access.token.claim'),('fbc0de46-b3a8-4ee0-b93a-1df25120d772','birthdate','claim.name'),('fbc0de46-b3a8-4ee0-b93a-1df25120d772','true','id.token.claim'),('fbc0de46-b3a8-4ee0-b93a-1df25120d772','true','introspection.token.claim'),('fbc0de46-b3a8-4ee0-b93a-1df25120d772','String','jsonType.label'),('fbc0de46-b3a8-4ee0-b93a-1df25120d772','birthdate','user.attribute'),('fbc0de46-b3a8-4ee0-b93a-1df25120d772','true','userinfo.token.claim'),('fd329ef4-f9dd-4ca4-af59-af90c06c8d31','true','access.token.claim'),('fd329ef4-f9dd-4ca4-af59-af90c06c8d31','true','id.token.claim'),('fd329ef4-f9dd-4ca4-af59-af90c06c8d31','true','introspection.token.claim'),('febdfd9a-350a-4896-a476-397c5b4b4a7e','true','access.token.claim'),('febdfd9a-350a-4896-a476-397c5b4b4a7e','true','introspection.token.claim');
/*!40000 ALTER TABLE `PROTOCOL_MAPPER_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM`
--

DROP TABLE IF EXISTS `REALM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REALM` (
  `ID` varchar(36) NOT NULL,
  `ACCESS_CODE_LIFESPAN` int DEFAULT NULL,
  `USER_ACTION_LIFESPAN` int DEFAULT NULL,
  `ACCESS_TOKEN_LIFESPAN` int DEFAULT NULL,
  `ACCOUNT_THEME` varchar(255) DEFAULT NULL,
  `ADMIN_THEME` varchar(255) DEFAULT NULL,
  `EMAIL_THEME` varchar(255) DEFAULT NULL,
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `EVENTS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `EVENTS_EXPIRATION` bigint DEFAULT NULL,
  `LOGIN_THEME` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `NOT_BEFORE` int DEFAULT NULL,
  `PASSWORD_POLICY` text,
  `REGISTRATION_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `REMEMBER_ME` bit(1) NOT NULL DEFAULT b'0',
  `RESET_PASSWORD_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `SOCIAL` bit(1) NOT NULL DEFAULT b'0',
  `SSL_REQUIRED` varchar(255) DEFAULT NULL,
  `SSO_IDLE_TIMEOUT` int DEFAULT NULL,
  `SSO_MAX_LIFESPAN` int DEFAULT NULL,
  `UPDATE_PROFILE_ON_SOC_LOGIN` bit(1) NOT NULL DEFAULT b'0',
  `VERIFY_EMAIL` bit(1) NOT NULL DEFAULT b'0',
  `MASTER_ADMIN_CLIENT` varchar(36) DEFAULT NULL,
  `LOGIN_LIFESPAN` int DEFAULT NULL,
  `INTERNATIONALIZATION_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `DEFAULT_LOCALE` varchar(255) DEFAULT NULL,
  `REG_EMAIL_AS_USERNAME` bit(1) NOT NULL DEFAULT b'0',
  `ADMIN_EVENTS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `ADMIN_EVENTS_DETAILS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `EDIT_USERNAME_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `OTP_POLICY_COUNTER` int DEFAULT '0',
  `OTP_POLICY_WINDOW` int DEFAULT '1',
  `OTP_POLICY_PERIOD` int DEFAULT '30',
  `OTP_POLICY_DIGITS` int DEFAULT '6',
  `OTP_POLICY_ALG` varchar(36) DEFAULT 'HmacSHA1',
  `OTP_POLICY_TYPE` varchar(36) DEFAULT 'totp',
  `BROWSER_FLOW` varchar(36) DEFAULT NULL,
  `REGISTRATION_FLOW` varchar(36) DEFAULT NULL,
  `DIRECT_GRANT_FLOW` varchar(36) DEFAULT NULL,
  `RESET_CREDENTIALS_FLOW` varchar(36) DEFAULT NULL,
  `CLIENT_AUTH_FLOW` varchar(36) DEFAULT NULL,
  `OFFLINE_SESSION_IDLE_TIMEOUT` int DEFAULT '0',
  `REVOKE_REFRESH_TOKEN` bit(1) NOT NULL DEFAULT b'0',
  `ACCESS_TOKEN_LIFE_IMPLICIT` int DEFAULT '0',
  `LOGIN_WITH_EMAIL_ALLOWED` bit(1) NOT NULL DEFAULT b'1',
  `DUPLICATE_EMAILS_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `DOCKER_AUTH_FLOW` varchar(36) DEFAULT NULL,
  `REFRESH_TOKEN_MAX_REUSE` int DEFAULT '0',
  `ALLOW_USER_MANAGED_ACCESS` bit(1) NOT NULL DEFAULT b'0',
  `SSO_MAX_LIFESPAN_REMEMBER_ME` int NOT NULL,
  `SSO_IDLE_TIMEOUT_REMEMBER_ME` int NOT NULL,
  `DEFAULT_ROLE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_ORVSDMLA56612EAEFIQ6WL5OI` (`NAME`),
  KEY `IDX_REALM_MASTER_ADM_CLI` (`MASTER_ADMIN_CLIENT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM`
--

LOCK TABLES `REALM` WRITE;
/*!40000 ALTER TABLE `REALM` DISABLE KEYS */;
INSERT INTO `REALM` VALUES ('8d500cba-ea97-4fd0-b969-91f305c70891',60,300,1800,NULL,NULL,NULL,_binary '',_binary '\0',0,NULL,'master',0,'hashAlgorithm(bcrypt)',_binary '',_binary '',_binary '\0',_binary '\0','EXTERNAL',3600,10800,_binary '\0',_binary '','d372473f-7d72-44b8-8301-7369776129e3',1800,_binary '\0',NULL,_binary '\0',_binary '\0',_binary '\0',_binary '\0',0,1,30,6,'HmacSHA1','totp','d4922d03-1495-4786-94bc-29798e5fabba','670cd7c2-219c-45c4-b7e0-7169bb82c932','0a656aa2-c9c4-46ec-a570-f51b934e2b96','541f3d44-53cb-439b-949d-b701210d1e7b','6364010b-a2a2-42fc-81d0-b64d9aa2f80d',2592000,_binary '',900,_binary '\0',_binary '\0','e6831c4b-a6a0-4554-807a-3ac47ce86e2d',2,_binary '\0',0,0,'53d6b454-e873-4aea-81e7-694212aa1f91');
/*!40000 ALTER TABLE `REALM` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_ATTRIBUTE`
--

DROP TABLE IF EXISTS `REALM_ATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REALM_ATTRIBUTE` (
  `NAME` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `VALUE` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  PRIMARY KEY (`NAME`,`REALM_ID`),
  KEY `IDX_REALM_ATTR_REALM` (`REALM_ID`),
  CONSTRAINT `FK_8SHXD6L3E9ATQUKACXGPFFPTW` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_ATTRIBUTE`
--

LOCK TABLES `REALM_ATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `REALM_ATTRIBUTE` DISABLE KEYS */;
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.contentSecurityPolicy','8d500cba-ea97-4fd0-b969-91f305c70891','frame-src \'self\'; frame-ancestors \'self\'; object-src \'none\';'),('_browser_header.contentSecurityPolicyReportOnly','8d500cba-ea97-4fd0-b969-91f305c70891',''),('_browser_header.referrerPolicy','8d500cba-ea97-4fd0-b969-91f305c70891','no-referrer'),('_browser_header.strictTransportSecurity','8d500cba-ea97-4fd0-b969-91f305c70891','max-age=31536000; includeSubDomains'),('_browser_header.xContentTypeOptions','8d500cba-ea97-4fd0-b969-91f305c70891','nosniff'),('_browser_header.xFrameOptions','8d500cba-ea97-4fd0-b969-91f305c70891','SAMEORIGIN'),('_browser_header.xRobotsTag','8d500cba-ea97-4fd0-b969-91f305c70891','none'),('_browser_header.xXSSProtection','8d500cba-ea97-4fd0-b969-91f305c70891','1; mode=block'),('acr.loa.map','8d500cba-ea97-4fd0-b969-91f305c70891','{}'),('actionTokenGeneratedByAdminLifespan','8d500cba-ea97-4fd0-b969-91f305c70891','43200'),('actionTokenGeneratedByUserLifespan','8d500cba-ea97-4fd0-b969-91f305c70891','300'),('actionTokenGeneratedByUserLifespan.execute-actions','8d500cba-ea97-4fd0-b969-91f305c70891',''),('actionTokenGeneratedByUserLifespan.idp-verify-account-via-email','8d500cba-ea97-4fd0-b969-91f305c70891',''),('actionTokenGeneratedByUserLifespan.reset-credentials','8d500cba-ea97-4fd0-b969-91f305c70891',''),('actionTokenGeneratedByUserLifespan.verify-email','8d500cba-ea97-4fd0-b969-91f305c70891',''),('bruteForceProtected','8d500cba-ea97-4fd0-b969-91f305c70891','false'),('cibaAuthRequestedUserHint','8d500cba-ea97-4fd0-b969-91f305c70891','login_hint'),('cibaBackchannelTokenDeliveryMode','8d500cba-ea97-4fd0-b969-91f305c70891','poll'),('cibaExpiresIn','8d500cba-ea97-4fd0-b969-91f305c70891','120'),('cibaInterval','8d500cba-ea97-4fd0-b969-91f305c70891','5'),('client-policies.policies','8d500cba-ea97-4fd0-b969-91f305c70891','{\"policies\":[]}'),('client-policies.profiles','8d500cba-ea97-4fd0-b969-91f305c70891','{\"profiles\":[]}'),('clientOfflineSessionIdleTimeout','8d500cba-ea97-4fd0-b969-91f305c70891','0'),('clientOfflineSessionMaxLifespan','8d500cba-ea97-4fd0-b969-91f305c70891','0'),('clientSessionIdleTimeout','8d500cba-ea97-4fd0-b969-91f305c70891','3600'),('clientSessionMaxLifespan','8d500cba-ea97-4fd0-b969-91f305c70891','10800'),('defaultSignatureAlgorithm','8d500cba-ea97-4fd0-b969-91f305c70891','RS256'),('displayName','8d500cba-ea97-4fd0-b969-91f305c70891','Keycloak'),('displayNameHtml','8d500cba-ea97-4fd0-b969-91f305c70891','<div class=\"kc-logo-text\"><span>Keycloak</span></div>'),('failureFactor','8d500cba-ea97-4fd0-b969-91f305c70891','30'),('frontendUrl','8d500cba-ea97-4fd0-b969-91f305c70891','http://localhost:8080'),('maxDeltaTimeSeconds','8d500cba-ea97-4fd0-b969-91f305c70891','43200'),('maxFailureWaitSeconds','8d500cba-ea97-4fd0-b969-91f305c70891','900'),('minimumQuickLoginWaitSeconds','8d500cba-ea97-4fd0-b969-91f305c70891','60'),('oauth2DeviceCodeLifespan','8d500cba-ea97-4fd0-b969-91f305c70891','600'),('oauth2DevicePollingInterval','8d500cba-ea97-4fd0-b969-91f305c70891','5'),('offlineSessionMaxLifespan','8d500cba-ea97-4fd0-b969-91f305c70891','5184000'),('offlineSessionMaxLifespanEnabled','8d500cba-ea97-4fd0-b969-91f305c70891','false'),('parRequestUriLifespan','8d500cba-ea97-4fd0-b969-91f305c70891','60'),('permanentLockout','8d500cba-ea97-4fd0-b969-91f305c70891','false'),('quickLoginCheckMilliSeconds','8d500cba-ea97-4fd0-b969-91f305c70891','1000'),('realmReusableOtpCode','8d500cba-ea97-4fd0-b969-91f305c70891','false'),('shortVerificationUri','8d500cba-ea97-4fd0-b969-91f305c70891',''),('waitIncrementSeconds','8d500cba-ea97-4fd0-b969-91f305c70891','60'),('webAuthnPolicyAttestationConveyancePreference','8d500cba-ea97-4fd0-b969-91f305c70891','not specified'),('webAuthnPolicyAttestationConveyancePreferencePasswordless','8d500cba-ea97-4fd0-b969-91f305c70891','not specified'),('webAuthnPolicyAuthenticatorAttachment','8d500cba-ea97-4fd0-b969-91f305c70891','not specified'),('webAuthnPolicyAuthenticatorAttachmentPasswordless','8d500cba-ea97-4fd0-b969-91f305c70891','not specified'),('webAuthnPolicyAvoidSameAuthenticatorRegister','8d500cba-ea97-4fd0-b969-91f305c70891','false'),('webAuthnPolicyAvoidSameAuthenticatorRegisterPasswordless','8d500cba-ea97-4fd0-b969-91f305c70891','false'),('webAuthnPolicyCreateTimeout','8d500cba-ea97-4fd0-b969-91f305c70891','0'),('webAuthnPolicyCreateTimeoutPasswordless','8d500cba-ea97-4fd0-b969-91f305c70891','0'),('webAuthnPolicyRequireResidentKey','8d500cba-ea97-4fd0-b969-91f305c70891','not specified'),('webAuthnPolicyRequireResidentKeyPasswordless','8d500cba-ea97-4fd0-b969-91f305c70891','not specified'),('webAuthnPolicyRpEntityName','8d500cba-ea97-4fd0-b969-91f305c70891','keycloak'),('webAuthnPolicyRpEntityNamePasswordless','8d500cba-ea97-4fd0-b969-91f305c70891','keycloak'),('webAuthnPolicyRpId','8d500cba-ea97-4fd0-b969-91f305c70891',''),('webAuthnPolicyRpIdPasswordless','8d500cba-ea97-4fd0-b969-91f305c70891',''),('webAuthnPolicySignatureAlgorithms','8d500cba-ea97-4fd0-b969-91f305c70891','ES256'),('webAuthnPolicySignatureAlgorithmsPasswordless','8d500cba-ea97-4fd0-b969-91f305c70891','ES256'),('webAuthnPolicyUserVerificationRequirement','8d500cba-ea97-4fd0-b969-91f305c70891','not specified'),('webAuthnPolicyUserVerificationRequirementPasswordless','8d500cba-ea97-4fd0-b969-91f305c70891','not specified');
/*!40000 ALTER TABLE `REALM_ATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_DEFAULT_GROUPS`
--

DROP TABLE IF EXISTS `REALM_DEFAULT_GROUPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REALM_DEFAULT_GROUPS` (
  `REALM_ID` varchar(36) NOT NULL,
  `GROUP_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`REALM_ID`,`GROUP_ID`),
  UNIQUE KEY `CON_GROUP_ID_DEF_GROUPS` (`GROUP_ID`),
  KEY `IDX_REALM_DEF_GRP_REALM` (`REALM_ID`),
  CONSTRAINT `FK_DEF_GROUPS_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_DEFAULT_GROUPS`
--

LOCK TABLES `REALM_DEFAULT_GROUPS` WRITE;
/*!40000 ALTER TABLE `REALM_DEFAULT_GROUPS` DISABLE KEYS */;
/*!40000 ALTER TABLE `REALM_DEFAULT_GROUPS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_ENABLED_EVENT_TYPES`
--

DROP TABLE IF EXISTS `REALM_ENABLED_EVENT_TYPES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REALM_ENABLED_EVENT_TYPES` (
  `REALM_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  PRIMARY KEY (`REALM_ID`,`VALUE`),
  KEY `IDX_REALM_EVT_TYPES_REALM` (`REALM_ID`),
  CONSTRAINT `FK_H846O4H0W8EPX5NWEDRF5Y69J` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_ENABLED_EVENT_TYPES`
--

LOCK TABLES `REALM_ENABLED_EVENT_TYPES` WRITE;
/*!40000 ALTER TABLE `REALM_ENABLED_EVENT_TYPES` DISABLE KEYS */;
INSERT INTO `REALM_ENABLED_EVENT_TYPES` VALUES ('8d500cba-ea97-4fd0-b969-91f305c70891','AUTHREQID_TO_TOKEN'),('8d500cba-ea97-4fd0-b969-91f305c70891','AUTHREQID_TO_TOKEN_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','CLIENT_DELETE'),('8d500cba-ea97-4fd0-b969-91f305c70891','CLIENT_DELETE_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','CLIENT_INITIATED_ACCOUNT_LINKING'),('8d500cba-ea97-4fd0-b969-91f305c70891','CLIENT_INITIATED_ACCOUNT_LINKING_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','CLIENT_LOGIN'),('8d500cba-ea97-4fd0-b969-91f305c70891','CLIENT_LOGIN_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','CLIENT_REGISTER'),('8d500cba-ea97-4fd0-b969-91f305c70891','CLIENT_REGISTER_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','CLIENT_UPDATE'),('8d500cba-ea97-4fd0-b969-91f305c70891','CLIENT_UPDATE_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','CODE_TO_TOKEN'),('8d500cba-ea97-4fd0-b969-91f305c70891','CODE_TO_TOKEN_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','CUSTOM_REQUIRED_ACTION'),('8d500cba-ea97-4fd0-b969-91f305c70891','CUSTOM_REQUIRED_ACTION_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','DELETE_ACCOUNT'),('8d500cba-ea97-4fd0-b969-91f305c70891','DELETE_ACCOUNT_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','EXECUTE_ACTION_TOKEN'),('8d500cba-ea97-4fd0-b969-91f305c70891','EXECUTE_ACTION_TOKEN_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','EXECUTE_ACTIONS'),('8d500cba-ea97-4fd0-b969-91f305c70891','EXECUTE_ACTIONS_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','FEDERATED_IDENTITY_LINK'),('8d500cba-ea97-4fd0-b969-91f305c70891','FEDERATED_IDENTITY_LINK_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','GRANT_CONSENT'),('8d500cba-ea97-4fd0-b969-91f305c70891','GRANT_CONSENT_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','IDENTITY_PROVIDER_FIRST_LOGIN'),('8d500cba-ea97-4fd0-b969-91f305c70891','IDENTITY_PROVIDER_FIRST_LOGIN_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','IDENTITY_PROVIDER_LINK_ACCOUNT'),('8d500cba-ea97-4fd0-b969-91f305c70891','IDENTITY_PROVIDER_LINK_ACCOUNT_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','IDENTITY_PROVIDER_POST_LOGIN'),('8d500cba-ea97-4fd0-b969-91f305c70891','IDENTITY_PROVIDER_POST_LOGIN_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','IMPERSONATE'),('8d500cba-ea97-4fd0-b969-91f305c70891','IMPERSONATE_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','LOGIN'),('8d500cba-ea97-4fd0-b969-91f305c70891','LOGIN_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','LOGOUT'),('8d500cba-ea97-4fd0-b969-91f305c70891','LOGOUT_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','OAUTH2_DEVICE_AUTH'),('8d500cba-ea97-4fd0-b969-91f305c70891','OAUTH2_DEVICE_AUTH_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','OAUTH2_DEVICE_CODE_TO_TOKEN'),('8d500cba-ea97-4fd0-b969-91f305c70891','OAUTH2_DEVICE_CODE_TO_TOKEN_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','OAUTH2_DEVICE_VERIFY_USER_CODE'),('8d500cba-ea97-4fd0-b969-91f305c70891','OAUTH2_DEVICE_VERIFY_USER_CODE_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','PERMISSION_TOKEN'),('8d500cba-ea97-4fd0-b969-91f305c70891','REGISTER'),('8d500cba-ea97-4fd0-b969-91f305c70891','REGISTER_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','REMOVE_FEDERATED_IDENTITY'),('8d500cba-ea97-4fd0-b969-91f305c70891','REMOVE_FEDERATED_IDENTITY_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','REMOVE_TOTP'),('8d500cba-ea97-4fd0-b969-91f305c70891','REMOVE_TOTP_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','RESET_PASSWORD'),('8d500cba-ea97-4fd0-b969-91f305c70891','RESET_PASSWORD_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','RESTART_AUTHENTICATION'),('8d500cba-ea97-4fd0-b969-91f305c70891','RESTART_AUTHENTICATION_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','REVOKE_GRANT'),('8d500cba-ea97-4fd0-b969-91f305c70891','REVOKE_GRANT_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','SEND_IDENTITY_PROVIDER_LINK'),('8d500cba-ea97-4fd0-b969-91f305c70891','SEND_IDENTITY_PROVIDER_LINK_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','SEND_RESET_PASSWORD'),('8d500cba-ea97-4fd0-b969-91f305c70891','SEND_RESET_PASSWORD_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','SEND_VERIFY_EMAIL'),('8d500cba-ea97-4fd0-b969-91f305c70891','SEND_VERIFY_EMAIL_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','TOKEN_EXCHANGE'),('8d500cba-ea97-4fd0-b969-91f305c70891','TOKEN_EXCHANGE_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','UPDATE_CONSENT'),('8d500cba-ea97-4fd0-b969-91f305c70891','UPDATE_CONSENT_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','UPDATE_EMAIL'),('8d500cba-ea97-4fd0-b969-91f305c70891','UPDATE_EMAIL_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','UPDATE_PASSWORD'),('8d500cba-ea97-4fd0-b969-91f305c70891','UPDATE_PASSWORD_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','UPDATE_PROFILE'),('8d500cba-ea97-4fd0-b969-91f305c70891','UPDATE_PROFILE_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','UPDATE_TOTP'),('8d500cba-ea97-4fd0-b969-91f305c70891','UPDATE_TOTP_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','USER_DISABLED_BY_PERMANENT_LOCKOUT'),('8d500cba-ea97-4fd0-b969-91f305c70891','VERIFY_EMAIL'),('8d500cba-ea97-4fd0-b969-91f305c70891','VERIFY_EMAIL_ERROR'),('8d500cba-ea97-4fd0-b969-91f305c70891','VERIFY_PROFILE'),('8d500cba-ea97-4fd0-b969-91f305c70891','VERIFY_PROFILE_ERROR');
/*!40000 ALTER TABLE `REALM_ENABLED_EVENT_TYPES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_EVENTS_LISTENERS`
--

DROP TABLE IF EXISTS `REALM_EVENTS_LISTENERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REALM_EVENTS_LISTENERS` (
  `REALM_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  PRIMARY KEY (`REALM_ID`,`VALUE`),
  KEY `IDX_REALM_EVT_LIST_REALM` (`REALM_ID`),
  CONSTRAINT `FK_H846O4H0W8EPX5NXEV9F5Y69J` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_EVENTS_LISTENERS`
--

LOCK TABLES `REALM_EVENTS_LISTENERS` WRITE;
/*!40000 ALTER TABLE `REALM_EVENTS_LISTENERS` DISABLE KEYS */;
INSERT INTO `REALM_EVENTS_LISTENERS` VALUES ('8d500cba-ea97-4fd0-b969-91f305c70891','custom-event-listener'),('8d500cba-ea97-4fd0-b969-91f305c70891','jboss-logging');
/*!40000 ALTER TABLE `REALM_EVENTS_LISTENERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_LOCALIZATIONS`
--

DROP TABLE IF EXISTS `REALM_LOCALIZATIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REALM_LOCALIZATIONS` (
  `REALM_ID` varchar(255) NOT NULL,
  `LOCALE` varchar(255) NOT NULL,
  `TEXTS` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`REALM_ID`,`LOCALE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_LOCALIZATIONS`
--

LOCK TABLES `REALM_LOCALIZATIONS` WRITE;
/*!40000 ALTER TABLE `REALM_LOCALIZATIONS` DISABLE KEYS */;
/*!40000 ALTER TABLE `REALM_LOCALIZATIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_REQUIRED_CREDENTIAL`
--

DROP TABLE IF EXISTS `REALM_REQUIRED_CREDENTIAL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REALM_REQUIRED_CREDENTIAL` (
  `TYPE` varchar(255) NOT NULL,
  `FORM_LABEL` varchar(255) DEFAULT NULL,
  `INPUT` bit(1) NOT NULL DEFAULT b'0',
  `SECRET` bit(1) NOT NULL DEFAULT b'0',
  `REALM_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`REALM_ID`,`TYPE`),
  CONSTRAINT `FK_5HG65LYBEVAVKQFKI3KPONH9V` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_REQUIRED_CREDENTIAL`
--

LOCK TABLES `REALM_REQUIRED_CREDENTIAL` WRITE;
/*!40000 ALTER TABLE `REALM_REQUIRED_CREDENTIAL` DISABLE KEYS */;
INSERT INTO `REALM_REQUIRED_CREDENTIAL` VALUES ('password','password',_binary '',_binary '','8d500cba-ea97-4fd0-b969-91f305c70891');
/*!40000 ALTER TABLE `REALM_REQUIRED_CREDENTIAL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_SMTP_CONFIG`
--

DROP TABLE IF EXISTS `REALM_SMTP_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REALM_SMTP_CONFIG` (
  `REALM_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`REALM_ID`,`NAME`),
  CONSTRAINT `FK_70EJ8XDXGXD0B9HH6180IRR0O` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_SMTP_CONFIG`
--

LOCK TABLES `REALM_SMTP_CONFIG` WRITE;
/*!40000 ALTER TABLE `REALM_SMTP_CONFIG` DISABLE KEYS */;
INSERT INTO `REALM_SMTP_CONFIG` VALUES ('8d500cba-ea97-4fd0-b969-91f305c70891','false','auth'),('8d500cba-ea97-4fd0-b969-91f305c70891','','envelopeFrom'),('8d500cba-ea97-4fd0-b969-91f305c70891','jackson22153@gmail.com','from'),('8d500cba-ea97-4fd0-b969-91f305c70891','no-reply','fromDisplayName'),('8d500cba-ea97-4fd0-b969-91f305c70891','smtp.gmail.com','host'),('8d500cba-ea97-4fd0-b969-91f305c70891','','password'),('8d500cba-ea97-4fd0-b969-91f305c70891','587','port'),('8d500cba-ea97-4fd0-b969-91f305c70891','','replyTo'),('8d500cba-ea97-4fd0-b969-91f305c70891','','replyToDisplayName'),('8d500cba-ea97-4fd0-b969-91f305c70891','false','ssl'),('8d500cba-ea97-4fd0-b969-91f305c70891','false','starttls'),('8d500cba-ea97-4fd0-b969-91f305c70891','','user');
/*!40000 ALTER TABLE `REALM_SMTP_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_SUPPORTED_LOCALES`
--

DROP TABLE IF EXISTS `REALM_SUPPORTED_LOCALES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REALM_SUPPORTED_LOCALES` (
  `REALM_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  PRIMARY KEY (`REALM_ID`,`VALUE`),
  KEY `IDX_REALM_SUPP_LOCAL_REALM` (`REALM_ID`),
  CONSTRAINT `FK_SUPPORTED_LOCALES_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_SUPPORTED_LOCALES`
--

LOCK TABLES `REALM_SUPPORTED_LOCALES` WRITE;
/*!40000 ALTER TABLE `REALM_SUPPORTED_LOCALES` DISABLE KEYS */;
/*!40000 ALTER TABLE `REALM_SUPPORTED_LOCALES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REDIRECT_URIS`
--

DROP TABLE IF EXISTS `REDIRECT_URIS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REDIRECT_URIS` (
  `CLIENT_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  PRIMARY KEY (`CLIENT_ID`,`VALUE`),
  KEY `IDX_REDIR_URI_CLIENT` (`CLIENT_ID`),
  CONSTRAINT `FK_1BURS8PB4OUJ97H5WUPPAHV9F` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REDIRECT_URIS`
--

LOCK TABLES `REDIRECT_URIS` WRITE;
/*!40000 ALTER TABLE `REDIRECT_URIS` DISABLE KEYS */;
INSERT INTO `REDIRECT_URIS` VALUES ('5983633b-180b-4071-bcc5-58248f3ea1b4','http://localhost:4180/oauth2/callback/*'),('724f9f53-5886-4a8b-b797-591ad05a9caa','/admin/master/console/*'),('7391f048-5a3e-480f-8cfc-5b8d90e27912','/realms/master/account/*'),('eb2edf3d-204a-41e7-aff2-87136e3038bb','/realms/master/account/*');
/*!40000 ALTER TABLE `REDIRECT_URIS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REQUIRED_ACTION_CONFIG`
--

DROP TABLE IF EXISTS `REQUIRED_ACTION_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REQUIRED_ACTION_CONFIG` (
  `REQUIRED_ACTION_ID` varchar(36) NOT NULL,
  `VALUE` longtext,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`REQUIRED_ACTION_ID`,`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REQUIRED_ACTION_CONFIG`
--

LOCK TABLES `REQUIRED_ACTION_CONFIG` WRITE;
/*!40000 ALTER TABLE `REQUIRED_ACTION_CONFIG` DISABLE KEYS */;
/*!40000 ALTER TABLE `REQUIRED_ACTION_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REQUIRED_ACTION_PROVIDER`
--

DROP TABLE IF EXISTS `REQUIRED_ACTION_PROVIDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REQUIRED_ACTION_PROVIDER` (
  `ID` varchar(36) NOT NULL,
  `ALIAS` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `DEFAULT_ACTION` bit(1) NOT NULL DEFAULT b'0',
  `PROVIDER_ID` varchar(255) DEFAULT NULL,
  `PRIORITY` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_REQ_ACT_PROV_REALM` (`REALM_ID`),
  CONSTRAINT `FK_REQ_ACT_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REQUIRED_ACTION_PROVIDER`
--

LOCK TABLES `REQUIRED_ACTION_PROVIDER` WRITE;
/*!40000 ALTER TABLE `REQUIRED_ACTION_PROVIDER` DISABLE KEYS */;
INSERT INTO `REQUIRED_ACTION_PROVIDER` VALUES ('9c3216bf-a68a-465f-b12d-929ba19f4c4a','TERMS_AND_CONDITIONS','Terms and Conditions','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '\0',_binary '\0','TERMS_AND_CONDITIONS',20),('a3c2875c-97a4-468d-9d81-26ba926c4abb','VERIFY_EMAIL','Verify Email','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '',_binary '\0','VERIFY_EMAIL',50),('bf2afbdb-ac98-4224-a7b9-0b087feb86c9','webauthn-register','Webauthn Register','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '',_binary '\0','webauthn-register',70),('d41173fd-d691-4556-bcdd-c8e071f45d5a','webauthn-register-passwordless','Webauthn Register Passwordless','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '',_binary '\0','webauthn-register-passwordless',80),('e113b4ce-245e-47fd-ab99-83b8d7381053','UPDATE_PROFILE','Update Profile','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '',_binary '\0','UPDATE_PROFILE',40),('e82b1b66-4c34-4cd0-adee-e7ba1e9099e3','delete_account','Delete Account','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '\0',_binary '\0','delete_account',60),('f81b702a-a23f-4abe-b6f7-d2ebb1132ae9','UPDATE_PASSWORD','Update Password','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '',_binary '\0','UPDATE_PASSWORD',30),('f98ab6de-826f-411a-845f-6561d5757853','CONFIGURE_TOTP','Configure OTP','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '',_binary '\0','CONFIGURE_TOTP',10),('fc210738-9d6b-48da-9fce-d49bc84b1d44','update_user_locale','Update User Locale','8d500cba-ea97-4fd0-b969-91f305c70891',_binary '',_binary '\0','update_user_locale',1000);
/*!40000 ALTER TABLE `REQUIRED_ACTION_PROVIDER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_ATTRIBUTE`
--

DROP TABLE IF EXISTS `RESOURCE_ATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESOURCE_ATTRIBUTE` (
  `ID` varchar(36) NOT NULL DEFAULT 'sybase-needs-something-here',
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `RESOURCE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_5HRM2VLF9QL5FU022KQEPOVBR` (`RESOURCE_ID`),
  CONSTRAINT `FK_5HRM2VLF9QL5FU022KQEPOVBR` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_ATTRIBUTE`
--

LOCK TABLES `RESOURCE_ATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `RESOURCE_ATTRIBUTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `RESOURCE_ATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_POLICY`
--

DROP TABLE IF EXISTS `RESOURCE_POLICY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESOURCE_POLICY` (
  `RESOURCE_ID` varchar(36) NOT NULL,
  `POLICY_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`RESOURCE_ID`,`POLICY_ID`),
  KEY `IDX_RES_POLICY_POLICY` (`POLICY_ID`),
  CONSTRAINT `FK_FRSRPOS53XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`),
  CONSTRAINT `FK_FRSRPP213XCX4WNKOG82SSRFY` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_POLICY`
--

LOCK TABLES `RESOURCE_POLICY` WRITE;
/*!40000 ALTER TABLE `RESOURCE_POLICY` DISABLE KEYS */;
/*!40000 ALTER TABLE `RESOURCE_POLICY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_SCOPE`
--

DROP TABLE IF EXISTS `RESOURCE_SCOPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESOURCE_SCOPE` (
  `RESOURCE_ID` varchar(36) NOT NULL,
  `SCOPE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`RESOURCE_ID`,`SCOPE_ID`),
  KEY `IDX_RES_SCOPE_SCOPE` (`SCOPE_ID`),
  CONSTRAINT `FK_FRSRPOS13XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`),
  CONSTRAINT `FK_FRSRPS213XCX4WNKOG82SSRFY` FOREIGN KEY (`SCOPE_ID`) REFERENCES `RESOURCE_SERVER_SCOPE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_SCOPE`
--

LOCK TABLES `RESOURCE_SCOPE` WRITE;
/*!40000 ALTER TABLE `RESOURCE_SCOPE` DISABLE KEYS */;
/*!40000 ALTER TABLE `RESOURCE_SCOPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_SERVER`
--

DROP TABLE IF EXISTS `RESOURCE_SERVER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESOURCE_SERVER` (
  `ID` varchar(36) NOT NULL,
  `ALLOW_RS_REMOTE_MGMT` bit(1) NOT NULL DEFAULT b'0',
  `POLICY_ENFORCE_MODE` tinyint DEFAULT NULL,
  `DECISION_STRATEGY` tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_SERVER`
--

LOCK TABLES `RESOURCE_SERVER` WRITE;
/*!40000 ALTER TABLE `RESOURCE_SERVER` DISABLE KEYS */;
INSERT INTO `RESOURCE_SERVER` VALUES ('5983633b-180b-4071-bcc5-58248f3ea1b4',_binary '',0,1);
/*!40000 ALTER TABLE `RESOURCE_SERVER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_SERVER_PERM_TICKET`
--

DROP TABLE IF EXISTS `RESOURCE_SERVER_PERM_TICKET`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESOURCE_SERVER_PERM_TICKET` (
  `ID` varchar(36) NOT NULL,
  `OWNER` varchar(255) DEFAULT NULL,
  `REQUESTER` varchar(255) DEFAULT NULL,
  `CREATED_TIMESTAMP` bigint NOT NULL,
  `GRANTED_TIMESTAMP` bigint DEFAULT NULL,
  `RESOURCE_ID` varchar(36) NOT NULL,
  `SCOPE_ID` varchar(36) DEFAULT NULL,
  `RESOURCE_SERVER_ID` varchar(36) NOT NULL,
  `POLICY_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_FRSR6T700S9V50BU18WS5PMT` (`OWNER`,`REQUESTER`,`RESOURCE_SERVER_ID`,`RESOURCE_ID`,`SCOPE_ID`),
  KEY `FK_FRSRHO213XCX4WNKOG82SSPMT` (`RESOURCE_SERVER_ID`),
  KEY `FK_FRSRHO213XCX4WNKOG83SSPMT` (`RESOURCE_ID`),
  KEY `FK_FRSRHO213XCX4WNKOG84SSPMT` (`SCOPE_ID`),
  KEY `FK_FRSRPO2128CX4WNKOG82SSRFY` (`POLICY_ID`),
  CONSTRAINT `FK_FRSRHO213XCX4WNKOG82SSPMT` FOREIGN KEY (`RESOURCE_SERVER_ID`) REFERENCES `RESOURCE_SERVER` (`ID`),
  CONSTRAINT `FK_FRSRHO213XCX4WNKOG83SSPMT` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`),
  CONSTRAINT `FK_FRSRHO213XCX4WNKOG84SSPMT` FOREIGN KEY (`SCOPE_ID`) REFERENCES `RESOURCE_SERVER_SCOPE` (`ID`),
  CONSTRAINT `FK_FRSRPO2128CX4WNKOG82SSRFY` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_SERVER_PERM_TICKET`
--

LOCK TABLES `RESOURCE_SERVER_PERM_TICKET` WRITE;
/*!40000 ALTER TABLE `RESOURCE_SERVER_PERM_TICKET` DISABLE KEYS */;
/*!40000 ALTER TABLE `RESOURCE_SERVER_PERM_TICKET` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_SERVER_POLICY`
--

DROP TABLE IF EXISTS `RESOURCE_SERVER_POLICY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESOURCE_SERVER_POLICY` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `TYPE` varchar(255) NOT NULL,
  `DECISION_STRATEGY` tinyint DEFAULT NULL,
  `LOGIC` tinyint DEFAULT NULL,
  `RESOURCE_SERVER_ID` varchar(36) DEFAULT NULL,
  `OWNER` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_FRSRPT700S9V50BU18WS5HA6` (`NAME`,`RESOURCE_SERVER_ID`),
  KEY `IDX_RES_SERV_POL_RES_SERV` (`RESOURCE_SERVER_ID`),
  CONSTRAINT `FK_FRSRPO213XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_SERVER_ID`) REFERENCES `RESOURCE_SERVER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_SERVER_POLICY`
--

LOCK TABLES `RESOURCE_SERVER_POLICY` WRITE;
/*!40000 ALTER TABLE `RESOURCE_SERVER_POLICY` DISABLE KEYS */;
INSERT INTO `RESOURCE_SERVER_POLICY` VALUES ('18a31336-8c4e-4d22-bd5a-252a2498a8c4','Default Permission','A permission that applies to the default resource type','resource',1,0,'5983633b-180b-4071-bcc5-58248f3ea1b4',NULL),('96aef6b8-c5cb-4037-afa0-e2c85805836b','Default Policy','A policy that grants access only for users within this realm','js',0,0,'5983633b-180b-4071-bcc5-58248f3ea1b4',NULL);
/*!40000 ALTER TABLE `RESOURCE_SERVER_POLICY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_SERVER_RESOURCE`
--

DROP TABLE IF EXISTS `RESOURCE_SERVER_RESOURCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESOURCE_SERVER_RESOURCE` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `TYPE` varchar(255) DEFAULT NULL,
  `ICON_URI` varchar(255) DEFAULT NULL,
  `OWNER` varchar(255) DEFAULT NULL,
  `RESOURCE_SERVER_ID` varchar(36) DEFAULT NULL,
  `OWNER_MANAGED_ACCESS` bit(1) NOT NULL DEFAULT b'0',
  `DISPLAY_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_FRSR6T700S9V50BU18WS5HA6` (`NAME`,`OWNER`,`RESOURCE_SERVER_ID`),
  KEY `IDX_RES_SRV_RES_RES_SRV` (`RESOURCE_SERVER_ID`),
  CONSTRAINT `FK_FRSRHO213XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_SERVER_ID`) REFERENCES `RESOURCE_SERVER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_SERVER_RESOURCE`
--

LOCK TABLES `RESOURCE_SERVER_RESOURCE` WRITE;
/*!40000 ALTER TABLE `RESOURCE_SERVER_RESOURCE` DISABLE KEYS */;
INSERT INTO `RESOURCE_SERVER_RESOURCE` VALUES ('f1b92966-f93e-4290-97e0-196d18dc7c35','Default Resource','urn:b7afe9b7-6c53-44a1-8e76-bff80f45bead:resources:default',NULL,'5983633b-180b-4071-bcc5-58248f3ea1b4','5983633b-180b-4071-bcc5-58248f3ea1b4',_binary '\0',NULL);
/*!40000 ALTER TABLE `RESOURCE_SERVER_RESOURCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_SERVER_SCOPE`
--

DROP TABLE IF EXISTS `RESOURCE_SERVER_SCOPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESOURCE_SERVER_SCOPE` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `ICON_URI` varchar(255) DEFAULT NULL,
  `RESOURCE_SERVER_ID` varchar(36) DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_FRSRST700S9V50BU18WS5HA6` (`NAME`,`RESOURCE_SERVER_ID`),
  KEY `IDX_RES_SRV_SCOPE_RES_SRV` (`RESOURCE_SERVER_ID`),
  CONSTRAINT `FK_FRSRSO213XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_SERVER_ID`) REFERENCES `RESOURCE_SERVER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_SERVER_SCOPE`
--

LOCK TABLES `RESOURCE_SERVER_SCOPE` WRITE;
/*!40000 ALTER TABLE `RESOURCE_SERVER_SCOPE` DISABLE KEYS */;
/*!40000 ALTER TABLE `RESOURCE_SERVER_SCOPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_URIS`
--

DROP TABLE IF EXISTS `RESOURCE_URIS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESOURCE_URIS` (
  `RESOURCE_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  PRIMARY KEY (`RESOURCE_ID`,`VALUE`),
  CONSTRAINT `FK_RESOURCE_SERVER_URIS` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_URIS`
--

LOCK TABLES `RESOURCE_URIS` WRITE;
/*!40000 ALTER TABLE `RESOURCE_URIS` DISABLE KEYS */;
INSERT INTO `RESOURCE_URIS` VALUES ('f1b92966-f93e-4290-97e0-196d18dc7c35','/*');
/*!40000 ALTER TABLE `RESOURCE_URIS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ROLE_ATTRIBUTE`
--

DROP TABLE IF EXISTS `ROLE_ATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ROLE_ATTRIBUTE` (
  `ID` varchar(36) NOT NULL,
  `ROLE_ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_ROLE_ATTRIBUTE` (`ROLE_ID`),
  CONSTRAINT `FK_ROLE_ATTRIBUTE_ID` FOREIGN KEY (`ROLE_ID`) REFERENCES `KEYCLOAK_ROLE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ROLE_ATTRIBUTE`
--

LOCK TABLES `ROLE_ATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `ROLE_ATTRIBUTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `ROLE_ATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SCOPE_MAPPING`
--

DROP TABLE IF EXISTS `SCOPE_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SCOPE_MAPPING` (
  `CLIENT_ID` varchar(36) NOT NULL,
  `ROLE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`CLIENT_ID`,`ROLE_ID`),
  KEY `IDX_SCOPE_MAPPING_ROLE` (`ROLE_ID`),
  CONSTRAINT `FK_OUSE064PLMLR732LXJCN1Q5F1` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SCOPE_MAPPING`
--

LOCK TABLES `SCOPE_MAPPING` WRITE;
/*!40000 ALTER TABLE `SCOPE_MAPPING` DISABLE KEYS */;
INSERT INTO `SCOPE_MAPPING` VALUES ('7391f048-5a3e-480f-8cfc-5b8d90e27912','1ac7d27a-c0fb-4f69-a997-37952adfd110'),('7391f048-5a3e-480f-8cfc-5b8d90e27912','3334862e-1eee-49ec-a515-7d4b38d20da3');
/*!40000 ALTER TABLE `SCOPE_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SCOPE_POLICY`
--

DROP TABLE IF EXISTS `SCOPE_POLICY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SCOPE_POLICY` (
  `SCOPE_ID` varchar(36) NOT NULL,
  `POLICY_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`SCOPE_ID`,`POLICY_ID`),
  KEY `IDX_SCOPE_POLICY_POLICY` (`POLICY_ID`),
  CONSTRAINT `FK_FRSRASP13XCX4WNKOG82SSRFY` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`),
  CONSTRAINT `FK_FRSRPASS3XCX4WNKOG82SSRFY` FOREIGN KEY (`SCOPE_ID`) REFERENCES `RESOURCE_SERVER_SCOPE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SCOPE_POLICY`
--

LOCK TABLES `SCOPE_POLICY` WRITE;
/*!40000 ALTER TABLE `SCOPE_POLICY` DISABLE KEYS */;
/*!40000 ALTER TABLE `SCOPE_POLICY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_ATTRIBUTE`
--

DROP TABLE IF EXISTS `USER_ATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_ATTRIBUTE` (
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `USER_ID` varchar(36) NOT NULL,
  `ID` varchar(36) NOT NULL DEFAULT 'sybase-needs-something-here',
  PRIMARY KEY (`ID`),
  KEY `IDX_USER_ATTRIBUTE` (`USER_ID`),
  KEY `IDX_USER_ATTRIBUTE_NAME` (`NAME`,`VALUE`),
  CONSTRAINT `FK_5HRM2VLF9QL5FU043KQEPOVBR` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_ATTRIBUTE`
--

LOCK TABLES `USER_ATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `USER_ATTRIBUTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_ATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_CONSENT`
--

DROP TABLE IF EXISTS `USER_CONSENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_CONSENT` (
  `ID` varchar(36) NOT NULL,
  `CLIENT_ID` varchar(255) DEFAULT NULL,
  `USER_ID` varchar(36) NOT NULL,
  `CREATED_DATE` bigint DEFAULT NULL,
  `LAST_UPDATED_DATE` bigint DEFAULT NULL,
  `CLIENT_STORAGE_PROVIDER` varchar(36) DEFAULT NULL,
  `EXTERNAL_CLIENT_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_JKUWUVD56ONTGSUHOGM8UEWRT` (`CLIENT_ID`,`CLIENT_STORAGE_PROVIDER`,`EXTERNAL_CLIENT_ID`,`USER_ID`),
  KEY `IDX_USER_CONSENT` (`USER_ID`),
  CONSTRAINT `FK_GRNTCSNT_USER` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_CONSENT`
--

LOCK TABLES `USER_CONSENT` WRITE;
/*!40000 ALTER TABLE `USER_CONSENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_CONSENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_CONSENT_CLIENT_SCOPE`
--

DROP TABLE IF EXISTS `USER_CONSENT_CLIENT_SCOPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_CONSENT_CLIENT_SCOPE` (
  `USER_CONSENT_ID` varchar(36) NOT NULL,
  `SCOPE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`USER_CONSENT_ID`,`SCOPE_ID`),
  KEY `IDX_USCONSENT_CLSCOPE` (`USER_CONSENT_ID`),
  CONSTRAINT `FK_GRNTCSNT_CLSC_USC` FOREIGN KEY (`USER_CONSENT_ID`) REFERENCES `USER_CONSENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_CONSENT_CLIENT_SCOPE`
--

LOCK TABLES `USER_CONSENT_CLIENT_SCOPE` WRITE;
/*!40000 ALTER TABLE `USER_CONSENT_CLIENT_SCOPE` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_CONSENT_CLIENT_SCOPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_ENTITY`
--

DROP TABLE IF EXISTS `USER_ENTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_ENTITY` (
  `ID` varchar(36) NOT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `EMAIL_CONSTRAINT` varchar(255) DEFAULT NULL,
  `EMAIL_VERIFIED` bit(1) NOT NULL DEFAULT b'0',
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `FEDERATION_LINK` varchar(255) DEFAULT NULL,
  `FIRST_NAME` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `LAST_NAME` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `REALM_ID` varchar(255) DEFAULT NULL,
  `USERNAME` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `CREATED_TIMESTAMP` bigint DEFAULT NULL,
  `SERVICE_ACCOUNT_CLIENT_LINK` varchar(255) DEFAULT NULL,
  `NOT_BEFORE` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_DYKN684SL8UP1CRFEI6ECKHD7` (`REALM_ID`,`EMAIL_CONSTRAINT`),
  UNIQUE KEY `UK_RU8TT6T700S9V50BU18WS5HA6` (`REALM_ID`,`USERNAME`),
  KEY `IDX_USER_EMAIL` (`EMAIL`),
  KEY `IDX_USER_SERVICE_ACCOUNT` (`REALM_ID`,`SERVICE_ACCOUNT_CLIENT_LINK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_ENTITY`
--

LOCK TABLES `USER_ENTITY` WRITE;
/*!40000 ALTER TABLE `USER_ENTITY` DISABLE KEYS */;
INSERT INTO `USER_ENTITY` VALUES ('a659ef18-6b57-41fd-8a5a-f69d912943de','phucxauxi9292@gmail.com','phucxauxi9292@gmail.com',_binary '',_binary '',NULL,NULL,NULL,'8d500cba-ea97-4fd0-b969-91f305c70891','admin',1725177129977,NULL,0),('dc8d0878-f73e-4aae-ab23-212f6c0e50e8',NULL,'75a0115c-ada0-46fc-b7d8-97aed72a09d6',_binary '\0',_binary '',NULL,NULL,NULL,'8d500cba-ea97-4fd0-b969-91f305c70891','service-account-b7afe9b7-6c53-44a1-8e76-bff80f45bead',1725359487382,'5983633b-180b-4071-bcc5-58248f3ea1b4',0);
/*!40000 ALTER TABLE `USER_ENTITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_FEDERATION_CONFIG`
--

DROP TABLE IF EXISTS `USER_FEDERATION_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_FEDERATION_CONFIG` (
  `USER_FEDERATION_PROVIDER_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`USER_FEDERATION_PROVIDER_ID`,`NAME`),
  CONSTRAINT `FK_T13HPU1J94R2EBPEKR39X5EU5` FOREIGN KEY (`USER_FEDERATION_PROVIDER_ID`) REFERENCES `USER_FEDERATION_PROVIDER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_FEDERATION_CONFIG`
--

LOCK TABLES `USER_FEDERATION_CONFIG` WRITE;
/*!40000 ALTER TABLE `USER_FEDERATION_CONFIG` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_FEDERATION_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_FEDERATION_MAPPER`
--

DROP TABLE IF EXISTS `USER_FEDERATION_MAPPER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_FEDERATION_MAPPER` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `FEDERATION_PROVIDER_ID` varchar(36) NOT NULL,
  `FEDERATION_MAPPER_TYPE` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_USR_FED_MAP_FED_PRV` (`FEDERATION_PROVIDER_ID`),
  KEY `IDX_USR_FED_MAP_REALM` (`REALM_ID`),
  CONSTRAINT `FK_FEDMAPPERPM_FEDPRV` FOREIGN KEY (`FEDERATION_PROVIDER_ID`) REFERENCES `USER_FEDERATION_PROVIDER` (`ID`),
  CONSTRAINT `FK_FEDMAPPERPM_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_FEDERATION_MAPPER`
--

LOCK TABLES `USER_FEDERATION_MAPPER` WRITE;
/*!40000 ALTER TABLE `USER_FEDERATION_MAPPER` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_FEDERATION_MAPPER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_FEDERATION_MAPPER_CONFIG`
--

DROP TABLE IF EXISTS `USER_FEDERATION_MAPPER_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_FEDERATION_MAPPER_CONFIG` (
  `USER_FEDERATION_MAPPER_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`USER_FEDERATION_MAPPER_ID`,`NAME`),
  CONSTRAINT `FK_FEDMAPPER_CFG` FOREIGN KEY (`USER_FEDERATION_MAPPER_ID`) REFERENCES `USER_FEDERATION_MAPPER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_FEDERATION_MAPPER_CONFIG`
--

LOCK TABLES `USER_FEDERATION_MAPPER_CONFIG` WRITE;
/*!40000 ALTER TABLE `USER_FEDERATION_MAPPER_CONFIG` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_FEDERATION_MAPPER_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_FEDERATION_PROVIDER`
--

DROP TABLE IF EXISTS `USER_FEDERATION_PROVIDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_FEDERATION_PROVIDER` (
  `ID` varchar(36) NOT NULL,
  `CHANGED_SYNC_PERIOD` int DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `FULL_SYNC_PERIOD` int DEFAULT NULL,
  `LAST_SYNC` int DEFAULT NULL,
  `PRIORITY` int DEFAULT NULL,
  `PROVIDER_NAME` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_USR_FED_PRV_REALM` (`REALM_ID`),
  CONSTRAINT `FK_1FJ32F6PTOLW2QY60CD8N01E8` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_FEDERATION_PROVIDER`
--

LOCK TABLES `USER_FEDERATION_PROVIDER` WRITE;
/*!40000 ALTER TABLE `USER_FEDERATION_PROVIDER` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_FEDERATION_PROVIDER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_GROUP_MEMBERSHIP`
--

DROP TABLE IF EXISTS `USER_GROUP_MEMBERSHIP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_GROUP_MEMBERSHIP` (
  `GROUP_ID` varchar(36) NOT NULL,
  `USER_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`GROUP_ID`,`USER_ID`),
  KEY `IDX_USER_GROUP_MAPPING` (`USER_ID`),
  CONSTRAINT `FK_USER_GROUP_USER` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_GROUP_MEMBERSHIP`
--

LOCK TABLES `USER_GROUP_MEMBERSHIP` WRITE;
/*!40000 ALTER TABLE `USER_GROUP_MEMBERSHIP` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_GROUP_MEMBERSHIP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_REQUIRED_ACTION`
--

DROP TABLE IF EXISTS `USER_REQUIRED_ACTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_REQUIRED_ACTION` (
  `USER_ID` varchar(36) NOT NULL,
  `REQUIRED_ACTION` varchar(255) NOT NULL DEFAULT ' ',
  PRIMARY KEY (`REQUIRED_ACTION`,`USER_ID`),
  KEY `IDX_USER_REQACTIONS` (`USER_ID`),
  CONSTRAINT `FK_6QJ3W1JW9CVAFHE19BWSIUVMD` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_REQUIRED_ACTION`
--

LOCK TABLES `USER_REQUIRED_ACTION` WRITE;
/*!40000 ALTER TABLE `USER_REQUIRED_ACTION` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_REQUIRED_ACTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_ROLE_MAPPING`
--

DROP TABLE IF EXISTS `USER_ROLE_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_ROLE_MAPPING` (
  `ROLE_ID` varchar(255) NOT NULL,
  `USER_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ROLE_ID`,`USER_ID`),
  KEY `IDX_USER_ROLE_MAPPING` (`USER_ID`),
  CONSTRAINT `FK_C4FQV34P1MBYLLOXANG7B1Q3L` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_ROLE_MAPPING`
--

LOCK TABLES `USER_ROLE_MAPPING` WRITE;
/*!40000 ALTER TABLE `USER_ROLE_MAPPING` DISABLE KEYS */;
INSERT INTO `USER_ROLE_MAPPING` VALUES ('53d6b454-e873-4aea-81e7-694212aa1f91','a659ef18-6b57-41fd-8a5a-f69d912943de'),('6ca8ad49-86d1-4a5a-8214-43a728e5e4c7','a659ef18-6b57-41fd-8a5a-f69d912943de'),('53d6b454-e873-4aea-81e7-694212aa1f91','dc8d0878-f73e-4aae-ab23-212f6c0e50e8'),('a4e23802-4deb-4217-8013-8dfe37e9c5bb','dc8d0878-f73e-4aae-ab23-212f6c0e50e8');
/*!40000 ALTER TABLE `USER_ROLE_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_SESSION`
--

DROP TABLE IF EXISTS `USER_SESSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_SESSION` (
  `ID` varchar(36) NOT NULL,
  `AUTH_METHOD` varchar(255) DEFAULT NULL,
  `IP_ADDRESS` varchar(255) DEFAULT NULL,
  `LAST_SESSION_REFRESH` int DEFAULT NULL,
  `LOGIN_USERNAME` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(255) DEFAULT NULL,
  `REMEMBER_ME` bit(1) NOT NULL DEFAULT b'0',
  `STARTED` int DEFAULT NULL,
  `USER_ID` varchar(255) DEFAULT NULL,
  `USER_SESSION_STATE` int DEFAULT NULL,
  `BROKER_SESSION_ID` varchar(255) DEFAULT NULL,
  `BROKER_USER_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_SESSION`
--

LOCK TABLES `USER_SESSION` WRITE;
/*!40000 ALTER TABLE `USER_SESSION` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_SESSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_SESSION_NOTE`
--

DROP TABLE IF EXISTS `USER_SESSION_NOTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_SESSION_NOTE` (
  `USER_SESSION` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `VALUE` text,
  PRIMARY KEY (`USER_SESSION`,`NAME`),
  CONSTRAINT `FK5EDFB00FF51D3472` FOREIGN KEY (`USER_SESSION`) REFERENCES `USER_SESSION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_SESSION_NOTE`
--

LOCK TABLES `USER_SESSION_NOTE` WRITE;
/*!40000 ALTER TABLE `USER_SESSION_NOTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_SESSION_NOTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USERNAME_LOGIN_FAILURE`
--

DROP TABLE IF EXISTS `USERNAME_LOGIN_FAILURE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USERNAME_LOGIN_FAILURE` (
  `REALM_ID` varchar(36) NOT NULL,
  `USERNAME` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `FAILED_LOGIN_NOT_BEFORE` int DEFAULT NULL,
  `LAST_FAILURE` bigint DEFAULT NULL,
  `LAST_IP_FAILURE` varchar(255) DEFAULT NULL,
  `NUM_FAILURES` int DEFAULT NULL,
  PRIMARY KEY (`REALM_ID`,`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USERNAME_LOGIN_FAILURE`
--

LOCK TABLES `USERNAME_LOGIN_FAILURE` WRITE;
/*!40000 ALTER TABLE `USERNAME_LOGIN_FAILURE` DISABLE KEYS */;
/*!40000 ALTER TABLE `USERNAME_LOGIN_FAILURE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `WEB_ORIGINS`
--

DROP TABLE IF EXISTS `WEB_ORIGINS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WEB_ORIGINS` (
  `CLIENT_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  PRIMARY KEY (`CLIENT_ID`,`VALUE`),
  KEY `IDX_WEB_ORIG_CLIENT` (`CLIENT_ID`),
  CONSTRAINT `FK_LOJPHO213XCX4WNKOG82SSRFY` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `WEB_ORIGINS`
--

LOCK TABLES `WEB_ORIGINS` WRITE;
/*!40000 ALTER TABLE `WEB_ORIGINS` DISABLE KEYS */;
INSERT INTO `WEB_ORIGINS` VALUES ('5983633b-180b-4071-bcc5-58248f3ea1b4','http://localhost:5173'),('724f9f53-5886-4a8b-b797-591ad05a9caa','+');
/*!40000 ALTER TABLE `WEB_ORIGINS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'keycloak'
--

--
-- Dumping routines for database 'keycloak'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-30 15:47:59

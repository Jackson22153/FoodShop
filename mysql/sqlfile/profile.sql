CREATE DATABASE  IF NOT EXISTS `profile` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `profile`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: profile
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
-- Temporary view structure for view `customerdetails`
--

DROP TABLE IF EXISTS `customerdetails`;
/*!50001 DROP VIEW IF EXISTS `customerdetails`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `customerdetails` AS SELECT 
 1 AS `CustomerID`,
 1 AS `UserID`,
 1 AS `ContactName`,
 1 AS `Address`,
 1 AS `City`,
 1 AS `Phone`,
 1 AS `Picture`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `CustomerID` varchar(36) NOT NULL,
  `ContactName` varchar(30) DEFAULT NULL,
  `ProfileID` varchar(36) NOT NULL,
  PRIMARY KEY (`CustomerID`),
  KEY `ProfileID` (`ProfileID`),
  CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`ProfileID`) REFERENCES `userprofile` (`ProfileID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES ('491f1e10-64ba-45cf-bcec-878e8ead22c3','happy123','47045f6a-34c4-4dc6-b3a0-fa4453b5da30'),('c02bafd6-0082-4b9f-b232-342471eae90b','happy123','3a8e9fad-c24a-4e20-be0a-edce05e36150');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `employeedetails`
--

DROP TABLE IF EXISTS `employeedetails`;
/*!50001 DROP VIEW IF EXISTS `employeedetails`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `employeedetails` AS SELECT 
 1 AS `EmployeeID`,
 1 AS `UserID`,
 1 AS `BirthDate`,
 1 AS `HireDate`,
 1 AS `Phone`,
 1 AS `Picture`,
 1 AS `Title`,
 1 AS `Address`,
 1 AS `City`,
 1 AS `Notes`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `EmployeeID` varchar(36) NOT NULL,
  `Title` varchar(30) DEFAULT NULL,
  `BirthDate` date DEFAULT NULL,
  `HireDate` date DEFAULT NULL,
  `Notes` text,
  `ProfileID` varchar(36) NOT NULL,
  PRIMARY KEY (`EmployeeID`),
  KEY `ProfileID` (`ProfileID`),
  CONSTRAINT `employees_ibfk_1` FOREIGN KEY (`ProfileID`) REFERENCES `userprofile` (`ProfileID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES ('35e31e6b-d3c1-45c1-9b38-66813e576713',NULL,NULL,'2024-06-14','','71fc0684-de2e-4757-a11b-b8b85ed9ba1b'),('577378d4-bb82-495a-9c70-d0b1aa6de9d6',NULL,'2000-04-04','2024-05-01','This is an awesome guy....','7e7fc974-755e-4d24-be2a-bba6fd3e5dd4');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shippers`
--

DROP TABLE IF EXISTS `shippers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shippers` (
  `ShipperID` int NOT NULL AUTO_INCREMENT,
  `CompanyName` varchar(40) NOT NULL,
  `Phone` varchar(24) DEFAULT NULL,
  PRIMARY KEY (`ShipperID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shippers`
--

LOCK TABLES `shippers` WRITE;
/*!40000 ALTER TABLE `shippers` DISABLE KEYS */;
INSERT INTO `shippers` VALUES (1,'Speedy Express','(503) 555-9831'),(2,'United Package','(503) 555-3199'),(3,'Federal Shipping','(503) 555-9931');
/*!40000 ALTER TABLE `shippers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userprofile`
--

DROP TABLE IF EXISTS `userprofile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userprofile` (
  `ProfileID` varchar(36) NOT NULL,
  `Address` varchar(200) DEFAULT NULL,
  `City` varchar(50) DEFAULT NULL,
  `Phone` varchar(24) DEFAULT NULL,
  `Picture` varchar(256) DEFAULT NULL,
  `UserID` varchar(36) NOT NULL,
  PRIMARY KEY (`ProfileID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userprofile`
--

LOCK TABLES `userprofile` WRITE;
/*!40000 ALTER TABLE `userprofile` DISABLE KEYS */;
INSERT INTO `userprofile` VALUES ('3a8e9fad-c24a-4e20-be0a-edce05e36150','Obere Str. 57ss','HCM','0379349824','b7ab668e-4c99-4730-b3df-809685d88534.png','3072e836-7469-454d-9165-e7761f3f2eb7'),('47045f6a-34c4-4dc6-b3a0-fa4453b5da30',NULL,NULL,'0379349826',NULL,'704c1690-d96b-4258-bbea-061ac2261ccd'),('71fc0684-de2e-4757-a11b-b8b85ed9ba1b',NULL,NULL,NULL,'9df7ccee-b3cb-44fb-b1c2-1aae2ec946a4.png','01efea5a-2668-497b-a756-44e70191fa1b'),('7e7fc974-755e-4d24-be2a-bba6fd3e5dd4','OIbanoi X','Hồ Chí Minh','0982359945','d2fa2968-a767-41ec-b989-b49b0155af5e.png','47fdb832-46bb-495c-8b3d-06bb817e29db');
/*!40000 ALTER TABLE `userprofile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userverification`
--

DROP TABLE IF EXISTS `userverification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userverification` (
  `id` varchar(36) NOT NULL,
  `phoneverification` bit(1) DEFAULT NULL,
  `profileverification` bit(1) DEFAULT NULL,
  `profileid` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `profileid` (`profileid`),
  CONSTRAINT `userverification_ibfk_1` FOREIGN KEY (`profileid`) REFERENCES `userprofile` (`ProfileID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userverification`
--

LOCK TABLES `userverification` WRITE;
/*!40000 ALTER TABLE `userverification` DISABLE KEYS */;
INSERT INTO `userverification` VALUES ('28100089-8b5c-411b-8704-9f71bb40351d',_binary '\0',_binary '\0','47045f6a-34c4-4dc6-b3a0-fa4453b5da30'),('29f9b8f7-0331-4e25-bc32-1b628830d58a',_binary '',_binary '\0','3a8e9fad-c24a-4e20-be0a-edce05e36150'),('7f1d43ee-872d-4e0e-8c68-7f057e33a6a9',_binary '\0',_binary '\0','71fc0684-de2e-4757-a11b-b8b85ed9ba1b'),('9bdaaf1a-271c-45e9-8aae-eb0a9875c6cc',_binary '\0',_binary '\0','7e7fc974-755e-4d24-be2a-bba6fd3e5dd4');
/*!40000 ALTER TABLE `userverification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'profile'
--

--
-- Dumping routines for database 'profile'
--
/*!50003 DROP PROCEDURE IF EXISTS `AddNewCustomer` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddNewCustomer`(IN profileID CHAR(36), IN userID CHAR(36), 
	IN customerID CHAR(36), IN contactName VARCHAR(30), OUT result BIT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET result = 0;
    END;

    START TRANSACTION;
    BEGIN
        INSERT INTO UserProfile (ProfileID, UserID) VALUES (profileID, userID);
        insert into userverification(id, phoneverification, profileverification, profileid) 
        values(UUID(), 0, 0, profileID);
        INSERT INTO Customers (CustomerID, ContactName, ProfileID) VALUES (customerID, contactName, profileID);
        SET result = 1;
        COMMIT;
    END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `AddNewEmployee` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddNewEmployee`(IN profileID CHAR(36), IN userID CHAR(36), 
	IN employeeID CHAR(36), OUT result BIT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET result = 0;
    END;

    START TRANSACTION;
    BEGIN
        INSERT INTO UserProfile (ProfileID, UserID) VALUES (profileID, userID);
        INSERT INTO Employees (EmployeeID, ProfileID) VALUES (employeeID, profileID);
        SET result = 1;
        COMMIT;
    END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `CreateUserVerification` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `CreateUserVerification`(
	IN verificationID varchar(36),
    IN phoneVerification bit,
    IN profileVerification bit,
    IN profileID varchar(36),
    OUT result bit
)
begin
	declare exit handler for sqlexception
    begin
		rollback;
        set result=0;
	end;
    start transaction;
    begin
		insert into userverification(id, phoneverification, profileverification, profileid) 
        values(verificationID, phoneVerification, profileVerification, profileID);
        set result=1;
        commit;
	end;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateAdminEmployeeInfo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateAdminEmployeeInfo`(IN employeeID CHAR(36), 
	IN hireDate DATE, IN picture VARCHAR(256), IN title VARCHAR(30), 
	IN notes TEXT, OUT result BIT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET result = 0;
    END;

    START TRANSACTION;
    BEGIN
        UPDATE UserProfile u
        JOIN Employees e ON u.ProfileID = e.ProfileID
        SET u.Picture = picture
        WHERE e.EmployeeID = employeeID;

        UPDATE Employees
        SET HireDate = hireDate,
            Title = title,
            Notes = notes
        WHERE EmployeeID = employeeID;
        SET result = 1;
        COMMIT;
    END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateCustomerInfo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateCustomerInfo`(IN customerID CHAR(36), 
	IN contactName VARCHAR(30), IN address VARCHAR(200), IN city VARCHAR(50),
	IN phone VARCHAR(24), IN picture VARCHAR(256), OUT result BIT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET result = 0;
    END;

    START TRANSACTION;
    BEGIN
        UPDATE UserProfile u
        JOIN Customers c ON u.ProfileID = c.ProfileID
        SET u.Address = address, u.City = city, u.Phone = phone, u.Picture = picture
        WHERE c.CustomerID = customerID;

        UPDATE Customers
        SET ContactName = contactName
        WHERE CustomerID = customerID;
        SET result = 1;
        COMMIT;
    END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateEmployeeInfo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateEmployeeInfo`(IN employeeID CHAR(36), 
	IN birthDate DATE, IN address VARCHAR(200), IN city VARCHAR(50), 
	IN phone VARCHAR(24), IN picture VARCHAR(256), OUT result BIT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET result = 0;
    END;

    START TRANSACTION;
    BEGIN
        UPDATE UserProfile u
        JOIN Employees e ON u.ProfileID = e.ProfileID
        SET u.Address = address, u.City = city, u.Phone = phone, u.Picture = picture
        WHERE e.EmployeeID = employeeID;

        UPDATE Employees
        SET BirthDate = birthDate
        WHERE EmployeeID = employeeID;
        SET result = 1;
        COMMIT;
    END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdatePhoneVerification` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `UpdatePhoneVerification`(
	IN profileID varchar(36),
    IN phoneVerification bit,
    OUT result bit
)
begin
	declare exit handler for sqlexception
    begin 
		rollback;
        set result = 0;
	end;
    start transaction;
    begin
		Update userverification uv set uv.phoneverification=phoneVerification
        where uv.profileid=profileID;
        set result=1;
        commit;
    end;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `updateProfileVerification` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `updateProfileVerification`(
	IN profileID varchar(36),
    IN profileVerification bit,
    OUT result bit
)
begin
	declare exit handler for sqlexception
    begin 
		rollback;
        set result = 0;
	end;
    start transaction;
    begin
		Update userverification uv set uv.profileverification=profileVerification
        where uv.profileid=profileID;
        set result=1;
        commit;
    end;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `customerdetails`
--

/*!50001 DROP VIEW IF EXISTS `customerdetails`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `customerdetails` AS select `c`.`CustomerID` AS `CustomerID`,`u`.`UserID` AS `UserID`,`c`.`ContactName` AS `ContactName`,`u`.`Address` AS `Address`,`u`.`City` AS `City`,`u`.`Phone` AS `Phone`,`u`.`Picture` AS `Picture` from (`customers` `c` join `userprofile` `u` on((`c`.`ProfileID` = `u`.`ProfileID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `employeedetails`
--

/*!50001 DROP VIEW IF EXISTS `employeedetails`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `employeedetails` AS select `e`.`EmployeeID` AS `EmployeeID`,`u`.`UserID` AS `UserID`,`e`.`BirthDate` AS `BirthDate`,`e`.`HireDate` AS `HireDate`,`u`.`Phone` AS `Phone`,`u`.`Picture` AS `Picture`,`e`.`Title` AS `Title`,`u`.`Address` AS `Address`,`u`.`City` AS `City`,`e`.`Notes` AS `Notes` from (`employees` `e` join `userprofile` `u` on((`e`.`ProfileID` = `u`.`ProfileID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-28 16:17:54

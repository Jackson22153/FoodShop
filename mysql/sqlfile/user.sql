CREATE DATABASE  IF NOT EXISTS `user` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `user`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: user
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
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `RoleID` int NOT NULL AUTO_INCREMENT,
  `Rolename` varchar(20) NOT NULL,
  PRIMARY KEY (`RoleID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'CUSTOMER'),(2,'EMPLOYEE'),(3,'ADMIN');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userrole`
--

DROP TABLE IF EXISTS `userrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userrole` (
  `UserID` char(36) NOT NULL,
  `RoleID` int NOT NULL,
  PRIMARY KEY (`UserID`,`RoleID`),
  KEY `RoleID` (`RoleID`),
  CONSTRAINT `userrole_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`),
  CONSTRAINT `userrole_ibfk_2` FOREIGN KEY (`RoleID`) REFERENCES `roles` (`RoleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userrole`
--

LOCK TABLES `userrole` WRITE;
/*!40000 ALTER TABLE `userrole` DISABLE KEYS */;
INSERT INTO `userrole` VALUES ('3072e836-7469-454d-9165-e7761f3f2eb7',1),('704c1690-d96b-4258-bbea-061ac2261ccd',1),('01efea5a-2668-497b-a756-44e70191fa1b',2),('47fdb832-46bb-495c-8b3d-06bb817e29db',2),('47fdb832-46bb-495c-8b3d-06bb817e29db',3);
/*!40000 ALTER TABLE `userrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `userroles`
--

DROP TABLE IF EXISTS `userroles`;
/*!50001 DROP VIEW IF EXISTS `userroles`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `userroles` AS SELECT 
 1 AS `UserID`,
 1 AS `Username`,
 1 AS `Email`,
 1 AS `RoleID`,
 1 AS `Rolename`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `UserID` char(36) NOT NULL,
  `Username` varchar(20) NOT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `Email` varchar(320) DEFAULT NULL,
  `Enabled` bit(1) NOT NULL,
  `EmailVerified` bit(1) DEFAULT NULL,
  `FirstName` varchar(10) DEFAULT NULL,
  `LastName` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('01efea5a-2668-497b-a756-44e70191fa1b','trongphuc123','$2a$10$Xeu34/NLqwySDUXLrG5Q4O6Hvb9VPgOj/4df2PFFejFQDo8D40prK','trongphuc123@example.com',_binary '',_binary '','Phuc','Nguyen'),('3072e836-7469-454d-9165-e7761f3f2eb7','happy123','$2a$10$uQ/ebTNFyXjEUQiwlQp.WeVh/hCIGa28xSfqpS5oc617lHCxqOqKy','happy123@gmail.com',_binary '',_binary '','Phuc','Nguyen'),('47fdb832-46bb-495c-8b3d-06bb817e29db','another2001','$2a$12$4QnH1RBnsz5xn/vv6zpO7efFq29ZI/jjxGWoxMqLGBp5ncy/9GH2.','another2001@example.com',_binary '',_binary '','Phuc','Nguyen'),('704c1690-d96b-4258-bbea-061ac2261ccd','trongphuc22153','$2a$10$Ok9KSJxndxDFafv1OwIAbeZvoBVBXIjEzqZIzCV1EB1d6GtHmnqhC','trongphuc22153@gmail.com',_binary '',_binary '','Phuc','Nguyen');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'user'
--

--
-- Dumping routines for database 'user'
--
/*!50003 DROP PROCEDURE IF EXISTS `AssignUserRole` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `AssignUserRole`(IN username VARCHAR(20), IN roleName VARCHAR(20), out result bit)
BEGIN
	declare exit handler for sqlexception
    begin
		rollback;
        set result=0;
    end;

    IF NOT EXISTS (SELECT * FROM UserRoles ur WHERE ur.Username = username AND ur.RoleName = roleName) THEN
        START TRANSACTION;
        BEGIN
            INSERT INTO UserRole (UserID, RoleID) 
            select u.userid, r.roleid 
            from users u, roles r 
            where u.username=username and r.rolename=rolename;
            
            set result=1;
            COMMIT;
        END;
    ELSE
        SELECT 'UserRole has been existed' AS message;
        set result=0;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `AssignUserRoles` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `AssignUserRoles`(
  IN username VARCHAR(20),
  IN listRoleID VARCHAR(255),
  OUT result BIT
)
BEGIN
  DECLARE userID CHAR(36);
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET result = 0;
  END;

  START TRANSACTION;

  SELECT userID = UserID FROM Users WHERE Username = username;

  DROP TEMPORARY TABLE IF EXISTS ListTable;
  CREATE TEMPORARY TABLE ListTable (RoleID INT);
  INSERT INTO ListTable (RoleID)
  SELECT CAST(substring_index(substring_index(listRoleID, ',', n), ',', -1) AS UNSIGNED)
  FROM (SELECT @row := @row + 1 AS n FROM (SELECT 0) r, (SELECT @row := 0) init
       WHERE @row < LENGTH(listRoleID) - LENGTH(REPLACE(listRoleID, ',', '')) + 1) num
  CROSS JOIN (SELECT @row) r;

  DELETE FROM UserRole ur WHERE ur.UserID = userID;

  INSERT INTO UserRole (UserID, RoleID)
  SELECT userID, RoleID FROM ListTable;

  COMMIT;
  SET result = 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `DeleteUserRole` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `DeleteUserRole`(IN username VARCHAR(20), IN roleName VARCHAR(20), out result bit)
BEGIN
	declare exit handler for sqlexception
    begin
		rollback;
        set result=0;
	end;
    IF ((SELECT COUNT(*) FROM UserRoles ur WHERE ur.Username = username AND ur.RoleName = roleName) > 0) THEN
        START TRANSACTION;
        BEGIN
            DECLARE roleID INT;
            DECLARE userID char(36);
            SET roleID = (SELECT r.RoleID FROM Roles r WHERE r.RoleName = roleName);
            SET userID = (SELECT u.UserID FROM Users u WHERE u.Username = username);

            DELETE ur 
            FROM UserRole ur
            WHERE ur.userid = userid AND ur.roleid=roleid;
			
            set result=1;
            COMMIT;
        END;
    ELSE
        SELECT 'UserRole does not exist';
        set result =0;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateUserPassword` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `UpdateUserPassword`(IN userID VARCHAR(36), IN password VARCHAR(255), OUT result BIT)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET result = 0;
    END;
    START TRANSACTION;
    BEGIN
        UPDATE Users SET password = password WHERE userID = userID;
        SET result = 1;
        COMMIT;
    END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `userroles`
--

/*!50001 DROP VIEW IF EXISTS `userroles`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 SQL SECURITY DEFINER */
/*!50001 VIEW `userroles` AS select `u`.`UserID` AS `UserID`,`u`.`Username` AS `Username`,`u`.`Email` AS `Email`,`r`.`RoleID` AS `RoleID`,`r`.`Rolename` AS `Rolename` from ((`users` `u` left join `userrole` `ur` on((`u`.`UserID` = `ur`.`UserID`))) join `roles` `r` on((`ur`.`RoleID` = `r`.`RoleID`))) */;
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

-- Dump completed on 2024-09-02 18:42:03

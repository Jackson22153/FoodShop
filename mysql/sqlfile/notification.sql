CREATE DATABASE  IF NOT EXISTS `notification` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `notification`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: notification
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
-- Temporary view structure for view `notificationdetails`
--

DROP TABLE IF EXISTS `notificationdetails`;
/*!50001 DROP VIEW IF EXISTS `notificationdetails`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `notificationdetails` AS SELECT 
 1 AS `NotificationID`,
 1 AS `Title`,
 1 AS `Message`,
 1 AS `SenderID`,
 1 AS `ReceiverID`,
 1 AS `Topic`,
 1 AS `Status`,
 1 AS `IsRead`,
 1 AS `Time`,
 1 AS `Picture`,
 1 AS `RepliedTo`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `NotificationID` char(36) NOT NULL,
  `Message` text NOT NULL,
  `SenderID` char(36) DEFAULT NULL,
  `ReceiverID` char(36) DEFAULT NULL,
  `TopicID` int DEFAULT NULL,
  `Status` varchar(20) NOT NULL,
  `Time` datetime NOT NULL,
  `Title` varchar(100) NOT NULL,
  `Picture` varchar(256) DEFAULT NULL,
  `RepliedTo` char(36) DEFAULT NULL,
  PRIMARY KEY (`NotificationID`),
  KEY `fk_Notifications_Topics` (`TopicID`),
  KEY `fk_Notifications_Notifications` (`RepliedTo`),
  CONSTRAINT `fk_Notifications_Notifications` FOREIGN KEY (`RepliedTo`) REFERENCES `notifications` (`NotificationID`),
  CONSTRAINT `fk_Notifications_Topics` FOREIGN KEY (`TopicID`) REFERENCES `topics` (`TopicID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificationuser`
--

DROP TABLE IF EXISTS `notificationuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificationuser` (
  `NotificationID` char(36) NOT NULL,
  `UserID` char(36) NOT NULL,
  `IsRead` tinyint(1) NOT NULL,
  PRIMARY KEY (`NotificationID`,`UserID`),
  CONSTRAINT `fk_NotificationUser_Notifications` FOREIGN KEY (`NotificationID`) REFERENCES `notifications` (`NotificationID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificationuser`
--

LOCK TABLES `notificationuser` WRITE;
/*!40000 ALTER TABLE `notificationuser` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificationuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topics`
--

DROP TABLE IF EXISTS `topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topics` (
  `TopicID` int NOT NULL AUTO_INCREMENT,
  `TopicName` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`TopicID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topics`
--

LOCK TABLES `topics` WRITE;
/*!40000 ALTER TABLE `topics` DISABLE KEYS */;
INSERT INTO `topics` VALUES (1,'Order'),(2,'Account');
/*!40000 ALTER TABLE `topics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'notification'
--

--
-- Dumping routines for database 'notification'
--
/*!50003 DROP PROCEDURE IF EXISTS `CreateNotification` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CreateNotification`(
  IN notificationID CHAR(36),
  IN title VARCHAR(100),
  IN message TEXT,
  IN picture VARCHAR(256),
  IN senderID CHAR(36),
  IN receiverID CHAR(36),
  IN topicName VARCHAR(20),
  IN repliedTo CHAR(36),
  IN status VARCHAR(20),
  IN isRead TINYINT(1),
  IN time DATETIME,
  OUT result TINYINT(1)
)
BEGIN
	DECLARE topicID INT;
	  declare exit handler for sqlexception
	  begin
		rollback;
		set result=0;
	  end;
  
  SELECT t.TopicID INTO topicID FROM Topics t WHERE t.TopicName = topicName;

  IF topicID IS NOT NULL THEN
    START TRANSACTION;
    BEGIN
      INSERT INTO Notifications (NotificationID, Title, Message, SenderID, ReceiverID, TopicID, RepliedTo, Status, Time, Picture)
      VALUES (notificationID, title, message, senderID, receiverID, topicID, repliedTo, status, time, picture);

      INSERT INTO NotificationUser (NotificationID, UserID, IsRead)
      VALUES (notificationID, receiverID, isRead);

      SET result = 1;
      COMMIT;
    END;
  ELSE
    SET result = 0;
  END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateNotificationReadStatusByNotificationID` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateNotificationReadStatusByNotificationID`(
  IN notificationID CHAR(36),
  IN isRead TINYINT(1),
  OUT result TINYINT(1)
)
BEGIN
	declare exit handler for sqlexception
    begin
		rollback;
		set result=0;
    end;

  START TRANSACTION;
  BEGIN
    IF NOT EXISTS (SELECT * FROM NotificationUser nu, Notifications n WHERE n.NotificationID = notificationID AND n.ReceiverID = nu.UserID) THEN
      INSERT INTO NotificationUser (NotificationID, UserID, IsRead)
      SELECT n.NotificationID, n.ReceiverID, n.isRead
      FROM Notifications n
      WHERE n.NotificationID = notificationID;
    END IF;

    UPDATE NotificationUser nu
    SET nu.IsRead = isRead
    WHERE nu.NotificationID = notificationID;

    SET result = 1;
    COMMIT;
  END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateNotificationReadStatusByNotificationIDAndUserID` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateNotificationReadStatusByNotificationIDAndUserID`(
  IN notificationID CHAR(36),
  IN userID CHAR(36),
  IN isRead TINYINT(1),
  OUT result TINYINT(1)
)
BEGIN
	declare exit handler for sqlexception
    begin
		rollback;
        set result=0;
	end;
  START TRANSACTION;
  BEGIN
    IF EXISTS (SELECT * FROM NotificationUser nu WHERE nu.NotificationID = notificationID AND nu.UserID = userID) THEN
      UPDATE NotificationUser nu
      SET nu.IsRead = isRead
      WHERE nu.NotificationID = notificationID AND nu.UserID = userID;
    ELSE
      INSERT INTO NotificationUser (NotificationID, UserID, IsRead)
      VALUES (notificationID, userID, isRead);
    END IF;

    SET result = 1;
    COMMIT;
  END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateNotificationReadStatusByUserID` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `UpdateNotificationReadStatusByUserID`(
	in userID nchar(36), 
    in isRead bit, 
    out result bit)
begin
	declare exit handler for sqlexception
    begin
		rollback;
		set result=0;
	end;
	start transaction;
	begin 
		update NotificationUser nu set nu.IsRead=isRead where nu.UserID=userID;
		set result=1;
		commit;
	end;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateNotificationsReadByNotificationID` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateNotificationsReadByNotificationID`(IN notificationIDs VARCHAR(255), IN isRead BIT, OUT result BIT)
BEGIN
   DECLARE tokens INT DEFAULT 0;
	DECLARE token VARCHAR(100) DEFAULT '';
    declare exit handler for sqlexception
    begin 
		rollback;
        set result=0;
	end;

	DROP TEMPORARY TABLE IF EXISTS ListIds;
	CREATE TEMPORARY TABLE ListIds (NotificationID CHAR(36));

	WHILE LOCATE(',', notificationIDs) > 0 DO
		SET token = SUBSTRING_INDEX(notificationIDs, ',', 1);
		SET notificationIDs = SUBSTRING(notificationIDs, LENGTH(token) + 2);
		SET token = TRIM(BOTH ',' FROM token);
		INSERT INTO ListIds VALUES (token);
		SET tokens = tokens + 1;
	END WHILE;

	-- Insert the last token (if any)
	IF notificationIDs <> '' THEN
		INSERT INTO ListIds VALUES (notificationIDs);
		SET tokens = tokens + 1;
	END IF;

    UPDATE NotificationUser nu
    JOIN ListIds li ON nu.NotificationID = li.NotificationID
    SET nu.IsRead = isRead;

    SET result = 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `notificationdetails`
--

/*!50001 DROP VIEW IF EXISTS `notificationdetails`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `notificationdetails` AS select `n`.`NotificationID` AS `NotificationID`,`n`.`Title` AS `Title`,`n`.`Message` AS `Message`,`n`.`SenderID` AS `SenderID`,`n`.`ReceiverID` AS `ReceiverID`,`t`.`TopicName` AS `Topic`,`n`.`Status` AS `Status`,ifnull(`nu`.`IsRead`,0) AS `IsRead`,`n`.`Time` AS `Time`,`n`.`Picture` AS `Picture`,`n`.`RepliedTo` AS `RepliedTo` from ((`notifications` `n` join `topics` `t` on((`n`.`TopicID` = `t`.`TopicID`))) left join `notificationuser` `nu` on((`nu`.`NotificationID` = `n`.`NotificationID`))) */;
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

-- Dump completed on 2024-09-25 19:22:35

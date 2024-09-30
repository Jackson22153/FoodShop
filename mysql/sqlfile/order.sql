CREATE DATABASE  IF NOT EXISTS `order` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `order`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: order
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
-- Temporary view structure for view `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!50001 DROP VIEW IF EXISTS `invoices`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `invoices` AS SELECT 
 1 AS `ShipName`,
 1 AS `ShipAddress`,
 1 AS `ShipCity`,
 1 AS `Phone`,
 1 AS `CustomerID`,
 1 AS `EmployeeID`,
 1 AS `OrderID`,
 1 AS `OrderDate`,
 1 AS `RequiredDate`,
 1 AS `ShippedDate`,
 1 AS `ShipperID`,
 1 AS `ProductID`,
 1 AS `UnitPrice`,
 1 AS `Quantity`,
 1 AS `ExtendedPrice`,
 1 AS `Freight`,
 1 AS `Status`,
 1 AS `DiscountID`,
 1 AS `DiscountPercent`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `orderdetails`
--

DROP TABLE IF EXISTS `orderdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderdetails` (
  `OrderID` varchar(36) NOT NULL,
  `ProductID` int NOT NULL,
  `UnitPrice` decimal(10,2) NOT NULL DEFAULT '0.00',
  `Quantity` smallint NOT NULL DEFAULT '1',
  PRIMARY KEY (`OrderID`,`ProductID`),
  CONSTRAINT `FK_Order_Details_Orders` FOREIGN KEY (`OrderID`) REFERENCES `orders` (`OrderID`),
  CONSTRAINT `orderdetails_ibfk_1` FOREIGN KEY (`OrderID`) REFERENCES `orders` (`OrderID`),
  CONSTRAINT `CK_Quantity` CHECK ((`Quantity` > 0)),
  CONSTRAINT `CK_UnitPrice` CHECK ((`UnitPrice` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderdetails`
--

LOCK TABLES `orderdetails` WRITE;
/*!40000 ALTER TABLE `orderdetails` DISABLE KEYS */;
/*!40000 ALTER TABLE `orderdetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderdetailsdiscounts`
--

DROP TABLE IF EXISTS `orderdetailsdiscounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderdetailsdiscounts` (
  `DiscountID` varchar(36) NOT NULL,
  `OrderID` varchar(36) NOT NULL,
  `ProductID` int NOT NULL,
  `AppliedDate` datetime DEFAULT NULL,
  `DiscountPercent` int NOT NULL,
  PRIMARY KEY (`DiscountID`,`OrderID`,`ProductID`),
  KEY `FK_OrderDetail_ProductID_OrderID` (`OrderID`,`ProductID`),
  CONSTRAINT `FK_OrderDetail_ProductID_OrderID` FOREIGN KEY (`OrderID`, `ProductID`) REFERENCES `orderdetails` (`OrderID`, `ProductID`),
  CONSTRAINT `orderdetailsdiscounts_ibfk_1` FOREIGN KEY (`OrderID`, `ProductID`) REFERENCES `orderdetails` (`OrderID`, `ProductID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderdetailsdiscounts`
--

LOCK TABLES `orderdetailsdiscounts` WRITE;
/*!40000 ALTER TABLE `orderdetailsdiscounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `orderdetailsdiscounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `orderdetailsdiscountsum`
--

DROP TABLE IF EXISTS `orderdetailsdiscountsum`;
/*!50001 DROP VIEW IF EXISTS `orderdetailsdiscountsum`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `orderdetailsdiscountsum` AS SELECT 
 1 AS `OrderID`,
 1 AS `ProductID`,
 1 AS `NumberOfDiscounts`,
 1 AS `TotalDiscount`,
 1 AS `UnitPrice`,
 1 AS `Quantity`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `orderdetailsextended`
--

DROP TABLE IF EXISTS `orderdetailsextended`;
/*!50001 DROP VIEW IF EXISTS `orderdetailsextended`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `orderdetailsextended` AS SELECT 
 1 AS `OrderID`,
 1 AS `ProductID`,
 1 AS `UnitPrice`,
 1 AS `Quantity`,
 1 AS `Discount`,
 1 AS `ExtendedPrice`,
 1 AS `Status`,
 1 AS `CustomerID`,
 1 AS `EmployeeID`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `OrderID` varchar(36) NOT NULL,
  `CustomerID` varchar(36) DEFAULT NULL,
  `EmployeeID` varchar(36) DEFAULT NULL,
  `OrderDate` datetime DEFAULT NULL,
  `RequiredDate` datetime DEFAULT NULL,
  `ShippedDate` datetime DEFAULT NULL,
  `ShipVia` int DEFAULT NULL,
  `Freight` decimal(10,2) DEFAULT '0.00',
  `ShipName` varchar(40) DEFAULT NULL,
  `ShipAddress` varchar(200) DEFAULT NULL,
  `ShipCity` varchar(50) DEFAULT NULL,
  `Status` varchar(10) DEFAULT NULL,
  `Phone` varchar(24) NOT NULL,
  PRIMARY KEY (`OrderID`),
  CONSTRAINT `CK_Order_Status` CHECK ((`Status` in (_utf8mb4'canceled',_utf8mb4'successful',_utf8mb4'shipping',_utf8mb4'confirmed',_utf8mb4'pending')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'order'
--

--
-- Dumping routines for database 'order'
--
/*!50003 DROP PROCEDURE IF EXISTS `InsertOrder` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `InsertOrder`(
  IN orderID VARCHAR(36),
  IN orderDate DATETIME,
  IN requiredDate DATETIME,
  IN shippedDate DATETIME,
  IN freight DECIMAL(10, 2),
  IN shipName VARCHAR(40),
  IN shipAddress VARCHAR(200),
  IN shipCity VARCHAR(50),
  IN phone VARCHAR(24),
  IN status VARCHAR(10),
  IN customerID CHAR(36),
  IN employeeID CHAR(36),
  IN shipperID INT,
  OUT result BIT
)
BEGIN
  DECLARE exit_handler BOOLEAN DEFAULT FALSE;
  DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET exit_handler = TRUE;

  START TRANSACTION;
  BEGIN
    INSERT INTO Orders (
      OrderID,
      OrderDate,
      RequiredDate,
      ShippedDate,
      Freight,
      ShipName,
      ShipAddress,
      ShipCity,
      Phone,
      Status,
      CustomerID,
      EmployeeID,
      ShipVia
    ) VALUES (
      orderID,
      orderDate,
      requiredDate,
      shippedDate,
      freight,
      shipName,
      shipAddress,
      shipCity,
      phone,
      status,
      customerID,
      employeeID,
      shipperID
    );
    IF exit_handler THEN
      ROLLBACK;
      SET result = 0;
    ELSE
      COMMIT;
      SET result = 1;
    END IF;
  END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InsertOrderDetail` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `InsertOrderDetail`(
  IN productID INT,
  IN orderID VARCHAR(36),
  IN unitPrice DECIMAL(10, 2),
  IN quantity INT,
  OUT result BIT
)
BEGIN
  DECLARE exit_handler BOOLEAN DEFAULT FALSE;
  DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET exit_handler = TRUE;

  START TRANSACTION;
  BEGIN
    INSERT INTO OrderDetails (
      OrderID,
      ProductID,
      UnitPrice,
      Quantity
    ) VALUES (
      orderID,
      productID,
      unitPrice,
      quantity
    );
    IF exit_handler THEN
      ROLLBACK;
      SET result = 0;
    ELSE
      COMMIT;
      SET result = 1;
    END IF;
  END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InsertOrderDetailDiscount` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `InsertOrderDetailDiscount`(
    IN orderID VARCHAR(36),
    IN productID INT,
    IN discountID VARCHAR(36),
    IN discountPercent INT,
    IN appliedDate DATETIME,
    OUT Result BIT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET Result = 0;
        SELECT 'Some errors have occured' AS Error_Message;
    END;

    START TRANSACTION;
    INSERT INTO OrderDetailsDiscounts (DiscountID, OrderID, ProductID, AppliedDate, DiscountPercent)
    VALUES (discountID, orderID, productID, appliedDate, discountPercent);
    SET Result = 1;
    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `invoices`
--

/*!50001 DROP VIEW IF EXISTS `invoices`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 SQL SECURITY DEFINER */
/*!50001 VIEW `invoices` AS select `o`.`ShipName` AS `ShipName`,`o`.`ShipAddress` AS `ShipAddress`,`o`.`ShipCity` AS `ShipCity`,`o`.`Phone` AS `Phone`,`o`.`CustomerID` AS `CustomerID`,`o`.`EmployeeID` AS `EmployeeID`,`o`.`OrderID` AS `OrderID`,`o`.`OrderDate` AS `OrderDate`,`o`.`RequiredDate` AS `RequiredDate`,`o`.`ShippedDate` AS `ShippedDate`,`o`.`ShipVia` AS `ShipperID`,`ode`.`ProductID` AS `ProductID`,`ode`.`UnitPrice` AS `UnitPrice`,`ode`.`Quantity` AS `Quantity`,`ode`.`ExtendedPrice` AS `ExtendedPrice`,ifnull(`o`.`Freight`,0) AS `Freight`,`o`.`Status` AS `Status`,`odd`.`DiscountID` AS `DiscountID`,ifnull(`odd`.`DiscountPercent`,0) AS `DiscountPercent` from ((`orders` `o` join `orderdetailsextended` `ode` on((`o`.`OrderID` = `ode`.`OrderID`))) left join `orderdetailsdiscounts` `odd` on(((`odd`.`OrderID` = `ode`.`OrderID`) and (`odd`.`ProductID` = `ode`.`ProductID`)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `orderdetailsdiscountsum`
--

/*!50001 DROP VIEW IF EXISTS `orderdetailsdiscountsum`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 SQL SECURITY DEFINER */
/*!50001 VIEW `orderdetailsdiscountsum` AS select `od`.`OrderID` AS `OrderID`,`od`.`ProductID` AS `ProductID`,count(`odd`.`DiscountID`) AS `NumberOfDiscounts`,ifnull(sum(`odd`.`DiscountPercent`),0) AS `TotalDiscount`,`od`.`UnitPrice` AS `UnitPrice`,`od`.`Quantity` AS `Quantity` from (`orderdetails` `od` left join `orderdetailsdiscounts` `odd` on(((`od`.`OrderID` = `odd`.`OrderID`) and (`od`.`ProductID` = `odd`.`ProductID`)))) group by `od`.`OrderID`,`od`.`ProductID`,`od`.`UnitPrice`,`od`.`Quantity` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `orderdetailsextended`
--

/*!50001 DROP VIEW IF EXISTS `orderdetailsextended`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 SQL SECURITY DEFINER */
/*!50001 VIEW `orderdetailsextended` AS select `ode`.`OrderID` AS `OrderID`,`ode`.`ProductID` AS `ProductID`,`ode`.`UnitPrice` AS `UnitPrice`,`ode`.`Quantity` AS `Quantity`,`ode`.`TotalDiscount` AS `Discount`,cast(((`ode`.`UnitPrice` * `ode`.`Quantity`) * (1 - (`ode`.`TotalDiscount` / 100))) as decimal(10,2)) AS `ExtendedPrice`,`o`.`Status` AS `Status`,`o`.`CustomerID` AS `CustomerID`,`o`.`EmployeeID` AS `EmployeeID` from (`orderdetailsdiscountsum` `ode` join `orders` `o` on((`o`.`OrderID` = `ode`.`OrderID`))) */;
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

-- Dump completed on 2024-08-31 17:12:52

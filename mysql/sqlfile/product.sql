CREATE DATABASE  IF NOT EXISTS `product` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `product`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: product
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
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `CategoryID` int NOT NULL AUTO_INCREMENT,
  `CategoryName` varchar(15) NOT NULL,
  `Description` text,
  `Picture` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`CategoryID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Beverages','Soft drinks, coffees, teas, beers, and ales','f35fb269-b9cd-4f98-b1b3-07f22dc72511.jpg'),(2,'Condiments','Sweet and savory sauces, relishes, spreads, and seasonings','cd709f4c-b8c4-4060-989b-8282deff85c8.jpg'),(3,'Confections','Desserts, candies, and sweet breads','a22927d5-2b7d-4bfe-b853-37b12a46dd04.jpg'),(4,'Dairy Products','Cheeses','2b43f34f-0732-4eef-87f4-6a8315685a85.jpg'),(5,'Grains/Cereals','Breads, crackers, pasta, and cereal','9b086f17-e380-43c2-83fb-1192cc9323e6.jpg'),(6,'Meat/Poultry','Prepared meats','677e635f-3729-4723-a86c-54cb8fe5e760.jpg'),(7,'Produce','Dried fruit and bean curd','1b30bcda-5ba0-494d-9a80-70d0cf6ddd33.jpg'),(8,'Seafood','Seaweed and fish','ffcffbc7-e86a-4c9a-a776-019e738061d3.jpg');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `current product list`
--

DROP TABLE IF EXISTS `current product list`;
/*!50001 DROP VIEW IF EXISTS `current product list`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `current product list` AS SELECT 
 1 AS `ProductID`,
 1 AS `ProductName`,
 1 AS `UnitPrice`,
 1 AS `UnitsInStock`,
 1 AS `Picture`,
 1 AS `CategoryName`,
 1 AS `DiscountID`,
 1 AS `DiscountPercent`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `current valid discounts`
--

DROP TABLE IF EXISTS `current valid discounts`;
/*!50001 DROP VIEW IF EXISTS `current valid discounts`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `current valid discounts` AS SELECT 
 1 AS `DiscountID`,
 1 AS `DiscountPercent`,
 1 AS `DiscountCode`,
 1 AS `StartDate`,
 1 AS `EndDate`,
 1 AS `Active`,
 1 AS `DiscountType`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `currentvaliddiscountpercentagebased`
--

DROP TABLE IF EXISTS `currentvaliddiscountpercentagebased`;
/*!50001 DROP VIEW IF EXISTS `currentvaliddiscountpercentagebased`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `currentvaliddiscountpercentagebased` AS SELECT 
 1 AS `DiscountID`,
 1 AS `DiscountPercent`,
 1 AS `DiscountType`,
 1 AS `DiscountCode`,
 1 AS `StartDate`,
 1 AS `EndDate`,
 1 AS `Active`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `discountdetails`
--

DROP TABLE IF EXISTS `discountdetails`;
/*!50001 DROP VIEW IF EXISTS `discountdetails`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `discountdetails` AS SELECT 
 1 AS `DiscountID`,
 1 AS `DiscountPercent`,
 1 AS `DiscountCode`,
 1 AS `StartDate`,
 1 AS `EndDate`,
 1 AS `Active`,
 1 AS `DiscountType`,
 1 AS `Description`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `discounts`
--

DROP TABLE IF EXISTS `discounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discounts` (
  `DiscountID` varchar(36) NOT NULL,
  `DiscountPercent` int NOT NULL,
  `DiscountCode` varchar(255) DEFAULT NULL,
  `StartDate` datetime NOT NULL,
  `EndDate` datetime NOT NULL,
  `Active` bit(1) NOT NULL,
  `DiscountTypeID` int NOT NULL,
  PRIMARY KEY (`DiscountID`),
  KEY `FK_Discounts_DiscountTypes` (`DiscountTypeID`),
  CONSTRAINT `FK_Discounts_DiscountTypes` FOREIGN KEY (`DiscountTypeID`) REFERENCES `discounttypes` (`DiscountTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discounts`
--

LOCK TABLES `discounts` WRITE;
/*!40000 ALTER TABLE `discounts` DISABLE KEYS */;
INSERT INTO `discounts` VALUES ('0a405179-7c98-429d-b564-5bbd75864fe3',12,'','2024-06-26 00:00:00','2025-06-26 00:00:00',_binary '',1),('3be96477-95de-4bfd-8be7-60fad2020e84',30,'','2024-06-26 00:00:00','2026-06-26 00:00:00',_binary '\0',1),('57366b38-e463-4536-bcd8-5535d01cd4b8',10,'','2024-06-26 00:00:00','2024-07-31 00:00:00',_binary '',1),('590a6dd2-baf2-4b64-8bd6-75f3874dea8a',20,'TheChosenOne','2024-04-08 12:30:00','2025-04-08 12:30:00',_binary '\0',1),('8997ccff-e004-4627-a897-df7a6012f563',20,'6753fd74-cca8-40c3-83f4-be0190ea631c','2024-05-09 00:00:00','2024-06-05 00:00:00',_binary '',2),('8d9ec5ca-67ac-4e1b-b05d-f8e2bad66c7a',30,'33d81ebf-ff8c-4f38-8c4b-de41d2f681bf','2024-04-08 12:30:00','2025-04-08 12:30:00',_binary '',1),('b6ff6ba9-6dd3-408d-9acc-af4dcc2b90c3',30,'50ee22c1-b499-4e76-8bab-d23b4e0ee46e','2024-04-08 12:30:00','2025-04-08 12:30:00',_binary '',2),('cd3507b7-f023-4a06-8dd4-b776520d266d',25,'','2024-04-30 00:00:00','2024-07-10 02:00:00',_binary '\0',1),('d7f2059b-97ef-4edc-9544-11859ea4eb4f',66,'4ee41d7c-4fa8-47b6-b60f-2b12491d9c2c','2024-04-16 00:00:00','2025-04-09 00:00:00',_binary '\0',1),('f12ace16-8ce7-43a4-b5fa-da0fe4070e4c',5,'a8f2019a-cc2a-4f1d-a412-4bae2eb2cfc1','2024-06-26 00:00:00','2026-06-26 00:00:00',_binary '',1);
/*!40000 ALTER TABLE `discounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discounttypes`
--

DROP TABLE IF EXISTS `discounttypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discounttypes` (
  `DiscountTypeID` int NOT NULL AUTO_INCREMENT,
  `DiscountType` varchar(20) NOT NULL,
  `Description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`DiscountTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discounttypes`
--

LOCK TABLES `discounttypes` WRITE;
/*!40000 ALTER TABLE `discounttypes` DISABLE KEYS */;
INSERT INTO `discounttypes` VALUES (1,'Percentage-based','Discount is used for product as default'),(2,'Code','Discount codes are entered by the user using the code');
/*!40000 ALTER TABLE `discounttypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `existed product list`
--

DROP TABLE IF EXISTS `existed product list`;
/*!50001 DROP VIEW IF EXISTS `existed product list`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `existed product list` AS SELECT 
 1 AS `ProductID`,
 1 AS `ProductName`,
 1 AS `UnitPrice`,
 1 AS `UnitsInStock`,
 1 AS `Picture`,
 1 AS `CategoryName`,
 1 AS `DiscountID`,
 1 AS `DiscountPercent`,
 1 AS `Discontinued`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `productdetails`
--

DROP TABLE IF EXISTS `productdetails`;
/*!50001 DROP VIEW IF EXISTS `productdetails`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `productdetails` AS SELECT 
 1 AS `ProductID`,
 1 AS `ProductName`,
 1 AS `CategoryID`,
 1 AS `QuantityPerUnit`,
 1 AS `UnitPrice`,
 1 AS `UnitsInStock`,
 1 AS `Discontinued`,
 1 AS `Picture`,
 1 AS `Description`,
 1 AS `CategoryName`,
 1 AS `DiscountID`,
 1 AS `DiscountPercent`,
 1 AS `StartDate`,
 1 AS `EndDate`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `productinfos`
--

DROP TABLE IF EXISTS `productinfos`;
/*!50001 DROP VIEW IF EXISTS `productinfos`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `productinfos` AS SELECT 
 1 AS `ProductID`,
 1 AS `ProductName`,
 1 AS `CategoryID`,
 1 AS `QuantityPerUnit`,
 1 AS `UnitPrice`,
 1 AS `UnitsInStock`,
 1 AS `Discontinued`,
 1 AS `Picture`,
 1 AS `Description`,
 1 AS `CategoryName`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `ProductID` int NOT NULL AUTO_INCREMENT,
  `ProductName` varchar(40) NOT NULL,
  `CategoryID` int DEFAULT NULL,
  `QuantityPerUnit` varchar(20) DEFAULT NULL,
  `UnitPrice` decimal(10,2) DEFAULT '0.00',
  `UnitsInStock` smallint DEFAULT '0',
  `Discontinued` bit(1) NOT NULL DEFAULT b'0',
  `Picture` varchar(256) DEFAULT NULL,
  `Description` text,
  PRIMARY KEY (`ProductID`),
  KEY `FK_Products_Categories` (`CategoryID`),
  CONSTRAINT `FK_Products_Categories` FOREIGN KEY (`CategoryID`) REFERENCES `categories` (`CategoryID`),
  CONSTRAINT `CK_Products_UnitPrice` CHECK ((`UnitPrice` >= 0)),
  CONSTRAINT `CK_UnitsInStock` CHECK ((`UnitsInStock` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Chai',1,'10 boxes x 20 bags',18.00,26,_binary '\0','cd2ac241-ced1-498f-bc2b-13add216e6fb.jpg','<p style=\"line-height: var(--cib-type-body2-line-height)\"><span style=\"font-size: 16px\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Chai</strong>, also known as<strong>masala chai</strong>, is a delightful and aromatic beverage with a rich history and cultural significance. Let’s explore what chai is all about:</span></p><ol><li><p style=\"line-height: var(--cib-type-body2-line-height)\"><span style=\"font-size: 16px\"><strong>Origins and Traditional Preparation</strong>:</span></p><ul><li style=\"font-size: 16px\"><strong>Chai</strong>originated in ancient India, where it was known as<strong>masala chai</strong>. In Sanskrit, “chai” means<strong>tea</strong>, while “masala” refers to the blend of spices used in its preparation.</li><li style=\"font-size: 16px\">The base of chai is typically a<strong>black tea</strong>, with<strong>Assam</strong>being the most common variety due to its strong, full-bodied flavor. Some people blend Assam with<strong>Darjeeling tea</strong>for a balance of color, body, aroma, and flavor.</li><li><span style=\"font-size: 16px\"><strong>Spices</strong>play a crucial role in chai. Commonly used spices include:</span><ul><li style=\"font-size: 16px\"><strong>Cardamom</strong>: Provides a warm, aromatic flavor.</li><li style=\"font-size: 16px\"><strong>Cinnamon</strong>: Adds sweetness and warmth.</li><li style=\"font-size: 16px\"><strong>Ginger</strong>: Offers a spicy kick.</li><li style=\"font-size: 16px\"><strong>Cloves</strong>: Contribute depth and richness.</li><li style=\"font-size: 16px\"><strong>Black pepper</strong>,<strong>coriander</strong>,<strong>nutmeg</strong>, and<strong>fennel</strong>are also used, though less commonly.</li></ul><span style=\"font-size: 16px\"></span></li><li style=\"font-size: 16px\">Chai is typically brewed by simmering tea leaves and spices in<strong>water or milk</strong>, allowing the flavors to meld together.</li></ul></li><li><p style=\"line-height: var(--cib-type-body2-line-height)\"><span style=\"font-size: 16px\"><strong>Ayurvedic Roots</strong>:</span></p><ul><li style=\"font-size: 16px\">Chai tea has ancient<strong>Ayurvedic</strong>roots. Ayurveda, an Indian system of medicine, emphasizes balance between mind, body, and spirit.</li><li style=\"font-size: 16px\">Each spice in chai has specific healing properties, making it more than just a flavorful beverage.</li><li style=\"font-size: 16px\">The combination of spices and herbs in chai contributes to its age-old healing benefits.</li></ul></li><li><p style=\"line-height: var(--cib-type-body2-line-height)\"><span style=\"font-size: 16px\"><strong>Social Significance</strong>:</span></p><ul><li style=\"font-size: 16px\">Chai symbolizes<strong>warmth</strong>,<strong>hospitality</strong>, and<strong>connection</strong>in Indian traditions.</li><li style=\"font-size: 16px\">It’s an important social beverage, often shared among friends, family, and neighbors.</li><li style=\"font-size: 16px\">In India, you’ll find<strong>chaiwallas</strong>(vendors selling chai) on street corners, and serving chai is a common practice in households.</li></ul></li></ol>'),(2,'Chang',1,'24 - 12 oz bottles.',19.00,13,_binary '\0','0f2bfb08-84bd-4368-9e4b-17e33aaf331f.jpg','<ul><li style=\"font-size: 16px\"><strong>Chang&nbsp;</strong>is a premium Thai beer known for its smooth and refreshing taste. It’s perfect for sharing with friends during social gatherings. The beer features a distinctive logo that symbolizes Thai culture and values.</li><li style=\"font-size: 16px\">If you’re looking for a delightful beer experience, might be the one to try.</li></ul>'),(3,'Aniseed Syrup',2,'12 - 550 ml bottles',10.00,11,_binary '\0','517b0105-da18-4487-be02-8d80906a0226.jpg','<p><span style=\"font-size: 16px\">Aniseed syrup is known for its sweet, licorice-like taste and aroma. It’s commonly used in a variety of pastries and liquors globally. Ideal for enhancing the flavor of cocktails, mocktails, sodas, iced teas, lemonades, and can also be drizzled over fruit and desserts</span><br></p>'),(4,'Chef Anton\'s Cajun Seasoning',2,'48 - 6 oz jars',22.00,49,_binary '\0','74be6e22-466b-4611-8619-2885cfb08105.jpg','<p><span style=\"font-size: 16px\">Chef Anton’s Cajun Seasoning is a blend of spices that typically includes paprika, onion and garlic powder, oregano, and cayenne pepper. It’s used to add a spicy kick to dishes like gumbo, shrimp and grits, and jambalaya. The exact recipe can vary, but these ingredients form the base for this flavorful seasoning.</span></p><p><br></p>'),(5,'Chef Anton\'s Gumbo Mix',2,'36 boxes',21.35,0,_binary '','dd626b7d-fdcd-448b-9f16-5a8540237dcb.jpg',NULL),(6,'Grandma\'s Boysenberry Spread',2,'12 - 8 oz jars',25.00,120,_binary '\0','96ad8a74-99c1-4504-8cc7-c1077fd10ee9.jpg','<p><span style=\"font-size: 16px\">Grandma’s Boysenberry Spread is a jam-like product made with cane sugar, seedless boysenberries, water, pectin, and citric acid. It’s likely to have a sweet and tangy flavor characteristic of boysenberries and can be used as a spread for breads, pastries, or as a topping for desserts.</span></p>'),(7,'Uncle Bob\'s Organic Dried Pears',7,'12 - 1 lb pkgs.',30.00,15,_binary '\0','9a40a253-8c37-4feb-98cd-4da878d1d4bc.jpg','<p><span style=\"font-size: 16px\">Uncle Bob’s Organic Dried Pears are a healthy snack option, made from organic pears that are dried without the addition of any preservatives or sweeteners. They are typically sold in 1 lb packages and can be enjoyed on their own or added to cereals, baked goods, or salads for a natural sweetness and chewy texture.</span></p>'),(8,'Northwoods Cranberry Sauce',2,'12 - 12 oz jars',40.00,6,_binary '\0','175e5aa6-0519-440e-a38d-ea8785d2eacd.jpg','<p><span style=\"font-size: 16px\">Northwoods Cranberry Sauce is likely a traditional cranberry sauce, which is a condiment made from cranberries. It’s commonly served with Thanksgiving dinner in North America and Christmas dinner in the United Kingdom and Canada. The sauce is known for its tart flavor, which complements rich holiday dishes.</span><br></p>'),(9,'Mishi Kobe Niku',6,'18 - 500 g pkgs.',97.00,29,_binary '','bcbdc622-e34f-40ea-b654-acfa1b80a153.jpg',NULL),(10,'Ikura',8,'12 - 200 ml jars',31.00,31,_binary '\0','368a8366-f4b7-4a3a-8799-e5adb4688e1a.jpg','<p><span style=\"font-size: 16px\">Ikura is the Japanese term for salmon roe, also known as red caviar. These large eggs are known for their soft texture, briny flavor, and a mild fishiness. Ikura is a popular and healthy ingredient in Japanese cuisine, often served on sushi or as a standalone dish.</span><br></p>'),(11,'Queso Cabrales',4,'1 kg pkg.',21.00,22,_binary '\0','c53b309c-8f86-464f-a425-ec692503f90f.jpg','<p><span style=\"font-size: 16px\">Queso Cabrales is a blue cheese from Asturias, Spain, known for its intense flavor and creamy texture. It’s made traditionally by rural dairy farmers and can be produced from pure, unpasteurized cow’s milk or blended with goat and/or sheep milk for a spicier taste. The cheese features distinctive blue-green veins from natural mold developed during aging</span><br></p>'),(12,'Queso Manchego La Pastora',4,'10 - 500 g pkgs.',38.00,86,_binary '\0','1f1dcb3f-ed4d-4f0a-ab0c-3a2aee5dc2a5.jpg','<p><span style=\"font-size: 16px\">Queso Manchego La Pastora is a variety of Manchego cheese, which is a pressed cheese made from the milk of Manchega sheep breed. It comes from the La Mancha region of Spain and can be aged between 60 days to 2 years. This cheese is known for its rich, nutty flavor and firm texture. It’s often used in cheese platters and pairs well with fruits, nuts, and charcuterie</span><br></p>'),(13,'Konbu',8,'2 kg box',6.00,24,_binary '\0','329fb8b0-1b91-44f0-90eb-7d7ed4207283.jpg','<p><span style=\"font-size: 16px\">Konbu, also known as kombu, is an edible kelp widely used in East Asian cuisine, especially in Japan and Korea. It’s a key ingredient in making dashi, a foundational broth in Japanese cooking, and is prized for its umami flavor due to high glutamic acid content. Konbu is rich in minerals and can be used as a condiment or garnish in various dishes</span><br></p>'),(14,'Tofu',7,'40 - 100 g pkgs.',23.25,35,_binary '\0','43149946-0d51-41df-b29c-03bc87a9b4d2.jpg','<p><span style=\"font-size: 16px\">Tofu, also known as bean curd, is a food made by coagulating soy milk and then pressing the resulting curds into solid white blocks. It originates from East Asia and is a staple in many Asian cuisines. Tofu is high in protein, low in fat, and rich in calcium and iron. It has a subtle flavor, making it versatile for absorbing spices, sauces, and marinades</span><br></p>'),(15,'Genen Shouyu',2,'24 - 250 ml bottles',15.50,39,_binary '\0','bc2b3ded-5dfe-427d-b81d-0e77dfd8d7dd.jpg','<p><span style=\"font-size: 16px\">Genen Shouyu is a type of Japanese soy sauce made by Kikkoman. It is naturally brewed and has a reduced salt content compared to regular soy sauces. It contains ingredients like water, soybeans, wheat, salt, vinegar, alcohol, and sugar. Genen Shouyu is known for its balanced flavor that enhances dishes without overpowering them</span><br></p>'),(16,'Pavlova',3,'32 - 500 g boxes',17.45,29,_binary '\0','14c1f80c-e173-471b-8e39-bc91e06244b3.jpg','<p><span style=\"font-size: 16px\">Pavlova is a meringue-based dessert named after the Russian ballerina Anna Pavlova. It has origins in Australia and New Zealand, where it is considered a national delicacy. The dessert features a crisp crust with a soft, light interior, commonly topped with whipped cream and fruit. It’s celebrated for its delicate texture and is often served during holidays and special occasions</span><br></p>'),(17,'Alice Mutton',6,'20 - 1 kg tins',39.00,0,_binary '','d50e852d-c77d-45b1-9f84-066ee2aff0e2.jpg',NULL),(18,'Carnarvon Tigers',8,'16 kg pkg.',62.50,42,_binary '\0','c1490261-9698-4c9f-9d01-7d372194387e.jpg',NULL),(19,'Teatime Chocolate Biscuits',3,'10 boxes x 12 pieces',9.20,25,_binary '\0','df048fb1-34ee-4f21-89e7-508a6bbec1e7.jpg','<p><span style=\"font-size: 16px\">Tea Time Biscuits by WIBISCO are sandwich cookies with two crisp baked cookies and a layer of smooth cream filling, creating a delicious full-flavored taste. They are available in Chocolate, Vanilla, and Double Chocolate flavors and are enjoyed by people of all ages. These biscuits are known for their delightful taste and are a popular treat during tea time</span><br></p>'),(20,'Sir Rodney\'s Marmalade',3,'30 gift boxes',81.00,40,_binary '\0','94731fd4-4233-4fd2-b559-eb91af2b5c5c.jpg',NULL),(21,'Sir Rodney\'s Scones',3,'24 pkgs. x 4 pieces',10.00,3,_binary '\0','a2ae9bed-ff1d-49c8-9530-09a2bca201ef.jpg',NULL),(22,'Gustaf\'s Knäckebröd',5,'24 - 500 g pkgs.',21.00,104,_binary '\0','e14caa7e-2dc4-4b3d-9e2c-9211e3a5f2be.jpg','<p><span style=\"font-size: 16px\">Gustaf’s Knäckebröd is a type of Swedish crispbread that is large, dry, and flat, made primarily from rye flour. It’s derived from a thicker bread called spisbröd and is enjoyed in Sweden as a staple bread alternative. Its crunchy texture and thin form are similar to crackers, and it’s high in fiber and very sustaining</span></p>'),(23,'Tunnbröd',5,'12 - 250 g pkgs.',9.00,61,_binary '\0','f62644c9-8926-4922-ab42-b4d104fc8308.jpg','<p><span style=\"font-size: 16px\">Tunnbröd is a traditional Swedish flatbread that can be either soft or crisp. It’s typically round and thin, made from a mix of whole wheat, white wheat, and sometimes barley flour. Ingredients may include water, salt, and sometimes a touch of fat. It comes in many variants depending on the choice of grain, leavening agent, and rolling pin. It was created out of necessity for long-term storage but is now enjoyed in various forms.</span><br></p>'),(24,'Guaraná Fantástica',1,'12 - 355 ml cans',4.50,20,_binary '','7ac4ccd7-fd91-4b7f-8cd7-0f32798f4974.jpg',NULL),(25,'NuNuCa Nuß-Nougat-Creme',3,'20 - 450 g glasses',14.00,76,_binary '\0','4ba11a2b-4831-42c8-8dc9-d8d6a9c21a43.jpg',NULL),(26,'Gumbär Gummibärchen',3,'100 - 250 g bags',31.23,15,_binary '\0','86f47e96-6a85-4731-b133-08f24b35128b.jpg','<p><span style=\"font-size: 16px\">Gummy bears, known as Gummibär in German, are small, fruit gum candies that are roughly 2 cm long and shaped like a bear. They are a popular gelatin-based candy sold in a variety of shapes and colors</span><br></p>'),(27,'Schoggi Schokolade',3,'100 - 100 g pieces',43.90,49,_binary '\0','3dc931f3-1016-4703-996c-0d37188337ce.jpg','<p><span style=\"font-size: 16px\">Schoggi Schokolade refers to a variety of chocolate products offered by Schoggi Meier. They have a range of chocolates such as dark chocolate with homemade pistachio gianduja, caramelized hazelnuts enrobed in milk chocolate, and white chocolate with macadamia gianduja and feuilletine, among others</span><br></p>'),(28,'Rössle Sauerkraut',7,'25 - 825 g cans',45.60,26,_binary '','0a14e67a-76b3-4fbf-86fd-110b2f4851a8.jpeg',NULL),(29,'Thüringer Rostbratwurst',6,'50 bags x 30 sausgs.',123.79,0,_binary '','0957e392-795e-4a51-9045-991a3613d534.jpg',NULL),(30,'Nord-Ost Matjeshering',8,'10 - 200 g glasses',25.89,10,_binary '\0','1cc810ea-8b91-4cf9-9e69-50a36e852fe8.jpg','<p><span style=\"font-size: 16px\">Nord-Ost Matjeshering is a seafood product offered by Nord-Ost-Fisch Handelsgesellschaft mbH. It comes in 10 - 200 g glasses and seems to be categorized under seafood. Matjeshering refers to a type of herring, and it’s typically served in a variety of dishes in Northern Europe.</span><br></p>'),(31,'Gorgonzola Telino',4,'12 - 100 g pkgs',12.50,0,_binary '\0','8ed947cb-5328-46df-a488-855cae66ff47.jpg','<p><span style=\"font-size: 16px\">Gorgonzola is an Italian blue cheese known for its crumbly texture, bold flavor, and distinctive blue-green veins. It’s made from cow’s milk and can be either sweet (dolce) or spicy (piccante)</span><br></p>'),(32,'Mascarpone Fabioli',4,'24 - 200 g pkgs.',32.00,9,_binary '\0','a1457446-8f06-4f96-b44f-a976cf5f153b.jpg','<p><span style=\"font-size: 16px\">Mascarpone is a soft Italian cream cheese known for its rich, creamy texture and natural sweetness. It’s often used in desserts like tiramisu or as a spread. There’s also a product called “Mascarpone 40” by Fabbri, which is a flavoring powder that gives ice cream and confectionery preparations the taste of mascarpone</span><br></p>'),(33,'Geitost',4,'500 g',2.50,112,_binary '\0','89cb7382-e4b7-418d-8512-d3a536b3b4e1.jpg','<p><span style=\"font-size: 16px\">Geitost, also known as Brunost or Gjetost, is a Norwegian cheese that is typically made with whey, milk, and/or cream. It has a unique caramelized flavor and is made from a combination of cow’s milk and goat’s milk, or purely from goat’s milk</span><br></p>'),(34,'Sasquatch Ale',1,'24 - 12 oz bottles',14.00,111,_binary '\0','27ec47d4-74d7-47f1-bc95-b46090bb98f0.jpg','<p><span style=\"font-size: 16px\">Sasquatch Ale is an Imperial IPA style beer brewed by Six Rivers Brewery. It has a translucent, medium dark orange and reddish appearance with a full, deeply caramelized, medium toast barley malt and spiced floral hops aroma. The beer has a good amount of body and a notable bite in the hop department</span><br></p>'),(35,'Steeleye Stout',1,'24 - 12 oz bottles',18.00,20,_binary '\0','677da96d-278c-4c8d-b989-728340d4b48d.jpg',NULL),(36,'Inlagd Sill',8,'24 - 250 g  jars',19.00,112,_binary '\0','28be74d0-493c-40fc-8e25-1621ac981408.jpg','<p><span style=\"font-size: 16px\">Inlagd Sill is a traditional Swedish dish of pickled herring that is commonly served at Midsummer, Christmas, and Easter celebrations. The dish consists of cleaned, skinned, and salted herring that is soaked in a marinade of vinegar, sugar, chopped onions and carrots, allspice, bay leaves, pepper, and crushed peppercorns1. It’s often featured on a Swedish smörgåsbord but can also be enjoyed with fresh new potatoes and sometimes soured cream</span><br></p>'),(37,'Gravad lax',8,'12 - 500 g pkgs.',26.00,11,_binary '\0','34beb1e0-b1d2-49d5-a212-921f413c01e4.jpg','<p><span style=\"font-size: 16px\">Gravad lax, also known as gravlax, is a traditional Nordic dish consisting of salmon that is cured using a mix of salt, sugar, and dill. It’s typically served with a mustard sauce and garnished with fresh dill. The name translates to “buried salmon,” which refers to the old method of curing the fish by burying it in the ground.</span><br></p>'),(38,'Côte de Blaye',1,'12 - 75 cl bottles',263.50,17,_binary '\0','69aae184-70a1-4dd4-b58c-47d2eca004b7.jpg','<p><span style=\"font-size: 16px\">Côtes de Blaye are white wines from the Blaye appellation in Bordeaux, France. They’re made mostly from Colombard and Ugni blanc grapes, resulting in dry, light, and mildly fruity wines</span><br></p>'),(39,'Chartreuse verte',1,'750 cc per bottle',18.00,69,_binary '\0','1b161792-7007-4762-9c12-dc006b389402.jpg','<p><span style=\"font-size: 16px\">Chartreuse verte, or Green Chartreuse, is an herbal liqueur crafted by Carthusian monks in the French Alps from a 400-year-old recipe. It’s the only liqueur with a completely natural green color and is made with 130 herbs, plants, and flowers</span><br></p>'),(40,'Boston Crab Meat',8,'24 - 4 oz tins',18.40,123,_binary '\0','3fbb7900-6fa7-47e8-aeb2-1765725512b8.jpg','<p><span style=\"font-size: 16px\">Boston Crab Meat typically refers to the meat from crabs that are found in the waters around Boston, known for its sweet, delicate flavor and tender texture. In Boston, you can find fresh crab meat at various seafood markets and restaurants, often used in dishes like crab cakes, salads, or served steamed with butter.</span></p>'),(41,'Jack\'s New England Clam Chowder',8,'12 - 12 oz cans',9.65,85,_binary '\0','02f1b4ce-0453-4ed0-9ec4-d38f25e38963.jpg','<p><span style=\"font-size: 16px\">New England Clam Chowder, also known as Boston Clam Chowder, is a creamy stew famous for its rich and hearty flavor. It typically contains tender clams, diced potatoes, onions, sometimes celery, and is seasoned with salt pork or bacon. The chowder is known for its thick consistency and is often served with oyster crackers.</span><br></p>'),(42,'Singaporean Hokkien Fried Mee',5,'32 - 1 kg pkgs.',14.00,26,_binary '','01f9564c-f58d-4e31-bfd3-8cfc184c4f81.jpg',NULL),(43,'Ipoh Coffee',1,'16 - 500 g tins',46.00,17,_binary '\0','f1e3a531-f48c-40df-a397-85587b2f57c1.jpg','<p><span style=\"font-size: 16px\">Ipoh Coffee, often referred to as Ipoh White Coffee, is a renowned coffee drink from Ipoh, Perak, Malaysia. The coffee beans are roasted with palm oil margarine, which gives the coffee a unique, creamy flavor and reduces its bitterness. It’s typically served with condensed milk, resulting in a rich and smooth taste. The drink’s name comes from the lighter color achieved through this roasting process.</span><br></p>'),(44,'Gula Malacca',2,'20 - 2 kg bags',19.45,27,_binary '\0','1599978d-3223-4807-9e98-5d85bef59d40.jpg','<p><span style=\"font-size: 16px\">Gula Malacca, commonly known as Gula Melaka or palm sugar, is a sweetener derived from the sap of palm trees, particularly the coconut palm. It’s widely used in Southeast Asian cuisine for its rich, caramel-like flavor. The sap is boiled down and then hardened into cylindrical blocks or granules. It’s darker and has a more intense flavor compared to other types of palm sugar.</span><br></p>'),(45,'Rogede sild',8,'1k pkg.',9.50,5,_binary '\0','69b7d844-7261-4cef-9b53-25bdd02a2a91.jpg','<p><span style=\"font-size: 16px\">“Rogede sild” refers to a Danish dish involving herring. “Sild” is the Danish word for herring, and “rogede” suggests that the herring may be smoked. Typically, this dish would involve smoked herring served in various ways, often as part of traditional Danish smørrebrød (open-faced sandwiches)</span><br></p>'),(46,'Spegesild',8,'4 - 450 g glasses',12.00,95,_binary '\0','abde05da-9e13-4ea8-adad-3c17d8590653.jpg','<p><span style=\"font-size: 16px\">Spegesild is a traditional Scandinavian dish, specifically Norwegian, where herring is preserved using salt curing. The process involves using salt to extract water from the herring, creating an environment that inhibits microbial growth. This method of preservation allows the herring to be stored for extended periods. The best quality of spegesild is known as “diamanter” and is produced in Norway and Iceland from Atlantic herring with a high fat content.</span><br></p>'),(47,'Zaanse koeken',3,'10 - 4 oz boxes',9.50,36,_binary '\0','e68075e9-816f-44b0-ab74-6f09a272728b.jpeg','<p><span style=\"font-size: 16px\">“Zaanse koeken” refers to cookies or biscuits that originate from the Zaan region in the Netherlands. The term “koeken” is Dutch for cookies, and these are often traditional recipes that may include ingredients like almonds. They are typically enjoyed with coffee or tea.</span></p>'),(48,'Chocolade',3,'10 pkgs.',12.75,15,_binary '\0','71732701-f0f0-4916-ab0b-0a433aed9bd1.jpg','<p><span style=\"font-size: 16px\">Chocolade, known as chocolate in English, is a food product made from cocoa beans. It is consumed as candy and used to make beverages and to flavor or coat various confections and bakery products. Chocolate is rich in carbohydrates and is an excellent source of quick energy. It has several health benefits and comes in various forms such as liquid, solid, or paste. The process of making chocolate involves fermenting, drying, and roasting the cacao seeds, which gives chocolate its distinctive taste.</span><br></p>'),(49,'Maxilaku',3,'24 - 50 g pkgs.',20.00,10,_binary '\0','d3aee32e-d449-4d4d-aab4-015daa089eac.jpg',NULL),(50,'Valkoinen suklaa',3,'12 - 100 g bars',16.25,65,_binary '\0','424a53df-3125-4a61-b63e-f98dbd8d5506.jpg','<p><span style=\"font-size: 16px\">Valkoinen suklaa, or white chocolate in English, is a confection made from cocoa butter, sugar, and milk solids. Unlike dark and milk chocolates, white chocolate does not contain cocoa solids, which is why it has a pale color and a different flavor profile. It’s known for its creamy texture and sweet, buttery taste with hints of vanilla. White chocolate is used in various desserts and confections, such as mousse, cakes, and candies. It’s also enjoyed on its own as a sweet treat.</span></p>'),(51,'Manjimup Dried Apples',7,'50 - 300 g pkgs.',53.00,20,_binary '\0','071b1ca3-4134-48b5-babc-18b0750eaff1.jpg','<p><span style=\"font-size: 16px\">Manjimup is a region in Western Australia known for its rich soils and diverse produce, including apples. While the search results don’t provide specific details about “Manjimup Dried Apples,” it’s likely that these are apples that have been dried for preservation and snacking. Dried apples are a popular healthy snack and can be used in various recipes.</span><br></p>'),(52,'Filo Mix',5,'16 - 2 kg boxes',7.00,38,_binary '\0','78f1807b-9534-4d2e-b3e8-efd549504015.png',NULL),(53,'Perth Pasties',6,'48 pieces',32.80,0,_binary '','90936ff1-eb60-4bc5-a2b4-15edb0c473f3.jpg',NULL),(54,'Tourtière',6,'16 pies',7.45,21,_binary '\0','1cc9a005-e0ae-4eca-8ccd-a3cc4f0ab552.jpeg','<p><span style=\"font-size: 16px\">Tourtière is a traditional French-Canadian meat pie that typically features a double crust and a filling made from ground or chopped meats like pork, beef, or game (such as rabbit, pheasant, or moose). It’s often seasoned with spices like cloves, cinnamon, and nutmeg, and may include vegetables and herbs. This savory pie is particularly popular during the holiday season in Quebec and other parts of Canada.</span><br></p>'),(55,'Pâté chinois',6,'24 boxes x 2 pies',24.00,115,_binary '\0','4bb45d0c-c379-4685-baee-aacde9483d28.jpg','<p><span style=\"font-size: 16px\">Pâté chinois is a Quebecois dish that is similar to shepherd’s pie or the French “hachis Parmentier.” It consists of layered ground beef (sometimes mixed with sautéed diced onions) on the bottom, canned corn (either whole-kernel, creamed, or a mixture) in the middle, and mashed potatoes on top. This comfort food staple is often seasoned and baked, and it’s traditionally served with tomato ketchup on the side. It’s a popular dish in Quebec and can be found in most cafeterias throughout the region.</span><br></p>'),(56,'Gnocchi di nonna Alice',5,'24 - 250 g pkgs.',38.00,21,_binary '\0','37b8fbac-6c3d-4068-af81-6e7a5c4e27d8.jpg','<p><span style=\"font-size: 16px\">“Gnocchi di nonna Alice” seems to be a specific or traditional recipe for gnocchi, which is an Italian potato dumpling dish. While I couldn’t find a recipe specifically named “Gnocchi di nonna Alice,” the term “Nonna” is Italian for grandmother, and it’s common for gnocchi recipes to be passed down through generations in Italian families. Traditional gnocchi is made with mashed potatoes, flour, and eggs, and can be served with various sauces.</span><br></p>'),(57,'Ravioli Angelo',5,'24 - 250 g pkgs.',19.50,36,_binary '\0','51ea6d25-fc9b-4ead-9468-a71d5f0fae0c.jpg','<p><span style=\"font-size: 16px\">“Ravioli Angelo” could refer to a specific recipe or style of ravioli. While I couldn’t find a precise match for this term, the name Angelo is associated with a restaurant called “Charlie Gitto’s ‘On The Hill’” (formerly known as “Angelo’s”) in St. Louis, Missouri, where toasted ravioli was made famous. Toasted ravioli, also known as T-Ravs, is breaded deep-fried ravioli typically served as an appetizer.<br><br>Ravioli itself is a classic Italian pasta filled with various ingredients like cheese, meat, vegetables, or seafood.?</span><br></p>'),(58,'Escargots de Bourgogne',8,'24 pieces',13.25,62,_binary '\0','83e5fdbc-e4c9-418c-b1bb-181f4b553321.jpg','<p><span style=\"font-size: 16px\">“Escargots de Bourgogne” is a traditional French dish originating from the Burgundy region. It consists of snails (specifically the Helix pomatia species, also known as Burgundy snails) that are prepared with garlic and parsley butter. This dish is widely recognized as a refined French gastronomic specialty and is known for its flavorful taste</span><br></p>'),(59,'Raclette Courdavault',4,'5 kg pkg.',55.00,79,_binary '\0','856afd44-021d-4778-8c66-f9db748d8747.jpg',NULL),(60,'Camembert Pierrot',4,'15 - 300 g rounds',34.00,19,_binary '\0','294a208b-fe56-444c-9ec5-ce3bfcbd728d.jpg','<p><span style=\"font-size: 16px\">“Camembert Pierrot” does not appear in the search results as a widely recognized term or brand. However, Camembert itself is a famous soft, creamy cheese that originated in the late 18th century in Normandy, France. It’s known for its moist, soft texture and creamy, buttery center, with an ivory-colored exterior and a downy white surface</span><br></p>'),(61,'Sirop d\'érable',2,'24 - 500 ml bottles',28.50,113,_binary '\0','adbbb711-fadc-400a-8479-8dc6d8c7fb66.jpg','<p><span style=\"font-size: 16px\">“Sirop d’érable” is the French term for maple syrup, a naturally sweet solution made from the sap of maple trees. The sap is collected in early spring and concentrated by boiling or reverse osmosis to produce the syrup. Maple syrup is emblematic of Canadian cuisine and is believed to originate from North America</span><br></p>'),(62,'Tarte au sucre',3,'48 pies',49.30,17,_binary '\0','8aef518a-6174-457e-ab8b-aaa9517fa695.jpg','<p><span style=\"font-size: 16px\">“Tarte au sucre,” also known as French Canadian Sugar Pie, is a traditional dessert from Quebec. It features a crust and a sweet, creamy filling made with ingredients such as brown sugar or maple syrup, cream, and lots of butter. The result is a moist and decadent pie with a caramel-like texture after baking</span><br></p>'),(63,'Vegie-spread',2,'15 - 625 g jars',43.90,24,_binary '\0','9092b891-5f65-47fe-87a1-9d5881df9391.jpg','<p><span style=\"font-size: 16px\">“Vegie-spread” refers to a variety of vegetable-based spreads that can be used on bagels, toast, sandwiches, wraps, or as a dip for crackers or raw vegetables. These spreads are typically made with fresh veggies and may include ingredients like cream cheese (dairy or dairy-free), herbs, and spices to create a creamy and tangy flavor</span><br></p>'),(64,'Wimmers gute Semmelknödel',5,'20 bags x 4 pieces',33.25,22,_binary '\0','9c0f4231-37e1-4c6c-9c9a-4164ff450e15.jpg','<p><span style=\"font-size: 16px\">“Semmelknödel” are traditional German bread dumplings made from stale bread rolls, warm milk, seasoning, and eggs. They are a versatile and nutritious side dish in German cuisine, often served with gravies or sauces</span><br></p>'),(65,'Louisiana Fiery Hot Pepper Sauce',2,'32 - 8 oz bottles',21.05,76,_binary '\0','353e6430-8abd-4f3a-b94d-426c4584eaa2.jpg','<p><span style=\"font-size: 16px\">“Louisiana Fiery Hot Pepper Sauce” likely refers to a type of hot sauce that is known for its fiery heat and robust flavor. Louisiana hot sauce typically features cayenne peppers, vinegar, salt, and sometimes garlic. It’s a beloved condiment in Louisiana cuisine and has become popular worldwide</span><br></p>'),(66,'Louisiana Hot Spiced Okra',2,'24 - 8 oz jars',17.00,4,_binary '\0','33ae9b98-85c2-493c-a075-9f7397b8426a.jpg','<p><span style=\"font-size: 16px\">“Louisiana Hot Spiced Okra” likely refers to a dish where okra is cooked with Louisiana-style spices, giving it a spicy kick. The dish often includes ingredients like cayenne, onions, tomatoes, and sometimes celery. It can be served warm and is typically smothered or stewed</span><br></p>'),(67,'Laughing Lumberjack Lager',1,'24 - 12 oz bottles',14.00,52,_binary '\0','57acb063-5d27-4af3-9bd6-922f6968ed22.jpg','<p><span style=\"font-size: 16px\">“Laughing Lumberjack Lager” is an American Amber Lager style beer. It’s brewed by Shooter ‘Franz’ McLovin, also known as Johnny Golf or The Archduke of Golf, at his Midtown Brewery in Atlanta, Georgia. Lagers are beers that are fermented and conditioned at low temperatures, known for their crisp and refreshing taste.</span><br></p>'),(68,'Scottish Longbreads',3,'10 boxes x 8 pieces',12.50,6,_binary '\0','cb54a8e6-3096-41bc-af42-2dc5d68fabf0.jpg','<p><span style=\"font-size: 16px\">Scottish Shortbread, often simply called ‘shortbread,’ is a traditional Scottish biscuit known for its crumbly texture and rich buttery flavor. It’s typically made from one part white sugar, two parts butter, and three to four parts plain wheat flour, without any leavening agents like baking powder or baking soda. This treat is a staple in Scottish cuisine and has been enjoyed for centuries</span><br></p>'),(69,'Gudbrandsdalsost',4,'10 kg pkg.',36.00,26,_binary '\0','600fcaf0-db8d-402c-ab94-a4553603be91.jpg','<p><span style=\"font-size: 16px\">Gudbrandsdalsost, also known as Norwegian brown cheese or ‘brunost,’ is a whey cheese made from a pasteurized mixture of cow’s and goat’s milk. It has a soft and aromatic character with a fat content of 35%. This cheese is compressed and sold in cubes, offering a unique flavor that’s a staple in Norwegian cuisine. It’s been enjoyed since 1863 and is commonly used for breakfast, lunch, or as a snack</span><br></p>'),(70,'Outback Lager',1,'24 - 355 ml bottles',15.00,15,_binary '\0','cfb66fc1-2569-4e2b-b439-9a8f2082dfe0.jpg','<p><span style=\"font-size: 16px\">Outback Lager appears to be a craft beer, possibly part of a range called ‘Outback’ by a brewing company. While I couldn’t find specific details about Outback Lager, lagers in general are a type of beer that is fermented and conditioned at low temperatures, known for their clean, crisp taste</span><br></p>'),(71,'Flotemysost',4,'10 - 500 g pkgs.',21.50,26,_binary '\0','d6152885-9017-4e27-ac43-ddf73334328c.jpg','<p><span style=\"font-size: 16px\">Flotemysost, also known as Fløytemysost, is a type of Norwegian brown cheese (brunost) made from cow’s milk. It has a mild taste and bright color, which makes it very popular. It’s lighter and milder compared to its counterpart Geitost, which is made from goat’s milk and has a stronger taste. Flotemysost can be enjoyed on bread, crispbread, waffles, or used in pots and sauces for a touch of caramel flavor</span><br></p>'),(72,'Mozzarella di Giovanni',4,'24 - 200 g pkgs.',34.80,14,_binary '\0','a39a1b88-e223-4af9-850c-14cdb2b6df9c.jpg','<p><span style=\"font-size: 16px\">“Mozzarella di Giovanni” does not appear in the search results, so it might be a specific brand or artisanal product. Generally, mozzarella is a traditional southern Italian cheese known for its soft, moist texture and mild yet tangy taste. It’s commonly made from the milk of water buffalos or cows and is popular worldwide for its unique production process and versatility in dishes</span></p>'),(73,'Röd Kaviar',8,'24 - 150 g jars',15.00,101,_binary '\0','76f0171d-f2ba-4857-9fef-1d477d89c3d8.jpg','<p><span style=\"font-size: 16px\">Röd Kaviar, or red caviar, is made from the roe of salmonid fishes like salmon, trout, graylings, and char. It has an intense reddish hue and is distinct from black caviar, which comes from sturgeon. Red caviar has a rich history dating back to 12th-century Russia and is now a global delicacy enjoyed in various cuisines, including Russian and Japanese</span><br></p>'),(74,'Longlife Tofu',7,'5 kg pkg.',10.00,4,_binary '\0','d4a6d838-4146-4e0c-be01-da95933063b5.jpg','<p><span style=\"font-size: 16px\">“Longlife Tofu” might refer to tofu that has a longer shelf life due to its packaging or processing methods. Generally, fresh tofu can last 3-5 days beyond its sell-by date if stored properly, and shelf-stable tofu can last for 6 months to a year from the date of production</span></p>'),(75,'Rhönbräu Klosterbier',1,'24 - 0.5 l bottles',7.75,125,_binary '\0','1a855faf-a738-48a8-b0e3-5cc4e07bb7df.jpg','<p><span style=\"font-size: 16px\">Rhönbräu Klosterbier is a traditional beer from the Klosterbrauerei Kreuzberg, located in the Rhön region of Bavaria, Germany. The brewery is situated in a Franciscan monastery and has been a popular destination for centuries. It boasts a nearly 300-year history and offers a variety of beers brewed in accordance with monastic traditions. The Rhön Valley, where the monastery is located, is also known for its scenic hiking trails and beautiful monastic grounds.</span><br></p>'),(76,'Lakkalikööri',1,'500 ml',18.00,57,_binary '\0','2c75c337-1645-4b00-89a4-957cea80486a.jpg','<p><span style=\"font-size: 16px\">Lakkalikööri, also known as Lakka, is a Finnish liqueur that gets its distinct flavor from cloudberries, a fruit native to cool temperate regions, alpine and arctic tundra, and boreal forests. The word “lakka” means cloudberry in Finnish. This liqueur is typically made by steeping cloudberries in neutral grain alcohol for several months and is known for its sweet, almost floral flavor profile</span><br></p>'),(77,'Original Frankfurter grüne Soße',2,'12 boxes',13.00,32,_binary '\0','d19265d2-f70b-4d05-aacc-84f3bcd1318e.jpg','<p><span style=\"font-size: 16px\">Original Frankfurter grüne Soße is a famous cold herb sauce from Frankfurt, Germany. It’s made with a blend of sour cream, spices, and traditionally seven specific herbs: borage, chervil, garden cress, parsley, salad burnet, sorrel, and chives. At least 70% of these herbs should have been grown in Frankfurt to be considered authentic. The sauce is typically served with boiled potatoes and eggs and is known for its fresh and tangy flavor profile</span><br></p>');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `products by category`
--

DROP TABLE IF EXISTS `products by category`;
/*!50001 DROP VIEW IF EXISTS `products by category`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `products by category` AS SELECT 
 1 AS `CategoryName`,
 1 AS `ProductName`,
 1 AS `QuantityPerUnit`,
 1 AS `UnitsInStock`,
 1 AS `Discontinued`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `productsdiscounts`
--

DROP TABLE IF EXISTS `productsdiscounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productsdiscounts` (
  `DiscountID` varchar(36) NOT NULL,
  `ProductID` int NOT NULL,
  PRIMARY KEY (`DiscountID`,`ProductID`),
  KEY `ProductID` (`ProductID`),
  CONSTRAINT `productsdiscounts_ibfk_1` FOREIGN KEY (`DiscountID`) REFERENCES `discounts` (`DiscountID`),
  CONSTRAINT `productsdiscounts_ibfk_2` FOREIGN KEY (`ProductID`) REFERENCES `products` (`ProductID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productsdiscounts`
--

LOCK TABLES `productsdiscounts` WRITE;
/*!40000 ALTER TABLE `productsdiscounts` DISABLE KEYS */;
INSERT INTO `productsdiscounts` VALUES ('590a6dd2-baf2-4b64-8bd6-75f3874dea8a',1),('8997ccff-e004-4627-a897-df7a6012f563',1),('8d9ec5ca-67ac-4e1b-b05d-f8e2bad66c7a',1),('b6ff6ba9-6dd3-408d-9acc-af4dcc2b90c3',1),('cd3507b7-f023-4a06-8dd4-b776520d266d',1),('d7f2059b-97ef-4edc-9544-11859ea4eb4f',1),('0a405179-7c98-429d-b564-5bbd75864fe3',2),('3be96477-95de-4bfd-8be7-60fad2020e84',3),('57366b38-e463-4536-bcd8-5535d01cd4b8',3),('f12ace16-8ce7-43a4-b5fa-da0fe4070e4c',4);
/*!40000 ALTER TABLE `productsdiscounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'product'
--

--
-- Dumping routines for database 'product'
--
/*!50003 DROP PROCEDURE IF EXISTS `AddCategory` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddCategory`(
  IN categoryname VARCHAR(15),
  IN description TEXT,
  IN picture VARCHAR(256),
  OUT result BIT
)
BEGIN
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET result = 0;
  END;

  START TRANSACTION;
  IF NOT EXISTS (SELECT * FROM Categories c WHERE c.CategoryName = categoryname) THEN
    INSERT INTO Categories (CategoryName, Description, Picture)
    VALUES (categoryname, description, picture);
    SET result = 1;
  ELSE
    SET result = 0;
  END IF;
  COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InsertDiscount` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertDiscount`(
  IN discountID VARCHAR(36),
  IN discountPercent INT,
  IN discountCode TEXT,
  IN startDate DATETIME,
  IN endDate DATETIME,
  IN active BIT,
  IN discountType VARCHAR(20),
  IN productID INT,
  OUT result BIT
)
BEGIN
  DECLARE discountTypeID INT;
  DECLARE percentageBasedID INT;

  IF (SELECT COUNT(1) FROM DiscountTypes dt WHERE dt.DiscountType = discountType) > 0 THEN
    START TRANSACTION;
    BEGIN
      SELECT discountTypeID = dt.DiscountTypeID FROM DiscountTypes dt WHERE dt.DiscountType = discountType;
      SELECT percentageBasedID = dt.DiscountTypeID FROM DiscountTypes dt WHERE dt.DiscountType = 'Percentage-based';

      IF (SELECT COUNT(1) FROM DiscountDetails d JOIN ProductsDiscounts pd ON d.DiscountID = pd.DiscountID
          WHERE d.DiscountType = 'Percentage-based' AND discountType = d.DiscountType AND active = 1 AND pd.ProductID = productID) > 0
          AND active = 1 THEN
        UPDATE Discounts d JOIN ProductsDiscounts pd ON d.DiscountID = pd.DiscountID
        SET d.Active = 0
        WHERE d.DiscountTypeID = percentageBasedID AND pd.ProductID = productID AND d.Active = 1;
      END IF;

      INSERT INTO Discounts (DiscountID, DiscountPercent, StartDate, EndDate, DiscountCode, Active, DiscountTypeID)
      VALUES (discountID, discountPercent, startDate, endDate, discountCode, active, discountTypeID);
      INSERT INTO ProductsDiscounts (DiscountID, ProductID) VALUES (discountID, productID);

      SET result = 1;
      COMMIT;
    END;
  ELSE
    SET result = 0;
    SELECT 'Error: the provided discountType does not exist' AS error_message;
  END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InsertProduct` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertProduct`(
  IN productName VARCHAR(40),
  IN quantityPerUnit VARCHAR(20),
  IN unitPrice DECIMAL(10, 2),
  IN unitsInStock SMALLINT,
  IN discontinued BIT,
  IN picture VARCHAR(256),
  IN description TEXT,
  IN categoryID INT,
  OUT result BIT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET result = 0;
  END;
  START TRANSACTION;
  BEGIN
    INSERT INTO Products (ProductName, QuantityPerUnit, UnitPrice, UnitsInStock, Discontinued, Picture, Description, CategoryID)
    VALUES (productName, quantityPerUnit, unitPrice, unitsInStock, discontinued, picture, description, categoryID);
    SET result = 1;
    COMMIT;
  END;
  
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `TenMostExpensiveProducts` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `TenMostExpensiveProducts`()
BEGIN
  SELECT p.ProductName AS TenMostExpensiveProducts, p.UnitPrice
  FROM Products p
  ORDER BY p.UnitPrice DESC
  LIMIT 10;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateDiscount` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateDiscount`(
  IN discountID VARCHAR(36),
  IN discountPercent INT,
  IN discountCode VARCHAR(255),
  IN startDate DATETIME,
  IN endDate DATETIME,
  IN active BIT,
  IN discountType VARCHAR(20),
  OUT result BIT
)
BEGIN
  DECLARE discountTypeID INT;
  DECLARE productID INT;
  DECLARE percentageBasedID INT;
  DECLARE exit handler FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET result = 0;
  END;

  START TRANSACTION;
  BEGIN
    IF (SELECT COUNT(1) FROM DiscountTypes dt WHERE dt.DiscountType = discountType) > 0 THEN
       SELECT dt.DiscountTypeID INTO discountTypeID
      FROM DiscountTypes dt
      WHERE dt.DiscountType = discountType;

      SELECT pd.ProductID INTO productID
      FROM ProductsDiscounts pd
      JOIN Discounts d ON pd.DiscountID = d.DiscountID
      WHERE d.DiscountID = discountID;

      SELECT dt.DiscountTypeID INTO percentageBasedID
      FROM DiscountTypes dt
      WHERE dt.DiscountType = 'Percentage-based';

      IF ((SELECT COUNT(1)
           FROM DiscountDetails d
           JOIN ProductsDiscounts pd ON d.DiscountID = pd.DiscountID
           WHERE d.DiscountType = 'Percentage-based'
             AND discountType = d.DiscountType
             AND pd.ProductID = productID
             AND d.Active = 1) > 0
          AND active = 1) THEN
        UPDATE Discounts d
        JOIN ProductsDiscounts pd ON d.DiscountID = pd.DiscountID
        SET d.Active = 0
        WHERE d.DiscountTypeID = percentageBasedID
          AND pd.ProductID = productID
          AND d.Active = 1;
      END IF;

      UPDATE Discounts d
      SET d.DiscountPercent = discountPercent,
          d.StartDate = startDate,
          d.EndDate = endDate,
          d.DiscountCode = discountCode,
          d.Active = active,
          d.DiscountTypeID = discountTypeID
      WHERE d.DiscountID = discountID;

      SET result = 1;
    ELSE
      SET result = 0;
      SELECT 'Error: the provided discountType does not exist' AS error_message;
    END IF;
  END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateDiscountStatus` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateDiscountStatus`(
  IN discountID VARCHAR(36),
  IN active BIT,
  OUT result BIT
)
BEGIN
  DECLARE discountTypeID INT;
  DECLARE percentageBasedID INT;
  DECLARE exit handler FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET result = 0;
  END;

  START TRANSACTION;
  BEGIN
    SELECT d.DiscountTypeID INTO discountTypeID
    FROM Discounts d
    WHERE d.DiscountID = discountID;

    SELECT dt.DiscountTypeID INTO percentageBasedID
    FROM DiscountTypes dt
    WHERE dt.DiscountType = 'Percentage-based';

    IF (SELECT COUNT(1)
        FROM Discounts d
        JOIN DiscountTypes dt ON d.DiscountTypeID = dt.DiscountTypeID
        WHERE dt.DiscountType = 'Percentage-based'
          AND discountTypeID = dt.DiscountTypeID
          AND Active = 1) > 0 THEN
      UPDATE Discounts d
      SET d.Active = 0
      WHERE d.DiscountTypeID = percentageBasedID;
    END IF;

    UPDATE Discounts d
    SET d.Active = active
    WHERE d.DiscountID = discountID;

    SET result = 1;
    COMMIT;
  END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateProduct` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateProduct`(
  IN productID INT,
  IN productName VARCHAR(40),
  IN quantityPerUnit VARCHAR(20),
  IN unitPrice DECIMAL(10, 2),
  IN unitsInStock SMALLINT,
  IN discontinued BIT,
  IN picture VARCHAR(256),
  IN description TEXT,
  IN categoryID INT,
  OUT result BIT
)
BEGIN
  DECLARE exit handler FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET result = 0;
  END;

  START TRANSACTION;
  BEGIN
    UPDATE Products p
    SET p.ProductName = productName, p.QuantityPerUnit = quantityPerUnit,
        p.UnitPrice = unitPrice, p.UnitsInStock = unitsInStock,
        p.Discontinued = discontinued, p.picture = picture,
        p.Description = description, p.CategoryID = categoryID
    WHERE p.ProductID = productID;

    SET result = 1;
    COMMIT;
  END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateProductsUnitsInStock` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateProductsUnitsInStock`(
  IN productIDs TEXT,
  IN unitsInStocks TEXT,
  OUT result BIT
)
BEGIN
  DECLARE exit handler FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET result = 0;
  END;

  START TRANSACTION;
  BEGIN
    DROP TEMPORARY TABLE IF EXISTS listProductIDs;
    CREATE TEMPORARY TABLE listProductIDs (IndexId INT AUTO_INCREMENT, ProductID INT, PRIMARY KEY (IndexId));
    INSERT INTO listProductIDs (ProductID) SELECT DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(productIDs, ',', n), ',', -1) AS ProductID
    FROM (SELECT @curRow := @curRow + 1 AS n FROM (SELECT 1) r, (SELECT @curRow := 0) AS init WHERE @curRow < LENGTH(productIDs) - LENGTH(REPLACE(productIDs, ',', '')) + 1) AS numbers;

    DROP TEMPORARY TABLE IF EXISTS listUnitsInstocks;
    CREATE TEMPORARY TABLE listUnitsInstocks (IndexId INT AUTO_INCREMENT, UnitsInStocks INT, PRIMARY KEY (IndexId));
    INSERT INTO listUnitsInstocks (UnitsInStocks) SELECT DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(unitsInStocks, ',', n), ',', -1) AS UnitsInStocks
    FROM (SELECT @curRow := @curRow + 1 AS n FROM (SELECT 1) r, (SELECT @curRow := 0) AS init WHERE @curRow < LENGTH(unitsInStocks) - LENGTH(REPLACE(unitsInStocks, ',', '')) + 1) AS numbers;

    UPDATE Products p
    JOIN listProductIDs lp ON p.ProductID = lp.ProductID
    JOIN listUnitsInstocks lu ON lp.IndexId = lu.IndexId
    SET p.UnitsInStock = lu.UnitsInStocks;

    SET result = 1;
    COMMIT;
  END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateProductUnitsInStock` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateProductUnitsInStock`(
  IN productID INT,
  IN unitsInStock INT,
  OUT result BIT
)
BEGIN
  DECLARE exit handler FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET result = 0;
  END;

  START TRANSACTION;
  BEGIN
    UPDATE Products p
    SET p.UnitsInStock = unitsInStock
    WHERE p.ProductID = productID;

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
-- Final view structure for view `current product list`
--

/*!50001 DROP VIEW IF EXISTS `current product list`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `current product list` AS select `p`.`ProductID` AS `ProductID`,`p`.`ProductName` AS `ProductName`,`p`.`UnitPrice` AS `UnitPrice`,`p`.`UnitsInStock` AS `UnitsInStock`,`p`.`Picture` AS `Picture`,`p`.`CategoryName` AS `CategoryName`,`d`.`DiscountID` AS `DiscountID`,ifnull(`d`.`DiscountPercent`,0) AS `DiscountPercent` from (`productinfos` `p` left join (`productsdiscounts` `pd` join `currentvaliddiscountpercentagebased` `d` on((`pd`.`DiscountID` = `d`.`DiscountID`))) on((`p`.`ProductID` = `pd`.`ProductID`))) where (`p`.`Discontinued` = 0) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `current valid discounts`
--

/*!50001 DROP VIEW IF EXISTS `current valid discounts`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `current valid discounts` AS select `d`.`DiscountID` AS `DiscountID`,`d`.`DiscountPercent` AS `DiscountPercent`,`d`.`DiscountCode` AS `DiscountCode`,`d`.`StartDate` AS `StartDate`,`d`.`EndDate` AS `EndDate`,`d`.`Active` AS `Active`,`dt`.`DiscountType` AS `DiscountType` from (`discounts` `d` join `discounttypes` `dt` on((`d`.`DiscountTypeID` = `dt`.`DiscountTypeID`))) where ((cast(now() as datetime) between `d`.`StartDate` and `d`.`EndDate`) and (`d`.`Active` = 1)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `currentvaliddiscountpercentagebased`
--

/*!50001 DROP VIEW IF EXISTS `currentvaliddiscountpercentagebased`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `currentvaliddiscountpercentagebased` AS select `d`.`DiscountID` AS `DiscountID`,`d`.`DiscountPercent` AS `DiscountPercent`,`dt`.`DiscountType` AS `DiscountType`,`d`.`DiscountCode` AS `DiscountCode`,`d`.`StartDate` AS `StartDate`,`d`.`EndDate` AS `EndDate`,`d`.`Active` AS `Active` from (`discounts` `d` join `discounttypes` `dt` on((`d`.`DiscountTypeID` = `dt`.`DiscountTypeID`))) where ((`dt`.`DiscountType` = 'percentage-based') and (`d`.`Active` = 1) and (cast(now() as datetime) between `d`.`StartDate` and `d`.`EndDate`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `discountdetails`
--

/*!50001 DROP VIEW IF EXISTS `discountdetails`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `discountdetails` AS select `d`.`DiscountID` AS `DiscountID`,`d`.`DiscountPercent` AS `DiscountPercent`,`d`.`DiscountCode` AS `DiscountCode`,`d`.`StartDate` AS `StartDate`,`d`.`EndDate` AS `EndDate`,`d`.`Active` AS `Active`,`dt`.`DiscountType` AS `DiscountType`,`dt`.`Description` AS `Description` from (`discounts` `d` join `discounttypes` `dt` on((`d`.`DiscountTypeID` = `dt`.`DiscountTypeID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `existed product list`
--

/*!50001 DROP VIEW IF EXISTS `existed product list`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `existed product list` AS select `p`.`ProductID` AS `ProductID`,`p`.`ProductName` AS `ProductName`,`p`.`UnitPrice` AS `UnitPrice`,`p`.`UnitsInStock` AS `UnitsInStock`,`p`.`Picture` AS `Picture`,`p`.`CategoryName` AS `CategoryName`,`d`.`DiscountID` AS `DiscountID`,ifnull(`d`.`DiscountPercent`,0) AS `DiscountPercent`,`p`.`Discontinued` AS `Discontinued` from (`productinfos` `p` left join (`productsdiscounts` `pd` join `currentvaliddiscountpercentagebased` `d` on((`pd`.`DiscountID` = `d`.`DiscountID`))) on((`p`.`ProductID` = `pd`.`ProductID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `productdetails`
--

/*!50001 DROP VIEW IF EXISTS `productdetails`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `productdetails` AS select `p`.`ProductID` AS `ProductID`,`p`.`ProductName` AS `ProductName`,`p`.`CategoryID` AS `CategoryID`,`p`.`QuantityPerUnit` AS `QuantityPerUnit`,`p`.`UnitPrice` AS `UnitPrice`,`p`.`UnitsInStock` AS `UnitsInStock`,`p`.`Discontinued` AS `Discontinued`,`p`.`Picture` AS `Picture`,`p`.`Description` AS `Description`,`p`.`CategoryName` AS `CategoryName`,`d`.`DiscountID` AS `DiscountID`,ifnull(`d`.`DiscountPercent`,0) AS `DiscountPercent`,`d`.`StartDate` AS `StartDate`,`d`.`EndDate` AS `EndDate` from (`productinfos` `p` left join (`productsdiscounts` `pd` join `currentvaliddiscountpercentagebased` `d` on((`pd`.`DiscountID` = `d`.`DiscountID`))) on((`p`.`ProductID` = `pd`.`ProductID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `productinfos`
--

/*!50001 DROP VIEW IF EXISTS `productinfos`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `productinfos` AS select `p`.`ProductID` AS `ProductID`,`p`.`ProductName` AS `ProductName`,`p`.`CategoryID` AS `CategoryID`,`p`.`QuantityPerUnit` AS `QuantityPerUnit`,`p`.`UnitPrice` AS `UnitPrice`,`p`.`UnitsInStock` AS `UnitsInStock`,`p`.`Discontinued` AS `Discontinued`,`p`.`Picture` AS `Picture`,`p`.`Description` AS `Description`,`c`.`CategoryName` AS `CategoryName` from (`products` `p` left join `categories` `c` on((`p`.`CategoryID` = `c`.`CategoryID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `products by category`
--

/*!50001 DROP VIEW IF EXISTS `products by category`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `products by category` AS select `c`.`CategoryName` AS `CategoryName`,`p`.`ProductName` AS `ProductName`,`p`.`QuantityPerUnit` AS `QuantityPerUnit`,`p`.`UnitsInStock` AS `UnitsInStock`,`p`.`Discontinued` AS `Discontinued` from (`categories` `c` join `productinfos` `p` on((`c`.`CategoryID` = `p`.`CategoryID`))) where (`p`.`Discontinued` <> 1) */;
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

-- Dump completed on 2024-09-07 23:23:38

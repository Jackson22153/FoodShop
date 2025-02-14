USE [master]
GO
/****** Object:  Database [product]    Script Date: 10/25/2024 9:48:28 AM ******/
CREATE DATABASE [product]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'product', FILENAME = N'/var/opt/mssql/data/product.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'product_log', FILENAME = N'/var/opt/mssql/data/product_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [product] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [product].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [product] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [product] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [product] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [product] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [product] SET ARITHABORT OFF 
GO
ALTER DATABASE [product] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [product] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [product] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [product] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [product] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [product] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [product] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [product] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [product] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [product] SET  DISABLE_BROKER 
GO
ALTER DATABASE [product] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [product] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [product] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [product] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [product] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [product] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [product] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [product] SET RECOVERY FULL 
GO
ALTER DATABASE [product] SET  MULTI_USER 
GO
ALTER DATABASE [product] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [product] SET DB_CHAINING OFF 
GO
ALTER DATABASE [product] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [product] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [product] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [product] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'product', N'ON'
GO
ALTER DATABASE [product] SET QUERY_STORE = OFF
GO
USE [product]
GO
/****** Object:  UserDefinedTableType [dbo].[ProductStockTableType]    Script Date: 10/25/2024 9:48:28 AM ******/
CREATE TYPE [dbo].[ProductStockTableType] AS TABLE(
	[ProductID] [int] NULL,
	[UnitsInStock] [smallint] NULL
)
GO
/****** Object:  Table [dbo].[Categories]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Categories](
	[CategoryID] [int] IDENTITY(1,1) NOT NULL,
	[CategoryName] [nvarchar](15) NOT NULL,
	[Description] [ntext] NULL,
	[Picture] [varchar](256) NULL,
 CONSTRAINT [PK_Categories] PRIMARY KEY CLUSTERED 
(
	[CategoryID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Products]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Products](
	[ProductID] [int] IDENTITY(1,1) NOT NULL,
	[ProductName] [nvarchar](40) NOT NULL,
	[CategoryID] [int] NULL,
	[QuantityPerUnit] [nvarchar](20) NULL,
	[UnitPrice] [money] NULL,
	[UnitsInStock] [smallint] NULL,
	[Discontinued] [bit] NOT NULL,
	[Picture] [varchar](256) NULL,
	[Description] [text] NULL,
 CONSTRAINT [PK_Products] PRIMARY KEY CLUSTERED 
(
	[ProductID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  View [dbo].[ProductInfos]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE view [dbo].[ProductInfos] as
SELECT p.ProductID, p.ProductName, p.CategoryID, p.QuantityPerUnit, p.UnitPrice, p.UnitsInStock, p.Discontinued, p.Picture, p.Description, c.CategoryName
FROM     dbo.Products AS p LEFT OUTER JOIN
                  dbo.Categories AS c ON p.CategoryID = c.CategoryID
GO
/****** Object:  Table [dbo].[Discounts]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Discounts](
	[DiscountID] [varchar](36) NOT NULL,
	[DiscountPercent] [int] NOT NULL,
	[DiscountCode] [varchar](max) NULL,
	[StartDate] [datetime] NOT NULL,
	[EndDate] [datetime] NOT NULL,
	[Active] [bit] NOT NULL,
	[DiscountTypeID] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[DiscountID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DiscountTypes]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DiscountTypes](
	[DiscountTypeID] [int] IDENTITY(1,1) NOT NULL,
	[DiscountType] [varchar](20) NOT NULL,
	[Description] [varchar](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[DiscountTypeID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  View [dbo].[CurrentValidDiscountPercentageBased]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

create view [dbo].[CurrentValidDiscountPercentageBased] as
SELECT DiscountID, DiscountPercent, DiscountType, DiscountCode, StartDate, EndDate, Active
FROM     dbo.Discounts d join DiscountTypes dt on d.DiscountTypeID=dt.DiscountTypeID
WHERE  (DiscountType = 'percentage-based') AND (Active = 1) AND (CAST(GETDATE() AS datetime) BETWEEN StartDate AND EndDate)
GO
/****** Object:  Table [dbo].[ProductsDiscounts]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ProductsDiscounts](
	[DiscountID] [varchar](36) NOT NULL,
	[ProductID] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[DiscountID] ASC,
	[ProductID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[ProductDetails]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE view [dbo].[ProductDetails] as
SELECT p.ProductID, p.ProductName, p.CategoryID, p.QuantityPerUnit, p.UnitPrice, p.UnitsInStock, p.Discontinued, p.Picture, p.Description, p.CategoryName, d.DiscountID, ISNULL(d.DiscountPercent, 0) 
                  AS DiscountPercent, d.StartDate, d.EndDate
FROM     dbo.ProductInfos AS p LEFT OUTER JOIN
                  dbo.ProductsDiscounts AS pd INNER JOIN
                  dbo.CurrentValidDiscountPercentageBased AS d ON pd.DiscountID = d.DiscountID ON p.ProductID = pd.ProductID
GO
/****** Object:  View [dbo].[Products by Category]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

create view [dbo].[Products by Category] AS
SELECT Categories.CategoryName, Products.ProductName, Products.QuantityPerUnit, Products.UnitsInStock, Products.Discontinued
FROM Categories INNER JOIN Products ON Categories.CategoryID = Products.CategoryID
WHERE Products.Discontinued <> 1
--ORDER BY Categories.CategoryName, Products.ProductName
GO
/****** Object:  View [dbo].[DiscountDetails]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[DiscountDetails] as
SELECT d.DiscountID, d.DiscountPercent, d.DiscountCode, d.StartDate, d.EndDate, d.Active, dt.DiscountType, dt.Description
FROM     dbo.Discounts AS d INNER JOIN
                  dbo.DiscountTypes AS dt ON d.DiscountTypeID = dt.DiscountTypeID
GO
/****** Object:  View [dbo].[Current Valid Discounts]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[Current Valid Discounts] as
SELECT DiscountID, DiscountPercent, DiscountCode, StartDate, EndDate, Active, DiscountType
FROM     dbo.Discounts d join DiscountTypes dt on d.DiscountTypeID=dt.DiscountTypeID
WHERE  (CAST(GETDATE() AS datetime) BETWEEN StartDate AND EndDate) AND (Active = 1)
GO
/****** Object:  View [dbo].[Current Product List]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[Current Product List] as
SELECT p.ProductID, p.ProductName, p.UnitPrice, p.UnitsInStock, p.picture as Picture, p.CategoryName, d.DiscountID, ISNULL(d.DiscountPercent, 0) AS DiscountPercent
FROM     dbo.ProductInfos AS p LEFT OUTER JOIN
                  dbo.ProductsDiscounts AS pd INNER JOIN
                  dbo.CurrentValidDiscountPercentageBased AS d ON pd.DiscountID = d.DiscountID ON p.ProductID = pd.ProductID
WHERE  (p.Discontinued = 0)

GO
/****** Object:  View [dbo].[Existed Product List]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[Existed Product List] as
SELECT p.ProductID, p.ProductName, p.UnitPrice, p.UnitsInStock, p.Picture, p.CategoryName, d.DiscountID, ISNULL(d.DiscountPercent, 0) AS DiscountPercent, p.Discontinued
FROM     dbo.ProductInfos AS p LEFT OUTER JOIN
        dbo.ProductsDiscounts AS pd INNER JOIN
        dbo.CurrentValidDiscountPercentageBased AS d ON pd.DiscountID = d.DiscountID ON p.ProductID = pd.ProductID
GO
/****** Object:  View [dbo].[ProductSizeInfo]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[ProductSizeInfo] as
	SELECT 
        p.ProductID AS ProductID,
        p.ProductName AS ProductName,
        p.CategoryID AS CategoryID,
        p.QuantityPerUnit AS QuantityPerUnit,
        p.UnitPrice AS UnitPrice,
        p.UnitsInStock AS UnitsInStock,
        p.Discontinued AS Discontinued,
        p.Picture AS Picture,
        p.Description AS Description,
    c.CategoryName AS CategoryName
    FROM
        (products p
        LEFT JOIN categories c ON ((p.CategoryID = c.CategoryID)))
GO
/****** Object:  Table [dbo].[ProductSize]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ProductSize](
	[Id] [varchar](36) NOT NULL,
	[Height] [int] NULL,
	[Width] [int] NULL,
	[Length] [int] NULL,
	[Weight] [int] NULL,
	[ProductID] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[Categories] ON 

INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [Picture]) VALUES (1, N'Beverages', N'Soft drinks, coffees, teas, beers, and ales.', N'f35fb269-b9cd-4f98-b1b3-07f22dc72511.jpg')
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [Picture]) VALUES (2, N'Condiments', N'Sweet and savory sauces, relishes, spreads, and seasonings', N'cd709f4c-b8c4-4060-989b-8282deff85c8.jpg')
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [Picture]) VALUES (3, N'Confections', N'Desserts, candies, and sweet breads', N'a22927d5-2b7d-4bfe-b853-37b12a46dd04.jpg')
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [Picture]) VALUES (4, N'Dairy Products', N'Cheeses', N'2b43f34f-0732-4eef-87f4-6a8315685a85.jpg')
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [Picture]) VALUES (5, N'Grains/Cereals', N'Breads, crackers, pasta, and cereal', N'9b086f17-e380-43c2-83fb-1192cc9323e6.jpg')
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [Picture]) VALUES (6, N'Meat/Poultry', N'Prepared meats', N'677e635f-3729-4723-a86c-54cb8fe5e760.jpg')
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [Picture]) VALUES (7, N'Produce', N'Dried fruit and bean curd', N'1b30bcda-5ba0-494d-9a80-70d0cf6ddd33.jpg')
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [Picture]) VALUES (8, N'Seafood', N'Seaweed and fish', N'ffcffbc7-e86a-4c9a-a776-019e738061d3.jpg')
SET IDENTITY_INSERT [dbo].[Categories] OFF
GO
INSERT [dbo].[Discounts] ([DiscountID], [DiscountPercent], [DiscountCode], [StartDate], [EndDate], [Active], [DiscountTypeID]) VALUES (N'0a405179-7c98-429d-b564-5bbd75864fe3', 12, N'', CAST(N'2024-06-26T00:00:00.000' AS DateTime), CAST(N'2025-06-26T00:00:00.000' AS DateTime), 1, 1)
INSERT [dbo].[Discounts] ([DiscountID], [DiscountPercent], [DiscountCode], [StartDate], [EndDate], [Active], [DiscountTypeID]) VALUES (N'3be96477-95de-4bfd-8be7-60fad2020e84', 30, N'', CAST(N'2024-06-26T00:00:00.000' AS DateTime), CAST(N'2026-06-26T00:00:00.000' AS DateTime), 0, 1)
INSERT [dbo].[Discounts] ([DiscountID], [DiscountPercent], [DiscountCode], [StartDate], [EndDate], [Active], [DiscountTypeID]) VALUES (N'57366b38-e463-4536-bcd8-5535d01cd4b8', 10, N'', CAST(N'2024-06-26T00:00:00.000' AS DateTime), CAST(N'2024-07-31T00:00:00.000' AS DateTime), 1, 1)
INSERT [dbo].[Discounts] ([DiscountID], [DiscountPercent], [DiscountCode], [StartDate], [EndDate], [Active], [DiscountTypeID]) VALUES (N'590a6dd2-baf2-4b64-8bd6-75f3874dea8a', 20, N'TheChosenOne', CAST(N'2024-04-08T12:30:00.000' AS DateTime), CAST(N'2025-04-08T12:30:00.000' AS DateTime), 0, 1)
INSERT [dbo].[Discounts] ([DiscountID], [DiscountPercent], [DiscountCode], [StartDate], [EndDate], [Active], [DiscountTypeID]) VALUES (N'8997ccff-e004-4627-a897-df7a6012f563', 20, N'6753fd74-cca8-40c3-83f4-be0190ea631c', CAST(N'2024-05-09T00:00:00.000' AS DateTime), CAST(N'2024-06-05T00:00:00.000' AS DateTime), 1, 2)
INSERT [dbo].[Discounts] ([DiscountID], [DiscountPercent], [DiscountCode], [StartDate], [EndDate], [Active], [DiscountTypeID]) VALUES (N'8d9ec5ca-67ac-4e1b-b05d-f8e2bad66c7a', 30, N'33d81ebf-ff8c-4f38-8c4b-de41d2f681bf', CAST(N'2024-04-08T12:30:00.000' AS DateTime), CAST(N'2025-04-08T12:30:00.000' AS DateTime), 1, 1)
INSERT [dbo].[Discounts] ([DiscountID], [DiscountPercent], [DiscountCode], [StartDate], [EndDate], [Active], [DiscountTypeID]) VALUES (N'b6ff6ba9-6dd3-408d-9acc-af4dcc2b90c3', 30, N'50ee22c1-b499-4e76-8bab-d23b4e0ee46e', CAST(N'2024-04-08T12:30:00.000' AS DateTime), CAST(N'2025-04-08T12:30:00.000' AS DateTime), 1, 2)
INSERT [dbo].[Discounts] ([DiscountID], [DiscountPercent], [DiscountCode], [StartDate], [EndDate], [Active], [DiscountTypeID]) VALUES (N'c88dca88-262a-4ca7-8238-31e3e1576e57', 12, N'7f7b6d51-b9f6-4580-94b6-7f649f2bb5ef', CAST(N'2024-10-16T00:00:00.000' AS DateTime), CAST(N'2026-10-24T00:00:00.000' AS DateTime), 1, 1)
INSERT [dbo].[Discounts] ([DiscountID], [DiscountPercent], [DiscountCode], [StartDate], [EndDate], [Active], [DiscountTypeID]) VALUES (N'cd3507b7-f023-4a06-8dd4-b776520d266d', 25, N'', CAST(N'2024-04-30T00:00:00.000' AS DateTime), CAST(N'2024-07-10T02:00:00.000' AS DateTime), 0, 1)
INSERT [dbo].[Discounts] ([DiscountID], [DiscountPercent], [DiscountCode], [StartDate], [EndDate], [Active], [DiscountTypeID]) VALUES (N'd7f2059b-97ef-4edc-9544-11859ea4eb4f', 66, N'4ee41d7c-4fa8-47b6-b60f-2b12491d9c2c', CAST(N'2024-04-16T00:00:00.000' AS DateTime), CAST(N'2025-04-09T00:00:00.000' AS DateTime), 0, 1)
INSERT [dbo].[Discounts] ([DiscountID], [DiscountPercent], [DiscountCode], [StartDate], [EndDate], [Active], [DiscountTypeID]) VALUES (N'f12ace16-8ce7-43a4-b5fa-da0fe4070e4c', 5, N'a8f2019a-cc2a-4f1d-a412-4bae2eb2cfc1', CAST(N'2024-06-26T00:00:00.000' AS DateTime), CAST(N'2026-06-26T00:00:00.000' AS DateTime), 1, 1)
GO
SET IDENTITY_INSERT [dbo].[DiscountTypes] ON 

INSERT [dbo].[DiscountTypes] ([DiscountTypeID], [DiscountType], [Description]) VALUES (1, N'Percentage-based', N'Discount is used for product as default')
INSERT [dbo].[DiscountTypes] ([DiscountTypeID], [DiscountType], [Description]) VALUES (2, N'Code', N'Discount codes are entered by the user using the code')
SET IDENTITY_INSERT [dbo].[DiscountTypes] OFF
GO
SET IDENTITY_INSERT [dbo].[Products] ON 

INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (1, N'Chai', 1, N'10 boxes x 20 bags', 18.0000, 20, 0, N'cd2ac241-ced1-498f-bc2b-13add216e6fb.jpg', N'<p style="line-height: var(--cib-type-body2-line-height)"><span style="font-size: 16px"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Chai</strong>, also known as<strong>masala chai</strong>, is a delightful and aromatic beverage with a rich history and cultural significance. Let’s explore what chai is all about:</span></p><ol><li><p style="line-height: var(--cib-type-body2-line-height)"><span style="font-size: 16px"><strong>Origins and Traditional Preparation</strong>:</span></p><ul><li style="font-size: 16px"><strong>Chai</strong>originated in ancient India, where it was known as<strong>masala chai</strong>. In Sanskrit, “chai” means<strong>tea</strong>, while “masala” refers to the blend of spices used in its preparation.</li><li style="font-size: 16px">The base of chai is typically a<strong>black tea</strong>, with<strong>Assam</strong>being the most common variety due to its strong, full-bodied flavor. Some people blend Assam with<strong>Darjeeling tea</strong>for a balance of color, body, aroma, and flavor.</li><li><span style="font-size: 16px"><strong>Spices</strong>play a crucial role in chai. Commonly used spices include:</span><ul><li style="font-size: 16px"><strong>Cardamom</strong>: Provides a warm, aromatic flavor.</li><li style="font-size: 16px"><strong>Cinnamon</strong>: Adds sweetness and warmth.</li><li style="font-size: 16px"><strong>Ginger</strong>: Offers a spicy kick.</li><li style="font-size: 16px"><strong>Cloves</strong>: Contribute depth and richness.</li><li style="font-size: 16px"><strong>Black pepper</strong>,<strong>coriander</strong>,<strong>nutmeg</strong>, and<strong>fennel</strong>are also used, though less commonly.</li></ul><span style="font-size: 16px"></span></li><li style="font-size: 16px">Chai is typically brewed by simmering tea leaves and spices in<strong>water or milk</strong>, allowing the flavors to meld together.</li></ul></li><li><p style="line-height: var(--cib-type-body2-line-height)"><span style="font-size: 16px"><strong>Ayurvedic Roots</strong>:</span></p><ul><li style="font-size: 16px">Chai tea has ancient<strong>Ayurvedic</strong>roots. Ayurveda, an Indian system of medicine, emphasizes balance between mind, body, and spirit.</li><li style="font-size: 16px">Each spice in chai has specific healing properties, making it more than just a flavorful beverage.</li><li style="font-size: 16px">The combination of spices and herbs in chai contributes to its age-old healing benefits.</li></ul></li><li><p style="line-height: var(--cib-type-body2-line-height)"><span style="font-size: 16px"><strong>Social Significance</strong>:</span></p><ul><li style="font-size: 16px">Chai symbolizes<strong>warmth</strong>,<strong>hospitality</strong>, and<strong>connection</strong>in Indian traditions.</li><li style="font-size: 16px">It’s an important social beverage, often shared among friends, family, and neighbors.</li><li style="font-size: 16px">In India, you’ll find<strong>chaiwallas</strong>(vendors selling chai) on street corners, and serving chai is a common practice in households.</li></ul></li></ol>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (2, N'Chang', 1, N'24 - 12 oz bottles.', 19.0000, 13, 0, N'0f2bfb08-84bd-4368-9e4b-17e33aaf331f.jpg', N'<ul><li style="font-size: 16px"><strong>Chang&nbsp;</strong>is a premium Thai beer known for its smooth and refreshing taste. It’s perfect for sharing with friends during social gatherings. The beer features a distinctive logo that symbolizes Thai culture and values.</li><li style="font-size: 16px">If you’re looking for a delightful beer experience, might be the one to try.</li></ul>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (3, N'Aniseed Syrup', 2, N'12 - 550 ml bottles', 10.0000, 11, 0, N'517b0105-da18-4487-be02-8d80906a0226.jpg', N'<p><span style="font-size: 16px">Aniseed syrup is known for its sweet, licorice-like taste and aroma. It’s commonly used in a variety of pastries and liquors globally. Ideal for enhancing the flavor of cocktails, mocktails, sodas, iced teas, lemonades, and can also be drizzled over fruit and desserts</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (4, N'Chef Anton''s Cajun Seasoning', 2, N'48 - 6 oz jars', 22.0000, 49, 0, N'74be6e22-466b-4611-8619-2885cfb08105.jpg', N'<p><span style="font-size: 16px">Chef Anton’s Cajun Seasoning is a blend of spices that typically includes paprika, onion and garlic powder, oregano, and cayenne pepper. It’s used to add a spicy kick to dishes like gumbo, shrimp and grits, and jambalaya. The exact recipe can vary, but these ingredients form the base for this flavorful seasoning.</span></p><p><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (5, N'Chef Anton''s Gumbo Mix', 2, N'36 boxes', 21.3500, 0, 1, N'dd626b7d-fdcd-448b-9f16-5a8540237dcb.jpg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (6, N'Grandma''s Boysenberry Spread', 2, N'12 - 8 oz jars', 25.0000, 120, 0, N'96ad8a74-99c1-4504-8cc7-c1077fd10ee9.jpg', N'<p><span style="font-size: 16px">Grandma’s Boysenberry Spread is a jam-like product made with cane sugar, seedless boysenberries, water, pectin, and citric acid. It’s likely to have a sweet and tangy flavor characteristic of boysenberries and can be used as a spread for breads, pastries, or as a topping for desserts.</span></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (7, N'Uncle Bob''s Organic Dried Pears', 7, N'12 - 1 lb pkgs.', 30.0000, 15, 0, N'9a40a253-8c37-4feb-98cd-4da878d1d4bc.jpg', N'<p><span style="font-size: 16px">Uncle Bob’s Organic Dried Pears are a healthy snack option, made from organic pears that are dried without the addition of any preservatives or sweeteners. They are typically sold in 1 lb packages and can be enjoyed on their own or added to cereals, baked goods, or salads for a natural sweetness and chewy texture.</span></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (8, N'Northwoods Cranberry Sauce', 2, N'12 - 12 oz jars', 40.0000, 6, 0, N'175e5aa6-0519-440e-a38d-ea8785d2eacd.jpg', N'<p><span style="font-size: 16px">Northwoods Cranberry Sauce is likely a traditional cranberry sauce, which is a condiment made from cranberries. It’s commonly served with Thanksgiving dinner in North America and Christmas dinner in the United Kingdom and Canada. The sauce is known for its tart flavor, which complements rich holiday dishes.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (9, N'Mishi Kobe Niku', 6, N'18 - 500 g pkgs.', 97.0000, 29, 1, N'bcbdc622-e34f-40ea-b654-acfa1b80a153.jpg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (10, N'Ikura', 8, N'12 - 200 ml jars', 31.0000, 31, 0, N'/shop/image/product/368a8366-f4b7-4a3a-8799-e5adb4688e1a.jpg', N'<p><span style="font-size: 16px">Ikura is the Japanese term for salmon roe, also known as red caviar. These large eggs are known for their soft texture, briny flavor, and a mild fishiness. Ikura is a popular and healthy ingredient in Japanese cuisine, often served on sushi or as a standalone dish.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (11, N'Queso Cabrales', 4, N'1 kg pkg.', 21.0000, 22, 0, N'c53b309c-8f86-464f-a425-ec692503f90f.jpg', N'<p><span style="font-size: 16px">Queso Cabrales is a blue cheese from Asturias, Spain, known for its intense flavor and creamy texture. It’s made traditionally by rural dairy farmers and can be produced from pure, unpasteurized cow’s milk or blended with goat and/or sheep milk for a spicier taste. The cheese features distinctive blue-green veins from natural mold developed during aging</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (12, N'Queso Manchego La Pastora', 4, N'10 - 500 g pkgs.', 38.0000, 86, 0, N'1f1dcb3f-ed4d-4f0a-ab0c-3a2aee5dc2a5.jpg', N'<p><span style="font-size: 16px">Queso Manchego La Pastora is a variety of Manchego cheese, which is a pressed cheese made from the milk of Manchega sheep breed. It comes from the La Mancha region of Spain and can be aged between 60 days to 2 years. This cheese is known for its rich, nutty flavor and firm texture. It’s often used in cheese platters and pairs well with fruits, nuts, and charcuterie</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (13, N'Konbu', 8, N'2 kg box', 6.0000, 24, 0, N'329fb8b0-1b91-44f0-90eb-7d7ed4207283.jpg', N'<p><span style="font-size: 16px">Konbu, also known as kombu, is an edible kelp widely used in East Asian cuisine, especially in Japan and Korea. It’s a key ingredient in making dashi, a foundational broth in Japanese cooking, and is prized for its umami flavor due to high glutamic acid content. Konbu is rich in minerals and can be used as a condiment or garnish in various dishes</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (14, N'Tofu', 7, N'40 - 100 g pkgs.', 23.2500, 35, 0, N'43149946-0d51-41df-b29c-03bc87a9b4d2.jpg', N'<p><span style="font-size: 16px">Tofu, also known as bean curd, is a food made by coagulating soy milk and then pressing the resulting curds into solid white blocks. It originates from East Asia and is a staple in many Asian cuisines. Tofu is high in protein, low in fat, and rich in calcium and iron. It has a subtle flavor, making it versatile for absorbing spices, sauces, and marinades</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (15, N'Genen Shouyu', 2, N'24 - 250 ml bottles', 15.5000, 39, 0, N'bc2b3ded-5dfe-427d-b81d-0e77dfd8d7dd.jpg', N'<p><span style="font-size: 16px">Genen Shouyu is a type of Japanese soy sauce made by Kikkoman. It is naturally brewed and has a reduced salt content compared to regular soy sauces. It contains ingredients like water, soybeans, wheat, salt, vinegar, alcohol, and sugar. Genen Shouyu is known for its balanced flavor that enhances dishes without overpowering them</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (16, N'Pavlova', 3, N'32 - 500 g boxes', 17.4500, 29, 0, N'14c1f80c-e173-471b-8e39-bc91e06244b3.jpg', N'<p><span style="font-size: 16px">Pavlova is a meringue-based dessert named after the Russian ballerina Anna Pavlova. It has origins in Australia and New Zealand, where it is considered a national delicacy. The dessert features a crisp crust with a soft, light interior, commonly topped with whipped cream and fruit. It’s celebrated for its delicate texture and is often served during holidays and special occasions</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (17, N'Alice Mutton', 6, N'20 - 1 kg tins', 39.0000, 0, 1, N'd50e852d-c77d-45b1-9f84-066ee2aff0e2.jpg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (18, N'Carnarvon Tigers', 8, N'16 kg pkg.', 62.5000, 42, 0, N'c1490261-9698-4c9f-9d01-7d372194387e.jpg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (19, N'Teatime Chocolate Biscuits', 3, N'10 boxes x 12 pieces', 9.2000, 25, 0, N'df048fb1-34ee-4f21-89e7-508a6bbec1e7.jpg', N'<p><span style="font-size: 16px">Tea Time Biscuits by WIBISCO are sandwich cookies with two crisp baked cookies and a layer of smooth cream filling, creating a delicious full-flavored taste. They are available in Chocolate, Vanilla, and Double Chocolate flavors and are enjoyed by people of all ages. These biscuits are known for their delightful taste and are a popular treat during tea time</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (20, N'Sir Rodney''s Marmalade', 3, N'30 gift boxes', 81.0000, 40, 0, N'94731fd4-4233-4fd2-b559-eb91af2b5c5c.jpg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (21, N'Sir Rodney''s Scones', 3, N'24 pkgs. x 4 pieces', 10.0000, 3, 0, N'a2ae9bed-ff1d-49c8-9530-09a2bca201ef.jpg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (22, N'Gustaf''s Knäckebröd', 5, N'24 - 500 g pkgs.', 21.0000, 104, 0, N'e14caa7e-2dc4-4b3d-9e2c-9211e3a5f2be.jpg', N'<p><span style="font-size: 16px">Gustaf’s Knäckebröd is a type of Swedish crispbread that is large, dry, and flat, made primarily from rye flour. It’s derived from a thicker bread called spisbröd and is enjoyed in Sweden as a staple bread alternative. Its crunchy texture and thin form are similar to crackers, and it’s high in fiber and very sustaining</span></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (23, N'Tunnbröd', 5, N'12 - 250 g pkgs.', 9.0000, 61, 0, N'f62644c9-8926-4922-ab42-b4d104fc8308.jpg', N'<p><span style="font-size: 16px">Tunnbröd is a traditional Swedish flatbread that can be either soft or crisp. It’s typically round and thin, made from a mix of whole wheat, white wheat, and sometimes barley flour. Ingredients may include water, salt, and sometimes a touch of fat. It comes in many variants depending on the choice of grain, leavening agent, and rolling pin. It was created out of necessity for long-term storage but is now enjoyed in various forms.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (24, N'Guaraná Fantástica', 1, N'12 - 355 ml cans', 4.5000, 20, 1, N'7ac4ccd7-fd91-4b7f-8cd7-0f32798f4974.jpg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (25, N'NuNuCa Nuß-Nougat-Creme', 3, N'20 - 450 g glasses', 14.0000, 76, 0, N'4ba11a2b-4831-42c8-8dc9-d8d6a9c21a43.jpg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (26, N'Gumbär Gummibärchen', 3, N'100 - 250 g bags', 31.2300, 15, 0, N'86f47e96-6a85-4731-b133-08f24b35128b.jpg', N'<p><span style="font-size: 16px">Gummy bears, known as Gummibär in German, are small, fruit gum candies that are roughly 2 cm long and shaped like a bear. They are a popular gelatin-based candy sold in a variety of shapes and colors</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (27, N'Schoggi Schokolade', 3, N'100 - 100 g pieces', 43.9000, 49, 0, N'3dc931f3-1016-4703-996c-0d37188337ce.jpg', N'<p><span style="font-size: 16px">Schoggi Schokolade refers to a variety of chocolate products offered by Schoggi Meier. They have a range of chocolates such as dark chocolate with homemade pistachio gianduja, caramelized hazelnuts enrobed in milk chocolate, and white chocolate with macadamia gianduja and feuilletine, among others</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (28, N'Rössle Sauerkraut', 7, N'25 - 825 g cans', 45.6000, 26, 1, N'0a14e67a-76b3-4fbf-86fd-110b2f4851a8.jpeg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (29, N'Thüringer Rostbratwurst', 6, N'50 bags x 30 sausgs.', 123.7900, 0, 1, N'0957e392-795e-4a51-9045-991a3613d534.jpg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (30, N'Nord-Ost Matjeshering', 8, N'10 - 200 g glasses', 25.8900, 10, 0, N'1cc810ea-8b91-4cf9-9e69-50a36e852fe8.jpg', N'<p><span style="font-size: 16px">Nord-Ost Matjeshering is a seafood product offered by Nord-Ost-Fisch Handelsgesellschaft mbH. It comes in 10 - 200 g glasses and seems to be categorized under seafood. Matjeshering refers to a type of herring, and it’s typically served in a variety of dishes in Northern Europe.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (31, N'Gorgonzola Telino', 4, N'12 - 100 g pkgs', 12.5000, 0, 0, N'8ed947cb-5328-46df-a488-855cae66ff47.jpg', N'<p><span style="font-size: 16px">Gorgonzola is an Italian blue cheese known for its crumbly texture, bold flavor, and distinctive blue-green veins. It’s made from cow’s milk and can be either sweet (dolce) or spicy (piccante)</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (32, N'Mascarpone Fabioli', 4, N'24 - 200 g pkgs.', 32.0000, 9, 0, N'a1457446-8f06-4f96-b44f-a976cf5f153b.jpg', N'<p><span style="font-size: 16px">Mascarpone is a soft Italian cream cheese known for its rich, creamy texture and natural sweetness. It’s often used in desserts like tiramisu or as a spread. There’s also a product called “Mascarpone 40” by Fabbri, which is a flavoring powder that gives ice cream and confectionery preparations the taste of mascarpone</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (33, N'Geitost', 4, N'500 g', 2.5000, 112, 0, N'89cb7382-e4b7-418d-8512-d3a536b3b4e1.jpg', N'<p><span style="font-size: 16px">Geitost, also known as Brunost or Gjetost, is a Norwegian cheese that is typically made with whey, milk, and/or cream. It has a unique caramelized flavor and is made from a combination of cow’s milk and goat’s milk, or purely from goat’s milk</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (34, N'Sasquatch Ale', 1, N'24 - 12 oz bottles', 14.0000, 111, 0, N'27ec47d4-74d7-47f1-bc95-b46090bb98f0.jpg', N'<p><span style="font-size: 16px">Sasquatch Ale is an Imperial IPA style beer brewed by Six Rivers Brewery. It has a translucent, medium dark orange and reddish appearance with a full, deeply caramelized, medium toast barley malt and spiced floral hops aroma. The beer has a good amount of body and a notable bite in the hop department</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (35, N'Steeleye Stout', 1, N'24 - 12 oz bottles', 18.0000, 20, 0, N'677da96d-278c-4c8d-b989-728340d4b48d.jpg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (36, N'Inlagd Sill', 8, N'24 - 250 g  jars', 19.0000, 112, 0, N'28be74d0-493c-40fc-8e25-1621ac981408.jpg', N'<p><span style="font-size: 16px">Inlagd Sill is a traditional Swedish dish of pickled herring that is commonly served at Midsummer, Christmas, and Easter celebrations. The dish consists of cleaned, skinned, and salted herring that is soaked in a marinade of vinegar, sugar, chopped onions and carrots, allspice, bay leaves, pepper, and crushed peppercorns1. It’s often featured on a Swedish smörgåsbord but can also be enjoyed with fresh new potatoes and sometimes soured cream</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (37, N'Gravad lax', 8, N'12 - 500 g pkgs.', 26.0000, 11, 0, N'34beb1e0-b1d2-49d5-a212-921f413c01e4.jpg', N'<p><span style="font-size: 16px">Gravad lax, also known as gravlax, is a traditional Nordic dish consisting of salmon that is cured using a mix of salt, sugar, and dill. It’s typically served with a mustard sauce and garnished with fresh dill. The name translates to “buried salmon,” which refers to the old method of curing the fish by burying it in the ground.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (38, N'Côte de Blaye', 1, N'12 - 75 cl bottles', 263.5000, 17, 0, N'69aae184-70a1-4dd4-b58c-47d2eca004b7.jpg', N'<p><span style="font-size: 16px">Côtes de Blaye are white wines from the Blaye appellation in Bordeaux, France. They’re made mostly from Colombard and Ugni blanc grapes, resulting in dry, light, and mildly fruity wines</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (39, N'Chartreuse verte', 1, N'750 cc per bottle', 18.0000, 69, 0, N'1b161792-7007-4762-9c12-dc006b389402.jpg', N'<p><span style="font-size: 16px">Chartreuse verte, or Green Chartreuse, is an herbal liqueur crafted by Carthusian monks in the French Alps from a 400-year-old recipe. It’s the only liqueur with a completely natural green color and is made with 130 herbs, plants, and flowers</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (40, N'Boston Crab Meat', 8, N'24 - 4 oz tins', 18.4000, 123, 0, N'3fbb7900-6fa7-47e8-aeb2-1765725512b8.jpg', N'<p><span style="font-size: 16px">Boston Crab Meat typically refers to the meat from crabs that are found in the waters around Boston, known for its sweet, delicate flavor and tender texture. In Boston, you can find fresh crab meat at various seafood markets and restaurants, often used in dishes like crab cakes, salads, or served steamed with butter.</span></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (41, N'Jack''s New England Clam Chowder', 8, N'12 - 12 oz cans', 9.6500, 85, 0, N'02f1b4ce-0453-4ed0-9ec4-d38f25e38963.jpg', N'<p><span style="font-size: 16px">New England Clam Chowder, also known as Boston Clam Chowder, is a creamy stew famous for its rich and hearty flavor. It typically contains tender clams, diced potatoes, onions, sometimes celery, and is seasoned with salt pork or bacon. The chowder is known for its thick consistency and is often served with oyster crackers.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (42, N'Singaporean Hokkien Fried Mee', 5, N'32 - 1 kg pkgs.', 14.0000, 26, 1, N'01f9564c-f58d-4e31-bfd3-8cfc184c4f81.jpg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (43, N'Ipoh Coffee', 1, N'16 - 500 g tins', 46.0000, 17, 0, N'f1e3a531-f48c-40df-a397-85587b2f57c1.jpg', N'<p><span style="font-size: 16px">Ipoh Coffee, often referred to as Ipoh White Coffee, is a renowned coffee drink from Ipoh, Perak, Malaysia. The coffee beans are roasted with palm oil margarine, which gives the coffee a unique, creamy flavor and reduces its bitterness. It’s typically served with condensed milk, resulting in a rich and smooth taste. The drink’s name comes from the lighter color achieved through this roasting process.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (44, N'Gula Malacca', 2, N'20 - 2 kg bags', 19.4500, 27, 0, N'1599978d-3223-4807-9e98-5d85bef59d40.jpg', N'<p><span style="font-size: 16px">Gula Malacca, commonly known as Gula Melaka or palm sugar, is a sweetener derived from the sap of palm trees, particularly the coconut palm. It’s widely used in Southeast Asian cuisine for its rich, caramel-like flavor. The sap is boiled down and then hardened into cylindrical blocks or granules. It’s darker and has a more intense flavor compared to other types of palm sugar.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (45, N'Rogede sild', 8, N'1k pkg.', 9.5000, 5, 0, N'69b7d844-7261-4cef-9b53-25bdd02a2a91.jpg', N'<p><span style="font-size: 16px">“Rogede sild” refers to a Danish dish involving herring. “Sild” is the Danish word for herring, and “rogede” suggests that the herring may be smoked. Typically, this dish would involve smoked herring served in various ways, often as part of traditional Danish smørrebrød (open-faced sandwiches)</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (46, N'Spegesild', 8, N'4 - 450 g glasses', 12.0000, 95, 0, N'abde05da-9e13-4ea8-adad-3c17d8590653.jpg', N'<p><span style="font-size: 16px">Spegesild is a traditional Scandinavian dish, specifically Norwegian, where herring is preserved using salt curing. The process involves using salt to extract water from the herring, creating an environment that inhibits microbial growth. This method of preservation allows the herring to be stored for extended periods. The best quality of spegesild is known as “diamanter” and is produced in Norway and Iceland from Atlantic herring with a high fat content.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (47, N'Zaanse koeken', 3, N'10 - 4 oz boxes', 9.5000, 36, 0, N'e68075e9-816f-44b0-ab74-6f09a272728b.jpeg', N'<p><span style="font-size: 16px">“Zaanse koeken” refers to cookies or biscuits that originate from the Zaan region in the Netherlands. The term “koeken” is Dutch for cookies, and these are often traditional recipes that may include ingredients like almonds. They are typically enjoyed with coffee or tea.</span></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (48, N'Chocolade', 3, N'10 pkgs.', 12.7500, 15, 0, N'71732701-f0f0-4916-ab0b-0a433aed9bd1.jpg', N'<p><span style="font-size: 16px">Chocolade, known as chocolate in English, is a food product made from cocoa beans. It is consumed as candy and used to make beverages and to flavor or coat various confections and bakery products. Chocolate is rich in carbohydrates and is an excellent source of quick energy. It has several health benefits and comes in various forms such as liquid, solid, or paste. The process of making chocolate involves fermenting, drying, and roasting the cacao seeds, which gives chocolate its distinctive taste.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (49, N'Maxilaku', 3, N'24 - 50 g pkgs.', 20.0000, 10, 0, N'd3aee32e-d449-4d4d-aab4-015daa089eac.jpg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (50, N'Valkoinen suklaa', 3, N'12 - 100 g bars', 16.2500, 65, 0, N'424a53df-3125-4a61-b63e-f98dbd8d5506.jpg', N'<p><span style="font-size: 16px">Valkoinen suklaa, or white chocolate in English, is a confection made from cocoa butter, sugar, and milk solids. Unlike dark and milk chocolates, white chocolate does not contain cocoa solids, which is why it has a pale color and a different flavor profile. It’s known for its creamy texture and sweet, buttery taste with hints of vanilla. White chocolate is used in various desserts and confections, such as mousse, cakes, and candies. It’s also enjoyed on its own as a sweet treat.</span></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (51, N'Manjimup Dried Apples', 7, N'50 - 300 g pkgs.', 53.0000, 20, 0, N'071b1ca3-4134-48b5-babc-18b0750eaff1.jpg', N'<p><span style="font-size: 16px">Manjimup is a region in Western Australia known for its rich soils and diverse produce, including apples. While the search results don’t provide specific details about “Manjimup Dried Apples,” it’s likely that these are apples that have been dried for preservation and snacking. Dried apples are a popular healthy snack and can be used in various recipes.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (52, N'Filo Mix', 5, N'16 - 2 kg boxes', 7.0000, 38, 0, N'78f1807b-9534-4d2e-b3e8-efd549504015.png', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (53, N'Perth Pasties', 6, N'48 pieces', 32.8000, 0, 1, N'90936ff1-eb60-4bc5-a2b4-15edb0c473f3.jpg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (54, N'Tourtière', 6, N'16 pies', 7.4500, 21, 0, N'1cc9a005-e0ae-4eca-8ccd-a3cc4f0ab552.jpeg', N'<p><span style="font-size: 16px">Tourtière is a traditional French-Canadian meat pie that typically features a double crust and a filling made from ground or chopped meats like pork, beef, or game (such as rabbit, pheasant, or moose). It’s often seasoned with spices like cloves, cinnamon, and nutmeg, and may include vegetables and herbs. This savory pie is particularly popular during the holiday season in Quebec and other parts of Canada.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (55, N'Pâté chinois', 6, N'24 boxes x 2 pies', 24.0000, 115, 0, N'4bb45d0c-c379-4685-baee-aacde9483d28.jpg', N'<p><span style="font-size: 16px">Pâté chinois is a Quebecois dish that is similar to shepherd’s pie or the French “hachis Parmentier.” It consists of layered ground beef (sometimes mixed with sautéed diced onions) on the bottom, canned corn (either whole-kernel, creamed, or a mixture) in the middle, and mashed potatoes on top. This comfort food staple is often seasoned and baked, and it’s traditionally served with tomato ketchup on the side. It’s a popular dish in Quebec and can be found in most cafeterias throughout the region.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (56, N'Gnocchi di nonna Alice', 5, N'24 - 250 g pkgs.', 38.0000, 21, 0, N'37b8fbac-6c3d-4068-af81-6e7a5c4e27d8.jpg', N'<p><span style="font-size: 16px">“Gnocchi di nonna Alice” seems to be a specific or traditional recipe for gnocchi, which is an Italian potato dumpling dish. While I couldn’t find a recipe specifically named “Gnocchi di nonna Alice,” the term “Nonna” is Italian for grandmother, and it’s common for gnocchi recipes to be passed down through generations in Italian families. Traditional gnocchi is made with mashed potatoes, flour, and eggs, and can be served with various sauces.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (57, N'Ravioli Angelo', 5, N'24 - 250 g pkgs.', 19.5000, 36, 0, N'51ea6d25-fc9b-4ead-9468-a71d5f0fae0c.jpg', N'<p><span style="font-size: 16px">“Ravioli Angelo” could refer to a specific recipe or style of ravioli. While I couldn’t find a precise match for this term, the name Angelo is associated with a restaurant called “Charlie Gitto’s ‘On The Hill’” (formerly known as “Angelo’s”) in St. Louis, Missouri, where toasted ravioli was made famous. Toasted ravioli, also known as T-Ravs, is breaded deep-fried ravioli typically served as an appetizer.<br><br>Ravioli itself is a classic Italian pasta filled with various ingredients like cheese, meat, vegetables, or seafood.?</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (58, N'Escargots de Bourgogne', 8, N'24 pieces', 13.2500, 62, 0, N'83e5fdbc-e4c9-418c-b1bb-181f4b553321.jpg', N'<p><span style="font-size: 16px">“Escargots de Bourgogne” is a traditional French dish originating from the Burgundy region. It consists of snails (specifically the Helix pomatia species, also known as Burgundy snails) that are prepared with garlic and parsley butter. This dish is widely recognized as a refined French gastronomic specialty and is known for its flavorful taste</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (59, N'Raclette Courdavault', 4, N'5 kg pkg.', 55.0000, 79, 0, N'856afd44-021d-4778-8c66-f9db748d8747.jpg', NULL)
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (60, N'Camembert Pierrot', 4, N'15 - 300 g rounds', 34.0000, 19, 0, N'294a208b-fe56-444c-9ec5-ce3bfcbd728d.jpg', N'<p><span style="font-size: 16px">“Camembert Pierrot” does not appear in the search results as a widely recognized term or brand. However, Camembert itself is a famous soft, creamy cheese that originated in the late 18th century in Normandy, France. It’s known for its moist, soft texture and creamy, buttery center, with an ivory-colored exterior and a downy white surface</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (61, N'Sirop d''érable', 2, N'24 - 500 ml bottles', 28.5000, 113, 0, N'adbbb711-fadc-400a-8479-8dc6d8c7fb66.jpg', N'<p><span style="font-size: 16px">“Sirop d’érable” is the French term for maple syrup, a naturally sweet solution made from the sap of maple trees. The sap is collected in early spring and concentrated by boiling or reverse osmosis to produce the syrup. Maple syrup is emblematic of Canadian cuisine and is believed to originate from North America</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (62, N'Tarte au sucre', 3, N'48 pies', 49.3000, 17, 0, N'8aef518a-6174-457e-ab8b-aaa9517fa695.jpg', N'<p><span style="font-size: 16px">“Tarte au sucre,” also known as French Canadian Sugar Pie, is a traditional dessert from Quebec. It features a crust and a sweet, creamy filling made with ingredients such as brown sugar or maple syrup, cream, and lots of butter. The result is a moist and decadent pie with a caramel-like texture after baking</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (63, N'Vegie-spread', 2, N'15 - 625 g jars', 43.9000, 24, 0, N'9092b891-5f65-47fe-87a1-9d5881df9391.jpg', N'<p><span style="font-size: 16px">“Vegie-spread” refers to a variety of vegetable-based spreads that can be used on bagels, toast, sandwiches, wraps, or as a dip for crackers or raw vegetables. These spreads are typically made with fresh veggies and may include ingredients like cream cheese (dairy or dairy-free), herbs, and spices to create a creamy and tangy flavor</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (64, N'Wimmers gute Semmelknödel', 5, N'20 bags x 4 pieces', 33.2500, 22, 0, N'9c0f4231-37e1-4c6c-9c9a-4164ff450e15.jpg', N'<p><span style="font-size: 16px">“Semmelknödel” are traditional German bread dumplings made from stale bread rolls, warm milk, seasoning, and eggs. They are a versatile and nutritious side dish in German cuisine, often served with gravies or sauces</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (65, N'Louisiana Fiery Hot Pepper Sauce', 2, N'32 - 8 oz bottles', 21.0500, 76, 0, N'353e6430-8abd-4f3a-b94d-426c4584eaa2.jpg', N'<p><span style="font-size: 16px">“Louisiana Fiery Hot Pepper Sauce” likely refers to a type of hot sauce that is known for its fiery heat and robust flavor. Louisiana hot sauce typically features cayenne peppers, vinegar, salt, and sometimes garlic. It’s a beloved condiment in Louisiana cuisine and has become popular worldwide</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (66, N'Louisiana Hot Spiced Okra', 2, N'24 - 8 oz jars', 17.0000, 4, 0, N'33ae9b98-85c2-493c-a075-9f7397b8426a.jpg', N'<p><span style="font-size: 16px">“Louisiana Hot Spiced Okra” likely refers to a dish where okra is cooked with Louisiana-style spices, giving it a spicy kick. The dish often includes ingredients like cayenne, onions, tomatoes, and sometimes celery. It can be served warm and is typically smothered or stewed</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (67, N'Laughing Lumberjack Lager', 1, N'24 - 12 oz bottles', 14.0000, 51, 0, N'57acb063-5d27-4af3-9bd6-922f6968ed22.jpg', N'<p><span style="font-size: 16px">“Laughing Lumberjack Lager” is an American Amber Lager style beer. It’s brewed by Shooter ‘Franz’ McLovin, also known as Johnny Golf or The Archduke of Golf, at his Midtown Brewery in Atlanta, Georgia. Lagers are beers that are fermented and conditioned at low temperatures, known for their crisp and refreshing taste.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (68, N'Scottish Longbreads', 3, N'10 boxes x 8 pieces', 12.5000, 6, 0, N'cb54a8e6-3096-41bc-af42-2dc5d68fabf0.jpg', N'<p><span style="font-size: 16px">Scottish Shortbread, often simply called ‘shortbread,’ is a traditional Scottish biscuit known for its crumbly texture and rich buttery flavor. It’s typically made from one part white sugar, two parts butter, and three to four parts plain wheat flour, without any leavening agents like baking powder or baking soda. This treat is a staple in Scottish cuisine and has been enjoyed for centuries</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (69, N'Gudbrandsdalsost', 4, N'10 kg pkg.', 36.0000, 26, 0, N'600fcaf0-db8d-402c-ab94-a4553603be91.jpg', N'<p><span style="font-size: 16px">Gudbrandsdalsost, also known as Norwegian brown cheese or ‘brunost,’ is a whey cheese made from a pasteurized mixture of cow’s and goat’s milk. It has a soft and aromatic character with a fat content of 35%. This cheese is compressed and sold in cubes, offering a unique flavor that’s a staple in Norwegian cuisine. It’s been enjoyed since 1863 and is commonly used for breakfast, lunch, or as a snack</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (70, N'Outback Lager', 1, N'24 - 355 ml bottles', 15.0000, 15, 0, N'cfb66fc1-2569-4e2b-b439-9a8f2082dfe0.jpg', N'<p><span style="font-size: 16px">Outback Lager appears to be a craft beer, possibly part of a range called ‘Outback’ by a brewing company. While I couldn’t find specific details about Outback Lager, lagers in general are a type of beer that is fermented and conditioned at low temperatures, known for their clean, crisp taste</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (71, N'Flotemysost', 4, N'10 - 500 g pkgs.', 21.5000, 26, 0, N'd6152885-9017-4e27-ac43-ddf73334328c.jpg', N'<p><span style="font-size: 16px">Flotemysost, also known as Fløytemysost, is a type of Norwegian brown cheese (brunost) made from cow’s milk. It has a mild taste and bright color, which makes it very popular. It’s lighter and milder compared to its counterpart Geitost, which is made from goat’s milk and has a stronger taste. Flotemysost can be enjoyed on bread, crispbread, waffles, or used in pots and sauces for a touch of caramel flavor</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (72, N'Mozzarella di Giovanni', 4, N'24 - 200 g pkgs.', 34.8000, 14, 0, N'a39a1b88-e223-4af9-850c-14cdb2b6df9c.jpg', N'<p><span style="font-size: 16px">“Mozzarella di Giovanni” does not appear in the search results, so it might be a specific brand or artisanal product. Generally, mozzarella is a traditional southern Italian cheese known for its soft, moist texture and mild yet tangy taste. It’s commonly made from the milk of water buffalos or cows and is popular worldwide for its unique production process and versatility in dishes</span></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (73, N'Röd Kaviar', 8, N'24 - 150 g jars', 15.0000, 101, 0, N'76f0171d-f2ba-4857-9fef-1d477d89c3d8.jpg', N'<p><span style="font-size: 16px">Röd Kaviar, or red caviar, is made from the roe of salmonid fishes like salmon, trout, graylings, and char. It has an intense reddish hue and is distinct from black caviar, which comes from sturgeon. Red caviar has a rich history dating back to 12th-century Russia and is now a global delicacy enjoyed in various cuisines, including Russian and Japanese</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (74, N'Longlife Tofu', 7, N'5 kg pkg.', 10.0000, 4, 0, N'd4a6d838-4146-4e0c-be01-da95933063b5.jpg', N'<p><span style="font-size: 16px">“Longlife Tofu” might refer to tofu that has a longer shelf life due to its packaging or processing methods. Generally, fresh tofu can last 3-5 days beyond its sell-by date if stored properly, and shelf-stable tofu can last for 6 months to a year from the date of production</span></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (75, N'Rhönbräu Klosterbier', 1, N'24 - 0.5 l bottles', 7.7500, 125, 0, N'1a855faf-a738-48a8-b0e3-5cc4e07bb7df.jpg', N'<p><span style="font-size: 16px">Rhönbräu Klosterbier is a traditional beer from the Klosterbrauerei Kreuzberg, located in the Rhön region of Bavaria, Germany. The brewery is situated in a Franciscan monastery and has been a popular destination for centuries. It boasts a nearly 300-year history and offers a variety of beers brewed in accordance with monastic traditions. The Rhön Valley, where the monastery is located, is also known for its scenic hiking trails and beautiful monastic grounds.</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (76, N'Lakkalikööri', 1, N'500 ml', 18.0000, 57, 0, N'2c75c337-1645-4b00-89a4-957cea80486a.jpg', N'<p><span style="font-size: 16px">Lakkalikööri, also known as Lakka, is a Finnish liqueur that gets its distinct flavor from cloudberries, a fruit native to cool temperate regions, alpine and arctic tundra, and boreal forests. The word “lakka” means cloudberry in Finnish. This liqueur is typically made by steeping cloudberries in neutral grain alcohol for several months and is known for its sweet, almost floral flavor profile</span><br></p>')
INSERT [dbo].[Products] ([ProductID], [ProductName], [CategoryID], [QuantityPerUnit], [UnitPrice], [UnitsInStock], [Discontinued], [Picture], [Description]) VALUES (77, N'Original Frankfurter grüne Soße', 2, N'12 boxes', 13.0000, 32, 0, N'd19265d2-f70b-4d05-aacc-84f3bcd1318e.jpg', N'<p><span style="font-size: 16px">Original Frankfurter grüne Soße is a famous cold herb sauce from Frankfurt, Germany. It’s made with a blend of sour cream, spices, and traditionally seven specific herbs: borage, chervil, garden cress, parsley, salad burnet, sorrel, and chives. At least 70% of these herbs should have been grown in Frankfurt to be considered authentic. The sauce is typically served with boiled potatoes and eggs and is known for its fresh and tangy flavor profile</span><br></p>')
SET IDENTITY_INSERT [dbo].[Products] OFF
GO
INSERT [dbo].[ProductsDiscounts] ([DiscountID], [ProductID]) VALUES (N'0a405179-7c98-429d-b564-5bbd75864fe3', 2)
INSERT [dbo].[ProductsDiscounts] ([DiscountID], [ProductID]) VALUES (N'3be96477-95de-4bfd-8be7-60fad2020e84', 3)
INSERT [dbo].[ProductsDiscounts] ([DiscountID], [ProductID]) VALUES (N'57366b38-e463-4536-bcd8-5535d01cd4b8', 3)
INSERT [dbo].[ProductsDiscounts] ([DiscountID], [ProductID]) VALUES (N'590a6dd2-baf2-4b64-8bd6-75f3874dea8a', 1)
INSERT [dbo].[ProductsDiscounts] ([DiscountID], [ProductID]) VALUES (N'8997ccff-e004-4627-a897-df7a6012f563', 1)
INSERT [dbo].[ProductsDiscounts] ([DiscountID], [ProductID]) VALUES (N'8d9ec5ca-67ac-4e1b-b05d-f8e2bad66c7a', 1)
INSERT [dbo].[ProductsDiscounts] ([DiscountID], [ProductID]) VALUES (N'b6ff6ba9-6dd3-408d-9acc-af4dcc2b90c3', 1)
INSERT [dbo].[ProductsDiscounts] ([DiscountID], [ProductID]) VALUES (N'c88dca88-262a-4ca7-8238-31e3e1576e57', 10)
INSERT [dbo].[ProductsDiscounts] ([DiscountID], [ProductID]) VALUES (N'cd3507b7-f023-4a06-8dd4-b776520d266d', 1)
INSERT [dbo].[ProductsDiscounts] ([DiscountID], [ProductID]) VALUES (N'd7f2059b-97ef-4edc-9544-11859ea4eb4f', 1)
INSERT [dbo].[ProductsDiscounts] ([DiscountID], [ProductID]) VALUES (N'f12ace16-8ce7-43a4-b5fa-da0fe4070e4c', 4)
GO
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'023b4402-81f5-4d98-b3bf-c3443e4e6603', 10, 5, 5, 400, 8)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'02f6ee71-a090-4872-a9d2-50dbd91078fd', 14, 7, 7, 450, 6)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'0341ab81-4e45-4767-b013-4d0d910e27ed', 6, 10, 10, 113, 40)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'0db71e39-11b6-4ea7-ad3d-230b7473a75b', 10, 10, 20, 1000, 45)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'100a4ad8-806a-446f-9d86-552a06fe064d', 30, 20, 20, 2000, 52)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'11057d40-cc76-4489-a24e-5f1f736d1e3d', 10, 10, 15, 300, 51)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'1d4e2a1b-00a5-45e2-aa45-29b30e6b9717', 5, 10, 15, 100, 48)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'245f3ce0-46cc-4eff-bec6-2fdfc1492204', 10, 7, 7, 250, 36)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'2c0427a0-199f-42f1-87eb-4195829e118c', 15, 10, 10, 500, 43)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'2e2bcece-1fb3-426d-a6f7-81eb0a797acb', 10, 10, 10, 450, 46)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'327c6314-c2a2-4418-b31c-422e07105c88', 30, 8, 8, 750, 38)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'3504dab5-f748-45d8-8f11-70bf4e08b831', 9, 5, 5, 300, 10)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'368d4059-3205-4f25-abd1-9124334f4ab5', 25, 8, 8, 340, 35)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'3ce5999e-1873-4851-9ea1-1b47d59331fb', 25, 7, 7, 500, 61)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'3dc25a51-643f-4d27-a27c-bf4f834f319e', 12, 6, 6, 300, 2)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'4272492d-8c7a-4bb9-a074-196d4ee39e31', 10, 10, 15, 300, 20)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'4287fdbb-b029-4bae-9256-d7c4365ab5b1', 5, 15, 20, 500, 55)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'4299ce66-49fd-4417-9f11-ae2ddbea207d', 8, 4, 4, 200, 4)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'4ca90010-bc88-4bcd-984a-1e9b04f55c08', 10, 10, 15, 1000, 17)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'4d474ff5-11e5-49a0-af88-fe9e3a413818', 30, 20, 15, 2000, 44)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'4fe61926-e521-4e56-9aab-44a35106d283', 25, 6, 6, 250, 15)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'56313dba-63e0-4995-911c-a4afb313e969', 5, 10, 15, 500, 37)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'56d6c8d5-38d5-4aa0-b513-87e2e6475250', 30, 20, 20, 10000, 69)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'575f9494-6b90-4ea3-934c-2e8fff8c06fb', 10, 5, 15, 100, 50)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'5e03b958-1701-4036-ac97-0dcba20e0dbc', 10, 8, 12, 50, 49)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'5f811de9-2e57-4200-b871-1bf1869f1894', 25, 20, 20, 1500, 29)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'62bb96e1-5d04-4e4a-bc89-0818568741a1', 5, 10, 15, 250, 23)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'6a5ec43b-014a-4210-9085-661351c85bcb', 5, 10, 15, 150, 58)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'6daf9373-2b51-4acf-ad4e-1eaab3f47284', 12, 6, 6, 350, 7)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'71be9aca-fcc2-4b7e-b22d-63dc7e3047e6', 25, 8, 8, 340, 67)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'7a6b710c-2719-46e6-b0a4-548995314ca8', 5, 15, 20, 150, 68)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'7f07d085-1f15-47b9-98c4-7e2d134f9739', 5, 15, 20, 500, 77)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'82ea9427-ffa3-4e04-9e88-efa6d7db995b', 5, 15, 20, 113, 47)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'8423aa5a-f690-4be3-8d17-c0c7a5227a49', 25, 8, 8, 340, 34)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'87197fdf-cc04-4e96-9959-f10bcc582815', 10, 20, 25, 800, 64)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'8cd62046-14b3-4507-9cb7-f08c9cba41ec', 30, 20, 20, 5000, 74)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'8cf79ba1-140a-4b97-a77e-2e46211ec679', 5, 10, 10, 100, 14)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'8def537e-9931-439a-ae32-b798a9114a83', 25, 8, 8, 500, 75)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'910cb0d7-f20b-4dc9-909b-e7710406c9df', 30, 7, 7, 500, 76)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'94547304-daad-493e-a8ef-9c2411dfd355', 5, 15, 15, 200, 21)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'975bd472-ac03-4210-853a-0635608375ef', 5, 10, 15, 250, 57)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'9a9f228b-8c60-47cb-bb74-41a4a5247511', 10, 20, 20, 500, 16)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'9d46de03-eea4-4294-b81b-e75cd93bee07', 15, 8, 8, 450, 25)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'a7f63235-70e1-4e9d-9796-0a6cc2fb702c', 10, 10, 10, 340, 41)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'adeddb96-c973-4168-86d6-29f669cf8e94', 10, 5, 10, 100, 27)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'b1525f70-6290-4bbb-a5bb-eb8ee829f418', 15, 10, 10, 825, 28)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'b20d4380-89c4-45bd-be70-3c623274e430', 5, 8, 8, 150, 73)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'b294b512-f26e-4781-b4e2-27fdd6f94a9a', 5, 10, 15, 250, 56)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'b4bac5f7-4269-4450-a975-8ca308b68912', 5, 10, 10, 500, 33)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'b5833247-ca76-4a97-b6bd-69dd66cf290c', 5, 10, 15, 100, 31)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'b70f625f-1117-4793-b2a9-151e92d71333', 10, 5, 5, 250, 5)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'b9301cd8-b22f-4378-b2f5-f0a799e8eee5', 30, 30, 30, 16000, 18)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'bab157a5-054d-493d-b7e3-0e49c48af1b6', 10, 6, 6, 200, 30)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'bccf88aa-7180-4c49-95fd-b4fd3179f49e', 5, 10, 15, 200, 72)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'bcd7ea1c-356d-439d-8639-bf552a6eacc0', 5, 10, 15, 500, 12)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'bdea962e-17ed-4c3d-9fc2-118c62757199', 20, 30, 30, 5000, 59)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'c1c83be7-7e58-447d-a20d-c62a02502b03', 12, 6, 6, 355, 24)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'c29d6a69-6ddc-4f4b-b2b1-145c42fcaae5', 15, 8, 8, 625, 63)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'c2f9f880-69ea-4e17-9180-c1496c14b477', 5, 10, 15, 500, 22)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'c50db52c-398c-4d4f-ac93-92bddfe72bfb', 10, 10, 15, 200, 32)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'c6e031a1-283a-48bf-a62d-bc94b7abe30d', 10, 15, 25, 1000, 42)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'ce6dc35b-1c0a-4be4-93a9-f6b42d68dc45', 10, 15, 20, 1000, 53)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'd6c81b60-0f50-4954-879e-7979740ee03e', 5, 10, 15, 500, 71)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'd8e4c61e-253c-4cc3-bed7-c5f24dbbd51c', 25, 8, 8, 355, 70)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'd8ffc3fa-3d4e-4fbd-95af-681d8db95567', 12, 6, 6, 227, 66)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'db1047e6-d601-4e0a-a6e1-d516854b658a', 5, 10, 10, 300, 60)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'dc721c56-245a-493e-b20b-12deff9af073', 10, 15, 20, 250, 26)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'ddc875f0-6f37-4bcb-9eac-500a97ceaead', 15, 7, 7, 500, 3)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'e1452206-81fa-4bbe-a2a7-dc4e79c573ed', 15, 10, 20, 120, 19)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'e2fe387e-9163-4290-8103-c02c023b9a29', 20, 20, 30, 2000, 13)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'e3475c5d-1145-43c0-ba22-6ad1c50e8404', 5, 20, 20, 400, 62)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'e36661dc-c45f-4265-bfed-0feac8a59fee', 20, 5, 5, 227, 65)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'e5f0b938-b4dd-4a2b-a8a1-c46d2bc12375', 8, 4, 4, 200, 9)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'ecdd1009-a1cf-4df5-8453-feb6003e80cc', 10, 10, 10, 1000, 11)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'f844f117-90d7-4e0f-b347-217e4c0a2a05', 10, 5, 5, 250, 1)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'fa96c060-1c0a-4c08-a170-49c5696fb7ad', 5, 20, 20, 400, 54)
INSERT [dbo].[ProductSize] ([Id], [Height], [Width], [Length], [Weight], [ProductID]) VALUES (N'febb8dda-4fdd-4dbe-a290-a3052e524bce', 30, 8, 8, 750, 39)
GO
ALTER TABLE [dbo].[Products] ADD  CONSTRAINT [DF_Products_UnitPrice]  DEFAULT ((0)) FOR [UnitPrice]
GO
ALTER TABLE [dbo].[Products] ADD  CONSTRAINT [DF_Products_UnitsInStock]  DEFAULT ((0)) FOR [UnitsInStock]
GO
ALTER TABLE [dbo].[Products] ADD  CONSTRAINT [DF_Products_Discontinued]  DEFAULT ((0)) FOR [Discontinued]
GO
ALTER TABLE [dbo].[ProductSize] ADD  DEFAULT (NULL) FOR [Height]
GO
ALTER TABLE [dbo].[ProductSize] ADD  DEFAULT (NULL) FOR [Width]
GO
ALTER TABLE [dbo].[ProductSize] ADD  DEFAULT (NULL) FOR [Length]
GO
ALTER TABLE [dbo].[ProductSize] ADD  DEFAULT (NULL) FOR [Weight]
GO
ALTER TABLE [dbo].[ProductSize] ADD  DEFAULT (NULL) FOR [ProductID]
GO
ALTER TABLE [dbo].[Discounts]  WITH CHECK ADD FOREIGN KEY([DiscountTypeID])
REFERENCES [dbo].[DiscountTypes] ([DiscountTypeID])
GO
ALTER TABLE [dbo].[Products]  WITH NOCHECK ADD  CONSTRAINT [FK_Products_Categories] FOREIGN KEY([CategoryID])
REFERENCES [dbo].[Categories] ([CategoryID])
GO
ALTER TABLE [dbo].[Products] CHECK CONSTRAINT [FK_Products_Categories]
GO
ALTER TABLE [dbo].[ProductsDiscounts]  WITH CHECK ADD FOREIGN KEY([DiscountID])
REFERENCES [dbo].[Discounts] ([DiscountID])
GO
ALTER TABLE [dbo].[ProductsDiscounts]  WITH CHECK ADD FOREIGN KEY([ProductID])
REFERENCES [dbo].[Products] ([ProductID])
GO
ALTER TABLE [dbo].[ProductSize]  WITH CHECK ADD FOREIGN KEY([ProductID])
REFERENCES [dbo].[Products] ([ProductID])
GO
ALTER TABLE [dbo].[Products]  WITH NOCHECK ADD  CONSTRAINT [CK_Products_UnitPrice] CHECK  (([UnitPrice]>=(0)))
GO
ALTER TABLE [dbo].[Products] CHECK CONSTRAINT [CK_Products_UnitPrice]
GO
ALTER TABLE [dbo].[Products]  WITH NOCHECK ADD  CONSTRAINT [CK_UnitsInStock] CHECK  (([UnitsInStock]>=(0)))
GO
ALTER TABLE [dbo].[Products] CHECK CONSTRAINT [CK_UnitsInStock]
GO
/****** Object:  StoredProcedure [dbo].[AddCategory]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[AddCategory] @categoryname nvarchar(15), @description ntext, @picture varchar(256), @result bit output as
begin
	begin transaction;
	begin try
		if not exists (select * from Categories where CategoryName=@categoryname)
		begin
			insert into Categories(CategoryName, Description, Picture)
			values(@categoryname, @description, @picture)
			set @result = 1;
		end
		else begin set @result= 0 end;
		commit;
	end try
	begin catch
		rollback;
		set @result = 0;
	end catch;
end
GO
/****** Object:  StoredProcedure [dbo].[CreateProduct]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[CreateProduct]
	@productName VARCHAR(40),
	@quantityPerUnit VARCHAR(20),
	@unitPrice DECIMAL(10, 2),
	@unitsInStock SMALLINT,
	@discontinued BIT,
	@picture VARCHAR(256),
	@description TEXT,
	@categoryID INT,
	@height int,
    @width int,
    @length int,
    @weight int,
	@result bit output
as
begin
	begin transaction;
      begin try
		DECLARE @productID int;
		INSERT Products(ProductName, QuantityPerUnit, UnitPrice, UnitsInStock, Discontinued, Picture, Description, CategoryID)
		VALUES (@productName, @quantityPerUnit, @unitPrice, @unitsInStock, @discontinued, @picture, @description, @categoryID)
		
		SELECT @productid=SCOPE_IDENTITY();
        
        INSERT ProductSize(Id, Height, Width, Length, Weight, ProductID)
        VALUES(NEWID(), @height, @width, @length, @weight, @productid);
      
		set @result = 1;
        commit;
      end try
	  begin catch
		rollback;
		set @result=0;
	  end catch;
end ;;
GO
/****** Object:  StoredProcedure [dbo].[CreateProductSize]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[CreateProductSize]
	@productsizeid varchar(36),
    @productid int,
    @height int,
    @width int,
    @length int,
    @weight int,
    @result bit OUTPUT
AS
begin
	begin transaction;
    begin try
		INSERT ProductSize(Id, Height, Width, Length, Weight, ProductID)
        VALUES(@productsizeid, @height, @width, @length, @weight, @productid);
        SET @result = 1;
        COMMIT;
    end try
	begin catch
		rollback;
		set @result=0;
	end catch;
end ;;
GO
/****** Object:  StoredProcedure [dbo].[InsertDiscount]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[InsertDiscount] @discountID varchar(36), @discountPercent int, @discountCode varchar(MAX), @startDate datetime, @endDate datetime, @active bit,  @discountType varchar(20), @productID int, @result bit output as
begin 
	if (select count(1) from DiscountTypes dt where DiscountType=@discountType)>0
		begin
			begin transaction;
			begin try
				declare @discountTypeID int;
				select @discountTypeID=DiscountTypeID from DiscountTypes dt where DiscountType=@discountType

				declare @percentageBasedID int;
				select @percentageBasedID=DiscountTypeID from DiscountTypes dt where DiscountType='Percentage-based'

				if(select count(1) from DiscountDetails d join ProductsDiscounts pd on d.DiscountID=pd.DiscountID 
					where d.DiscountType='Percentage-based' and @discountType=DiscountType and @active=1 and pd.ProductID=@productID)>0 
					and @active=1
				begin
					update Discounts set Active=0 
					from Discounts d join ProductsDiscounts pd on d.DiscountID=pd.DiscountID 
					where DiscountTypeID=@percentageBasedID and pd.ProductID=@productID and d.Active=1;
				end

				insert into Discounts(DiscountID, DiscountPercent, StartDate, EndDate, DiscountCode, Active, DiscountTypeID) values(@discountID, @discountPercent, @startDate, @endDate, @discountCode, @active, @discountTypeID);
				insert into ProductsDiscounts(DiscountID, ProductID) values(@discountID, @productID);

				set @result=1;
			commit;
			end try
			begin catch
				rollback;
				set @result=0;
			end catch;
		end
	else 
	begin
		set @result=0;
		print 'Error: the provided discountType does not exist';
	end
end;
GO
/****** Object:  StoredProcedure [dbo].[InsertProduct]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[InsertProduct]
	@productName nvarchar(40), 
	@quantityPerUnit nvarchar(20), 
	@unitPrice money, 
	@unitsInStock smallint, 
	@discontinued bit, 
	@picture varchar(256),
	@description text,
	@categoryID int,
	@result bit output
as
begin
    begin transaction;
    begin try
        insert into Products(ProductName, QuantityPerUnit, UnitPrice, UnitsInStock, Discontinued, Picture, Description, CategoryID) 
        values(@productName, @quantityPerUnit, @unitPrice, @unitsInStock, @discontinued, @picture, @description, @categoryID)
        set @result=1;
        commit;
    end try
    begin catch
        rollback;
        set @result=0;
    end catch;
end;
GO
/****** Object:  StoredProcedure [dbo].[Ten Most Expensive Products]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

create procedure [dbo].[Ten Most Expensive Products] AS
SET ROWCOUNT 10
SELECT Products.ProductName AS TenMostExpensiveProducts, Products.UnitPrice
FROM Products
ORDER BY Products.UnitPrice DESC
GO
/****** Object:  StoredProcedure [dbo].[UpdateDiscount]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[UpdateDiscount] @discountID varchar(36), @discountPercent int, @discountCode varchar(MAX), @startDate datetime, @endDate datetime, @active bit,  @discountType varchar(20), @result bit output as
begin
	if (select count(1) from DiscountTypes dt where DiscountType=@discountType)>0
		begin
			begin transaction;
			begin try
				declare @discountTypeID int;
				declare @productID int;

				select @discountTypeID=DiscountTypeID 
				from DiscountTypes dt 
				where DiscountType=@discountType
				
				select @productID=pd.ProductID 
				from ProductsDiscounts pd join Discounts d on pd.DiscountID=d.DiscountID 
				where d.DiscountID=@discountID

				declare @percentageBasedID int;
				select @percentageBasedID=DiscountTypeID from DiscountTypes dt where DiscountType='Percentage-based'

				if((select count(1) 
					from DiscountDetails d join ProductsDiscounts pd 
						on d.DiscountID=pd.DiscountID 
					where d.DiscountType='Percentage-based' and 
						@discountType=DiscountType and 
						pd.ProductID=@productID and active=1)>0 
					and @active=1)
				begin
					update Discounts set Active=0 
					from Discounts d join ProductsDiscounts pd on d.DiscountID=pd.DiscountID 
					where DiscountTypeID=@percentageBasedID and pd.ProductID=@productID and Active=1;
				end

				update Discounts set DiscountPercent=@discountPercent, StartDate=@startDate, EndDate=@endDate, DiscountCode=@discountCode, Active=@active, DiscountTypeID=@discountTypeID
				where DiscountID=@discountID;

				set @result=1;
			commit;
			end try
			begin catch
				rollback;
				set @result=0;
			end catch;
		end
	else 
	begin
		set @result=0;
		print 'Error: the provided discountType does not exist';
	end
end;
GO
/****** Object:  StoredProcedure [dbo].[UpdateDiscountStatus]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create procedure [dbo].[UpdateDiscountStatus] @discountID varchar(36), @active bit, @result bit output as
begin
	begin transaction;
	begin try
		declare @discountTypeID int;
		select @discountTypeID=DiscountTypeID from Discounts where DiscountID=@discountID

		declare @percentageBasedID int;
		select @percentageBasedID=DiscountTypeID from DiscountTypes dt where DiscountType='Percentage-based'

		if(select count(1) from Discounts d join DiscountTypes dt on d.DiscountTypeID=dt.DiscountTypeID where DiscountType='Percentage-based' and @discountTypeID=dt.DiscountTypeID and Active=1)>0
		begin
			update Discounts set Active=0 where DiscountTypeID=@percentageBasedID;
		end

		update Discounts set Active=@active
		where DiscountID=@discountID;

		set @result=1;
	commit;
	end try
	begin catch
		rollback;
		set @result=0;
	end catch;
end;
GO
/****** Object:  StoredProcedure [dbo].[UpdateProduct]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[UpdateProduct] 
	@productID int, 
	@productName nvarchar(40), 
	@quantityPerUnit nvarchar(20), 
	@unitPrice money, 
	@unitsInStock smallint,  
	@discontinued bit, 
	@picture varchar(256),
	@descripton text,
	@categoryID int,
	@result bit output
as
begin
	begin transaction;
	begin try
		update Products
		set ProductName=@productName, QuantityPerUnit=@quantityPerUnit, 
			UnitPrice=@unitPrice, UnitsInStock=@unitsInStock, 
			Discontinued=@discontinued, picture=@picture, Description=@descripton,
			CategoryID=@categoryID
		where ProductID=@productID;

		set @result=1;
		commit;
	end try
	begin catch
		rollback;
		set @result=0;
	end catch;
end;
GO
/****** Object:  StoredProcedure [dbo].[UpdateProductSize]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[UpdateProductSize]
	@productID int,
    @height int,
    @width int,
    @length int,
    @weight int,
    @result BIT output
as
begin
    begin transaction;
    begin try
        UPDATE ProductSize
        SET Height=@height, Width=@width, Length=@length, Weight=@weight
		FROM ProductSize s join Products p on s.ProductID=p.ProductID
        where p.ProductID=@productID;
        set @result = 1;
        commit;
    end try
	begin catch
		rollback;
		set @result=0;
	end catch;
end ;;
GO
/****** Object:  StoredProcedure [dbo].[UpdateProductsUnitsInStock]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE procedure [dbo].[UpdateProductsUnitsInStock] @productIDs nvarchar(max), @unitsInStocks nvarchar(max), @result bit output as
begin
	begin transaction;
	begin try
		declare @listProductIDs table (IndexId int Identity(1,1), ProductID int)
		declare @listUnitsInstocks table (IndexId int Identity(1,1), UnitsInStocks int)

		insert into @listProductIDs select value from string_split(@productIDs, ',');
		insert into @listUnitsInstocks select value from string_split(@unitsInStocks, ',');

		update Products set UnitsInStock=lu.UnitsInStocks 
		from Products p join @listProductIDs lp on p.ProductID=lp.ProductID
			join @listUnitsInstocks lu on lp.IndexId=lu.IndexId

		set @result=1;
		commit;
	end try
	begin catch
		rollback;
		set @result=0;
	end catch;
end;
GO
/****** Object:  StoredProcedure [dbo].[UpdateProductUnitsInStock]    Script Date: 10/25/2024 9:48:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

create procedure [dbo].[UpdateProductUnitsInStock] @productID int, @unitsInStock int, @result bit output as
begin
	begin transaction;
	begin try
		update Products set UnitsInStock=@unitsInStock
		where ProductID=@productID
		set @result=1;
		commit;
	end try
	begin catch
		rollback;
		set @result=0;
	end catch;
end;
GO
USE [master]
GO
ALTER DATABASE [product] SET  READ_WRITE 
GO

USE [master]
GO
/****** Object:  Database [order]    Script Date: 10/25/2024 9:45:43 AM ******/
CREATE DATABASE [order]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'order', FILENAME = N'/var/opt/mssql/data/order.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'order_log', FILENAME = N'/var/opt/mssql/data/order_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [order] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [order].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [order] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [order] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [order] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [order] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [order] SET ARITHABORT OFF 
GO
ALTER DATABASE [order] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [order] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [order] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [order] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [order] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [order] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [order] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [order] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [order] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [order] SET  DISABLE_BROKER 
GO
ALTER DATABASE [order] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [order] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [order] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [order] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [order] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [order] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [order] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [order] SET RECOVERY FULL 
GO
ALTER DATABASE [order] SET  MULTI_USER 
GO
ALTER DATABASE [order] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [order] SET DB_CHAINING OFF 
GO
ALTER DATABASE [order] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [order] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [order] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [order] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'order', N'ON'
GO
ALTER DATABASE [order] SET QUERY_STORE = OFF
GO
USE [order]
GO
/****** Object:  Table [dbo].[OrderDetailsDiscounts]    Script Date: 10/25/2024 9:45:43 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[OrderDetailsDiscounts](
	[DiscountID] [varchar](36) NOT NULL,
	[ProductID] [int] NOT NULL,
	[OrderID] [varchar](36) NOT NULL,
	[AppliedDate] [datetime] NULL,
	[DiscountPercent] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[DiscountID] ASC,
	[ProductID] ASC,
	[OrderID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Order Details]    Script Date: 10/25/2024 9:45:43 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Order Details](
	[OrderID] [varchar](36) NOT NULL,
	[ProductID] [int] NOT NULL,
	[UnitPrice] [money] NOT NULL,
	[Quantity] [smallint] NOT NULL,
 CONSTRAINT [PK_Order_Details] PRIMARY KEY CLUSTERED 
(
	[OrderID] ASC,
	[ProductID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[Order Details Discount Sum]    Script Date: 10/25/2024 9:45:43 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[Order Details Discount Sum] as
SELECT od.OrderID, od.ProductID, COUNT(odd.DiscountID) AS NumberOfDiscounts, ISNULL(SUM(odd.DiscountPercent), 0) AS TotalDiscount, od.UnitPrice, od.Quantity
FROM     dbo.[Order Details] AS od LEFT OUTER JOIN
                  dbo.OrderDetailsDiscounts AS odd ON od.OrderID = odd.OrderID AND od.ProductID = odd.ProductID
GROUP BY od.OrderID, od.ProductID, od.UnitPrice, od.Quantity

GO
/****** Object:  Table [dbo].[Orders]    Script Date: 10/25/2024 9:45:43 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Orders](
	[OrderID] [varchar](36) NOT NULL,
	[CustomerID] [nchar](36) NULL,
	[EmployeeID] [nchar](36) NULL,
	[OrderDate] [datetime] NULL,
	[RequiredDate] [datetime] NULL,
	[ShippedDate] [datetime] NULL,
	[ShipVia] [int] NULL,
	[Freight] [money] NULL,
	[ShipName] [nvarchar](40) NULL,
	[ShipAddress] [nvarchar](200) NULL,
	[ShipCity] [nvarchar](50) NULL,
	[Status] [varchar](10) NULL,
	[Phone] [nvarchar](24) NOT NULL,
	[ShipDistrict] [nvarchar](50) NULL,
	[ShipWard] [nvarchar](50) NULL,
 CONSTRAINT [PK_Orders] PRIMARY KEY CLUSTERED 
(
	[OrderID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[Order Details Extended]    Script Date: 10/25/2024 9:45:43 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE view [dbo].[Order Details Extended] as
SELECT ode.OrderID, ode.ProductID, ode.UnitPrice, ode.Quantity, 
		ode.TotalDiscount AS Discount, 
		CONVERT(money, (ode.UnitPrice * ode.Quantity) * (1 - CAST(ode.TotalDiscount AS float) / 100)) AS ExtendedPrice, 
		o.Freight, o.Status, o.CustomerID, 
                  o.EmployeeID
FROM     dbo.[Order Details Discount Sum] AS ode INNER JOIN
         dbo.Orders AS o ON o.OrderID = ode.OrderID
GO
/****** Object:  View [dbo].[Invoices]    Script Date: 10/25/2024 9:45:43 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE view [dbo].[Invoices] as
SELECT o.ShipName, o.ShipAddress, o.ShipCity, o.ShipDistrict, o.ShipWard, o.Phone, o.CustomerID, o.EmployeeID, o.OrderID, o.OrderDate, o.RequiredDate, o.ShippedDate, o.ShipVia AS ShipperID, ode.ProductID, ode.UnitPrice, ode.Quantity, ode.ExtendedPrice, 
                  ISNULL(o.Freight, 0) AS Freight, o.Status, odd.DiscountID, ISNULL(odd.DiscountPercent, 0) AS DiscountPercent
FROM     dbo.Orders AS o INNER JOIN
                  dbo.[Order Details Extended] AS ode ON o.OrderID = ode.OrderID LEFT OUTER JOIN
                  dbo.OrderDetailsDiscounts AS odd ON odd.OrderID = ode.OrderID AND odd.ProductID = ode.ProductID
GO
ALTER TABLE [dbo].[Order Details] ADD  CONSTRAINT [DF_Order_Details_UnitPrice]  DEFAULT ((0)) FOR [UnitPrice]
GO
ALTER TABLE [dbo].[Order Details] ADD  CONSTRAINT [DF_Order_Details_Quantity]  DEFAULT ((1)) FOR [Quantity]
GO
ALTER TABLE [dbo].[Orders] ADD  CONSTRAINT [DF_Orders_Freight]  DEFAULT ((0)) FOR [Freight]
GO
ALTER TABLE [dbo].[Order Details]  WITH NOCHECK ADD  CONSTRAINT [FK_Order_Details_Orders] FOREIGN KEY([OrderID])
REFERENCES [dbo].[Orders] ([OrderID])
GO
ALTER TABLE [dbo].[Order Details] CHECK CONSTRAINT [FK_Order_Details_Orders]
GO
ALTER TABLE [dbo].[OrderDetailsDiscounts]  WITH CHECK ADD  CONSTRAINT [FK_OrderDetail_ProductID_OrderID] FOREIGN KEY([OrderID], [ProductID])
REFERENCES [dbo].[Order Details] ([OrderID], [ProductID])
GO
ALTER TABLE [dbo].[OrderDetailsDiscounts] CHECK CONSTRAINT [FK_OrderDetail_ProductID_OrderID]
GO
ALTER TABLE [dbo].[Order Details]  WITH NOCHECK ADD  CONSTRAINT [CK_Quantity] CHECK  (([Quantity]>(0)))
GO
ALTER TABLE [dbo].[Order Details] CHECK CONSTRAINT [CK_Quantity]
GO
ALTER TABLE [dbo].[Order Details]  WITH NOCHECK ADD  CONSTRAINT [CK_UnitPrice] CHECK  (([UnitPrice]>=(0)))
GO
ALTER TABLE [dbo].[Order Details] CHECK CONSTRAINT [CK_UnitPrice]
GO
ALTER TABLE [dbo].[Orders]  WITH CHECK ADD  CONSTRAINT [CK_Order_Status] CHECK  (([Status]='canceled' OR [Status]='successful' OR [Status]='shipping' OR [Status]='confirmed' OR [Status]='pending'))
GO
ALTER TABLE [dbo].[Orders] CHECK CONSTRAINT [CK_Order_Status]
GO
/****** Object:  StoredProcedure [dbo].[GetCustomerInvoice]    Script Date: 10/25/2024 9:45:43 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create procedure [dbo].[GetCustomerInvoice] @orderId nchar(36), @customerId nchar(36) as
SELECT * FROM invoices i 
WHERE i.orderid=@orderId AND 
	i.customerid=@customerId;
GO
/****** Object:  StoredProcedure [dbo].[InsertOrder]    Script Date: 10/25/2024 9:45:43 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[InsertOrder] @orderID varchar(36), @orderDate datetime, @requiredDate datetime, 
	@shippedDate datetime, @freight money, @shipName nvarchar(40), @shipAddress nvarchar(200),
	@shipCity nvarchar(50), @shipDistrict nvarchar(50), @shipWard nvarchar(50), @phone nvarchar(24), 
	@status varchar(10), @customerID nchar(36),@employeeID nchar(36), @shipperID int, @result bit output as
begin
	begin transaction;
	begin try
		insert Orders(OrderID, OrderDate, RequiredDate, ShippedDate, Freight, ShipName, ShipAddress, ShipCity, ShipDistrict, ShipWard, Phone, Status, CustomerID, EmployeeID, ShipVia)
		values (@orderID, @orderDate, @requiredDate, @shippedDate, @freight, @shipName, @shipAddress, @shipCity, @shipDistrict, @shipWard, @phone, @status, @customerID, @employeeID, @shipperID);
		set @result=1;
		commit;
	end try
	begin catch
		rollback;
		set @result=0;
	end catch
end;
GO
/****** Object:  StoredProcedure [dbo].[InsertOrderDetail]    Script Date: 10/25/2024 9:45:43 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create procedure [dbo].[InsertOrderDetail] @productID int, @orderID varchar(36), @unitPrice money, @quantity int, @result bit output as
begin
	begin transaction;
	begin try
		insert [Order Details](OrderID, ProductID, UnitPrice, Quantity)
		values (@orderID, @productID, @unitPrice, @quantity);
		set @result=1;
		commit;
	end try
	begin catch
		rollback;
		set @result=0;
	end catch
end;
/****** Object:  StoredProcedure [dbo].[InsertOrder]    Script Date: 5/16/2024 2:05:00 PM ******/
SET ANSI_NULLS ON
GO
/****** Object:  StoredProcedure [dbo].[InsertOrderDetailDiscount]    Script Date: 10/25/2024 9:45:43 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[InsertOrderDetailDiscount] @orderID varchar(36), @productID int, @discountID varchar(36), @discountPercent int, @appliedDate datetime, @Result bit output as
begin
	-- declare @countDiscount int;
	-- declare @maxCountDiscountType int;
	-- declare @discountTypeID int;
	-- declare @discountPercent int;
	
	-- select @discountTypeID=DiscountTypeID, @discountPercent=DiscountPercent 
	-- from Discounts where DiscountID=@discountID

	-- select @countDiscount= count(DiscountID) 
	-- from OrderDetailsDiscounts 
	-- where OrderID=@orderID and ProductID=@productID
		
	-- select @maxCountDiscountType=(count(odd.DiscountID))
	-- from OrderDetailsDiscounts odd join Discounts d on odd.DiscountID=d.DiscountID
	-- where OrderID=@orderID and ProductID=@productID and d.DiscountTypeID=@discountTypeID

	begin transaction;
	begin try
		-- if @countDiscount<2 and @maxCountDiscountType<1
		-- 	begin
		-- 	end;
		-- else
		-- 	begin 
		-- 		print'Can not insert into OrderDetailsDiscount table';
		-- 		set @Result=0;
		-- 	end
        insert into OrderDetailsDiscounts(DiscountID, OrderID, ProductID, AppliedDate, [DiscountPercent]) 
            values(@discountID, @orderID, @productID, @appliedDate, @discountPercent);
        set @Result=1;
		commit;
	end try 
	begin catch
		rollback;
		print'Some errors have occured'
		set @Result=0;
	end catch
end;
GO
USE [master]
GO
ALTER DATABASE [order] SET  READ_WRITE 
GO

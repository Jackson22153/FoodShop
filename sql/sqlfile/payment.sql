USE [master]
GO
/****** Object:  Database [payment]    Script Date: 10/25/2024 9:46:55 AM ******/
CREATE DATABASE [payment]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'payment', FILENAME = N'/var/opt/mssql/data/payment.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'payment_log', FILENAME = N'/var/opt/mssql/data/payment_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [payment] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [payment].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [payment] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [payment] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [payment] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [payment] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [payment] SET ARITHABORT OFF 
GO
ALTER DATABASE [payment] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [payment] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [payment] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [payment] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [payment] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [payment] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [payment] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [payment] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [payment] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [payment] SET  DISABLE_BROKER 
GO
ALTER DATABASE [payment] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [payment] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [payment] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [payment] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [payment] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [payment] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [payment] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [payment] SET RECOVERY FULL 
GO
ALTER DATABASE [payment] SET  MULTI_USER 
GO
ALTER DATABASE [payment] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [payment] SET DB_CHAINING OFF 
GO
ALTER DATABASE [payment] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [payment] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [payment] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [payment] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'payment', N'ON'
GO
ALTER DATABASE [payment] SET QUERY_STORE = OFF
GO
USE [payment]
GO
/****** Object:  Table [dbo].[PaymentMethods]    Script Date: 10/25/2024 9:46:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PaymentMethods](
	[MethodID] [nchar](36) NOT NULL,
	[MethodName] [varchar](20) NOT NULL,
	[Details] [text] NULL,
PRIMARY KEY CLUSTERED 
(
	[MethodID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Payments]    Script Date: 10/25/2024 9:46:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Payments](
	[PaymentID] [nchar](36) NOT NULL,
	[PaymentDate] [datetime] NULL,
	[TransactionID] [varchar](255) NULL,
	[Amount] [decimal](10, 2) NULL,
	[CustomerID] [nchar](36) NULL,
	[OrderID] [nchar](36) NULL,
	[MethodID] [nchar](36) NULL,
	[Status] [varchar](10) NULL,
PRIMARY KEY CLUSTERED 
(
	[PaymentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[PaymentDetails]    Script Date: 10/25/2024 9:46:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[PaymentDetails] as
select p.*, m.MethodName
from Payments p join PaymentMethods m on p.MethodID=m.MethodID;
GO
/****** Object:  Table [dbo].[StoreLocation]    Script Date: 10/25/2024 9:46:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[StoreLocation](
	[Id] [nchar](36) NOT NULL,
	[Phone] [nvarchar](24) NULL,
	[Address] [nvarchar](200) NULL,
	[City] [nvarchar](50) NULL,
	[District] [nvarchar](50) NULL,
	[Ward] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
INSERT [dbo].[PaymentMethods] ([MethodID], [MethodName], [Details]) VALUES (N'1363d5fa-0084-42ad-9550-29151b7182c9', N'zalopay', N'ZaloPay is a prominent player in the fintech industry, focusing on digital financial services.')
INSERT [dbo].[PaymentMethods] ([MethodID], [MethodName], [Details]) VALUES (N'4bc4cecb-8609-4118-a8e3-eb7f18992c6f', N'paypal', N'PayPal is a popular online payment service that allows users to send and receive payments online. It provides a secure and convenient way for individuals and businesses to make payments online.')
INSERT [dbo].[PaymentMethods] ([MethodID], [MethodName], [Details]) VALUES (N'59e1a754-d01b-41a4-bdce-5788a34645c3', N'cod', N'COD stands for Cash on Delivery, which is a payment method where the payment is made at the time of delivery of the goods or services.')
GO
INSERT [dbo].[StoreLocation] ([Id], [Phone], [Address], [City], [District], [Ward]) VALUES (N'E7B9DFBB-0A6A-4852-9483-C8CAE3D23893', N'0375078039', N'17A Đ. Cộng Hòa', N'Hồ Chí Minh', N'Quận Tân Bình', N'Phường 4')
GO
ALTER TABLE [dbo].[Payments] ADD  DEFAULT (NULL) FOR [PaymentDate]
GO
ALTER TABLE [dbo].[Payments] ADD  DEFAULT (NULL) FOR [TransactionID]
GO
ALTER TABLE [dbo].[Payments] ADD  DEFAULT (NULL) FOR [Amount]
GO
ALTER TABLE [dbo].[Payments] ADD  DEFAULT (NULL) FOR [CustomerID]
GO
ALTER TABLE [dbo].[Payments] ADD  DEFAULT (NULL) FOR [OrderID]
GO
ALTER TABLE [dbo].[Payments] ADD  DEFAULT (NULL) FOR [MethodID]
GO
ALTER TABLE [dbo].[Payments] ADD  DEFAULT (NULL) FOR [Status]
GO
ALTER TABLE [dbo].[Payments]  WITH CHECK ADD FOREIGN KEY([MethodID])
REFERENCES [dbo].[PaymentMethods] ([MethodID])
GO
/****** Object:  StoredProcedure [dbo].[GetAmountPerMonth]    Script Date: 10/25/2024 9:46:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create procedure [dbo].[GetAmountPerMonth] @year int, @paymentStatus varchar(10) as
begin
	select MONTH(paymentdate) as Month, sum(amount) as Total
	from Payments p
	where YEAR(paymentdate) = @year and status=@paymentStatus
	group by MONTH(paymentdate)
	order by Month
end;
GO
/****** Object:  StoredProcedure [dbo].[GetPaymentPercentage]    Script Date: 10/25/2024 9:46:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[GetPaymentPercentage] @year int
AS BEGIN
	select p.status as Status,  
		ROUND(CAST(COUNT(*) AS float) / NULLIF((SELECT COUNT(*) FROM Payments), 0)*100, 2) AS Percentage
	from Payments p
	where YEAR(p.paymentdate) = @year
	group by p.status
END;
GO
/****** Object:  StoredProcedure [dbo].[GetPaymentYear]    Script Date: 10/25/2024 9:46:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create procedure [dbo].[GetPaymentYear] 
as begin
	SELECT Year(paymentdate) as Year
	FROM Payments
	group by year(paymentdate)
	order by Year desc
end;
GO
/****** Object:  StoredProcedure [dbo].[SaveFullPayment]    Script Date: 10/25/2024 9:46:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[SaveFullPayment]
  @paymentID nchar(36),
  @paymentDate datetime,
  @amount DECIMAL(10, 2),
  @transactionID VARCHAR(255),
  @customerID VARCHAR(36),
  @orderID VARCHAR(36),
  @status VARCHAR(10),
  @paymentMethod VARCHAR(20)
AS
BEGIN
	BEGIN transaction;
	BEGIN TRY
		INSERT INTO Payments(PaymentID, PaymentDate, TransactionID, Amount, CustomerID, OrderID, Status, MethodID)
		SELECT @paymentID, @paymentDate, @transactionID, @amount, @customerID, @orderID, @status, m.MethodID
		FROM PaymentMethods m
		WHERE m.MethodName = @paymentMethod;
		COMMIT;
	END TRY
	BEGIN CATCH
		ROLLBACK;
	END CATCH;
END;
GO
/****** Object:  StoredProcedure [dbo].[SavePayment]    Script Date: 10/25/2024 9:46:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[SavePayment]
	@paymentID varchar(36),
    @paymentDate datetime,
    @amount decimal(10, 2),
    @customerID varchar(36),
    @orderID varchar(36),
    @status varchar(10),
    @paymentMethod varchar(20)
as
BEGIN
	BEGIN TRANSACTION;
    begin TRY
		INSERT INTO Payments(PaymentID, PaymentDate, Amount, CustomerID, OrderID, Status, MethodID)
        select @paymentID, @paymentDate, @amount, @customerID, @orderID, @status, m.methodid
        from PaymentMethods m
        where m.MethodName=@paymentMethod;
        
		commit;
    END TRY
	BEGIN CATCH
		ROLLBACK;
	END CATCH;
END ;
GO
/****** Object:  StoredProcedure [dbo].[UpdatePayment]    Script Date: 10/25/2024 9:46:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[UpdatePayment]
	@paymentID varchar(36),
    @transactionID varchar(255),
    @status varchar(10)
as
begin
	begin transaction;
    begin try
		Update Payments set TransactionID=@transactionID, Status=@status
		FROM Payments
        where PaymentID=@paymentID;
        commit;
    end try
	begin catch
		rollback;
	end catch;
end ;
GO
/****** Object:  StoredProcedure [dbo].[UpdatePaymentStatus]    Script Date: 10/25/2024 9:46:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[UpdatePaymentStatus]
	@paymentID varchar(36),
    @status varchar(10)
as
begin
	begin transaction;
    begin try
		Update Payments set Status = @status
        where PaymentID=@paymentID;
        commit;
    end try
	begin catch
		rollback;
	end catch;
end ;;
GO
/****** Object:  StoredProcedure [dbo].[UpdatePaymentStatusByOrderID]    Script Date: 10/25/2024 9:46:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[UpdatePaymentStatusByOrderID]
	@orderID varchar(36),
    @status varchar(10)
as
begin
	begin transaction;
    begin try
		update Payments set Status=@status where Orderid=@orderID;
		commit;
    end try
	begin catch
		rollback;
	end catch;
end ;;
GO
USE [master]
GO
ALTER DATABASE [payment] SET  READ_WRITE 
GO

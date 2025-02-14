USE [master]
GO
/****** Object:  Database [profile]    Script Date: 10/25/2024 9:49:55 AM ******/
CREATE DATABASE [profile]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'profile', FILENAME = N'/var/opt/mssql/data/profile.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'profile_log', FILENAME = N'/var/opt/mssql/data/profile_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [profile] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [profile].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [profile] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [profile] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [profile] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [profile] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [profile] SET ARITHABORT OFF 
GO
ALTER DATABASE [profile] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [profile] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [profile] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [profile] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [profile] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [profile] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [profile] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [profile] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [profile] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [profile] SET  DISABLE_BROKER 
GO
ALTER DATABASE [profile] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [profile] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [profile] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [profile] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [profile] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [profile] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [profile] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [profile] SET RECOVERY FULL 
GO
ALTER DATABASE [profile] SET  MULTI_USER 
GO
ALTER DATABASE [profile] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [profile] SET DB_CHAINING OFF 
GO
ALTER DATABASE [profile] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [profile] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [profile] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [profile] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'profile', N'ON'
GO
ALTER DATABASE [profile] SET QUERY_STORE = OFF
GO
USE [profile]
GO
/****** Object:  Table [dbo].[UserProfile]    Script Date: 10/25/2024 9:49:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[UserProfile](
	[ProfileID] [nchar](36) NOT NULL,
	[Address] [nvarchar](200) NULL,
	[City] [nvarchar](50) NULL,
	[Phone] [nvarchar](24) NULL,
	[Picture] [varchar](256) NULL,
	[UserID] [nchar](36) NOT NULL,
	[District] [nvarchar](50) NULL,
	[Ward] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[ProfileID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Employees]    Script Date: 10/25/2024 9:49:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Employees](
	[EmployeeID] [nchar](36) NOT NULL,
	[Title] [nvarchar](30) NULL,
	[BirthDate] [date] NULL,
	[HireDate] [date] NULL,
	[Notes] [ntext] NULL,
	[ProfileID] [nchar](36) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[EmployeeID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  View [dbo].[EmployeeDetails]    Script Date: 10/25/2024 9:49:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE view [dbo].[EmployeeDetails] as
SELECT e.EmployeeID, u.UserID, e.BirthDate, e.HireDate, u.Phone, u.Picture, e.Title, u.Address, u.City, u.District, u.Ward, e.Notes
FROM     dbo.UserProfile AS u INNER JOIN
         dbo.Employees AS e ON u.ProfileID = e.ProfileID
GO
/****** Object:  Table [dbo].[Customers]    Script Date: 10/25/2024 9:49:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Customers](
	[CustomerID] [nchar](36) NOT NULL,
	[ContactName] [nvarchar](30) NULL,
	[ProfileID] [nchar](36) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[CustomerID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[CustomerDetails]    Script Date: 10/25/2024 9:49:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE view [dbo].[CustomerDetails] as
SELECT c.CustomerID, u.UserID, c.ContactName, u.Address, u.City, u.District, u.Ward, u.Phone, u.Picture
FROM dbo.UserProfile AS u INNER JOIN
     dbo.Customers AS c ON u.ProfileID = c.ProfileID
GO
/****** Object:  Table [dbo].[Shippers]    Script Date: 10/25/2024 9:49:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Shippers](
	[ShipperID] [int] IDENTITY(1,1) NOT NULL,
	[CompanyName] [nvarchar](40) NOT NULL,
	[Phone] [nvarchar](24) NULL
) ON [PRIMARY]
GO
INSERT [dbo].[Customers] ([CustomerID], [ContactName], [ProfileID]) VALUES (N'491f1e10-64ba-45cf-bcec-878e8ead22c3', N'Trong Phu1c', N'47045f6a-34c4-4dc6-b3a0-fa4453b5da30')
INSERT [dbo].[Customers] ([CustomerID], [ContactName], [ProfileID]) VALUES (N'c02bafd6-0082-4b9f-b232-342471eae90b', N'happy123', N'3a8e9fad-c24a-4e20-be0a-edce05e36150')
GO
INSERT [dbo].[Employees] ([EmployeeID], [Title], [BirthDate], [HireDate], [Notes], [ProfileID]) VALUES (N'35e31e6b-d3c1-45c1-9b38-66813e576713', NULL, NULL, CAST(N'2024-06-14' AS Date), N'', N'71fc0684-de2e-4757-a11b-b8b85ed9ba1b')
INSERT [dbo].[Employees] ([EmployeeID], [Title], [BirthDate], [HireDate], [Notes], [ProfileID]) VALUES (N'577378d4-bb82-495a-9c70-d0b1aa6de9d6', NULL, CAST(N'2000-04-04' AS Date), CAST(N'2024-05-01' AS Date), N'This is an awesome guy....', N'7e7fc974-755e-4d24-be2a-bba6fd3e5dd4')
GO
SET IDENTITY_INSERT [dbo].[Shippers] ON 

INSERT [dbo].[Shippers] ([ShipperID], [CompanyName], [Phone]) VALUES (1, N'Speedy Express', N'(503) 555-9831')
INSERT [dbo].[Shippers] ([ShipperID], [CompanyName], [Phone]) VALUES (2, N'United Package', N'(503) 555-3199')
INSERT [dbo].[Shippers] ([ShipperID], [CompanyName], [Phone]) VALUES (3, N'Federal Shipping', N'(503) 555-9931')
SET IDENTITY_INSERT [dbo].[Shippers] OFF
GO
INSERT [dbo].[UserProfile] ([ProfileID], [Address], [City], [Phone], [Picture], [UserID], [District], [Ward]) VALUES (N'3a8e9fad-c24a-4e20-be0a-edce05e36150', N'Obere Str. 57', N'Lào Cai', N'0300074321', N'3a1a3f5b-d4ee-4d62-9d29-9c155b23aac6.png', N'3072e836-7469-454d-9165-e7761f3f2eb7', N'Huyện Văn Bàn', N'Xã Sơn Thuỷ')
INSERT [dbo].[UserProfile] ([ProfileID], [Address], [City], [Phone], [Picture], [UserID], [District], [Ward]) VALUES (N'47045f6a-34c4-4dc6-b3a0-fa4453b5da30', NULL, NULL, N'0379349826', NULL, N'704c1690-d96b-4258-bbea-061ac2261ccd', NULL, NULL)
INSERT [dbo].[UserProfile] ([ProfileID], [Address], [City], [Phone], [Picture], [UserID], [District], [Ward]) VALUES (N'71fc0684-de2e-4757-a11b-b8b85ed9ba1b', NULL, NULL, NULL, N'9df7ccee-b3cb-44fb-b1c2-1aae2ec946a4.png', N'01efea5a-2668-497b-a756-44e70191fa1b', NULL, NULL)
INSERT [dbo].[UserProfile] ([ProfileID], [Address], [City], [Phone], [Picture], [UserID], [District], [Ward]) VALUES (N'7e7fc974-755e-4d24-be2a-bba6fd3e5dd4', N'OIbanoi Xs', N'Hồ Chí Minh', N'0982359295', N'd2fa2968-a767-41ec-b989-b49b0155af5e.png', N'47fdb832-46bb-495c-8b3d-06bb817e29db', N'Quận Tân Bình', N'Phường 4')
GO
ALTER TABLE [dbo].[Customers]  WITH CHECK ADD FOREIGN KEY([ProfileID])
REFERENCES [dbo].[UserProfile] ([ProfileID])
GO
ALTER TABLE [dbo].[Employees]  WITH CHECK ADD FOREIGN KEY([ProfileID])
REFERENCES [dbo].[UserProfile] ([ProfileID])
GO
/****** Object:  StoredProcedure [dbo].[AddNewCustomer]    Script Date: 10/25/2024 9:49:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[AddNewCustomer] @profileID nchar(36), @userID nchar(36), 
	@customerID nchar(36), @contactName nvarchar(30), @result bit output as
begin
	begin transaction
	begin try
		insert into UserProfile(ProfileID, UserID) 
		values(@profileID, @userID);

		insert into Customers(CustomerID, ContactName, ProfileID) 
		values (@customerID, @contactName, @profileID )

		set @result=1;
		commit;
	end try
	begin catch
		rollback
		set @result=0;
	end catch;
end
GO
/****** Object:  StoredProcedure [dbo].[AddNewEmployee]    Script Date: 10/25/2024 9:49:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[AddNewEmployee] @profileID nchar(36), @userID nchar(36), 
	@employeeID nchar(36), @result bit output as
begin
	begin transaction
	begin try
		insert into UserProfile(ProfileID, UserID) 
		values(@profileID, @userID);

		insert into Employees(EmployeeID, ProfileID) 
		values(@employeeID, @profileID)
		set @result=1;
		commit;
	end try
	begin catch
		rollback
		set @result=0;
	end catch;
end
GO
/****** Object:  StoredProcedure [dbo].[UpdateAdminEmployeeInfo]    Script Date: 10/25/2024 9:49:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[UpdateAdminEmployeeInfo] @employeeID nchar(36), 
	@hireDate date, @picture varchar(256), @title nvarchar(30), 
	@notes ntext, @result bit OUTPUT as
begin 
	begin transaction;
	begin try
		update UserProfile set Picture=@picture
		from UserProfile u join Employees e on u.ProfileID=e.ProfileID
		where e.EmployeeID=@employeeID

		update Employees set HireDate=@hireDate,
				Title=@title,
				Notes=@notes
			where EmployeeID=@employeeID
		set @result=1;
		commit;
	end try
	begin catch
		rollback;
		set @result=0;
	end catch;
end
GO
/****** Object:  StoredProcedure [dbo].[UpdateCustomerInfo]    Script Date: 10/25/2024 9:49:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[UpdateCustomerInfo] @customerID nchar(36), 
	@contactName nvarchar(30),@address nvarchar(200), @city nvarchar(50),
	@district nvarchar(50), @ward nvarchar(50), @phone nvarchar(24), 
	@picture varchar(256), @result bit OUTPUT as
begin 
	begin transaction;
	begin try
		update UserProfile set 
			Address=@address, 
			City=@city,
			District=@district,
			Ward=@ward,
			Phone=@phone, 
			picture=@picture 
				from UserProfile u join Customers c on u.ProfileID=c.ProfileID where c.CustomerID=@customerID
		update Customers 
			set ContactName=@contactName
			where CustomerID=@customerID
		set @result=1;
		commit;
	end try
	begin catch
		rollback;
		set @result=0;
	end catch;
end
GO
/****** Object:  StoredProcedure [dbo].[UpdateEmployeeInfo]    Script Date: 10/25/2024 9:49:55 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[UpdateEmployeeInfo] @employeeID nchar(36), 
	@birthDate date, @address nvarchar(200), @city nvarchar(50), 
	@district nvarchar(50), @ward nvarchar(50),@phone nvarchar(24), 
	@picture varchar(256), @result bit OUTPUT as
begin 
	begin transaction;
	begin try
		update UserProfile set Address=@address, 
				City=@city,
				District=@district,
				Ward=@ward,
				Phone=@phone,
				Picture=@picture
			from UserProfile u join Employees e on u.ProfileID=e.ProfileID
			where e.EmployeeID=@employeeID;

		update Employees set BirthDate=@birthDate
			where EmployeeID=@employeeID
		set @result=1;
		commit;
	end try
	begin catch
		rollback;
		set @result=0;
	end catch;
end
GO
USE [master]
GO
ALTER DATABASE [profile] SET  READ_WRITE 
GO

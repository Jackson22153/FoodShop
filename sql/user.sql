USE [master]
GO
/****** Object:  Database [user]    Script Date: 6/29/2024 12:51:06 PM ******/
CREATE DATABASE [user]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'user', FILENAME = N'/var/opt/mssql/data/user.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'user_log', FILENAME = N'/var/opt/mssql/data/user_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [user] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [user].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [user] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [user] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [user] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [user] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [user] SET ARITHABORT OFF 
GO
ALTER DATABASE [user] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [user] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [user] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [user] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [user] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [user] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [user] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [user] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [user] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [user] SET  DISABLE_BROKER 
GO
ALTER DATABASE [user] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [user] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [user] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [user] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [user] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [user] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [user] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [user] SET RECOVERY FULL 
GO
ALTER DATABASE [user] SET  MULTI_USER 
GO
ALTER DATABASE [user] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [user] SET DB_CHAINING OFF 
GO
ALTER DATABASE [user] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [user] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [user] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [user] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'user', N'ON'
GO
ALTER DATABASE [user] SET QUERY_STORE = OFF
GO
USE [user]
GO
/****** Object:  Table [dbo].[Employees]    Script Date: 6/29/2024 12:51:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Employees](
	[EmployeeID] [nchar](36) NOT NULL,
	[LastName] [nvarchar](20) NOT NULL,
	[FirstName] [nvarchar](10) NOT NULL,
	[Title] [nvarchar](30) NULL,
	[BirthDate] [date] NULL,
	[HireDate] [date] NULL,
	[Address] [nvarchar](200) NULL,
	[City] [nvarchar](50) NULL,
	[HomePhone] [nvarchar](24) NULL,
	[Notes] [ntext] NULL,
	[UserID] [nchar](36) NOT NULL,
	[Photo] [varchar](256) NULL,
 CONSTRAINT [PK_Employees] PRIMARY KEY CLUSTERED 
(
	[EmployeeID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 6/29/2024 12:51:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[UserID] [nchar](36) NOT NULL,
	[Username] [nvarchar](20) NOT NULL,
	[Password] [nvarchar](max) NOT NULL,
	[Email] [varchar](320) NULL,
	[EmailVerified] [bit] NOT NULL,
	[Enabled] [bit] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[UserID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  View [dbo].[EmployeeDetails]    Script Date: 6/29/2024 12:51:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE view [dbo].[EmployeeDetails] as
SELECT e.EmployeeID, u.UserID, u.Username, u.Email, e.FirstName, e.LastName, e.BirthDate, e.HireDate, e.HomePhone, e.Photo, e.Title, e.Address, e.City
FROM     dbo.Users AS u INNER JOIN
                  dbo.Employees AS e ON u.UserID = e.UserID
GO
/****** Object:  View [dbo].[EmployeeAccounts]    Script Date: 6/29/2024 12:51:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE view [dbo].[EmployeeAccounts] as
SELECT u.UserID, u.Username, u.Email, e.EmployeeID, e.FirstName, e.LastName, e.Photo
FROM     dbo.Users AS u INNER JOIN
                  dbo.Employees AS e ON u.UserID = e.userID
GO
/****** Object:  Table [dbo].[Customers]    Script Date: 6/29/2024 12:51:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Customers](
	[CustomerID] [nchar](36) NOT NULL,
	[ContactName] [nvarchar](30) NULL,
	[Address] [nvarchar](200) NULL,
	[City] [nvarchar](50) NULL,
	[Phone] [nvarchar](24) NULL,
	[Picture] [varchar](256) NULL,
	[UserID] [nchar](36) NOT NULL,
 CONSTRAINT [PK_Customers] PRIMARY KEY CLUSTERED 
(
	[CustomerID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[CustomerAccounts]    Script Date: 6/29/2024 12:51:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE view [dbo].[CustomerAccounts] as
SELECT u.UserID, u.Username, u.Email, c.CustomerID, c.ContactName, c.picture as Picture
FROM     dbo.Users AS u INNER JOIN
        dbo.Customers AS c ON u.UserID = c.userID
GO
/****** Object:  View [dbo].[CustomerDetails]    Script Date: 6/29/2024 12:51:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE view [dbo].[CustomerDetails] as
SELECT c.CustomerID, u.UserID, u.Username, u.Email, c.ContactName, c.Address, c.City, c.Phone, c.Picture
FROM     dbo.Users AS u INNER JOIN
                  dbo.Customers AS c ON u.UserID = c.UserID
GO
/****** Object:  Table [dbo].[Roles]    Script Date: 6/29/2024 12:51:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Roles](
	[RoleID] [int] IDENTITY(1,1) NOT NULL,
	[RoleName] [nvarchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[RoleID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UserRole]    Script Date: 6/29/2024 12:51:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[UserRole](
	[UserID] [nchar](36) NOT NULL,
	[RoleID] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[UserID] ASC,
	[RoleID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[UserRoles]    Script Date: 6/29/2024 12:51:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[UserRoles] as
SELECT u.userID as UserID, u.username as Username, u.email as Email, r.roleID as RoleID, r.roleName as RoleName
FROM     dbo.Users AS u LEFT OUTER JOIN
                  dbo.UserRole AS ur INNER JOIN
                  dbo.Roles AS r ON ur.roleID = r.roleID ON u.userID = ur.userID
GO
/****** Object:  Table [dbo].[Shippers]    Script Date: 6/29/2024 12:51:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Shippers](
	[ShipperID] [int] IDENTITY(1,1) NOT NULL,
	[CompanyName] [nvarchar](40) NOT NULL,
	[Phone] [nvarchar](24) NULL,
 CONSTRAINT [PK_Shippers] PRIMARY KEY CLUSTERED 
(
	[ShipperID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
INSERT [dbo].[Customers] ([CustomerID], [ContactName], [Address], [City], [Phone], [Picture], [UserID]) VALUES (N'491f1e10-64ba-45cf-bcec-878e8ead22c3', N'Trong Phu1c', NULL, NULL, N'0379349826', NULL, N'704c1690-d96b-4258-bbea-061ac2261ccd')
INSERT [dbo].[Customers] ([CustomerID], [ContactName], [Address], [City], [Phone], [Picture], [UserID]) VALUES (N'c02bafd6-0082-4b9f-b232-342471eae90b', N'happy123', N'Obere Str. 57', N'HCM', N'0300074321', N'3a1a3f5b-d4ee-4d62-9d29-9c155b23aac6.png', N'3072e836-7469-454d-9165-e7761f3f2eb7')
INSERT [dbo].[Customers] ([CustomerID], [ContactName], [Address], [City], [Phone], [Picture], [UserID]) VALUES (N'c437e4a9-f66b-476a-ab14-1229db7b925a', N'another2001', NULL, NULL, NULL, NULL, N'47fdb832-46bb-495c-8b3d-06bb817e29db')
GO
INSERT [dbo].[Employees] ([EmployeeID], [LastName], [FirstName], [Title], [BirthDate], [HireDate], [Address], [City], [HomePhone], [Notes], [UserID], [Photo]) VALUES (N'35e31e6b-d3c1-45c1-9b38-66813e576713', N'trong', N'phuc', NULL, NULL, CAST(N'2024-06-14' AS Date), NULL, NULL, NULL, N'', N'01efea5a-2668-497b-a756-44e70191fa1b', N'9df7ccee-b3cb-44fb-b1c2-1aae2ec946a4.png')
INSERT [dbo].[Employees] ([EmployeeID], [LastName], [FirstName], [Title], [BirthDate], [HireDate], [Address], [City], [HomePhone], [Notes], [UserID], [Photo]) VALUES (N'577378d4-bb82-495a-9c70-d0b1aa6de9d6', N'Phúc', N'Nguyễn', NULL, CAST(N'2000-04-04' AS Date), CAST(N'2024-05-01' AS Date), N'OIbanoi X', N'Hồ Chí Minh', N'0982359945', N'This is an awesome guy....', N'47fdb832-46bb-495c-8b3d-06bb817e29db', N'd2fa2968-a767-41ec-b989-b49b0155af5e.png')
GO
SET IDENTITY_INSERT [dbo].[Roles] ON 

INSERT [dbo].[Roles] ([RoleID], [RoleName]) VALUES (1, N'CUSTOMER')
INSERT [dbo].[Roles] ([RoleID], [RoleName]) VALUES (2, N'EMPLOYEE')
INSERT [dbo].[Roles] ([RoleID], [RoleName]) VALUES (3, N'ADMIN')
SET IDENTITY_INSERT [dbo].[Roles] OFF
GO
SET IDENTITY_INSERT [dbo].[Shippers] ON 

INSERT [dbo].[Shippers] ([ShipperID], [CompanyName], [Phone]) VALUES (1, N'Speedy Express', N'(503) 555-9831')
INSERT [dbo].[Shippers] ([ShipperID], [CompanyName], [Phone]) VALUES (2, N'United Package', N'(503) 555-3199')
INSERT [dbo].[Shippers] ([ShipperID], [CompanyName], [Phone]) VALUES (3, N'Federal Shipping', N'(503) 555-9931')
SET IDENTITY_INSERT [dbo].[Shippers] OFF
GO
INSERT [dbo].[UserRole] ([UserID], [RoleID]) VALUES (N'01efea5a-2668-497b-a756-44e70191fa1b', 2)
INSERT [dbo].[UserRole] ([UserID], [RoleID]) VALUES (N'3072e836-7469-454d-9165-e7761f3f2eb7', 1)
INSERT [dbo].[UserRole] ([UserID], [RoleID]) VALUES (N'47fdb832-46bb-495c-8b3d-06bb817e29db', 2)
INSERT [dbo].[UserRole] ([UserID], [RoleID]) VALUES (N'47fdb832-46bb-495c-8b3d-06bb817e29db', 3)
INSERT [dbo].[UserRole] ([UserID], [RoleID]) VALUES (N'704c1690-d96b-4258-bbea-061ac2261ccd', 1)
GO
INSERT [dbo].[Users] ([UserID], [Username], [Password], [Email], [EmailVerified], [Enabled]) VALUES (N'01efea5a-2668-497b-a756-44e70191fa1b', N'trongphuc123', N'$2a$10$Xeu34/NLqwySDUXLrG5Q4O6Hvb9VPgOj/4df2PFFejFQDo8D40prK', N'trongphuc123@example.com', 1, 1)
INSERT [dbo].[Users] ([UserID], [Username], [Password], [Email], [EmailVerified], [Enabled]) VALUES (N'3072e836-7469-454d-9165-e7761f3f2eb7', N'happy123', N'$2a$10$uQ/ebTNFyXjEUQiwlQp.WeVh/hCIGa28xSfqpS5oc617lHCxqOqKy', N'happy123@gmail.com', 1, 1)
INSERT [dbo].[Users] ([UserID], [Username], [Password], [Email], [EmailVerified], [Enabled]) VALUES (N'47fdb832-46bb-495c-8b3d-06bb817e29db', N'another2001', N'$2a$12$4QnH1RBnsz5xn/vv6zpO7efFq29ZI/jjxGWoxMqLGBp5ncy/9GH2.', N'another2001@example.com', 1, 1)
INSERT [dbo].[Users] ([UserID], [Username], [Password], [Email], [EmailVerified], [Enabled]) VALUES (N'704c1690-d96b-4258-bbea-061ac2261ccd', N'trongphuc22153', N'$2a$10$Ok9KSJxndxDFafv1OwIAbeZvoBVBXIjEzqZIzCV1EB1d6GtHmnqhC', N'trongphuc22153@gmail.com', 1, 1)
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [U_Users_Username]    Script Date: 6/29/2024 12:51:07 PM ******/
ALTER TABLE [dbo].[Users] ADD  CONSTRAINT [U_Users_Username] UNIQUE NONCLUSTERED 
(
	[Username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Customers]  WITH CHECK ADD  CONSTRAINT [FK_CusUserID] FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([UserID])
GO
ALTER TABLE [dbo].[Customers] CHECK CONSTRAINT [FK_CusUserID]
GO
ALTER TABLE [dbo].[Employees]  WITH CHECK ADD  CONSTRAINT [FK_EmpUserID] FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([UserID])
GO
ALTER TABLE [dbo].[Employees] CHECK CONSTRAINT [FK_EmpUserID]
GO
ALTER TABLE [dbo].[UserRole]  WITH CHECK ADD FOREIGN KEY([RoleID])
REFERENCES [dbo].[Roles] ([RoleID])
GO
ALTER TABLE [dbo].[UserRole]  WITH CHECK ADD FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([UserID])
GO
/****** Object:  StoredProcedure [dbo].[AddNewCustomer]    Script Date: 6/29/2024 12:51:07 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[AddNewCustomer] @userID nchar(36), @username nvarchar(20), @password nvarchar(max), @email varchar(320), @emailVerified bit, @enabled bit, @customerID nchar(36), @contactName nvarchar(30), @result bit output as
begin
	begin transaction
	begin try
		insert into Users(UserID, Username, Password, Email, EmailVerified, Enabled) 
		values(@userID, @username, @password, @email, @emailVerified, @enabled);

		exec AssignUserRole @username, 'CUSTOMER';

		insert into Customers(CustomerID, ContactName, UserID) 
		values(@customerID, @contactName, @userID)
		set @result=1;
		commit;
	end try
	begin catch
		rollback
		set @result=0;
	end catch;
end
GO
/****** Object:  StoredProcedure [dbo].[AddNewEmployee]    Script Date: 6/29/2024 12:51:07 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[AddNewEmployee] @userID nchar(36), @username nvarchar(20), @password nvarchar(max), @email varchar(320), @emailVerified bit, @enabled bit, @employeeID nchar(36), @firstName nvarchar(10), @lastName nvarchar(20), @result bit output as
begin
	begin transaction
	begin try
		insert into Users(UserID, Username, Password, Email, EmailVerified, Enabled) 
		values(@userID, @username, @password, @email, @emailVerified, @enabled);

		exec AssignUserRole @username, 'EMPLOYEE';

		insert into Employees(EmployeeID, FirstName, LastName, UserID) 
		values(@employeeID, @firstName, @lastName, @userID)
		set @result=1;
		commit;
	end try
	begin catch
		rollback
		set @result=0;
	end catch;
end
GO
/****** Object:  StoredProcedure [dbo].[AssignUserRole]    Script Date: 6/29/2024 12:51:07 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create procedure [dbo].[AssignUserRole] @username nvarchar(20), @roleName nvarchar(20) as
begin 

	if ((select count(1) from UserRoles where Username=@username and RoleName=@roleName)=0)
	begin
		begin transaction;
		begin try
			declare @roleID int;
			declare @userID nvarchar(36);
			set @roleID = (select RoleID from Roles where RoleName=@roleName);
			set @userID = (select UserID from Users where Username=@username);

			INSERT INTO UserRole(UserID, RoleID) 
            OUTPUT inserted.*
            VALUES (@userID, @roleID);

			commit;
		end try
		begin catch
			print 'error';
			rollback;
		end catch
	end;
	else begin 
		print 'UserRole has been existed';
	end;

end;
GO
/****** Object:  StoredProcedure [dbo].[AssignUserRoles]    Script Date: 6/29/2024 12:51:07 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create procedure [dbo].[AssignUserRoles] @username nvarchar(20), @listRoleID varchar(Max), @result bit output as
begin 
	begin transaction;
	begin try
		DECLARE @ListTable TABLE (RoleID INT);
		declare @userID nchar(36);

		select @userID=UserID from Users where Username=@username

		INSERT INTO @ListTable
		SELECT value FROM STRING_SPLIT(@listRoleID, ',')

		delete from UserRole where UserID=@userID;

		INSERT INTO UserRole(UserID, RoleID)
		SELECT @userID, RoleID FROM @ListTable
		
		set @result=1;
		commit;
	end try
	begin catch
		print 'error';
		rollback;
		set @result=0;
	end catch
end;
GO
/****** Object:  StoredProcedure [dbo].[CreateCustomerInfo]    Script Date: 6/29/2024 12:51:07 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create procedure [dbo].[CreateCustomerInfo] @customerID nchar(36), @contactName nvarchar(30), @username nvarchar(20) as
begin 
	declare @userID nchar(36);
	if exists (select UserID from Users where Username=@username)
		begin
			begin transaction;
			begin try
				set @userID = (select UserID from Users where Username=@username);
				insert into Customers(CustomerID, ContactName, UserID) values (@customerID, @contactName, @userID);
				commit;
			end try
			begin catch
				print 'Can not insert new Customer info';
				rollback;
			end catch
		end
	else begin print 'User does not existed'; end;
end
GO
/****** Object:  StoredProcedure [dbo].[CreateEmployeeInfo]    Script Date: 6/29/2024 12:51:07 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create procedure [dbo].[CreateEmployeeInfo] @employeeID nchar(36), @lastName nvarchar(20), @firstName nvarchar(10), @username nvarchar(20) as
begin 
	declare @userID nchar(36);
	if exists (select UserID from Users where Username=@username)
		begin
			begin transaction;
			begin try
				set @userID = (select UserID from Users where Username=@username);
				insert into Employees(EmployeeID, FirstName, LastName, UserID) values (@employeeID, @firstName, @lastName, @userID);
				commit;
			end try
			begin catch
				print 'Can not insert new Employee info';
				rollback;
			end catch
		end
	else begin print 'User does not existed'; end;
end
GO
/****** Object:  StoredProcedure [dbo].[DeleteUserRole]    Script Date: 6/29/2024 12:51:07 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[DeleteUserRole] @username nvarchar(20), @roleName nvarchar(20) as
begin 

	if ((select count(1) from UserRoles where Username=@username and RoleName=@roleName)>0)
	begin
		begin transaction;
		begin try
			declare @roleID int;
			declare @userID nvarchar(36);
			set @roleID = (select RoleID from Roles where RoleName=@roleName);
			set @userID = (select UserID from Users where Username=@username);

			DELETE FROM UserRole 
			OUTPUT deleted.*
			WHERE UserID = @userID AND RoleID = @roleID;

			commit;
		end try
		begin catch
			print 'error';
			rollback;
		end catch
	end;
	else begin 
		print 'UserRole does not existed';
	end;

end;
GO
/****** Object:  StoredProcedure [dbo].[UpdateAdminEmployeeInfo]    Script Date: 6/29/2024 12:51:07 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[UpdateAdminEmployeeInfo] @employeeID nchar(36), 
	@firstName nvarchar(10), @lastName nvarchar(20), @hireDate date, 
	@photo varchar(256), @notes ntext, @result bit OUTPUT as
begin 
	declare @userID nchar(36);
	select @userID=userID from Employees where EmployeeID=@employeeID;
	begin transaction;
	begin try
		update Employees 
			set FirstName=@firstName,
				LastName=@lastName,
				HireDate=@hireDate,
				Photo=@photo,
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
/****** Object:  StoredProcedure [dbo].[UpdateCustomerInfo]    Script Date: 6/29/2024 12:51:07 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[UpdateCustomerInfo] @customerID nchar(36), @email varchar(320), 
	@contactName nvarchar(30), @address nvarchar(200), @city nvarchar(50),
	@phone nvarchar(24), @picture varchar(256), @result bit OUTPUT as
begin 
	declare @userID nchar(36);
	select @userID=userID from Customers where CustomerID=@customerID;
	begin transaction;
	begin try
		update Users set email=@email where userID=@userID;
		update Customers 
			set ContactName=@contactName, 
				[Address]=@address, 
				City=@city,
				Phone=@phone,
				picture=@picture
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
/****** Object:  StoredProcedure [dbo].[UpdateEmployeeInfo]    Script Date: 6/29/2024 12:51:07 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create procedure [dbo].[UpdateEmployeeInfo] @employeeID nchar(36), @email varchar(320), 
	@firstName nvarchar(10), @lastName nvarchar(20), @birthDate date, 
	@address nvarchar(200), @city nvarchar(50), @homePhone nvarchar(24), 
	@photo varchar(256), @result bit OUTPUT as
begin 
	declare @userID nchar(36);
	select @userID=userID from Employees where EmployeeID=@employeeID;
	begin transaction;
	begin try
		update Users set email=@email where userID=@userID;
		update Employees 
			set FirstName=@firstName,
				LastName=@lastName,
				BirthDate=@birthDate,
				[Address]=@address, 
				City=@city,
				HomePhone=@homePhone,
				Photo=@photo
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
/****** Object:  StoredProcedure [dbo].[UpdateUserPassword]    Script Date: 6/29/2024 12:51:07 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create procedure [dbo].[UpdateUserPassword] @userID nchar(36), @password nvarchar(MAX), @result bit output as
begin
	begin transaction;
	begin try
		update Users set password=@password where userID=@userID;
		set @result = 1;
		commit;
	end try
	begin catch
		rollback;
		set @result = 0;
	end catch
end
GO
USE [master]
GO
ALTER DATABASE [user] SET  READ_WRITE 
GO

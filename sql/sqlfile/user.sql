USE [master]
GO
/****** Object:  Database [user]    Script Date: 10/25/2024 9:50:43 AM ******/
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
/****** Object:  Table [dbo].[Users]    Script Date: 10/25/2024 9:50:43 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[UserID] [nchar](36) NOT NULL,
	[Username] [nvarchar](20) NOT NULL,
	[Password] [nvarchar](max) NULL,
	[Email] [varchar](320) NULL,
	[Enabled] [bit] NOT NULL,
	[EmailVerified] [bit] NULL,
	[FirstName] [nvarchar](10) NULL,
	[LastName] [nvarchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[UserID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Roles]    Script Date: 10/25/2024 9:50:43 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Roles](
	[RoleID] [int] IDENTITY(1,1) NOT NULL,
	[Rolename] [nvarchar](20) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[RoleID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UserRole]    Script Date: 10/25/2024 9:50:43 AM ******/
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
/****** Object:  View [dbo].[UserRoles]    Script Date: 10/25/2024 9:50:43 AM ******/
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
SET IDENTITY_INSERT [dbo].[Roles] ON 

INSERT [dbo].[Roles] ([RoleID], [Rolename]) VALUES (1, N'CUSTOMER')
INSERT [dbo].[Roles] ([RoleID], [Rolename]) VALUES (2, N'EMPLOYEE')
INSERT [dbo].[Roles] ([RoleID], [Rolename]) VALUES (3, N'ADMIN')
SET IDENTITY_INSERT [dbo].[Roles] OFF
GO
INSERT [dbo].[UserRole] ([UserID], [RoleID]) VALUES (N'01efea5a-2668-497b-a756-44e70191fa1b', 2)
INSERT [dbo].[UserRole] ([UserID], [RoleID]) VALUES (N'3072e836-7469-454d-9165-e7761f3f2eb7', 1)
INSERT [dbo].[UserRole] ([UserID], [RoleID]) VALUES (N'47fdb832-46bb-495c-8b3d-06bb817e29db', 2)
INSERT [dbo].[UserRole] ([UserID], [RoleID]) VALUES (N'47fdb832-46bb-495c-8b3d-06bb817e29db', 3)
INSERT [dbo].[UserRole] ([UserID], [RoleID]) VALUES (N'704c1690-d96b-4258-bbea-061ac2261ccd', 1)
INSERT [dbo].[UserRole] ([UserID], [RoleID]) VALUES (N'c3e2a1e2-fbed-4899-b8bd-c5d833d841fb', 1)
GO
INSERT [dbo].[Users] ([UserID], [Username], [Password], [Email], [Enabled], [EmailVerified], [FirstName], [LastName]) VALUES (N'01efea5a-2668-497b-a756-44e70191fa1b', N'trongphuc123', N'$2a$10$Xeu34/NLqwySDUXLrG5Q4O6Hvb9VPgOj/4df2PFFejFQDo8D40prK', N'trongphuc123@example.com', 1, 1, N'Phuc', N'Nguyen')
INSERT [dbo].[Users] ([UserID], [Username], [Password], [Email], [Enabled], [EmailVerified], [FirstName], [LastName]) VALUES (N'3072e836-7469-454d-9165-e7761f3f2eb7', N'happy123', N'$2a$10$uQ/ebTNFyXjEUQiwlQp.WeVh/hCIGa28xSfqpS5oc617lHCxqOqKy', N'happy123@gmail.com', 1, 1, N'Phuc', N'Nguyen')
INSERT [dbo].[Users] ([UserID], [Username], [Password], [Email], [Enabled], [EmailVerified], [FirstName], [LastName]) VALUES (N'47fdb832-46bb-495c-8b3d-06bb817e29db', N'another2001', N'$2a$12$4QnH1RBnsz5xn/vv6zpO7efFq29ZI/jjxGWoxMqLGBp5ncy/9GH2.', N'another2001@example.com', 1, 1, N'Phuc', N'Nguyen')
INSERT [dbo].[Users] ([UserID], [Username], [Password], [Email], [Enabled], [EmailVerified], [FirstName], [LastName]) VALUES (N'704c1690-d96b-4258-bbea-061ac2261ccd', N'trongphuc22153', N'$2a$10$Ok9KSJxndxDFafv1OwIAbeZvoBVBXIjEzqZIzCV1EB1d6GtHmnqhC', N'trongphuc22153@gmail.com', 1, 1, N'Phuc', N'Nguyen')
INSERT [dbo].[Users] ([UserID], [Username], [Password], [Email], [Enabled], [EmailVerified], [FirstName], [LastName]) VALUES (N'c3e2a1e2-fbed-4899-b8bd-c5d833d841fb', N'phucxauxi929', N'$2a$10$MNqgkFkbciwU9xW.gzxbtO735U7mbX88UkrxsG7Hfrmf98UCvsvoW', N'phucxauxi929@gmail.com', 1, 1, N'Nguyễn', N'Phúc')
GO
ALTER TABLE [dbo].[UserRole]  WITH CHECK ADD FOREIGN KEY([RoleID])
REFERENCES [dbo].[Roles] ([RoleID])
GO
ALTER TABLE [dbo].[UserRole]  WITH CHECK ADD FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([UserID])
GO
/****** Object:  StoredProcedure [dbo].[AssignUserRole]    Script Date: 10/25/2024 9:50:43 AM ******/
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
/****** Object:  StoredProcedure [dbo].[AssignUserRoles]    Script Date: 10/25/2024 9:50:43 AM ******/
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
/****** Object:  StoredProcedure [dbo].[DeleteUserRole]    Script Date: 10/25/2024 9:50:43 AM ******/
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
/****** Object:  StoredProcedure [dbo].[UpdateUserPassword]    Script Date: 10/25/2024 9:50:43 AM ******/
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

USE [master]
GO
/****** Object:  Database [notification]    Script Date: 10/25/2024 9:44:10 AM ******/
CREATE DATABASE [notification]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'notification', FILENAME = N'/var/opt/mssql/data/notification.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'notification_log', FILENAME = N'/var/opt/mssql/data/notification_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [notification] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [notification].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [notification] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [notification] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [notification] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [notification] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [notification] SET ARITHABORT OFF 
GO
ALTER DATABASE [notification] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [notification] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [notification] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [notification] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [notification] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [notification] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [notification] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [notification] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [notification] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [notification] SET  DISABLE_BROKER 
GO
ALTER DATABASE [notification] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [notification] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [notification] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [notification] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [notification] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [notification] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [notification] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [notification] SET RECOVERY FULL 
GO
ALTER DATABASE [notification] SET  MULTI_USER 
GO
ALTER DATABASE [notification] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [notification] SET DB_CHAINING OFF 
GO
ALTER DATABASE [notification] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [notification] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [notification] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [notification] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'notification', N'ON'
GO
ALTER DATABASE [notification] SET QUERY_STORE = OFF
GO
USE [notification]
GO
/****** Object:  Table [dbo].[Notifications]    Script Date: 10/25/2024 9:44:10 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Notifications](
	[NotificationID] [nchar](36) NOT NULL,
	[Message] [nvarchar](max) NOT NULL,
	[SenderID] [nchar](36) NULL,
	[ReceiverID] [nchar](36) NULL,
	[TopicID] [int] NULL,
	[Status] [varchar](20) NOT NULL,
	[Time] [datetime] NOT NULL,
	[Title] [nvarchar](100) NOT NULL,
	[Picture] [varchar](256) NULL,
	[RepliedTo] [nchar](36) NULL,
PRIMARY KEY CLUSTERED 
(
	[NotificationID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Topics]    Script Date: 10/25/2024 9:44:10 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Topics](
	[TopicID] [int] IDENTITY(1,1) NOT NULL,
	[TopicName] [varchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[TopicID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NotificationUser]    Script Date: 10/25/2024 9:44:10 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NotificationUser](
	[NotificationID] [nchar](36) NOT NULL,
	[UserID] [nchar](36) NOT NULL,
	[IsRead] [bit] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[NotificationID] ASC,
	[UserID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[NotificationDetails]    Script Date: 10/25/2024 9:44:10 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE view [dbo].[NotificationDetails] as
SELECT n.NotificationID, n.Title, n.Message, n.SenderID, n.ReceiverID, 
		t.TopicName AS Topic, n.Status, ISNULL(nu.IsRead, 0) AS IsRead, 
		n.Time, n.Picture, n.RepliedTo
FROM     dbo.Notifications AS n INNER JOIN
        dbo.Topics AS t ON n.TopicID = t.TopicID LEFT OUTER JOIN
        dbo.NotificationUser AS nu ON nu.NotificationID = n.NotificationID
GO
SET IDENTITY_INSERT [dbo].[Topics] ON 

INSERT [dbo].[Topics] ([TopicID], [TopicName]) VALUES (1, N'Order')
INSERT [dbo].[Topics] ([TopicID], [TopicName]) VALUES (2, N'Account')
SET IDENTITY_INSERT [dbo].[Topics] OFF
GO
ALTER TABLE [dbo].[Notifications]  WITH CHECK ADD FOREIGN KEY([TopicID])
REFERENCES [dbo].[Topics] ([TopicID])
GO
ALTER TABLE [dbo].[Notifications]  WITH CHECK ADD  CONSTRAINT [RepliedTo_FK] FOREIGN KEY([RepliedTo])
REFERENCES [dbo].[Notifications] ([NotificationID])
GO
ALTER TABLE [dbo].[Notifications] CHECK CONSTRAINT [RepliedTo_FK]
GO
ALTER TABLE [dbo].[NotificationUser]  WITH CHECK ADD FOREIGN KEY([NotificationID])
REFERENCES [dbo].[Notifications] ([NotificationID])
GO
/****** Object:  StoredProcedure [dbo].[CreateNotification]    Script Date: 10/25/2024 9:44:10 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[CreateNotification] @notificationID nchar(36), @title nvarchar(100), 
	@messgae nvarchar(max), @picture varchar(256), @senderID nchar(36), @receiverID nchar(36), 
	@topicName varchar(20), @repliedTo nchar(36), @status varchar(20), @isRead bit, 
	@time datetime, @result bit output as
begin
	if exists(select TopicID from Topics where TopicName=@topicName)
		begin
		begin transaction
		begin try
				insert into Notifications(NotificationID, Title, Message, SenderID, ReceiverID, TopicID, RepliedTo, Status, Time, Picture)
				select @notificationID, @title, @messgae, @senderID, @receiverID, t.TopicID, @repliedTo, @status, @time, @picture
				from Topics t where t.TopicName=@topicName
				
				insert into NotificationUser(NotificationID, UserID, IsRead) 
				values(@notificationID, @receiverID, @isRead);
				
				set @result=1;
				commit;
		end try
		begin catch
			rollback
			set @result=0;
		end catch
		end;
	else 
	begin 
		set @result=0 
	end;
end;
GO
/****** Object:  StoredProcedure [dbo].[UpdateNotificationReadStatusByNotificationID]    Script Date: 10/25/2024 9:44:10 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[UpdateNotificationReadStatusByNotificationID] @notificationID nchar(36), @isRead bit, @result bit output as
begin
	begin transaction
	begin try
		if not exists (SELECT * FROM NotificationUser nu, Notifications n where n.NotificationID=@notificationID and n.ReceiverID=nu.UserID)
			begin
				insert into NotificationUser(NotificationID, UserID, IsRead) 
				select NotificationID, ReceiverID, @isRead
				from Notifications where NotificationID=@notificationID
			end

		update NotificationUser set IsRead=@isRead 
		where NotificationID=@notificationID;

		set @result=1;
		commit;
	end try
	begin catch
		rollback;
		set @result=0;
	end catch;
end;
GO
/****** Object:  StoredProcedure [dbo].[UpdateNotificationReadStatusByNotificationIDAndUserID]    Script Date: 10/25/2024 9:44:10 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[UpdateNotificationReadStatusByNotificationIDAndUserID] @notificationID nchar(36), @userID nchar(36), @isRead bit, @result bit output as
begin
	begin transaction
	begin try
		if exists (SELECT * FROM NotificationUser where NotificationID=@notificationID and UserID=@userID)
		begin
			update NotificationUser set IsRead=@isRead where NotificationID=@notificationID and UserID=@userID;
		end
		else begin
			insert into NotificationUser(NotificationID, UserID, IsRead) values(@notificationID, @userID, @isRead);
		end;
		set @result=1;
		commit;
	end try
	begin catch
		rollback;
		set @result=0;
	end catch;
end;
GO
/****** Object:  StoredProcedure [dbo].[UpdateNotificationReadStatusByUserID]    Script Date: 10/25/2024 9:44:10 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[UpdateNotificationReadStatusByUserID] @userID nchar(36), @isRead bit, @result bit output as
begin
	begin transaction;
	begin try
		update NotificationUser set IsRead=@isRead where UserID=@userID;
		set @result=1;
		commit;
	end try
	begin catch
		rollback;
		set @result=0;
	end catch;
end;
GO
/****** Object:  StoredProcedure [dbo].[UpdateNotificationsReadByNotificationID]    Script Date: 10/25/2024 9:44:10 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
Create procedure [dbo].[UpdateNotificationsReadByNotificationID] @notificationIDs varchar(max), @isRead bit, @result bit output as
begin
	begin transaction;
	begin try
		declare @ListIds table (NotificationID nchar(36))
		insert into @ListIds select value from string_split(@notificationIDs, ',');

		update NotificationUser set IsRead=@isRead 
		from NotificationUser nu join @ListIds li on nu.NotificationID=li.NotificationID

		set @result=1
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
ALTER DATABASE [notification] SET  READ_WRITE 
GO

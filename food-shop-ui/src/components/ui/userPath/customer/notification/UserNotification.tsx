import { useContext, useEffect, useState } from "react";
import { Notification, Pageable } from "../../../../../model/Type";
import { displayProductImage } from "../../../../../service/Image";
import { getPageNumber } from "../../../../../service/Pageable";
import PaginationSection from "../../../../shared/website/sections/paginationSection/PaginationSection";
import { getCustomerNotifications, markAsReadCustomerNotification 
} from "../../../../../api/NotificationApi";
import { Alert } from "../../../../../model/WebType";
import { ALERT_TIMEOUT, ALERT_TYPE, MARK_NOTIFICATION_TYPE } from "../../../../../constant/WebConstant";
import AlertComponent from "../../../../shared/functions/alert/Alert";
import notificationMessagesContext from "../../../../contexts/NotificationMessagesContext";
import { getCustomerUrlFromNotification } from "../../../../../service/Notification";

export default function UserNotificationComponent(){
    const [notifications, setNotifications] = useState<Notification[]>([])
    const notificationMessage = useContext<Notification|undefined>(notificationMessagesContext);
    const [pageable, setPageable] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    })
    const [alert, setAlert] = useState<Alert>({
        message: "",
        type: ALERT_TYPE.INFO,
        isShowed: false
    })
    useEffect(()=>{
        initial();
    }, [notificationMessage])

    const initial = ()=>{
        if(notificationMessage) setNotifications([...notifications, notificationMessage])
        const pageNumber = getPageNumber();
        fetchNotifications(pageNumber);
    }
    // fetch user's notifications
    const fetchNotifications = async (pageNumber: number)=>{
        const res = await getCustomerNotifications(pageNumber);
        if(200<=res.status&&res.status<300){
            const data = res.data;
            setNotifications(data.content);
            setPageable({
                first: data.first,
                last: data.last,
                number: data.number,
                totalPages: data.totalPages
            })
        }
    }

    // calculate time
    const subtractTime = (time: string) => {
        const currentTime = new Date();
        const notificationTime = new Date(time);
        let result: number = currentTime.getTime() - notificationTime.getTime(); // difference in milliseconds
    
        const seconds = result / 1000;
        const minutes = seconds / 60;
        const hours = minutes / 60;
        const days = hours / 24;
        const months = days / 30.44; // Average number of days in a month
        const years = days / 365.25; // Average number of days in a year, accounting for leap years
    
        if (years >= 1) {
            return `${Math.round(years)} year(s)`;
        } else if (months >= 1) {
            return `${Math.round(months)} month(s)`;
        } else if (days >= 1) {
            return `${Math.round(days)} day(s)`;
        } else if (hours >= 1) {
            return `${Math.round(hours)} hour(s)`;
        } else if (minutes >= 1) {
            return `${Math.round(minutes)} minute(s)`;
        } else {
            return `${Math.round(seconds)} second(s)`;
        }
    }

    // mark all as read
    const onClickMarkAllAsRead = ()=>{
        markAllAsRead();
    }

    const markAllAsRead = async ()=>{
        try {
            const res = await markAsReadCustomerNotification({}, MARK_NOTIFICATION_TYPE.ALL);
            if(200<=res.status && res.status<300){
                const data = res.data
                // console.log(data)
                const status = data.status;
                setAlert({
                    message: status?'All messages have been marked as read': 'All messages can not be marked as read',
                    type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                    isShowed: true
                })    
            }
        } catch (error) {
            setAlert({
                message: "All messages can not be marked as read",
                type: ALERT_TYPE.DANGER,
                isShowed: true
            }) 
        }finally{
            setTimeout(()=>{
                setAlert({...alert, isShowed: false});
            }, ALERT_TIMEOUT)
        } 
    }
    const onClickNotification = (_event: any, notification: Notification)=>{
        readCustomerNotification(notification.notificationID);
    }
    // mark as read for customer
    const readCustomerNotification = async (notificationID: string)=>{
        const data = {
            notificationID: notificationID
        }
        const res = await markAsReadCustomerNotification(data, MARK_NOTIFICATION_TYPE.NOTIFICATION)
        if(200<=res.status && res.status<300){
            // setTotalUnreadNotifications(value => value-1);
        }
    }

    return(
        <div className="container w-100">
            <AlertComponent alert={alert}/>
            <div className="notification-wrap">
                <div className="notification-panel px-3 me-md-5 ms-md-5 mb-3">
                    <div className="notify-header d-flex justify-content-end">
                        <button onClick={onClickMarkAllAsRead}>Mark as read</button>
                    </div>
                    <div className="notify-body">
                        {notifications.map((notification, index)=>(
                            <a href={getCustomerUrlFromNotification(notification)} key={index}>
                                <div className={`notify-item cursor-pointer px-3 ${notification.isRead?'read':''}`} 
                                    onClick={(e)=> onClickNotification(e, notification)}>
                                    <div className="notify-img">
                                        <img src={displayProductImage(null)} alt="profile-pic"/>
                                    </div>
                                    <div className={`notify_info`}>
                                        <p className={`text-black ${notification.isRead?'opacity-50':''}`}>{notification.message}</p>
                                        <span className="notify-time">{subtractTime(notification.time)} ago</span>
                                    </div>
                                </div>
                            </a>
                        ))}
                    </div>
                    <div className="notification-footer mt-3">
                        <PaginationSection pageable={pageable}/>
                    </div>
                </div>
            </div>
        </div>
    );
}
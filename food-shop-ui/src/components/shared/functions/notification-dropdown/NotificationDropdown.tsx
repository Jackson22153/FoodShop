import { faBell } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { displayProductImage } from "../../../../service/Image";
import { useContext, useEffect, useRef, useState } from "react";
import { Notification } from "../../../../model/Type";
import { getCustomerNotifications, getCustomerSummaryNotifications, 
    getEmployeeNotifications, getEmployeeSummaryNotifications, 
    markAsReadCustomerNotification, markAsReadEmployeeNotification 
} from "../../../../api/NotificationApi";
import { MARK_NOTIFICATION_TYPE, ORDER_NOTIFICATION, ROLE } from "../../../../constant/WebConstant";
import { CUSTOMER_NOTIFICATION, EMPLOYEE_NOTIFICATION } from "../../../../constant/FoodShoppingURL";
import { getPageNumber } from "../../../../service/Pageable";
import notificationMessagesContext from "../../../contexts/NotificationMessagesContext";
import { getUrlFromNotification } from "../../../../service/Notification";
import { Link } from "react-router-dom";

interface Props{
    roles: string[]
}
export default function NotificationDropdown(prop:Props){
    const [IsNotificationDropdownEnabled, setIsNotificationDropdownEnabled] = useState(false);
    const [notifications, setNotifications] = useState<Notification[]>([])
    const notificationMessage = useContext<Notification|undefined>(notificationMessagesContext)
    const [totalUnreadNotifications, setTotalUnreadNotifications] = useState(0);
    const notificationRef = useRef<HTMLDivElement>(null);

    useEffect(()=>{
        initial();
    }, [notificationMessage])

    const initial = ()=>{
        // add new notification message
        if(notificationMessage) {
            setNotifications([...notifications, notificationMessage])
        }
        // get user's notifications
        const pageNumber = getPageNumber();
        getUserNotifications(pageNumber);
        // check number of unread notifications 
        checkNumberOfNotifications();
        // add default event listener for notification dropdown
        document.addEventListener("click", onClickOutsideNotificationDropdown);
    }

    // toggle notification dropdown
    const onClickShowNotificationDropdown = ()=>{
        // toggle notification icon
        setIsNotificationDropdownEnabled(status => !status);
    }
    // close notification dropdown
    const onClickCloseNotificationDropdown = ()=>{
        setIsNotificationDropdownEnabled(false);
    }
    // click outside notification dropdown
    const onClickOutsideNotificationDropdown = (event: any)=>{
        if(notificationRef.current && !notificationRef.current.contains(event.target as Node)){
            onClickCloseNotificationDropdown();
        }
    }

    // onclickNotification
    // mark notification as read
    const onClickNotification = (_event: any, notification: Notification)=>{
        if(prop.roles.includes(ROLE.EMPLOYEE.toLowerCase()) && !notification.isRead){
            if(!(notification.title.toLowerCase()==ORDER_NOTIFICATION.PLACE_ORDER.toLowerCase())){
                // employee click 
                readEmployeeNotification(notification.notificationID);
            }
        }else if(prop.roles.includes(ROLE.CUSTOMER.toLowerCase()) && !notification.isRead){
            // customer click
            readCustomerNotification(notification.notificationID);
        }
    }
    // mark as read for customer
    const readCustomerNotification = async (notificationID: string)=>{
        const data = {
            notificationID: notificationID
        }
        const res = await markAsReadCustomerNotification(data, MARK_NOTIFICATION_TYPE.NOTIFICATION)
        if(200<=res.status && res.status<300){
            setTotalUnreadNotifications(value => value-1);
        }
    }
    // mark as read for employee
    const readEmployeeNotification = async (notificationID: string)=>{
        const data = {
            notificationID: notificationID
        }
        const res = await markAsReadEmployeeNotification(data, MARK_NOTIFICATION_TYPE.NOTIFICATION)
        if(200<=res.status && res.status<300){
            setTotalUnreadNotifications(value => value-1);
        }
    }

    // get notifications
    const getUserNotifications = (pageNumber: number)=>{
        if(prop.roles.includes(ROLE.EMPLOYEE.toLowerCase())){
            fetchEmployeeNotifications(pageNumber);
        }else if(prop.roles.includes(ROLE.CUSTOMER.toLowerCase())){
            fetchCustomerNotifications(pageNumber)
        }
    }
    const fetchCustomerNotifications = async (pageNumber: number)=>{
        const res = await getCustomerNotifications(pageNumber);
        if(200<=res.status&&res.status<300){
          const data = res.data;
          setNotifications(data.content);
        }
    }
    const fetchEmployeeNotifications = async (pageNumber: number)=>{
        const res = await getEmployeeNotifications(pageNumber);
        if(200<=res.status&&res.status<300){
          const data = res.data;
        //   console.log(data)
          setNotifications(data.content);
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
    
    // count number of notifications
    const checkNumberOfNotifications = ()=>{
        if(prop.roles.includes(ROLE.CUSTOMER.toLowerCase())){
            fetchCustomerSummaryNotifications();
        }else if(prop.roles.includes(ROLE.EMPLOYEE.toLowerCase())){
            fetchEmployeeSummaryNotifications();
        }
    }

    const fetchCustomerSummaryNotifications = async ()=>{
        const res = await getCustomerSummaryNotifications();
        if(200<=res.status&&res.status<300){
            const data = res.data;
            setTotalUnreadNotifications(data.totalOfUnreadNotifications)
        }
    }
    const fetchEmployeeSummaryNotifications = async ()=>{
        const res = await getEmployeeSummaryNotifications();
        if(200<=res.status&&res.status<300){
            const data = res.data;
            setTotalUnreadNotifications(data.totalOfUnreadNotifications)
        }
    }
    // get notification url based on role
    const getNotificationUrl = ()=>{
        if(prop.roles.includes(ROLE.CUSTOMER.toLowerCase())){
            return CUSTOMER_NOTIFICATION;
        }else if(prop.roles.includes(ROLE.EMPLOYEE.toLowerCase())){
            return EMPLOYEE_NOTIFICATION;
        }
    }

    return(
        <div className="wrapper" ref={notificationRef}>
            <div className="notification-wrap">
                <div className={`notification-icon ${IsNotificationDropdownEnabled?'active':''}`} 
                    onClick={onClickShowNotificationDropdown}>
                    <span className="nav-link cursor-pointer">
                        <FontAwesomeIcon icon={faBell}/>
                        {totalUnreadNotifications>0 &&
                            <span className="position-absolute top-0 badge rounded-pill badge-notification bg-danger user-select-none">
                                {totalUnreadNotifications}
                            </span>
                        }
                    </span>
                </div>
                <div className={`dropdown position-absolute z-3 ${IsNotificationDropdownEnabled?'active':''}`}>
                    <div className="notify-body">
                        {notifications.map((notification, index)=>(
                            <Link to={getUrlFromNotification(notification, prop.roles)} key={index}>
                                <div className={`notify-item cursor-pointer ${notification.isRead?'read':''}`} 
                                    onClick={(e)=> onClickNotification(e, notification)}>
                                    <div className="notify-img">
                                        <img src={displayProductImage(notification.picture)} alt="profile-pic"/>
                                    </div>
                                    <div className="notify_info">
                                        <p className="text-black">{notification.message}</p>
                                        <span className="notify-time">{subtractTime(notification.time)} ago</span>
                                    </div>
                                </div>   
                            </Link>
                        ))}
                    </div>
                    <div className="notify-footer">
                        <Link to={getNotificationUrl()}>
                            <div className="btn btn-success btn-block w-100">View All</div>
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    )
}
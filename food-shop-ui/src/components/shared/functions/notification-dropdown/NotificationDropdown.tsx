import { faBell } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { displayProductImage } from "../../../../service/image";
import { useContext, useEffect, useRef, useState } from "react";
import { NotificationContext } from "../../../../model/WebType";
import notificationMessagesContext from "../../../contexts/NotificationMessagesContext";
import { Notification } from "../../../../model/Type";
import { getCustomerSummaryNotifications, getEmployeeSummaryNotifications, markAsReadCustomerNotification, markAsReadEmployeeNotification } from "../../../../api/NotificationApi";
import { ROLE } from "../../../../constant/config";

interface Props{
    roles: string[]
}
export default function NotificationDropdown(prop:Props){
    const [IsNotificationDropdownEnabled, setIsNotificationDropdownEnabled] = useState(false);
    const {notifications: notifications} = useContext<NotificationContext>(notificationMessagesContext);
    const [totalUnreadNotifications, setTotalUnreadNotifications] = useState(0);
    const notificationRef = useRef<HTMLDivElement>(null);

    useEffect(()=>{
        checkNumberOfNotifications();
        document.addEventListener("click", onClickOutsideNotificationDropdown);
    }, [])

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
    const onClickNotification = (_event: any, notification: Notification)=>{
        if(prop.roles.includes(ROLE.EMPLOYEE.toLowerCase()) && notification.active){
            // employee click 
            if(notification.active){
                readEmployeeNotification(notification.notificationID);
            }
        }else if(prop.roles.includes(ROLE.CUSTOMER.toLowerCase()) && notification.active){
            // customer click
            if(notification.active){
                readCustomerNotification(notification.notificationID);
            }
        }
    }

    
    const readCustomerNotification = async (notificationID: string)=>{
        const data = {
            notificationID: notificationID
        }
        const res = await markAsReadCustomerNotification(data)
        if(200<=res.status && res.status<300){
            setTotalUnreadNotifications(value => value-1);
        }
    }
    const readEmployeeNotification = async (notificationID: string)=>{
        const data = {
            notificationID: notificationID
        }
        const res = await markAsReadEmployeeNotification(data)
        if(200<=res.status && res.status<300){
            setTotalUnreadNotifications(value => value-1);
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

    return(
        <div className="wrapper" ref={notificationRef}>
            <div className="notification-wrap">
                <div className="notification-icon" onClick={onClickShowNotificationDropdown}>
                    <span className="nav-link cursor-pointer">
                        <FontAwesomeIcon icon={faBell}/>
                        {totalUnreadNotifications>0 &&
                            <span className="position-absolute top-0 badge rounded-pill badge-notification bg-danger">
                                {totalUnreadNotifications}
                            </span>
                        }
                    </span>
                </div>
                <div className={`dropdown ${IsNotificationDropdownEnabled?'active':''}`}>
                    <div className="notify-body">
                        {notifications.map((notification)=>(
                            <div className={`notify-item cursor-pointer ${!notification.active?'read':''}`} 
                                key={notification.notificationID} onClick={(e)=> onClickNotification(e, notification)}>
                                <div className="notify-img">
                                    <img src={displayProductImage(null)} alt="profile-pic" style={{width: "50px"}}/>
                                </div>
                                <div className="notify_info">
                                    <p className="text-black">{notification.message}</p>
                                    <span className="notify-time">{subtractTime(notification.time)} ago</span>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="notify-footer">
                        <a href="#!" className="btn btn-success btn-block w-100">View All</a>
                    </div>
                </div>
            </div>
        </div>
    )
}
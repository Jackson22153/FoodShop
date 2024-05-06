import { faBell } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { UserInfo } from "../../../../model/Type";
import { useContext, useEffect, useRef, useState } from "react";
import { ROLE } from "../../../../constant/config";
import { adminPath, customerPath, employeePath } from "../../../../constant/FoodShoppingURL";
import { Notification } from "../../../../model/Type";
import notificationMessagesContext from "../../../contexts/NotificationMessagesContext";
import { turnOffCustomerNotification } from "../../../../api/UserApi";
import { turnOffEmployeeNotification } from "../../../../api/EmployeeApi";
import { NotificationContext } from "../../../../model/WebType";

interface Props{
    userInfo: UserInfo,
    onClickLogout: any
}
export default function UserInfoNav(prop: Props){
    const userInfo = prop.userInfo;
    const [isExpandedUserDropdown, setIsExpandedUserDropdown] = useState(false);
    const [isExpandedNotificationDropdown, setIsExpandedNotificationDropdown] = useState(false);
    const roles = userInfo.roles;
    const {notifications: notificationMessages} 
        = useContext<NotificationContext>(notificationMessagesContext);
    const notificationRef = useRef<HTMLLIElement>(null);

    useEffect(()=>{
        document.addEventListener("click", onClickOutsideNotificationDropdown)
    }, [])

    const onClickLogout = ()=>{
        prop.onClickLogout();
    }
    // expand user userinfo
    const toggleUserExpanded = (status: boolean)=>{
        setIsExpandedUserDropdown(status)
    }
    const onClickShowUserDropdown = ()=>{
        toggleUserExpanded(true);
    }
    const onClickCloseUserDropdown = ()=>{
        toggleUserExpanded(false);
    }

    // expand user userinfo
    const toggleNotificationExpanded = (status: boolean)=>{
        setIsExpandedNotificationDropdown(status)
    }
    const onClickShowNotificationDropdown = ()=>{
        setIsExpandedNotificationDropdown(isShowed => !isShowed)
    }
    const onClickCloseNotificationDropdown = ()=>{
        toggleNotificationExpanded(false);
    }
    const onClickOutsideNotificationDropdown = (event: any)=>{
        if(notificationRef.current && !notificationRef.current.contains(event.target as Node)){
            onClickCloseNotificationDropdown();
        }
    }

    // USER ROLES
    const roleNames = ()=>{
        const arr = roles.map(role => role.roleName.toLowerCase());
        return arr;
    }

    // onclickNotification
    const onClickNotification = (event: any,notification: Notification)=>{
        if(roleNames().includes(ROLE.EMPLOYEE.toLowerCase()) && notification.active){
            fetchTurnOffEmployeeNotification(notification.notificationID);
            const li = event.target.closest("li")
            li.classList.add("text-black-50");
        }else if(roleNames().includes(ROLE.CUSTOMER.toLowerCase()) && notification.active){
            fetchTurnOffCustomerNotification(notification.notificationID);
            const li = event.target.closest("li")
            li.classList.add("text-black-50");
        }
    }

    const fetchTurnOffCustomerNotification = async (notificationID: string)=>{
        const data = {
            notificationID: notificationID
        }
        const res = await turnOffCustomerNotification(data)
        if(res.status){}
    }
    const fetchTurnOffEmployeeNotification = async (notificationID: string)=>{
        const data = {
            notificationID: notificationID
        }
        const res = await turnOffEmployeeNotification(data)
        if(res.status){}
    }
    // count number of notifications
    const checkNumberOfNotifications = ()=>{
        if(notificationMessages.length>0){
            const numberNotifications = notificationMessages.filter((notification:Notification) => notification.active)
            return numberNotifications.length;
        }
        return 0;
    }

    if(notificationMessages.length<=0) return "...Loading"
    else
    return(
        <div className="navbar-expand navbar-light text-white">
            <div id="navbarNavDropdown">
                <ul className="navbar-nav ml-auto">
                    {notificationMessages &&
                        <li className="nav-item dropdown pe-4" ref={notificationRef}>
                            <span className="nav-link cursor-pointer" onClick={onClickShowNotificationDropdown}>
                                <FontAwesomeIcon icon={faBell}/>
                                {checkNumberOfNotifications()>0 &&
                                    <span className="position-absolute top-0 badge rounded-pill badge-notification bg-danger">
                                        {checkNumberOfNotifications()}
                                    </span>
                                }
                            </span>
                            <ul className={`dropdown-menu translate-middle-x cursor-default ${isExpandedNotificationDropdown?'show': ''} user-notification-dropdown`}>
                                {notificationMessages.length>0 ?
                                    notificationMessages.map((notification: Notification, index: number) =>(
                                        <li className={`dropdown-item d-flex border-bottom ${!notification.active?'text-black-50':''}`} key={index}
                                            onClick={(e)=>onClickNotification(e, notification)}>
                                            {/* <div className="col-2">
                                                <img width={"40px"} src={displayUserImage(notification.picture)} alt={notification.username}/>
                                            </div> */}
                                            <div className="col-12 ps-1">
                                                <div className="h6">{notification.title}</div>
                                                <div className="text display-linebreak">
                                                    {notification.message}
                                                </div> 
                                            </div>
                                        </li>
                                    )):
                                    <li className="dropdown-item d-flex border-bottom">
                                        <div className="col-12 ps-1">
                                            <div className="h6"></div>
                                            <div className="text display-linebreak">
                                                No notification
                                            </div> 
                                        </div>
                                    </li>
                                }
                            </ul>
                        </li>
                    }
                    <li className="nav-item dropdown" onMouseEnter={onClickShowUserDropdown} onMouseLeave={onClickCloseUserDropdown}>
                        <span className="nav-link dropdown-toggle" id="navbarDropdownMenuLink" role="button" 
                            data-toggle="dropdown" aria-haspopup='true' aria-expanded={isExpandedUserDropdown} >
                            {userInfo.user.username}
                        </span>
                        <div id="appheader-user" className={`dropdown-menu ${isExpandedUserDropdown?'show':''}`} 
                            aria-labelledby="navbarDropdownMenuLink">
                            {roleNames().includes(ROLE.CUSTOMER.toLowerCase())&&
                                <a className="dropdown-item cursor-pointer" href={customerPath}>Profile</a>
                            }
                            {roleNames().includes(ROLE.EMPLOYEE.toLowerCase())&&
                                <a className="dropdown-item cursor-pointer" href={employeePath}>Profile</a>
                            }
                            {roleNames().includes(ROLE.ADMIN.toLowerCase())&&     
                                <a className="dropdown-item cursor-pointer" href={adminPath}>Admin Dashboard</a>
                            }
                            {/* <a className="dropdown-item cursor-pointer" href="#">Settings</a> */}
                            <div className="dropdown-divider"></div>
                            <span className="dropdown-item cursor-pointer" onClick={onClickLogout}>Logout</span>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    )
}
import axios from "axios";
import { CustomerNotificationsUrl, CustomerSummaryNotificationUrl, EmployeeNotificationsUrl, 
    EmployeeSummaryNotificationUrl, MarkAsReadCustomerNotificationUrl, MarkAsReadEmployeeNotificationUrl 
} from "../constant/FoodShoppingApiURL";

// customer
// mark a specific notification as read
export async function markAsReadCustomerNotification(notification, markType){
    return axios.post(`${MarkAsReadCustomerNotificationUrl}?type=${markType}`, JSON.stringify(notification), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
// get customer's notifications
export async function getCustomerNotifications(pageNumber){
    return axios.get(`${CustomerNotificationsUrl}?page=${pageNumber}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
// get summary of notifications
export async function getCustomerSummaryNotifications(){
    return axios.get(`${CustomerSummaryNotificationUrl}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}

// employee
// mark as read
export async function markAsReadEmployeeNotification(notification, markType){
    return axios.post(`${MarkAsReadEmployeeNotificationUrl}?type=${markType}`, JSON.stringify(notification), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
// get employee's notifications
export async function getEmployeeNotifications(pageNumber){
    return axios.get(`${EmployeeNotificationsUrl}?page=${pageNumber}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
// get summary of notifications
export async function getEmployeeSummaryNotifications(){
    return axios.get(`${EmployeeSummaryNotificationUrl}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
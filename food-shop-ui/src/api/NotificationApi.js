import axios from "axios";
import { CustomerNotificationsUrl, CustomerSummaryNotificationUrl, EmployeeNotificationsUrl, 
    EmployeeSummaryNotificationUrl, MarkAllAsReadCustomerNotificationUrl, 
    MarkAllAsReadEmployeeNotificationUrl, MarkAsReadCustomerNotificationUrl, 
    MarkAsReadEmployeeNotificationUrl } 
    from "../constant/FoodShoppingApiURL";

// customer
// mark a specific notification as read
export async function markAsReadCustomerNotification(notification){
    return axios.post(`${MarkAsReadCustomerNotificationUrl}`, JSON.stringify(notification), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
// mark all as read
export async function markAllAsReadCustomerNotifications(){
    return axios.post(`${MarkAllAsReadCustomerNotificationUrl}`, null, {
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
export async function markAsReadEmployeeNotification(notification){
    return axios.post(`${MarkAsReadEmployeeNotificationUrl}`, JSON.stringify(notification), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
// mark all as read
export async function markAllAsReadEmployeeNotifications(){
    return axios.post(`${MarkAllAsReadEmployeeNotificationUrl}`, null, {
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
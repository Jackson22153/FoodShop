import axios from "axios";
import { CustomerNotificationSummaryUrl, EmployeeNotificationSummaryUrl, MarkAsReadCustomerNotificationUrl, MarkAsReadEmployeeNotificationUrl, UserNotificationUrl } from "../constant/FoodShoppingApiURL";

export async function getUserNotifications(pageNumber){
    return axios.get(`${UserNotificationUrl}?page=${pageNumber}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}

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
// get summary of notifications
export async function getCustomerSummaryNotifications(){
    return axios.get(`${CustomerNotificationSummaryUrl}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}

// employee
export async function markAsReadEmployeeNotification(notification){
    return axios.post(`${MarkAsReadEmployeeNotificationUrl}`, JSON.stringify(notification), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
// get summary of notifications
export async function getEmployeeSummaryNotifications(){
    return axios.get(`${EmployeeNotificationSummaryUrl}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
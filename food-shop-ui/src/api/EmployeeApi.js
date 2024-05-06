
import axios from "axios";
import { EmployeeInfoUrl, EmployeeNotificationsUrl, EmployeeOrdersUrl, EmployeePendingOrdersUrl, EmployeeUrl } from "../constant/FoodShoppingApiURL";

export async function getEmployeeInfo(){
    return axios.get(EmployeeInfoUrl, {
        withCredentials: true,
        headers:{
            'Content-Type': 'application/json'
        }
    })
}
export function updateEmployeeInfo(employeeInfo){
    return axios.post(EmployeeInfoUrl, JSON.stringify(employeeInfo), {
        withCredentials: true,
        headers:{
            'Content-Type': 'application/json'
        }
    })
}
// get orders
export function getOrders(pageNumber, type){
    return axios.get(`${EmployeeOrdersUrl}?page=${pageNumber}&type=${type}`, {
        withCredentials: true,
        headers:{
            'Content-Type': 'application/json'
        }
    });
}
// get order detail
export function getOrderDetail(orderID){
    return axios.get(`${EmployeeOrdersUrl}/${orderID}`, {
        withCredentials: true,
        headers:{
            'Content-Type': 'application/json'
        }
    });
}
// get all pending order
export function getPendingOrders(pageNumber){
    return axios.get(`${EmployeePendingOrdersUrl}?page=${pageNumber}`, {
        withCredentials: true,
        headers:{
            'Content-Type': 'application/json'
        }
    });
}
export function getPendingOrder(orderID){
    return axios.get(`${EmployeePendingOrdersUrl}/${orderID}`, {
        withCredentials: true,
        headers:{
            'Content-Type': 'application/json'
        }
    });
}
// notification
export async function getEmployeeNotifications(pageNumber){
    return axios.get(`${EmployeeNotificationsUrl}?page=${pageNumber}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
export async function turnOffEmployeeNotification(data){
    return axios.post(`${EmployeeNotificationsUrl}`, JSON.stringify(data), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
import axios from "axios";
import { CustomerInfoUrl, CustomerOrdersUrl, CustomerNotificationsUrl, IsCustomerUrl } from "../constant/FoodShoppingApiURL";

export function isCustomer(){
    return axios.get(IsCustomerUrl,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export async function updateUserInfo(userInfo){
    return axios.post(CustomerInfoUrl, JSON.stringify(userInfo), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}

export async function getCustomerOrders(pageNumber, type){
    return axios.get(`${CustomerOrdersUrl}?page=${pageNumber}&type=${type}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}

export async function getCustomerInvoice(orderID){
    return axios.get(`${CustomerOrdersUrl}/${orderID}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}

export async function getCustomerInfo(){
    return axios.get(CustomerInfoUrl, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
// notifications
export async function getCustomerNotifications(pageNumber){
    return axios.get(`${CustomerNotificationsUrl}?page=${pageNumber}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
export async function turnOffCustomerNotification(data){
    return axios.post(`${CustomerNotificationsUrl}`, JSON.stringify(data), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
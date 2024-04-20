import axios from "axios";
import { CustomerInfoUrl, CustomerOrdersUrl } from "../constant/FoodShoppingApiURL";

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
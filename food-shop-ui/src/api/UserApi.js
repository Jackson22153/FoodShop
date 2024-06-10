import axios from "axios";
import { CustomerInfoUrl, CustomerOrdersUrl, CustomerNotificationsUrl, IsCustomerUrl, UploadUserImageUrl } from "../constant/FoodShoppingApiURL";

export function isCustomer(){
    return axios.get(IsCustomerUrl,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// get user info
export async function updateUserInfo(userInfo){
    return axios.post(CustomerInfoUrl, JSON.stringify(userInfo), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}

// upload user's image
export function uploadUserImage(file){
    const formData = new FormData();
    formData.append('file', file);
    
    return axios.post(UploadUserImageUrl, formData, {
        withCredentials: true,
        headers:{
            "Content-Type": "multipart/form-data",
        }
    });
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
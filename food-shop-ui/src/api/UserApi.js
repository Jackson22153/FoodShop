import axios from "axios";
import { CustomerInfoUrl, CustomerNotificationsUrl, GenerateOTPPhoneUrl, IsCustomerUrl, 
    UploadCustomerImageUrl, UploadEmployeeImageUrl, 
    VerifyOTPPhoneUrl
} from "../constant/FoodShoppingApiURL";

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
    
    return axios.post(UploadCustomerImageUrl, formData, {
        withCredentials: true,
        headers:{
            "Content-Type": "multipart/form-data",
        }
    });
}

// upload employee's image
export function uploadEmployeeImage(file){
    const formData = new FormData();
    formData.append('file', file);
    
    return axios.post(UploadEmployeeImageUrl, formData, {
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


// generate phone otp
export function generateOTPPhoneForUser(phone){
    return axios.post(`${GenerateOTPPhoneUrl}?phone=${phone}`, "", {
        withCredentials: true,
        headers: {
            "Content-Type": 'application/json',
        }
    })
}

// verify phone otp
export function verifyOTPPhoneForUser(otp, phone){
    return axios.post(`${VerifyOTPPhoneUrl}?otp=${otp}&phone=${phone}`, "", {
        withCredentials: true,
        headers: {
            "Content-Type": 'application/json',
        }
    })
}
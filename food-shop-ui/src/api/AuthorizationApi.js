import axios from "axios";
import { CustomerInfoUrl, IsAuthenticatedUrl, LoginUrl, LogoutUrl, UserInfoUrl, UsernameUrl } from "../constant/FoodShoppingApiURL";

export async function logout(){
    return axios.post(LogoutUrl, null,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}

export async function isAuthenticated(){
    return axios.get(UserInfoUrl, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
    // return axios.post(IsAuthenticatedUrl, null,{
    //     withCredentials: true,
    //     headers:{
    //         "Content-Type": 'application/json',
    //     }
    // })
}
export async function getUsername(){
    return axios.get(UsernameUrl, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}

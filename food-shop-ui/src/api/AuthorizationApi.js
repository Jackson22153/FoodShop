import axios from "axios";
import { LogoutUrl, UserInfoUrl } from "../constant/FoodShoppingApiURL";

// logout
export async function logout(){
    return axios.post(LogoutUrl, null,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
// get userinfo
export async function isAuthenticated(){
    return axios.get(UserInfoUrl, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}

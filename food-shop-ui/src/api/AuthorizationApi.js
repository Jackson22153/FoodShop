import axios from "axios";
import { ForgotUrl, LoginUrl, LogoutUrl, RegisterCustomerUrl, ResetPasswordUrl, UserInfoUrl, VerifyResetPasswordUrl } from "../constant/FoodShoppingApiURL";

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
// register
export function registerCustomer(data){
    return axios.post(RegisterCustomerUrl, JSON.stringify(data), {
        withCredentials: true,
        headers: {
            "Content-Type": 'application/json',
        }
    })
}
// login
export function login(username, password){
    const data = btoa(`${username}:${password}`)
    return axios.get(LoginUrl, {
        withCredentials: true,
        headers: {
            "Content-Type": 'application/json',
            Authorization: `Basic ${data}`
        }
    })
}
// forgot password
export function forgotPassword(email){
    return axios.post(`${ForgotUrl}?email=${email}`, "", {
        withCredentials: true,
        headers: {
            "Content-Type": "application/json"
        }
    })
}

// verify reset token
export function verifyResetToken(token){
    return axios.get(`${VerifyResetPasswordUrl}?token=${token}`, {
        withCredentials: true,
        headers: {
            "Content-Type": "application/json"
        }
    })
}

// reset password
export function resetPassword(data){
    return axios.post(ResetPasswordUrl, JSON.stringify(data), {
        withCredentials: true,
        headers: {
            "Content-Type": "application/json"
        }
    })
}


import axios from "axios";
import { EmployeeInfoUrl, IsEmployeeUrl} 
    from "../constant/FoodShoppingApiURL";

export function isEmployee(){
    return axios.get(IsEmployeeUrl,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export async function getEmployeeInfo(){
    return axios.get(EmployeeInfoUrl, {
        withCredentials: true,
        headers:{
            'Content-Type': 'application/json',
        }
    })
}
export function updateEmployeeInfo(employeeInfo){
    return axios.post(EmployeeInfoUrl, JSON.stringify(employeeInfo), {
        withCredentials: true,
        headers:{
            'Content-Type': 'application/json',
        }
    })
}



import axios from "axios";
import { EmployeeInfoUrl, EmployeePendingOrdersUrl, EmployeeUrl } from "../constant/FoodShoppingApiURL";

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
export function getPendingOrders(pageNumber){
    return axios.get(EmployeePendingOrdersUrl, {
        withCredentials: true,
        headers:{
            'Content-Type': 'application/json'
        }
    });
}
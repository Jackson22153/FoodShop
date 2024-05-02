import axios from "axios";
import { CategoryAdminUrl, CustomersAdminUrl, DiscountAdminUrl, DiscountTypeAdminUrl, DiscountsByProductAdminUrl, EmployeesAdminUrl, ProductAdminUrl, ResetPasswordAdminUrl, RolesAdminUrl, RolesEmployeeAdminUrl, UsersAdminUrl, UsersRolesAdminUrl } from "../constant/FoodShoppingApiURL";


export function updateCategory(category){
    return axios.post(CategoryAdminUrl, JSON.stringify(category), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}

// product
export function updateProduct(productDetail){
    const data = JSON.stringify(productDetail);
    return axios.post(ProductAdminUrl, data,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}

export function getProductDetail(productID){
    return axios.get(`${ProductAdminUrl}/${productID}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// customer
export function getCustomers(page){
    return axios.get(`${CustomersAdminUrl}?page=${page}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function getCustomersBySearchParam(page, searchParam, searchValue){
    return axios.get(`${CustomersAdminUrl}?page=${page}&${searchParam}=${searchValue}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function getCustomer(customerID){
    return axios.get(`${CustomersAdminUrl}/${customerID}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// employee
export function getEmployees(page){
    return axios.get(`${EmployeesAdminUrl}?page=${page}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function getEmployee(employeeID){
    return axios.get(`${EmployeesAdminUrl}/${employeeID}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function getEmployeesBySearchParam(page, searchParam, searchValue){
    return axios.get(`${EmployeesAdminUrl}?page=${page}&${searchParam}=${searchValue}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function updateEmployee(data){
    return axios.post(EmployeesAdminUrl, JSON.stringify(data), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// user
export function getUsers(page){
    return axios.get(`${UsersAdminUrl}?page=${page}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function getUsersBySearchParam(page, searchParam, searchValue){
    return axios.get(`${UsersAdminUrl}?page=${page}&${searchParam}=${searchValue}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function getUser(userID){
    return axios.get(`${UsersAdminUrl}/${userID}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function assignRoles(data){
    return axios.post(UsersRolesAdminUrl, JSON.stringify(data), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function resetPassword(userID){
    return axios.post(`${ResetPasswordAdminUrl(userID)}`, "",{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// discount
export function insertDiscount(discount){
    const data = JSON.stringify(discount);
    return axios.put(DiscountAdminUrl, data,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function updateDiscount(discount){
    const data = JSON.stringify(discount);
    return axios.post(DiscountAdminUrl, data,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}

export function getDiscountsByProduct(productID, pageNumber){
    return axios.get(`${DiscountsByProductAdminUrl}/${productID}?page=${pageNumber}`,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function getDiscountDetail(discountID){
    return axios.get(`${DiscountAdminUrl}/${discountID}`,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}

export function getDiscountTypes(pageNumber){
    return axios.get(`${DiscountTypeAdminUrl}?page=${pageNumber}`,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// roles
export function getRolesEmployee(pageNumber){
    return axios.get(`${RolesEmployeeAdminUrl}?page=${pageNumber}`,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
import axios from "axios";
import { 
    AccountAdminCustomer, AccountAdminEmployee, AccountAdminUserCustomer, 
    AccountAdminUserEmployee, AdminResetUserPassword, CategoryAdminUrl, 
    CustomersAdminUrl, DiscountAdminUrl, DiscountTypeAdminUrl, 
    DiscountsByProductAdminUrl, EmployeesAdminUrl, IsAdminUrl, 
    ProductAdminUrl, ProductSizeAdminUrl, ProductSizesAdminUrl, 
    GetRevenuePerMonth,
    GetTopSellingProductByYear,
    GetPaymentStatusPercentageByYear,
    GetPaymentYears,
    RegisterEmployeeUrl
} from "../constant/FoodShoppingApiURL";

export function isAdmin(){
    return axios.get(IsAdminUrl,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// category
export function updateCategory(category){
    return axios.post(CategoryAdminUrl, JSON.stringify(category), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function addCategory(category){
    return axios.put(CategoryAdminUrl, JSON.stringify(category), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}

// product
export function addProduct(productDetail){
    const data = JSON.stringify(productDetail);
    return axios.put(ProductAdminUrl, data,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function updateProduct(productDetail){
    const data = JSON.stringify(productDetail);
    return axios.post(ProductAdminUrl, data,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// update product size
export function updateProductSize(data){
    return axios.post(ProductSizeAdminUrl, JSON.stringify(data), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// update product sizes
export function updateProductSizes(data){
    return axios.post(ProductSizesAdminUrl, JSON.stringify(data), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// get products
export function getProducts(page){
    return axios.get(`${ProductAdminUrl}?page=${page}`, {
        withCredentials: true,
        headers: {
            "Content-Type": "application/json"
        }
    })
} 
// get product details
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
    return axios.get(`${AccountAdminCustomer}?page=${page}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function getCustomersBySearchParam(page, searchParam, searchValue){
    return axios.get(`${CustomersAdminUrl}?page=${page}&searchType=${searchParam}&searchValue=${searchValue}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function getCustomer(customerID){
    return axios.get(`${AccountAdminUserCustomer}/${customerID}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function addNewCustomer(data){
    return axios.put(CustomersAdminUrl, JSON.stringify(data), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function resetUserPassword(userId){
    return axios.post(`${AdminResetUserPassword}/${userId}`, "", {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}

export function updateCustomer(data){
    return axios.post(AccountAdminCustomer, JSON.stringify(data), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// employee
export function getEmployees(page){
    return axios.get(`${AccountAdminEmployee}?page=${page}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function getEmployee(employeeID){
    return axios.get(`${AccountAdminUserEmployee}/${employeeID}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function getEmployeesBySearchParam(page, searchParam, searchValue){
    return axios.get(`${EmployeesAdminUrl}?page=${page}&searchType=${searchParam}&searchValue=${searchValue}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
export function updateEmployee(data){
    return axios.post(AccountAdminEmployee, JSON.stringify(data), {
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

// get revenue
export function getRevenuePerMonthByYear(year){
    return axios.get(`${GetRevenuePerMonth}?year=${year}`,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}

// get selling products
export function getTopSellingProductByYear(year){
    return axios.get(`${GetTopSellingProductByYear}?year=${year}`,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// get payment status percentage
export function getPaymentStatusPercentageByYear(year){
    return axios.get(`${GetPaymentStatusPercentageByYear}?year=${year}`,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}

// get payment years
export function getPaymentYears(){
    return axios.get(`${GetPaymentYears}`,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}

// register employee
export function registerEmployee(data){
    return axios.post(`${RegisterEmployeeUrl}`, JSON.stringify(data),{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
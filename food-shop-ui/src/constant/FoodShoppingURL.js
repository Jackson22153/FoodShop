import { convertNameForUrl } from "../service/convert";

// home path
export const homePath = "";

export const foodsPath = `${homePath}/foods`;
export const categoriesPath = `${homePath}/categories`;
export const cartPath = `${homePath}/cart`;
export const orderPath = `${homePath}/order`;
export const contactPath = `${homePath}/contact`;
export const userPath = `${homePath}/user`;
export const customerPath = `${userPath}/customer`;
export const employeePath = `${userPath}/employee`;
export const adminPath = `${userPath}/admin`;

export const foodsAdminPath = `${adminPath}/foods`
// customerPath
export const customerInfo = `${customerPath}/info`
export const customerOrder = `${customerPath}/order`
export const customerNotification = `${customerPath}/notification`
// employeePath
export const employeeInfo = `${employeePath}/info`
export const employeeOrder = `${employeePath}/order`
export const employeePendingOrder = `${employeeOrder}/pending`
export const employeeConfirmedOrder = `${employeeOrder}/confirmed`
export const employeeNotification = `${employeePath}/notification`
// adminPath
export const adminCategories = `${adminPath}/categories`;
export const adminProducts = `${adminPath}/products`;
export const adminUsers = `${adminPath}/users`;
export const adminEmployee = `${adminPath}/employee`;
export const adminCustomer = `${adminPath}/customer`;
// product
export function SearchFoodsPath(search){
    return `${foodsPath}?s=${search}`;
}
export function FoodPath(foodName, foodID){
    const name = convertNameForUrl(foodName);
    return `${foodsPath}/${name}?sp=${foodID}`;
}


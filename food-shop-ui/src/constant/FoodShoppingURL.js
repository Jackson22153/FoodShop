import { convertNameForUrl } from "../service/Convert";

// home path
export const HOME_PATH = "";

export const FOODS_PATH = `${HOME_PATH}/foods`;
export const CATEGORIES_PATH = `${HOME_PATH}/categories`;
export const CART_PATH = `${HOME_PATH}/cart`;
export const ORDER_PATH = `${HOME_PATH}/order`;
export const CONTACT_PATH = `${HOME_PATH}/contact`;
// user path
const USER_PATH = `${HOME_PATH}/user`;
export const CUSTOMER_PATH = `${USER_PATH}/customer`;
export const EMPLOYEE_PATH = `${USER_PATH}/employee`;
export const ADMIN_PATH = `${USER_PATH}/admin`;

export const FOODS_ADMIN_PATH = `${ADMIN_PATH}/foods`
// customerPath
export const CUSTOMER_INFO = `${CUSTOMER_PATH}/info`
export const CUSTOMER_ORDER = `${CUSTOMER_PATH}/order`
export const CUSTOMER_NOTIFICATION = `${CUSTOMER_PATH}/notification`
// employeePath
export const EMPLOYEE_INFO = `${EMPLOYEE_PATH}/info`
export const EMPLOYEE_ORDER = `${EMPLOYEE_PATH}/order`
export const EMPLOYEE_PENDING_ORDER = `${EMPLOYEE_ORDER}/pending`
export const EPMLOYEE_CONFIRMED_ORDER = `${EMPLOYEE_ORDER}/confirmed`
export const EMPLOYEE_NOTIFICATION = `${EMPLOYEE_PATH}/notification`
// adminPath
export const ADMIN_CATEGORIES = `${ADMIN_PATH}/categories`;
export const ADMIN_ADD_CATEGORY = `${ADMIN_CATEGORIES}/addCategory`;
export const ADMIN_PRODUCTS = `${ADMIN_PATH}/products`;
export const ADMIN_ADD_PRODUCT = `${ADMIN_PRODUCTS}/addProduct`;
export const ADMIN_USERS = `${ADMIN_PATH}/users`;
export const ADMIN_ADD_USER = `${ADMIN_USERS}/adduser`;
export const ADMIN_EMPLOYEE = `${ADMIN_PATH}/employee`;
export const ADMIN_CUSTOMER = `${ADMIN_PATH}/customer`;
// product
export function SearchFoodsPath(search){
    return `${FOODS_PATH}?s=${search}`;
}
export function FoodPath(foodName, foodID){
    const name = convertNameForUrl(foodName);
    return `${FOODS_PATH}/${name}?sp=${foodID}`;
}

// error page
const ERROR_PAGE= `${HOME_PATH}/error`;
export const FORBIDDEN_ERROR_PAGE = `${ERROR_PAGE}/403`;
export const NOT_FOUND_ERROR_PAGE = `${ERROR_PAGE}/404`;
export const INTERNAL_ERROR_PAGE = `${ERROR_PAGE}/500`;


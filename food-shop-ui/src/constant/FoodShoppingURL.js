import { convertNameForUrl } from "../service/convertStr";

// home path
export const homePath = "";

export const foodsPath = `${homePath}/foods`;
export const categoriesPath = `${homePath}/categories`;
export const cartPath = `${homePath}/cart`;
export const contactPath = `${homePath}/contact`;
export const userPath = `${homePath}/user`;
export const customerPath = `${userPath}/customer`;
export const adminPath = `${userPath}/admin`;
// admin path
export const foodsAdminPath = `${adminPath}/foods`


// product
export function SearchFoodsPath(search){
    return `${foodsPath}?s=${search}`;
}
export function FoodPath(foodName, foodID){
    const name = convertNameForUrl(foodName);
    return `${foodsPath}/${name}?sp=${foodID}`;
}


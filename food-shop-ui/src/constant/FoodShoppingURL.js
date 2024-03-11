import { convertNameForUrl } from "../service/convertStr";

// home path
export const homePath = "";
// foods path
export const foodsPath = `/foods`;
export const categoriesPath = '/categories';
export const cartPath = '/cart';
export const contactPath = '/contact';
export const userPath = '/user';
export const customerPath = `${userPath}/customer`;
// product
export function SearchFoodsPath(search){
    return `${foodsPath}?s=${search}`;
}
export function FoodPath(foodName, foodID){
    const name = convertNameForUrl(foodName);
    return `${foodsPath}/${name}?sp=${foodID}`;
}


import axios from "axios";
import { CartProductsUrl, CartUrl } from "../constant/FoodShoppingApiURL";

export function addProductToCart(product){
    return axios.post(CartUrl, JSON.stringify(product), {
        withCredentials: true,
        headers: {
            'Content-Type': 'application/json',
        }
    });
}

export function deleteProductToCart(productID){
    return axios.delete(`${CartUrl}/${productID}`, {
        withCredentials: true,
        headers: {
            'Content-Type': 'application/json',
        }
    });
}

export function getNumberOfCartProducts(){
    return axios.get(`${CartUrl}/${productID}`, {
        withCredentials: true,
        headers: {
            'Content-Type': 'application/json',
        }
    });
}

export function getProductsFromCart(){
    return axios.get(CartProductsUrl, {
        withCredentials: true,
        headers: {
            'Content-Type': 'application/json',
        }
    });
}
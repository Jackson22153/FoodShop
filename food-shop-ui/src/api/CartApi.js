import axios from "axios";
import { CartOrderUrl, CartProductsUrl, CartUrl } from "../constant/FoodShoppingApiURL";

// add product to cart
export function addProductToCart(product){
    return axios.post(CartUrl, JSON.stringify(product), {
        withCredentials: true,
        headers: {
            'Content-Type': 'application/json',
        }
    });
}
// remove product from cart
export function deleteProductToCart(productID){
    return axios.delete(`${CartUrl}/${productID}`, {
        withCredentials: true,
        headers: {
            'Content-Type': 'application/json',
        }
    });
}
// get number of product in cart
export function getNumberOfCartProducts(){
    return axios.get(`${CartUrl}/${productID}`, {
        withCredentials: true,
        headers: {
            'Content-Type': 'application/json',
        }
    });
}
// get products from cart
export function getProductsFromCart(){
    return axios.get(CartProductsUrl, {
        withCredentials: true,
        headers: {
            'Content-Type': 'application/json',
        }
    });
}
// get order of cart for user to checkout
export function getOrder(){
    return axios.get(CartOrderUrl, {
        withCredentials: true,
        headers: {
            'Content-Type': 'application/json',
        }
    });
}
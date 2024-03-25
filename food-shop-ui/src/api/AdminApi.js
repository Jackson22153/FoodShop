import axios from "axios";
import { DiscountAdminUrl, ProductAdminUrl } from "../constant/FoodShoppingApiURL";

export function updateProduct(productDetail){
    const data = JSON.stringify(productDetail);
    return axios.post(ProductAdminUrl, data,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}

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
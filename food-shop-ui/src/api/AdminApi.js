import axios from "axios";
import { ProductAdminUrl } from "../constant/FoodShoppingApiURL";

export function updateProduct(productDetail){
    const data = JSON.stringify(productDetail);
    return axios.post(ProductAdminUrl, data,{
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
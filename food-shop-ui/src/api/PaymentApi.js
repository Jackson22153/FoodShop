import axios from "axios";
import { PaymentMethodsUrl } from "../constant/FoodShoppingApiURL";

// get payument methods
export function getPaymentMethods(){
    return axios.get(PaymentMethodsUrl, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
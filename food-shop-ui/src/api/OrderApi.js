import axios from "axios";
import { CancelOrderUrl, ConfirmOrderUrl, 
    CustomerOrdersUrl, FulfillOrderUrl, 
    PlaceOrderUrl, ReceiveOrderUrl } 
    from "../constant/FoodShoppingApiURL";

// place order
export function placeOrder(order){
    return axios.post(PlaceOrderUrl, JSON.stringify(order), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// receive order
export function receiveOrder(order){
    return axios.post(ReceiveOrderUrl, JSON.stringify(order), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// confirm order
export function confirmOrder(order){
    return axios.post(ConfirmOrderUrl, JSON.stringify(order), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// cancel order
export function cancelOrder(order){
    return axios.post(CancelOrderUrl, JSON.stringify(order), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}
// fulfill order
export function fulfillOrder(order){
    return axios.post(FulfillOrderUrl, JSON.stringify(order), {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    });
}

// customer
// get customer's invoice
export async function getCustomerInvoice(orderID){
    return axios.get(`${CustomerOrdersUrl}/${orderID}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
// get customer's orders
export async function getCustomerOrders(pageNumber, type){
    return axios.get(`${CustomerOrdersUrl}?page=${pageNumber}&type=${type}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
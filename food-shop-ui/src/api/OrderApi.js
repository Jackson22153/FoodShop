import axios from "axios";
import { CancelOrderUrl, ConfirmOrderUrl, 
    CustomerOrdersUrl, EmployeeOrderSummarysUrl, EmployeeOrdersUrl, FulfillOrderUrl, 
    PlaceOrderUrl, ReceiveOrderUrl } 
    from "../constant/FoodShoppingApiURL";


// employee
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
// get orders
export function getOrders(pageNumber, type){
    return axios.get(`${EmployeeOrdersUrl}?page=${pageNumber}&type=${type}`, {
        withCredentials: true,
        headers:{
            'Content-Type': 'application/json',
        }
    });
}
// get order detail
export function getOrderDetail(orderID, type){
    const url = type?`${EmployeeOrdersUrl}/${orderID}?type=${type}`:`${EmployeeOrdersUrl}/${orderID}`
    return axios.get(url, {
        withCredentials: true,
        headers:{
            'Content-Type': 'application/json',
        }
    });
}
// get order summary
export function getOrderSummary(){
    return axios.get(`${EmployeeOrderSummarysUrl}`, {
        withCredentials: true,
        headers:{
            'Content-Type': 'application/json',
        }
    });
}

// customer
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
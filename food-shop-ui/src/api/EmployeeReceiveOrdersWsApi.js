// import { OrderServiceWSUrl, OrderWsUrl, QUEUE_MESSAGES } from "../constant/FoodShoppingApiURL";
// import { Stomp } from "@stomp/stompjs";
// import SockJS from "sockjs-client";

// var stompClient = null;
// export const connectReceiveOrderEmployee = (getNewOrderCallback)=>{
//     stompClient = Stomp.over(()=> new SockJS(OrderServiceWSUrl));
//     stompClient.connect({},()=> onShopConnectEmployee(getNewOrderCallback), stompFailureCallback);
// }
// function onShopConnectEmployee(getNewOrderCallback) {
//     if(stompClient){
//         // listen to order topic
//         stompClient.subscribe(OrderWsUrl,(payload)=> onReceivePendingOrder(payload, getNewOrderCallback), {
//             'auto-delete': 'true'
//         });
//         // listen to itself
//         stompClient.subscribe(QUEUE_MESSAGES, onReceiveMessage, {
//             'auto-delete': 'true'
//         })
//         stompClient.reconnect_delay=1000
//     }
// }
// function onOrderConnectCustomer() {
//     if(stompClient){
//         console.log('Connected');
//         stompClient.subscribe(QUEUE_MESSAGES, onPrivateAccountOrderMessageReceived, {
//             'auto-delete': 'true'
//         });
//         stompClient.reconnect_delay=1000
//     }
// }
// // receive message to itself
// async function onReceiveMessage(payload) {
//     const message = JSON.parse(payload.body);
//     console.log(message)
// }
// //   receive new order from employee
// async function onReceivePendingOrder(payload, getNewOrderCallback) {
//     const message = JSON.parse(payload.body);
//     const newOrder = message;
//     // console.log(newOrder);
//     getNewOrderCallback(newOrder);
//     // if(newOrder){
//     //     fetchPendingOrders(page.number);
//     //     setPendingOrders([...pendingOrders, newOrder]);
//     // }
// }


// async function onPrivateAccountOrderMessageReceived(payload) {
//     // console.log('Message received', payload);
//     const message = JSON.parse(payload.body);
//     const statusMessage = message.status;
//     // if(statusMessage.toLowerCase() === ALERT_TYPE.SUCCESS.toLowerCase()){
//     //     setNotification({...message, isShowed: true});
//     // }else {
//     //     setNotification({...message, isShowed: true});
//     // }
//     console.log(message)
// }
// const stompFailureCallback = function (error) {
//     console.log('STOMP: ' + error);
// };

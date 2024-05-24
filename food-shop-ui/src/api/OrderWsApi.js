import { OrderServiceWSUrl, QUEUE_MESSAGES, PlaceOrderWsUrl } from "../constant/FoodShoppingApiURL";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

var stompClient = null;
export const connectCustomer = (getMessageCallback)=>{
    stompClient = Stomp.over(()=> new SockJS(OrderServiceWSUrl));
    stompClient.connect({},()=> onOrderConnectCustomer(getMessageCallback), stompFailureCallback);
}
function onOrderConnectCustomer(getMessageCallback) {
    if(stompClient){
        console.log('Connected');
        stompClient.subscribe(QUEUE_MESSAGES,(payload) => onPrivateAccountOrderMessageReceived(payload, getMessageCallback), {
            'auto-delete': 'true'
        });
        stompClient.reconnect_delay=1000
    }
}
async function onPrivateAccountOrderMessageReceived(payload, getMessageCallback) {
    // console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    getMessageCallback(message);
    // const statusMessage = message.status;
    // if(statusMessage.toLowerCase() === ALERT_TYPE.SUCCESS.toLowerCase()){
    //     setNotification({...message, isShowed: true});
    // }else {
    //     setNotification({...message, isShowed: true});
    // }
    console.log(message)
}
const stompFailureCallback = function (error) {
    console.log('STOMP: ' + error);
};

// send order message to employee
export const sendMessagePlaceOrder = (order)=>{
    if(stompClient && order){
        stompClient.send(PlaceOrderWsUrl, {}, JSON.stringify(order))
    }
}

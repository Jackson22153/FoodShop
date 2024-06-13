import { QUEUE_MESSAGES, NotificationServiceWsUrl, CustomerNotificationAccountWsUrl } from "../constant/FoodShoppingApiURL";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

var stompClient = null;
export const customerReceiveNotificationConnect = (getMessageCallback)=>{
    stompClient = Stomp.over(()=> new SockJS(NotificationServiceWsUrl));
    stompClient.connect({},()=> onConnectNotification(getMessageCallback), onConnectFailure);
}
function onConnectNotification(getMessageCallback) {
    if(stompClient){
        // receive account message
        stompClient.subscribe(CustomerNotificationAccountWsUrl,
            (payload) => onPrivateNotificationMessageReceived(payload, getMessageCallback), {
            'auto-delete': 'true'
        });
        // receive user's message 
        stompClient.subscribe(QUEUE_MESSAGES,(payload) => onPrivateNotificationMessageReceived(payload, getMessageCallback), {
            'auto-delete': 'true'
        });
        stompClient.reconnect_delay=500
    }
}
async function onPrivateNotificationMessageReceived(payload, getMessageCallback) {
    const message = JSON.parse(payload.body);
    getMessageCallback(message);
}
const onConnectFailure = function (error) {
    console.error('STOMP: ' + error);
};
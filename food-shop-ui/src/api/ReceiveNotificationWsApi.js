import { QUEUE_MESSAGES, NotificationServiceWsUrl } from "../constant/FoodShoppingApiURL";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

var stompClient = null;
export const notificationReceiveConnect = (getMessageCallback)=>{
    stompClient = Stomp.over(()=> new SockJS(NotificationServiceWsUrl));
    stompClient.connect({},()=> onConnectNotification(getMessageCallback), onConnectFailure);
}
function onConnectNotification(getMessageCallback) {
    if(stompClient){
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
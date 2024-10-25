import { NotificationServiceWsUrl, EmployeeNotificationOrderWsUrl, QUEUE_MESSAGES, EmployeeNotificationAccountWsUrl } from "../constant/FoodShoppingApiURL";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

var stompClient = null;
export const employeeReceiveNotificationConnect = (getMessageCallback)=>{
    stompClient = Stomp.over(()=> new SockJS(NotificationServiceWsUrl));
    stompClient.connect({},()=> onConnectNotification(getMessageCallback), onConnectFailure);
}
function onConnectNotification(getMessageCallback) {
    if(stompClient){
        // receive new order
        stompClient.subscribe(EmployeeNotificationOrderWsUrl,
            (payload) => onPrivateNotificationMessageReceived(payload, getMessageCallback), {
            'auto-delete': 'true'
        });
        // receive account message
        stompClient.subscribe(EmployeeNotificationAccountWsUrl,
            (payload) => onPrivateNotificationMessageReceived(payload, getMessageCallback), {
            'auto-delete': 'true'
        });
        // receive message
        stompClient.subscribe(QUEUE_MESSAGES,(payload) => onPrivateNotificationMessageReceived(payload, getMessageCallback), {
            'auto-delete': 'true'
        });
        stompClient.reconnect_delay=500
    }
}
async function onPrivateNotificationMessageReceived(payload, getMessageCallback) {
    const message = JSON.parse(payload.body);
    // console.log(message)
    getMessageCallback(message);
}
const onConnectFailure = function (error) {
    // console.error('STOMP: ' + error);
};
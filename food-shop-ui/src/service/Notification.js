import { CUSTOMER_INFO, CUSTOMER_ORDER, EMPLOYEE_INFO, EMPLOYEE_ORDER 
} from "../constant/FoodShoppingURL";
import { NOTIFICATION_TOPIC, ORDER_NOTIFICATION, ORDER_STATUS, ROLE } from "../constant/WebConstant";

// get url for any roles
export function getUrlFromNotification(notification, roles){
    if(roles.includes(ROLE.CUSTOMER.toLowerCase())){
        return getCustomerUrlFromNotification(notification);
    }else if(roles.includes(ROLE.EMPLOYEE.toLowerCase())){
        return getEmployeeUrlFromNotification(notification);
    }
    return '/';
}
// employee
export function getEmployeeUrlFromNotification(notification){
    if(notification.topic.toLowerCase()==NOTIFICATION_TOPIC.ORDER.toLowerCase()){
        var title = notification.title
        if(title!=null){
            title = title.toLowerCase();
            switch (title) {
                case ORDER_NOTIFICATION.PLACE_ORDER:
                    return `${EMPLOYEE_ORDER}?order=${ORDER_STATUS.PENDING}`
                case ORDER_NOTIFICATION.CONFIRM_ORDER: 
                    return `${EMPLOYEE_ORDER}?order=${ORDER_STATUS.CONFIRMED}`
                case ORDER_NOTIFICATION.FULFILL_ORDER: 
                    return `${EMPLOYEE_ORDER}?order=${ORDER_STATUS.SHIPPING}`
                case ORDER_NOTIFICATION.RECEIVE_ORDER: 
                    return `${EMPLOYEE_ORDER}?order=${ORDER_STATUS.SUCCESSFUL}`
                case ORDER_NOTIFICATION.CANCEL_ORDER: 
                    return `${EMPLOYEE_ORDER}?order=${ORDER_STATUS.CANCELED}`
            }
        }
        return EMPLOYEE_ORDER;
    }else if(notification.topic.toLowerCase()==NOTIFICATION_TOPIC.ACCOUNT.toLowerCase()){
        return EMPLOYEE_INFO;
    }
    return '/'
}
// customer
export function getCustomerUrlFromNotification(notification){
    if(notification.topic.toLowerCase()==NOTIFICATION_TOPIC.ORDER.toLowerCase()){
        var title = notification.title
        if(title!=null){
            title = title.toLowerCase();
            switch (title) {
                case ORDER_NOTIFICATION.PLACE_ORDER:
                    return `${CUSTOMER_ORDER}?order=${ORDER_STATUS.PENDING}`
                case ORDER_NOTIFICATION.CONFIRM_ORDER: 
                    return `${CUSTOMER_ORDER}?order=${ORDER_STATUS.CONFIRMED}`
                case ORDER_NOTIFICATION.FULFILL_ORDER: 
                    return `${CUSTOMER_ORDER}?order=${ORDER_STATUS.SHIPPING}`
                case ORDER_NOTIFICATION.RECEIVE_ORDER: 
                    return `${CUSTOMER_ORDER}?order=${ORDER_STATUS.SUCCESSFUL}`
                case ORDER_NOTIFICATION.CANCEL_ORDER: 
                    return `${CUSTOMER_ORDER}?order=${ORDER_STATUS.CANCELED}`
            }
        }
        return CUSTOMER_ORDER;
    }else if(notification.topic.toLowerCase()==NOTIFICATION_TOPIC.ACCOUNT.toLowerCase()){
        return CUSTOMER_INFO;
    }
    return '/'
}
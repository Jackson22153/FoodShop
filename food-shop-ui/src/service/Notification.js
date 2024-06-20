import { CUSTOMER_INFO, CUSTOMER_ORDER, EMPLOYEE_INFO, EMPLOYEE_ORDER 
} from "../constant/FoodShoppingURL";
import { NOTIFICATION_TOPIC, ROLE } from "../constant/WebConstant";

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
        return EMPLOYEE_ORDER;
    }else if(notification.topic.toLowerCase()==NOTIFICATION_TOPIC.ACCOUNT.toLowerCase()){
        return EMPLOYEE_INFO;
    }
    return '/'
}
// customer
export function getCustomerUrlFromNotification(notification){
    if(notification.topic.toLowerCase()==NOTIFICATION_TOPIC.ORDER.toLowerCase()){
        return CUSTOMER_ORDER;
    }else if(notification.topic.toLowerCase()==NOTIFICATION_TOPIC.ACCOUNT.toLowerCase()){
        return CUSTOMER_INFO;
    }
    return '/'
}